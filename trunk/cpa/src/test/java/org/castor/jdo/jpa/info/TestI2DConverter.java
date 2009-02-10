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

import junit.framework.TestCase;

import org.castor.jdo.jpa.info.entity.TestI2DCJPAExtendsTest;
import org.castor.jdo.jpa.info.entity.TestI2DCJPATest;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.xml.util.ClassDescriptorResolutionCommand;

/**
 * Test the conversion features of the {@link InfoToDescriptorConverter} on the
 * external classes {@link TestI2DCJPATest} and {@link TestI2DCJPAExtendsTest}.
 * 
 * @author Peter Schmidt
 */
public class TestI2DConverter extends TestCase {

    /** the used {@link ClassInfoBuilder}. */
    private ClassInfoBuilder _classBuilder;
    /** the used {@link ClassDescriptorManager}. */
    private ClassDescriptorResolutionCommand _cmd;

    /**
     * Set the {@link #_classBuilder} and the {@link #_cmd}.
     */
    public void setUp() {
        _classBuilder = new ClassInfoBuilder();
        _cmd = new ClassDescriptorResolutionCommand() {

            public ClassDescriptor resolve(Class type) {

                try {
                    return InfoToDescriptorConverter.convert(_classBuilder
                            .buildClassInfo(type), this);
                } catch (MappingException e) {
                    return null;
                }

            }

            public Object getProperty(String name) {
                return null;
            }

            public void setProperty(String name, Object value) {

            }

            public void addNature(String nature) {

            }

            public boolean hasNature(String nature) {
                return false;
            }

        };
    }

    /**
     * Test conversion of {@link TestI2DCJPATest}.
     * @throws MappingException 
     */
    public void testJPATest() throws MappingException {
        /* build class */
        ClassInfo classInfo = buildClassInfo(TestI2DCJPATest.class);

        assertNotNull(classInfo);
        assertEquals(TestI2DCJPATest.class, classInfo.getDescribedClass());
        assertEquals(Object.class, classInfo.getExtendedClass());

        assertEquals(3, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        ClassDescriptorImpl classDescriptor = 
            InfoToDescriptorConverter.convert(classInfo, _cmd);

        FieldDescriptor[] fdFields = classDescriptor.getFields();
        FieldDescriptor[] fdIdentities = classDescriptor.getIdentities();

        assertNotNull(fdFields);
        assertEquals(3, fdFields.length);
        assertNotNull(fdIdentities);
        assertEquals(0, fdIdentities.length);

    }

    /**
     * Test conversion of {@link TestI2DCJPAExtendsTest}.
     * @throws MappingException 
     */
    public void testJPADefaultTest() throws MappingException {
        ClassInfo classInfo = buildClassInfo(TestI2DCJPAExtendsTest.class);

        assertNotNull(classInfo);
        assertEquals(TestI2DCJPAExtendsTest.class, classInfo.getDescribedClass());
        assertEquals(TestI2DCJPATest.class, classInfo.getExtendedClass());

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        ClassDescriptorImpl classDescriptor = 
            InfoToDescriptorConverter.convert(classInfo, _cmd);

        FieldDescriptor[] fdFields = classDescriptor.getFields();
        FieldDescriptor[] fdIdentities = classDescriptor.getIdentities();

        assertNotNull(fdFields);
        assertEquals(1, fdFields.length);
        assertNotNull(fdIdentities);
        assertEquals(0, fdIdentities.length);
    }

    private ClassInfo buildClassInfo(Class<?> aClass) throws MappingException {
        ClassInfo classInfo = _classBuilder.buildClassInfo(aClass);
        return classInfo;
    }

}
