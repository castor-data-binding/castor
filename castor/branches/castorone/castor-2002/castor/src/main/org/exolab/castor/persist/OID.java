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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist;

import java.math.BigDecimal;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Object identifier. An object identifier is unique within a cache
 * engine or other persistence mechanism and is used to locate object
 * based on their identity as well as assure no duplicate identities.
 * The object type and it's identity object define the OID's identity.
 * In addition the OID is used to hold the object's stamp and
 * db-lock access fields which are used to optimize dirty checking
 * within a transaction.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class OID {


    /**
     * The object's identity if known, null if the object was created
     * without an identity.
     */
    private final Object     _identity;


    /**
     * The object's type.
     */
    private final Class        _javaClass;

    /**
     * The LockEngine of the object
     */
    private final LockEngine  _engine;

    /**
     * The ClassMolder of the object
     */
    private final ClassMolder _molder;


    /**
     * The OID of depended object
     */
    private final OID         _depends;

    /**
     * The object's stamp, used for efficient dirty checking.
     */
    private Object       _stamp;


    /**
     * True if the object is loaded with db-lock access.
     */
    private boolean      _dbLock;


    /**
     * The OID's hash code.
     */
    private final int          _hashCode;


    /**
     * The top level class, used for equating OIDs based on commong parent.
     */
    private Class _topClass;

    /**
     * Constructor
     */
    OID( LockEngine engine, ClassMolder molder, Object identity ) {
        this( engine, molder, null, identity );
    }

    /**
     * Constructor
     */
    OID( LockEngine engine, ClassMolder molder, OID depends, Object identity ) {

        if ( engine == null )
            throw new IllegalArgumentException("engine can't be null");
        if ( molder == null )
            throw new IllegalArgumentException("molder can't be null");
        if ( identity instanceof Object[] ) {
            IllegalArgumentException e = new IllegalArgumentException("identity can't be object array!");
            throw e;
        }
        _engine = engine;
        _molder = molder;
        _identity = identity;
        _javaClass = molder.getJavaClass();
        _depends = depends;
        // OID must be unique across the engine: always use the parent
        // most class of an object, getting it from the descriptor
        while ( molder.getExtends() != null )
            molder = (ClassMolder) molder.getExtends();
        
        _topClass = molder.getJavaClass();

        // calculate hashCode
        _hashCode = _topClass.hashCode() + (_identity == null? 0 : _identity.hashCode());
    }

    /**
     * Get the depended object's oid
     *
     * @return the depended object's oid
     */
    public OID getDepends() {
        return _depends;
    }

    /**
     * Get the ClassMolder of this object
     *
     * @return the ClassMolder of this object
     */ 
    ClassMolder getMolder() {
        return _molder;
    }


    /**
     * Get the LockEngine of this object
     *
     * @return the LockEngine of this object.
     */
    LockEngine getLockEngine() {
        return _engine;
    }

    /**
     * Returns the OID's stamp. The stamp may be used to efficiently
     * implement dirty checking. The stamp is set with a call to
     * {@link #setStamp} when the object is loaded, created or stored
     * in persistent storage. Not all persistence engines support the
     * stamp mechanism.
     *
     * @return The OID's stamp, or null
     */
    Object getStamp() {
        return _stamp;
    }


    /**
     * Sets the OID's stamp. The stamp may be used to efficiently
     * implement dirty checking. Not all persistence engines support
     * the stamp mechanism.
     *
     * @param stamp The OID's stamp
     */
    void setStamp( Object stamp ) {
        _stamp = stamp;
    }


    /**
     * Specifies whether the object represented by this OID has
     * a database lock. Database locks overrides the need to perform
     * dirty checking on the object. This status is set when the
     * object is loaded with db-lock access, created or deleted.
     * It is reset when the object is unlocked.
     *
     * @param dbLock True the object represented by this OID has
     *  a database lock
     */
    void setDbLock( boolean dbLock ) {
        _dbLock = dbLock;
    }


    /**
     * Returns true if the object represented by this OID has
     * a database lock. Database locks overrides the need to perform
     * dirty checking on the object. This status is set when the
     * object is loaded with db-lock access, created or deleted.
     * It is reset when the object is unlocked.
     *
     * @return True the object represented by this OID is loaded
     *  with a datbase lock
     */
    boolean isDbLock() {
        return _dbLock;
    }


    /**
     * Return the object's identity, if known. And identity exists for
     * every object that was loaded within a transaction and for those
     * objects that were created with an identity. No two objects may
     * have the same identity in persistent storage. If the object was
     * created without an identity this method will return null until
     * the object is first stored and it's identity is set.
     *
     * @return The object's identity, or null
     */
    Object getIdentity() {
        return _identity;
    }


    /**
     * Return the object's type. When using inheritance this is the
     * type of the top most object in the inheritance heirarchy
     * specified in the object mapping.
     *
     * @return The object's type
     */
    Class getJavaClass() {
        return _javaClass;
    }


    /**
     * Returns true if the two OID's are identical. Two OID's are
     * identical only if they represent the same object type and have
     * the same identity (based on equality test). If no identity was
     * specified for either or both objects, the objects are not
     * identical.
     */
    public boolean equals( Object obj ) {
        
        OID other;
        if ( this == obj )
            return true;
        // Equality test is based on the following rules:
        //   Classes are identical
        //   Identity pass equality test
        // There is no need to do equality test on class or
        // database engine since only the same instances imply
        // the same OID.
        // Null primary identity exist only for objects created and
        // have no primary identity, therefore all such objects are
        // not identical.
        other = (OID) obj;
        return ( _topClass == other._topClass && _identity != null &&
                _identity.equals( other._identity ) );
    }


    public String toString() {
        return _javaClass.getName() + "/" + ( _identity == null ? "<new>" : _identity.toString() );
    }


    public int hashCode() {
        return _hashCode;
    }


}
