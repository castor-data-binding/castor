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
 * Date         Author          Changes
 * 05/22/2001   Arnaud Blandin  Created
 */
package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Date;

/**
 * <p>Describes an XML schema Time.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>(-)hh:mm:ss.sss(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 * @see DateTimeBase
 */

public class Time extends DateTimeBase {


    /** The Time Format used by the toDate() method */
    private static final String TIME_FORMAT = "HH:mm:ss.SSS";

    public Time() {
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
     * @param the long value that represents the time instance.
     */
    public Time (long l) {
        if (l>8640000L)
            throw new IllegalArgumentException("Bad Time: the long value can't represent more than 24h.");
        this.setHour((short)(l/360000));
        l = l % 360000;
        this.setMinute((short)(l/6000));
        l = l % 6000;
        this.setSecond((short)(l / 100), (short)(l%100));
    }

    /**
     * Sets all the fields by reading the values in an array.
     * <p>if a Time Zone is specificied it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     * @param values an array of shorts with the values
     * the array is supposed to be of length 4 and ordered like that:
     * <ul>
     *      <li>hour</li>
     *      <li>minute</li>
     *      <li>second</li>
     *      <li>millisecond</li>
     * </ul>
     */
     public void setValues(short[] values) {
        if (values.length != 4)
             throw new IllegalArgumentException("Time#setValues: not the right number of values");
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
        short[] result = null;
        result = new short[4];
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
    public Date toDate(){

        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);
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
     * convert this Time to a string
     * The format is defined by W3C XML Schema Recommendation and ISO8601
     * i.e (-)hh:mm:ss.sss(Z|(+|-)hh:mm)
     * @return a string representing this Time
     */
    public String toString() {

        StringBuffer result = new StringBuffer();
         if (isNegative())
           result.append('-');

        //two figures are required
        if ((this.getHour()/10) == 0)
            result.append(0);
        result.append(this.getHour());

        result.append(':');
        if ((this.getMinute() / 10) == 0 )
           result.append(0);
        result.append(this.getMinute());

        result.append(':');
        if ((this.getSeconds()/10) == 0 )
            result.append(0);
        result.append(this.getSeconds());
        result.append('.');
        result.append(this.getMilli());

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

        if (str == null)
             throw new IllegalArgumentException("The string to be parsed must not"
                                                +"be null.");
        Time result = new Time();
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
        //-- ::(char): = b1111 (15)
        int flags = 15;

        while (idx < chars.length) {
             char ch = chars[idx++];

             switch (ch) {

                 case ':' :
                       //the string representation must have 2 digits
                       if (!has2Digits)
                           throw new ParseException("Bad Time format:"+str+"\nA time field must have 2 digits.",idx);
                       if (flags == 15) {
                          result.setHour(number);
                          flags =  7;
                       }
                       else if (flags == 7) {
                          result.setMinute(number);
                          flags = 3;
                       }
                       else if (flags == 3) {
                           result.setSecond(number2, number);
                       }
                       else if (flags == 1) {
                           number2 = number;
                           number = -1;
                           flags = 0;
                       }
                       else throw new ParseException("Bad Time Format:"+str+"\nA Time must follow the pattern hh:mm:ss.s(Z|((+|-)hh:mm)).",idx);
                       hasNumber = false;
                       has2Digits = false;
                       break;

                 case '.' :
                      if (flags != 3)
                         throw new ParseException("Bad Time Format:"+str+"\n'.' is wrong placed",idx);
                      number2 = number;
                      hasNumber = false;
                      has2Digits = false;
                      break;
                 case 'Z' :
                      if (flags != 3)
                         throw new ParseException("Bad Time Format:"+str+"\n'Z' is wrong placed",idx);
                      else result.setUTC();
                      break;

                 case '-' :
                    if (flags != 3)
                        throw new ParseException("Bad Time Format:"+str+"\n'-' is wrong placed",idx);
                    else {
                       result.setSecond(number2, number);
                       result.setUTC();
                       result.setZoneNegative();
                       flags = 1;
                    }
                    hasNumber = false;
                    has2Digits = false;
                    break;
                 case '+' :
                    if (flags != 3)
                        throw new ParseException("Bad Time Format:"+str+"\n'+' is wrong placed",idx);
                    else {
                       result.setSecond(number2, number);
                       result.setUTC();
                       flags = 1;
                    }
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
        //we have to set the seconds or the time zone
         if (flags!=3 && flags != 0)
            throw new ParseException("Bad Time Format: "+str+"\nA Time must follow the pattern hh:mm:ss.s(Z|((+|-)hh:mm)).",idx);
        else if (flags == 3) {
                result.setSecond(number, number2);
        }

        else if (flags == 0) {
            if (number != -1)
                result.setZone(number2,number);
            else throw new ParseException(str+"\n In a time zone, the minute field must always be present.",idx);
        }
        return result;
    }//parse


    //////////////////////DISALLOW DATE METHODS////////////////////////
    public short getCentury(){
        String err = "Time: couldn't access to the Century field.";
        throw new OperationNotSupportedException(err);
    }
    public short getYear(){
        String err = "Time: couldn't access to the Year field.";
        throw new OperationNotSupportedException(err);
    }
    public short getMonth(){
        String err = "Time: couldn't access to the Month field.";
        throw new OperationNotSupportedException(err);
    }
    public short getDay(){
        String err = "Time: couldn't access to the Day field.";
        throw new OperationNotSupportedException(err);
    }
     public void setCentury(short century){
        String err = "Time: couldn't access to the Century field.";
        throw new OperationNotSupportedException(err);
    }
    public void getYear(short year){
        String err = "Time: couldn't access to the Year field.";
        throw new OperationNotSupportedException(err);
    }
    public void getMonth(short month){
        String err = "Time: couldn't access to the Month field.";
        throw new OperationNotSupportedException(err);
    }
    public void getDay(short day){
        String err = "Time: couldn't access to the Day field.";
        throw new OperationNotSupportedException(err);
    }
}//Time