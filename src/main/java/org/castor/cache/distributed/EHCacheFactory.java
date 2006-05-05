/*
 * Copyright 2005 Tim Telcik, Werner Guttmann, Ralf Joachim
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
package org.castor.cache.distributed;

import org.castor.cache.AbstractCacheFactory;

/**
 * Implements {@link CacheFactory} for the {@link EHCache} implementation of 
 * {@link Cache}.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5872 $ $Date: 2006-04-26 00:09:10 0200 (Mi, 26 Apr 2006) $
 * @since 1.0.1
 */
public final class EHCacheFactory extends AbstractCacheFactory {
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheType()
     */
    public String getCacheType() { 
        return EHCache.TYPE;
    }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheClassName()
     */
    public String getCacheClassName() { 
        return EHCache.class.getName(); 
    }
    
}