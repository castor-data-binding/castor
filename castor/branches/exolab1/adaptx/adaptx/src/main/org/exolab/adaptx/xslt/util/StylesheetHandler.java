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

package org.exolab.adaptx.xslt.util;


import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.net.URIResolver;
import org.exolab.adaptx.net.URIException;

import org.exolab.adaptx.util.ErrorObserverAdapter;
import org.exolab.adaptx.util.ErrorObserver;
import org.exolab.adaptx.util.QuickStack;
import org.exolab.adaptx.xml.AttributeListWrapper;
import org.exolab.adaptx.xml.Whitespace;
import org.exolab.adaptx.xml.XMLUtil;
import org.exolab.adaptx.xslt.*;

import org.xml.sax.*;

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;


/**
 * A class which implements DocumentHandler and is used by the
 * XSLReader when reading an XSLT stylsheet.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class StylesheetHandler extends ErrorObserverAdapter
    implements DocumentHandler
{

    /**
     * I use this so that I don't have to check for a Null
     * attribute list everywhere
    **/
    private static AttributeListWrapper _defaultAtts 
        = new AttributeListWrapper(null);


    /**
     * Keeps track of the tree depth
    **/
    private int _depth = 0;
    
    /**
     * SAX Locator
    **/
    private Locator    _locator                = null;
    
    /**
     * Keeps track of the current XSLObjects
    **/
    private QuickStack    _objects     = null;
    
    /**
     * The mapping for xsl:functions handlers
    **/
    private static final Hashtable  _functionsTypeMap   
        = createFunctionMapping();
    
    /**
     * The stylesheet we are building
    **/
    private XSLTStylesheet _stylesheet  = null;
    
    /**
     * The XSLT namespace prefix
    **/
    private String        _xsltPrefix = null;
    
    /**
     * A flag indicating a literal stylesheet, NOT
     * a literal element
    **/
    private boolean       _literal    = false;
    
    private boolean       _ignore      = false;
    private int           _ignoreDepth = 0;
    
    private boolean       _dirty       = false;
    
    /**
     * The XSLTReader to use for xsl:include and xsl:import
    **/
    private XSLTReader     _xsltReader  = null;
    private URIResolver   _resolver   = null;
    
    /**
     * Creates a new StylesheetHandler
    **/
    public StylesheetHandler() {
        this(null);
    } //-- StylesheetHandler
    
    /**
     * Creates a new StylesheetHandler using the given XSLReader.
     * @param xsltReader the XSLTReader to use when reading imported
     * or included stylesheets
    **/
    public StylesheetHandler(XSLTReader xsltReader) {
        super();
        if (xsltReader == null) {
            _xsltReader = new XSLTReader();
            //-- add ourselves as an observer
            //-- to the XSLReader
            _xsltReader.addErrorObserver(this);
        }
        else {
            _xsltReader = xsltReader;
            //-- add the given xslReader to our
            //-- list of observers
            addErrorObserver(_xsltReader);
        }
            
       init();
    } //-- StylesheetHandler
    
    /**
     * Iniatilizes this StylesheetHandler
    **/
    private void init() {
        _depth        = 0;
        _ignore       = false;
        _ignoreDepth  = 0;
        _literal      = false;
        _objects      = new QuickStack();
        _stylesheet   = new XSLTStylesheet();
        _xsltPrefix   = null;
        _dirty        = false;
        //-- add stylesheet to stack
        _objects.push(_stylesheet);
    } //--

    /**
     * Return the XSLStylesheet created by this StylesheetHandler
     * @return the XSLStylesheet created by this StylesheetHandler
    **/
    public XSLTStylesheet getStylesheet() {
        return _stylesheet;
    } //-- getStylesheet
    
    
    /**
     * Sets the URILocation for the stylesheet being read
     *
     * @param location the URILocation for the stylesheet
    **/
    public void setURILocation(URILocation location) {
        _stylesheet.setURILocation(location);
    } //-- setDocumentBase
    
    /**
     * Sets the URIResolver for this StylesheetHandler
     *
     * @param resolver the URIResolver this StylesheetHandler should
     * use for resolving all URIs.
    **/
    public void setURIResolver(URIResolver resolver) {
        _resolver = resolver;   
    } //-- URIResolver
    
    //---------------------------/
    //- DocumentHandler Methods -/
    //---------------------------/
    
    /**
     * Signals the start of characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void characters(char[] chars, int start, int length) 
        throws org.xml.sax.SAXException
    {   
        _dirty = true;
        
        
        if (_objects.empty()) return;
        
        XSLObject xslObject = (XSLObject)_objects.peek();
        
        if (XMLUtil.isWhitespace(chars, start, length)) {
            //-- we need to check for "xsl:text" as parent
            switch (xslObject.getType()) {
                case XSLObject.CDATA:
                case XSLObject.TEXT:
                    xslObject.appendText(chars, start, length);
                    break;
                default:
                    break;
            }
            return;
        }
        
        
        
		//-- need to add the following support which I had 
		//-- when using the DOM ...
		//-- set strip leading whitespace flag
		//boolean stripLWS = (node.getPreviousSibling() == null);
		//-- set strip trailing whitespace flag
		//boolean stripTWS = (node.getNextSibling() == null);
		//text = Whitespace.stripSpace(text,stripLWS,stripTWS);
		
		xslObject.appendText(chars, start, length);
		
        
    } //-- characters
    
    /**
     * Signals the end of the document
    **/
    public void endDocument() 
        throws org.xml.sax.SAXException
    {
        _dirty = true;
        
    } //-- endDocument
    
    /**
     * Signals the start of element
     * @param name the name of the element
     * @param atts the AttributeList containing the associated
     * attributes for the element
    **/
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        _dirty = true;
        
        //System.out.println("#endElement: " + name);
        
        //-- check to see if we are in "ignore" mode
        //-- if, so decrement the depth and reset "ignore".
        if (_ignore) {
            --_ignoreDepth;
            _ignore = (_ignoreDepth != 0);
            return;
        }
        
        --_depth;
        
        
        XSLObject xslObject = (XSLObject)_objects.pop();
        
        //-- handle root
        if (_depth == 0) {
            //-- do nothing for now
        }
        //-- handle top-level processing 
        else if ((_depth == 1) && (!_literal)) {
            
            switch (xslObject.getType()) {
                // xsl:attribute-set
                case XSLObject.ATTRIBUTE_SET:
                    _stylesheet.addAttributeSet((AttributeSet)xslObject);
                    break;
                // xsl:param
                case XSLObject.PARAM:
                    try {
                        _stylesheet.addParam((Param)xslObject);
                    }
                    catch(XSLException xslException) {
                        String err = xslException.getMessage();
                        err += "\n -- processing without top-level param.";
                        handleError(err);
                    }
                    break;
                    
                // xsl:variable
                case XSLObject.VARIABLE:
                    try {
                        _stylesheet.addVariable((Variable)xslObject);
                    }
                    catch(XSLException xslException) {
                        String err = xslException.getMessage();
                        err += "\n -- processing without variable.";
                        handleError(err);
                    }
                    break;
                case XSLObject.OUTPUT:
                    _stylesheet.appendAction(xslObject);
                    break;
                case XSLObject.ID:
                    _stylesheet.addId((Id)xslObject);
                    break;
                // xsl:import
                case XSLObject.IMPORT:
                    importStyle(xslObject.getAttribute(Names.HREF_ATTR));
                    break;
                // xsl:include
                case XSLObject.INCLUDE:
                {
                    includeStyle(xslObject.getAttribute(Names.HREF_ATTR));
                    break;
                }
                //<xsl:preserve-space>
                //<xsl:strip-space>
                case XSLObject.PRESERVE_SPACE:
                case XSLObject.STRIP_SPACE:
                    _stylesheet.appendAction(xslObject);
                    break;
                // xsl:template
                case XSLObject.TEMPLATE:
                    try {
                        _stylesheet.addTemplate((TemplateRule)xslObject);
                    }
                    catch(XSLException xslException) {
                        String err = xslException.getMessage();
                        handleError(err);
                    }
                    break;
                // xsl:script  (proprietary)
                case XSLObject.SCRIPT:
                    _stylesheet.addScript((XSLScript)xslObject);
                    break;
            }
        }
        //-- non top-level element
        else {
            XSLObject parent = (XSLObject)_objects.peek();
            parent.appendAction(xslObject);
        }
        
    } //-- endElement
    
    /**
     * Signals the start of ignorable whitespace characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void ignorableWhitespace(char[] chars, int start, int length) 
        throws org.xml.sax.SAXException
    {
        _dirty = true;
        
        //-- we need to check for "xsl:text" as parent
        if (!_objects.empty()) {
            XSLObject xslObject = (XSLObject)_objects.peek();
            switch (xslObject.getType()) {
                case XSLObject.CDATA:
                case XSLObject.TEXT:
                    xslObject.appendText(chars, start, length);
                    break;
                default:
                    break;
            }
        }
    } //-- ignorableWhitespace
        
    /**
     * Signals to recieve a processing instruction
     * @param target the target of the processing instruction
     * @param data the content of the processing instruction
    **/
    public void processingInstruction(String target, String data) 
        throws org.xml.sax.SAXException
    {
        _dirty = true;
        
    } //-- processingInstruction
    
    /**
     * Sets the document locator 
     * @param locator the Locator used by this DocumentHandler
    **/
    public void setDocumentLocator(Locator locator) {
        _locator = locator;
    } //-- setDocumentLocator
    
    
    
    /**
     * Signals the start of a document
    **/
    public void startDocument() 
        throws org.xml.sax.SAXException
    {
        if (_dirty) init();
    } //-- startDocument
    
    /**
     * Signals the start of element
     * @param name the name of the element
     * @param atts the AttributeList containing the associated
     * attributes for the element
    **/
    public void startElement(String name, AttributeList atts) 
        throws org.xml.sax.SAXException 
    {
        
        _dirty = true;
        
        //System.out.println("startElement: " + name);
        
        //-- check to see if we are ignoring elements
        if (_ignore) {
            //System.out.println(" -- ignore mode");
            ++_ignoreDepth;
            return;
        }
        
        String     tagName   = name;
        String     nsPrefix  = XMLUtil.getNameSpacePrefix(tagName);
        short      xslType   = XSLObject.LITERAL;
            

        if (atts == null) atts = _defaultAtts;
        
        
        if (_depth == 0) {
            //-- copy attributes to handle namespace declarations
            try {
                for (int i = 0; i < atts.getLength(); i++) {
                    _stylesheet.setAttribute(atts.getName(i),
                        atts.getValue(i));
                }
            }
            catch(XSLException xslx) {
                handleError(xslx.getMessage());
            }
        }
        
        //-- handle XSLT namespace prefix
        if (_xsltPrefix == null) 
            _xsltPrefix = _stylesheet.getXSLNSPrefix();
            
        //-- remove xslt prefix from element name, if necessary
        boolean xsltElement = false;
        if ((_xsltPrefix != null) && (_xsltPrefix.equals(nsPrefix))) {
            xsltElement = true;
            //-- remove prefix
            if (nsPrefix.length() > 0)
                tagName = tagName.substring(nsPrefix.length()+1);
        }
        else if ((_xsltPrefix == null) && (nsPrefix.length() == 0)) {
            xsltElement = true;
        }
        //-- get XSLT action type
        if (xsltElement) {
            xslType = XSLObject.getTypeFromName(tagName);
        }
        
        
        
        boolean copyAtts        = true;
        boolean processChildren = true;
        
        XSLObject xslObject = null;
        
        
        //-- handle root
        if (_depth == 0) {
            
            //-- atts already copied above for this case.
            copyAtts = false;
            
            //-- LRE Stylesheet ??
            if (xslType != XSLObject.STYLESHEET) {
                
                try {
                    //-- create template rule
                    TemplateRule rule = new TemplateRule();
                    rule.setMatchAttr("/");
                    _stylesheet.addTemplate(rule);
                    _objects.push(rule);
                    ++_depth;
                    //-- copy literal element
                    //_literal = true;
                    xslObject = new XSLObject(XSLObject.LITERAL);
                    xslObject.setTypeName(tagName);
                    
                    //-- copy non-XSLT namespaces and attributes
                    for (int i = 0; i < atts.getLength(); i++) {
                        String attName = atts.getName(i);
                        String attValue = atts.getValue(i);
                        //-- suppress XSLT namespaces
                        if (attName.startsWith("xmlns")) {
                            if (attValue.equals(XSLTStylesheet.XSLT_NAMESPACE))
                            {
                                continue;
                            }
                        }
                        xslObject.setAttribute(attName,attValue);
                    }
                    _objects.push(xslObject);
                    ++_depth;
                    
                }
                catch(XSLException xslx) {
                    handleError(xslx.getMessage());
                }
            }
            
            xslObject = _stylesheet;
            String version = atts.getValue(Names.VERSION_ATTR);
            if ((version == null) || (version.length() == 0)) {
                if (_xsltPrefix != null) {
                    String attName = _xsltPrefix + ':' + Names.VERSION_ATTR;
                    version =  atts.getValue(attName);
                }
            }
            if ((version == null) || (version.length() == 0)) {
                    
                String filename = null;
                if (_locator != null)
                    filename = _locator.getSystemId();
                else
                    filename = "[uri unknown]";
                        
                String err = "error in XSLT file: " + filename;
                    
                err += ";\n -- " + name;
                err += " element must have a 'version' " +
                    " attribute. Please update your stylesheets " + 
                    " accordingly, as this will be changed to a fatal " +
                    " error soon. - Assuming XSLT 1.0";
                        
                version = "1.0";
                handleError(err, ErrorObserver.WARNING);
                    
            }
            //else if (!version.equals("1.0")) {
                //-- forwards compatible processing
                //-- ignore for now...perhaps we should warn user?
            //}
        }
        //-- handle top-level elements 
        else if ((_depth == 1) && (!_literal)) {
            
            
            switch(xslType) {
                
                // xsl:attribute-set
                case XSLObject.ATTRIBUTE_SET:
                {
                    String setName = atts.getValue(Names.NAME_ATTR);
                    String useAtts = atts.getValue(Names.USE_ATTRIBUTE_SETS_ATTR);
                    xslObject = new AttributeSet(setName);
                    if (useAtts != null) {
                        try {
                            xslObject.setAttribute(Names.USE_ATTRIBUTE_SETS_ATTR, 
                                useAtts);
                        }
                        catch(XSLException xslx) {
                            handleError(xslx.getMessage());
                        }
                    }
                    copyAtts = false;
                    break;
                }
                case XSLObject.IMPORT:
                    xslObject = new XSLImport(_stylesheet);
                    processChildren = false;
                    break;
                // <xsl:id>
                case XSLObject.ID:
                    xslObject = new Id();
                    processChildren = false;
                    break;
                // <xsl:output>
                case XSLObject.OUTPUT:
                    xslObject = new XSLOutput();
                    break;
                case XSLObject.PARAM:
                {
                    // since the name of a parameter can't be changed,
                    // we need to set it at creation time
                    xslObject = new Param(atts.getValue(Names.NAME_ATTR));
                        
                    String select = atts.getValue(Names.SELECT_ATTR);
                    if (select != null) {
                        try {
                            xslObject.setAttribute(Names.SELECT_ATTR, select);
                        }
                        catch(XSLException xslx) {
                            handleError(xslx.getMessage());
                        }
                    }
                        
                    copyAtts = false;
                    break;
                }
                case XSLObject.INCLUDE:
                case XSLObject.PRESERVE_SPACE:
                case XSLObject.STRIP_SPACE:
                    xslObject = new EmptyXSLObject(xslType);
                    processChildren = false;
                    break;            
                case XSLObject.TEMPLATE:
                    xslObject = new TemplateRule();
                    break;
                case XSLObject.VARIABLE:
                {
                    // since the name of a variable can't be changed,
                    // we need to set it at creation time
                    xslObject = new Variable(atts.getValue(Names.NAME_ATTR));
                        
                    String select  = atts.getValue(Names.SELECT_ATTR);
                    if (select != null) {
                        try {
                            xslObject.setAttribute(Names.SELECT_ATTR, select);
                        }
                        catch(XSLException xslx) {
                            handleError(xslx.getMessage());
                        }
                    }
                    copyAtts = false;
                    break;
                }
                default:
                    xslObject = new XSLObject(XSLObject.LITERAL);
                    processChildren = false;
                    copyAtts = false;
                    invalidTopLevelError(name);
                    break;
                   
            } //-- switch
            
            //-- add xslObject to stack
            _objects.push(xslObject);
        }
        //-- non top-level elements
        else {

            switch (xslType) {
                
                //-- handle all out-of-order top-level elements
                case XSLObject.ATTRIBUTE_SET:
                case XSLObject.ID:
                case XSLObject.IMPORT:
                case XSLObject.INCLUDE:
                case XSLObject.OUTPUT:
                case XSLObject.PRESERVE_SPACE:
                case XSLObject.STRIP_SPACE:
                    topLevelOnlyError(name);
                    break;
                case XSLObject.APPLY_TEMPLATES:
                case XSLObject.APPLY_IMPORTS:
                case XSLObject.FOR_EACH:
                    xslObject = new Selection(xslType);
                    break;
                case XSLObject.CALL_TEMPLATE:
                {
                    String templateName = null;
                    if (atts != null) 
                        templateName = atts.getValue(Names.NAME_ATTR);
                    xslObject = new XSLCallTemplate(templateName);
                    break;
                }
                case XSLObject.CDATA:   //-- proprietary
                    xslObject = new XSLCData();
                    break;
                case XSLObject.COPY_OF:
                    xslObject = new CopyOf();
                    processChildren = false;
                    break;
                case XSLObject.IF:
                case XSLObject.WHEN:
                    xslObject = new XSLIf();
                    break;
                case XSLObject.NUMBER:
                    xslObject = new XSLNumber();
                    break;
                case XSLObject.OTHERWISE:
                    xslObject = new XSLOtherwise();
                    copyAtts = false;
                    break;
                /*
                case XSLObject.FUNCTIONS:
                    //-- change later to XSLFunctions
                    xslObj = new XSLScript(_stylesheet, XSLObject.getText(element));
                    processChildren = false;
                    break;
                case XSLObject.SCRIPT:
                    xslObj = new XSLScript(parent,XSLObject.getText(element));
                    processChildren = false;
                    break;
                */
                case XSLObject.SORT:
                    xslObject = new XSLSort();
                    processChildren = false;
                    break;
                case XSLObject.TEXT:
                    xslObject = new XSLText();
                    break;
                case XSLObject.VALUE_OF:
                    xslObject = new ValueOf();
                    processChildren = false;
                    break;
                case XSLObject.VARIABLE:
                {
                    // since the name of a variable can't be changed,
                    // we need to set it at creation time
                    xslObject = new Variable(atts.getValue(Names.NAME_ATTR));
                        
                    String select  = atts.getValue(Names.SELECT_ATTR);
                    if (select != null) {
                        try {
                            xslObject.setAttribute(Names.SELECT_ATTR, select);
                        }
                        catch(XSLException xslx) {
                            handleError(xslx.getMessage());
                        }
                    }
                    copyAtts = false;
                    break;
                }
                case XSLObject.WITH_PARAM:
                {
                    // since the name of a variable can't be changed,
                    // we need to set it at creation time
                    String paramName = atts.getValue(Names.NAME_ATTR);
                    xslObject = new ParamVariable(paramName);
                    
                    String select = atts.getValue(Names.SELECT_ATTR);
                    if (select != null) {
                        try {
                            xslObject.setAttribute(Names.SELECT_ATTR, select);
                        }
                        catch(XSLException xslx) {
                            handleError(xslx.getMessage());
                        }
                    }
                        
                    copyAtts = false;
                    break;
                }
                case XSLObject.PARAM:
                {
                    // since the name of a variable can't be changed,
                    // we need to set it at creation time
                    xslObject = new Param(atts.getValue(Names.NAME_ATTR));
                        
                    String select = atts.getValue(Names.SELECT_ATTR);
                    if (select != null) {
                        try {
                            xslObject.setAttribute(Names.SELECT_ATTR, select);
                        }
                        catch(XSLException xslx) {
                            handleError(xslx.getMessage());
                        }
                    }
                        
                    copyAtts = false;
                    break;
                }
                case XSLObject.ENTITY_REF: //--proprietary
                    processChildren = false;
                case XSLObject.ATTRIBUTE:
                case XSLObject.CHOOSE:
                case XSLObject.COMMENT:
                case XSLObject.COPY:
                case XSLObject.ELEMENT:
                case XSLObject.MESSAGE:
                case XSLObject.PI:
                    xslObject = new XSLObject(xslType);
                    break;
                default: //-- LITERAL or unsupported XSL element
                    xslObject = new XSLObject(XSLObject.LITERAL);
                    xslObject.setTypeName(tagName);
                    break;
            }
            //-- add xslObject to stack
            _objects.push(xslObject);
            
        }
        
        
        //-- copy attributes if necessary
        if (copyAtts) {
            try {
                for (int i = 0; i < atts.getLength(); i++) {
                    xslObject.setAttribute(atts.getName(i),
                        atts.getValue(i));
                }
            }
            catch(XSLException xslx) {
                handleError(xslx.getMessage());
            }
        }
        
        ++_depth;
    } //-- startElement
    
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
   private static Hashtable createFunctionMapping() {
    
	    //-- temporary mapping for xsl:functions type attr to
	    //-- xsl:script language attr
        Hashtable ht = new Hashtable();
        ht.put("text/javascript", "ECMAScript");
        ht.put("text/ecmascript", "ECMAScript");
        ht.put("text/jpython", "JPython");
        return ht;
   } //-- createFunctionMapping
    
    /**
     * A wrapper for handling error messages
     * @param message the error message
     * @see ErrorObserver#receiveError
    **/
    private void handleError(String message) 
        throws org.xml.sax.SAXException
    {
        receiveError(message, ErrorObserver.NORMAL);
    } //-- handleError
    
    /**
     * A wrapper for handling error messages
     * @param message the error message
     * @param level the error level
     * @see ErrorObserver#receiveError
    **/
    private void handleError(String message, int level) 
        throws org.xml.sax.SAXException
    {
        if (level == ErrorObserver.FATAL) {
            throw new SAXException("fatal error: " + message);
        }
        else {
            receiveError(message, level);
        }
    } //-- handleError

    /**
     * Includes an XSLStylesheet into the given XSLStylesheet
     * @param href the name of the stylesheet to include 
     * @exception SAXException
    **/
    private void importStyle(String href)
        throws SAXException {
        
        if (_resolver == null)
            _resolver = _xsltReader.getURIResolver();
            
        URILocation location = null;
        
        try {
            location = _resolver.resolve(href, _stylesheet.getDocumentBase());
        }
        catch (URIException exception) {
            throw new SAXException(exception);
        }
            
        href = location.getAbsoluteURI();
        
        // Make sure we aren't trying to import stylesheet already 
        // imported
        if (!_stylesheet.isAllowableImport(href)) {
            String err = "error importing stylesheet: '" + href+ "'.\n";
            err += "The stylesheet was already directly or indirectly ";
            err += "included.";
            handleError(err);
            return;
        }
        
        try {
            _stylesheet.importFrom(_xsltReader.read(location));
        }
        catch(Exception ex) {
            String err = "error including stylesheet: '" + href + "'.\n";
            err += " - " + ex.getMessage() + "\n";
            err += " - continuing processing without the import.";
            handleError(err);
        }
    } //-- importStyle

    /**
     * Includes an XSLStylesheet into the given XSLStylesheet
     * @param href the name of the stylesheet to include 
     * @exception SAXException
    **/
    private void includeStyle(String href)
        throws org.xml.sax.SAXException 
    {
        
        if (_resolver == null)
            _resolver = _xsltReader.getURIResolver();
            
        URILocation location = null;
        
        try {
            location = _resolver.resolve(href, _stylesheet.getDocumentBase());
        }
        catch (URIException exception) {
            throw new SAXException(exception);
        }
            
        href = location.getAbsoluteURI();
        
        // Make sure we aren't trying to import stylesheet already 
        // imported
        if (!_stylesheet.isAllowableImport(href)) {
            String err = "error including stylesheet: '" + href+ "'.\n";
            err += "The stylesheet was already directly or indirectly ";
            err += "included.";
            handleError(err);
            return;
        }
        
        try {
            _stylesheet.includeFrom(_xsltReader.read(location));
        }
        catch(Exception ex) {
            String err = "error including stylesheet: '" + href + "'.\n";
            err += " - " + ex.getMessage() + "\n";
            err += " - continuing processing without the import.";
            handleError(err);
        }
    } //-- includeStyle
    
    /**
     * Signals an error when a non top-level xsl element
     * appears as a top-level element.
     * @param name the name of the element.
    **/
    private void invalidTopLevelError(String name) {
        
        String err = "' must not appear as a top-level element, " +
            "may only be a descendant of a 'template' element.";
            
        receiveError("'"+name+err);
    } //-- invalidTopLevelError
    
    
    /**
     * Signals an error when a top-level xsl element appears in
     * wrong spot.
     * @param name the name of the element.
    **/
    private void topLevelOnlyError(String name) {
        
        String err = "' must only appear as a top-level element, " +
            "a direct child of the stylesheet element.";
            
        receiveError("'"+name+err);
    } //-- topLevelOnlyError
    
    
} //-- StylesheetHandler
