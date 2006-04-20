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


package org.exolab.castor.util;


import java.text.*;
import java.util.*;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class Messages
{


    public static final String ResourceName = "org.exolab.castor.util.messages";
    

    private static ResourceBundle   _messages;
    
    
    private static Hashtable        _formats;
    

    public static String format( String message, Object arg1 )
    {
        return format( message, new Object[] { arg1 } );
    }

    
    public static String format( String message, Object arg1, Object arg2 )
    {
        return format( message, new Object[] { arg1, arg2 } );
    }

    
    public static String format( String message, Object arg1, Object arg2, Object arg3 )
    {
        return format( message, new Object[] { arg1, arg2, arg3 } );
    }

    
    public static String format( String message, Object[] args )
    {
        MessageFormat   mf;
        String          msg;
        
        try
        {
            mf = (MessageFormat) _formats.get( message );
            if ( mf == null )
            {
                try
                {
                    msg = _messages.getString( message );
                }
                catch ( MissingResourceException except )
                {
                    return message;
                }
                mf = new MessageFormat( msg );
                _formats.put( message, mf );
            }
            return mf.format( args );
        }
        catch ( Exception except )
        {
            return "An internal error occured while processing message " + message;
        }
    }
    
    
    public static String message( String message )
    {
        try
        {
            return _messages.getString( message );
        }
        catch ( MissingResourceException except )
        {
            return message;
        }
    }

    
    public static void setLocale( Locale locale )
    {
	_formats = new Hashtable();
	try {
	    if ( locale == null )
		_messages = ResourceBundle.getBundle( ResourceName ); 
	    else
		_messages = ResourceBundle.getBundle( ResourceName, locale ); 
	} catch ( Exception except ) {
	    _messages = new EmptyResourceBundle();
	    Logger.getSystemLogger().println( "Failed to locate messages resource " +
					      ResourceName );
	}
    }
    
    
    static
    {
        setLocale( Locale.getDefault() );
    }


    static class EmptyResourceBundle
	extends ResourceBundle
	implements Enumeration
    {

	public Enumeration getKeys()
	{
	    return this;
	}

	protected Object handleGetObject( String name )
	{
	    return "[Missing message " + name + "]";
	}

	public boolean hasMoreElements()
	{
	    return false;
	}

	public Object nextElement()
	{
	    return null;
	}

    }
    
    
}
