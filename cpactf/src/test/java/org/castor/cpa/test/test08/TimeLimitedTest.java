package org.castor.cpa.test.test08;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.simple.TimeLimited;
import org.junit.Ignore;

@Ignore
public class TimeLimitedTest extends TimeLimited {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TimeLimitedTest.class);
    
    private static final int EXPIRE_SEC = 10;
    private static final int EXPIRE_MILLI = EXPIRE_SEC * 1000;
	
    private List<CacheEntry> _expiredTooFast;

    
    TimeLimitedTest(final int interval, final List<CacheEntry> expiredTooFast)
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
