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


package org.exolab.castor.dsml.mozilla;


import java.util.Enumeration;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPSchema;
import netscape.ldap.LDAPv2;
import org.exolab.castor.dsml.Consumer;
import org.exolab.castor.dsml.Exporter;
import org.exolab.castor.dsml.SearchDescriptor;
import org.exolab.castor.dsml.ImportDescriptor;
import org.exolab.castor.dsml.ImportExportException;
import org.exolab.castor.util.Messages;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class MozillaExporter
    extends Exporter
{


    private LDAPConnection   _conn;


    public MozillaExporter( LDAPConnection conn )
    {
	_conn = conn;
    }


    protected Consumer createConsumer()
    {
	return new MozillaConsumer();
    }


    public void export( DocumentHandler docHandler,
			boolean serverSchema, boolean importPolicy )
	throws ImportExportException
    {
	Enumeration enum;
	String[]    attrs;
	MozillaProducer  producer;
	LDAPSchema  schema;
	int         count;
	int         scope;

	if ( getSearchDescriptor() == null )
	    throw new IllegalStateException( Messages.message( "dsml.searchDescriptorRequired" ) );

	attrs = getSearchDescriptor().getReturnAttrs();
	try {
	    scope = getSearchDescriptor().getScope();
	    switch ( scope ) {
	    case SearchDescriptor.Scope.OneLevel:
		scope = LDAPv2.SCOPE_ONE;
		break;
	    case SearchDescriptor.Scope.Base:
		scope = LDAPv2.SCOPE_BASE;
		break;
	    case SearchDescriptor.Scope.SubTree:
		scope = LDAPv2.SCOPE_SUB;
		break;
	    }
	    enum = _conn.search( getSearchDescriptor().getBaseDN(), scope,
				getSearchDescriptor().getFilter(), attrs, false );
	} catch ( LDAPException except ) {
	    // Object does not exist, was not removed, ignore.
	    // Anything else, we must complain.
	    if ( except.getLDAPResultCode() != LDAPException.NO_SUCH_OBJECT )
		throw new ImportExportException( except );
	    enum = null;
	}

	try {
	    producer = new MozillaProducer( docHandler, false );
	    producer.startDocument();
	    if ( serverSchema ) {
		schema = new LDAPSchema();
		schema.fetchSchema( _conn );
		producer.produce( schema );
	    }
	    if ( enum != null )
		producer.produce( enum );
	    if ( importPolicy && getImportDescriptor() != null )
		producer.produce( getImportDescriptor() );
	    producer.endDocument();
	} catch ( SAXException except ) {
	    throw new ImportExportException( except );
	} catch ( LDAPException except ) {
	    throw new ImportExportException( except );
	}
    }



}

