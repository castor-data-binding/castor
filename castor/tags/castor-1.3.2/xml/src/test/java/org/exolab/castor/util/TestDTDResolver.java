/*
 * Copyright 2005 Werner Guttmann
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
package org.exolab.castor.util;

import junit.framework.TestCase;

import org.xml.sax.InputSource;

public class TestDTDResolver extends TestCase {

    /**
     * Castor-specific EntityResolver to test.
     */
    private DTDResolver _entityResolver;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        _entityResolver = new DTDResolver();
    }
    
    private void testSystemId(final String systemId) throws Exception {
        InputSource inputSource = _entityResolver.resolveEntity(null, systemId);
        assertNotNull(inputSource);
        assertEquals(systemId, inputSource.getSystemId());
    }
    
    public void testMappingDTD() throws Exception {
        testSystemId("http://castor.org/mapping.dtd");
    }
    
    public void testMappingDTDOld() throws Exception {
        testSystemId("http://castor.exolab.org/mapping.dtd");
    }
    
    public void testJDODTD() throws Exception {
        testSystemId("http://castor.org/jdo-conf.dtd");
    }
    
    public void testJdoDTDOld() throws Exception {
        testSystemId("http://castor.exolab.org/jdo-conf.dtd");
    }
    
    public void testMappingXSD() throws Exception {
        testSystemId("http://castor.org/mapping.xsd");
    }
    
    public void testMappingXSDOld() throws Exception {
        testSystemId("http://castor.exolab.org/mapping.xsd");
    }
    
    public void testJDOXSD() throws Exception {
        testSystemId("http://castor.org/jdo-conf.xsd");
    }
    
    public void testJdoXSDOld() throws Exception {
        testSystemId("http://castor.exolab.org/jdo-conf.xsd");
    }
}
