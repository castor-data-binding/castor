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
 * Date         Author          Changes
 * 10/02/2002   Arnaud          Added support for 3 figure millisecond and clean up
 * 04/18/2002   Arnaud          String Constructor
 * 05/22/2001   Arnaud Blandin  Created
 */
package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Describes an XML schema Time.
 * <p>
 * The format is defined by W3C XML Schema Recommendation and ISO8601 i.e
 * <tt>hh:mm:ss.sss(Z|(+|-)hh:mm)</tt>
 * <p>
 * Deep support of fractional seconds is not implemented. This implementation
 * only supports fractional second resolution of milliseconds.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$
 * @see DateTimeBase
 */
public class Time extends DateTimeBase {

    /** SerialVersionUID */
    private static final long serialVersionUID = -8268707778437931489L;

    /** The Time Format used by the toDate() method */
    private static final String TIME_FORMAT_MILLI = "HH:mm:ss.SSS";
    private static final String TIME_FORMAT_NO_MILLI = "HH:mm:ss";

    private static final String BAD_TIME = "Bad Time format: ";

    /**
     * No-arg constructor.
     */
    public Time() {
        // Nothing to do
    }

    /**
     * Constructs a XML Schema Time instance given all the values of
     * the different fields.
     * By default a Time is not UTC and is local.
     * @param values an array of shorts that represent the different fields of Time.
     */
    public Time(short[] values) {
        setValues(values);
    }

    /**
     * Constructs a XML Schema Time instance given a long representing the time in milliseconds.
     * By default a Time is not UTC and is local.
     * @param l The long value that represents the time instance.
     */
    public Time (long l) {
        if (l > 86400000L) {
            throw new IllegalArgumentException("Bad Time: the long value can't represent more than 24h.");
        }
        this.setHour((short)(l / 3600000));
        l = l % 3600000;
        this.setMinute((short)(l / 60000));
        l = l % 60000;
        this.setSecond((short)(l / 1000), (short)(l % 1000));
    }

   /**
    * Constructs a Time given a string representation.
    * @param time the string representation of the Time to instantiate
    * @throws ParseException a parse exception is thrown if the string to parse
    *                        does not follow the rigth format (see the description
    *                        of this class)
    */
    public Time(String time) throws ParseException {
       parseTimeInternal(time, this);
    }

    /**
     * Sets all the fields by reading the values in an array.
     * <p>
     * if a Time Zone is specified it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     *
     * @param values
     *            an array of shorts with the values the array is supposed to be
     *            of length 4 and ordered like that:
     *            <ul>
     *            <li>hour</li>
     *            <li>minute</li>
     *            <li>second</li>
     *            <li>millisecond</li>
     *            </ul>
     */
     public void setValues(short[] values) {
        if (values.length != 4) {
             throw new IllegalArgumentException("Time#setValues: not the right number of values");
        }
        this.setHour(values[0]);
        this.setMinute(values[1]);
        this.setSecond(values[2],values[3]);
     }

    /**
     * returns an array of short with all the fields that describe
     * this time type.
     * <p>Note:the time zone is not included.
     * @return  an array of short with all the fields that describe
     * this time type.
     */
    public short[] getValues() {
        short[] result = new short[4];
        result[0] = this.getHour();
        result[1] = this.getMinute();
        result[2] = this.getSeconds();
        result[3] = this.getMilli();
        return result;
    } //getValues

    /**
     * converts this Time into a local java Date.
     * @return a local date representing this Time
     */
    public Date toDate() {
        Date date = null;
        SimpleDateFormat df = null;
        String temp = this.toString();
        if (temp.indexOf('.') > 0) {
            df = new SimpleDateFormat(TIME_FORMAT_MILLI);
        } else {
            df = new SimpleDateFormat(TIME_FORMAT_NO_MILLI);
        }

        setDateFormatTimeZone(df);

        try {
            date = df.parse(temp);
        } catch (ParseException e) {
            //this can't happen since toString() should return the proper string format
           e.printStackTrace();
           return null;
        }
        return date;
    } //toDate()

    /**
     * convert this Time to a string
     * The format is defined by W3C XML Schema Recommendation and ISO8601
     * i.e hh:mm:ss.sss(Z|(+|-)hh:mm)
     * @return a string representing this Time
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        appendTimeString(result);
        appendTimeZoneString(result);
        return result.toString();
    } //toString

   /**
    * parses a String and converts it into a java.lang.Object
    * @param str the string to parse
    * @return the java.lang.Object represented by the string
    * @throws ParseException a parse exception is thrown if the string to parse
    *                        does not follow the rigth format (see the description
    *                        of this class)
    */
    public static Object parse(String str) throws ParseException {
        return parseTime(str);
    }

    /**
     * parses a String and converts it into a Time.
     * @param str the string to parse
     * @return the Time represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static Time parseTime(String str) throws ParseException {
        return parseTimeInternal(str, null);
    }

    private static Time parseTimeInternal(String str, Time result) throws ParseException {
        if (str == null) {
            throw new IllegalArgumentException("The string to be parsed must not be null.");
        }

        if (result == null) {
            result = new Time();
        }

        char[] chars = str.toCharArray();
        int idx = parseTime(str, result, chars, 0, BAD_TIME);
        parseTimeZone(str, result, chars, idx, BAD_TIME);

        return result;
    } //parse

    /////////////////////////// DISALLOWED METHODS ///////////////////////////

    public boolean hasIsNegative() {
        return false;
    }
    public boolean isNegative() {
        String err = "org.exolab.castor.types.Time does not have a 'negative' field.";
        throw new UnsupportedOperationException(err);
    }
    public void setNegative() {
        String err = "org.exolab.castor.types.Time cannot be negative.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasCentury() {
        return false;
    }
    public short getCentury() {
        String err = "org.exolab.castor.types.Time does not have a Century field.";
        throw new UnsupportedOperationException(err);
    }
    public void setCentury(short century) {
        String err = "org.exolab.castor.types.Time does not have a Century field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasYear() {
        return false;
    }
    public short getYear() {
        String err = "org.exolab.castor.types.Time does not have a Year field.";
        throw new UnsupportedOperationException(err);
    }
    public void setYear(short year) {
        String err = "org.exolab.castor.types.Time does not have a Year field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasMonth() {
        return false;
    }
    public short getMonth() {
        String err = "org.exolab.castor.types.Time does not have a Month field.";
        throw new UnsupportedOperationException(err);
    }
    public void setMonth(short month) {
        String err = "org.exolab.castor.types.Time does not have a Month field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasDay() {
        return false;
    }
    public short getDay() {
        String err = "org.exolab.castor.types.Time does not have a Day field.";
        throw new UnsupportedOperationException(err);
    }
    public void setDay(short month) {
        String err = "org.exolab.castor.types.Time does not have a Day field.";
        throw new UnsupportedOperationException(err);
    }

} //Time
