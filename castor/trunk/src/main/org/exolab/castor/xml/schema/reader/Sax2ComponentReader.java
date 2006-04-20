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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.util.AttributeSetImpl;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A SAX adapter class for the ComponentReader.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class Sax2ComponentReader
    implements DocumentHandler, org.xml.sax.ErrorHandler
{
      //-------------------/
     //- Class Variables -/
    //-------------------/

    private static final String XMLNS        = "xmlns";
    private static final String XMLNS_PREFIX = "xmlns:";
    private static final String XML_PREFIX   = "xml";

      //----------------------/
     //- Instance Variables -/
    //----------------------/

    private ComponentReader _compReader = null;

    private Namespaces _namespaces = null;

      //----------------/
     //- Constructors -/
    //----------------/

    public Sax2ComponentReader(ComponentReader compReader) {
        super();
        _compReader = compReader;
        _namespaces = new Namespaces();
    } //-- Sax2ComponentReader


    /**
     * Processes the attributes and namespace declarations found
     * in the given SAX AttributeList. The global AttributeSet
     * is cleared and updated with the attributes. Namespace
     * declarations are added to the set of namespaces in scope.
     *
     * @param atts the AttributeList to process.
    **/
    private AttributeSet processAttributeList(AttributeList atts)
        throws SAXException
    {

        if (atts == null) return new AttributeSetImpl(0);


        //-- process all namespaces first
        int attCount = 0;
        boolean[] validAtts = new boolean[atts.getLength()];
        for (int i = 0; i < validAtts.length; i++) {
            String attName = atts.getName(i);
            if (attName.equals(XMLNS)) {
                _namespaces.addNamespace("", atts.getValue(i));
            }
            else if (attName.startsWith(XMLNS_PREFIX)) {
                String prefix = attName.substring(XMLNS_PREFIX.length());
                _namespaces.addNamespace(prefix, atts.getValue(i));
            }
            else {
                validAtts[i] = true;
                ++attCount;
            }
        }
        //-- process validAtts...if any exist
        AttributeSetImpl attSet = null;
        if (attCount > 0) {
            attSet = new AttributeSetImpl(attCount);
            for (int i = 0; i < validAtts.length; i++) {
                if (!validAtts[i]) continue;
                String namespace = null;
                String attName = atts.getName(i);
                int idx = attName.indexOf(':');
                if (idx > 0) {
                    String prefix = attName.substring(0, idx);
                    if (!prefix.equals(XML_PREFIX)) {
                        attName = attName.substring(idx+1);
                        namespace = _namespaces.getNamespaceURI(prefix);
                        if (namespace == null) {
                            String error = "The namespace associated with "+
                                "the prefix '" + prefix +
                                "' could not be resolved.";
                            throw new SAXException(error);

                        }
                    }
                }
                attSet.setAttribute(attName, atts.getValue(i), namespace);
            }
        }
        else attSet = new AttributeSetImpl(0);

        return attSet;

    } //-- method: processAttributeList

    //---------------------------------------/
    //- org.xml.sax.DocumentHandler methods -/
    //---------------------------------------/

    public void characters(char[] ch, int start, int length)
        throws org.xml.sax.SAXException
    {
        try {
            _compReader.characters(ch, start, length);
        }
        catch(XMLException ex) {
            throw new SAXException(ex);
        }

    } //-- characters

    public void endDocument()
        throws org.xml.sax.SAXException
    {
        //-- do nothing

    } //-- endDocument

    public void endElement(String name)
        throws org.xml.sax.SAXException
    {
        String namespace = null;
        int idx = name.indexOf(':');
        if (idx >= 0 ) {
            String prefix = name.substring(0,idx);
            name = name.substring(idx+1);
            namespace = _namespaces.getNamespaceURI(prefix);
        }
        else namespace = _namespaces.getNamespaceURI("");

        //-- remove namespaces
        if (_namespaces.getParent() != null) {
            _namespaces = _namespaces.getParent();
        }

        try {
            _compReader.endElement(name, namespace);
        }
        catch(XMLException ex) {
            throw new SAXException(ex);
        }

    } //-- endElement


    public void ignorableWhitespace(char[] ch, int start, int length)
        throws org.xml.sax.SAXException
    {
        //-- do nothing

    } //-- ignorableWhitespace

    public void processingInstruction(String target, String data)
        throws org.xml.sax.SAXException
    {
        //-- do nothing

    } //-- processingInstruction

    public void setDocumentLocator(Locator locator) {
        _compReader.setDocumentLocator(locator);
    } //-- setDocumentLocator

    public void startDocument()
        throws org.xml.sax.SAXException
    {
        //-- do nothing

    } //-- startDocument


    public void startElement(String name, AttributeList atts)
        throws org.xml.sax.SAXException
    {
        //-- create new Namespace scope
        Namespaces nsDecls = _namespaces.createNamespaces();
        _namespaces = nsDecls;

        //-- handle namespaces
        AttributeSet attSet = processAttributeList(atts);

        String namespace = null;
        int idx = name.indexOf(':');
        if (idx >= 0 ) {
            String prefix = name.substring(0,idx);
            name = name.substring(idx+1);
            namespace = _namespaces.getNamespaceURI(prefix);
        }
        else namespace = _namespaces.getNamespaceURI("");

        try {
            _compReader.startElement(name, namespace, attSet, nsDecls);
        }
        catch(XMLException ex) {
            throw new SAXException(ex);
        }

    } //-- startElement


    //------------------------------------/
    //- org.xml.sax.ErrorHandler methods -/
    //------------------------------------/

       //------------------------------------/
    //- org.xml.sax.ErrorHandler methods -/
    //------------------------------------/

    public void error(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String systemId = exception.getSystemId();
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        if (systemId != null) {
            err = "In document: '"+systemId+"'\n" + err;
        }

        throw new SAXException (err);
    } //-- error

    public void fatalError(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String systemId = exception.getSystemId();
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        if (systemId != null) {
            err = "In document: '"+systemId+"'\n" + err;
        }
        throw new SAXException (err);

    } //-- fatalError


    public void warning(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String systemId = exception.getSystemId();
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        if (systemId != null) {
            err = "In document: '"+systemId+"'\n" + err;
        }
        throw new SAXException (err);

    } //-- warning

} //-- Sax2ComponentReader

