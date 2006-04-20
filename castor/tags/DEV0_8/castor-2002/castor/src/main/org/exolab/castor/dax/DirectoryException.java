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


package org.exolab.castor.dax;


import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * An exception indicating an error occured talking to the directory.
 * This exception may be an application fault or an exception reported
 * by the service provider (see {@link #getSPIException}).
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DirectoryException
    extends Exception
{


    private Exception _except;


    /**
     * Constructs a new exception with the specified message.
     *
     * @param message The exception message
     */
    public DirectoryException( String message )
    {
        super( message );
    }


    /**
     * Constructs a new exception based on SPI exception.
     *
     * @param except The underlying exception
     */
    public DirectoryException( Exception except )
    {
        super( except.toString() );
        _except = except;
    }


    /**
     * Returns the SPI exception. If this exception was generated
     * due to an exception thrown by the service provider, the
     * service provider exception can be retrieved in this manner.
     * The SPI exception type depends on the service provider in
     * use.
     *
     * @return The SPI exception, or null
     */
    public Exception getSPIException()
    {
        return _except;
    }


    public void printStackTrace()
    {
        if ( _except == null )
            super.printStackTrace();
        else
            _except.printStackTrace();
    }


    public void printStackTrace( PrintStream printer )
    {
        if ( _except == null )
            super.printStackTrace( printer );
        else
            _except.printStackTrace( printer );
    }


    public void printStackTrace( PrintWriter printer )
    {
        if ( _except == null )
            super.printStackTrace( printer );
        else
            _except.printStackTrace( printer );
    }


}

