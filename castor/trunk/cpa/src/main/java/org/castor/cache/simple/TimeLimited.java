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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

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
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 *
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:dulci AT start DOT no">Stein M. Hugubakken</a> 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public class TimeLimited<K, V> extends AbstractBaseCache<K, V> {
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
    private final ConcurrentHashMap<K, QueueItem> _map = new ConcurrentHashMap<K, QueueItem>();

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

        _map.clear();
        
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
    public final int size() { return _map.size(); }

    /**
     * {@inheritDoc}
     */
    public final boolean isEmpty() { return _map.isEmpty(); }

    /**
     * {@inheritDoc}
     */
    public final boolean containsKey(final Object key) {
        return _map.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public final boolean containsValue(final Object value) {
        for (QueueItem item : _map.values()) {
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
    public final V get(final Object key) {
        QueueItem item = _map.get(key);
        return (item == null) ? null : item._value;
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final V put(final K key, final V value) {
        QueueItem item = _map.putIfAbsent(key, new QueueItem(key, value, _ttl));
        return (item == null) ? null : item.update(value, _ttl);
    }
    
    /**
     * {@inheritDoc}
     */
    public V remove(final Object key) {
        QueueItem item = _map.remove(key);
        return (item == null) ? null : item._value;
    }
    
    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final void putAll(final Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void clear() { _map.clear(); }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final Set<K> keySet() {
        return Collections.unmodifiableSet(_map.keySet());
    }

    /**
     * {@inheritDoc}
     */
    public final Collection<V> values() {
        Collection<V> col = new ArrayList<V>();
        for (QueueItem item : _map.values()) {
            col.add(item._value);
        }
        return Collections.unmodifiableCollection(col);
    }

    /**
     * {@inheritDoc}
     */
    public final Set<Entry<K, V>> entrySet() {
        Map<K, V> map = new HashMap<K, V>();
        for (QueueItem item : _map.values()) {
            map.put(item._key, item._value);
        }
        return Collections.unmodifiableSet(map.entrySet());
    }

    //--------------------------------------------------------------------------
    
    /**
     * QueueItem holding value and time of each entry in the cache.
     */
    private final class QueueItem {
        /** The key. */
        private final K _key;
        
        /** The value. */
        private volatile V _value;
        
        /** The time to life (ttl). */
        private final AtomicInteger _time;

        /**
         * Construct a new QueueItem with given value and time.
         * 
         * @param key The key.
         * @param value The value.
         * @param time The time to life (ttl).
         */
        private QueueItem(final K key, final V value, final int time) {
            _key = key;
            _value = value;
            _time = new AtomicInteger(time);
        }
        
        /**
         * Update value and time of this QueueItem. Returns the previouly hold value.
         * 
         * @param value The new value.
         * @param time The new time to life (ttl).
         * @return The previouly hold value.
         */
        private V update(final V value, final int time) {
            V oldValue = _value;
            _value = value;
            _time.set(time);
            return oldValue;
        }
    }

    /**
     * TickThread to remove cache entries whose ttl has been elapsed.
     */
    private static final class TickThread extends Thread {
        /** The list of all registered caches. */
        private final Set<TimeLimited> _set = new CopyOnWriteArraySet<TimeLimited>();
        
        /** The intervall to checked cache for elapsed entries in milliseconds. */
        private final int _tick;
        
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
            _set.add(cache);
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
                    for (TimeLimited cache : _set) {
                        cache.tick();
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
    private void tick() {
        for (QueueItem queueItem : _map.values()) {
            if (queueItem._time.getAndAdd(-TICK_DELAY) <= 0) {
                K key = queueItem._key;
                _map.remove(key);
                if (LOG.isDebugEnabled()) { LOG.trace("dispose(" + key + ")"); }
            }
        }
    }

    //--------------------------------------------------------------------------
}
