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
package ctf.jdo.tc9x;

public final class Owner {
    
    private Integer _id;
    private String _name;
    private Product _product;

    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public Product getProduct() { return _product; }
    public void setProduct(final Product product) { _product = product; }
    
    public String toString () {
        return this.getClass().getName() + "/" + getId();
    }
}
