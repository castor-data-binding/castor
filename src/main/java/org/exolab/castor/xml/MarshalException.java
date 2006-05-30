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
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;


/**
 * An exception that is used to signal marshalling exceptions.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class MarshalException extends XMLException {
    /** SerialVersionUID */
    private static final long serialVersionUID = -1648679783713336948L;

    //------------------/
    //- error messages -/
    //------------------/
    
    public static final String BASE_CLASS_OR_VOID_ERR
        = "The marshaller cannot marshal/unmarshal types of Void.class, " +
          "Class.class or Object.class";

    public static final String NON_SERIALIZABLE_ERR
        = "The marshaller cannot unmarshal non primitive types that " +
          "do not implement java.io.Serializable";
          
    
    /**
     * Creates a new MarshalException with no message
     * or nested Exception.
    **/
    public MarshalException() {
        super();
    } //-- MarshalException
    
    /**
     * Creates a new MarshalException with the given message.
     *
     * @param message the message for this Exception
    **/
    public MarshalException(String message) {
        super(message);
    } //-- MarshalException(String)

    /**
     * Creates a new MarshalException with the given message.
     *
     * @param message the message for this Exception
     * @param errorCode the errorCode for this Exception
    **/
    public MarshalException(String message, int errorCode) {
        super(message, errorCode);
    } //-- MarshalException(String)
    
    /**
     * Creates a new MarshalException with the given nested
     * exception.
     *
     * @param exception the nested exception
    **/
    public MarshalException(Throwable exception) {
        super(exception);
    } //-- MarshalException(Exception)

    /**
     * Creates a new MarshalException with the given message
     * and nested exception.
     *
     * @param message the detail message for this exception
     * @param exception the nested exception
    **/
    public MarshalException(String message, Throwable exception) {
        super(message, exception);
    } //-- MarshalException(String, Exception)

    /**
     * Creates a new MarshalException with the given message,
     * nested exception, and errorCode.
     *
     * @param message the detail message for this exception
     * @param exception the nested exception
     * @param errorCode the errorCode for this Exception
    **/
    public MarshalException
        (String message, Throwable exception, int errorCode) 
    {
        super(message, exception, errorCode);
    } //-- MarshalException(String, Exception, int)
    
} //-- MarshalException
