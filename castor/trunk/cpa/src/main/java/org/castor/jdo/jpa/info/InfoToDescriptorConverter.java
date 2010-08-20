/*
 * Copyright 2008 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.jdo.jpa.info;

import java.util.Map;

import java.util.Properties;
import java.lang.reflect.Method;

import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.castor.cpa.persistence.convertor.ObjectToString;
import org.castor.cpa.persistence.convertor.EnumToOrdinal;
import org.castor.cpa.persistence.convertor.EnumTypeConvertor;
import org.castor.cpa.persistence.convertor.EnumTypeConversionHelper;
import org.castor.cpa.persistence.sql.keygen.TableKeyGenerator;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.exolab.castor.jdo.engine.KeyGeneratorDescriptor;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.ClassChoice;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.Sql;
import org.exolab.castor.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.ResolverException;

/**
 * <p>
 * This Class converts a {@link ClassInfo} and its contained {@link FieldInfo}s
 * to a {@link ClassDescriptor} with {@link FieldDescriptor}s.
 * </p>
 * Working getters for Classes ({@link #convert(ClassInfo)}) and Fields (
 * {@link #convert(ClassDescriptor, FieldInfo)})
 * 
 * @author Peter Schmidt
 * @since 1.3
 */
public final class InfoToDescriptorConverter {

    /**
     * This is a utility class that only offers static functions.
     */
    private InfoToDescriptorConverter() {

    }

