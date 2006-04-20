/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.persist.cache.distributed;

import org.exolab.castor.persist.cache.AbstractCacheFactory;
import org.exolab.castor.persist.cache.CacheFactory;


/**
* Implements {@link CacheFactory} for the {@link JCache} implementation of {@link Cache}.
*
* @author <a href="mailto:ttelcik@hbf.com.au">Tim Telcik</a>
*/
public class JCacheFactory extends AbstractCacheFactory implements CacheFactory
{
    
    /**
     * The name of the factory
     */
    private static final String NAME = "jcache";
    
    /**
     * Full class name of the underlying cache implementation.
     */
    private static final String CLASS_NAME = 
        "org.castor.persist.cache.distributed.JCache"; 

    /**
     * Returns the short alias for this factory instance.
     * @return The short alias name. 
     */
    public String getName() {
        return NAME;
    }
    
    /**
     * Returns the full class name of the underlying cache implementation.
     * @return The full cache class name. 
     */
    public String getCacheClassName() {
        return CLASS_NAME;   
    }

    /**
     * Cache-specific shutdown operations and resource cleanup.
     */
    public void shutdown() {
        //nothing to do
    }

}
