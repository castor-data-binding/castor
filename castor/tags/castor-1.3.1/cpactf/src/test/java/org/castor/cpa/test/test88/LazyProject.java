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
public class LazyProject {
    private int _id;
    private String _name;
    private LazyEmployee _owner;

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }
    
    public void setName(final String name) { _name = name; }
    public String getName() { return _name; }
    
    public void setOwner(final LazyEmployee owner) { _owner = owner; }
    public LazyEmployee getOwner() { return _owner; }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(_id);
        sb.append(": ");
        sb.append(_name);
        sb.append(" of ");
        if (_owner != null) {
            sb.append(_owner.getLastName());
            sb.append(", ");
            sb.append(_owner.getFirstName());
        } else {
            sb.append("--nobody--");
        }
        sb.append(">");
        return sb.toString();
    }
}
