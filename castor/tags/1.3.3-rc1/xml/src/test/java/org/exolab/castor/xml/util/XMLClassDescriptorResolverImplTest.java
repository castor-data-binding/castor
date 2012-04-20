/*
 * Copyright 2009 Torsten Juergeleit
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
package org.exolab.castor.xml.util;

import junit.framework.Assert;

import org.castor.xml.AbstractInternalContext;
import org.easymock.EasyMock;
import org.exolab.castor.xml.UnmarshalFranz;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link XMLClassDescriptorResolverImpl}.
 * 
 * @author Torsten Juergeleit
 * @since 1.3.1
 */
public class XMLClassDescriptorResolverImplTest {

    @SuppressWarnings("unchecked")
    private static final Class TEST_CLASS = UnmarshalFranz.class;
    private static final String TEST_CLASS_NAME = TEST_CLASS.getName();

    private XMLClassDescriptorResolverImpl resolver;

    @Before
    public void setup() {
        AbstractInternalContext internalContext = new AbstractInternalContext() { };
        internalContext.setClassLoader(getClass().getClassLoader());

        ResolverStrategy strategy = new AbstractResolverStrategy() {
            public XMLClassDescriptor createDescriptor(String className) {
                return createMockDescriptor(className);
            }
        };
        internalContext.setResolverStrategy(strategy);

        resolver = new XMLClassDescriptorResolverImpl();
        resolver.setInternalContext(internalContext);
        resolver.setResolverStrategy(strategy);
        resolver.setUseIntrospection(false);
        internalContext.setXMLClassDescriptorResolver(resolver);
    }

    @Test
    public void testAddClass() throws Throwable {
        resolver.addClass(TEST_CLASS);
        XMLClassDescriptor descriptor = (XMLClassDescriptor) resolver.resolve(TEST_CLASS);
        Assert.assertNotNull(descriptor);
        Assert.assertEquals(TEST_CLASS_NAME, descriptor.getXMLName());
    }

    @Test
    public void testResolveClass() throws Throwable {
        XMLClassDescriptor descriptor = (XMLClassDescriptor) resolver.resolve(TEST_CLASS);
        Assert.assertNotNull(descriptor);
        Assert.assertEquals(TEST_CLASS_NAME, descriptor.getXMLName());
    }

    @Test
    public void testAddClassName() throws Throwable {
        resolver.addClass(TEST_CLASS_NAME);
        XMLClassDescriptor descriptor = resolver.resolve(TEST_CLASS_NAME);
        Assert.assertNotNull(descriptor);
        Assert.assertEquals(TEST_CLASS_NAME, descriptor.getXMLName());
    }

    @Test
    public void testResolveClassName() throws Throwable {
        XMLClassDescriptor descriptor = resolver.resolve(TEST_CLASS_NAME);
        Assert.assertNotNull(descriptor);
        Assert.assertEquals(TEST_CLASS_NAME, descriptor.getXMLName());
    }

    private XMLClassDescriptor createMockDescriptor(String className) {
        XMLClassDescriptor descriptor = EasyMock.createMock(
                "TestClassDescriptor", XMLClassDescriptor.class);
        EasyMock.expect(descriptor.getXMLName()).andReturn(className)
                .anyTimes();
        EasyMock.replay(descriptor);
        return descriptor;
    }
}
