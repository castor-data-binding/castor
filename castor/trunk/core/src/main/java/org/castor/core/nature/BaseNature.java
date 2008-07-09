/*
 * Copyright 2008 Lukas Lang
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
 */
package org.castor.core.nature;

import java.util.LinkedList;
import java.util.List;

/**
 * Adds property handle methods and a constructor including a health check. See
 * constructor comments for more details.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 * 
 */
public abstract class BaseNature implements Nature {

    /**
     * The PropertyHolder in focus.
     */
    private PropertyHolder _holder = null;

    /**
     * Constructor taking a {@link PropertyHolder}. Must be called from
     * subclasses. Does a health check on the given PropertyHolder, whether the
     * extending Nature exists.
     * 
     * @param holder
     *            a PropertyHolder.
     */
    protected BaseNature(final PropertyHolder holder) {
        if (holder == null) {
            throw new NullPointerException("Holder must be set");
        }
        // Health check
        if (holder.hasNature(getId())) {
            _holder = holder;
        } else {
            throw new IllegalStateException(getId()
                    + " Nature must be set before");
        }
    }

    /**
     * Returns the property mapped to the key or null if not set before.
     * 
     * @param key
     *            to look up.
     * @return value or null if not found.
     */
    protected final Object getProperty(final String key) {
        return _holder.getProperty(addPrefix(key));
    }

    /**
     * Sets the property for a given. Key must NOT be null, but can be an empty
     * String.
     * 
     * @param property
     *            to set.
     * @param key
     *            to insert.
     */
    protected final void setProperty(final String key, final Object property) {
        if (key != null) {
            _holder.setProperty(addPrefix(key), property);
        }
    }

    /**
     * Generates a Key by adding a prefix.
     * 
     * @param key
     *            to use.
     * @return prefix + given key.
     */
    private String addPrefix(final String key) {
        StringBuffer buf = new StringBuffer();
        buf.append(getId());
        buf.append(key);
        return buf.toString();
    }

    /**
     * Returns boolean value of the property or false if property value is null.
     * Make sure, not to request a property, which does not have a boolean
     * value!
     * 
     * @param propertyName
     *            name of the property.
     * @return false if null or false.
     */
    protected final boolean getBooleanPropertyDefaultFalse(
            final String propertyName) {
        Boolean b = (Boolean) this.getProperty(propertyName);
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }

    /**
     * Returns the {@link PropertyHolder}.
     * @return the holder
     */
    protected final PropertyHolder getHolder() {
        return _holder;
    }

    /**
     * Returns value of the property as a List. If the property was not set
     * before, a new List will be returned. Make sure, not to request a
     * property, which is not a List!
     * 
     * @param propertyName
     *            name of the property.
     * @return A List.
     */
    protected List getPropertyAsList(String property) {
        List list = (List) getProperty(property);
        if (list == null) {
            list = new LinkedList();
            this.setProperty(property, list);
        }
        return list;
    }
}
