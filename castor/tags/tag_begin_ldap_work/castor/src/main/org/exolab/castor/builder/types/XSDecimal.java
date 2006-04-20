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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author          Changes
 * 06/01/2001   Arnaud Blandin  Upgrade to XML Schema Recommendation
 * 10/31/200    Arnaud Blandin  support for min/max, scale&precision facets
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
public  class XSDecimal extends XSType
{

    /**
     * Facets for Decimal type
     */
    java.math.BigDecimal _maxInclusive = null;
    java.math.BigDecimal _maxExclusive = null;
    java.math.BigDecimal _minInclusive = null;
    java.math.BigDecimal _minExclusive = null;
    private int  _totalDigits = -1;
    private int _fractionDigits = -1;



    /**
     * The JType represented by this XSType
    **/
    private static final JType jType
        = new JClass("java.math.BigDecimal");

    private String value = null;

    public XSDecimal() {
        super(XSType.DECIMAL_TYPE);
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
     * Returns the maximum exclusive value that this XSInteger can hold.
     * @return the maximum exclusive value that this XSInteger can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
    **/
    public java.math.BigDecimal  getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSInteger can hold.
     * @return the maximum inclusive value that this XSInteger can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
    **/
    public java.math.BigDecimal  getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSInteger can hold.
     * @return the minimum exclusive value that this XSInteger can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive
     * @see #setMaxInclusive
    **/
    public java.math.BigDecimal  getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSInteger can hold.
     * @return the minimum inclusive value that this XSInteger can hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
    **/
    public java.math.BigDecimal  getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive
      /**
     * Returns the totalDigits facet value of this XSInteger.
     * @return the totalDigits facet value of this XSInteger.
     */
    public int getTotalDigits() {
        return _totalDigits;
    }

    /**
     * Returns the fractionDigits facet value of this XSInteger.
     * @return the fractionDigits facet value of this XSInteger.
     */
    public int getFractionDigits() {
        return _fractionDigits;
    }

    public boolean hasMaximum() {
        return ((_maxInclusive != null) || (_maxExclusive != null));
    } //-- hasMaximum

    public boolean hasMinimum() {
        return ((_minInclusive != null) || (_minExclusive != null));
    } //-- hasMinimum


    /**
     * Sets the maximum exclusive value that this XSDecimal can hold.
     * @param max the maximum exclusive value this XSDecimal can be
     * @see #setMaxInclusive
    **/
    public void setMaxExclusive(java.math.BigDecimal  max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive


    /**
     * Sets the maximum inclusive value that this XSDecimal can hold.
     * @param max the maximum inclusive value this XSDecimal can be
     * @see #setMaxExclusive
    **/
    public void setMaxInclusive(java.math.BigDecimal  max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSDecimal can hold.
     * @param max the minimum exclusive value this XSDecimal can be
     * @see #setMinInclusive
    **/
    public void setMinExclusive(java.math.BigDecimal min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive


    /**
     * Sets the minimum inclusive value that this XSDecimalcan hold.
     * @param max the minimum inclusive value this XSDecimal can be
     * @see #setMinExclusive
    **/
    public void setMinInclusive(java.math.BigDecimal  min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the totalDigits facet for this XSInteger.
     * @param totalDig the value of totalDigits (must be >0)
     */
     public void setTotalDigits(int totalDig) {
          if (totalDig <= 0)
              throw new IllegalArgumentException(this.getName()+": the totalDigits facet must be positive");
          else _totalDigits = totalDig;
     }

    /**
     * Sets the fractionDigits facet for this XSInteger.
     * @param fractionDig the value of fractionDigits (must be >=0)
     */
     public void setFractionDigits(int fractionDig) {
          if (fractionDig < 0)
              throw new IllegalArgumentException(this.getName()+": the fractionDigits facet must be positive");
          else _fractionDigits = fractionDig;
     }


    public void setFacets(SimpleType simpleType) {
     Enumeration enumeration = getFacets(simpleType);
            while (enumeration.hasMoreElements()) {

                Facet facet = (Facet)enumeration.nextElement();
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
                //--totalDigits
                else if (Facet.TOTALDIGITS.equals(name))
                    setTotalDigits(facet.toInt());
                //--fractionDigits
                else if (Facet.FRACTIONDIGITS.equals(name))
                   setFractionDigits(facet.toInt());
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
        String result = "new java.math.BigDecimal(0);";
        return result;
	}
	
   	/**
	 * Creates the validation code for an instance of this XSType. The validation
     * code should if necessary create a newly configured TypeValidator, that
     * should then be added to a FieldValidator instance whose name is provided.
	 * 
	 * @param fixedValue a fixed value to use if any
	 * @param jsc the JSourceCode to fill in.
     * @param fieldValidatorInstanceName the name of the FieldValidator
     * that the configured TypeValidator should be added to.
	 */
	public void validationCode (JSourceCode jsc, String fixedValue, String fieldValidatorInstanceName) {

        if (jsc == null)
            jsc = new JSourceCode();
        jsc.add("DecimalValidator typeValidator = new DecimalValidator();");
        if (hasMinimum()) {
            java.math. BigDecimal min = getMinExclusive();
            if (min != null)
                jsc.add("typeValidator.setMinExclusive(new java.math.BigDecimal(\"");
            else {
                min = getMinInclusive();
                jsc.add("typeValidator.setMinInclusive(new java.math.BigDecimal(\"");
            }
            jsc.append(min.toString()+"\")");
            jsc.append(");");
        }
        if (hasMaximum()) {
            java.math.BigDecimal max = getMaxExclusive();
            if (max != null)
                jsc.add("typeValidator.setMaxExclusive(new java.math.BigDecimal(\"");
            else {
                max = getMaxInclusive();
                jsc.add("typeValidator.setMaxInclusive(new java.math.BigDecimal(\"");
            }
            jsc.append(max.toString()+"\")");
            jsc.append(");");
        }

        //-- totalDigits
        int totalDigits = getTotalDigits();

        if (totalDigits != -1) {
            jsc.add("typeValidator.setTotalDigits(");
            jsc.append(Integer.toString(totalDigits));
            jsc.append(");");
        }

        //-- fractionDigits
        int fractionDigits = getFractionDigits();
        if (fractionDigits != -1) {
            jsc.add("typeValidator.setFractionDigits(");
            jsc.append(Integer.toString(fractionDigits));
            jsc.append(");");
        }

        //-- fixed values
        if (fixedValue != null) {

            jsc.add("typeValidator.setFixed(");
            jsc.append(fixedValue);
            jsc.append(");");
        }
        jsc.add(fieldValidatorInstanceName+".setValidator(typeValidator);");
		
    }
}
