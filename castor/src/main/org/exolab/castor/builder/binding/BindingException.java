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
 * Copyright 2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder.binding;

/**
 * The base exception for the <tt>binding</tt> package.
 * This exception is nested in order to keep a correct stack trace while nesting
 * the exception that causes the call to BindingException
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Version:$ $Date$
 */
public class BindingException extends Exception {

    /**
     * The embedded exception if tunnelling, or null.
     */
    private Exception _exception;

    /**
     * Create a new BindingException
     *
     * @param message The error or warning message.
     */
    public BindingException(String message) {
        super(message);
  	    _exception = null;
    }

    /**
     * Create a new BindingException wrapping an existing exception.
     *
     * <p>The existing exception will be embedded in the new
     * one, and its message will become the default message for
     * the BindingException.</p>
     *
     * @param exception The exception to be wrapped in a BindingException.
     */
    public BindingException(Exception exception) {
  	    super();
  	    _exception = exception;
    }


   /**
    * Create a new BindingException from an existing exception.
    *
    * <p>The existing exception will be embedded in the new
    * one, but the new exception will have its own message.</p>
    *
    * @param message The detail message.
    * @param exception The exception to be wrapped in a BindingException.
    */
    public BindingException(String message, Exception exception) {
    	super(message);
    	_exception = exception;
    }


   /**
    * Return a detail message for this exception.
    *
    * <p>If there is an embedded exception, and if the BindingException
    * has no detail message of its own, this method will return
    * the detail message from the embedded exception.</p>
    *
    * @return String The error or warning message.
    */
    public String getMessage() {
    	String message = super.getMessage();

    	if (message == null && _exception != null)
    	    return _exception.getMessage();
    	else
    	    return message;
    }


   /**
    * Return the embedded exception, if any.
    *
    * @return Exception The embedded exception, or null if there is none.
    */
    public Exception getException() {
    	return _exception;
    }

   /**
    * Override printStackTrace to keep the stack trace
    * of the embedded exception
    */
   public void printStackTrace() {
       if (_exception != null) {
          System.out.println("--------------------------------");
          System.out.println("Stack Trace for :"+_exception);
          _exception.printStackTrace();
          System.out.println("--------------------------------");
       }
       super.printStackTrace();
   }

   /**
    * Override toString to pick up any embedded exception.
    *
    * @return String A string representation of this exception.
    */
    public String toString() {
        if (_exception != null)
    	    return

			 _exception.toString();
    	else
    	    return super.toString();
    }
}