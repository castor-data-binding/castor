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
 *
 * $Id$
 */
package org.castor.cache.hashbelt.reaper;

import org.castor.cache.hashbelt.container.Container;

/**
 * Reapers implement a single method - they are called with a container that has expired,
 * and told to perform an action on the expired container before the container is garbage
 * collected.
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public interface Reaper {
    /**
     * Methode called with a container that has expired before the container is garbage
     * collected.
     * 
     * @param expiredContainer The container that has expired.
     */
    void handleExpiredContainer(final Container expiredContainer);
}
