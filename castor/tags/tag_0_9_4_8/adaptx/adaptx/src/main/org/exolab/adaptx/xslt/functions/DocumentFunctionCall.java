/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
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

package org.exolab.adaptx.xslt.functions;

import org.exolab.adaptx.net.URIException;
import org.exolab.adaptx.net.URIResolver;
import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.net.impl.URIResolverImpl;
import org.exolab.adaptx.xslt.ProcessorState;
import org.exolab.adaptx.xslt.util.Configuration;
import org.exolab.adaptx.xslt.XSLTFunction;
import org.exolab.adaptx.xslt.Names;
import org.exolab.adaptx.xpath.*;
import org.exolab.adaptx.util.*;
import org.exolab.adaptx.xml.dom2xpn.DocumentWrapperXPathNode;
import org.exolab.adaptx.xml.parser.DOMParser;

import java.io.Reader;

import org.w3c.dom.Node;
import org.w3c.dom.Document;


/**
 * A class that represents the XSL document() function call
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DocumentFunctionCall extends XSLTFunction {
    
    private ProcessorState ps = null;
    
    /**
     * Creates a new Document() Function Call
    **/
    public DocumentFunctionCall(ProcessorState ps) {
        super(Names.DOCUMENT_FN);
        this.ps = ps;
    } //-- DocumentFunctionCall
    
    /**
     * Invokes the function and returns the XPath result.
     *
     * @param context The XPath context
     * @param params A list of zero or more arguments
     * @return An XPath result (not null)
     * @throws XPathException An error occured while invoking this function
     */
    public XPathResult call(XPathContext context, XPathResult[] args)
        throws XPathException
    {
        
        if ((args.length < 1) || (args.length > 2))
            throw new XPathException(INVALID_NUMBER_PARAMS+this);
            
        NodeSet nodeSet = new NodeSet();
        
        switch (args[0].getResultType()) {
            case XPathResult.NODE_SET:
                NodeSet nodes = (NodeSet)args[0];
                for (int i = 0; i < nodes.size(); i++) {
                    String href = nodes.item(i).getStringValue();
                    Document doc = getDocument(href);
                    if (doc == null) {
                        String warn = "Unable to read document: " + href;
                        ps.getErrorObserver().receiveError(warn,
                            ErrorObserver.WARNING);
                    }
                    if (doc != null) {
                        nodeSet.add(new DocumentWrapperXPathNode(doc));
                    }
                }
                break;
            default:
                String href = args[0].stringValue();
                Document doc = getDocument(href);
                if (doc == null) {
                    String warn = "Unable to read document: " + href;
                    ps.getErrorObserver().receiveError(warn,
                        ErrorObserver.WARNING);
                }
                if (doc != null) {
                    nodeSet.add(new DocumentWrapperXPathNode(doc));
                }
                break;
        }
        return nodeSet;
    } //-- evaluate
    
    private Document getDocument(String href) {
        
        URILocation target        = null;
        URILocation styleLocation = ps.getStylesheetLocation();
        
        if ((href == null) || (href.length() == 0)) {
            if (styleLocation == null) return null;
            target = styleLocation;
        }
        else {
            String documentBase = null;
            if (styleLocation != null) 
                documentBase = styleLocation.getBaseURI();
                
            URIResolver resolver = ps.getURIResolver();
            try {
                target = resolver.resolve(href, documentBase);
            }
            catch(URIException exception) {
                ErrorObserver observer = ps.getErrorObserver();
                observer.receiveError(exception.getMessage());
                return null;
            }
        }
        
        ErrorObserver observer = ps.getErrorObserver();
        DOMParser  domParser  = Configuration.getDOMParser();
        return domParser.readDocument(target, observer);
        
    }
    
} //-- DocumentFunctionCall
