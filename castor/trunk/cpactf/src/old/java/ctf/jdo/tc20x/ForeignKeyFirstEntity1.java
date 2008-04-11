/*
 * Copyright 2007 Ralf Joachim
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
package ctf.jdo.tc20x;

import java.util.ArrayList;
import java.util.Collection;

public class ForeignKeyFirstEntity1 {
    private int _id;
    private int _number;
    private Collection _entities = new ArrayList(0);

    public int getId() { return _id; }
    
    public void setId(final int id) { _id = id; }
    
    public int getNumber() { return _number; }
    
    public void setNumber(final int number) { _number = number; }
    
    public Collection getEntities() {
        if (_entities == null) { _entities = new ArrayList(0); }
        return _entities;
    }
   
    public void setEntities(final Collection entities) {
        _entities = entities;
    }
}
