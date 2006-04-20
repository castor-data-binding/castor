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

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;

import org.exolab.javasource.*;

/**
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SGGroupMember extends SGMember {
        
    private CodeHelper codeHelper = null;
    
    /**
     * Creates a new SGGroupMember with with given name and type
     * @param name the name of the element member to create
     * @param xsType the XSType of the element member
    **/
    public SGGroupMember(XSType xsType, String name) {
        super(xsType, name);
    }
    
    /**
     * Generates the marshalling code required for serializing
     * the SGMember as XML
     * @param jsc the JSourceCode object to write the source code
     * to
    **/
    public void generateMarshalCode(JSourceCode jsc) {
        jsc.add("//-- print ");
        jsc.append(getName());
                    
        XSType xsType = getXSType();
        
        switch(xsType.getType()) {
            case XSType.CLASS:
                jsc.add("if (");
                jsc.append(getName());
                jsc.append(" != null) {");
                jsc.indent();
                jsc.add(getName());
                jsc.append(".marshal(handler);");
                jsc.unindent();
                jsc.add("}");
                break;
        }
        
    } //-- generateMarshalCode
    
    /**
     * Generates the validation code required for validating
     * members represented by SGMember of this type
     * @param jsc the JSourceCode object to write the source code
     * to
    **/
    public void generateValidationCode(JSourceCode jsc) {
        if (getRequired()) {
            jsc.add("");
            jsc.add("//-- make sure ");
            jsc.append(getName());
            jsc.append(" is not null");
            jsc.add("if (this.");
            jsc.append(getName());
            jsc.append(" == null) {");
            jsc.indent();
            jsc.add("String err = \"The ");
            jsc.append(getXMLName());
            jsc.append(" object cannot be null in order to be valid.\";");
            jsc.add("throw new ValidationException(err);");
            jsc.unindent();
            jsc.add("}");
        }
    } //-- generateValidationCode
    
    public short getFromType() {
        return SGMember.ELEMENT;
    }
    
    /**
     * Return whether or not this member is a multivalued member or not
     * @return true if this member can appear more than once
    **/
    public boolean isMultivalued() {
        return false;
    }
    
    /**
     * Sets the CodeHelper to use when creating source code
     * @param codeHelper the CodeHelper to use when creating source code
     * @see SGMember
    **/
    public void setCodeHelper(CodeHelper codeHelper) {
        this.codeHelper = codeHelper;
    } //-- setCodeHelper
    
} // SGElementMember
