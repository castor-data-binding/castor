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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;

@Ignore
public final class TestIterator {

    private List<IteratorItem> _item = new ArrayList<IteratorItem>();
    
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        _id = id;
    }

    public boolean containsItem(final IteratorItem item) {
        if (_item == null || _item.size() == 0) {
            return false;
        }
        return _item.contains(item);
    }

    public Iterator<IteratorItem> itemIterator() {
        if (_item == null || _item.size() == 0) {
            return new ArrayList<IteratorItem>().iterator();
        }
        return _item.iterator();
    }

    public void removeItem(final IteratorItem item) {
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

    public Iterator<IteratorItem> getItem() {
        return _item.iterator();
    }

    public void setItem(final Iterator<IteratorItem> item) {
        _item.clear();
        while (item.hasNext()) {
            _item.add(item.next());
        }
    }

    public void addItem(final IteratorItem item) {
        if (_item == null) {
            _item = new ArrayList<IteratorItem>();
        }
        _item.add(item);
        item.setTestCol(this);
    }
}
