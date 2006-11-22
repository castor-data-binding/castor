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
 * Date         Author              Changes
 * 04/18/2002   Arnaud              Constructor with string
 * 05/24/2001   Arnaud Blandin      Created
 */
package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Describe an XML schema gMonthDay type.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>--MM-DD(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 */
public class GMonthDay extends DateTimeBase {

    /** SerialVersionUID */
    private static final long serialVersionUID = -6351252242146921258L;

    /** The gMonthDay SimpleDateFormat string. */
    private static final String MONTHDAY_FORMAT = "--MM-dd";
    /** Prefix of any complaint we make. */
    private static final String BAD_GMONTHDAY = "Bad gMonthDay format: ";

    /**
     * public only for the generated source code
     */
    public GMonthDay() {
        // Nothing to do
    }

    /**
     * Instantiates a new gMonthDay given the value
     * of the month and the value of the day.
     * @param month the month value
     * @param day the day value
     */
    public GMonthDay(short month, short day) {
        setMonth(month);
        setDay(day);
    }

    /**
     * Instantiates a new gMonthDay given the value
     * of the month and the value of the day.
     * @param month the month value
     * @param day the day value
     */
    public GMonthDay(int month, int day) {
        setMonth((short)month);
        setDay((short)day);
    }

    /**
     * Constructs a XML Schema GMonthDay instance given all the values of
     * the different fields.
     * By default a GMonthDay is not UTC and is local.
     * @param values an array of shorts that represent the different fields of Time.
     */
    public GMonthDay(short[] values) {
        this.setValues(values);
    }

    /**
     * Construct a GMonthDay from a string value
     * @param gmonthDay the string representation of the GMonthDay to instantiate
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public GMonthDay(String gmonthDay) throws ParseException {
        parseGMonthDayInternal(gmonthDay, this);
    }

    /**
     * Sets all the fields by reading the values in an array
     * <p>if a Time Zone is specificied it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     * @param values an array of shorts with the values
     * the array is supposed to be of length 2 and ordered like
     * the following:
     * <ul>
     *      <li>Month</li>
     *      <li>Day</li>
     * </ul>
     */
    public void setValues(short[] values) {
        if (values.length != 2) {
            throw new IllegalArgumentException("GMonthDay#setValues: not the right number of values");
        }
        this.setMonth(values[0]);
        this.setDay(values[1]);
    }

    /**
     * Returns an array of short with all the fields that describe
     * this gMonthDay type.
     * <p>Note:the time zone is not included.
     * @return  an array of short with all the fields that describe
     * this Date type.
     */
    public short[] getValues() {
        short[] result = new short[2];
        result[0] = this.getMonth();
        result[1] = this.getDay();
        return result;
    } //getValues

    /**
     * converts this gMonthDay into a local java Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate() {
        SimpleDateFormat df = new SimpleDateFormat(MONTHDAY_FORMAT);
        setDateFormatTimeZone(df);

        java.util.Date date = null;
        try {
            date = df.parse(this.toString());
        } catch (ParseException e) {
            //this can't happen since toString() should return the proper string format
            e.printStackTrace();
            return null;
        }

        return date;
    } //toDate()

    /**
     * convert this gMonthDay to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e --MM-DD(Z|(+|-)hh:mm)
     * @return a string representing this Date
     */
     public String toString() {
         StringBuffer result = new StringBuffer("--");

         if ((this.getMonth()/10) == 0) {
             result.append(0);
         }
         result.append(this.getMonth());

         result.append("-");

         if ((this.getDay()/10) == 0) {
             result.append(0);
         }
         result.append(this.getDay());

         appendTimeZoneString(result);

         return result.toString();
     } //toString

     /**
      * parse a String and convert it into an java.lang.Object
      * @param str the string to parse
      * @return an Object represented by the string
      * @throws ParseException a parse exception is thrown if the string to parse
      *                        does not follow the rigth format (see the description
      *                        of this class)
      */
     public static Object parse(String str) throws ParseException {
         return parseGMonthDay(str);
     }

     /**
      * parse a String and convert it into a gMonthDay.
      * @param str the string to parse
      * @return the Date represented by the string
      * @throws ParseException a parse exception is thrown if the string to parse
      *                        does not follow the rigth format (see the description
      *                        of this class)
      */
     public static GMonthDay parseGMonthDay(String str) throws ParseException {
         return parseGMonthDayInternal(str, null);
     }

     private static GMonthDay parseGMonthDayInternal(String str, GMonthDay result) throws ParseException {
         if (str == null) {
             throw new IllegalArgumentException("The string to be parsed must not be null.");
         }

         if (result == null) {
             result = new GMonthDay();
         }

         char[] chars = str.toCharArray();

         int idx = 0;
         if (chars[idx] != '-' || chars[idx+1] != '-') {
             throw new ParseException(BAD_GMONTHDAY+str+"\nA gMonthDay must follow the pattern --MM-DD(Z|((+|-)hh:mm)).", 0);
         }

         idx += 2;

         // Month
         if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
             throw new ParseException(BAD_GMONTHDAY+str+"\nThe Month must be 2 digits long", idx);
         }

         short value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
         result.setMonth(value1);

         idx += 2;

         if (chars[idx] != '-') {
             throw new ParseException(BAD_GMONTHDAY+str+"\nA gMonthDay must follow the pattern --MM-DD(Z|((+|-)hh:mm)).", 0);
         }

         idx++;

         // Day
         if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
             throw new ParseException(BAD_GMONTHDAY+str+"\nThe Day must be 2 digits long", idx);
         }

         value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
         result.setDay(value1);

         idx += 2;

         parseTimeZone(str, result, chars, idx, BAD_GMONTHDAY);

         return result;
     } //parse

     /////////////////////////// DISALLOWED METHODS ///////////////////////////

     public void setNegative() {
         String err = "org.exolab.castor.types.GMonthDay cannot be negative.";
         throw new OperationNotSupportedException(err);
     }
     public short getCentury() {
         String err = "org.exolab.castor.types.GMonthDay does not have a Century field.";
         throw new OperationNotSupportedException(err);
     }
     public void setCentury(short century) {
         String err = "org.exolab.castor.types.GMonthDay does not have a Century field.";
         throw new OperationNotSupportedException(err);
     }
     public short getYear() {
         String err = "org.exolab.castor.types.GMonthDay does not have a Year field.";
         throw new OperationNotSupportedException(err);
     }
     public void setYear(short year) {
         String err = "org.exolab.castor.types.GMonthDay does not have a Year field.";
         throw new OperationNotSupportedException(err);
     }
     public short getHour() {
         String err = "org.exolab.castor.types.GMonthDay does not have an Hour field.";
         throw new OperationNotSupportedException(err);
     }
     public void setHour(short hour) {
         String err = "org.exolab.castor.types.GMonthDay does not have an Hour field.";
         throw new OperationNotSupportedException(err);
     }
     public short getMinute() {
         String err = "org.exolab.castor.types.GMonthDay does not have a Minute field.";
         throw new OperationNotSupportedException(err);
     }
     public void setMinute(short minute) {
         String err = "org.exolab.castor.types.GMonthDay does not have a Minute field.";
         throw new OperationNotSupportedException(err);
     }
     public short getSeconds() {
         String err = "org.exolab.castor.types.GMonthDay does not have a Seconds field.";
         throw new OperationNotSupportedException(err);
     }
     public void setSecond(short second) {
         String err = "org.exolab.castor.types.GMonthDay does not have a Seconds field.";
         throw new OperationNotSupportedException(err);
     }
     public short getMilli() {
         String err = "org.exolab.castor.types.GMonthDay does not have a Milliseconds field.";
         throw new OperationNotSupportedException(err);
     }
     public void setMilliSecond(short millisecond) {
         String err = "org.exolab.castor.types.GMonthDay does not have a Milliseconds field.";
         throw new OperationNotSupportedException(err);
     }

}
