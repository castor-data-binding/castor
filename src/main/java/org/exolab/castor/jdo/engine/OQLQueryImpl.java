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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import org.castor.jdo.engine.SQLTypeConverters;
import org.castor.jdo.util.ClassLoadingUtils;
import org.castor.persist.TransactionContext;
import org.castor.util.Messages;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DbMetaInfo;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.oql.Lexer;
import org.exolab.castor.jdo.oql.ParamInfo;
import org.exolab.castor.jdo.oql.ParseTreeNode;
import org.exolab.castor.jdo.oql.ParseTreeWalker;
import org.exolab.castor.jdo.oql.Parser;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * An OQLQuery implementation to execute a query based upon an OQL statement   
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class OQLQueryImpl
    implements Query, OQLQuery
{

    private LockEngine _dbEngine;

    private Database _database;

    private Class _objClass;

    private JDOClassDescriptor _clsDesc;

    private QueryExpression _expr;

    /**
     * Stored procedure call
     */
    private String _spCall;

    private Class[] _bindTypes;

    private Object[] _bindValues;

    private Hashtable _paramInfo;

    private int _fieldNum;

    private int _projectionType;
    
    private Vector _projectionInfo;

    private PersistenceQuery _query;

    private QueryResults _results;

    /**
     * Creates an instance to execute a query based upon an OQL statement
     * @param database The Castor database to run the query against.
     */
    OQLQueryImpl(final Database database)
    {
        _database = database;
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#bind(java.lang.Object)
     */
    public void bind(Object value)
    {
        if ( _expr == null && _spCall == null )
            throw new IllegalStateException( "Must create query before using it" );
        if ( _fieldNum == _paramInfo.size() )
            throw new IllegalArgumentException( "Only " + _paramInfo.size() +
                                                " fields in this query" );
        try {
            ParamInfo info = (ParamInfo) _paramInfo.get(new Integer( _fieldNum + 1 ));

            //do type checking and conversion
            Class paramClass = info.getTheClass();
            Class fieldClass = info.getFieldType();
            Class sqlClass = info.getSQLType();

            if ( value != null ) {
                Class valueClass = value.getClass();

                if ( paramClass.isAssignableFrom( valueClass ) ) {
                    ClassMolder molder = ((AbstractDatabaseImpl) _database).getLockEngine().getClassMolder( valueClass );

                    if ( molder != null ) {
                        Identity temp = molder.getActualIdentity( _database.getClassLoader(), value );
                        if (temp == null) {
                            value = null;
                        } else  if (temp.size() == 1) {
                            value = temp.get(0);
                        } else {
                            throw new IllegalArgumentException("Unable to bind multi column identities");
                        }
                    }
                } else if ( info.isUserDefined() ) {
                        //If the user specified a type they must pass that exact type.

                        throw new IllegalArgumentException( "Query paramter " +
                                                            ( _fieldNum + 1 ) +
                                                            " is not of the expected type " +
                                                            paramClass +
                                                            " it is an instance of the class "
                                                            + valueClass );
                }
                if ( sqlClass != null && !sqlClass.isAssignableFrom( valueClass ) ) {
                    // First convert the actual value to the field value
                    if ( fieldClass != valueClass ) {
                        try {
                            TypeConvertor tc = SQLTypeConverters.getConvertor( valueClass, fieldClass );
                            value = tc.convert( value, null );
                        } catch ( MappingException e ) {
                            throw new IllegalArgumentException( "Query parameter "
                                                                + ( _fieldNum + 1 )
                                                                + " cannot be converted from "
                                                                + valueClass + " to "
                                                                + paramClass
                                                                + ", because no convertor can be found." );
                        }
                    }
                    // Perform conversion from field type to SQL type, if needed
                    if (info.getConvertor() != null) {
                        value = info.getConvertor().convert( value, info.getConvertorParam() );
                    }
                }
            }
            if ( _bindValues == null )
                _bindValues = new Object[ _bindTypes.length ];

            _bindValues[_fieldNum++] = value;
        } catch ( IllegalArgumentException except ) {
            throw except;
        }
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#bind(boolean)
     */
    public void bind(final boolean value)
    {
        bind(new Boolean(value));
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#bind(short)
     */
    public void bind(final short value)
    {
        bind(new Short(value));
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#bind(int)
     */
    public void bind(final int value)
    {
        bind(new Integer(value));
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#bind(long)
     */
    public void bind(final long value)
    {
        bind(new Long(value));
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#bind(float)
     */
    public void bind(final float value)
    {
        bind(new Float(value));
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#bind(double)
     */
    public void bind(final double value)
    {
        bind(new Double(value));
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.OQLQuery#create(java.lang.String)
     */
    public void create(final String oql) throws PersistenceException
    {
        _fieldNum = 0;
        _expr = null;
        _spCall = null;

        // Separate parser for CALL-type queries (using stored procedured)
        if ( oql.startsWith("CALL ") ) {
            createCall( oql );
            return;
        }

        Lexer lexer = new Lexer(oql);
        Parser parser = new Parser(lexer);
        ParseTreeNode parseTree = parser.getParseTree();

        _dbEngine = ((AbstractDatabaseImpl) _database).getLockEngine();
        if ( _dbEngine == null )
            throw new QueryException( "Could not get a persistence engine" );

	    TransactionContext trans = ((AbstractDatabaseImpl) _database).getTransaction();
	    DbMetaInfo dbInfo = trans.getConnectionInfo(_dbEngine);

	    ParseTreeWalker walker = new ParseTreeWalker(_dbEngine, parseTree, _database.getClassLoader(), dbInfo);

        _objClass = walker.getObjClass();
        _clsDesc = walker.getClassDescriptor();
        _expr = walker.getQueryExpression();
        _paramInfo = walker.getParamInfo();
        _projectionType = walker.getProjectionType();
        _projectionInfo = walker.getProjectionInfo();

        // create the types array and fill it
        _bindTypes = new Class[_paramInfo.size()];
        int paramIndex = 0;
        for (Enumeration e = _paramInfo.elements(); e.hasMoreElements(); ) {
            ParamInfo info = (ParamInfo) e.nextElement();

            _bindTypes[paramIndex++] = (info.getSQLType()==null? info.getTheClass(): info.getSQLType());
        }
    }

    /**
     * @param oql
     * @throws QueryException
     */
    public void createCall(final String oql) throws QueryException {
        StringBuffer sql;
        int as;
        int leftParen;
        int rightParen;
        int paramCnt;
        String objType;
        ParamInfo info;
        StringBuffer sb;
        Integer paramNo;

        if ( !oql.startsWith("CALL ") ) {
            throw new QueryException( "Stored procedure call must start with CALL" );
        }

        // Fix for bug #995
        // as = oql.indexOf( " AS " );
        as = oql.lastIndexOf( " AS " );
        if ( as < 0 ) {
            throw new QueryException( "Stored procedure call must end with \"AS <class-name>\"" );
        }
        leftParen = oql.indexOf( "(" );
        rightParen = oql.indexOf( ")" );
        sql = new StringBuffer();
        paramCnt = 0;
        _paramInfo = new Hashtable();
        if ( oql.startsWith("CALL SQL") ) {
            int startOff = oql.toUpperCase().indexOf("WHERE "); // parameters begin here!

            if (!(startOff < 0)) {
                startOff += 6;
                sql.append(oql.substring(5, startOff));

                for ( int i = startOff; i < as; ++i ) {
                    if ( oql.charAt( i ) == '$' ) {
                        // get parameter number if given
                        sb = new StringBuffer();
                        for ( int j = i + 1; j < as; j++ ) {
                            char c = oql.charAt( j );
                            if (!Character.isDigit(c))
                                break;
                            sb.append( c );
                        }
                        sql.append('?'); // replace "$" with "?"
                        if ( sb.length() > 0 ) {
                            sql.append(sb); // and add parameter number to it
                            paramNo = Integer.valueOf( sb.toString() );
                        } else {
                            paramNo = new Integer( paramCnt + 1 );
                        }
                        info = (ParamInfo) _paramInfo.get( paramNo );
                        if ( info == null ) {
                            info = new ParamInfo( "", "java.lang.Object", null, _database.getClassLoader());
                        }
                        //info.mapToSQLParam( paramCnt + 1 );
                        _paramInfo.put( paramNo , info );
                        paramCnt++;

                        i += sb.length();
                    } else {
                        sql.append(oql.charAt(i));
                    }
                }
            } else {
                sql.append(oql.substring(5, as));
            }
        }
        else if ((leftParen < 0 && rightParen < 0) ) {
            sql.append( oql.substring( 5, as ) );
        } else {
            if ( ( leftParen < 0 && rightParen >= 0 )
                    || ( leftParen > rightParen ) ) {
                throw new QueryException( "Syntax error: parenthesis" );
            }
            sql.append( oql.substring( 5, leftParen ) );
            sql.append( '(' );
            for ( int i = leftParen + 1; i < rightParen; i++ ) {
                if ( oql.charAt( i ) == '$' ) {
                    // get parameter number if given
                    sb = new StringBuffer();
                    for ( int j = i + 1; j < rightParen; j++ ) {
                        char c = oql.charAt( j );
                        if (!Character.isDigit(c))
                            break;
                        sb.append( c );
                    }
                    if ( sb.length() > 0 ) {
                        paramNo = Integer.valueOf( sb.toString() );
                    } else {
                        paramNo = new Integer( paramCnt + 1 );
                    }
                    info = (ParamInfo) _paramInfo.get( paramNo );
                    if ( info == null ) {
                        info = new ParamInfo( "", "java.lang.Object", null, _database.getClassLoader());
                    }
                    //info.mapToSQLParam( paramCnt + 1 );
                    _paramInfo.put( paramNo , info );
                    paramCnt++;
                }
            }
            for ( int i = 0; i < paramCnt; i++ ) {
                sql.append( '?' );
                if ( i < paramCnt - 1 )
                    sql.append( ',' );
            }
            sql.append( ')' );
        }
        _spCall = sql.toString();
        _projectionType = ParseTreeWalker.PARENT_OBJECT;
        _bindTypes = new Class[ paramCnt ];
        for ( int i = 0; i < paramCnt; i++ )
            _bindTypes[ i ] = Object.class;

        objType = oql.substring( as + 4 ).trim();
        if ( objType.length() == 0 ) {
            throw new QueryException( "Missing object name" );
        }
        try {
            _objClass = ClassLoadingUtils.loadClass(_database.getClassLoader(), objType);
        } catch ( ClassNotFoundException except ) {
            throw new QueryException( "Could not find class " + objType );
        }
        _dbEngine = ((AbstractDatabaseImpl) _database).getLockEngine();
        if ( _dbEngine == null || _dbEngine.getPersistence( _objClass ) == null )
            throw new QueryException( "Could not find an engine supporting class " + objType );
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#execute()
     */
    public QueryResults execute()
    throws QueryException, PersistenceException, TransactionNotInProgressException {
        return execute( null );
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#execute(boolean)
     */
    public QueryResults execute(final boolean scrollable)
    throws QueryException, PersistenceException, TransactionNotInProgressException {
        return execute( null, scrollable );
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#execute(org.exolab.castor.mapping.AccessMode)
     */
    public QueryResults execute(final AccessMode accessMode)
    throws QueryException, PersistenceException, TransactionNotInProgressException {
        return execute(accessMode, false);
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#execute(org.exolab.castor.mapping.AccessMode, boolean)
     */
    public QueryResults execute(final AccessMode accessMode, final boolean scrollable)
    throws QueryException, PersistenceException, TransactionNotInProgressException {
        org.exolab.castor.persist.QueryResults      results;
        SQLEngine         engine;

        if ( _expr == null && _spCall == null )
            throw new IllegalStateException( "Must create query before using it" );
        if (_results != null) {
            _results.close();
        }
        try {
            switch (_projectionType) {
                case ParseTreeWalker.PARENT_OBJECT:
                case ParseTreeWalker.DEPENDANT_OBJECT:
                case ParseTreeWalker.DEPENDANT_OBJECT_VALUE:
                    try {
                        engine = (SQLEngine) _dbEngine.getPersistence( _objClass );
                        if ( _expr != null ) {
                            _query = engine.createQuery( _expr, _bindTypes, accessMode );
                        } else {
                            _query = engine.createCall( _spCall, _bindTypes );
                        }
                        if ( _bindValues != null ) {
                            for ( int i = 0 ; i < _bindValues.length ; ++i )
                                _query.setParameter( i, _bindValues[ i ] );
                        }
                    } catch ( QueryException except ) {
                        throw new QueryException( except.getMessage() );
                    }
                    results = ((AbstractDatabaseImpl) _database).getTransaction().query( _dbEngine, _query, accessMode, scrollable );
                    _fieldNum = 0;

                    if ( _projectionType == ParseTreeWalker.PARENT_OBJECT )
                      _results = new OQLEnumeration( results );
                    else
                      _results = new OQLEnumeration( results, _projectionInfo, _clsDesc);
                    break;

                case ParseTreeWalker.DEPENDANT_VALUE:
                case ParseTreeWalker.AGGREGATE:
                case ParseTreeWalker.FUNCTION:
                    try {
                        
                        java.sql.Connection conn = ((AbstractDatabaseImpl) _database).getTransaction().getConnection(_dbEngine);
                        SimpleQueryExecutor sqe = new SimpleQueryExecutor( _database );
                        _results =  sqe.execute(conn, _expr, _bindValues);
		            } catch ( QueryException except ) {
		                throw new QueryException(Messages.message ("persist.simple.query.failed"), except);
		            }
                    _fieldNum = 0;
            }
        } catch ( PersistenceException except ) {
            throw except;
        }

        return _results;
    }

    /**
     * Get the generated SQL statement for this OQLQuery
     * @return A SQL statement.
     * @throws QueryException If the SQL query cannot be generated.
     */
    public String getSQL() throws org.exolab.castor.jdo.QueryException {
	  if(_expr != null) {
          return _expr.getStatement(true);
	  }

	  return _spCall;
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Query#close()
     */
    public void close()
    {
        if ( _query != null ) {
            _query.close();
            _query = null;
        }
        if ( _results != null ) {
            _results.close();
            _results = null;
        }
    }

    /**
     * {@see java.util.Enumeration} implementation to traverse the result as returned by the
     * execution of the OQL query.
     */
    class OQLEnumeration implements QueryResults, Enumeration
    {
        private Object                 _lastObject;

        private Vector                 _pathInfo;

        private JDOClassDescriptor     _classDescriptor;

        private org.exolab.castor.persist.QueryResults _results;

        /**
         * Creates an instance of this class.
         * @param results
         * @param pathInfo
         * @param clsDesc
         */
        OQLEnumeration(org.exolab.castor.persist.QueryResults results,
                       Vector pathInfo, JDOClassDescriptor clsDesc)
        {
            _results = results;
            _pathInfo = pathInfo;
            _classDescriptor = clsDesc;
        }


        OQLEnumeration( org.exolab.castor.persist.QueryResults results )
        {
            _results = results;
            _pathInfo = null;
            _classDescriptor = null;
        }

        /**
         * @inheritDoc
         * @see org.exolab.castor.jdo.QueryResults#absolute(int)
         */
        public boolean absolute(int row) throws PersistenceException
        {
            return _results.absolute(row);
        }
        
        /**
         * @inheritDoc
         * @see org.exolab.castor.jdo.QueryResults#size()
         */
        public int size() throws PersistenceException
        {
            return _results.size();
        }

        /**
         * @inheritDoc
         * @see java.util.Enumeration#hasMoreElements()
         */
        public boolean hasMoreElements()
        {
            try {
                return hasMore( true );
            } catch ( PersistenceException except ) {
                // Will never happen
                return false;
            }
        }

        /**
         * @inheritDoc
         * @see org.exolab.castor.jdo.QueryResults#hasMore()
         */
        public boolean hasMore()
            throws PersistenceException
        {
            return hasMore( false );
        }


        public boolean hasMore(final boolean skipError)
            throws PersistenceException
        {
            Object identity;

            if ( _lastObject != null )
                return true;
            if ( _results == null )
                return false;
            try {
                identity = _results.nextIdentity();
                while ( identity != null ) {
                    try {
                        _lastObject = _results.fetch();
                        if ( _lastObject != null )
                            break;
                    } catch ( ObjectNotFoundException except ) {
                        // Object not found, deleted, etc. Just skip to next one.
                        identity = _results.nextIdentity();
                    } catch ( PersistenceException except ) {
                        // Error occured. If not throwing exception just skip to
                        // next object.
                        identity = _results.nextIdentity();
                        if ( ! skipError )
                            throw except;
                    }
                }
                if ( identity == null ) {
                    _results.close();
                    _results = null;
                }
            } catch ( PersistenceException except ) {
                _results.close();
                _results = null;
                if ( ! skipError )
                    throw except;
            }
            return ( _lastObject != null );
        }

        /**
         * @inheritDoc
         * @see java.util.Enumeration#nextElement()
         */
        public Object nextElement()
            throws NoSuchElementException
        {
            try {
                return next( true );
            } catch ( PersistenceException except ) {
                // Will never happen
                return null;
            }
        }

        /**
         * @inheritDoc
         * @see org.exolab.castor.jdo.QueryResults#next()
         */
        public Object next()
            throws PersistenceException, NoSuchElementException
        {
            return next( false );
        }


        private Object next( boolean skipError )
            throws PersistenceException, NoSuchElementException
        {
            Object identity;

            if ( _lastObject != null ) {
                Object result = _lastObject;

                _lastObject = null;
                if ( _pathInfo == null ) return result;
                return followPath( result );
            }
            if ( _results == null )
                throw new NoSuchElementException();
            try {
                identity = _results.nextIdentity();
                while ( identity != null ) {
                    try {
                        Object result = _results.fetch();

                        if ( result != null )
                            if ( _pathInfo == null ) return result;
                        return followPath( result );
                    } catch ( ObjectNotFoundException except ) {
                        // Object not found, deleted, etc. Just skip to next one.
                    } catch ( PersistenceException except ) {
                        // Error occured. If not throwing exception just skip to
                        // next object.
                        if ( ! skipError )
                            throw except;
                    }
                    identity = _results.nextIdentity();
                }
                if ( identity == null ) {
                    _results.close();
                    _results = null;
                }
            } catch ( PersistenceException except ) {
                _results.close();
                _results = null;
                if ( ! skipError )
                    throw except;
            }
            throw new NoSuchElementException();
        }

        /**
         * @inheritDoc
         * @see org.exolab.castor.jdo.QueryResults#close()
         */
        public void close()
        {
            if ( _results != null ) {
                _results.close();
                _results = null;
            }
        }
        
        private Object followPath(Object parent) {
            JDOClassDescriptor curClassDesc = _classDescriptor;
            Object curObject = parent;
            for ( int i = 1; i < _pathInfo.size(); i++ ) {
                String curFieldName = (String) _pathInfo.elementAt(i);
                try {
                    JDOFieldDescriptor curFieldDesc =
                        curClassDesc.getField( curFieldName );
                    FieldHandler handler = curFieldDesc.getHandler();
                    curObject = handler.getValue( curObject );
                    curClassDesc = (JDOClassDescriptor) curFieldDesc.getClassDescriptor();
                }
                catch (Exception ex) {
                    throw new NoSuchElementException( "An exception was thrown trying to access get methods to follow the path expression. " + ex.toString() );
                }
            }
            
            return curObject;
        }
        
    }

}
