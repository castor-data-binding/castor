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


package org.exolab.castor.dax.engine;


import java.io.PrintWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import netscape.ldap.LDAPv2;
import netscape.ldap.LDAPv3;
import netscape.ldap.LDAPUrl;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import org.exolab.castor.dax.Directory;
import org.exolab.castor.dax.DirectorySource;
import org.exolab.castor.dax.DirectoryException;
import org.exolab.castor.dax.XADirectory;
import org.exolab.castor.dax.XADirectorySource;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.persist.OutputLogInterceptor;



/**
 * A DirectorySource object is a factory for {@link Directory}
 * objects. An object that implements this interface will probably be
 * registered with a JNDI service provider.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see Directory
 */
public class DirectorySourceImpl
    implements DirectorySource
{

    
    public static class Protocol
    {
        public static final int LDAPv2 = 2;
        public static final int LDAPv3 = 3;
    }


    private static MappingResolver  _mapResolver;


    private LDAPUrl     _url;


    private String      _rootDN;


    private int         _protocol = Protocol.LDAPv2;


    private LogInterceptor _logInterceptor;


    public void setURL( String url )
        throws MalformedURLException
    {
        _url = new LDAPUrl( url );
        _rootDN = _url.getDN();
        if ( _rootDN == null )
            throw new MalformedURLException( "LDAP URL must specify root DN" );
        if ( _rootDN.charAt( 0 ) == '/' )
            _rootDN = _rootDN.substring( 1 );
        // Normalize the URL, so that we can match it to a mapping
        _url = new LDAPUrl( _url.getHost(), _url.getPort(), _url.getDN() );
    }


    public String getURL()
    {
        return ( _url == null ? null : _url.toString() );
    }
    

    public void setProtocol( int protocol )
    {
        if ( protocol != Protocol.LDAPv2 && protocol != Protocol.LDAPv3 )
            throw new IllegalArgumentException( "LDAP protocol version " + protocol + " not supported" );
        _protocol = protocol;
    }


    public int getProtocol()
    {
        return _protocol;
    }


    public Directory getDirectory()
        throws DirectoryException
    {
        return new DirectoryImpl( getConnection( null, null ), _url, _mapResolver, _logInterceptor );
    }
    
    
    public Directory getDirectory( String userDN, String password )
        throws DirectoryException
    {
        return new DirectoryImpl( getConnection( userDN, password ), _url, _mapResolver, _logInterceptor );
    }
    
    
    /**
     * Sets the log writer for this directory source.
     * <p>
     * The log writer is a character output stream to which all
     * logging and tracing messages will be printed.
     *
     * @param logWriter The log writer, null if disabled
     */
    public void setLogWriter( PrintWriter logWriter )
    {
        if ( logWriter == null)
            _logInterceptor = null;
        else
            _logInterceptor = new OutputLogInterceptor( logWriter );
    }


    private LDAPConnection getConnection( String userDN, String password )
        throws DirectoryException
    {
        LDAPConnection conn;
        int            port;
        
        try {
            conn = new LDAPConnection();
            port = _url.getPort();
            if ( port <= 0 )
                port = ( _protocol == Protocol.LDAPv2 ? LDAPv2.DEFAULT_PORT : LDAPv3.DEFAULT_PORT );
            conn.connect( _protocol, _url.getHost(), port, userDN, password );
            return conn;
        } catch ( LDAPException except ) {
            throw new DirectoryException( except );
        }
    }


    public static synchronized void loadMapping( InputSource source )
        throws MappingException
    {
        loadMapping( source, null, null );
    }


    public static synchronized void loadMapping( InputSource source,
                                                 EntityResolver resolver,
                                                 PrintWriter logWriter )
        throws MappingException
    {
        Mapping mapping;

        try {
            mapping = new Mapping( null );
            mapping.setEntityResolver( resolver );
            mapping.setLogWriter( logWriter );
            mapping.loadMapping( source );
            _mapResolver = mapping.getResolver( Mapping.DAX );
        } catch ( IOException except ) {
            throw new MappingException( except );
        }
    }
    

}
