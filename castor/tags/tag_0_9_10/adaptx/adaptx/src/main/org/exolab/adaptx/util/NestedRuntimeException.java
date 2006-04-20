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
 * The Original Code is XSL:P XSLT processor.
 * 
 * The Initial Developer of the Original Code is Keith Visco.
 * Portions created by Keith Visco (C) 1998-2001 Keith Visco.
 * All Rights Reserved..
 *
 * Contributor(s): 
 * Keith Visco, kvisco@ziplink.net
 *    -- original author. 
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 * 
 * $Id$
 */

package org.exolab.adaptx.util;


import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * A RuntimeException class which can hold another Exception.
 * Very useful when reporting deep errors that occur in
 * methods that have no defined exception reporting.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class NestedRuntimeException
    extends RuntimeException
{
    

    /**
     * The nested exception which caused the error
    **/
    private Exception   _exception;

    /**
     * Creates a new NestedRuntimeException with the given message
     *
     * @param message the error message for this NestedRuntimeException
    **/
    public NestedRuntimeException( String message ) {
        super( message );
        _exception = null;
    } //-- NestedRuntimeException

    /**
     * Creates a new NestedRuntimeException with the given message
     * and exception.
     *
     * @param message the error message for this NestedRuntimeException
     * @param exception the Exception which caused the error.
    **/
    public NestedRuntimeException( String message, Exception exception ) {
        super( message );
        
        if (( exception instanceof NestedRuntimeException ) && 
             ( (NestedRuntimeException) exception )._exception != null )
            _exception = ( (NestedRuntimeException) exception )._exception;
        else
            _exception = exception;
            
    } //-- NestedRuntimeException

    /**
     * Creates a new NestedRuntimeException with the given exception.
     *
     * @param exception the Exception which caused the error.
    **/
    public NestedRuntimeException( Exception exception ) {
        this( exception.getMessage(), exception );
    } //-- NestedRuntimeException


    /**
     * Returns the nested exception for this NestedRuntimeException.
     *
     * @return the nested exception, or null if no nested exception 
     * exists.
    **/     
    public Exception getException() {
        return _exception;
    } //-- getException


    /**
     * Prints the stack trace for this exception
    **/
    public void printStackTrace() {
        if ( _exception == null )
            super.printStackTrace();
        else
            _exception.printStackTrace();
    } //-- printStackTrace


    /**
     * Prints the stack trace for this exception
     *
     * @param stream the PrintStream to print the stack trace to.
    **/
    public void printStackTrace( PrintStream stream ) {
        if ( _exception == null )
            super.printStackTrace( stream );
        else
            _exception.printStackTrace( stream );
    } //-- printStackTrace

    /**
     * Prints the stack trace for this exception
     *
     * @param writer the PrintWriter to print the stack trace to.
    **/
    public void printStackTrace( PrintWriter writer ) {
        if ( _exception == null )
            super.printStackTrace( writer );
        else
            _exception.printStackTrace( writer );
    } //-- printStackTrace
       
} //-- NestedRuntimeException
