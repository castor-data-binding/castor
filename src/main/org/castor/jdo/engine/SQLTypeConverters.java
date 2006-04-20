/*
 * Copyright 2005 Assaf Arkin, Bruce Snyder, Ralf Joachim
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.util.Base64Decoder;
import org.castor.util.Base64Encoder;
import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

import org.exolab.castor.jdo.engine.ClobImpl;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;

/**
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class SQLTypeConverters {
    //-----------------------------------------------------------------------------------

    /**
     * Abstract class to convert from one type to another.
     */
    public abstract static class Convertor implements TypeConvertor {
        /** The type being converted from. */
        private final Class _fromType;

        /** The type being converted to. */
        private final Class _toType;

        /**
         * Construct a Converter between given fromType an toType.
         * 
         * @param fromType The type being converted from.
         * @param toType The type being converted to.
         */
        public Convertor(final Class fromType, final Class toType) {
            _fromType = fromType;
            _toType = toType;
        }
        
        /**
         * Get the type being converted from.
         * 
         * @return The type being converted from.
         */
        public final Class fromType() { return _fromType; }
        
        /**
         * Get the type being converted to.
         * 
         * @return The type being converted to.
         */
        public final Class toType() { return _toType; }
        
        /**
         * {@inheritDoc}
         * @see org.exolab.castor.mapping.TypeConvertor
         *      #convert(java.lang.Object, java.lang.String)
         */
        public abstract Object convert(final Object obj, final String pm);

        /**
         * {@inheritDoc}
         * @see java.lang.Object#toString()
         */
        public final String toString() {
            return _fromType.getName() + "-->" + _toType.getName();
        }
    }

    //-----------------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLTypeConverters.class);
    
    /** Default pattern to convert java.sql.Timestamp to String and back. */
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /** Default pattern to convert Double and Date. */
    private static final String DECIMAL_PATTERN = "#################0";
    
    /** Default value of LOB buffer size if property could not been loaded. */
    private static final int DEFAULT_LOB_SIZE = 256;

    /** Constant used for calculation of time nanos. */
    private static final long THOUSAND = 1000;

    /** Constant used for calculation of time nanos. */
    private static final long MILLION = 1000000;

    /**
     * Date format used by the date convertor. Use the {@link #getDateFormat}
     * accessor to access this variable.
     * 
     * @see #getDefaultDateFormat()
     */
    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat();

    /**
     * Date format used by the date convertor when nonempty parameter
     * is specified. Use the {@link #getParamDateFormat} accessor to access
     * this variable.
     *
     * @see #getParamDateFormat()
     */
    private static final SimpleDateFormat PARAM_DATE_FORMAT = new SimpleDateFormat();

    /**
     * Date format used by the double->date convertor. Use the {@link #getDecimalFormat} 
     * accessor to access this variable.
     * 
     * @see #getDecimalFormat()
     */
    private static final DecimalFormat DECIMAL_FORMAT = 
        new DecimalFormat(DECIMAL_PATTERN);
    
    /** LOB buffer size. */
    private static int _lobBufferSize = -1;

    //-----------------------------------------------------------------------------------

    /**
     * Use this accessor to access the <tt>DEFAULT_DATE_FORMAT</tt>.
     * 
     * @return A clone of DEFAULT_DATE_FORMAT.
     */
    private static SimpleDateFormat getDefaultDateFormat() {
        return (SimpleDateFormat) DEFAULT_DATE_FORMAT.clone();
    }

    /**
     * Use this accessor to access the <tt>PARAM_DATE_FORMAT</tt>.
     * 
     * @return A clone of PARAM_DATE_FORMAT.
     */
    private static SimpleDateFormat getParamDateFormat() {
        return (SimpleDateFormat) PARAM_DATE_FORMAT.clone();
    }

    /**
     * Use this accessor to access the <tt>DECIMAL_FORMAT</tt>.
     * 
     * @return A clone of DECIMAL_FORMAT.
     */
    private static DecimalFormat getDecimalFormat() {
        return (DecimalFormat) DECIMAL_FORMAT.clone();
    }

    /**
     * Grabs the LOB buffer size from castor.properties.
     * 
     * @return The LOB buffer size.
     */
    private static int getLobBufferSize() {
        if (_lobBufferSize == -1) {
            Configuration config = Configuration.getInstance();
            _lobBufferSize = config.getProperty(
                    ConfigKeys.LOB_BUFFER_SIZE, DEFAULT_LOB_SIZE);
            if (LOG.isDebugEnabled()) { LOG.debug("Using lobSize: " + _lobBufferSize); }
        }
        return _lobBufferSize;
    }

    /**
     * Transforms short date format pattern into full format pattern
     * for SimpleDateFormat (e.g., "YMD" to "yyyyMMdd").
     *
     * @param pattern The short pattern.
     * @return The full pattern.
     */
    public static String getFullDatePattern(final String pattern) {
        int len = pattern.length();
        if ((pattern == null) || (len == 0)) {
            return "yyyyMMdd";
        }
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            switch (pattern.charAt(i)) {
            case 'y': case 'Y': sb.append("yyyy"); break;
            case 'M':           sb.append("MM");   break;
            case 'd': case 'D': sb.append("dd");   break;
            case 'h': case 'H': sb.append("HH");   break;
            case 'm':           sb.append("mm");   break;
            case 's':           sb.append("ss");   break;
            case 'S':           sb.append("SSS");  break;
            default:                               break;
            }
        }
        
        return sb.toString();
    }

    /**
     * Returns a type convertor. A type convertor can be used to convert
     * an object from Java type <tt>fromType</tt> to Java type <tt>toType</tt>.
     *
     * @param fromType The Java type to convert from.
     * @param toType The Java type to convert to.
     * @return The TypeConverter to convert fromType into toType.
     * @throws MappingException No suitable convertor was found.
     */
    public static TypeConvertor getConvertor(final Class fromType, final Class toType)
    throws MappingException {
        int len = _convertors.length;
        Convertor conv;

        // first seek for exact match
        for (int i = 0; i < len; ++i) {
            conv = _convertors[i];
            if (conv._fromType.equals(fromType)
                    && toType.equals(conv._toType)) {
                
                return conv;
            }
        }

        // else seek for any match
        for (int i = 0; i < len; ++i) {
            conv = _convertors[i];
            if (conv._fromType.isAssignableFrom(fromType)
                    && toType.isAssignableFrom(conv._toType)) {
                
                return conv;
            }
        }
        
        throw new MappingException("mapping.noConvertor",
                                   fromType.getName(), toType.getName());
    }

    //-----------------------------------------------------------------------------------

    /** List of all the default convertors between Java types. */
    private static Convertor[] _convertors = new Convertor[] {
        // Convertors to boolean
        new Convertor(BigDecimal.class, Boolean.class) {
            public Object convert(final Object obj, final String pm) {
                return new Boolean(((BigDecimal) obj).intValue() != 0);
            }
        },
        new Convertor(Integer.class, Boolean.class) {
            public Object convert(final Object obj, final String pm) {
                return new Boolean(((Integer) obj).intValue() != 0);
            }
        },
        new Convertor(Short.class, Boolean.class) {
            public Object convert(final Object obj, final String pm) {
                return new Boolean(((Short) obj).shortValue() != 0);
            }
        },
        new Convertor(String.class, Boolean.class) {
            public Object convert(final Object obj, final String pm) {
                char t = 'T';
                char f = 'F';
                if ((pm != null) && (pm.length() == 2)) {
                    t = Character.toUpperCase(pm.charAt(1));
                    f = Character.toUpperCase(pm.charAt(0));
                }
                
                if (((String) obj).length() == 1) {
                    char c = Character.toUpperCase(((String) obj).charAt(0));
                    if (c == t) { return Boolean.TRUE; }
                    if (c == f) { return Boolean.FALSE; }
                }
                
                throw new ClassCastException("Failed to convert string '" + obj
                        + "' to boolean because it didn't match the expected values '"
                        + t + "'/'" + f + "' (true/false).");
            }
        },
        
        // Convertors to integer
        new Convertor(BigDecimal.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                return new Integer(((BigDecimal) obj).intValue());
            }
        },
        new Convertor(Boolean.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                boolean val = ((Boolean) obj).booleanValue();
                if ((pm == null) || (pm.length() != 1) || (pm.charAt(0) != '-')) {
                    return new Integer(val ? 1 : 0);
                } else {
                    return new Integer(val ? -1 : 0);
                }
            }
        },
        new Convertor(Byte.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                return new Integer(((Byte) obj).intValue());
            }
        },
        new Convertor(Date.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                SimpleDateFormat paramDateFormat = getParamDateFormat();
                paramDateFormat.applyPattern(getFullDatePattern(pm));
                return new Integer(paramDateFormat.format((Date) obj));
            }
        },
        new Convertor(Double.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                return new Integer(((Double) obj).intValue());
            }
        },
        new Convertor(Float.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                return new Integer(((Float) obj).intValue());
            }
        },
        new Convertor(Long.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                return new Integer(((Long) obj).intValue());
            }
        },
        new Convertor(Short.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                return new Integer(((Short) obj).intValue());
            }
        },
        new Convertor(String.class, Integer.class) {
            public Object convert(final Object obj, final String pm) {
                return Integer.valueOf((String) obj);
            }
        },

        // Convertors to long
        new Convertor(BigDecimal.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
                return new Long(((BigDecimal) obj).longValue());
            }
        },
        new Convertor(Date.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
              return new Long(((Date) obj).getTime());
            }
        },
        new Convertor(Double.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
                return new Long(((Double) obj).longValue());
            }
        },
        new Convertor(Float.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
                return new Long(((Float) obj).longValue());
            }
        },
        new Convertor(Integer.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
                return new Long(((Integer) obj).longValue());
            }
        },
        new Convertor(Short.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
                return new Long(((Short) obj).longValue());
            }
        },
        new Convertor(String.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
                return Long.valueOf((String) obj);
            }
        },

        // Convertors to short
        new Convertor(BigDecimal.class, Short.class) {
            public Object convert(final Object obj, final String pm) {
                return new Short(((BigDecimal) obj).shortValue());
            }
        },
        new Convertor(Boolean.class, Short.class) {
            public Object convert(final Object obj, final String pm) {
                boolean val = ((Boolean) obj).booleanValue();
                if ((pm == null) || (pm.length() != 1) || (pm.charAt(0) != '-')) {
                    return new Short(val ? (byte) 1 : (byte) 0);
                } else {
                    return new Short(val ? (byte) -1 : (byte) 0);
                }
            }
        },
        new Convertor(Byte.class, Short.class) {
            public Object convert(final Object obj, final String pm) {
                return new Short(((Byte) obj).shortValue());
            }
        },
        new Convertor(Integer.class, Short.class) {
            public Object convert(final Object obj, final String pm) {
                return new Short(((Integer) obj).shortValue());
            }
        },
        new Convertor(Long.class, Short.class) {
            public Object convert(final Object obj, final String pm) {
                return new Short(((Long) obj).shortValue());
            }
        },
        new Convertor(String.class, Short.class) {
            public Object convert(final Object obj, final String pm) {
                return Short.valueOf((String) obj);
            }
        },

        // Convertors to byte
        new Convertor(BigDecimal.class, Byte.class) {
            public Object convert(final Object obj, final String pm) {
                return new Byte(((BigDecimal) obj).byteValue());
            }
        },
        new Convertor(Integer.class, Byte.class) {
            public Object convert(final Object obj, final String pm) {
                return new Byte(((Integer) obj).byteValue());
            }
        },
        new Convertor(Short.class, Byte.class) {
            public Object convert(final Object obj, final String pm) {
                return new Byte(((Short) obj).byteValue());
            }
        },

        // Convertors to double
        new Convertor(BigDecimal.class, Double.class) {
            public Object convert(final Object obj, final String pm) {
                return new Double(((BigDecimal) obj).doubleValue());
            }
        },
        new Convertor(Date.class, Double.class) {
            public Object convert(final Object obj, final String pm) {
                SimpleDateFormat paramDateFormat = getParamDateFormat();
                paramDateFormat.applyPattern(getFullDatePattern(pm));
                return new Double(paramDateFormat.format((Date) obj));
            }
        },
        new Convertor(Float.class, Double.class) {
            public Object convert(final Object obj, final String pm) {
                return new Double(((Float) obj).floatValue());
            }
        },
        new Convertor(Integer.class, Double.class) {
            public Object convert(final Object obj, final String pm) {
                return new Double(((Integer) obj).doubleValue());
            }
        },
        new Convertor(Long.class, Double.class) {
            public Object convert(final Object obj, final String pm) {
                return new Double(((Long) obj).doubleValue());
            }
        },
        new Convertor(String.class, Double.class) {
            public Object convert(final Object obj, final String pm) {
                return Double.valueOf((String) obj);
            }
        },

        // Convertors to float
        new Convertor(BigDecimal.class, Float.class) {
            public Object convert(final Object obj, final String pm) {
                return new Float(((BigDecimal) obj).floatValue());
            }
        },
        new Convertor(Double.class, Float.class) {
            public Object convert(final Object obj, final String pm) {
                return new Float(((Double) obj).floatValue());
            }
        },
        new Convertor(Integer.class, Float.class) {
            public Object convert(final Object obj, final String pm) {
                return new Float(((Integer) obj).floatValue());
            }
        },
        new Convertor(Long.class, Float.class) {
            public Object convert(final Object obj, final String pm) {
                return new Float(((Long) obj).floatValue());
            }
        },
        new Convertor(String.class, Float.class) {
            public Object convert(final Object obj, final String pm) {
                return Float.valueOf((String) obj);
            }
        },

        // Convertors to big decimal
        new Convertor(Boolean.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                boolean val = ((Boolean) obj).booleanValue();
                if ((pm == null) || (pm.length() != 1) || (pm.charAt(0) != '-')) {
                    return BigDecimal.valueOf(val ? 1 : 0);
                } else {
                    return BigDecimal.valueOf(val ? -1 : 0);
                }
            }
        },
        new Convertor(Byte.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                return BigDecimal.valueOf(((Byte) obj).byteValue());
            }
        },
        new Convertor(Date.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                SimpleDateFormat paramDateFormat = getParamDateFormat();
                paramDateFormat.applyPattern(getFullDatePattern(pm));
                return new BigDecimal(
                       new BigInteger(paramDateFormat.format((Date) obj)));
            }
        },
        new Convertor(Double.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                // Don't remove "toString" below! Otherwise the result is incorrect.
                return new BigDecimal(((Double) obj).toString());
            }
        },
        new Convertor(Float.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                // Don't remove "toString" below! Otherwise the result is incorrect.
                return new BigDecimal(((Float) obj).toString());
            }
        },
        new Convertor(Integer.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                return BigDecimal.valueOf(((Integer) obj).intValue());
            }
        },
        new Convertor(Long.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                return BigDecimal.valueOf(((Long) obj).longValue());
            }
        },
        new Convertor(Short.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                return BigDecimal.valueOf(((Short) obj).shortValue());
            }
        },
        new Convertor(String.class, BigDecimal.class) {
            public Object convert(final Object obj, final String pm) {
                return new BigDecimal((String) obj);
            }
        },

        // Convertors to string
        new Convertor(byte[].class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return new String(Base64Encoder.encode((byte[]) obj));
            }
        },
        new Convertor(char[].class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return new String((char[]) obj);
            }
        },
        new Convertor(Boolean.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                boolean val = ((Boolean) obj).booleanValue();
                if ((pm == null) || (pm.length() != 2)) {
                    return val ? "T" : "F";
                } else {
                    return val ? pm.substring(1, 2) : pm.substring(0, 1);
                }
            }
        },
        new Convertor(Character.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return obj.toString();
            }
        },
        new Convertor(Date.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                if ((pm == null) || (pm.length() == 0)) {
                    return obj.toString();
                } else {
                    SimpleDateFormat paramDateFormat = getParamDateFormat();
                    paramDateFormat.applyPattern(pm);
                    return paramDateFormat.format((Date) obj);
                }
            }
        },
        new Convertor(Double.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return obj.toString();
            }
        },
        new Convertor(Float.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return obj.toString();
            }
        },
        new Convertor(Integer.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return obj.toString();
            }
        },
        new Convertor(Long.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return obj.toString();
            }
        },
        new Convertor(Object.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return obj.toString();
            }
        },
        new Convertor(Short.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                return obj.toString();
            }
        },

        // Convertors to byte[], char[] and character
        new Convertor(String.class, byte[].class) {
            public Object convert(final Object obj, final String pm) {
                return Base64Decoder.decode((String ) obj);
            }
        },
        new Convertor(String.class, char[].class) {
            public Object convert(final Object obj, final String pm) {
                return ((String) obj).toCharArray();
            }
        },
        new Convertor(String.class, Character.class) {
            public Object convert(final Object obj, final String pm) {
                String str = (String ) obj;
                return (new Character((str.length() == 0) ? 0 : str.charAt(0)));
            }
        },

        // Convertors to date
        new Convertor(BigDecimal.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    SimpleDateFormat paramDateFormat = getParamDateFormat();
                    paramDateFormat.applyPattern(getFullDatePattern(pm));
                    return paramDateFormat.parse(obj.toString());
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },
        new Convertor(Double.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    SimpleDateFormat paramDateFormat = getParamDateFormat();
                    paramDateFormat.applyPattern(getFullDatePattern(pm));
                    return paramDateFormat.parse(getDecimalFormat().format(obj).trim());
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },
        new Convertor(Integer.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    SimpleDateFormat paramDateFormat = getParamDateFormat();
                    paramDateFormat.applyPattern(getFullDatePattern(pm));
                    return paramDateFormat.parse(obj.toString());
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },
        new Convertor(Long.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                return new Date(((Long) obj).longValue());
            }
        },
        new Convertor(String.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    if ((pm == null) || (pm.length() == 0)) {
                        return getDefaultDateFormat().parse((String) obj);
                    } else {
                        SimpleDateFormat paramDateFormat = getParamDateFormat();
                        paramDateFormat.applyPattern(pm);
                        return paramDateFormat.parse((String) obj);
                    }
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },

        // Convertors from and to java.sql.Date, java.sql.Time and java.sql.Timestamp
        new Convertor(Date.class, java.sql.Date.class) {
            public Object convert(final Object obj, final String pm) {
                return new java.sql.Date(((Date) obj).getTime());
            }
        },
        new Convertor(java.sql.Date.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                return obj;
            }
        },
        new Convertor(Date.class, java.sql.Time.class) {
            public Object convert(final Object obj, final String pm) {
                return new java.sql.Time(((Date) obj).getTime());
            }
        },
        new Convertor(java.sql.Time.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                return obj;
            }
        },
        new Convertor(Date.class, java.sql.Timestamp.class) {
            public Object convert(final Object obj, final String pm) {
                long time = ((Date) obj).getTime();
                
                java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
                timestamp.setNanos((int) ((time % THOUSAND) * MILLION));
                //timestamp.setNanos(0);  // this can workaround the bug in SAP DB
                return timestamp;
            }
        },
        new Convertor(java.sql.Timestamp.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                java.sql.Timestamp timestamp = (java.sql.Timestamp) obj;
                return new Date(timestamp.getTime());
            }
        },
        new Convertor(String.class, java.sql.Timestamp.class) {
            public Object convert(final Object obj, final String pm) {
                SimpleDateFormat paramDateFormat = getParamDateFormat();
                if ((pm == null) || (pm.length() ==  0)) {
                    paramDateFormat.applyPattern(TIMESTAMP_PATTERN);
                } else {
                    paramDateFormat.applyPattern(pm);
                }

                long time;
                try {
                    time = paramDateFormat.parse((String) obj).getTime();
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
                
                java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
                timestamp.setNanos((int) ((time % THOUSAND) * MILLION));
                //timestamp.setNanos(0);  // this can workaround the bug in SAP DB
                return timestamp;
            }
        },
        new Convertor(java.sql.Timestamp.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                SimpleDateFormat paramDateFormat = getParamDateFormat();
                if ((pm == null) || (pm.length() ==  0)) {
                    paramDateFormat.applyPattern(TIMESTAMP_PATTERN);
                } else {
                    paramDateFormat.applyPattern(pm);
                }
                
                java.sql.Timestamp timestamp = (java.sql.Timestamp) obj;
                return paramDateFormat.format(
                        new Date(timestamp.getTime() + timestamp.getNanos() / MILLION));
            }
        },

        // Convertors from and to InputStream
        new Convertor(byte[].class, InputStream.class) {
            public Object convert(final Object obj, final String pm) {
                return new ByteArrayInputStream((byte[]) obj);
            }
        },
        new Convertor(InputStream.class, byte[].class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    InputStream is = (InputStream) obj;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[getLobBufferSize()];
                    int len = 0;
                    while ((len = is.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }
                    return bos.toByteArray();
                } catch (IOException ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },

        // Convertors from and to java.sql.Clob
        new Convertor(char[].class, java.sql.Clob.class) {
            public Object convert(final Object obj, final String pm) {
                char[] chars = (char[]) obj;
                return new ClobImpl(new java.io.CharArrayReader(chars), chars.length);
            }
        },
        new Convertor(java.sql.Clob.class, char[].class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    java.io.Reader reader = ((java.sql.Clob) obj).getCharacterStream();
                    java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
                    char[] buffer = new char[getLobBufferSize()];
                    int len = 0;
                    while ((len = reader.read(buffer)) > 0) {
                        writer.write(buffer, 0, len);
                    }
                    return writer.toCharArray();
                } catch (Exception ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },
        new Convertor(String.class, java.sql.Clob.class) {
            public Object convert(final Object obj, final String pm) {
                String str = (String) obj;
                return new ClobImpl(new java.io.StringReader(str), str.length());
            }
        },
        new Convertor(java.sql.Clob.class, String.class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    java.io.Reader reader = ((java.sql.Clob) obj).getCharacterStream();
                    java.io.CharArrayWriter writer = new java.io.CharArrayWriter();
                    char[] buffer = new char[getLobBufferSize()];
                    int len = 0;
                    while ((len = reader.read(buffer)) > 0) {
                        writer.write(buffer, 0, len);
                    }
                    return writer.toString();
                } catch (Exception ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },

        // Convertors from and to org.exolab.castor.types
        new Convertor(Date.class, org.exolab.castor.types.Date.class) {
            public Object convert(final Object obj, final String pm) {
                return new org.exolab.castor.types.Date((Date) obj);
            }
        },
        new Convertor(org.exolab.castor.types.Date.class, Date.class) {
            public Object convert(final Object obj, final String pm) {
                return ((org.exolab.castor.types.Date) obj).toDate();
            }
        },
        new Convertor(java.sql.Date.class, org.exolab.castor.types.Date.class) {
            public Object convert(final Object obj, final String pm) {
                return new org.exolab.castor.types.Date((Date) obj);
            }
        },
        new Convertor(org.exolab.castor.types.Date.class, java.sql.Date.class) {
            public Object convert(final Object obj, final String pm) {
                long time = ((org.exolab.castor.types.Date) obj).toDate().getTime();
                return new java.sql.Date(time);
            }
        },
        new Convertor(Long.class, org.exolab.castor.types.Duration.class) {
            public Object convert(final Object obj, final String pm) {
                return new org.exolab.castor.types.Duration(((Long) obj).longValue());
            }
        },
        new Convertor(org.exolab.castor.types.Duration.class, Long.class) {
            public Object convert(final Object obj, final String pm) {
                return new Long(((org.exolab.castor.types.Duration) obj).toLong());
            }
        },

        // Convertors from and to Serializable
        // Because Serializable need the right ClassLoader when serializing, we
        // convert to byte[] and vice-versa.
        new Convertor(byte[].class, Serializable.class) {
            public Object convert(final Object obj, final String pm) {
                return obj;
            }
        },
        new Convertor(Serializable.class, byte[].class) {
            public Object convert(final Object obj, final String pm) {
                return obj;
            }
        },
        new Convertor(InputStream.class, Serializable.class) {
            public Object convert(final Object obj, final String pm) {
                try {
                    InputStream is = (InputStream) obj;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[getLobBufferSize()];
                    int len = 0;
                    while ((len = is.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }
                    return bos.toByteArray();
                } catch (IOException ex) {
                    throw new IllegalArgumentException(ex.toString());
                }
            }
        },
        new Convertor(Serializable.class, InputStream.class) {
            public Object convert(final Object obj, final String pm) {
                return new ByteArrayInputStream((byte[]) obj);
            }
        }
    };

//  -----------------------------------------------------------------------------------

  /**
   * Hide utility class constructor.
   */
  private SQLTypeConverters() { }

//  -----------------------------------------------------------------------------------
}
