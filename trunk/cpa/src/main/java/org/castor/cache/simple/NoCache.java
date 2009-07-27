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
     */
    public String getType() { return TYPE; }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     */
    public int size() { return 0; }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() { return true; }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) { return false; }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) { return false; }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object key) { return null; }

    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Object put(final Object key, final Object value) { return null; }

    /**
     * {@inheritDoc}
     */
    public Object remove(final Object key) { return null; }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public void putAll(final Map < ? extends Object, ? extends Object > map) { }

    /**
     * {@inheritDoc}
     */
    public void clear() { }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Set < Object > keySet() { return Collections.emptySet(); }

    /**
     * {@inheritDoc}
     */
    public Collection < Object > values() { return Collections.emptyList(); }

    /**
     * {@inheritDoc}
     */
    public Set < Entry < Object, Object > > entrySet() { return Collections.emptySet(); }
    
    //--------------------------------------------------------------------------
}