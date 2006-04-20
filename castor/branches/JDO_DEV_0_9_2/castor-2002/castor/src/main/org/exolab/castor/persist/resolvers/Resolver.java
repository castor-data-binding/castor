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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id $
 */
package org.exolab.castor.persist.resolvers;


import org.exolab.castor.persist.session.OID;
import org.exolab.castor.persist.AccessMode;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.LogInterceptor;
import org.exolab.castor.persist.session.TransactionContext;
import org.exolab.castor.persist.session.OID;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.LockNotGrantedException;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class Resolver {

    public static Map createResolvers( MappingRoot mapping, LockEngine lock, LogInterceptor log ) {
        // it is a big task

        // initialize everything: RelatonResolver, SQLEngine etc. 
        // should RelationResolver the one who trigger the initization
        // of SQLEngine or DAXEngine? 
        //
        // also, we have more 
        // is a big and complicate task. what appoarch should we take?

        return null;
    }

    public abstract Object load( TransactionContext tx, OID id, AccessMode mode, int timeout )
            throws ObjectNotFoundException, PersistenceException;

        // check oid

        // load the array of fields from LockEngine

        // set time stamp

        // if it is an extend class, all table would have
        // be joined already here. we will try to determined
        // the right class here.
        // It is where we add determinance support later too.
        //     
        //     after we determined, we call tx.addObject( id, object)
        //     before we iterate htur the avaliable strategies

        // get the right set of accessors, depend on the actual
        // initstated object

        // iterate thur all avaliable strategies

    public abstract void preStore( TransactionContext tx, OID id, 
            Object objectToBeTestForModification, int timeout )
            throws LockNotGrantedException, PersistenceException;

        // check oid

        // load the locked fields from LockEngine

        // get accessors

        // iterate thur all avaliable strategies

    public abstract void store( TransactionContext tx, OID id, Object objectToBeStored )
            throws DuplicateIdentityException, ObjectModifiedException, 
            ObjectDeletedException, LockNotGrantedException, PersistenceException;

    public abstract void preCreate( TransactionContext tx, OID id, Object objectToBeCreated )
            throws DuplicateIdentityException, PersistenceException;

    public abstract void create( TransactionContext tx, OID id, Object objectToBeCreated )
            throws DuplicateIdentityException, PersistenceException;

    public abstract void postCreate( TransactionContext tx, OID id, Object objectToBeCreated )
            throws PersistenceException;

    public abstract void update( TransactionContext tx, OID id, 
            Object objectLoadedOutsideOfThisTransactionToBeUpdated, int timeout )
            throws DuplicateIdentityException, LockNotGrantedException, 
            PersistenceException;

    public abstract void markDelete( TransactionContext tx, OID id, Object objectToBeDeleted,
            int timeout ) 
            throws ObjectNotFoundException, LockNotGrantedException, 
            PersistenceException;

    public abstract void delete( TransactionContext tx, OID id, Object objectToBeDeleted )
            throws PersistenceException;

    public abstract void writeLock( TransactionContext tx, OID id, Object objectToBeLocked,
            int timeout )
            throws PersistenceException;

    public abstract void softLock( TransactionContext tx, OID id, Object objectToBeLocked,
            int timeout )
            throws LockNotGrantedException;

    public abstract void releaseLock( TransactionContext tx, OID id, Object objectToBeUnLocked );

    public abstract void revertObject( TransactionContext tx, OID id, Object objectToBeReverted );

    // note that updateCache, releaseLock, forgetObject will not
    // needed anymore. LockEngine will keep trace of loaded object
    // and updateCache, releaseLock and forgetObject when needed









    /**
     * The fully qualified name of the java data object class which this Resolver
     * corresponds to. We call it base class.
     */
    private String _name;

    /**
     * <tt>Resolver</tt> of the java data object class's Resolver which
     * the base class extends.
     */
    private Resolver _extends;

    /**
     * <tt>Resolver</tt> of the java data object class's Resolver which
     * the base class depends on. <tt>null</tt> if it class is an indenpendent
     * class.
     */
    private Resolver _depends;

    /**
     * A Vector of <tt>Resolver</tt>s for all the direct dependent class of the
     * base class.
     */
    private Vector _dependent;

    /**
     * A Vector of <tt>Resolver</tt>s for all the direct extending class of the
     * base class.
     */
    private Vector _extendent;

    /**
     * Default accessMode of the base class.
     */
    private AccessMode _accessMode;

    /**
     * The LockEngine which this instace of Resolver belong to.
     */
    private LockEngine _engine;

    /**
     * The CallbackInterceptor for the base class.
     */
    private CallbackInterceptor _callback;

    /**
     * The LRU mechanism to be used for caching freed instance of the base class.
     */
    private int _cachetype;

    /**
     * The LRU parameter(or capcity) to be used for caching freed instance of the
     * base class.
     */
    private int _cacheparam;

    /**
     * Is a key kenerator used for the base class?
     */
    private boolean _isKeyGenUsed;

    /**
     * True if org.exolab.castor.debug="true"
     */
    private boolean _debug;


    /**
     * Remove the reference of a related object from an object of.
     * the base class. <p>
     *
     * If the related object is PersistanceCapable, the field will
     * be set null. If the related object is a Collection, then
     * the related object will be removed from the Collection. <p>
     *
     * If any changed occured, transactionContext.markModified
     * will be called, to indicate the object is modified. <p>
     *
     * It method will iterate thur all of the object's field and
     * try to remove all the occurence.
     *
     * @param tx      the TransactionContext of the transaction in action
     * @param object  the target object of the base type of this Resolver
     * @param relatedMolder  the Resolver of the related object to be
     *                       removed from the object
     * @param related        the object to be removed
     */
    private boolean removeRelation( TransactionContext tx, Object object,
            Resolver relatedMolder, Object relatedObject )  {

        /*
        boolean removed = false;
        boolean updateCache = false;
        boolean updatePersist = false;
        Resolver relatedBaseMolder = null;
        for ( int i=0; i < _fhs.length; i++ ) {
            int fieldType = _fhs[i].getFieldType();
            Resolver fieldResolver;
            switch (fieldType) {
            case FieldMolder.PERSISTANCECAPABLE:
                // de-reference the object
                fieldResolver = _fhs[i].getFieldResolver();

                relatedBaseMolder = relatedMolder;
                while ( fieldResolver != relatedBaseMolder && relatedBaseMolder != null ) {
                    relatedBaseMolder = relatedBaseMolder._extends;
                }
                if ( fieldResolver == relatedBaseMolder ) {
                    Object related = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( related == relatedObject ) {
                        _fhs[i].setValue( object, null, tx.getClassLoader() );
                        updateCache = true;
                        updatePersist = true;
                        removed = true;
                    }
                    // |
                }
                break;
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                // remove the object from the collection
                fieldResolver = _fhs[i].getFieldResolver();
                relatedBaseMolder = relatedMolder;
                while ( fieldResolver != relatedBaseMolder && relatedBaseMolder != null ) {
                    relatedBaseMolder = relatedBaseMolder._extends;
                }
                if ( fieldResolver == relatedBaseMolder ) {
                    // same action to be taken for lazy and non lazy collection
                    boolean changed = false;
                    Object related = _fhs[i].getValue( object, tx.getClassLoader() );
                    Iterator itor = getIterator( related );
                    while ( itor.hasNext() ) {
                        Object o = itor.next();
                        if ( o == relatedObject ) {
                            changed = true;
                            itor.remove();
                        }
                    }
                    if ( changed ) {
                        updateCache = true;
                        updatePersist = false;
                        removed = true;
                    }
                }
                break;
            }
        }
        tx.markModified( object, updatePersist, updateCache );
        return removed;
        */ return true;
    }

    /**
     * Return a new instance of the base class with the provided ClassLoader object
     *
     * @param laoder the ClassLoader object to use to create a new object
     * @return Object the object reprenseted by this Resolver, and instanciated
     * with the provided ClassLoader instance.
     */
    public Object newInstance( ClassLoader loader ) {
        try {
            if (loader != null )
                return loader.loadClass(_name).newInstance();
            else
                return Class.forName(_name).newInstance();
        } catch (ClassNotFoundException e) {
        } catch ( IllegalAccessException e ) {
        } catch ( InstantiationException e ) {
        } catch ( ExceptionInInitializerError e ) {
        } catch ( SecurityException e ) {
        }
        return null;
    }

    /**
     * Get the effective accessMode of the the base type
     *
     * @param txMode - the default transaction accessMode
     * @return the effective acessMode of the base type
     */
    public AccessMode getAccessMode( AccessMode txMode ) {

        if ( txMode == null )
            return _accessMode;

        if ( _accessMode == AccessMode.ReadOnly || txMode == AccessMode.ReadOnly )
            return AccessMode.ReadOnly;
        if ( _accessMode == AccessMode.DbLocked || txMode == AccessMode.DbLocked )
            return AccessMode.DbLocked;
        if ( _accessMode == AccessMode.Exclusive || txMode == AccessMode.Exclusive )
            return AccessMode.Exclusive;
        return txMode;

    }

    /**
     * Get the callback interceptor of the base type
     *
     */
    public CallbackInterceptor getCallback() {
        return _callback;
    }

    /**
     * Get the identity from a object of the base type
     * If object isn't persistent and key generator is used, returns null
     *
     * @param tx the transaction context
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getIdentity( TransactionContext tx, Object o ) {
        // [oleg] In the case where key generator is used,
        // the value of identity is dummy, set it to null
        if ( isKeyGeneratorUsed() && ! (tx.isPersistent(o) || tx.isReadOnly(o))) {
            return null;
        } else {
            return getActualIdentity( tx, o );
        }
    }

    /**
     * Get the identity from a object of the base type
     *
     * @param tx the transaction context
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getActualIdentity( TransactionContext tx, Object o ) {
        return getActualIdentity( tx.getClassLoader(), o );
    }

    /**
     * Get the identity from a object of the base type
     *
     * @param loader the current class loader
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getActualIdentity( ClassLoader loader, Object o ) {
        /*
        Object temp;

        if ( _ids.length == 1 ) {
            return _ids[0].getValue( o, loader );
        } else if ( _ids.length == 2 ) {
            temp = _ids[0].getValue( o, loader );
            return temp==null? null: new Complex( temp, _ids[1].getValue( o, loader ) );
        } else {
        Object[] osIds = new Object[_ids.length];
        for ( int i=0; i<osIds.length; i++ ) {
            osIds[i] = _ids[i].getValue( o, loader );
        }
            if ( osIds[0] == null )
                return null;
            else
                return new Complex( osIds );
        }*/
        return null;
    }

    /**
     * Set the identity into an object
     *
     * @param tx the transaction context
     * @param object the object to set the identity
     * @param identity the new identity for the object
     */
    public void setIdentity( TransactionContext tx, Object object, Object identity )
            throws PersistenceException {

        /*
        if ( _ids.length > 1 ) {
            if ( identity instanceof Complex ) {
                Complex com = (Complex) identity;
                if ( com.size() != _ids.length )
                    throw new PersistenceException( "Complex size mismatched!" );

                for ( int i=0; i<_ids.length; i++ ) {
                    _ids[i].setValue( object, com.get(i), tx.getClassLoader() );
                }
            }
        } else {
            if ( identity instanceof Complex )
                throw new PersistenceException( "Complex type not accepted!" );
            _ids[0].setValue( object, identity, tx.getClassLoader() );
        }*/
    }

    /**
     * Get the base class of this Resolver given a ClassLoader
     * @param loader the classloader
     * @return the <code>Class</code> instance
     */
    public Class getJavaClass( ClassLoader loader ) {
        Class result = null;
        try {
            result = ( loader != null) ? loader.loadClass(_name) : Class.forName(_name);
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return result;
    }

    // ssa, FIXME : is that method necessary ?
    /**
     * check if the current ClassModlder is assignable from the <code>class</code>
     * instance.
     *
     * @param cls the Class to check the assignation
     * @return true if assignable
     */
    public boolean isAssignableFrom ( Class cls ) {
        ClassLoader loader = cls.getClassLoader();
        Class molderClass = null;
        try {
            if ( loader != null )
                molderClass = loader.loadClass( _name );
            else
                molderClass = Class.forName( _name );
        } catch ( ClassNotFoundException e ) {
            return false;
        }
        return molderClass.isAssignableFrom( cls );

    }

    /**
     * Get the fully qualified name of the base type of this Resolver
     *
     */
    public String getName() {
        return _name;
    }

    /**
     * Get the extends class' Resolver
     *
     */
    public Resolver getExtends() {
        return _extends;
    }

    /**
     * Get the depends class' Resolver
     */
    public Resolver getDepends() {
        return _depends;
    }

    /**
     * Get the LockEngine which this Resolver belongs to.
     *
     */
    public LockEngine getLockEngine() {
        return _engine;
    }
    /**
     * Return the preferred LRU cache mechanism for caching object of this type
     *
     */
    public int getCacheType() {
        return _cachetype;
    }
    /**
     * Return the preferred LRU cache capacity for caching object of this type
     *
     */
    public int getCacheParam() {
        return _cacheparam;
    }

    /**
     * Return true if the base type of this Resolver is an dependent
     * class.
     */
    public boolean isDependent() {
        return _depends != null;
    }

    /**
     * Set all persistence fields of object of the base type to null.
     *
     * @param object - target object
     */
    public void setFieldsNull( Object object ) {
        /*
        for ( int i=0; i < _ids.length; i++ ) {
            _ids[i].setValue( object, null );
        }
        for ( int i=0; i < _fhs.length; i++ ) {
            _fhs[i].setValue( object, null );
        }*/
    }

    /**
     * Mutator method to add a extent Resolver
     *
     */
    void addExtendent( Resolver ext ) {
        if ( _extendent == null )
            _extendent = new Vector();
        _extendent.add( ext );
    }

    /**
     * Mutator method to add a dependent Resolver
     *
     */
    void addDependent( Resolver dep ) {
        if ( _dependent == null )
            _dependent = new Vector();
        _dependent.add( dep );
    }
    /**
     * Mutator method to set the extends Resolver
     *
     */
    void setExtends( Resolver ext ) {
        _extends = ext;
        ext.addExtendent( this );
    }

    /**
     * Mutator method to set the depends Resolver
     *
     */
    void setDepends( Resolver dep ) {
        _depends = dep;
        dep.addDependent( this );
    }

    /**
     * Return the iterator on values of the specified Collection
     * Or, return the iterator on values of the specified Map
     *
     * @param o - a Collection
     */
    private Iterator getIterator( Object o ) {
        if ( o == null ) {
            return new Iterator() {
                public boolean hasNext() {
                    return false;
                }
                public Object next() {
                    throw new NoSuchElementException();
                }
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } else if ( o instanceof Collection ) {
            return ((Collection) o).iterator();
        } else if ( o instanceof Map ) {
            return ((Map) o).values().iterator();
        } else {
            throw new IllegalArgumentException();
        }
    }



    /**
     * Return all the object identity of a Collection of object of the same
     * type.
     *
     * @param tx the transaction context
     * @param molder - class molder of the type of the objects
     * @param col - a Collection or Vector containing
     * @return an <tt>ArrayList</tt>s which contains list of object identity
     */
    private ArrayList extractIdentityList( TransactionContext tx, Resolver molder, Object col ) {
        if ( col == null ) {
            return new ArrayList();
        } else if ( col instanceof Collection ) {
            ArrayList idList = new ArrayList();
            Iterator itor = ((Collection)col).iterator();
            while ( itor.hasNext() ) {
                Object id = molder.getIdentity( tx, itor.next() );
                if ( id != null )
                    idList.add( id );
            }
            return idList;
        } else if ( col instanceof Map ) {
            ArrayList idList = new ArrayList();
            Iterator itor = ((Map)col).keySet().iterator();
            while ( itor.hasNext() ) {
                idList.add( itor.next() );
            }
            return idList;
        } else {
            throw new IllegalArgumentException("A Collection or Map is expected!");
        }
    }

    public String toString() {
        return "Resolver "+ _name;
    }

    /**
     * Return true if a key generator is used for the base type of this Resolver
     */
    public boolean isKeyGeneratorUsed() {
        return _isKeyGenUsed || (_extends != null && _extends. isKeyGeneratorUsed());
    }

}



