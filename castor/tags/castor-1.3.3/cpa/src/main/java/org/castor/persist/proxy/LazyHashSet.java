/*
 * Copyright 2009 Thomas Yip, Werner Guttmann, Ralf Joachim
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.spi.Identity;

/**
 * It is a lazy Collection. The collection initially contains only the
 * identities of elements of one type. If any element is needed, it will
 * be fetched "on the fly".
 *
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class LazyHashSet<E> implements LazyCollection<E>, Set<E> {

    /** Transaction to fetch an entity on the fly if needed. */
    private final TransactionContext _tx;

    /** The ClassMolder of the entities. */
    private final ClassMolder _molder;

    /** Set of identities of the entities currently contained. */
    private final List<Identity> _current = new ArrayList<Identity>();

    /** Set of identities of entities that has been removed. */
    private final List<Identity> _removed = new ArrayList<Identity>();

    /** Set of identities of entities that have been added. */
    private final List<Identity> _added = new ArrayList<Identity>();

    /** Map entities loaded to their identity. */
    private final Map<Identity, E> _loaded = new HashMap<Identity, E>();

    /** Change count of the collection. */
    private int _changecount;

    /** Number of elements in this collection. */
    private int _size;

    /** Suggested access mode */
    private AccessMode _accessMode;

    /**
     * Creates an instance of LazyHashSet.
     * 
     * @param tx Current transaction context
     * @param molder Associated ClassMolder
     * @param ids Set of identifiers.
    * @param suggestedAccessMode 
     */
    public LazyHashSet(final TransactionContext tx, final ClassMolder molder, final List<Identity> ids, AccessMode accessMode) {
        _tx = tx;
        _molder = molder;
        _accessMode = accessMode;
        
        if (ids != null) { _current.addAll(ids); }
        _size = _current.size();
    }

    public boolean add(final E entity) {
        Identity id = _molder.getIdentity(_tx, entity);
        
        if (_current.contains(id)) {
            if (_removed.contains(id)) {
                _removed.remove(id);
                _loaded.put(id, entity);
                _changecount++;
                _size++;
                return true;
            }
            return _loaded.put(id, entity) != entity;
        }
        if (_removed.contains(id)) {
            throw new RuntimeException("Illegal Internal State.");
        }

        if (_added.add(id)) {
            _loaded.put(id, entity);
            _changecount++;
            _size++;
            return true;
        }
        return _loaded.put(id, entity) != entity;
    }

    public boolean addAll(final Collection<? extends E> collection) {
        boolean changed = false;
        Iterator<? extends E> iter = collection.iterator();
        while (iter.hasNext()) {
            if (add(iter.next())) {
                changed = true;
            }
        }
        if (changed) {
            _changecount++;
        }
        return changed;
    }

    public void clear() {
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            iter.next();
            iter.remove();
        }
    }

    public boolean contains(final Object entity) {
        Identity id = _molder.getIdentity(_tx, entity);
        if (_added.contains(id)) {
            return true;
        }
        if (_current.contains(id) && !_removed.contains(id)) {
            return true;
        }
        return false;
    }

    public boolean containsAll(final Collection<?> collection) {
        Iterator<?> iter = collection.iterator();
        while (iter.hasNext()) {
            if (!contains(iter.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<E> iterator() {
        return new IteratorImp(this);
    }

    public boolean remove(final Object entity) {
        Identity id = _molder.getIdentity(_tx, entity);

        if (_removed.contains(id)) {
            return false;
        }

        if (_added.contains(id)) {
            _added.remove(id);
            _changecount++;
            _size--;
            return true;
        } else if (_current.contains(id)) {
            // We need to have the object in our _loaded map because
            // when the TX tries to commit later, it will call our
            // find() method for any deleted objects. See find()
            // [below] for details.
            _loaded.put(id, (E) entity);
            _removed.add(id);
            _changecount++;
            _size--;
            return true;
        }

        return false;
    }

	public boolean removeAll(final Collection<?> collection) {
        boolean changed = false;
        Iterator<?> iter = collection.iterator();
        while (iter.hasNext()) {
            if (remove(iter.next())) {
                changed = true;
            }
        }
        if (changed) {
            _changecount++;
        }
        return changed;
    }

	public boolean retainAll(final Collection<?> collection) {
        boolean changed = false;
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E entity = iter.next();
            if (!collection.contains(entity)) {
                changed = true;
                iter.remove();
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

        int count = 0;
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            result[count++] = iter.next();
        }
        return result;
    }

	public <A> A[] toArray(final A[] array) {
        if (array == null) {
            throw new NullPointerException();
        }

        A[] result;
        int size = size();

        if (size <= array.length) {
            result = array;
        } else {
            result = (A[]) Array.newInstance(array.getClass()
                    .getComponentType(), size);
        }

        Iterator<E> iter = iterator();
        int count = 0;
        while (iter.hasNext()) {
            result[count++] = (A) iter.next();
        }

        // patch the extra space with null
        while (count < result.length) {
            result[count++] = null;
        }
        return result;
    }

	public boolean equals(final Object o) {
        return this == o;
    }
    
    public int hashCode() {
        return super.hashCode();
    }
	
	
    private final class IteratorImp implements Iterator<E> {
        private int _changestamp;
        private int _cursor;
        private int _iterationsize;
        private LazyHashSet<E> _parent;

        private IteratorImp(final LazyHashSet<E> rc) {
            _parent = rc;
            _changestamp = rc._changecount;
            // iterationsize is the number of elements to iterate over
            // during the life of this Iterator. Items in _deleted are
            // not included because they are duplicates of items in
            // _ids and thus are not iterated over.
            _iterationsize = _parent._added.size() + _parent._current.size();
        }
        
        public boolean hasNext() {
            if (_changestamp != _parent._changecount) {
                throw new ConcurrentModificationException(
                        "Concurrent Modification is not allowed!");
            }

            if (_cursor >= _added.size()) {
                // skip deleted ids
                while ((_cursor < _iterationsize)
                        && isSkipped(_current.get(_cursor - _added.size()))) {
                    _cursor++;
                }
            }

            if (_cursor >= _iterationsize) {
                return false;
            }
            return true;
        }
        
        public E next() {
            if (_changestamp != _parent._changecount) {
                throw new ConcurrentModificationException(
                        "Concurrent Modification is not allowed!");
            }
            // only needed if application did not call hasNext(), will skip deleted ids
            if (!hasNext()) {
                throw new NoSuchElementException(
                        "Read after the end of iterator!");
            }

            Identity id;
            E entity;
            if (_cursor < _added.size()) {
                id = _added.get(_cursor++);
                entity = _loaded.get(id);
                if (entity != null) {
                    return entity;
                }
                return lazyLoad(id);
            }
            // the deleted ids were skipped by hasNext(), get is safe
            id = _current.get(_cursor++ - _added.size());

            entity = _loaded.get(id);
            if (entity != null) {
                return entity;
            }
            return lazyLoad(id);
        }
        
        private boolean isSkipped(final Identity id) {
            if (_removed.contains(id)) { return true; }
            // make sure the object is not deleted in
            // the current transaction outside this class
            OID oid = new OID(_parent._molder, id);
            return _parent._tx.isDeletedByOID(_parent._molder.getLockEngine(), oid);
        }
        
        private E lazyLoad(final Identity ids) {
            E o;

            if (!_tx.isOpen()) {
                throw new RuntimeException("Transaction is closed!");
            }

            try {
                ProposedEntity proposedValue = new ProposedEntity(_parent._molder);
                o = (E) _parent._tx.load(ids, proposedValue, _accessMode);
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

            Identity id;
            _cursor--;
            if (_cursor < _added.size()) {
                _parent._added.remove(_cursor);
                _parent._size--;
                // Manipulating the _added array must be
                // reflected in iteration size
                _iterationsize--;
                _parent._changecount++;
                _changestamp = _parent._changecount;
            } else {
                // backward to the first not deleted ids
                id = _current.get(_cursor);
                while (_removed.contains(id)) {
                    id = _current.get(_cursor--);
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
                    _parent._removed.add(id);
                    _parent._size--;
                    _parent._changecount++;
                    _cursor++; // undo decrement of cursor above
                    _changestamp = _parent._changecount;
                }
            }
        }
    }

    public List<Identity> getIdsList() {
        List<Identity> result = new ArrayList<Identity>();
        result.addAll(_current);
        result.addAll(_added);
        result.removeAll(_removed);
        return result;
    }

    public List<Identity> getRemovedIdsList() {
        return _removed;
    }

    public List<E> getAddedEntitiesList() {
    	List<E> added = new ArrayList<E>();
    	for (Iterator<Identity> iter = _added.iterator(); iter.hasNext();) {
    		added.add(_loaded.get(iter.next()));
    	}
        return added;
    }

    public void committed(final TransactionContext tx) {
        // just reset state if we are called in our transaction
        if (tx == _tx) {
            _added.clear();
            _removed.clear();
            _changecount = 0;
            
            // ClassMolder registered us, we have to unregister ourself
            tx.removeTxSynchronizable(this);
        }
    }

    public void rolledback(final TransactionContext tx) {
        committed(tx);
    }
}
