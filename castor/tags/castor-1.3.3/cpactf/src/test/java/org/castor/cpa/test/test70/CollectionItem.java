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
public class CollectionItem {

    private int _id;
    private TestCollection _testCol;

    public CollectionItem() {
    }

    public CollectionItem(final int id) {
        _id = id;
    }

    public final int getId() {
        return _id;
    }

    public final void setId(final int id) {
        _id = id;
    }

    public final TestCollection getTestCol() {
        return _testCol;
    }

    public final void setTestCol(final TestCollection testCol) {
        _testCol = testCol;
    }

    public final String toString() {
        return getClass().getName() + ":" + _id;
    }

    public final int hashCode() {
        return _id;
    }

    public final boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof CollectionItem)) {
            return false;
        }

        CollectionItem item = (CollectionItem) object;
        return item._id == _id;
    }
    
    public class CollectionItemIterator implements Iterator<CollectionItem> {
        private CollectionItem[] _item;
        private int _position = 0;

        public CollectionItemIterator(final CollectionItem[] item) {
            _item = item;
        }
        
        public boolean hasNext() {
            return _position < _item.length;
        }

        public CollectionItem next() {
            return _item[_position++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
