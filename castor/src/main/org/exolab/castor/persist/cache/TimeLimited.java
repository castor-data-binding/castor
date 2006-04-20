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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
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
 * @author <a href="mailto:dulci@start.no">Stein M. Hugubakken</a> 
 * @version $Revision$ $Date$
 */
public class TimeLimited extends AbstractBaseCache implements Cache {
	
	/**
	 * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
	 * Commons Logging</a> instance used for all logging.
	 */
	private static Log _log = LogFactory.getFactory().getInstance(TimeLimited.class);
	
	/**
	 * Seconds between ticks, default is 1 second.
	 * This value is used to decrease QueueItem.time on each tick.
	 */
	final private static int TICK_DELAY = 1;

	/**
	 *  The Default precision in millisecond is 1000. Precision is the interval 
	 *  between each time which the timer thread will wake up and trigger 
	 *  clean up of least-recently-used Object.
	 */
	final private static int DEFAULT_PRECISION = 1000 * TICK_DELAY;
	
	/**
	 * Timer is used to start a task that runs the tick-method.
	 */
	private static TickThread timer = new TickThread (DEFAULT_PRECISION);
	
	/* Castor JDO is still required to support JDK/JRE 1.2. As such, Timer cannot be 
	 * used (yet) as it is only available with JDK 1.3.
	 */  
	// private static Timer timer = new Timer(true);
	
    /**
     * Container for cached objects.
     */
    private Hashtable map = new Hashtable();

    /**
     * Creates an instance of TimeLimited. 
     */
    public TimeLimited () {
    	super();
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
        if (oldItem != null) {
            // XXX [SMH]: This code-block is never reached.
            if (_log.isDebugEnabled()) {
                _log.trace("TimeLimitedLRU: update(" + value + ")");
            }
            Object oldObject = oldItem.value;
            oldItem.value = value;
            oldItem.time = getCapacity();
            return oldObject;
        } else {
            if (_log.isDebugEnabled()) {
                _log.trace("TimeLimitedLRU: put(" + value + ")");
            }
            QueueItem newitem = new QueueItem(key, value);
            newitem.time = getCapacity();
            map.put(key, newitem);
            return null;
        }
    }
    
    /**
     * Returns the value to which the specified key is mapped in this Map.
     * @param key - a key in the Map.
     * @return the value to which the key is mapped in this Map; null if 
     * the key is not mapped to any value in this Map.
     */
    public synchronized Object get(Object key) {
        Object o = map.get(key);
        if ( o == null )
            return null;
        else 
            return ((QueueItem)o).value;
    }
	
    /**
     * @see org.exolab.castor.persist.cache.Cache#getCapacity()
     */
    public void setCapacity (int capacity) {
        super.setCapacity(capacity);
        if (timer.list.contains(this)) {
            timer.list.remove(this);
            map.clear();
        }
        timer.addTickerTask(this);
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
                _log.trace("TimeLimitedLRU: not in cache ... remove(" + key + ")");
            }
            return null;
        } else {
            if (_log.isDebugEnabled()) {
                _log.trace("TimeLimitedLRU: remove(" + key + ") = " + queueItem.value);
            }
            return queueItem.value;
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
	    return new ValuesEnumeration(map.values());
	}

	
	/**
	 * Remove the object identified by key from the cache.
	 * @param key the key that needs to be removed.
	 */
	public void expire (Object key) {
		// remove key from map, object will ultimately be
		// removed from queue when interval expires or a subsequent
		// call to put() overwrites the reference to it in QueueItem
		remove(key);
		dispose(key);
	}
	
	
	/** 
	 * Indicates whether the cache holds a value object for the specified key.
	 * @see org.exolab.castor.persist.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(Object key) {
	    if (_log.isDebugEnabled()) {
	        _log.trace("Testing for entry for key " + key);
	    }
	    return map.containsKey(key);
	}

	/**
	 * This method is called when an object is disposed.
	 * Override this method if you interested in the disposed object.
	 * @param o - the disposed object
	 */
	protected void dispose( Object o ) {
		if (_log.isDebugEnabled()) 
			_log.trace("Disposing " + o);
	}
	
	/**
	 * Called by TickThread
	 */
	private synchronized void tick() {
	    if (!map.isEmpty()) {
	        for (Iterator iter = map.values().iterator(); iter.hasNext();) {
	            QueueItem queueItem = (QueueItem) iter.next();
	            Object value = queueItem.value;
	            if (queueItem.time <= 0) {
	                iter.remove();
	                dispose(value);
	            } else {
	                queueItem.time -= TICK_DELAY;
	            }
	        }
	    }
	}


    private class ValuesEnumeration implements Enumeration {
        private Enumeration enumeration;
        private ValuesEnumeration(Collection coll) {
            enumeration = (new Vector(coll)).elements();
        }
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }
        public Object nextElement() throws NoSuchElementException {
            return ((QueueItem) enumeration.nextElement()).value;
        }
    }
    
    private class QueueItem {
        private Object key;
        private int time;
        private Object value;

        private QueueItem(Object key, Object item) {
            this.key = key;
            this.value = item;
        }
    }

    /**
     * TickThread. Generate tick in fixed interval of time.
     */
    private static class TickThread extends Thread {
        private long lastTime;
        private ArrayList list = new ArrayList();
        private int tick;
        public TickThread(int tick) {
            super("Time-limited cache daemon");
            this.tick = tick;
            setDaemon(true);
            setPriority(MIN_PRIORITY);
            start();
        }
        void addTickerTask(TimeLimited cache) {
            list.add(cache);
        }
        public void run() {
            try {
                while (true) {
                    long time = System.currentTimeMillis();
                    if (time - lastTime < tick) {
                        sleep(tick - (time - lastTime));
                    }
                    lastTime = System.currentTimeMillis();
                    for (int i = 0; i < list.size(); i++) {
                        ((TimeLimited) list.get(i)).tick();
                    }
                }
            } catch (InterruptedException e) {
            	// just consume exception
            }
        }
    }

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#size()
	 */
	public int size() {
		return map.size();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#clear()
	 */
	public void clear() {
		map.clear();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#isEmpty()
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return map.containsKey (key);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#values()
	 */
	public Collection values() {
		return map.values();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#putAll(java.util.Map)
	 */
	public void putAll(Map aMap) {
		map.putAll (aMap);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#entrySet()
	 */
	public Set entrySet() {
		return map.entrySet();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#keySet()
	 */
	public Set keySet() {
		return map.keySet();
	}

}
