/*
 * Copyright 2008 Lukas Lang
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
package org.exolab.castor.jdo.engine;

import junit.framework.TestCase;

import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;

/**
 * Runs some tests on the {@link JDOMappingLoader}.
 * 
 * @author Lukas Lang
 */
public final class JDOMappingLoaderTest extends TestCase {

    /**
     * Test resolve relations for One-To-One with mapping file.
     */
    public void testResolveRelationsOneOneMapping() throws Exception {
        org.exolab.castor.mapping.Mapping mapping = new org.exolab.castor.mapping.Mapping();
        String mappingFile = getClass().getResource("mapping.xml")
                .toExternalForm();
        mapping.loadMapping(mappingFile);

        MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
        JDOMappingLoader mappingLoader = (JDOMappingLoader) mappingUnmarshaller
                .getMappingLoader(mapping, BindingType.JDO);

        assertEquals(2, mapping.getRoot().getClassMappingCount());

        ClassDescriptor entityDescriptor = mappingLoader
                .getDescriptor("org.exolab.castor.jdo.engine.Entity");

        assertNotNull(entityDescriptor);

        FieldDescriptor id = entityDescriptor.getIdentity();
        assertEquals("id", id.getFieldName());
        FieldDescriptor[] fields = entityDescriptor.getFields();
        assertEquals("item", fields[0].getFieldName());

        ClassDescriptor itemDescriptor = mappingLoader
                .getDescriptor("org.exolab.castor.jdo.engine.Item");

        assertNotNull(itemDescriptor);

        id = itemDescriptor.getIdentity();
        assertEquals("id", id.getFieldName());
    }

    /**
     * Test resolve relations for One-To-One with the first entity in the
     * mapping file. The second entity as descriptor on the filesystem.
     * 
     * @throws Exception
     *             If initialization fails.
     */
    public void testResolveRelationsOneAsMappingOneAsFile() throws Exception {
        org.exolab.castor.mapping.Mapping mapping = new org.exolab.castor.mapping.Mapping();
        String mappingFile = getClass().getResource("mapping-employee.xml")
                .toExternalForm();
        mapping.loadMapping(mappingFile);

        MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
        JDOMappingLoader mappingLoader = (JDOMappingLoader) mappingUnmarshaller
                .getMappingLoader(mapping, BindingType.JDO);

        assertEquals(1, mapping.getRoot().getClassMappingCount());

        ClassDescriptor entityDescriptor = mappingLoader
                .getDescriptor("org.exolab.castor.jdo.engine.Employee");

        assertNotNull(entityDescriptor);

        FieldDescriptor id = entityDescriptor.getIdentity();
        assertEquals("id", id.getFieldName());
        FieldDescriptor[] fields = entityDescriptor.getFields();
        assertEquals("address", fields[0].getFieldName());
    }

    /**
     * Test resolve relations for One-To-One with the first entity on the
     * filesystem. The second entity is specified in the mapping.
     * 
     * @throws Exception
     *             If initialization fails.
     */
    public void testResolveRelationsOneAsFileSecondAsMapping() throws Exception {
        org.exolab.castor.mapping.Mapping mapping = new org.exolab.castor.mapping.Mapping();
        String mappingFile = getClass().getResource("mapping-address.xml")
                .toExternalForm();
        mapping.loadMapping(mappingFile);

        MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
        JDOMappingLoader mappingLoader = (JDOMappingLoader) mappingUnmarshaller
                .getMappingLoader(mapping, BindingType.JDO);

        assertEquals(1, mapping.getRoot().getClassMappingCount());

        ClassDescriptor entityDescriptor = mappingLoader
                .getDescriptor("org.exolab.castor.jdo.engine.Address");

        assertNotNull(entityDescriptor);

        FieldDescriptor id = entityDescriptor.getIdentity();
        assertEquals("id", id.getFieldName());
    }

}
