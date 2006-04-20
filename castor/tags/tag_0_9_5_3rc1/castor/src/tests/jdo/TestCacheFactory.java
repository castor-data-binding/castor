/*
 * Created on Feb 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jdo;

import org.exolab.castor.persist.cache.Cache;
import org.exolab.castor.persist.cache.CacheFactory;
import org.exolab.castor.persist.cache.CacheType;
import org.exolab.castor.persist.cache.InvalidCacheTypeException;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestCacheFactory extends TestCase {
	/**
	 * Constructor for CacheFactoryTest.
	 * @param arg0
	 */
	public TestCacheFactory(String arg0) {
		super(arg0);
	}
    
	public void testCreate() throws Exception {
        
		Cache cache = null;
        
        cache = CacheFactory.create (CacheType.CACHE_NONE_TEXT, 0);
        assertEquals (CacheType.CACHE_NONE, cache.getCacheType());
        
        cache = CacheFactory.create (CacheType.CACHE_COUNT_LIMITED_TEXT, 0);
        assertEquals (CacheType.CACHE_COUNT_LIMITED, cache.getCacheType());

        cache = CacheFactory.create (CacheType.CACHE_TIME_LIMITED_TEXT, 0);
        assertEquals (CacheType.CACHE_TIME_LIMITED, cache.getCacheType());
        
        cache = CacheFactory.create (CacheType.CACHE_UNLIMITED_TEXT, 0);
        assertEquals (CacheType.CACHE_UNLIMITED, cache.getCacheType());

        try {
        	cache = CacheFactory.create ("cache-unknown", 0);
        }
        catch (InvalidCacheTypeException e) {
        	assertEquals (InvalidCacheTypeException.class.getName(), e.getClass().getName());
        }
	}
}
