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
package org.exolab.castor.jdo.transactionmanager;

import org.exolab.castor.core.exceptions.CastorException;

/**
 * An exception encapsulating an exception that occurs during the operation 
 * to acquire a <tt>javax.transaction.TransactionManager</tt>.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TransactionManagerAcquireException extends CastorException {
    //--------------------------------------------------------------------------

    /**
     * Creates a new TransactionManagerAcquireException with the given message.
     * 
     * @param message the message for this Exception
     */
    public TransactionManagerAcquireException(final String message) {
        super (message);
    }

    /**
     * Creates a new TransactionManagerAcquireException with the given message and cause.
     * 
     * @param message The message for this exception.
     * @param cause A Throwable instance.
     */
    public TransactionManagerAcquireException(final String message,
                                              final Throwable cause) {
        super(message, cause);
    }

    //--------------------------------------------------------------------------
}
