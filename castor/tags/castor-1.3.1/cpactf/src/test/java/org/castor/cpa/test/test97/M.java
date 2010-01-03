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
package org.castor.cpa.test.test97;

import java.util.Collection;

import org.junit.Ignore;

@Ignore
public final class M {
    private int _id;
    private String _name;
    private Collection<N> _ns;

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public Collection<N> getNs() { return _ns; }
    public void setNs(final Collection<N> ns) { _ns = ns; }
}
