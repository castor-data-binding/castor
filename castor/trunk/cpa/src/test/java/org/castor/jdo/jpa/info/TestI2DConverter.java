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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.exolab.castor.xml.ClassDescriptorResolver;
import org.castor.jdo.jpa.info.entity.TestI2DCJPAExtendsTest;
import org.castor.jdo.jpa.info.entity.TestI2DCJPATest;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
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
public class TestI2DConverter {

    /** the used {@link ClassDescriptorResolver}. */
    private ClassDescriptorResolver _cmd;

    /**
     * Set the {@link #_classBuilder} and the {@link #_cmd}.
     */
    @Before
    public void setUp() {
        _cmd = new CDRCmd();
    }

    /**
     * Test conversion of {@link TestI2DCJPATest}.
     * @throws MappingException 
     */
    @Test
    public void testJPATest() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(TestI2DCJPATest.class);

        assertNotNull(classInfo);
        assertEquals(TestI2DCJPATest.class, classInfo.getDescribedClass());
        assertEquals(Object.class, classInfo.getExtendedClass());

        assertEquals(3, classInfo.getFieldCount());
        assertEquals(1, classInfo.getKeyFieldCount());

        ClassDescriptorImpl classDescriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, _cmd, classDescriptor);

        FieldDescriptor[] fdFields = classDescriptor.getFields();
        FieldDescriptor[] fdIdentities = classDescriptor.getIdentities();

        assertNotNull(fdFields);
        assertEquals(3, fdFields.length);
        assertNotNull(fdIdentities);
        assertEquals(1, fdIdentities.length);

        /*
         * check default settings for field "default"
         */
        
    }

    /**
     * Test conversion of {@link TestI2DCJPAExtendsTest}.
     * @throws MappingException 
     */
    @Test
    public void testJPADefaultTest() throws MappingException {
        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(TestI2DCJPAExtendsTest.class);

        assertNotNull(classInfo);
        assertEquals(TestI2DCJPAExtendsTest.class, classInfo.getDescribedClass());
        assertEquals(TestI2DCJPATest.class, classInfo.getExtendedClass());

        assertEquals(0, classInfo.getFieldCount());
        assertEquals(1, classInfo.getKeyFieldCount());

        ClassDescriptorImpl classDescriptor = new ClassDescriptorImpl();
        InfoToDescriptorConverter.convert(classInfo, _cmd, classDescriptor);

        FieldDescriptor[] fdFields = classDescriptor.getFields();
        FieldDescriptor[] fdIdentities = classDescriptor.getIdentities();

        assertNotNull(fdFields);
        assertEquals(0, fdFields.length);
        assertNotNull(fdIdentities);
        assertEquals(1, fdIdentities.length);
    }

    @Ignore
    public class CDRCmd implements ClassDescriptorResolver {
        private JPATestResolveCommand _cmd = new JPATestResolveCommand();
        
        public ClassDescriptor resolve(final Class type) {
            return _cmd.resolve(type);
        }

        public MappingLoader getMappingLoader() {
            return null;
        }

        public void setMappingLoader(final MappingLoader mappingLoader) {
        }

    }

}
