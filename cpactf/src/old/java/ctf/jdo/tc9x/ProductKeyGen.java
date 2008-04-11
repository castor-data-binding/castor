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

import java.util.Collection;

public class ProductKeyGen {
    private int _id;
    private String _name;
    private ProductDetail _detail;
    private Collection _orders;

    public final int getId() { return _id; }
    public final void setId(final int id) { _id = id; }

    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }

    public final ProductDetail getDetail() { return _detail; }
    public final void setDetail(final ProductDetail detail) { _detail = detail; }

    public final Collection getOrders() { return _orders; }
    public final void setOrders(final Collection orders) { _orders = orders; }
}
