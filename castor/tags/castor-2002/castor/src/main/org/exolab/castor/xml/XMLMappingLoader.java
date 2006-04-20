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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;


import java.io.PrintWriter;
import java.util.Vector;
import java.util.Enumeration;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.BindXml;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.castor.mapping.xml.types.CollectionType;
import org.exolab.castor.util.Configuration;

import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLClassDescriptorAdapter;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.NameValidator;

/**
 * An XML implementation of mapping helper. Creates XML class
 * descriptors from the mapping file.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class XMLMappingLoader
    extends MappingLoader
{

    /**
     * The NCName Schema type
    **/
    private static final String NCNAME = "NCName";
    

    /**
     * naming conventions
    **/
    private static XMLNaming    _naming = null;

    /**
     * Introspector
    **/
    private static NodeType _primitiveNodeType = null;

    static {
        _naming = XMLNaming.getInstance();

        if (_primitiveNodeType == null) {
            _primitiveNodeType = Configuration.getPrimitiveNodeType();
            if (_primitiveNodeType == null)
                _primitiveNodeType = NodeType.Attribute;
        }
    }

    /**
     * Creates a new XMLMappingLoader
    **/
    public XMLMappingLoader( ClassLoader loader, PrintWriter logWriter )
        throws MappingException
    {
        super( loader, logWriter );

    } //-- XMLMappingLoader


    protected void resolveRelations( ClassDescriptor clsDesc )
        throws MappingException
    {
        FieldDescriptor[] fields;

        fields = clsDesc.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            ClassDescriptor   relDesc;

            relDesc = getDescriptor( fields[ i ].getFieldType() );
            if ( relDesc == NoDescriptor ) {
                // XXX Error message should come here
            }
            else if ( relDesc != null &&
                      relDesc instanceof XMLClassDescriptor &&
                      fields[ i ] instanceof XMLFieldDescriptorImpl )
            {
		        ( (XMLFieldDescriptorImpl) fields[ i ] ).setClassDescriptor( (XMLClassDescriptor) relDesc );

		        //-- removed kvisco 20010814
                // ( (XMLFieldDescriptorImpl) fields[ i ] ).setNodeType( NodeType.Element );
            }
        }
        if ( clsDesc instanceof XMLClassDescriptorImpl )
            ( (XMLClassDescriptorImpl) clsDesc ).sortDescriptors();
    }


    protected ClassDescriptor createDescriptor( ClassMapping clsMap )
        throws MappingException
    {
        ClassDescriptor clsDesc;
        String          xmlName;

        // See if we have a compiled descriptor.
        clsDesc = loadClassDescriptor( clsMap.getName() );
        if ( clsDesc != null && clsDesc instanceof XMLClassDescriptor )
            return clsDesc;

        // Use super class to create class descriptor. Field descriptors will be
        // generated only for supported fields, see createFieldDesc later on.
        clsDesc = super.createDescriptor( clsMap );
        MapTo mapTo = clsMap.getMapTo();
        if (( mapTo == null) || (mapTo.getXml() == null))
            xmlName = _naming.toXMLName( clsDesc.getJavaClass().getName() );
        else {
            xmlName = clsMap.getMapTo().getXml();

        }

        XMLClassDescriptorImpl xmlClassDesc
            = new XMLClassDescriptorAdapter( clsDesc, xmlName );

        if (clsMap.getAutoComplete()) {

            Introspector introspector = new Introspector();

            XMLClassDescriptor introspectedDesc;

            try {
                introspectedDesc = introspector.generateClassDescriptor(xmlClassDesc.getJavaClass());
            } catch (MarshalException mx) {
                throw new MappingException("Unable to introspect class for auto-complete: " + mx);
            }

            //-- check for identity
            String identity = "";
            if (clsMap.getIdentityCount() > 0)
                identity = clsMap.getIdentity(0);

            FieldDescriptor[] fields = xmlClassDesc.getFields();

            // Attributes
            XMLFieldDescriptor[] introFields = introspectedDesc.getAttributeDescriptors();
            for (int i = 0; i<introFields.length; ++i) {
                if (!isMatchFieldName(fields, introFields[i].getFieldName())) {
                    // If there is no field with this name, we can add it
                    if (introFields[i].getFieldName().equals(identity)) {
                        xmlClassDesc.setIdentity(introFields[i]);
                    }
                    else {
                        xmlClassDesc.addFieldDescriptor(introFields[i]);
                    }
                }
            }

            // Elements
            introFields = introspectedDesc.getElementDescriptors();
            for (int i = 0; i<introFields.length; ++i) {
                if (!isMatchFieldName(fields, introFields[i].getFieldName())) {
                    // If there is no field with this name, we can add it
                    if (introFields[i].getFieldName().equals(identity)) {
                        xmlClassDesc.setIdentity(introFields[i]);
                    }
                    else {
                        xmlClassDesc.addFieldDescriptor(introFields[i]);
                    }
                }
            }

            // Content
            XMLFieldDescriptor field = introspectedDesc.getContentDescriptor();
            if (field!= null)
                if (isMatchFieldName(fields, field.getFieldName()))
                    // If there is no field with this name, we can add
                    xmlClassDesc.addFieldDescriptor(field);


        } //-- End of auto-complete


        //-- copy ns-uri + ns-prefix
        if (mapTo != null) {
            xmlClassDesc.setNameSpacePrefix(mapTo.getNsPrefix());
            xmlClassDesc.setNameSpaceURI(mapTo.getNsUri());
        }
        return xmlClassDesc;
    } //-- createDescriptor


    /**
     * Match if a field named <code>fieldName</code> is in fields
     */
    private boolean isMatchFieldName(FieldDescriptor[] fields, String fieldName) {
        for (int i=0; i< fields.length; ++i)
            if (fields[i].getFieldName().equals(fieldName))
                return true;

        return false;
    } //-- method: isMatchFieldName


    protected FieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
        throws MappingException
    {
        FieldDescriptor        fieldDesc;
        String                 xmlName  = null;
        NodeType               nodeType = null;
        String                 match    = null;
        XMLFieldDescriptorImpl xmlDesc;
        boolean                isReference = false;
        boolean                isXMLTransient = false;

        // Create an XML field descriptor

        fieldDesc = super.createFieldDesc( javaClass, fieldMap );
        BindXml xml = fieldMap.getBindXml();

        boolean deriveNameByClass = false;
        
        if (xml != null) {
            //-- xml name
            xmlName = xml.getName();
            
            //-- node type
            if ( xml.getNode() != null )
                nodeType = NodeType.getNodeType( xml.getNode().toString() );

            //-- matches
            match = xml.getMatches();

            //-- reference
            isReference = xml.getReference();
            
            //-- XML transient
            isXMLTransient = xml.getTransient() || fieldDesc.isTransient();
            
            //-- autonaming
            BindXmlAutoNamingType autoName = xml.getAutoNaming();
            if (autoName != null) {
                deriveNameByClass = (autoName == BindXmlAutoNamingType.DERIVEBYCLASS);
            }
            
        }
        
        //-- handle QName for xmlName
        String namespace = null;
        if ((xmlName != null) && (xmlName.length() > 0)){
            if (xmlName.charAt(0) == '{') {
                int idx = xmlName.indexOf('}');
                if (idx < 0) {
                    throw new MappingException("Invalid QName: " + xmlName);
                }
                namespace = xmlName.substring(1, idx);
                xmlName = xmlName.substring(idx+1);
            }
        }

        if (nodeType == null) {
            if (isPrimitive(javaClass))
                nodeType = _primitiveNodeType;
            else
                nodeType = NodeType.Element;
        }        

        //-- Create XML name if necessary. Note if name is to be derived
        //-- by class..we just make sure we set the name to null...
        //-- the Marshaller does this during runtime. This allows
        //-- Collections to be handled properly.
        if ((!deriveNameByClass) && ((xmlName == null) && (match == null)))
        {
            xmlName = _naming.toXMLName( fieldDesc.getFieldName() );
            match = xmlName + ' ' + fieldDesc.getFieldName();
        }

        xmlDesc = new XMLFieldDescriptorImpl( fieldDesc, xmlName, nodeType );

        //-- transient?
        xmlDesc.setTransient(isXMLTransient);
        
        //-- If deriveNameByClass we need to reset the name to
        //-- null because XMLFieldDescriptorImpl tries to be smart
        //-- and automatically creates the name.
        if (deriveNameByClass) {
            xmlDesc.setXMLName(null);
        }
        
        //-- namespace
        if (namespace != null) {
            xmlDesc.setNameSpaceURI(namespace);
        }
        
        //-- matches
        if (match != null) {
            xmlDesc.setMatches(match);
            //-- special fix for xml-name since XMLFieldDescriptorImpl
            //-- will create a default name based off the field name
            if (xmlName == null) xmlDesc.setXMLName(null);
        }

        //-- reference
        xmlDesc.setReference(isReference);

        xmlDesc.setContainer(fieldMap.getContainer());

        //is the value type needs specific handling
        //such as QName or NCName support?
        if (xml != null) {
            String xmlType = xml.getType();
            xmlDesc.setSchemaType(xmlType);
            xmlDesc.setQNamePrefix(xml.getQNamePrefix());
            TypeValidator validator = null;
            if (NCNAME.equals(xmlType)) {
                validator = new NameValidator(NameValidator.NCNAME);
                xmlDesc.setValidator(new FieldValidator(validator));
            }
        }
        
        //-- isMapped item
        CollectionType colType = fieldMap.getCollection();
        if (colType != null) {
            xmlDesc.setMapped((colType == CollectionType.HASHTABLE) ||
               (colType == CollectionType.MAP));
               
            //-- special NodeType.Namespace handling
            //-- prevent FieldHandlerImpl from using CollectionHandler
            //-- during calls to #getValue
            if (nodeType == NodeType.Namespace) {
                Object handler = xmlDesc.getHandler();
                if (handler instanceof FieldHandlerImpl) {
                    FieldHandlerImpl handlerImpl = (FieldHandlerImpl)handler;
                    handlerImpl.setConvertFrom(new IdentityConvertor());
                }
            }
        }

        return xmlDesc;
    }


    protected TypeInfo getTypeInfo( Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap )
        throws MappingException
    {
        fieldType = Types.typeFromPrimitive( fieldType );
        return new TypeInfo( fieldType, null, null,
                             fieldMap.getRequired(), null, colHandler );
    }

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
    
    /**
     * A special TypeConvertor that simply returns the object
     * given. This is used for preventing the FieldHandlerImpl
     * from using a CollectionHandler when getValue is called.
    **/
    class IdentityConvertor implements TypeConvertor {
        public Object convert( Object object, String param )
            throws ClassCastException
        {
            return object;
        }
    } //-- class: IdentityConvertor
    
} //-- class: XMLMappingLoader



