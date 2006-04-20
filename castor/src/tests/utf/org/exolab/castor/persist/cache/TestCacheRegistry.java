/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package utf.org.exolab.castor.persist.cache;

import java.util.Collection;
import java.util.Iterator;

import org.exolab.castor.persist.cache.Cache;
import org.exolab.castor.persist.cache.CacheAcquireException;
import org.exolab.castor.persist.cache.CacheRegistry;

import junit.framework.TestCase;

/**
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 */
public class TestCacheRegistry extends TestCase {
    
    private static final String JCACHE_CACHE_TYPE = "jcache";
    private static final String FKCACHE_CACHE_TYPE = "fkcache";
    private static final String JCS_NAME = "jcs";
    private static final String COHERENCE_NAME = "coherence";
    private static final String CLASS_NAME = "org.exolab.castor.entity.Sample";

    /**
     * @param arg0
     */
    public TestCacheRegistry(final String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testGetCacheFactories() throws Exception  {
        String[] cacheFactories = CacheRegistry.getCacheFactoriesNames();
        assertEquals (9, cacheFactories.length);
    }
    
    public void testGetCaches() throws Exception  {
        Collection factories = CacheRegistry.getCacheNames();
        Iterator iterator = factories.iterator();
        assertNotNull (iterator);
        assertEquals (true, iterator.hasNext());
    }
    
    private Cache getCache (String type, int capacity) throws Exception {
        return CacheRegistry.getCache(type, capacity, CLASS_NAME, getClass().getClassLoader());
    }
    
    public void testGetCache() throws Exception {
        Cache cache = null;
        
        cache = getCache ("time-limited", 10);
        assertEquals ("time-limited", cache.getCacheType());
        assertEquals (10, cache.getCapacity());
        
        cache = getCache("unlimited", 10); 
        assertEquals ("unlimited", cache.getCacheType());
        assertEquals (10, cache.getCapacity());
        
        cache = getCache("count-limited", 3); 
        assertEquals ("count-limited", cache.getCacheType());
        assertEquals (30, cache.getCapacity());
        
        cache = getCache("none", 10); 
        assertEquals ("none", cache.getCacheType());
        assertEquals (10, cache.getCapacity());

//        cache = CacheRegistry.getCache("custom", 15, 
//        "org.exolab.castor.entity.Sample", getClass().getClassLoader());
//        assertEquals ("custom", cache.getCacheType());
//        assertEquals (15, cache.getCapacity());
        
        cache = getCache("not-existing", 0); 
        assertNull (cache);

        cache = getCache("", 10); 
        assertEquals ("count-limited", cache.getCacheType());
        assertEquals (30, cache.getCapacity());
        
        cache = getCache(null, 10); 
        assertEquals ("count-limited", cache.getCacheType());
        assertEquals (30, cache.getCapacity());
    }

    public void testGetDistributedCaches() throws Exception {
        
        Cache cache = null;
        
        cache = getCache(COHERENCE_NAME, 10); 
        assertEquals (COHERENCE_NAME, cache.getCacheType());
        assertEquals (10, cache.getCapacity());

        cache = getCache(JCS_NAME, 10);
        assertEquals (JCS_NAME, cache.getCacheType());
        assertEquals (10, cache.getCapacity());

        cache = getCache(FKCACHE_CACHE_TYPE, 10); 
        assertEquals (FKCACHE_CACHE_TYPE, cache.getCacheType());
        assertEquals (10, cache.getCapacity());

        cache = getCache(JCACHE_CACHE_TYPE, 10); 
        assertEquals (JCACHE_CACHE_TYPE, cache.getCacheType());
        assertEquals (10, cache.getCapacity());

    }
    
    private void useCacheEngine(Cache cache) throws Exception {
        Object old = cache.put("key1", "value1");
        assertNull (old);
        boolean stored = cache.contains("key1");
        assertTrue (stored);
        boolean storedKey2 = cache.contains ("key2");
        assertFalse (storedKey2);
        Object got = cache.get("key1");
        assertNotNull (got);
        assertEquals ("value1", got);
    }
    
    public void testCoherenceCacheEngine() throws Exception {
        Cache cache = getCache(COHERENCE_NAME, 10); 
        assertEquals (COHERENCE_NAME, cache.getCacheType());
        assertEquals (10, cache.getCapacity());
        
        cache.initialize();
        useCacheEngine(cache);
        
    }

    public void testFKCacheEngine() throws Exception {
        Cache cache = getCache(FKCACHE_CACHE_TYPE, 10); 
        
        cache.initialize();
        useCacheEngine(cache);
        
    }

    public void testJCSEngine() throws Exception {
        Cache cache = getCache(JCS_NAME, 10); 
        
        cache.initialize();
        useCacheEngine(cache);
        
    }

//    public void testJCacheEngine() throws Exception {
//        JCache cache = null;
//        
//        cache = (JCache) CacheRegistry.getCache("jcache", 10, 
//                "org.exolab.castor.entity.Sample", 
//                getClass().getClassLoader());
//        
//        cache.init();
//        useCacheEngine(cache);
//        
//    }

    
}
