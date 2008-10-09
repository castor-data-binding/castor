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
package org.exolab.castor.jdo.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cache.Cache;
import org.castor.cache.simple.CountLimited;
import org.castor.cache.simple.TimeLimited;
import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;
import org.castor.cpa.persistence.convertor.AbstractSimpleTypeConvertor;
import org.castor.cpa.persistence.convertor.TypeConvertorRegistry;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.mapping.BindingType;
import org.castor.util.Messages;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.ExtendedFieldHandler;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.FieldHandlerFriend;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.CacheTypeMapping;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.mapping.xml.NamedQuery;
import org.exolab.castor.mapping.xml.Param;
import org.exolab.castor.mapping.xml.Sql;
import org.exolab.castor.mapping.xml.types.SqlDirtyType;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.xml.util.ClassLoaderNature;
import org.exolab.castor.xml.util.ClassResolutionByFile;

/**
 * A JDO implementation of mapping helper. Creates JDO class descriptors
 * from the mapping file.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-13 07:37:49 -0600 (Thu, 13 Apr 2006) $
 */
public final class JDOMappingLoader extends AbstractMappingLoader {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(JDOMappingLoader.class);

    /** Separators between type name and parameter, e.g. "char[01]". */
    private static final char LEFT_PARAM_SEPARATOR = '[';

    /** Separators after parameter, e.g. "char[01]". */
    private static final char RIGHT_PARAM_SEPARATOR = ']';

    /**
     * Extracts parameter for type convertor from the SQL type definition of the
     * form "SQL_TYPE_NAME[PARAMETER]". If the type is not parameterized, returns
     * null.
     *
     * @param sqlTypeDef SQL type definition (e.g. char[01]).
     * @return Parameter (e.g. "01") or null if not parameterized.
     */
    public static String definition2param(final String sqlTypeDef) {
        int left = sqlTypeDef.indexOf(LEFT_PARAM_SEPARATOR);
        int right = sqlTypeDef.indexOf(RIGHT_PARAM_SEPARATOR);
        if (right < 0) {
            right = sqlTypeDef.length();
        }
        if (left < 0) {
            return null;
        }
        return sqlTypeDef.substring(left + 1, right);
    }

    /**
     * Extracts SQL type name from the the SQL type definition of the form
     * "SQL_TYPE_NAME[PARAMETER]".
     *
     * @param sqlTypeDef SQL type definition (e.g. char[01]).
     * @return SQL type name (e.g. "char").
     */
    public static String definition2type(final String sqlTypeDef) {
        int sep = sqlTypeDef.indexOf(LEFT_PARAM_SEPARATOR);
        if (sep < 0) {
            return sqlTypeDef;
        }
        return sqlTypeDef.substring(0, sep);
    }

    private final TypeConvertorRegistry _typeConvertorRegistry;

    /** 
     * Map of key generator descriptors associated by their name. 
     */
    private final Map < String, KeyGeneratorDescriptor > _keyGeneratorDescriptors
        = new HashMap < String, KeyGeneratorDescriptor > ();

    /** Used by the constructor for creating key generators. Each database must have a
     *  proprietary KeyGeneratorRegistry instance, otherwise it is impossible to
     *  implement stateful key generator algorithms like HIGH-LOW correctly. */
    private final KeyGeneratorRegistry _keyGenReg = new KeyGeneratorRegistry();

    /** Set of names of all named queries to identify duplicate names. */    
    private final Set _queryNames = new HashSet();

    /** 
     * The JDO {@link PersistenceFactory} is used for adjusting SQL type for
     * the given database. 
     */
    private PersistenceFactory _factory;

    /**
     * Creates an instance of {@link JDOMappingLoader}, providing a 
     * {@link ClassLoader} instance.
     * @param loader A Custom {@link ClassLoader} instance.
     */
    public JDOMappingLoader(final ClassLoader loader) {
        super(loader);

        Configuration config = CPAConfiguration.getInstance();
        _typeConvertorRegistry = new TypeConvertorRegistry(config);
    }
    
    /**
     * {@inheritDoc}
     */
    public BindingType getBindingType() { 
        return BindingType.JDO; 
    }

    /**
     * {@inheritDoc}
     */
    public void loadMapping(final MappingRoot mapping, final Object param)
    throws MappingException {
        if (loadMapping()) {
            _factory = (PersistenceFactory) param;
            
            createKeyGenDescriptors(mapping);
            createClassDescriptors(mapping);
        }
    }

