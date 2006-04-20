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


import java.util.Enumeration;

/**
 * Base implementation of all LRU cache types. 
 *
 * @author <a href="werner DOT guttmannt AT gmx DOT com">Werner Guttmann</a>
 */

public abstract class AbstractBaseCache 
implements Cache 
{

    /**
     * Indicates the type of this cache.
     */
    private CacheType _cacheType;
    
	/**
	 * Maps the specified <code>key</code> to the specified 
	 * <code>value</code> in this hashtable. Neither the key nor the 
	 * value can be <code>null</code>. 
	 * <p>
	 * The value can be retrieved by calling the <code>get</code> method 
	 * with a key that is equal to the original key, before it is diposed
	 * by the least-recently-used map. 
	 * <p>
	 * @param      key     the hashtable key.
	 * @param      value   the value.
	 * @return     the previous value of the specified key in this hashtable,
	 *             or <code>null</code> if it did not have one.
	 * @exception  NullPointerException  if the key or value is
	 *               <code>null</code>.
	 */
	public abstract Object put(Object key, Object value);
	
	/**
	 *Returns the value to which the specified key is mapped in this hashtable.
	 *@param key - a key in the hashtable.
	 *@return the value to which the key is mapped in this hashtable; null if 
	 * the key is not mapped to any value in this hashtable.
	 */
	public abstract Object get(Object key);
	
	/**
	 * Removes the key (and its corresponding value) from this 
	 * hashtable. This method does nothing if the key is not in the hashtable.
	 *
	 * @param   key   the key that needs to be removed.
	 * @return  the value to which the key had been mapped in this hashtable,
	 *          or <code>null</code> if the key did not have a mapping.
	 */
	public abstract Object remove(Object key);
	
	/**
	 * Returns an enumeration of the values in this LRU map.
	 * Use the Enumeration methods on the returned object to fetch the elements
	 * sequentially.
	 *
	 * @return  an enumeration of the values in this hashtable.
	 * @see     java.util.Enumeration
	 */
	public abstract Enumeration elements();
	
	/**
	 * Remove the object identified by key from the cache.
	 *
	 * @param   key   the key that needs to be removed.
	 */
	public abstract void expire(Object key);
    
    
    /**
     * Indicates whether the cache holds value object mapped to the specified key.
     * @param key - A key identifying a value object.
     * @return True if the cache holds a value object for the specified key, false otherwise.
     */
    public abstract boolean contains(Object key);
    
    /**
     * Indicates the type of this cache.
     * @return the cache type.
     */	
    public CacheType getCacheType() {
		return _cacheType;
	}
     
     /**
      * Sets the type of this cache instance.
      * @param cacheType the type of this cache.
      */
     public void setCacheType(CacheType cacheType) {
        this._cacheType = cacheType;
	}
}
