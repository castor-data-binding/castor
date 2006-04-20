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
import org.exolab.castor.util.Configuration;

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
    
    private static NodeType _primitiveNodeType = null;
    
    static {
        if (_primitiveNodeType == null) {
            _primitiveNodeType = Configuration.getPrimitiveNodeType();
            if (_primitiveNodeType == null)
                _primitiveNodeType = NodeType.Attribute;
        }
    }
    
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
        
        process(classDesc);
        setJavaClass(classDesc.getJavaClass());
        
        if (xmlName == null) {
            if (classDesc instanceof XMLClassDescriptor) {
                xmlName = ((XMLClassDescriptor)classDesc).getXMLName();
            }
            else {
                XMLNaming naming = XMLNaming.getInstance();
                String name = classDesc.getJavaClass().getName();
                //-- strip package
                int idx = name.lastIndexOf('.');
                if (idx >= 0) {
                    name = name.substring(idx+1);
                }
                xmlName = naming.toXMLName(name);
            }
        }
        setXMLName(xmlName);

    } //-- XMLClassDescriptorAdapter

    /**
     * Copies the fieldDescriptors of the given XMLClassDesctiptor
     * into this XMLClassDescriptor.
     *
     * @param classDesc the XMLClassDescriptor to process
    **/
    private void process(ClassDescriptor classDesc) 
        throws org.exolab.castor.mapping.MappingException
    {
            
        if (classDesc instanceof XMLClassDescriptor) {
            //-- hopefully this won't happen, but we can't prevent it.
            process((XMLClassDescriptor)classDesc);
            return;
        }
        
        FieldDescriptor   identity = classDesc.getIdentity();
        FieldDescriptor[] fields   = classDesc.getFields();
        
        for (int i = 0; i < fields.length; i++) {
            FieldDescriptor fieldDesc = fields[i];
            if (fieldDesc == null) continue;
            if ( fieldDesc instanceof XMLFieldDescriptorImpl ) {
                if (identity == fieldDesc) {
                    setIdentity((XMLFieldDescriptorImpl)fieldDesc);
                    identity = null; //-- clear identity
                }
                else {
                    addFieldDescriptor((XMLFieldDescriptorImpl)fieldDesc);
                }
            }
            else {
                String name = fieldDesc.getFieldName();
                XMLNaming naming = XMLNaming.getInstance();
                String xmlFieldName = naming.toXMLName(name);
                    
                if (identity == fieldDesc) {
                    setIdentity(new XMLFieldDescriptorImpl(fieldDesc,
                                                          xmlFieldName,
                                                         NodeType.Attribute));
                    identity = null; //-- clear identity
                }
                else {
                    NodeType nodeType = NodeType.Element;
                    if (isPrimitive(fieldDesc.getFieldType()))
                        nodeType = _primitiveNodeType;
                        
                    addFieldDescriptor(new XMLFieldDescriptorImpl(fieldDesc,
                                                                xmlFieldName, 
                                                                nodeType));
                }
            }
        }
        //-- handle Identity if it wasn't already handled
        if ( identity != null ) {
            String  xmlFieldName;
            if ( identity instanceof XMLFieldDescriptor ) {
                setIdentity((XMLFieldDescriptor)identity);
            }
            else {
                XMLNaming naming = XMLNaming.getInstance();
                xmlFieldName = naming.toXMLName(identity.getFieldName());
                setIdentity(new XMLFieldDescriptorImpl(identity,xmlFieldName,NodeType.Attribute));
            }
        }
        //-- handle inheritence
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
        
    } //-- process
    
    /**
     * Copies the fieldDescriptors of the given XMLClassDesctiptor
     * into this XMLClassDescriptor.
     *
     * @param classDesc the XMLClassDescriptor to process
    **/
    private void process(XMLClassDescriptor classDesc) 
        throws org.exolab.castor.mapping.MappingException
    {
        FieldDescriptor identity = classDesc.getIdentity();
        FieldDescriptor[] fields = classDesc.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (identity == fields[i]) {
                setIdentity((XMLFieldDescriptor)fields[i]);
                identity = null; //-- clear identity
            }
            else {
                addFieldDescriptor((XMLFieldDescriptor)fields[i]);
            }
        }
        
        //-- handle identity if not already processed
        if (identity != null) {
            setIdentity((XMLFieldDescriptor)identity);
        }
        setXMLName(classDesc.getXMLName());
        setExtendsWithoutFlatten((XMLClassDescriptor)classDesc.getExtends());
        
    } //-- process
    
    /**
     * Returns true if the given class should be treated as a primitive
     * type
     * @return true if the given class should be treated as a primitive
     * type
    **/
    private static boolean isPrimitive(Class type) {

        if (type.isPrimitive()) return true;
        
        if ((type == Boolean.class) || (type == Character.class))
            return true;
            
        return (type.getSuperclass() == Number.class);
       
    } //-- isPrimitive
    
} //-- XMLClassDescriptorAdapter
