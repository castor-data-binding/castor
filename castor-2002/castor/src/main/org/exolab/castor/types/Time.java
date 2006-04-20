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
 */
package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.StringTokenizer;
import java.util.Date;

import org.exolab.castor.types.TimeDuration;

/**
 * <p>Describe an XML schema Time.
 * <p>The time type is derived from recurringDuration by setting up the facet:
 *      <ul>
 *          <li>duration to "P0Y"</li>
 *          <li>period to "P1D"</li>
 *      </ul>
 * <p>The format is defined by W3C XML Schema draft and ISO8601
 * i.e <tt>(+|-)hh:mm:ss.sss(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 * @see RecurringDuration
 */

public class Time extends RecurringDurationBase {

    /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;

    /** The Time Format used by the toDate() method */
    private static final String TIME_FORMAT = "HH:mm:ss.SSS";

    public Time() {
        super("P0Y","P1D");
    }

    /**
     * convert this Time into a local Date
     * @return a local date representing this Time
     * @throws ParseException
     */
    public Date toDate() throws ParseException {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);
        SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");
        String str = this.toString();
        // Set the time zone
        if ( !isUTC() ) {
            int offset = 0;
            offset = (int) ( (this.getZoneMinute() + this.getZoneHour()*60)*60*1000);
            offset = isZoneNegative() ? -offset : offset;
            timeZone.setRawOffset(offset);
            timeZone.setID(timeZone.getAvailableIDs(offset)[0]);
        }

        df.setTimeZone(timeZone);
        date = df.parse(this.toString());
        return date;

    }//toDate()

    /**
     * convert this Time to a string
     * The format is defined by W3C XML Schema draft and ISO8601
     * i.e (+|-)hh:mm:ss.sss(Z|(+|-)hh:mm)
     * @return a string representing this Time
     */
    public String toString() {

        String result = null;
        String timeZone = null;
        String temp = null;
        temp = String.valueOf(this.getHour());
        if (temp.length()==1)
            temp = "0"+temp;
        result = temp;

        temp = String.valueOf(this.getMinute());
        if (temp.length()==1)
            temp = "0"+temp;
        result = result + ":" + temp;

        temp = String.valueOf(this.getSeconds());
        if (temp.length()==1)
            temp = "0"+temp;
        result = result + ":" + temp +"."+String.valueOf(this.getMilli());


        result = isNegative() ? "-"+result : result;

        // by default we choose to not concat the Z
        if (!isUTC()) {
            temp = String.valueOf(this.getZoneHour());
            if (temp.length()==1)
                temp = "0"+temp;
            timeZone = temp;

            temp = String.valueOf(this.getZoneMinute());
            if (temp.length()==1)
                temp = "0"+temp;
            timeZone = timeZone + ":" + temp;

            timeZone = isZoneNegative() ? "-"+timeZone : "+"+timeZone;
            result = result + timeZone;
        }

        return result;

    }//toString

     /**
     * parse a String and convert it into a Time
     * @param str the string to parse
     * @return the Time represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */

    public static Time parse(String str) throws ParseException {


        Time result = new Time();
        //remove if necessary the Z at the end
        if ( str.endsWith("Z"))
            str = str.substring(0,str.indexOf("Z"));
        //isNegative ? is there a time zone ?

        if ( str.startsWith("-") )
            result.setNegative();

        String zoneStr = str.substring(str.length()-6,str.length());

        boolean timeZone = ( ( (zoneStr.lastIndexOf("-") !=-1)  ||
                               (zoneStr.lastIndexOf("+") !=-1) )  );

        if (DEBUG) {
            System.out.println("In parsing method of Time");
            System.out.println("String to parse : "+str);
            System.out.println("Negative ? "+result.isNegative());
            String tzone = timeZone?zoneStr:"false";
            System.out.println("Time zone :" +tzone);
        }

        if (!timeZone) zoneStr = null;
        else {
            int index =  str.lastIndexOf("+") != -1? str.lastIndexOf("+") :
                                                         str.lastIndexOf("-");
            str = str.substring(0,index);
        }

        //proceed Time
        StringTokenizer token  = new StringTokenizer(str,":");

        if ((token.countTokens() < 3) && (token.countTokens() > 5) )
            throw new ParseException("Bad time format",11);

        String temp = token.nextToken();
        temp = temp.substring(temp.indexOf("T")+1);
        if (temp.length() != 2)
            throw new ParseException("Bad hour format",11);
         if (DEBUG) {
            System.out.println("Processing hour: "+temp);
        }
        try {
            result.setHour(Short.parseShort( temp ));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a time type
            //(if not a ParseException is thrown) so we can never reach that point
        }
        temp=token.nextToken();
        if (temp.length() != 2)
            throw new ParseException("Bad minute format",14);
        if (DEBUG) {
            System.out.println("Processing minute: "+temp);
        }
        try {
            result.setMinute( Short.parseShort(temp));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a time type
            //(if not a ParseException is thrown) so we can never reach that point
        }

        temp=token.nextToken();
        String milsecond = "0";
        if (temp.indexOf(".") != -1) {
            milsecond = temp.substring(temp.indexOf(".")+1);
            temp = temp.substring(0,temp.indexOf("."));
        }

        if (temp.length() != 2)
            throw new ParseException("Bad second format",17);
        if (DEBUG) {
            System.out.println("Processing seconds: "+temp);
        }
        try {
            result.setSecond(Short.parseShort(temp.substring(0,2)),
                         Short.parseShort(milsecond));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a time type
            //(if not a ParseException is thrown) so we can never reach that point
        }


        // proceed TimeZone if any
        if (timeZone) {
            try {
                if (zoneStr.startsWith("-")) result.setZoneNegative();
            } catch(OperationNotSupportedException e) {
                //-- can never happen (a parse exception is thrown if the
                //-- object is not valide)
            }
            if (zoneStr.length()!= 6)
                throw new ParseException("Bad time zone format",20);
            if (DEBUG) {
                System.out.println("Processing timeZone: "+zoneStr);
            }
            try {
                result.setZone(Short.parseShort(zoneStr.substring(1,3)),
                                Short.parseShort(zoneStr.substring(4,6)));
            } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a time type
            //(if not a ParseException is thrown) so we can never reach that point
            }
        }
        else result.setUTC();
        temp = null;
        return result;
    }//parse
}//Time