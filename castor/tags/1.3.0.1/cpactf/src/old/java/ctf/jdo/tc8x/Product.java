/*
 * Copyright 2005 Ralf Joachim
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

public final class Product {
    private int         _id;
    private String      _name;
    private KindEnum    _kind;
    
    public Product() { }
    
    public Product(final int id, final String name, final KindEnum kind) {
        _id = id;
        _name = name;
        _kind = kind;
    }

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public KindEnum getKind() { return _kind; }
    public void setKind(final KindEnum kind) { _kind = kind; }
    
    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (!(other instanceof Product)) { return false; }
        
        Product prod = (Product) other;
        return (_id == prod._id)
            && (((_name == null) && (prod._name == null))
            || ((_name != null) && _name.equals(prod._name)))
            && (_kind == prod._kind);
    }
    
    public int hashCode() {
        return _id
             + ((_name == null) ? 0 : _name.hashCode())
             + ((_kind == null) ? 0 : _kind.hashCode());
    }
    
    public String toString() {
        return "Product(" + _id + ", '" + _name + "', " + _kind + ")";
    }
}
