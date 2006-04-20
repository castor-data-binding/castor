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


package org.exolab.castor.jdo.engine;


import org.exolab.castor.persist.PersistenceEngine;
import org.exolab.castor.persist.ClassHandler;
import org.exolab.castor.persist.TransactionContext;
import org.exolab.castor.persist.ObjectNotPersistentExceptionImpl;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

/**
 * The utility class which adds to the standard JDO functionality
 * the support for "long" transactions. The term "long" transaction here
 * means that the loaded object is passed somewhere by copy,
 * returns back, and is stored in the same transaction.
 * For example, the transaction is opened on a server in shared mode
 * (without database locks), the object is sent to a client and then
 * returns back to the server. Another example: in EJB server the object
 * is passed from one EJB container to another or to WEB container and then 
 * returns back.
 * In these cases the returned object must be copied into the original object
 * that was fetched, so that during db.commit() Castor will write all
 * the changes to the database.
 */
public class LongTransactionSupport 
{
    
    private final PersistenceEngine _dbEngine;
    private final TransactionContext _tx;
    

    /**
     * Create LongTransactionSupport for the given DatabaseImpl
     * @param db The Database object
     */
    public LongTransactionSupport( Database db ) 
        throws PersistenceException
    {
        _dbEngine = ((DatabaseImpl) db).getPersistenceEngine();
        _tx = ((DatabaseImpl) db).getTransaction();
    }

    /**
     * @param source The object returned by copy from the outside.
     * @param target The object fetched by Castor 
     */
    public void copyObject(Object source, Object target) 
        throws PersistenceException
    {
        //TransactionContext tx;
        ClassHandler handler;

        //tx = _dbImpl.getTransaction();
        handler = _dbEngine.getClassHandler( target.getClass() );
        handler.copyObject( source, target );
    }
    
    /**
     * @param object The object returned by copy from the outside.
     * @return The updated object participating in the given transaction
     */
    public Object updateFromCopy(Object object) 
        throws PersistenceException
    {
        ClassHandler handler;
        Object result;

        handler = _dbEngine.getClassHandler( object.getClass() );
        result = handler.fillFromCopy( object, _tx, _dbEngine );
        if ( result == object ) 
            throw new ObjectNotPersistentExceptionImpl( object.getClass() );
        return result;
    }
    
}
