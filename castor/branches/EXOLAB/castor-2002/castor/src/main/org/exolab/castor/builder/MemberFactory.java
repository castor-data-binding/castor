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
import org.exolab.castor.xml.JavaXMLNaming;
import org.exolab.castor.xml.schema.*;

import org.exolab.javasource.JSourceCode;

/**
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class MemberFactory {
    
    /**
     * Creates a member for content models that support "any" element.
     * @return the new SGMember
    **/
    public static SGMember createMemberForAny() {
        
                
        //-- determine type
                
        JSourceCode jsc = null;
        SGMember member = null;
        XSType xsType = new XSClass(SGTypes.Object);
                
        String memberName = "obj";
        String vName = "_anyList";
        String xmlName = "object";
        SGList sgList = new SGList(xsType, vName, xmlName);
        XSList xsList = sgList.getXSList();
        xsList.setMinimumSize(0);
        member = sgList;
        member.setSchemaType("any");
        member.setRequired(false);
        member.setXMLName(xmlName);
        return member;
    } //-- createMemberForAny()
        
    /**
     * Creates a member based on the given ElementDecl
     * @param eDecl the ElementDecl to create the member from
    **/
    public static SGMember createMember(ElementDecl eDecl) {
        
        //-- check whether this should be a Vector or not
        int maxOccurs = eDecl.getMaximumOccurance();
        int minOccurs = eDecl.getMinimumOccurance();
        
        if (eDecl.isReference()) {
            ElementDecl eRef = eDecl.getReference();
            if (eRef == null) {
                String err = "unable to resolve element reference: ";
                err += eDecl.getName();
                System.out.println(err);
                return null;
            }
            else eDecl = eRef;
        }
        String typeRef = eDecl.getTypeRef();
        if (typeRef == null) {
            typeRef = JavaXMLNaming.toJavaClassName(eDecl.getName());
        }
                
        //-- determine type
                
        JSourceCode jsc = null;
        SGMember member = null;
        XSType xsType = TypeConversion.createXSType(typeRef);
                
        String memberName = "v"+JavaXMLNaming.toJavaClassName(eDecl.getName());
                
        if (maxOccurs != 1) {
            String vName = memberName+"List";
            SGList sgList = new SGList(xsType, vName, eDecl.getName());
            XSList xsList = sgList.getXSList();
            xsList.setMaximumSize(maxOccurs);
            xsList.setMinimumSize(minOccurs);
            member = sgList;
                    
        }
        else {
            member = new SGElementMember(xsType, memberName);
        }
        member.setSchemaType(typeRef);
        member.setRequired(minOccurs > 0);
        member.setXMLName(eDecl.getName());
        return member;
    } //-- createMember(ElementDecl)
        
} //-- MemberFactory