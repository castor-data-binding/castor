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

import java.util.ArrayList;

import org.junit.Ignore;

@Ignore
public final class PrimaryKeysCategory {
    private int _id;
    private String _name;
    private ArrayList<PrimaryKeysContract> _contract;
    
    public PrimaryKeysCategory() { }

    public PrimaryKeysCategory(final int id, final String name) {
        _id = id;
        _name = name;
    }

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }
    
    public ArrayList<PrimaryKeysContract> getContract() { return _contract; }
    public void setContract(final ArrayList<PrimaryKeysContract> contract) {
        _contract = contract;
    }
}
