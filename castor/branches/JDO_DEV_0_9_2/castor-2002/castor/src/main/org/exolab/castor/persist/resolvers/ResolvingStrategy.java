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
 * $Id$
 */
package org.exolab.castor.persist.resolvers;

import org.exolab.castor.persist.AccessMode;
import org.exolab.castor.persist.Entity;
import org.exolab.castor.persist.session.OID;
import org.exolab.castor.persist.session.TransactionContext;

/**
 * Each ResolverStrategy responsible to one data object field for
 * a data object class, in general. However, we always pass all 
 * fields from the cache. For example, the "container support"
 * in the future may make use of more than one field.
 * 
 * Accessors are get/set/add/remove/has method. To support 
 * multiple ClassLoader enviorment, we need a set of accessors 
 * for each ClassLoader. RelationResolver is responsible for 
 * giving the right accessor the each strategy.
 */
public abstract class ResolvingStrategy {
    // should strategy contains the field number?
    // or, it should be pass to the method everytime
    protected int _offset;
    protected int _length;

    public abstract void load( TransactionContext tx, OID id, 
            Object objectToBeLoaded, Entity entityFromDataStore, 
            AccessMode mode, int timeout );

    public abstract void preCreate( TransactionContext tx, OID id, 
            Object objectToBeCreated );

    public abstract void create( TransactionContext tx, OID id, 
            Object objectToBeCreated, Entity entityToBeCreated );

    public abstract void postCreate( TransactionContext tx, OID id, 
            Object objectToBeCreated, Entity entityToBeCreated );

    // long transaction
    public abstract void update( TransactionContext tx, OID id, 
            Object updateObject, Entity entityToBeUpdated, 
            int timeout );

    public abstract void preStore( TransactionContext tx, OID id, 
            Object objectToBePreStored, Entity entityFromCache, 
            int timeout );
        // walk object, call tx.markModified( objectToBeTestForModification,...)
        // to mark object dirty

    public abstract void store( TransactionContext tx, OID id, 
            Object objectToBeStored, Entity entityToBeStored );

    public abstract void revert( TransactionContext tx, OID id, 
            Object objectToBeStored, Entity entityToBeReverted );


    public abstract void markDelete( TransactionContext tx, OID id, 
            Object objectToBeDeleted, Entity entityToBeDeleted, 
            int timeout );

    public abstract void delete( TransactionContext tx, OID id, 
            Object objectToBeDeleted, Entity entityToBeDeleted, 
            int timeout );

    public abstract void writeLock( TransactionContext tx, OID id, 
            Object objectToBeLocked, Entity entityToBeLocked, 
            int timeout );

}