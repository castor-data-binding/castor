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
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;

import org.exolab.castor.builder.SourceGenerator;

import org.exolab.javasource.*;

import java.util.Enumeration;
/**
 * The XML Schema Integer type
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class XSInt extends XSPatternBase {

    //- Constraints for Integer type
    Integer maxInclusive = null;
    Integer maxExclusive = null;
    Integer minInclusive = null;
    Integer minExclusive = null;

    private boolean _asWrapper = false;

    /**
     * The JType represented by this XSType
    **/
    private static JType jType = JType.Int;

    //private Integer value = null;
    public XSInt() {
        this(false);
    }
    public XSInt(boolean asWrapper) {
        super(XSType.INT_TYPE);
        _asWrapper = asWrapper;
        if (_asWrapper)
            this.jType = new JClass("java.lang.Integer");
        else this.jType = JType.Int;
    } //-- XSInt


    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jType;
    }

    /**
     * Returns the maximum exclusive value that this XSInt can hold.
     * @return the maximum exclusive value that this XSInt can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
    **/
    public Integer getMaxExclusive() {
        return maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSInt can hold.
     * @return the maximum inclusive value that this XSInt can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
    **/
    public Integer getMaxInclusive() {
        return maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSInt can hold.
     * @return the minimum exclusive value that this XSInt can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive
     * @see #setMaxInclusive
    **/
    public Integer getMinExclusive() {
        return minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSInt can hold.
     * @return the minimum inclusive value that this XSInt can hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
    **/
    public Integer getMinInclusive() {
        return minInclusive;
    } //-- getMinInclusive

    public boolean hasMaximum() {
        return ((maxInclusive != null) || (maxExclusive != null));
    } //-- hasMaximum

    public boolean hasMinimum() {
        return ((minInclusive != null) || (minExclusive != null));
    } //-- hasMinimum


    //public String toString() {
    //    return value.toString();
    //}


    /**
     * Sets the maximum exclusive value that this XSInt can hold.
     * @param max the maximum exclusive value this XSInt can be
     * @see #setMaxInclusive
    **/
    public void setMaxExclusive(int max) {
        maxExclusive = new Integer(max);
        maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum exclusive value that this XSInt can hold.
     * @param max the maximum exclusive value this XSInt can be
     * @see #setMaxInclusive
    **/
    public void setMaxExclusive(Integer max) {
        maxExclusive = max;
        maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSInt can hold.
     * @param max the maximum inclusive value this XSInt can be
     * @see #setMaxExclusive
    **/
    public void setMaxInclusive(int max) {
        maxInclusive = new Integer(max);
        maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the maximum inclusive value that this XSInt can hold.
     * @param max the maximum inclusive value this XSInt can be
     * @see #setMaxExclusive
    **/
    public void setMaxInclusive(Integer max) {
        maxInclusive = max;
        maxExclusive = null;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSInt can hold.
     * @param max the minimum exclusive value this XSInt can be
     * @see #setMinInclusive
    **/
    public void setMinExclusive(int min) {
        minExclusive = new Integer(min);
        minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum exclusive value that this XSInt can hold.
     * @param max the minimum exclusive value this XSInt can be
     * @see #setMinInclusive
    **/
    public void setMinExclusive(Integer min) {
        minExclusive = min;
        minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSInt can hold.
     * @param max the minimum inclusive value this XSInt can be
     * @see #setMinExclusive
    **/
    public void setMinInclusive(int min) {
        minInclusive = new Integer(min);
        minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the minimum inclusive value that this XSInt can hold.
     * @param max the minimum inclusive value this XSInt can be
     * @see #setMinExclusive
    **/
    public void setMinInclusive(Integer min) {
        minInclusive = min;
        minExclusive = null;
    } //-- setMinInclusive


    /**
     * Reads and sets the facets for XSTimeDuration
     * override the readFacet method of XSType
     * @param simpletype the Simpletype containing the facets
     * @param xsType the XSType to set the facets of
     * @see org.exolab.castor.builder.types.XSType#getFacets
     */
    public void setFacets(SimpleType simpleType){

        //-- copy valid facets
        Enumeration enum = getFacets(simpleType);
        while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();

            //-- maxExclusive
            if (Facet.MAX_EXCLUSIVE.equals(name))
                setMaxExclusive(facet.toInt());
            //-- maxInclusive
            else if (Facet.MAX_INCLUSIVE.equals(name))
                setMaxInclusive(facet.toInt());
            //-- minExclusive
            else if (Facet.MIN_EXCLUSIVE.equals(name))
                setMinExclusive(facet.toInt());
            //-- minInclusive
            else if (Facet.MIN_INCLUSIVE.equals(name))
                setMinInclusive(facet.toInt());
            else if (Facet.PATTERN.equals(name)) {
                setPattern(facet.getValue());
            }

        }

    } //-- toXSInt


    /**
     * Returns the String necessary to convert an instance of this XSType
     * to an Object. This method is really only useful for primitive types
     * @param variableName the name of the instance variable
     * @return the String necessary to convert an instance of this XSType
     * to an Object
    **/
    public String createToJavaObjectCode(String variableName) {
       if (_asWrapper)
            return super.createToJavaObjectCode(variableName);
        else {
            StringBuffer sb = new StringBuffer("new java.lang.Integer(");
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
       StringBuffer sb = new StringBuffer("((java.lang.Integer)");
       sb.append(variableName);
       sb.append(")");
       if (!_asWrapper)
           sb.append(".intValue()");
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
       
        jsc.add("IntegerValidator typeValidator= new IntegerValidator();");
        if (hasMinimum()) {
            Integer min = getMinExclusive();
            if (min != null)
                jsc.add("typeValidator.setMinExclusive(");
            else {
                min = getMinInclusive();
                jsc.add("typeValidator.setMinInclusive(");
            }
            jsc.append(min.toString());
            jsc.append(");");
        }
        if (hasMaximum()) {
            Integer max = getMaxExclusive();
            if (max != null)
                jsc.add("typeValidator.setMaxExclusive(");
            else {
                max = getMaxInclusive();
                jsc.add("typeValidator.setMaxInclusive(");
            }
            jsc.append(max.toString());
            jsc.append(");");
        }

        //-- fixed values
        if (fixedValue != null) {
            //-- make sure we have a valid value...
            //-- Only if we are not using Object
            if (this.jType == JType.Int)
                Integer.parseInt(fixedValue);

            jsc.add("typeValidator.setFixed(");
            jsc.append(fixedValue);
            jsc.append(");");
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
     

} //-- XSInt
