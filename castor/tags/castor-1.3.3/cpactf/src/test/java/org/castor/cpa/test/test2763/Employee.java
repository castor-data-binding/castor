/*
 * Copyright 2009 Lukas Lang
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
package org.castor.cpa.test.test2763;

import org.junit.Ignore;

/**
 * A simple Employee entity.
 * 
 * @author lukas.lang
 */
@Ignore
public final class Employee {
    /** A unique identifier. */
    private long _id;

    /** The {@link Employee}'s name. */
    private String _name;

    /** The entity version. */
    private long _version;

    /**
     * @return the id
     */
    public long getId() {
        return _id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final long id) {
        _id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        _name = name;
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return _version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final long version) {
        _version = version;
    }
}
