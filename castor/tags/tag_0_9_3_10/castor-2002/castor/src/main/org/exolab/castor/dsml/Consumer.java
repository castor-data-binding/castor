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


import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.AttributeList;
import org.xml.sax.Locator;
import org.exolab.castor.util.Messages;


/**
 *
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class Consumer
    implements DocumentHandler
{


    private boolean           _insideRoot;


    private DocumentHandler   _redirect;


    public Consumer()
    {
    }


    public abstract Enumeration getResults();


    protected abstract DocumentHandler getEntryConsumer();


    public void startElement( String tagName, AttributeList attr )
	throws SAXException
    {
	if ( _redirect != null ) {
	    // When redirecting all element creations pass directly to
	    // the other handler, we only look for closure.
	    _redirect.startElement( tagName, attr );
	} else if ( tagName.equals( XML.Namespace.Root ) ) {
	    // Flag when entering (and leaving) the root element.
	    if ( _insideRoot )
		throw new SAXException( Messages.format( "dsml.elementNested",
							 XML.Namespace.Root ) );
	    _insideRoot = true;
	} else {
	    if ( ! _insideRoot )
		throw new SAXException( Messages.format( "dsml.expectingOpeningTag",
							 XML.Namespace.Root, tagName ) );
	    if ( tagName.equals( XML.Schema.Element ) ||
		 tagName.equals( XML.Entries.Element ) ) {
		DocumentHandler entry;

		entry = getEntryConsumer();
		entry.startElement( tagName, attr );
		_redirect = entry;
	    } else {
		throw new SAXException( Messages.format( "dsml.openingTagNotRecognized",
							 tagName ) );
	    }
	}
    }


    public void endElement( String tagName )
	throws SAXException
    {
	if ( _redirect == null ) {
	    // This is the only case where we expect the root element
	    // to be closed.
	    if ( tagName.equals( XML.Namespace.Root ) ) {
		if ( _insideRoot = true )
		    _insideRoot = false;
		else
		    throw new SAXException( Messages.format( "dsml.closingOutsideRoot",
							     tagName ) );
	    } else {
		throw new SAXException( Messages.format( "dsml.expectingClosingTag",
							 XML.Namespace.Root, tagName ) );
	    }
	} else {
	    _redirect.endElement( tagName );
	    
	    if ( tagName.equals( XML.Schema.Element ) ||
			tagName.equals( XML.Entries.Element ) ) {
		// If we've reached this point we must be inside the
		// entries/schema redirect.
		_redirect = null;
	    }
	    // If we've reached this point we must be inside the
	    // entries/schema redirect, we ignore all closing tags.
	}
    }


    public void characters( char[] ch, int offset, int length )
	throws SAXException
    {
	if ( _redirect != null ) {
	    _redirect.characters( ch, offset, length );
	}
    }


    public void ignorableWhitespace( char[] ch, int offset, int length )
	throws SAXException
    {
	if ( _redirect != null ) {
	    _redirect.ignorableWhitespace( ch, offset, length );
	}
    }


    public void processingInstruction( String target, String instruction )
	throws SAXException
    {
	if ( _redirect != null ) {
	    _redirect.processingInstruction( target, instruction );
	}
    }


    public void startDocument()
	throws SAXException
    {
    }


    public void endDocument()
	throws SAXException
    {
	if ( _insideRoot )
	    throw new SAXException( Messages.message( "dsml.documentRootStillOpen" ) );
    }


    public void setDocumentLocator( Locator locator )
    {
    }


}

