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
 */

package org.exolab.castor.persist;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * <p>A count limted LRU hashtable for caching objects.
 * 
 * <p>
 * Every object being put in the hashtable will live for until the
 * hashtable is full, then an object which <em>one of the</em> 
 * least recent used object will be disposed. 
 *
 * <p>
 * If you are interested in the disposing object, extend it class
 * and override the method <code>dispose(Object o)</code>.
 * Otherwise, the object is silently discarded.
 *
 * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
 */
public class NoCache implements LRU {

	/**
	 * ...work like Hashtable's <code>put</code>...except it's time limited
	 */
	public synchronized Object put( Object key, Object object ) {
		dispose( object );
		return null;
	}
	/**
	 * ...work like Hashtable's <code>get</code>...except it's time limited
	 */
	public synchronized Object get( Object key ) {
		return null;
	}
	/**
	 * ...work like Hashtable's <code>remove</code>...except it's time limited
	 */
	public synchronized Object remove( Object key ) {
		return null;
	}
	/*
	 * ...work like Hashtable's <code>elements</code>...except it's time limited
	 */
	public Enumeration elements() {
		return new EmptyEnumeration();
	}
	/**
	 * This method is called when an object is disposed.
	 * Override this method if you interested in the disposed object.
	 */
	protected void dispose( Object o ) {
		//System.out.println("dispose: "+o+" by NoCache.");
	}
	private class EmptyEnumeration implements Enumeration {

		private EmptyEnumeration() {
		}
		public boolean hasMoreElements() {
			return false;
		}
		public Object nextElement() throws NoSuchElementException {
			throw new NoSuchElementException();
		}
	}
}