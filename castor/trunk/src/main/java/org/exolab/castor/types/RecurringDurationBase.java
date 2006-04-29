
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
 * 12/05/2000   Arnaud Blandin  Added the support for NotSupportedOperationException
 * 11/07/2000   Arnaud Blandin  Added a new constructor and setValues method
 * 11/02/2000   Arnaud Blandin  Changed the constructor
 * 26/10/2000   Arnaud Blandin  Created
 */

package org.exolab.castor.types;


import org.exolab.castor.types.TimeDuration;
import org.exolab.castor.xml.ValidationException;

import java.text.ParseException;

/**
 * <p>The base class for recurring Duration types.
 * <p>This base class contains all the time fields (including the time zone ones)
 * and also the facets period and duration
 * <p>The validation of the time fields is done in the set methods and follows
 * <a href="http://www.iso.ch/markete/8601.pdf">the ISO8601 Date and Time Format</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @see RecurringDuration
 * @see Time
 * @see TimeInstant
 * @see TimePeriod
 */
public abstract class RecurringDurationBase
    implements java.io.Serializable {

    /**
     * the period facet of this recurringDuration
     */
    private TimeDuration _period = null;
    /**
     * the duration facet of this recurringDuration
     */
    private TimeDuration _duration = null;

    /**
     * the hour field of this recurringDuration
     * -1 means that the field has been omitted (cf section 4.5 of ISO 8601)
     */
    private short _hour = 0;
    /**
     * the minute field of this recurringDuration
     * -1 means that the field has been omitted (cf section 4.5 of ISO 8601)
     */
    private short _minute = 0;
    /**
     * the second field of this recurringDuration
     * -1 means that the field has been omitted (cf section 4.5 of ISO 8601)
     */
    private short _second = 0;
    /**
     * the millsecond field of this recurringDuration
     */
    private short _millsecond = 0;
    /**
     * the time zone hour field of this recurringDuration
     */
    private short _zoneHour = 0;
    /**
     * the time zone minute field of this recurringDuration
     */
    private short _zoneMinute = 0;
    /**
     * true if this recurringDuration is UTC related
     */
    private boolean _UTC = false;
    private boolean _zoneNegative = false;
    private boolean _isNegative = false;

    protected RecurringDurationBase() {
    }

    /**
     * returns a recurringDurationBase with the facets
     * duration and period set up
     * @param duration the TimeDuration representing the duration facet
     * @param period the TimeDuration reprensenting the period facet
     */
    protected RecurringDurationBase(TimeDuration duration, TimeDuration period)
    {
        try {
            this.setDuration(duration);
            this.setPeriodInternal(period);
        } catch (OperationNotSupportedException e) {
            String err = "Recurring Duration :"+e;
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * returns a recurringDurationBase with the facets
     * duration and period set up
     * @param duration the String representing the duration facet
     * @param period the String reprensenting the period facet
     * @throws IllegalArgumentException this exception is thrown when the parameter strings
     *                                  are not corresponding to valid TimeDuration
     */
    protected RecurringDurationBase(String duration, String period)
        throws IllegalArgumentException
    {
        try {
            this.setDuration(TimeDuration.parseTimeDuration(duration));
            this.setPeriodInternal(TimeDuration.parseTimeDuration(period));
        } catch (Exception e) {
            String err = "In RecurringDurationBase : "+e;
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * returns a recurringDurationBase with the facets
     * duration and period set up but also the fields
     * @param duration the String representing the duration facet
     * @param period the String reprensenting the period facet
     * @param values an array of shorts which contains the values of the fields
     * @throws IllegalArgumentException this exception is thrown when the values array
     *                                  is not of length 6.
     * @see #setValues
     */

    protected RecurringDurationBase(String duration, String period, short[] values)
        throws OperationNotSupportedException
    {
        new RecurringDuration(duration, period);
        if (values.length != 6) {
            throw new IllegalArgumentException("Wrong numbers of values");
        }
        this.setValues(values);
    }

    /**
     * set the period facet this recurringDuration
     * this method is used to avoid calling the setPeriod method
     * of subclasses
     * @param period the period to set
     */
    private void setPeriodInternal (TimeDuration period) {
        _period = period;
    }

    /**
     * set the period facet for this recurringDuration
     * @param period the period to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the period facet is not allowed
     */
    public void setPeriod (TimeDuration period)
        throws OperationNotSupportedException
    {
        setPeriodInternal(period);
    }

    /**
     * set the period facet for this recurringDuration
     * @param period the period to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the period facet is not allowed
     */
    public void setPeriod (String period)
        throws OperationNotSupportedException
    {
        try {
            setPeriodInternal(TimeDuration.parseTimeDuration(period));
        } catch (ParseException e) {
            String err = "RecurringDuration, setPeriod:"+e;
            throw new IllegalArgumentException(err);
        }
    }

    /**
     * set the duration facet for this recurringDuration
     * @param duration the period to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the duration facet is not allowed
     */
     public void setDuration (TimeDuration duration)
        throws OperationNotSupportedException
    {
        _duration = duration;
    }

    /**
     * set the duration facet for this recurringDuration
     * @param duration the period to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the duration facet is not allowed
     */
    public void setDuration(String duration)
        throws OperationNotSupportedException
    {
        try {
            _duration = (TimeDuration.parseTimeDuration(duration));
        } catch (ParseException e) {
            String err = "RecurringDuration, setDuration:"+e;
            throw new IllegalArgumentException(err);
        }
    }


    /**
     * set the hour field for this recurringDuration
     * @param hour the hour to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the hour field is not allowed
     */
    public void setHour(short hour)
        throws OperationNotSupportedException
    {
        String err = "";
        if (hour > 23) {
            err = "the hour field ("+hour+")must be strictly lower than 24";
            throw new IllegalArgumentException(err);
        }
        _hour = hour;
    }

    /**
     * set the minute field for this recurringDuration
     * @param minute the minute to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the minute field is not allowed
     */
    public void setMinute(short minute)
        throws OperationNotSupportedException
    {
         String err = "";
         if ( (minute == -1) && (_hour != -1)) {
             err = "minute cannot be omitted if the previous field is not omitted.";
             throw new IllegalArgumentException(err);
         }
         else if (minute > 59) {
            err = "the minute field ("+minute+")must be lower than 59";
            throw new IllegalArgumentException(err);
        }
        _minute = minute ;
    }

    /**
     * set the second field for this recurringDuration
     * @param second the second to set
     * @param millsecond the millisecond to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the second field is not allowed
     */
    public void setSecond(short second,short millsecond)
        throws OperationNotSupportedException
     {
         String err = "";
         if ( (second == -1) && (_minute != -1)) {
             err = "second cannot be omitted if the previous field is not omitted.";
             throw new IllegalArgumentException(err);
         }

         else if (second > 60) {
            err = "the second field ("+second+")must be lower than 60";
            throw new IllegalArgumentException(err);
        }
        _second = second;
        _millsecond = millsecond;
    }

    /**
     * set the time zone fields for this recurringDuration
     * @param hour the time zone hour to set
     * @param minute the time zone minute to set
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the value of the time zone fields is not allowed
     */
    public void setZone(short hour, short minute)
        throws OperationNotSupportedException
    {
         String err = "";
         if (hour > 23) {
            err = "the zone hour field ("+hour+")must be strictly lower than 24";
            throw new IllegalArgumentException(err);
        }
        _zoneHour = hour;
         if (minute > 59) {
            err = "the minute field ("+minute+")must be lower than 59";
            throw new IllegalArgumentException(err);
        }
        _zoneMinute = minute;
    }

    /**
     * set all the fields by reading the values in an array
     * @param values an array of shorts with the values
     * the array is supposed to be of length 6 and ordered like that:
     * <ul>
     *      <li>hour</li>
     *      <li>minute</li>
     *      <li>second</li>
     *      <li>millisecond</li>
     *      <li>zoneHour</li>
     *      <li>zoneMinute</li>
     * </ul>
     * @throws OperationNotSupportedException this exception is thrown when changing
     *         the value of a time related field is not allowed
     */
     public void setValues(short[] values)
        throws OperationNotSupportedException
    {
        this.setHour(values[0]);
        this.setMinute(values[1]);
        this.setSecond(values[2],values[3]);
        this.setZone(values[4],values[5]);
     }

    /**
     * set the negative field to true
     */
    public void setNegative() {
        _isNegative = true;
    }


    /**
     * set the time zone negative field to true
     * @throws OperationNotSupportedException this exception is thrown when
     *         changing the time zone fields is not allowed
     */

    public void setZoneNegative()
        throws OperationNotSupportedException
    {
        _zoneNegative = true;
    }


    /**
     * set the UTC field to true
     */
    public void setUTC() {
        _UTC = true;
    }

    //Get methods
    public TimeDuration getPeriod() {
        return(_period);
    }

    public TimeDuration getDuration() {
        return(_duration);
    }

    public short getHour() {
        return(_hour);
    }

    public short getMinute() {
        return(_minute);
    }

    public short getSeconds() {
        return(_second);
    }

    public short getMilli() {
        return(_millsecond);
    }

    public short getZoneHour() {
        return(_zoneHour);
    }

    public short getZoneMinute() {
        return(_zoneMinute);
    }

    /**
     * returns an array of short with all the fields which describe
     * a RecurringDurationBase
     * @return  an array of short with all the fields which describe
     * a RecurringDurationBase
     */
    public short[] getValues() {
        short[] result = null;
        result = new short[6];
        result[0] = this.getHour();
        result[1] = this.getMinute();
        result[2] = this.getSeconds();
        result[3] = this.getMilli();
        result[4] = this.getZoneHour();
        result[5] = this.getZoneMinute();
        return result;
    } //getValues

    /**
     * return true if this recurring Duration type is UTC
     * i.e if there is no time zone.
     * @returns true if this recurringDuration type is UTC
     *          else false
     */
    public boolean isUTC() {
        _UTC = ( (_zoneHour == 0) && (_zoneMinute == 0) );
        return(_UTC);
    }

    public boolean isNegative() {
        return _isNegative;
    }

    public boolean isZoneNegative() {
        return _zoneNegative;
    }

     /**
     * Override the java.lang.equals method
     * @see #equal
     */
     public boolean equals(Object object) {
        if (object instanceof RecurringDurationBase) {
            try {
                return equal( (RecurringDurationBase) object);
            } catch (ValidationException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    /**
     * <p> Returns true if the present instance of Recurring Duration Base is equal to
     * the parameter.
     * <p> The equals relation is the following :
     * <tt>rd1 equals rd2 iff each field of rd1 is equal to the corresponding field of rd2 </tt>
     * @param reccD the recurring duration to compare with the present instance
     * @return true if the present instance is equal to the parameter false if not
     */
     public boolean equal(RecurringDurationBase reccD) throws ValidationException
     {
        boolean result = false;
         if ( !(this.getPeriod().equals(reccD.getPeriod())) ||
             !(this.getDuration().equals(reccD.getDuration())) )
        {
                String err = " Recurring Duration which have different values "
                            +"for the duration and period can not be compared";
                throw new ValidationException(err);
        }
        result = (this.getHour() == reccD.getHour());
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
     * <p>Returns true if the present instance of RecurringDurationBase is greater than
     * the parameter
     * <p>Note : the order relation follows the W3C XML Schema draft i.e
     * <tt>rd1 < rd2 iff rd2-rd1>0</tt>
     * @param reccD the recurring duration base to compare with the present instance
     * @return true if the present instance is the greatest, false if not
     */
    public boolean isGreater(RecurringDurationBase reccD) throws ValidationException
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
        while ( (result != true) || (i< val_this.length) ) {
            result = val_this[i] > val_reccD[i];
            if ( val_this[i] < val_reccD[i])
                return false;
            i++;
        }
        return result;
    }//isGreater

}//-- RecurringDurationBase