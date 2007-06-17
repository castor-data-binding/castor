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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author           Changes
 * 07/04/2002   Arnaud           Support for milliseconds
 * 04/18/2002   Arnaud           String constructor
 * 05/22/2000   Arnaud Blandin   Created
 */
package org.exolab.castor.types;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the representation of XML Schema datatype: <b>duration</b>.
 * <p>
 * This representation does not support the decimal fraction for the lowest
 * order item.
 * <p>
 * The order relation provided by this implementation does not follow the
 * guidelines of XML Schema Specification that defines only a partial order.
 * <p>
 * For more information see <a
 * href="http://www.w3.org/TR/xmlschema-2/#duration"> X3C XML Schema
 * Specification</a>.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class Duration implements java.io.Serializable {
    /** SerialVersionUID. */
    private static final long serialVersionUID = -6475091654291323029L;
    /** Jakarta's common-logging logger. */
    private static final Log LOG = LogFactory.getLog(Duration.class);

    /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;
    /** the flag representing the 'T' position. */
    private static final int TIME_FLAG = 8;

    /** the number of years. */
    private short _year = 0;
    /** the number of months. */
    private short _month = 0;
    /** the number of days. */
    private short _day = 0;
    /** the number of hours. */
    private short _hour = 0;
    /** the number of minutes. */
    private short _minute = 0;
    /** the number of seconds. */
    private short _second = 0;
    /** the potential number of milliseconds. */
    private long _millisecond = 0;
    /** true if the Duration is negative. */
    private boolean _isNegative = false;

    /**
     * default constructor.
     */
    public Duration() {
        // Nothing to do
    }

    /**
     * Constructs a duration from a string.
     * @param duration the string representation of the duration to create
     * @throws ParseException thrown when the string is not a valid duration
     */
    public Duration(String duration) throws ParseException {
        parseDurationInternal(duration, this);
    }

    /**
     * This constructor fills in the duration fields according to the value of
     * the long by calling setValue.
     *
     * @see #setValue
     * @param l  the long value of the Duration
     */
    public Duration(long l) {
        long refSecond = 1000;
        long refMinute = 60 * refSecond;
        long refHour   = 60 * refMinute;
        long refDay    = 24 * refHour;
        long refMonth  = (long) (30.42 * refDay);
        long refYear   = 12 * refMonth;

        if (DEBUG) {
            System.out.println("In time duration Constructor");
            System.out.println("long : "+l);
        }

        if (l < 0) {
            this.setNegative();
            l = -l;
        }

        short year = (short) (l / refYear);
        l = l % refYear;
        if (DEBUG) {
            System.out.println("nb years:"+year);
            System.out.println("New long : "+l);
        }

        short month = (short) (l / refMonth);
        l = l % refMonth;
        if (DEBUG) {
            System.out.println("nb months:"+month);
            System.out.println("New long : "+l);
            System.out.println(refDay);
        }

        short day = (short) (l / refDay);
        l = l % refDay;
        if (DEBUG) {
            System.out.println("nb days:"+day);
            System.out.println("New long : "+l);
        }

        short hour = (short) (l / refHour);
        l = l % refHour;
        if (DEBUG) {
            System.out.println("nb hours:"+hour);
            System.out.println("New long : "+l);
        }

        short minute = (short) (l / refMinute);
        l = l % refMinute;
        if (DEBUG) {
            System.out.println("nb minutes:"+minute);
            System.out.println("New long : "+l);
        }

        short seconds = (short) (l / refSecond);
        l = l % refSecond;
        if (DEBUG) {
            System.out.println("nb seconds:"+seconds);
        }

        long milliseconds = l;
        if (DEBUG) {
            System.out.println("nb milliseconds:"+milliseconds);
        }

        this.setValue(year, month, day, hour, minute, seconds, milliseconds);
    }

    //Set methods

    public void setYear(short year) {
        if (year < 0) {
            String err = "In a duration all fields have to be positive.";
            throw new IllegalArgumentException(err);
        }
        _year = year;
    }

    public void setMonth(short month) {
        if (month < 0) {
            String err = "In a duration all fields have to be positive.";
            throw new IllegalArgumentException(err);
        }
        _month = month;
    }

    public void setDay(short day) {
        if (day < 0) {
            String err = "In a duration all fields have to be positive.";
            throw new IllegalArgumentException(err);
        }
        _day = day;
    }

    public void setHour(short hour) {
        if (hour < 0) {
            String err = "In a duration all fields have to be positive.";
            throw new IllegalArgumentException(err);
        }
        _hour = hour;
    }

    public void setMinute(short minute) {
        if (minute < 0) {
            String err = "In a duration all fields have to be positive.";
            throw new IllegalArgumentException(err);
        }
        _minute = minute ;
    }

    public void setSeconds(short second) {
        if (second < 0) {
            String err = "In a duration all fields have to be positive.";
            throw new IllegalArgumentException(err);
        }
        _second = second;
    }

    public void setMilli(long milli) {
        if (milli < 0) {
            String err = "In a duration all fields have to be positive.";
            throw new IllegalArgumentException(err);
        }
        _millisecond = milli;
    }

    public void setNegative() {
        _isNegative = true;
    }

    /**
     * Fill in the fields of the duration with the given values
     * @param year the year value
     * @param month the month value
     * @param day the day value
     * @param hour the hour value
     * @param minute the minute value
     * @param second the second value
     * @param millisecond the second value
     */
    public void setValue(short year, short month, short day,
                         short hour, short minute, short second, long millisecond) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
        this.setMinute(minute);
        this.setSeconds(second);
        this.setMilli(millisecond);
    }

    //Get methods

    public short getYear() {
        return _year;
    }

    public short getMonth() {
        return _month;
    }

    public short getDay() {
        return _day;
    }

    public short getHour() {
        return _hour;
    }

    public short getMinute() {
        return _minute;
    }

    public short getSeconds() {
        return _second;
    }

    public long getMilli() {
        return _millisecond;
    }

    public boolean isNegative() {
        return _isNegative;
    }

    /**
     * <p>Convert a duration into a long
     * This long represents the duration in milliseconds.
     * @return a long representing the duration
     */
    public long toLong() {
        long result = 0;

        // 30.42 days in a month (365/12) (Horner method)
        result = (long) (((((((_year*12L) +_month) * 30.42
                                    +_day) * 24L
                                    +_hour) * 60L
                                    +_minute) * 60L
                                    +_second) * 1000L
                                    +_millisecond);

        result = isNegative() ? -result : result;
        return result;
    }

    /**
     * Convert a duration into a String conforming to ISO8601 and <a
     * href="http://www.w3.org/TR/xmlschema-2/#duration"> XML Schema specs </a>
     *
     * @return a string representing the duration
     */
    public String toString() {
        // if the duration is empty, we choose as a standard to return "PTOS"
        if (this.toLong() == 0) {
            return "PT0S";
        }

        StringBuffer result = new StringBuffer();
        if (_isNegative) {
            result.append('-');
        }
        result.append("P");

        if (_year != 0) {
            result.append(_year);
            result.append('Y');
        }

        if (_month != 0) {
            result.append(_month);
            result.append('M');
        }

        if (_day != 0) {
            result.append(_day);
            result.append('D');
        }

        boolean isThereTime = _hour != 0 || _minute != 0 || _second != 0 || _millisecond != 0;
        if (isThereTime) {
            result.append('T');

            if (_hour != 0) {
                result.append(_hour);
                result.append('H');
            }

            if (_minute != 0) {
                result.append(_minute);
                result.append('M');
            }

            if (_second != 0 || _millisecond != 0) {
                result.append(_second);
                if (_millisecond != 0) {
                    result.append('.');
                    if (_millisecond < 100) {
                        result.append('0');
                        if (_millisecond < 10)
                            result.append('0');
                    }
                    result.append(_millisecond);
                }
                result.append('S');
            }
        }

        return result.toString();
    } //toString

    /**
     * parse a String and convert it into a java.lang.Object
     * @param str the string to parse
     * @return the java.lang.Object represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static Object parse(String str) throws ParseException {
        return parseDuration(str);
    }

    /**
     * <p>Parse the given string and return a time duration
     * which represents this string.
     * @param str the string to parse
     * @return a TimeDuration instance which represent the string
     * @throws ParseException thrown when the string is not valid
     */
    public static Duration parseDuration(String str) throws ParseException {
        Duration result = new Duration();
        return parseDurationInternal(str, result);
    }

    private static Duration parseDurationInternal(String str, Duration result) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("the string to be parsed must not be null");
        }

        //str = "" means a null TimeDuration
        if (str.length() == 0) {
            return null;
        }

        if (result == null) {
            result = new Duration();
        }

        char[] chars = str.toCharArray();
        int idx = 0;

        if (chars[idx] == '-') {
            ++idx;
            result.setNegative();
            if (idx >= chars.length) {
                throw new ParseException("'-' is wrongly placed",0);
            }
        }

        //-- make sure we start with 'P'
        if (chars[idx] != 'P') {
            throw new ParseException("Missing 'P' delimiter", idx);
        }
        ++idx;

        if (idx == chars.length) {
            throw new ParseException("Bad format for a duration:"+str, idx);
        }
        int number = 0;
        boolean hasNumber = false;

        //-- parse flags
        // YMDTHMS = b1111111 (127)
        // Year = 64, Month = 32, etc
        int flags = 0;

        while (idx < chars.length) {

            char ch = chars[idx++];

            switch(ch) {

                //-- Year
                case 'Y':
                    //-- check for error
                    if (flags > 0) {
                        String err = str + ":Syntax error, 'Y' must proceed all other delimiters.";
                        throw new ParseException(err, idx);
                    }
                    //--set flags
                    flags = 64;
                    if (hasNumber) {
                        result.setYear((short)number);
                        hasNumber = false;
                    } else {
                        String err = str+":missing number of years before 'Y'";
                        throw new ParseException(err, idx);
                    }

                    break;
                //-- Month or Minute
                case 'M':
                    //-- Either month or minute, check for T flag,
                    //-- if present then Minute, otherwise Month
                    if ((flags & TIME_FLAG) == 8) {

                        // make sure no existing minute or second
                        // flags have been set.
                        if ((flags & 3) > 0) {
                            throw new ParseException(str+": Syntax Error...", idx);
                        }
                        flags = flags | 2;
                        if (hasNumber) {
                            result.setMinute((short)number);
                            hasNumber = false;
                        } else {
                            String err =str+": missing number of minutes before 'M'";
                            throw new ParseException(err, idx);
                        }
                    }
                    //-- Month
                    else {
                        // make sure no existing month, day or time
                        // flags have been set
                        if ((flags & 63) > 0) {
                            throw new ParseException(str+":Syntax Error...", idx);
                        }
                        flags = flags | 32;
                        if (hasNumber) {
                            result.setMonth((short)number);
                            hasNumber = false;
                        } else {
                            String err = str+":missing number of months before 'M'";
                            throw new ParseException(err, idx);
                        }
                    }
                    break;
                    //-- Day
                case 'D':
                    // make sure no day or time flags have been set
                    if ((flags & 31) > 0) {
                        throw new ParseException(str+":Syntax Error...", idx);
                    }
                    flags = flags | 16;
                    if (hasNumber) {
                        result.setDay((short)number);
                        hasNumber = false;
                    } else {
                        String err = str+":missing number of days before 'D'";
                        throw new ParseException(err, idx);
                    }
                    break;
                //-- Time
                case 'T':
                    // make sure no T flag already exists
                    if ((flags & TIME_FLAG) == 8) {
                        String err =str + ":Syntax error, 'T' may not " +
                            "exist more than once.";
                        throw new ParseException(err, idx);
                    }
                    flags = flags | 8;
                    break;
                    //-- Hour
                case 'H':
                    // make sure no time flags have been set, but
                    // that T exists
                    if ((flags & 15) != 8) {
                        String err = null;
                        if ((flags & 8) != 8)
                            err = str+": Missing 'T' before 'H'";
                        else
                            err = str+": Syntax Error, 'H' must appear for 'M' or 'S'";
                        throw new ParseException(err, idx);
                    }
                    flags = flags | 4;
                    if (hasNumber) {
                        result.setHour((short)number);
                        hasNumber = false;
                    } else {
                        String err =str+":missing number of hours before 'H'";
                        throw new ParseException(err, idx);
                    }
                    break;
                case 'S':
                    if (flags != 0) {
                        // make sure T exists, but no 'S'
                        if ((flags & 8) != 8) {
                            String err = str+": Missing 'T' before 'S'";
                            throw new ParseException(err, idx);
                        }
                        if ((flags & 1) == 1) {
                            String err =str+": Syntax error 'S' may not exist more than once.";
                            throw new ParseException(err, idx);
                        }

                        flags = flags | 1;
                        if (hasNumber) {
                            result.setSeconds((short)number);
                            hasNumber = false;
                        } else {
                            String err = str+": missing number of seconds before 'S'";
                            throw new ParseException(err, idx);
                        }
                    } else {
                        if (hasNumber) {
                            String numb = Integer.toString(number);
                            if (numb.length() < 3) {
                                if (numb.length() < 2) {
                                    number = number * 10;
                                }
                                number = number * 10;
                            }
                            result.setMilli(number);
                            hasNumber = false;
                        } else {
                            String err = str+": missing number of milliseconds before 'S'";
                            throw new ParseException(err, idx);
                        }
                    }

                    break;

                case '.':
                    // make sure T exists, but no 'S'
                    if ((flags & 8) != 8) {
                        String err = str+": Missing 'T' before 'S'";
                        throw new ParseException(err, idx);
                    }

                    if ((flags | 1) == 1) {
                        String err =str+": Syntax error '.' may not exist more than once.";
                        throw new ParseException(err, idx);
                    }

                    flags = 0;

                    if (hasNumber) {
                        result.setSeconds((short)number);
                        hasNumber = false;
                    } else {
                        String err = str+": missing number of seconds before 'S'";
                        throw new ParseException(err, idx);
                    }
                    break;

                default:
                    // make sure ch is a digit...
                    if ('0' <= ch && ch <= '9') {
                        if (hasNumber) {
                            number = (number*10) + (ch-48);
                        } else {
                            hasNumber = true;
                            number = (ch-48);
                        }
                    } else {
                        throw new ParseException(str+":Invalid character: " + ch, idx);
                    }
                    break;
            }
        }

        //-- check for T, but no HMS
        if ((flags & 15) == 8) {
            LOG.warn("Warning: " + str + ": T shall be omitted");
        }

        if (hasNumber) {
            throw new ParseException(str+": expecting ending delimiter", idx);
        }

        return result;
    } //parse

    /**
     * {@inheritDoc}
     * Overrides the java.lang.Object#hashcode method.
     */
    public int hashCode() {
        return 37 * (_year ^ _month ^ _day ^ _hour ^ _minute ^ _second);
    }

    /**
     * {@inheritDoc}
     * Override the java.lang.equals method
     * @see #equal
     */
    public boolean equals(Object object) {
        if (object instanceof Duration) {
            return equal((Duration) object);
        }
        return false;
    }

    /**
     * Returns true if the instance of TimeDuration has the same fields
     * of the parameter
     * @param duration the time duration to compare
     * @return true if equal, false if not
     */
    public boolean equal(Duration duration) {
        boolean result = false;
        if (duration == null) {
            return result;
        }
        result = (_year == duration.getYear());
        result = result && (_month == duration.getMonth());
        result = result && (_day == duration.getDay());
        result = result && (_hour == duration.getHour());
        result = result && (_minute == duration.getMinute());
        result = result && (_second == duration.getSeconds());
        result = result && (_millisecond == duration.getMilli());
        result = result && (this.isNegative() == duration.isNegative());
        return result;
    } //equals

    /**
     * Returns true if the present instance of TimeDuration is greater than the
     * parameter
     * <p>
     * Note This definition does not follow the XML SCHEMA RECOMMENDATION
     * 05022001 the following total order relation is used :
     * <tt>givent t1,t2 timeDuration types
     * t1>t2 iff t1.toLong()>t2.toLong()</tt>
     *
     * @param duration
     *            the time duration to compare with the present instance
     * @return true if the present instance is the greatest, false if not
     */
    public boolean isGreater(Duration duration) {
        boolean result = false;
        // to be optimized ??
        result = this.toLong() > duration.toLong();
        return result;
    } //isGreater

} //Duration
