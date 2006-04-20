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
package org.exolab.castor.persist;

import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import javax.transaction.xa.Xid;

/**
 * A Key is used by a KeyHolder to access locked Entities thru the LockEngine.
 * A key might be shared by different {@link LockEngine} to represent the same
 * KeyHolder.
 * <p>
 * Key holds only a WeakReference to KeyHolder, so that it doesn't not prohibit
 * KeyHolder being garbage collected.
 *
 * @see also LockEngine
 *
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 */
public final class Key {

    /**
     * Indicates the lock which the current thread is blocked on.
     */
    private ObjectLock _waitOnLock;

    /**
     * Stored the next Key which is waiting for the same lock as this
     */
    private Key _next;

    /**
     * The Key holder of this key that stored in a WeakReference
     */
    private WeakReference keyholder;

    /**
     * Key share the same hashcode of it keyholder
     */
    private int hashCode;

    /**
     * The Xid of which hold this key. It is optional.
     */
    private Xid xid;

    /**
     * Constructor for Key.
     * Keyholder may not be null
     *
     * @throws NullPointerException if keyholder is null
     */
    public Key( KeyHolder keyholder, ReferenceQueue q ) {
        if ( keyholder == null )
            throw new NullPointerException();

        this.hashCode = keyholder.hashCode();
        this.keyholder = new WeakReference( keyholder, q );
    }

    /**
     * Indicates which lock the KeyHolder of this Key is waiting for.
     * When a KeyHolder uses this key and attempts to acquire a lock,
     * it must indicate which lock it attempts to acquire in order
     * for dead-lock detection to be preformed. This method is called
     * by {@link ObjectLock} before entering the temporary lock-acquire
     * state.
     *
     * @param lock The lock which this transaction attempts to acquire
     */
    void setWaitOnLock( ObjectLock lock ) {
        _waitOnLock = lock;
    }


    /**
     * Returns the lock which this Key is acquiring.
     *
     * @return The lock which this key is acquiring.
     */
    ObjectLock getWaitOnLock() {
        return _waitOnLock;
    }

    /**
     * Returns the next Key which is waiting for the same lock as this
     *
     * @return The next Key which is waiting for the same lock as this
     */
    Key getNext() {
        return _next;
    }

    /**
     * Set the next Key which is waiting for the same lock as this
     *
     * @param key the next Key which is waiting fot the same lock as this
     */
    void setNext( Key next ) {
        _next = next;
    }
    /**
     * @specified {@link java.lang.Object}
     */
    public int hashCode() {
        return hashCode;
    }

    /*
     * Two keys are equals iff they have the common key holder that is not
     * garbage collected.
     */
    /*
    public boolean equals( Object object ) {
        if ( object == this )
            return true;

        if ( object instanceof Key ) {
            Key key = (Key) object;
            if (keyholder != null && key.keyholder != null)
                if ( keyholder.get() != null && key.keyholder.get() != null )
                    return true;
        }
        return false;
    }*/

    /**
     * A Lockable is an object which represented by a Lockage. To avoid
     * garbage collected class keep holding locks, Lockable
     */
    public interface KeyHolder {

        public abstract void setKey( Key key );

        public abstract Key getKey();

    }
}
