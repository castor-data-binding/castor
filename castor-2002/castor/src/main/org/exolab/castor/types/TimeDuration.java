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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author           Changes
 * 10/26/2000   Arnaud Blandin   add TimeDuration(long l)
 * 10/26/2000   Arnaud Blandin   change parse method
 * 10/23/2000   Arnaud Blandin   Created
 */

package org.exolab.castor.types;

import java.text.ParseException;

/**
 * <p>Represents the timeDuration XML Schema type.
 * <p>This representation does not support the decimal fraction
 * for the lowest order item.
 * Besides setting TimeDuration to '0' is not possible thus there is
 * no distinction between '0' and 'P0Y'
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 **/
public class TimeDuration  {

    /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;
    /**the flag representing the 'T' position*/
    private static final int TIME_FLAG = 8;
    //Private variables

    /**
     * the number of years
     */
    private short _year = 0;
    /**
     * the number of months
     */
    private short _month = 0;
    /**
     * the number of days
     */
    private short _day = 0;
    /**
     * the number of hours
     */
    private short _hour = 0;
    /**
     * the number of minutes
     */
    private short _minute = 0;
    /**
     * the number of seconds
     */
    private short _second = 0;
    /**
     * true if the Time Duration is negative
     */
    private boolean _isNegative = false;

    /**
     * default constructor
     */
    public TimeDuration() {
    }

     /**
     * <p>This constructor fills in the time duration fields according to the
     * value of the long by calling setValue
     * @see #setValue
     * @param l the long value of the Time Duration
     * @return A time duration which represents the long
     */
    public TimeDuration(long l) {

        long refSecond = 1000;
        long refMinute = 60 * refSecond;
        long refHour = 60 * refMinute;
        long refDay = 24 * refHour;
        long refMonth = (long) (30.42 * refDay);
        long refYear = 12 * refMonth;

        if (DEBUG) {
            System.out.println("In time duration Constructor");
            System.out.println("long : "+l);
        }

        if (l<0) {
            this.setNegative();
            l =-l;
        }

        short year = (short) (l/refYear);
        l = l % refYear;
        if (DEBUG) {
            System.out.println("nb years:"+year);
            System.out.println("New long : "+l);
        }

        short month = (short) (l/refMonth);
        l = l % refMonth;
        if (DEBUG) {
            System.out.println("nb months:"+month);
            System.out.println("New long : "+l);
            System.out.println(refDay);
        }

        short day = (short) (l/refDay);
        l = l % refDay;
         if (DEBUG) {
            System.out.println("nb days:"+day);
            System.out.println("New long : "+l);
        }

        short hour = (short) (l/refHour);
        l = l % refHour;
        if (DEBUG) {
            System.out.println("nb hours:"+hour);
            System.out.println("New long : "+l);
        }

        short minute = (short) (l/refMinute);
        l = l % refMinute;
        if (DEBUG) {
            System.out.println("nb minutes:"+minute);
            System.out.println("New long : "+l);
        }

        short seconds = (short) (l/refSecond);
          if (DEBUG) {
            System.out.println("nb seconds:"+seconds);
        }

        this.setValue(year, month, day, hour, minute, seconds);
    }

    //Set methods
    public void setYear(short year) {
        _year = year;
    }

    public void setMonth(short month) {
        _month = month;
    }

    public void setDay(short day) {
        _day = day;
    }

    public void setHour(short hour) {
        _hour = hour;
    }

    public void setMinute(short minute) {
        _minute = minute ;
    }

    public void setSeconds(short second) {
        _second = second;
    }

    public void setNegative() {
        _isNegative = true;
    }

    /**
     * Fill in the fields of the TimeDuration with the given values
     * @param year the year value
     * @param month the month value
     * @param day the day value
     * @param hour the hour value
     * @param minute the minute value
     * @param second the second value
     */
     public void setValue(short year, short month, short day,
                         short hour, short minute, short second)
    {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setHour(hour);
        this.setMinute(minute);
        this.setSeconds(second);
    }



    //Get methods
     public short getYear() {
        return(_year);
    }

    public short getMonth() {
        return(_month);
    }

    public short getDay() {
        return(_day);
    }

    public short getHour() {
        return(_hour);
    }

    public short getMinute() {
        return(_minute);
    }

    public short getSeconds() {
        return(_second);
    }

    public boolean isNegative() {
        return _isNegative;
    }

   /**
    * <p>Convert a timeDuration into a long
    * This long represents the duration in milliseconds
    * @return a long representing the duration
    */
    public long toLong() {
        long result = 0;
        //30.42 days in a month (365/12)
        //Horner method
        result = ( (long) ( ((((( (_year*12L) +_month ) * 30.42
                                    +_day)*24L
                                    +_hour)*60L
                                    +_minute)*60L
                                    +_second)*1000L ));

        result = isNegative()? -result : result;
        return result;
    }

