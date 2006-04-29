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

import org.castor.persist.cache.CacheEntry;

import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.persist.ObjectLock;
import org.exolab.castor.persist.OID;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestCacheEntry extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("CacheEntry Tests");

        suite.addTest(new TestCacheEntry("testOIDConstructor"));
        suite.addTest(new TestCacheEntry("testCacheEntryConstructor"));
        suite.addTest(new TestCacheEntry("testObjectLockConstructor"));

        suite.addTest(new TestCacheEntry("testGetterSetter"));
        suite.addTest(new TestCacheEntry("testConstructor"));

        return suite;
    }

    public TestCacheEntry(final String name) { super(name); }

    public void testOIDConstructor() throws Exception {
        Constructor constructor = OID.class.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        Object object = constructor.newInstance(null);
        assertTrue(object instanceof OID);
    }

    private OID createOID() throws Exception {
        Constructor constructor = OID.class.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        return (OID) constructor.newInstance(null);
    }

    public void testCacheEntryConstructor() throws Exception {
        Constructor constructor = CacheEntry.class.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        Object obj = constructor.newInstance(null);
        assertTrue(obj instanceof CacheEntry);
    }

    private CacheEntry createCacheEntry() throws Exception {
        Constructor constructor = CacheEntry.class.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        return (CacheEntry) constructor.newInstance(null);
    }

    public void testObjectLockConstructor() throws Exception {
        CacheEntry ce = createCacheEntry();
        Object object = new ObjectLock(ce);
        assertTrue(object instanceof ObjectLock);
    }

    public void testGetterSetter() throws Exception {
        CacheEntry ce = createCacheEntry();
        assertNull(ce.getOID());
        assertNull(ce.getEntry());
        assertEquals(TimeStampable.NO_TIMESTAMP, ce.getTimeStamp());

        OID oid = createOID();
        ce.setOID(oid);
        assertTrue(oid == ce.getOID());
        ce.setOID(null);
        assertNull(ce.getOID());
        
        Object entry = new Object();
        ce.setEntry(entry);
        assertTrue(entry == ce.getEntry());
        ce.setEntry(null);
        assertNull(ce.getEntry());
        
        ce.setTimeStamp(Long.MAX_VALUE);
        assertTrue(Long.MAX_VALUE == ce.getTimeStamp());
        ce.setTimeStamp(Long.MIN_VALUE);
        assertTrue(Long.MIN_VALUE == ce.getTimeStamp());
    }

    public void testConstructor() throws Exception {
        OID oid = createOID();
        Object entry = new Object();

        CacheEntry src = createCacheEntry();
        src.setOID(oid);
        src.setEntry(entry);
        src.setTimeStamp(Long.MAX_VALUE);

        ObjectLock ol = new ObjectLock(src);
        
        CacheEntry dest = new CacheEntry(ol);
        assertTrue(oid == dest.getOID());
        assertTrue(entry == dest.getEntry());
        assertTrue(Long.MAX_VALUE == dest.getTimeStamp());
    }
}
