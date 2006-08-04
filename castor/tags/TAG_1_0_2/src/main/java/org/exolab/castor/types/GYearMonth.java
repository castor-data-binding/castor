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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author              Changes
 * 04/18/2002   Arnaud              constructor with string
 * 05/24/2001   Arnaud Blandin      Created
 */

package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Describe an XML schema gYearMonth type.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>(-)CCYY-MM(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 */


public class GYearMonth extends Date {
    /** SerialVersionUID */
    private static final long serialVersionUID = -8864050276805766473L;

    /**
     * The gYearMonth format.
     */
    private static final String YEARMONTH_FORMAT = "yyyy-MM";

    private static final String BAD_GYEARMONTH = "Bad gYearMonth format: ";


    /**
     * public only for the generated source code
     */
    public GYearMonth() {
    }

    /**
     * Instantiates a new gYearMonth given the value
     * of the month and the value of the day.
     * @param century the month value
     * @param year the year value
     * @param month the month value
     */
    public GYearMonth(short century,  short year, short month) {
         this();
         this.setCentury(century);
         this.setYear(year);
         this.setMonth(month);
    }

    /**
     * Instantiates a new gYearMonth given the value
     * of the month and the value of the day.
     * @param year the year value
     * @param month the month value
     */
    public GYearMonth(int year, int month) {
         short century = (short) (year/100);
         year = year % 100;
         setCentury(century);
         setYear((short)year);
         setMonth((short)month);
    }

    /**
     * Constructs a XML Schema GYearMonth instance given all the values of
     * the different fields.
     * By default a GYearMonth is not UTC and is local.
     * @param values an array of shorts that represent the different fields of Time.
     */

    public GYearMonth(short[] values) {
        setValues(values);
    }

    /**
     * Constructs a GYearMonth given a string representation
     * @param gyearMonth the string representation of the GYearMonth to instantiate
     */
     public GYearMonth(String gyearMonth) throws ParseException {
         this();
         parseGYearMonthInternal(gyearMonth, this);
     }

    /**
     * Sets all the fields by reading the values in an array
     * <p>if a Time Zone is specificied it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     * @param values an array of shorts with the values
     * the array is supposed to be of length 3 and ordered like
     * the following:
     * <ul>
     *      <li>century</li>
     *      <li>year</li>
     *      <li>month</li>
     * </ul>
     *
     */
     public void setValues(short[] values) {
         if (values.length != 3)
             throw new IllegalArgumentException("GYearMonth#setValues: not the right number of values");
        this.setCentury(values[0]);
        this.setYear(values[1]);
        this.setMonth(values[2]);
     }


    /**
     * Returns an array of short with all the fields that describe
     * this gYearMonth type.
     * <p>Note:the time zone is not included.
     * @return  an array of short with all the fields that describe
     * this Date type.
     */
    public short[] getValues() {
        short[] result = null;
        result = new short[3];
        result[0] = this.getCentury();
        result[1] = this.getYear();
        result[2] = this.getMonth();
        return result;
    } //getValues



    /**
     * converts this gYearMonth into a local java Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate(){

        java.util.Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(YEARMONTH_FORMAT);
        // Set the time zone
        if ( isUTC() ) {
            SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");
            int offset = 0;
            offset = ( (this.getZoneMinute() + this.getZoneHour()*60)*60*1000);
            offset = isZoneNegative() ? -offset : offset;
            timeZone.setRawOffset(offset);
            timeZone.setID(TimeZone.getAvailableIDs(offset)[0]);
            df.setTimeZone(timeZone);
        }

        try {
            date = df.parse(this.toString());
        } catch (ParseException e) {
           //this can't happen since toString() should return the proper
           //string format
           e.printStackTrace();
           return null;
        }
        return date;
    }//toDate()

    /**
     * convert this gYearMonth to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e (+|-)CCYY-MM(Z|(+|-)hh:mm)
     * @return a string representing this Date
     */
     public String toString() {

        StringBuffer result = new StringBuffer();
        if (isNegative())
            result.append('-');

        result.append(this.getCentury());
        if (result.length() == 1)
            result.insert(0,0);

        if ((this.getYear()/10) == 0)
            result.append(0);
        result.append(this.getYear());

        result.append('-');
        if ((this.getMonth() / 10) == 0 )
           result.append(0);
        result.append(this.getMonth());

        if (isUTC()) {
            //By default we append a 'Z' to indicate UTC
            if ( (this.getZoneHour() == 0) && (this.getZoneMinute() ==0) )
                result.append('Z');
            else {
                StringBuffer timeZone = new StringBuffer();
                if (isZoneNegative())
                   timeZone.append('-');
                else timeZone.append('+');

                if ((this.getZoneHour()/10) == 0)
                    timeZone.append(0);
                timeZone.append(this.getZoneHour());

                timeZone.append(':');
                if ((this.getZoneMinute()/10) == 0)
                    timeZone.append(0);
                timeZone.append(this.getZoneMinute());

               result.append(timeZone.toString());
               timeZone = null;
            }
        }
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
        return parseGYearMonth(str);
    }

    /**
     * parse a String and convert it into a gYearMonth.
     * @param str the string to parse
     * @return the Date represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static GYearMonth parseGYearMonth(String str) throws ParseException {
        GYearMonth result = new GYearMonth();
        return parseGYearMonthInternal(str, result);
    }

    private static GYearMonth parseGYearMonthInternal(String str, GYearMonth result)
        throws ParseException
    {
        if (str == null)
             throw new IllegalArgumentException("The string to be parsed must not "
                                                +"be null.");
        if (result == null)
              result = new GYearMonth();
        char[] chars = str.toCharArray();
        int idx = 0;

        if (chars[idx] == '-') {
             idx++;
             result.setNegative();
        }

        boolean hasNumber = false;
        boolean has2Digits = false;
        short number = 0;
        short number2 = 0;
        //-- parse flags
        //-- -(char): = b111 (7)
        int flags = 7;

        while (idx < chars.length) {
             char ch = chars[idx++];

             switch (ch) {

                 case '-' :
                       if (flags == 7) {
                           if ((number != 0) || (number2 != 0)) {
                               if (has2Digits)
                                   result.setCentury(number);
                               else throw new ParseException(BAD_GYEARMONTH+str+"\nThe Century field must have 2 digits.",idx);
                               //must test number2
                               result.setYear(number2);
                               flags = 3;
                               number2 = -1;
                           } else throw new ParseException(BAD_GYEARMONTH+str+"\n'0000' is not allowed as a year.",idx);
                       }
                       else if (flags == 3) {
                           if ((has2Digits) && (number2 == -1)){
                               result.setMonth(number);
                               flags = 1;
                               result.setUTC();
                               result.setZoneNegative(true);
                           } else throw new ParseException(BAD_GYEARMONTH+str+"\nThe month field must have 2 digits.",idx);
                       }
                       else   throw new ParseException(BAD_GYEARMONTH+str+"\nA gYearMonth must follow the pattern CCYY-MM-DD(Z|((+|-)hh:mm)).",idx);
                       hasNumber = false;
                       has2Digits = false;
                       break;

                 case 'Z' :
                      if (flags != 3)
                         throw new ParseException(BAD_GYEARMONTH+str+"'Z' "+WRONGLY_PLACED,idx);
                      result.setUTC();
                      break;

                 case '+' :
                    if (flags != 3)
                        throw new ParseException(BAD_GYEARMONTH+str+"'+' "+WRONGLY_PLACED,idx);
                    if (has2Digits && number2 == -1){
                         result.setMonth(number);
                         flags = 1;
                         result.setUTC();
                     } else throw new ParseException(BAD_GYEARMONTH+str+"\nThe month field must have 2 digits.",idx);
                    hasNumber = false;
                    has2Digits = false;
                    break;

                 case ':' :
                     if (flags != 1)
                        throw new ParseException(BAD_GYEARMONTH+str+"':' "+WRONGLY_PLACED,idx);
                     number2 = number;
                     number = -1;
                     flags = 0;
                     hasNumber = false;
                     has2Digits = false;
                     break;
                 default:
                    //make sure we have a digit
                    if ( ('0' <= ch) && (ch <= '9')) {
                        if (hasNumber) {
                            if (has2Digits) {
                                 number2 = (short) ((number2*10)+(ch-48));
                            }
                            else {
                                number = (short)((number*10)+(ch-48));
                                has2Digits = true;
                            }
                        }
                        else {
                            hasNumber = true;
                            number = (short) (ch-48);
                        }
                    }
                    else
                        throw new ParseException (str+": Invalid character: "+ch, idx);
                    break;
             }//switch
        }//while
        if (flags!=3 && flags != 0)
            throw new ParseException(BAD_GYEARMONTH+str+"\nA gYearMonth must follow the pattern CCYY-MM(Z|((+|-)hh:mm)).",idx);
        else if (flags == 3) {
            if ((has2Digits) && (number2 == -1))
                result.setMonth(number);
            else throw new ParseException(BAD_GYEARMONTH+str+"\nThe month field must have 2 digits.",idx);

        }

        else if (flags == 0) {
            if (number != -1)
                result.setZone(number2,number);
            else throw new ParseException(str+"\n In a time zone, the minute field must always be present.",idx);
        }
        return result;

    }//parse

    ///////////////////////////DISALLOW Day methods////////////////////////////
    public short getDay() {
        String err = "GYearMonth: couldn't access to the Day field.";
        throw new OperationNotSupportedException(err);
    }

    public void setDay(short day) {
        String err = "GYearMonth: couldn't access to the Day field.";
        throw new OperationNotSupportedException(err);
    }
}