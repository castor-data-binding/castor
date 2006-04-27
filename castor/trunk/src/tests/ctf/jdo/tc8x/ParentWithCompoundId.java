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
package ctf.jdo.tc8x;

public class ParentWithCompoundId {
    private Integer _id1;
    private Integer _id2;
    private String _name;
    
    public final Integer getId1() {
        return _id1;
    }
    
    public final void setId1(final Integer id) {
        _id1 = id;
    }
    
    public final Integer getId2() {
        return _id2;
    }

    public final void setId2(final Integer id2) {
        _id2 = id2;
    }
    
    public final String getName() {
        return _name;
    }
    
    public final void setName(final String name) {
        _name = name;
    }
}
