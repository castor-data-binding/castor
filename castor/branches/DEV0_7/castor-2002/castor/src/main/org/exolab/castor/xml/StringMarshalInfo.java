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


  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.lang.reflect.Method;

/**
 * The default StringMarshalInfo class
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class StringMarshalInfo 
    implements MarshalInfo 
{


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The set of element descriptors
    **/
    private static final MarshalDescriptor[] elements =
        new MarshalDescriptor[0];

    /**
     * The set of attribute descriptors
    **/
    private static final MarshalDescriptor[] attributes =
        new MarshalDescriptor[0];

    /**
     * The content descriptor
    **/
    private static final MarshalDescriptor contentDesc = null;

    /**
     * the Validator to use for validating Strings described
     * by this MarshalInfo
    **/
    private StringValidator _validator = null;
    
    private ValidationRule[] rules = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    public StringMarshalInfo() {
        super();
        rules = new ValidationRule[1];
        rules[0] = new BasicValidationRule();
    } //-- StringMarshalInfo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the set of attribute MarshalDescriptors
     * @return an array of MarshalDescriptors for all members that
     * should be marshalled as Attributes
    **/
    public MarshalDescriptor[] getAttributeDescriptors() {
        return attributes;
    } //-- getAttributeDescriptors() 

    /**
     * Returns the Class that this MarshalInfo describes
     * @return the Class that this MarshalInfo describes
    **/
    public Class getClassType() {
        return java.lang.String.class;
    } //-- getClassType() 

    /**
     * Returns the set of element MarshalDescriptors
     * @return an array of MarshalDescriptors for all members that
     * should be marshalled as Elements
    **/
    public MarshalDescriptor[] getElementDescriptors() {
        return elements;
    } //-- getElementDescriptors() 


    /**
     * Returns the descriptor for dealing with Text content
     * @return the MarshalDescriptor for dealing with Text content
    **/
    public MarshalDescriptor getContentDescriptor() {
        return contentDesc;
    } //-- getContentDescriptor() 

    /**
     * Returns the ValidationRules used for validating the instances
     * of the class associated with this MarshalInfo
     * @return the ValidationRules used for validating the instances
     * of the class associated with this MarshalInfo
    **/
    public ValidationRule[] getValidationRules() {
        return rules;
    } //-- getValidationRule
    
    /**
     * @return the namespace prefix to use when marshalling as XML.
    **/
    public String getNameSpacePrefix() {
        return null;
    } //-- getNameSpacePrefix
    
    /**
     * @return the namespace URI used when marshalling and unmarshalling as XML.
    **/
    public String getNameSpaceURI() {
        return null;
    } //-- getNameSpaceURI
    
    
    public void setValidator(StringValidator validator) {
        this._validator = validator;
        rules[0].setTypeValidator(_validator);
    } //-- setValidator
    
} //-- StringMarshalInfo
