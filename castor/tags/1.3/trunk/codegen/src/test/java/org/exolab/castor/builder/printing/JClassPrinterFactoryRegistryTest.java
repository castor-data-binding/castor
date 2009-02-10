/*
 * Copyright 2005-2007 Werner Guttmann
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
package org.exolab.castor.builder.printing;

import java.util.Arrays;

import org.exolab.castor.builder.BuilderConfiguration;

import junit.framework.TestCase;

/**
 * JUnit test case for {@link JClassPrinter} registry.
 * 
 * @since 1.2.1
 */
public class JClassPrinterFactoryRegistryTest extends TestCase {

    /**
     * The {@link JClassPrinterFactoryRegistry} instance to test.
     */
    private JClassPrinterFactoryRegistry _registry;

    protected void setUp() throws Exception {
        super.setUp();
        _registry = new JClassPrinterFactoryRegistry(new BuilderConfiguration());
        
    }
    
    public final void testGetNames() {
        String[] jclassPrinterFactoryNames = _registry.getJClassPrinterFactoryNames();
        assertNotNull(jclassPrinterFactoryNames);
        assertTrue(jclassPrinterFactoryNames.length == 2);
        assertTrue(Arrays.asList(jclassPrinterFactoryNames).contains("standard"));
        assertTrue(Arrays.asList(jclassPrinterFactoryNames).contains("velocity"));
    }
    
    public final void testObtainStandardJClassPrinter() {
       JClassPrinterFactory classPrinterFactory = _registry.getJClassPrinterFactory("standard");
       assertNotNull(classPrinterFactory);
       assertEquals("standard", classPrinterFactory.getName());
    }

    public final void testObtainVelocityJClassPrinter() {
        JClassPrinterFactory classPrinterFactory = _registry.getJClassPrinterFactory("velocity");
        assertNotNull(classPrinterFactory);
        assertEquals("velocity", classPrinterFactory.getName());
     }

}
