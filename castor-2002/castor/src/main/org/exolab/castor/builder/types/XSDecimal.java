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
 * Date         Author          Changes
 * 10/31/00     Arnaud Blandin  support for min/max, scale&precision facets
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.*;

import java.util.Enumeration;

/**
 * The decimal XML Schema datatype
 * TODO : handle pattern, enumeration
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a>
**/
public final class XSDecimal extends XSType
{

    /**
     * Facets for Decimal type
     */
    Integer _scale = null;
    Integer _precision = null;
    java.math.BigDecimal _maxInclusive = null;
    java.math.BigDecimal _maxExclusive = null;
    java.math.BigDecimal _minInclusive = null;
    java.math.BigDecimal _minExclusive = null;


    /**
     * The JType represented by this XSType
    **/
    private static final JType jType
        = new JClass("java.math.BigDecimal");

    private String value = null;

    public XSDecimal() {
        super(XSType.DECIMAL);
    } //-- XSNMToken

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
    **/
    public String createFromJavaObjectCode(String variableName) {
        return "(java.math.BigDecimal)"+variableName;
    } //-- fromJavaObject

    /**
     * Returns the precision value of this XSDecimal
     * @return the precision value of this XSDecimal.
    **/

    public Integer getPrecision() {
        return _precision;
    }


    /**
     * Returns the scale value of this XSDecimal
     * @return the scale value of this XSDecimal.
    **/
    public Integer getScale() {
        return _scale;
    }

    /**
     * Returns the maximum exclusive value that this XSInteger can hold.
     * @return the maximum exclusive value that this XSInteger can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see getMaxInclusive
    **/
    public java.math.BigDecimal  getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSInteger can hold.
     * @return the maximum inclusive value that this XSInteger can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see getMaxExclusive
    **/
    public java.math.BigDecimal  getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSInteger can hold.
     * @return the minimum exclusive value that this XSInteger can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see getMinInclusive
     * @see setMaxInclusive
    **/
    public java.math.BigDecimal  getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSInteger can hold.
     * @return the minimum inclusive value that this XSInteger can hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see getMinExclusive
    **/
    public java.math.BigDecimal  getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

    public boolean hasMaximum() {
        return ((_maxInclusive != null) || (_maxExclusive != null));
    } //-- hasMaximum

    public boolean hasMinimum() {
        return ((_minInclusive != null) || (_minExclusive != null));
    } //-- hasMinimum


    /**
     * Sets the maximum exclusive value that this XSDecimal can hold.
     * @param max the maximum exclusive value this XSDecimal can be
     * @see setMaxInclusive
    **/
    public void setMaxExclusive(java.math.BigDecimal  max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive


    /**
     * Sets the maximum inclusive value that this XSDecimal can hold.
     * @param max the maximum inclusive value this XSDecimal can be
     * @see setMaxExclusive
    **/
    public void setMaxInclusive(java.math.BigDecimal  max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSDecimal can hold.
     * @param max the minimum exclusive value this XSDecimal can be
     * @see setMinInclusive
    **/
    public void setMinExclusive(java.math.BigDecimal min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive


    /**
     * Sets the minimum inclusive value that this XSDecimalcan hold.
     * @param max the minimum inclusive value this XSDecimal can be
     * @see setMinExclusive
    **/
    public void setMinInclusive(java.math.BigDecimal  min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive


    public void setPrecision(int p) throws ValidationException {
        String err = "";
        if ( p<0 || p==0 ) {
            err = "decimal precision must be a positiveInteger";
            throw new ValidationException(err);
        }

        _precision = new Integer(p);
    }

    public void setScale(int s) throws ValidationException {
        String err = "";
        if (s<0) {
            err = "decimal scale must be a nonNegativeInteger";
            throw new ValidationException(err);
        }

        if ( (_precision!=null) && (s > _precision.intValue()) ) {
            err = "decimal scale must be lower than precision";
            throw new ValidationException(err);
        }

        _scale = new Integer(s);
    }

    public void setFacets(SimpleType simpleType) {
     Enumeration enum = getFacets(simpleType);
        try {
            while (enum.hasMoreElements()) {

                Facet facet = (Facet)enum.nextElement();
                String name = facet.getName();

                //-- maxExclusive
                if (Facet.MAX_EXCLUSIVE.equals(name))
                    setMaxExclusive(new java.math.BigDecimal(facet.getValue()));
                //-- maxInclusive
                else if (Facet.MAX_INCLUSIVE.equals(name))
                    setMaxInclusive(new java.math.BigDecimal(facet.getValue()));
                //-- minExclusive
                else if (Facet.MIN_EXCLUSIVE.equals(name))
                    setMinExclusive(new java.math.BigDecimal(facet.getValue()));
                //-- minInclusive
                else if (Facet.MIN_INCLUSIVE.equals(name))
                    setMinInclusive(new java.math.BigDecimal(facet.getValue()));
                //-- precision
                else if (Facet.PRECISION.equals(name))
                    setPrecision(facet.toInt());
                //-- scale
                else if (Facet.SCALE.equals(name)) {
                     setScale(facet.toInt());
                }
            }
        } catch (ValidationException e) {
            System.out.println("Error in setting up the Facets");
            // perhaps could be better to throw a new exception?
            System.out.println(e);
            return;
        }

    } //-- setFacets
    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jType;
    }

	/**
	 * Returns the Java code neccessary to create a new instance of the
	 * JType associated with this XSType
	 */
	public String newInstanceCode()
	{
        String result = "new java.math.BigDecimal(0)";
        //can't create a BigDecimal that follows the precision facet
        result = (_scale!=null) ? result+".setScale("+_scale.intValue()+");"
                                  : result+";";
        return result;
	}
}
