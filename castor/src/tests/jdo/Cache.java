package jdo;

import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.persist.cache.TimeLimited;

import harness.CastorTestCase;
import harness.TestHarness;

/**
 * @author m.renner at exxcellent.de
 */
public class Cache extends CastorTestCase {

    /**
     * After how many seconds should the cache expire?
     */
    private final int EXPIRE_SEC = 10;
    private final int EXPIRE_MILLI = EXPIRE_SEC * 1000;

    /**
     * How many entries should be inserted into the cache?
     */
    private final int COUNT = 10;

    public Cache(TestHarness category) {
        super(category, "TC10", "Cache expiry measure");
    }

    public void runTest() {
        testTimeLimitedExpiring();
    }

    /**
     * Put some entries into the cache. Upon inserting, each entry gets a timestamp.
     * This timestamp will be examined, when the entry is removed from the cache.
     */
    private void testTimeLimitedExpiring() {
        List expiredTooFast = new ArrayList();
        TimeLimitedTest cache = new TimeLimitedTest(EXPIRE_SEC, expiredTooFast);
        try {
            stream.println("Putting some entries in the cache...");
            for (int i = 0; i < COUNT; i++) {
                cache.put(new Integer(i), new CacheEntry(i, System.currentTimeMillis()));
                Thread.currentThread().sleep(100);
            }

            stream.println("Waiting for cache to expire...");
            // Wait at least as long as the cache needs to expire
            Thread.currentThread().sleep((EXPIRE_SEC + 1) * 1000);
            stream.println("Finished waiting.");
        }
        catch (InterruptedException ie) {
        }

        // Inspect the result. If the list contains more than 66% of all cache entries,
        // then the test has failed.
        if (expiredTooFast.size() > COUNT * 2 / 3) {
            fail("More than 66% of all cache entries (" + expiredTooFast.size() + " out of " + COUNT +
                ") expired much too fast!");
        }
    }

    class TimeLimitedTest extends TimeLimited {

        private List _expiredTooFast;

        TimeLimitedTest(int interval, List expiredTooFast) {
            super(interval);
            this._expiredTooFast = expiredTooFast;
        }

        protected void dispose(Object o) {
            long now = System.currentTimeMillis();
            CacheEntry entry = (CacheEntry)o;
            long diff = Math.abs(now - entry.getTimestamp());
            // "diff" should be approx. EXPIRE_MILLI. If it is less than 66% of this value,
            // we put the entry into a list.
            if (diff < (EXPIRE_MILLI * 2 / 3)) {
                _expiredTooFast.add(entry);
                stream.println("Entry " + entry.getId() + " expired after " + diff +
                    " millis (should be " + EXPIRE_MILLI + ")");
            } else {
                stream.println("Entry " + entry.getId() + " expired after " + diff +
                    " millis");
            }
        }
    }


    class CacheEntry {
        final int _id;
        final long _timestamp;

        CacheEntry(int id, long timestamp) {
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




