/*
 * Copyright 2005, 2006 Gregory Block, Ralf Joachim
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
package org.castor.cache.hashbelt.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A very basic, HashMap-based implementation of the hashmap container strategy,
 * using nothing more than a basic hashmap to store key/value pairs. This works
 * well for lots of gets and a reasonably high volume of removes; if few removes
 * are required, and iterators are important to your particluar use-case of the
 * cache, it's better to use the FastIteratingContainer, which can handle
 * iterating at a higher speed, still has a map for accessing hash values, but has
 * a higher removal cost.
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class MapContainer
extends ConcurrentHashMap < Object, Object >
implements Container {
    //--------------------------------------------------------------------------
    
    /** SerialVersionUID. */
    private static final long serialVersionUID = -7215860376133906243L;
    
    /** Timestamp of this container. */
    private long _timestamp = 0;
    
    //--------------------------------------------------------------------------
    // additional operations of container interface

    /**
     * {@inheritDoc}
     */
    public void updateTimestamp() { _timestamp = System.currentTimeMillis(); }
    
    /**
     * {@inheritDoc}
     */
    public long getTimestamp() { return _timestamp; }
    
    /**
     * {@inheritDoc}
     */
    public Iterator < Object > keyIterator() {
        return new ArrayList < Object > (keySet()).iterator();
    }
    
    /**
     * {@inheritDoc}
     */
    public Iterator < Object > valueIterator() {
        return new ArrayList < Object > (values()).iterator();
    }
    
    //--------------------------------------------------------------------------
}
