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
 */
package org.castor.persist.cache;

import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.persist.OID;

/**
 * Utility class to store 'data' accessed through Castor JDO in performance caches.
 * 
 * @author <a href="mailto:werner DOT guttmann @ gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2005-12-01 14:45:18 -0700 (Thu, 01 Dec 2005) $
 * @since 0.9.9
 */
public final class CacheEntry implements java.io.Serializable {
    //--------------------------------------------------------------------------

    /** SerialVersionUID. */
    private static final long serialVersionUID = -5165311222436920871L;

    /** OID of the entry to be cached. */
    private OID _oid;

    /** Actual data to be cached. */
    private Object[] _entry;

    /** Associated time stamp. */
    private long _timeStamp = TimeStampable.NO_TIMESTAMP;

    //--------------------------------------------------------------------------

    public CacheEntry() { }
    
    public CacheEntry(final OID oid, final Object[] entry, final long timeStamp) {
        _oid = oid;
        _entry = entry;
        _timeStamp = timeStamp;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Get OID of the entry to be cached.
     * 
     * @return OID of the entry to be cached.
     */
    public OID getOID() {
        return _oid;
    }

    /**
     * Set OID of the entry to be cached.
     * 
     * @param oid   OID of the entry to be cached.
     */
    public void setOID(final OID oid) {
        _oid = oid;
    }

    /**
     * Get actual data to be cached.
     * 
     * @return Actual data to be cached.
     */
    public Object[] getEntry() {
        return _entry;
    }

    /**
     * Set actual data to be cached.
     * 
     * @param entry Actual data to be cached.
     */
    public void setEntry(final Object[] entry) {
        _entry = entry;
    }

    /**
     * Get associated time stamp.
     * 
     * @return Associated time stamp.
     */
    public long getTimeStamp() {
        return _timeStamp;
    }

    /**
     * Set associated time stamp.
     * 
     * @param stamp Associated time stamp.
     */
    public void setTimeStamp(final long stamp) {
        _timeStamp = stamp;
    }

    //--------------------------------------------------------------------------
}
