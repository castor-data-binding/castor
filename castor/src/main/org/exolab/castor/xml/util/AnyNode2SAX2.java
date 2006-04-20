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
 * Copyright 2001-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author              Changes
 * 04/06/2001   Arnaud Blandin      Created
 * 09/09/2004   Keith Visco         Modified for SAX 2 support
 */

package org.exolab.castor.xml.util;

import java.util.HashSet;

import org.xml.sax.ContentHandler;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.Namespaces;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.SAXException;

/**
 * A class for converting an AnyNode to SAX events
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * 
 * @version $Revision$ $Date$
 */
public class AnyNode2SAX2 {

   /**
    * The AnyNode we are firing events for
    */
    private AnyNode _node;

   /**
    * The Content Handler
    */
    private ContentHandler _handler;

   /**
    * The stack to store the elements
    */
    private HashSet _elements;

    /**
     * The namespace context
     */
    private Namespaces _context;

    /**
     * The namespace declaration String
     */
    private static final String XMLNS_PREFIX  = "xmlns";


    public AnyNode2SAX2() {
		_elements = new HashSet();
	}


    /**
     * Creates a AnyNode2SAX for the given node.
     */
    public AnyNode2SAX2(AnyNode node) {
        this();
        _node = node;
        _context = new Namespaces();
    }

    /**
     * Creates a AnyNode2SAX for the given node and the namespace context.
     */
    public AnyNode2SAX2(AnyNode node, Namespaces context) {
        this();
        _node = node;
        if (context == null)
           _context = new Namespaces();
        else _context = context;
    }

    /**
     * Set the ContentHandler to send events to
     * 
     * @param handler the document handler to set
     */
    public void setContentHandler(ContentHandler handler) {
        if (handler == null)
           throw new IllegalArgumentException("AnyNode2SAX2#setContentHandler 'null' value for handler");
        _handler = handler;
    }

    public static void fireEvents(AnyNode node, ContentHandler handler)
        throws SAXException
    {
        fireEvents(node, handler, null);
    }

    public static void fireEvents(AnyNode node, ContentHandler handler, Namespaces context)
        throws SAXException
    {
        AnyNode2SAX2 eventProducer = new AnyNode2SAX2(node, context);
        eventProducer.setContentHandler(handler);
        eventProducer.start();
    }
    public void start() throws org.xml.sax.SAXException {
        if ( (_node == null) || (_handler == null) )
           return;
        else processAnyNode(_node, _handler);
    }

    private void processAnyNode(AnyNode node, ContentHandler handler)
         throws SAXException
    {

        if ( (node == null) || (handler == null) ) {
            throw new IllegalArgumentException();
        }

		//-- so we don't potentially get into
		//-- an endlessloop
		if (!_elements.add(node)) return;

        if (node.getNodeType() == AnyNode.ELEMENT) 
		{
        	//-- node local name
            String name = node.getLocalName();
            
            //-- retrieve the namespaces declaration and handle them
            AnyNode tempNode = node.getFirstNamespace();
            String prefix = null;
            while (tempNode != null) {
            	prefix = tempNode.getNamespacePrefix();
            	if (prefix == null) prefix = "";
            	String value = tempNode.getNamespaceURI();
            	if (value == null) value = "";               
            	handler.startPrefixMapping(prefix, value);              
            	if (value != null && value.length() >0)
            		_context.addNamespace(prefix, value);
            	tempNode = tempNode.getNextSibling();
            }//namespaceNode

            //-- retrieve the attributes and handle them
            AttributesImpl atts = new AttributesImpl();
            tempNode = node.getFirstAttribute();
            String xmlName = null;
            String value = null;
            String attUri = null;
            String attPrefix = null;            
            while (tempNode != null) {                
                xmlName = tempNode.getLocalName();
                String localName = xmlName;
                //--retrieve a prefix?
                attUri = tempNode.getNamespaceURI();
                if (attUri != null)
                    attPrefix = _context.getNamespacePrefix(attUri);
                else
                    attUri = "";
                
                if (attPrefix != null && attPrefix.length() > 0)
                    xmlName = attPrefix + ':' + xmlName;
                
                value = tempNode.getStringValue();
                atts.addAttribute(attUri, localName, xmlName, "CDATA", value);
                tempNode = tempNode.getNextSibling();
            }//attributes

            //-- namespace management
            _context = _context.createNamespaces();
            String nsPrefix = node.getNamespacePrefix();
            String nsURI = node.getNamespaceURI();

            String qName = null;
            //maybe the namespace is already bound to a prefix in the
            //namespace context
            if (nsURI != null && nsURI.length() > 0) {
                String tempPrefix = _context.getNamespacePrefix(nsURI);
                if (tempPrefix != null)
                    nsPrefix = tempPrefix;
                else {
                    _context.addNamespace(nsPrefix, nsURI);
                }
            }
            else nsURI = "";


            if (nsPrefix != null) {
                int len = nsPrefix.length();
                if (len > 0) {
                    StringBuffer sb = new StringBuffer(len+name.length()+1);
                    sb.append(nsPrefix);
                    sb.append(':');
                    sb.append(name);
                    qName = sb.toString();
                } else qName = name;
            } else {
                qName = name;
            }

             try {
                 //_context.declareAsAttributes(atts,true);
                 handler.startElement(nsURI, name, qName, atts);
             } catch (SAXException sx) {
                  throw new SAXException(sx);
             }

             //-- handle child&daughter elements
             tempNode = node.getFirstChild();
             while (tempNode != null) {
                 processAnyNode(tempNode, handler);
                 tempNode = tempNode.getNextSibling();
             }

             //-- finish element
             try {
               handler.endElement(nsURI, name, qName);
               _context = _context.getParent();
               
               //-- retrieve the namespaces declaration and handle them
               tempNode = node.getFirstNamespace();
               while (tempNode != null) {
                   prefix = tempNode.getNamespacePrefix();
                   if (prefix == null) prefix = "";
                   handler.endPrefixMapping(prefix);
                   tempNode = tempNode.getNextSibling();
                }//namespaceNode
               
               
             } catch(org.xml.sax.SAXException sx) {
                  throw new SAXException(sx);
             }
             
             

       }//ELEMENTS
        else {
            if (node.getNodeType() == AnyNode.TEXT) {
                String value = node.getStringValue();
                if ( (value != null) && (value.length() >0) ) {
                    char[] chars = value.toCharArray();
                    try {
                        handler.characters(chars, 0, chars.length);
                    } catch(org.xml.sax.SAXException sx) {
                        throw new SAXException(sx);
                    }
                }
            }
        }
    }

    private void processTextNode(AnyNode node, ContentHandler handler)
        throws SAXException
    {
        if (node.getNodeType() != AnyNode.TEXT) {
           String err = "This node can not be handle by processTextNode.";
            throw new IllegalArgumentException(err);
        }
        String value = node.getStringValue();
        if ( (value != null) && (value.length() >0) ) {
            char[] chars = value.toCharArray();
            try {
                handler.characters(chars, 0, chars.length);
            } catch(org.xml.sax.SAXException sx) {
                 throw new SAXException(sx);
            }
         }
    }//processTextNode
}