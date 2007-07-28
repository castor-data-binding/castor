/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.dtx;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.mapping.xml.BindXml;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.Sql;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;
import org.xml.sax.DocumentHandler;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * A single query that takes OQL query language, reads data from an
 * RDBMS, and returns the results as SAX events. The query can be
 * parameterized, and be re-used with different bound values.
 *
 * There are currently some severe limitations on the contents of the
 * OQL query. The query needs to be a SELECT and must return a single
 * class.  However, it can handle multiple returned objects, which are
 * sent to the DocumentHandler as multiple documents.
 *
 * DTXQuery's can't be created on their own; use the DTXEngine to
 * prepare a new query.
 *
 * An additional note: the bind() methods take the parameters in order
 * that they appear, NOT using the number value of the parameter
 * name. So, if you have a query like:
 *
 * <pre>... WHERE foo.id = $2 AND bar.name = $1 ... </pre>
 *
 * then the statement bind(1, 12) will bind the value 12 to the first
 * parameter, even though it's named "$2." This may or may not be
 * changed in future releases.
 *
 * @author <a href="0@intalio.com">Evan Prodromou</a>
 * @version $Revision$ $Date: 2005-12-06 14:55:28 -0700 (Tue, 06 Dec 2005) $
 */

public class DTXQuery {

    protected DTXEngine _eng = null;
    protected DocumentHandler _handler = null;
    protected PrintWriter _logWriter = null;
    protected PreparedStatement _stmt = null;
    protected String _objName = null;
    protected String _objType = null;
    protected ClassMapping _clsMapping = null;
    protected ArrayList _ids = null;
    protected int _lastCol = 0;
    protected HashMap _cols = null;
    protected HashMap _classes = null;

    /**
     * Set the DocumentHandler that will receive the results (as SAX
     * events) for this query. By default, the query will use the
     * document handler of its DTXEngine.
     *
     * Changing the DocumentHandler in the middle of a query is
     * ill-advised.
     *
     * @param handler The DocumentHandler to use.
     */

    public void setHandler(final DocumentHandler handler) {
	_handler = handler;
    }

    /**
     * Set the log writer.
     *
     * @param logWriter The log writer to use.
     */

    public void setLogWriter(final PrintWriter logWriter) {
	_logWriter = logWriter;
    }

    /**
     * Binds an Object value to a parameter in the query.
     *
     * @param param 1-based index of the param (see note above).
     * @param value Object to bind.
     */
    public void bind(final int param, final Object value) throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    _stmt.setObject(param, value);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /**
     * Binds an String value to a parameter in the query.
     *
     * @param param 1-based index of the param (see note above).
     * @param value String to bind.
     */

    public void bind(final int param, final String value) throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    _stmt.setString(param, value);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /**
     * Binds an integer value to a parameter in the query.
     *
     * @param param 1-based index of the param (see note above).
     * @param value int to bind.
     */

    public void bind(final int param, final int value) throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    _stmt.setInt(param, value);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /**
     * Binds a long integer value to a parameter in the query.
     *
     * @param param 1-based index of the param (see note above).
     * @param value long integer to bind.
     */

    public void bind(final int param, final long value) throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    _stmt.setLong(param, value);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /**
     * Binds a float value to a parameter in the query.
     *
     * @param param 1-based index of the param (see note above).
     * @param value float to bind.
     */

    public void bind(final int param, final float value) throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    _stmt.setFloat(param, value);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /**
     * Binds a double value to a parameter in the query.
     *
     * @param param 1-based index of the param (see note above).
     * @param value double to bind.
     */

    public void bind(final int param, final double value) throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    _stmt.setDouble(param, value);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /**
     * Binds a boolean value to a parameter in the query.
     *
     * @param param 1-based index of the param (see note above).
     * @param value boolean to bind.
     */

    public void bind(final int param, final boolean value) throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    _stmt.setBoolean(param, value);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /**
     * This method executes the query. All results of the query are
     * sent to the DocumentHandler specified by setHandler() as SAX
     * events.
     */

