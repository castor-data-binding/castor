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
 */
package org.exolab.castor.jdo.engine;

/**
 * Simple domain class.
 * 
 * @author Lukas Lang
 */
public class Employee {

    /**
     * Id.
     */
    private int _id;
    /**
     * Address.
     */
    private Address _address;

    /**
     * @return the id
     */
    public final int getId() {
        return _id;
    }

    /**
     * @param id
     *            the id to set
     */
    public final void setId(final int newId) {
        this._id = newId;
    }

    /**
     * @return the items
     */
    public final Address getAddress() {
        return _address;
    }

    /**
     * @param items
     *            the items to set
     */
    public final void setAddress(final Address address) {
        this._address = address;
    }

}
