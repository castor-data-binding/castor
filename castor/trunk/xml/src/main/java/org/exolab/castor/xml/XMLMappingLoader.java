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
 * Copyright 1999-2004(C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.mapping.BindingType;
import org.exolab.castor.mapping.AbstractFieldHandler;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.AbstractFieldDescriptor;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.BindXml;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.Property;
import org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.castor.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.castor.xml.handlers.ContainerFieldHandler;
import org.exolab.castor.xml.handlers.ToStringFieldHandler;
import org.exolab.castor.xml.util.ContainerElement;
import org.exolab.castor.xml.util.XMLClassDescriptorAdapter;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLContainerElementFieldDescriptor;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.IdRefValidator;
import org.exolab.castor.xml.validators.NameValidator;

/**
 * An XML implementation of mapping helper. Creates XML class
 * descriptors from the mapping file.
 *
 * @author <a href="keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-02-23 01:37:50 -0700 (Thu, 23 Feb 2006) $
 */
public final class XMLMappingLoader extends AbstractMappingLoader {

    /** 
     * {@link Log} instance to be used. 
     */
    private static final Log LOG = LogFactory.getLog(XMLMappingLoader.class);
    
    //-----------------------------------------------------------------------------------

    /** The default xml prefix used on certain attributes such as xml:lang, xml:base,
     *  etc. */
    private static final String XML_PREFIX = "xml:";
    
    /** Empty array of class types used for reflection. */
    private static final Class[] EMPTY_ARGS = new Class[0];

    /** The NCName Schema type. */
    private static final String NCNAME = "NCName";

    /** The string argument for the valueOf method, used for introspection. */
    private static final Class[] STRING_ARG = {String.class};

    /** Factory method name for type-safe enumerations. This is primarily for allowing
     *  users to map classes that were created by Castor's SourceGenerator. */
    private static final String VALUE_OF = "valueOf";

    //-----------------------------------------------------------------------------------

    /**
     * Creates a new XMLMappingLoader.
     * Joachim 2007-08-19: called via ClassLoader from XMLMappingLoaderFactory.getMappingLoader()
     * must not be modified!!!
     * @param loader the class loader to use
     */
    public XMLMappingLoader(final ClassLoader loader) {
        super(loader);
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public BindingType getBindingType() { return BindingType.XML; }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void loadMapping(final MappingRoot mapping, final Object param)
    throws MappingException {
        if (loadMapping()) {
            createFieldHandlers(mapping);
            createClassDescriptors(mapping);
        }
    }

    //-----------------------------------------------------------------------------------
    
    /**
     * To create the class descriptor for the given class mapping.
     * Throws IllegalStateException if the class has no valid internal context.
     * @param clsMap the class mapping information to process
     * @return the {@link ClassDescriptor} created for the class mapping
     * @throws MappingException ...
     */
    protected ClassDescriptor createClassDescriptor(final ClassMapping clsMap)
    throws MappingException {
        // execution makes no sense without a context or without a resolver...
        if ((getInternalContext() == null) 
                || (getInternalContext().getXMLClassDescriptorResolver() == null)) {
            String message = "Internal context or class descriptor resolver within are not valid";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        // Create the class descriptor.
        XMLClassDescriptorAdapter xmlClassDesc = new XMLClassDescriptorAdapter();

        // Introspection and package level stuff needs to be disabled !!
        getInternalContext().getXMLClassDescriptorResolver().setUseIntrospection(false);
        getInternalContext().getXMLClassDescriptorResolver().setLoadPackageMappings(false);

        try {
            if (clsMap.getAutoComplete()) {
                if ((clsMap.getMapTo() == null) 
                        && ((clsMap.getClassChoice() == null) 
                                || (clsMap.getClassChoice().getFieldMappingCount() == 0)) 
                                && (clsMap.getIdentityCount() == 0)) {
                    // If we make it here we simply try to load a compiled mapping
                    try {
                        ClassDescriptor clsDesc = 
                            getInternalContext()
                            .getXMLClassDescriptorResolver().resolve(clsMap.getName());
                        if (clsDesc != null) { return clsDesc; }
                    } catch (ResolverException e) {
                        if (LOG.isDebugEnabled()) {
                            String message =
                                new StringBuffer().append("Ignoring exception: ").append(e)
                                .append(" at resolving: ").append(clsMap.getName()).toString();
                            LOG.debug(message);
                        }
                    }
                }
            }
            
            // Obtain the Java class.
            Class javaClass = resolveType(clsMap.getName());
            if (clsMap.getVerifyConstructable()) {
                if (!Types.isConstructable(javaClass, true)) {
                    throw new MappingException(
                            "mapping.classNotConstructable", javaClass.getName());
                }
            }
            xmlClassDesc.setJavaClass(javaClass);

            // Obtain XML name.
            String xmlName;
            MapTo mapTo = clsMap.getMapTo();
            if ((mapTo != null) && (mapTo.getXml() != null)) {
                xmlName = mapTo.getXml();
            } else {
                String clsName = getInternalContext().getJavaNaming().getClassName(javaClass);
                xmlName = getInternalContext().getXMLNaming().toXMLName(clsName);
            }
            xmlClassDesc.setXMLName(xmlName);

            // If this class extends another class, we need to obtain the extended
            // class and make sure this class indeed extends it.
            ClassDescriptor extDesc = getExtended(clsMap, javaClass);
            xmlClassDesc.setExtends((XMLClassDescriptor) extDesc);
            
            // Create all field descriptors.
            AbstractFieldDescriptor[] allFields = createFieldDescriptors(clsMap, javaClass);

            // Make sure there are no two fields with the same name.
            checkFieldNameDuplicates(allFields, javaClass);
            
            // Identify identity and normal fields. Note that order must be preserved.
            List fieldList = new ArrayList(allFields.length);
            List idList = new ArrayList();
            if (extDesc == null) {
                // Sort fields into 2 lists based on identity definition of field.
                for (int i = 0; i < allFields.length; i++) {
                    if (!allFields[i].isIdentity()) {
                        fieldList.add(allFields[i]);
                    } else {
                        idList.add(allFields[i]);
                    }
                }
                
                if (idList.size() == 0) {
                    // Found no identities based on identity definition of field.
                    // Try to find identities based on identity definition on class.
                    String[] idNames = clsMap.getIdentity();
                    
                    FieldDescriptor identity;
                    for (int i = 0; i < idNames.length; i++) {
                        identity = findIdentityByName(fieldList, idNames[i], javaClass);
                        if (identity != null) {
                            idList.add(identity);
                        } else {
                            throw new MappingException("mapping.identityMissing",
                                    idNames[i], javaClass.getName());
                        }
                    }
                }
            } else {
                // Add all fields of extending class to field list.
                for (int i = 0; i < allFields.length; i++) { fieldList.add(allFields[i]); }
                
                // Add identity of extended class to identity list.
                if (extDesc.getIdentity() != null) { idList.add(extDesc.getIdentity()); }
                
                // Search redefined identities in extending class.
                FieldDescriptor identity;
                for (int i = 0; i < idList.size(); i++) {
                    String idname = ((FieldDescriptor) idList.get(i)).getFieldName();
                    identity = findIdentityByName(fieldList, idname, javaClass);
                    if (identity != null) { idList.set(i, identity); }
                }
            }
            
            FieldDescriptor xmlId = null;
            if (idList.size() != 0) { xmlId = (FieldDescriptor) idList.get(0); }
            
            if (xmlId != null) { xmlClassDesc.setIdentity((XMLFieldDescriptorImpl) xmlId); }
            for (int i = 0; i < fieldList.size(); i++) {
                FieldDescriptor fieldDesc = (FieldDescriptor) fieldList.get(i);
                if (fieldDesc != null) {
                    xmlClassDesc.addFieldDescriptor((XMLFieldDescriptorImpl) fieldDesc);
                }
            }
            
            if (clsMap.getAutoComplete()) {

                XMLClassDescriptor referenceDesc = null;
                
                Class type = xmlClassDesc.getJavaClass();
                
                //-- check compiled descriptors 
                if ((getInternalContext() == null) 
                        || (getInternalContext().getXMLClassDescriptorResolver() == null)) {
                    String message = "Internal context or class descriptor resolver within are not valid";
                    LOG.warn(message);
                    throw new IllegalStateException(message);
                }
                try {
                    referenceDesc = (XMLClassDescriptor) getInternalContext().getXMLClassDescriptorResolver().resolve(type);
                } catch (ResolverException rx) {
                    throw new MappingException(rx);
                }

                if (referenceDesc == null) {
                    Introspector introspector = getInternalContext().getIntrospector();
                    try {
                        referenceDesc = introspector.generateClassDescriptor(type);
                        if (clsMap.getExtends() != null) {
                            //-- clear parent from introspected descriptor since
                            //-- a mapping was provided in the mapping file
                            ((XMLClassDescriptorImpl) referenceDesc).setExtends(null);
                        }
                    } catch (MarshalException mx) {
                        String error = "unable to introspect class '" +
                            type.getName() + "' for auto-complete: ";
                        throw new MappingException(error + mx.getMessage());
                    }
                }

                //-- check for identity
                String identity = "";
                if (clsMap.getIdentityCount() > 0) {
                    identity = clsMap.getIdentity(0);
                }

                
                FieldDescriptor[] xmlFields2 = xmlClassDesc.getFields();

                // Attributes
                XMLFieldDescriptor[] introFields = referenceDesc.getAttributeDescriptors();
                for (int i = 0; i < introFields.length; ++i) {
                    if (!isMatchFieldName(xmlFields2, introFields[i].getFieldName())) {
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
                introFields = referenceDesc.getElementDescriptors();
                for (int i = 0; i < introFields.length; ++i) {
                    if (!isMatchFieldName(xmlFields2, introFields[i].getFieldName())) {
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
                XMLFieldDescriptor field = referenceDesc.getContentDescriptor();
                if (field != null) {
                    if (!isMatchFieldName(xmlFields2, field.getFieldName())) {
                        // If there is no field with this name, we can add
                        xmlClassDesc.addFieldDescriptor(field);
                    }
                }
            }

            // Copy ns-uri + ns-prefix + element-definition
            if (mapTo != null) {
                xmlClassDesc.setNameSpacePrefix(mapTo.getNsPrefix());
                xmlClassDesc.setNameSpaceURI(mapTo.getNsUri());
                xmlClassDesc.setElementDefinition(mapTo.getElementDefinition());
            }
        }
        finally {
            getInternalContext().getXMLClassDescriptorResolver().setUseIntrospection(true);
            getInternalContext().getXMLClassDescriptorResolver().setLoadPackageMappings(true);
        }
        
        return xmlClassDesc;
    }

    protected final FieldDescriptor findIdentityByName(
            final List fldList, final String idName, final Class javaClass) {
        for (int i = 0; i < fldList.size(); i++) {
            FieldDescriptor field = (FieldDescriptor) fldList.get(i);
            if (idName.equals(field.getFieldName())) {
                fldList.remove(i);
                return field;
            }
        }
        return null;
    }

    protected final void resolveRelations(ClassDescriptor clsDesc) {
        FieldDescriptor[] fields;

        fields = clsDesc.getFields();
        for (int i = 0 ; i < fields.length ; ++i ) {
            if (fields[i].getClassDescriptor() != null) continue;
            ClassDescriptor   relDesc;
            
            Class fieldType = fields[i].getFieldType();
            if (fieldType != null) {
                relDesc = getDescriptor(fieldType.getName());
                if (relDesc != null &&
                        relDesc instanceof XMLClassDescriptor &&
                        fields[ i ] instanceof XMLFieldDescriptorImpl) {
                    ((XMLFieldDescriptorImpl) fields[i]).setClassDescriptor(relDesc);
                }
            }
        }
        if ( clsDesc instanceof XMLClassDescriptorImpl )
            ( (XMLClassDescriptorImpl) clsDesc ).sortDescriptors();
    }

    //-----------------------------------------------------------------------------------

    /**
     * Match if a field named <code>fieldName</code> is in fields
     */
    private boolean isMatchFieldName(FieldDescriptor[] fields, String fieldName) {
        for (int i=0; i< fields.length; ++i)
            if (fields[i].getFieldName().equals(fieldName))
                return true;

        return false;
    } //-- method: isMatchFieldName


    protected AbstractFieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
        throws MappingException
    {

        FieldDescriptor        fieldDesc;
        FieldMappingCollectionType         colType  = fieldMap.getCollection();
        String                 xmlName  = null;
        NodeType               nodeType = null;
        String                 match    = null;
        XMLFieldDescriptorImpl xmlDesc;
        boolean                isReference = false;
        boolean                isXMLTransient = false;

        //-- handle special case for HashMap/Hashtable
        if ((fieldMap.getType() == null) && (colType != null)) {
            if ((colType == FieldMappingCollectionType.HASHTABLE) ||
                (colType == FieldMappingCollectionType.MAP) ||
                (colType == FieldMappingCollectionType.SORTEDMAP))
            {
                fieldMap.setType(MapItem.class.getName());
            }
        }

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
            isXMLTransient = xml.getTransient();

            //-- autonaming
            BindXmlAutoNamingType autoName = xml.getAutoNaming();
            if (autoName != null) {
                deriveNameByClass = (autoName == BindXmlAutoNamingType.DERIVEBYCLASS);
            }

        }

        //-- transient
        //-- XXXX -> if it's transient we probably shouldn't do all
        //-- XXXX -> the unecessary work
        isXMLTransient = isXMLTransient || fieldDesc.isTransient();

        //--

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
            else if (xmlName.startsWith(XML_PREFIX)) {
                namespace = Namespaces.XML_NAMESPACE;
                xmlName = xmlName.substring(4);
            }
        }

        if (nodeType == null) {
            if (isPrimitive(javaClass))
                nodeType = getInternalContext().getPrimitiveNodeType();
            else
                nodeType = NodeType.Element;
        }

        //-- Create XML name if necessary. Note if name is to be derived
        //-- by class..we just make sure we set the name to null...
        //-- the Marshaller does this during runtime. This allows
        //-- Collections to be handled properly.
        if ((!deriveNameByClass) && ((xmlName == null) && (match == null)))
        {
            xmlName = getInternalContext().getXMLNaming().toXMLName( fieldDesc.getFieldName() );
            match = xmlName + ' ' + fieldDesc.getFieldName();
        }

        xmlDesc = new XMLFieldDescriptorImpl( fieldDesc, xmlName, nodeType, getInternalContext().getPrimitiveNodeType() );
        
        if (xmlDesc.getHandler() != null && xmlDesc.getHandler() instanceof AbstractFieldHandler) {
            AbstractFieldHandler handler = (AbstractFieldHandler) xmlDesc.getHandler();
            handler.setFieldDescriptor(xmlDesc);
        }

        //-- transient?
        xmlDesc.setTransient(isXMLTransient);

        //--set a default fieldValidator
        xmlDesc.setValidator(new FieldValidator());

        //-- enable use parent namespace if explicit one doesn't exist
        xmlDesc.setUseParentsNamespace(true);

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
        if (isReference) {
        	if (colType == null) {
        		FieldValidator fieldValidator = new FieldValidator();
        		fieldValidator.setValidator(new IdRefValidator());
        		xmlDesc.setValidator(fieldValidator);
        	} else {
        		// TODO handle other cases
        	}
        }

        xmlDesc.setContainer(fieldMap.getContainer());
        
        xmlDesc.setNillable(fieldMap.isNillable());

        if (xml != null) {

            //-- has class descriptor for type specified
            if (xml.getClassMapping() != null) {
                ClassDescriptor cd = createClassDescriptor(xml.getClassMapping());
                xmlDesc.setClassDescriptor(cd);
            }

            //-- has location path?
            if (xml.getLocation() != null) {
                xmlDesc.setLocationPath(xml.getLocation());
            }
            //is the value type needs specific handling
            //such as QName or NCName support?
            String xmlType = xml.getType();
            xmlDesc.setSchemaType(xmlType);
            xmlDesc.setQNamePrefix(xml.getQNamePrefix());
            TypeValidator validator = null;
            if (NCNAME.equals(xmlType)) {
                validator = new NameValidator(XMLConstants.NAME_TYPE_NCNAME);
                xmlDesc.setValidator(new FieldValidator(validator));
            }

            //-- special properties?
            Property[] props = xml.getProperty();
            if ((props != null) && (props.length > 0)) {
                for (int pIdx = 0; pIdx < props.length; pIdx++) {
                    Property prop = props[pIdx];
                    xmlDesc.setProperty(prop.getName(), prop.getValue());
                }
            }
        }

        //-- Get collection type
        if (colType == null) {
            //-- just in case user forgot to use collection="..."
            //-- in the mapping file
            Class type = fieldDesc.getFieldType();
            if (type != null && CollectionHandlers.hasHandler(type)) {
                String typeName = CollectionHandlers.getCollectionName(type);
                colType = FieldMappingCollectionType.valueOf(typeName);
            }
        }

        //-- isMapped item
        if (colType != null) {
            if ((colType == FieldMappingCollectionType.HASHTABLE) ||
                (colType == FieldMappingCollectionType.MAP) ||
                (colType == FieldMappingCollectionType.SORTEDMAP))
            {
                //-- Make sure user is not using an addMethod
                //-- before setting the mapped field to true.
                String methodName = fieldMap.getSetMethod();
                if (methodName != null) {
                    if (!methodName.startsWith("add")) {
                        xmlDesc.setMapped(true);
                    }
                }
                else xmlDesc.setMapped(true);
            }


            //-- special NodeType.Namespace handling
            //-- prevent FieldHandlerImpl from using CollectionHandler
            //-- during calls to #getValue
            if ((nodeType == NodeType.Namespace) || (xmlDesc.isMapped())) {
                Object handler = xmlDesc.getHandler();
                if (handler instanceof FieldHandlerImpl) {
                    FieldHandlerImpl handlerImpl = (FieldHandlerImpl)handler;
                    handlerImpl.setConvertFrom(new IdentityConvertor());
                }
            }
            //-- wrap collection in element?
            if (nodeType == NodeType.Element) {
                if (fieldMap.hasContainer() && (!fieldMap.getContainer())) {
                    xmlDesc = wrapCollection(xmlDesc);
                }
            }
        }

        //-- is Type-Safe Enumeration?
        //-- This is not very clean, we should have a way
        //-- to specify something is a type-safe enumeration
        //-- without having to guess.
        else if ((!isReference) && (!isXMLTransient)) {
            Class fieldType = xmlDesc.getFieldType();
            if (!isPrimitive(fieldType)) {
                //-- make sure no default constructor
                Constructor cons = null;
                try {
                    cons = fieldType.getConstructor(EMPTY_ARGS);
                    if (!Modifier.isPublic(cons.getModifiers())) {
                        cons = null;
                    }
                }
                catch(NoSuchMethodException nsmx) {
                    //-- Do nothing
                }
                try {
                    if (cons == null) {
                        //-- make sure a valueOf factory method
                        //-- exists and no user specified handler exists
                        Method method = fieldType.getMethod(VALUE_OF, STRING_ARG);
                        Class returnType = method.getReturnType();
                        if ((returnType != null) && fieldType.isAssignableFrom(returnType)) {
                            if (fieldMap.getHandler() == null) {
                                //-- Use EnumFieldHandler
                                //-- mapping loader now supports a basic EnumFieldHandler
                                //-- for xml we simply need to make sure the toString()
                                //-- method is called during getValue()
                                //FieldHandler handler = xmlDesc.getHandler();
                                //handler = new EnumFieldHandler(fieldType, handler);

                                FieldHandler handler = new ToStringFieldHandler(fieldType, xmlDesc.getHandler());

                                xmlDesc.setHandler(handler);
                                xmlDesc.setImmutable(true);
                            }
                        }
                    }
                }
                catch(NoSuchMethodException nsmx) {
                    //-- Do nothing
                }
            }
        }

        //-- constructor argument?
        String setter = fieldMap.getSetMethod();
        if (setter != null) {
            if (setter.startsWith("%")) {
                int index = 0;
                setter = setter.substring(1);
                index = Integer.parseInt(setter);
                if ((index < 1) || (index > 9)) {
                    throw new MappingException("mapper.invalidParameterIndex", setter);
                }
                //-- adjust index to base zero
                --index;
                xmlDesc.setConstructorArgumentIndex(index);
            }
        }

        return xmlDesc;
    }

    /**
     * Sets whether or not to look for and load package specific
     * mapping files (".castor.xml" files).
     *
     * @param loadPackageMappings a boolean that enables or
     * disables the loading of package specific mapping files
     */
    public void setLoadPackageMappings(final boolean loadPackageMappings) {
        if ((getInternalContext() == null) 
                || (getInternalContext().getXMLClassDescriptorResolver() == null)) {
            String message = "Internal context or class descriptor resolver within are not valid";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        getInternalContext()
            .getXMLClassDescriptorResolver()
            .setLoadPackageMappings(loadPackageMappings);
    } //-- setLoadPackageMappings


    protected TypeInfo getTypeInfo( Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap )
    throws MappingException {
        return new TypeInfo(fieldType, null, null, fieldMap.getRequired(), null, colHandler, false);
    }

    /**
     * This method allows a collection to be treated as a first class
     * object (and not a container) so that it has an element representation
     * in the marshalled XML.
     */
     private XMLFieldDescriptorImpl wrapCollection
        (XMLFieldDescriptorImpl fieldDesc)
            throws MappingException
    {
        //-- If we have a field 'c' that is a collection and
        //-- we want to wrap that field in an element <e>, we
        //-- need to create a field descriptor for
        //-- an object that represents the element <e> and
        //-- acts as a go-between from the parent of 'c'
        //-- denoted as P(c) and 'c' itself
        //
        //   object model: P(c) -> c
        //   xml : <p><e><c></e><p>

        //-- Make new class descriptor for the field that
        //-- will represent the container element <e>
        Class type = ContainerElement.class;
        XMLClassDescriptorImpl classDesc = new XMLClassDescriptorImpl(type);
        //-- make copy of fieldDesc and add it to our new class descriptor
        XMLFieldDescriptorImpl newFieldDesc
            = new XMLFieldDescriptorImpl(fieldDesc,
                                         fieldDesc.getXMLName(),
                                         fieldDesc.getNodeType(),
                                         getInternalContext().getPrimitiveNodeType());
        //-- nullify xmlName so that auto-naming will be enabled,
        //-- we can't do this in the constructor because
        //-- XMLFieldDescriptorImpl will create a default one.
        newFieldDesc.setXMLName(null);
        newFieldDesc.setMatches("*");

        //-- add the field descriptor to our new class descriptor
        classDesc.addFieldDescriptor(newFieldDesc);
        //-- reassociate the orignal class descriptor (for 'c')
        // of fieldDesc with our new classDesc
        fieldDesc.setClassDescriptor(classDesc);

        //-- wrap the field handler in a special container field
        //-- handler that will actually do the delgation work
        FieldHandler handler = new ContainerFieldHandler(fieldDesc.getHandler());
        newFieldDesc.setHandler(handler);
        fieldDesc.setHandler(handler);

        //-- Change fieldType of original field descriptor and
        //-- return new descriptor
        return new XMLContainerElementFieldDescriptor(fieldDesc, getInternalContext().getPrimitiveNodeType());
    } //-- createWrapperDescriptor

    /**
     * A special TypeConvertor that simply returns the object
     * given. This is used for preventing the FieldHandlerImpl
     * from using a CollectionHandler when getValue is called.
    **/
    class IdentityConvertor implements TypeConvertor {
        public Object convert(final Object object) {
            return object;
        }
    } //-- class: IdentityConvertor
} //-- class: XMLMappingLoader



