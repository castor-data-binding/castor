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


package org.exolab.castor.dsml;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.dsml.SearchDescriptor;
import org.exolab.castor.dsml.ImportDescriptor;


/**
 *
 *
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class Exporter
{


    private SearchDescriptor _searchDesc;


    private ImportDescriptor _importDesc;


    public void export( OutputStream output, boolean serverSchema, boolean importPolicy )
	throws ImportExportException
    {
        try {
            export( Configuration.getSerializer( output ), serverSchema, importPolicy );
        } catch ( IOException except ) {
            throw new ImportExportException( except );
        }
    }


    public void export( Writer output, boolean serverSchema, boolean importPolicy )
	throws ImportExportException
    {
        try {
            export( Configuration.getSerializer( output ), serverSchema, importPolicy );
        } catch ( IOException except ) {
            throw new ImportExportException( except );
        }
    }


    public abstract void export( DocumentHandler docHandler,
				 boolean serverSchema, boolean importPolicy )
	throws ImportExportException;


    public void setSearchDescriptor( SearchDescriptor searchDesc )
    {
	_searchDesc = searchDesc;
    }


    public SearchDescriptor getSearchDescriptor()
    {
	return _searchDesc;
    }


    public void setImportDescriptor( ImportDescriptor importDesc )
    {
	_importDesc = importDesc;
    }


    public ImportDescriptor getImportDescriptor()
    {
	return _importDesc;
    }


    public void readSearchDescriptor( InputStream input )
	throws IOException, SAXException
    {
	readSearchDescriptor( Configuration.getParser(), new InputSource( input ) );
    }


    public void readSearchDescriptor( Reader input )
	throws IOException, SAXException
    {
	readSearchDescriptor( Configuration.getParser(), new InputSource( input ) );
    }


    protected void readSearchDescriptor( Parser parser, InputSource input )
	throws IOException, SAXException
    {
	SearchDescriptor desc;

	desc = new SearchDescriptor();
	parser.setDocumentHandler( desc );
	parser.parse( input );
	setSearchDescriptor( desc );
    }


    protected abstract Consumer createConsumer();


}

