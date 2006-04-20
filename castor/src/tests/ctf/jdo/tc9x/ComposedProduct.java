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

import java.util.Iterator;
import java.util.Vector;

import org.exolab.castor.jdo.TimeStampable;

public final class ComposedProduct extends Product1893 implements TimeStampable {
    private String _name;
    private Vector _subProducts = new Vector();
    private long _timeStamp;

    public String getExtraName() { return _name; }
    public void setExtraName(final String name) { _name = name; }

    public String getExtraDescription() { return _name; }
    public void setExtraDescription(final String name) { _name = name; }

    public Vector getSubProducts() { return _subProducts; }
    public void setSubProducts(final Vector products) {
        Iterator i = products.iterator();
        while (i.hasNext()) {
            ComposedProduct cp = (ComposedProduct) i.next();
            if (!_subProducts.contains(cp)) {
                _subProducts.add(cp);
                cp.addComposition(this);
            }
        }
    }
    public void addSubProduct(final Product1893 product) {
        if (!_subProducts.contains(product)) {
            _subProducts.addElement(product);
            product.addComposition(this);
        }
    }
}
