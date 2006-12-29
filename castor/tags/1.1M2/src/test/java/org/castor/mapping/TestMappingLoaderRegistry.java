/*
 * Copyright 2006 Werner Guttmann
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
package org.castor.mapping;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.castor.util.Configuration;
import org.exolab.castor.jdo.engine.JDOMappingLoader;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.xml.XMLMappingLoader;

/**
 * UTF test case for {@see org.castor.jdo.drivers.PointbaseFactory}.
 */
public class TestMappingLoaderRegistry extends TestCase {

    public final void testGetInstance() throws Exception {
        Configuration config = Configuration.getInstance();
        MappingLoaderRegistry registry = new MappingLoaderRegistry(config);
        assertNotNull(registry);
    }

    public final void testEnlistMappingLoaders() throws Exception {
        Configuration config = Configuration.getInstance();
        MappingLoaderRegistry registry = new MappingLoaderRegistry(config);
        assertNotNull(registry);

        Collection factories = registry.getMappingLoaderFactories();
        assertNotNull(factories);
        assertTrue(factories.size() > 0);
        assertEquals(2, factories.size());

        Iterator iter = factories.iterator();

        MappingLoaderFactory factory = (MappingLoaderFactory) iter.next();
        assertNotNull(factory);
        assertEquals("JDO", factory.getName());
        assertEquals("org.castor.mapping.JDOMappingLoaderFactory",
                factory.getClass().getName());
        assertEquals("CastorXmlMapping", factory.getSourceType());

        factory = (MappingLoaderFactory) iter.next();
        assertNotNull(factory);
        assertEquals("XML", factory.getName());
        assertEquals("org.castor.mapping.XMLMappingLoaderFactory",
                factory.getClass().getName());
        assertEquals("CastorXmlMapping", factory.getSourceType());

    }

    public final void testGetXMLMappingLoader() throws Exception {
        Configuration config = Configuration.getInstance();
        MappingLoaderRegistry registry = new MappingLoaderRegistry(config);
        assertNotNull(registry);

        MappingLoader mappingLoader = registry.getMappingLoader(
                "CastorXmlMapping", BindingType.XML);
        assertNotNull(mappingLoader);
        assertTrue(mappingLoader instanceof XMLMappingLoader);
    }

    public final void testGetJDOMappingLoader() throws Exception {
        Configuration config = Configuration.getInstance();
        MappingLoaderRegistry registry = new MappingLoaderRegistry(config);
        assertNotNull(registry);

        MappingLoader mappingLoader = registry.getMappingLoader(
                "CastorXmlMapping", BindingType.JDO);
        assertNotNull(mappingLoader);
        assertTrue(mappingLoader instanceof JDOMappingLoader);
    }

}
