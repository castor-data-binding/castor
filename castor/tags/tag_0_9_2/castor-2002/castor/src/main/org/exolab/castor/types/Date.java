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
 * 11/16/2000   Arnaud Blandin      Constructor Date(java.util.Date)
 * 11/01/2000   Arnaud Blandin      Enhancements (constructor, methods access...)
 * 10/23/2000   Arnaud Blandin      Created
 */

package org.exolab.castor.types;
import org.exolab.castor.types.TimePeriod;

import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.SimpleTimeZone;
import java.util.GregorianCalendar;
/**
 * Describe an XML schema Date
 * The date type is derived from time period by setting up the facet :
 *  <ul>
 *      <li>duration to "P1D"</li>
 *  </ul>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 */

public class Date extends TimePeriod {

     /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;

    public Date() {
        super("P1D");
        //we need to set the time zone to the computer local time zone
        //if we want to use the toDate() method.
        int temp = SimpleTimeZone.getDefault().getRawOffset();
        if (temp < 0){
            temp = -temp;
            try {
                super.setZoneNegative();
            } catch (OperationNotSupportedException e) {
            }
        }
        short zhour = (short) (temp / (60*60*1000));
        temp = temp % (60*60*1000);
        short zmin  = (short)(temp / (60*1000));
        try {
            super.setZone(zhour,zmin);
        } catch (OperationNotSupportedException e) {
        }
    }

    /**
     * This constructor is used to convert a java.util.Date into
     * a new org.exolab.castor.types.Date
     * <p>Note : all the information concerning the time part of
     * the java.util.Date is lost since a W3C Schema Date only represents
     * CCYY-MM-YY
     */
    public Date(java.util.Date dateRef) {
        this();
        GregorianCalendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTime(dateRef);
        try {
            this.setCentury((short) (tempCalendar.get(tempCalendar.YEAR)/100));
            this.setYear((short) (tempCalendar.get(tempCalendar.YEAR)%100));
        } catch (OperationNotSupportedException e) {
            // we can never reach that point because
            // we are allowed in a date type to set those fields
        }
        //we need to add 1 to the Month value returned by GregorianCalendar
        //because 0<MONTH<11 (i.e January is 0)
        try {
            this.setMonth((short) (tempCalendar.get(tempCalendar.MONTH)+1));
            this.setDay((short) (tempCalendar.get(tempCalendar.DAY_OF_MONTH)));
        } catch (OperationNotSupportedException e) {
            // we can never reach that point because
            // we are allowed in a date type to set those fields
        }

    } //Date(java.util.Date)


     /*Disallow the access to time method */
     public void setHour(short hour)
        throws OperationNotSupportedException
    {
        String err = "In a Date : the hour field must not be changed";
        throw new OperationNotSupportedException(err);
     }

    public void setMinute(short minute)
        throws OperationNotSupportedException
    {
        String err = "In a Date : the minute field must not be changed";
        throw new OperationNotSupportedException(err);
    }

    public void setSecond(short second,short millsecond)
        throws OperationNotSupportedException
    {
        String err = "In a Date : the second fields must not be changed";
        throw new OperationNotSupportedException(err);}

    public void setZone(short hour, short minute)
        throws OperationNotSupportedException
    {
      String err = "In a Date : the time zone fields must not be changed";
      throw new OperationNotSupportedException(err);
    }

    public void setZoneNegative()
        throws OperationNotSupportedException
    {
        String err = "In a Date : the time zone fields must not be changed";
        throw new OperationNotSupportedException(err);
    }


     /**
     * convert this Date to a string
     * The format is defined by W3C XML Schema draft and ISO8601
     * i.e (+|-)CCYY-MM-DD
     * @return a string representing this Date
     */
     public String toString() {

        String result = null;
        result = String.valueOf(this.getCentury());
        if (result.length() == 1)
            result = "0"+result;
        String temp = String.valueOf(this.getYear());
        if (temp.length()==1)
            temp = "0"+temp;
        result =  result  + temp;

        temp = String.valueOf(this.getMonth());
        if (temp.length()==1)
            temp = "0"+temp;
        result = result + "-" + temp;

        temp=String.valueOf(this.getDay());
        if (temp.length()==1)
            temp = "0"+temp;
        result = result + "-" + temp;

        result = isNegative() ? "-"+result : result;

        return result;
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
     * parse a String and convert it into a Date
     * @param str the string to parse
     * @return the Date represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static Date parseDate(String str) throws ParseException {

        Date result = new Date();

        if ( str.startsWith("-") )
            result.setNegative();

        if (DEBUG) {
            System.out.println("In parsing method of Date");
            System.out.println("String to parse : "+str);
            System.out.println("Negative ? "+result.isNegative());
        }

        // proceed date
        StringTokenizer token = new StringTokenizer(str,"-");

        if (token.countTokens() != 3)
            throw new ParseException(str+": Bad XML Schema date type format (CCYY-MM-DD)",0);

        String temp = token.nextToken();
        if (temp.length() != 4)
            throw new ParseException(str+": Bad year format",1);
        if (DEBUG) {
            System.out.println("Processing century: "+temp.substring(0,2));
        }
        result.setCentury(Short.parseShort( temp.substring(0,2) ));
        if (DEBUG) {
            System.out.println("Processing year: "+temp.substring(2,4));
        }
        try {
            result.setYear(Short.parseShort( temp.substring(2,4) ));
        } catch (OperationNotSupportedException e) {
            // we can never reach that point because
            // we are allowed in a date type to set those fields
        }

        temp=token.nextToken();
        if (temp.length() != 2)
            throw new ParseException(str+": Bad month format",5);
        if (DEBUG) {
            System.out.println("Processing month: "+temp);
        }
        try {
            result.setMonth(Short.parseShort(temp));
        } catch (OperationNotSupportedException e) {
            // we can never reach that point because
            // we are allowed in a date type to set those fields
        }

        temp=token.nextToken();
        if (temp.length() != 2)
            throw new ParseException(str+": Bad day format",8);
        if (DEBUG) {
            System.out.println("Processing day: "+temp);
        }
        try {
            result.setDay(Short.parseShort(temp));
        } catch (OperationNotSupportedException e) {
            // we can never reach that point because
            // we are allowed in a date type to set those fields
        }

        temp = null;
        return result;
    }//parse

}// Date