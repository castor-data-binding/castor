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

import org.junit.Ignore;

@Ignore
public final class TestArray {
    private int _id;
    private ArrayItem[] _item;

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        _id = id;
    }

    public boolean containsItem(final ArrayItem item) {
        if ((_item == null) || (_item.length == 0)) {
            return false;
        }
        for (int i = 0; i < _item.length; i++) {
            if (_item[i].equals(item)) {
                return true;
            }
        }
        return false;
    }
    
    public Iterator<ArrayItem> itemIterator() {
        if ((_item == null) || (_item.length == 0)) {
            return new ArrayList<ArrayItem>().iterator();
        }
        return new ArrayIterator(_item);
    }

    public void addItem(final ArrayItem item) {
    }

    public void removeItem(final ArrayItem item) {
        int position = -1;
        for (int i = 0; i < _item.length; i++) {
            if (_item[i].equals(item)) {
                position = i;
            }
        }
        if (position >= 0) {
            ArrayItem[] aux = new ArrayItem[_item.length - 1];
            for (int i = 0; i < _item.length; i++) {
                if (i < position) {
                    aux[i] = _item[i];
                }
                if (i > position) {
                    aux[i - 1] = _item[i];
                }
            }
            _item = aux;
        }
    }

    public int itemSize() {
        if (_item == null) {
            return 0;
        }
        return _item.length;
    }


    public void setItems(final ArrayItem[] item) {
        _item = item;
    }

    public ArrayItem[] getItems() {
        return _item;
    }

    public String toString() {
        return getClass().getName() + ":" + _id;
    }
}
