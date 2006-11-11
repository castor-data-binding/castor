/*
 * Copyright 2005 Bruce Snyder, Werner Guttmann, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.transactionmanager;

import java.util.Properties;
import javax.transaction.TransactionManager;

/**
 * A factory for properly acquiring <tt>javax.transaction.TransactionManager</tt> 
 * from J2EE containers. To provide an implementation for a specific J2EE container,
 * implement this interface.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href=" mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-19 15:48:30 -0700 (Mon, 19 Dec 2005) $
 * @since 1.0
 */
public interface TransactionManagerFactory {
    //--------------------------------------------------------------------------

    /**
     * Returns the short alias for this factory instance.
     * 
     * @return The short alias name. 
     */
    String getName();

    /**
     * Acquires the appropriate <tt>javax.transaction.TransactionManager</tt> with the
     * given properties.
     * 
     * @param properties The properties passed to the transaction manager.
     * @return The transaction manager.
     * @throws TransactionManagerAcquireException If any failure occured when loading
     *         the transaction manager.
     */
    TransactionManager getTransactionManager(final Properties properties) 
    throws TransactionManagerAcquireException;

    //--------------------------------------------------------------------------
}
