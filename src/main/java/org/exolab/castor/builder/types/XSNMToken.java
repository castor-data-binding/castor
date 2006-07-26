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

import java.util.Enumeration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.*;

/**
 * The XML Schema String type
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public final class XSNMToken extends XSType {

    /**
     * The JType represented by this XSType
    **/
    private static final JType jType
        = new JClass("java.lang.String");

    /**
     * The length facet
     */
    private int _length = 0;

    /**
     * The max length facet
    **/
    private int maxLength = -1;

    /**
     * The min length facet
    **/
    private int minLength = 0;

    public XSNMToken() {
        super(XSType.NMTOKEN_TYPE);
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
        return "(String)"+variableName;
    } //-- fromJavaObject

    public void setFacets(SimpleType simpleType) {
        //-- extract valid facets
        Enumeration enumeration = getFacets(simpleType);
        while (enumeration.hasMoreElements()) {

            Facet facet = (Facet) enumeration.nextElement();
            String name = facet.getName();

            //-- maxLength
            if (Facet.MAX_LENGTH.equals(name))
                setMaxLength(facet.toInt());
            //-- minLength
            else if (Facet.MIN_LENGTH.equals(name))
                setMinLength(facet.toInt());
            //-- length
            else if (Facet.LENGTH.equals(name))
                setLength(facet.toInt());

        }
    }
    
    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return XSNMToken.jType;
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
	    jsc.add("org.exolab.castor.xml.validators.NameValidator typeValidator = new org.exolab.castor.xml.validators.NameValidator(org.exolab.castor.xml.validators.NameValidator.NMTOKEN);");
        
        if ((hasMinLength()) && (!hasLength())) {
            jsc.add("typeValidator.setMinLength(");
            jsc.append(Integer.toString(getMinLength()));
            jsc.append(");");
        }
        if ((hasMaxLength()) && (!hasLength())) {
            jsc.add("typeValidator.setMaxLength(");
            jsc.append(Integer.toString(getMaxLength()));
            jsc.append(");");
        }
        if (hasLength()) {
            jsc.add("typeValidator.setLength(");
            jsc.append(Integer.toString(getLength()));
            jsc.append(");");
        }
        
	    jsc.add(fieldValidatorInstanceName+".setValidator(typeValidator);");
	}

    /**
     * Returns true if a maximum length has been set
     * @return true if a maximum length has been set
    **/
    public boolean hasMaxLength() {
        return (maxLength >= 0);
    } //-- hasMaxLength

    /**
     * Returns true if a minimum length has been set
     * @return true if a minimum length has been set
    **/
    public boolean hasMinLength() {
        return (minLength > 0);
    } //-- hasMinLength

    /**
     * Returns true if a length has been set
     * @return true if a length has been set
     */
    public boolean hasLength() {
        return (_length > 0);
    }

    /**
     * Sets the length of this XSNMToken.
     * While setting the length, the maxLength and minLength are also
     * set up to this length
     * @param length the length to set
     * @see #setMaxLength
     * @see #setMinLength
     */
    public void setLength(int length) {
        this._length = length;
        setMaxLength(length);
        setMinLength(length);
    }

    /**
     * Sets the maximum length of this XSString. To remove the max length
     * facet, use a negative value.
     * @param maxLength the maximum length for occurances of this type
    **/
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    } //-- setMaxLength

    /**
     * Sets the minimum length of this XSString.
     * @param minLength the minimum length for occurances of this type
    **/
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    } //-- setMinLength

    /**
     * Returns the maximum length occurances of this type can be.
     * A negative value denotes no maximum length
     * @return the maximum length facet
    **/
    public int getMaxLength() {
        return maxLength;
    } //-- getMaxLength

    /**
     * Returns the minimum length occurances of this type can be.
     * @return the minimum length facet
    **/
    public int getMinLength() {
        return minLength;
    } //-- getMinLength

    /**
     * Returns the length that this type must have
     * @return the length that this type must have
     */
    public int getLength() {
        return this._length;
    }
    
    

} //-- XSNMToken
