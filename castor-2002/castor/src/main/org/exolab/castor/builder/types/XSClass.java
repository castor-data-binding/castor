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

package org.exolab.castor.builder.types;

import org.exolab.javasource.*;

/**
 * The XML Schema user-defined archetype
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XSClass extends XSType {
    
    private JClass  jClass     = null;
    private String  name       = null;
    private boolean enumerated = false;
    
    /**
     * Creates a new XSClass with the given JClass reference
     * @param jClass the JClass type of this XSClass
    **/
    public XSClass(JClass jClass) {
        this(jClass, null);
    } //-- XSClass

    /**
     * Creates a new XSClass with the given JClass reference
     * @param jClass the JClass associated with this XSType
     * @param schemaTypeName The XML Schema type name
    **/
    public XSClass(JClass jClass, String schemaTypeName) {
        super(XSType.CLASS);
        this.jClass = jClass;
        if (schemaTypeName != null) {
            this.name = schemaTypeName;
        }
        else this.name = jClass.getName();
    } //-- XSClass
    
    
    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jClass;
    } //-- getJType
    
    /**
     * Return true if this XSClass represents an enumerated type,
     * otherwise false
     * @return true if this XSClass represents an enumerated type,
     * otherwise false.
    **/
    public boolean isEnumerated() {
        return enumerated;
    } //-- isEnumerated
    
    public String getName() {
        return this.name;
    } //-- getName
    
    /**
     * Sets the enumerated flag for this XSClass
     * @param enumerated a boolean indicating whether or not this XSClass 
     * represents an enumerated type
    **/
    public void setAsEnumertated(boolean enumerated) {
        this.enumerated = enumerated;
    } //-- setAsEnumerated
    
} //-- XSClass
