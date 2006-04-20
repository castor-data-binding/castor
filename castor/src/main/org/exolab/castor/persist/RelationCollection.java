/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.persist;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;

/**
 * RelationCollection implements {@link java.util.Collection}
 * It is a lazy Colllection. The collection initially contains only the
 * identities of elements of one type. If any element is needed, it will
 * be fetched "on the fly".
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public class RelationCollection implements Collection, Lazy {

    /**
     * Transaction to fetch an element on the fly if needed.
     */
    private TransactionContext _tx;

    /**
     * The LockEngine which the elements belong to.
     */
    private LockEngine _engine;

    /**
     * The ClassMolder of the elemtns.
     */
    private ClassMolder _molder;

    /**
     * AccessMode of the elements.
     */
    private AccessMode _accessMode;

    /**
     * The oid of related object of all the elements.
     */
    private OID _oid;

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
     * Constructor
     *
     */
    public RelationCollection( TransactionContext tx, OID enclosing, LockEngine engine,
            ClassMolder molder, AccessMode amode, ArrayList ids ) {
        _tx = tx;
        _oid = enclosing;
        _molder = molder;
        _engine = engine;
        _accessMode = amode;
        if ( ids == null )
            ids = new ArrayList();
        _ids = ids;
        _size = _ids.size();
        _deleted = new ArrayList();
        _added = new ArrayList();
        _loaded = new HashMap();
    }


    public boolean add(Object o) {
        Object id = _molder.getIdentity( _tx, o );
        //boolean changed = false;
        if ( _ids.contains( id ) ) {
            if ( _deleted.contains( id ) ) {
                _deleted.remove( id );
                _loaded.put( id, o );
                _changecount++;
                _size++;
                return true;
            } else {
                return (_loaded.put( id, o )!=o);
            }
        } else {
            if ( _deleted.contains( id ) )
                throw new RuntimeException("Illegal Internal State.");

            if ( _added.add( id ) ) {
                _loaded.put( id, o );
                _changecount++;
                _size++;
                return true;
            } else {
                return (_loaded.put( id, o )!=o);
            }
        }
    }

    public boolean addAll(Collection c) {
        boolean changed = false;
        Iterator a = c.iterator();
        while ( a.hasNext() ) {
            if ( add( a.next() ) ) {
                changed = true;
            }
        }
        if ( changed )
            _changecount++;
        return changed;
    }

    public void clear() {
        Object o;
        Object id;

        Iterator itor = iterator();
        while ( itor.hasNext() ) {
            itor.next();
            itor.remove();
        }
    }

    public boolean contains(Object o) {
        Object ids = _molder.getIdentity( _tx, o );
        if ( _added.contains( ids ) )
            return true;
        if ( _ids.contains( ids ) && !_deleted.contains( ids ) )
            return true;
        return false;
    }

    public boolean containsAll(Collection c) {
        Object id;
        Iterator it = c.iterator();
        while ( it.hasNext() )
            if ( !contains( it.next() ) )
                return false;
        return true;
    }

    public boolean equals(Object o) {
        return this == o;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private class IteratorImp implements Iterator {
        private int changestamp;
        private int cursor;
        private RelationCollection parent;

        private IteratorImp( RelationCollection rc ) {
            parent = rc;
            changestamp = rc._changecount;
        }
        public boolean hasNext() {
            if ( changestamp != parent._changecount )
                throw new ConcurrentModificationException("Concurrent Modification is not allowed!");
            if ( cursor >= parent._size )
                return false;
            return true;
        }
        public Object next() {
            if ( changestamp != parent._changecount )
                throw new ConcurrentModificationException("Concurrent Modification is not allowed!");
            if ( cursor >= parent._size )
                throw new NoSuchElementException("Read after the end of iterator!");

            Object id;
            Object o;
            if ( cursor < _added.size() ) {
                id = _added.get( cursor++ );
                o = _loaded.get( id );
                if ( o != null )
                    return o;
                return lazyLoad( id );
            } else {
                // skip to the first "not deleted" id
                id = _ids.get(cursor++ - _added.size());

                while ( isSkipped( id ) ) {
                    id = _ids.get(cursor++ - _added.size());
                }
                o = _loaded.get( id );
                if ( o != null )
                    return o;
                return lazyLoad( id );
            }
        }
        private boolean isSkipped( Object id ) {
            if ( _deleted.contains(id) ) {
                return true;
            }
            // make sure the object is not deleted in
            // the current transaction outside this class
            OID oid = new OID(parent._engine, parent._molder, id);
            return parent._tx.isDeletedByOID( oid );
        }
        private Object lazyLoad( Object ids ) {
            Object o;

            if ( ! _tx.isOpen() )
                throw new RuntimeException("Transaction is closed!");

            try {
                o = parent._tx.load( parent._engine, parent._molder, ids, null, null );
                parent._loaded.put( ids, o );
                return o;
            } catch ( LockNotGrantedException e ) {
                throw new RuntimeException("Lock Not Granted for lazy loaded object\n"+e);
            } catch ( PersistenceException e ) {
                throw new RuntimeException("PersistenceException for lazy loaded object\n"+e);
            }
        }

        public void remove() {
            if ( cursor <= 0 )
                throw new IllegalStateException("Method next() must be called before remove!");
            if ( changestamp != parent._changecount )
                throw new ConcurrentModificationException("Concurrent Modification is not allowed!");

            Object id;
            cursor--;
            if ( cursor < _added.size() ) {
                parent._added.remove( cursor );
                parent._size--;
                parent._changecount++;
                changestamp = parent._changecount;
            } else {
                // backward to the first not deleted ids
                id = _ids.get(cursor);
                while ( _deleted.contains(id) ) {
                    id = _ids.get(cursor--);
                }
                if ( cursor < _added.size() ) {
                    parent._added.remove( id );
                    parent._size--;
                    parent._changecount++;
                    changestamp = parent._changecount;
                } else {
                    parent._deleted.add( id );
                    parent._size--;
                    parent._changecount++;
                    changestamp = parent._changecount;
                }
            }
        }
    }

    public Iterator iterator() {
        return new IteratorImp( this );
    }

    public boolean remove(Object o) {
        Object id = _molder.getIdentity( _tx, o );
        boolean changed = false;

        if ( _deleted.contains( id ) )
            return false;

        if ( _added.contains( id ) ) {
            _added.remove( id );
            _changecount++;
            _size--;
            return true;
        } else if ( _ids.contains( id ) ) {
            _deleted.add( id );
            _changecount++;
            _size--;
            return true;
        }

        return false;
    }

    public boolean removeAll(Collection c) {
        Object o;
        boolean changed = false;
        Iterator it = c.iterator();
        while ( it.hasNext() ) {
            if ( remove( it.next() ) )
                changed = true;
        }
        if ( changed )
            _changecount++;
        return changed;
    }

    public boolean retainAll(Collection c) {
        Object o;
        boolean changed = false;
        Iterator org = iterator();
        while ( org.hasNext() ) {
            o = org.next();
            if ( ! c.contains( o ) ) {
                changed = true;
                org.remove();
            }
        }
        if ( changed )
            _changecount++;
        return changed;
    }

    public int size() {
        return _size;
    }

    public Object[] toArray() {
        Object[] result = new Object[size()];
        Iterator itor = iterator();

        while ( itor.hasNext() ) {
            result = (Object[])itor.next();
        }
        return result;
    }

    public Object[] toArray(Object[] a) {
        if ( a == null )
            throw new NullPointerException();

        Object[] result;
        int size = size();

        if ( size < a.length )
            result = a;
        else
            result = (Object[])Array.newInstance( a.getClass().getComponentType(), size );

        Iterator itor = iterator();
        int count = 0;
        while ( itor.hasNext() ) {
            result[count++] = itor.next();
        }

        // patch the extra space with null
        while ( count < result.length ) {
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

    public Object find( Object ids ) {
        return _loaded.get( ids );
    }

    public ArrayList getDeleted() {
        return (ArrayList)_deleted.clone();
    }

    public ArrayList getAdded() {
        return (ArrayList)_added.clone();
    }
}
