/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote products derived
 * from this Software without prior written permission of Intalio, Inc. For
 * written permission, please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab" nor may
 * "Exolab" appear in their names without prior written permission of Intalio,
 * Inc. Exolab is a registered trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.handlers;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.types.DateTime;
import org.exolab.castor.xml.XMLFieldHandler;

/**
 * A specialized FieldHandler for the XML Schema Date/Time related types.
 *
 * @author <a href="kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-09 13:04:19 -0700 (Wed, 09 Feb
 *          2005) $
 */
public class DateFieldHandler extends XMLFieldHandler {

    /** The default length of the date string, used by the format method. */
    private static final byte         DEFAULT_DATE_LENGTH       = 25;
    /** The error message prefix. */
    private static final String       INVALID_DATE              = "Invalid dateTime format: ";
    /** The default parse options when none are specified. */
    private static final ParseOptions DEFAULT_PARSE_OPTIONS     = new ParseOptions();

    /** The local timezone offset from UTC. */
    private static TimeZone           _timezone                 = TimeZone.getDefault();
    /** A boolean to indicate that the TimeZone can be suppressed if the TimeZone
     * is equivalent to the "default" timezone. */
    private static boolean            _allowTimeZoneSuppression = false;
    /** if true, milliseconds should be suppressed upon formatting. */
    private static boolean            _suppressMillis           = false;

    /** The nested FieldHandler. */
    private final FieldHandler        _handler;
    /** The current set of parse options. */
    private ParseOptions              _options                  = new ParseOptions();
    /** A flag to indicate that java.sql.Date should be returned instead. */
    private boolean                   _useSQLDate               = false;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Creates a new DateFieldHandler using the given FieldHandler for
     * delegation.
     *
     * @param fieldHandler the fieldHandler for delegation.
     */
    public DateFieldHandler(final FieldHandler fieldHandler) {
        if (fieldHandler == null) {
            String err = "The FieldHandler argument passed to "
                    + "the constructor of DateFieldHandler must not be null.";
            throw new IllegalArgumentException(err);
        }
        _handler = fieldHandler;
    } // -- DateFieldHandler

    // ------------------/
    // - Public Methods -/
    // ------------------/

    /**
     * Returns the value of the field associated with this descriptor from the
     * given target object.
     *
     * @param target the object to get the value from
     * @return the value of the field associated with this descriptor from the
     *         given target object.
     */
    public Object getValue(final Object target) {
        Object val = _handler.getValue(target);
        if (val == null) {
            return val;
        }

        Object formatted = null;

        Class type = val.getClass();

        if (java.util.Date.class.isAssignableFrom(type)) {
            formatted = format((Date) val);
        } else if (type.isArray()) {
            int size = Array.getLength(val);
            String[] values = new String[size];
            for (int i = 0; i < size; i++) {
                values[i] = format(Array.get(val, i));
            }
            formatted = values;
        } else if (java.util.Enumeration.class.isAssignableFrom(type)) {
            Enumeration enumeration = (Enumeration) val;
            Vector values = new Vector();
            while (enumeration.hasMoreElements()) {
                values.addElement(format(enumeration.nextElement()));
            }
            String[] valuesArray = new String[values.size()];
            values.copyInto(valuesArray);
            formatted = valuesArray;
        } else {
            formatted = val.toString();
        }
        return formatted;
    } // -- getValue

    /**
     * Sets the value of the field associated with this descriptor.
     *
     * @param target the object in which to set the value
     * @param value the value of the field
     * @throws IllegalStateException if the value provided cannot be parsed into
     *         a legal date/time.
     */
    public void setValue(final Object target, final Object value)
                                        throws java.lang.IllegalStateException {
        Date date = null;

        if (value == null || value instanceof Date) {
            date = (Date) value;
        } else {
            try {
                date = parse(value.toString(), _options);
                // -- java.sql.Date?
                if (_useSQLDate && date != null) {
                    date = new java.sql.Date(date.getTime());
                }
            } catch (java.text.ParseException px) {
                // -- invalid dateTime
                throw new IllegalStateException(px.getMessage());
            }
        }

        _handler.setValue(target, date);
    } // -- setValue

    /**
     * Sets the value of the field to a default value.
     *
     * @param target The object
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatiable with the Java object
     */
    public void resetValue(final Object target) throws java.lang.IllegalStateException {
        _handler.resetValue(target);
    }

    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and cannot be
     *         instantiated
     */
    public Object newInstance(final Object parent) throws IllegalStateException {
        Object obj = _handler.newInstance(parent);
        if (obj == null) {
            obj = new Date();
        }
        return obj;
    } // -- newInstance

