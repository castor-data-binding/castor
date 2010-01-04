/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test15;

import org.junit.Ignore;

@Ignore
public final class PrimaryKeysAddress {
    private int _id;
    private String _street;
    private String _city;
    private String _state;
    private String _zip;
    private PrimaryKeysPerson _person;
    
    public PrimaryKeysAddress() { }

    public PrimaryKeysAddress(final int id, final String street,
            final String city, final String state, final String zip) {
        _id = id;
        _street = street;
        _city = city;
        _state = state;
        _zip = zip;
    }

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }
    
    public String getStreet() { return _street; }
    public void setStreet(final String street) { _street = street; }
    
    public String getCity() { return _city; }
    public void setCity(final String city) { _city = city; }
    
    public String getState() { return _state; }
    public void setState(final String state) { _state = state; }
    
    public String getZip() { return _zip; }
    public void setZip(final String zip) { _zip = zip; }
    
    public PrimaryKeysPerson getPerson() { return _person; }
    public void setPerson(final PrimaryKeysPerson person) { _person = person; }
}
