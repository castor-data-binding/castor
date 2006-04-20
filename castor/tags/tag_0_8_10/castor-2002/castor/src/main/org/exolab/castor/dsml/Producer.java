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
import java.util.Enumeration;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class Producer
{


    protected DocumentHandler  _docHandler;


    private boolean            _namespace;


    private boolean            _insideDirectory;


    private boolean            _insideSchema;



    public Producer( DocumentHandler docHandler, boolean namespace  )
    {
	_docHandler = docHandler;
	_namespace = namespace;
    }


    public void startDocument()
	throws SAXException
    {
	AttributeListImpl attrList;

	attrList = new AttributeListImpl();
	if ( _namespace ) 
	    attrList.addAttribute( "xmlns:" + XML.Namespace.Prefix, "CDATA", XML.Namespace.URI );
	else
	    attrList.addAttribute( "xmlns", "CDATA", XML.Namespace.URI );
	_docHandler.startElement( prefix( XML.Namespace.Root ), attrList );
	
    }


    public void endDocument()
	throws SAXException
    {
	leaveDirectory();
	leaveSchema();
	_docHandler.endElement( prefix( XML.Namespace.Root ) );
    }


    public void produce( ImportDescriptor importDesc )
	throws SAXException
    {
	leaveDirectory();
	leaveSchema();
	importDesc.produce( _docHandler );
    }


    public void produce( SearchDescriptor searchDesc )
	throws SAXException
    {
	leaveDirectory();
	leaveSchema();
	searchDesc.produce( _docHandler );
    }


    protected void enterDirectory()
	throws SAXException
    {
	// If not inside directory element, start it
	if ( ! _insideDirectory ) {
	    _insideDirectory = true;
	    _docHandler.startElement( prefix( XML.Entries.Element ), new AttributeListImpl() );
	}
    }


    protected void leaveDirectory()
	throws SAXException
    {
	// If inside directory element, end it
	if ( _insideDirectory ) {
	    _insideDirectory = false;
	    _docHandler.endElement( prefix( XML.Entries.Element ) );
	}
    }


    protected void enterSchema()
	throws SAXException
    {
	// If not inside schema element, start it
	if ( ! _insideSchema ) {
	    _insideSchema = true;
	    _docHandler.startElement( prefix( XML.Schema.Element ), new AttributeListImpl() );
	}
    }


    protected void leaveSchema()
	throws SAXException
    {
	// If not inside schema element, end it
	if ( _insideSchema ) {
	    _insideSchema = false;
	    _docHandler.endElement( prefix( XML.Schema.Element ) );
	}
    }


    protected String prefix( String tagName )
    {
	if ( _namespace )
	    return XML.Namespace.Prefix + ":" + tagName;
	else
	    return tagName;
    }


}
