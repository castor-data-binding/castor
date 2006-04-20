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
 * $Id
 * Date         Author              Changes
 * 11/01/2000   Arnaud Blandin      Created
 */
package org.exolab.castor.types;

import org.exolab.castor.types.TimePeriod;

import java.text.ParseException;
import java.util.SimpleTimeZone;
import java.text.SimpleDateFormat;

/**
 * Describe an XML schema Year
 * The date type is derived from time period by setting up the facet :
 *      - duration to "P1Y"
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 */

public class Century extends TimePeriod {

    /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;

    /** The month format used by the toDate() method */
    private static final String YEAR_FORMAT = "yyyy";

    public Century() {
        super("P100Y");
        //we need to set the time zone to the computer local time zone
        //if we want to use the toDate() method.
        int temp = SimpleTimeZone.getDefault().getRawOffset();
        if (temp < 0){
            temp = -temp;
            _zoneNegative = true ;
        }
        _zoneHour = (short) (temp / (60*60*1000));
        temp = temp % (60*60*1000);
        _zoneMinute = (short)(temp / (60*1000));
    }


    /*Disallow the access to set year method*/
    public void setYear(short year) {
        throw new UnsupportedOperationException("year must not be changed");
    }

    /*Disallow the access to set month method*/
    public void setMonth(short month) {
        throw new UnsupportedOperationException("month must not be changed");
    }
    /*Disallow the access to set day method*/
    public void setDay(short day) {
        throw new UnsupportedOperationException("day must not be changed");
    }

    /*Disallow the access to set time methods */
    public void setHour(short hour) {
       throw new UnsupportedOperationException("hour must not be changed");
    }

    public void setMinute(short minute) {
      throw new UnsupportedOperationException("minute must not be changed");
    }

    public void setSecond(short second,short millsecond) {
      throw new UnsupportedOperationException("second must not be changed");
    }

    public void setZone(short hour, short minute) {
      throw new UnsupportedOperationException("time zone must not be changed");
    }

    public void setZoneNegative() {
        throw new UnsupportedOperationException("time zone must not be changed");
    }


     /**
     * convert this Year to a string
     * The format is defined by W3C XML Schema draft and ISO8601
     * i.e (+|-)CC
     * @return a string representing this Century
     */
     public String toString() {

        String result = null;

        result = String.valueOf(this.getCentury());
        if (result.length() == 1)
            result = "0"+result;

        result = isNegative() ? "-"+result : result;

        return result;

    }//toString

   /**
    * parse a String and convert it into a java.lang.Object
    * @param str the string to parse
    * @return the java.lang.Object represented by the string
    * @throws ParseException a parse exception is thrown if the string to parse
    *                        does not follow the rigth format (see the description
    *                        of this class)
    */
    public static Object parse(String str) throws ParseException {
        return parseCentury(str);
    }

    /**
     * parse a String and convert it into a Century
     * @param str the string to parse
     * @return the Century represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static Century parseCentury(String str) throws ParseException {

        Century result = new Century();

        if ( str.startsWith("-") ) {
            result.setNegative();
            str =str.substring(1);
        }

        if (DEBUG) {
            System.out.println("In parsing method of Century");
            System.out.println("String to parse : "+str);
            System.out.println("Negative ? "+result.isNegative());
        }


        if (str.length() != 2)
            throw new ParseException(str+": Bad XML Schema Century type format (CC)",0);


        if (DEBUG) {
            System.out.println("Processing century: "+str.substring(0,2));
        }
        result.setCentury(Short.parseShort(str));

        return result;
    }//parse

     public java.util.Date toDate() throws ParseException {
        java.util.Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(YEAR_FORMAT);
        SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");

        // Set the time zone
        if ( !isUTC() ) {
            int offset = 0;
            offset = (int) ( (_zoneMinute + _zoneHour*60)*60*1000);
            offset = isZoneNegative() ? -offset : offset;
            timeZone.setRawOffset(offset);
            timeZone.setID(timeZone.getAvailableIDs(offset)[0]);
        }
        df.setTimeZone(timeZone);
        date = df.parse(this.toString());
        return date;
    }//toDate()
} //--Century