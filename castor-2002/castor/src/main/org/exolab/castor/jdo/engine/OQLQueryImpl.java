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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.jdo.engine;


import java.util.Vector;
import java.util.StringTokenizer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.odmg.OQLQuery;
import org.odmg.ODMGRuntimeException;
import org.odmg.ODMGException;
import org.odmg.QueryParameterCountInvalidException;
import org.odmg.QueryParameterTypeInvalidException;
import org.odmg.QueryInvalidException;
import org.odmg.QueryException;
import org.odmg.TransactionNotInProgressException;
import org.exolab.castor.util.Messages;
import org.exolab.castor.jdo.ODMGSQLException;
import org.exolab.castor.jdo.desc.ObjectDesc;
import org.exolab.castor.jdo.desc.FieldDesc;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class OQLQueryImpl
    implements OQLQuery
{


    private int                _fieldNum;


    private SQLEngine          _engine;


    private DatabaseEngine     _dbEngine;


    private Vector             _fields;


    private Object[]           _values;


    private String             _sql;


    private Class              _objClass;


    public void bind( Object obj )
	throws QueryParameterCountInvalidException,
	       QueryParameterTypeInvalidException
    {
	if ( _sql == null ) {
	    throw new ODMGRuntimeException( "Must create query before using it" );
	}
	if ( _fieldNum == _values.length )
	    throw new QueryParameterCountInvalidException( "Only " + _fieldNum + " fields in this query" );
	_values[ _fieldNum ] = obj;
	++_fieldNum;
    }


    public void create( String oql )
	throws QueryInvalidException
    {
	StringTokenizer token;
	String          objType;
	String          objName;
	StringBuffer    sql;
	ObjectDesc      objDesc;

	_sql = null;
	_fieldNum = 0;
	_fields = new Vector();
	sql = new StringBuffer();
	token = new StringTokenizer( oql );
	if ( ! token.hasMoreTokens() || ! token.nextToken().equalsIgnoreCase( "SELECT" ) )
	    throw new QueryInvalidException( "Query must start with SELECT" );
	if ( ! token.hasMoreTokens() )
	    throw new QueryInvalidException( "Missing object name" );
	objName = token.nextToken();
	if ( ! token.hasMoreTokens() || ! token.nextToken().equalsIgnoreCase( "FROM" ) )
	    throw new QueryInvalidException( "Object must be followed by FROM" );
	if ( ! token.hasMoreTokens() )
	    throw new QueryInvalidException( "Missing object type" );
	objType = token.nextToken();
	if ( ! token.hasMoreTokens() )
	    throw new QueryInvalidException( "Missing object name" );
	if ( ! objName.equals( token.nextToken() ) )
	    throw new QueryInvalidException( "Object name not same in SELECT and FROM" );
	if ( ! token.hasMoreTokens() || ! token.nextToken().equalsIgnoreCase( "WHERE" ) )
	    throw new QueryInvalidException( "Missing WHERE clause" );

	try {
	    _objClass = Class.forName( objType );
	} catch ( ClassNotFoundException except ) {
	    throw new QueryInvalidException( "Could not find class " + objType );
	}
	_dbEngine = DatabaseEngine.getDatabaseEngine( _objClass ); 
	if ( _dbEngine == null )
	    throw new QueryInvalidException( "Cold not find an engine supporting class " + objType );
	_engine = _dbEngine.getSQLEngine( _objClass );
	objDesc = _engine.getObjectDesc();

	parseField( objDesc, token, sql );
	while ( token.hasMoreTokens() ) {
	    if ( ! token.nextToken().equals( "AND" ) )
		throw new QueryInvalidException( "Only AND supported in WHERE clause" );
	    parseField( objDesc, token, sql );
	}

	_sql = sql.insert( 0, _engine._sqlFinder ).append( _engine._sqlFinderJoin ).toString();
	_values = new Object[ _fields.size() ];
    }


    private void parseField( ObjectDesc objDesc, StringTokenizer token, StringBuffer sqlWhere )
	throws QueryInvalidException
    {
	String      name;
	String      op;
	String      value;
	FieldDesc[] fields;
	FieldDesc   field;

	if ( ! token.hasMoreTokens() )
	    throw new QueryInvalidException( "Missing field name" );
	name = token.nextToken();
	if ( ! token.hasMoreTokens() )
	    throw new QueryInvalidException( "Missing operator" );
	op = token.nextToken();
	if ( ! token.hasMoreTokens() )
	    throw new QueryInvalidException( "Missing field value" );

	value = token.nextToken();
	if ( name.indexOf( "." ) > 0 )
	    name = name.substring( name.indexOf( "." ) + 1 );
	fields = objDesc.getFieldDescs();
	field = null;
	for ( int i = 0 ; i < fields.length ; ++i ) {
	    if ( fields[ i ].getFieldName().equals( name ) ) {
		sqlWhere.append( objDesc.getSqlName( fields[ i ].getSqlName() ) );
		field = fields[ i ];
		break;
	    }
	}
	if ( field == null ) {
	    fields = objDesc.getPrimaryKey().getFieldDescs();
	    for ( int i = 0 ; i < fields.length ; ++i ) {
		if ( fields[ i ].getFieldName().equals( name ) ) {
		    sqlWhere.append( objDesc.getSqlName( fields[ i ].getSqlName() ) );
		    field = fields[ i ];
		    break;
		}
	    }
	}
	if ( fields == null ) {
	    if ( objDesc.getPrimaryKey().isPrimitive() &&
		 objDesc.getPrimaryKeyField().getFieldName().equals( name ) ) {
		sqlWhere.append( objDesc.getSqlName( objDesc.getPrimaryKey().getSqlName() ) );
		field = objDesc.getPrimaryKeyField();
	    }
	}
	if ( field == null )
	    throw new QueryInvalidException( "The field " + name + " was not found" );
	sqlWhere.append( op );
	if ( value.startsWith( "$" ) ) {
	    sqlWhere.append( "?" );
	} else {
	    sqlWhere.append( value );
	}
	_fields.addElement( field.getObjectClass() );
    }


    public Object execute()
	throws QueryException
    {
	TransactionContext tx;
	Object             obj;

	try {
	    tx = TransactionImpl.getCurrentContext();
	    if ( tx == null || tx.getStatus() != TransactionContext.Status.Open )
		throw new TransactionNotInProgressException( Messages.message( "castor.jdo.odmg.dbTxNotInProgress" ) );
	    obj = tx.query( _dbEngine, _objClass, _sql, _values, false );
	    _fieldNum = 0;
	    return obj;
	} catch ( ODMGException except ) {
	    throw new QueryException( except.toString() );
	} catch ( ODMGSQLException except ) {
	    throw new QueryException( except.toString() );
	}
    }


}
