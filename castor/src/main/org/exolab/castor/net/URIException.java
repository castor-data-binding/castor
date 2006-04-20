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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.net;


import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * URIException is used when an error occurs during
 * URI resolving
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public final class URIException extends Exception {
    /** SerialVersionUID */
    private static final long serialVersionUID = 4230299234562430190L;

    /**
     * nested exception, may be null.
     **/
    private Exception _exception = null;

    /**
     * Creates a new URIException
     *
     * @param message the explaination for this exception
     **/
    public URIException( String message ) {
        super( message );
    } //-- URIException

    /**
     * Creates a new URIException
     *
     * @param message the explaination for this exception
     * @param exception, an optional nested exception, most
     * likely, the exception which caused this exception.
     **/
    public URIException( String message, Exception exception ) {
        super( message );
        if ( exception instanceof URIException )
            _exception = ((URIException) exception)._exception;
        else
            _exception = exception;
    } //-- URIException


    /**
     * Creates a new URIException
     *
     * @param exception the exception which caused this exception.
     * Note: to use this constructor exception must not be null.
     **/
    public URIException( Exception exception ) {
        this( exception.getMessage() , exception );
    } //-- URIException


    /**
     * Returns the nested exception, or null if no exception
     * exists.
     *
     * @return the nested exception, or null if no exception
     * exists
     **/
    public Exception getException() {
        return _exception;
    } //-- getException


    public void printStackTrace() {
        if ( _exception != null )
            _exception.printStackTrace();
        else
            super.printStackTrace();
    } //-- printStackTrace


    public void printStackTrace( PrintStream s ) {
        if ( _exception != null )
            _exception.printStackTrace(s);
        else
            super.printStackTrace(s);
    } //-- printStackTrace


    public void printStackTrace( PrintWriter s ) {
        if ( _exception != null )
            _exception.printStackTrace(s);
        else
            super.printStackTrace(s);
    } //-- printStackTrace
} //-- URIException


