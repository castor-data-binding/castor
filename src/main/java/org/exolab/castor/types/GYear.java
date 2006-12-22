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
 * 05/24/2001   Arnaud Blandin      Created
 */
package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Describe an XML schema gYear type.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>(-)CCYY(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$
 */
public class GYear extends DateTimeBase {

    /** SerialVersionUID */
    private static final long serialVersionUID = -8977039151222106864L;

    /** The gYear SimpleDateFormat string. */
    private static final String YEAR_FORMAT = "yyyy";
    /** Prefix of any complaint we make. */
    private static final String BAD_GYEAR = "Bad gYear format: ";

    /**
     * public only for the generated source code
     */
    public GYear() {
        // Nothing to do
    }

    /**
     * Instantiates a new gYear given the value of the century and year.
     * @param century the month value
     * @param year the year value
     */
    public GYear(short century,  short year) {
         setCentury(century);
         setYear(year);
    }

    /**
     * Instantiates a new gYear given the value of the year.  Here, the year
     * is the normal representation of a year, that is, a four-digit value.
     * @param year the year value
     */
    public GYear(int year) {
         short century = (short) (year / 100);
         year = year % 100;
         setCentury(century);
         setYear((short)year);
    }

    /**
     * Constructs a XML Schema GYear instance given all the values of
     * the different fields.
     * By default a GYear is not UTC and is local.
     * @param values an array of shorts that represent the different fields of Time.
     */
    public GYear(short[] values) {
        this.setValues(values);
    }

    /**
     * Construct a GYear from a string value
     * @param gyear the string representation of the GYear to instantiate
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public GYear(String gyear) throws ParseException {
        parseGYearInternal(gyear, this);
    }

    /**
     * Sets all the fields by reading the values in an array
     * <p>if a Time Zone is specificied it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     * @param values an array of shorts with the values
     * the array is supposed to be of length 2 and ordered like
     * the following:
     * <ul>
     *      <li>century</li>
     *      <li>year</li>
     * </ul>
     */
    public void setValues(short[] values) {
        if (values.length != 2) {
            throw new IllegalArgumentException("GYear#setValues: not the right number of values");
        }
        this.setCentury(values[0]);
        this.setYear(values[1]);
    }

    /**
     * Returns an array of short with all the fields that describe
     * this gYear type.
     * <p>Note:the time zone is not included.
     * @return  an array of short with all the fields that describe
     * this Date type.
     */
     public short[] getValues() {
         short[] result = new short[2];
         result[0] = this.getCentury();
         result[1] = this.getYear();
         return result;
     } //getValues

    /**
     * converts this gYear into a local java Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate() {
        SimpleDateFormat df = new SimpleDateFormat(YEAR_FORMAT);
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
     * convert this gYear to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e (+|-)CCYY-MM(Z|(+|-)hh:mm)
     * @return a string representing this Date
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        if (isNegative()) {
            result.append('-');
        }

        if ((this.getCentury()/10) == 0) {
            result.append(0);
        }
        result.append(this.getCentury());

        if ((this.getYear()/10) == 0) {
            result.append(0);
        }
        result.append(this.getYear());

        appendTimeZoneString(result);

        return result.toString();
    }//toString

    /**
     * parse a String and convert it into an java.lang.Object
     * @param str the string to parse
     * @return an Object represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
     public static Object parse(String str) throws ParseException {
         return parseGYear(str);
     }

    /**
     * parse a String and convert it into a gYear.
     * @param str the string to parse
     * @return the Date represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
     public static GYear parseGYear(String str) throws ParseException {
         return parseGYearInternal(str, null);
     }

     private static GYear parseGYearInternal(String str, GYear result) throws ParseException{
         if (str == null) {
             throw new IllegalArgumentException("The string to be parsed must not be null.");
         }

         if (result == null) {
             result = new GYear();
         }

         char[] chars = str.toCharArray();

         int idx = 0;

         // Negative?
         if (chars[idx] == '-') {
             result.setNegative();
             idx++;
         }

         // Century
         if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])
             || !Character.isDigit(chars[idx + 2]) || !Character.isDigit(chars[idx + 3])) {
             throw new ParseException(BAD_GYEAR+str+"\nA gYear must follow the pattern CCYY(Z|((+|-)hh:mm)).",idx);
         }

         short value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
         short value2 = (short) ((chars[idx+2] - '0') * 10 + (chars[idx+3] - '0'));
         if (value1 == 0 && value2 == 0) {
             throw new ParseException(BAD_GYEAR+str+"\n'0000' is not allowed as a year.", idx);
         }

         result.setCentury(value1);
         result.setYear(value2);

         idx += 4;

         parseTimeZone(str, result, chars, idx, BAD_GYEAR);

         return result;
     }//parse

     /////////////////////////// DISALLOWED METHODS ///////////////////////////

     public boolean hasMonth() {
         return false;
     }
     public short getMonth() {
         String err = "org.exolab.castor.types.GYear does not have a Month field.";
         throw new OperationNotSupportedException(err);
     }
     public void setMonth(short year) {
         String err = "org.exolab.castor.types.GYear does not have a Month field.";
         throw new OperationNotSupportedException(err);
     }
     public boolean hasDay() {
         return false;
     }
     public short getDay() {
         String err = "org.exolab.castor.types.GYear does not have a Day field.";
         throw new OperationNotSupportedException(err);
     }
     public void setDay(short month) {
         String err = "org.exolab.castor.types.GYear does not have a Day field.";
         throw new OperationNotSupportedException(err);
     }
     public boolean hasHour() {
         return false;
     }
     public short getHour() {
         String err = "org.exolab.castor.types.GYear does not have an Hour field.";
         throw new OperationNotSupportedException(err);
     }
     public void setHour(short hour) {
         String err = "org.exolab.castor.types.GYear does not have an Hour field.";
         throw new OperationNotSupportedException(err);
     }
     public boolean hasMinute() {
         return false;
     }
     public short getMinute() {
         String err = "org.exolab.castor.types.GYear does not have a Minute field.";
         throw new OperationNotSupportedException(err);
     }
     public void setMinute(short minute) {
         String err = "org.exolab.castor.types.GYear does not have a Minute field.";
         throw new OperationNotSupportedException(err);
     }
     public boolean hasSeconds() {
         return false;
     }
     public short getSeconds() {
         String err = "org.exolab.castor.types.GYear does not have a Seconds field.";
         throw new OperationNotSupportedException(err);
     }
     public void setSecond(short second) {
         String err = "org.exolab.castor.types.GYear does not have a Seconds field.";
         throw new OperationNotSupportedException(err);
     }
     public boolean hasMilli() {
         return false;
     }
     public short getMilli() {
         String err = "org.exolab.castor.types.GYear does not have a Milliseconds field.";
         throw new OperationNotSupportedException(err);
     }
     public void setMilliSecond(short millisecond) {
         String err = "org.exolab.castor.types.GYear does not have a Milliseconds field.";
         throw new OperationNotSupportedException(err);
     }

}
