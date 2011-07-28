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
package org.castor.persist.cache;

import java.lang.reflect.Constructor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.persist.OID;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestCacheEntry extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("CacheEntry Tests");

        suite.addTest(new TestCacheEntry("testOIDConstructor"));
        suite.addTest(new TestCacheEntry("testCacheEntry"));

        return suite;
    }

    public TestCacheEntry(final String name) { super(name); }

    public void testOIDConstructor() throws Exception {
        Object object = createOID();
        assertTrue(object instanceof OID);
    }

    public void testCacheEntry() throws Exception {
        CacheEntry ce1 = new CacheEntry(null, null, TimeStampable.NO_TIMESTAMP);
        assertNull(ce1.getOID());
        assertNull(ce1.getValues());
        assertEquals(TimeStampable.NO_TIMESTAMP, ce1.getVersion());
        
        OID oid = createOID();
        Object[] entry = new Object[] {};
        
        CacheEntry ce2 = new CacheEntry(oid, entry, Long.MAX_VALUE);
        assertTrue(oid == ce2.getOID());
        assertTrue(entry == ce2.getValues());
        assertTrue(Long.MAX_VALUE == ce2.getVersion());

        
        CacheEntry ce3 = new CacheEntry(oid, entry, Long.MIN_VALUE);
        assertTrue(oid == ce3.getOID());
        assertTrue(entry == ce3.getValues());
        assertTrue(Long.MIN_VALUE == ce3.getVersion());
    }

    private OID createOID() throws Exception {
        Constructor<OID> constructor = OID.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
