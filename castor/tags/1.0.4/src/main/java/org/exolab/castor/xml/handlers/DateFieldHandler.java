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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.handlers;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.xml.XMLFieldHandler;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

/**
 * A specialized FieldHandler for the XML Schema Date/Time related types
 * 
 * @author <a href="kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-09 13:04:19 -0700 (Wed, 09 Feb 2005) $
 */
public class DateFieldHandler extends XMLFieldHandler {


    /** 
     * The default length of the date string, used by
     * the format method
     */
    private static final byte DEFAULT_DATE_LENGTH = 23;
    
    
    /**
     * The error message prefix
     */
    private static final String INVALID_DATE = "Invalid dateTime format: ";
    
    
    // The various flags used when parsing dates
    private static final byte START_FLAG    = -1;
    private static final byte YEAR_FLAG     = START_FLAG    +   1; 
    private static final byte MONTH_FLAG    = YEAR_FLAG     +   1; 
    private static final byte DAY_FLAG      = MONTH_FLAG    +   1; 
    private static final byte HOURS_FLAG    = DAY_FLAG      +   1; 
    private static final byte MINUTES_FLAG  = HOURS_FLAG    +   1; 
    private static final byte SECONDS_FLAG  = MINUTES_FLAG  +   1; 
    private static final byte MILLIS_FLAG   = SECONDS_FLAG  +   1;
    
    
    /**
     * The default parse options when none are specified
     */
    private static final ParseOptions DEFAULT_PARSE_OPTIONS = new ParseOptions();
    
    /** 
     * The string name for the UTC TimeZone
     */
    private static final String UTC_TIMEZONE = "UTC";
    
    /**
     * The local timezone offset from UTC
     */
    private static TimeZone TIMEZONE = TimeZone.getDefault();
    
    /**
     * UTC TimeZone instance 
     */
	private static final TimeZone UTC_TIMEZONE_INSTANCE = TimeZone.getTimeZone( UTC_TIMEZONE );    
    
    
    /**
     * A boolean to indicate that the TimeZone can be suppressed
     * if the TimeZone is equivalent to the "default" timezone.
     */
    private static boolean _allowTimeZoneSuppression = false;
    
    /**
     * The nested FieldHandler
     */
    private FieldHandler _handler = null;
    
    /**
     * The current set of parse options
     */
    private ParseOptions _options = new ParseOptions();
    
    /**
     * A boolean to indicate milliseconds should be
     * suppressed upon formatting to a String
     * (marshalling)
     */
    private static boolean _suppressMillis = false;
    
    
    /**
     * A flag to indicate that java.sql.Date should
     * be returned instead
     */
    private boolean _useSQLDate = false;
    
    
    
    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new DateFieldHandler using the given
     * FieldHandler for delegation.
     * @param fieldHandler the fieldHandler for delegation.
    **/
    public DateFieldHandler(FieldHandler fieldHandler) {

        if (fieldHandler == null) {
            String err = "The FieldHandler argument passed to " +
                "the constructor of DateFieldHandler must not be null.";
            throw new IllegalArgumentException(err);
        }
        _handler = fieldHandler;
    } //-- DateFieldHandler
    
    //------------------/
    //- Public Methods -/
    //------------------/


    /**
     * Returns the value of the field associated with this
     * descriptor from the given target object.
     * @param target the object to get the value from
     * @return the value of the field associated with this
     * descriptor from the given target object.
    **/
    public Object getValue(Object target)
        throws java.lang.IllegalStateException
    {

        Object val = _handler.getValue(target);

        if (val == null) return val;

        Object formatted = null;

        Class type = val.getClass();
        
        if (java.util.Date.class.isAssignableFrom(type)) {
            formatted = format((Date)val);
        }
        else if (type.isArray()) {
            int size = Array.getLength(val);
            String[] values = new String[ size ];
            for (int i = 0; i < size; i++) {
                values[i] = format(Array.get(val, i));
            }
            formatted = values;
        }
        else if (java.util.Enumeration.class.isAssignableFrom(type)) {
            Enumeration enumeration = (Enumeration)val;
            Vector values = new Vector();
            while (enumeration.hasMoreElements()) {
                values.addElement(format(enumeration.nextElement()));
            }
            String[] valuesArray = new String[values.size()];
            values.copyInto(valuesArray);
            formatted = valuesArray;
        }
        else {
            formatted = val.toString();
        }
        return formatted;
    } //-- getValue

