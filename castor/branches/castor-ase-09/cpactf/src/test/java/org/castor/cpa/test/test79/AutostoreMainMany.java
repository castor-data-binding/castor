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
package org.castor.cpa.test.test79;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

/**
 * Entity class for CTF test case.
 */
@Ignore
public final class AutostoreMainMany {
    
    private Integer _id;
    private String _name;
    private List<AutostoreAssociatedMany> _associatedMany =
        new ArrayList<AutostoreAssociatedMany>();

    public Integer getId() {
        return _id;
    }
    
    public void setId(final Integer id) {
        _id = id;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }

    public List<AutostoreAssociatedMany> getAssociatedMany() {
        return _associatedMany;
    }

    public void setAssociatedMany(final List<AutostoreAssociatedMany> associatedMany) {
        _associatedMany = associatedMany;
    }
}
