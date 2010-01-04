/*
 * Copyright 2006 Edward Kuns
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
 *
 * $Id:  $
 */
package org.exolab.castor.types;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Describe an XML schema DateTime.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>(-)CCYY-MM-DD'T'HH:MM:SS(.SSSSS)(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $
 */
public class DateTime extends DateTimeBase {
    /** SerialVersionUID. */
    private static final long serialVersionUID = 6278590966410879734L;
    /** Complaint string. */
    private static final String BAD_DATE = "Bad DateTime format: ";

    /**
     * Default constructor.
     */
    public DateTime() {
        // Nothing for the default
    }

    /**
     * Constructs a XML Schema DateTime instance given all the values of the
     * different date and time (but not time zone) fields.
     * <p>
     * By default a DateTime is not UTC, and is local. To set a timezone, you
     * need to separately call {@link #setZone(short, short)}.
     *
     * @param values
     *            an array of shorts that represent the different fields of
     *            Time.
     * @see #setValues
     */
    public DateTime(short[] values) {
        setValues(values);
    }

    /**
     * Creates a new XML Schema DateTime instance from a long that represents a
     * Date.  No time zone information is set.
     * <p>
     * By default a DateTime is not UTC, and is local. To set a timezone, you
     * need to separately call {@link #setZone(short, short)}.
     *
     * @param dateAsLong
     *            java.util.Date represented as a long.
     */
    public DateTime(long dateAsLong) {
        this(new java.util.Date(dateAsLong));
    }

    /**
     * Creates a new XML Schema DateTime instance from a java.util.Date. No time
     * zone information is set.
     * <p>
     * By default a DateTime is not UTC, and is local. To set a timezone, you
     * need to separately call {@link #setZone(short, short)}.
     *
     * @param dateRef
     *            a java.util.Date to convert.
     */
    public DateTime(java.util.Date dateRef) {
        GregorianCalendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTime(dateRef);

        setCentury((short) (tempCalendar.get(Calendar.YEAR) / 100));
        setYear((short) (tempCalendar.get(Calendar.YEAR) % 100));
        // In GregorianCalendar, 0 <= Month <= 11; January == 0
        setMonth((short) (tempCalendar.get(Calendar.MONTH) + 1));
        setDay((short) tempCalendar.get(Calendar.DAY_OF_MONTH));

        setHour((short) tempCalendar.get(Calendar.HOUR_OF_DAY));
        setMinute((short) tempCalendar.get(Calendar.MINUTE));
        setSecond((short) tempCalendar.get(Calendar.SECOND), (short) tempCalendar.get(Calendar.MILLISECOND));
    }

    /**
     * Constructs a DateTime from a String. The String is expected to be in W3C
     * Schema DateTime format.
     *
     * @param date
     *            the string representing the date
     * @throws java.text.ParseException
     *             if we are passed an illegal value
     */
    public DateTime(String date) throws java.text.ParseException {
        parseDateTimeInternal(date, this);
    }

    /**
     * Sets all the fields to the values provided in an Array. The Array must
     * be at least eight entries long.  Extra entries are ignored.  The order of
     * entries in the array is as follows:
     * <ul>
     * <li>century</li>
     * <li>year</li>
     * <li>month</li>
     * <li>day</li>
     * <li>hour</li>
     * <li>minute</li>
     * <li>second</li>
     * <li>millisecond</li>
     * </ul>
     * If a Time Zone is to be specified, it has to be set separately by using
     * {@link DateTimeBase#setZone(short, short) setZone}.  A time zone
     * previously set will not be cleared.
     *
     * @param values
     *            An array of shorts containing the values for the DateTime
     */
     public void setValues(short[] values) {
         if (values.length != 8) {
             throw new IllegalArgumentException("DateTime#setValues: Array length "
                                                + values.length + " != 8");
         }
         this.setCentury(values[0]);
         this.setYear(values[1]);
         this.setMonth(values[2]);
         this.setDay(values[3]);
         this.setHour(values[4]);
         this.setMinute(values[5]);
         this.setSecond(values[6], values[7]);
     }

    /**
     * Returns an array of shorts with all the fields that describe this
     * DateTime type. The order of entries in the array is as follows:
     * <ul>
     * <li>century</li>
     * <li>year</li>
     * <li>month</li>
     * <li>day</li>
     * <li>hour</li>
     * <li>minute</li>
     * <li>second</li>
     * <li>millisecond</li>
     * </ul>
     * Note:the time zone is not included.
     *
     * @return an array of short with all the fields that describe this Date
     *         type.
     */
    public short[] getValues() {
        short[] result = new short[8];
        result[0] = this.getCentury();
        result[1] = this.getYear();
        result[2] = this.getMonth();
        result[3] = this.getDay();
        result[4] = this.getHour();
        result[5] = this.getMinute();
        result[6] = this.getSeconds();
        result[7] = this.getMilli();
        return result;
    } //getValues

    /**
     * Converts this DateTime into a local java.util.Date.
     * @return a local java.util.Date representing this DateTime.
     */
    public java.util.Date toDate() {
        Calendar calendar = new GregorianCalendar(getCentury()*100+getYear(), getMonth()-1, getDay(), getHour(), getMinute(), getSeconds());
        calendar.set(Calendar.MILLISECOND, getMilli());
        setDateFormatTimeZone(calendar);
        return calendar.getTime();
    } //toDate()

    /**
     * Converts this DateTime into a long value representing a java.util.Date.
     * @return This DateTime instance as a long value representing a java.util.Date.
     */
    public long toLong() {
        return toDate().getTime();
    }

    /**
     * Converts this DateTime to a string. The format is defined by W3C XML
     * Schema recommendation and ISO8601: (+|-)CCYY-MM-DDTHH:MM:SS.SSS(+/-)HH:SS
     *
     * @return a string representing this Date
     */
     public String toString() {
        StringBuffer result = new StringBuffer();

        appendDateString(result);
        result.append('T');
        appendTimeString(result);
        appendTimeZoneString(result);

        return result.toString();
    } //toString

    /**
     * Parses a String into a new DateTime instance.
     *
     * @param str
     *            the string to parse
     * @return a new DateTime instance with the value of the parsed string.
     * @throws ParseException
     *             If the string to parse does not follow the right format
     */
    public static DateTime parse(String str) throws ParseException {
        return parseDateTime(str);
    }

    /**
     * Parses a String into a new DateTime instance.
     *
     * @param str
     *            the string to parse
     * @return a new DateTime instance with the value of the parsed string.
     * @throws ParseException
     *             If the string to parse does not follow the right format
     */
    public static DateTime parseDateTime(String str) throws ParseException {
        return parseDateTimeInternal(str, new DateTime());
     }

    /**
     * Parses a String into the provided DateTime instance (or a new DateTime
     * instance if the one provided is null) and assigns that value to the
     * DateTime instance given.
     *
     * @param str
     *            the string to parse
     * @param result
     *            the DateTime instance to assign to the parsed value of the
     *            String.  If null is passed, a new DateTime instance is created
     *            to be returned.
     * @return the DateTime instance with the value of the parsed string.
     * @throws ParseException
     *             If the string to parse does not follow the right format
     */
    private static DateTime parseDateTimeInternal(String str, DateTime result) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("The string to be parsed must not be null.");
        }

        if (result == null) {
            result = new DateTime();
        }

        char[] chars = str.toCharArray();

        if (chars.length < 19) {
            throw new ParseException(BAD_DATE + str + "\nDateTime is not long enough", 0);
        }

        int idx = 0;
        idx = parseYear(str, result, chars, idx, BAD_DATE);
        idx = parseMonth(str, result, chars, idx, BAD_DATE);
        idx = parseDay(str, result, chars, idx, BAD_DATE);

        if (chars[idx] != 'T') {
            throw new ParseException(BAD_DATE + str + "\n 'T' " + DateTimeBase.WRONGLY_PLACED, idx);
        }

        idx++;

        idx = parseTime(str, result, chars, idx, BAD_DATE);
        parseTimeZone(str, result, chars, idx, BAD_DATE);

        return result;
    } //parse

}
