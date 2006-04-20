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
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * NoCache is a Map which dispose all object right the way.
 * <p>
 * Every object being put in the Map will be disposed. 
 * <p>
 * Method {@link #dispose(Object)} will be called whenever an 
 * old object is diposed. Developer can get notify by overriding
 * the dispose method {@link #dispose(Object)}.
 *
 * @author <a href="werner DOT guttmann AT gmx DOT com">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class NoCache 
extends AbstractBaseCache
implements Cache 
{
	
	/**
	 * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
	 * Commons Logging</a> instance used for all logging.
	 */
	private static Log _log = LogFactory.getFactory().getInstance(NoCache.class);
	
	/**
	 * Maps the specified <code>key</code> to the specified 
	 * <code>value</code> in this Map. Neither the key nor the 
	 * value can be <code>null</code>. End of theory.
	 * <p>
	 * Every object being put in the Map will be disposed.
	 * <p>
	 * @param      key     the Map key.
	 * @param      value   the value.
	 * @return     the previous value of the specified key in this Map,
	 *             or <code>null</code> if it did not have one.
	 * @exception  NullPointerException  if the key or value is
	 *               <code>null</code>.
	 */
	public synchronized Object put( Object key, Object value ) {
		if (_log.isDebugEnabled()) {
			_log.trace ("Creating cache entry for key " + key + " with value " + value);
		}
		dispose( value );
		return null;
	}
	
	/**
	 *Returns the value to which the specified key is mapped in this Map.
	 *@param key - a key in the Map.
	 *@return the value to which the key is mapped in this Map; null if 
	 * the key is not mapped to any value in this Map.
	 */
	public synchronized Object get( Object key ) {
		if (_log.isDebugEnabled()) {
			_log.trace ("Getting cache entry for key " + key);
		}
		return null;
	}
	
	/**
	 * Removes the key (and its corresponding value) from this 
	 * Map. This method does nothing if the key is not in the Map.
	 *
	 * @param   key   the key that needs to be removed.
	 * @return  the value to which the key had been mapped in this Map,
	 *          or <code>null</code> if the key did not have a mapping.
	 */
	public synchronized Object remove( Object key ) {
		if (_log.isDebugEnabled()) {
			_log.trace ("Removing cache entry for key " + key);
		}
		return null;
	}
	
	/**
	 * Returns an enumeration of the values in this LRU map.
	 * Use the Enumeration methods on the returned object to fetch the elements
	 * sequentially.
	 *
	 * @return  an enumeration of the values in this Map.
	 * @see     java.util.Enumeration
	 */
	public Enumeration elements() {
		return new EmptyEnumeration();
	}
	
	/**
	 * Remove the object identified by key from the cache.
	 *
	 * @param   key   the key that needs to be removed.
	 */
	public void expire(Object key) {
		if (_log.isDebugEnabled()) {
			_log.trace ("Expiring cache entry for key " + key);
		}
		dispose(key);
	}
	
	/**
	 * This method is called when an object is disposed.
	 * Override this method if you interested in the disposed object.
	 */
	protected void dispose( Object o ) {
		if (_log.isDebugEnabled()) {
			_log.trace ("Disposing object " + o);
		}
	}
	
	private class EmptyEnumeration implements Enumeration {
		
		/**
		 * Creates an instance of this class.
		 */
		private EmptyEnumeration() {
            // creates ....
		}
		public boolean hasMoreElements() {
			return false;
		}
		public Object nextElement() throws NoSuchElementException {
			throw new NoSuchElementException();
		}
	}
	
	/* Indicates whether the cache holds a valuze object for the specified key.
	 * @see org.exolab.castor.persist.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(Object key) {
		return false;
	}
	
}