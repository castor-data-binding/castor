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



package org.exolab.castor.persist.spi;


import org.exolab.castor.jdo.Database;


/**
 * A callback interceptor informs objects about changes to their
 * state. Different callbacks can cause different methods to be
 * called on the objects.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public interface CallbackInterceptor
{


    /**
     * Called to indicate that the object has been loaded from persistent
     * storage.
     *
     * @return null or the extending Class. In the latter case Castor will
     * reload the object of the given class with the same identity.
     * @param object The object
     * @throws Exception An exception occured, the object cannot be loaded
     */
    public Class loaded( Object object, short accessMode )
        throws Exception;


    /**
     * Called to indicate that an object is to be stored in persistent
     * storage.
     *
     * @param object The object
     * @param modified Is the object modified?
     * @throws Exception An exception occured, the object cannot be stored
     */
    public void storing( Object object, boolean modified )
        throws Exception;


    /**
     * Called to indicate that an object is to be created in persistent
     * storage.
     *
     * @param object The object
     * @param db The database in which this object will be created
     */
    public void creating( Object object, Database db )
        throws Exception;


    /**
     * Called to indicate that an object has been created.
     *
     * @param object The object
     */
    public void created( Object object )
        throws Exception;


    /**
     * Called to indicate that an object is to be deleted.
     * <p>
     * This method is made at commit time on objects deleted during the
     * transaction before setting their fields to null.
     *
     * @param object The object
     */
    public void removing( Object object )
        throws Exception;


    /**
     * Called to indicate that an object has been deleted.
     * <p>
     * This method is called during db.remove().
     *
     * @param object The object
     */
    public void removed( Object object )
        throws Exception;


    /**
     * Called to indicate that an object has been made transient.
     * <p>
     * This method is made at commit or rollback time on all objects
     * that were presistent during the life time of the transaction.
     *
     * @param object The object
     * @param committed True if the object has been commited, false
     *  if rollback or otherwise cancelled
     */
    public void releasing( Object object, boolean committed );


    /**
     * Called to indicate that an object has been made persistent.
     *
     * @param object The object
     * @param db The database to which this object belongs
     */
    public void using( Object object, Database db );


    /**
     * Called to indicate that an object has been updated at the end of
     * a "long" transaction.
     *
     * @param object The object
     */
    public void updated( Object object )
        throws Exception;


}



