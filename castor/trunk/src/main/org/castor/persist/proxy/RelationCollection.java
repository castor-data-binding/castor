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

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.Lazy;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.TxSynchronizable;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;

/**
 * RelationCollection implements {@link java.util.Collection}
 * It is a lazy Colllection. The collection initially contains only the
 * identities of elements of one type. If any element is needed, it will
 * be fetched "on the fly".
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public final class RelationCollection implements Collection, Lazy, TxSynchronizable {

    /**
     * Transaction to fetch an element on the fly if needed.
     */
    private TransactionContext _tx;

    /**
     * The ClassMolder of the elemtns.
     */
    private ClassMolder _molder;

    /* Vector of identity */
    private ArrayList _ids;

    /* Vector of identity */
    private ArrayList _deleted;

    /* Vector of identity */
    private ArrayList _added;

    /* Vector of object */
    private Map _loaded;

    /* the change count of the collection */
    private int _changecount;

    /* the number of elements in this collection */
    private int _size;

    /**
     * Creates an instance of RelationCollection
     * @param tx Current transaction context
     * @param enclosing Enclosing OID 
     * @param engine Associated LockEngine
     * @param molder Associated ClassMolder
     * @param amode Access mode
     * @param ids Set of identifiers.
     */
    public RelationCollection(final TransactionContext tx, final OID enclosing,
            final ClassMolder molder,
            final AccessMode amode, final ArrayList ids) {
        _tx = tx;
        _molder = molder;
        _ids = (ids != null) ? ids : new ArrayList();
        _size = _ids.size();
        _deleted = new ArrayList();
        _added = new ArrayList();
        _loaded = new HashMap();
    }


    public boolean add(final Object o) {
        Object id = _molder.getIdentity(_tx, o);
        // boolean changed = false;
        if (_ids.contains(id)) {
            if (_deleted.contains(id)) {
                _deleted.remove(id);
                _loaded.put(id, o);
                _changecount++;
                _size++;
                return true;
            }
            return _loaded.put(id, o) != o;
        }
        if (_deleted.contains(id)) {
            throw new RuntimeException("Illegal Internal State.");
        }

        if (_added.add(id)) {
            _loaded.put(id, o);
            _changecount++;
            _size++;
            return true;
        }
        return _loaded.put(id, o) != o;
    }

    public boolean addAll(final Collection c) {
        boolean changed = false;
        Iterator a = c.iterator();
        while (a.hasNext()) {
            if (add(a.next())) {
                changed = true;
            }
        }
        if (changed) {
            _changecount++;
        }
        return changed;
    }

    public void clear() {
        Iterator itor = iterator();
        while (itor.hasNext()) {
            itor.next();
            itor.remove();
        }
    }

    public boolean contains(final Object o) {
        Object ids = _molder.getIdentity(_tx, o);
        if (_added.contains(ids)) {
            return true;
        }
        if (_ids.contains(ids) && !_deleted.contains(ids)) {
            return true;
        }
        return false;
    }

    public boolean containsAll(final Collection c) {
        Iterator it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(final Object o) {
        return this == o;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private final class IteratorImp implements Iterator {
        private int _changestamp;
        private int _cursor;
        private int _iterationsize;
        private RelationCollection _parent;

        private IteratorImp(final RelationCollection rc) {
            _parent = rc;
            _changestamp = rc._changecount;
            // iterationsize is the number of elements to iterate over
            // during the life of this Iterator. Items in _deleted are
            // not included because they are duplicates of items in
            // _ids and thus are not iterated over.
            _iterationsize = _parent._added.size() + _parent._ids.size();
        }
        public boolean hasNext() {
            if (_changestamp != _parent._changecount) {
                throw new ConcurrentModificationException(
                        "Concurrent Modification is not allowed!");
            }

            if (_cursor >= _added.size()) {
                // skip deleted ids
                while ((_cursor < _iterationsize)
                        && isSkipped(_ids.get(_cursor - _added.size()))) {
                    _cursor++;
                }
            }

            if (_cursor >= _iterationsize) {
                return false;
            }
            return true;
        }
        public Object next() {
            if (_changestamp != _parent._changecount) {
                throw new ConcurrentModificationException(
                        "Concurrent Modification is not allowed!");
            }
            // only needed if application did not call hasNext(), will skip
            // deleted ids
            if (!hasNext()) {
                throw new NoSuchElementException(
                        "Read after the end of iterator!");
            }

            Object id;
            Object o;
            if (_cursor < _added.size()) {
                id = _added.get(_cursor++);
                o = _loaded.get(id);
                if (o != null) {
                    return o;
                }
                return lazyLoad(id);
            }
            // the deleted ids were skipped by hasNext(), get is safe
            id = _ids.get(_cursor++ - _added.size());

            o = _loaded.get(id);
            if (o != null) {
                return o;
            }
            return lazyLoad(id);
        }
        private boolean isSkipped(final Object id) {
            if (_deleted.contains(id)) {
                return true;
            }
            // make sure the object is not deleted in
            // the current transaction outside this class
            OID oid = new OID(_parent._molder, id);
            return _parent._tx.isDeletedByOID(oid);
        }
        private Object lazyLoad(final Object ids) {
            Object o;

            if (!_tx.isOpen()) {
                throw new RuntimeException("Transaction is closed!");
            }

            try {
                ProposedEntity proposedValue = new ProposedEntity(_parent._molder);
                o = _parent._tx.load(ids, proposedValue, null);
                _parent._loaded.put(ids, o);
                return o;
            } catch (LockNotGrantedException e) {
                throw new RuntimeException(
                        "Lock Not Granted for lazy loaded object\n" + e);
            } catch (PersistenceException e) {
                throw new RuntimeException(
                        "PersistenceException for lazy loaded object\n" + e);
            }
        }

        public void remove() {
            if (_cursor <= 0) {
                throw new IllegalStateException(
                        "Method next() must be called before remove!");
            }
            if (_changestamp != _parent._changecount) {
                throw new ConcurrentModificationException(
                        "Concurrent Modification is not allowed!");
            }

            Object id;
            _cursor--;
            if (_cursor < _added.size()) {
                _parent._added.remove(_cursor);
                _parent._size--;
                // Manipulating the _added array must be
                // reflected in iterationsize
                _iterationsize--;
                _parent._changecount++;
                _changestamp = _parent._changecount;
            } else {
                // backward to the first not deleted ids
                id = _ids.get(_cursor);
                while (_deleted.contains(id)) {
                    id = _ids.get(_cursor--);
                }
                if (_cursor < _added.size()) {
                    _parent._added.remove(id);
                    _parent._size--;
                    // Manipulating the _added array must be
                    // reflected in iterationsize
                    _iterationsize--;
                    _parent._changecount++;
                    _changestamp = _parent._changecount;
                } else {
                    _parent._deleted.add(id);
                    _parent._size--;
                    _parent._changecount++;
                    _cursor++; // undo decrement of cursor above
                    _changestamp = _parent._changecount;
                }
            }
        }
    }

    public Iterator iterator() {
        return new IteratorImp(this);
    }

    public boolean remove(final Object o) {
        Object id = _molder.getIdentity(_tx, o);

        if (_deleted.contains(id)) {
            return false;
        }

        if (_added.contains(id)) {
            _added.remove(id);
            _changecount++;
            _size--;
            return true;
        } else if (_ids.contains(id)) {
            // We need to have the object in our _loaded map because
            // when the TX tries to commit later, it will call our
            // find() method for any deleted objects. See find()
            // [below] for details.
            _loaded.put(id, o);
            _deleted.add(id);
            _changecount++;
            _size--;
            return true;
        }

        return false;
    }

    public boolean removeAll(final Collection c) {
        boolean changed = false;
        Iterator it = c.iterator();
        while (it.hasNext()) {
            if (remove(it.next())) {
                changed = true;
            }
        }
        if (changed) {
            _changecount++;
        }
        return changed;
    }

    public boolean retainAll(final Collection c) {
        Object o;
        boolean changed = false;
        Iterator org = iterator();
        while (org.hasNext()) {
            o = org.next();
            if (!c.contains(o)) {
                changed = true;
                org.remove();
            }
        }
        if (changed) {
            _changecount++;
        }
        return changed;
    }

    public int size() {
        return _size;
    }

    public Object[] toArray() {
        Object[] result = new Object[size()];
        Iterator itor = iterator();

        int count = 0;

        while (itor.hasNext()) {
            // result = (Object[])itor.next();
            // bug 1391
            result[count++] = itor.next();
        }
        return result;
    }

    public Object[] toArray(final Object[] a) {
        if (a == null) {
            throw new NullPointerException();
        }

        Object[] result;
        int size = size();

        if (size <= a.length) {
            result = a;
        } else {
            result = (Object[]) Array.newInstance(a.getClass()
                    .getComponentType(), size);
        }

        Iterator itor = iterator();
        int count = 0;
        while (itor.hasNext()) {
            result[count++] = itor.next();
        }

        // patch the extra space with null
        while (count < result.length) {
            result[count++] = null;
        }
        return result;
    }

    public ArrayList getIdentitiesList() {
        ArrayList result = new ArrayList();
        result.addAll(_ids);
        result.addAll(_added);
        result.removeAll(_deleted);
        return result;
    }

    public Object find(final Object ids) {
        return _loaded.get(ids);
    }

    public ArrayList getDeleted() {
        return (ArrayList) _deleted.clone();
    }

    public ArrayList getAdded() {
        return (ArrayList) _added.clone();
    }

    public void committed(final TransactionContext tx) {
        // just reset state if we are called in our transaction
        if (tx == _tx) {
            _added = new ArrayList();
            _deleted = new ArrayList();
            _changecount = 0;
            // ClassMolder registered us, we have to unregister
            tx.removeTxSynchronizable(this);
        }
    }

    public void rolledback(final TransactionContext tx) {
        committed(tx);
    }
}
