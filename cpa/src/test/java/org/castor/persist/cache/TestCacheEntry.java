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
        suite.addTest(new TestCacheEntry("testConstructor"));
        suite.addTest(new TestCacheEntry("testGetterSetter"));

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

    public void testConstructor() throws Exception {
        Object obj = new CacheEntry();
        assertTrue(obj instanceof CacheEntry);
    }

    public void testGetterSetter() throws Exception {
        CacheEntry ce = new CacheEntry();
        assertNull(ce.getOID());
        assertNull(ce.getEntry());
        assertEquals(TimeStampable.NO_TIMESTAMP, ce.getTimeStamp());

        OID oid = createOID();
        ce.setOID(oid);
        assertTrue(oid == ce.getOID());
        ce.setOID(null);
        assertNull(ce.getOID());
        
        Object[] entry = new Object[] {};
        ce.setEntry(entry);
        assertTrue(entry == ce.getEntry());
        ce.setEntry(null);
        assertNull(ce.getEntry());
        
        ce.setTimeStamp(Long.MAX_VALUE);
        assertTrue(Long.MAX_VALUE == ce.getTimeStamp());
        ce.setTimeStamp(Long.MIN_VALUE);
        assertTrue(Long.MIN_VALUE == ce.getTimeStamp());
    }
}
