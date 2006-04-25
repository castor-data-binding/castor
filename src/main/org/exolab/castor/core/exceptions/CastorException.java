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

package org.exolab.castor.core.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * The base exception for Castor (or at least Castor XML)
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class CastorException extends Exception {
    /** SerialVersionUID */
    private static final long serialVersionUID = -5963804406955523505L;

    /** The cause for this exception. */
    private Throwable cause;

    /**
     * Creates a new CastorException with no message, or nested Exception
     */
    public CastorException() {
        super();
    }

    /**
     * Creates a new CastorException with the given message.
     * 
     * @param message the message for this Exception
     */
    public CastorException(String message) {
        super(message);
    }

    /**
     * Creates a new CastorException with the given message and cause.
     * @param message The message for this exception.
     * @param cause A Throwable instance.
     */
    public CastorException(String message, Throwable cause) {
    	super (message);
        this.cause = cause;
    }

    /**
     * Creates a new CastorException with the given cause.
     * @param cause A Throwable instance.
     */
    public CastorException(Throwable cause) {
        this.cause = cause;
    }

    /**
     * Match the JDK 1.4 Throwable version of initCause() on JDK<1.4 systems.
     * @param cause The throwable you wish to attach to this exception as the 'cause' of the exception.
     * @return This exception.  (Throwable also returns this, not the cause.)
     */
    public synchronized Throwable initCause(Throwable cause) {
        this.cause = cause;
        return this;
    }
    
    /**
     * Match the JDK 1.4 Throwable version of getCause() on JDK<1.4 systems.
     * @return The throwable cause of this exception.
     */
    public Throwable getCause() {
        return cause;
    }
    
    /** Retrieve the cause of a specific exception.  This is a nice, safe, easy thing to call internally
     * to ensure we never call getCause on something that shouldn't have it.  More hand-holding than anything
     * else, really, but it makes it easy to read.
     * @param e The exception you wish to extract a cause from.
     * @return The throwable attached to that exception as a cause.
     */
    private static Throwable getNestedException(Throwable e) {
        // Deal with the myriad ways of getting a nested exception.
        if (e instanceof CastorException) return ((CastorException) e).getCause();
        return null;
    }

    /**
     * Return the detailed message from this exception.  Chain message information from child exceptions into it, so that
     * the message shows the chain of message information available.
     */
    public String getMessage() {
        // Get this exception's message.
        String msg = super.getMessage();

        Throwable parent = this;
        Throwable child = getNestedException(parent);

        if (child!=null) {
            // Get the child's message.
            String msg2 = child.getMessage();

            // If we found a message for the child exception, 
            // we append it.
            if (msg2 != null) {
                if (msg != null) {
                    msg += ": " + msg2;
                } else {
                    msg = msg2;
                }
            }
        }

        // Return the completed message.
        return msg;
    }

    /**
     * Print a stack trace to stderr.
     */
    public void printStackTrace() {
        // Print the stack trace for this exception.
        super.printStackTrace();

        Throwable child = getNestedException(this);

        if (child != null) {
            System.err.print("Caused by: ");
            child.printStackTrace();
        }
    }

    /**
     * Print a stack trace to the specified PrintStream.
     * @param s The PrintStream to print a stack trace to.
     */
    public void printStackTrace(PrintStream s) {
        // Print the stack trace for this exception.
        super.printStackTrace(s);

        Throwable child = getNestedException(this);

        if (child != null) {
            s.print("Caused by: ");
            child.printStackTrace(s);
        }
    }

    /**
     * Print a stack trace to the specified PrintWriter.
     * @param w The PrintWriter to print a stack trace to.
     */
    public void printStackTrace(PrintWriter w) {
        // Print the stack trace for this exception.
        super.printStackTrace(w);

        Throwable child = getNestedException(this);
        if (child != null) {
            w.print("Caused by: ");
            child.printStackTrace(w);
        }
    }
}