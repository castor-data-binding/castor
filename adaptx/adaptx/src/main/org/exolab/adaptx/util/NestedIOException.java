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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.adaptx.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An exception that is used to signal I/O errors which are
 * caused by other exceptions. This class allows the user
 * get to the original exception.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class NestedIOException 
    extends java.io.IOException 
{
    
    /**
     * A nested exception
    **/
    private Exception _exception  = null;
    
    /**
     * A flag to indicate a local stack trace only
    **/
    private boolean _localTrace = false;
    
    /**
     * Creates a new NestedIOException with no message,
     * or nested Exception
    **/
    public NestedIOException() {
        super();
    } //-- NestedIOException
    
    /**
     * Creates a new NestedIOException with the given message.
     * @param message the message for this Exception
    **/
    public NestedIOException(String message) {
        super(message);
    } //-- NestedIOException(String)

    
    /**
     * Creates a new NestedIOException with the given nested
     * exception.
     *
     * @param exception the nested exception. (Must not be null).
    **/
    public NestedIOException(Exception exception) {
        super(exception.getMessage());
        _exception = exception;
    } //-- NestedIOException(Exception)

    /**
     * Creates a new NestedIOException with the given message
     * and nested exception.
     *
     * @param message the detail message for this exception
     * @param exception the nested exception
    **/
    public NestedIOException(String message, Exception exception) 
    {
        super(message);
        _exception = exception;
    } //-- NestedIOException(String, Exception)

    /**
     * Returns the exception, which in turn caused this Exception to
     * be thrown, or null if nested exception exists.
     * 
     * @return the exception, which in turn caused this Exception to
     * be thrown, or null if nested exception exists.
    **/
    public Exception getException() {
        return _exception;
    } //-- getException
    
    /**
     * Sets whether or not to print the local stack trace or
     * the nested stack trace when calls to #printStackTrace are made. By
     * default the nested exception is used for printing stack trace.
     *
     * @param localTrace a boolean when true enables local stack trace only.
    **/
    public void setLocalStackTraceOnly(boolean localTrace) {
        _localTrace = localTrace;
    } //-- setLocalStackTraceOnly
    
    /**
     * Returns the String representation of this Exception.
     *
     * @return the String representation of this Exception.
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer("NestedIOException: ");
        if (getMessage() != null) {
            sb.append(getMessage());
        }
        if ((_exception != null) && (_exception.getMessage() != null)) {
            sb.append(" { nested error: ");
            sb.append(_exception.getMessage());
            sb.append('}');
        }
        return sb.toString();
    } //-- toString

    //----------------------------------------------------------------/
    //- Overwrite printStackTrace methods to handle nested exception -/
    //----------------------------------------------------------------/
    
    public void printStackTrace()
    {
        if (( _exception == null ) || _localTrace)
            super.printStackTrace();
        else
            _exception.printStackTrace();
    }

    public void printStackTrace( PrintWriter printer )
    {
        if (_localTrace || ( _exception == null ))
            super.printStackTrace( printer );
        else
            _exception.printStackTrace( printer);
    }

    public void printStackTrace( PrintStream printer )
    {
        if (_localTrace || ( _exception == null ))
            super.printStackTrace( printer );
        else
            _exception.printStackTrace( printer );
    }
    
} //-- NestedIOException
