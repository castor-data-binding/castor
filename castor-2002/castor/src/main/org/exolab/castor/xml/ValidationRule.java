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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

/**
 * A class for defining simple rules used for validating a content model
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class ValidationRule {
    
    /**
     * The attribute type
    **/
    public static final short ATTRIBUTE = 0;
    
    /**
     * The element type
    **/
    public static final short ELEMENT   = 1;
    
    /**
     * A collection or group 
    **/
    public static final short GROUP     = 2;
    
    /**
     * The text (#PCDATA) type
    **/
    public static final short TEXT      = 3;
    
        
    /**
     * The name of the node that this validation rule applies to
    **/
    private String name = null;
    
    /**
     * The minimum occurance that the node must appear
    **/
    private int minOccurs = 0;
    
    /**
     * The maximum occurance that the node must appear
    **/
    private int maxOccurs = -1;
    
    /**
     * The TypeValidator used for Attribute rules, or Element
     * rules that contain Text content
    **/
    private TypeValidator typeValidator = null;
    
    /**
     * Creates a default ValidationRule
    **/
    public ValidationRule() {
        super();
    } //-- ValidationRule
    
    /**
     * Creates a ValidationRule for elements of the given name
     * @param name the name of the element that this ValidationRule
     * validates
    **/
    public ValidationRule(String name) {
        super();
        this.name = name;
    } //-- ValidationRule(String)
    
    /**
     * Returns the maximum number of occurances that objects described by
     * this descriptor may appear
     * @return the maximum number of occurances that objects described by
     * this descriptor may appear, -1 will be returned if no maximum has
     * been set
    **/
    public int getMaxOccurs() {
        return maxOccurs;
    } //-- getMaxOccurs

    /**
     * Returns the minimum number of occurances that objects described by
     * this descriptor may appear
     * @return the minimum number of occurances that objects described by
     * this descriptor may appear, 0 will be returned by default
    **/
    public int getMinOccurs() {
        return minOccurs;
    } //-- getMaxOccurs
    
    
    /**
     * Return the name of the node to which this ValidationRule applies
     * @return the name of the node to which this ValidationRule applies
    **/
    public String getName() {
        return name;
    } //-- getName
    
    /**
     * Returns the type of this ValidationRule
     * @return the type of this ValidationRule 
    **/
    public abstract short getType();
    
    public TypeValidator getTypeValidator() {
        return typeValidator;
    } //-- getTypeValidator
    
    /**
     * Sets the maximum occurance that the described field may occur
     * @param maxOccurs the maximum occurance that the descibed field 
     * may occur.
    **/
    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    } //-- setMaxOccurs

    /**
     * Sets the minimum occurance that the described field may occur
     * @param minOccurs the minimum occurance that the descibed field 
     * may occur.
    **/
    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    } //-- setMinOccurs
    
    /**
     * Sets the TypeValidator for this ValidationRule. TypeValidators will
     * only apply to Attribute rules, or Element rules with text content
    **/
    public void setTypeValidator(TypeValidator typeValidator) {
        this.typeValidator = typeValidator;
    } //-- setTypeValidator
    
} //-- ContentValidator
