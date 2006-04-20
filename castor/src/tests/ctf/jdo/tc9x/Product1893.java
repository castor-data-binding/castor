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

public class Product1893 implements TimeStampable {
    private int _id;
    private String _name;
    private String _desc;
    private Vector _composition = new Vector();
    private long _timeStamp;

    public final int getId() { return _id; }
    public final void setId(final int id) { _id = id; }

    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }

    public final String getDescription() { return _desc; }
    public final void setDescription(final String desc) { _desc = desc; }

    public final Vector getCompositions() { return _composition; }
    public final void setCompositions(final Vector products) {
        Iterator iter = products.iterator();
        while (iter.hasNext()) {
            ComposedProduct cp = (ComposedProduct) iter.next();
            if (!_composition.contains(cp)) {
                _composition.add(cp);
                cp.addSubProduct(this);
            }
        }
    }
    public final void addComposition(final ComposedProduct product) {
        if (!_composition.contains(product)) {
            _composition.addElement(product);
            product.addSubProduct(this);
        }
    }

    /**
     * @see org.exolab.castor.jdo.TimeStampable#jdoSetTimeStamp(long)
     */
    public final void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }

    /**
     * @see org.exolab.castor.jdo.TimeStampable#jdoGetTimeStamp()
     */
    public final long jdoGetTimeStamp() {
        return _timeStamp;
    }

    public String toString() {
        return "<id: " + _id + " name: " + _name + ">";
    }
}
