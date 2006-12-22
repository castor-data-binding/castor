/*
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
 * Copyright 2000-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author          Changes
 * 08/30/2001   Arnaud Blandin  added to Calendar() (patch from Sébastien Stormacq [S.Stormacq@aubay-si.lu])
 * 05/29/2001   Arnaud Blandin  Added order methods
 * 05/22/2001   Arnaud Blandin  Created
 */
package org.exolab.castor.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * The base class for date/time XML Schema types.
 * <p>
 * The validation of the date/time fields is done in the set methods and follows
 * <a href="http://www.iso.ch/markete/8601.pdf">the ISO8601 Date and Time Format</a>.
 * <p>
 * Note: the Castor date/time types are mutable, unlike the date/time types of
 * the JDK in Java2. This is needed by the Marshaling framework.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$
 * @see DateTime
 * @see Date
 * @see Time
 */
public abstract class DateTimeBase implements java.io.Serializable, Cloneable {
    /** Public constant referring to an indeterminate Date/Time comparison. */
    public static final int       INDETERMINATE   = -1;
    /** Public constant referring to a Date/Time comparison result of "less than". */
    public static final int       LESS_THAN       = 0;
    /** Public constant referring to a Date/Time comparison result of "equals". */
    public static final int       EQUALS          = 1;
    /** Public constant referring to a Date/Time comparison result of "greater than". */
    public static final int       GREATER_THAN    = 2;

    /** When comparing a date/time with a time zone to one without, the recommendation
     * says that 14 hours is the time zone offset to use for comparison. */
    protected static final int    MAX_TIME_ZONE_COMPARISON_OFFSET = 14;

    /** Convenience String for complaints. */
    protected static final String WRONGLY_PLACED  = " is wrongly placed.";

    /** true if this date/time type is negative. */
    private boolean               _isNegative     = false;
    /** The century field. */
    private short                 _century        = 0;
    /** The year field. */
    private short                 _year           = 0;
    /** The month field. */
    private short                 _month          = 0;
    /** The day field. */
    private short                 _day            = 0;
    /** the hour field. */
    private short                 _hour           = 0;
    /** the minute field. */
    private short                 _minute         = 0;
    /** the second field. */
    private short                 _second         = 0;
    /** the millsecond field. */
    private short                 _millsecond     = 0;
    /** true if the time zone is negative. */
    private boolean               _zoneNegative   = false;
    /** true if this date/time type has a time zone assigned. */
    private boolean               _UTC            = false;
    /** the time zone hour field. */
    private short                 _zoneHour       = 0;
    /** the time zone minute field. */
    private short                 _zoneMinute     = 0;

    //////////////////////////Abstract methods////////////////////////////////////

    /**
     * Returns a java.util.Date that represents the XML Schema Date datatype.
     * @return a java.util.Date that represents the XML Schema Date datatype.
     */
    public abstract Date toDate();

    /**
     * Sets all the fields by reading the values in an array.
     * @param values an array of shorts with the values.
     */
    public abstract void setValues(short[] values);

    /**
     * returns an array of short with all the fields that describe a date/time
     * type.
     * @return an array of short with all the fields that describe a date/time
     *         type.
     */
    public abstract short[] getValues();

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns true if the given year represents a leap year. A specific year is
     * a leap year if it is either evenly divisible by 400 OR evenly divisible
     * by 4 and not evenly divisible by 100.
     *
     * @param year
     *            the year to test where 0 < year <= 9999
     * @return true if the given year represents a leap year
     */
    public final boolean isLeap(int year) {
        return ((year % 4) == 0 && (year % 100) != 0) || (year % 400) == 0;
    }

    /**
     * Returns true if the given year represents a leap year. A specific year is
     * a leap year if it is either evenly divisible by 400 OR evenly divisible
     * by 4 and not evenly divisible by 100.
     *
     * @param year
     *            the year to test where 0 <= year <= 99
     * @param century
     *            the century to test where 0 <= century <= 99
     * @return true if the given year represents a leap year
     */
    private final boolean isLeap(short century, short year) {
        return isLeap(century * 100 + year);
    }

    //////////////////////////Setter methods////////////////////////////////////

    /**
     * Set the negative field to true.
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the
     *             "century+year is negative" field is not allowed.
     */
    public void setNegative() throws OperationNotSupportedException {
        _isNegative = true;
    }

    /**
     * Set the century field. Note: year 0000 is not allowed.
     * @param century the value to set
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the
     *             century field is not allowed
     */
    public void setCentury(short century) throws OperationNotSupportedException {
        String err = "";
        if (century < 0) {
            err = "century " + century + " must not be negative.";
            throw new IllegalArgumentException(err);
        } else if (_year == 0 && century == 0) {
            err = "century:  0000 is not an allowed year.";
            throw new IllegalArgumentException(err);
        }

        _century = century;
    }