    /**
     * This method converts a {@link ClassInfo} to a {@link ClassDescriptorImpl}
     * . Implemented Features of {@link ClassDescriptorImpl}
     * <ul>
     * <li> {@link ClassDescriptorImpl#getExtends()}</li>
     * <li> {@link ClassDescriptorImpl#getFields()}</li>
     * <li> {@link ClassDescriptorImpl#getIdentities()}</li>
     * <li> {@link ClassDescriptorImpl#getIdentity()}</li>
     * <li> {@link ClassDescriptorImpl#getJavaClass()}</li>
     * <li> {@link ClassDescriptorImpl#getMapping()}</li>
     * </ul>
     * Unimplemented Features of {@link ClassDescriptorImpl}
     * <ul>
     * <li> {@link ClassDescriptorImpl#getDepends()}</li>
     * </ul>
     * 
     * Implemented Features of {@link ClassDescriptorJDONature}
     * <ul>
     * <li> {@link ClassDescriptorJDONature#getTableName()}</li>
     * <li> {@link ClassDescriptorJDONature#getExtended()}</li>
     * <li> {@link ClassDescriptorJDONature#getField(String)}</li>
     * <li> {@link ClassDescriptorJDONature#getCacheParams()}</li>
     * </ul>
     * Unimplemented Features of {@link ClassDescriptorJDONature}
     * <ul>
     * <li> {@link ClassDescriptorJDONature#getAccessMode()}</li>
     * <li> {@link ClassDescriptorJDONature#getKeyGeneratorDescriptor()}</li>
     * <li> {@link ClassDescriptorJDONature#getNamedQueries()}</li>
     * </ul>
     * 
     * @see InfoToDescriptorConverter
     * @param classInfo
     *            The {@link ClassInfo} to convert.
     * @param cdr
     *            The {@link ClassDescriptorResolver} to ask for needed
     *            {@link ClassDescriptor}s (of extended or related classes).
     * @param descriptor
     *            A {@link ClassDescriptorImpl} for the class described by the
     *            given {@link ClassInfo}. This will be filled with information!
     * @throws MappingException
     *             if the class has not a public available default constructor
     *             or the {@link ClassDescriptor} of a related class can not be
     *             found by the {@link ClassDescriptorManager}.
     */
    public static void convert(final ClassInfo classInfo,
            final ClassDescriptorResolver cdr,
            final ClassDescriptorImpl descriptor) throws MappingException {

        /*
         * parameter checking
         */
        if (classInfo == null) {
            throw new IllegalArgumentException("ClassInfo must not be null!");

        }

        if (descriptor == null) {
            throw new IllegalArgumentException(
                    "ClassDescriptor must not be null!");

        }

        if (!classInfo.hasNature(JPAClassNature.class.getName())) {
            throw new IllegalArgumentException(
                    "ClassInfo must have JPAClassNature on it!");
        }

        if (!Types.isConstructable(classInfo.getDescribedClass(), true)) {
            throw new MappingException("mapping.classNotConstructable",
                    classInfo.getDescribedClass().getName());
        }

        JPAClassNature nature = new JPAClassNature(classInfo);

        descriptor.addNature(ClassDescriptorJDONature.class.getName());
        ClassDescriptorJDONature jdoNature = new ClassDescriptorJDONature(
                descriptor);

        /*
         * ClassMapping: initialize
         */
        ClassMapping clsMapping = new ClassMapping();

        /*
         * set classDescriptor infos
         */

        descriptor.setJavaClass(classInfo.getDescribedClass());

        descriptor.setExtends(null);
        Class<?> extendedClass = classInfo.getExtendedClass();
        if (extendedClass != null && extendedClass != Object.class) {
            ClassDescriptor extendedClassDescriptor = null;
            try {
                extendedClassDescriptor = cdr.resolve(extendedClass);
                if (extendedClassDescriptor == null) {
                    throw new MappingException(
                            "Unable to resolve extended class "
                                    + extendedClass.getName() + " in "
                                    + classInfo.getDescribedClass().getName());
                }
            } catch (ResolverException e) {
                throw new MappingException("Unable to resolve extended class "
                        + extendedClass.getName() + " in "
                        + classInfo.getDescribedClass().getName(), e);
            }
            if (new ClassDescriptorJDONature(extendedClassDescriptor).hasMappedSuperclass()) {
                ClassInfo extendedClassInfo = ClassInfoRegistry.getClassInfo(extendedClass);
                for (FieldInfo fieldInfo : extendedClassInfo.getKeyFieldInfos()) {
                    classInfo.addKey(fieldInfo);
                }
                for (FieldInfo fieldInfo : extendedClassInfo.getFieldInfos()) {
                    classInfo.addFieldInfo(fieldInfo);
                }
            } else {
                descriptor.setExtends(extendedClassDescriptor);
                clsMapping.setExtends(((ClassDescriptorImpl)extendedClassDescriptor).getMapping());
                if (extendedClassDescriptor.hasNature(ClassDescriptorJDONature.class.getName())) {
                    ClassDescriptorJDONature jdoClassNature = new ClassDescriptorJDONature(extendedClassDescriptor);
                    jdoClassNature.addExtended(descriptor);
                }
            }
        }

        /*
         * TODO: NOT IMPLEMENTED
         */
        descriptor.setDepends(null);

        clsMapping.setName(classInfo.getDescribedClass().getName());
        for (FieldInfo idInfo : classInfo.getKeyFieldInfos()) {
            clsMapping.addIdentity(idInfo.getFieldName());
        }
        clsMapping.setClassChoice(new ClassChoice());
        descriptor.setMapping(clsMapping);
        MapTo mapTo = new MapTo();
        clsMapping.setMapTo(mapTo);

        /*
         * set ClassDescriptorJDONature infos
         */

        // Table name
        String tableName = nature.getTableName();
        if ((tableName == null) || (tableName.trim().length() == 0)) {
            tableName = nature.getEntityName();
        }
        jdoNature.setTableName(tableName);
        mapTo.setTable(tableName);

        jdoNature
                .addCacheParam("name", classInfo.getDescribedClass().getName());
        Properties cacheProperties = nature.getCacheProperties();
        if (cacheProperties != null) {
            for (Object propertyKey : cacheProperties.keySet()) {
                String key = (String) propertyKey;
                jdoNature.addCacheParam(key, cacheProperties.getProperty(key));
            }
        }

        /*
         * TODO: NOT IMPLEMENTED
         */
        jdoNature.setAccessMode(null);

        // Set abstract if present
        jdoNature.setAbstract(nature.hasMappedSuperclass());

        // Add named queries if present.
        final Map<String, String> namedQuery = nature.getNamedQuery();
        if (namedQuery != null && namedQuery.size() > 0) {
            for (Map.Entry<String, String> entry : namedQuery.entrySet()) {
        	    jdoNature.addNamedQuery(entry.getKey(), entry.getValue());
            }
        }

        // Add named native queries if present.
        final Map<String, String> namedNativeQueryMap = nature.getNamedNativeQuery();
        if (namedNativeQueryMap != null && namedNativeQueryMap.size() > 0) {
            for (Map.Entry<String,String> entry: namedNativeQueryMap.entrySet()){
                org.exolab.castor.mapping.xml.NamedNativeQuery namedNativeQuery = 
                    new org.exolab.castor.mapping.xml.NamedNativeQuery();
                namedNativeQuery.setName(entry.getKey());
                namedNativeQuery.setQuery(entry.getValue());
                namedNativeQuery.setResultClass(classInfo.getDescribedClass().getName());
                jdoNature.addNamedNativeQuery(namedNativeQuery.getName(), namedNativeQuery);
            }
        }

        /*
         * generate and set FieldDescriptors for identities
         */
        FieldDescriptor[] keys = new FieldDescriptor[classInfo
                .getKeyFieldCount()];
        int i = 0;
        for (FieldInfo fieldInfo : classInfo.getKeyFieldInfos()) {
        	JPAFieldNature jpaKeyInfo = new JPAFieldNature(fieldInfo);
        	if (jpaKeyInfo.getGeneratedValueStrategy() != null) {
        		KeyGeneratorDescriptor generatorDescriptor;
                generatorDescriptor = describeKeyGenerator(fieldInfo, jpaKeyInfo);
        		jdoNature.setKeyGeneratorDescriptor(generatorDescriptor);
        	}
            keys[i] = convert(descriptor, fieldInfo, cdr);
            i++;
        }

        descriptor.setIdentities(keys);

        /*
         * generate and set FieldDescriptors for fields
         */
        FieldDescriptor[] fields = new FieldDescriptor[classInfo
                .getFieldCount()];
        i = 0;
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            fields[i] = convert(descriptor, fieldInfo, cdr);
            i++;
        }

