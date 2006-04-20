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


package dsml;


import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPv2;
import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import org.apache.xml.serialize.*;
import org.xml.sax.DocumentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.apache.xerces.parsers.SAXParser;
import org.exolab.castor.dsml.Importer;
import org.exolab.castor.dsml.Exporter;
import org.exolab.castor.dsml.ImportExportException;
import org.exolab.castor.dsml.mozilla.MozillaImporter;
import org.exolab.castor.dsml.mozilla.MozillaExporter;
import org.exolab.castor.dsml.jndi.JNDIImporter;
import org.exolab.castor.dsml.jndi.JNDIExporter;
import org.exolab.castor.dsml.tools.PrintImportListener;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */

public class Test
{


    public static final String usage = "Usage: test.sh dsml [jndi|mozilla] <host> <root-dn> <rood-pwd>";


    public static void main( String args[] )
    {
	boolean jndi = false;

	if ( args.length < 4 ) {
	    System.out.println( usage );
	    System.exit( 1 );
	}
	if ( args[ 0 ].equalsIgnoreCase( "jndi" ) )
	    jndi = true;
	else if ( args[ 0 ].equalsIgnoreCase( "mozilla" ) )
	    jndi = false;
	else {
	    System.out.println( usage );
	    System.exit( 1 );
	}

	try {

	    LDAPConnection      conn = null;
	    Hashtable           env;
	    InitialDirContext   ctx = null;
	    Importer            importer;
	    Exporter            exporter;
	    PrintImportListener printer;

	    if ( jndi ) {
		env = new Hashtable();
		env.put( Context.INITIAL_CONTEXT_FACTORY, "com.netscape.jndi.ldap.LdapContextFactory" );
		env.put( Context.PROVIDER_URL, "ldap://" + args[ 1 ] );
		env.put( Context.SECURITY_PRINCIPAL, args[ 2 ] );
		env.put( Context.SECURITY_CREDENTIALS, args[ 3 ] );
		ctx = new InitialDirContext( env );
	    } else {
		conn = new LDAPConnection();
		conn.connect( args[ 1 ], LDAPv2.DEFAULT_PORT );
		conn.authenticate( args[ 2 ], args[ 3 ] );
	    }

	    if ( jndi ) {
		importer = new JNDIImporter( ctx );
	    } else {
		importer = new MozillaImporter( conn );
	    }
	    printer = new PrintImportListener( new PrintWriter( System.out, true ) );
	    importer.setImportEventListener( printer );
	    importer.readImportDescriptor( Test.class.getResourceAsStream( "import.xml" ) );
	    importer.importDocument( Test.class.getResourceAsStream( "test.xml" ) );

	    if ( jndi ) {
		exporter = new JNDIExporter( ctx );
	    } else {
		exporter = new MozillaExporter( conn );
	    }
	    exporter.readSearchDescriptor( Test.class.getResourceAsStream( "search.xml" ) );
	    exporter.setImportDescriptor( importer.getImportDescriptor() );
	    exporter.export( System.out, false, true );

	    if ( jndi ) {
		ctx.close();
	    } else {
		conn.disconnect();
	    }

	} catch ( Exception except ) {
	    if ( except instanceof ImportExportException ) {
		except = ( (ImportExportException) except).getException();
	    }
	    System.out.println( except );
	    except.printStackTrace();
	}
    }


}
