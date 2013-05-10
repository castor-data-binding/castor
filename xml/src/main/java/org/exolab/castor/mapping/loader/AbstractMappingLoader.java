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
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */
package org.exolab.castor.mapping.loader;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;


import org.castor.xml.InternalContext;
import org.castor.xml.AbstractInternalContext;
import org.castor.core.util.Messages;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.ConfigurableFieldHandler;
import org.exolab.castor.mapping.ExtendedFieldHandler;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.mapping.handlers.EnumFieldHandler;
import org.exolab.castor.mapping.handlers.TransientFieldHandler;
import org.exolab.castor.mapping.xml.ClassChoice;
import org.exolab.castor.mapping.xml.FieldHandlerDef;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.Param;

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

    /** The prefix for the "add" method. */
    private static final String ADD_METHOD_PREFIX = "add";

    /** The prefix for an enumeration method. */
    private static final String ENUM_METHOD_PREFIX = "enum";

    /** The prefix for an enumeration method. */
    private static final String ITER_METHOD_PREFIX = "iterate";

    /** The standard prefix for the getter method. */
    private static final String GET_METHOD_PREFIX = "get";

    /** The prefix for the "is" method for booleans. */
    private static final String IS_METHOD_PREFIX = "is";

    /** The standard prefix for the setter method. */
    private static final String SET_METHOD_PREFIX = "set";

    /** The prefix for the "create" method. */
    private static final String CREATE_METHOD_PREFIX = "create";

    /** The prefix for the "has" method. */
    private static final String HAS_METHOD_PREFIX = "has";

    /** The prefix for the "delete" method. */
    private static final String DELETE_METHOD_PREFIX = "delete";

    /** Empty array of class types used for reflection. */
    protected static final Class[] EMPTY_ARGS = new Class[0];

    /** The string argument for the valueOf method, used for introspection when searching for
     *  type-safe enumeration style classes. */
    protected static final Class[] STRING_ARG = {String.class};

    /** Factory method name for type-safe enumerations. */
    protected static final String VALUE_OF = "valueOf";

    /**
     * The {@link AbstractInternalContext} is the centerpiece providing runtime configuration
     * and state information.
     */
    private InternalContext _internalContext;

    /** Map of field handlers associated by their name. */
    private final Map _fieldHandlers = new HashMap();

    /**
     * Constructs a new mapping helper. This constructor is used by a derived class.
     *
     * @param loader The class loader to use, null for the default
     */
    protected AbstractMappingLoader(final ClassLoader loader) {
        super(loader);
    }

    /**
     * {@inheritDoc}
     */
    public final String getSourceType() {
        return "CastorXmlMapping";
    }

    /**
     * Loads the mapping from the specified mapping object if not loaded previously.
     *
     * @param mapping The mapping information.
     * @param param Arbitrary parameter that can be used by subclasses.
     * @throws MappingException The mapping file is invalid.
     */
    public abstract void loadMapping(final MappingRoot mapping, final Object param)
    throws MappingException;
    
    /**
     * Load field handler definitions, check for duplicate definitions and 
     * instantiate the appropriate FieldHandler implementations.
     * 
     * @param mapping Mapping to load field handler definitions from.
     * @throws MappingException If mapping contains more then one field handler
     *        definition with same name.
     */
    protected void createFieldHandlers(final MappingRoot mapping)
    throws MappingException {
        Enumeration enumeration = mapping.enumerateFieldHandlerDef();
        while (enumeration.hasMoreElements()) {
            FieldHandlerDef def = (FieldHandlerDef) enumeration.nextElement();
            
            String name = def.getName();
            
            if (_fieldHandlers.containsKey(name)) {
                throw new MappingException(Messages.format("mapping.dupFieldHandler", name));
            }
            
            
            Class clazz = resolveType(def.getClazz());
            FieldHandler fieldHandler = null;
            try {
                if (!FieldHandler.class.isAssignableFrom(clazz)) {
                    throw new MappingException(Messages.format("mapping.classNotFieldHandler", 
                            name, def.getClazz()));
                }
                fieldHandler = (FieldHandler) clazz.newInstance();
                _fieldHandlers.put(name, fieldHandler);
            } catch (InstantiationException e) {
                throw new MappingException(e);
            } catch (IllegalAccessException e) {
                throw new MappingException(e);
            }
            
            // Add configuration data, if there is any
            configureFieldHandler(def, fieldHandler);
            
         }
    }

    /*
     * Checks if the field handler is configurable, and adds the configuration
     * data to the field handler if this is the case.
     */
    private void configureFieldHandler(final FieldHandlerDef def, final FieldHandler fieldHandler) 
    throws MappingException {
        
        // Gather the configuration data (parameters).
        Properties params = new Properties();            
        Enumeration enumerateParam = def.enumerateParam();
        while (enumerateParam.hasMoreElements()) {
            Param par = (Param) enumerateParam.nextElement();
            params.put(par.getName(), par.getValue());
        }
        
        // If there is configuration data, make sure that the field handler class
        // supports it.
        if (params.size() > 0) {
            if (!ConfigurableFieldHandler.class.isAssignableFrom(fieldHandler.getClass())) {
                throw new MappingException(Messages.format("mapping.classNotConfigurableFieldHandler", 
                        def.getName(), def.getClazz()));
            }
            
            // Pass the configuration data to the field handler.
            try {
                ((ConfigurableFieldHandler)fieldHandler).setConfiguration(params);
            } catch (ValidityException e) {
                throw new MappingException(Messages.format("mapping.invalidFieldHandlerConfig",
                        def.getName(), e.getMessage()), e);
            }
        }
    }
    
    protected final void createClassDescriptors(final MappingRoot mapping)
    throws MappingException {
        // Load the mapping for all the classes. This is always returned
        // in the same order as it appeared in the mapping file.
        Enumeration enumeration = mapping.enumerateClassMapping();

        List retryList = new ArrayList();
        while (enumeration.hasMoreElements()) {
            ClassMapping clsMap = (ClassMapping) enumeration.nextElement();
            try {
                ClassDescriptor clsDesc = createClassDescriptor(clsMap);
                if (clsDesc != null) { addDescriptor(clsDesc); }
            } catch (MappingException mx) {
                // save for later for possible out-of-order mapping files...
                retryList.add(clsMap);
                continue;
            }
        }

        // handle possible retries, for now we only loop once on the retries, but we
        // should change this to keep looping until we have no more success rate.
        for (Iterator i = retryList.iterator(); i.hasNext();) {
            ClassMapping clsMap = (ClassMapping) i.next();
            ClassDescriptor clsDesc = createClassDescriptor(clsMap);
            if (clsDesc != null) { addDescriptor(clsDesc); }
        }

        // iterate over all class descriptors and resolve relations between them
        for (Iterator i = descriptorIterator(); i.hasNext();) {
            resolveRelations((ClassDescriptor) i.next());
        }
    }

    protected abstract ClassDescriptor createClassDescriptor(final ClassMapping clsMap)
    throws MappingException;

    /**
     * Gets the ClassDescriptor the given <code>classMapping</code> extends.
     *
     * @param clsMap The ClassMapping to find the required descriptor for.
     * @param javaClass The name of the class that is checked (this is used for
     *        generating the exception).
     * @return The ClassDescriptor the given ClassMapping extends or
     *         <code>null</code> if the given ClassMapping does not extend
     *         any.
     * @throws MappingException If the given ClassMapping extends another
     *         ClassMapping but its descriptor could not be found.
     */
    protected final ClassDescriptor getExtended(final ClassMapping clsMap,
                                                final Class javaClass) throws MappingException {
        if (clsMap.getExtends() == null) { return null; }

        ClassMapping mapping = (ClassMapping) clsMap.getExtends();
        Class type = resolveType(mapping.getName());
        ClassDescriptor result = getDescriptor(type.getName());

        if (result == null) {
            throw new MappingException(
                    "mapping.extendsMissing", mapping, javaClass.getName());
        }

        if (!result.getJavaClass().isAssignableFrom(javaClass)) {
            throw new MappingException("mapping.classDoesNotExtend",
                    javaClass.getName(), result.getJavaClass().getName());
        }

        return result;
    }

    /**
     * Gets the ClassDescriptor the given <code>classMapping</code> depends
     * on.
     *
     * @param clsMap The ClassMapping to find the required ClassDescriptor for.
     * @param javaClass The name of the class that is checked (this is used for
     *        generating the exception).
     * @return The ClassDescriptor the given ClassMapping depends on or
     *         <code>null</code> if the given ClassMapping does not depend on
     *         any.
     * @throws MappingException If the given ClassMapping depends on another
     *         ClassMapping but its descriptor could not be found.
     */
    protected final ClassDescriptor getDepended(final ClassMapping clsMap,
                                                final Class javaClass) throws MappingException {
        if (clsMap.getDepends() == null) { return null; }

        ClassMapping mapping = (ClassMapping) clsMap.getDepends();
        Class type = resolveType(mapping.getName());
        ClassDescriptor result = getDescriptor(type.getName());

        if (result == null) {
            throw new MappingException(
                    "Depends not found: " + mapping + " " + javaClass.getName());
        }

        return result;
    }

    /**
     * Checks all given fields for name equality and throws a MappingException if at
     * least two fields have the same name.
     *
     * @param fields The fields to be checked.
     * @param cls Class that is checked (this is used for generating the exception).
     * @throws MappingException If at least two fields have the same name.
     */
    protected final void checkFieldNameDuplicates(final FieldDescriptor[] fields,
                                                  final Class cls) throws MappingException {
        for (int i = 0; i < fields.length - 1; i++) {
            String fieldName = fields[i].getFieldName();
            for (int j = i + 1; j < fields.length; j++) {
                if (fieldName.equals(fields[j].getFieldName())) {
                    throw new MappingException("The field " + fieldName
                            + " appears twice in the descriptor for " + cls.getName());
                }
            }
        }
    }

    protected abstract void resolveRelations(final ClassDescriptor clsDesc);

    //--------------------------------------------------------------------------

    /**
     * Returns the Java class for the named type. The type name can be one of the
     * accepted short names (e.g. <tt>integer</tt>) or the full Java class name (e.g.
     * <tt>java.lang.Integer</tt>). If the short name is used, the primitive type might
     * be returned.
     */
    protected final Class resolveType(final String typeName)
    throws MappingException {
        try {
            return Types.typeFromName(getClassLoader(), typeName);
        } catch (ClassNotFoundException ex) {
            throw new MappingException("mapping.classNotFound", typeName);
        }
    }

    /**
     * Create field descriptors. The class mapping information is used to create
     * descriptors for all the fields in the class, except for container fields.
     * Implementations may extend this method to create more suitable descriptors, or
     * create descriptors only for a subset of the fields.
     *
     * @param clsMap The class to which the fields belong.
     * @param javaClass The field mappings.
     * @throws MappingException An exception indicating why mapping for the class cannot
     *         be created.
     */
    protected final FieldDescriptorImpl[] createFieldDescriptors(final ClassMapping clsMap,
                                                                     final Class javaClass) throws MappingException {
        FieldMapping[] fldMap = null;

        if (clsMap.getClassChoice() != null) {
            fldMap = clsMap.getClassChoice().getFieldMapping();
        }

        if ((fldMap == null) || (fldMap.length == 0)) {
            return new FieldDescriptorImpl[0];
        }

        FieldDescriptorImpl[] fields = new FieldDescriptorImpl[fldMap.length];
        for (int i = 0; i < fldMap.length; i++) {
            fields[i] = createFieldDesc(javaClass, fldMap[i]);

            // set identity flag
            fields[i].setIdentity(fldMap[i].getIdentity());
        }

        return fields;
    }

    /**
     * Gets the top-most (i.e. without any further 'extends') extends of the given
     * <code>classMapping</code>.
     *
     * @param clsMap The ClassMapping to get the origin for.
     * @return The top-most extends of the given ClassMapping or the ClassMapping itself
     *         if it does not extend any other ClassMapping.
     */
    protected final ClassMapping getOrigin(final ClassMapping clsMap) {
        ClassMapping result = clsMap;

        while (result.getExtends() != null) {
            result = (ClassMapping) result.getExtends();
        }

        return result;
    }

    protected final FieldDescriptor[] divideFieldDescriptors(final FieldDescriptor[] fields,
            final String[] ids, final FieldDescriptor[] identities) {
        List fieldList = new ArrayList(fields.length);

        for (int i = 0; i < fields.length; i++) {
            FieldDescriptor field = fields[i];
            final int index = getIdColumnIndex(field, ids);
            if (index == -1) {
                // copy non identity field from list of fields.
                fieldList.add(field);
            } else {
                if (field instanceof FieldDescriptorImpl) {
                    ((FieldDescriptorImpl) field).setRequired(true);
                }
                if (field.getHandler() instanceof FieldHandlerImpl) {
                    ((FieldHandlerImpl) field.getHandler()).setRequired(true);
                }

                identities[index] = field;
            }
        }

        // convert regularFieldList into array
        FieldDescriptor[] result = new FieldDescriptor[fieldList.size()];
        return (FieldDescriptor[]) fieldList.toArray(result);
    }

    /**
     * Finds the index in the given <code>idColumnNames</code> that has the same name as
     * the given <code>field</code>.
     *
     * @param field The FieldDescriptor to find the column index for.
     * @param ids The id columnNames available.
     * @return The index of the id column name that matches the given field's name or
     *         <code>-1</code> if no such id column name exists.
     */
    protected int getIdColumnIndex(FieldDescriptor field, String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            if (field.getFieldName().equals(ids[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Creates a single field descriptor. The field mapping is used to create a new stock
     * {@link FieldDescriptor}. Implementations may extend this class to create a more
     * suitable descriptor.
     *
     * @param javaClass The class to which the field belongs.
     * @param fieldMap The field mapping information.
     * @return The field descriptor.
     * @throws MappingException The field or its accessor methods are not
     *         found, not accessible, not of the specified type, etc.
     */
    protected FieldDescriptorImpl createFieldDesc(final Class javaClass,
                                                      final FieldMapping fieldMap) throws MappingException {
        String fieldName = fieldMap.getName();

        // If the field type is supplied, grab it and use it to locate the field/accessor.
        Class fieldType = null;
        if (fieldMap.getType() != null) {
            fieldType = resolveType(fieldMap.getType());
        }

        // If the field is declared as a collection, grab the collection type as
        // well and use it to locate the field/accessor.
        CollectionHandler colHandler = null;
        if (fieldMap.getCollection() != null) {
            String colTypeName = fieldMap.getCollection().toString();
            Class colType = CollectionHandlers.getCollectionType(colTypeName);
            colHandler = CollectionHandlers.getHandler(colType);
        }

        TypeInfo typeInfo = getTypeInfo(fieldType, colHandler, fieldMap);

        ExtendedFieldHandler exfHandler = null;
        FieldHandler handler = null;

        // Check for user supplied FieldHandler
        if (fieldMap.getHandler() != null) {
            handler = getFieldHandler(fieldMap);

            // ExtendedFieldHandler?
            if (handler instanceof ExtendedFieldHandler) {
                exfHandler = (ExtendedFieldHandler) handler;
            }

            // Fix for CastorJDO from Steve Vaughan, CastorJDO requires FieldHandlerImpl
            // or a ClassCastException will be thrown... [KV 20030131 - also make sure
            // this new handler doesn't use it's own CollectionHandler otherwise it'll
            // cause unwanted calls to the getValue method during unmarshalling]
            colHandler = typeInfo.getCollectionHandler();
            typeInfo.setCollectionHandler(null);
            handler = new FieldHandlerImpl(handler, typeInfo);
            typeInfo.setCollectionHandler(colHandler);
            // End Castor JDO fix
        }

        boolean generalized = (exfHandler instanceof GeneralizedFieldHandler);

        // If generalized we need to change the fieldType to whatever is specified in the
        // GeneralizedFieldHandler so that the correct getter/setter methods can be found
        if (generalized) {
            fieldType = ((GeneralizedFieldHandler)exfHandler).getFieldType();
        }

        if (generalized || (handler == null)) {
            // Create TypeInfoRef to get new TypeInfo from call to createFieldHandler
            FieldHandler custom = handler;
            TypeInfoReference typeInfoRef = new TypeInfoReference();
            typeInfoRef.typeInfo = typeInfo;
            handler = createFieldHandler(javaClass, fieldType, fieldMap, typeInfoRef);
            if (custom != null) {
                ((GeneralizedFieldHandler) exfHandler).setFieldHandler(handler);
                handler = custom;
            } else {
                boolean isTypeSafeEnum = false;
                // Check for type-safe enum style classes
                if ((fieldType != null) && !isPrimitive(fieldType)) {
                    if (!hasPublicDefaultConstructor(fieldType)) {
                        Method method = getStaticValueOfMethod(fieldType);
                        if (method != null) {
                            handler = new EnumFieldHandler(fieldType, handler, method);
                            typeInfo.setImmutable(true);
                            isTypeSafeEnum = true;
                        }
                    }
                }
                // Reset proper TypeInfo
                if (!isTypeSafeEnum) { typeInfo = typeInfoRef.typeInfo; }
            }
        }

        FieldDescriptorImpl fieldDesc = new FieldDescriptorImpl(
                fieldName, typeInfo, handler, fieldMap.getTransient());

        fieldDesc.setRequired(fieldMap.getRequired());

        // If we're using an ExtendedFieldHandler we need to set the FieldDescriptor
        if (exfHandler != null) {
            ((FieldHandlerFriend) exfHandler).setFieldDescriptor(fieldDesc);
        }

        return fieldDesc;
    }

    private FieldHandler getFieldHandler(final FieldMapping fieldMap) 
    throws MappingException {
        
        // If there is a custom field handler present in the mapping, that one
        // is returned.
        FieldHandler handler = (FieldHandler) _fieldHandlers.get(fieldMap.getHandler());
        if (handler != null) {
        	return handler;
        }
        
        Class handlerClass = null;
        handlerClass = resolveType(fieldMap.getHandler());

        if (!FieldHandler.class.isAssignableFrom(handlerClass)) {
            String err = "The class '" + fieldMap.getHandler() + "' must implement "
                       + FieldHandler.class.getName();
            throw new MappingException(err);
        }

        // Get default constructor to invoke. We can't use the newInstance method
        // unfortunately becaue FieldHandler overloads this method
        try {
            Constructor constructor = handlerClass.getConstructor(new Class[0]);
            return (FieldHandler) constructor.newInstance(new Object[0]);
        } catch (Exception ex) {
            String err = "The class '" + handlerClass.getName()
                       + "' must have a default public constructor.";
            throw new MappingException(err);
        }
    }

    /**
     * Does the given class has a public default constructor?
     *
     * @param type Class to check for a public default constructor.
     * @return <code>true</code> if class has a public default constructor.
     */
    private boolean hasPublicDefaultConstructor(final Class type) {
        try {
            Constructor cons = type.getConstructor(EMPTY_ARGS);
            return Modifier.isPublic(cons.getModifiers());
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    /**
     * Get static valueOf(String) factory method of given class.
     *
     * @param type Class to check for a static valueOf(String) factory method.
     * @return Static valueOf(String) factory method or <code>null</code> if none could
     *         be found.
     */
    private Method getStaticValueOfMethod(final Class type) {
        try {
            Method method = type.getMethod(VALUE_OF, STRING_ARG);
            Class returnType = method.getReturnType();
            if (returnType == null) { return null; }
            if (!type.isAssignableFrom(returnType)) { return null; }
            if (!Modifier.isStatic(method.getModifiers())) { return null; }
            return method;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    /**
     * Creates the FieldHandler for the given FieldMapping.
     *
     * @param javaClass the class type of the parent of the field.
     * @param fldType the Java class type for the field.
     * @param fldMap the field mapping.
     * @return the newly created FieldHandler.
     */
    protected final FieldHandler createFieldHandler(Class javaClass, Class fldType,
                                                    final FieldMapping fldMap,
                                                    final TypeInfoReference typeInfoRef) throws MappingException {
        // Prevent introspection of transient fields
        if (fldMap.getTransient()) {
            return new TransientFieldHandler();
        }

        Class colType = null;
        CollectionHandler colHandler = null;
        boolean colRequireGetSet = true;

        String fieldName = fldMap.getName();

        // If the field is declared as a collection, grab the collection type as
        // well and use it to locate the field/accessor.
        if (fldMap.getCollection() != null) {
            String colTypeName = fldMap.getCollection().toString();
            colType = CollectionHandlers.getCollectionType(colTypeName);
            colHandler = CollectionHandlers.getHandler(colType);
            colRequireGetSet = CollectionHandlers.isGetSetCollection(colType);
            if (colType == Object[].class) {
                if (fldType == null) {
                    String msg = "'type' is a required attribute for field that are "
                               + "array collections: " + fieldName;
                    throw new MappingException(msg);
                }
                Object obj = Array.newInstance(fldType, 0);
                colType = obj.getClass();
            }
        }


        FieldHandlerImpl  handler            = null;

        // If get/set methods not specified, use field names to determine them.
        if (fldMap.getDirect()) {
            // No accessor, map field directly.
            Field field = findField(javaClass, fieldName, (colType == null ? fldType : colType));
            if (field == null) {
                throw new MappingException(
                        "mapping.fieldNotAccessible", fieldName, javaClass.getName());
            }
            if (fldType == null) {
                fldType = field.getType();
            }

            typeInfoRef.typeInfo = getTypeInfo(fldType, colHandler, fldMap);

            handler = new FieldHandlerImpl(field, typeInfoRef.typeInfo);
        } else if ((fldMap.getGetMethod() == null) && (fldMap.getSetMethod() == null)) {
            // If both methods (get/set) are not specified, determine them automatically
            if (fieldName == null) {
                throw new MappingException(
                        "mapping.missingFieldName", javaClass.getName());
            }

            List getSequence = new ArrayList();
            List setSequence = new ArrayList();
            Method getMethod = null;
            Method setMethod = null;

            // Get method normally starts with "get", but may start with "is"
            // if it's a boolean.
            try {
                // Handle nested fields
                while (true) {
                    int point = fieldName.indexOf('.');
                    if (point < 0) { break; }

                    String parentField = fieldName.substring(0, point);

                    // Getter method for parent field
                    String methodName = GET_METHOD_PREFIX + capitalize(parentField);
                    Method method = javaClass.getMethod(methodName, (Class[]) null);
                    if (isAbstractOrStatic(method)) {
                        throw new MappingException("mapping.accessorNotAccessible",
                                methodName, javaClass.getName());
                    }
                    getSequence.add(method);

                    Class nextClass = method.getReturnType();

                    // Setter method for parent field
                    try {
                        methodName = SET_METHOD_PREFIX + capitalize(parentField);
                        Class[] types = new Class[] {nextClass};
                        method = javaClass.getMethod(methodName, types);
                        if (isAbstractOrStatic(method)) { method = null; }
                    } catch (Exception ex) {
                        method = null;
                    }
                    setSequence.add(method);

                    javaClass = nextClass;
                    fieldName = fieldName.substring(point + 1);
                }

                // Find getter method for actual field
                String methodName = GET_METHOD_PREFIX + capitalize( fieldName );
                Class returnType = (colType == null) ? fldType : colType;
                getMethod = findAccessor(javaClass, methodName, returnType, true);

                // If getMethod is null, check for boolean type method prefix
                if (getMethod == null) {
                    if ((fldType == Boolean.class) || (fldType == Boolean.TYPE)) {
                        methodName = IS_METHOD_PREFIX + capitalize(fieldName);
                        getMethod = findAccessor(javaClass, methodName, returnType, true);
                    }
                }
            } catch (MappingException ex) {
                throw ex;
            } catch (Exception ex) {
                // LOG.warn("Unexpected exception", ex);
            }

            if (getMethod == null) {
                String getAccessor = GET_METHOD_PREFIX + capitalize(fieldName);
                String isAccessor = IS_METHOD_PREFIX + capitalize(fieldName);
                throw new MappingException("mapping.accessorNotFound",
                        getAccessor + "/" + isAccessor,
                        (colType == null ? fldType : colType),
                        javaClass.getName());
            }

            if ((fldType == null) && (colType == null)) {
                fldType = getMethod.getReturnType();
            }

            // We try to locate a set method anyway but complain only if we need one
            String methodName = SET_METHOD_PREFIX + capitalize(fieldName);
            setMethod = findAccessor(javaClass, methodName,
                    (colType == null ? fldType : colType), false);

            // If we have a collection that need both set and get but we don't have a
            // set method, we fail
            if ((setMethod == null) && (colType != null) && colRequireGetSet) {
                throw new MappingException("mapping.accessorNotFound", methodName,
                        (colType == null ? fldType : colType), javaClass.getName());
            }

            typeInfoRef.typeInfo = getTypeInfo(fldType, colHandler, fldMap);

            fieldName = fldMap.getName();
            if (fieldName == null) {
                if (getMethod == null) {
                    fieldName = setMethod.getName();
                } else {
                    fieldName = getMethod.getName();
                }
            }

            // Convert method call sequence for nested fields to arrays
            Method[] getArray = null;
            Method[] setArray = null;
            if (getSequence.size() > 0) {
                getArray = new Method[getSequence.size()];
                getArray = (Method[]) getSequence.toArray(getArray);
                setArray = new Method[setSequence.size()];
                setArray = (Method[]) setSequence.toArray(setArray);
            }

            // Create handler
            handler = new FieldHandlerImpl(fieldName, getArray, setArray,
                    getMethod, setMethod, typeInfoRef.typeInfo);

            if (setMethod != null) {
                if (setMethod.getName().startsWith(ADD_METHOD_PREFIX)) {
                    handler.setAddMethod(setMethod);
                }
            }
        } else {
            Method getMethod = null;
            Method setMethod = null;

            // First look up the get accessors
            if (fldMap.getGetMethod() != null) {
                Class rtype = fldType;
                if (colType != null) {
                    String methodName = fldMap.getGetMethod();
                    if (methodName.startsWith(ENUM_METHOD_PREFIX)) {
                        // An enumeration method must really return a enumeration.
                        rtype = Enumeration.class;
                    } else if (methodName.startsWith(ITER_METHOD_PREFIX)) {
                        // An iterator method must really return a iterator.
                        rtype = Iterator.class;
                    } else {
                        rtype = colType;
                    }
                }

                getMethod = findAccessor(javaClass, fldMap.getGetMethod(), rtype, true);
                if (getMethod == null) {
                    throw new MappingException("mapping.accessorNotFound",
                            fldMap.getGetMethod(), rtype, javaClass.getName());
                }

                if ((fldType == null) && (colType == null)) {
                    fldType = getMethod.getReturnType();
                }
            }

            // Second look up the set/add accessor
            if (fldMap.getSetMethod() != null) {
                String methodName = fldMap.getSetMethod();
                Class type = fldType;
                if (colType != null) {
                    if (!methodName.startsWith(ADD_METHOD_PREFIX)) {
                        type = colType;
                    }
                }

                // Set via constructor?
                if (methodName.startsWith("%")) {
                    // Validate index value
                    int index = 0;

                    String temp = methodName.substring(1);
                    try {
                        index = Integer.parseInt(temp);
                    } catch(NumberFormatException ex) {
                        throw new MappingException("mapping.invalidParameterIndex", temp);
                    }

                    if ((index < 1) || (index > 9)) {
                        throw new MappingException("mapping.invalidParameterIndex", temp);
                    }
                } else {
                    setMethod = findAccessor(javaClass, methodName, type , false);
                    if (setMethod == null) {
                        throw new MappingException("mapping.accessorNotFound",
                                methodName, type, javaClass.getName());
                    }

                    if (fldType == null) {
                        fldType = setMethod.getParameterTypes()[0];
                    }
                }
            }

            typeInfoRef.typeInfo = getTypeInfo(fldType, colHandler, fldMap);

            fieldName = fldMap.getName();
            if (fieldName == null) {
                if (getMethod == null) {
                    fieldName = setMethod.getName();
                } else {
                    fieldName = getMethod.getName();
                }
            }

            // Create handler
            handler = new FieldHandlerImpl(fieldName, null, null,
                    getMethod, setMethod, typeInfoRef.typeInfo);

            if (setMethod != null) {
                if (setMethod.getName().startsWith(ADD_METHOD_PREFIX)) {
                    handler.setAddMethod(setMethod);
                }
            }
        }

        // If there is a create method, add it to the field handler
        String methodName = fldMap.getCreateMethod();
        if (methodName != null) {
            try {
                Method method = javaClass.getMethod(methodName, (Class[]) null);
                handler.setCreateMethod(method);
            } catch (Exception ex) {
                throw new MappingException("mapping.createMethodNotFound",
                        methodName, javaClass.getName());
            }
        } else if ((fieldName != null) && !Types.isSimpleType(fldType)) {
            try {
                methodName = CREATE_METHOD_PREFIX + capitalize(fieldName);
                Method method = javaClass.getMethod(methodName, (Class[]) null);
                handler.setCreateMethod(method);
            } catch (Exception ex) {
                // LOG.warn ("Unexpected exception", ex);
            }
        }

        // If there is an has/delete method, add them to field handler
        if (fieldName != null) {
            try {
                methodName = fldMap.getHasMethod();
                if (methodName == null) {
                    methodName = HAS_METHOD_PREFIX + capitalize(fieldName);
                }
                Method hasMethod = javaClass.getMethod(methodName, (Class[]) null);

                if ((hasMethod.getModifiers() & Modifier.STATIC) != 0) {
                    hasMethod = null;
                }

                Method deleteMethod = null;
                try {
                    methodName = DELETE_METHOD_PREFIX + capitalize(fieldName);
                    deleteMethod = javaClass.getMethod(methodName, (Class[]) null );
                    if ((deleteMethod.getModifiers() & Modifier.STATIC) != 0) {
                        deleteMethod = null;
                    }
                } catch (Exception ex) {
                    // Purposely Ignore exception we're just seeing if the method exists
                }

                handler.setHasDeleteMethod(hasMethod, deleteMethod);
            } catch (Exception ex) {
                // LOG.warn("Unexpected exception", ex);
            }
        }

        return handler;
    }

    private static boolean isAbstract(final Class cls) {
        return ((cls.getModifiers() & Modifier.ABSTRACT) != 0);
    }

    private static boolean isAbstractOrStatic(final Method method) {
        return ((method.getModifiers() & Modifier.ABSTRACT) != 0)
            || ((method.getModifiers() & Modifier.STATIC) != 0);
    }

    protected TypeInfo getTypeInfo(final Class fieldType,
                                   final CollectionHandler colHandler,
                                   final FieldMapping fieldMap) throws MappingException {
        return new TypeInfo(Types.typeFromPrimitive(fieldType), null, null,
                            fieldMap.getRequired(), null, colHandler, false);
    }

    /**
     * Returns the named field. Uses reflection to return the named field and check the
     * field type, if specified.
     *
     * @param javaClass The class to which the field belongs.
     * @param fieldName The name of the field.
     * @param fieldType The type of the field if known, or null.
     * @return The field, null if not found.
     * @throws MappingException The field is not accessible or is not of the
     *         specified type.
     */
    private final Field findField(final Class javaClass, final String fieldName,
                                  Class fieldType) throws MappingException {
        try {
            // Look up the field based on its name, make sure it's only modifier
            // is public. If a type was specified, match the field type.
            Field field = javaClass.getField(fieldName);
            if ((field.getModifiers() != Modifier.PUBLIC)
                    && (field.getModifiers() != (Modifier.PUBLIC | Modifier.VOLATILE))) {
                throw new MappingException(
                        "mapping.fieldNotAccessible", fieldName, javaClass.getName());
            }

            if (fieldType == null) {
                fieldType = Types.typeFromPrimitive(field.getType());
            } else {
                Class ft1 = Types.typeFromPrimitive(fieldType);
                Class ft2 = Types.typeFromPrimitive(field.getType());
                if ((ft1 != ft2) && (fieldType != Serializable.class)) {
                    throw new MappingException(
                            "mapping.fieldTypeMismatch", field, fieldType.getName());
                }
            }
            return field;
        } catch (NoSuchFieldException ex) {
            return null;
        } catch (SecurityException ex) {
            return null;
        }
    }

    /**
     * Returns the named accessor. Uses reflection to return the named accessor and
     * check the return value or parameter type, if specified.
     *
     * @param javaClass The class to which the field belongs.
     * @param methodName The name of the accessor method.
     * @param fieldType The type of the field if known, or null.
     * @param getMethod True if get method, false if set method.
     * @return The method, null if not found.
     * @throws MappingException The method is not accessible or is not of the
     *         specified type.
     */
    public static final Method findAccessor(final Class javaClass,
                                            final String methodName, Class fieldType,
                                            final boolean getMethod) throws MappingException {
        try {
            Method method = null;

            if (getMethod) {
                // Get method: look for the named method or prepend get to the method
                // name. Look up the field and potentially check the return type.
                method = javaClass.getMethod(methodName, new Class[0]);

                // The MapItem is used to handle the contents of maps. Since the MapItem
                // has to use Object for its methods we cannot (but also don't have to)
                // check for correct types.
                if (javaClass == MapItem.class) {
                    if (methodName.equals("getKey")) { return method; }
                    if (methodName.equals("getValue")) { return method; }
                }

                if (fieldType == null) {
                    fieldType = Types.typeFromPrimitive(method.getReturnType());
                } else {
                    fieldType = Types.typeFromPrimitive(fieldType);
                    Class returnType = Types.typeFromPrimitive(method.getReturnType());

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
                // Set method: look for the named method or prepend set to the method
                // name. If the field type is know, look up a suitable method. If the
                // field type is unknown, lookup the first method with that name and
                // one parameter.
                Class fieldTypePrimitive = null;
                if (fieldType != null) {
                    fieldTypePrimitive = Types.typeFromPrimitive(fieldType);
                    try {
                        method = javaClass.getMethod(methodName, new Class[] {fieldType});
                    } catch (Exception ex) {
                        try {
                            method = javaClass.getMethod(
                                    methodName, new Class[] {fieldTypePrimitive});
                        } catch (Exception ex2) {
                            // LOG.warn("Unexpected exception", ex2);
                        }
                    }

                    /* Replace above try catch block with the following one to resolve
                     * CASTOR-1141 for jdo part. After this change you can use this method
                     * also in FieldMolder and remove its findAccessor() method to omit
                     * code duplication. Having said that this introduces a problem
                     * with xmlctf that have to resolved first.
                    // first check for setter with reference type (e.g. setXxx(Integer))
                    try {
                        method = javaClass.getMethod(methodName, new Class[] {fieldTypePrimitive});
                    } catch (Exception ex) {
                        // if setter for reference type could not be found
                        // try to find one for primitive type (e.g. setXxx(int))
                        try {
                            method = javaClass.getMethod(methodName, new Class[] {fieldType});
                        } catch (Exception ex2) {
                            // LOG.warn("Unexpected exception", ex2);
                        }
                    }
                    */
                }

                if (method == null) {
                    Method[] methods = javaClass.getMethods();
                    for (int i = 0; i < methods.length; ++i) {
                        if (methods[i].getName().equals(methodName)) {
                            Class[] paramTypes = methods[i].getParameterTypes();
                            if (paramTypes.length != 1) { continue; }

                            Class paramType = Types.typeFromPrimitive(paramTypes[0]);

                            if (fieldType == null) {
                                method = methods[i];
                                break;
                            } else if (paramType.isAssignableFrom(fieldTypePrimitive)) {
                                method = methods[i];
                                break;
                            } else if (fieldType.isInterface() || isAbstract(fieldType)) {
                                if (fieldTypePrimitive.isAssignableFrom(paramType)) {
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
                        "mapping.accessorNotAccessible", methodName, javaClass.getName());
            }
            return method;
        } catch (MappingException ex) {
            throw ex;
        } catch (Exception ex) {
            return null;
        }
    }

    private static final String capitalize(final String name) {
        char first = name.charAt(0);
        if (Character.isUpperCase(first)) { return name; }
        return Character.toUpperCase(first) + name.substring(1);
    }

    /**
     * Returns a list of column names that are part of the identity.
     *
     * @param ids Known identity names.
     * @param clsMap Class mapping.
     * @return List of identity column names.
     */
    public static final String[] getIdentityColumnNames(final String[] ids,
                                                        final ClassMapping clsMap) {

        String[] idNames = ids;

        if ((ids == null) || (ids.length == 0)) {
            ClassChoice classChoice = clsMap.getClassChoice();
            if (classChoice == null) { classChoice = new ClassChoice(); }

            FieldMapping[] fieldMappings = classChoice.getFieldMapping();

            List idNamesList = new ArrayList();
            for (int i = 0; i < fieldMappings.length; i++) {
                if (fieldMappings[i].getIdentity() == true) {
                    idNamesList.add(fieldMappings[i].getName());
                }
            }

            if (idNamesList.size() > 0) {
                idNames = new String[idNamesList.size()];
                idNames = (String[]) idNamesList.toArray(idNames);
            }
        }

        return idNames;
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

    public void setInternalContext(final InternalContext internalContext) {
        _internalContext = internalContext;
    }
    
    public InternalContext getInternalContext() {
        return _internalContext;
    }

}