    public void execute() throws DTXException {
	if (_stmt == null) {
	    throw new DTXException("No prepared statement.");
	}

	try {
	    ResultSet rs = _stmt.executeQuery();
	    emitSaxEvents(rs);
	    rs.close();
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /* Package scope */

    /* Default do-nothing constructor. */

    DTXQuery() {
    }

    /* Sets the engine to use for getting connections and
       descriptors. */

    void setEngine(final DTXEngine eng) {
	_eng = eng;
    }

    /* Prepares this query for use. Parses the OQL into SQL (plus some
       residual information), connects to the database and prepares a
       SQL statement. */

    void prepare(final String oql) throws DTXException {
	try {
	    String sql = parseOQL(oql);
	    Connection conn = _eng.getConnection();
	    _stmt = conn.prepareStatement(sql);
	} catch (SQLException sqle) {
	    throw new DTXException(sqle);
	}
    }

    /* protected */

    /* Translates a JDBC resultset into SAX events. Uses the recursive
       helper to get started. */

    protected void emitSaxEvents(final ResultSet rs) throws DTXException {

	try {
	    if (rs.next()) {
		emitSaxInt(rs, 0);
	    }
	} catch (Exception e) {
	    e.printStackTrace(_logWriter);
	    throw new DTXException(e);
	}
    }

    /* Recursive SAX emitter. Emits top-level elements, attributes,
       and simple (non-object) elements, then descends recursively
       into lower and lower child elements.

       This code is -highly- loopy, and it makes some assumptions
       about the ordering of sub-elements that may be unacceptable.
    */

    protected boolean emitSaxInt(final ResultSet rs, final int idIndex) throws DTXException {

	boolean hasValue = true;

	try {
	    String initParentValue = null;
	    String parentValue = null;
	    int parentColNum = 0;

	    if (idIndex != 0) {
		String parentCol = (String) _ids.get(idIndex - 1);
		Integer parentColInt = (Integer) _cols.get(parentCol);
		parentColNum = parentColInt.intValue();
		initParentValue = rs.getString(parentColNum);
		parentValue = initParentValue;
	    }

	    String idCol = (String) _ids.get(idIndex);
	    Integer idColNum = (Integer) _cols.get(idCol);

	    DTXClassDescriptor desc = (DTXClassDescriptor) _classes.get(idCol);
	    ClassMapping clsMapping = desc.getClassMapping();
	    String elementName = null;

	    if (clsMapping.getMapTo() == null ||
		clsMapping.getMapTo().getXml() == null) {
		elementName = clsMapping.getName();
	    } else {
		elementName = clsMapping.getMapTo().getXml();
	    }

	    String[] attrCols = desc.getAttrCols();
	    String[] simpleElementCols = desc.getSimpleElementCols();
	    String textCol = desc.getTextCol();

	    while (hasValue && (idIndex == 0 || parentValue.equalsIgnoreCase(initParentValue))) {
		if (idIndex == 0) {
		    _handler.startDocument();
		}

		String idVal = rs.getString(idColNum.intValue());

		AttributeListImpl attrs = new AttributeListImpl();

		for (int i = 0; i < attrCols.length; i++) {
		    String attrCol = attrCols[i];
		    Integer attrColNum = (Integer) _cols.get(attrCol);
		    FieldMapping field = desc.getAttr(attrCol);
		    String attrName = null;
		    if (field.getBindXml() == null || field.getBindXml().getName() == null) {
			attrName = field.getName();
		    } else {
			attrName = field.getBindXml().getName();
		    }

		    String attrValue = rs.getString(attrColNum.intValue());
		    attrs.addAttribute(attrName, "CDATA", attrValue);
		}

		_handler.startElement(elementName, attrs);

		for (int j = 0; j < simpleElementCols.length; j++) {
		    String simpleElementCol = simpleElementCols[j];
		    Integer elementColNum = (Integer) _cols.get(simpleElementCol);
		    FieldMapping elField = desc.getSimpleElement(simpleElementCol);
		    String elName = null;
		    if (elField.getBindXml() == null || elField.getBindXml().getName() == null) {
			elName = elField.getName();
		    } else {
			elName = elField.getBindXml().getName();
		    }

		    String elValue = rs.getString(elementColNum.intValue());
		    _handler.startElement(elName, new AttributeListImpl());
		    _handler.characters(elValue.toCharArray(), 0, elValue.length());
		    _handler.endElement(elName);
		}

		if (idIndex < _ids.size() - 1) {
		    hasValue = emitSaxInt(rs, idIndex + 1);
		}

		if (textCol != null) {
		    Integer textColNum = (Integer) _cols.get(textCol);
		    String textColValue = rs.getString(textColNum.intValue());
		    _handler.characters(textColValue.toCharArray(), 0, textColValue.length());
		}

		_handler.endElement(elementName);

		if (idIndex == 0) {
		    _handler.endDocument();
		}

		String newIdVal = rs.getString(idColNum.intValue());

		// Check to see if sub-call advanced this for us.

		if (newIdVal.equalsIgnoreCase(idVal)) {
		    hasValue = rs.next();
		}
		if (hasValue) {
		    if (idIndex != 0) {
			parentValue = rs.getString(parentColNum);
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace(_logWriter);
	    throw new DTXException(e);
	}

	return hasValue;
    }

    /* OQL parser. Most of this code is highjacked from the JDO
       OQLQueryImpl, with a lot of changes to keep from using
       JDOClassDescriptors, which depend on loading a Java class.

       It'd be nice to refactor this so there's only one OQL-parsing
       class.
    */

    protected String parseOQL(final String oql) throws DTXException {

	try {
	    _ids = new ArrayList();
	    _cols = new HashMap();
	    _classes = new HashMap();

	    StringTokenizer token = new StringTokenizer(oql);

	    if (! token.hasMoreTokens() || ! token.nextToken().equalsIgnoreCase("SELECT"))
		throw new DTXException("Query must start with SELECT");
	    if (! token.hasMoreTokens())
		throw new DTXException("Missing object name");
	    _objName = token.nextToken();
	    if (! token.hasMoreTokens() || ! token.nextToken().equalsIgnoreCase("FROM"))
		throw new DTXException("Object must be followed by FROM");
	    if (! token.hasMoreTokens())
		throw new DTXException("Missing object type");
	    _objType = token.nextToken();
	    if (! token.hasMoreTokens())
		throw new DTXException("Missing object name");
	    if (! _objName.equals(token.nextToken()))
		throw new DTXException("Object name not same in SELECT and FROM");

	    if (_logWriter != null) {
		_logWriter.println("Querying " + _objName + " of type " + _objType);
	    }

	    _clsMapping = _eng.getClassMapping(_objType);

	    if (_clsMapping == null) {
		throw new DTXException("dtx.NoClassDescription: " + _objType);
	    }

	    PersistenceFactory factory = _eng.getFactory();

	    if (factory == null) {
		throw new DTXException("dtx.NoFactory");
	    }

	    QueryExpression expr = factory.getQueryExpression();

	    if (expr == null) {
		throw new DTXException("dtx.NoQueryExpression");
	    }

	    initQuery(_clsMapping, expr);

	    if (token.hasMoreTokens()) {
		if (! token.nextToken().equalsIgnoreCase("WHERE")) {
		    throw new DTXException("Missing WHERE clause");
		}
		addField(_clsMapping, token, expr);
		while (token.hasMoreTokens()) {
		    if (!token.nextToken().equals("AND")) {
			throw new QueryException( "Only AND supported in WHERE clause" );
		    }
		    addField(_clsMapping, token, expr);
		}
	    }

	    String sql = expr.getStatement(false);

	    sql = sql + " ORDER BY ";

	    for (java.util.Iterator it = _ids.iterator(); it.hasNext(); ) {
		String id = (String) it.next();
		sql = sql + " " + id;
		if (it.hasNext()) {
		    sql = sql + ",";
		}
	    }

	    if (_logWriter != null) {
		_logWriter.println("SQL: " + sql);
	    }

	    return sql;

	} catch (Exception e) {
	    if (_logWriter != null) {
		e.printStackTrace(_logWriter);
	    }
	    throw new DTXException(e);
	}
    }


    /* This makes the basic parts of a query. Again, a lot of code was
       lifted from e.g. SQLEngine, and edited to use *Mapping instead
       of *Descriptor. It also should probably be re-factored
       somewhere.

       Additionally, contained ("rel") elements go through the same
       kind of manipulation as the main class; this stuff should be
       re-factored, too.
    */

    protected void initQuery(final ClassMapping clsMapping, final QueryExpression expr)
    throws DTXException {
        MapTo mapTo = clsMapping.getMapTo();

        if (mapTo == null) {
            throw new DTXException("no table mapping for: " + clsMapping.getName());
	}

	String table = mapTo.getTable();

        FieldMapping[] fields = clsMapping.getClassChoice().getFieldMapping();
	FieldMapping identity = null;

        String identityName = clsMapping.getIdentity(0);

	for (int j = 0; j < fields.length; j++) {
	    if (fields[j].getName().equals(identityName)) {
		identity = fields[j];
		break;
	    }
	}

	if (identity == null) {
	    throw new DTXException("no identity field in class: " + clsMapping.getName());
	}

	Sql identitySQLElement = identity.getSql();

	if (identitySQLElement == null) {
	    throw new DTXException("no identity SQL info in class: " + clsMapping.getName());
	}

	String identitySQL = identitySQLElement.getName()[0];

	_ids.add(table + "." + identitySQL);

	DTXClassDescriptor desc = new DTXClassDescriptor(clsMapping);

	_classes.put(table + "." + identitySQL, desc);

        // If this class extends another class, create a join with the parent table and
        // add the load fields of the parent class (but not the store fields)
        if (clsMapping.getExtends() != null) {

        /**
          * TODO : Needs to be resolved by Hand
          */

        MapTo extendsTo = new MapTo();//(ClassMapping) clsMapping.getExtends()).getMapTo();
	    if (extendsTo == null) {
		throw new DTXException("no mapping info for extends table.");
	    }
	    String extendsTable = extendsTo.getTable();
            expr.addInnerJoin(table, identitySQL, extendsTable, identitySQL);
            /**
             * needs to be resolved by hand
             */
            initQuery(new ClassMapping(), expr);
            //(ClassMapping) clsMapping.getExtends(), expr);
        }

        for (int i = 0; i < fields.length; ++i) {
	    FieldMapping field = fields[i];
	    Sql fieldSql = field.getSql();
	    ClassMapping relMapping = _eng.getClassMapping(field.getType());

	    if (fieldSql == null) {
		if (relMapping != null) {

		    // We have a one-to-many relationship with a sub object.
		    // get those objects, too.

                    FieldMapping[] relFields = relMapping.getClassChoice().getFieldMapping();
		    MapTo relMapTo = relMapping.getMapTo();

		    if (relMapTo == null) {
			throw new DTXException("dtx.NoRelatedMapTo");
		    }

		    String relTable = relMapTo.getTable();

            String foreKey = null;

            for (int k = 0; k < relFields.length; k++ ) {
			Sql relSql = relFields[k].getSql();
                        if (relSql != null) {
			    String type = relFields[k].getType();
			    if (type != null && type.equals(clsMapping.getName())) {
				foreKey = relSql.getName()[0];
			    }
			}
                    }

                    if (foreKey != null) {
                        expr.addOuterJoin(table, identitySQL, relTable, foreKey);
			DTXClassDescriptor relDesc = new DTXClassDescriptor(relMapping);

			for (int n = 0; n < relFields.length; n++ ) {
			    FieldMapping relField = relFields[n];
			    Sql relSql = relFields[n].getSql();
			    if (relSql != null) {
				String relFieldName = relSql.getName()[0];
				if (relFieldName == null) {
				    relFieldName = relFields[n].getName();
				}
				String relFullName = relTable + "." + relFieldName;

				BindXml relFieldXml = relFields[n].getBindXml();
				String node = "element";
				if (relFieldXml != null) {
				    node = relFieldXml.getNode().toString();
				}

				if (!relFieldName.equals(foreKey)) {
				    expr.addColumn(relTable, relFieldName);
				    _cols.put(relTable + "." + relFieldName, new Integer(++_lastCol));

				    if (node.equalsIgnoreCase("attribute")) {
					relDesc.addAttr(relFullName, relField);
				    } else if (node.equalsIgnoreCase("element")) {
					relDesc.addSimpleElement(relFullName, relField);
				    } else if (node.equalsIgnoreCase("text")) {
					relDesc.setTextCol(relFullName, relField);
				    }
				}
				if (relField.getName().equals(relMapping.getIdentity())) {
				    _ids.add(relTable + "." + relFieldName);
				    desc.addContained(relTable + "." + relFieldName, relMapping);
				    _classes.put(relTable + "." + relFieldName, relDesc);
				}
			    }
			}
                    }
                }

	    } else {

		String fieldName = fieldSql.getName()[0];
		if (fieldName == null) {
		    fieldName = fields[i].getName();
		}
		String fullName = table + "." + fieldName;

		BindXml fieldXml = field.getBindXml();
		String node = "element";
		if (fieldXml != null) {
		     node = fieldXml.getNode().toString();
		}

		if (node.equalsIgnoreCase("attribute")) {
		    desc.addAttr(fullName, field);
		} else if (node.equalsIgnoreCase("element")) {
		    desc.addSimpleElement(fullName, field);
		} else if (node.equalsIgnoreCase("text")) {
		    desc.setTextCol(fullName, field);
		}

		if (relMapping == null || fieldSql.getManyTable() == null) {
		    expr.addColumn(table, fieldName);
		    _cols.put(fullName, new Integer(++_lastCol));
		} else {
		    expr.addColumn(fieldSql.getManyTable(),
				   fieldName);
		    _cols.put(fullName, new Integer(++_lastCol));
		    expr.addOuterJoin(table, identitySQL,
				      fieldSql.getManyTable(),
				      fieldSql.getManyKey()[0]);
		}
	    }
        }
    }

    /* A misnomer, addField() actually adds a condition. Lifted, once
       again, from JDO stuff. */

    private void addField(final ClassMapping clsMapping, final StringTokenizer token, final QueryExpression expr)
    throws DTXException {
        if (!token.hasMoreTokens()) {
            throw new DTXException("Missing field name");
        }
        String name = token.nextToken();
        if (!token.hasMoreTokens()) {
            throw new DTXException("Missing operator");
        }
        String op = token.nextToken();
        if (!token.hasMoreTokens()) {
            throw new DTXException("Missing field value");
        }

        String value = token.nextToken();
        if (name.indexOf(".") > 0) {
            name = name.substring(name.indexOf(".") + 1);
        }

        FieldMapping[] fields = clsMapping.getClassChoice().getFieldMapping();
        FieldMapping field = null;

        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].getSql() != null && fields[i].getName().equals(name)) {
                field = fields[i];
                break;
            }
        }

        if (field == null) {
            throw new DTXException("The field " + name + " was not found");
        }

        Sql fieldSql = field.getSql();
        String table = clsMapping.getMapTo().getTable();

        if (value.startsWith("$")) {
            expr.addParameter(table, fieldSql.getName()[0], op);
        } else {
            expr.addCondition(table, fieldSql.getName()[0], op, value);
        }
    }
}
