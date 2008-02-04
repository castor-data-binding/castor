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
package org.castor.persist;

/**
 * Holds several flags indicating whetehr particular actions related to updating data 
 * items should take place.
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public class UpdateFlags {
    /** Indicates whether a data chnage needs to be persisted. */
    private boolean _updatePersist = false;

    /** Indicates whetehr the caches need to be updated. */
    private boolean _updateCache = false;

    /** Holds the value of a new field. */
    private Object _newField;

    public final boolean getUpdateCache() {
        return this._updateCache;
    }

    public final void setUpdateCache(final boolean updateCache) {
        this._updateCache = updateCache;
    }

    public final boolean getUpdatePersist() {
        return this._updatePersist;
    }

    public final void setUpdatePersist(final boolean updatePersist) {
        this._updatePersist = updatePersist;
    }

    public final Object getNewField() {
        return this._newField;
    }

    public final void setNewField(final Object newField) {
        this._newField = newField;
    }
}