    /**
     * <p> Convert a timeDuration into a String
     * conforming to ISO8601 and
     * <a href="http://www.w3.org/TR/2000/WD-xmlschema-2-20000922/#timeDuration">
     * XML Schema specs </a>
     *@return a string representing the time duration
     */
     public String toString() {
        String result = "P";
        if (_year != 0) {
            result = result+_year+"Y";
        }
        if (_month != 0) {
           result = result + _month+"M";
        }
        if (_day !=0 ) {
            result = result + _day+"D";
        }
        boolean isThereTime = ( (_hour != 0) || (_minute != 0) || (_second != 0) );
        if (isThereTime) {
            result = result + "T";
            if (_hour !=0 ) {
                result = result + _hour +"H";
            }
            if (_minute !=0) {
                result = result + _minute +"M";
            }
            if (_second != 0) {
                result = result + _second +"S";
            }
        }
        result = (_isNegative) ? "-" + result : result;
        return result;
    } //toString

    /**
     * <p>Parse the given string and return a time duration
     * which represents this string
     * @param String str the string to parse
     * @return a TimeDuration instance which represent the string
     * @throws ParseException thrown when the string is not valid
     */
     public static TimeDuration parse (String str) throws ParseException
    {


        if (str == null) {
            throw new IllegalArgumentException("the string to be parsed must not"
                                                +" be null");

        }

        TimeDuration result = new TimeDuration();
        char[] chars = str.toCharArray();
        int idx = 0;


        if (chars.length == 0) {
            //str = "" means a null TimeDuration
            return null;
        }

        if (chars[idx] == '-') {
            ++idx;
            result.setNegative();
            if (idx >= chars.length) {
                throw new ParseException("'-' is wrong placed",0);
            }
        }

        //-- make sure we start with 'P'
        if (chars[idx] != 'P') {
            throw new ParseException("Missing 'P' delimiter", idx);
        }
        ++idx;


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
                        String err = str+":Syntax error, 'Y' must " +
                            "proceed all other delimiters.";
                        throw new ParseException(err, idx);
                    }
                    //--set flags
                    flags = 64;
                    if (hasNumber) {
                        result.setYear((short)number);
                        hasNumber = false;
                    }
                    else {
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
                        }
                        else {
                            String err =str+": missing number of minutes " +
                                "before 'M'";
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
                        }
                        else {
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
                    }
                    else {
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
                    }
                    else {
                        String err =str+":missing number of hours before 'H'";
                        throw new ParseException(err, idx);
                    }
                    break;
                case 'S':
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
                    }
                    else {
                        String err = str+": missing number of seconds before 'S'";
                        throw new ParseException(err, idx);
                    }
                    break;
                default:
                    // make sure ch is a digit...
                    if (('0' <= ch) && (ch <= '9')) {

                        if (hasNumber)
                            number = (number*10)+(ch-48);
                        else {
                            hasNumber = true;
                            number = (ch-48);
                        }
                    }
                    else
                        throw new ParseException(str+":Invalid character: " + ch, idx);
                    break;
            }
        }

        //-- check for T, but no HMS
        if ((flags & 15) == 8) {
            throw new ParseException(str+": T must be omitted", idx);
        }

        if (hasNumber) {
            throw new ParseException(str+": expecting ending delimiter", idx);
        }

        return result;

    } //parse

    /**
     * Override the java.lang.equals method
     * @see equal
     */
     public boolean equals(Object object) {
        if (object instanceof TimeDuration)
               return equal( (TimeDuration) object);
        else return false;
    }
    /**
     * Returns true if the instance of TimeDuration has the same fields
     * of the parameter
     * @param timeD the time duration to compare
     * @return true if equal, false if not
     */
    public boolean equal(TimeDuration timeD) {
        boolean result = false;
        if (timeD == null)
            return result;
        result = (_year == timeD.getYear());
        result = result && (_month == timeD.getMonth());
        result = result && (_day == timeD.getDay());
        result = result && (_hour == timeD.getHour());
        result = result && (_minute == timeD.getMinute());
        result = result && (_second == timeD.getSeconds());
        result = result && (this.isNegative() == timeD.isNegative());
        return result;
    } //equals

    /**
     * <p>Returns true if the present instance of TimeDuration is greater than
     * the parameter
     * <p>Note This definition does not follow the XML SCHEMA DRAFT 20001024
     * the following orger relation is used :
     * t1,t2 timeDuration types
     * t1>t2 iff t1.toLong()>t2.toLong()
     * @param timeD the time duration to compare with the present instance
     * @return true if the present instance is the greatest, false if not
     */
     public boolean isGreater(TimeDuration timeD) {
        boolean result = false;
        // to be optimized ??
        result = this.toLong() > timeD.toLong();
        return result;
     } //isGreater
}//TimeDuration