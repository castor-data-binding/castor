/*
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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author              Changes
 * 10/02/2002   Arnaud              Clean up
 * 04/18/2002   Arnaud              String constructor
 * 05/23/2001   Arnaud Blandin      Update to be compliant with XML Schema Recommendation
 * 11/16/2000   Arnaud Blandin      Constructor Date(java.util.Date)
 * 11/01/2000   Arnaud Blandin      Enhancements (constructor, methods access...)
 * 10/23/2000   Arnaud Blandin      Created
 */
package org.exolab.castor.types;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Describe an XML Schema Date.
 * <p>
 * The format is defined by W3C XML Schema Recommendation and ISO8601 i.e
 * <tt>(-)CCYY-MM-DD(Z|(+|-)hh:mm)</tt>
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$
 */
public class Date extends DateTimeBase {
    /** SerialVersionUID */
    private static final long serialVersionUID = -1634875709019365137L;
    /** Complaint string. */
    private static final String BAD_DATE = "Bad Date format: ";

    /**
     * No-arg constructor.
     */
    public Date() {
        // Nothing to do
    }

    /**
     * Constructs a XML Schema Date instance given all the values of the
     * different fields. By default a Date is not UTC and is local.
     *
     * @param values
     *            an array of shorts that represent the different fields of
     *            Time.
     */
    public Date(short[] values) {
        setValues(values);
    }

    /**
     * This constructor is used to convert a long value representing a Date to a
     * new org.exolab.castor.types.Date instance.
     * <p>
     * Note : all the information concerning the time part of the java.util.Date
     * is lost since a W3C Schema Date only represents CCYY-MM-YY
     *
     * @param dateAsLong
     *            Date represented in from of a long value.
     */
    public Date (long dateAsLong) {
        this (new java.util.Date (dateAsLong));
    }

    /**
     * This constructor is used to convert a java.util.Date into a new
     * org.exolab.castor.types.Date.
     * <p>
     * Note : all the information concerning the time part of the java.util.Date
     * is lost since a W3C Schema Date only represents CCYY-MM-YY.
     *
     * @param dateRef
     *            the java.util.Date to use to construct a new
     *            org.exolab.castor.types.Date
     */
    public Date(java.util.Date dateRef) {
        GregorianCalendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTime(dateRef);
        setCentury((short) (tempCalendar.get(Calendar.YEAR) / 100));
        setYear((short) (tempCalendar.get(Calendar.YEAR) % 100));

        //we need to add 1 to the Month value returned by GregorianCalendar
        //because 0<MONTH<11 (i.e January is 0)
        setMonth((short) (tempCalendar.get(Calendar.MONTH) + 1));
        setDay((short) tempCalendar.get(Calendar.DAY_OF_MONTH));
    } //Date(java.util.Date)

    /**
     * Constructs a date from a string.
     * @param date the string representing the date
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the right format (see the description
     *                        of this class)
     */
    public Date(String date) throws java.text.ParseException {
        parseDateInternal(date, this);
    }

    /**
     * Sets all the fields by reading the values in an array.
     * <p>
     * If a Time Zone is specified, it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     *
     * @param values
     *            an array of shorts with the values the array is supposed to be
     *            of length 4 and ordered like the following:
     *            <ul>
     *            <li>century</li>
     *            <li>year</li>
     *            <li>month</li>
     *            <li>day</li>
     *            </ul>
     */
    public void setValues(short[] values) {
        if (values.length != 4) {
            throw new IllegalArgumentException("Date#setValues: not the right number of values");
        }
        this.setCentury(values[0]);
        this.setYear(values[1]);
        this.setMonth(values[2]);
        this.setDay(values[3]);
    }

    /**
     * Returns an array of short with all the fields that describe this Date
     * type.
     * <p>
     * Note:the time zone is not included.
     *
     * @return an array of short with all the fields that describe this Date
     *         type.
     */
    public short[] getValues() {
        short[] result = new short[4];
        result[0] = this.getCentury();
        result[1] = this.getYear();
        result[2] = this.getMonth();
        result[3] = this.getDay();
        return result;
    } //getValues

    /**
     * Converts this Date into a local java.util.Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate() {
        Calendar calendar = new GregorianCalendar(getCentury()*100+getYear(), getMonth()-1, getDay());
        setDateFormatTimeZone(calendar);
        return calendar.getTime();
    } //toDate()

    /**
     * Converts this date into a long value.
     * @return This date instance as a long value.
     */
    public long toLong () {
        return this.toDate().getTime();
    }

