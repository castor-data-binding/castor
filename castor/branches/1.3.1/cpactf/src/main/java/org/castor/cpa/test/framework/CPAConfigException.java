package org.castor.cpa.test.framework;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class CPAConfigException extends RuntimeException {
    //--------------------------------------------------------------------------

    /** Serial version UID. */
    private static final long serialVersionUID = -563472918295178998L;

    //--------------------------------------------------------------------------

    /** The cause of this exception or <code>null</code> if the cause is nonexistent or unknown. */
    private Throwable _cause = null;
    
    /** Has the cause of this exception been initialized? */
    private boolean _initCause = false;

    //--------------------------------------------------------------------------

    /**
     * Constructs a new CPATestException without a message. The cause is not initialized
     * but may subsequently be initialized by a call to initCause(Throwable).
     */
    public CPAConfigException() {
        super();
    }
    
    /**
     * Constructs a new CPATestException with the specified detail message. The cause is
     * not initialized but may subsequently be initialized by a call to initCause(Throwable).
     * 
     * @param message The detail message.
     */
    public CPAConfigException(final String message) {
        super(message);
    }

    /**
     * Constructs a new CPATestException with the specified cause and the detail message
     * of the cause. This constructor is useful for exceptions that are wrappers for others.
     * 
     * @param cause The cause.
     */
    public CPAConfigException(final Throwable cause) {
        super((cause == null) ? null : cause.getMessage());
        _cause = cause;
        _initCause = true;
    }

    /**
     * Constructs a new CPATestException with the specified detail message and cause.
     * 
     * @param message The detail message.
     * @param cause The cause.
     */
    public CPAConfigException(final String message, final Throwable cause) {
        super(message);
        _cause = cause;
        _initCause = true;
    }
    
    //--------------------------------------------------------------------------

    /**
     * The method emulates the JDK 1.4 Throwable version of initCause() for JDKs before 1.4.
     * <br/>
     * {@inheritDoc}
     */
    public Throwable initCause(final Throwable cause) {
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
    public Throwable getCause() {
        return _cause;
    }
    
    //--------------------------------------------------------------------------

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
    public void printStackTrace(final PrintStream s) {
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
