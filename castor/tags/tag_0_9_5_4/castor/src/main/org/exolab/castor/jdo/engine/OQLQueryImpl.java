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


import org.exolab.castor.jdo.*;
import org.exolab.castor.jdo.oql.*;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.QueryExpression;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 *
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class OQLQueryImpl
    implements Query, OQLQuery
{


    private LockEngine          _dbEngine;


    private DatabaseImpl        _dbImpl;


    private Class               _objClass;


    private JDOClassDescriptor  _clsDesc;


    private QueryExpression     _expr;


    /**
     * Stored procedure call
     */
    private String             _spCall;


    private Class[]            _bindTypes;


    private Object[]           _bindValues;

    private Hashtable          _paramInfo;


    private int                _fieldNum;

    private int                _projectionType;
    private Vector             _pathInfo;

    private PersistenceQuery   _query;

    private QueryResults       _results;


    OQLQueryImpl( DatabaseImpl dbImpl )
    {
        _dbImpl = dbImpl;
    }


    public void bind( Object value )
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
                    ClassMolder molder = _dbImpl.getLockEngine().getClassMolder( valueClass );

                    if ( molder != null ) {
                        value = molder.getActualIdentity( _dbImpl.getClassLoader(), value );
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
                if ( sqlClass != null && ! sqlClass.isAssignableFrom( valueClass ) ) {
                    // First convert the actual value to the field value
                    if ( fieldClass != valueClass ) {
                        try {
                            TypeConvertor tc = SQLTypes.getConvertor( valueClass, fieldClass );
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

            for (Enumeration e = info.getParamMap().elements(); e.hasMoreElements(); )
            {
                int fieldNum = ( (Integer) e.nextElement() ).intValue();
                _bindValues[ fieldNum - 1 ] = value;
            }

        } catch ( IllegalArgumentException except ) {
            throw except;
        }
        ++_fieldNum;
    }

    public void bind( boolean value )
    {
        bind( new Boolean( value ) );
    }


    public void bind( short value )
    {
        bind( new Short( value ) );
    }


    public void bind( int value )
    {
        bind( new Integer( value ) );
    }


    public void bind( long value )
    {
        bind( new Long( value ) );
    }


    public void bind( float value )
    {
        bind( new Float( value ) );
    }


    public void bind( double value )
    {
        bind( new Double( value ) );
    }

    public void create( String oql )
        throws QueryException
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

        _dbEngine = _dbImpl.getLockEngine();
        if ( _dbEngine == null )
            throw new QueryException( "Could not get a persistence engine" );

        ParseTreeWalker walker = new ParseTreeWalker(_dbEngine, parseTree, _dbImpl.getClassLoader());

        _objClass = walker.getObjClass();
        _clsDesc = walker.getClassDescriptor();
        _expr = walker.getQueryExpression();
        _paramInfo = walker.getParamInfo();
        _projectionType = walker.getProjectionType();
        _pathInfo = walker.getPathInfo();


        //port param info types back to the format of old bind types.
        //first get the maximum SQL param.
        int max = 0;
        for (Enumeration e = _paramInfo.elements(); e.hasMoreElements(); ) {
            ParamInfo info = (ParamInfo) e.nextElement();
            for (Enumeration f = info.getParamMap().elements(); f.hasMoreElements(); )
            {
                int paramIndex = ( (Integer) f.nextElement() ).intValue();
                if (  paramIndex > max )
                    max = paramIndex;
            }
        }

        //then create the types array and fill it
        _bindTypes = new Class[max];
        for (Enumeration e = _paramInfo.elements(); e.hasMoreElements(); )
        {
            ParamInfo info = (ParamInfo) e.nextElement();
            for (Enumeration f = info.getParamMap().elements(); f.hasMoreElements(); )
            {
                int paramIndex = ( (Integer) f.nextElement() ).intValue();
                _bindTypes[ paramIndex - 1 ] = (info.getSQLType() == null ? info.getTheClass()
                                                                          : info.getSQLType());
            }
        }

    }

    public void createCall( String oql ) throws QueryException {
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
                        boolean didParam = false;
                        for ( int j = i + 1; j < as; j++ ) {
                            char c = oql.charAt( j );
                            if (!Character.isDigit(c)) {
                                didParam = true;
                                sql.append("?"); // replace characters with "?"
                                break;
                            }
                            sb.append( c );
                        }
                        if (!didParam) sql.append('?'); // we reached the end of the string!
                        if ( sb.length() > 0 ) {
                            paramNo = Integer.valueOf( sb.toString() );
                        } else {
                            paramNo = new Integer( paramCnt + 1 );
                        }
                        info = (ParamInfo) _paramInfo.get( paramNo );
                        if ( info == null ) {
                            info = new ParamInfo( "", "java.lang.Object", null);
                        }
                        info.mapToSQLParam( paramCnt + 1 );
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
                        char c;

                        c = oql.charAt( j );
                        if ( c < '0' || c > '9' ) {
                            break;
                        }
                        sb.append( c );
                    }
                    if ( sb.length() > 0 ) {
                        paramNo = Integer.valueOf( sb.toString() );
                    } else {
                        paramNo = new Integer( paramCnt + 1 );
                    }
                    info = (ParamInfo) _paramInfo.get( paramNo );
                    if ( info == null ) {
                        info = new ParamInfo( "", "java.lang.Object", null);
                    }
                    info.mapToSQLParam( paramCnt + 1 );
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
            if ( _dbImpl.getClassLoader() == null )
                _objClass = Class.forName( objType );
            else
                _objClass = _dbImpl.getClassLoader().loadClass( objType );
        } catch ( ClassNotFoundException except ) {
            throw new QueryException( "Could not find class " + objType );
        }
        _dbEngine = _dbImpl.getLockEngine();
        if ( _dbEngine == null || _dbEngine.getPersistence( _objClass ) == null )
            throw new QueryException( "Could not find an engine supporting class " + objType );
    }


    public QueryResults execute()
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
        return execute( null );
    }

    public QueryResults execute(boolean scrollable)
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
        return execute( null, scrollable );
    }

    public QueryResults execute( short accessMode )
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
      return execute(accessMode, false);
    }

    public QueryResults execute( short accessMode,  boolean scrollable )
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
        switch ( accessMode ) {
        case Database.ReadOnly:
            return execute( AccessMode.ReadOnly, scrollable );
        case Database.Shared:
            return execute( AccessMode.Shared, scrollable );
        case Database.Exclusive:
            return execute( AccessMode.Exclusive, scrollable );
        case Database.DbLocked:
            return execute( AccessMode.DbLocked, scrollable );
        default:
            throw new IllegalArgumentException( "Value for 'accessMode' is invalid" );
        }
    }

    private QueryResults execute( AccessMode accessMode )
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
      return execute(accessMode, false);
    }

    private QueryResults execute( AccessMode accessMode, boolean scrollable )
        throws QueryException, PersistenceException, TransactionNotInProgressException
    {
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
                    results = _dbImpl.getTransaction().query( _dbEngine, _query, accessMode, scrollable );
                    _fieldNum = 0;

                    if ( _projectionType == ParseTreeWalker.PARENT_OBJECT )
                      _results = new OQLEnumeration( results );
                    else
                      _results = new OQLEnumeration( results, _pathInfo, _clsDesc);
                    break;
                case ParseTreeWalker.DEPENDANT_VALUE:
                case ParseTreeWalker.AGGREGATE:
                case ParseTreeWalker.FUNCTION:

                    SimpleQueryExecutor sqe = new SimpleQueryExecutor( _dbImpl );
                    _results =  sqe.execute( _expr, _bindValues);
                    _fieldNum = 0;

            }
        } catch ( PersistenceException except ) {
            throw except;
        }
        return _results;
    }

    /**
     * Get the generated SQL statement for this OQLQuery
     */
    public String getSQL() throws org.exolab.castor.jdo.QueryException {
	  if(_expr != null)
          return _expr.getStatement(true);
        else
         return  _spCall;
    }

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

    class OQLEnumeration
        implements QueryResults, Enumeration
    {


        private Object                 _lastObject;

        private Vector                 _pathInfo;

        private JDOClassDescriptor     _classDescriptor;


        private org.exolab.castor.persist.QueryResults _results;


        OQLEnumeration( org.exolab.castor.persist.QueryResults results,
                        Vector pathInfo, JDOClassDescriptor clsDesc )
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

         public boolean absolute(int row)
            throws PersistenceException
         {
               return _results.absolute(row);
         }

         public int size()
            throws PersistenceException
         {
               return _results.size();
         }

        public boolean hasMoreElements()
        {
            try {
                return hasMore( true );
            } catch ( PersistenceException except ) {
                // Will never happen
                return false;
            }
        }


        public boolean hasMore()
            throws PersistenceException
        {
            return hasMore( false );
        }


        public boolean hasMore( boolean skipError )
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
                Object result;

                result = _lastObject;
                _lastObject = null;
                if ( _pathInfo == null )
                    return result;
                else
                    return followPath( result );
            }
            if ( _results == null )
                throw new NoSuchElementException();
            try {
                identity = _results.nextIdentity();
                while ( identity != null ) {
                    try {
                        Object result;

                        result = _results.fetch();
                        if ( result != null )
                            if ( _pathInfo == null )
                                return result;
                            else
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

       public void close()
        {
            if ( _results != null ) {
                _results.close();
                _results = null;
            }
        }

        private Object followPath(Object parent) {
            //System.out.println("Following the path.");
            //follow the path
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
