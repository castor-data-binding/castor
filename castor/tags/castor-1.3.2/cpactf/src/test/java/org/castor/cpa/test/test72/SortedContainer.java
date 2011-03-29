/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test72;

import java.util.SortedSet;

import org.junit.Ignore;

@Ignore
public final class SortedContainer {
    private Integer _id;
    private String _name;
    private SortedSet<SortedContainerItem> _twos;
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public SortedSet<SortedContainerItem> getTwos() { return _twos; }
    public void setTwos(final SortedSet<SortedContainerItem> twos) { _twos = twos; }
}
