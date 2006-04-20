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

package org.exolab.castor.xml;

//-- xml related imports
import org.xml.sax.*;

import java.io.Writer;
import java.io.PrintWriter;

/**
 * A Simple DocumentHandler that intercepts SAX events and
 * prints them to the console
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DebugHandler implements DocumentHandler {
    
    
    /**
     * The writer to report events to
    **/
    Writer _out = null;
    
    /**
     * The DocumentHandler to forward events to
    **/
    DocumentHandler _handler = null;
    
    boolean newLine = false;
    
    /**
     * Creates a new DebugHandler which forwards events to the
     * given document handler
     * @param handler the DocumentHandler to forward events to
    **/
    public DebugHandler(DocumentHandler handler) {
        this(handler, null);
    } //-- DebugHandler
    
    /**
     * Creates a new DebugHandler which forwards events to the
     * given document handler
     * @param handler the DocumentHandler to forward events to
     * @param out the Writer to print debug information to
    **/
    public DebugHandler(DocumentHandler handler, Writer out) {
        if (out == null) this._out = new PrintWriter(System.out);
        this._handler = handler;
    } //-- DebugHandler
    
    
    //- DocumentHandler methods -/
    
    public void characters(char[] ch, int start, int length) 
        throws org.xml.sax.SAXException
    {
        try {
            _out.write(ch, start, length);
            _out.flush();
        }
        catch(java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (_handler != null) _handler.characters(ch, start, length);
        
    } //-- characters
    
    public void endDocument()
        throws org.xml.sax.SAXException
    {
        try {
            _out.write("#endDocument\n");
            _out.flush();
        }
        catch(java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (_handler != null) _handler.endDocument();
    } //-- endDocument
    
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        try {
            _out.write("</");
            _out.write(name);
            _out.write(">\n");
            _out.flush();
        }
        catch(java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (_handler != null) _handler.endElement(name);
    } //-- endElement


    public void ignorableWhitespace(char[] ch, int start, int length) 
        throws org.xml.sax.SAXException
    {
        if (_handler != null) _handler.ignorableWhitespace(ch, start, length);
        
    } //-- ignorableWhitespace

    public void processingInstruction(String target, String data) 
        throws org.xml.sax.SAXException
    {
        try {
            _out.write("--#processingInstruction\n");
            _out.write("target: ");
            _out.write(target);
            _out.write(" data: ");
            _out.write(data);
            _out.write('\n');
            _out.flush();
        }
        catch(java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (_handler != null) _handler.processingInstruction(target, data);

    } //-- processingInstruction
    
    public void setDocumentLocator(Locator locator) {
        if (_handler != null) _handler.setDocumentLocator(locator);
    } //-- setDocumentLocator
    
    public void startDocument()
        throws org.xml.sax.SAXException
    {
        try {
            _out.write("#startDocument\n");
            _out.flush();
        }
        catch(java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (_handler != null) _handler.startDocument();
    } //-- startDocument

    
    public void startElement(String name, AttributeList atts) 
        throws org.xml.sax.SAXException
    {
        try {
            _out.write('<');
            _out.write(name);
            if ((atts != null) && (atts.getLength() > 0)) {
                for (int i = 0; i < atts.getLength(); i++) {
                    _out.write(' ');
                    _out.write(atts.getName(i));
                    _out.write("=\"");
                    _out.write(atts.getValue(i));
                    _out.write("\"");
                }
            }
            _out.write(">\n");
            _out.flush();
        }
        catch(java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (_handler != null) _handler.startElement(name, atts);
    } //-- startElement
    
} //-- Marshaller

