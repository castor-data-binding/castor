/*
 * Created on Feb 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jdo;

import org.exolab.castor.persist.cache.CacheType;
import org.exolab.castor.persist.cache.InvalidCacheTypeException;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestCacheType extends TestCase {
    
    /**
     * Craetes an instance of this class.
     * @param arg0
     */
    public TestCacheType(String arg0) {
        super(arg0);
    }
    
	public void testCreate() throws Exception {
        
        CacheType cacheType = null;
        
        cacheType = CacheType.create(CacheType.CACHE_NONE_TEXT);
        assertEquals (CacheType.CACHE_NONE, cacheType);

        cacheType = CacheType.create(CacheType.CACHE_COUNT_LIMITED_TEXT);
        assertEquals (CacheType.CACHE_COUNT_LIMITED, cacheType);

        cacheType = CacheType.create(CacheType.CACHE_TIME_LIMITED_TEXT);
        assertEquals (CacheType.CACHE_TIME_LIMITED, cacheType);
        
        cacheType = CacheType.create(CacheType.CACHE_UNLIMITED_TEXT);
        assertEquals (CacheType.CACHE_UNLIMITED, cacheType);

        try {
        	cacheType = CacheType.create("not-existing");
        }
        catch (InvalidCacheTypeException e) {
        	assertEquals ("org.exolab.castor.persist.cache.InvalidCacheTypeException",
        			e.getClass().getName());
        }
        
    }
	/*
	 * Method to test for String toString()
	 */
	public void testToString() {
        
        assertEquals (CacheType.CACHE_NONE.toString(), CacheType.CACHE_NONE_TEXT); 
        assertEquals (CacheType.CACHE_COUNT_LIMITED.toString(), CacheType.CACHE_COUNT_LIMITED_TEXT); 
        assertEquals (CacheType.CACHE_TIME_LIMITED.toString(), CacheType.CACHE_TIME_LIMITED_TEXT);
        assertEquals (CacheType.CACHE_UNLIMITED.toString(), CacheType.CACHE_UNLIMITED_TEXT); 
        
    }
    
    /*
     * Method to test for String toString()
     */
    public void testEquals() {
        
        assertEquals (true, CacheType.CACHE_NONE.equals (CacheType.CACHE_NONE)); 
        assertEquals (true, CacheType.CACHE_COUNT_LIMITED.equals (CacheType.CACHE_COUNT_LIMITED)); 
        assertEquals (true, CacheType.CACHE_TIME_LIMITED.equals (CacheType.CACHE_TIME_LIMITED)); 
        assertEquals (true, CacheType.CACHE_UNLIMITED.equals (CacheType.CACHE_UNLIMITED)); 
        
    }
    
}
