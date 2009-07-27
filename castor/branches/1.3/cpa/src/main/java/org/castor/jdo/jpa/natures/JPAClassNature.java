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
package org.castor.jdo.jpa.natures;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;

/**
 * A {@link BaseNature} extension, that gives access to information derived from
 * class bound JPA annotations.
 * 
 * @see PropertyHolder
 * @author Peter Schmidt
 * @since 1.3
 */
public class JPAClassNature extends BaseNature {

    /**
     * Instantiate a {@link JPAClassNature} to access the given 
     * {@link PropertyHolder}.
     * 
     * @param holder
     *            The underlying {@link PropertyHolder} (obviously a
     *            {@link org.castor.jdo.jpa.info.ClassInfo ClassInfo}).
     *
     * @see PropertyHolder
     * 
     */
    public JPAClassNature(final PropertyHolder holder) {
        super(holder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }
}
