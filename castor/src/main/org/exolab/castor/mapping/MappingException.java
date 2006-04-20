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


package org.exolab.castor.mapping;


import java.io.PrintWriter;
import java.io.PrintStream;
import org.exolab.castor.util.Messages;


/**
 * An exception indicating an invalid mapping error.
 *
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class MappingException
    extends Exception
{


    private Exception  _except;


    public MappingException( String message )
    {
        super( Messages.message( message ) );
    }


    public MappingException( String message, Object arg1 )
    {
        super( Messages.format( message, arg1 ) );
    }


    public MappingException( String message, Object arg1, Object arg2 )
    {
        super( Messages.format( message, arg1, arg2 ) );
    }


    public MappingException( String message, Object arg1, Object arg2, Object arg3 )
    {
        super( Messages.format( message, arg1, arg2, arg3 ) );
    }


    public MappingException( Exception except )
    {
        super( Messages.format( "mapping.nested", except.toString() ) );
        _except = except;
    }


    public Exception getException()
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


    public void printStackTrace( PrintStream print )
    {
        if ( _except == null )
            super.printStackTrace( print );
        else
            _except.printStackTrace( print );
    }


    public void printStackTrace( PrintWriter print )
    {
        if ( _except == null ) 
            super.printStackTrace( print );
        else
            _except.printStackTrace( print );
    }


}

