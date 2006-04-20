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
package org.exolab.castor.jdo;

import org.exolab.castor.mapping.AccessMode;

/**
 * A callback informs objects about changes to their state.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public interface Persistent {
    /**
     * Called to set the database to which this object belongs when
     * this object becomes persistent. The object may use the database
     * to load/create/delete related objects.
     * <p>
     * Called when the object is first created as the result of a
     * query, perior to calling {@link #jdoLoad}, or after {@link
     * Database#create} has been called on the object, prior to
     * calling {@link #jdoStore}.
     *
     * @param db The database to which this object belongs
     */
    public void jdoPersistent(Database db);

    /**
     * Called to indicate the object is now transient. The object may
     * no longer use the database object assigned to it, and will
     * become hollow with various fields set to null.
     * <p>
     * Called when the object #setFacetscompletes following any
     * call to {@link #jdoStore} or when the object is deleted from
     * the database.
     */
    public void jdoTransient();

    /**
     * Called to indicate that the object has been loaded from persistent
     * storage. This method is called immediately after synchronizing an
     * object with the database.
     *
     * @param accessMode The access mode that was specified for this object
     * either in {@link org.exolab.castor.jdo.OQLQuery#execute(AccessMode accessMode)}, or in
     * {@link Database#load(Class type, Object identity, AccessMode accessMode)}.
     * The constants are defined in {@link Database}.
     * @return the return value is discarded
     * @throws Exception An exception occured, the object cannot be loaded
     */
    public Class jdoLoad(AccessMode accessMode) throws Exception;

    /**
     * Called to indicate that an object is to be stored in persistent
     * storage. This method is called at commit time on all persistent
     * objects in this transaction. Managed fields may not necessarily be
     * persisted if the object has not been identified as modified.
     *
     * @param modified Is the object modified?
     * @throws Exception An exception occured, the object cannot be stored
     */
    public void jdoStore(boolean modified) throws Exception;

    /**
     * Called to indicate that an object is to be created in persistent
     * storage. This method is called during db.create().
     *
     * @param db The database in which this object will be created
     */
    public void jdoBeforeCreate(Database db) throws Exception;

    /**
     * Called to indicate that an object has been created in persistent
     * storage. This method is called during db.create().
     */
    public void jdoAfterCreate() throws Exception;

    /**
     * Called to indicate that an object is to be removed from persistent
     * storage. This method is called during db.remove().
     */
    public void jdoBeforeRemove() throws Exception;

    /**
     * Called to indicate that an object has been removed from persistent
     * storage. This method is called during db.remove().
     */
    public void jdoAfterRemove() throws Exception;

    /**
     * Called to indicate that an object has been included to the current
     * transaction by means of db.update() method (at the end of a "long "
     * transaction).
     *
     * @throws Exception An exception occured, the object cannot be stored
     */
    public void jdoUpdate() throws Exception;
}


