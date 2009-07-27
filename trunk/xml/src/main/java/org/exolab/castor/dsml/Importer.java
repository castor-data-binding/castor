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


package org.exolab.castor.dsml;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.castor.core.util.Messages;
import org.castor.xml.BackwardCompatibilityContext;
import org.castor.xml.InternalContext;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public abstract class Importer
{
    private InternalContext _xmlContext = new BackwardCompatibilityContext();

    private ImportDescriptor _importDesc;


    private ImportEventListener  _listener;


    public void setImportDescriptor( ImportDescriptor importDesc )
    {
	_importDesc = importDesc;
    }


    public ImportDescriptor getImportDescriptor()
    {
	if ( _importDesc == null )
	    _importDesc = new ImportDescriptor();
	return _importDesc;
    }


    public void setImportEventListener( ImportEventListener listener )
	throws TooManyListenersException
    {
	if ( _listener != null )
	    throw new TooManyListenersException( Messages.message( "dsml.onlyOneListener" ) );
	_listener = listener;
    }


    public ImportEventListener getImportEventListener()
    {
	return _listener;
    }


    public void importDocument( InputStream stream )
	throws ImportExportException
    {
	importDocument( _xmlContext.getParser(), new InputSource( stream ) );
    }


    public void importDocument( Reader reader )
	throws ImportExportException
    {
	importDocument( _xmlContext.getParser(), new InputSource( reader ) );
    }


    public void importDocument( Parser parser, InputSource input )
	throws ImportExportException
    {
	Consumer       consumer;

	consumer = createConsumer();
	parser.setDocumentHandler( consumer );
	try {
	    parser.parse( input );
	} catch ( SAXException except ) {
	    throw new ImportExportException( except );
	} catch ( IOException except ) {
	    throw new ImportExportException( except );
	}
	if ( consumer.getResults() != null ) {
	    importEntries( consumer.getResults() );
	}
    }

    public void readImportDescriptor( InputStream input )
	throws IOException, SAXException
    {
	readImportDescriptor( _xmlContext.getParser(), new InputSource( input ) );
    }


    public void readImportDescriptor( Reader input )
	throws IOException, SAXException
    {
	readImportDescriptor( _xmlContext.getParser(), new InputSource( input ) );
    }


    protected void readImportDescriptor( Parser parser, InputSource input )
	throws IOException, SAXException
    {
	ImportDescriptor desc;

	desc = new ImportDescriptor();
	parser.setDocumentHandler( desc );
	parser.parse( input );
	setImportDescriptor( desc );
    }


    protected void notify( String name, int flag )
    {
	if ( _listener != null ) {
	    _listener.processedEntry( name, flag );
	}
    }


    protected abstract Consumer createConsumer();


    public abstract void importEntries( Enumeration enumeration )
	throws ImportExportException;


}

