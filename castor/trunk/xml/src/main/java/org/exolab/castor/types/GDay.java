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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author              Changes
 * 04/18/2002   Arnaud              String constructor
 * 05/24/2001   Arnaud Blandin      Created
 */
package org.exolab.castor.types;

import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Describe an XML schema gDay type.
 * <p>The format is defined by W3C XML Schema Recommendation and ISO8601
 * i.e <tt>---DD(Z|(+|-)hh:mm)</tt>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$
 */
public class GDay extends DateTimeBase {

    /** SerialVersionUID */
    private static final long serialVersionUID = 8571596440117087631L;

    /** The gDay SimpleDateFormat string. */
    private static final String DAY_FORMAT = "---dd";
    /** Prefix of any complaint we make. */
    private static final String BAD_GDAY = "Bad gDay format: ";

    /**
     * public only for the generated source code
     */
    public GDay() {
        // Nothing needed
    }

    /**
     * Constructs a XML Schema GDay instance given all the values of
     * the different fields.
     * By default a GDay is not UTC and is local.
     * @param day
     */
    public GDay(short day) {
       setDay(day);
    }

    /**
     * Constructs a XML Schema GDay instance given all the values of
     * the different fields.
     * By default a GDay is not UTC and is local.
     * @param day
     */
    public GDay(int day) {
        setDay((short)day);
    }

    /**
     * Constructs a GDay from a string value.
     * @param gday the string representation of the GDay to instantiate
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public GDay(String gday) throws ParseException {
        parseGDayInternal(gday, this);
    }

    /**
     * Sets all the fields by reading the values in an array
     * <p>
     * if a Time Zone is specificied it has to be set by using
     * {@link DateTimeBase#setZone(short, short) setZone}.
     *
     * @param values
     *            an array of shorts with the values the array is supposed to be
     *            of length 1 and ordered like the following:
     *            <ul>
     *            <li>Day</li>
     *            </ul>
     */
    public void setValues(short[] values) {
        if (values.length != 1) {
            throw new IllegalArgumentException("GDay#setValues: not the right number of values");
        }
        this.setDay(values[0]);
    }

    /**
     * Returns an array of short with all the fields that describe
     * this gDay type.
     * <p>Note:the time zone is not included.
     * @return  an array of short with all the fields that describe
     * this Date type.
     */
    public short[] getValues() {
        short[] result = new short[1];
        result[0] = this.getDay();
        return result;
    } //getValues

    /**
     * converts this GDay into a local java Date.
     * @return a local date representing this Date.
     */
    public java.util.Date toDate() {
        SimpleDateFormat df = new SimpleDateFormat(DAY_FORMAT);
        setDateFormatTimeZone(df);

        java.util.Date date = null;
        try {
            date = df.parse(this.toString());
        } catch (ParseException e) {
            //this can't happen since toString() should return the proper string format
            e.printStackTrace();
            return null;
        }

        return date;
    } //toDate()

    /**
     * convert this GDay to a string
     * The format is defined by W3C XML Schema recommendation and ISO8601
     * i.e ---DD(Z|(+|-)hh:mm)
     * @return a string representing this Date
     */
     public String toString() {
        StringBuffer result = new StringBuffer("---");

        if ((this.getDay()/10) == 0) {
            result.append(0);
        }
        result.append(this.getDay());

        appendTimeZoneString(result);

        return result.toString();
    } //toString

    /**
     * parse a String and convert it into an java.lang.Object
     * @param str the string to parse
     * @return an Object represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static Object parse(String str) throws ParseException {
        return parseGDay(str);
    }

    /**
     * parse a String and convert it into a GDay.
     * @param str the string to parse
     * @return the Date represented by the string
     * @throws ParseException a parse exception is thrown if the string to parse
     *                        does not follow the rigth format (see the description
     *                        of this class)
     */
    public static GDay parseGDay(String str) throws ParseException {
        GDay result = new GDay();
        return parseGDayInternal(str, result);
    }

    private static GDay parseGDayInternal(String str, GDay result) throws ParseException {
        if (str == null) {
             throw new IllegalArgumentException("The string to be parsed must not be null.");
        }

        if (result == null) {
            result = new GDay();
        }

        char[] chars = str.toCharArray();
        if (chars[0] != '-' || chars[1] != '-' || chars[2] != '-') {
            throw new ParseException(BAD_GDAY+str+"\nA gDay must follow the pattern ---DD(Z|((+|-)hh:mm)).", 0);
        }

        int idx = 2;
        idx = parseDay(str, result, chars, idx, BAD_GDAY);
        parseTimeZone(str, result, chars, idx, BAD_GDAY);

        return result;
    } //parse

    /////////////////////////// DISALLOWED METHODS ///////////////////////////

    public boolean hasIsNegative() {
        return false;
    }
    public boolean isNegative() {
        String err = "org.exolab.castor.types.GDay does not have a 'negative' field.";
        throw new UnsupportedOperationException(err);
    }
    public void setNegative() {
        String err = "org.exolab.castor.types.GDay cannot be negative.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasCentury() {
        return false;
    }
    public short getCentury() {
        String err = "org.exolab.castor.types.GDay does not have a Century field.";
        throw new UnsupportedOperationException(err);
    }
    public void setCentury(short century) {
        String err = "org.exolab.castor.types.GDay does not have a Century field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasYear() {
        return false;
    }
    public short getYear() {
        String err = "org.exolab.castor.types.GDay does not have a Year field.";
        throw new UnsupportedOperationException(err);
    }
    public void setYear(short year) {
        String err = "org.exolab.castor.types.GDay does not have a Year field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasMonth() {
        return false;
    }
    public short getMonth() {
        String err = "org.exolab.castor.types.GDay does not have a Month field.";
        throw new UnsupportedOperationException(err);
    }
    public void setMonth(short month) {
        String err = "org.exolab.castor.types.GDay does not have a Month field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasHour() {
        return false;
    }
    public short getHour() {
        String err = "org.exolab.castor.types.GDay does not have an Hour field.";
        throw new UnsupportedOperationException(err);
    }
    public void setHour(short hour) {
        String err = "org.exolab.castor.types.GDay does not have an Hour field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasMinute() {
        return false;
    }
    public short getMinute() {
        String err = "org.exolab.castor.types.GDay does not have a Minute field.";
        throw new UnsupportedOperationException(err);
    }
    public void setMinute(short minute) {
        String err = "org.exolab.castor.types.GDay does not have a Minute field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasSeconds() {
        return false;
    }
    public short getSeconds() {
        String err = "org.exolab.castor.types.GDay does not have a Seconds field.";
        throw new UnsupportedOperationException(err);
    }
    public void setSecond(short second) {
        String err = "org.exolab.castor.types.GDay does not have a Seconds field.";
        throw new UnsupportedOperationException(err);
    }
    public boolean hasMilli() {
        return false;
    }
    public short getMilli() {
        String err = "org.exolab.castor.types.GDay does not have a Milliseconds field.";
        throw new UnsupportedOperationException(err);
    }
    public void setMilliSecond(short millisecond) {
        String err = "org.exolab.castor.types.GDay does not have a Milliseconds field.";
        throw new UnsupportedOperationException(err);
    }

}
