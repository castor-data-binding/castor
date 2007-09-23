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


package org.exolab.castor.dsml.jndi;


import java.util.Enumeration;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.directory.DirContext;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.exolab.castor.dsml.Consumer;
import org.exolab.castor.dsml.Exporter;
import org.exolab.castor.dsml.ImportDescriptor;
import org.exolab.castor.dsml.SearchDescriptor;
import org.exolab.castor.dsml.ImportExportException;
import org.exolab.castor.util.Messages;


/**
 *
 *
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JNDIExporter
    extends Exporter
{


    private DirContext  _ctx;


    public JNDIExporter( DirContext ctx )
    {
	_ctx = ctx;
    }


    protected Consumer createConsumer()
    {
	return new JNDIConsumer();
    }


    public void export( DocumentHandler docHandler,
			boolean serverSchema, boolean importPolicy )
	throws ImportExportException
    {
	NamingEnumeration enum;
	String            filter;
	JNDIProducer      producer;
	SearchControls    searchCtrl;
	String[]          returnAttrs;

	if ( getSearchDescriptor() == null )
	    throw new IllegalStateException( Messages.message( "dsml.searchDescriptorRequired" ) );

	filter = getSearchDescriptor().getFilter();
	if ( filter == null )
	    filter = "()";
	try {
	    searchCtrl = new SearchControls();
	    searchCtrl.setReturningAttributes( getSearchDescriptor().getReturnAttrs() );
	    switch ( getSearchDescriptor().getScope() ) {
	    case SearchDescriptor.Scope.Base:
		searchCtrl.setSearchScope( SearchControls.OBJECT_SCOPE );
		break;
	    case SearchDescriptor.Scope.OneLevel:
		searchCtrl.setSearchScope( SearchControls.ONELEVEL_SCOPE );
		break;
	    case SearchDescriptor.Scope.SubTree:
		searchCtrl.setSearchScope( SearchControls.SUBTREE_SCOPE );
		break;
	    }
	    enum = _ctx.search( getSearchDescriptor().getBaseDN(), filter, searchCtrl );
	} catch ( NameNotFoundException except ) {
	    enum = null;
	} catch ( NamingException except ) {
	    throw new ImportExportException( except );
	}

	try {
	    producer = new JNDIProducer( docHandler, false );
	    producer.startDocument();
	    if ( enum != null )
		producer.produce( enum );
	    if ( importPolicy && getImportDescriptor() != null )
		producer.produce( getImportDescriptor() );
	    producer.endDocument();
	} catch ( SAXException except ) {
	    throw new ImportExportException( except );
	}
    }


}
