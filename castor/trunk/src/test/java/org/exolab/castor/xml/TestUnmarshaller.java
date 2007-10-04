/*
 * Copyright 2007 Werner Guttmann
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
package org.exolab.castor.xml;

import junit.framework.TestCase;

import org.castor.xml.XMLConfiguration;

/**
 * Test case for testing various pieces of functionality of {@link Unmarshaller}.
 */
public class TestUnmarshaller extends TestCase {
    
    /**
     * Tests usage of get-/setProperty() methods.
    */
    public void testSetProperty() {
        
        XMLContext xmlContext = new XMLContext();
        Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
        assertNotNull(unmarshaller);
        
        String lenientSequenceValidation = 
            unmarshaller.getProperty(XMLConfiguration.LENIENT_SEQUENCE_ORDER);
        assertNotNull(lenientSequenceValidation);
        assertEquals("false", lenientSequenceValidation);
        
        unmarshaller.setProperty(XMLConfiguration.LENIENT_SEQUENCE_ORDER, "true");
 
        lenientSequenceValidation = 
            unmarshaller.getProperty(XMLConfiguration.LENIENT_SEQUENCE_ORDER);
        assertNotNull(lenientSequenceValidation);
        assertEquals("true", lenientSequenceValidation);
    }

}
