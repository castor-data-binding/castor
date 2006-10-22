/*
 * Copyright 2005 Werner Guttmann
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
package org.exolab.castor.tests.framework;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Exception that signals a compilation error.
 */
public class CompilationException extends RuntimeException {
    /** SerialVersionUID */
    private static final long serialVersionUID = -1459631801932567828L;

    /** The cause for this exception. */
    private Throwable _cause;

    /**
     * Creates an instance of this exception type.
     */
    public CompilationException() {
        super();
    }

    /**
     * Creates an instance of this exception type.
     * @param message The error message
     * @param t The root cause
     */
    public CompilationException(final String message, final Throwable cause) {
        super(message);
        _cause = cause;
    }

    /**
     * Creates an instance of this exception type.
     * @param message The error message
     */
    public CompilationException(final String message) {
        super(message);
    }

    /**
     * Creates an instance of this exception type.
     * @param t The root cause
     */
    public CompilationException(final Throwable cause) {
        super();
        _cause = cause;
    }

    /**
     * Match the JDK 1.4 Throwable version of getCause() on JDK<1.4 systems.
     * 
     * @return The throwable cause of this exception.
     */
    public final Throwable getCause() {
        return _cause;
    }
    
    /**
     * Print a stack trace to stderr.
     */
    public final void printStackTrace() {
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
    public final void printStackTrace(final PrintStream s) {
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
    public final void printStackTrace(final PrintWriter w) {
        // Print the stack trace for this exception.
        super.printStackTrace(w);

        if (_cause != null) {
            w.print("Caused by: ");
            _cause.printStackTrace(w);
        }
    }
}
