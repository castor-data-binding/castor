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
 * $Id: LRU.java
 */

package org.exolab.castor.persist.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Factory for creating Cache instances.
 * 
 * @author <a href="werner DOT guttmann AT gmx DOT com">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class CacheFactory {
	
    /** 
     * Default cache type identifier
     */
    public final static String DEFAULT_TYPE_IDENTIFIER = "count-limited";
    
	/** 
	 * Default LRU mechanism
	 */
	public final static CacheType DEFAULT_TYPE = CacheType.CACHE_COUNT_LIMITED;
	/**
	 * Specify the default LRU parameter
	 */
	public final static int DEFAULT_PARAM = 100;
	
	/**
	 * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
	 * Commons Logging</a> instance used for all logging.
	 */
	private static Log _log = LogFactory.getFactory().getInstance( CacheFactory.class );
	
	
	/**
	 * Factory method to create a LRU map of specified type.
	 *
	 * @param type   mechanism type
	 * @param param   capacity of the lru
	 */
	public static Cache create( String type, int param ) 
	    throws InvalidCacheTypeException 
    {
		
		Cache cache = null;
		CacheType cacheType = null;
        
        if (type == null) {
        	type = DEFAULT_TYPE_IDENTIFIER;
        }
		
		try {
			cacheType = CacheType.create (type);
		}
		catch (InvalidCacheTypeException e) {
			_log.error ("Invalid cache type encountered", e);
            throw e;
		}
        
		if (cacheType == DEFAULT_TYPE) {
			param = DEFAULT_PARAM;
		}
		
		if (cacheType == CacheType.CACHE_COUNT_LIMITED) {
			if ( param > 0 ) 
				cache = new CountLimited( param );
			else 
				cache = new NoCache();
		}
		else if (cacheType == CacheType.CACHE_TIME_LIMITED) {
			if ( param > 0 ) 
				cache = new TimeLimited( param );
			else 
				cache = new NoCache();
		}
		else if (cacheType == CacheType.CACHE_UNLIMITED) {
			cache = new Unlimited();
		}
		else if (cacheType == CacheType.CACHE_NONE) {
			cache = new NoCache();
		}
		
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating cache instance for type " + cacheType.toString());
		}
        
        // setting cache type
        cache.setCacheType(cacheType);
		
		return cache;
	}
	
}
