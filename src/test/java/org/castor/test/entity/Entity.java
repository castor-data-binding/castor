/*
 * Copyright 2007 Werner Guttmann
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
package org.castor.test.entity;

/**
 * Domain entity for XMLContext JUnit test case.
 * 
 * @author <a herf="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1.2
 */
public class Entity {

    /**
     * Unique identifier.
     */
    private int _id;

    /**
     * Entity name.
     */
    private String _name;

    /**
     * Retrusn the unique identifier.
     * @return the unique identifier.
     */
    public int getId() {
        return _id;
    }

    /**
     * Sets a new unique identifier.
     * @param id a new unique identifier.
     */
    public void setId(final int id) {
        this._id = id;
    }

    /**
     * Retrusn the entity name.
     * @return the entity name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets a new entity name.
     * @param id a new entity name.
     */
    public void setName(final String name) {
        this._name = name;
    }

}
