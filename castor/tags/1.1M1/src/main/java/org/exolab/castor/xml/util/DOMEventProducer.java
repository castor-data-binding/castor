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
 * This file was adapted From XSL:P 
 * 
 * $Id$
 */

package org.exolab.castor.xml.util;


import org.exolab.castor.xml.EventProducer;

import org.w3c.dom.*;
import org.xml.sax.*;


/**
 * A class for converting a DOM document to SAX events
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
**/
public class DOMEventProducer implements EventProducer {

    
    private DocumentHandler _handler = null;
    
    private Node _node = null;

    /**
     * Creates a new DOMEventProducer 
    **/
    public DOMEventProducer() {
        super();
    } //-- DOMEventProducer
    
    /**
     * Creates a new DOMEventProducer for the given Node
     * @param node the node to create the DOMEventProducer for.
    **/
    public DOMEventProducer(Node node) {
        super();
        this._node = node;
    } //-- DOMEventProducer
    
    
    /**
     * Sets the DocumentHandler to use when firing events
    **/
    public void setDocumentHandler(DocumentHandler handler) {
        this._handler = handler;
    } //-- setDocumentHandler
    
    /** 
     * Sets the node which is to be converted into SAX events
     * @param node the node which is to be converted into SAX events
    **/
    public void setNode(Node node) {
        this._node = node;
    } //-- setNode
    
    /**
     * Starts producing the events for the Node which is to be
     * converted into SAX events
    **/
    public void start() 
        throws org.xml.sax.SAXException 
    {
        if ((_node == null) || (_handler == null)) return;

        process(_node, _handler);
        
    } //-- start
    
    /**
     * Walks the given DOM Document and converts it into it's corresponding
     * SAX events
     * @param document the Node to process into SAX events
     * @param handler the DocumentHandler to send events to
    **/
    public static void process(Document document, DocumentHandler handler) 

        throws org.xml.sax.SAXException

    {

        if (document == null) return;

        if (handler == null) return;

        

        handler.startDocument();

        processChildren(document, handler);

        handler.endDocument();

        

    } //-- process(Document, DocumentHandler)

    

    /**

     * Breaks down the given node into it's corresponding SAX events

     * @param node the Node to process into SAX events

     * @param handler the DocumentHandler to send events to

    **/

    public static void process(Node node, DocumentHandler handler) 

        throws org.xml.sax.SAXException

    {

        

        if ((node == null) || (handler == null)) return;

        

        switch(node.getNodeType()) {

            case Node.DOCUMENT_NODE:

                process((Document)node, handler);

                break;

            case Node.DOCUMENT_FRAGMENT_NODE:

                processChildren(node, handler);

                break;

            case Node.ELEMENT_NODE:

                process((Element)node, handler);

                break;

            case Node.CDATA_SECTION_NODE:

            case Node.TEXT_NODE:

                process((Text)node, handler);

                break;

            case Node.PROCESSING_INSTRUCTION_NODE:

                process((ProcessingInstruction)node, handler);

                break;

            case Node.COMMENT_NODE:

            

            default:

                break;

        }

        

    } //-- process(Node, DocumentHandler)

    

    //-------------------/

    //- Private Methods -/

    //-------------------/

    

    /**

     * Breaks down the given node into it's corresponding SAX events

     * @param handler the DocumentHandler to send events to

    **/

    private static void process(Element element, DocumentHandler handler) 

        throws org.xml.sax.SAXException

    {

        

        String name = element.getNodeName();
        
        AttributeList atts 
            = new AttributeListWrapper(element.getAttributes());

        handler.startElement(name, atts);

        processChildren(element, handler);

        handler.endElement(name);

    } //-- process(Element, DocumentHandler);

    

    /**

     * Breaks down the given node into it's corresponding SAX events

     * @param handler the DocumentHandler to send events to

    **/

    private static void process(Text text, DocumentHandler handler) 

        throws org.xml.sax.SAXException

    {

        String data = text.getData();

        if ((data != null) && (data.length() > 0)) {

            char[] chars = data.toCharArray();

            handler.characters(chars, 0, chars.length);

        }

    } //-- process(Text, DocumentHandler)

    

    /**

     * Breaks down the given ProcessingInstruction into it's corresponding 

     * SAX event

     * @param pi the processing instruction to process

     * @param handler the DocumentHandler to send events to

    **/

    private static void process

        (ProcessingInstruction pi, DocumentHandler handler) 

        throws org.xml.sax.SAXException

    {

        handler.processingInstruction(pi.getTarget(),pi.getData());

        

    } //-- process(ProcessingInstruction, DocumentHandler);



    /**

     * Processes the children of the given Node

     * @param node the Node to process the children of

     * @param handler the DocumentHandler to send events to

    **/

    private static void processChildren

        (Node node, DocumentHandler handler)

        throws org.xml.sax.SAXException

    {

        Node child = node.getFirstChild();

        while (child != null) {

            process(child, handler);

            child = child.getNextSibling();

        }

        

    } //-- processChildren

    

    

} //-- DOMEventProducer

