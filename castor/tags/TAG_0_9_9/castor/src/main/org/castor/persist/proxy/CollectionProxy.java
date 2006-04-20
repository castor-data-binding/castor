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
 */
package org.castor.persist.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.exolab.castor.persist.FieldMolder;

/**
 * This class is a proxy for different types of Collection and Maps.
 */
public abstract class CollectionProxy {

    public abstract Object getCollection();

    public abstract void add(Object key, Object value);

    public abstract void close();

    public static CollectionProxy create(final FieldMolder fieldMolder,
            final Object object, final ClassLoader classLoader) {
        Class cls = fieldMolder.getCollectionType();
        if (cls == Vector.class) {
            return new ColProxy(fieldMolder, object, classLoader, new Vector());
        } else if (cls == ArrayList.class) {
            return new ColProxy(fieldMolder, object, classLoader,
                    new ArrayList());
        } else if (cls == Collection.class) {
            return new ColProxy(fieldMolder, object, classLoader,
                    new ArrayList());
        } else if (cls == Set.class) {
            return new ColProxy(fieldMolder, object, classLoader, new HashSet());
        } else if (cls == HashSet.class) {
            return new ColProxy(fieldMolder, object, classLoader, new HashSet());
        } else if (cls == Hashtable.class) {
            return new MapProxy(fieldMolder, object, classLoader,
                    new Hashtable());
        } else if (cls == HashMap.class) {
            return new MapProxy(fieldMolder, object, classLoader, new HashMap());
        } else if (cls == Map.class) {
            return new MapProxy(fieldMolder, object, classLoader, new HashMap());
        } else {
            throw new IllegalArgumentException(
                    "Collection Proxy doesn't exist for this type : " + cls);
        }
    }

    private static final class ColProxy extends CollectionProxy {
        
        private Collection _col;

        private FieldMolder _fm;

        private Object _object;

        private ClassLoader _cl;

        private ColProxy(final FieldMolder fm, final Object object,
                final ClassLoader cl, final Collection col) {
            _cl = cl;
            _fm = fm;
            _col = col;
            _object = object;
        }

        public Object getCollection() {
            return _col;
        }

        public void add(final Object key, final Object value) {
            if (!_fm.isAddable()) {
                // [TODO] Find a better way to express this scenario where no
                // setter is specified either.
                _col.add(value);
            } else {
                _fm.addValue(_object, value, _cl);
            }
        }

        public void close() {
            if (!_fm.isAddable()) {
                _fm.setValue(_object, _col, _cl);
            }
        }
    }

    private static final class MapProxy extends CollectionProxy {
        private Map _map;

        private FieldMolder _fm;

        private Object _object;

        private ClassLoader _cl;

        private MapProxy(final FieldMolder fm, final Object object,
                final ClassLoader cl, final Map map) {
            _cl = cl;
            _map = map;
            _fm = fm;
            _object = object;
        }

        public Object getCollection() {
            return _map;
        }

        public void add(final Object key, final Object value) {
            _map.put(key, value);
        }

        public void close() {
            if (!_fm.isAddable()) {
                _fm.setValue(_object, _map, _cl);
            }
        }

    }
}
