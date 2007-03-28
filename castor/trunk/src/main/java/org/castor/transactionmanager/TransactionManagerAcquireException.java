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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An exception encapsulating an exception that occurs during the operation 
 * to acquire a <tt>javax.transaction.TransactionManager</tt>.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-19 15:48:30 -0700 (Mon, 19 Dec 2005) $
 * @since 1.0
 */
public final class TransactionManagerAcquireException extends Exception {
    //--------------------------------------------------------------------------

    /** SerialVersionUID. */
    private static final long serialVersionUID = -4473907453496999735L;

    /** The cause for this exception. */
    private Throwable _cause;

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
    public TransactionManagerAcquireException(
            final String message, final Throwable cause) {
        super(message);
        _cause = cause;
    }

    /**
     * Match the JDK 1.4 Throwable version of getCause() on JDK<1.4 systems.
     * 
     * @return The throwable cause of this exception.
     */
    public Throwable getCause() {
        return _cause;
    }
    
    /**
     * Print a stack trace to stderr.
     */
    public void printStackTrace() {
        // Print the stack trace for this exception.
        super.printStackTrace();

        if (_cause != null) {
            System.err.print("Caused by: ");
            _cause.printStackTrace();
        }
    }

    /**
     * Print a stack trace to the specified PrintStream.
     * 
     * @param s The PrintStream to print a stack trace to.
     */
    public void printStackTrace(final PrintStream s) {
        // Print the stack trace for this exception.
        super.printStackTrace(s);

        if (_cause != null) {
            s.print("Caused by: ");
            _cause.printStackTrace(s);
        }
    }

    /**
     * Print a stack trace to the specified PrintWriter.
     * 
     * @param w The PrintWriter to print a stack trace to.
     */
    public void printStackTrace(final PrintWriter w) {
        // Print the stack trace for this exception.
        super.printStackTrace(w);

        if (_cause != null) {
            w.print("Caused by: ");
            _cause.printStackTrace(w);
        }
    }

    //--------------------------------------------------------------------------
}
