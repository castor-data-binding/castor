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
package org.exolab.castor.persist.cache;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 */
public class TestCacheRegistry extends TestCase {
    
    /**
     * @param arg0
     */
    public TestCacheRegistry(String arg0) {
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
		String[] cacheFactories = CacheRegistry.getCacheFactories();
        assertEquals (5, cacheFactories.length);
	}
    
	public void testGetCaches() throws Exception  {
        Collection factories = CacheRegistry.getCaches();
        Iterator iterator = factories.iterator();
        assertNotNull (iterator);
        assertEquals (true, iterator.hasNext());
	}

    public void testGetCache() throws Exception {
        Cache cache = null;
        
        cache = CacheRegistry.getCache("time-limited", 10);
        assertEquals ("time-limited", cache.getCacheType());
        assertEquals (10, cache.getCapacity());
        
        cache = CacheRegistry.getCache("unlimited", 10);
        assertEquals ("unlimited", cache.getCacheType());
        assertEquals (10, cache.getCapacity());
        
        cache = CacheRegistry.getCache("count-limited", 3);
        assertEquals ("count-limited", cache.getCacheType());
        assertEquals (3, cache.getCapacity());
        
        cache = CacheRegistry.getCache("none", 10);
        assertEquals ("none", cache.getCacheType());
        assertEquals (10, cache.getCapacity());

        cache = CacheRegistry.getCache("custom", 15);
        assertEquals ("custom", cache.getCacheType());
        assertEquals (15, cache.getCapacity());
        
        cache = CacheRegistry.getCache("not-existing", 10);
        assertNull (cache);

        cache = CacheRegistry.getCache("", 10);
        assertEquals ("count-limited", cache.getCacheType());
        assertEquals (10, cache.getCapacity());
        
        cache = CacheRegistry.getCache(null, 10);
        assertEquals ("count-limited", cache.getCacheType());
        assertEquals (10, cache.getCapacity());
    }
    
}
