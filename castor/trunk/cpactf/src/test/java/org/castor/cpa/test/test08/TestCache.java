/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test08;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cache.CacheAcquireException;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

public final class TestCache extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestCache.class);
    
    /** After how many seconds should the cache expire? */
    private static final int EXPIRE_SEC = 10;

    /** How many entries should be inserted into the cache? */
    private static final int COUNT = 10;

    public TestCache(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    /**
     * Put some entries into the cache. Upon inserting, each entry gets a
     * timestamp. This timestamp will be examined, when the entry is removed
     * from the cache.
     */
    public void testTimeLimitedExpiring() throws CacheAcquireException {
        List<CacheEntry> expiredTooFast = new ArrayList<CacheEntry>();
        TimeLimitedTest cache = new TimeLimitedTest(EXPIRE_SEC, expiredTooFast);
        try {
            LOG.info("Putting some entries in the cache...");
            for (int i = 0; i < COUNT; i++) {
                cache.put(new Integer(i),
                          new CacheEntry(i, System.currentTimeMillis()));
                Thread.sleep(100);
            }

            LOG.info("Waiting for cache to expire...");
            // Wait at least as long as the cache needs to expire
            Thread.sleep((EXPIRE_SEC + 1) * 1000);
            LOG.info("Finished waiting.");
        } catch (InterruptedException ex) {
        }

        // Inspect the result. If the list contains more than 66% of all cache
        // entries, then the test has failed.
        if (expiredTooFast.size() > COUNT * 2 / 3) {
            String msg = "More than 66% of all cache entries ("
                + expiredTooFast.size() + " out of " + COUNT
                + ") expired much too fast!";
            LOG.error(msg);
            fail(msg);
        }
    }
}




