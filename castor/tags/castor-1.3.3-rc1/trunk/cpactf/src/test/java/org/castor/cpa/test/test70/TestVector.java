/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test70;

import java.util.Iterator;
import java.util.Vector;

import org.junit.Ignore;

@Ignore
public final class TestVector {

    private Vector<VectorItem> _item;
    
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        _id = id;
    }

    public boolean containsItem(final VectorItem item) {
        if ((_item == null) || (_item.size() == 0)) {
            return false;
        }
        return _item.contains(item);
    }

    public Iterator<VectorItem> itemIterator() {
        if ((_item == null) || (_item.size() == 0)) {
            return new Vector<VectorItem>().iterator();
        }
        return _item.iterator();
    }

    public void removeItem(final VectorItem item) {
        if (_item != null) {
            _item.remove(item);
            item.setTestCol(null);
        }
    }

    public int itemSize() {
        if (_item == null) {
            return 0;
        }
        return _item.size();
    }

    public Vector<VectorItem> getItem() {
        return _item;
    }

    public void setItem(final Vector <VectorItem> item) {
        _item = item;
    }

    public void addItem(final VectorItem item) {
        if (_item == null) {
            _item = new Vector<VectorItem>();
        }
        _item.add(item);
        item.setTestCol(this);
    }
}
