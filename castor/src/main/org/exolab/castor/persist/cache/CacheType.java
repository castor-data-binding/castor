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

/**
 * Enlists the available performance cache implementations.
 * 
 * @author <a href="werner DOT guttmann AT gmx DOT com">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class CacheType {
	
	/**
	 * Map type for no caching as the caching mechanism. All object
	 * put into the map will be discarded.
	 */
	public static final CacheType CACHE_NONE = new CacheType (1);
    public static final String CACHE_NONE_TEXT = "none";
	
	/**
	 * Map type of Count-Limited least-recently-used as caching mechanism.
	 * Object Lock which is not hold by any transcation will be put in the cache, until
	 * the cache is full and other object overwritten it.
	 */
	public static final CacheType CACHE_COUNT_LIMITED = new CacheType (2);
    public static final String CACHE_COUNT_LIMITED_TEXT = "count-limited";
	/**
	 * Map type of Time-Limited least-recently-used is used as caching mechanism.
	 * Object Lock which is not hold by any transcation will be put in the cache, until
	 * timeout is reached.
	 */
	public static final CacheType CACHE_TIME_LIMITED = new CacheType (3);
    public static final String CACHE_TIME_LIMITED_TEXT = "time-limited";
	/**
	 * Map type of unlimited cache as caching mechanism.
	 * Object Lock which is not hold by any transcation will be put in the cache 
	 * for later use.
	 */
	public static final CacheType CACHE_UNLIMITED = new CacheType (4);
    public static final String CACHE_UNLIMITED_TEXT = "unlimited";
    
	/**
	 * Holds the value of the cache type that this enumeration represents.
	 */
	private int _type = 0;
	
	
	/**
	 * Creates an instance of CacheType 
	 * @param type The cache type mnemonic.
	 */
	private CacheType (int type) {
		this._type = type;
	}
	
	/**
	 * Factory method for creating CacheType instances based on a String 
	 * representation of the cache type. 
	 * @param type String representation of the cache type.
	 * @return A CacheType instance.
	 * @throws InvalidCacheTypeException Indicate that the specified cache type does not exist.
	 */
	public static CacheType create (String type) 
	throws InvalidCacheTypeException 
	{
		CacheType cacheType = null;
		
		if ( type == null || type.equals("") )
			throw new InvalidCacheTypeException ("Required cache type not specified");
		else if ( type.toLowerCase().equals(CACHE_NONE_TEXT) )
			cacheType = CACHE_NONE;
		else if ( type.toLowerCase().equals(CACHE_COUNT_LIMITED_TEXT) )
			cacheType = CACHE_COUNT_LIMITED;
		else if ( type.toLowerCase().equals(CACHE_TIME_LIMITED_TEXT) )
			cacheType = CACHE_TIME_LIMITED;
		else if ( type.toLowerCase().equals(CACHE_UNLIMITED_TEXT) )
			cacheType = CACHE_UNLIMITED;
        else
            throw new InvalidCacheTypeException ("Specified cache type " + type + " not supported.");
        
		return cacheType;
		
	}
	
	public String toString () {
		
		String cacheType = null;
		
		switch (_type) {
		case 1:
			cacheType = CACHE_NONE_TEXT;
			break;
		case 2:
			cacheType = CACHE_COUNT_LIMITED_TEXT;
			break;
		case 3:
			cacheType = CACHE_TIME_LIMITED_TEXT;
			break;
		case 4:
			cacheType = CACHE_UNLIMITED_TEXT;;
			break;
		}
		
		return cacheType;
		
	}
	
	public boolean equals (Object cacheType) {
		if (cacheType != null)
			return this._type == ((CacheType) cacheType)._type;
		else 
			return false;
	}
	
}
