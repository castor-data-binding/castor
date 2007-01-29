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
 * Implements {@link org.castor.cache.CacheFactory} for the {@link JCache}
 * implementation of {@link org.castor.cache.Cache}.
 *
 * @author <a href="mailto:ttelcik AT hbf DOT com DOT au">Tim Telcik</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class JCacheFactory extends AbstractCacheFactory {
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheType()
     */
    public String getCacheType() { return JCache.TYPE; }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheClassName()
     */
    public String getCacheClassName() { return JCache.class.getName(); }
}
