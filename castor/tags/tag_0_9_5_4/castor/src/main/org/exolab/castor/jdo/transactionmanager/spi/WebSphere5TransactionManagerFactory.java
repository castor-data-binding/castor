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


package org.exolab.castor.jdo.transactionmanager.spi;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.transaction.TransactionManager;

import org.exolab.castor.jdo.transactionmanager.TransactionManagerAcquireException;
import org.exolab.castor.util.Messages;

/**
 * An IBM Websphere 5 specific factory for acquiring transactions from 
 * this J2EE container.
 *
 * @author <a href="mailto:ferret@frii.com">Bruce Snyder</a>
 * @author <a href="mailto:werner.guttmann@gmx.net">Werner Guttmann</a>
 */
public class WebSphere5TransactionManagerFactory 
   extends BaseTransactionManagerFactory
{
    
    /**
     * The <tt>javax.transaction.TransactionManager</tt> that Castor will use.
     */
    private TransactionManager _transactionManager;


    /**
     * The name of the factory
     */
    private final String _name = "websphere5";


    /**
     * Acquires the appropriate TransactionManager.
     */
    public TransactionManager getTransactionManager() 
        throws TransactionManagerAcquireException
    {
        Class              webSphereTxMgr = null;
        Method             method = null;

        try 
        {
            webSphereTxMgr = Class.forName( "com.ibm.ejcs.jts.jta.JTSXA" );
            method = webSphereTxMgr.getMethod( "instance", null );
            _transactionManager = ( TransactionManager ) method.invoke( webSphereTxMgr, null );
        }
        catch( ClassNotFoundException cnfe )
        {
            throw new TransactionManagerAcquireException( Messages.format( 
                "jdo.transaction.unableToAcquireTransactionManager", cnfe.getMessage() ) );
        }
        catch( IllegalAccessException iae )
        {
            throw new TransactionManagerAcquireException( Messages.format( 
                "jdo.transaction.unableToAcquireTransactionManager", iae.getMessage() ) );
        }
        catch( InvocationTargetException ite )
        {
            throw new TransactionManagerAcquireException( Messages.format( 
                "jdo.transaction.unableToAcquireTransactionManager", ite.getMessage() ) );
        }
        catch( NoSuchMethodException nsme )
        {
            throw new TransactionManagerAcquireException( Messages.format( 
                "jdo.transaction.unableToAcquireTransactionManager", nsme.getMessage() ) );
        }

        return _transactionManager;
    }


    public String getName()
    {
        return _name;
    }
}
