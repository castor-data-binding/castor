/*
 * Copyright 2007 Ralf Joachim
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
 *
 * $Id: Configuration.java 6907 2007-03-28 21:24:52Z rjoachim $
 */
package org.castor.core.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * CastorRuntimeException is the superclass of all unchecked Castor exceptions that are thrown
 * during the normal operation of the Java Virtual Machine.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public class CastorRuntimeException extends RuntimeException {
    /** SerialVersionUID */
    private static final long serialVersionUID = 3984585622253325513L;

    /** The cause of this exception or <code>null</code> if the cause is nonexistent or unknown. */
    private Throwable _cause = null;
    
    /** Has the cause of this exception been initialized? */
    private boolean _initCause = false;

    /**
     * Constructs a new Castor runtime exception without a message. The cause is not initialized
     * but may subsequently be initialized by a call to initCause(Throwable).
     */
    public CastorRuntimeException() {
        super();
    }
    
    /**
     * Constructs a new Castor runtime exception with the specified detail message. The cause is
     * not initialized but may subsequently be initialized by a call to initCause(Throwable).
     * 
     * @param message The detail message.
     */
    public CastorRuntimeException(final String message) {
        super(message);
    }

    /**
     * Constructs a new Castor runtime exception with the specified cause and the detail message
     * of the cause. This constructor is useful for exceptions that are wrappers for others.
     * 
     * @param cause The cause.
     */
    public CastorRuntimeException(final Throwable cause) {
        super((cause == null) ? null : cause.getMessage());
        _cause = cause;
        _initCause = true;
    }

    /**
     * Constructs a new Castor runtime exception with the specified detail message and cause.
     * 
     * @param message The detail message.
     * @param cause The cause.
     */
    public CastorRuntimeException(final String message, final Throwable cause) {
        super(message);
        _cause = cause;
        _initCause = true;
    }
    
    /**
     * The method emulates the JDK 1.4 Throwable version of initCause() for JDKs before 1.4.
     * <br/>
     * {@inheritDoc}
     */
    public final Throwable initCause(final Throwable cause) {
        if (cause == this) { throw new IllegalArgumentException(); }
        if (_initCause) { throw new IllegalStateException(); }
        _cause = cause;
        _initCause = true;
        return this;
    }

    /**
     * The method emulates the JDK 1.4 Throwable version of getCause() for JDKs before 1.4.
     * <br/>
     * {@inheritDoc}
     */
    public final Throwable getCause() {
        return _cause;
    }
    
    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
