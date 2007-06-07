/*
 * Copyright 2005 Werner Guttmann, Ralf Joachim
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
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.castor.cache.AbstractBaseCache;

/**
 * NoCache is a Map which dispose all object right the way. Every object being put in the
 * Map will be disposed.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class NoCache extends AbstractBaseCache {
    //--------------------------------------------------------------------------

    /** The type of the cache. */
    public static final String TYPE = "none";
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#getType()
     */
    public String getType() { return TYPE; }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     * @see java.util.Map#size()
     */
    public int size() { return 0; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() { return true; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) { return false; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value) { return false; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final Object key) { return null; }

    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final Object key, final Object value) { return null; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(final Object key) { return null; }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map map) { }

    /**
     * {@inheritDoc}
     * @see java.util.Map#clear()
     */
    public void clear() { }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#keySet()
     */
    public Set keySet() { return Collections.EMPTY_SET; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#values()
     */
    public Collection values() { return Collections.EMPTY_LIST; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() { return Collections.EMPTY_SET; }
    
    //--------------------------------------------------------------------------
}