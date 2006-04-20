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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping;

import org.exolab.castor.util.Messages;
import java.io.PrintWriter;
import java.io.PrintStream;


/**
 * An exception indicating an invalid mapping error. This
 * exception extends IllegalStateException so that it can
 * be used to replace current uses of IllegalStateException
 * within the mapping framework. This exception is used
 * when a nested exception needs to be reported.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class MappingRuntimeException extends IllegalStateException {
    /** SerialVersionUID */
    private static final long serialVersionUID = 238861866150334375L;

    /**
     * The exception which caused this Exception
    **/
    private Throwable  _exception;


    /**
     * Creates a new MappingRuntimeException
     *
     * @param message the error message
    **/
    public MappingRuntimeException( String message ) {
        super( Messages.message( message ) );
    } //-- MappingRuntimeException


    /**
     * Creates a new MappingRuntimeException
     *
     * @param message the error message
    **/
    public MappingRuntimeException( String message, Object[] args) {
        super( Messages.format( message, args ) );
    } //-- MappingRuntimeException


    /**
     * Creates a new MappingRuntimeException
     *
     * @param exception the Exception which caused this Exception.
    **/
    public MappingRuntimeException(Throwable exception) {
        super( Messages.format( "mapping.nested", exception.toString() ) );
        _exception = exception;
    } //-- MappingRuntimeException
    
    /**
     * Creates a new MappingRuntimeException
     *
     * @param exception the Exception which caused this Exception.
     * @param message the error message
    **/
    public MappingRuntimeException(Throwable exception, String message) {
        super( Messages.format( "mapping.nested", message ) );
        _exception = exception;
    } //-- MappingRuntimeException


    /**
     * Returns the Exception which caused this Exception, or null if
     * no nested exception exists.
     *
     * @return the nested Exception.
    **/
    public Throwable getException() {
        return _exception;
    } //-- getException


    public void printStackTrace() {
        if ( _exception == null )
            super.printStackTrace();
        else
            _exception.printStackTrace();
    } //-- printStackTrace


    public void printStackTrace( PrintStream print ) {
        if ( _exception == null )
            super.printStackTrace( print );
        else
            _exception.printStackTrace( print );
    } //-- printStackTrace


    public void printStackTrace( PrintWriter print ) {
        if ( _exception == null ) 
            super.printStackTrace( print );
        else
            _exception.printStackTrace( print );
    } //-- printStackTrace


} //-- MappingRuntimeException

