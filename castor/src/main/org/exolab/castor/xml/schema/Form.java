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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

/**
 * Represents an enumerated type which consists of two values:
 * "qualified" and "unqualified". This is used for the "form"
 * property on attribute and element defintions as well as
 * the attributeFormDefault and elementFormDefault proprties on 
 * the Schema itself.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Form {
    
    /**
     * The String value for the qualified Form
    **/
    public static final String QUALIFIED_VALUE   = "qualified";
    
    /**
     * The String value for the unqualified Form
    **/
    public static final String UNQUALIFIED_VALUE = "unqualified";
    
    
    /**
     * The Qualified Form Object
    **/
    public static final Form Qualified   = new Form(QUALIFIED_VALUE);

    /**
     * The Qualified Form Object
    **/
    public static final Form Unqualified = new Form(UNQUALIFIED_VALUE);
    
    /**
     * The value of this Form
    **/
    private String _value = UNQUALIFIED_VALUE;
    
    /**
     * Creates a new Form
     *
     * @param value the value of the Form
    **/
    private Form(String value) {
        _value = value;
    } //-- Form
    
    /**
     * Returns the String value of this Form.
     *
     * @return the String value of this Form.
    **/
    public String getValue() {
        return _value;
    } //-- getValue
    
    /**
     * Returns true if this Form is the qualified Form.
     *
     * @return true if this Form is the qualified Form.
    **/
    public boolean isQualified() {
        return (this == Qualified);
    } //-- isQualified

    /**
     * Returns true if this Form is the unqualified Form.
     *
     * @return true if this Form is the unqualified Form.
    **/
    public boolean isUnqualified() {
        return (this == Unqualified);
    } //-- isUnqualified
    
    /**
     * Returns the String value of this Form.
     *
     * @return the String value of this Form.
    **/
    public String toString() {
        return _value;
    } //-- toString
    
    /**
     * Returns the Form corresponding to the given value.
     *
     * @param formValue the value of the Form to return.
     * @return the Form corresponding to the given value.
     * @exception IllegalArgumentException when the given value
     * is not valid.
    **/
    public static Form valueOf(String formValue) {
        if (QUALIFIED_VALUE.equals(formValue)) {
            return Form.Qualified;
        }
        else if (UNQUALIFIED_VALUE.equals(formValue)) {
            return Form.Unqualified;
        }
        else {
            String err = formValue + " is not a valid Form value.";
            throw new IllegalArgumentException(err);
        }
    } //-- valueOf
    
} //-- Form
