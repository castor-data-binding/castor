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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.xml.schema.SimpleType;

import org.exolab.javasource.*;

/**
 * The boolean XML Schema datatype.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class XSBoolean extends XSType {

    /**
     * The JType represented by this XSType
    **/
    private static JType jType = JType.Boolean;
    private boolean _asWrapper = false;

    public XSBoolean() {
        this(false);
    }

    public XSBoolean(boolean asWrapper) {
        super(XSType.BOOLEAN_TYPE);
        _asWrapper = asWrapper;
        if (_asWrapper)
            jType = new JClass("java.lang.Boolean");
        else jType = JType.Boolean;
    } //-- XSBoolean


    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return jType;
    } //-- getJType

    public void setFacets(SimpleType simpleType) {}

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
            StringBuffer sb = new StringBuffer("(");
            sb.append(variableName);
            sb.append(" ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE)");
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
        StringBuffer sb = new StringBuffer("((java.lang.Boolean)");
        sb.append(variableName);
        sb.append(")");
        if (!_asWrapper) {
            sb.append(".booleanValue()");
        }
        return sb.toString();
    } //-- fromJavaObject
    
    /**
     * Returns a JSourceCode that contains the validation method for this XSBoolean.
     * 
     * @param fixedValue a fixed value to use if any
     * @param jsc the JSourceCode to fill in.
     */
    public void validationCode (JSourceCode jsc, String fixedValue, String fieldValidatorInstanceName) {

        if (jsc == null) 
            jsc = new JSourceCode();
        jsc.add("BooleanValidator typeValidator = new BooleanValidator();");
        if (fixedValue != null) {
            Boolean.valueOf(fixedValue);
            jsc.add("typeValidator.setFixed(");
            jsc.append(fixedValue);
            jsc.append(");");
        }
        
        jsc.add("fieldValidator.setValidator(typeValidator);");
    }
} //-- XSBoolean
