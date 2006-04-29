/*
 * Copyright 2006 Ralf Joachim
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
package org.castor.cache.hashbelt.container;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestMapContainer extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("MapContainer Tests");

        suite.addTest(new TestMapContainer("testConstructor"));
        suite.addTest(new TestMapContainer("testTimestamp"));
        suite.addTest(new TestMapContainer("testKeyIterator"));
        suite.addTest(new TestMapContainer("testValueIterator"));

        return suite;
    }

    public TestMapContainer(final String name) { super(name); }
    
    public void testConstructor() {
        Object container = new MapContainer();
        assertTrue(container instanceof Container);
        assertTrue(container instanceof MapContainer);
    }
    
    public void testTimestamp() {
        Container container = new MapContainer();
        assertEquals(0L, container.getTimestamp());
        
        long before = System.currentTimeMillis();
        container.updateTimestamp();
        long after = System.currentTimeMillis();
        assertTrue(before <= container.getTimestamp());
        assertTrue(after >= container.getTimestamp());
    }
    
    public void testKeyIterator() {
        Container container = new MapContainer();
        Iterator iter = container.keyIterator();
        assertNotNull(iter);
        assertFalse(iter.hasNext());
        
        for (int i = 0; i < 10; i++) {
            container.put(new Integer(i), Integer.toString(i));
        }
        
        iter = container.keyIterator();
        assertNotNull(iter);
        for (int i = 0; i < 10; i++) {
            assertTrue(iter.hasNext());
            Object obj = iter.next();
            assertTrue(obj instanceof Integer);
            assertTrue(((Integer) obj).intValue() >= 0);
            assertTrue(((Integer) obj).intValue() < 10);
        }
    }
    
    public void testValueIterator() {
        Container container = new MapContainer();
        Iterator iter = container.valueIterator();
        assertNotNull(iter);
        assertFalse(iter.hasNext());
        
        for (int i = 0; i < 10; i++) {
            container.put(Integer.toString(i), new Integer(i));
        }
        
        iter = container.valueIterator();
        assertNotNull(iter);
        for (int i = 0; i < 10; i++) {
            assertTrue(iter.hasNext());
            Object obj = iter.next();
            assertTrue(obj instanceof Integer);
            assertTrue(((Integer) obj).intValue() >= 0);
            assertTrue(((Integer) obj).intValue() < 10);
        }
    }
}
