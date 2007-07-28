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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.dtx;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A general class for exceptions associated with Castor DTX. Can
 * haved a nested exception or just a regular message.
 *
 * @author <a href="0@intalio.com">Evan Prodromou</a> 
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */

public class DTXException extends Exception {
    /** SerialVersionUID */
    private static final long serialVersionUID = -3419863291873007258L;

    private Exception _except = null;

    private String _message = null;

    /**
     * Constructor using a nested exception.
     *
     * @param except The nested exception.
     */
    public DTXException(Exception except) {
        _except = except;
    }

    /**
     * Constructor using a simple string message.
     *
     * @param message The message.
     */

    public DTXException(String message) {
        _message = message;
    }

    /**
     * Returns this message, or the nested exception's message.
     *
     * @return String value of the message.
     */

    public String getMessage() {
    	String _msg = null;
    	if (_message != null) {
    	    _msg = _message;
    	} else if (_except != null) {
    	    _msg = "Nested exception (" + _except.getClass().getName() + "):" + _except.getMessage();
    	}
    	return _msg;
    }

    /**
     * Gets the nested exception, if there is one.
     *
     * @return Nested exception, or null if this is a message exception only.
     */

    public Exception getNestedException() {
    	return _except;
    }

    public void printStackTrace(PrintWriter out) {
    	if (_except != null) {
    	    _except.printStackTrace(out);
    	}
    	super.printStackTrace(out);
    }

    public void printStackTrace(PrintStream out) {
    	if (_except != null) {
    	    _except.printStackTrace(out);
    	}
    	super.printStackTrace(out);
    }
}
