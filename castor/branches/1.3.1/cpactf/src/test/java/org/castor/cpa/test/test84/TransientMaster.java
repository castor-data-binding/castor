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
package org.castor.cpa.test.test84;

import java.util.Collection;

import org.junit.Ignore;

@Ignore
public final class TransientMaster {
    private Integer _id;
    private String _name;
    private Integer _property1;
    private Integer _property2;
    private Integer _property3;
    private TransientChildOne _entityTwo;
    private Collection<TransientChildTwo> _entityThrees;
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public Integer getProperty1() { return _property1; }
    public void setProperty1(final Integer property) { _property1 = property; }
    
    public Integer getProperty2() { return _property2; }
    public void setProperty2(final Integer property2) { _property2 = property2; }

    public Integer getProperty3() { return _property3; }
    public void setProperty3(final Integer property3) { _property3 = property3; }
    
    public TransientChildOne getEntityTwo() { return _entityTwo; }
    public void setEntityTwo(final TransientChildOne entityTwo) {
        _entityTwo = entityTwo;
    }

    public Collection<TransientChildTwo> getEntityThrees() { return _entityThrees; }
    public void setEntityThrees(final Collection<TransientChildTwo> entityThrees) {
        _entityThrees = entityThrees;
    }
}
