package org.exolab.castor.core.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class CastorRuntimeException extends RuntimeException {
    /** SerialVersionUID */
    private static final long serialVersionUID = 6183971667763969115L;
    
    /** The cause for this exception. */
    private Throwable _cause;

    public CastorRuntimeException() {
        super();
    }
    
    public CastorRuntimeException(final Throwable cause) {
        super();
        _cause = cause;
    }

    public CastorRuntimeException(final String message) {
        super(message);
    }

    public CastorRuntimeException(final String message, final Throwable cause) {
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
}
