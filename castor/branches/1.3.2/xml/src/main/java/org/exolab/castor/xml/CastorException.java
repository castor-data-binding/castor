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

package org.exolab.castor.xml;

/**
 * The base exception for Castor (or at least Castor XML)
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
**/
public class CastorException extends Exception {
    /** SerialVersionUID */
    private static final long serialVersionUID = -1648679783713336948L;

    /**
     * The error code for this Exception
    **/
    private int _errorCode  = -1;
    
    /**
     * The message for this Exception
    **/
    private String    _message    = null;
    
    //----------------/
    //- Constructors -/
    //----------------/
    
    /**
     * Creates a new CastorException with no message,
     * or nested Exception
    **/
    public CastorException() {
        super();
    } //-- CastorException
    
    /**
     * Creates a new CastorException with the given message.
     * @param message the message for this Exception
    **/
    public CastorException(String message) {
        super(message);
        this._message = message;
    } //-- CastorException(String)
    
    /**
     * Creates a new CastorException with the given nested
     * exception.
     * @param errorCode the error code for this Exception
    **/
    public CastorException(int errorCode) {
        super();
        this._errorCode = errorCode;
    } //-- CastorException(int)

    /**
     * Creates a new CastorException with the given message
     * nested exception.
     * @param message the detail message for this exception
     * @param errorCode the error code for this Exception
    **/
    public CastorException(String message, int errorCode) {
        super(message);
        this._message = message;
        this._errorCode = errorCode;
    } //-- CastorException(String, int)
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Returns the error code for this Exception, or -1 if no
     * error code exists.
     * @return the error code for this Exception, or -1 if
     * no error code exists
    **/
    public int getErrorCode() {
        return _errorCode;
    } //-- getErrorCode
    
    /**
     * Returns the detail message for this Exception
     * @return the detail message for this Exception
    **/
    public String getMessage() {
        //-- simply return message, or if null, 
        //-- to prevent null pointer exceptions while printing
        //-- error message, return ""
        if (_message == null) return "";
        return _message;
    } //-- getMessage
    
    
    /**
     * Sets the error code for this Exception
     * @param errorCode the error code 
    **/
    public void setErrorCode(int errorCode) {
        this._errorCode = errorCode;
    } //-- setErrorCode
    
    /**
     * Sets the message for this Exception
     * @param message the message for this Exception
    **/
    public void setMessage(String message) {
        this._message = message;
    } //-- setMessage
    
    
} //-- CastorException
