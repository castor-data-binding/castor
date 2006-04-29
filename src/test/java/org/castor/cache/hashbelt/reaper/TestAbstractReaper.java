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

import org.castor.cache.Cache;
import org.castor.cache.hashbelt.reaper.AbstractReaper;
import org.castor.cache.simple.NoCache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestAbstractReaper extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("AbstractReaper Tests");

        suite.addTest(new TestAbstractReaper("test"));

        return suite;
    }

    public TestAbstractReaper(final String name) { super(name); }
    
    public void test() {
        AbstractReaper reaper = new AbstractReaperMock();
        
        assertNull(reaper.getCache());
        Cache cache = new NoCache();
        reaper.setCache(cache);
        assertNotNull(reaper.getCache());
        assertTrue(cache == reaper.getCache());
    }
}
