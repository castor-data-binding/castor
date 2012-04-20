/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test89;

import org.junit.Ignore;

@Ignore
public class LazyAddress {
    private int _id;
    private String _street;
    private String _city;
    private String _state;
    private String _zip;
    private LazyPerson _person;

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }
    
    public void setStreet(final String street) { _street = street; }
    public String getStreet() { return _street; }
    
    public void setCity(final String city) { _city = city; }
    public String getCity() { return _city; }
    
    public void setState(final String state) { _state = state; }
    public String getState() { return _state; }
    
    public void setZip(final String zip) { _zip = zip; }
    public String getZip() { return _zip; }
    
    public void setPerson(final LazyPerson person) { _person = person; }
    public LazyPerson getPerson() { return _person; }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(_id);
        sb.append(": ");
        sb.append(_street);
        sb.append("  ");
        sb.append(_city);
        sb.append("  ");
        sb.append(_state);
        sb.append("  ");
        sb.append(_zip);
        sb.append(" of ");
        if (_person != null) {
            sb.append(_person.getLastName());
            sb.append(", ");
            sb.append(_person.getFirstName());
        } else {
            sb.append("--nobody--");
        }
        sb.append(">");
        return sb.toString();
    }
    
    public int hashCode() {
        return _id;
    }
    
    public boolean equals(final Object o) {
        if (this == o) { return true; }

        if (o instanceof LazyAddress) {
            LazyAddress other = (LazyAddress) o;
            return (other._id == _id);
        }
        return false;
    }
}
