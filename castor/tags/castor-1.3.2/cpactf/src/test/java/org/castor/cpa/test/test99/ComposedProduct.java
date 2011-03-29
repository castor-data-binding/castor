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
package org.castor.cpa.test.test99;

import java.util.Iterator;
import java.util.Vector;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public final class ComposedProduct extends Product1893 implements TimeStampable {
    private String _name;
    private Vector<Product1893> _subProducts = new Vector<Product1893>();

    public String getExtraName() { return _name; }
    public void setExtraName(final String name) { _name = name; }

    public String getExtraDescription() { return _name; }
    public void setExtraDescription(final String name) { _name = name; }

    public Vector<Product1893> getSubProducts() { return _subProducts; }
    public void setSubProducts(final Vector<Product1893> products) {
        Iterator<Product1893> i = products.iterator();
        while (i.hasNext()) {
            Product1893 cp = i.next();
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
