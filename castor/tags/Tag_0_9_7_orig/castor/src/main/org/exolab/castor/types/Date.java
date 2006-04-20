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

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.GregorianCalendar;
import java.util.TimeZone;
/**
 * Describe an XML schema Date.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>(-)CCYY-MM-DD(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 */

public class Date extends DateTimeBase{

    /**
     * Flag indicating that we are still looking for a year
     */
    private static final int YEAR_FLAG = 15;

   /**
    * Flag indicating that we are still looking for a month
    */
    private static final int MONTH_FLAG = 7;

   /**
    * Flag indicating that we are still looking for a day
    */
    private static final int DAY_FLAG = 3;

   /**
    * Flag indicating that we are still looking for a timeZone
    */
    private static final int TIMEZONE_FLAG = 1;

    private static final String BAD_DATE = "Bad Date format: ";

    /**
     * The Date Format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public Date() {
    }

    /**
     * Constructs a XML Schema Date instance given all the values of
     * the different fields.
     * By default a Date is not UTC and is local.
     * @param values an array of shorts that represent the different fields of Time.
     */

    public Date(short[] values) {
        setValues(values);
    }

    /**
     * This constructor is used to convert a long value representing a Date
     * to a new org.exolab.castor.types.Date instance.
     * <p>Note : all the information concerning the time part of
     * the java.util.Date is lost since a W3C Schema Date only represents
     * CCYY-MM-YY
     * @param dateAsLong Date represented in from of a long value.
     */
    public Date (long dateAsLong) {
    	this (new java.util.Date (dateAsLong));
    }
    
    /**
     * This constructor is used to convert a java.util.Date into
     * a new org.exolab.castor.types.Date
     * <p>Note : all the information concerning the time part of
     * the java.util.Date is lost since a W3C Schema Date only represents
     * CCYY-MM-YY
     */
    public Date(java.util.Date dateRef) {
        GregorianCalendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTime(dateRef);
        setCentury((short) (tempCalendar.get(Calendar.YEAR)/100));
        setYear((short) (tempCalendar.get(Calendar.YEAR)%100));

        //we need to add 1 to the Month value returned by GregorianCalendar
        //because 0<MONTH<11 (i.e January is 0)
        setMonth((short) (tempCalendar.get(Calendar.MONTH)+1));
        setDay((short) (tempCalendar.get(Calendar.DAY_OF_MONTH)));
    } //Date(java.util.Date)

    /**
     * Constructs a date from a string
     * @param date the string representing the date
     */
    public Date(String date) throws java.text.ParseException {
        this();
        parseDateInternal(date, this);
    }
    /**
     * Sets all the fields by reading the values in an array
     * <p>if a Time Zone is specificied it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     * @param values an array of shorts with the values
     * the array is supposed to be of length 4 and ordered like
     * the following:
     * <ul>
     *      <li>century</li>
     *      <li>year</li>
     *      <li>month</li>
     *      <li>day</li>
     * </ul>
     *
     */
     public void setValues(short[] values) {
         if (values.length != 4)
             throw new IllegalArgumentException("Date#setValues: not the right number of values");
        this.setCentury(values[0]);
        this.setYear(values[1]);
        this.setMonth(values[2]);
        this.setDay(values[3]);
     }


    /**
     * Returns an array of short with all the fields that describe
     * this Date type.
     * <p>Note:the time zone is not included.
     * @return  an array of short with all the fields that describe
     * this Date type.
     */
    public short[] getValues() {
        short[] result = null;
        result = new short[4];
        result[0] = this.getCentury();
        result[1] = this.getYear();
        result[2] = this.getMonth();
        result[3] = this.getDay();
        return result;
    } //getValues



    /**
     * converts this Date into a local java Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate(){

        java.util.Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        // Set the time zone
        if ( isUTC() ) {
            SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");
            int offset = 0;
            offset = (int) ( (this.getZoneMinute() + this.getZoneHour()*60)*60*1000);
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
     * Converts this date into a long value.
     * @return This date instance as a long value.
     */
    public long toLong () {
    	java.util.Date date = toDate();
    	return date.getTime();
    }
    
    /**
     * convert this Date to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e (+|-)CCYY-MM-DD
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

        result.append('-');
        if ((this.getDay()/10) == 0 )
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
        return parseDate(str);
    }

   /**
    * parse a String and convert it into a Date.
    * @param str the string to parse
    * @return the Date represented by the string
    * @throws ParseException a parse exception is thrown if the string to parse
    *                        does not follow the rigth format (see the description
    *                        of this class)
    */
    public static Date parseDate(String str) throws ParseException {
        Date result = new Date();
        return parseDateInternal(str, result);
     }

