/*
 * Copyright 2005 Gregory Block, Ralf Joachim
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
package org.castor.cache.hashbelt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cache.AbstractBaseCache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.hashbelt.container.Container;
import org.castor.cache.hashbelt.container.MapContainer;
import org.castor.cache.hashbelt.reaper.AbstractReaper;
import org.castor.cache.hashbelt.reaper.NullReaper;
import org.castor.util.concurrent.ReadWriteLock;
import org.castor.util.concurrent.WriterPreferenceReadWriteLock;

/**
 * An abstract, core implementation of the hashbelt functionality; individual
 * implementations will differ on the underlying behavior.
 * <p>
 * A hashbelt has six important values which get set at initialization:
 * <dl>
 * <dt>containers</dt>
 * <dd>The number of containers in the conveyor belt. For example: If a box
 * will drop off of the conveyor belt every 30 seconds, and you want a cache
 * that lasts for 5 minutes, you want 5 / 30 = 6 containers on the belt. Every
 * 30 seconds, another, clean container goes on the front of the conveyor belt,
 * and everything in the last belt gets discarded. If not specified 10 containers
 * are used by default.
 * <br/>
 * For systems with fine granularity, you are free to use a large number of
 * containers; but the system is most efficient when the user decides on a
 * "sweet spot" determining both the number of containers to be managed on the
 * whole and the optimal number of buckets in those containers for managing. This
 * is ultimately a performance/accuracy tradeoff with the actual discard-from-cache
 * time being further from the mark as the rotation time goes up. Also the number
 * of objects discarded at once when capacity limit is reached depends upon the
 * number of containers.</dd>
 * <dt>capacity</dt>
 * <dd>Maximum capacity of the whole cache. If there are, for example, ten
 * containers on the belt and the capacity has been set to 1000, each container
 * will hold a maximum of 1000/10 objects. Therefore if the capacity limit is
 * reached and the last container gets droped from the belt there are up to 100
 * objects discarted at once. By default the capacity is set to 0 which causes
 * capacity limit to be ignored so the cache can hold an undefined number of
 * objects.</dd>
 * <dt>ttl</dt>
 * <dd>The maximum time an object lifes in cache. If the are, for example, ten
 * containers and ttl is set to 300 seconds (5 minutes), a new container will be
 * put in front of the belt every 300/10 = 30 seconds while another is dropped at
 * the end at the same time. Due to the granularity of 30 seconds, everything just
 * until 5 minutes 30 seconds will also end up in this box. The default value for
 * ttl is 60 seconds. If ttl is set to 0 which means that objects life in cache
 * for unlimited time and may only discarded by a capacity limit.</dd>
 * <dt>monitor</dt>
 * <dd>The monitor intervall in minutes when hashbelt cache rports the current
 * number of containers used and objects cached. If set to 0 (default) monitoring
 * is disabled.</dd>
 * <dt>container-class</dt>
 * <dd>The implementation of <b>org.castor.cache.hashbelt.container.Container</b>
 * interface to be used for all containers of the cache. Castor provides the following
 * 3 implementations of the Container interface.<br/>
 * org.castor.cache.hashbelt.container.FastIteratingContainer<br/>
 * org.castor.cache.hashbelt.container.MapContainer<br/>
 * org.castor.cache.hashbelt.container.WeakReferenceContainer<br/>
 * If not specified the MapContainer will be used as default.</dd>
 * <dt>reaper-class</dt>
 * <dd>Specific reapers yield different behaviors. The GC reaper, the default,
 * just dumps the contents to the garbage collector. However, custom
 * implementations may want to actually do something when a bucket drops off the
 * end; see the javadocs on other available reapers to find a reaper strategy
 * that meets your behavior requirements. Apart of the default
 * <b>org.castor.cache.hashbelt.reaper.NullReaper</b> we provide 3 abstract
 * implementations of <b>org.castor.cahe.hashbelt.reaper.Reaper</b> interface:<br/>
 * org.castor.cache.hashbelt.reaper.NotifyingReaper<br/>
 * org.castor.cache.hashbelt.reaper.RefreshingReaper<br/>
 * org.castor.cache.hashbelt.reaper.ReinsertingReaper<br/>
 * to be extended by your custom implementation.</dd>
 * <dl>
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public abstract class AbstractHashbelt extends AbstractBaseCache {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractHashbelt.class);
    
    /** Mapped initialization parameter: containers */
    public static final String PARAM_CONTAINERS = "containers";

    /** Mapped initialization parameter: container-class */
    public static final String PARAM_CONTAINER_CLASS = "container-class";

    /** Mapped initialization parameter: reaper-class */
    public static final String PARAM_REAPER_CLASS = "reaper-class";

    /** Mapped initialization parameter: capacity */
    public static final String PARAM_CAPACITY = "capacity";

    /** Mapped initialization parameter: ttl */
    public static final String PARAM_TTL = "ttl";

    /** Mapped initialization parameter: monitor */
    public static final String PARAM_MONITOR = "monitor";

    /** Default number of containers for cache. */
    public static final int DEFAULT_CONTAINERS = 10;
    
    /** Default container class. */
    public static final Class DEFAULT_CONTAINER_CLASS = MapContainer.class;
    
    /** Default reaper class. */
    public static final Class DEFAULT_REAPER_CLASS = NullReaper.class;
    
    /** Default capacity of cache. */
    public static final int DEFAULT_CAPACITY = 0;
    
    /** Default ttl of cache in seconds. */
    public static final int DEFAULT_TTL = 60;
    
    /** Default monitor interval of cache in minutes. */
    public static final int DEFAULT_MONITOR = 0;
    
    /** Milliseconds per second. */
    private static final long ONE_SECOND = 1000;
    
    /** Milliseconds per minute. */
    private static final long ONE_MINUTE = 60 * ONE_SECOND;
    
    //--------------------------------------------------------------------------
    
    /** ReadWriteLock to synchronize access to cache. */
    private final ReadWriteLock _lock = new WriterPreferenceReadWriteLock();
    
    /** The internal array of containers building the cache. */
    private Container[] _cache = new Container[0];
    
    /** The internal array of empty conatiners to be used for cache on demand. */
    private Container[] _pool;
    
    /** Number of containers currently available in pool. */
    private int _poolCount;
    
    /** Real capacity limit of this cache. If set to Integer.MAX_VALUE the capacity
     *  of the cache is not restricted. The capacity needs to be greater then twice
     *  the number of containers. */
    private int _cacheCapacity;

    /** Approximat number of entries in this cache. */
    private int _cacheSize = 0;

    /** Target number of containers for this cache. The real number of containers
     *  may vary between 0 and twice the target number. */
    private int _containerTarget;

    /** Real number of containers in the cache. */
    private int _containerCount = 0;
    
    /** Capacity limit of a container. */
    private int _containerCapacity;

    /** Real ttl of the entries in this cache. */
    private int _ttl;
    
    /** The reaper to pass all expired containers to. */
    private AbstractReaper _reaper;

    /** Real monitor interval. */
    private int _monitor;

    /** Timer to expire containers and the objects they contain after ttl. */
    private Timer _expirationTimer;

    /** Timer to monitor container count and cache size every monitor interval. */
    private Timer _monitoringTimer;

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#initialize(java.util.Map)
     */
    public final void initialize(final Properties params)
    throws CacheAcquireException {
        super.initialize(params);
        
        String param;
        
        try {
            param = params.getProperty(PARAM_CONTAINERS);
            if (param != null) { _containerTarget = Integer.parseInt(param); }
            if (_containerTarget <= 0) { _containerTarget = DEFAULT_CONTAINERS; }
        } catch (NumberFormatException ex) {
            _containerTarget = DEFAULT_CONTAINERS;
        }

        try {
            Class cls = DEFAULT_CONTAINER_CLASS;
            param = params.getProperty(PARAM_CONTAINER_CLASS);
            if ((param != null) && !"".equals(param)) {
                cls = Class.forName(param);
            }

            _poolCount = 2 * _containerTarget;
            _pool = new Container[_poolCount];
            for (int i = 0; i < _poolCount; i++) {
                _pool[i] = (Container) cls.newInstance();
            }
        } catch (Exception ex) {
            String msg = "Failed to instantiate hashbelt container.";
            throw new CacheAcquireException(msg, ex);
        }

        try {
            Class cls = DEFAULT_REAPER_CLASS;
            param = params.getProperty(PARAM_REAPER_CLASS);
            if ((param != null) && !"".equals(param)) {
                cls = Class.forName(param);
            }

            _reaper = (AbstractReaper) cls.newInstance();
            _reaper.setCache(this);
        } catch (Exception ex) {
            String msg = "Failed to instantiate hashbelt reaper.";
            throw new CacheAcquireException(msg, ex);
        }

        try {
            param = params.getProperty(PARAM_CAPACITY);
            if (param != null) { _cacheCapacity = Integer.parseInt(param); }
            if (_cacheCapacity < 0) { _cacheCapacity = DEFAULT_CAPACITY; }
        } catch (NumberFormatException ex) {
            _cacheCapacity = DEFAULT_CAPACITY;
        }
        int minCapacity = 2 * _containerTarget;
        if ((_cacheCapacity > 0) && (_cacheCapacity < minCapacity)) {
            _cacheCapacity = minCapacity;
        }

        _containerCapacity = _cacheCapacity / _containerTarget;
        
        try {
            param = params.getProperty(PARAM_TTL);
            if (param != null) { _ttl = Integer.parseInt(param); }
            if (_ttl < 0) { _ttl = DEFAULT_TTL; }
        } catch (NumberFormatException ex) {
            _ttl = DEFAULT_TTL;
        }

        if (_ttl > 0) {
            long periode = (_ttl * ONE_SECOND) / _containerTarget;
            _expirationTimer = new Timer(true);
            _expirationTimer.schedule(new ExpirationTask(this), periode, periode);
        }

        try {
            param = params.getProperty(PARAM_MONITOR);
            if (param != null) { _monitor = Integer.parseInt(param); }
            if (_monitor < 0) { _monitor = DEFAULT_MONITOR; }
        } catch (NumberFormatException ex) {
            _monitor = DEFAULT_MONITOR;
        }

        if (_monitor > 0) {
            long periode = _monitor * ONE_MINUTE;
            _monitoringTimer = new Timer(true);
            _monitoringTimer.schedule(new MonitoringTask(this), periode, periode);
        }
    }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#close()
     */
    public final void close() {
        if (_monitoringTimer != null) {
            _monitoringTimer.cancel();
            _monitoringTimer = null;
        }
        
        _monitor = 0;
        
        if (_expirationTimer != null) {
            _expirationTimer.cancel();
            _expirationTimer = null;
        }
        
        _ttl = 0;

        clear();

        _containerCapacity = 0;
        _cacheCapacity = 0;
        _reaper = null;
        _poolCount = 0;
        _pool = null;
        _containerTarget = 0;
        
        super.close();
    }

    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * Get real capacity of this cache.
     * 
     * @return Real capacity of this cache.
     */
    public final int getCapacity() { return _cacheCapacity; }

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
     * @see java.util.Map#size()
     */
    public final int size() {
        try {
            _lock.readLock().acquire();
            int size = _cacheSize;
            _lock.readLock().release();
            return size;
        } catch (InterruptedException ex) {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#isEmpty()
     */
    public final boolean isEmpty() { return (size() == 0); }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public final boolean containsKey(final Object key) {
        if (key == null) { throw new NullPointerException("key"); }

        boolean found = false;
        
        try {
            _lock.readLock().acquire();
        } catch (InterruptedException ex) {
            return false;
        }
        
        try {
            for (int i = 0; (i < _containerCount) && !found; i++) {
                if (_cache[i].containsKey(key)) { found = true; }
            }
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            _lock.readLock().release();
        }
        
        return found;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public final boolean containsValue(final Object value) {
        if (value == null) { throw new NullPointerException("value"); }
        
        boolean found = false;

        try {
            _lock.readLock().acquire();
        } catch (InterruptedException ex) {
            return false;
        }
        
        try {
            for (int i = 0; (i < _containerCount) && !found; i++) {
                if (_cache[i].containsValue(value)) { found = true; }
            }
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            _lock.readLock().release();
        }
        return found;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#clear()
     */
    public final void clear() {
        try {
            _lock.writeLock().acquire();
        } catch (InterruptedException ex) {
            return;
        }
        
        try {
            while (_containerCount > 0) { expireCacheContainer(); }
            _cacheSize = 0;
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            _lock.writeLock().release();
        }
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#keySet()
     */
    public final Set keySet() {
        Set set = new HashSet(size());
        
        try {
            _lock.readLock().acquire();
        } catch (InterruptedException ex) {
            return set;
        }
        
        try {
            for (int i = 0; i < _containerCount; i++) {
                set.addAll(_cache[i].keySet());
            }
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            _lock.readLock().release();
        }
        
        return set;
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#values()
     */
    public final Collection values() {
        Collection col = new ArrayList(size());
        
        try {
            _lock.readLock().acquire();
        } catch (InterruptedException ex) {
            return col;
        }
        
        try {
            for (int i = 0; i < _containerCount; i++) {
                col.addAll(_cache[i].values());
            }
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            _lock.readLock().release();
        }

        return col;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#entrySet()
     */
    public final Set entrySet() {
        Map map = new HashMap(size());
        
        try {
            _lock.readLock().acquire();
        } catch (InterruptedException ex) {
            return map.entrySet();
        }
        
        try {
            for (int i = 0; i < _containerCount; i++) {
                map.putAll(_cache[i]);
            }
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            _lock.readLock().release();
        }
        
        return map.entrySet();
    }

    //--------------------------------------------------------------------------
    // protected methods for concrete implementations
    
    /**
     * Get reference to the ReadWriteLock of this cache instance.
     * 
     * @return ReadWriteLock to synchronize access to cache.
     */
    protected final ReadWriteLock lock() { return _lock; }
    
    /**
     * Get object currently associated with given key from cache. Take care to acquire a
     * read or write lock before calling this method and release the lock thereafter.
     * 
     * @param key The key to return the associated object for.
     * @return The object associated with given key.
     */
    protected final Object getObjectFromCache(final Object key) {
        Object result;
        for (int i = 0; i < _containerCount; i++) {
            result = _cache[i].get(key);
            if (result != null) { return result; }
        }
        return null;
    }
    
    /**
     * Put given value with given key in cache. Return the object previously associated
     * with key. Take care to acquire a write lock before calling this method and release
     * the lock thereafter.
     * 
     * @param key The key to associate the given value with.
     * @param value The value to associate with given key.
     * @return The object previously associated with given key. <code>null</code> will
     *         be returned if no value has been associated with key.
     */
    protected final Object putObjectIntoCache(final Object key, final Object value) {
        // We first check if a new container have to be created. This is the case
        // if there is none or if we have a capacity limit and the head container
        // holds the maximum allowed number of entries.
        if ((_containerCount == 0) || ((_cacheCapacity > 0)
                && (_cache[0].size() >= _containerCapacity))) {
            addCacheContainer();
        }
        
        // Then we can put the new or updated one to the head of the belt.
        Object result = _cache[0].put(key, value);
        
        if (result != null) { return result; }

        // If result is null we have to search the other containers of the
        // cache if they contain an entry for the key, in which case we have
        // to remove that old entry.
        for (int i = 1; (i < _containerCount) && (result == null); i++) {
            result = _cache[i].remove(key);
        }
        
        if (result != null) { return null; }
        
        // If result still is null we have added a new entry and need to
        // increment size of the whole cache. As size has increased we also
        // need to check if size exceeds capacity limit, in which case we
        // have to expire the oldest container of the cache.
        _cacheSize++;

        if (_cacheCapacity > 0) {
            while (_cacheCapacity < _cacheSize) {
                expireCacheContainer();
            }
        }
        
        return result;
    }
    
    /**
     * Remove any available association for given key. Take care to acquire a write lock
     * before calling this method and release the lock thereafter.
     * 
     * @param key The key to remove any previously associate value for.
     * @return The object previously associated with given key. <code>null</code> will
     *         be returned if no value has been associated with key.
     */
    protected final Object removeObjectFromCache(final Object key) {
        Object result;
        for (int i = 0; i < _containerCount; i++) {
            result = _cache[i].remove(key);
            if (result != null) {
                _cacheSize--;
                return result;
            }
        }
        return null;
    }
    
    //--------------------------------------------------------------------------
    // private helper methods
    
    /**
     * Recalculate the number of entries in the cache by summing the size of all
     * containers.
     */
    private void recalcCacheSize() {
        int size = 0; 
        for (int i = 0; i < _containerCount; i++) {
            size += _cache[i].size();
        }
        _cacheSize = size;
    }
    
    /**
     * Add an empty container from the pool to the cache.
     */
    private void addCacheContainer() {
        Container[] temp = new Container[++_containerCount];
        System.arraycopy(_cache, 0, temp, 1, _cache.length);
        temp[0] = _pool[--_poolCount];
        temp[0].updateTimestamp();
        _cache = temp;
    }
    
    /**
     * Remove the oldest container from the cache and pass it to the configured reaper
     * to do its expiration work. Then clear the container and put it back into the
     * pool for further use.
     */
    private void expireCacheContainer() {
        Container expired = _cache[--_containerCount];
        
        Container[] temp = new Container[_containerCount];
        System.arraycopy(_cache, 0, temp, 0, _containerCount);
        _cache = temp;
        _cacheSize -= expired.size();
        
        _reaper.handleExpiredContainer(expired);
        
        expired.clear();
        
        _pool[_poolCount++] = expired;
    }
    
    /**
     * Check the containers of the cache if their ttl has been expired. If the ttl of
     * the oldest container has expired the expireCacheContainer() method is called to
     * remove it. After removing it, the next container will be checked until one is
     * found thats ttl has not expired.
     */
    private void timeoutCacheContainers() {
        long timeout = System.currentTimeMillis() - _ttl;
        while ((_containerCount > 0)
            && (_cache[_containerCount - 1].getTimestamp() <= timeout)) {
            
            expireCacheContainer();
        }
    }
    
    //--------------------------------------------------------------------------
    // private helper classes

    /**
     * TimerTask that checks if ttl of containers in the cache has expired. If some
     * of them have expired they are removed. One new container will be added to
     * the head of the cache. In addition the size of the cache is recalculated. This
     * is not reqired for containers that hold strong references to their objects
     * but for those containers holding soft or weak references its the only way to
     * adjust the size with regard to the objects that have been garbage collected.
     * <p>
     * The interval this task will be executed is set to the ttl divided by the
     * number of containers in the cache. If ttl has been configured to be 0 the
     * ExpirationTask will never be executed.
     */
    private static class ExpirationTask extends TimerTask {
        /** Reference to the hashbelt this ExpirationTask belongs to. */
        private AbstractHashbelt _owner;
        
        /**
         * Construct a new ExpirationTask for the given hashbelt.
         * 
         * @param owner The hashbelt this ExpirationTask belongs to.
         */
        public ExpirationTask(final AbstractHashbelt owner) { _owner = owner; }
        
        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                _owner._lock.writeLock().acquire();
                
                _owner.timeoutCacheContainers();
                _owner.addCacheContainer();
                _owner.recalcCacheSize();
            } catch (ThreadDeath t) {
                LOG.debug("Stopping expiration thread: " + _owner.getName());
                throw t;
            } catch (Throwable t) {
                LOG.error("Caught exception during expiration: " + _owner.getName(), t);
                if (t instanceof VirtualMachineError) { throw (VirtualMachineError) t; }
            } finally {
                _owner._lock.writeLock().release();
            }
        }
    }
    
    /**
     * TimerTask that logs the number of containers and objects in the cache at the
     * interval configured by the monitor parameter. If monitor parameter has been
     * set to 0 the MonitoringTask will never be executed.
     */
    private static class MonitoringTask extends TimerTask {
        /** Reference to the hashbelt this MonitoringTask belongs to. */
        private AbstractHashbelt _owner;
        
        /**
         * Construct a new MonitoringTask for the given hashbelt.
         * 
         * @param owner The hashbelt this MonitoringTask belongs to.
         */
        public MonitoringTask(final AbstractHashbelt owner) { _owner = owner; }
        
        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                _owner._lock.readLock().acquire();
                
                LOG.info("Cache '" + _owner.getName() + "' "
                       + "currently holds " + _owner._containerCount + " containers "
                       + "with " + _owner._cacheSize + " objects.");
            } catch (ThreadDeath t) {
                LOG.debug("Stopping monitoring thread: " + _owner.getName());
                throw t;
            } catch (Throwable t) {
                LOG.error("Caught exception during monitoring: " + _owner.getName(), t);
                if (t instanceof VirtualMachineError) { throw (VirtualMachineError) t; }
            } finally {
                _owner._lock.readLock().release();
            }
        }
    }
    
    //--------------------------------------------------------------------------
}
