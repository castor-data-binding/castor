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


import java.sql.Types;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.exolab.castor.util.Messages;
import org.exolab.castor.mapping.MappingException;


/**
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class SQLTypes
{


    /**
     * Separator between words in SQL name.
     */
    private static final char SQLWordSeparator = '_';


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
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( sqlTypeName.equals( _typeInfos[ i ].sqlTypeName ) )
                return _typeInfos[ i ].javaType;
        }
        throw new MappingException( "jdo.sqlTypeNotSupported", sqlTypeName );
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













