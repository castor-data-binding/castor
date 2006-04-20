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
 * 05/24/2001   Arnaud Blandin      Created
 */

package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.SimpleTimeZone;
import java.util.GregorianCalendar;
/**
 * Describe an XML schema gMonthDay type.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>--MM-DD(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 */

public class GMonthDay extends Date {

   /**
    * The gMonthDay format
    */
    private static final String MONTHDAY_FORMAT = "-MM-dd";

    /**
     * public only for the generated source code
     */
    public GMonthDay() {
    }

    /**
     * Instantiates a new gMonthDay given the value
     * of the month and the value of the day.
     * @param month the month value
     * @param day the day value
     */
    public GMonthDay(short month,  short day) {
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
        this();
        this.setValues(values);
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
     *
     */
     public void setValues(short[] values) {
         if (values.length != 2)
             throw new IllegalArgumentException("GYear#setValues: not the right number of values");
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
        short[] result = null;
        result = new short[2];
        result[0] = this.getCentury();
        result[1] = this.getYear();
        return result;
    } //getValues



    /**
     * converts this gMonthDay into a local java Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate(){

        java.util.Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(MONTHDAY_FORMAT);
        // Set the time zone
        if ( isUTC() ) {
            SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");
            int offset = 0;
            offset = (int) ( (this.getZoneMinute() + this.getZoneHour()*60)*60*1000);
            offset = isZoneNegative() ? -offset : offset;
            timeZone.setRawOffset(offset);
            timeZone.setID(timeZone.getAvailableIDs(offset)[0]);
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
     * convert this gMonthDay to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e --MM-DD(Z|(+|-)hh:mm)
     * @return a string representing this Date
     */
     public String toString() {

        StringBuffer result = new StringBuffer();
        result.append('-');
        result.append('-');

        result.append(this.getMonth());
        if (result.length() == 3)
            result.insert(2,0);

        result.append('-');
        if ((this.getDay()/10) == 0)
            result.append(0);
        result.append(this.getDay());

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

        if (str == null)
             throw new IllegalArgumentException("The string to be parsed must not "
                                                +"be null.");
        GMonthDay result = new GMonthDay();
        char[] chars = str.toCharArray();
        int idx = 0;

        boolean hasNumber = false;
        boolean has2Digits = false;
        short number = -1;
        short number2 = 0;
        //-- parse flags
        //-- ---(char): = b11111 (31)
        int flags = 31;

        while (idx < chars.length) {
             char ch = chars[idx++];

             switch (ch) {

                 case '-' :
                       if ( (flags == 31) && (number == -1))
                          flags = 15;
                       else if ((flags == 15)  && (number == -1))
                          flags = 7;
                       else if  ((flags == 7) && (number != -1) ) {
                           if (has2Digits) {
                               result.setMonth(number);
                               flags = 3;
                           } else throw new ParseException("Bad gMonthDay Format:"+str+"\nThe month field must have 2 digits.", idx);
                       }
                       else if (flags == 3) {
                           if (has2Digits) {
                               result.setDay(number);
                               result.setUTC();
                               result.setZoneNegative();
                               flags = 1;
                           } else throw new ParseException("Bad gMonthDay Format:"+str+"\nThe day field must have 2 digits.",idx);
                       }
                       else throw new ParseException("Bad gMonthDay Format: "+str+"\nA gMonthDay must follow the pattern --MM-DD(Z|((+|-)hh:mm)).",idx);

                       hasNumber = false;
                       has2Digits = false;
                       break;

                 case 'Z' :
                      if (flags != 3)
                          throw new ParseException("Bad gMonthDay Format: "+str+"\n'Z' is wrongly placed",idx);
                      else
                          result.setUTC();
                      break;

                case '+' :
                    if (flags != 3)
                        throw new ParseException("Bad gMonthDay Format: "+str+"\n'+' is wrongly placed",idx);
                    else {
                        if (has2Digits) {
                          result.setDay(number);
                          result.setUTC();
                          flags = 1;
                          hasNumber = false;
                          has2Digits = false;
                      }
                      else throw new ParseException("Bad gMonthDay Format:"+str+"\nTthe day field must have 2 digits.",idx);
                    }
                    break;

                 case ':' :
                     if (flags != 1)
                        throw new ParseException("Bad gMonthDay Format: "+str+"\n':' is wrongly placed",idx);
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
                            number = (short)((number*10)+(ch-48));
                            has2Digits = true;
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
            throw new ParseException("Bad gMonthDay Format: "+str+"\nA gMonthDay must follow the pattern --MM-DD(Z|((+|-)hh:mm)).",idx);
        else if (flags == 3) {
            if (has2Digits)
                result.setDay(number);
            else
               throw new ParseException("Bad gMonthDay Format:"+str+"\nThe day field must have 2 digits.",idx);
        }

        else if (flags == 0) {
            if (number != -1)
                result.setZone(number2,number);
            else throw new ParseException(str+"\n In a time zone, the minute field must always be present.",idx);
        }
        return result;

    }//parse

    ///////////////////////////DISALLOW YEAR METHODS///////////////////////////
     public short getCentury() {
        String err = "GMonthDay: couldn't access to the Century field.";
        throw new OperationNotSupportedException(err);
    }

    public void setCentury(short century) {
        String err = "GMonthDay: couldn't access to the Century field.";
        throw new OperationNotSupportedException(err);
    }

    public short getYear() {
        String err = "GMonthDay: couldn't access to the Year field.";
        throw new OperationNotSupportedException(err);
    }

    public void setYear(short year) {
        String err = "GMonthDay: couldn't access to the Year field.";
        throw new OperationNotSupportedException(err);
    }

    public void setNegative() {
        String err = "GMonthDay: couldn't set the type to be negative.";
        throw new OperationNotSupportedException(err);
    }
}