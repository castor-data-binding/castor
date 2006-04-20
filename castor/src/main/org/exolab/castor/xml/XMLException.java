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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintStream;

/**
 * An exception that is used to signal an error that
 * has occured during marshalling or unmarshalling.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XMLException extends CastorException {
    
          
    /**
     * A nested exception
    **/
    private Exception _exception  = null;
    
    /**
     * The location for this Exception
    **/
    private Location  _location   = null;
    
    /**
     * Creates a new XMLException with no message
     * or nested Exception.
    **/
    public XMLException() {
        super();
    } //-- XMLException
    
    /**
     * Creates a new XMLException with the given message.
     *
     * @param message the message for this Exception
    **/
    public XMLException(String message) {
        super(message);
    } //-- XMLException(String)

    
    /**
     * Creates a new XMLException with the given nested
     * exception.
     *
     * @param exception the nested exception
    **/
    public XMLException(Exception exception) {
        super();
        if (exception != null) {
            super.setMessage(exception.getMessage());
            this._exception = exception;
        }
    } //-- XMLException(Exception)

    /**
     * Creates a new XMLException with the given message.
     *
     * @param message the message for this Exception
     * @param errorCode the errorCode for this Exception
    **/
    public XMLException(String message, int errorCode) {
        super(message, errorCode);
    } //-- XMLException(String)

    /**
     * Creates a new XMLException with the given message
     * and nested exception.
     *
     * @param message the detail message for this exception
     * @param exception the nested exception
    **/
    public XMLException(String message, Exception exception) {
        super(message);
        this._exception = exception;
    } //-- XMLException(String, Exception)

    /**
     * Creates a new XMLException with the given message,
     * nested exception, and errorCode.
     *
     * @param message the detail message for this exception
     * @param exception the nested exception
     * @param errorCode the errorCode for this Exception
    **/
    public XMLException
        (String message, Exception exception, int errorCode) 
    {
        super(message, errorCode);
        this._exception = exception;
    } //-- XMLException(String, Exception, int)
    
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
     * Sets the location information for this Exception
     *
     * @param location, the location information for this validation
     * exception
    **/
    public void setLocation(Location location) {
        this._location = location;
    } //-- setLocation
    
    /**
     * Returns the String representation of this Exception.
     *
     * @return the String representation of this Exception.
    **/
    public String toString() {
        String message;

        if (_exception == null )
            message = getMessage();
        else
            message = _exception.toString();
        if (_location == null)
            return message;
        else
            return message + "{" + _location.toString() + "}";
    } //-- toString



    public void printStackTrace()
    {
        if ( _exception == null )
            super.printStackTrace();
        else
            _exception.printStackTrace();
    }

    public void printStackTrace( PrintWriter printer )
    {
        if ( _exception == null )
            super.printStackTrace( printer );
        else
            _exception.printStackTrace( printer);
    }

    public void printStackTrace( PrintStream printer )
    {
        if ( _exception == null )
            super.printStackTrace( printer );
        else
            _exception.printStackTrace( printer );
    }
    
} //-- XMLException
