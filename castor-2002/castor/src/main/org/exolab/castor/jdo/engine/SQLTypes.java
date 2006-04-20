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

import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.IOException;
import org.exolab.castor.util.Messages;
import org.exolab.castor.mapping.MappingException;


/**
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class SQLTypes
{


    /**
     * Separator between words in SQL name.
     */
    private static final char SQLWordSeparator = '_';


    /**
     * Separators between type name and parameter, e.g. "char[01]"
     */
    private static final char LeftParamSeparator = '[';


    private static final char RightParamSeparator = ']';


    /**
     * Returns the Java type from the SQL type name.
     *
     * @param sqlTypeName SQL type name (e.g. numeric)
     * @return The suitable Java type
     * @throws MappingException The SQL type is not recognized.
     */
    public static Class typeFromName( String sqlTypeName )
        throws MappingException
    {
        int sep;

        sep = sqlTypeName.indexOf( LeftParamSeparator );
        if ( sep >= 0 ) 
            sqlTypeName = sqlTypeName.substring( 0, sep );
        
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( sqlTypeName.equals( _typeInfos[ i ].sqlTypeName ) )
                return _typeInfos[ i ].javaType;
        }
        throw new MappingException( "jdo.sqlTypeNotSupported", sqlTypeName );
    }
    

    /**
     * Extracts parameter for type convertor from the name of the SQL type
     * of the form "SQL_type.domain".
     * If the type is not parameterized, returns null.
     *
     * @param sqlTypeName SQL type name (e.g. char[01])
     * @return Parameter (e.g. "01") or null
     */
    public static String paramFromName( String sqlTypeName )
    {
        int left;
        int right;

        left = sqlTypeName.indexOf( LeftParamSeparator );
        right = sqlTypeName.indexOf( RightParamSeparator );
        if ( right < 0 ) 
            right = sqlTypeName.length();

        if ( left >= 0 )
            return sqlTypeName.substring( left + 1, right );
        else
            return null;
    }


    /**
     * Returns the Java type from the SQL type.
     *
     * @param sqlType SQL type name (see JDBC API)
     * @return The suitable Java type
     * @throws MappingException The SQL type is not recognized.
     */
    public static Class typeFromSQLType( int sqlType )
        throws MappingException
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( sqlType == _typeInfos[ i ].sqlType )
                return _typeInfos[ i ].javaType;
        }
        throw new MappingException( "jdo.sqlTypeNotSupported", new Integer( sqlType ) );
    }


    /**
     * Returns the SQL type from the specified Java type. Returns <tt>OTHER</tt>
     * if the Java type has no suitable SQL type mapping. The argument
     * <tt>sqlType</tt> must be the return value from a previous call to
     * {@link #typeFromSQLType} or {@link #typeFromName}.
     *
     * @param javaType The Java class of the SQL type
     * @return SQL type from the specified Java type
     */
    public static int getSQLType( Class javaType )
    {
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( javaType.isAssignableFrom( _typeInfos[ i ].javaType ) )
                return _typeInfos[ i ].sqlType;
        }
        return java.sql.Types.OTHER;
    }
    
    
    /**
     * Convert from Java name to SQL name. Performs trivial conversion
     * by lowering case of all letters and adding underscore between
     * words, a word is identifier as starting with an upper case. Only
     * the last part of the Java name (following the last period) is used.
     * <p>
     * For example:
     * </ul>
     * <li>prodId becomes prod_id (field -> column)
     * <li>jdbc.ProdGroup becomes prod_group (object -> table)
     * <li>hobbies.getALife becomes get_a_life (compound field -> column)
     * </ul>
     *
     * @param javaName The Java identifier name
     * @return An equivalent SQL identifier name
     */
    public static String javaToSqlName( String javaName )
    {
        StringBuffer sql;
        int          i;
        char         ch;
        boolean      wasLower;
        
        // Get only the last part of the Java name (whether it's
        // class name with package, or field name with parent)
        if ( javaName.indexOf( '.' ) > 0 ) {
            javaName = javaName.substring( javaName.lastIndexOf( '.' ) + 1 );
        }
        
        sql = new StringBuffer( javaName.length() );
        wasLower = false;
        for ( i = 0 ; i < javaName.length() ; ++i ) {
            ch = javaName.charAt( i );
            // Our potential break point is an upper case letter
            // signalling the next word (thus must not be the first)
            if ( i > 0 && Character.isUpperCase( ch ) ) {
                // New word: previous letter was lower case
                if ( wasLower )
                    sql.append( SQLWordSeparator );
                else
                    // New word: next letter is lower case
                    if ( i < javaName.length() - 1 &&
                         Character.isLowerCase( javaName.charAt( i + 1 ) ) )
                        sql.append( SQLWordSeparator );
            }
            wasLower = Character.isLowerCase( ch );
            sql.append( Character.toLowerCase( ch ) );
        }
        return sql.toString();
    }


    /**
     * Convert from SQL name to Java name. Performs trivial conversion
     * by treating each underscore as a word separator and converting the
     * first letter of each word to upper case. If a scope is specified,
     * the scope is prepended to the Java name separated by a period.
     * <p>
     * For example:
     * </ul>
     * <li>prod_id, false becomes prodId (column -> field)
     * <li>prod_group, true becomes ProdGroup (table -> object)
     * <li>get_a_life, false, "product" becomes product.getALife (column -> field)
     * </ul>
     *
     * @param sqlName The SQL identifier name
     * @param className True if class name (first letter must be upper case)
     * @param scope Optional scope preceding name (package name, compound field)
     * @return An equivalent Java identifier name
     */
    public static String sqlToJavaName( String sqlName, boolean className, String scope )
    {
        StringBuffer java;
        int          i;
        
        java = new StringBuffer( sqlName.length() );
        if ( scope != null )
            java.append( scope ).append( '.' );
        for ( i = 0 ; i < sqlName.length() ; ++i ) {
            if ( i == 0 && className ) {
                java.append( Character.toUpperCase( sqlName.charAt( i ) ) );
            } else  if ( sqlName.charAt( i ) == SQLWordSeparator ) {
                ++i;
                if ( i < sqlName.length() )
                    java.append( Character.toUpperCase( sqlName.charAt( i ) ) );
            } else {
                java.append( Character.toLowerCase( sqlName.charAt( i ) ) );
            }
        }
        return java.toString();
    }


    public static Object getObject( ResultSet rs, int index, int sqlType )
            throws SQLException
    {
        Object value;
        long longVal;
        int intVal;
        boolean boolVal;
        double doubleVal;
        float floatVal;
        short shortVal;
        byte byteVal;

        switch ( sqlType ) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return rs.getString(index);
        case Types.DECIMAL:
        case Types.NUMERIC:
            return rs.getBigDecimal( index );
        case Types.INTEGER:
            intVal = rs.getInt( index );
            return ( rs.wasNull() ? null : new Integer( intVal ) );
        case Types.TIME:
            return rs.getTime( index );
        case Types.DATE:
            return rs.getDate( index );
        case Types.TIMESTAMP:
            return rs.getTimestamp( index ); 
        case Types.FLOAT:
        case Types.DOUBLE:
            doubleVal = rs.getDouble( index );
            return ( rs.wasNull() ? null : new Double( doubleVal ) );
        case Types.REAL:
            floatVal = rs.getFloat( index );
            return ( rs.wasNull() ? null : new Float( floatVal ) );
        case Types.SMALLINT:
            shortVal = rs.getShort( index );
            return ( rs.wasNull() ? null : new Short( shortVal ) );
        case Types.TINYINT:
            byteVal = rs.getByte( index );
            return ( rs.wasNull() ? null : new Byte( byteVal ) );
        case Types.LONGVARBINARY:
        case Types.VARBINARY:
        case Types.BINARY:
            return rs.getBytes(index);
        case Types.BLOB:
            try {
                Blob blob = rs.getBlob( index );
                InputStream blobIs = blob.getBinaryStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[256];
                int len = 0;
                int b;
                while ( (len = blobIs.read(buffer)) > 0 )
                    bos.write( buffer, 0, len );

                return bos.toByteArray();
            } catch ( IOException e ) {
                throw new SQLException("IOException thrown while reading BLOB into a byte array!");
            }
        case Types.CLOB:
            try {
                Clob blob = rs.getClob( index );
                Reader blobIs = blob.getCharacterStream();
                CharArrayWriter bos = new CharArrayWriter();
                char[] buffer = new char[256];
                int len = 0;
                int b;
                while ( (len = blobIs.read(buffer)) > 0 )
                    bos.write( buffer, 0, len );

                return bos.toString();
            } catch ( IOException e ) {
                throw new SQLException("IOException thrown while reading CLOB into a string!");
            }
        case Types.BIGINT:
            longVal = rs.getLong( index );
            return ( rs.wasNull() ? null : new Long( longVal ) );
        case Types.BIT:
            boolVal = rs.getBoolean( index );
            return ( rs.wasNull() ? null : new Boolean( boolVal ) );
        default:               
            value = rs.getObject( index );
            return ( rs.wasNull()? null : value );
        }
    }


    static class TypeInfo
    {
        final int    sqlType;
        
        final String sqlTypeName;
        
        final Class  javaType;
        
        TypeInfo( int sqlType, String sqlTypeName, Class javaType )
        {
            this.sqlType     = sqlType;
            this.sqlTypeName = sqlTypeName;
            this.javaType  = javaType;
        }

    }

    
    /**
     * List of all the SQL types supported by Castor JDO.
     */
    static TypeInfo[] _typeInfos = new TypeInfo[] {
        new TypeInfo( java.sql.Types.BIT,           "bit",           java.lang.Boolean.class ),
        new TypeInfo( java.sql.Types.TINYINT,       "tinyint",       java.lang.Byte.class ),
        new TypeInfo( java.sql.Types.SMALLINT,      "smallint",      java.lang.Short.class ),
        new TypeInfo( java.sql.Types.INTEGER,       "integer",       java.lang.Integer.class ),
        new TypeInfo( java.sql.Types.BIGINT,        "bigint",        java.lang.Long.class ),
        new TypeInfo( java.sql.Types.FLOAT,         "float",         java.lang.Double.class ),
        new TypeInfo( java.sql.Types.DOUBLE,        "double",        java.lang.Double.class ),
        new TypeInfo( java.sql.Types.REAL,          "real",          java.lang.Float.class ),
        new TypeInfo( java.sql.Types.NUMERIC,       "numeric",       java.math.BigDecimal.class ),
        new TypeInfo( java.sql.Types.DECIMAL,       "decimal",       java.math.BigDecimal.class ),
        new TypeInfo( java.sql.Types.CHAR,          "char",          java.lang.String.class ),
        new TypeInfo( java.sql.Types.VARCHAR,       "varchar",       java.lang.String.class ),
        new TypeInfo( java.sql.Types.LONGVARCHAR,   "longvarchar",   java.lang.String.class ),
        new TypeInfo( java.sql.Types.DATE,          "date",          java.sql.Date.class ),
        new TypeInfo( java.sql.Types.TIME,          "time",          java.sql.Time.class ),
        new TypeInfo( java.sql.Types.TIMESTAMP,     "timestamp",     java.sql.Timestamp.class ),
        new TypeInfo( java.sql.Types.BINARY,        "binary",        byte[].class ),
        new TypeInfo( java.sql.Types.VARBINARY,     "varbinary",     byte[].class ),
        new TypeInfo( java.sql.Types.LONGVARBINARY, "longvarbinary", byte[].class ),
        new TypeInfo( java.sql.Types.OTHER,         "other",         java.lang.Object.class ),
        new TypeInfo( java.sql.Types.JAVA_OBJECT,   "javaobject",    java.lang.Object.class )
    };
    

}













