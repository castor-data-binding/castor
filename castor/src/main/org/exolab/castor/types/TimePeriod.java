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
 * Date         Author          Changes
 * 12/05/2000   Aranud Blandin  Added support for OperationNotSupportedException
 * 11/02/2000   Arnaud Blandin  Changed the constructor
 * 26/10/2000   Arnaud Blandin  Created
 */

package org.exolab.castor.types;
import org.exolab.castor.types.RecurringDuration;

import java.text.ParseException;
import java.util.StringTokenizer;

/**
 * <p>Describe an XML schema TimePeriod.
 * <p>The time period type is derived from recurringDuration by setting up the facet:
 *      <ul>
 *          <li>period to "P0Y"</li>
 *      </ul>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
 * @see RecurringDuration
 */
public class TimePeriod extends RecurringDuration{

    /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;

    public TimePeriod() {
        super("","P0Y");
    }

    /**
     * returns a TimePeriod with the duration facet set up
     * @param duration the String value of the duration facet
     */
    public TimePeriod(String duration) {
        super(duration,"P0Y");
    }

    /**
     * parse a string to set the fields of a TimePeriod
     * @param str the string to parse
     */
     public void setFields (String str) throws ParseException {

        //remove if necessary the Z at the end
        if ( str.endsWith("Z"))
            str = str.substring(0,str.indexOf("Z"));

        //isNegative ? is there a time zone ?
        if ( str.startsWith("-") )
            this.setNegative();

        String zoneStr = str.substring(str.length()-6,str.length());
        boolean timeZone = (  ((zoneStr.lastIndexOf("-") !=-1)  ||
                               (zoneStr.lastIndexOf("+") !=-1 )) &&
                               (zoneStr.lastIndexOf(":") !=-1)  );

        if (DEBUG) {
            System.out.println("In parsing method of TimePeriod");
            System.out.println("String to parse : "+str);
            System.out.println("Negative ? "+this.isNegative());
            String tzone = timeZone?zoneStr:"false";
            System.out.println("Time zone :" +tzone);
        }

        if (!timeZone) zoneStr = null;
        else {
            int index =  str.lastIndexOf("+") != -1? str.lastIndexOf("+") :
                                                         str.lastIndexOf("-");
            str = str.substring(0,index);
        }

        // the 'T' is required
        if (str.indexOf('T') == -1) {
            throw new ParseException("The 'T' element is required",0);
        }
        String date = str.substring(0,str.indexOf("T"));
        String time = str.substring(str.indexOf("T"));

        // proceed date
        StringTokenizer token = new StringTokenizer(date,"-");

        if (token.countTokens() != 3)
            throw new ParseException(str+": Bad date format",0);

        //CCYY
        String temp = token.nextToken();
        if (temp.length() != 4)
            throw new ParseException(str+":Bad year format",1);
        if (DEBUG) {
            System.out.println("Processing century: "+temp.substring(0,2));
        }
        this.setCentury(Short.parseShort( temp.substring(0,2) ));
        if (DEBUG) {
            System.out.println("Processing year: "+temp.substring(2,4));
        }
        try {
            this.setYear(Short.parseShort( temp.substring(2,4) ));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
        }


        //MM
        temp=token.nextToken();
        if (temp.length() != 2)
            throw new ParseException(str+": Bad month format",5);
        if (DEBUG) {
            System.out.println("Processing month: "+temp);
        }
        try {
            this.setMonth(Short.parseShort(temp));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
        }

        //DD
        temp=token.nextToken();
        if (temp.length() != 2)
            throw new ParseException(str+":Bad day format",8);
        if (DEBUG) {
            System.out.println("Processing day: "+temp);
        }
        try {
            this.setDay(Short.parseShort(temp));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
        }

        //proceed Time
        token = new StringTokenizer(time,":");

        if ((token.countTokens() < 3) && (token.countTokens() > 5) )
            throw new ParseException(str+": Bad time format",11);

        //hh
        temp = token.nextToken();
        temp = temp.substring(temp.indexOf("T")+1);
        if (temp.length() != 2)
            throw new ParseException(str+": Bad hour format",11);
         if (DEBUG) {
            System.out.println("Processing hour: "+temp);
        }

         try {
            this.setHour(Short.parseShort( temp ));
         } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
         }

        //mm
        temp=token.nextToken();
        if (temp.length() != 2)
            throw new ParseException(str+": Bad minute format",14);
        if (DEBUG) {
            System.out.println("Processing minute: "+temp);
        }
        try {
            this.setMinute( Short.parseShort(temp));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
        }

        //ss
        temp=token.nextToken();
        String milsecond = "0";
        if (temp.indexOf(".") != -1) {
            milsecond = temp.substring(temp.indexOf(".")+1);
            temp = temp.substring(0,temp.indexOf("."));
        }

        if (temp.length() != 2)
            throw new ParseException(str+": Bad second format",17);
        if (DEBUG) {
            System.out.println("Processing seconds: "+temp);
        }
        try {
            this.setSecond(Short.parseShort(temp.substring(0,2)),
                           Short.parseShort(milsecond));
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
        }


        // proceed TimeZone if any
        if (timeZone) {
            try {
                if (zoneStr.startsWith("-")) this.setZoneNegative();
            } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
            }

            if (zoneStr.length()!= 6)
                throw new ParseException(str+": Bad time zone format",20);
            try {
                this.setZone(Short.parseShort(zoneStr.substring(1,3)),
                             Short.parseShort(zoneStr.substring(4,6)));
            } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a timePeriod type
            //(if not a ParseException is thrown) so we can never reach that point
            }

        }
        else this.isUTC();
        temp = null;
    }//setFields


     public void setPeriod(TimeDuration period)
        throws OperationNotSupportedException
    {
        throw new OperationNotSupportedException("in a time period type,the period must not be changed");
    }

} //TimePeriod