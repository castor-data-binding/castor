/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 */
package ctf.jdo.tc0x;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import harness.CastorTestCase;
import harness.TestHarness;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException; 
import org.castor.cache.simple.TimeLimited;

public final class TestCache extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestCache.class);
    
    /**
     * After how many seconds should the cache expire?
     */
    private static final int EXPIRE_SEC = 10;
    private static final int EXPIRE_MILLI = EXPIRE_SEC * 1000;

    /**
     * How many entries should be inserted into the cache?
     */
    private static final int COUNT = 10;

    public TestCache(final TestHarness category) {
        super(category, "TC08", "Cache expiry measure");
    }

    public void runTest() throws CacheAcquireException {
        testTimeLimitedExpiring();
    }

    /**
     * Put some entries into the cache. Upon inserting, each entry gets a
     * timestamp. This timestamp will be examined, when the entry is removed
     * from the cache.
     */
    private void testTimeLimitedExpiring() throws CacheAcquireException {
        List expiredTooFast = new ArrayList();
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

    class TimeLimitedTest extends TimeLimited {
        private List _expiredTooFast;

        TimeLimitedTest(final int interval, final List expiredTooFast)
        throws CacheAcquireException {
            super();
            
            Properties params = new Properties();
            params.put(Cache.PARAM_NAME, "dummy");
            params.put(TimeLimited.PARAM_TTL, new Integer(interval));
            initialize(params);

            this._expiredTooFast = expiredTooFast;
        }

        public Object remove(final Object key) {
            Object obj = super.remove(key);

            long now = System.currentTimeMillis();
            CacheEntry entry = (CacheEntry) obj;
            long diff = Math.abs(now - entry.getTimestamp());
            
            // "diff" should be approx. EXPIRE_MILLI. If it is less than 66% of
            // this value, we put the entry into a list.
            if (diff < (EXPIRE_MILLI * 2 / 3)) {
                _expiredTooFast.add(entry);
                LOG.debug("Entry " + entry.getId() + " expired after "
                        + diff + " millis (should be " + EXPIRE_MILLI + ")");
            } else {
                LOG.debug("Entry " + entry.getId() + " expired after "
                        + diff + " millis");
            }
            
            return obj;
        }
    }

    private final class CacheEntry {
        private final int _id;
        private final long _timestamp;

        CacheEntry(final int id, final long timestamp) {
            this._id = id;
            this._timestamp = timestamp;
        }

        public long getId() {
            return _id;
        }

        public long getTimestamp() {
            return _timestamp;
        }
    }
}




