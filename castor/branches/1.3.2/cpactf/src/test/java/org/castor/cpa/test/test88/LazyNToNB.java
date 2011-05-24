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

import org.junit.Ignore;

@Ignore
public class LazyNToNB {
    private String _id;
    private int _status;

    public void setIdB(final String id) { _id = id; }
    public String getIdB() { return _id; }
    
    public void setStatusB(final int status) { _status = status; }
    public int getStatusB() { return _status; }
    
    public int hashCode() {
        return (_id == null) ? 0 : _id.hashCode();
    }
    
    public boolean equals(final Object otherObj) {
        try {
            LazyNToNB lb = (LazyNToNB) otherObj;
            return (lb.getIdB() == _id);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
