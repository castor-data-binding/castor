/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.cache.hashbelt;

import org.castor.cache.AbstractCacheFactory;

/**
 * Implements {@link CacheFactory} for the {@link FIFOHashbelt} implementation
 * of {@link Cache}.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Id$
 * @since 1.0
 */
public final class FIFOHashbeltFactory extends AbstractCacheFactory {
    /**
     * @see org.castor.cache.CacheFactory#getCacheType()
     */
    public String getCacheType() { return FIFOHashbelt.TYPE; }
    
    /**
     * @see org.castor.cache.CacheFactory#getCacheClassName()
     */
    public String getCacheClassName() { return FIFOHashbelt.class.getName(); }
}
