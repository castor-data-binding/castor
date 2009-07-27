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

import org.castor.jdo.jpa.natures.JPAClassNature;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.xml.util.ClassDescriptorResolutionCommand;

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
     * This method converts a {@link ClassInfo} to a {@link ClassDescriptorImpl}. 
     * Implemented Features of {@link ClassDescriptorImpl}
     * <ul>
     * <li> {@link ClassDescriptorImpl#getExtends()}</li>
     * <li> {@link ClassDescriptorImpl#getFields()}</li>
     * <li> {@link ClassDescriptorImpl#getIdentities()}</li>
     * <li> {@link ClassDescriptorImpl#getIdentity()}</li>
     * <li> {@link ClassDescriptorImpl#getJavaClass()}</li>
     * </ul>
     * Unimplemented Features of {@link ClassDescriptorImpl}
     * <ul>
     * <li> {@link ClassDescriptorImpl#getDepends()}</li>
     * <li> {@link ClassDescriptorImpl#getMapping()}</li>
     * </ul>
     * 
     * Implemented Features of {@link ClassDescriptorJDONature}
     * <ul>
     * <li> {@link ClassDescriptorJDONature#getExtended()}</li>
     * <li> {@link ClassDescriptorJDONature#getField(String)}</li>
     * </ul>
     * Unimplemented Features of {@link ClassDescriptorJDONature}
     * <ul>
     * <li> {@link ClassDescriptorJDONature#getTableName()}</li>
     * <li> {@link ClassDescriptorJDONature#getAccessMode()}</li>
     * <li> {@link ClassDescriptorJDONature#getCacheParams()}</li>
     * <li> {@link ClassDescriptorJDONature#getKeyGeneratorDescriptor()}</li>
     * <li> {@link ClassDescriptorJDONature#getNamedQueries()}</li>
     * </ul>
     * 
     * @see InfoToDescriptorConverter
     * @param classInfo
     *            The {@link ClassInfo} to convert.
     * @param command
     *            The {@link ClassDescriptorResolutionCommand} to ask for needed
     *            {@link ClassDescriptor}s (of extended, used classes).
     * @return A {@link ClassDescriptorImpl} for the class described by the
     *         given {@link ClassInfo}.
     * @throws MappingException
     *             if the class has not a public available default constructor
     *             or the {@link ClassDescriptor} of a related class can not be
     *             found by the {@link ClassDescriptorManager}.
     */
    public static ClassDescriptorImpl convert(final ClassInfo classInfo,
            final ClassDescriptorResolutionCommand command)
            throws MappingException {

        if (!classInfo.hasNature(JPAClassNature.class.getName())) {
            throw new IllegalArgumentException(
                    "ClassInfo must have JPAClassNature on it!");
        }

        if (!Types.isConstructable(classInfo.getDescribedClass(), true)) {
            throw new MappingException("mapping.classNotConstructable",
                    classInfo.getDescribedClass().getName());
        }

        JPAClassNature nature = new JPAClassNature(classInfo);

        ClassDescriptorImpl descriptor = new ClassDescriptorImpl();
        descriptor.addNature(ClassDescriptorJDONature.class.getName());
        ClassDescriptorJDONature jdoNature = new ClassDescriptorJDONature(
                descriptor);

        /*
         * set classDescriptor infos
         */

        /*
         * working
         */
        descriptor.setJavaClass(classInfo.getDescribedClass());
        descriptor.setExtends(null);
        Class<?> extendedClass = classInfo.getExtendedClass();

        if (extendedClass != null && extendedClass != Object.class) {
            ClassDescriptor extendedClassDescriptor = command
                    .resolve(extendedClass);
            if (extendedClassDescriptor == null) {
                throw new MappingException("mapping.extendsMissing", classInfo
                        .getDescribedClass(), extendedClass);
            }
            descriptor.setExtends(extendedClassDescriptor);
            if (extendedClassDescriptor
                    .hasNature(ClassDescriptorJDONature.class.getName())) {
                new ClassDescriptorJDONature(extendedClassDescriptor)
                        .addExtended(descriptor);
            }
        }

        /*
         * interm setters of not working features
         */
        descriptor.setDepends(null);
        descriptor.setMapping(null);

        /*
         * set ClassDescriptorJDONature infos
         */

        /*
         * working
         */
        /*
         * interm setters of not working features
         */
        jdoNature.setTableName(null);
        jdoNature.setAccessMode(null);
        jdoNature.setKeyGeneratorDescriptor(null);

        /*
         * generate and set FieldDescriptors for fields
         */
        FieldDescriptor[] fields = new FieldDescriptor[classInfo
                .getFieldCount()];
        int i = 0;
        for (FieldInfo fieldInfo : classInfo.getFieldInfos()) {
            fields[i] = convert(descriptor, fieldInfo);
            i++;
        }

        descriptor.setFields(fields);

        /*
         * generate and set FieldDescriptors for identities
         */
        FieldDescriptor[] keys = new FieldDescriptor[classInfo
                .getKeyFieldCount()];
        i = 0;
        for (FieldInfo fieldInfo : classInfo.getKeyFieldInfos()) {
            keys[i] = convert(descriptor, fieldInfo);
            i++;
        }

        descriptor.setIdentities(keys);

        return descriptor;
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
     * </ul>
     * Unimplemented Features of {@link FieldDescriptorImpl}
     * <ul>
     * <li> {@link FieldDescriptorImpl#isTransient()}</li>
     * <li> {@link FieldDescriptorImpl#isIdentity()}</li>
     * <li> {@link FieldDescriptor#getHandler()} (for property access)</li>
     * <li> {@link FieldDescriptor#getClassDescriptor()}</li>
     * <li> {@link FieldDescriptor#isImmutable()}</li>
     * <li> {@link FieldDescriptor#isMultivalued()}</li>
     * <li> {@link FieldDescriptor#isRequired()}</li>
     * </ul>
     * 
     * Implemented Features of {@link FieldDescriptorJDONature}
     * <ul>
     * </ul>
     * Unimplemented Features of {@link FieldDescriptorJDONature}
     * <ul>
     * <li> {@link FieldDescriptorJDONature#isDirtyCheck()}</li>
     * <li> {@link FieldDescriptorJDONature#isReadonly()}</li>
     * <li> {@link FieldDescriptorJDONature#getConvertor()}</li>
     * <li> {@link FieldDescriptorJDONature#getManyKey()}</li>
     * <li> {@link FieldDescriptorJDONature#getManyTable()}</li>
     * <li> {@link FieldDescriptorJDONature#getSQLName()}</li>
     * <li> {@link FieldDescriptorJDONature#getSQLType()}</li>
     * </ul>
     * 
     * @param parent
     *            The {@link ClassDescriptor} of the Class containing the field
     *            described by the given {@link FieldInfo}.
     * @param fieldInfo
     *            The {@link FieldInfo} to convert.
     * @return A {@link FieldDescriptorImpl} for the field described by the
     *         given {@link FieldInfo}.
     * @throws MappingException
     *             if the Field is not accessible.
     * @see InfoToDescriptorConverter
     */
    private static FieldDescriptorImpl convert(final ClassDescriptor parent,
            final FieldInfo fieldInfo) throws MappingException {

        if (!fieldInfo.hasNature(JPAFieldNature.class.getName())) {
            throw new IllegalArgumentException(
                    "FieldInfo must have JPAFieldNature on it!");
            // see JDOMappingLoader.java
        }

        JPAFieldNature jpaNature = new JPAFieldNature(fieldInfo);

        String name = fieldInfo.getFieldName();
        TypeInfo typeInfo = new TypeInfo(fieldInfo.getFieldType());

        FieldHandler handler = fieldInfo.getFieldHandler();

        boolean isTransient = false;
        // isTransient = jpaNature.isTransient ();

        FieldDescriptorImpl descriptor = new FieldDescriptorImpl(name,
                typeInfo, handler, isTransient);

        /*
         * working!
         */
        descriptor.setContainingClassDescriptor(parent);
        descriptor.setFieldType(fieldInfo.getFieldType());
        // descriptor.setIdentity (jpaNature.isId ());
        /*
         * interim setters for not implemented getters!
         */
        descriptor.setIdentity(false);
        descriptor.setClassDescriptor(null);
        descriptor.setImmutable(false);
        descriptor.setMultivalued(false);
        descriptor.setRequired(false);

        /*
         * FieldDescriptorJDONature
         */
        descriptor.addNature(FieldDescriptorJDONature.class.getName());
        FieldDescriptorJDONature jdoNature = new FieldDescriptorJDONature(
                descriptor);

        /*
         * working!
         */

        /*
         * interim setters for not implemented getters!
         */
        jdoNature.setManyKey(null);
        jdoNature.setManyTable(null);
        jdoNature.setSQLType(null);
        jdoNature.setSQLName(null);
        jdoNature.setTypeConvertor(null);
        jdoNature.setDirtyCheck(true);
        jdoNature.setReadOnly(false);

        /* JPA.@Id */

        return descriptor;
    }

}
