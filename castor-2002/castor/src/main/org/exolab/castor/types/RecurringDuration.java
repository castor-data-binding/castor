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
 * 05/13/2001   Arnaud Blandin  Added the support for omitted components (section 4.5 of ISO8601)
 * 12/05/2000   Arnaud Blandin  Added the support for NotSupportedOperationException
 * 11/08/2000   Arnaud Blandin  Added new constructor and setValues method
 * 11/07/2000   Arnaud Blandin  Added isEqual() and isGreater() methods
 * 11/02/2000   Arnaud Blandin  Changed the constructor
 * 10/26/2000   Arnaud Blandin  Created
 */

package org.exolab.castor.types;

import org.exolab.castor.xml.ValidationException;

import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.StringTokenizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p> Represents recurringDuration utterly
 * a recurringDuration must contain all the fields :
 * <p> (+|-)CCYY-MM-DDThh:mm:ss.sss(Z|(+|-)hh:mm)
 * <p> The validation of the date fields is done in the set methods and follows
 * <a href="http://www.iso.ch/markete/8601.pdf">the ISO8601 Date and Time Format</a>
 * <p>It is possible to omit higher components by using '-'.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$
**/
public class RecurringDuration extends RecurringDurationBase{

    /** The date format used by the toDate() method */
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    /** Set to true and recompile to include debugging code in class. */
    private static final boolean DEBUG = false;


    //Private variables
    //-1 means that the field has been omitted (cf section 4.5 of ISO 8601)
    private short _century = 0;
    private short _year = 0;
    private short _month = 0;
    private short _day = 0;

    private static final short OMITED = Short.parseShort("-1");

    public RecurringDuration() {
    }

    /**
     * returns a recurringDuration with the facets
     * duration and period set up
     * @param duration the TimeDuration representing the duration facet
     * @param period the TimeDuration reprensenting the period facet
     * @return a recurringDuration with the facets
     *          duration and period set up
     */
    public RecurringDuration(TimeDuration duration, TimeDuration period) {
        super(duration,period);
    }

    /**
     * returns a recurringDuration with the facets
     * duration and period set up
     * @param duration the String representing the duration facet
     * @param period the String reprensenting the period facet
     * @return a recurringDuration with the facets
     *          duration and period set up
     */
    public RecurringDuration(String duration, String period) {
        super(duration, period);
    }


    /**
     * returns a recurringDuration with the facets
     * duration and period set up but also the fields
     * @param duration the String representing the duration facet
     * @param period the String reprensenting the period facet
     * @param values an array of shorts which contains the values of the fields
     * @return a recurringDuration with the facets
     *          duration and period set up
     * @see setValues
     */
     public RecurringDuration(String duration, String period, short[] values)
        throws OperationNotSupportedException
    {
        this(duration, period);
        if (values.length != 10) {
            throw new IllegalArgumentException("Wrong numbers of values");
        }
        this.setValues(values);
    }

    /**
     * set the century field
     * @param century the value to set up
     */
    public void setCentury(short century) {
        String err ="";
        if (century < -1) {
            err = "century : "+century+" must not be a negative value.";
            throw new IllegalArgumentException(err);
        }
        _century = century;
    }

    /**
     * set the Year field
     * Note: 0000 is not allowed
     * @param the year to set up
     */
    public void setYear(short year)
        throws OperationNotSupportedException
    {
        String err ="";
        if (year < -1) {
            err = "year : "+year+" must not be a negative value.";
            throw new IllegalArgumentException(err);
        }
        else if ( (year == -1) && (_century != -1) ) {
            err = "year can not be omitted if century is not omitted.";
            throw new IllegalArgumentException(err);
        }
        else if ( (year ==0) && (_century==0)) {
            err = "0000 is not an allowed year";
            throw new IllegalArgumentException(err);
        }

        _year = year;
    }

