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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */
package org.exolab.castor.mapping.loader;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.ExtendedFieldHandler;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.handlers.EnumFieldHandler;
import org.exolab.castor.mapping.handlers.TransientFieldHandler;
import org.exolab.castor.mapping.xml.ClassChoice;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;

/**
 * Assists in the construction of descriptors. Can be used as a mapping
 * resolver to the engine. Engines will implement their own mapping
 * scheme typically by extending this class.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public abstract class AbstractMappingLoader extends AbstractMappingLoader2 {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractMappingLoader.class);
    
    /** The prefix for the "add" method. */
    private static final String ADD_METHOD_PREFIX = "add";

    /** The standard prefix for the getter method. */
    private static final String GET_METHOD_PREFIX = "get";
    
    /** The prefix for the "is" method for booleans. */
    private static final String IS_METHOD_PREFIX = "is";
    
    /** Empty array of class types used for reflection. */
    protected static final Class[] EMPTY_ARGS = new Class[0];

    /** The string argument for the valueOf method, used for introspection when searching for
     *  type-safe enumeration style classes. */
    protected static final Class[] STRING_ARG = { String.class };

    /** Factory method name for type-safe enumerations. */
    protected static final String VALUE_OF = "valueOf";
    
    public static final ClassDescriptor NoDescriptor = new ClassDescriptorImpl(Class.class);

    /**
     * Constructs a new mapping helper. This constructor is used by
     * a derived class.
     *
     * @param loader The class loader to use, null for the default
     */
    protected AbstractMappingLoader(final ClassLoader loader) {
        super(loader);
    }
    
    public final String getSourceType() { return "CastorXmlMapping"; }

    /**
     * Returns the Java class for the named type. The type name can
     * be one of the accepted short names (e.g. <tt>integer</tt>) or
     * the full Java class name (e.g. <tt>java.lang.Integer</tt>).
     * If the short name is used, the primitive type might be returned.
     */
    protected final Class resolveType(final String typeName)
    throws ClassNotFoundException {
        return Types.typeFromName(getClassLoader(), typeName);
    }

    /**
     * Loads the mapping from the specified mapping object. Calls {@link
     * #createDescriptor} to create each descriptor and {@link
     * #addDescriptor} to store it. Also loads all the included mapping
     * files.
     *
     * @param mapping The mapping information
     * @param param Arbitrary parameter that can be used by subclasses
     * @throws MappingException The mapping file is invalid
     */
    protected void loadMappingInternal(final MappingRoot mapping, final Object param)
    throws MappingException {
        Enumeration   enumeration;

        // Load the mapping for all the classes. This is always returned
        // in the same order as it appeared in the mapping file.
        enumeration = mapping.enumerateClassMapping();
        
        Vector retryList = null;
        while (enumeration.hasMoreElements()) {

            ClassMapping clsMap = (ClassMapping) enumeration.nextElement();
            ClassDescriptor clsDesc = null;
            
            try {
                clsDesc = createDescriptor(clsMap);
            } catch(MappingException mx) {
                //-- save for later for possible out-of-order
                //-- mapping files...
                if (retryList == null) {
                    retryList = new Vector();
                }
                retryList.addElement(clsMap); 
                continue;
            }
            
            if (clsDesc != NoDescriptor) {
                addDescriptor(clsDesc);
            } else {
                // If the return value is NoDescriptor then the derived
                // class was not successful in constructing a descriptor.
                LOG.info(Messages.format("mapping.ignoringMapping", clsMap.getName()));
            }
        }
        
        //-- handle possible retries, for now we only loop once
        //-- on the retries, but we should change this to keep
        //-- looping until we have no more success rate.
        if (retryList != null) {
            Vector tmpRetryList = retryList;
            retryList = null;
            enumeration = tmpRetryList.elements();
            while (enumeration.hasMoreElements()) {
                ClassMapping clsMap = (ClassMapping) enumeration.nextElement();
                ClassDescriptor clsDesc = createDescriptor( clsMap );
                if (clsDesc != NoDescriptor) {
                    addDescriptor(clsDesc);
                } else {
                    // If the return value is NoDescriptor then the derived
                    // class was not successful in constructing a descriptor.
                    LOG.info(Messages.format("mapping.ignoringMapping", clsMap.getName()));
                }
            }
        }
        
        Iterator iter = descriptorIterator();
        while (iter.hasNext()) {
            ClassDescriptor clsDesc = (ClassDescriptor) iter.next();
            if (clsDesc != NoDescriptor) { resolveRelations(clsDesc); }
        }
    } //-- loadMapping

    protected void resolveRelations(ClassDescriptor clsDesc) throws MappingException {
        FieldDescriptor[] fields;

        fields = clsDesc.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            ClassDescriptor   relDesc;

            relDesc = getDescriptor( fields[ i ].getFieldType().getName() );
            if ( relDesc == NoDescriptor ) {
                // XXX Error message should come here
            } else if ( relDesc != null && fields[ i ] instanceof FieldDescriptorImpl ) {
                ( (FieldDescriptorImpl) fields[ i ] ).setClassDescriptor( relDesc );
            }
        }
    }

    /**
     * Creates a new descriptor. The class mapping information is used
     * to create a new stock {@link ClassDescriptor}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param clsMap The class mapping information
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    protected ClassDescriptor createDescriptor(ClassMapping clsMap)
    throws MappingException {
        FieldDescriptor[] fields;
        FieldDescriptor[] identities;
        Class             javaClass;
        ClassDescriptor   extend;
        ClassDescriptor   depend;
        ClassDescriptor   clsDesc;

        // See if we have a compiled descriptor.
        clsDesc = null;
        if ( clsDesc != null )
            return clsDesc;

        // Obtain the Java class.
        try {
            javaClass = resolveType( clsMap.getName() );
        } catch ( ClassNotFoundException except ) {
            throw new MappingException( "mapping.classNotFound", clsMap.getName() );
        }

        // If this class extends another class, need to obtain the extended
        // class and make sure this class indeed extends it.
        if ( clsMap.getExtends() != null ) {
            try {
                extend = getDescriptor( resolveType( ( (ClassMapping) clsMap.getExtends() ).getName() ).getName() );
                if ( extend == null )
                    throw new MappingException( "mapping.extendsMissing",
                                                clsMap.getExtends(), javaClass.getName() );
                if ( extend == NoDescriptor )
                    throw new MappingException( "mapping.extendsNoMapping",
                                                clsMap.getExtends(), javaClass.getName() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( except );
            }
        } else
            extend = null;


        // If this class depends another class, need to obtain the depended class
        if ( clsMap.getDepends() != null ) {
            try {
                depend = getDescriptor( resolveType( ( (ClassMapping)  clsMap.getDepends() ).getName() ).getName() );
                if ( depend == null )
                    throw new MappingException( "Depends not found" +
                                                clsMap.getDepends() + " " + javaClass.getName() );
                if ( depend == NoDescriptor )
                    throw new MappingException( "Depends not found" +
                                                clsMap.getDepends() + " " + javaClass.getName() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( except );
            }
        } else
            depend = null;


        // Get field descriptors first. Note: order must be preserved for fields,
        // but not for relations or container fields. Add all the container fields
        // in there.
        FieldMapping[] fm = null;
        if (clsMap.getClassChoice() != null) {
            fm = clsMap.getClassChoice().getFieldMapping();
        } else  {
            fm = new FieldMapping[0];
        }
        fields = createFieldDescs( javaClass, fm );

        // Make sure there are no two fields with the same name.
        // Crude but effective way of doing this.
        for ( int i = 0 ; i < fields.length ; ++i ) {
            for ( int j = i + 1 ; j < fields.length ; ++j ) {
                if ( fields[ i ].getFieldName().equals( fields[ j ].getFieldName() ) )
                    throw new MappingException( "The field " + fields[ i ].getFieldName() +
                                                " appears twice in the descriptor for " +
                                                javaClass.getName() );
            }
        }

        // Obtain the identity field from one of the above fields.
        // The identity field is removed from the list of fields.
        identities = null;
        boolean idfield = false;
        String[] ids;
        ClassMapping origin = clsMap;
        Vector fieldList = new Vector();

        while (origin.getExtends() != null) {
            origin = (ClassMapping) origin.getExtends();
        }
        ids = origin.getIdentity();
        ids = AbstractMappingLoader.getIdentityColumnNames (ids, origin);
        
        if (( ids != null ) && ( ids.length > 0)) {

            // Check that an XML mapping file do not declare more identity
            // attributes for a given class than there are field elements
            // defined for that class.
            // (Patch submitted by Gabriel Richard Pack <gpack@electroneconomy.com>)
            if ( ids.length > fields.length && origin == clsMap ) {
                String badIdentities = "";
                String delimiter     = " or ";
                for ( int index = 0; index < ids.length; index++ ) {
                    badIdentities += ids[index];
                    if ( index != ids.length - 1 )
                        badIdentities += delimiter;
                }
                throw new MappingException( "mapping.identityMissing",
                badIdentities, javaClass.getName() );
            }

            identities = new FieldDescriptor[ids.length];
            // separates fields into identity fields and regular fields
            for ( int i=0; i < fields.length ; i++ ) {
                //System.out.println("MappingLoader.createClassDesc.for:id: " + i );
                idfield = false;
                for ( int k=0; k<ids.length; k++ ) {
                    //System.out.println(fields[i].getFieldName() + " " + ids[k] );
                    if ( fields[i].getFieldName().equals( ids[k] ) ) {
                        identities[k] = fields[i];
                        idfield = true;
                        break;
                    }
                }
                if ( idfield ) {
                    //System.out.println("Field["+i+"] is an id field");
                    if ( fields[i] instanceof FieldDescriptorImpl )
                        ( (FieldDescriptorImpl) fields[i] ).setRequired( true );
                    if ( fields[i].getHandler() instanceof FieldHandlerImpl )
                        ( (FieldHandlerImpl) fields[i].getHandler() ).setRequired( true );
                } else {
                    // copy non identity field from list of fields.
                    fieldList.addElement(fields[i]);
                }
            }

            if (extend != null) {
                // we allow identity fields to be re-defined in the extends
                // class mapping to override some properties of the field,
                // for example, <sql name="..."/>.
                if ( extend instanceof ClassDescriptorImpl ) {
                    ClassDescriptorImpl extendImpl = (ClassDescriptorImpl) extend;
                    for (int i = 0; i < identities.length; i++) {
                        if (identities[i] == null) {
                            identities[i] = extendImpl.getIdentities()[i];
                        }
                    }
                } else {
                    // we leave things in the old way for the XML side
                    if ( identities[0] == null )
                        if ( extend.getIdentity() != null ) {
                            identities = new FieldDescriptor[] {extend.getIdentity()};
                        } else {
                            identities = new FieldDescriptor[0];
                        }
                }
            }

            // convert fieldList into array
            fields = new FieldDescriptor[fieldList.size()];
            fieldList.copyInto(fields);

            // the following check only needed by JDO side, move it to JDOMappingLoader
            /*
            if ( identities == null || identities.length == 0 ) {
                throw new MappingException( "mapping.identityMissing", clsMap.getIdentity(),
                                            javaClass.getName() );
            }*/

            // do a more general test instead
            if ( ids != null && ids.length > 0
                    && (identities == null || identities.length <= 0 ) ) {
                StringBuffer sb = new StringBuffer();
                for ( int i=0; i < ids.length; i++ ) {
                    if ( i != 0 ) sb.append("/");
                    sb.append( ids[i] );
                }
                throw new MappingException("mapping.identityMissing", sb,
                        javaClass.getName() );
            }
        }
        
        // Create the class descriptor.
        clsDesc = new ClassDescriptorImpl(clsMap, javaClass, fields, identities, extend, depend);

        return clsDesc;
    }

    /**
     * Create field descriptors. The class mapping information is used
     * to create descriptors for all the fields in the class, except
     * for container fields. Implementations may extend this method to
     * create more suitable descriptors, or create descriptors only for
     * a subset of the fields.
     *
     * @param javaClass The class to which the fields belong
     * @param fieldMaps The field mappings
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    protected final FieldDescriptor[] createFieldDescs(Class javaClass, FieldMapping[] fieldMaps)
    throws MappingException {
        FieldDescriptor[] fields;

        if ( fieldMaps == null || fieldMaps.length == 0 )
            return new FieldDescriptor[ 0 ];
        fields = new FieldDescriptor[ fieldMaps.length ];
        for ( int i = 0 ; i < fieldMaps.length ; ++i )
            fields[ i ] = createFieldDesc( javaClass, fieldMaps[ i ] );
        return fields;
    }

    /**
     * Creates a single field descriptor. The field mapping is used to
     * create a new stock {@link FieldDescriptor}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldMap The field mapping information
     * @return The field descriptor
     * @throws MappingException The field or its accessor methods are not
     *  found, not accessible, not of the specified type, etc
     */
    protected FieldDescriptor createFieldDesc(Class javaClass, FieldMapping fieldMap)
    throws MappingException {
        String fieldName = fieldMap.getName();
        
        // If the field type is supplied, grab it and use it to locate the
        // field/accessor.
        Class fieldType = null;
        if ( fieldMap.getType() != null ) {
            try {
                fieldType = resolveType( fieldMap.getType() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( "mapping.classNotFound", fieldMap.getType() );
            }
        }
        
        // If the field is declared as a collection, grab the collection type as
        // well and use it to locate the field/accessor.
        CollectionHandler colHandler = null;
        if ( fieldMap.getCollection() != null ) {
            Class colType = CollectionHandlers.getCollectionType( fieldMap.getCollection().toString() );
            colHandler = CollectionHandlers.getHandler( colType );
        }

        TypeInfo typeInfo = getTypeInfo( fieldType, colHandler, fieldMap );
            
        ExtendedFieldHandler exfHandler = null;
        FieldHandler handler = null;
        
        //-- check for user supplied FieldHandler
        if (fieldMap.getHandler() != null) {
            
            Class handlerClass = null;
            try {
                handlerClass = resolveType( fieldMap.getHandler() );
            }
            catch (ClassNotFoundException except) {
                throw new MappingException( "mapping.classNotFound", fieldMap.getHandler() );
            }
            
            if (!FieldHandler.class.isAssignableFrom(handlerClass)) {
                String err = "The class '" + fieldMap.getHandler() + 
                    "' must implement " + FieldHandler.class.getName();
                throw new MappingException(err);
            }
            
            //-- get default constructor to invoke. We can't use the
            //-- newInstance method unfortunately becaue FieldHandler
            //-- overloads this method 
            Constructor constructor = null;
            try {
                constructor = handlerClass.getConstructor(new Class[0]);
                handler = (FieldHandler) 
                    constructor.newInstance(new Object[0]);
            }
            catch(java.lang.Exception except) {
                String err = "The class '" + handlerClass.getName() + 
                    "' must have a default public constructor.";
                throw new MappingException(err);
            }
            
            
            //-- ExtendedFieldHandler?
            if (handler instanceof ExtendedFieldHandler) {
                exfHandler = (ExtendedFieldHandler) handler;
            }
            
            //-- Fix for Castor JDO from Steve Vaughan, Castor JDO
            //-- requires FieldHandlerImpl or a ClassCastException
            //-- will be thrown... [KV 20030131 - also make sure this new handler 
            //-- doesn't use it's own CollectionHandler otherwise
            //-- it'll cause unwanted calls to the getValue method during
            //-- unmarshalling]
            colHandler = typeInfo.getCollectionHandler();
            typeInfo.setCollectionHandler(null);
            handler = new FieldHandlerImpl(handler, typeInfo);
            typeInfo.setCollectionHandler(colHandler);
            //-- End Castor JDO fix
            
        } 
        
        boolean generalized = (exfHandler instanceof GeneralizedFieldHandler);
        
        //-- if generalized we need to change the fieldType to whatever
        //-- is specified in the GeneralizedFieldHandler so that the
        //-- correct getter/setter methods can be found
        FieldHandler custom = handler;
        if (generalized) {
            fieldType = ((GeneralizedFieldHandler)exfHandler).getFieldType();
        }
        
        if (generalized || (handler == null)) {
            //-- create TypeInfoRef to get new TypeInfo from call
            //-- to createFieldHandler
            TypeInfoReference typeInfoRef = new TypeInfoReference();
            typeInfoRef.typeInfo = typeInfo;
            handler = createFieldHandler(javaClass, fieldType, fieldMap, typeInfoRef);
            if (custom != null) {
                ((GeneralizedFieldHandler)exfHandler).setFieldHandler(handler);
                handler = custom;
            }
            else {
                
                boolean isTypeSafeEnum = false;
            	//-- check for type-safe enum style classes
                if ((fieldType != null) && (!isPrimitive(fieldType))) {
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
                                
                                int mods = method.getModifiers();
                                if (Modifier.isStatic(mods)) {
                                    //-- Use EnumFieldHandler
                                    handler = new EnumFieldHandler(fieldType, handler, method);
                                    typeInfo.setImmutable(true);
                                    isTypeSafeEnum = true;
                                }
                            }
                        }
                    }
                    catch(NoSuchMethodException nsmx) {
                        //-- Do nothing
                    }
                }
                //-- reset proper TypeInfo
                if (!isTypeSafeEnum) typeInfo = typeInfoRef.typeInfo;

            }
            
        }
                
        FieldDescriptorImpl fieldDesc = new FieldDescriptorImpl(
                fieldName, typeInfo, handler, fieldMap.getTransient());

        fieldDesc.setRequired(fieldMap.getRequired());

        //-- If we're using an ExtendedFieldHandler we need to set the 
        //-- FieldDescriptor
        if (exfHandler != null)
            ((FieldHandlerFriend)exfHandler).setFieldDescriptor(fieldDesc);

        return fieldDesc;
    } //-- createFieldDesc

    /**
     * Creates the FieldHandler for the given FieldMapping
     *
     * @param javaClass the class type of the parent of the field
     * @param fieldType the Java class type for the field.
     * @param fieldMap  the field mapping
     * @return the newly created FieldHandler
     */
    protected final FieldHandler createFieldHandler(Class javaClass, Class fieldType,
            FieldMapping fieldMap, TypeInfoReference typeInfoRef)
    throws MappingException {
        
        //-- prevent introspection of transient fields
        if (fieldMap.getTransient()) {
        	return new TransientFieldHandler();
        }
        
        CollectionHandler colHandler         = null;
        Class             colType            = null;
        FieldHandlerImpl  handler            = null;
        Method            getMethod          = null;
        Method            setMethod          = null;
        Method[]          getSequence        = null;
        boolean           getSetCollection   = true;
        Method[]          setSequence        = null;
        
        String fieldName = fieldMap.getName();
        
        // If the field is declared as a collection, grab the collection type as
        // well and use it to locate the field/accessor.
        if ( fieldMap.getCollection() != null ) {
            colType = CollectionHandlers.getCollectionType( fieldMap.getCollection().toString() );
            colHandler = CollectionHandlers.getHandler( colType );
            getSetCollection = CollectionHandlers.isGetSetCollection( colType );
            if ( colType == Object[].class ) {
                if (fieldType == null) {
                    String error = "'type' is a required attribute for " + 
                        "field that are array collections: " + fieldName;
                    throw new MappingException(error);
                }
                Object obj = Array.newInstance(fieldType, 0);
                colType = obj.getClass();
            }
        }
        
        
        // If get/set methods not specified, use field names to determine them.
        if ( fieldMap.getDirect() ) {
            // No accessor, map field directly.
            Field field;

            field = findField( javaClass, fieldName, ( colType == null ? fieldType : colType ) );
            if ( field == null )
                throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
            if ( fieldType == null )
                fieldType = field.getType();
                
            typeInfoRef.typeInfo = getTypeInfo(fieldType, colHandler, fieldMap);
            
            handler = new FieldHandlerImpl( field, typeInfoRef.typeInfo );
        } 
        else {
            //-- if both methods (get/set) are not specified, then
            //-- automatically determine them.
            if ( fieldMap.getGetMethod() == null && fieldMap.getSetMethod() == null ) {
                int    point;
                Vector getSeq = new Vector();
                Vector setSeq = new Vector();
                String methodName;
                Method method;

                if ( fieldName == null )
                    throw new MappingException( "mapping.missingFieldName", javaClass.getName() );
                    
                //-- get method normally starts with "get", but
                //-- may start with "is" if it's a boolean.
                String getPrefix = GET_METHOD_PREFIX;

                try {
                    
                    //-- handle nested fields
                    while ( true ) {
                        
                        Class last;

                        point = fieldName.indexOf( '.' );
                        if ( point < 0 )
                            break;
                        last = javaClass;
                        
                        
                        // * getter for parent field *
                        String parentField = fieldName.substring(0, point);
                        methodName = GET_METHOD_PREFIX + capitalize( parentField );
                        method = javaClass.getMethod( methodName, (Class[]) null );                        
                        
                        fieldName = fieldName.substring( point + 1 );
                        
                        // Make sure method is not abstract/static
                        // (note: Class.getMethod() returns only public methods).
                        if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                             ( method.getModifiers() & Modifier.STATIC ) != 0 )
                            throw new MappingException( "mapping.accessorNotAccessible",
                                                        methodName, javaClass.getName() );
                        getSeq.addElement( method );
                        javaClass = method.getReturnType();
                        // setter;   Note: javaClass already changed, use "last"
                        methodName = "set" + methodName.substring(getPrefix.length());
                        try {
                            method = last.getMethod( methodName, new Class[] { javaClass } );
                            if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                                 ( method.getModifiers() & Modifier.STATIC ) != 0 )
                                method = null;
                        } catch ( Exception except ) {
                            method = null;
                        }
                        setSeq.addElement( method );
                    } //-- end of nested fields
                    
                    //-- save method-call sequence for nested fields
                    if ( getSeq.size() > 0 ) {
                        getSequence = new Method[ getSeq.size() ];
                        getSeq.copyInto( getSequence );
                        setSequence = new Method[ setSeq.size() ];
                        setSeq.copyInto( setSequence );
                    }
                    
                    
                    //-- find get-method for actual field
                    methodName = getPrefix + capitalize( fieldName );
                    Class returnType = (colType == null) ? fieldType : colType;
                    getMethod = findAccessor( javaClass, methodName, returnType, true);
                                          
                    //-- If getMethod is null, check for boolean type 
                    //-- method prefix might be "is".
                    if (getMethod == null) {
                        if ((fieldType == Boolean.class) || 
                            (fieldType == Boolean.TYPE)) 
                        {
                            getPrefix = IS_METHOD_PREFIX;
                            methodName = getPrefix + capitalize( fieldName );
                            getMethod = findAccessor(javaClass, methodName,
                                    returnType, true);
                        }
                    }
                } catch ( MappingException except ) {
                    throw except;
                } catch ( Exception except ) {
                	// log.warn ("Unexpected exception", except);
                }
                if ( getMethod == null )
                    throw new MappingException( "mapping.accessorNotFound",
                                                getPrefix + capitalize( fieldName ),
                                                ( colType == null ? fieldType : colType ),
                                                javaClass.getName() );
                if ( fieldType == null && colType == null )
                    fieldType = getMethod.getReturnType();


                // We try to locate a set method anyway and we complain only if we really need one.
                    setMethod = findAccessor( javaClass, "set" + capitalize( fieldName ),
                                              ( colType == null ? fieldType : colType ), false );

                // If we have a collection that need both set and get and that
                // we don't have a set method, we fail
                if ( setMethod == null && colType != null && getSetCollection )
                        throw new MappingException( "mapping.accessorNotFound",
                                                    "set" + capitalize( fieldName ),
                                                    ( colType == null ? fieldType : colType ),
                                                    javaClass.getName() );


            } else {
                // First look up the get accessors
                if ( fieldMap.getGetMethod() != null ) {
                    getMethod = findAccessor( javaClass, fieldMap.getGetMethod(),
                                              ( colType == null ? fieldType : colType ), true );
                    if ( getMethod == null )
                        throw new MappingException( "mapping.accessorNotFound",
                                                    fieldMap.getGetMethod(), ( colType == null ? fieldType : colType ),
                                                    javaClass.getName() );
                    if ( fieldType == null && colType == null )
                        fieldType = getMethod.getReturnType();
                }

                // Second look up the set/add accessor
                if ( fieldMap.getSetMethod() != null ) {
                    
                    String methodName = fieldMap.getSetMethod();
                    Class type = fieldType;
                    if (colType != null) {
                        if (!methodName.startsWith(ADD_METHOD_PREFIX))
                            type = colType;
                    }
                    
                    //-- set via constructor?
                    if (methodName.startsWith("%")) {
                        //-- validate index value
                        String sIdx = methodName.substring(1);
                        int index = 0;
                        try {
                            index = Integer.parseInt(sIdx);
                        }
                        catch(NumberFormatException nfe) {
                            throw new MappingException("mapping.invalidParameterIndex", sIdx);
                        }
                        if ((index < 1) || (index > 9)) {
                            throw new MappingException("mapping.invalidParameterIndex", sIdx);
                        }
                    }
                    else {
                        setMethod = findAccessor( javaClass, fieldMap.getSetMethod(),
                                                type , false );
                        if ( setMethod == null )
                            throw new MappingException( "mapping.accessorNotFound",
                                                        fieldMap.getSetMethod(), type,
                                                        javaClass.getName() );
                        if ( fieldType == null )
                            fieldType = setMethod.getParameterTypes()[ 0 ];
                    }
                }
            }

            typeInfoRef.typeInfo = getTypeInfo( fieldType, colHandler, fieldMap );
              
            fieldName = fieldMap.getName(); // Not the same for nested fields
            if ( fieldName == null )
                fieldName = ( getMethod == null ? setMethod.getName() : getMethod.getName() );

            //-- create handler
            handler = new FieldHandlerImpl( fieldName, 
                                            getSequence, 
                                            setSequence, 
                                            getMethod, 
                                            setMethod, 
                                            typeInfoRef.typeInfo );

            if ((setMethod != null) && (setMethod.getName().startsWith(ADD_METHOD_PREFIX)))
                handler.setAddMethod(setMethod);
        }

        // If there is a create method, add it to the field handler
        if ( fieldMap.getCreateMethod() != null ) {
            try {
                Method method;

                method = javaClass.getMethod( fieldMap.getCreateMethod(), (Class[]) null );
                handler.setCreateMethod( method );
            } catch ( Exception except ) {
                // No such/access to method
                throw new MappingException( "mapping.createMethodNotFound",
                                            fieldMap.getCreateMethod(), javaClass.getName() );
            }
        } else if ( fieldName != null && ! Types.isSimpleType( fieldType ) ) {
            try {
                Method method;

                method = javaClass.getMethod( "create" + capitalize( fieldName ), (Class[]) null );
                handler.setCreateMethod( method );
            } catch ( Exception except ) {
            	// log.warn ("Unexpected exception", except);
            }
        }

        // If there is an has/delete method, add them to field handler
        if ( fieldName != null ) {
            Method hasMethod = null;
            Method deleteMethod = null;

            try {
                if (fieldMap.getHasMethod() != null)
                    hasMethod = javaClass.getMethod( fieldMap.getHasMethod(), (Class[]) null );
                else 
                    hasMethod = javaClass.getMethod( "has" + capitalize( fieldName ), (Class[]) null );
                
                if ((hasMethod.getModifiers() & Modifier.STATIC ) != 0)
                    hasMethod = null;
                try {
                    deleteMethod = javaClass.getMethod( "delete" + capitalize( fieldName ), (Class[]) null );
                    if (( deleteMethod.getModifiers() & Modifier.STATIC ) != 0 )
                          deleteMethod = null;
                } 
                catch ( Exception except ) { 
                    //-- Purposely Ignore NoSuchMethodException
                    //-- we're just seeing if the method exists
                }
                handler.setHasDeleteMethod( hasMethod, deleteMethod );
            } 
            catch ( Exception except ) { 
            	// log.warn("Unexpected exception", except);
            }
        }
        
        return handler;
    } //-- createFieldHandler

    protected TypeInfo getTypeInfo(Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap)
    throws MappingException {
        return new TypeInfo(Types.typeFromPrimitive(fieldType), null, null, null,
                            fieldMap.getRequired(), null, colHandler, false);
    }

    /**
     * Returns the named field. Uses reflection to return the named
     * field and check the field type, if specified.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldName The name of the field
     * @param fieldType The type of the field if known, or null
     * @return The field, null if not found
     * @throws MappingException The field is not accessible or is not of the
     *  specified type
     */
    private final Field findField(Class javaClass, String fieldName, Class fieldType)
    throws MappingException {
        Field field;

        try {
            // Look up the field based on its name, make sure it's only modifier
            // is public. If a type was specified, match the field type.
            field = javaClass.getField( fieldName );
            if ( field.getModifiers() != Modifier.PUBLIC &&
                 field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
                throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
            if ( fieldType == null )
                fieldType = Types.typeFromPrimitive( field.getType() );
            else if ( fieldType != java.io.Serializable.class
                    && Types.typeFromPrimitive( fieldType ) != Types.typeFromPrimitive( field.getType() ) )
                throw new MappingException( "mapping.fieldTypeMismatch", field, fieldType.getName() );
            return field;
        } catch ( NoSuchFieldException except ) {
        } catch ( SecurityException except ) {
        }
        return null;
    }

    /**
     * Returns the named accessor. Uses reflection to return the named
     * accessor and check the return value or parameter type, if
     * specified.
     *
     * @param javaClass The class to which the field belongs
     * @param methodName The name of the accessor method
     * @param fieldType The type of the field if known, or null
     * @param getMethod True if get method, false if set method
     * @return The method, null if not found
     * @throws MappingException The method is not accessible or is not of the
     *  specified type
     */
    protected static final Method findAccessor(final Class javaClass, final String methodName,
            Class fieldType, final boolean getMethod) throws MappingException {
        Method   method;
        Method[] methods;
        Class[] parameterTypes;
        Class fieldTypeFromPrimitive;
        int      i;

        try {
            if (getMethod) {
                // Get method: look for the named method or prepend get to the
                // method name. Look up the field and potentially check the
                // return type.
                method = javaClass.getMethod(methodName, new Class[0]);

                if (fieldType == null) {
                    fieldType = Types.typeFromPrimitive(method.getReturnType());
                } else {
                    fieldType = Types.typeFromPrimitive(fieldType);
                    Class returnType = Types.typeFromPrimitive( method.getReturnType());
                    
                    //-- First check against whether the declared type is
                    //-- an interface or abstract class. We also check
                    //-- type as Serializable for CMP 1.1 compatibility.
                    if (fieldType.isInterface()
                            || ((fieldType.getModifiers() & Modifier.ABSTRACT) != 0)
                            || (fieldType == java.io.Serializable.class)) {
                        
                        if (!fieldType.isAssignableFrom(returnType)) {
                            throw new MappingException(
                                    "mapping.accessorReturnTypeMismatch",
                                    method, fieldType.getName());
                        }
                    } else {
                        if (!returnType.isAssignableFrom(fieldType)) {
                            throw new MappingException(
                                    "mapping.accessorReturnTypeMismatch",
                                    method, fieldType.getName());
                        }
                    }
                }                    
            } else {
                method = null;
                fieldTypeFromPrimitive = null;
                // Set method: look for the named method or prepend set to the
                // method name. If the field type is know, look up a suitable
                // method. If the fielf type is unknown, lookup the first
                // method with that name and one parameter.
                if (fieldType != null) {
                    fieldTypeFromPrimitive = Types.typeFromPrimitive(fieldType);
                    try {
                        method = javaClass.getMethod(methodName, new Class[] { fieldType });
                    } catch (Exception except) {
                        try {
                            method = javaClass.getMethod(methodName, new Class[] { fieldTypeFromPrimitive });
                        } catch ( Exception except2 ) {
                        	// log.warn ("Unexpected exception", except2);
                        }
                    }
                }
                if (null == method) {
                    methods = javaClass.getMethods();
                    method = null;
                    for (i = 0 ; i < methods.length ; ++i) {
                        if (methods[i].getName().equals(methodName)) {
                            parameterTypes = methods[i].getParameterTypes();
                            if (parameterTypes.length != 1) { continue; }
                            
                            Class paramType = Types.typeFromPrimitive(parameterTypes[0]);
                            
                            if ((fieldType == null)
                                    || paramType.isAssignableFrom(fieldTypeFromPrimitive)) {
                                //-- check straight match
                                method = methods[i];
                                break;
                            } else if (fieldType.isInterface()
                                    || ((fieldType.getModifiers() & Modifier.ABSTRACT) != 0)) {
                                //-- Check against whether the declared type is
                                //-- an interface or abstract class. 
                                if (fieldTypeFromPrimitive.isAssignableFrom(paramType)) {
                                    method = methods[i];
                                    break;
                                }
                            }
                        }
                    }
                    if (method == null) { return null; }
                }
            }
            // Make sure method is public and not static.
            // (note: Class.getMethod() returns only public methods).
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                throw new MappingException(
                        "mapping.accessorNotAccessible",
                        methodName, javaClass.getName());
            }
            return method;
        } catch (MappingException except) {
            throw except;
        } catch (Exception except) {
            //System.out.println(except.toString());
            return null;
        }
    }

    private static final String capitalize(final String name) {
        char first = name.charAt( 0 );
        if (Character.isUpperCase(first)) { return name; }
        return Character.toUpperCase(first) + name.substring(1);
    }
    
    /**
     * Returns a list of column names that are part of the identity.
     * @param ids Known identity names.
     * @param clsMap Class mapping.
     * @return List of identity column names.
     */
    public static final String[] getIdentityColumnNames(
            final String[] ids, final ClassMapping clsMap) {
        
        String[] identityColumnNames = ids;
        
        if (ids == null || ids.length == 0) {
            int identityCount = 0;
            ClassChoice classChoice = clsMap.getClassChoice();
            if (classChoice == null) { 
                classChoice = new ClassChoice(); 
            }
            FieldMapping[] fieldMappings = classChoice.getFieldMapping();
            for (int i = 0; i < fieldMappings.length; i++) {
                if (fieldMappings[i].getIdentity() == true) {
                    identityCount++;
                }
            }
    
            List identityDescriptorList = new ArrayList();
            if (identityCount > 0) {
                for (int i = 0; i < fieldMappings.length; i++) {
                    if (fieldMappings[i].getIdentity() == true) {
                        identityDescriptorList.add(fieldMappings[i].getName());
                    }
                }
                
                Iterator idIter = identityDescriptorList.iterator();
                int i = 0;
                identityColumnNames = new String[identityDescriptorList.size()]; 
                while (idIter.hasNext()) {
                    identityColumnNames[i] = (String) idIter.next();
                    i++;
                }
            }
        }
    
        return identityColumnNames;
    }

    /**
     * Returns true if the given class should be treated as a primitive
     * type
     * @return true if the given class should be treated as a primitive
     * type
     */
    protected static final boolean isPrimitive(final Class type) {
        if (type.isPrimitive()) { return true; }
        if ((type == Boolean.class) || (type == Character.class)) { return true; }
        return (type.getSuperclass() == Number.class);
    }

    /**
     * A class used to by the createFieldHandler method in order to
     * save the reference of the TypeInfo that was used.
     */
    public class TypeInfoReference {
        public TypeInfo typeInfo = null;
    }
} //-- MappingLoader
