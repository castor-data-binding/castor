/*
 * Copyright 2005 Nissim Karpenstein, Stein M. Hugubakken
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.jdo.oql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.exolab.castor.jdo.DbMetaInfo;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.SQLHelper;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * A class which walks the parse tree created by the parser to check for errors
 * and translate to SQL.
 *
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class ParseTreeWalker {
    private LockEngine _dbEngine;

    private ParseTreeNode _parseTree;

    private String _projectionName;
    private String _projectionAlias;
    private int _projectionType;

    private String _fromClassName;
    private String _fromClassAlias;

    private ClassLoader _classLoader;
    private Class _objClass;
    private QueryExpression _queryExpr;
    private DbMetaInfo _dbInfo;

    private Hashtable _paramInfo;
    private HashMap _fieldInfo;
    private HashMap _pathInfo;
    private HashMap _allPaths;

    private SQLEngine _engine;
    private ClassDescriptor _clsDesc;

    //projection types
    public static final int AGGREGATE = 1;
    public static final int FUNCTION = 2;
    public static final int PARENT_OBJECT = 3;
    public static final int DEPENDANT_OBJECT = 4;
    public static final int DEPENDANT_OBJECT_VALUE = 5;
    public static final int DEPENDANT_VALUE = 6;

    public static final int MAX_TABLE_LENGTH = 30;

    /**
     * Creates a new parse tree walker.  Which checks the tree for errors, and
     * generates a QueryExpression containing the SQL translation.
     *
     * @param dbEngine The Persistence Engine
     * @param parseTree The parse tree to walk
     * @param classLoader A ClassLoader instance to load classes.
     * @throws QueryException Thrown by checkErrors.
     */
    public ParseTreeWalker(final LockEngine dbEngine, final ParseTreeNode parseTree,
                           final ClassLoader classLoader, final DbMetaInfo dbInfo)
    throws QueryException {
        _dbEngine = dbEngine;
        _parseTree = parseTree;
        _classLoader = classLoader;
        _dbInfo = dbInfo;

        if (!_parseTree.isRoot()) {
            throw new QueryException("ParseTreeWalker must be created with the "
                    + "root node of the parse tree.");
        }

        checkErrors();
        createQueryExpression();
    }

    /**
     * Accessor method for _objClass.
     *
     * @return The _objClass member.
     */
    public Class getObjClass() {
        return _objClass;
    }

    /**
     * Accessor method for _projectionType.
     *
     * @return The _projectionType member.
     */
    public int getProjectionType() {
        return _projectionType;
    }

    /**
     * Accessor method for private _queryExpr member.
     *
     * @return private _queryExpr member
     */
    public QueryExpression getQueryExpression() {
        return _queryExpr;
    }

    /**
     * Accessor method for _paramInfo.
     *
     * @return The _paramInfo member.
     */
    public Hashtable getParamInfo() {
        if (_paramInfo == null) { _paramInfo = new Hashtable(); }
        return _paramInfo;
    }

    private HashMap getFieldInfo() {
        if (_fieldInfo == null) { _fieldInfo = new HashMap(); }
        return _fieldInfo;
    }

    private HashMap getPathInfo() {
        if (_pathInfo == null) { _pathInfo = new HashMap(); }
        return _pathInfo;
    }

    /**
     * Accessor method for _clsDesc.
     *
     * @return The _clsDesc member.
     */
    public ClassDescriptor getClassDescriptor() {
        return _clsDesc;
    }

    /**
     * Method to get path info for the selected object.  This is the path which
     * will be used by the QueryResults to follow the path if the object
     * selected is a DEPENDANT_OBJECT or DEPENDANT_OBJECT_VALUE.  Any other
     * projectionTypes do not need this, so null will be returned.
     * 
     * @return Path info for the selected element, null otherwise. 
     */
    public Vector getProjectionInfo() {
        switch (_projectionType) {
        case DEPENDANT_OBJECT:
        case DEPENDANT_OBJECT_VALUE:
            ParseTreeNode projectionNode;

            int type = _parseTree.getChild(0).getToken().getTokenType();
            if (type == TokenType.KEYWORD_DISTINCT) {
                projectionNode = _parseTree.getChild(1);
            } else {
                projectionNode = _parseTree.getChild(0);
            }

            if (projectionNode.getToken().getTokenType() == TokenType.KEYWORD_AS) {
                projectionNode = projectionNode.getChild(0);
            }

            return (Vector) getPathInfo().get(projectionNode);
        default:
            return null;
        }
    }

    /**
     * Traverses the tree checking for errors.
     *
     * @throws QueryException if there is an error.
     */
    private void checkErrors() throws QueryException {
        for (Iterator iter = _parseTree.children(); iter.hasNext(); ) {
            ParseTreeNode curChild = (ParseTreeNode) iter.next();
            if (curChild.getToken().getTokenType() == TokenType.KEYWORD_FROM) {
                checkFromPart(curChild.getChild(0));
                break;
            }
        }

        int type = _parseTree.getChild(0).getToken().getTokenType();
        if (type == TokenType.KEYWORD_DISTINCT) {
            checkSelectPart(_parseTree.getChild(1));
        } else {
            checkSelectPart(_parseTree.getChild(0));
        }

        for (int curChild = 2; curChild <= _parseTree.getChildCount() - 1; curChild++) {
            int tokenType = _parseTree.getChild(curChild).getToken().getTokenType();
            switch (tokenType) {
            case TokenType.KEYWORD_WHERE:
                checkWhereClause(_parseTree.getChild(curChild));
                break;
            case TokenType.KEYWORD_ORDER:
                checkOrderClause(_parseTree.getChild(curChild));
                break;
            case TokenType.KEYWORD_LIMIT:
                checkLimitClause(_parseTree.getChild(curChild));
                break;
            case TokenType.KEYWORD_OFFSET:
                checkOffsetClause(_parseTree.getChild(curChild));
                break;
            default:
                break;
            }
        }
    }

    /**
     * Checks the from parts of the query.
     *
     * @param fromPart is the ParseTreeNode containing the from part of the
     *        queryTree.
     * @throws QueryException if there is an error.
     */
    private void checkFromPart(final ParseTreeNode fromPart) throws QueryException {
        //get the class name from the from clause
        if (fromPart.getToken().getTokenType() == TokenType.KEYWORD_AS) {
            ParseTreeNode classNameNode = fromPart.getChild(0);
            if (classNameNode.getToken().getTokenType() == TokenType.DOT) {
                StringBuffer sb = new StringBuffer();

                for (Iterator iter = classNameNode.children(); iter.hasNext(); ) {
                    ParseTreeNode theChild = (ParseTreeNode) iter.next();
                    sb.append(theChild.getToken().getTokenValue()).append(".");
                }

                sb.setLength(sb.length() - 1);
                _fromClassName = sb.toString();
            } else {
                _fromClassName = classNameNode.getToken().getTokenValue();
            }

            _fromClassAlias = fromPart.getChild(1).getToken().getTokenValue();
        } else {
            if (fromPart.getToken().getTokenType() == TokenType.DOT) {
                StringBuffer sb = new StringBuffer();

                for (Iterator iter = fromPart.children(); iter.hasNext(); ) {
                    ParseTreeNode theChild = (ParseTreeNode) iter.next();
                    sb.append(theChild.getToken().getTokenValue()).append(".");
                }

                _fromClassName = sb.deleteCharAt(sb.length() - 1).toString();
            } else {
                _fromClassName = fromPart.getToken().getTokenValue();
            }

            _fromClassAlias = _fromClassName;
        }

        try {
            if (_classLoader == null) {
                _objClass = Class.forName(_fromClassName);
            } else {
                _objClass = _classLoader.loadClass(_fromClassName);
            }
        } catch (ClassNotFoundException except) {
            throw new QueryException("Could not find class " + _fromClassName, except);
        }
        _engine = (SQLEngine) _dbEngine.getPersistence(_objClass);
        if (_engine == null) {
            throw new QueryException(
                    "Could not find mapping for class " + _fromClassName);
        }
        _clsDesc = _engine.getDescriptor();
        // This should never happen
        if (_clsDesc == null) {
            throw new QueryException(
                    "Could not get a descriptor for class " + _fromClassName);
        }
    }

    /**
     * Checks the select part of the query.
     *
     * @param selectPart is the ParseTreeNode containing the select part of the
     *        queryTree.
     * @throws QueryException if there is an error.
     */
    private void checkSelectPart(final ParseTreeNode selectPart) throws QueryException {
        if (selectPart.getToken().getTokenType() ==  TokenType.KEYWORD_AS) {
            checkProjection(selectPart.getChild(0), true, false);
            _projectionAlias = selectPart.getChild(1).getToken().getTokenValue();
        } else {
            checkProjection(selectPart, true, false);
            _projectionAlias = "";
        }
    }

    /**
     * Search for the field in the given class descriptor and descriptors of the
     * super classes, return null if not found.
     * 
     * @param fieldName The field name.
     * @param clsDesc A JDO class descriptor.
     * @return JDOFieldDescriptor for the specified field, null if not found.
     */
    private JDOFieldDescriptor getFieldDesc(final String fieldName,
                                            final ClassDescriptor clsDesc) {

        ClassDescriptor classDescriptor = clsDesc;
        JDOFieldDescriptor fieldDescriptor;
        while (classDescriptor != null) {
            fieldDescriptor = new ClassDescriptorJDONature(classDescriptor).getField(fieldName);
            if (fieldDescriptor != null) {
                return fieldDescriptor;
            }
            classDescriptor = classDescriptor.getExtends();
        }
        return null;
    }

    /**
     * Search for the field in the given class descriptor and descriptors of the
     * super classes. Returns new Object[2] {fieldDesc, classDesc}, where classDesc is a
     * descriptor of the class where the field was found. If not found null will be
     * returned. Also adds inner joins to the QueryExpression.
     * 
     * @param path The path info vector to build the alias with
     * @param tableIndex Field index in the path info
     */
    private Object[] getFieldAndClassDesc(final String fieldName,
                                          final ClassDescriptor clsDesc,
                                          final QueryExpression expr,
                                          final Vector path, final int tableIndex) {
        
        JDOFieldDescriptor field = null;
        ClassDescriptor cd = clsDesc;
        JDOFieldDescriptor tempField = null;
        ClassDescriptor tempCd = clsDesc;
        Object[] retVal;

        while (tempCd != null) {
            tempField = new ClassDescriptorJDONature(tempCd).getField(fieldName);
            if (tempField != null) {
                field = tempField;
                cd = tempCd;
            }
            tempCd = tempCd.getExtends();
        }
     
        if (field == null) { return null; }

        // prepare the return value
        retVal = new Object[] {field, cd};
        if (cd != clsDesc) {
            // now add inner join for "extends"
            String tableAlias1 = new ClassDescriptorJDONature(clsDesc).getTableName();
            String tableAlias2 = new ClassDescriptorJDONature(cd).getTableName();
            if (tableIndex > 0) {
                tableAlias1 = buildTableAlias(tableAlias1, path, tableIndex);
                tableAlias2 = buildTableAlias(tableAlias2, path, tableIndex);
            }
            expr.addTable(new ClassDescriptorJDONature(cd).getTableName(), tableAlias2);
            String[] clsDescIdNames = SQLHelper.getIdentitySQLNames(clsDesc);
            String[] cdIdNames = SQLHelper.getIdentitySQLNames(cd);
            expr.addInnerJoin(
                    new ClassDescriptorJDONature(clsDesc).getTableName(), clsDescIdNames, tableAlias1,
                    new ClassDescriptorJDONature(cd).getTableName(), cdIdNames, tableAlias2);
        }
        return retVal;
    }

    /**
     * Checks a projection (one of the items selected). Determines the
     * _projectionType.
     *
     * @param projection The ParseTreeNode containing the projection.
     * @param topLevel pass true when calling on the top level of projection
     *        in the parse tree.  False when recursing
     * @param onlySimple pass true if you want to throw errors if the
     *        object passed as the ParseTreeNode is not a simple type (use this
     *        when recursing on the arguments passed to Aggregate and SQL functions).
     * @throws QueryException if there is an error.
     * @return a JDOFieldDescriptor representation of the field, if it represents a field.
     */
    private JDOFieldDescriptor checkProjection(final ParseTreeNode projection,
                                               final boolean topLevel,
                                               final boolean onlySimple)
    throws QueryException {
        JDOFieldDescriptor field = null;

        if (projection.getChildCount() == 0) {
            if (topLevel) {
                _projectionType = PARENT_OBJECT;
                _projectionName = projection.getToken().getTokenValue();
                if (!_projectionName.equals(_fromClassAlias)) {
                    throw new QueryException("Object name not the same in SELECT and "
                            + "FROM - select: " + _projectionName + ", from: "
                            + _fromClassAlias);
                }
            } else {
                if (onlySimple) {
                    throw new QueryException("Only primitive values are allowed to be "
                            + "passed as parameters to Aggregate and SQL functions.");
                }
                return null;
            }
        } else {
            int tokenType = projection.getToken().getTokenType();
            switch (tokenType) {
            case TokenType.IDENTIFIER:
                //a SQL function call -- check the arguments
                _projectionType = FUNCTION;
                for (Iterator iter = projection.getChild(0).children(); iter.hasNext(); ) {
                    checkProjection((ParseTreeNode) iter.next(), false, true);
                }
                break;
            case TokenType.DOT:
                //a path expression -- check if it is valid, and create a paramInfo
                Iterator iter = projection.children();
                ParseTreeNode curNode = null;
                String curName = null;
                StringBuffer projectionName = new StringBuffer();
                Vector projectionInfo = new Vector();

                //check that the first word before the dot is our class
                if (iter.hasNext()) {
                    curNode = (ParseTreeNode) iter.next();
                    curName = curNode.getToken().getTokenValue();
                    if ((!curName.equals(_projectionName))
                            && (!curName.equals(_projectionAlias))
                            && (!curName.equals(_fromClassName))
                            && (!curName.equals(_fromClassAlias))) {
                        
                        // reset the enumeration
                        iter = projection.children();
                        curName = _fromClassAlias;
                    }
                    projectionName.append(curName);
                    projectionInfo.addElement(curName);
                }

                //use the ClassDescriptor to check that the rest of the path is valid.
                ClassDescriptor curClassDesc = _clsDesc;
                JDOFieldDescriptor curField = null;
                int count = 0;
                String curToken;
                while (iter.hasNext()) {
                    // there may be nested attribute name
                    curField = null;
                    curName = null;
                    while ((curField == null) && iter.hasNext()) {
                        curNode = (ParseTreeNode) iter.next();
                        curToken = curNode.getToken().getTokenValue();
                        if (curName == null) {
                            curName = curToken;
                        } else {
                            curName = curName + "." + curToken;
                        }
                        curField = getFieldDesc(curName, curClassDesc);
                    }
                    if (curField == null) {
                        throw new QueryException("An unknown field was requested: "
                                + curName + " (" + curClassDesc + ")");
                    }
                    projectionName.append(".").append(curName);
                    projectionInfo.addElement(curName);
                    curClassDesc = curField.getClassDescriptor();
                    if ((curClassDesc == null) && iter.hasNext()) {
                        throw new QueryException("An non-reference field was requested: "
                                + curName + " (" + curClassDesc + ")");
                    }
                    count++;
                }
                field = curField;

                getPathInfo().put(projection, projectionInfo);
                getFieldInfo().put(projection, curField);

                Class theClass = curField.getFieldType();
                // is it actually a Java primitive, or String, or a subclass of Number
                boolean isSimple = Types.isSimpleType(theClass);

                if (topLevel) {
                    _projectionName = projectionName.toString();
                    if (!isSimple) {
                        _projectionType = DEPENDANT_OBJECT;
                    } else if (count > 1) {
                        _projectionType = DEPENDANT_OBJECT_VALUE;
                    } else if (field.getContainingClassDescriptor() != _clsDesc) {
                        _projectionType = DEPENDANT_OBJECT_VALUE;
                    } else {
                        _projectionType = DEPENDANT_VALUE;
                    }
                } else {
                    if (!isSimple && onlySimple) {
                        throw new QueryException("Only primitive values are allowed "
                                + "to be passed as parameters to Aggregate and SQL "
                                + "functions.");
                    }
                }
                break;
            case TokenType.KEYWORD_COUNT:
                //count a special case
                _projectionType = AGGREGATE;
                int type = projection.getChild(0).getToken().getTokenType();
                if (type == TokenType.TIMES) {
                    //count(*)
                } else if (type == TokenType.KEYWORD_DISTINCT) {
                    checkProjection(projection.getChild(1), false, false);
                } else {
                    //can call count on object types -- recurse over child
                    checkProjection(projection.getChild(0), false, false);
                }
                break;

            case TokenType.KEYWORD_SUM:
            case TokenType.KEYWORD_MIN:
            case TokenType.KEYWORD_MAX:
            case TokenType.KEYWORD_AVG:
                //an aggregate -- recurse over the child
                _projectionType = AGGREGATE;
                checkProjection(projection.getChild(0), false, true);
                break;

            default:
                //we use function to describe sql expressions also, like
                // SELECT s.age - 5 FROM Student s
                _projectionType = FUNCTION;
                for (iter = projection.children(); iter.hasNext(); ) {
                    checkProjection((ParseTreeNode) iter.next(), false, false);
                }
            }
        }
        return field;
    }

    /**
     * Traverses the where clause sub-tree and checks for errors.  Creates a
     * Hashtables with FieldInfo for fields of selected objects which are
     * mentioned in the where clause (i.e. nodes with tokenType of IDENTIFIER
     * or (DOT, IDENTIFIER, IDENTIFIER)).  Als creates a Hashtable of paramInfo
     * with type information for query parameters (i.e. $(Class)1 or $1).
     *
     * @param whereClause WHERE-clause to traverse.
     * @throws QueryException if an error is detected.
     */
    private void checkWhereClause(final ParseTreeNode whereClause)
    throws QueryException {
        Iterator iter;
        int tokenType = whereClause.getToken().getTokenType();

        switch (tokenType) {
        case TokenType.DOT:
            checkProjection(whereClause, false, false);
            break;
        case TokenType.IDENTIFIER:
            iter = whereClause.children();
            if (iter.hasNext()) {
                int type = whereClause.getChild(0).getToken().getTokenType();
                if (type == TokenType.LPAREN) {
                    // A function.
                    while (iter.hasNext()) {
                        checkWhereClause((ParseTreeNode) iter.next());
                    }
                }
            } else {
                checkField(whereClause);
            }
            break;
        case TokenType.DOLLAR:
            checkParameter(whereClause);
            break;
        case TokenType.KEYWORD_IN:
            checkField(whereClause.getChild(0));
            checkInClauseRightSide(whereClause.getChild(1));
        default:
            for (iter = whereClause.children(); iter.hasNext(); ) {
                checkWhereClause((ParseTreeNode) iter.next());
            }
        }
    }

    /**
     * Traverses the limit clause sub-tree and checks for errors. Creates
     * a Hashtable of paramInfo with type information for query parameters
     * (i.e. $1).
     * 
     * @param limitClause LIMIT-clause to traverse.
     * @throws QueryException if an error is detected.
     */
    private void checkLimitClause(final ParseTreeNode limitClause)
    throws QueryException {
        int tokenType = limitClause.getToken().getTokenType();
        switch (tokenType) {
        case TokenType.DOLLAR:
            checkParameter(limitClause);
            break;
        default:
            for (Iterator iter = limitClause.children(); iter.hasNext(); ) {
              checkLimitClause((ParseTreeNode) iter.next());
            }
        }
    }

    /**
     * Traverses the offset clause sub-tree and checks for errors. Creates
     * a Hashtable of paramInfo with type information for query parameters
     * (i.e. $1).
     * 
     * @param offsetClause OFFSET-clause to traverse.
     * @throws QueryException if an error is detected.
     */
    private void checkOffsetClause(final ParseTreeNode offsetClause)
    throws QueryException {
        int tokenType = offsetClause.getToken().getTokenType();
        switch (tokenType) {
        case TokenType.DOLLAR:
            checkParameter(offsetClause);
            break;
        default:
            for (Iterator iter = offsetClause.children(); iter.hasNext(); ) {
              checkLimitClause((ParseTreeNode) iter.next());
            }
        }
    }

    /**
     * Checks whether the field passed in is valid within this object.  Also
     * adds this field to a Hashtable.
     *
     * @param fieldTree A leaf node containing an identifier, or a tree with DOT
     *        root, and two IDENTIFIER children (for fields that look like
     *        Person.name or Person-&gt;name)
     * @return a JDOFieldDescriptor representation of this field.
     * @throws QueryException if the field does not exist.
     */
    private JDOFieldDescriptor checkField(final ParseTreeNode fieldTree)
    throws QueryException {
        //see if we've checked this field before.
        JDOFieldDescriptor field = (JDOFieldDescriptor) getFieldInfo().get(fieldTree);
        if (field != null) { return field; }

        if (fieldTree.getToken().getTokenType() == TokenType.DOT) {
            field = checkProjection(fieldTree, false, false);
        } else {
            field = getFieldDesc(fieldTree.getToken().getTokenValue(), _clsDesc);
            if (field != null) {
                getFieldInfo().put(fieldTree, field);
            }
        }
        if (field == null) {
            throw new QueryException("The field " + fieldTree.getToken().getTokenValue()
                    + " was not found.");
        }
        return field;
    }
  
    /**
     * Checks a numbered parameter from an OQL Parse Tree.  Creates a {@link ParamInfo}
     * object which stores the user or system defined class for this parameter.
     * If there's a user defined type for this parameter it is compared to see if
     * it is castable from the system defined class.  If not, an exception is
     * thrown.  If a user defined type is specified for a numbered parameter which
     * has already been examined, and the user defined types don't match an
     * exception is thrown.
     *
     * @param paramTree the Tree node containing DOLLAR, with children user
     *        defined class (if available) and parameter number.
     * @throws QueryException if an invalid class is specified by the user.
     */
    private void checkParameter(final ParseTreeNode paramTree) throws QueryException {
        //get the parameter number and user defined type
        Integer paramNumber;
        String userDefinedType = "";
        if (paramTree.getChildCount() == 1) {
            paramNumber = Integer.decode(
                    paramTree.getChild(0).getToken().getTokenValue());
        } else {
            paramNumber = Integer.decode(
                    paramTree.getChild(1).getToken().getTokenValue());
            userDefinedType = paramTree.getChild(0).getToken().getTokenValue();
        }

        //Get the system defined type
        String systemType = "";
        // Get the SQL type if known
        JDOFieldDescriptor desc = null;
        int operation = paramTree.getParent().getToken().getTokenType();
        switch (operation) {
        case TokenType.PLUS: case TokenType.MINUS: case TokenType.TIMES:
        case TokenType.DIVIDE: case TokenType.KEYWORD_MOD: case TokenType.KEYWORD_ABS:
        case TokenType.KEYWORD_LIMIT: //Alex
            systemType = "java.lang.Number";
            break;
        case TokenType.KEYWORD_OFFSET:
            systemType = "java.lang.Number";
            break;
        case TokenType.KEYWORD_LIKE:  case TokenType.CONCAT:
            systemType = "java.lang.String";
            break;
        case TokenType.KEYWORD_AND: case TokenType.KEYWORD_OR: case TokenType.KEYWORD_NOT:
            systemType = "java.lang.Boolean";
            break;
        case TokenType.EQUAL: case TokenType.NOT_EQUAL: case TokenType.GT:
        case TokenType.GTE: case TokenType.LT: case TokenType.LTE:
        case TokenType.KEYWORD_BETWEEN:
            systemType = getParamTypeForComparison(paramTree.getParent());
            desc = getJDOFieldDescriptor(paramTree.getParent());
            break;
        case TokenType.KEYWORD_LIST:
            systemType = getParamTypeForList(paramTree.getParent());
            break;
        default:
            break;
        }

        //get the param info for this numbered param
        ParamInfo paramInfo = (ParamInfo) getParamInfo().get(paramNumber);
        if (paramInfo == null) {
            paramInfo = new ParamInfo(userDefinedType, systemType, desc, _classLoader);
            getParamInfo().put(paramNumber, paramInfo);
        } else {
            paramInfo.check(userDefinedType, systemType);
        }
    }

    private String getParamTypeForComparison(final ParseTreeNode comparisonTree)
    throws QueryException {
        for (Iterator iter = comparisonTree.children(); iter.hasNext(); ) {
            ParseTreeNode curChild = (ParseTreeNode) iter.next();
            int tokenType = curChild.getToken().getTokenType();

            switch(tokenType) {
            case TokenType.STRING_LITERAL:    return "java.lang.String";
            case TokenType.DOUBLE_LITERAL:    return "java.lang.Double";
            case TokenType.LONG_LITERAL:      return "java.lang.Long";
            case TokenType.BOOLEAN_LITERAL:   return "java.lang.Boolean";
            case TokenType.CHAR_LITERAL:      return "java.lang.Character";
            case TokenType.DATE_LITERAL:      return "java.util.Date";
            case TokenType.TIME_LITERAL:
            case TokenType.TIMESTAMP_LITERAL: return "java.util.Time";
            
            case TokenType.DOT:
            case TokenType.IDENTIFIER:
                JDOFieldDescriptor field = checkField(curChild);
                return field.getFieldType().getName();
            default:
                break;
            }
        }

        throw new QueryException("Could not get type for comparison.");
    }

    /**
     * Determine type of the list expression from the first contained
     * constant. If there are only bind variables without user defined
     * type present in the list, default to String as type.
     *  
     * @param listTree non-empty parser tree of list expression
     * @return class name of the list type
     * @throws QueryException
     */
    private String getParamTypeForList(final ParseTreeNode listTree)
    throws QueryException {
        for (Iterator iter = listTree.children(); iter.hasNext(); ) {
            ParseTreeNode curChild = (ParseTreeNode) iter.next();
            int tokenType = curChild.getToken().getTokenType();

            switch (tokenType) {
            case TokenType.STRING_LITERAL:    return "java.lang.String";
            case TokenType.DOUBLE_LITERAL:    return "java.lang.Double";
            case TokenType.LONG_LITERAL:      return "java.lang.Long";
            case TokenType.BOOLEAN_LITERAL:   return "java.lang.Boolean";
            case TokenType.CHAR_LITERAL:      return "java.lang.Character";
            case TokenType.DATE_LITERAL:      return "java.util.Date";
            case TokenType.TIME_LITERAL:
            case TokenType.TIMESTAMP_LITERAL: return "java.util.Time";

            case TokenType.DOLLAR:    // look for bind parameters with user defined type
                if (curChild.getChildCount() == 2) {
                    String udt = curChild.getChild(0).getToken().getTokenValue();
                    try {
                        return Types.typeFromName(_classLoader, udt).getName();
                    } catch (ClassNotFoundException e1) {
                        throw new QueryException("Could not find class " + udt);
                    }
                }
            default:
                break;
            }
        }

        // default to String, if only simple bind variables are present in the list 
        return "java.lang.String";
    }

    private JDOFieldDescriptor getJDOFieldDescriptor(final ParseTreeNode comparisonTree)
    throws QueryException {
        for (Iterator iter = comparisonTree.children(); iter.hasNext(); ) {
            ParseTreeNode curChild = (ParseTreeNode) iter.next();
            int tokenType = curChild.getToken().getTokenType();
            if ((tokenType == TokenType.DOT) || (tokenType == TokenType.IDENTIFIER)) {
                return checkField(curChild);
            }
        }
        return null;
    }
  
    /**
     * Checks the right side of an IN clause.  it must be a LIST, and
     * the children must all be literals.
     *
     * @param theList the ParseTreeNode containing the list which is the
     *        right side argument to IN.
     * @throws QueryException if theList is not a list, or the list
     *         contains non literals.
     */
    private void checkInClauseRightSide(final ParseTreeNode theList)
    throws QueryException {
        if (theList.getToken().getTokenType() != TokenType.KEYWORD_LIST) {
            throw new QueryException("The right side of the IN operator must be a LIST.");
        }

        for (Iterator iter = theList.children(); iter.hasNext(); ) {
            ParseTreeNode node = (ParseTreeNode) iter.next();
            int tokenType = node.getToken().getTokenType();

            switch (tokenType) {
            case TokenType.KEYWORD_NIL: case TokenType.KEYWORD_UNDEFINED:
            case TokenType.BOOLEAN_LITERAL: case TokenType.LONG_LITERAL:
            case TokenType.DOUBLE_LITERAL: case TokenType.CHAR_LITERAL:
            case TokenType.STRING_LITERAL: case TokenType.DATE_LITERAL:
            case TokenType.TIME_LITERAL: case TokenType.TIMESTAMP_LITERAL:
                break;
            case TokenType.DOLLAR:    // bind variable parameter
                checkParameter(node);
                break;
            default:
                throw new QueryException("The LIST can only contain literals, bind "
                        + "variables and the keywords 'nil' and 'undefined'.");
            }
        }
    }

    /**
     * Traverses the order by clause sub-tree and checks for errors.
     *
     * @param orderClause ORDER-BY clause to traverse.
     * @throws QueryException if an error is detected.
     */
    private void checkOrderClause(final ParseTreeNode orderClause)
    throws QueryException {
        if (orderClause.getToken().getTokenType() != TokenType.KEYWORD_ORDER) {
            throw new QueryException("checkOrderClause was called on a subtree which "
                    + "is not an order clause.");
        }

        ParseTreeNode prevChild = null;
        for (Iterator iter = orderClause.children(); iter.hasNext(); ) {
            ParseTreeNode curChild = (ParseTreeNode) iter.next();

            int tokenType = curChild.getToken().getTokenType();
            switch (tokenType) {
            case TokenType.KEYWORD_ASC:
            case TokenType.KEYWORD_DESC:
                // iterate on child
                curChild = curChild.getChild(0);
                tokenType = curChild.getToken().getTokenType();
                break;
            default:
                break;
            }
            
            switch (tokenType) {
            case TokenType.DOT:
                checkProjection(curChild, false, false);
                break;
            case TokenType.IDENTIFIER:
                if (curChild.children().hasNext()) {
                    int type = curChild.getChild(0).getToken().getTokenType();
                    if (type == TokenType.LPAREN) {
                        // A function, skip to next element
                        Iterator arguments = curChild.getChild(0).children();
                        while (arguments.hasNext()) {
                            ParseTreeNode nn = (ParseTreeNode) arguments.next();
                            checkWhereClause(nn);
                        }
                    }
                } else {
                    checkField(curChild);
                }
                break;
            case TokenType.LPAREN:
                int type = prevChild.getToken().getTokenType();
                if ((prevChild == null) || (type != TokenType.IDENTIFIER)) {
                    throw new QueryException("Illegal use of left parenthesis in "
                            + "ORDER BY clause.");
                }
                break;
            default:
                throw new QueryException("Only identifiers, path expressions, and the "
                        + "keywords ASC and DESC are allowed in the ORDER BY clause.");
            }
            prevChild = curChild;
        }
    }

    /**
     * Generates the QueryExpression which is an SQL representation or the OQL
     * parse tree.
     * 
     * @throws SyntaxNotSupportedException If a specific OQL feature is not supported
     *         by a RDBMS.
     */
    private void createQueryExpression() throws SyntaxNotSupportedException {
        //We use the SQLEngine buildfinder for any queries which require
        //us to load the entire object from the database.  Otherwise
        //we use the local addSelectFromJoins method.

        switch (_projectionType) {
        case PARENT_OBJECT:
        case DEPENDANT_OBJECT:
        case DEPENDANT_OBJECT_VALUE:
            _queryExpr = _engine.getFinder();
            break;
        default:
            _queryExpr = _engine.getQueryExpression();
            addSelectFromJoins();
        }

        _queryExpr.setDbMetaInfo(_dbInfo);

        //check for DISTINCT
        int type = _parseTree.getChild(0).getToken().getTokenType(); 
        if (type == TokenType.KEYWORD_DISTINCT) {
            _queryExpr.setDistinct(true);
        }

        //process where clause and order clause
        for (Iterator iter = _parseTree.children(); iter.hasNext(); ) {
            ParseTreeNode curChild = (ParseTreeNode) iter.next();
            int tokenType = curChild.getToken().getTokenType();
            switch (tokenType) {
            case TokenType.KEYWORD_WHERE:
                addWhereClause(curChild);
                break;
            case TokenType.KEYWORD_ORDER:
                _queryExpr.addOrderClause(getOrderClause(curChild));
                break;
            case TokenType.KEYWORD_LIMIT:
                addLimitClause(curChild);
                break;
            case TokenType.KEYWORD_OFFSET:
                addOffsetClause(curChild);
                break;
            default:
                break;
            }
        }
    }

    /**
     * Adds the selected fields, and the necessary joins to the QueryExpression.
     * This method is used when the query is for dependant values, aggregates,
     * or SQL functions, where we're just gonna query the item directly.
     */
    private void addSelectFromJoins() {
        ParseTreeNode selectPart = null;
        int type = _parseTree.getChild(0).getToken().getTokenType();
        if (type == TokenType.KEYWORD_DISTINCT) {
            selectPart = _parseTree.getChild(1);
        } else {
            selectPart = _parseTree.getChild(0);
        }

        _queryExpr.addTable(new ClassDescriptorJDONature(_clsDesc).getTableName());

        // add table names and joins for all base classes
        ClassDescriptor oldDesc = _clsDesc;
        ClassDescriptor tempDesc = _clsDesc.getExtends();
        while (tempDesc != null) {
            String tableName = new ClassDescriptorJDONature(tempDesc).getTableName();
            _queryExpr.addTable(tableName);
            
            JDOFieldDescriptor leftField = (JDOFieldDescriptor) oldDesc.getIdentity();
            JDOFieldDescriptor rightField = (JDOFieldDescriptor) tempDesc.getIdentity();
            
            _queryExpr.addInnerJoin(
                    new ClassDescriptorJDONature(oldDesc).getTableName(), leftField.getSQLName(),
                    new ClassDescriptorJDONature(tempDesc).getTableName(), rightField.getSQLName());

            oldDesc = tempDesc;
            tempDesc = tempDesc.getExtends();
        }
        _queryExpr.addSelect(getSQLExpr(selectPart));
    }

    /**
     * Builds the alias name for a table from the path info.
     *
     * @param tableName The name of the table to add to the select clause
     * @param path The path info vector to build the alias with
     * @param tableIndex Field index in the path info
     * @return Alias name for a given table.
     */
    public String buildTableAlias(final String tableName, final Vector path, final int tableIndex) {
        /* FIXME This aliasing will cause problems if a (different) table
           with the same name as our alias already exists (or will be added
           at a later time) in the query. Catching this might become a bit
           tricky ... */

        String tableAlias = tableName;
        int index;
        Integer i;

        /* We don't just alias all tables here, but only those where some
           attribute is queried (not those where we query a whole object).
           The reasons for this are impossible for me to explain right now,
           as I am rather tired; I'll write some better explanation later -
           I promise! :-) */
        if ((path != null) && (path.size() > 2)) {
            if (_allPaths == null) { _allPaths = new HashMap(); }
            ArrayList tablePath = new ArrayList(path.subList(0, tableIndex + 1));
            i = (Integer) _allPaths.get(tablePath);
            if (i == null) {
                index = _allPaths.size();
                _allPaths.put(tablePath, new Integer(index));
            } else {
                index = i.intValue();
            }
            
            /* fix for oracle support. If we have a 30 character table name, adding
               anything to it (_#) will cause it to be too long and will make oracle
               barf. the length we are trying to evaluate is:
               length of the table name + the length of the index number we are using
               + the _ character. */
            String stringIndex = String.valueOf(index);
            if ((tableAlias.length() + stringIndex.length() + 1) > MAX_TABLE_LENGTH) {
                /* use a substring of the original table name, rather than the table alias
                   because if we truncate the table alias it could become "ununique"
                   (by truncating the _# we add) by truncating the table name
                   beforehand we are guaranteed uniqueness as long as the index is
                   not reused. */
                tableAlias = tableAlias.substring(
                        MAX_TABLE_LENGTH - (stringIndex.length() + 1));
            }
            /* If table name contains '.', it should be replaced, since such names aren't
               allowed for aliases */
            tableAlias = tableAlias.replace('.', '_') + "_" + index;
        }
        return tableAlias;
    }
  
    /**
     * Adds joins to the queryExpr for path expressions in the OQL.
     *
     * @param path Path expression to be added to JOIN clause.
     */
    private void addJoinsForPathExpression(final Vector path) {
        if (path == null) {
            throw new IllegalStateException("path = null !");
        }

        // the class for the join is even this class or one of the base classes
        ClassDescriptor sourceClass = _clsDesc;

        for (int i = 1; i < path.size() - 1; i++) {
            JDOFieldDescriptor fieldDesc = null;

            // Find the sourceclass and the fielsddescriptor in the class hierachie
            Object[] fieldAndClass = getFieldAndClassDesc(
                    (String) path.elementAt(i), sourceClass, _queryExpr, path, i - 1);
            if (fieldAndClass == null) {
                throw new IllegalStateException("Field not found:" + path.elementAt(i));
            }
            fieldDesc = (JDOFieldDescriptor) fieldAndClass[0];
            sourceClass = (ClassDescriptor) fieldAndClass[1];

            ClassDescriptor clsDesc = fieldDesc.getClassDescriptor();
            if (clsDesc != null) {
                //we must add this table as a join
                ClassDescriptorJDONature sourceClassJDONature = 
                    new ClassDescriptorJDONature(sourceClass);
                if (fieldDesc.getManyKey() == null) {
                    //a many -> one relationship
                    JDOFieldDescriptor foreignKey =
                        (JDOFieldDescriptor) clsDesc.getIdentity();
                    String sourceTableAlias = sourceClassJDONature.getTableName();
                    if (i > 1) {
                        sourceTableAlias = buildTableAlias(sourceTableAlias, path, i - 1);
                    }

                    _queryExpr.addInnerJoin(
                            sourceClassJDONature.getTableName(), fieldDesc.getSQLName(),
                            sourceTableAlias,
                            new ClassDescriptorJDONature(clsDesc).getTableName(), foreignKey.getSQLName(),
                            buildTableAlias(new ClassDescriptorJDONature(clsDesc).getTableName(), path, i));
                } else if (fieldDesc.getManyTable() == null) {
                    //a one -> many relationship
                    JDOFieldDescriptor identity =
                        (JDOFieldDescriptor) sourceClass.getIdentity();
                    String sourceTableAlias = sourceClassJDONature.getTableName();
                    if (i > 1) {
                        sourceTableAlias = buildTableAlias(sourceTableAlias, path, i - 1);
                    }

                    _queryExpr.addInnerJoin(
                            sourceClassJDONature.getTableName(), identity.getSQLName(),
                            sourceTableAlias,
                            new ClassDescriptorJDONature(clsDesc).getTableName(), fieldDesc.getManyKey(),
                            buildTableAlias(new ClassDescriptorJDONature(clsDesc).getTableName(), path, i));
                } else {
                    //a many -> many relationship
                    JDOFieldDescriptor identity =
                        (JDOFieldDescriptor) sourceClass.getIdentity();
                    JDOFieldDescriptor foreignKey =
                        (JDOFieldDescriptor) clsDesc.getIdentity();
                    String manyTableAlias = fieldDesc.getManyTable();
                    String sourceTableAlias = sourceClassJDONature.getTableName();
                    if (i > 1) {
                        manyTableAlias = buildTableAlias(manyTableAlias, path, i - 1);
                        sourceTableAlias = buildTableAlias(sourceTableAlias, path, i - 1);
                    }

                    _queryExpr.addInnerJoin(
                            sourceClassJDONature.getTableName(), identity.getSQLName(),
                            sourceTableAlias,
                            fieldDesc.getManyTable(), fieldDesc.getManyKey(),
                            manyTableAlias);

                    _queryExpr.addInnerJoin(
                            fieldDesc.getManyTable(), fieldDesc.getSQLName(),
                            manyTableAlias,
                            new ClassDescriptorJDONature(clsDesc).getTableName(), foreignKey.getSQLName(),
                            buildTableAlias(new ClassDescriptorJDONature(clsDesc).getTableName(), path, i));
                }
                sourceClass = clsDesc;
            }
        }
    }

    /**
     * Adds a SQL version of an OQL where clause.
     *
     * @param whereClause the parse tree node with the where clause
     */
    private void addWhereClause(final ParseTreeNode whereClause) {
        String sqlExpr = getSQLExpr(whereClause.getChild(0));
        _queryExpr.addWhereClause(sqlExpr);
    }

    /**
     * Adds a SQL version of an OQL limit clause.
     *
     * @param limitClause the parse tree node with the limit clause
     * @throws SyntaxNotSupportedException If the LIMIT clause is not supported by a
     *         RDBMS.
     */
    private void addLimitClause(final ParseTreeNode limitClause)
    throws SyntaxNotSupportedException {
        String sqlExpr = getSQLExpr(limitClause);
        _queryExpr.addLimitClause(sqlExpr);
    }

    /**
     * Adds a SQL version of an OQL offset clause.
     * 
     * @param offsetClause The parse tree node with the offset clause
     * @throws SyntaxNotSupportedException If the LIMIT clause is not supported by a
     *         RDBMS.
     */
    private void addOffsetClause(final ParseTreeNode offsetClause)
    throws SyntaxNotSupportedException {
        String sqlExpr = getSQLExpr(offsetClause);
        _queryExpr.addOffsetClause(sqlExpr);
    }
  
    /**
     * Returns a SQL version of an OQL expr.
     *
     * @param exprTree the parse tree node with the expr
     * @return The SQL translation of the expr.
     */
    private String getSQLExpr(final ParseTreeNode exprTree) {
        StringBuffer sb = null;
        int tokenType =  exprTree.getToken().getTokenType();

        switch (tokenType) {
        case TokenType.LPAREN:
            //Parens passed through from where clause in SQL
            return "( " + getSQLExpr(exprTree.getChild(0)) + " )";
        case TokenType.PLUS:
        case TokenType.MINUS:
        case TokenType.KEYWORD_ABS:
        case TokenType.KEYWORD_NOT:
            //(possible) unary operators
            if (exprTree.getChildCount() == 1) {
                return exprTree.getToken().getTokenValue() + " "
                       + getSQLExpr(exprTree.getChild(0));
            }
            //this was binary PLUS or MINUS
            return getSQLExpr(exprTree.getChild(0)) + " "
                   + exprTree.getToken().getTokenValue() + " "
                   + getSQLExpr(exprTree.getChild(1));
        case TokenType.KEYWORD_AND:
        case TokenType.KEYWORD_OR:
        case TokenType.EQUAL:
        case TokenType.NOT_EQUAL:
        case TokenType.CONCAT:
        case TokenType.GT:
        case TokenType.GTE:
        case TokenType.LT:
        case TokenType.LTE:
        case TokenType.TIMES:
        case TokenType.DIVIDE:
        case TokenType.KEYWORD_MOD:
        case TokenType.KEYWORD_LIKE:
        case TokenType.KEYWORD_IN:
            //binary operators
            return getSQLExpr(exprTree.getChild(0)) + " "
                   + exprTree.getToken().getTokenValue() + " "
                   + getSQLExpr(exprTree.getChild(1));
        case TokenType.KEYWORD_BETWEEN:
            //tertiary BETWEEN operator
            return getSQLExpr(exprTree.getChild(0)) + " "
                   + exprTree.getToken().getTokenValue() + " "
                   + getSQLExpr(exprTree.getChild(1)) + " AND "
                   + getSQLExpr(exprTree.getChild(2));
        case TokenType.KEYWORD_IS_DEFINED:
            //built in functions
            return getSQLExpr(exprTree.getChild(0)) + " IS NOT NULL ";
        case TokenType.KEYWORD_IS_UNDEFINED:
            return getSQLExpr(exprTree.getChild(0)) + " IS NULL ";
        case TokenType.KEYWORD_COUNT:
            int type = exprTree.getChild(0).getToken().getTokenType();
            if (type == TokenType.TIMES) {
                return " COUNT(*) ";
            } else if (type == TokenType.KEYWORD_DISTINCT) {
                return " COUNT(DISTINCT " + getSQLExpr(exprTree.getChild(1)) + ") ";
            } else {
                return " COUNT(" + getSQLExpr(exprTree.getChild(0)) + ") ";
            }
        case TokenType.KEYWORD_SUM:
        case TokenType.KEYWORD_MIN:
        case TokenType.KEYWORD_MAX:
        case TokenType.KEYWORD_AVG:
            return " " + exprTree.getToken().getTokenValue() + "("
                   + getSQLExpr(exprTree.getChild(0)) + ") ";
        case TokenType.KEYWORD_LIST:
            //List creation
            sb = new StringBuffer("( ");
            for (Iterator iter = exprTree.children(); iter.hasNext(); ) {
                sb.append(getSQLExpr((ParseTreeNode) iter.next())).append(" , ");
            }
            //replace final comma space with close paren.
            sb.replace(sb.length() - 2, sb.length() - 1, " )").append(" ");
            return sb.toString();

        case TokenType.IDENTIFIER:
        case TokenType.DOT:
            //fields or SQL functions
            boolean lparen = (exprTree.getChildCount() > 0);
            if (lparen) {
                int temp = exprTree.getChild(0).getToken().getTokenType();
                lparen = lparen && (temp == TokenType.LPAREN);
            }
            if (lparen) {
                //An SQL function
                sb = new StringBuffer(exprTree.getToken().getTokenValue()).append("(");
                int paramCount = 0;
                Iterator iter = exprTree.children();
                iter = ((ParseTreeNode) iter.next()).children(); // LPAREN's children
                for (; iter.hasNext(); ) {
                    sb.append(getSQLExpr((ParseTreeNode) iter.next())).append(" , ");
                    paramCount++;
                }

                if (paramCount > 0) {
                    //replace final comma space with close paren.
                    sb.replace(sb.length() - 2, sb.length() - 1, " )");
                } else {
                    //there were no parameters, so no commas.
                    sb.append(") ");
                }
                return sb.toString();
            }
            //a field
            Vector path = (Vector) getPathInfo().get(exprTree);
            if (tokenType == TokenType.DOT) {
              if (path == null) {
                System.err.println("exprTree=" + exprTree.toStringEx()
                                   + "\npathInfo = {");
                Iterator iter = getPathInfo().keySet().iterator();
                ParseTreeNode n;
                while (iter.hasNext()) {
                  n = (ParseTreeNode) iter.next();
                  System.err.println("\t" + n.toStringEx());
                }
                // Exception follows in addJoinsForPathExpression()
              }
              addJoinsForPathExpression(path);
            }
            
            JDOFieldDescriptor field =
              (JDOFieldDescriptor) getFieldInfo().get(exprTree);
            if (field == null) {
                throw new IllegalStateException(
                        "fieldInfo for " + exprTree.toStringEx() + " not found");
            }
            
            ClassDescriptor clsDesc = field.getContainingClassDescriptor();
            if (clsDesc == null) {
                throw new IllegalStateException(
                        "ContainingClass of " + field.toString() + " is null !");
            }
            
            String clsTableAlias;
            ClassDescriptorJDONature classDescriptorJDONature = 
                new ClassDescriptorJDONature(clsDesc);
                if (tokenType == TokenType.DOT && path != null && path.size() > 2) {
              clsTableAlias = buildTableAlias(
                      classDescriptorJDONature.getTableName(), path, path.size() - 2);
              ClassDescriptor srcDesc = _clsDesc;
              for (int i = 1; i < path.size(); i++) {
                Object[] fieldAndClass = getFieldAndClassDesc(
                                                              (String) path.elementAt(i),
                                                              srcDesc, _queryExpr, path, i - 1);
                if (fieldAndClass == null) {
                  throw new IllegalStateException("Field not found: "
                                                  + path.elementAt(i) + " class "
                                                  + srcDesc.getJavaClass());
                }
                JDOFieldDescriptor fieldDesc =
                  (JDOFieldDescriptor) fieldAndClass[0];
                srcDesc = fieldDesc.getClassDescriptor();
              }
            } else {
              clsTableAlias = buildTableAlias(classDescriptorJDONature.getTableName(), path, 9999);
            }
            
            return _queryExpr.encodeColumn(clsTableAlias, field.getSQLName()[0]);
        case TokenType.DOLLAR:
            //parameters
            //return a question mark with the parameter number.
            //The calling function will do a mapping
            int child = exprTree.getChildCount() - 1;
            return "?" + exprTree.getChild(child).getToken().getTokenValue();
        case TokenType.BOOLEAN_LITERAL:
        case TokenType.LONG_LITERAL:
        case TokenType.DOUBLE_LITERAL:
        case TokenType.CHAR_LITERAL:
            //literals which need no modification
            return exprTree.getToken().getTokenValue();
        case TokenType.STRING_LITERAL:
            //String literals: change \" to ""
            //char replace function should really be somewhere else first change \" to "
            sb = new StringBuffer();
            String copy = exprTree.getToken().getTokenValue();

            int pos = copy.indexOf("\\\"", 1);
            while (pos != -1) {
                sb.append(copy.substring(0, pos)).append("\"\"");
                copy = copy.substring(pos + 2);
                pos = copy.indexOf("\\\"");
            }

            sb.append(copy);

            //Then change surrounding double quotes to single quotes, and change ' to ''

            copy = sb.deleteCharAt(0).toString();

            sb.setLength(0);
            sb.append("'");
            pos = copy.indexOf("'", 1);
            while (pos != -1) {
                sb.append(copy.substring(0, pos)).append("''");
                copy = copy.substring(pos + 1);
                pos = copy.indexOf("'");
            }

            sb.append(copy);
            //replace final double quote with single quote
            sb.replace(sb.length() - 1, sb.length(), "'");

            return sb.toString();

        case TokenType.DATE_LITERAL:
        case TokenType.TIME_LITERAL:
            //Date, time and timestamp literals...strip off keyword (?is that all?)
            //date and time both 4 chars long.
            return exprTree.getToken().getTokenValue().substring(5);
        case TokenType.TIMESTAMP_LITERAL:
            return exprTree.getToken().getTokenValue().substring(10);
        case TokenType.KEYWORD_NIL:
        case TokenType.KEYWORD_UNDEFINED:
            return " NULL ";
        case TokenType.KEYWORD_LIMIT:
        case TokenType.KEYWORD_OFFSET:
            return getSQLExprForLimit(exprTree);
        default:
            break;
        }

        return "";
    }
  
    private String getSQLExprForLimit(final ParseTreeNode limitClause) {
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = limitClause.children(); iter.hasNext(); ) {
            ParseTreeNode exprTree = (ParseTreeNode) iter.next();
            int tokenType =  exprTree.getToken().getTokenType();

            switch (tokenType) {
            case TokenType.DOLLAR:
                //parameters
                //return a question mark with the parameter number.
                //The calling function will do a mapping
                int count = exprTree.getChildCount() - 1;
                sb.append("?" + exprTree.getChild(count).getToken().getTokenValue());
                break;
            case TokenType.COMMA:
                sb.append(" , ");
                break;
            case TokenType.BOOLEAN_LITERAL:
            case TokenType.LONG_LITERAL:
            case TokenType.DOUBLE_LITERAL:
            case TokenType.CHAR_LITERAL:
                return exprTree.getToken().getTokenValue();
            default:
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Returns a SQL version of an OQL order by clause.
     *
     * @param orderClause the parse tree node with the order by clause
     * @return The SQL translation of the order by clause.
     */
    private String getOrderClause(final ParseTreeNode orderClause) {
        StringBuffer sb = new StringBuffer();

        for (Iterator iter = orderClause.children(); iter.hasNext(); ) {
            sb.append(", ");
            ParseTreeNode curChild = (ParseTreeNode) iter.next();
            int tokenType = curChild.getToken().getTokenType();
            
            switch (tokenType) {
            case TokenType.KEYWORD_ASC:
                sb.append(getSQLExpr(curChild.getChild(0))).append(" ASC ");
                break;
            case TokenType.KEYWORD_DESC:
                sb.append(getSQLExpr(curChild.getChild(0))).append(" DESC ");
                break;
            case TokenType.DOT:
            case TokenType.IDENTIFIER:
                sb.append(getSQLExpr(curChild)).append(" ");
                break;
            default:
                break;
            }
        }

        //remove the additional comma space at the beginning
        sb.deleteCharAt(0).deleteCharAt(0);

        return sb.toString();
    }
}
