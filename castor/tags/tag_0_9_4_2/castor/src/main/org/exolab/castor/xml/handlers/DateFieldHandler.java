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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.handlers;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.xml.XMLFieldHandler;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.SimpleTimeZone;
import java.util.Vector;

/**
 * A specialized FieldHandler for the XML Schema
 * Date/Time related types
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DateFieldHandler extends XMLFieldHandler {


    public static final String DATE_FORMAT =
        "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_FORMAT_2 =
        "yyyy-MM-dd'T'HH:mm:ss";

	private static TimeZone _defaultTimezone = TimeZone.getDefault();

    private FieldHandler handler = null;

    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new DateFieldHandler using the given
     * FieldHandler for delegation.
     * @param fieldHandler the fieldHandler for delegation.
    **/
    public DateFieldHandler(FieldHandler fieldHandler) {

        if (fieldHandler == null) {
            String err = "The FieldHandler argument passed to " +
                "the constructor of DateFieldHandler must not be null.";
            throw new IllegalArgumentException(err);
        }
        this.handler = fieldHandler;
    } //-- DateFieldHandler


    //------------------/
    //- Public Methods -/
    //------------------/


    /**
     * Returns the value of the field associated with this
     * descriptor from the given target object.
     * @param target the object to get the value from
     * @return the value of the field associated with this
     * descriptor from the given target object.
    **/
    public Object getValue(Object target)
        throws java.lang.IllegalStateException
    {

        Object val = handler.getValue(target);

        if (val == null) return val;

		Object formatted = null;

        Class type = val.getClass();
        
        if (java.util.Date.class.isAssignableFrom(type)) {
            formatted = getFormattedDate((Date)val);
        }
        else if (type.isArray()) {
            int size = Array.getLength(val);
            String[] values = new String[ size ];
            for (int i = 0; i < size; i++) {
                values[i] = format(Array.get(val, i));
            }
            formatted = values;
        }
        else if (java.util.Enumeration.class.isAssignableFrom(type)) {
            Enumeration enum = (Enumeration)val;
            Vector values = new Vector();
            while (enum.hasMoreElements()) {
                values.addElement(format(enum.nextElement()));
            }
            String[] valuesArray = new String[values.size()];
            values.copyInto(valuesArray);
            formatted = valuesArray;
        }
        else {
            formatted = val.toString();
        }
        return formatted;
    } //-- getValue

    /**
     * Sets the value of the field associated with this descriptor.
     * @param target the object in which to set the value
     * @param value the value of the field
    **/
    public void setValue(Object target, Object value)
        throws java.lang.IllegalStateException
    {
        Date date = null;

        if (! (value instanceof Date) ) {

            DateFormat df = new SimpleDateFormat(DATE_FORMAT);

            if ( (value.toString().indexOf(".")) == -1)
                df = new SimpleDateFormat(DATE_FORMAT_2);

            // Check for XML Schema supported timezone portion
            TimeZone tz = getTimeZone( value.toString() );

            if ( tz != null ) {
                df.setTimeZone( tz );
            }

            try {
                date = df.parse(value.toString());
            }
            catch (java.text.ParseException ex) {
                //-- if there is no value we return
                //-- a new date
                if (value == null) date = new Date();
                //-- else it is not a valid timeInstant
                throw new IllegalStateException("Bad 'timeInstant' format:it should be "+DATE_FORMAT+".\n");
            }
        }
        else date = (Date)value;

        handler.setValue(target, date);

    } //-- setValue

    public void resetValue(Object target)
        throws java.lang.IllegalStateException
    {
        handler.resetValue(target);
    }


    /**
     * Checks the field validity. Returns successfully if the field
     * can be stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The field is invalid, is required and
     *  null, or any other validity violation
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler
     *  is not compatiable with the Java object
     */
    public void checkValidity( Object object )
        throws ValidityException, IllegalStateException
    {
        //-- do nothing for now
    } //-- checkValidity


    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        return new Date();
    } //-- newInstance

    /**
     * Returns true if the given object is an XMLFieldHandler that
     * is equivalent to the delegated handler. An equivalent XMLFieldHandler
	 * is an XMLFieldHandler that is an instances of the same class.
     *
     * @return true if the given object is an XMLFieldHandler that
     * is equivalent to this one.
    **/
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof FieldHandler)) return false;
        return (handler.getClass().isInstance(obj) ||
                getClass().isInstance(obj));
    } //-- equals

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Interrogate datetime value for XML Schema Recommendation
     * compliant timezone.
     *
     * Datetime string expected to be one of following formats:
     *      (i)     yyyy-MM-dd'T'HH:mm:ss'Z'
     *      (ii)    yyyy-MM-dd'T'HH:mm:ss'+'HH:mm
     *      (iii)   yyyy-MM-dd'T'HH:mm:ss'-'HH:mm
     *
     * @param dateTime The datetime as a String
     * @return The timezone if found, null otherwise
     * @throws IllegalStateException datetime contains unsupported timezone or
     *                               timezone indicator in wrong format
     */
    private TimeZone getTimeZone( String dateTime ) throws IllegalStateException {
        TimeZone tz = null;
        int pos = -1;

        // First, check for UTC timezone
        if ( dateTime.indexOf( 'Z' ) != -1 ) {
            tz = TimeZone.getTimeZone( "UTC" );
        } else if ( ( pos = dateTime.indexOf( '+' ) ) != -1 ) {
            // Check that timezone conforms to HH:mm schema format
            try {
                Date dt = new SimpleDateFormat( "HH:mm" ).parse( dateTime.substring( pos + 1 ) );
                Calendar cal = new GregorianCalendar();
                cal.setTime( dt );

                int offset = ( cal.get( Calendar.HOUR_OF_DAY ) * 60 * 60 * 1000 ) +
                             ( cal.get( Calendar.MINUTE ) * 60 * 1000 );
				return new SimpleTimeZone(offset, "UTC");

            } catch ( ParseException pe ) {
                throw new IllegalStateException( "Invalid 'timezone' format : should be '+HH:mm'\n" );
            }
        } else if ( ( pos = dateTime.lastIndexOf( '-' ) ) != -1 ) {
            // Check that dash not part of the date ( i.e. must be after the 'T' separator )
            int posT = dateTime.indexOf( 'T' );

            if ( pos > posT ) {
                // Check that timezone conforms to HH:mm schema format
                try {
                    Date dt = new SimpleDateFormat( "HH:mm" ).parse( dateTime.substring( pos + 1 ) );
                    Calendar cal = new GregorianCalendar();
                    cal.setTime( dt );

                    int offset = -1 * ( ( cal.get( Calendar.HOUR_OF_DAY ) * 60 * 60 * 1000 ) +
                                        ( cal.get( Calendar.MINUTE ) * 60 * 1000 ) );
					return new SimpleTimeZone(offset, "UTC");

                } catch ( ParseException pe ) {
                    throw new IllegalStateException( "Invalid 'timezone' format : should be '-HH:mm'\n" );
                }
            }
        }

        return tz;
    } //-- getTimeZone

    /**
     * Formats the given object. If the object is a Date, a call
     * to #getFormattedDate will be made, otherwise the toString()
     * method is called on the object.
     *
     * @return the formatted object.
     */
    private static String format(Object object) {
        if (object == null) 
            return null;
        if (object instanceof java.util.Date) {
            return getFormattedDate((Date)object);
        }
        return object.toString();
    } //-- format
     
	/**
	 * Returns the Date formatted as
	 *  yyyy-MM-dd'T'HH:mm:ss.SSSZ or
	 *  yyyy-MM-dd'T'HH:mm:ss.SSS+HH:MM or
	 *  yyyy-MM-dd'T'HH:mm:ss.SSS-HH:MM
	 *
     * @return The date formatted as an xs:dateTime string (uses default TimeZone)
	 */
	private static String getFormattedDate(Date date)
	{
		// Retrieve the time zone offset
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(_defaultTimezone);
		cal.setTime(date);
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET)+cal.get(Calendar.DST_OFFSET);

		// Format the date
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		df.setTimeZone(_defaultTimezone);
		String xmlDate = df.format( date );
		if (zoneOffset==0)
			return xmlDate+"Z";

		// Format the time zone offset
		String tz = (zoneOffset<0) ? "-" : "+";
		zoneOffset = Math.abs(zoneOffset);
		short zhour = (short) (zoneOffset / (60*60*1000));
		zoneOffset = zoneOffset % (60*60*1000);
		short zmin = (short)(zoneOffset / (60*1000));
		tz+= (zhour<10 ? "0"+zhour : ""+zhour) + ":" + (zmin<10 ? "0"+zmin : ""+zmin);
		return xmlDate+tz;

	} //-- getFormattedDate

    /**
     * Sets the TimeZone used when marshalling out xsd:dateTime values using this handler
	 * Default is JVM default returned by TimeZone.getDefault()
     * @param timeZone TimeZone to use instead of JVM default
    **/
	public static void setDefaultTimeZone(TimeZone timeZone)
	{
		_defaultTimezone = timeZone;
	} //-- setDefaultTimeZone

} //-- DateFieldHandler