    /**
     * Load key generator definitions, check for duplicate definitions and convert them
     * to key generator descriptors.
     * 
     * @param mapping Mapping to load key generator defintions from.
     * @throws MappingException If mapping contains more then one key generator
     *        definition with same name.
     */
    private void createKeyGenDescriptors(final MappingRoot mapping)
    throws MappingException {
        Enumeration enumeration = mapping.enumerateKeyGeneratorDef();
        while (enumeration.hasMoreElements()) {
            KeyGeneratorDef def = (KeyGeneratorDef) enumeration.nextElement();
            
            // resolve name of the key generator definition; if there's an alias
            // defined, use it; if not, fall back to the name attribute (this
            // is to ensure that for one key generator type (e.g. HIGH-LOW), it is 
            // possible to define more than one instance with individual
            // configuration(s).
            String name = def.getAlias();
            if (name == null) { 
                name = def.getName(); 
            }
            
            KeyGeneratorDescriptor desc = _keyGeneratorDescriptors.get(name);
            if (desc != null) {
                throw new MappingException(Messages.format("mapping.dupKeyGen", name));
            }
            
            Properties params = new Properties();
            
            Enumeration enumerateParam = def.enumerateParam();
            while (enumerateParam.hasMoreElements()) {
                Param par = (Param) enumerateParam.nextElement();
                params.put(par.getName(), par.getValue());
            }
            
            desc = new KeyGeneratorDescriptor(
                    name, def.getName(), params, _keyGenReg);
            
            _keyGeneratorDescriptors.put(name, desc);
        }
    }
    
    protected ClassDescriptor createClassDescriptor(final ClassMapping classMapping)
    throws MappingException {
        // If there is no SQL information for class, ignore it.
        if ((classMapping.getMapTo() == null) || (classMapping.getMapTo().getTable() == null)) {
            LOG.info(Messages.format("mapping.ignoringMapping", classMapping.getName()));
            return null;
        }

        // Create the class descriptor, and register the JDO nature with it.
        ClassDescriptorImpl clsDesc = new ClassDescriptorImpl();
        clsDesc.addNature(ClassDescriptorJDONature.class.getName());
        ClassDescriptorJDONature jdoNature = new ClassDescriptorJDONature(clsDesc);
        
        // Set reference to class mapping on class descriptor.
        clsDesc.setMapping(classMapping);
        
        // Obtain the Java class.
        Class javaClass = resolveType(classMapping.getName());
        if (!Types.isConstructable(javaClass, true)) {
            throw new MappingException(
                    "mapping.classNotConstructable", javaClass.getName());
        }
        clsDesc.setJavaClass(javaClass);
        
        // If this class extends another class, we need to obtain the extended
        // class and make sure this class indeed extends it.
        ClassDescriptor extDesc = getExtended(classMapping, javaClass);
        if (extDesc != null) {
            if (!(extDesc.hasNature(ClassDescriptorJDONature.class.getName()))) {
                throw new IllegalArgumentException(
                        "Extended class does not have a JDO descriptor");
            }
            
            new ClassDescriptorJDONature(extDesc).addExtended(clsDesc);
        }
        clsDesc.setExtends(extDesc);
        
        // If this class depends on another class, obtain the depended class.
        clsDesc.setDepends(getDepended(classMapping, javaClass));
        
        // Create all field descriptors.
        FieldDescriptorImpl[] allFields = createFieldDescriptors(classMapping, javaClass);

        // Make sure there are no two fields with the same name.
        checkFieldNameDuplicates(allFields, javaClass);

        // Set class descriptor containing the field
        for (int i = 0; i < allFields.length; i++) {
            allFields[i].setContainingClassDescriptor(clsDesc);
        }
        
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
                String[] idNames = classMapping.getIdentity();
                if ((idNames == null) || (idNames.length == 0)) {
                    // There are also no identity definitions on class.
                    throw new MappingException("mapping.noIdentity", javaClass.getName());
                }

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
            
            // Add all identities of extended class to identity list.
            FieldDescriptor[] extIds = ((ClassDescriptorImpl) extDesc).getIdentities();
            for (int i = 0; i < extIds.length; i++) { idList.add(extIds[i]); }
            
            // Search redefined identities in extending class.
            FieldDescriptor identity;
            for (int i = 0; i < idList.size(); i++) {
                String idName = ((FieldDescriptor) idList.get(i)).getFieldName();
                identity = findIdentityByName(fieldList, idName, javaClass);
                if (identity != null) { idList.set(i, identity); }
            }
        }
        
