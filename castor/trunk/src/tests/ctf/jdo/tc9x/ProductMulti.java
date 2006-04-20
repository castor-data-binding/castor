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
 *
 * $Id$
 *
 */
package ctf.jdo.tc9x;

public class ProductMulti {
    private int _id1;
    private int _id2;
    private String _name;
    private ProductDetail _detail;

    public final int getId1() { return _id1; }
    public final void setId1(final int id1) { _id1 = id1; }

    public final int getId2() { return _id2; }
    public final void setId2(final int id2) { _id2 = id2; }

    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }

    public final ProductDetail getDetail() { return _detail; }
    public final void setDetail(final ProductDetail detail) { _detail = detail; }
}
