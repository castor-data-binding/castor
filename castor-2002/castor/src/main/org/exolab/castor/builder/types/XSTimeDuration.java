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
 * $Id$
 * Date         Author              Changes
 * 10/27/2000   Arnaud Blandin      Added the support for min/max facets
 * 10/9/2000    Arnaud Blandin      Created
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.types.TimeDuration;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.types.TimeDuration;

import org.exolab.javasource.JType;
import org.exolab.javasource.JClass;

import java.text.ParseException;
import java.util.Enumeration;

public final class XSTimeDuration extends XSType {

    private static final JType JTYPE =
                    new JClass("org.exolab.castor.types.TimeDuration");

    private TimeDuration _maxInclusive;
    private TimeDuration _maxExclusive;
    private TimeDuration _minInclusive;
    private TimeDuration _minExclusive;

    public XSTimeDuration() {
        super(XSType.TIME_DURATION);
    }


    public JType getJType() {
        return this.JTYPE;
    }
     /**
     * Returns the maximum exclusive value that this XSTimeDuration can hold.
     * @return the maximum exclusive value that this XSTimeDuration can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see getMaxInclusive
    **/
    public TimeDuration getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSTimeDuration can hold.
     * @return the maximum inclusive value that this XSTimeDuration can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see getMaxExclusive
    **/
    public TimeDuration getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSTimeDuration can hold.
     * @return the minimum exclusive value that this XSTimeDuration can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see getMinInclusive
     * @see setMaxInclusive
    **/
    public TimeDuration getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSTimeDuration can hold.
     * @return the minimum inclusive value that this can XSTimeDuration hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see getMinExclusive
    **/
    public TimeDuration getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

    /**
     * Sets the maximum exclusive value that this XSTimeDuration can hold.
     * @param max the maximum exclusive value this XSTimeDuration can be
     * @see setMaxInclusive
    **/
    public void setMaxExclusive(TimeDuration max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSTimeDuration can hold.
     * @param max the maximum inclusive value this XSTimeDuration can be
     * @see setMaxExclusive
    **/
    public void setMaxInclusive(TimeDuration max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSTimeDuration can hold.
     * @param max the minimum exclusive value this XSTimeDuration can be
     * @see setMinInclusive
    **/
    public void setMinExclusive(TimeDuration min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSInt can hold.
     * @param max the minimum inclusive value this XSInt can be
     * @see setMinExclusive
    **/
    public void setMinInclusive(TimeDuration min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    public boolean hasMinimum() {
        return ( (_minInclusive != null) || (_minExclusive != null) );
    }


    public boolean hasMaximum() {
       return ( (_maxInclusive != null) || (_maxExclusive != null) );
    }

    /**
     * Reads and sets the facets for XSTimeDuration
     * override the readFacet method of XSType
     * @param simpletype the Simpletype containing the facets
     * @param xsType the XSType to set the facets of
     * @see org.exolab.castor.builder.xstype#readFacets
     */

    public void setFacets(SimpleType simpleType)
    {
        //-- copy valid facets
        Enumeration enum = getFacets(simpleType);
        while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();

            try {
                //-- maxExclusive
                if (Facet.MAX_EXCLUSIVE.equals(name))
                    this.setMaxExclusive(TimeDuration.parse(facet.getValue()));
                //-- maxInclusive
                else if (Facet.MAX_INCLUSIVE.equals(name))
                    this.setMaxInclusive(TimeDuration.parse(facet.getValue()));
                //-- minExclusive
                else if (Facet.MIN_EXCLUSIVE.equals(name))
                    this.setMinExclusive(TimeDuration.parse(facet.getValue()));
                //-- minInclusive
                else if (Facet.MIN_INCLUSIVE.equals(name))
                    this.setMinInclusive(TimeDuration.parse(facet.getValue()));
                //-- pattern
                else if (Facet.PATTERN.equals(name)) {
                    //do nothing for the moment
                }
            } catch (ParseException e) {
                // trow a new exception
                System.out.println(e);
                return;
            }
        }//while

    }//readTimeFacets



} //--XSTimeDuration
