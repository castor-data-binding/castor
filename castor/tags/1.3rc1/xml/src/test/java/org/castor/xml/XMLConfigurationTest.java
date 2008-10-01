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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Configuration;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * The XMLConfiguration test has one goal - check if the proper defaults
 * are in place.
 * 
 * @author Joachim Grueneis, jgrueneis_at_gmail_dot_com
 * @version $Id$
 */
public class XMLConfigurationTest extends TestCase {
    private static final Log LOG = LogFactory.getLog(XMLConfigurationTest.class);

    private Configuration _configuration;

    public XMLConfigurationTest(final String name) {
        super(name);
    }

    public void setUp() {
        _configuration = XMLConfiguration.newInstance();
        Assert.assertNotNull(
                "Configuration must exist after call to newInstance()", _configuration);
        Assert.assertNotNull(
                "Application class loader must not be null",
                _configuration.getApplicationClassLoader());
        Assert.assertNotNull(
                "Domain class loader must not be null",
                _configuration.getDomainClassLoader());
    }

    public void testNewInstanceClassLoaderClassLoader() {
        Configuration c = XMLConfiguration.newInstance(null, null);
        Assert.assertNotNull(
                "Configuration must exist after call to newInstance()", c);
//        Assert.assertNotNull(
//                "Application class loader must not be null",
//                c.getApplicationClassLoader());
//        Assert.assertNotNull(
//                "Domain class loader must not be null",
//                c.getDomainClassLoader());
    }

    public void testGetBooleanString() {
        Boolean notExistingProperty = _configuration.getBoolean("Something which doesn't exist");
        Assert.assertNull("A not existing property needs to return null", notExistingProperty);
    }

    public void testGetBooleanStringBoolean() {
        boolean notExistingPropertyWithDefault = 
            _configuration.getBoolean("Something which doesn't exist", true);
        Assert.assertTrue(
                "A not existing property with default true", 
                notExistingPropertyWithDefault);
    }

    /**
     * Tests wether the expected defaults are present or not.
     */
    public void testDefaults() {
        Boolean loadPackageMapping = 
            _configuration.getBoolean(XMLConfiguration.LOAD_PACKAGE_MAPPING);
        Assert.assertEquals(
                "load package mapping is expected to be set to: true", 
                Boolean.TRUE, loadPackageMapping);
        
        String serialzierFactory = _configuration.getString(XMLConfiguration.SERIALIZER_FACTORY);
        Assert.assertEquals(
                "check serializer factory default", 
                "org.exolab.castor.xml.XercesJDK5XMLSerializerFactory", 
                serialzierFactory);
        
        Boolean strictElements = _configuration.getBoolean(XMLConfiguration.STRICT_ELEMENTS);
        Assert.assertEquals("strict elements default", Boolean.FALSE, strictElements);
        
        Boolean marshallingValidation = 
            _configuration.getBoolean(XMLConfiguration.MARSHALLING_VALIDATION);
        Assert.assertEquals("marshallingValidation", Boolean.TRUE, marshallingValidation);
        
        Boolean useIntrospection = _configuration.getBoolean(XMLConfiguration.USE_INTROSPECTION);
        Assert.assertEquals("useIntrospection", Boolean.TRUE, useIntrospection);
    }
}
