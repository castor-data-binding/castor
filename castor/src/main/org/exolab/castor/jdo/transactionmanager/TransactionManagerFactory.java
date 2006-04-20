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
 * $Id: TransactionManagerFactory.java,v 1.1.1.1 2003/03/03 07:08:25 kvisco Exp
 * $
 */


package org.exolab.castor.jdo.transactionmanager;


import java.util.Properties;

import javax.transaction.TransactionManager;

/**
 * A factory for properly acquiring <tt>javax.transaction.TransactionManager</tt> 
 * from J2EE containers. To provide an implementation for a specific J2EE
 * container, implement this interface.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>, <a href=" mailto:
 * werner.guttmann@gmx.net">Werner Guttmann</a>
 * @version $Id: TransactionManagerFactory.java,v 1.1.1.1 2003/03/03 07:08:25
 * kvisco Exp $
 */
public interface TransactionManagerFactory
{

    /**
     * Acquires the appropriate <tt>javax.transaction.TransactionManager</tt>.
     */
    TransactionManager getTransactionManager() 
    	throws TransactionManagerAcquireException;

    /**
     * Returns the short alias for this factory instance.
     * @return The short alias name. 
     */
    String getName();
    
    /**
     * Returns the full set of parameters associated with this factory instance. 
	 * @return The full set of parameters. Null if no parameters are available.
	 */
	Properties getParams();
    
	/**
	 * Setsthe full set of parameters on this factory instance.
	 * @param The full set of parameters. 
	 */
	void setParams(Properties params);
    

}
