/*
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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.util;

import org.castor.xml.BackwardCompatibilityContext;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.AbstractXMLNaming;

/**
 * An adapter class which can turn an ordinary ClassDescriptor into an
 * XMLClassDescriptor.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class XMLClassDescriptorAdapter extends XMLClassDescriptorImpl {
    public XMLClassDescriptorAdapter() {
        super();
    }

    /**
     * Creates a new XMLClassDescriptorAdapter using the given ClassDescriptor.
     *
     * @param classDesc the ClassDescriptor to "adapt"
     * @param xmlName the XML name for the class
     */
    public XMLClassDescriptorAdapter(final ClassDescriptor classDesc, final String xmlName)
                                throws org.exolab.castor.mapping.MappingException {
        this(classDesc, xmlName, null);
    } //-- XMLClassDescriptor

    /**
     * Creates a new XMLClassDescriptorAdapter using the given ClassDescriptor.
     *
     * @param classDesc the ClassDescriptor to "adapt"
     * @param xmlName the XML name for the class
     * @param primitiveNodeType the NodeType to use for primitives
     */
    public XMLClassDescriptorAdapter(final ClassDescriptor classDesc,
            String xmlName, NodeType primitiveNodeType)
    throws MappingException {
        super();

        if (classDesc == null) {
            String err = "The ClassDescriptor argument to "
                + "XMLClassDescriptorAdapter must not be null.";
            throw new IllegalArgumentException(err);
        }

        if (primitiveNodeType == null) {
            primitiveNodeType = new BackwardCompatibilityContext().getPrimitiveNodeType();
        }

        if (primitiveNodeType == null) {
            primitiveNodeType = NodeType.Attribute;
        }

        process(classDesc, primitiveNodeType);
        setJavaClass(classDesc.getJavaClass());

        if (xmlName == null) {
            if (classDesc instanceof XMLClassDescriptor) {
                xmlName = ((XMLClassDescriptor) classDesc).getXMLName();
            } else {
                AbstractXMLNaming naming = AbstractXMLNaming.getInstance();
                String name = classDesc.getJavaClass().getName();
                //-- strip package
                int idx = name.lastIndexOf('.');
                if (idx >= 0) {
                    name = name.substring(idx + 1);
                }
                xmlName = naming.toXMLName(name);
            }
        }
        setXMLName(xmlName);
    }

    /**
     * Copies the fieldDescriptors of the given XMLClassDesctiptor into this
     * XMLClassDescriptor.
     *
     * @param classDesc the XMLClassDescriptor to process
     */
    private void process(ClassDescriptor classDesc, NodeType primitiveNodeType)
    throws MappingException {
        if (classDesc instanceof XMLClassDescriptor) {
            //-- hopefully this won't happen, but we can't prevent it.
            process((XMLClassDescriptor)classDesc);
            return;
        }

        //-- handle inheritence
        XMLClassDescriptor xmlClassDesc = null;
        ClassDescriptor extendsDesc = classDesc.getExtends();
        if (extendsDesc != null) {
            if (extendsDesc instanceof XMLClassDescriptor) {
                xmlClassDesc = (XMLClassDescriptor) extendsDesc;
            } else {
                xmlClassDesc = new XMLClassDescriptorAdapter(extendsDesc, null, primitiveNodeType);
            }
        }
        setExtends(xmlClassDesc);

        FieldDescriptor   identity = classDesc.getIdentity();
        FieldDescriptor[] fields   = classDesc.getFields();

        //-- Note from Keith for anyone interested...
        //-- hack for multiple identities if ClassDescriptor is
        //-- an implementation of ClassDescriptorImpl...
        //-- This is really a bug in ClassDescriptorImpl, but
        //-- since it's shared code between JDO + XML I don't
        //-- want to change it there until both the XML and JDO
        //-- folks can both approve the change.
        if (classDesc instanceof ClassDescriptorImpl) {
            ClassDescriptorImpl cdImpl = (ClassDescriptorImpl)classDesc;
            FieldDescriptor[] identities = cdImpl.getIdentities();
            if ((identities != null) && (identities.length > 1)) {
                int size = fields.length + identities.length;
                FieldDescriptor[] newFields = new FieldDescriptor[size];
                System.arraycopy(fields, 0, newFields, 0, fields.length);
                System.arraycopy(identities, 0, newFields, fields.length, identities.length);
                fields = newFields;
            }
        }
        //-- End ClassDescriptorImpl fix

        for (int i = 0; i < fields.length; i++) {
            FieldDescriptor fieldDesc = fields[i];
            if (fieldDesc == null) {
                continue;
            }
            if (fieldDesc instanceof XMLFieldDescriptorImpl) {
                if (identity == fieldDesc) {
                    setIdentity((XMLFieldDescriptorImpl)fieldDesc);
                    identity = null; //-- clear identity
                } else {
                    addFieldDescriptor((XMLFieldDescriptorImpl)fieldDesc);
                }
            } else {
                String name = fieldDesc.getFieldName();
                AbstractXMLNaming naming = AbstractXMLNaming.getInstance();
                String xmlFieldName = naming.toXMLName(name);

                if (identity == fieldDesc) {
                    setIdentity(new XMLFieldDescriptorImpl(fieldDesc,
                                                          xmlFieldName,
                                                         NodeType.Attribute,
                                                         primitiveNodeType));
                    identity = null; //-- clear identity
                } else {
                    NodeType nodeType = NodeType.Element;
                    if (isPrimitive(fieldDesc.getFieldType())) {
                        nodeType = primitiveNodeType;
                    }

                    addFieldDescriptor(new XMLFieldDescriptorImpl(fieldDesc,
                                                                xmlFieldName,
                                                                nodeType,
                                                                primitiveNodeType));
                }
            }
        }

        //-- Handle Identity if it wasn't already handled. This occurs
        //-- if the ClassDescriptor implementation doesn't return
        //-- the identity field as part of the collection of fields
        //-- returned by getFields (even though it should).
        if ( identity != null ) {
            String  xmlFieldName;
            if ( identity instanceof XMLFieldDescriptor ) {
                setIdentity((XMLFieldDescriptor)identity);
            } else {
                AbstractXMLNaming naming = AbstractXMLNaming.getInstance();
                xmlFieldName = naming.toXMLName(identity.getFieldName());
                setIdentity(new XMLFieldDescriptorImpl(identity,
                                                       xmlFieldName,
                                                       NodeType.Attribute,
                                                       primitiveNodeType));
            }
        }
    }

    /**
     * Copies the fieldDescriptors of the given XMLClassDesctiptor into this
     * XMLClassDescriptor.
     *
     * @param classDesc the XMLClassDescriptor to process
     */
    private void process(XMLClassDescriptor classDesc) {
        FieldDescriptor identity = classDesc.getIdentity();
        FieldDescriptor[] fields = classDesc.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (identity == fields[i]) {
                setIdentity((XMLFieldDescriptor)fields[i]);
                identity = null; //-- clear identity
            } else {
                addFieldDescriptor((XMLFieldDescriptor)fields[i]);
            }
        }

        //-- handle identity if not already processed
        if (identity != null) {
            setIdentity((XMLFieldDescriptor)identity);
        }
        setXMLName(classDesc.getXMLName());
        setExtendsWithoutFlatten((XMLClassDescriptor) classDesc.getExtends());
    }
}
