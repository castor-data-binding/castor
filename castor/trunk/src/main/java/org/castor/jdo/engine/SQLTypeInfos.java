/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.jdo.engine;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

import org.exolab.castor.mapping.MappingException;

/**
 * Utility class to translate SQL type by integer value or name into corresponding
 * Java type.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class SQLTypeInfos {
    //-----------------------------------------------------------------------------------

    /**
     * Class that assoiziates SQL type number, SQL type name and Java type.
     */
    private static class TypeInfo {
        /** SQL type number. */
        private final int _sqlTypeNum;

        /** SQL type name. */
        private final String _sqlTypeName;

        /** Java type. */
        private final Class _javaType;

        /**
         * Construct a new TypeInfo instance with given SQL type number, SQL type name
         * and Java type.
         * 
         * @param sqlTypeNum SQL type number.
         * @param sqlTypeName SQL type name.
         * @param javaType Java type.
         */
        TypeInfo(final int sqlTypeNum, final String sqlTypeName, final Class javaType) {
            _sqlTypeNum     = sqlTypeNum;
            _sqlTypeName = sqlTypeName;
            _javaType  = javaType;
        }
    }

    //-----------------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLTypeInfos.class);
    
    /** List of all the SQL types supported by Castor JDO. */
    private static final TypeInfo[] TYPEINFO = new TypeInfo[] {
        new TypeInfo(Types.BIT,           "bit",           java.lang.Boolean.class),
        new TypeInfo(Types.TINYINT,       "tinyint",       java.lang.Byte.class),
        new TypeInfo(Types.SMALLINT,      "smallint",      java.lang.Short.class),
        new TypeInfo(Types.INTEGER,       "integer",       java.lang.Integer.class),
        new TypeInfo(Types.BIGINT,        "bigint",        java.lang.Long.class),
        new TypeInfo(Types.FLOAT,         "float",         java.lang.Double.class),
        new TypeInfo(Types.DOUBLE,        "double",        java.lang.Double.class),
        new TypeInfo(Types.REAL,          "real",          java.lang.Float.class),
        new TypeInfo(Types.NUMERIC,       "numeric",       java.math.BigDecimal.class),
        new TypeInfo(Types.DECIMAL,       "decimal",       java.math.BigDecimal.class),
        new TypeInfo(Types.CHAR,          "char",          java.lang.String.class),
        new TypeInfo(Types.VARCHAR,       "varchar",       java.lang.String.class),
        new TypeInfo(Types.LONGVARCHAR,   "longvarchar",   java.lang.String.class),
        new TypeInfo(Types.DATE,          "date",          java.sql.Date.class),
        new TypeInfo(Types.TIME,          "time",          java.sql.Time.class),
        new TypeInfo(Types.TIMESTAMP,     "timestamp",     java.sql.Timestamp.class),
        new TypeInfo(Types.BINARY,        "binary",        byte[].class),
        new TypeInfo(Types.VARBINARY,     "varbinary",     byte[].class),
        new TypeInfo(Types.LONGVARBINARY, "longvarbinary", byte[].class),
        new TypeInfo(Types.OTHER,         "other",         java.lang.Object.class),
        new TypeInfo(Types.JAVA_OBJECT,   "javaobject",    java.lang.Object.class),
        new TypeInfo(Types.BLOB,          "blob",          java.io.InputStream.class),
        new TypeInfo(Types.CLOB,          "clob",          java.sql.Clob.class)
    };

    /** Thread local Calendar instance pool. */
    private static final ThreadLocal THREAD_SAFE_CALENDAR = new ThreadLocal() {
        // The Calendar passed to ResultSet.getTimestamp() etc can actually
        // be modified depending on the database driver implementation.  To guard
        // against synchronization issues, we need to pass either a local
        // instance (which is expensive, creating one for each call), or create
        // a thread-local instance (which only gets created once per thread).
        // The latter is what this is for.
        public Object initialValue() { return new GregorianCalendar(); }
    };
    
    /** Time zone based on setting in castor.properties file (or
     *  default time zone if not specified). */
    private static final TimeZone TIME_ZONE;
    
    static {
        Configuration config = Configuration.getInstance();
        String zone = config.getProperty(ConfigKeys.DEFAULT_TIMEZONE, "");
        if (zone.length() == 0) {
            TIME_ZONE = TimeZone.getDefault();
        } else {
            TIME_ZONE = TimeZone.getTimeZone(zone);
        }
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Get a Calendar instance for current thread.
     * 
     * @return A Calendar instance for current thread.
     */
    private static Calendar getCalendar() {
        // We have to reset the time zone each time in case the result set
        // implementation changes it.
        Calendar calendar = (Calendar) THREAD_SAFE_CALENDAR.get();
        calendar.setTimeZone(TIME_ZONE);
        return calendar;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Returns the Java type for the given SQL type.
     *
     * @param sqlTypeNum SQL type name (see JDBC API)
     * @return The suitable Java type
     * @throws MappingException The SQL type is not recognized.
     */
    public static Class sqlTypeNum2javaType(final int sqlTypeNum)
    throws MappingException {
        for (int i = 0; i < TYPEINFO.length; ++i) {
            if (sqlTypeNum == TYPEINFO[i]._sqlTypeNum) {
                return TYPEINFO[i]._javaType;
            }
        }
        
        throw new MappingException("jdo.sqlTypeNotSupported", new Integer(sqlTypeNum));
    }

    /**
     * Returns the Java type for the given SQL type name.
     *
     * @param sqlTypeName SQL type name (e.g. numeric).
     * @return The suitable Java type.
     * @throws MappingException The SQL type is not recognized.
     */
    public static Class sqlTypeName2javaType(final String sqlTypeName)
    throws MappingException {
        for (int i = 0; i < TYPEINFO.length; ++i) {
            if (sqlTypeName.equals(TYPEINFO[i]._sqlTypeName)) {
                return TYPEINFO[i]._javaType;
            }
        }
        
        throw new MappingException("jdo.sqlTypeNotSupported", sqlTypeName);
    }

    /**
     * Returns the SQL type from the specified Java type. Returns <tt>OTHER</tt>
     * if the Java type has no suitable SQL type mapping.
     *
     * @param javaType The Java class of the SQL type.
     * @return SQL type from the specified Java type.
     */
    public static int javaType2sqlTypeNum(final Class javaType) {
        for (int i = 0; i < TYPEINFO.length; ++i) {
            if (javaType.isAssignableFrom(TYPEINFO[i]._javaType)) {
                return TYPEINFO[i]._sqlTypeNum;
            }
        }
        return Types.OTHER;
    }

    //-----------------------------------------------------------------------------------

    /**
     * Get value from given ResultSet at given index with given SQL type.
     * 
     * @param rs The ResultSet to get the value from.
     * @param index The index of the value in the ResultSet.
     * @param sqlType The SQL type of the value.
     * @return The value.
     * @throws SQLException If a database access error occurs.
     */
    public static Object getValue(final ResultSet rs, final int index, final int sqlType)
    throws SQLException {
        switch (sqlType) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return rs.getString(index);
        case Types.DECIMAL:
        case Types.NUMERIC:
            return rs.getBigDecimal(index);
        case Types.INTEGER:
            int intVal = rs.getInt(index);
            return (rs.wasNull() ? null : new Integer(intVal));
        case Types.TIME:
            return rs.getTime(index, getCalendar());
        case Types.DATE:
            return rs.getDate(index, getCalendar());
        case Types.TIMESTAMP:
            return rs.getTimestamp(index, getCalendar());
        case Types.FLOAT:
        case Types.DOUBLE:
            double doubleVal = rs.getDouble(index);
            return (rs.wasNull() ? null : new Double(doubleVal));
        case Types.REAL:
            float floatVal = rs.getFloat(index);
            return (rs.wasNull() ? null : new Float(floatVal));
        case Types.SMALLINT:
            short shortVal = rs.getShort(index);
            return (rs.wasNull() ? null : new Short(shortVal));
        case Types.TINYINT:
            byte byteVal = rs.getByte(index);
            return (rs.wasNull() ? null : new Byte(byteVal));
        case Types.LONGVARBINARY:
        case Types.VARBINARY:
        case Types.BINARY:
            return rs.getBytes(index);
        case Types.BLOB:
            Blob blob = rs.getBlob(index);
            return (blob == null ? null :  blob.getBinaryStream());
        case Types.CLOB:
            return rs.getClob(index);
        case Types.BIGINT:
            long longVal = rs.getLong(index);
            return (rs.wasNull() ? null : new Long(longVal));
        case Types.BIT:
            boolean boolVal = rs.getBoolean(index);
            return (rs.wasNull() ? null : new Boolean(boolVal));
        default:
            Object value = rs.getObject(index);
            return (rs.wasNull() ? null : value);
        }
    }

    /**
     * Set given value on given PreparedStatement at given index with given SQL type.
     * 
     * @param stmt The PreparedStatement to set value on.
     * @param index The index of the value in the PreparedStatement.
     * @param value The value to set.
     * @param sqlType The SQL type of the value.
     */
    public static void setValue(final PreparedStatement stmt, final int index,
                                final Object value, final int sqlType) {
        try {
            if (value == null) {
                stmt.setNull(index, sqlType);
            } else {
                // Special processing for BLOB and CLOB types, because they are mapped
                // by Castor to java.io.InputStream and java.io.Reader, respectively,
                // while JDBC driver expects java.sql.Blob and java.sql.Clob.
                switch (sqlType) {
                case Types.FLOAT:
                case Types.DOUBLE:
                    stmt.setDouble(index, ((Double) value).doubleValue());
                    break;
                case Types.REAL:
                    stmt.setFloat(index, ((Float) value).floatValue());
                    break;
                case Types.DATE:
                    stmt.setDate(index, (Date) value);
                    break;
                case Types.TIMESTAMP:
                    stmt.setTimestamp(index, (Timestamp) value);
                    break;
                case Types.BLOB:
                    try {
                        InputStream stream = (InputStream) value;
                        stmt.setBinaryStream(index, stream, stream.available());
                    } catch (IOException ex) {
                        throw new SQLException(ex.toString());
                    }
                    break;
                case Types.CLOB:
                    Clob clob = (Clob) value;
                    stmt.setCharacterStream(index, clob.getCharacterStream(),
                            (int) Math.min(clob.length(), Integer.MAX_VALUE));
                    break;
                default:
                    stmt.setObject(index, value, sqlType);
                    break;
                }
            }
        } catch (SQLException ex) {
            LOG.error("Unexpected SQL exception: ", ex);
        }
    }

    //-----------------------------------------------------------------------------------

    /**
     * Hide utility class constructor.
     */
    private SQLTypeInfos() { }
    
    //-----------------------------------------------------------------------------------
}
