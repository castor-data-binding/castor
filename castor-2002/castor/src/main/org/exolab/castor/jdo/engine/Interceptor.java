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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.jdo.engine;


import org.odmg.ODMGException;


/**
 * The interceptor interface provides a notification mechanism for
 * persistent objects. It allows an external implementation to track
 * and affect object creation, deletion, loading and storing.
 * <p>
 * An interceptor is associated with a class of objects and is called
 * on every object processed of this type, whether persisted directly
 * or through the persisting of a related object.
 * <p>
 * An object is either created programmatically and then made
 * persistent or created automatically as part of a query. In the
 * latter case {@link #created} is called on the object to notify
 * that it has been created by the persistence engine. This method
 * may be called at any time prior to {@link #load}, in particular it
 * may set values to all the relevant fields of the object.
 * <p>
 * An object is either deleted from persistence storage or is cleared
 * from memory after a period of inactivity. In the first case {@link
 * #deleted} is called to inform about the deletion of the object, in
 * the second case {@link #forget} is called to inform that the object
 * will not longer be used.
 * <p>
 * An object that has not been deleted will be persisted at the end
 * of the transaction, following a successful call to {@link
 * #storing}. This method may cancel the storing and abort the
 * transaction by throwing an exception.
 * <p>
 * When an object is loaded from persistent storage {@link #loaded}
 * is called. This method may cancel the loading and abort the
 * transaction by throwing an exception. This method is called each
 * time the object is loaded, including as part of a query and as the
 * result of synchronization in exclusive access mode.
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public interface Interceptor
{


    /**
     * Called to notify that a new object has been created in memory
     * to represent a persistent object. This method is called when
     * objects are created as part of a query, not when they are
     * created programattically or made persistent.
     *
     * @param obj The object that was created
     * @throws ODMGException Reports a creation error that will
     *   cause the creation to be canceled
     */
    public void created( Object obj )
	throws ODMGException;


    /**
     * Called to notify that an object has been deleted from memory.
     * The object has already been deleted from persistent storage.
     * This substitutes a call to {@link #storing} and the object is
     * no longer accesssible to the persistent engine after this
     * method returns.
     *
     * @param obj The object that was deleted
     */
    public void deleted( Object obj );


    /**
     * Called to notify that an object has been loaded into memory.
     * When called the object is already populated with information
     * from the persistent storage and any operations required on
     * this information (e.g. unpacking of fields, conversions) must
     * be performed before this method returns. May be called more
     * than once if the object is synchronized with the database.
     * Will not be called if the object has been created
     * programatticaly and then made persistent.
     *
     * @param obj The object that was loaded
     * @throws ODMGException Reports a loading error that will
     *   cause the loading to be cancelled
     */
    public void loaded( Object obj )
	throws ODMGException;


    /**
     * Called to notify that an object is about to be stored.
     * Called prior to storing the object in persistent storage and
     * any operations required on the object (e.g. packing fields,
     * conversions) must be performed before this method returns.
     * May be called more than once when the object is used in
     * multiple transactions. Will not be called if {@link
     * #deleted} has been called on this object.
     *
     * @param obj The object that is being stored
     * @throws ODMGException Reports a storing error that will
     *   cause the storing to be cancelled
     */
    public void storing( Object obj )
	throws ODMGException;


    /**
     * Called to notify that the object is no longer used. The object
     * is not deleted in persistent storage (unlike #deleted}, but
     * only removed from memory. This method will be called for every
     * object including those for which {@link #deleted} has been called.
     *
     * @param obj The object that is being removed from memory
     */
    public void forget( Object obj );


}