    /**
     * set the Month Field
     * @param month the value to set up
     * Note 1<month<12
     */
    public void setMonth(short month)
        throws OperationNotSupportedException
    {
        String err ="";
        if (month == -1) {
            if (_century != -1) {
                 err = "month cannot be omitted if the previous component is not omitted.\n"+
                       "only higher level components can be omitted.";
                 throw new IllegalArgumentException(err);
            }
        }
        else if (month < 1) {
            err = "month : "+month+" is not a correct value."
                  +"\n 1<month<12";
            throw new IllegalArgumentException(err);
        }

        else if (month > 12) {
            err = "month : "+month+" is not a correct value.";
            err+= "\n 1<month<12";
            throw new IllegalArgumentException(err);
        }
        _month = month;
    }

    /**
     * set the Day Field
     * @param day the value to set up
     * Note a validation is done on the day field
     */

    public void setDay(short day)
        throws OperationNotSupportedException
    {
        String err = "";
        if  (day == -1) {
            if (_month != -1) {
                 err = "day cannot be omitted if the previous component is not omitted.\n"+
                       "only higher level components can be omitted.";
                 throw new IllegalArgumentException(err);
            }
        }
        else if (day < 1) {
            err = "day : "+day+" is not a correct value.";
            err+= "\n 1<day";
            throw new IllegalArgumentException(err);
        }
        // in february
        if (_month == 2) {
            if (isLeap()) {
                if (day > 29) {
                    err = "day : "+day+" is not a correct value.";
                    err+= "\n day<30 (leap year and month is february)";
                    throw new IllegalArgumentException(err);
                }
            } else if (day > 28) {
                    err = "day : "+day+" is not a correct value.";
                    err+= "\n day<30 (not a leap year and month is february)";
                    throw new IllegalArgumentException(err);
            } //february
        } else if ( (_month == 4) || (_month == 6) ||
                    (_month == 9) || (_month == 11) )
                {
                    if (day > 30) {
                    err = "day : "+day+" is not a correct value.";
                    err+= "\n day<31 ";
                    throw new IllegalArgumentException(err);
                }
        } else if (day > 31) {
                    err = "day : "+day+" is not a correct value.";
                    err+= "\n day<=31 ";
                    throw new IllegalArgumentException(err);
                }

        _day = day;
    }

    /**
     * return true if the year field represents a leap year
     * A specific year is a leap year if it is either evenly
     * divisible by 400 OR evenly divisible by 4 and not evenly divisible by 100
     * @return true if the year field represents a leap year
     */
    public boolean isLeap () {
        int temp = (_century * 100 + _year) ;
        boolean result =( ((temp % 4) == 0) && ((temp % 100) != 0) );
        result = (result || ((temp % 400)==0) );
        return result;
    }

     /**
     * set all the fields by reading the values in an array
     * @param values an array of shorts with the values
     * the array is supposed to be of length 10 and ordered like that:
     *<ul>
     *      <li>century</li>
     *      <li>year</li>
     *      <li>month</li>
     *      <li>day</li>
     *      <li>hour</li>
     *      <li>minute</li>
     *      <li>second</li>
     *      <li>millisecond</li>
     *      <li>zoneHour</li>
     *      <li>zoneMinute</li>
     * </ul>
     *
     * @throws OperationNotSupportedException this exception is thrown if changing
     *         the value of one field os not allowed
     * @see RecurringDurationBase.setValues
     */
     public void setValues(short[] values)
        throws OperationNotSupportedException
    {
        this.setCentury(values[0]);
        this.setYear(values[1]);
        this.setMonth(values[2]);
        this.setDay(values[3]);
        this.setHour(values[4]);
        this.setMinute(values[5]);
        this.setSecond(values[6],values[7]);
        this.setZone(values[8],values[9]);
     }


    //Get methods
    public short getCentury() {
        return(_century);
    }

    public short getYear() {
        return(_year);
    }

    public short getMonth() {
        return(_month);
    }

    public short getDay() {
        return(_day);
    }


