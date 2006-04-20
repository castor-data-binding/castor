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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.util;


import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.AccessMode;

import org.exolab.castor.xml.*;

/**
 * An adapter class which can turn an ordinary ClassDescriptor
 * into an XMLClassDescriptor
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class XMLClassDescriptorAdapter 
    extends XMLClassDescriptorImpl
{
    
    
    private XMLClassDescriptor delegate = null;
    
    
    /**
     * Creates a new XMLClassDescriptorAdapter using the
     * given ClassDescriptor
     *
     * @param classDesc the ClassDescriptor to "adapt"
    **/
    public XMLClassDescriptorAdapter(ClassDescriptor classDesc, String xmlName) 
        throws org.exolab.castor.mapping.MappingException 
    {
        
        super();
        
        if (classDesc == null) {
            String err = "The ClassDescriptor argument to " +
                 "XMLClassDescriptorAdapter must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        if (classDesc instanceof XMLClassDescriptor) {
            //-- hopefully this won't happen, but we can't prevent it.
            XMLClassDescriptor xmlClassDesc = (XMLClassDescriptor)classDesc;
            FieldDescriptor[] fields = classDesc.getFields();
            for (int i = 0; i < fields.length; i++)
                addFieldDescriptor((XMLFieldDescriptor)fields[i]);
            
            setXMLName(xmlClassDesc.getXMLName());
            setExtendsWithoutFlatten((XMLClassDescriptor)
                xmlClassDesc.getExtends());
            
        }
        else {
            FieldDescriptor[] fields = classDesc.getFields();
            for (int i = 0; i < fields.length; i++) {
                FieldDescriptor fieldDesc = fields[i];
                if ( fieldDesc instanceof XMLFieldDescriptorImpl )
                    addFieldDescriptor( (XMLFieldDescriptorImpl) fieldDesc );
                else {
                    String name = fieldDesc.getFieldName();
                    XMLNaming naming = XMLNaming.getInstance();
                    String xmlFieldName = naming.toXMLName(name);
                    addFieldDescriptor(new XMLFieldDescriptorImpl(fieldDesc,xmlFieldName, 
                        ( fieldDesc.getClassDescriptor() == null ? NodeType.Element : NodeType.Attribute )));
                }
            }
            
            XMLClassDescriptor xmlClassDesc = null;
            ClassDescriptor extendsDesc = classDesc.getExtends();
            if (extendsDesc != null) {
                if (extendsDesc instanceof XMLClassDescriptor)
                    xmlClassDesc = (XMLClassDescriptor) extendsDesc;
                else {
                    xmlClassDesc = new XMLClassDescriptorAdapter(extendsDesc, null);
                }
            }
            setExtends(xmlClassDesc);
        }
        
        if ( classDesc.getIdentity() != null ) {
            FieldDescriptor identity;
            String          xmlFieldName;

            identity = classDesc.getIdentity();
            if ( identity instanceof XMLFieldDescriptor )
                xmlFieldName = ((XMLFieldDescriptor)identity).getXMLName();
            else {
                XMLNaming naming = XMLNaming.getInstance();
                xmlFieldName = naming.toXMLName(identity.getFieldName());
            }
            addFieldDescriptor(new XMLFieldDescriptorImpl(identity,xmlFieldName,NodeType.Attribute));
        }

        setJavaClass(classDesc.getJavaClass());
        setXMLName(xmlName);

    } //-- XMLClassDescriptorAdapter
    
} //-- XMLClassDescriptorAdapter
