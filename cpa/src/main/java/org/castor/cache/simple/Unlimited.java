/*
 * Copyright 2005 Thomas Yip, Werner Guttmann, Ralf Joachim
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

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.castor.cache.AbstractBaseCache;

/**
 * UnLimited is a Map that holds any object being put into the map until it is removed
 * manually.
 * 
 * @author <a href="mailto:tyip AT leafsoft DOT com">Thomas Yip</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class Unlimited extends AbstractBaseCache {
    //--------------------------------------------------------------------------

    /** The type of the cache. */
    public static final String TYPE = "unlimited";
    
    /** The internal map. */
    private Hashtable < Object, Object > _map = new Hashtable < Object, Object > ();
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     */
    public String getType() { return TYPE; }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     */
    public int size() { return _map.size(); }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() { return _map.isEmpty(); }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        return _map.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        return _map.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object key) {
        return _map.get(key);
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Object put(final Object key, final Object value) {
        return _map.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public Object remove(final Object key) {
        return _map.remove(key);
    }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public void putAll(final Map < ? extends Object, ? extends Object > map) {
        _map.putAll (map);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() { _map.clear(); }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Set < Object > keySet() { return _map.keySet(); }
    
    /**
     * {@inheritDoc}
     */
    public Collection < Object > values() { return _map.values(); }

    /**
     * {@inheritDoc}
     */
    public Set < Entry < Object, Object > > entrySet() { return _map.entrySet(); }

    //--------------------------------------------------------------------------
}