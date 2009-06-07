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
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.junit.Ignore;

@Ignore
public final class ColEnumeration extends Col {

    private List < Item > _item = new ArrayList < Item >();

    public boolean containsItem(final Item item) {
        if (_item == null || _item.size() == 0) {
            return false;
        }
        return _item.contains(item);
    }

    public Iterator < Item > itemIterator() {
        if (_item == null || _item.size() == 0) {
            return EMPTY_ITORATOR;
        }
        return _item.iterator();
    }

    public void removeItem(final Item item) {
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

    public Enumeration < Item > getItem() {
        return Collections.enumeration(_item);
    }

    public void setItem(final Enumeration < Item > item) {
        _item.clear();
        while (item.hasMoreElements()) {
            _item.add(item.nextElement());
        }
    }

    public void addItem(final Item item) {
        if (_item == null) {
            _item = new ArrayList < Item > ();
        }
        _item.add(item);
        item.setTestCol(this);
    }
}
