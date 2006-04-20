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
 *
 * $Id$
 */
package org.castor.cache.hashbelt.container;

import java.util.Iterator;
import java.util.Map;

/**
 * An interface describing implementation strategies for containers on the hashbelt;
 * containers hold objects that are in the hashbelt, and provide the time-based
 * grouping that allows the container to be efficiently dealt with as a group of
 * objects to be expired.
 * <p>
 * Implementations of this interface need to be appropriately synchronized --
 * the implementations of the hashbelt rely on this object to be threadsafe.
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public interface Container extends Map {
    /**
     * Set the timestamp of this container to System.currentTimeMillis().
     */
    void updateTimestamp();
    
    /**
     * Returns the timestamp of this container.
     * 
     * @return The timestamp.
     */
    long getTimestamp();
    
    /**
     * Returns an iterator over the keys contained in this container. If the container
     * is modified while an iteration is in progress, the results of the iteration
     * is not affected and vice-versa.
     *
     * @return An iterator over the keys currently contained in the container.
     */
    Iterator keyIterator();
    
    /**
     * Returns an iterator over the values contained in this container. If the container
     * is modified while an iteration is in progress, the results of the iteration
     * is not affected and vice-versa.
     *
     * @return An iterator over the values currently contained in the container.
     */
    Iterator valueIterator();
}
