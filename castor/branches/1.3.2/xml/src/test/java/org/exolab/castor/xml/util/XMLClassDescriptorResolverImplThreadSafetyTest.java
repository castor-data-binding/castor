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
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.junit.Test;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;

/**
 * Test case for thread safetyness of {@link XMLClassDescriptorResolverImpl}.
 * <p><b>Only useful on multi-core systems!!!</b></p>
 *
 * @author Torsten Juergeleit
 * @since 1.3.1
 */
public class XMLClassDescriptorResolverImplThreadSafetyTest extends MultithreadedTestCase {

    private XMLClassDescriptorResolverImpl resolver;

    @Override
    public void initialize() {
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

    public void thread1() throws Throwable {
        waitForTick(3);
        resolveClass("Test1");
        resolveClass("Test1");
        resolveClass("Test11");
        resolveClass("Test11");
        resolveClass("Test111");
        resolveClass("Test111");
    }

    public void thread2() throws Throwable {
        waitForTick(3);
        resolveClass("Test2");
        resolveClass("Test2");
        resolveClass("Test22");
        resolveClass("Test22");
        resolveClass("Test222");
        resolveClass("Test222");
    }

    public void thread3() throws Throwable {
        waitForTick(3);
        resolveClass("Test3");
        resolveClass("Test3");
        resolveClass("Test33");
        resolveClass("Test33");
        resolveClass("Test333");
        resolveClass("Test333");
    }

    public void thread4() throws Throwable {
        waitForTick(3);
        resolveClass("Test4");
        resolveClass("Test4");
        resolveClass("Test44");
        resolveClass("Test44");
        resolveClass("Test444");
        resolveClass("Test444");
    }

    public void thread5() throws Throwable {
        waitForTick(3);
        resolveClass("Test5");
        resolveClass("Test5");
        resolveClass("Test55");
        resolveClass("Test55");
        resolveClass("Test5555");
        resolveClass("Test5555");
    }

    private void resolveClass(final String className) throws ResolverException {
        XMLClassDescriptor descriptor = resolver.resolve(className);
        Assert.assertNotNull("Descriptor '" + className + "' not resolved", descriptor);
        Assert.assertEquals(className, descriptor.getXMLName());
    }

    @Override
    public void finish() {
        Thread thread = getThreadByName("thread1");
        assertNotNull(thread);
        assertFalse(thread.isAlive());
        thread = getThreadByName("thread2");
        assertNotNull(thread);
        assertFalse(thread.isAlive());
        thread = getThreadByName("thread3");
        assertNotNull(thread);
        assertFalse(thread.isAlive());
        thread = getThreadByName("thread4");
        assertNotNull(thread);
        assertFalse(thread.isAlive());
        thread = getThreadByName("thread5");
        assertNotNull(thread);
        assertFalse(thread.isAlive());
    }

    @Test
    public void testCDR() throws Throwable {
        TestFramework.runManyTimes(new XMLClassDescriptorResolverImplThreadSafetyTest(), 100);
    }

    private XMLClassDescriptor createMockDescriptor(String className) {
        XMLClassDescriptor descriptor = EasyMock.createMock(className,
                XMLClassDescriptor.class);
        EasyMock.expect(descriptor.getXMLName()).andReturn(className)
                .anyTimes();
        EasyMock.makeThreadSafe(descriptor, true);
        EasyMock.replay(descriptor);
        return descriptor;
    }
}