        descriptor.setFields(fields);

    }

    private static KeyGeneratorDescriptor describeKeyGenerator(
            FieldInfo fieldInfo, JPAFieldNature jpaKeyInfo) {
        GenerationType strategy = jpaKeyInfo.getGeneratedValueStrategy();
        String strategyName = strategy.toString();
        Properties generatorParameters = new Properties();
        String generatorName;
        JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
        KeyGeneratorDescriptor generatorDescriptor;
        switch (strategy) {
        case SEQUENCE:
        	generatorName = jpaKeyInfo.getGeneratedValueGenerator();
        	JPASequenceGeneratorDescriptor sequenceGeneratorDescriptor = 
        		(JPASequenceGeneratorDescriptor)manager.get(generatorName);
        	String sequenceName = sequenceGeneratorDescriptor.getSequenceName();
        	if (!"".equals(sequenceName)) {
        		generatorParameters.put("sequence", sequenceName);
        	}
        	break;
        case AUTO:
            strategyName = GenerationType.TABLE.toString();
            JPATableGeneratorDescriptor autoGeneratorDescriptor = 
                (JPATableGeneratorDescriptor)manager.getAuto();
            autoGeneratorDescriptor.setPrimaryKeyType(fieldInfo.getFieldType());
            generatorParameters.put(TableKeyGenerator.DESCRIPTOR_KEY, autoGeneratorDescriptor);
            break;
        case TABLE:
        	generatorName = jpaKeyInfo.getGeneratedValueGenerator();       			
        	JPATableGeneratorDescriptor tableGeneratorDescriptor = 
        		(JPATableGeneratorDescriptor)manager.get(generatorName);
        	tableGeneratorDescriptor.setPrimaryKeyType(fieldInfo.getFieldType());
        	generatorParameters.put(TableKeyGenerator.DESCRIPTOR_KEY, tableGeneratorDescriptor);
        	break;
        case IDENTITY:
            strategyName = strategy.toString().toUpperCase();
            break;
        default:
        	break;
        }
        
        generatorDescriptor = 
        	new KeyGeneratorDescriptor(strategyName, 
        			strategyName, 
        			generatorParameters);
        return generatorDescriptor;
    }

    /**
     * This method converts a {@link FieldInfo} to a {@link FieldDescriptorImpl}
     * . Implemented Features of {@link ClassDescriptorImpl}
     * 
     * Implemented Features of {@link FieldDescriptorImpl}
     * <ul>
     * <li> {@link FieldDescriptorImpl#getContainingClassDescriptor()}</li>
     * <li> {@link FieldDescriptorImpl#getFieldName()}</li>
     * <li> {@link FieldDescriptorImpl#getFieldType()}</li>
     * <li> {@link FieldDescriptorImpl#getHandler()}</li>
     * <li> {@link FieldDescriptorImpl#isTransient()}</li>
     * <li> {@link FieldDescriptorImpl#isIdentity()}</li>
     * <li> {@link FieldDescriptor#isMultivalued()}</li>
     * <li> {@link FieldDescriptor#isRequired()}</li>
     * <li> {@link FieldDescriptor#getClassDescriptor()}</li>
     * </ul>
     * Unimplemented Features of {@link FieldDescriptorImpl}
     * <ul>
     * <li> {@link FieldDescriptor#isImmutable()}</li>
     * </ul>
     * 
     * Implemented Features of {@link FieldDescriptorJDONature}
     * <ul>
     * <li> {@link FieldDescriptorJDONature#getSQLName()}</li>
     * <li> {@link FieldDescriptorJDONature#getSQLType()}</li>
     * <li> {@link FieldDescriptorJDONature#getManyKey()}</li>
     * <li> {@link FieldDescriptorJDONature#getManyTable()}</li>
     * </ul>
     * Unimplemented Features of {@link FieldDescriptorJDONature}
     * <ul>
     * <li> {@link FieldDescriptorJDONature#isDirtyCheck()}</li>
     * <li> {@link FieldDescriptorJDONature#isReadonly()}</li>
     * <li> {@link FieldDescriptorJDONature#getConvertor()}</li>
     * </ul>
     * 
     * @param parent
     *            The {@link ClassDescriptor} of the Class containing the field
     *            described by the given {@link FieldInfo}.
     * @param fieldInfo
     *            The {@link FieldInfo} to convert.
     * @param cdr
     *            The {@link ClassDescriptorResolver} to ask for needed
     *            {@link ClassDescriptor}s (of extended, used classes).
     * @return A {@link FieldDescriptorImpl} for the field described by the
     *         given {@link FieldInfo}.
     * @throws MappingException
     *             if the Field is not accessible.
     * @see InfoToDescriptorConverter
     */
    private static FieldDescriptorImpl convert(
            final ClassDescriptorImpl parent, final FieldInfo fieldInfo,
            final ClassDescriptorResolver cdr) throws MappingException {

        /*
         * Instantiate Fielddescriptor, nature, etc.
         */
        if (!fieldInfo.hasNature(JPAFieldNature.class.getName())) {
            throw new IllegalArgumentException(
                    "FieldInfo must have JPAFieldNature on it!");
        }

        JPAFieldNature jpaNature = new JPAFieldNature(fieldInfo);

        String fieldName = fieldInfo.getFieldName();
        TypeInfo typeInfo = createTypeInfo(fieldInfo, jpaNature);

        FieldHandler handler = createFieldHandler(fieldInfo, typeInfo);

        boolean isTransient = jpaNature.isTransient();

        FieldDescriptorImpl fieldDescriptor = new FieldDescriptorImpl(
                fieldName, typeInfo, handler, isTransient);

        fieldDescriptor.addNature(FieldDescriptorJDONature.class.getName());
        FieldDescriptorJDONature jdoNature = new FieldDescriptorJDONature(
                fieldDescriptor);

        /*
         * Generate values and prerequisites
         */

        // Field type
        Class<?> javaType = org.exolab.castor.mapping.loader.Types
                .typeFromPrimitive(typeInfo.getFieldType());
        int sqlType = SQLTypeInfos.javaType2sqlTypeNum(javaType);

        if (fieldInfo.getFieldType().isEnum()) { // Serializing enum types.
            sqlType = jpaNature.isStringEnumType() ? java.sql.Types.VARCHAR :
                    java.sql.Types.INTEGER;
        } else if (jpaNature.isLob()) {
            sqlType =  typeInfo.getFieldType() == String.class ?
                    java.sql.Types.CLOB : java.sql.Types.BLOB;
        } else {
            final TemporalType temporalType = jpaNature.getTemporalType();
            if (temporalType != null) {
                switch (temporalType) {
                    case DATE:
                        sqlType = java.sql.Types.DATE;
                        break;
                    case TIME:
                        sqlType = java.sql.Types.TIME;
                        break;
                    case TIMESTAMP:
                        sqlType = java.sql.Types.TIMESTAMP;
                        break;
                    default:
                        break;
                }
            }
        }

        // ManyToMany JoinTable definition => if this field has a ManyToMany
        // relation, everything is defined from now on, because we do not
        // support mapping defaults and the ClassInfoBuilder already checks for
        // complete definition.
        if (jpaNature.isManyToManyInverseCopy()) {
            ClassInfo relatedClass = ClassInfoBuilder.buildClassInfo(jpaNature
                    .getRelationTargetEntity());
            String relatedFieldName = jpaNature.getRelationMappedBy();
            FieldInfo relatedField = relatedClass
                    .getFieldInfoByName(relatedFieldName);
            JPAFieldNature relatedFieldNature = new JPAFieldNature(relatedField);

            jpaNature.setJoinTableCatalog(relatedFieldNature
                    .getJoinTableCatalog());
            jpaNature.setJoinTableName(relatedFieldNature.getJoinTableName());
            jpaNature.setJoinTableSchema(relatedFieldNature
                    .getJoinTableSchema());
            jpaNature.setJoinTableInverseJoinColumns(relatedFieldNature
                    .getJoinTableJoinColumns());
            jpaNature.setJoinTableJoinColumns(relatedFieldNature
                    .getJoinTableInverseJoinColumns());
        }

        /*
         * FieldDescriptor value setting
         */
        fieldDescriptor.setContainingClassDescriptor(parent);
        fieldDescriptor.setFieldType(typeInfo.getFieldType());
        fieldDescriptor.setIdentity(jpaNature.isId());

        // descriptor.setClassDescriptor
        if (hasFieldRelation(jpaNature)) {
            try {
                fieldDescriptor.setClassDescriptor(cdr.resolve(jpaNature
                        .getRelationTargetEntity()));
            } catch (ResolverException e) {
                throw new MappingException(
                        "Can not resolve ClassDescriptor for Class "
                                + jpaNature.getRelationTargetEntity().getName()
                                + " needed by "
                                + fieldInfo.getDeclaringClassInfo()
                                        .getDescribedClass().getName() + "#"
                                + fieldName);
            }
        }

        // descriptor.setRequired
        fieldDescriptor.setRequired(createRequired(jpaNature));

        // fieldDescriptor.setMultivalued
        fieldDescriptor.setMultivalued(false);
        if (jpaNature.isOneToMany() || jpaNature.isManyToMany()) {
            fieldDescriptor.setMultivalued(true);
        }

        /*
         * TODO: NOT IMPLEMENTED
         */
        fieldDescriptor.setImmutable(false);

        /*
         * FieldDescriptorJDONature value setting
         */

        jdoNature.setSQLType(new int[] { sqlType });
        jdoNature.setSQLName(createSQLName(fieldName, jpaNature,
                fieldDescriptor.getClassDescriptor()));
        jdoNature.setManyKey(createManyKey(fieldName, jpaNature,
                fieldDescriptor.getClassDescriptor(), parent));

        // jdoNature.setManyTable (N:M only)
        if (jpaNature.isManyToMany()) {
            jdoNature.setManyTable(jpaNature.getJoinTableName());
        }

        /*
         * TODO: NOT IMPLEMENTED
         */
        jdoNature.setTypeConvertor(null);
        jdoNature.setReadOnly(false);
        /*
         * TODO: how to set this to false? How should JPA tell Castor to use or
         * not use dirtyCheck
         */
        jdoNature.setDirtyCheck(true);

        /*
         * Generate and setup FieldMapping
         */

        FieldMapping fieldMapping = new FieldMapping();
        parent.getMapping().getClassChoice().addFieldMapping(fieldMapping);

        fieldMapping.setName(fieldName);
        fieldMapping.setIdentity(fieldDescriptor.isIdentity());
        fieldMapping.setRequired(fieldDescriptor.isRequired());
        fieldMapping.setLazy(createFMLazy(jpaNature));
        fieldMapping.setDirect(false); // field access => not supported
        fieldMapping.setGetMethod(fieldInfo.getGetterMethod().getName());
        fieldMapping.setSetMethod(fieldInfo.getSetterMethod().getName());
        fieldMapping.setCollection(createColletionType(jpaNature));
        fieldMapping.setSql(createFMSQL(fieldName, jpaNature, fieldDescriptor
                .getClassDescriptor(), sqlType, parent));
        fieldMapping.setType(typeInfo.getFieldType().getName());

        return fieldDescriptor;
    }

    /**
     * Create a {@link FieldHandler} for the instantiating the
     * {@link FieldDescriptorImpl}.
     * 
     * @param fieldInfo
     *            The {@link FieldInfo} holding Java specific information and
     *            the nature.
     * @param typeInfo
     *            The {@link TypeInfo} holding information about the java Type.
     * @return a {@link FieldHandler} for the instantiating the
     *         {@link FieldDescriptorImpl}.
     * 
     * @throws MappingException
     *             If the get or set method are not public, are static, or do
     *             not specify the proper types.
     */
    private static FieldHandler createFieldHandler(final FieldInfo fieldInfo,
            final TypeInfo typeInfo) throws MappingException {
        FieldHandlerImpl fieldHandler = null;
        fieldHandler = new FieldHandlerImpl(fieldInfo.getFieldName(), null,
                null, fieldInfo.getGetterMethod(), fieldInfo.getSetterMethod(),
                typeInfo);
        return fieldHandler;
    }

    /**
     * Create a {@link TypeInfo} according to information from the given
     * {@link FieldInfo} and its {@link JPAFieldNature}. This is used to get the
     * fields type and to create the {@link FieldHandler}.
     * 
     * @param fieldInfo
     *            The {@link FieldInfo} holding Java specific information and
     *            the nature.
     * @param jpaNature
     *            The nature holding JPA information.
     * @return a {@link TypeInfo} according to information from the given
     *         {@link FieldInfo} and its {@link JPAFieldNature}.
     * @throws org.exolab.castor.mapping.MappingException on enum mapping error
     */
    private static TypeInfo createTypeInfo(final FieldInfo fieldInfo,
            final JPAFieldNature jpaNature) throws MappingException {
        final Class<?> fieldType = fieldInfo.getFieldType();
        if (hasFieldRelation(jpaNature)) {
            return new TypeInfo(jpaNature.getRelationTargetEntity());
        } else if (fieldType.isEnum()) {
            try { // Apply custom enum type conversion.
                final EnumTypeConversionHelper enumTypeConversionHelper =
                        new EnumTypeConversionHelper(fieldType);
                final Method method = jpaNature.isStringEnumType() ?
                        fieldType.getMethod("valueOf", String.class) :
                        enumTypeConversionHelper.getClass().getMethod(
                                "getEnumConstantValueByOrdinal", int.class);
                final EnumTypeConvertor typeConvertor = new EnumTypeConvertor(
                        jpaNature.isStringEnumType() ? String.class : int.class,
                        fieldType, method);
                final TypeInfo typeInfo = new TypeInfo(fieldType, typeConvertor,
                        jpaNature.isStringEnumType() ? new ObjectToString() :
                                new EnumToOrdinal(), createRequired(jpaNature),
                        null, null);
                Types.addEnumType(fieldType); // Register type accordingly.
                Types.addConvertibleType(fieldType);
                return typeInfo;
            } catch (NoSuchMethodException ex) {
                throw new MappingException(String.format(
                        "Problem occurred mapping enum `%s`: %s", fieldType,
                        ex.getMessage()), ex);
            }
        }
        return new TypeInfo(fieldInfo.getFieldType());
    }

    /**
     * Create the right parameter values for
     * {@link FieldMapping#setRequired(boolean)} and
     * {@link FieldDescriptorImpl#setRequired(boolean)} according to given JPA
     * specific information.
     * 
     * @param jpaNature
     *            The nature holding JPA information.
     * @return the right parameter values for
     *         {@link FieldMapping#setRequired(boolean)} and
     *         {@link FieldDescriptorImpl#setRequired(boolean)}.
     */
    private static boolean createRequired(final JPAFieldNature jpaNature) {
        if (jpaNature.getColumnNullable() != null) {
            return !jpaNature.getColumnNullable().booleanValue();
        }
        if (jpaNature.isId()) {
            return true;
        }
        if (jpaNature.isOneToMany()) {
            return true;
        }
        if (jpaNature.isManyToMany()) {
            return true;
        }
        if (!jpaNature.isRelationOptional()) {
            return true;
        }
        if (!jpaNature.isBasicOptional()) {
            return true;
        }

        return false;
    }

    /**
     * Create the right parameter values for
     * {@link FieldMapping#setCollection(FieldMappingCollectionType)} according
     * to given JPA specific information.
     * 
     * @param jpaNature
     *            The nature holding JPA information.
     * @return the right parameter values for
     *         {@link FieldMapping#setCollection(FieldMappingCollectionType)}.
     */
    private static FieldMappingCollectionType createColletionType(
            final JPAFieldNature jpaNature) {
        if (jpaNature.getRelationCollectionType() != null) {
            // OneToMany or ManyToMany
            String collectionTypeName = jpaNature.getRelationCollectionType()
                    .getSimpleName().toLowerCase();
            return FieldMappingCollectionType.fromValue(collectionTypeName);
        }
        return null;
    }

    /**
     * Create the right parameter values for {@link FieldMapping#setSql(Sql)}
     * according to given JPA specific information. This method may (in case of
     * a default mapped OneToMany relation) access the parents
     * {@link ClassDescriptorImpl#getIdentity()}, so identities have to be
     * converted before relational fields are, to avoid loops.
     * 
     * @param fieldName
     *            The name of the field.
     * @param jpaNature
     *            The nature holding JPA information.
     * @param fieldClassDescriptor
     *            This is needed for OneToOne and ManyToOne relations only!
     * @param sqlType
     *            the SQL type representing the java type.
     * @param parentClassDescriptor
     *            The {@link ClassDescriptorImpl} of the field owning class to
     *            get the identity field (see above).
     * @return the right parameter values for
     *         {@link FieldMapping#setLazy(boolean)}.
     * @throws MappingException
     *             if the sqlType is not recognized.
     */
    private static Sql createFMSQL(final String fieldName,
            final JPAFieldNature jpaNature,
            final ClassDescriptor fieldClassDescriptor, final int sqlType,
            final ClassDescriptorImpl parentClassDescriptor)
            throws MappingException {
        Sql fieldSql = new Sql();
        if (!hasFieldRelation(jpaNature)) {
            // for non-relational field
            for (String sqlName : createSQLName(fieldName, jpaNature,
                    fieldClassDescriptor)) {
                fieldSql.addName(sqlName);
            }
            fieldSql.setType(SQLTypeInfos.sqlTypeNum2sqlTypeName(sqlType));
        } else if (isXToOne(jpaNature)) {
            // for 1:1 or N:1 (owning side)
            for (String sqlName : createSQLName(fieldName, jpaNature,
                    fieldClassDescriptor)) {
                fieldSql.addName(sqlName);
                fieldSql.addManyKey(sqlName);
            }
        } else {
            // for 1:N or M:N
            String[] manyKeys = createManyKey(fieldName, jpaNature,
                    fieldClassDescriptor, parentClassDescriptor);
            for (String sqlName : manyKeys) {
                fieldSql.addName(sqlName);
                fieldSql.addManyKey(sqlName);
            }
        }
        return fieldSql;
    }

    /**
     * Create the right parameter values for
     * {@link FieldMapping#setLazy(boolean)} according to given JPA specific
     * information.
     * 
     * @param jpaNature
     *            The nature holding JPA information.
     * @return the right parameter values for
     *         {@link FieldMapping#setLazy(boolean)}.
     */
    private static boolean createFMLazy(final JPAFieldNature jpaNature) {
        if (jpaNature.isRelationLazyFetch()) {
            return true;
        }
        if (FetchType.LAZY.equals(jpaNature.getBasicFetch())) {
            return true;
        }
        return false;
    }

    /**
     * Create the right parameter values for
     * {@link FieldDescriptorJDONature#setManyKey(String[])} according to given
     * JPA specific information. This method may (in case of a default mapped
     * OneToMany relation) access the parents
     * {@link ClassDescriptorImpl#getIdentity()}, so identities have to be
     * converted before relational fields are to avoid loops.
     * 
     * @param fieldName
     *            The name of the field.
     * @param jpaNature
     *            The nature holding JPA information.
     * @param fieldClassDescriptor
     *            This is needed for OneToOne and ManyToOne relations only!
     * @param parentClassDescriptor
     *            The {@link ClassDescriptorImpl} of the field owning class to
     *            get the identity field (see above).
     * @return the right parameter values for
     *         {@link FieldDescriptorJDONature#setManyKey(String[])}.
     */
    private static String[] createManyKey(final String fieldName,
            final JPAFieldNature jpaNature,
            final ClassDescriptor fieldClassDescriptor,
            final ClassDescriptorImpl parentClassDescriptor) {
        // for 1:1 or N:1 (owning side)
        if (isXToOne(jpaNature)) {
            return createSQLName(fieldName, jpaNature, fieldClassDescriptor);
        }

        // for 1:N
        if (jpaNature.isOneToMany()) {
            String[] sqlManyKey = new String[1];
            // Column name if defined
            sqlManyKey[0] = jpaNature.getJoinColumnName();
            // default (<other field name>_<PK-ColName of this Entity>) if not
            // defined
            if ((sqlManyKey[0] == null) || (sqlManyKey[0].trim().length() == 0)) {
                // if bi-directional
                if (jpaNature.getRelationMappedBy() != null) {
                    sqlManyKey[0] = jpaNature.getRelationMappedBy()
                            + "_"
                            + parentClassDescriptor.getIdentity()
                                    .getFieldName();
                }

                // TODO: unidirectional Mapping (jointable, etc.)
            }

            return sqlManyKey;
        }

        if (jpaNature.isManyToMany()) {
            String[] sqlManyKey = new String[1];
            // Column name if defined
            sqlManyKey[0] = jpaNature.getJoinTableJoinColumns()[0].name();
            // TODO: maybe this should be referencedColumnName() instead...
            // needs testing!

            // If this is not defined, defaults are applying
            // defaults are not supported by Castor
            if ((sqlManyKey[0] == null) || (sqlManyKey[0].trim().length() == 0)) {
                throw new IllegalStateException(
                        "Could not find JoinColumn definition on M:N relation! "
                                + "This must be defined on either sides of the relation!");
            }
            return sqlManyKey;
        }
        // for non-relational field
        return null;

    }

    /**
     * Create the right parameter values for
     * {@link FieldDescriptorJDONature#setSQLName(String[])} according to given
     * JPA specific information.
     * 
     * @param fieldName
     *            The name of the field.
     * @param jpaNature
     *            The nature holding JPA information.
     * @param fieldClassDescriptor
     *            This is needed for OneToOne and ManyToOne relations only!
     * @return the right parameter values for
     *         {@link FieldDescriptorJDONature#setSQLName(String[])}.
     */
    private static String[] createSQLName(final String fieldName,
            final JPAFieldNature jpaNature,
            final ClassDescriptor fieldClassDescriptor) {

        String[] sqlName = new String[1];
        if (!hasFieldRelation(jpaNature)) {
            // for non-relational field

            // Column name if defined
            sqlName[0] = jpaNature.getColumnName();
            // default (field name) if not defined
            if ((sqlName[0] == null) || (sqlName[0].trim().length() == 0)) {
                sqlName[0] = fieldName;
            }
        } else if (isXToOne(jpaNature)) {
            // for 1:1 or N:1 (owning side)

            // Column name if defined
            sqlName[0] = jpaNature.getJoinColumnName();

            // default (<Fieldname>_<PK-ColName of other Entity>) if not defined
            if ((sqlName[0] == null) || (sqlName[0].trim().length() == 0)) {
                sqlName[0] = fieldName + "_"
                        + fieldClassDescriptor.getIdentity().getFieldName();
            }
        } else {
            // for 1:N (not owning side)
            sqlName = null;
        }

        return sqlName;
    }

    /**
     * Little helper to ease reading conditionals. Returns true if the field is
     * owning a relation in the database (i.e. is part of a OneToOne or
     * ManyToOne relation).
     * 
     * @param jpaNature
     *            The nature holding JPA information.
     * @return true if the field described by the nature is owning the relation
     *         to another one.
     */
    private static boolean isXToOne(final JPAFieldNature jpaNature) {
        return jpaNature.isOneToOne() || jpaNature.isManyToOne();
    }

    /**
     * Little helper to ease reading conditionals. Returns true if the field has
     * JPA relation specific information (i.e. is part of a OneToOne, OneToMany,
     * ManyToOne or ManyToMany relation).
     * 
     * @param jpaNature
     *            The nature holding JPA information.
     * @return true if the field described by the nature is related to another
     *         one.
     */
    private static boolean hasFieldRelation(final JPAFieldNature jpaNature) {
        return jpaNature.isOneToOne() || jpaNature.isManyToOne()
                || jpaNature.isOneToMany() || jpaNature.isManyToMany();
    }

}
