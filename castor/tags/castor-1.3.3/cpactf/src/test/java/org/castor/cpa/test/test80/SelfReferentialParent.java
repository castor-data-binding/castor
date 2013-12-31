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
package org.castor.cpa.test.test80;

import java.util.List;

import org.junit.Ignore;

@Ignore
public class SelfReferentialParent {
    
    private Integer _id;
    private String _name;
    private SelfReferentialParent _parent;
    private List<SelfReferentialParent> _entities;
    
    public final Integer getId() {
        return _id;
    }
    
    public final void setId(final Integer id) {
        _id = id;
    }
    
    public final String getName() {
        return _name;
    }
    
    public final void setName(final String name) {
        _name = name;
    }

    public final List<SelfReferentialParent> getEntities() {
        return _entities;
    }

    public final void setEntities(final List<SelfReferentialParent> entities) {
        _entities = entities;
    }

    public final SelfReferentialParent getParent() {
        return _parent;
    }

    public final void setParent(final SelfReferentialParent parent) {
        _parent = parent;
    }
}