    private static Date parseDateInternal(String str, Date result) throws ParseException {

        if (str == null)
             throw new IllegalArgumentException("The string to be parsed must not "
                                                +"be null.");
        if (result == null)
            result = new Date();
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
        //-- --(char): = b1111 (15)
        int flags = YEAR_FLAG;

        while (idx < chars.length) {
             char ch = chars[idx++];

             switch (ch) {

                 case '-' :
                       //year
                       if (flags == YEAR_FLAG) {
                          if ((number != 0) || (number2 != 0)) {
                              if (has2Digits)
                                 result.setCentury(number);
                              else throw new ParseException(BAD_DATE+str+"\nThe Century field must have 2 digits.",idx);
                              //must test number2
                              result.setYear(number2);
                              number2 = -1;
                              flags =  MONTH_FLAG;
                          } else throw new ParseException(BAD_DATE+str+"\n'0000' is not allowed as a year.",idx);
                       }
                       //month
                       else if (flags == MONTH_FLAG) {
                          if ( (has2Digits) && (number2 == -1) ) {
                              result.setMonth(number);
                              flags = DAY_FLAG;
                          } else throw new ParseException(BAD_DATE+str+"\nThe month field must have 2 digits.",idx);
                       }
                       //time zone
                       else if ( (flags == DAY_FLAG) && (number2 == -1) ) {
                           if (has2Digits) {
                               result.setUTC();
                               result.setZoneNegative(true);
                               result.setDay(number);
                               flags = TIMEZONE_FLAG;
                           }else throw new ParseException(BAD_DATE+str+"\nThe day field must have 2 digits.",idx);
                       }
                       else throw new ParseException(BAD_DATE+str+"\n '-' "+DateTimeBase.WRONGLY_PLACED,idx);

                       hasNumber = false;
                       has2Digits = false;
                       break;

                 case 'Z' :
                      if (flags != DAY_FLAG)
                         throw new ParseException("'Z' "+DateTimeBase.WRONGLY_PLACED,idx);
                      else
                          result.setUTC();
                      break;

                 case '+' :
                    if (flags != DAY_FLAG)
                        throw new ParseException("'+' "+DateTimeBase.WRONGLY_PLACED,idx);
                    else {
                        if ((has2Digits)&& (number2 == -1) ) {
                          result.setDay(number);
                          result.setUTC();
                          flags = TIMEZONE_FLAG;
                          hasNumber = false;
                          has2Digits = false;
                      }
                      else throw new ParseException(BAD_DATE+str+"\nThe day field must have 2 digits.",idx);
                    }
                    break;

                 case ':' :
                     if (flags != TIMEZONE_FLAG)
                        throw new ParseException(BAD_DATE+str+"\n':' "+DateTimeBase.WRONGLY_PLACED,idx);
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
                            if (has2Digits)
                                 number2 = (short) ((number2*10)+(ch-48));
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
        if (flags!= DAY_FLAG && flags != 0)
            throw new ParseException(BAD_DATE+str+"\nA date must follow the pattern CCYY-MM-DD(Z|((+|-)hh:mm)).",idx);
        else if (flags == DAY_FLAG) {
            if  ( (has2Digits) && (number2 == -1) )
                result.setDay(number);
            else
               throw new ParseException(BAD_DATE+str+"\nThe day field must have 2 digits.",idx);
        }

        else if (flags == 0) {
            if (number != -1)
                result.setZone(number2,number);
            else throw new ParseException(str+"\n In a time zone, the minute field must always be present.",idx);
        }
        return result;

    }//parse

    /////////////////////////DISALLOW TIME METHODS/////////////////////////////
    public short getHour(){
        String err = "Date: couldn't access to the Hour field.";
        throw new OperationNotSupportedException(err);
    }
    public short getMinute(){
        String err = "Date: couldn't access to the Minute field.";
        throw new OperationNotSupportedException(err);
    }
    public short getSeconds(){
        String err = "Date: couldn't access to the Second field.";
        throw new OperationNotSupportedException(err);
    }
    public short getMilli() {
        String err = "Date: couldn't access to the Millisecond field.";
        throw new OperationNotSupportedException(err);
    }
    public void setHour(short hour){
        String err = "Date: couldn't access to the Hour field.";
        throw new OperationNotSupportedException(err);
    }
    public void setMinute(short minute){
        String err = "Date: couldn't access to the Minute field.";
        throw new OperationNotSupportedException(err);
    }
    public void setSecond(short second) {
        String err = "Date: couldn't access to the second field.";
        throw new OperationNotSupportedException(err);
    }

    public void setMilliSecond(short millisecond) {
        String err = "Date: couldn't access to the Millisecond field.";
        throw new OperationNotSupportedException(err);
    }
}// Date