    /**
     * Returns true if the given object is an XMLFieldHandler that is equivalent
     * to the delegated handler. An equivalent XMLFieldHandler is an
     * XMLFieldHandler that is an instances of the same class.
     *
     * @param obj The object to compare against <code>this</code>
     * @return true if the given object is an XMLFieldHandler that is equivalent
     *         to this one.
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FieldHandler)) {
            return false;
        }
        return (_handler.getClass().isInstance(obj) || getClass().isInstance(obj));
    } // -- equals

    /**
     * Sets whether or not the time zone should always be displayed when
     * marshaling xsd:dateTime values. If true, then the time zone will not be
     * displayed if the time zone is the current local time zone.
     *
     * @param allowTimeZoneSuppression if true, the time zone will not be
     *        displayed if it is the current local time zone.
     */
    public static void setAllowTimeZoneSuppression(final boolean allowTimeZoneSuppression) {
        _allowTimeZoneSuppression = allowTimeZoneSuppression;
    } // -- setAlwaysUseUTCTime

    /**
     * Sets the default TimeZone used for comparing dates when marshaling
     * xsd:dateTime values using this handler. This is used when determining if
     * the timezone can be omitted when marshaling.
     *
     * Default is JVM default returned by TimeZone.getDefault()
     *
     * @param timeZone TimeZone to use instead of JVM default
     * @see #setAllowTimeZoneSuppression
     */
    public static void setDefaultTimeZone(final TimeZone timeZone) {
        if (timeZone == null) {
            // -- reset timezone to the default
            _timezone = TimeZone.getDefault();
        } else {
            _timezone = (TimeZone) timeZone.clone();
        }
    } // -- setDefaultTimeZone

    /**
     * Sets a flag indicating whether or not Milliseconds should be suppressed
     * upon formatting a dateTime as a String.
     *
     * @param suppressMillis a boolean when true indicates that millis should be
     *        suppressed during conversion of a dateTime to a String
     */
    public static void setSuppressMillis(final boolean suppressMillis) {
        _suppressMillis = suppressMillis;
    } // -- setAlwaysUseUTCTime

    /**
     * Specifies that this DateFieldHandler should use java.sql.Date when
     * creating new Date instances.
     *
     * @param useSQLDate a boolean that when true indicates that java.sql.Date
     *        should be used instead of java.util.Date.
     */
    public void setUseSQLDate(final boolean useSQLDate) {
        _useSQLDate = useSQLDate;
        _options._allowNoTime = _useSQLDate;
    } // -- setUseSQLDate

    // -------------------/
    // - Private Methods -/
    // -------------------/

    /**
     * Parses the given string, which must be in the following format:
     * <b>CCYY-MM-DDThh:mm:ss</b> or <b>CCYY-MM-DDThh:mm:ss.sss</b> where "CC"
     * represents the century, "YY" the year, "MM" the month and "DD" the day.
     * The letter "T" is the date/time separator and "hh", "mm", "ss" represent
     * hour, minute and second respectively.
     * <p>
     * CCYY represents the Year and each 'C' and 'Y' must be a digit from 0-9. A
     * minimum of 4 digits must be present.
     * <p>
     * MM represents the month and each 'M' must be a digit from 0-9, but
     * together "MM" must not represent a value greater than 12. "MM" must be 2
     * digits, use of leading zero is required for all values less than 10.
     * <p>
     * DD represents the day of the month and each 'D' must be a digit from 0-9.
     * DD must be 2 digits (use a leading zero if necessary) and must not be
     * greater than 31.
     * <p>
     * 'T' is the date/time separator and must exist!
     * <p>
     * hh represents the hour using 0-23. mm represents the minute using 0-59.
     * ss represents the second using 0-60. (60 for leap second) sss represents
     * the millisecond using 0-999.
     *
     * @param dateTime the string to convert to a Date
     * @return a new Date that represents the given string.
     * @throws ParseException when the given string does not conform to the
     *            above string.
     */
    protected static Date parse(final String dateTime) throws ParseException {
        return parse(dateTime, DEFAULT_PARSE_OPTIONS);
    } // -- parse