    /**
     * convert this Date to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e (+|-)CCYY-MM-DD
     * @return a string representing this Date
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        appendDateString(result);
        appendTimeZoneString(result);

        return result.toString();
    } //toString

    /**
     * parse a String and convert it into an java.lang.Object
     * @param str the string to parse
     * @return an Object represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the right format (see the description
     *                        of this class)
     */
    public static Object parse(String str) throws ParseException {
        return parseDate(str);
    }

    /**
     * parse a String and convert it into a Date.
     * @param str the string to parse
     * @return the Date represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the right format (see the description
     *                        of this class)
     */
    public static Date parseDate(String str) throws ParseException {
        Date result = new Date();
        return parseDateInternal(str, result);
    }

    private static Date parseDateInternal(String str, Date result) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("The string to be parsed must not be null.");
        }

        if (result == null) {
            result = new Date();
        }

        char[] chars = str.toCharArray();

        // Year
        int idx = 0;
        if (chars[idx] == '-') {
             idx++;
             result.setNegative();
        }

        if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])
                || !Character.isDigit(chars[idx + 2]) || !Character.isDigit(chars[idx + 3])) {
            throw new ParseException(BAD_DATE + "'" + str + "'\nThe Year must be 4 digits long", idx);
        }

        short value1;
        short value2;

        value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        value2 = (short) ((chars[idx+2] - '0') * 10 + (chars[idx+3] - '0'));

        if (value1 == 0 && value2 == 0) {
            throw new ParseException(BAD_DATE+str+"\n'0000' is not allowed as a year.", idx);
        }

        result.setCentury(value1);
        result.setYear(value2);

        idx += 4;

        // Month
        if (chars[idx] != '-') {
            throw new ParseException(BAD_DATE + "'" + str + "'\n '-' " + DateTimeBase.WRONGLY_PLACED, idx);
        }

        idx++;

        if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
            throw new ParseException(BAD_DATE + "'" + str + "'\nThe Month must be 2 digits long", idx);
        }

        value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        result.setMonth(value1);

        idx += 2;

        // Day
        if (chars[idx] != '-') {
            throw new ParseException(BAD_DATE + "'" + str + "'\n '-' " + DateTimeBase.WRONGLY_PLACED, idx);
        }

        idx++;

        if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
            throw new ParseException(BAD_DATE + "'" + str + "'\nThe Day must be 2 digits long", idx);
        }

        value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        result.setDay(value1);

        idx += 2;

        parseTimeZone(str, result, chars, idx, BAD_DATE);

        return result;
    } //parse

    /////////////////////////// DISALLOWED METHODS ///////////////////////////

    public boolean hasHour() {
        return false;
    }
    public short getHour() {
        String err = "org.exolab.castor.types.Date does not have an Hour field.";
        throw new OperationNotSupportedException(err);
    }
    public void setHour(short hour) {
        String err = "org.exolab.castor.types.Date does not have an Hour field.";
        throw new OperationNotSupportedException(err);
    }
    public boolean hasMinute() {
        return false;
    }
    public short getMinute() {
        String err = "org.exolab.castor.types.Date does not have a Minute field.";
        throw new OperationNotSupportedException(err);
    }
    public void setMinute(short minute) {
        String err = "org.exolab.castor.types.Date does not have a Minute field.";
        throw new OperationNotSupportedException(err);
    }
    public boolean hasSeconds() {
        return false;
    }
    public short getSeconds() {
        String err = "org.exolab.castor.types.Date does not have a Seconds field.";
        throw new OperationNotSupportedException(err);
    }
    public void setSecond(short second) {
        String err = "org.exolab.castor.types.Date does not have a Seconds field.";
        throw new OperationNotSupportedException(err);
    }
    public boolean hasMilli() {
        return false;
    }
    public short getMilli() {
        String err = "org.exolab.castor.types.Date does not have a Milliseconds field.";
        throw new OperationNotSupportedException(err);
    }
    public void setMilliSecond(short millisecond) {
        String err = "org.exolab.castor.types.Date does not have a Milliseconds field.";
        throw new OperationNotSupportedException(err);
    }

}// Date
