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
 * Holder object for flags related to update/remove operations.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public class UpdateAndRemovedFlags extends UpdateFlags {
    
    /**
     * Indicates whether an item needs to be removed.
     */
    private boolean _removed;
    
    public final boolean getRemoved() {
        return this._removed;
    }
    
    public final void setRemoved(final boolean removed) {
        this._removed = removed;
    }
}
