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
 * Copyright 2000-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author          Changes
 * 12/06/2000   Arnaud Blandin  Created
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.builder.SourceGenerator;

import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Facet;

import org.exolab.javasource.*;

import java.util.Enumeration;

/**
 * The XML Schema Float type
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class XSFloat extends XSPatternBase {

    //- Constraints for integer type
    Float maxInclusive = null;
    Float maxExclusive = null;
    Float minInclusive = null;
    Float minExclusive = null;

    /**
     * The JType represented by this XSType
    **/
    private static JType jType = JType.Float;
    private boolean _asWrapper = false;

    public XSFloat() {
        this(SourceGenerator.usePrimitiveWrapper());
    }

    public XSFloat(boolean asWrapper) {
        super(XSType.FLOAT_TYPE);
        _asWrapper = asWrapper;
        if (_asWrapper)
            this.jType = new JClass("java.lang.Float");
        else this.jType = JType.Float;
    } //-- XSFloat


    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jType;
    }

    /**
     * Returns the maximum exclusive value that this XSFloat can hold.
     * @return the maximum exclusive value that this XSFloat can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
    **/
    public Float getMaxExclusive() {
        return maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSFloat can hold.
     * @return the maximum inclusive value that this XSFloat can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
    **/
    public Float getMaxInclusive() {
        return maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSFloat can hold.
     * @return the minimum exclusive value that this XSFloat can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive
     * @see #setMaxInclusive
    **/
    public Float getMinExclusive() {
        return minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSFloat can hold.
     * @return the minimum inclusive value that this XSFloat can hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
    **/
    public Float getMinInclusive() {
        return minInclusive;
    } //-- getMinInclusive

    public boolean hasMaximum() {
        return ((maxInclusive != null) || (maxExclusive != null));
    } //-- hasMaximum

    public boolean hasMinimum() {
        return ((minInclusive != null) || (minExclusive != null));
    } //-- hasMinimum



    /**
     * Sets the maximum exclusive value that this XSFloat can hold.
     * @param max the maximum exclusive value this XSFloat can be
     * @see #setMaxInclusive
    **/
    public void setMaxExclusive(float max) {
        maxExclusive = new Float(max);
    } //-- setMaxExclusive

    /**
     * Sets the maximum exclusive value that this XSFloat can hold.
     * @param max the maximum exclusive value this XSFloat can be
     * @see #setMaxInclusive
    **/
    public void setMaxExclusive(Float max) {
        maxExclusive = max;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSFloat can hold.
     * @param max the maximum inclusive value this XSFloat can be
     * @see #setMaxExclusive
    **/
    public void setMaxInclusive(float max) {
        maxInclusive = new Float(max);
    } //-- setMaxInclusive

    /**
     * Sets the maximum inclusive value that this XSFloat can hold.
     * @param max the maximum inclusive value this XSFloat can be
     * @see #setMaxExclusive
    **/
    public void setMaxInclusive(Float max) {
        maxInclusive = max;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSFloat can hold.
     * @param max the minimum exclusive value this XSFloat can be
     * @see #setMinInclusive
    **/
    public void setMinExclusive(float min) {
        minExclusive = new Float(min);
    } //-- setMinExclusive

    /**
     * Sets the minimum exclusive value that this XSFloat can hold.
     * @param max the minimum exclusive value this XSFloat can be
     * @see #setMinInclusive
    **/
    public void setMinExclusive(Float min) {
        minExclusive = min;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSFloat can hold.
     * @param max the minimum inclusive value this XSFloat can be
     * @see #setMinExclusive
    **/
    public void setMinInclusive(float min) {
        minInclusive = new Float(min);
    } //-- setMinInclusive

    /**
     * Sets the minimum inclusive value that this XSFloat can hold.
     * @param max the minimum inclusive value this XSFloat can be
     * @see #setMinExclusive
    **/
    public void setMinInclusive(Float min) {
        minInclusive = min;
    } //-- setMinInclusive

    /**
     * Reads and sets the facets for XSFloat
     * @param simpleType the SimpleType containing the facets
     */
    public void setFacets(SimpleType simpleType) {

        //-- copy valid facets
        Enumeration enum = getFacets(simpleType);
        while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();

            //-- maxExclusive
            if (Facet.MAX_EXCLUSIVE.equals(name))
                setMaxExclusive(facet.toFloat());
            //-- maxInclusive
            else if (Facet.MAX_INCLUSIVE.equals(name))
                setMaxInclusive(facet.toFloat());
            //-- minExclusive
            else if (Facet.MIN_EXCLUSIVE.equals(name))
                setMinExclusive(facet.toFloat());
            //-- minInclusive
            else if (Facet.MIN_INCLUSIVE.equals(name))
                setMinInclusive(facet.toFloat());
            //-- pattern
            else if (Facet.PATTERN.equals(name))
                setPattern(facet.getValue());
        }
    }

    /**
     * Returns the String necessary to convert an instance of this XSType
     * to an Object. This method is really only useful for primitive types
     * @param variableName the name of the instance variable
     * @return the String necessary to convert an instance of this XSType
     * to an Object
    **/
    public String createToJavaObjectCode(String variableName) {
        if (SourceGenerator.usePrimitiveWrapper())
            return super.createToJavaObjectCode(variableName);
        else {
             StringBuffer sb = new StringBuffer("new Float(");
             sb.append(variableName);
             sb.append(")");
             return sb.toString();
        }
    } //-- toJavaObject

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
    **/
    public String createFromJavaObjectCode(String variableName) {
        StringBuffer sb = new StringBuffer("((Float)");
        sb.append(variableName);
        sb.append(")");
        if (!_asWrapper) {
           sb.append(".floatValue()");
        }
        return sb.toString();
    } //-- fromJavaObject
    
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
        jsc.add("FloatValidator typeValidator = new FloatValidator();");
        if (hasMinimum()) {
            Float min = getMinExclusive();
            if (min != null)
                 jsc.add("typeValidator.setMinExclusive(");
            else {
                 min = getMinInclusive();
                 jsc.add("typeValidator.setMinInclusive(");
            }
            if ( (min.equals(new Float(Float.NEGATIVE_INFINITY))) )
                jsc.append("Float.NEGATIVE_INFINITY");
            else if ( (min.equals(new Float(Float.POSITIVE_INFINITY))) )
                jsc.append("Float.POSITIVE_INFINITY");
            else jsc.append(min.toString()+"f");
                jsc.append(");");
        }
        if (hasMaximum()) {
            Float max = getMaxExclusive();
            if (max != null)
                jsc.add("typeValidator.setMaxExclusive(");
            else {
                max = getMaxInclusive();
                jsc.add("typeValidator.setMaxInclusive(");
            }
            if ( (max.equals(new Float(Float.NEGATIVE_INFINITY))) )
                jsc.append("Float.NEGATIVE_INFINITY");
            else if ( (max.equals(new Float(Float.POSITIVE_INFINITY))) )
                jsc.append("Float.POSITIVE_INFINITY");
            else jsc.append(max.toString()+"f");
                jsc.append(");");
        }

        //-- fixed values
        if (fixedValue != null) {

            //-- make sure we've got a good value
            Float test = new Float(fixedValue);
            jsc.add("typeValidator.setFixed(");
            jsc.append(fixedValue);
            jsc.append("f);");
        }
        //-- pattern facet
        String pattern = getPattern();
        if (pattern != null) {
            jsc.add("typeValidator.setPattern(\"");
            jsc.append(escapePattern(pattern));
            jsc.append("\");");
        }
        jsc.add(fieldValidatorInstanceName+".setValidator(typeValidator);");
    }
   

} //-- XStype