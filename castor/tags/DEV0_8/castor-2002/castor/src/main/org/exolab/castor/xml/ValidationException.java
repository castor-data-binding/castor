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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

/**
 * An exception that can be used to signal XML validation errors
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ValidationException extends CastorException {
    
    /**
     * A nested exception
    **/
    private Exception _exception  = null;
    
    /**
     * The location for this Exception
    **/
    private Location  _location   = null;
    
    /**
     * Creates a new ValidationException with no message,
     * or nested Exception
    **/
    public ValidationException() {
        super();
    } //-- ValidationException
    
    /**
     * Creates a new ValidationException with the given message.
     * @param message the message for this Exception
    **/
    public ValidationException(String message) {
        super(message);
    } //-- ValidationException(String)

    /**
     * Creates a new ValidationException with the given message.
     * @param message the message for this Exception
     * @param errorCode the errorCode for this Exception
    **/
    public ValidationException(String message, int errorCode) {
        super(message, errorCode);
    } //-- ValidationException(String)
    
    /**
     * Creates a new ValidationException with the given nested
     * exception.
     * @param exception the nested exception
    **/
    public ValidationException(Exception exception) {
        super();
        if (exception != null) {
            super.setMessage(exception.getMessage());
            this._exception = exception;
        }
    } //-- ValidationException(Exception)

    /**
     * Creates a new ValidationException with the given message
     * and nested exception.
     * @param message the detail message for this exception
     * @param exception the nested exception
    **/
    public ValidationException(String message, Exception exception) {
        super(message);
        this._exception = exception;
    } //-- ValidationException(String, Exception)

    /**
     * Creates a new ValidationException with the given message,
     * nested exception, and errorCode.
     * @param message the detail message for this exception
     * @param exception the nested exception
     * @param errorCode the errorCode for this Exception
    **/
    public ValidationException
        (String message, Exception exception, int errorCode) 
    {
        super(message, errorCode);
        this._exception = exception;
    } //-- ValidationException(String, Exception, int)
    
    /**
     * Returns the exception, which in turn caused this Exception to
     * be thrown, or null if nested exception exists.
     * @return the exception, which in turn caused this Exception to
     * be thrown, or null if nested exception exists.
    **/
    public Exception getException() {
        return _exception;
    } //-- getException
    
    /**
     * Sets the location information for this ValidationException
     * @param location, the location information for this validation
     * exception
    **/
    public void setLocation(Location location) {
        this._location = location;
    } //-- setLocation
    
    /**
     * Returns the String representation of this Exception
     * @return the String representation of this Exception
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer("ValidationException: ");
        String message = getMessage();
        if (message != null) sb.append(message);
        if (_location != null) {
            sb.append(";\n   ");
            sb.append(_location.toString());
        }
        return sb.toString();
    } //-- toString
    
} //-- ValidationException
