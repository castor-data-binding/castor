/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.cache;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestAbstractBaseCache extends TestCase {
    private Cache _cache;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("AbstractBaseCache Tests");

        suite.addTest(new TestAbstractBaseCache("testInitialize"));

        suite.addTest(new TestAbstractBaseCache("testExpire"));
        suite.addTest(new TestAbstractBaseCache("testExpireAll"));

        return suite;
    }

    public TestAbstractBaseCache(final String name) { super(name); }

    protected void setUp() {
        _cache = new CacheMock();
    }

    protected void tearDown() {
        _cache = null;
    }
    
    public void testInitialize() {
        Properties params;
        
        assertEquals("", _cache.getName());
        
        try {
            params = new Properties();
            params.put("foobar", "wrong parameter name");

            _cache.initialize(params);
            assertEquals("", _cache.getName());
        } catch (CacheAcquireException ex) {
            fail("Failed at initialize of AbstractBaseCache");
        }
        
        try {
            params = new Properties();
            params.put(Cache.PARAM_NAME, new Integer(1234));

            _cache.initialize(params);
            assertEquals("", _cache.getName());
        } catch (CacheAcquireException ex) {
            fail("Failed at initialize of AbstractBaseCache");
        }
        
        try {
            params = new Properties();
            params.put(Cache.PARAM_NAME, "this should be ok");

            _cache.initialize(params);
            assertEquals("this should be ok", _cache.getName());
        } catch (CacheAcquireException ex) {
            fail("Failed at initialize of AbstractBaseCache");
        }
        
        _cache.close();
        assertEquals("this should be ok", _cache.getName());
    }

    public void testExpire() {
        _cache.expire("third key");

        assertTrue(_cache.containsKey("first key"));
        assertTrue(_cache.containsKey("second key"));
        assertFalse(_cache.containsKey("third key"));
        assertFalse(_cache.containsKey("fourth key"));
        assertFalse(_cache.containsKey("fifth key"));
    }

    public void testExpireAll() {
        _cache.expireAll();

        assertFalse(_cache.containsKey("first key"));
        assertFalse(_cache.containsKey("second key"));
        assertFalse(_cache.containsKey("third key"));
        assertFalse(_cache.containsKey("fourth key"));
        assertFalse(_cache.containsKey("fifth key"));
    }
}
