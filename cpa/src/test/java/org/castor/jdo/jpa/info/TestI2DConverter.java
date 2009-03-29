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

import org.castor.cpa.util.JDOClassDescriptorResolver;
import org.castor.cpa.util.classresolution.command.ClassDescriptorResolutionCommand;
import org.castor.jdo.jpa.info.entity.TestI2DCJPAExtendsTest;
import org.castor.jdo.jpa.info.entity.TestI2DCJPATest;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test the conversion features of the {@link InfoToDescriptorConverter} on the
 * external classes {@link TestI2DCJPATest} and {@link TestI2DCJPAExtendsTest}.
 * 
 * @author Peter Schmidt
 */
public final class TestI2DConverter extends TestCase {

    /** the used {@link ClassInfoBuilder}. */
    private ClassInfoBuilder _classBuilder;
    /** the used {@link ClassDescriptorManager}. */
    private ClassDescriptorResolutionCommand _cmd;

    /**
     * Set the {@link #_classBuilder} and the {@link #_cmd}.
     */
    @Before
    public void setUp() {
        _classBuilder = new ClassInfoBuilder();
        _cmd = new TestDescriptorResolutionCommand();
    }

    /**
     * Test conversion of {@link TestI2DCJPATest}.
     * @throws MappingException 
     */
    @Test
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
    @Test
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

    private ClassInfo buildClassInfo(final Class<?> aClass) throws MappingException {
        ClassInfo classInfo = _classBuilder.buildClassInfo(aClass);
        return classInfo;
    }

    @Ignore
    class TestDescriptorResolutionCommand implements ClassDescriptorResolutionCommand {
        
        public ClassDescriptor resolve(final Class<?> type) {
            try {
                return InfoToDescriptorConverter.convert(
                        _classBuilder.buildClassInfo(type), this);
            } catch (MappingException e) {
                return null;
            }
        }

        public Object getProperty(final String name) {
            return null;
        }

        public void setProperty(final String name, final Object value) {
        }

        public void addNature(final String nature) {
        }

        public boolean hasNature(final String nature) {
            return false;
        }

        public void setClassDescriptorResolver(
                final JDOClassDescriptorResolver classDescriptorResolver) {
        }
    };
}