    /**
     * returns an array of short with all the fields which describe
     * a RecurringDuration
     * @return  an array of short with all the fields which describe
     * a RecurringDuration
     */
    public short[] getValues() {
        short[] result = null;
        result = new short[10];
        result[0] = this.getCentury();
        result[1] = this.getYear();
        result[2] = this.getMonth();
        result[3] = this.getDay();
        result[4] = this.getHour();
        result[5] = this.getMinute();
        result[6] = this.getSeconds();
        result[7] = this.getMilli();
        result[8] = this.getZoneHour();
        result[5] = this.getZoneMinute();
        return result;
    } //getValues

    /**
     * convert this recurringDuration into a local Date
     * <p>Note : Be aware a the 'local' property of the date i.e <tt>toDate()</tt> will de the
     * conversion between a UTC date and your computer date format.
     * For instance if you have set up your computer time zone on the Pacific Day Time
     * the conversion of <tt>2000-10-20T00:00:00.000</tt> into a <tt>java.util.Date</tt>
     * will return <tt>Thu Oct 19 17:00:00 PDT 2000</tt>
     * @return a local date representing this recurringDuration
     * @throws ParseException
     */
    public Date toDate() throws ParseException {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");

        // Set the time zone
        if ( !isUTC() ) {
            int offset = 0;
            offset = (int) ( (this.getZoneMinute() + this.getZoneHour()*60)*60*1000);
            offset = isZoneNegative() ? -offset : offset;
            timeZone.setRawOffset(offset);
            timeZone.setID(timeZone.getAvailableIDs(offset)[0]);
        }
        df.setTimeZone(timeZone);
        date = df.parse(this.toPrivateString());
        return date;
    }//toDate()

    /**
     * <p>Convert this recurringDuration to a string
     * <p>The format is defined by W3C XML Schema draft and ISO8601
     * i.e (+|-)CCYY-MM-DDThh:mm:ss.sss(Z|(+|-)hh:mm)
     * @return a string representing this recurringDuration
     */
    public String toString() {
        return this.toPrivateString();
    }

    /*This method is needed for the toDate() method
     */
    private final String toPrivateString() {

        StringBuffer result = new StringBuffer();
        StringBuffer timeZone = null;

        if (this.getCentury() == -1)
            result.append('-');
        else {
            if (this.getCentury()/10 == 0)
            result.append(0);
            result.append(this.getCentury());

             if ((this.getYear()/10) == 0)
                result.append(0);
             result.append(this.getYear());
        }
        result.append('-');
        if (this.getMonth() == -1)
            result.append('-');
        else {
            if ((this.getMonth() / 10) == 0 )
               result.append(0);
            result.append(this.getMonth());
        }
        result.append('-');
        if (this.getDay() == -1)
             result.append('-');
        else {
            if ((this.getDay()/10) == 0 )
                result.append(0);
            result.append(this.getDay());
        }
        // nowhere it is said in the specs that Time can be omitted
        // choose to always keep it
        result.append("T");
        if (this.getHour() == -1)
            result.append('-');
        else {
            if ((this.getHour()/10) == 0)
                result.append(0);
            result.append(this.getHour());
        }
        result.append(':');
        if (this.getMinute() == -1)
            result.append('-');
        else {
            if ((this.getMinute() / 10) == 0 )
               result.append(0);
            result.append(this.getMinute());
        }
        result.append(':');
        if (this.getSeconds() == -1)
            result.append('-');
        else {
            if ((this.getSeconds()/10) == 0 )
                result.append(0);
            result.append(this.getSeconds());
        }
        result.append('.');
        result.append(this.getMilli());

        if (isNegative())
           result.append('-');

        // by default we choose to not concat the Z
        if (!isUTC()) {
            timeZone = new StringBuffer();
            if ((this.getZoneHour()/10) == 0)
                timeZone.append(0);
            timeZone.append(this.getZoneHour());

            timeZone.append(':');
            if ((this.getZoneMinute()/10) == 0)
                timeZone.append(0);
            timeZone.append(this.getZoneMinute());

            if (isZoneNegative())
               timeZone.insert(0,'-');
            else timeZone.insert(0,'+');
            result.append(timeZone.toString());
        }

       if (isNegative())
          result.insert(0,'-');

        return result.toString();

    }//toString

    public static Object parse(String str) throws ParseException {
        return parseRecurringDuration(str);
    }

