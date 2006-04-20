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
 * $Id
 */
package org.exolab.castor.builder.types;

import org.exolab.castor.types.TimeDuration;
import org.exolab.castor.types.RecurringDuration;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JType;
import org.exolab.javasource.JClass;

import java.text.ParseException;
import java.util.Enumeration;


/**
 * The XML Schema recurring Duration type
 * This Class is used to represent a user DERIVED datatype from recurring Duration
 * It is a mistake to use it as if it was a PRIMITIVE datatype, i.e you always
 * have to set the duration and period facets.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 * @see org.exolab.castor.types.RecurringDuration
**/
public class XSRecurringDuration extends XSType {

    private TimeDuration _duration = null;
    private TimeDuration _period = null;
    /**
     * The JType represented by this XSType
     */
    private static final JType JTYPE
        = new JClass("org.exolab.castor.types.RecurringDuration");

    private RecurringDuration _maxInclusive;
    private RecurringDuration _maxExclusive;
    private RecurringDuration _minInclusive;
    private RecurringDuration _minExclusive;


    public XSRecurringDuration() {
        super(XSType.RECURRING_DURATION);

    }

    public XSRecurringDuration(short type,TimeDuration duration, TimeDuration period) {
        super(type);
        _duration=duration;
        _period=period;
    }


    public XSRecurringDuration(short type,String duration, String period)
        throws IllegalArgumentException
    {
        super(type);
        try {
            _duration = (TimeDuration.parse(duration));
            _period = (TimeDuration.parse(period));
        } catch (java.text.ParseException e) {
            System.out.println("Error in constructor of RecurringDuration: "+e);
            throw new IllegalArgumentException();
        }
    }//--XSRecurringDuration(String,String)

    /**
     * Returns the maximum exclusive value that this XSRecurringDuration can hold.
     * @return the maximum exclusive value that this XSRecurringDuration can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see getMaxInclusive
    **/
    public RecurringDuration getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSRecurringDuration can hold.
     * @return the maximum inclusive value that this XSRecurringDuration can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see getMaxExclusive
    **/
    public RecurringDuration getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSRecurringDuration can hold.
     * @return the minimum exclusive value that this XSRecurringDuration can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see getMinInclusive
     * @see setMaxInclusive
    **/
    public RecurringDuration getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSRecurringDuration can hold.
     * @return the minimum inclusive value that this can XSRecurringDuration hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see getMinExclusive
    **/
    public RecurringDuration getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

    /**
     * Sets the maximum exclusive value that this XSRecurringDuration can hold.
     * @param max the maximum exclusive value this XSRecurringDuration can be
     * @see setMaxInclusive
    **/
    public void setMaxExclusive(RecurringDuration max) {
        _maxExclusive = max;
        //if no period and duration, we take the ones from the current
        //XSRecurringDuration
        if (_maxExclusive.getDuration() == null)
            _maxExclusive.setDuration(this.getDuration());
        if (_maxExclusive.getPeriod() == null)
            _maxExclusive.setPeriod(this.getPeriod());
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSRecurringDuration can hold.
     * @param max the maximum inclusive value this XSRecurringDuration can be
     * @see setMaxExclusive
    **/
    public void setMaxInclusive(RecurringDuration max) {
        _maxInclusive = max;
       //if no period and duration, we take the ones from the current
        //XSRecurringDuration
        if (_maxExclusive.getDuration() == null)
            _maxInclusive.setDuration(this.getDuration());
        if (_maxExclusive.getPeriod() == null)
            _maxInclusive.setPeriod(this.getPeriod());
        _maxExclusive = null;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSRecurringDuration can hold.
     * @param max the minimum exclusive value this XSRecurringDuration can be
     * @see setMinInclusive
    **/
    public void setMinExclusive(RecurringDuration min) {
        _minExclusive = min;
       //if no period and duration, we take the ones from the current
        //XSRecurringDuration
        if (_minExclusive.getDuration() == null)
            _minExclusive.setDuration(this.getDuration());
        if (_minExclusive.getPeriod() == null)
            _minExclusive.setPeriod(this.getPeriod());
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSInt can hold.
     * @param max the minimum inclusive value this XSInt can be
     * @see setMinExclusive
    **/
    public void setMinInclusive(RecurringDuration min) {
        _minInclusive = min;
        //if no period and duration, we take the ones from the current
        //XSRecurringDuration
        if (_minInclusive.getDuration() == null)
            _minInclusive.setDuration(this.getDuration());
        if (_minInclusive.getPeriod() == null)
            _minInclusive.setPeriod(this.getPeriod());

        _minExclusive = null;
    } //-- setMinInclusive

    public boolean hasMinimum() {
        return ( (_minInclusive != null) || (_minExclusive != null) );
    }


    public boolean hasMaximum() {
       return ( (_maxInclusive != null) || (_maxExclusive != null) );
    }


   /**
    * returns the duration facet of this recurringDuration
    * @returns the duration facet of this recurringDuration
    */
    public TimeDuration getDuration() {
        return _duration;
    }

   /**
    * returns the period facet of this recurringDuration
    * @returns the period facet of this recurringDuration
    */
    public TimeDuration getPeriod() {
        return _period;
    }

    /**
     * set the duration facet for this recurringDuration
     * @param duration the period to set
     */

    public void setDuration (TimeDuration duration) {
        _duration = duration;
    }

   /**
    * set the period facet for this recurringDuration
    * @param period the period to set
    */
    public void setPeriod (TimeDuration period) {
        _period = period;
    }

    /**
     * Reads and sets the facets for this XSrecurringDuration
     * @param simpleType the SimpleType containing the facets
     */
    public void setFacets(SimpleType simpleType) {

       //-- copy valid facets
        Enumeration enum = getFacets(simpleType);
        //--duration and period must be treated separately
        try {
            Facet dur = simpleType.getFacet("duration");
            if (dur != null)
                setDuration(TimeDuration.parse(dur.getValue()));
            Facet per = simpleType.getFacet("period");
            if (per != null)
               setPeriod(TimeDuration.parse(per.getValue()));
         } catch(ParseException e) {
                System.out.println("Error in setting the period and duration facets of recurringDuration");
                e.printStackTrace();
                return;
         }

        while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();

            try {
                //-- maxExclusive
                if (Facet.MAX_EXCLUSIVE.equals(name))
                    this.setMaxExclusive(RecurringDuration.parseRecurring(facet.getValue()));
                //-- maxInclusive
                else if (Facet.MAX_INCLUSIVE.equals(name))
                    this.setMaxInclusive(RecurringDuration.parseRecurring(facet.getValue()));
                //-- minExclusive
                else if (Facet.MIN_EXCLUSIVE.equals(name))
                    this.setMinExclusive(RecurringDuration.parseRecurring(facet.getValue()));
                //-- minInclusive
                else if (Facet.MIN_INCLUSIVE.equals(name))
                    this.setMinInclusive(RecurringDuration.parseRecurring(facet.getValue()));
            } catch(ParseException e) {
                System.out.println("Error in setting the facets of recurringDuration");
                e.printStackTrace();
                return;
            }
        }

    } //setFacets

    public JType getJType() {
       return this.JTYPE;
    }

    /**
	 * Returns the Java code neccessary to create a new instance of the
	 * JType associated with this XSType
	 */
	public String newInstanceCode() {
        String result="new "+getJType().getName()+"(";
       //duration and period should never be null
        result = result +"\""+ _duration.toString()+"\", ";
        result = result + "\""+_period.toString()+"\"";
        result = result +");";
        return result;
    }
} //XSRecurringDuration