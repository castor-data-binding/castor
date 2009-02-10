/*
 * Copyright 2005 Thomas Yip, Stein M. Hugubakken, Werner Guttmann, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cache.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cache.AbstractBaseCache;
import org.castor.cache.CacheAcquireException;

/**
 * TimeLimited is a time limted first-in-first-out <tt>Map</tt>. Every object
 * being put in the Map will live until the timeout expired.
 * <p>
 * The expiration time is passed to the cache at initialization by the individual
 * cache property <b>ttl</b> which defines the timeout of every object in the cache in
 * seconds. If not specified a timeout of 30 seconds will be used.
 *
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:dulci AT start DOT no">Stein M. Hugubakken</a> 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public class TimeLimited extends AbstractBaseCache {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TimeLimited.class);

    /** The type of the cache. */
    public static final String TYPE = "time-limited";
    
    /** Mapped initialization parameter <code>ttl</code>. */
    public static final String PARAM_TTL = "ttl";

    /** Default ttl of cache. */
    public static final int DEFAULT_TTL = 30;
    
    /** Seconds between ticks, default is 1 second. This value is used to decrease
     *  QueueItem.time on each tick. */
    private static final int TICK_DELAY = 1;

    /** The Default precision in millisecond is 1000. Precision is the interval
     *  between each time which the timer thread will wake up and trigger clean up
     *  of least-recently-used objects. */
    private static final int DEFAULT_PRECISION = 1000 * TICK_DELAY;
    
    /** Timer is used to start a task that runs the tick-method. */
    private static final TickThread TIMER = new TickThread(DEFAULT_PRECISION);
    
    /** Container for cached objects. */
    private Hashtable < Object, QueueItem > _map = new Hashtable < Object, QueueItem > ();

    /** Real ttl of this cache. */
    private int _ttl = DEFAULT_TTL;

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * {@inheritDoc}
     */
    public final void initialize(final Properties params) throws CacheAcquireException {
        super.initialize(params);
        
        String param = params.getProperty(PARAM_TTL);
        try {
            if (param != null) { _ttl = Integer.parseInt(param); }
            if (_ttl <= 0) { _ttl = DEFAULT_TTL; }
        } catch (NumberFormatException ex) {
            _ttl = DEFAULT_TTL;
        }

        if (TIMER._list.contains(this)) {
            TIMER._list.remove(this);
            _map.clear();
        }
        TIMER.addTickerTask(this);
    }

    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     */
    public final String getType() { return TYPE; }
    
    /**
     * Get real ttl of this cache.
     * 
     * @return Real ttl of this cache.
     */
    public final int getTTL() { return _ttl; }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     */
    public final synchronized int size() { return _map.size(); }

    /**
     * {@inheritDoc}
     */
    public final synchronized boolean isEmpty() { return _map.isEmpty(); }

    /**
     * {@inheritDoc}
     */
    public final synchronized boolean containsKey(final Object key) {
        return _map.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public final synchronized boolean containsValue(final Object value) {
        Iterator < QueueItem > iter = _map.values().iterator();
        while (iter.hasNext()) {
            QueueItem item = iter.next();
            if (value == null) {
                if (item._value == null) { return true; }  
            } else {
                if (value.equals(item._value)) { return true; }  
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public final synchronized Object get(final Object key) {
        QueueItem item = _map.get(key);
        return (item == null) ? null : item._value;
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final synchronized Object put(final Object key, final Object value) {
        QueueItem item = _map.get(key);
        if (item != null) {
            return item.update(value, _ttl);
        }
        _map.put(key, new QueueItem(key, value, _ttl));
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public synchronized Object remove(final Object key) {
        QueueItem item = _map.remove(key);
        return (item == null) ? null : item._value;
    }
    
    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final void putAll(final Map < ? extends Object, ? extends Object > map) {
        Iterator < ? extends Entry < ? extends Object, ? extends Object > > iter;
        iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry < ? extends Object, ? extends Object > entry = iter.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    public final synchronized void clear() { _map.clear(); }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final synchronized Set < Object > keySet() {
        return Collections.unmodifiableSet(_map.keySet());
    }

    /**
     * {@inheritDoc}
     */
    public final synchronized Collection < Object > values() {
        Collection < Object > col = new ArrayList < Object > (_map.size());
        Iterator < QueueItem > iter = _map.values().iterator();
        while (iter.hasNext()) {
            QueueItem item = iter.next();
            col.add(item._value);
        }
        return Collections.unmodifiableCollection(col);
    }

    /**
     * {@inheritDoc}
     */
    public final synchronized Set < Entry < Object, Object > > entrySet() {
        Map < Object, Object > map = new Hashtable < Object, Object > (_map.size());
        Iterator < Entry < Object, QueueItem > > iter = _map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry < Object, QueueItem > entry = iter.next();
            QueueItem item = entry.getValue();
            map.put(entry.getKey(), item._value);
        }
        return Collections.unmodifiableSet(map.entrySet());
    }

    //--------------------------------------------------------------------------
    
    /**
     * QueueItem holding value and time of each entry in the cache.
     */
    private final class QueueItem {
        /** The key. */
        private Object _key;
        
        /** The value. */
        private Object _value;
        
        /** The time to life (ttl). */
        private int _time;

        /**
         * Construct a new QueueItem with given value and time.
         * 
         * @param key The key.
         * @param value The value.
         * @param time The time to life (ttl).
         */
        private QueueItem(final Object key, final Object value, final int time) {
            _key = key;
            _value = value;
            _time = time;
        }
        
        /**
         * Update value and time of this QueueItem. Returns the previouly hold value.
         * 
         * @param value The new value.
         * @param time The new time to life (ttl).
         * @return The previouly hold value.
         */
        private Object update(final Object value, final int time) {
            Object oldValue = _value;
            _value = value;
            _time = time;
            return oldValue;
        }
    }

    /**
     * TickThread to remove cache entries whose ttl has been elapsed.
     */
    private static final class TickThread extends Thread {
        /** The list of all registered caches. */
        private ArrayList < TimeLimited > _list = new ArrayList < TimeLimited > ();
        
        /** The intervall to checked cache for elapsed entries in milliseconds. */
        private int _tick;
        
        /**
         * Construct a TickThread to remove cache entries whose ttl has been elapsed.
         * The intervall to checked cache for elapsed entries is specified by tick. 
         * 
         * @param tick The intervall to checked cache for elapsed entries in milliseconds.
         */
        public TickThread(final int tick) {
            super("Time-limited cache daemon");
            setDaemon(true);
            setPriority(MIN_PRIORITY);

            _tick = tick;

            start();
        }
        
        /**
         * Add given cache to list of caches.
         * 
         * @param cache Cache to be added to cache list.
         */
        void addTickerTask(final TimeLimited cache) {
            _list.add(cache);
        }
        
        /**
         * After sleeping for <code>tick</code> milliseconds, as specified at construction
         * of the TickThread, it iterates through all registered caches and calls their
         * tick() method to decrement ttl and remove those entries whose ttl has expired.
         * if finished with that all starts from the beginning.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                long last = System.currentTimeMillis();
                while (true) {
                    long diff = System.currentTimeMillis() - last;
                    if (diff < _tick) { sleep(_tick - diff); }
                    last = System.currentTimeMillis();
                    for (int i = 0; i < _list.size(); i++) {
                        _list.get(i).tick();
                    }
                }
            } catch (InterruptedException e) {
                LOG.error("Time-limited cache daemon has been interrupted", e);
            }
        }
    }

    /**
     * Called by TickThread to decrement ttl of all entries of the cache and remove
     * those whose ttl has been expired.
     */
    private synchronized void tick() {
        if (!_map.isEmpty()) {
            for (Iterator < QueueItem > iter = _map.values().iterator(); iter.hasNext(); ) {
                QueueItem queueItem = iter.next();
                Object key = queueItem._key;
                if (queueItem._time <= 0) {
                    iter.remove();
                    if (LOG.isDebugEnabled()) { LOG.trace("dispose(" + key + ")"); }
                } else {
                    queueItem._time -= TICK_DELAY;
                }
            }
        }
    }

    //--------------------------------------------------------------------------
}
