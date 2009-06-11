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

import org.junit.Ignore;

@Ignore
public final class ColArray extends Col {

    private Item[] _item;

    public boolean containsItem(final Item item) {
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

    public Iterator<Item> itemIterator() {
        if ((_item == null) || (_item.length == 0)) {
            return EMPTY_ITORATOR;
        }
        return new ColArrayIterator(_item);
    }

    public void addItem(final Item item) {
    }

    public void removeItem(final Item item) {
        int position = -1;
        for (int i = 0; i < _item.length; i++) {
            if (_item[i].equals(item)) {
                position = i;
            }
        }
        if (position >= 0) {
            Item[] aux = new Item[_item.length - 1];
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

    public void setItems(final Item[] item) {
        _item = item;
    }

    public Item[] getItems() {
        return _item;
    }
}
