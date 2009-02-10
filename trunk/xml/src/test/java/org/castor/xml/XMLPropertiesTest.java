/*
 * Copyright 2007 Joachim Grueneis
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
package org.castor.xml;

import org.castor.core.util.AbstractProperties;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * The XMLProperties test has one goal - check if the proper defaults
 * are in place.
 * 
 * @author Joachim Grueneis, jgrueneis_at_gmail_dot_com
 * @version $Id$
 */
public class XMLPropertiesTest extends TestCase {
    private AbstractProperties _properties;

    public XMLPropertiesTest(final String name) {
        super(name);
    }

    public void setUp() {
        _properties = XMLProperties.newInstance();
        Assert.assertNotNull(
                "Properties must exist after call to newInstance()", _properties);
        Assert.assertNotNull(
                "Application class loader must not be null",
                _properties.getApplicationClassLoader());
        Assert.assertNotNull(
                "Domain class loader must not be null",
                _properties.getDomainClassLoader());
    }

    public void testNewInstanceClassLoaderClassLoader() {
        AbstractProperties properties = XMLProperties.newInstance(null, null);
        Assert.assertNotNull(
                "Properties must exist after call to newInstance()", properties);
//        Assert.assertNotNull(
//                "Application class loader must not be null",
//                properties.getApplicationClassLoader());
//        Assert.assertNotNull(
//                "Domain class loader must not be null",
//                properties.getDomainClassLoader());
    }

    public void testGetBooleanString() {
        Boolean notExistingProperty = _properties.getBoolean("Something which doesn't exist");
        Assert.assertNull("A not existing property needs to return null", notExistingProperty);
    }

    public void testGetBooleanStringBoolean() {
        boolean notExistingPropertyWithDefault = 
            _properties.getBoolean("Something which doesn't exist", true);
        Assert.assertTrue(
                "A not existing property with default true", 
                notExistingPropertyWithDefault);
    }

    /**
     * Tests wether the expected defaults are present or not.
     */
    public void testDefaults() {
        Boolean loadPackageMapping = 
            _properties.getBoolean(XMLProperties.LOAD_PACKAGE_MAPPING);
        Assert.assertEquals(
                "load package mapping is expected to be set to: true", 
                Boolean.TRUE, loadPackageMapping);
        
        String serialzierFactory = _properties.getString(XMLProperties.SERIALIZER_FACTORY);
        Assert.assertEquals(
                "check serializer factory default", 
                "org.exolab.castor.xml.XercesJDK5XMLSerializerFactory", 
                serialzierFactory);
        
        Boolean strictElements = _properties.getBoolean(XMLProperties.STRICT_ELEMENTS);
        Assert.assertEquals("strict elements default", Boolean.FALSE, strictElements);
        
        Boolean marshallingValidation = 
            _properties.getBoolean(XMLProperties.MARSHALLING_VALIDATION);
        Assert.assertEquals("marshallingValidation", Boolean.TRUE, marshallingValidation);
        
        Boolean useIntrospection = _properties.getBoolean(XMLProperties.USE_INTROSPECTION);
        Assert.assertEquals("useIntrospection", Boolean.TRUE, useIntrospection);
    }
}
