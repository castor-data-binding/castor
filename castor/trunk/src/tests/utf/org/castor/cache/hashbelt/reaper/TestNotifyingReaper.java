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
package utf.org.castor.cache.hashbelt.reaper;

import java.util.List;

import org.castor.cache.hashbelt.container.Container;
import org.castor.cache.hashbelt.container.MapContainer;
import org.castor.cache.hashbelt.reaper.AbstractReaper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestNotifyingReaper extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("NotifyingReaper Tests");

        suite.addTest(new TestNotifyingReaper("test"));

        return suite;
    }

    public TestNotifyingReaper(final String name) { super(name); }
    
    public void test() {
        NotifyingReaperMock.getExpiredObjects().clear();
        
        Container container = new MapContainer();
        for (int i = 0; i < 10; i++) {
            container.put(new Integer(i), Integer.toString(i));
        }
        
        AbstractReaper reaper = new NotifyingReaperMock();
        reaper.handleExpiredContainer(container);
        assertEquals(10, container.size());
        
        List expired = NotifyingReaperMock.getExpiredObjects();
        assertEquals(10, expired.size());
        for (int i = 0; i < 10; i++) {
            assertTrue(expired.contains(Integer.toString(i)));
        }
    }
}
