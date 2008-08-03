/*
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
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 *
 */
package org.exolab.castor.xml.util;

import org.exolab.castor.xml.Namespaces;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * A ContentHandler implementation that wraps a DocumentHandler. This
 * ContentHandler was written for the Marshaller and expects that QNames
 * are non-null in calls to startElement and endElement methods as well
 * as inside the Attributes list.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-09-10 12:15:10 -0600 (Fri, 10 Sep 2004) $
 */
public class DocumentHandlerAdapter implements ContentHandler {

    private static final String CDATA = "CDATA";

    private DocumentHandler _handler = null;
    private Namespaces _namespaces = null;

    private boolean _createNamespaceScope = true;

    /**
     * Creates a new DocumentHandlerAdapter
     *
     * @param handler the DocumentHandler to wrap (non-null).
     */
    public DocumentHandlerAdapter(DocumentHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("The argument 'handler' must not be null.");
        }
        _handler = handler;
        _namespaces = new Namespaces();
    }

    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] chars, int start, int length)
        throws SAXException
    {
        _handler.characters(chars, start, length);
    }

    /**
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument()
        throws SAXException
    {
        _handler.endDocument();
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
        _handler.endElement(qName);
        if (_namespaces.getParent() != null) {
            _namespaces = _namespaces.getParent();
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix)
        throws SAXException
    {
        //-- do nothing here, this is handled in endElement
        //-- by simply removing the current namespace scope

    }

    /**
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] chars, int start, int length)
        throws SAXException
    {
        _handler.ignorableWhitespace(chars, start, length);
    }

    /**
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    public void processingInstruction(String target, String data)
            throws SAXException
    {
        _handler.processingInstruction(target, data);
    }

    /**
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator locator)
    {
        _handler.setDocumentLocator(locator);
    }

    /**
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String arg0)
        throws SAXException
    {
        //-- do nothing
    }

    /**
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument()
        throws SAXException
    {
        _handler.startDocument();
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName, Attributes atts)
        throws SAXException
    {
        AttributeListImpl attList = new AttributeListImpl();

        //-- Create a new namespace scope if necessary and
        //-- make sure the flag is reset to true
        if (_createNamespaceScope) {
            //-- no current namespaces, but we create a new scope
            //-- to make things easier in the endElement method
            _namespaces = _namespaces.createNamespaces();
        }
        else {
            _createNamespaceScope = true;
            _namespaces.declareAsAttributes(attList, true);
        }

        //-- copy Attributes to AttributeList
        for (int i = 0; i < atts.getLength(); i++) {
            attList.addAttribute(atts.getQName(i), CDATA, atts.getValue(i));
        }

        _handler.startElement(qName, attList);

    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    {
        if (_createNamespaceScope) {
            _namespaces = _namespaces.createNamespaces();
            _createNamespaceScope = false;
        }

        _namespaces.addNamespace(prefix, uri);
    }
}
