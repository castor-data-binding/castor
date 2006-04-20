/*
 * (C) Copyright Keith Visco 1998, 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */

package org.exolab.adaptx.xml;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * A class for converting a DOM document to SAX events
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DOM2SAX {
    
    
    
    
    /**
     * Walks the given DOM Document and converts it into it's corresponding
     * SAX events
     * @param document, the Node to process into SAX events
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
     * @param node, the Node to process into SAX events
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
    
    
} //-- DOM2SAX
