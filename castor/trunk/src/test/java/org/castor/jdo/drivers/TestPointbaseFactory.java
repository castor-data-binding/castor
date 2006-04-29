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
package org.castor.jdo.drivers;

import junit.framework.TestCase;

import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * UTF test case for {@see org.castor.jdo.drivers.PointbaseFactory}.
 */
public final class TestPointbaseFactory extends TestCase {
    /**
     * Tests whether the factory name returned equals "pointbase"
     * @throws Exception
     */
    public void testGetName() throws Exception {
        PersistenceFactory factory = new PointbaseFactory();
        assertNotNull(factory);
        assertEquals("pointbase", factory.getFactoryName());
    }

    /**
     * Tests whether the QueryExpression instance returned by the 
     * {@see PointbaseFactory} is of type 
     * {@see org.castor.jdo.drivers.PointbaseQueryExpression}.
     * @throws Exception
     */
    public void testGetQueryExpression() throws Exception {
        PersistenceFactory factory = new PointbaseFactory();
        assertNotNull(factory);
        
        QueryExpression expression = factory.getQueryExpression();
        assertNotNull(expression);
        
        assertEquals(PointbaseQueryExpression.class.getName(), 
                expression.getClass().getName());
    }

}
