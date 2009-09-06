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

public class ForeignKeyFirstEntityN {
    private int _id;
    private ForeignKeyFirstEntity1 _entity;

    public int getId() { return _id; }
    
    public void setId(final int id) { _id = id; }
    
    public ForeignKeyFirstEntity1 getEntity() { return _entity; }
    
    public void setEntity(final ForeignKeyFirstEntity1 entity) { _entity = entity; }
}
