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
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.util.ClassLoadingUtils;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.spi.Identity;

/**
 * This class is a proxy for different types of Collection and Maps.
 */
public abstract class CollectionProxy {

    private static final Log LOG = LogFactory.getLog(CollectionProxy.class);
    
    public abstract Object getCollection();

    public abstract void add(Identity key, Object value);

    public abstract void close();

    public static CollectionProxy create(final FieldMolder fieldMolder,
            final Object object, final ClassLoader classLoader) {
        Class cls = fieldMolder.getCollectionType();
        if (cls == Vector.class) {
            return new ColProxy(fieldMolder, object, classLoader, new Vector());
        } else if (cls == ArrayList.class) {
            return new ColProxy(fieldMolder, object, classLoader, new ArrayList());
        } else if (cls == Collection.class) {
            return new ColProxy(fieldMolder, object, classLoader, new ArrayList());
        } else if (cls == Set.class) {
            return new ColProxy(fieldMolder, object, classLoader, new HashSet());
        } else if (cls == HashSet.class) {
            return new ColProxy(fieldMolder, object, classLoader, new HashSet());
        } else if (cls == Hashtable.class) {
            return new MapProxy(fieldMolder, object, classLoader, new Hashtable());
        } else if (cls == HashMap.class) {
            return new MapProxy(fieldMolder, object, classLoader, new HashMap());
        } else if (cls == Iterator.class) {
            return new IteratorProxy(fieldMolder, object, classLoader, new ArrayList());
        } else if (cls == Enumeration.class) {
            return new EnumerationProxy(fieldMolder, object, classLoader,
                    new ArrayList());
        } else if (cls == Map.class) {
            return new MapProxy(fieldMolder, object, classLoader, new HashMap());
        } else if (cls == SortedSet.class) {
            String comparatorClassName = fieldMolder.getComparator();
            if (comparatorClassName != null) {
                Comparator comparator = loadComparator(classLoader, comparatorClassName);
                return new ColProxy(fieldMolder, object, classLoader, new TreeSet(comparator));
            }
            return new ColProxy(fieldMolder, object, classLoader, new TreeSet());
        } else {
            throw new IllegalArgumentException(
                    "Collection Proxy doesn't exist for this type : " + cls);
        }
    }

    /**
     * Instantiates the specified java.util.Comparator implementation.
     * @param classLoader The class loader to use for instantiation.
     * @param comparatorClassName The name of the java.util.Comparator implementation.
     * @return An instance of the specified java.util.Comparator implementation.
     */
    private static Comparator loadComparator(final ClassLoader classLoader,
            final String comparatorClassName) {
        Comparator comparator;
        String message = "Problem instantiating instance of " + comparatorClassName;
        try {
            Class comparatorClass = ClassLoadingUtils.loadClass(classLoader, comparatorClassName);
            comparator = (Comparator) comparatorClass.newInstance();
        } catch (InstantiationException e) {
            LOG.error (message);
            throw new IllegalArgumentException(message);
        } catch (IllegalAccessException e) {
            message = "Problem accessing constructor of " + comparatorClassName;
            LOG.error (message);
            throw new IllegalArgumentException(message);
        } catch (ClassNotFoundException e) {
            LOG.error ("Problem loading class for " + comparatorClassName);
            throw new IllegalArgumentException(message);
        }
        return comparator;
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

        public void add(final Identity key, final Object value) {
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

    private static final class EnumerationProxy extends CollectionProxy {
        
        private Collection _collection;

        private FieldMolder _fm;

        private Object _object;

        private ClassLoader _cl;

        private EnumerationProxy(final FieldMolder fm, final Object object,
                final ClassLoader cl, final Collection collection) {
            _cl = cl;
            _fm = fm;
            _collection = collection;
            _object = object;
        }

        public Object getCollection() {
            return Collections.enumeration(_collection);
        }

        public void add(final Identity key, final Object value) {
            if (!_fm.isAddable()) {
                // [TODO] Find a better way to express this scenario where no
                // setter is specified either.
                _collection.add(value);
            } else {
                _fm.addValue(_object, value, _cl);
            }
        }

        public void close() {
            if (!_fm.isAddable()) {
                _fm.setValue(_object, Collections.enumeration(_collection), _cl);
            }
        }
    }

    private static final class IteratorProxy extends CollectionProxy {
        
        private Collection _collection;

        private FieldMolder _fieldMolder;

        private Object _object;

        private ClassLoader _cl;

        private IteratorProxy(final FieldMolder fm, final Object object,
                final ClassLoader cl, final Collection col) {
            _cl = cl;
            _fieldMolder = fm;
            _collection = col;
            _object = object;
        }

        public Object getCollection() {
            return _collection.iterator();
        }

        public void add(final Identity key, final Object value) {
            if (!_fieldMolder.isAddable()) {
                // [TODO] Find a better way to express this scenario where no
                // setter is specified either.
                _collection.add(value);
            } else {
                _fieldMolder.addValue(_object, value, _cl);
            }
        }

        public void close() {
            if (!_fieldMolder.isAddable()) {
                _fieldMolder.setValue(_object, _collection.iterator(), _cl);
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

        public void add(final Identity key, final Object value) {
            if (key.size() == 1) {
                _map.put(key.get(0), value);
            } else {
                _map.put(key, value);
            }
        }

        public void close() {
            if (!_fm.isAddable()) {
                _fm.setValue(_object, _map, _cl);
            }
        }

    }
}
