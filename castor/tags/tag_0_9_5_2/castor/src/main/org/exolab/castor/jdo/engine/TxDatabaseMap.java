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
 * $id: $
 */


package org.exolab.castor.jdo.engine;


import javax.transaction.Transaction;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Transactions to Databases Map. 
 *
 */
public final class TxDatabaseMap implements Serializable {

    private HashMap _txDbMap = new HashMap();

    public TxDatabaseMap() {
    }

    public synchronized boolean containsTx( Transaction tx ) {
        return _txDbMap.containsKey( tx );
    }

    public synchronized boolean containsDatabase( DatabaseImpl dbImpl ) {
        return _txDbMap.containsValue( dbImpl );
    }

    public synchronized DatabaseImpl get( Transaction tx ) {
        return (DatabaseImpl) _txDbMap.get( tx );
    }

    public synchronized void put( Transaction tx, DatabaseImpl dbImpl ) {
        Object oldDb = _txDbMap.put( tx, dbImpl );
        if ( oldDb != null && oldDb != dbImpl ) {
            _txDbMap.put( tx, oldDb );
            throw new IllegalStateException("The transaction and database association can not be changed!");
        }
        dbImpl.setTxMap( this );
    }

    public synchronized DatabaseImpl remove( Transaction tx ) {
        return (DatabaseImpl) _txDbMap.remove( tx );
    }

    public synchronized int size() {
        return _txDbMap.size();
    }

    public synchronized boolean isEmpty() {
        return _txDbMap.isEmpty();
    }
}