    /**
     * Sets the Year field. Note: year 0000 is not allowed.
     *
     * @param year
     *            the year to set
     * @throws OperationNotSupportedException
     *             in an overridden method in a derived class if that derived
     *             class does not support the year element.
     */
    public void setYear(short year) throws OperationNotSupportedException {
        String err = "";
        if (year < 0) {
            err = "year " + year + " must not be negative.";
            throw new IllegalArgumentException(err);
        } else if (year == -1) {
            if (_century != -1) {
                err = "year can not be omitted unless century is also omitted.";
                throw new IllegalArgumentException(err);
            }
        } else if (year == 0 && _century == 0) {
            err = "year:  0000 is not an allowed year";
            throw new IllegalArgumentException(err);
        } else if (year > 99) {
            err = "year " + year + " is out of range:  0 <= year <= 99.";
            throw new IllegalArgumentException(err);
        }

        _year = year;
    }

    /**
     * Sets the Month Field. Note 1 <= month <= 12.
     * @param month the value to set up
     * @throws OperationNotSupportedException
     *             in an overridden method in a derived class if that derived
     *             class does not support the month element.
     */
    public void setMonth(short month) throws OperationNotSupportedException {
        String err = "";
        if (month == -1) {
            if (_century != -1) {
                 err = "month cannot be omitted unless the previous component is also omitted.\n"
                       + "only higher level components can be omitted.";
                 throw new IllegalArgumentException(err);
            }
        } else if (month < 1 || month > 12) {
            err = "month " + month + " is out of range:  1 <= month <= 12";
            throw new IllegalArgumentException(err);
        }

        _month = month;
    }

    /**
     * Sets the Day Field. Note:  This field is validated before the assignment
     * is done.
     *
     * @param day
     *            the value to set up
     * @throws OperationNotSupportedException
     *             in an overridden method in a derived class if that derived
     *             class does not support the day element.
     */
    public void setDay(short day) throws OperationNotSupportedException {
        String err = "";
        if  (day == -1) {
            if (_month != -1) {
                err = "day cannot be omitted unless the previous component is also omitted.\n"
                      + "only higher level components can be omitted.";
                throw new IllegalArgumentException(err);
            }
        } else if (day < 1) {
            err = "day " + day + " cannot be negative.";
            throw new IllegalArgumentException(err);
        }

        short maxDay = maxDayInMonthFor(_century, _year, _month);
        if (day > maxDay) {
            if (_month != 2) {
                err = "day " + day + " is out of range for month " + _month + ":  "
                      + "1 <= day <= " + maxDay;
                throw new IllegalArgumentException(err);
            } else if (isLeap(_century, _year)) {
                err = "day " + day + " is out of range for February in a leap year:  "
                      + "1 <= day <= 29";
                throw new IllegalArgumentException(err);
            } else {
                err = "day " + day + " is out of range for February in a non-leap year:  "
                      + "1 <= day <= 28";
                throw new IllegalArgumentException(err);
            }
        }

        _day = day;
    }

    /**
     * Sets the hour field for this date/time type.
     *
     * @param hour
     *            the hour to set
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the hour
     *             field is not allowed
     */
    public void setHour(short hour) throws OperationNotSupportedException {
        if (hour > 23) {
            String err = "hour " + hour + " must be strictly less than 24";
            throw new IllegalArgumentException(err);
        } else if (hour < 0) {
            String err = "hour " + hour + " cannot be negative.";
            throw new IllegalArgumentException(err);
        }

        _hour = hour;
    }

    /**
     * set the minute field for this date/time type.
     *
     * @param minute
     *            the minute to set.
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the
     *             minute field is not allowed
     */
    public void setMinute(short minute) throws OperationNotSupportedException {
        if (minute > 59) {
            String err = "minute " + minute + " must be strictly less than 60.";
            throw new IllegalArgumentException(err);
        } else if (minute < 0) {
            String err = "minute " + minute + " cannot be negative.";
            throw new IllegalArgumentException(err);
        }

        _minute = minute ;
    }

    /**
     * Sets the seconds field for this date/time type, including fractional
     * seconds.  (In this implementation, fractional seconds are limited
     * to milliseconds and are truncated at millseconds if more precision
     * is provided.)
     *
     * @param second
     *            the second to set
     * @param millsecond
     *            the millisecond to set
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the
     *             second field is not allowed
     */
    public void setSecond(short second,short millsecond) throws OperationNotSupportedException {
        setSecond(second);
        setMilliSecond(millsecond);
    }