    /**
     * Parses the given string, which must be in the following format:
     * <b>CCYY-MM-DDThh:mm:ss</b> or <b>CCYY-MM-DDThh:mm:ss.sss</b> where "CC"
     * represents the century, "YY" the year, "MM" the month and "DD" the day.
     * The letter "T" is the date/time separator and "hh", "mm", "ss" represent
     * hour, minute and second respectively.
     * <p>
     * CCYY represents the Year and each 'C' and 'Y' must be a digit from 0-9. A
     * minimum of 4 digits must be present.
     * <p>
     * MM represents the month and each 'M' must be a digit from 0-9, but
     * together "MM" must not represent a value greater than 12. "MM" must be 2
     * digits, use of leading zero is required for all values less than 10.
     * <p>
     * DD represents the day of the month and each 'D' must be a digit from 0-9.
     * DD must be 2 digits (use a leading zero if necessary) and must not be
     * greater than 31.
     * <p>
     * 'T' is the date/time separator and must exist!
     * <p>
     * hh represents the hour using 0-23. mm represents the minute using 0-59.
     * ss represents the second using 0-60. (60 for leap second) sss represents
     * the millisecond using 0-999.
     *
     * @param dateTime the string to convert to a Date
     * @param options the parsing options to use
     * @return a new Date that represents the given string.
     * @throws ParseException when the given string does not conform to the
     *            above string.
     */
    protected static Date parse(final String dateTime, final ParseOptions options)
                                                         throws ParseException {
        if (dateTime == null) {
            throw new ParseException(INVALID_DATE + "null", 0);
        }

        ParseOptions pOptions = (options != null) ? options : DEFAULT_PARSE_OPTIONS;

        String trimmed = dateTime.trim();

        // If no time is present and we don't require time, use org.exolab.castor.types.Date
        if (pOptions._allowNoTime && trimmed.indexOf('T') == -1) {
            org.exolab.castor.types.Date parsedDate = new org.exolab.castor.types.Date(trimmed);
            return parsedDate.toDate();
        }

        DateTime parsedDateTime = new DateTime(trimmed);
        return parsedDateTime.toDate();
    } // -- parse

    /**
     * Returns the given date in a String format, using the ISO8601 format as
     * specified in the W3C XML Schema 1.0 Recommendation (Part 2: Datatypes)
     * for dateTime.
     *
     * @param date the Date to format
     * @return the formatted string
     */
    protected static String format(final Date date) {
        final SimpleDateFormat formatter;
        if (_suppressMillis) {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        }

        /* ensure the formatter does not use the default system timezone */
        formatter.setTimeZone(_timezone);
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.setTimeZone(_timezone);

        StringBuffer buffer = new StringBuffer(DEFAULT_DATE_LENGTH);
        if (cal.get(Calendar.ERA) == GregorianCalendar.BC) {
            buffer.append("-");
        }

        buffer.append(formatter.format(date));
        formatTimeZone(cal, buffer);
        return buffer.toString();
    } // -- format

    /**
     * Format the time zone information (only) from the provided Calendar.
     * @param cal a calendar containing a time and time zone
     * @param buffer the StringBuffer to which to format the time zone
     */
    private static void formatTimeZone(final Calendar cal, final StringBuffer buffer) {
        int value = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);

        if (value == 0 && dstOffset == 0) {
            buffer.append('Z'); // UTC
            return;
        }

        if (_allowTimeZoneSuppression && value == _timezone.getRawOffset()) {
            return;
        }

        // -- adjust for Daylight Savings Time
        value = value + dstOffset;

        if (value > 0) {
            buffer.append('+');
        } else {
            value = -value;
            buffer.append('-');
        }

        // -- convert to minutes from milliseconds
        int minutes = value / 60000;

        // -- hours: hh
        value = minutes / 60;
        if (value < 10) {
            buffer.append('0');
        }
        buffer.append(value);
        buffer.append(':');

        // -- remaining minutes: mm
        value = minutes % 60;
        if (value < 10) {
            buffer.append('0');
        }
        buffer.append(value);
    }

    /**
     * Formats the given object. If the object is a java.util.Date, it will be
     * formatted by a call to {@link #format(Date)}, otherwise the toString()
     * method is called on the object.
     * @param object The object to be formatted
     *
     * @return the formatted object.
     */
    private static String format(final Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof java.util.Date) {
            return format((Date) object);
        }
        return object.toString();
    } //-- format

    /**
     * A class for controlling the parse options.  There is currently only one
     * parse option.
     */
    static class ParseOptions {
        /** If true and the 'T' field is not present, a xsd:date is parsed, else xsd:dateTime. */
        public boolean _allowNoTime = false;
    }

} //-- DateFieldHandler