        // Set identities on class descriptor.
        FieldDescriptor[] ids = new FieldDescriptor[idList.size()];
        clsDesc.setIdentities(((FieldDescriptor[]) idList.toArray(ids)));

        // Set fields on class descriptor.
        FieldDescriptor[] fields = new FieldDescriptor[fieldList.size()];
        clsDesc.setFields((FieldDescriptor[]) fieldList.toArray(fields));
        
        jdoNature.setTableName(classMapping.getMapTo().getTable());
        
        extractAndSetAccessMode(jdoNature, classMapping);
        extractAndAddCacheParams(jdoNature, classMapping, javaClass);
        extractAndAddNamedQueries(jdoNature, classMapping);
        extractAndSetKeyGeneratorDescriptor(jdoNature, classMapping.getKeyGenerator());

        return clsDesc;
    }

    /**
     * Extract access mode from class mapping and set it at JDO class descriptor.
     * 
     * @param jdoNature JDO class descriptor to set the access mode on.
     * @param clsMap Class mapping to extract the access mode from.
     */
    private void extractAndSetAccessMode(final ClassDescriptorJDONature jdoNature,
            final ClassMapping clsMap) {
        if (clsMap.getAccess() != null) {
            jdoNature.setAccessMode(AccessMode.valueOf(clsMap.getAccess().toString()));
        }
    }
    
    /**
     * Extract cache parameters from class mapping and add them to JDO class descriptor.
     * 
     * @param jdoNature JDO class descriptor to add the cache parameters to.
     * @param clsMap Class mapping to extract the cache parameters from.
     * @param javaClass Class the cache parameters are defined for.
     * @throws MappingException If cache type <code>none</code> has been specified for
     *         a class that implements <code>TimeStampable</code> interface.
     */
    private void extractAndAddCacheParams(final ClassDescriptorJDONature jdoNature,
            final ClassMapping clsMap, final Class javaClass)
    throws MappingException {
        jdoNature.addCacheParam(Cache.PARAM_NAME, clsMap.getName());

        CacheTypeMapping cacheMapping = clsMap.getCacheTypeMapping();
        if (cacheMapping != null) {
            String type = cacheMapping.getType();
            jdoNature.addCacheParam(Cache.PARAM_TYPE, type);
            
            Param[] params = cacheMapping.getParam();
            for (int i = 0; i < params.length; i++) {
                jdoNature.addCacheParam(params[i].getName(), params[i].getValue());
            }

            String debug = new Boolean(cacheMapping.getDebug()).toString();
            jdoNature.addCacheParam(Cache.PARAM_DEBUG, debug);

            String capacity = Long.toString(cacheMapping.getCapacity());
            jdoNature.addCacheParam(CountLimited.PARAM_CAPACITY, capacity);
            jdoNature.addCacheParam(TimeLimited.PARAM_TTL, capacity);
        }
    }
    
    /**
     * Extract named queries from class mapping and add them to JDO class descriptor.
     * 
     * @param jdoNature JDO class descriptor to add the named queries to.
     * @param clsMap Class mapping to extract the named queries from.
     * @throws MappingException On duplicate query names.
     */
    private void extractAndAddNamedQueries(final ClassDescriptorJDONature jdoNature,
            final ClassMapping clsMap)
    throws MappingException {
        Enumeration namedQueriesEnum = clsMap.enumerateNamedQuery();
        while (namedQueriesEnum.hasMoreElements()) {
            NamedQuery query = (NamedQuery) namedQueriesEnum.nextElement();
            String queryName = query.getName();
            if (_queryNames.contains(queryName)) {
                throw new MappingException(
                        "Duplicate entry for named query with name " + queryName);
            }
            _queryNames.add(queryName);

            jdoNature.addNamedQuery(queryName, query.getQuery());
        }
    }
    
    /**
     * Resolves (or creates) the key generator from a given key generator name.
     * 
     * Search for an already existing key generator descriptor, e.g. those generated 
     * from the key generator definitions in mapping. If no descriptor can be found 
     * a new one is created and added to the map of class descriptors. Set the 
     * key generator descriptor at the class descriptor.
     * 
     * @param jdoNature JDO class descriptor to set the key generator descriptor at.
     * @param keyGeneratorName Name of the key generator.
     */
    private void extractAndSetKeyGeneratorDescriptor(final ClassDescriptorJDONature jdoNature,
            final String keyGeneratorName) {
        KeyGeneratorDescriptor keyGeneratorDescriptor = null;
        
        if (keyGeneratorName != null) {
            keyGeneratorDescriptor = _keyGeneratorDescriptors.get(keyGeneratorName);
            if (keyGeneratorDescriptor == null) {
                keyGeneratorDescriptor = new KeyGeneratorDescriptor(
                        keyGeneratorName, keyGeneratorName, new Properties(), _keyGenReg);
                _keyGeneratorDescriptors.put(keyGeneratorName, keyGeneratorDescriptor);
            }
        }
        
        jdoNature.setKeyGeneratorDescriptor(keyGeneratorDescriptor);
    }
    
    protected FieldDescriptor findIdentityByName(
            final List fldList, final String idName, final Class javaClass)
    throws MappingException {
        for (int i = 0; i < fldList.size(); i++) {
            FieldDescriptor field = (FieldDescriptor) fldList.get(i);
            if (idName.equals(field.getFieldName())) {
                if (!(field.hasNature(FieldDescriptorJDONature.class.getName()))) {
                    throw new IllegalStateException(
                            "Identity field must be of type JDOFieldDescriptor");
                }
                
                String[] sqlName = new FieldDescriptorJDONature(field).getSQLName();
                if (sqlName == null) {
                    throw new MappingException("mapping.noSqlName",
                            field.getFieldName(), javaClass.getName());
                }

                fldList.remove(i);
                return field;
            }
        }
        return null;
    }

    /**
     * Walks through all fields of a descriptor and resolves relation
     * {@link ClassDescriptor}s by using mapping information or, if not
     * present, resolution by file to support generated
     * {@link ClassDescriptor}s. Resolved {@link ClassDescriptor}s will be
     * set as a field's descriptor.
     * 
     * @param clsDesc
     *            The ClassDescriptor in focus.
     */
    protected void resolveRelations(final ClassDescriptor clsDesc) {
        FieldDescriptor[] fields = clsDesc.getFields();
        for (int i = 0; i < fields.length; ++i) {
            FieldDescriptor field = fields[i];
            ClassDescriptor desc = getDescriptor(field.getFieldType().getName());
            // Resolve ClassDescriptor from the file system as well.
            if (desc == null && !field.getFieldType().isPrimitive()) {
                ClassResolutionByFile resolutionCommand = new ClassResolutionByFile();
                resolutionCommand.addNature(ClassLoaderNature.class.getName());
                ClassLoaderNature clNature = new ClassLoaderNature(
                        resolutionCommand);
                clNature.setClassLoader(getClassLoader());
                desc = resolutionCommand.resolve(field.getFieldType());
                ((FieldDescriptorImpl) field).setClassDescriptor(desc);
            }
            if ((desc != null) && (field instanceof FieldDescriptorImpl)) {
                ((FieldDescriptorImpl) field).setClassDescriptor(desc);
            }
        }
    }

    /**
     * Parse the sql type attribute to build an
     * array of types, needed to support whitespace inside
     * parameterized types (see Bug 1045).
     */
    protected String[] getSqlTypes(final FieldMapping fieldMap) {
        if (fieldMap.getSql() == null) { return new String[0]; }
        String sqlType = fieldMap.getSql().getType();
        if (sqlType == null) { return new String[0]; }

        ArrayList types = new ArrayList();
        int current = 0;
        int begin = 0;
        int state = 0;
        while (current < sqlType.length()) {
            switch (state) {
            case 0:
                if (sqlType.charAt(current) == ' ') {
                    types.add(sqlType.substring(begin, current));
                    begin = current + 1;
                } else if (sqlType.charAt(current) == '[') {
                    state = 1;
                }
                break;
            case 1:
                if (sqlType.charAt(current) == ']') {
                    state = 0;
                }
                break;
            default:
                break;
            }
            current++;
        }
        types.add(sqlType.substring(begin, current));
        String[] result = new String[types.size()];
        return (String[]) types.toArray(result);
    }


    protected TypeInfo getTypeInfo(final Class fieldType, final CollectionHandler colHandler,
            final FieldMapping fieldMap) throws MappingException {
        Class internalFieldType = fieldType;
        TypeConvertor convertorTo = null;
        TypeConvertor convertorFrom = null;
        String        typeName = null;
        Class         sqlType = null;
        String        sqlParam = null;

        internalFieldType = Types.typeFromPrimitive(internalFieldType);
        String[] sqlTypes = getSqlTypes(fieldMap);

        if ((fieldMap.getSql() != null) && (sqlTypes.length > 0)) {
            //--TO Check
            typeName = sqlTypes[0];
            sqlType = SQLTypeInfos.sqlTypeName2javaType(definition2type(typeName));
            sqlParam = definition2param(typeName);
        } else {
            sqlType = internalFieldType;
        }
        if (_factory != null) {
            sqlType = _factory.adjustSqlType(sqlType);
        }
        if (internalFieldType != sqlType) {
            try {
                convertorTo = _typeConvertorRegistry.getConvertor(
                        sqlType, internalFieldType, sqlParam);
            } catch (MappingException ex) {
                boolean isTypeSafeEnum = false;
                //-- check for type-safe enum style classes
                if ((internalFieldType != null) && (!isPrimitive(internalFieldType))) {
                    //-- make sure no default constructor
                    Constructor cons = null;
                    try {
                        cons = internalFieldType.getConstructor(EMPTY_ARGS);
                        if (!Modifier.isPublic(cons.getModifiers())) {
                            cons = null;
                        }
                    } catch (NoSuchMethodException nsmx) {
                        //-- Do nothing
                    }
                    try {
                        if (cons == null) {
                            //-- make sure a valueOf factory method
                            //-- exists and no user specified handler exists
                            Method method = internalFieldType.getMethod(VALUE_OF, STRING_ARG);
                            Class returnType = method.getReturnType();
                            if ((returnType != null)
                                    && internalFieldType.isAssignableFrom(returnType)) {
                                int mods = method.getModifiers();
                                if (Modifier.isStatic(mods)) {
                                    // create individual SQLTypeConverter
                                    convertorTo = new EnumTypeConvertor(
                                            sqlType, internalFieldType, method);

                                    Types.addEnumType(internalFieldType);
                                    
                                    isTypeSafeEnum = true;
                                }
                            }
                        }
                    } catch (NoSuchMethodException nsmx) {
                        //-- Do nothing
                    }
                }
                if (!isTypeSafeEnum) {
                    throw new MappingException("mapping.noConvertor", sqlType.getName(),
                            internalFieldType.getName());
                }
            }
            convertorFrom = _typeConvertorRegistry.getConvertor(
                    internalFieldType, sqlType, sqlParam);
            
            if ((convertorTo != null) && (convertorFrom != null)) {
                Types.addConvertibleType(internalFieldType);
            }
        }
        return new TypeInfo(internalFieldType, convertorTo, convertorFrom,
                            fieldMap.getRequired(), null, colHandler);
    }
    
    private class EnumTypeConvertor extends AbstractSimpleTypeConvertor {
        private final Method _method;

        public EnumTypeConvertor(final Class < ? > fromType,
                final Class < ? > toType, final Method method) {
            super(fromType, toType);

            _method = method;
        }

        /**
         * {@inheritDoc}
         */
        public Object convert(final Object object) {
            try {
                return _method.invoke(toType(), new Object[] {(String) object});
            } catch (Exception ex) {
                return null;
            }
        }
    }


    protected FieldDescriptorImpl createFieldDesc(final Class javaClass,
            final FieldMapping fieldMap) throws MappingException {

        // If not an SQL field, return a stock field descriptor.
        Sql sql = fieldMap.getSql();
        if (sql == null) {
            return super.createFieldDesc(javaClass, fieldMap);
        }
        
        String fieldName = fieldMap.getName();
        
        // If the field type is supplied, grab it and use it to locate the
        // field/accessor.
        Class fieldType = null;
        if (fieldMap.getType() != null) {
            fieldType = resolveType(fieldMap.getType());
        }
        
        // If the field is declared as a collection, grab the collection type as
        // well and use it to locate the field/accessor.
        CollectionHandler colHandler = null;
        if (fieldMap.getCollection() != null) {
            Class colType = CollectionHandlers.getCollectionType(
                    fieldMap.getCollection().toString());
            colHandler = CollectionHandlers.getHandler(colType);
            
            if (colType.getName().equals("java.util.Iterator") && fieldMap.getLazy()) {
                String err = "Lazy loading not supported for collection type 'iterator'";
                throw new MappingException(err);
            }

            if (colType.getName().equals("java.util.Enumeration") && fieldMap.getLazy()) {
                String err = "Lazy loading not supported for collection type 'enumerate'";
                throw new MappingException(err);
            }
        }
        
        TypeInfo typeInfo = getTypeInfo(fieldType, colHandler, fieldMap);
            
        ExtendedFieldHandler exfHandler = null;
        FieldHandler handler = null;
        
        //-- check for user supplied FieldHandler
        if (fieldMap.getHandler() != null) {
            
            Class handlerClass = resolveType(fieldMap.getHandler());
            
            if (!FieldHandler.class.isAssignableFrom(handlerClass)) {
                String err = "The class '" + fieldMap.getHandler()
                    + "' must implement " + FieldHandler.class.getName();
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
            } catch (Exception except) {
                String err = "The class '" + handlerClass.getName()
                    + "' must have a default public constructor.";
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
            fieldType = ((GeneralizedFieldHandler) exfHandler).getFieldType();
        }
        
        if (generalized || (handler == null)) {
            //-- create TypeInfoRef to get new TypeInfo from call
            //-- to createFieldHandler
            TypeInfoReference typeInfoRef = new TypeInfoReference();
            typeInfoRef.typeInfo = typeInfo;
            handler = createFieldHandler(javaClass, fieldType, fieldMap, typeInfoRef);
            if (custom != null) {
                ((GeneralizedFieldHandler) exfHandler).setFieldHandler(handler);
                handler = custom;
            } else {
                typeInfo = typeInfoRef.typeInfo;
            }
        }
                
        String[] sqlName = sql.getName();

        String[] sqlTypes = getSqlTypes(fieldMap);

        int[] sqlTypeNum;
        if (sqlTypes.length > 0) {
            sqlTypeNum = new int[sqlTypes.length];
            for (int i = 0; i < sqlTypes.length; i++) {
                String sqlTypeString = definition2type(sqlTypes[i]);
                Class sqlType = SQLTypeInfos.sqlTypeName2javaType(sqlTypeString);
                if (_factory != null) { sqlType = _factory.adjustSqlType(sqlType); }
                sqlTypeNum[i] = SQLTypeInfos.javaType2sqlTypeNum(sqlType);
            }
        } else {
            Class sqlType = typeInfo.getFieldType();
            if (_factory != null) { sqlType = _factory.adjustSqlType(sqlType); }
            sqlTypeNum = new int[] {SQLTypeInfos.javaType2sqlTypeNum(sqlType)};
        }

        // create FieldDescriptor(Impl) instance, and apply JDO nature
        FieldDescriptorImpl fieldDescriptor = 
            new FieldDescriptorImpl(fieldName, typeInfo, handler, fieldMap.getTransient());
        fieldDescriptor.addNature(FieldDescriptorJDONature.class.getName());
        
        fieldDescriptor.setRequired(fieldMap.getRequired());

        // If we're using an ExtendedFieldHandler we need to set the FieldDescriptor
        if (exfHandler != null) {
            ((FieldHandlerFriend) exfHandler).setFieldDescriptor(fieldDescriptor);
        }

        // if SQL mapping declares transient 
        if (sql.getTransient()) {
            fieldDescriptor.setTransient(true);
        }
        
        FieldDescriptorJDONature fieldJdoNature = 
            new FieldDescriptorJDONature(fieldDescriptor);
        
        if (sqlName.length > 0) {
            fieldJdoNature.setSQLName(sqlName);
        }
        fieldJdoNature.setSQLType(sqlTypeNum);
        fieldJdoNature.setManyTable(sql.getManyTable());
        if (sql.getManyKey().length > 0) {
            fieldJdoNature.setManyKey(sql.getManyKey());
        }
        fieldJdoNature.setDirtyCheck(!SqlDirtyType.IGNORE.equals(sql.getDirty()));
        fieldJdoNature.setReadOnly(sql.getReadOnly());
        
        return fieldDescriptor;
    }

}