    /**
     * Sets the seconds field for this date/time type, not including the
     * fractional seconds.  Any fractional seconds previously set is unmodified.
     *
     * @param second
     *            the second to set
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the
     *             second field is not allowed
     */
    public void setSecond(short second) throws OperationNotSupportedException {
        if (second > 60) {
           String err = "seconds " + second + " must be less than 60";
           throw new IllegalArgumentException(err);
        } else if (second < 0) {
           String err = "seconds "+second+" cannot be negative.";
           throw new IllegalArgumentException(err);
        }

        _second = second;
    }

    /**
     * Sets the millisecond field for this date/time type.
     *
     * @param millisecond
     *            the millisecond to set
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the
     *             millisecond field is not allowed
     */
    public void setMilliSecond(short millisecond) throws OperationNotSupportedException {
        if (millisecond < 0) {
            String err = "milliseconds " + millisecond + " cannot be negative.";
            throw new IllegalArgumentException(err);
        } else if (millisecond > 999) {
            String err = "milliseconds " + millisecond + " is out of bounds: 0 <= milliseconds <= 999.";
            throw new IllegalArgumentException(err);
        }

        _millsecond = millisecond;
    }

    /**
     * Sets the UTC field.
     */
    public void setUTC() {
        _UTC = true;
    }

    /**
     * Sets the time zone negative field to true.
     *
     * @param zoneNegative
     *            indicates whether or not the time zone is negative.
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the time zone fields
     *             is not allowed
     */
    public void setZoneNegative(boolean zoneNegative) {
        _zoneNegative = zoneNegative;
    }

    /**
     * Sets the time zone fields for this date/time type. A call to this method
     * means that the date/time type used is UTC.
     * <p>
     * For a negative time zone, you first assign the absolute value of the time
     * zone using this method and then you call
     * {@link #setZoneNegative(boolean)}.
     *
     * @param hour
     *            The time zone hour to set. Must be positive.
     * @param minute
     *            The time zone minute to set.
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the time
     *             zone fields is not allowed
     */
    public void setZone(short hour, short minute) {
        setZoneHour(hour);
        setZoneMinute(minute);
    }

    /**
     * Sets the time zone hour field for this date/time type. A call to this
     * method means that the date/time type used is UTC.
     * <p>
     * For a negative time zone, you first assign the absolute value of the time
     * zone using this method and then you call
     * {@link #setZoneNegative(boolean)}.
     *
     * @param hour
     *            the time zone hour to set.  Must be positive.
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the time
     *             zone fields is not allowed
     */
    public void setZoneHour(short hour) {
        if (hour > 23) {
            String err = "time zone hour " + hour + " must be strictly less than 24";
            throw new IllegalArgumentException(err);
        } else if (hour < 0) {
            String err = "time zone hour " + hour + " cannot be negative.";
            throw new IllegalArgumentException(err);
        }

        _zoneHour = hour;

        // Any call to setZone means that you use the date/time you use is UTC
        setUTC();
    }

    /**
     * Sets the time zone minute field for this date/time type. A call to this
     * method means that the date/time type used is UTC.
     *
     * @param minute
     *            the time zone minute to set
     * @throws OperationNotSupportedException
     *             this exception is thrown when changing the value of the time
     *             zone fields is not allowed
     */
    public void setZoneMinute(short minute) {
        if (minute > 59) {
            String err = "time zone minute " + minute + " must be strictly lower than 60";
            throw new IllegalArgumentException(err);
        } else if (minute < 0) {
            String err = "time zone minute " + minute + " cannot be negative.";
            throw new IllegalArgumentException(err);
        }

        _zoneMinute = minute;

        // Any call to setZone means that you use the date/time you use is UTC
        setUTC();
    }

    ////////////////////////Getter methods//////////////////////////////////////

    public boolean isNegative() throws OperationNotSupportedException {
        return _isNegative;
    }

    public short getCentury() throws OperationNotSupportedException {
        return _century;
    }

    public short getYear() throws OperationNotSupportedException {
        return _year;
    }

    public short getMonth() throws OperationNotSupportedException {
        return _month;
    }

    public short getDay() throws OperationNotSupportedException {
        return _day;
    }

    public short getHour() throws OperationNotSupportedException {
        return _hour;
    }

    public short getMinute() throws OperationNotSupportedException {
        return _minute;
    }

    public short getSeconds() throws OperationNotSupportedException {
        return _second;
    }

    public short getMilli() throws OperationNotSupportedException {
        return _millsecond;
    }

    /**
     * Returns true if this date/time type is UTC, that is, has a time zone
     * assigned. A date/time type is UTC if a 'Z' appears at the end of the
     * lexical representation type or if it contains a time zone.
     *
     * @return true if this type has a time zone assigned, else false.
     */
    public boolean isUTC() {
        return _UTC;
    }

