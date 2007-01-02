/*
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
 * Copyright 2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.binding;

/**
 * The base exception for the <tt>binding</tt> package. This exception is
 * nested in order to keep a correct stack trace while nesting the exception
 * that causes the call to BindingException.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Version:$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class BindingException extends Exception {

    /** We add this field because an Exception is serializable. */
    private static final long serialVersionUID = 1726983206974247107L;
    /** The embedded exception if tunnelling, or null. */
    private Exception _exception;

    /**
     * Creates a new BindingException.
     *
     * @param message The error or warning message.
     */
    public BindingException(final String message) {
        super(message);
        _exception = null;
    }

    /**
     * Creates a new BindingException wrapping an existing Exception.
     * <p>
     * The existing Exception will be embedded in the new one, and its message
     * will become the default message for the BindingException.
     *
     * @param exception The Exception to be wrapped in a BindingException.
     */
    public BindingException(final Exception exception) {
        super();
        _exception = exception;
    }

    /**
     * Creates a new BindingException from an existing exception.
     * <p>
     * The existing Exception will be embedded in the new one, but the new
     * Exception will have its own message.
     *
     * @param message The detail message.
     * @param exception The Exception to be wrapped in a BindingException.
     */
    public BindingException(final String message, final Exception exception) {
        super(message);
        _exception = exception;
    }

   /**
     * Returns a detailed message for this Exception.
     * <p>
     * If there is an embedded Exception, and if the BindingException has no
     * detail message of its own, this method will return the detail message
     * from the embedded Exception.
     *
     * @return String The error or warning message.
     */
    public String getMessage() {
        String message = super.getMessage();

        if (message == null && _exception != null) {
            return _exception.getMessage();
        }
        return message;
    }

   /**
    * Returns the embedded Exception, if any.
    *
    * @return Exception The embedded Exception, or null if there is none.
    */
    public Exception getException() {
        return _exception;
    }

    /**
     * Overrides printStackTrace to keep the stack trace of the embedded
     * Exception.
     */
    public void printStackTrace() {
        if (_exception != null) {
            System.out.println("--------------------------------");
            System.out.println("Stack Trace for :" + _exception);
            _exception.printStackTrace();
            System.out.println("--------------------------------");
        }
        super.printStackTrace();
    }

    /**
     * Overrides toString to pick up any embedded Exception.
     *
     * @return String A string representation of this Exception.
     */
    public String toString() {
        if (_exception != null) {
            return _exception.toString();
        }
        return super.toString();
    }

}
