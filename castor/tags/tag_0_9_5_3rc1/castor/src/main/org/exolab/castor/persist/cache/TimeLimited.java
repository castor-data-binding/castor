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
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * TimeLimited is a time limted least-recently-used <tt>Map</tt>.
 * <p>
 * Every object being put in the Map will live until the timeout
 * expired. 
 * <p>
 * Method {@link #dispose(Object)} will be called whenever an 
 * old object is diposed. Developer can get notify by overriding
 * the dispose method {@link #dispose(Object)}.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 * @author <a href="werner DOT guttmann AT gmx DOT com">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class TimeLimited 
extends AbstractBaseCache
implements Cache 
{
	
	/**
	 * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
	 * Commons Logging</a> instance used for all logging.
	 */
	private static Log _log = LogFactory.getFactory().getInstance(TimeLimited.class);
	
	/**
	 *	The Default precision in millisecond is 1000. Percision is the interval 
	 *  between each time which the timer thread will wake up and trigger 
	 *  clean up of least-recently-used Object.
	 */
	public final static int DEFAULT_PRECISION = 1000;
	
	/**
	 * Thread instance responsible for removing least-recently-used objects
     * after the specified interval.
	 */
	private static TimeThread ticker = new TimeThread( DEFAULT_PRECISION );
	
	private int interval;
	private int tailtime;
	private QueueItem head;
	private QueueItem tail;
	private Hashtable map;
	
	/**
	 * Creates an instance of TimeLimited. 
	 *
	 * @param interval the number of ticks an object lives before it will be disposed.
	 */
	public TimeLimited( int interval ) {
		map = new Hashtable();
		this.interval = interval+1;
		ticker.addListener( this );
	}
	
	/**
	 * Maps the specified <code>key</code> to the specified 
	 * <code>value</code> in this Map. Neither the key nor the 
	 * value can be <code>null</code>. 
	 * <p>
	 * The value can be retrieved by calling the <code>get</code> method 
	 * with a key that is equal to the original key, before it is diposed
	 * when the timeout of the entry is expired. 
	 * <p>
	 * @param      key     the Map key.
	 * @param      value   the value.
	 * @return     the previous value of the specified key in this Map,
	 *             or <code>null</code> if it did not have one.
	 * @exception  NullPointerException  if the key or value is
	 *               <code>null</code>.
	 */
	public synchronized Object put(Object key, Object value) {
		QueueItem oldItem = (QueueItem) map.get(key);
		if ( oldItem != null ) {
			Object oldObject = oldItem.item;
			oldItem.item = value;
			remove(oldItem);
			add(oldItem);
			return oldObject;
		} else {
			QueueItem newitem = new QueueItem(key,value);
			map.put(key,newitem);
			add(newitem);
			return null;
		}
	}
	
	/**
	 *Returns the value to which the specified key is mapped in this Map.
	 *@param key - a key in the Map.
	 *@return the value to which the key is mapped in this Map; null if 
	 * the key is not mapped to any value in this Map.
	 */
	public synchronized Object get(Object key) {
		Object o = map.get(key);
		if ( o == null )
			return null;
		else 
			return ((QueueItem)o).item;
	}
	
	/**
	 * Removes the key (and its corresponding value) from this 
	 * Map. This method does nothing if the key is not in the Map.
	 *
	 * @param   key   the key that needs to be removed.
	 * @return  the value to which the key had been mapped in this Map,
	 *          or <code>null</code> if the key did not have a mapping.
	 */
	public synchronized Object remove(Object key) {
		
		QueueItem queueItem = (QueueItem) map.remove(key);
		if (queueItem == null) {
            if (_log.isDebugEnabled()) {
			_log.trace ("TimeLimiteLRU: not in cache ... remove(" + key + ")");
            }
			return null;
		} else {
            if (_log.isDebugEnabled()) {
			_log.trace ("TimeLimiteLRU: remove(" + key + ") = " + queueItem.item);
            }
			remove(queueItem);
			return queueItem.item;
		}
	}
	
	/**
	 * Returns an enumeration of the values in this LRU map.
	 * Use the Enumeration methods on the returned object to fetch the elements
	 * sequentially.
	 *
	 * @return  an enumeration of the values in this Map.
	 * @see     java.util.Enumeration
	 */
	public synchronized Enumeration elements() {
		return new ValuesEnumeration(map.elements());
	}
	
	/**
	 * Remove the object identified by key from the cache.
	 *
	 * @param   key   the key that needs to be removed.
	 */
	public void expire(Object key) {
		// remove key from map, object will ultimately be
		// removed from queue when interval expires or a subsequent
		// call to put() overwrites the reference to it in QueueItem
		remove(key);
		dispose(key);
	}
	
	/* Indicates whether the cache holds a valuze object for the specified key.
	 * @see org.exolab.castor.persist.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(Object key) {
		if (_log.isDebugEnabled()) {
			_log.trace ("Testing for entry for key " + key);
		}
		return (this.get(key) != null);
	}
	
	/**
	 * This method is called when an object is disposed.
	 * Override this method if you interested in the disposed object.
	 *
	 * @param o - the disposed object
	 */
	protected void dispose( Object o ) {
		if (_log.isDebugEnabled()) 
			_log.trace("Disposing " + o);
	}
	
	private void remove(QueueItem item) {
		//_log.trace ( "removing: <" + item + "> while...head=<"+head+"> tail=<"+tail+">" );
		QueueItem temp;
		if ( item == null ) 
			throw new NullPointerException();
		
		if ( item == head ) {
			temp = item;
			
			head = head.next;
			if ( head == null ) {
				tail = null;
			} else {
				head.prev = null;
				head.time += temp.time;
			}
			temp.prev = null;
			temp.next = null;
			temp.time = 0;
		} else if ( item == tail ) {
			tail = tail.prev;
			tailtime = 0;
		} else {
			temp = item;
			
			temp.prev.next = temp.next;
			temp.next.prev = temp.prev;
			temp.next.time += temp.time;
			
			temp.prev = null;
			temp.next = null;
			temp.time = 0;
		}
	}
	
	private void add(QueueItem item) {
		ticker.startTick();
		if ( head == null ) {
			head = tail = item;
			item.prev = null;
			item.next = null;
			item.time = interval;
			tailtime = interval;
		} else {
			tail.next = item;
			item.prev = tail;
			item.next = null;
			item.time = interval-tailtime;
			tailtime = interval;
			tail = item;
		}
	}
	
	/* 
	 * call by ticker daemon
	 */
	private synchronized void tick() {
		QueueItem temp;
		Object o;
		
		if ( head != null ) {
			head.time--;
			tailtime--;
		}
		while ( head != null && head.time <= 0 ) {
			
			temp = head;
			
			o = head.item;
			remove(temp);
			map.remove(temp.key);
			dispose(o);
		}
		//		if ( head == null ) {
		//			ticker.stopTick();
		//		}
	}
	
	private class ValuesEnumeration implements Enumeration {
		private Vector v = new Vector();
		private int cur;
		
		private ValuesEnumeration( Enumeration e ) {
			while ( e.hasMoreElements() ) {
				v.add(e.nextElement());
			}
			v.trimToSize();
		}
		public boolean hasMoreElements() {
			if ( v.size() > cur ) 
				return true;
			return false;
		}
		public Object nextElement() throws NoSuchElementException {
			if ( v.size() <= cur )
				throw new NoSuchElementException();
			Object o = v.get(cur++);
			if ( o != null )
				return ((QueueItem)o).item;
			else 
				return null;
		}
	}
	
	private class QueueItem {
		private QueueItem next;
		private QueueItem prev;
		private Object key;
		private Object item;
		private int time;
		
		private QueueItem( Object key, Object item ) {
			this.key = key;
			this.item = item;
		}
	}
	
	/*
	 * Ticker daemon. Generate tick in fixed interval of time.
	 */
	private static class TimeThread extends Thread {
		private int[] listenerLock = new int[0];
		private LinkList listener;
		private int[] lock = new int[0];
		private int tick;
		private long lastTime;
		private boolean isStopped;
		private boolean isStarted;
		
		public TimeThread(int tick) {
			super("Time-limited cache daemon");
			this.tick = tick;
			setDaemon(true);
			setPriority( MIN_PRIORITY );
			isStopped = true;
			start();
		}
		public void startTick() {
			//_log.trace ( "start tick" );
			if ( isStarted && isStopped ) {
				synchronized(lock) {
					lastTime = System.currentTimeMillis();
					lock.notify();
				}
			}
		}
		public void stopTick() {
			// _log.trace ( "stop tick" );
			isStopped = true;
		}
		public void run() {
			isStarted = true;
			try {
				while ( true ) {
					if ( isStopped ) {
						synchronized(lock) { lock.wait(); }
						isStopped = false;
					} else {							
						long time = System.currentTimeMillis();
						if ( time - lastTime < tick ) 
							sleep(tick-(time-lastTime));
						lastTime = System.currentTimeMillis();
					}
					
					LinkList temp = listener;
					while ( temp != null ) {
						temp.t.tick();
						temp = temp.next;
					}
				}
			} catch ( InterruptedException e ) {
				/*
                 * kindly disregard this exception
				 */ 
			}
		}
		public void addListener( TimeLimited t ) {
			synchronized ( listenerLock ) {
				listener = new LinkList( listener, t );
			}
		}
		private class LinkList {
			private LinkList next;
			private TimeLimited t;
			LinkList( LinkList next, TimeLimited t ) {
				this.next = next;
				this.t = t;
			}
		}
	}
	
}