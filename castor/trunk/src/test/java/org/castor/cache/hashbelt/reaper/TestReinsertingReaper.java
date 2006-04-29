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
package org.castor.cache.hashbelt.reaper;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.hashbelt.container.Container;
import org.castor.cache.hashbelt.container.MapContainer;
import org.castor.cache.simple.Unlimited;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestReinsertingReaper extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("ReinsertingReaper Tests");

        suite.addTest(new TestReinsertingReaper("test"));

        return suite;
    }

    public TestReinsertingReaper(final String name) { super(name); }
    
    public void test() {
        ReinsertingReaperMock.getExpiredObjects().clear();
        
        Cache cache = new Unlimited();
        
        Container container = new MapContainer();
        for (int i = 0; i < 10; i++) {
            container.put(new Integer(i), Integer.toString(i));
        }
        
        AbstractReaper reaper = new ReinsertingReaperMock();
        reaper.setCache(cache);
        reaper.handleExpiredContainer(container);
        assertEquals(10, container.size());
        
        assertEquals(10, cache.size());
        for (int i = 0; i < 10; i++) {
            Object key = new Integer(i);
            assertTrue(cache.containsKey(key));
            assertEquals(cache.get(key), Integer.toString(i));
        }

        List expired = ReinsertingReaperMock.getExpiredObjects();
        assertEquals(10, expired.size());
        for (int i = 0; i < 10; i++) {
            assertTrue(expired.contains(Integer.toString(i)));
        }
    }
}
