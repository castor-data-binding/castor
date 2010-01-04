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
package org.castor.cpa.test.test88;

import java.util.Collection;

import org.junit.Ignore;

@Ignore
public class LazyNToNA {
    private String _id;
    private int _status;
    private Collection<LazyNToNB> _refs;

    public void setIdA(final String id) { _id = id; }
    public String getIdA() { return _id; }
    
    public void setStatusA(final int status) { _status = status; }
    public int getStatusA() { return _status; }
    
    public void setRefs(final Collection<LazyNToNB> refs) { _refs = refs; }
    public Collection<LazyNToNB> getRefs() { return _refs; }
    
    public int hashCode() {
        return (_id == null) ? 0 : _id.hashCode();
    }
    
    public boolean equals(final Object otherObj) {
        try {
            LazyNToNA la = (LazyNToNA) otherObj;
            return (la.getIdA() == _id);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
