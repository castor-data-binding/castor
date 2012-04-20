package org.castor.cpa.test.test08;

import org.junit.Ignore;

@Ignore
public final class CacheEntry {
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
