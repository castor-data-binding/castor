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

import org.exolab.castor.persist.OID;

/**
 * Immutable class to store 'data' accessed through Castor JDO in performance caches.
 * 
 * @author <a href="mailto:werner DOT guttmann @ gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2005-12-01 14:45:18 -0700 (Thu, 01 Dec 2005) $
 * @since 0.9.9
 */
public final class CacheEntry implements java.io.Serializable {
    //--------------------------------------------------------------------------

    /** SerialVersionUID. */
    private static final long serialVersionUID = -5165311222436920871L;

    /** OID of the entity cached. */
    private final OID _oid;

    /** Actual values of the entity cached. */
    private final Object[] _values;

    /** Associated version of the entity. */
    private final long _version;

    //--------------------------------------------------------------------------

    /**
     * Construct CacheEntry with given OID, values and version.
     * 
     * @param oid OID of the entity cached.
     * @param values Actual values of the entity cached.
     * @param version Associated version of the entity.
     */
    public CacheEntry(final OID oid, final Object[] values, final long version) {
        _oid = oid;
        _values = values;
        _version = version;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Get OID of the entity cached.
     * 
     * @return OID of the entity cached.
     */
    public OID getOID() {
        return _oid;
    }

    /**
     * Get actual values of the entity cached.
     * 
     * @return Actual values of the entity cached.
     */
    public Object[] getValues() {
        return _values;
    }

    /**
     * Get associated version of the entity.
     * 
     * @return Associated version of the entity.
     */
    public long getVersion() {
        return _version;
    }

    //--------------------------------------------------------------------------
}
