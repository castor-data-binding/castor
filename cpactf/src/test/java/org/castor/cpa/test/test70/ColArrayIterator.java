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
public class ColArrayIterator implements Iterator<Item> {
    private Item[] _item;
    private int _position = 0;

    public ColArrayIterator(final Item[] item) {
        _item = item;
    }
    
    public boolean hasNext() {
        return _position < _item.length;
    }

    public Item next() {
        return _item[_position++];
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
