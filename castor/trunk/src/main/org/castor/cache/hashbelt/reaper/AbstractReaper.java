/*
 * Copyright 2005, 2006 Ralf Joachim
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

import org.castor.cache.Cache;

/**
 * Abstract base class all reapers have to extend. It allows the hsahbelt implementation
 * to pass a reference to itself to the reaper.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public abstract class AbstractReaper implements Reaper {
    /** The hashbelt that is owner of this reaper. */
    private Cache _owner;
    
    /**
     * Set the hashbelt this reaper is responsable for.
     * 
     * @param owner The hashbelt.
     */
    public final void setCache(final Cache owner) {
        _owner = owner;
    }
    
    /**
     * Set the hashbelt this reaper is responsable for.
     * 
     * @return The hashbelt.
     */
    public final Cache getCache() {
        return _owner;
    }
}