    /**
     * parse a String and convert it into a recurringDuration
     * @param str the string to parse
     * @return the recurringDuration represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     * @todo optimize this method (too much strings)
     */
    public static RecurringDuration parseRecurringDuration(String str) throws ParseException {

        RecurringDuration result = new RecurringDuration();

        //remove if necessary the Z at the end
        if ( str.endsWith("Z"))
            str = str.substring(0,str.indexOf("Z"));

        //isNegative ?
        if ( str.startsWith("-") && !str.startsWith("--"))
            result.setNegative();

        //Is there a time Zone?
        String zoneStr = str.substring(str.length()-6,str.length());
        boolean timeZone = (  ((zoneStr.lastIndexOf("-") !=-1)  ||
                               (zoneStr.lastIndexOf("+") !=-1 )) &&
                               (zoneStr.lastIndexOf(":") !=-1)  );

        if (DEBUG) {
            System.out.println("In parsing method of RecurringDuration");
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

        // the 'T' is required
        if (str.indexOf('T') == -1) {
            throw new ParseException("The 'T' element is required",0);
        }
        String date = str.substring(0,str.indexOf("T"));
        String time = str.substring(str.indexOf("T"));

        // proceed date
        StringTokenizer token = new StringTokenizer(date,"-");

        if (token.countTokens() > 3)
            throw new ParseException(str+": Bad date format",0);

        try {
            //CCYY
            boolean process = false;
            String temp;
            if (token.countTokens() == 3) {
                temp = token.nextToken();
                if (temp.length() != 4)
                    throw new ParseException(str+":Bad year format",1);
                if (DEBUG) {
                    System.out.println("Processing century: "+temp.substring(0,2));
                }
                result.setCentury(Short.parseShort( temp.substring(0,2) ));
                if (DEBUG) {
                    System.out.println("Processing year: "+temp.substring(2,4));
                }
                result.setYear(Short.parseShort( temp.substring(2,4) ));
                process = true;
            }
            if (!process)
               result.setCentury(OMITED);
            if (token.countTokens() == 2) {
               //MM
               temp=token.nextToken();
                if (temp.length() != 2)
                    throw new ParseException(str+": Bad month format",5);
                if (DEBUG) {
                    System.out.println("Processing month: "+temp);
                }
                result.setMonth(Short.parseShort(temp));
                process = true;
            }

            if ((!process)) {
               result.setMonth(OMITED);
            }
            if (token.countTokens() == 1) {
                //DD
                temp=token.nextToken();
                if (temp.length() != 2)
                    throw new ParseException(str+":Bad day format",8);
                if (DEBUG) {
                    System.out.println("Processing day: "+temp);
                }
                result.setDay(Short.parseShort(temp));
                process = true;
            }

           if (!process) {
               result.setDay(OMITED);
            }


            //proceed Time
            token = new StringTokenizer(time,":");
            process = false;
            if (token.countTokens() > 5)
                throw new ParseException(str+": Bad time format",11);

            if (token.countTokens() == 3) {
                //hh
                temp = token.nextToken();
                temp = temp.substring(temp.indexOf("T")+1);
                 if (temp.length() != 2)
                    throw new ParseException(str+": Bad hour format",11);
                  if (DEBUG) {
                    System.out.println("Processing hour: "+temp);
                  }
                  result.setHour(Short.parseShort( temp ));
                  process = true;
            }

            if (!process) {
                if (result.getDay() == OMITED )
                   result.setHour(OMITED);
                else throw new IllegalArgumentException("hour cannot be omitted");
            }
            if (token.countTokens() == 2) {
               //mm
               temp=token.nextToken();

                if (temp.length() != 2)
                    throw new ParseException(str+": Bad minute format",14);

                if (DEBUG) {
                    System.out.println("Processing minute: "+temp);
                }
                result.setMinute( Short.parseShort(temp));
                process = true;
            }
            if (!process){
                if (result.getDay() == OMITED ) {
                    result.setHour(OMITED);
                    result.setMinute(OMITED);
                 }
                 else throw new IllegalArgumentException("hour cannot be omitted");
            }
            if (token.countTokens() == 1) {
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
                result.setSecond(Short.parseShort(temp.substring(0,2)),
                                 Short.parseShort(milsecond));

                process = true;
            }

            if (!process) {
                if (result.getDay() == OMITED ) {
                    result.setHour(OMITED);
                    result.setMinute(OMITED);
                    result.setSecond(OMITED,OMITED);
                }
                else throw new IllegalArgumentException("hour cannot be omitted");
            }

            // proceed TimeZone if any
            if (timeZone) {
                if (zoneStr.startsWith("-")) result.setZoneNegative();
                if (zoneStr.length()!= 6)
                    throw new ParseException(str+": Bad time zone format",20);
                result.setZone(Short.parseShort(zoneStr.substring(1,3)),
                                Short.parseShort(zoneStr.substring(4,6)));
            }
            else result.isUTC();
            temp = null;
        } catch (OperationNotSupportedException e) {
            //we are sure that this method is used with a recurringDuration
            //(if not a ParseException is thrown) so we can never reach that point
        }
        return result;
    }//parse

    /**
     * Override the java.lang.equals method
     * @see equal
     */
     public boolean equals(Object object) {
        if (object instanceof RecurringDuration) {
            try {
                return equal( (RecurringDuration) object);
            } catch (ValidationException e) {
                e.printStackTrace();
                return false;
            }
        }
        else return false;
    }

    /**
     * <p> Returns true if the present instance of Recurring Duration is equal to
     * the parameter.
     * <p>The equals relation is the following :
     * <p> <tt>rd1 equals rd2 </tt> iff each field of rd1 is equal to the corresponding field of rd2
     * @param reccD the recurring duration to compare with the present instance
     * @return true if the present instance is equal to the parameter false if not
     */
     public boolean equal(RecurringDuration reccD) throws ValidationException
     {
        boolean result = false;
        if (reccD == null)
            return false;
        if ( !(this.getPeriod().equals(reccD.getPeriod())) ||
             !(this.getDuration().equals(reccD.getDuration())) )
        {
                String err = " Recurring Duration which have different values "
                            +"for the duration and period can not be compared";
                throw new ValidationException(err);
        }
        result = this.getCentury() == reccD.getCentury();
        result = result && (this.getYear() == reccD.getYear());
        result = result && (this.getMonth() == reccD.getMonth());
        result = result && (this.getDay() == reccD.getDay());
        result = result && (this.getHour() == reccD.getHour());
        result = result && (this.getMinute() == reccD.getMinute());
        result = result && (this.getSeconds() == reccD.getSeconds());
        result = result && (this.getMilli() == reccD.getMilli());
        result = result && (this.isNegative() == this.isNegative());
        if (!reccD.isUTC()) {
            result = result && (!this.isUTC());
            result = result && (this.getZoneHour() == reccD.getZoneHour());
            result = result && (this.getZoneMinute() == reccD.getZoneMinute());
        }
        return result;
    }//equals

    /**
     * <p>Returns true if the present instance of RecurringDuration is greater than
     * the parameter
     * <p>Note : the order relation follows the W3C XML Schema draft i.e
     * <tt>rd1 < rd2 </tt>iff rd2-rd1>0
     * @param reccD the recurring duration to compare with the present instance
     * @return true if the present instance is the greatest, false if not
     */
    public boolean isGreater(RecurringDuration reccD) throws ValidationException
    {
        boolean result = false;
        if ( !(this.getPeriod().equals(reccD.getPeriod())) ||
             !(this.getDuration().equals(reccD.getDuration())) )
        {
                String err = " Recurring Duration which have different values "
                            +"for the duration and period can not be compared";
                throw new ValidationException(err);
        }
        short[] val_this = this.getValues();
        short[] val_reccD = reccD.getValues();
        int i = 0;
        while ( (result != true) && (i< (val_this.length-1)) ) {
            result = val_this[i] > val_reccD[i];
            if ( val_this[i] < val_reccD[i])
                return false;
            i++;
        }
        return result;
    }//isGreater

} //RecurringDuration