/*
 * Copyright 2005, 2006 Gregory Block
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
package org.castor.cache.hashbelt.reaper;

import java.util.Iterator;

import org.castor.cache.hashbelt.container.Container;

/**
 * Does the obvious: Calls a handleExpiredObject method for each object in the
 * container. Note that this is an abstract class; you must supply the implementation
 * of timeExpired().
 * <p>
 * At the end, the objects in the container have been expired and there's no
 * record of them in the hashbelt.
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public abstract class NotifyingReaper extends AbstractReaper {
    /**
     * {@inheritDoc}
     */
    public final void handleExpiredContainer(final Container expiredContainer) {
        Iterator < Object > iter = expiredContainer.valueIterator();
        while (iter.hasNext()) {
            Object nextValue = iter.next();
            handleExpiredObject(nextValue);
        }
    }
    
    /**
     * Methode called with an object that has expired before it is garbage collected.
     * 
     * @param expiredObject The object that has expired.
     */
    protected abstract void handleExpiredObject(final Object expiredObject);
}
