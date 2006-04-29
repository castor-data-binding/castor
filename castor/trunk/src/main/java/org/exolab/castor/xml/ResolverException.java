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
 * THIS SOFTWARE IS PROVIDED BY THE CASTOR PROJECT AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * THE CASTOR PROJECT OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2005 (C) Keith Visco. All Rights Reserved.
 *
 * Created on Feb 26, 2005
 *
 * $Id$
 */
package org.exolab.castor.xml;

/**
 * The exception class thrown by the ClassDescriptorResolver
 * 
 * @author <a href="mailto:keith (at) kvisco (dot) com">Keith Visco</a>
 * @version $REVISION$ $DATE$
 */
public class ResolverException extends XMLException {
    /** SerialVersionUID */
    private static final long serialVersionUID = -8800218775708296399L;

    /**
     * 
     */
    public ResolverException() {
        super();
    }

    /**
     * @param message
     */
    public ResolverException(String message) {
        super(message);
    }

    /**
     * @param exception
     */
    public ResolverException(Throwable exception) {
        super(exception);
    }

    /**
     * @param message
     * @param errorCode
     */
    public ResolverException(String message, int errorCode) {
        super(message, errorCode);
    }

    /**
     * @param message
     * @param exception
     */
    public ResolverException(String message, Throwable exception) {
        super(message, exception);
    }

    /**
     * @param message
     * @param exception
     * @param errorCode
     */
    public ResolverException(String message, Throwable exception, int errorCode) {
        super(message, exception, errorCode);
    }

}