    /**
     * Sets the value of the field associated with this descriptor.
     * @param target the object in which to set the value
     * @param value the value of the field
    **/
    public void setValue(Object target, Object value)
        throws java.lang.IllegalStateException
    {
        Date date = null;
        
        if ( (value != null) && ! (value instanceof Date) ) {

            try {
                date = parse(value.toString(), _options);
                //-- java.sql.Date?
                if ((_useSQLDate) && (date != null)) {
                    date = new java.sql.Date(date.getTime());
                }
            }
            catch (java.text.ParseException px) {
                //-- invalid dateTime
                throw new IllegalStateException(px.getMessage());
            }
        }
        else date = (Date)value;

        _handler.setValue(target, date);

    } //-- setValue

    public void resetValue(Object target)
        throws java.lang.IllegalStateException
    {
        _handler.resetValue(target);
    }


    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        Object obj = _handler.newInstance( parent );
        if (obj == null) obj = new Date();
        return obj;
    } //-- newInstance

    /**
     * Returns true if the given object is an XMLFieldHandler that
     * is equivalent to the delegated handler. An equivalent XMLFieldHandler
     * is an XMLFieldHandler that is an instances of the same class.
     *
     * @return true if the given object is an XMLFieldHandler that
     * is equivalent to this one.
    **/
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof FieldHandler)) return false;
        return (_handler.getClass().isInstance(obj) ||
                getClass().isInstance(obj));
    } //-- equals


    /**
     * Sets whether or not UTC time should always be used when
     * marshalling out xsd:dateTime values.
     */
    public static void setAllowTimeZoneSuppression(boolean allowTimeZoneSuppression) {
        _allowTimeZoneSuppression = allowTimeZoneSuppression;
    } //-- setAlwaysUseUTCTime

    /**
     * Sets the default TimeZone used for comparing dates when marshalling out 
     * xsd:dateTime values using this handler. This is used when determining
     * if the timezone can be omitted when marshalling.
     *
     * Default is JVM default returned by TimeZone.getDefault()
     * @param timeZone TimeZone to use instead of JVM default
     * @see #setAllowTimeZoneSuppression
     */
    public static void setDefaultTimeZone(TimeZone timeZone)
    {
        if (timeZone == null) {
            //-- reset timezone to the default
            TIMEZONE = TimeZone.getDefault();
        }
        else {
            TIMEZONE = (TimeZone)timeZone.clone();
        }
    } //-- setDefaultTimeZone
    
    /**
     * Sets a flag indicating whether or not Milliseconds should
     * be suppressed upon formatting a dateTime as a String
     *
     * @param suppressMillis a boolean when true indicates that millis
     * should be suppressed during conversion of a dateTime to a String
     */
    public static void setSuppressMillis(boolean suppressMillis) {
        _suppressMillis = suppressMillis;
    } //-- setAlwaysUseUTCTime
    
    
    /**
     * Specifies that this DateFieldHandler should use
     * java.sql.Date when creating new Date instances.
     *
     * @param useSQLDate a boolean that when true indicates
     * that java.sql.Date should be used instead of java.util.Date.
     */
    public void setUseSQLDate(boolean useSQLDate) {
        _useSQLDate = useSQLDate;
        _options.allowNoTime = (_useSQLDate);
    } //-- setUseSQLDate
    

    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    
    /**
     * Parses the given string, which must be in the following format:
     * <b>CCYY-MM-DDThh:mm:ss</b> or <b>CCYY-MM-DDThh:mm:ss.sss</b>
     * where "CC" represents the century, "YY" the year, "MM" the 
     * month and "DD" the day. The letter "T" is the date/time 
     * separator and "hh", "mm", "ss" represent hour, minute and 
     * second respectively.
     * 
     * CCYY represents the Year and each 'C' and 'Y' must be a digit 
     * from 0-9. A minimum of 4 digits must be present.
     * 
     * MM represents the month and each 'M' must be a digit from 0-9, 
     * but together "MM" must not represent a value greater than 12.
     * "MM" must be 2 digits, use of leading zero is required for
     * all values less than 10.
     * 
     * DD represents the day of the month and each 'D' must be a digit 
     * from 0-9. DD must be 2 digits (use a leading zero if necessary) 
     * and must not be greater than 31.
     *
     * 'T' is the date/time separator and must exist!
     *
     * hh represents the hour using 0-23. 
     * mm represents the minute using 0-59.
     * ss represents the second using 0-60. (60 for leap second)
     * sss represents the millisecond using 0-999.
     * 
     * @param dateTime the string to convert to a Date
     * @return a new Date that represents the given string.
     * @exception ParseException when the given string does not conform
     * to the above string.
     */
    protected static Date parse( String dateTime ) 
            throws ParseException 
    {
        return parse( dateTime, DEFAULT_PARSE_OPTIONS);
    } //-- parse
                
                
    /**
     * Parses the given string, which must be in the following format:
     * <b>CCYY-MM-DDThh:mm:ss</b> or <b>CCYY-MM-DDThh:mm:ss.sss</b>
     * where "CC" represents the century, "YY" the year, "MM" the 
     * month and "DD" the day. The letter "T" is the date/time 
     * separator and "hh", "mm", "ss" represent hour, minute and 
     * second respectively.
     * 
     * CCYY represents the Year and each 'C' and 'Y' must be a digit 
     * from 0-9. A minimum of 4 digits must be present.
     * 
     * MM represents the month and each 'M' must be a digit from 0-9, 
     * but together "MM" must not represent a value greater than 12.
     * "MM" must be 2 digits, use of leading zero is required for
     * all values less than 10.
     * 
     * DD represents the day of the month and each 'D' must be a digit 
     * from 0-9. DD must be 2 digits (use a leading zero if necessary) 
     * and must not be greater than 31.
     *
     * 'T' is the date/time separator and must exist!
     *
     * hh represents the hour using 0-23. 
     * mm represents the minute using 0-59.
     * ss represents the second using 0-60. (60 for leap second)
     * sss represents the millisecond using 0-999.
     * 
     * @param dateTime the string to convert to a Date
     * @return a new Date that represents the given string.
     * @exception ParseException when the given string does not conform
     * to the above string.
     */
    protected static Date parse( String dateTime, ParseOptions options ) 
            throws ParseException 
    {
                
                
        if (dateTime == null)
            throw new ParseException(INVALID_DATE + "null", 0);
            
        if (options == null) options = DEFAULT_PARSE_OPTIONS;
            
        int values[] = new int[7];
        
        byte flags          = START_FLAG;
        byte sign           = 1;
        int value           = 0;
        int count           = 0;
        boolean delimiter   = true;
        char[] chars = dateTime.toCharArray();
        int i   = 0;
        boolean timezone    = false;
        int length = chars.length;
        
        //-- remove leading whitspace
        while (i < length) {
            boolean isWhitespace = false;
            switch(chars[i]) {
                case ' ':
                case '\r':
                case '\t':
                case '\n':
                    isWhitespace = true;
                    break;
                default:
                    break;
            }
            if (!isWhitespace)
                break;
            ++i;
        }
        
        //-- remove trailing whitespace
        while ((length-1) > i) {
            boolean isWhitespace = false;
            switch(chars[length-1]) {
                case ' ':
                case '\r':
                case '\t':
                case '\n':
                    isWhitespace = true;
                    break;
                default:
                    break;
            }
            if (!isWhitespace)
                break;
            --length;            
        }
        
        for ( ; i < length; i++) {
            char ch = chars[i];
            switch(ch) {
                case '-':
                    delimiter = true;
                    switch(flags) {
                        case START_FLAG:
                            sign = -1;
                            break;
                        case YEAR_FLAG:
                            if (value == 0) {
                                String err = INVALID_DATE + dateTime + 
                                    "; Year must be greater than 0";
                                throw new ParseException(err, i);
                            }
                            break;
                        case MONTH_FLAG:
                            value = value-1;
                            break;
                        case MILLIS_FLAG:
                        case SECONDS_FLAG:
                            timezone = true;
                            delimiter = false;
                            break;
                        default:
                            throw new ParseException(INVALID_DATE + dateTime, i);
                    }
                    break;
                case 'T':
                    delimiter = true;
                    if (flags != DAY_FLAG)
                        throw new ParseException(INVALID_DATE + dateTime, i);
                    break;
                case ':':
                    delimiter = true;
                    switch(flags) {
                        case HOURS_FLAG:
                        case MINUTES_FLAG:
                            break;
                        default:
                            throw new ParseException(INVALID_DATE + dateTime, i);
                    }
                    break;
                case '.':
                    delimiter = true;
                    if (flags != SECONDS_FLAG)
                        throw new ParseException(INVALID_DATE + dateTime, i);
                    break;
                case '+':
                case 'Z':
                    switch(flags) {
                        case SECONDS_FLAG:
                        case MILLIS_FLAG:
                            break;
                        default:
                            throw new ParseException(INVALID_DATE + dateTime, i);
                    }
                    timezone = true;
                    break;
                default:
                    delimiter = false;
                    if (flags == START_FLAG) flags = YEAR_FLAG;
                    if ((ch >= '0') && (ch <= '9')) {
                        ++count; 
                        if ((count > 3) && (flags == MILLIS_FLAG)) {
                            // save additional fractional seconds?
                        }
                        else value = (value*10) + Character.digit(ch,10);
                    }
                    else {
                        throw new ParseException(INVALID_DATE + dateTime, i);
                    }
                    break;
            }
            if (delimiter) {
                if (flags != START_FLAG) {
                    if (flags == YEAR_FLAG) value = value * sign;
                    values[flags] = value;
                }
                ++flags;
                value = 0;
                count = 0;
            }
            if (timezone) break;
        }
        
        Calendar cal = new GregorianCalendar(values[YEAR_FLAG],
                                             values[MONTH_FLAG], 
                                             values[DAY_FLAG],
                                             values[HOURS_FLAG],
                                             values[MINUTES_FLAG],
                                             values[SECONDS_FLAG]);
        
        //-- Set Seconds (if no '.') or Milliseconds 
        //-- Otherwise report error
        switch(flags) {
            case SECONDS_FLAG:
                cal.set(Calendar.SECOND, value);
                break;
            case MILLIS_FLAG:
                //-- adjust milliseconds
                while (count < 3) {
                	value = value*10;
                    ++count;
                }
                cal.set(Calendar.MILLISECOND, value);
                break;
            case DAY_FLAG:
                if (options.allowNoTime) {
                    break;
                }
                //-- do not break here (intentional)
            default:
                throw new ParseException(INVALID_DATE + dateTime, i);
        }
        
        
        //-- Handle TimeZone
        if (timezone) {
            TimeZone tz = null;
            char designator = chars[i++];
            if(designator == 'Z')
            {
            	//-- Use static for UTC as TimeZone.getTimeZone is very expensive (requires fileio)
            	tz = UTC_TIMEZONE_INSTANCE;
            }
            else
            {
                //-- Clone static for UTC as TimeZone.getTimeZone is very expensive (requires fileio)
                tz = (TimeZone)UTC_TIMEZONE_INSTANCE.clone();
	            flags = HOURS_FLAG;
	            int millis = 0;
	            count = 0;
	            value = 0;
	            for (; i < length; i++) {
	                char ch = chars[i];
	                switch(ch) {
	                    case ':':
	                        if ((count != 2) || (flags != HOURS_FLAG)) {
	                            String err = INVALID_DATE + dateTime + 
	                                "; TimeZone offset must be in the format 'hh:mm'";
	                            throw new ParseException(err, i);
	                        }
	                        //-- convert hours to milliseconds
	                        millis = value * 3600000; // 3600000 = (60 * 60 * 1000)
	                        count = 0;
	                        value = 0;
	                        ++flags;
	                        break;
	                    default:
	                        if ((ch >= '0') && (ch <= '9')) {
	                            if (count == 2) {
	                                //-- Be friendly to timezone without ':' between
	                                //-- hours and minutes such as -0500, which occur
	                                //-- frequently
	                                //-- convert hours to milliseconds
	                                millis = value * 3600000; // 3600000 = (60 * 60 * 1000)
	                                count = 0;
	                                value = 0;
	                                ++flags;
	                            }
	                            ++count; //-- keep track of number of digits
	                            value = (value*10) + Character.digit(ch,10);
	                        }
	                        else {
	                            throw new ParseException("Unparseable date: " + dateTime, i);
	                        }
	                        break;
	                }
	            }
	            if (flags == MINUTES_FLAG) {
	                if (count != 2) {
	                    String err = INVALID_DATE + dateTime + 
	                        "; TimeZone offset must be in the format 'hh:mm'";
	                    throw new ParseException(err, i);
	                }
	                millis = millis + (value * 60000);
	                if (designator == '-') millis = 0 - millis;
	                tz.setRawOffset(millis);
	            }
	            else if (designator != 'Z') {
	                String err = INVALID_DATE + dateTime + 
	                    "; TimeZone offset must be in the format 'hh:mm'";
	                throw new ParseException(err, i);
	            }
			}
            cal.setTimeZone(tz);
        }
        else {
            cal.setTimeZone((TimeZone)TIMEZONE.clone());
        }
        
        return cal.getTime();
    } //-- parse

    /** 
     * Returns the given date in a String format, using the
     * ISO8601 format as specified in the W3C XML Schema 1.0
     * Recommendation (Part 2: Datatypes) for dataTime.
     *
     * @param date the Date to format
     * @return the formatted string
     */
    protected static String format( Date date ) {

        StringBuffer buffer = null;
        //-- Year: CCYY
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        cal.setTimeZone(TIMEZONE);
        
        
        int value   = cal.get(Calendar.YEAR);
        //-- adjust year for ERA (BC or AD)
        if (cal.get(Calendar.ERA) == GregorianCalendar.BC) {
            value = 1-value;
        }
        
        if (value < 0) { //-- adjust for sign (BC)
            buffer = new StringBuffer(DEFAULT_DATE_LENGTH+1);
            buffer.append('-');
            value = -1*value;
        }
        else if (value > 9999) {
            buffer = new StringBuffer(DEFAULT_DATE_LENGTH+2);
        }
        else {
            buffer = new StringBuffer(DEFAULT_DATE_LENGTH);
        }
        
        //-- pad year to 4 digits if necessary
        for (int tmp = 1000; value < tmp; tmp = tmp / 10)
            buffer.append('0');
        
        buffer.append(value);
        
        //-- Year/Month Separator
        buffer.append('-');
        
        //-- Month: MM
        value = cal.get(Calendar.MONTH) + 1;
        if (value < 10) buffer.append('0');
        buffer.append(value);
        
        //-- Month/Day Separator
        buffer.append('-');
        
        //-- Day of Month: DD
        value = cal.get(Calendar.DAY_OF_MONTH);
        if (value < 10) buffer.append('0');
        buffer.append(value);
        
        //-- Date/Time Separator
        buffer.append('T');
        
        //-- Hours: hh
        value = cal.get(Calendar.HOUR_OF_DAY);
        if (value < 10) buffer.append('0');
        buffer.append(value);
        
        //-- Hours/Minutes Separator
        buffer.append(':');
        
        //-- Minutes: mm
        value = cal.get(Calendar.MINUTE);
        if (value < 10) buffer.append('0');
        buffer.append(value);
        
        //-- Minutes/Seconds Separator
        buffer.append(':');
        
        //-- Seconds: ss
        value = cal.get(Calendar.SECOND);
        if (value < 10) buffer.append('0');
        buffer.append(value);
        
        //-- Milliseconds
        if (!_suppressMillis) {
            buffer.append('.');
            value = cal.get(Calendar.MILLISECOND);
            for (int tmp = 100; value < tmp; tmp = tmp / 10)
                buffer.append(0);
            if (value > 0) 
                buffer.append(value);
        }
        
        //-- TimeZone
        value = cal.get(Calendar.ZONE_OFFSET);
        if ((value == 0) && (cal.get(Calendar.DST_OFFSET) == 0)) {
            buffer.append('Z'); // UTC
        }
        else {
            boolean useTimeZoneOffset = true;
            if (_allowTimeZoneSuppression) {
                useTimeZoneOffset = (value != TIMEZONE.getRawOffset());
            }
            
            if (useTimeZoneOffset) {
                //-- adjust for Daylight Savings Time
                value = value + cal.get(Calendar.DST_OFFSET);
                
                if (value > 0) 
                    buffer.append('+');
                else {
                    value = 0-value;
                    buffer.append('-');
                }
                
                //-- convert to minutes from milliseconds
                int minutes = value / 60000;
                
                //-- hours: hh
                value = minutes / 60;
                if (value < 10) buffer.append('0');
                buffer.append(value);
                buffer.append(':');
                
                //-- remaining minutes: mm
                value = minutes % 60;
                if (value < 10) buffer.append('0');
                buffer.append(value);
            }
        }
        return buffer.toString();
    } //-- format
    
    
    /* For Testing 
    public static void main(String[] args) {
        
        String[] dates = new String[] {                
            "2004-01-01T00:00:00.1", 
            "2004-01-01T00:00:00.01", 
            "2004-01-01T00:00:00.001",
            "-0004-01-01T00:00:00.001"
        };
        
        for (int i = 0; i < dates.length; i++) {
        	System.out.println("parsing: " + dates[i]);
            System.out.print("parse-result: ");
            try {
                System.out.println(format(parse(dates[i])));
            }
            catch(Exception ex) {
            	System.out.println(ex.getMessage());
            }
        }
    } //-- main
    /* */

    /**
     * Formats the given object. If the object is a Date, a call
     * to #getFormattedDate will be made, otherwise the toString()
     * method is called on the object.
     *
     * @return the formatted object.
     */
    private static String format(Object object) {
        if (object == null) 
            return null;
        if (object instanceof java.util.Date) {
            return format((Date)object);
        }
        return object.toString();
    } //-- format
     
    /**
     * A class for controlling the parse options
     */
    static class ParseOptions {
        public boolean allowNoTime = false;
    }
} //-- DateFieldHandler