    public boolean isZoneNegative() {
        return _zoneNegative;
    }

    public short getZoneHour() {
        return _zoneHour;
    }

    public short getZoneMinute() {
        return _zoneMinute;
    }

    ////////////////////////Getter methods//////////////////////////////////////

    public boolean hasIsNegative() {
        return true;
    }

    public boolean hasCentury() {
        return true;
    }

    public boolean hasYear() {
        return true;
    }

    public boolean hasMonth() {
        return true;
    }

    public boolean hasDay() {
        return true;
    }

    public boolean hasHour() {
        return true;
    }

    public boolean hasMinute() {
        return true;
    }

    public boolean hasSeconds() {
        return true;
    }

    public boolean hasMilli() {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds a Duration to this Date/Time type as defined in <a
     * href="http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes">
     * Adding Duration to dateTimes (W3C XML Schema, part 2 appendix E).</a>
     * This version uses the algorithm defined in the document from W3C. A later
     * version may optimize it.
     * <p>
     * The modified Date/Time instance will keep the same time zone it started
     * with, if any.
     * <p>
     * Don't use getter methods but use direct field access for dateTime in
     * order to have the behaviour defined in the Recommendation document.
     *
     * @param duration
     *            the duration to add
     */
    public void addDuration(Duration duration) {
        int temp  = 0;
        int carry = 0;
        int sign  = (duration.isNegative()) ? -1 : 1;

        // First add the month and year.

        // Months
        try {
            temp = _month + sign * duration.getMonth();
            carry = fQuotient(temp - 1, 12);
            this.setMonth((short) (modulo(temp - 1, 12) + 1));
        } catch (OperationNotSupportedException e) {
            // Ignore
        }

        // Years
        try {
            temp = _century * 100 + _year + sign * duration.getYear() + carry;
            this.setCentury((short) (temp / 100));
            this.setYear((short) (temp % 100));
        } catch (OperationNotSupportedException e) {
            // Ignore
        }

        // Next, pin the day-of-month so it is not outside the new month.

        int tempDay = _day;
        if (tempDay < 1) {
            tempDay = 1;
        } else {
            int maxDay = maxDayInMonthFor(_century, _year, _month);
            if (_day > maxDay) {
                tempDay = maxDay;
            }
        }

        // Next, add the time components

        // Seconds
        try {
            temp = _millsecond + sign * (int)duration.getMilli();
            carry = fQuotient(temp, 1000);
            this.setMilliSecond((short)modulo(temp, 1000));

            temp = _second + sign * duration.getSeconds() + carry;
            carry = fQuotient(temp, 60);
            this.setSecond((short)modulo(temp , 60));
        } catch (OperationNotSupportedException e) {
            // Ignore
        }

        // Minutes
        try {
            temp = _minute + sign * duration.getMinute() + carry;
            carry = fQuotient(temp, 60);
            this.setMinute((short)modulo(temp , 60));
        } catch (OperationNotSupportedException e) {
            // Ignore
        }

        // Hours
        try {
            temp = _hour + sign*duration.getHour() + carry;
            carry = fQuotient(temp, 24);
            this.setHour((short)modulo(temp , 24));
        } catch (OperationNotSupportedException e) {
            // Ignore
        }

        // Finally, set the day-of-month, rolling the month & year as needed

        // Days
        try {
            tempDay += sign * duration.getDay() + carry;

            // Loop until the day-of-month is within bounds
            while (true) {
                short maxDay = maxDayInMonthFor(_century, _year, _month);
                if (tempDay < 1) {
                    tempDay = (short) (tempDay + maxDayInMonthFor(_century, _year, _month - 1));
                    carry = -1;
                } else if (tempDay > maxDay) {
                    tempDay = (short)(tempDay - maxDay);
                    carry = 1;
                } else {
                    break;
                }

                try {
                    temp = _month + carry;
                    this.setMonth((short)(modulo(temp - 1, 12) + 1));
                    temp = this.getCentury() * 100 + this.getYear() + fQuotient(temp - 1, 12);
                    this.setCentury((short) (temp / 100));
                    this.setYear((short) (temp % 100));
                } catch (OperationNotSupportedException e) {
                    // Ignore
                }
            }

            this.setDay((short)tempDay);
        } catch (OperationNotSupportedException e) {
            // Ignore
        }
    } // addDuration

    ///////////////////////W3C XML SCHEMA Helpers///////////////////////////////

    /**
     * Helper function defined in W3C XML Schema Recommendation part 2.
     * @see <a href="http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes">
     * W3C XML Schema Recommendation part 2</a>
     */
     private int fQuotient(int a, int b) {
         return (int) Math.floor((float)a / (float)b);
     }

     /**
      * Helper function defined in W3C XML Schema Recommendation part 2.
      * @see <a href="http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes">
      * W3C XML Schema Recommendation part 2</a>
      */
     private int modulo(int a, int b) {
         return a - fQuotient(a,b) * b;
     }

     /**
     * Returns the maximum day in the given month of the given year.
     *
     * @param year
     * @param month
     * @return the maximum day in the given month of the given year.
     */
    private final short maxDayInMonthFor(short century, short year, int month) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else if (month == 2) {
            return (short) ((isLeap(century,year)) ? 29 : 28);
        } else {
            return 31;
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Normalizes a date/time datatype as defined in W3C XML Schema
     * Recommendation document: if a timeZone is present but it is not Z then we
     * convert the date/time datatype to Z using the addition operation defined
     * in <a
     * href="http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes">
     * Adding Duration to dateTimes (W3C XML Schema, part 2 appendix E).</a>
     *
     * @see #addDuration
     */
    public void normalize() {
        if (!isUTC() || (_zoneHour == 0 && _zoneMinute == 0)) {
            return;
        }

        Duration temp = new Duration();
        temp.setHour(_zoneHour);
        temp.setMinute(_zoneMinute);
        if (isZoneNegative()) {
            temp.setNegative();
        }

        this.addDuration(temp);

        //reset the zone
        this.setZone((short)0, (short)0);
        this.setZoneNegative(false);
    }

    /**
     * Compares two date/time data types. The algorithm of comparison is defined
     * in <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">W3C XML Schema
     * Recommendation (section 3.2.7.3)</a>
     * <p>
     * The returned value will be one of:
     * <ul>
     * <li>INDETERMINATE (-1): this ? dateTime</li>
     * <li>LESS_THAN (0): this < dateTime</li>
     * <li>EQUALS (1): this == dateTime</li>
     * <li>GREATER_THAN (2): this > dateTime</li>
     * </ul>
     * <p>
     * FIXME:  This code does not compare time zones properly for date/time types
     * that do not contain a time.
     *
     * @param dateTime
     *            the dateTime to compare with the current instance.
     * @return the status of the comparison.
     */
    public int compareTo(DateTimeBase dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("a Date/Time datatype cannot be compared with a null value");
        }

        // Make copies of the date/times we compare so we DO NOT MODIFY THE CURRENT VALUES!
        DateTimeBase tempDate1;
        DateTimeBase tempDate2;

        try {
            tempDate1 = clone(this);
            if (tempDate1.isUTC()) {
                tempDate1.normalize();
            }

            tempDate2 = clone(dateTime);
            if (tempDate2.isUTC()) {
                tempDate2.normalize();
            }
//        } catch (InstantiationException e) {
//            // This is a Castor coding error if this occurs -- it should never occur
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            // This is a Castor coding error if this occurs -- it should never occur
//            throw new RuntimeException(e);
        } catch (CloneNotSupportedException e) {
            // This is a Castor coding error if this occurs -- it should never occur
            throw new RuntimeException(e);
        }

        // If both date/time types are in Z-form (or both not), we just compare the fields.
        if (tempDate1.isUTC() == tempDate2.isUTC()) {
            return compareFields(tempDate1, tempDate2);
        }

        // If datetime1 has a time zone and datetime2 does not
        if (tempDate1.isUTC()) {
            tempDate2.setZone((short)MAX_TIME_ZONE_COMPARISON_OFFSET,(short)0);
            tempDate2.normalize();
            int result = compareFields(tempDate1, tempDate2);
            if (result == LESS_THAN) {
                return result;
            }

            // Restore time from previous offsetting
            tempDate2.setZone((short)MAX_TIME_ZONE_COMPARISON_OFFSET,(short)0);
            tempDate2.setZoneNegative(true);
            tempDate2.normalize();

            tempDate2.setZone((short)MAX_TIME_ZONE_COMPARISON_OFFSET,(short)0);
            tempDate2.setZoneNegative(true);
            tempDate2.normalize();
            result = compareFields(tempDate1, tempDate2);
            if (result == GREATER_THAN) {
                return result;
            }
            return INDETERMINATE;
        }

        // If datetime2 has a time zone and datetime1 does not
        if (tempDate2.isUTC()) {
            tempDate1.setZone((short)MAX_TIME_ZONE_COMPARISON_OFFSET,(short)0);
            tempDate1.normalize();
            int result = compareFields(tempDate1, tempDate2);
            if (result == GREATER_THAN) {
                return result;
            }

            // Restore time from previous offsetting
            tempDate1.setZone((short)MAX_TIME_ZONE_COMPARISON_OFFSET,(short)0);
            tempDate1.setZoneNegative(true);
            tempDate1.normalize();

            tempDate1.setZone((short)MAX_TIME_ZONE_COMPARISON_OFFSET,(short)0);
            tempDate1.setZoneNegative(true);
            tempDate1.normalize();
            result = compareFields(tempDate1, tempDate2);
            if (result == LESS_THAN) {
                return result;
            }
            return INDETERMINATE;
        }

        return INDETERMINATE;
    }

    /**
     * Copies a dateTime instance of some type -- all fields may or may not be
     * present. Always copy all fields, even if they are 0, and create a full
     * DateTime with all fields. This allows quick comparisons (no exceptions
     * thrown, which are expensive).
     */
    private DateTimeBase copyDateTimeInstance(DateTimeBase dateTime) {
        DateTimeBase newDateTime = new DateTime();
        newDateTime._isNegative   = dateTime._isNegative;
        newDateTime._century      = dateTime._century;
        newDateTime._year         = dateTime._year;
        newDateTime._month        = dateTime._month;
        newDateTime._day          = dateTime._day;
        newDateTime._hour         = dateTime._hour;
        newDateTime._minute       = dateTime._minute;
        newDateTime._second       = dateTime._second;
        newDateTime._millsecond   = dateTime._millsecond;
        newDateTime._zoneNegative = dateTime._zoneNegative;
        newDateTime._UTC          = dateTime._UTC;
        newDateTime._zoneHour     = dateTime._zoneHour;
        newDateTime._zoneMinute   = dateTime._zoneMinute;
        return newDateTime;
    }

    public DateTimeBase clone(DateTimeBase dateTime) throws CloneNotSupportedException {
        DateTimeBase newDateTime = (DateTimeBase) super.clone();
//        newDateTime = (DateTimeBase) dateTime.getClass().newInstance();
        newDateTime.setValues(dateTime.getValues());
        if (dateTime.hasIsNegative() && dateTime.isNegative()) {
           newDateTime.setNegative();
        }
        if (dateTime.isUTC()) {
            newDateTime.setUTC();
            newDateTime.setZone(dateTime.getZoneHour(), dateTime.getZoneMinute());
            newDateTime.setZoneNegative(dateTime.isZoneNegative());
        }
        return newDateTime;
    }

    private static int compareFields(DateTimeBase date1, DateTimeBase date2) {
        short field1;
        short field2;

        if (date1.hasCentury() != date2.hasCentury()) {
            return INDETERMINATE;
        }

        if (date1.hasCentury() && date2.hasCentury()) {
            field1 = date1.getCentury();
            field2 = date2.getCentury();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        if (date1.hasYear() != date2.hasYear()) {
            return INDETERMINATE;
        }

        if (date1.hasYear() && date2.hasYear()) {
            field1 = date1.getYear();
            field2 = date2.getYear();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        if (date1.hasMonth() != date2.hasMonth()) {
            return INDETERMINATE;
        }

        if (date1.hasMonth() && date2.hasMonth()) {
            field1 = date1.getMonth();
            field2 = date2.getMonth();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        if (date1.hasDay() != date2.hasDay()) {
            return INDETERMINATE;
        }

        if (date1.hasDay() && date2.hasDay()) {
            field1 = date1.getDay();
            field2 = date2.getDay();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        if (date1.hasHour() != date2.hasHour()) {
            return INDETERMINATE;
        }

        if (date1.hasHour() && date2.hasHour()) {
            field1 = date1.getHour();
            field2 = date2.getHour();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        if (date1.hasMinute() != date2.hasMinute()) {
            return INDETERMINATE;
        }

        if (date1.hasMinute() && date2.hasMinute()) {
            field1 = date1.getMinute();
            field2 = date2.getMinute();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        if (date1.hasSeconds() != date2.hasSeconds()) {
            return INDETERMINATE;
        }

        if (date1.hasSeconds() && date2.hasSeconds()) {
            field1 = date1.getSeconds();
            field2 = date2.getSeconds();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        if (date1.hasMilli() != date2.hasMilli()) {
            return INDETERMINATE;
        }

        if (date1.hasMilli() && date2.hasMilli()) {
            field1 = date1.getMilli();
            field2 = date2.getMilli();
            if (field1 < field2) {
                return LESS_THAN;
            } else if (field1 > field2) {
                return GREATER_THAN;
            }
        }

        return EQUALS;
    }

    /**
     * {@inheritDoc}
     * Overrides the java.lang.Object#hashcode method.
     */
    public int hashCode() {
        return _year^_month^_day^_hour^_minute^_second^_millsecond^_zoneHour^_zoneMinute;
    }

    /**
     * {@inheritDoc}
     * Overrides the java.lang.Object#equals method.
     * @see #equals(Object)
     */
    public boolean equals(Object object) {
        // No need to check if we are comparing two instances of the same class.
        // (if the class is not the same then #equal will return false).
        if (object instanceof DateTimeBase) {
            return equal((DateTimeBase) object);
        }
        return false;
    }

    /**
     * Returns true if the present instance of date/time type is equal to the
     * parameter.
     * <p>
     * The equals relation is as defined in the W3C XML Schema Recommendation,
     * part2.
     *
     * @param dateTime
     *            the date/time type to compare with the present instance
     * @return true if the present instance is equal to the parameter false if
     *         not
     */
    protected boolean equal(DateTimeBase dateTime) {
        return EQUALS == this.compareTo(dateTime);
    } //equals

    /**
     * converts this Date/Time into a local java Calendar.
     * @return a local calendar representing this Date or Time
     */
    public Calendar toCalendar(){
        Calendar result = new GregorianCalendar();
        result.setTime(toDate());
        return result;
    } //toCalendar()

    ////////////// COMMON CODE USED BY EXTENDING CLASSES ///////////////////////

    protected static int parseYear(final String str, final DateTimeBase result, final char[] chars,
                                   final int index, final String complaint) throws ParseException {
        int idx = index;

        if (chars[idx] == '-') {
            idx++;
            result.setNegative();
        }

        if (str.length() < idx + 4
            || !Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])
            || !Character.isDigit(chars[idx + 2]) || !Character.isDigit(chars[idx + 3])) {
            throw new ParseException(complaint + str + "\nThe Year must be 4 digits long", idx);
        }

        short value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        short value2 = (short) ((chars[idx+2] - '0') * 10 + (chars[idx+3] - '0'));

        if (value1 == 0 && value2 == 0) {
            throw new ParseException(complaint + str + "\n'0000' is not allowed as a year.", idx);
        }

        result.setCentury(value1);
        result.setYear(value2);

        idx += 4;

        return idx;
    }

    protected static int parseMonth(final String str, final DateTimeBase result, final char[] chars,
                                    final int index, final String complaint) throws ParseException {
        int idx = index;

        if (chars[idx] != '-') {
            throw new ParseException(complaint + str + "\n '-' " + DateTimeBase.WRONGLY_PLACED, idx);
        }

        idx++;

        if (str.length() < idx + 2 || !Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
            throw new ParseException(complaint + str + "\nThe Month must be 2 digits long", idx);
        }

        short value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        result.setMonth(value1);

        idx += 2;
        return idx;
    }

    protected static int parseDay(final String str, final DateTimeBase result, final char[] chars,
                                  final int index, final String complaint) throws ParseException {
        int idx = index;

        if (chars[idx] != '-') {
            throw new ParseException(complaint + str + "\n '-' " + DateTimeBase.WRONGLY_PLACED, idx);
        }

        idx++;

        if (str.length() < idx + 2 || !Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
            throw new ParseException(complaint + str + "\nThe Day must be 2 digits long", idx);
        }

        short value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        result.setDay(value1);

        idx += 2;
        return idx;
    }

    protected static int parseTime(final String str, final DateTimeBase result, final char[] chars,
                                   final int index, final String complaint) throws ParseException {
        int idx = index;

        if (str.length() < idx + 8) {
            throw new ParseException(complaint + str + "\nA Time field must be at least 8 characters long", idx);
        }

        if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
            throw new ParseException(complaint + str + "\nThe Hour must be 2 digits long", idx);
        }

        short value1;

        value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        result.setHour(value1);

        idx += 2;

        // Minutes
        if (chars[idx] != ':') {
            throw new ParseException(complaint + str + "\n ':#1' " + DateTimeBase.WRONGLY_PLACED, idx);
        }

        idx++;

        if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
            throw new ParseException(complaint+str+"\nThe Minute must be 2 digits long", idx);
        }

        value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        result.setMinute(value1);

        idx += 2;

        // Seconds
        if (chars[idx] != ':') {
            throw new ParseException(complaint + str + "\n ':#2' " + DateTimeBase.WRONGLY_PLACED, idx);
        }

        idx++;

        if (!Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])) {
            throw new ParseException(complaint + str + "\nThe Second must be 2 digits long", idx);
        }

        value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
        result.setSecond(value1);

        idx += 2;

        if (idx < chars.length && chars[idx] == '.') {
            idx++;

            long decimalValue = 0;
            long powerOfTen   = 1;
            while (idx < chars.length && Character.isDigit(chars[idx])) {
                decimalValue = decimalValue * 10 + (chars[idx] - '0');
                powerOfTen *= 10;
                idx++;
            }

            // Explicitly truncate to milliseconds if more digits were provided
            if (powerOfTen > 1000) {
                decimalValue /= (powerOfTen / 1000);
                powerOfTen = 1000;
            } else if (powerOfTen < 1000) {
                decimalValue *= (1000 / powerOfTen);
                powerOfTen = 1000;
            }
            result.setMilliSecond((short)decimalValue);
        }

        return idx;
    }

    protected static int parseTimeZone(final String str, final DateTimeBase result, final char[] chars,
                                       final int index, final String complaint) throws ParseException {
        // If we're at the end of the string, there's no time zone to parse
        if (index >= chars.length) {
            return index;
        }

        int idx = index;

        if (chars[idx] == 'Z') {
            result.setUTC();
            return ++idx;
        }

        if (chars[idx] == '+' || chars[idx] == '-') {
            if (chars[idx] == '-') {
                result.setZoneNegative(true);
            }
            idx++;
            if (idx + 5 > chars.length || chars[idx + 2] != ':'
                || !Character.isDigit(chars[idx]) || !Character.isDigit(chars[idx + 1])
                || !Character.isDigit(chars[idx + 3]) || !Character.isDigit(chars[idx + 4])) {
                throw new ParseException(complaint+str+"\nTimeZone must have the format (+/-)hh:mm", idx);
            }
            short value1 = (short) ((chars[idx] - '0') * 10 + (chars[idx+1] - '0'));
            short value2 = (short) ((chars[idx+3] - '0') * 10 + (chars[idx+4] - '0'));
            result.setZone(value1,value2);
            idx += 5;
        }

        return idx;
    }

    /**
     * Sets the time zone in the provided DateFormat.
     * @param df
     */
    protected void setDateFormatTimeZone(DateFormat df) {
        // If no time zone, nothing to do
        if (! isUTC()) {
            return;
        }

        int offset = (this.getZoneMinute() + this.getZoneHour() * 60) * 60 * 1000;
        offset = isZoneNegative() ? -offset : offset;

        SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");
        timeZone.setRawOffset(offset);
        timeZone.setID(TimeZone.getAvailableIDs(offset)[0]);
        df.setTimeZone(timeZone);
    }

    /**
     * Sets the time zone in the provided Calendar.
     * @param calendar
     */
    protected void setDateFormatTimeZone(Calendar calendar) {
        // If no time zone, nothing to do
        if (! isUTC()) {
            return;
        }

        int offset = (this.getZoneMinute() + this.getZoneHour() * 60) * 60 * 1000;
        offset = isZoneNegative() ? -offset : offset;

        SimpleTimeZone timeZone = new SimpleTimeZone(0,"UTC");
        timeZone.setRawOffset(offset);
        timeZone.setID(TimeZone.getAvailableIDs(offset)[0]);
        calendar.setTimeZone(timeZone);
    }

    protected void appendDateString(StringBuffer result) {
        if (isNegative()) {
            result.append('-');
        }

        if ((this.getCentury() / 10) == 0) {
            result.append(0);
        }
        result.append(this.getCentury());

        if ((this.getYear() / 10) == 0) {
            result.append(0);
        }
        result.append(this.getYear());

        result.append('-');
        if ((this.getMonth() / 10) == 0) {
            result.append(0);
        }
        result.append(this.getMonth());

        result.append('-');
        if ((this.getDay()/10) == 0) {
            result.append(0);
        }
        result.append(this.getDay());
    }

    protected void appendTimeString(StringBuffer result) {
        if ((this.getHour()/10) == 0) {
            result.append(0);
        }
        result.append(this.getHour());

        result.append(':');

        if ((this.getMinute() / 10) == 0) {
            result.append(0);
        }
        result.append(this.getMinute());

        result.append(':');

        if ((this.getSeconds() / 10) == 0) {
            result.append(0);
        }
        result.append(this.getSeconds());

        if (this.getMilli() != 0) {
            result.append('.');
            if (this.getMilli() < 100){
                result.append('0');
                if (this.getMilli() < 10){
                    result.append('0');
                }
            }
            result.append(this.getMilli());
        }
    }

    protected void appendTimeZoneString(StringBuffer result) {
        if (!isUTC()) {
            return;
        }

        // By default we append a 'Z' to indicate UTC
        if (this.getZoneHour() == 0 && this.getZoneMinute() == 0) {
            result.append('Z');
            return;
        }

        if (isZoneNegative()) {
            result.append('-');
        } else {
            result.append('+');
        }

        if ((this.getZoneHour()/10) == 0) {
            result.append(0);
        }
        result.append(this.getZoneHour());

        result.append(':');
        if ((this.getZoneMinute()/10) == 0) {
            result.append(0);
        }
        result.append(this.getZoneMinute());
    }

} //-- DateTimeBase
