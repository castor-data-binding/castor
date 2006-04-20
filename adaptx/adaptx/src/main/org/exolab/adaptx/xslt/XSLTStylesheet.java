/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
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

package org.exolab.adaptx.xslt;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.util.List;
import org.exolab.adaptx.util.HashMap;
import org.exolab.adaptx.util.Tokenizer;


import org.w3c.dom.*;

/**
 * This class represents an XSL stylesheet
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class XSLTStylesheet extends XSLObject {
    
    
    /**
     * The current (and ever changing) XSLT namespace
    **/
    public static final String XSLT_NAMESPACE = 
        "http://www.w3.org/1999/XSL/Transform";
        
    
    
    private static final String XMLNS_DECL = "xmlns:";
    private static final String XMLNS_ATTR = "xmlns";
    private static final String QUOTE      = "quote:";
    
    /**
     * HTTP protocol string
    **/
    private static final String HTTP_PROTOCOL  = "http:";
    
    /**
     * file protocol string
    **/
    private static final String FILE_PROTOCOL  = "file:";


    /**
     * To save some overhead of constructing an XSLT
     * stylesheet I use Hashtable for cloning when
     * another Hashtable is null
    **/
    private static final Hashtable  _emptyHash = new Hashtable(1);
        
	/**
	 * This stylesheet's attribute sets for use with xsl:use
	 * A Hashtable of AttributeSet objects 
	 * @see org.exolab.adaptx.xslt.AttributeSet
	**/
    private Hashtable  attributeSets       = null;
    
    /**
     * set of global variables
    **/
    private Hashtable variables            = null;
    
    /**
     * The set of top-level parameters
    **/
    private Hashtable parameters           = null;
    
	/**
	 * This stylesheet's document base
	**/
    private URILocation uriLocation = null;

    /**
     * The set of namedTemplates for this stylesheet
     * @see org.exolab.adaptx.xslt.NamedTemplates
    **/
	private Hashtable   namedTemplates    = null;	
	
	/**
	 * The list of extension-element-prefixes
	**/
	private List extElementPrefixes        = null;
	
	/**
	 * list of attributes that should be treated as IDs
	 * see WD 1.0 section 2.4.2.1
	**/
	private Hashtable      ids             = null;

    /**
     * a list of imported stylesheets
    **/
    private List imports                     = null;
    
    /**
     * a list of element types to preserve whitespace for
     * or strip whitespace from, based on default-space
     * attribute
    **/
	private List whitespaceElements      = null;
	
	
	/**
	 * This stylesheets template rules
	**/
    private List      templates         = null;
    
    /**
     * Proprietary Scripts (ECMAScript)
    **/
    private List     gScripts             = null;

	
	//private StylesheetElement ssElement   = null;
	//private Element ssElement             = null;
	
	private StringBuffer error            = null;
	
	private String resultDocType          = null;
    
    private String    xslNSPrefix = "xsl"; //-- default prefix
    private Hashtable namespaces  = null;
    private Hashtable quotedns    = null;
    
    private boolean allowImports = true;
    
	//- Map tables for Expressions -/
	
	private Hashtable avtMap              = null;
	
	private boolean preserveSpaceByDefault = true;
	
	
	//-- main xsl:output  
	XSLOutput output = null;
	
	
      //----------------/
     //- Constructors -/
    //----------------/
    
    
    /**
     * Create a new XSLStylesheet
    **/
	public XSLTStylesheet() {
	    
	    super(XSLObject.STYLESHEET);
	    
	    attributeSets      = new Hashtable();
	    extElementPrefixes = new List();
	    gScripts           = new List(); 
	    ids                = new Hashtable();
	    imports            = new List();
	    namedTemplates     = new Hashtable();
	    namespaces         = new Hashtable();
	    whitespaceElements = new List();
	    quotedns           = new Hashtable();
        templates          = new List();
	    output             = new XSLOutput();
	    
	    //-- add default output.
	    super.appendAction(output);
	    
	    //-- add default id attribute
	    ids.put("id","*");
	    
    } //-- XSLStylesheet
	
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Adds the given AttributeSet to this stylesheet
     * @param attributeSet the AttributeSet to add
    **/
	public void addAttributeSet(AttributeSet attributeSet) {
	    String name = attributeSet.getName();
	    
	    AttributeSet oldSet = (AttributeSet) attributeSets.get(name);
	    
	    attributeSets.put(name, attributeSet);
	    
	    // merge attribute sets if necessary
	    if (oldSet != null) {
	        attributeSet.copyActions(oldSet);
	        String useAtts = oldSet.getUseAttributeSets();
	        if (useAtts != null) {
	            if (attributeSet.getUseAttributeSets() != null) {
	                useAtts += ' ' + attributeSet.getUseAttributeSets();
	            }
	            attributeSet.setUseAttributeSets(useAtts);
	        }
	    }
	    
	} //-- addAttributeSet
	
	/**
	 * Adds a new top-level Variable to this stylesheet
	 * @param variable the Variable to add
	 * @exception XSLException when adding a Variable with the same
	 * name as a previously added Variable or Parameter.
	**/
    public void addVariable(Variable variable) 
        throws XSLException
    {
        addVariable(variable, false);
    } //-- addVariable
    
    
    
	/**
	 * Adds a new Id to the list of IDs for this stylesheet for all elements.
	 * @param idAttr the id attribute to add
	**/
    public void addId(String idAttr) {
        addId(idAttr,"*");
    } //-- addId
    
	/**
	 * Adds a new Id for the specified element type to the list of 
	 * IDs for this element type stylesheet.
	 * @param idAttr the ID to add
	 * @param elementType the tag name of the element that id is an ID for
	 * <B>
	 * Note: the wild card "*" will match all element types
	**/
    public void addId(String idAttr, String elementType) {
        if (elementType == null) elementType = "*";
        if (!ids.contains(idAttr)) {
            ids.put(idAttr,elementType);
            allowImports = false;
        }
    } //-- addId

	/**
	 * Adds a new Id for the specified element type to the list of 
	 * IDs for this element type stylesheet.
	 * @param id the Id to add
	**/
    public void addId(Id id) {
        String idAttr = id.getIdAttribute();
        String elementType = id.getElementType();
        addId(idAttr,elementType);
        super.appendAction(id);
    } //-- addId

	/**
	 * Adds a new top-level parameter to this stylesheet.
	 * The parameter may, or may not have a default value.
	 * @param param the top-level parameter to add
	 * @exception XSLException when adding a parameter with the same
	 * name as a previously declared parameter, or variable
	**/
    public void addParam(Param param) 
        throws XSLException
    {
        addParam(param, false);
        
    } //-- addParam

	/**
	 * Adds a new TemplateRule to the list of rules for this stylesheet.
	 * @param rule the TemplateRule to add
	 * @exception XSLException when a template already exists with the given
	 * name
	**/
    public void addTemplate(TemplateRule template) 
        throws XSLException 
    {                
        addTemplate(template, false);
    } //-- addTemplate
    
	/**
	 * Adds a new global level XSLScript to this stylesheet. 
	 * <BR>Note: This is currently a proprietary feature
	 * @param script the XSLScript to add
	**/
    public void addScript(XSLScript script) {        
        gScripts.add(script);
        allowImports = false;
    } //-- addScript
    
    /**
     * Adds the given XSLObject to this Stylesheet.<BR>
     * The following elements are valid:<BR>
     * <PRE>
     *   xsl:attribute-set
     *   xsl:constant
     *   xsl:id
     *   xsl:import
     *   xsl:include
     *   xsl:macro
     *   xsl:template
     *   xsl:preserve-space
     *   xsl:strip-space
     *
     * The following proprietary elemens are also valid:
     *   xsl:script
     * <PRE>
    **/
    public boolean appendAction(XSLObject xslObject) {
        
        if (xslObject == null) return false;
        
        try {
            
            boolean callParent = true;
            
            switch (xslObject.getType()) {
                case XSLObject.OUTPUT:
                    //-- merge output objects
                    mergeOutput((XSLOutput)xslObject);
                    break;
                case XSLObject.TEMPLATE:
                    addTemplate((TemplateRule)xslObject);
                    callParent = false;
                    break;
                case XSLObject.ATTRIBUTE_SET:
                    addAttributeSet((AttributeSet)xslObject);
                    break;
                case XSLObject.PARAM:
                    addParam((Param)xslObject, false);
                    callParent = false;
                    break;
                case XSLObject.VARIABLE:
                    addVariable((Variable)xslObject, false);
                    callParent = false;
                    break;
                case XSLObject.ID:
                    addId((Id)xslObject);
                    callParent = false;
                    break;
                case XSLObject.SCRIPT:
                    addScript((XSLScript)xslObject);
                    break;
                case XSLObject.IMPORT:
                    importFrom((XSLImport)xslObject);
                    break;
                case XSLObject.PRESERVE_SPACE:
                    preserveSpace(xslObject.getAttribute(Names.ELEMENTS_ATTR));
                    break;
                case XSLObject.STRIP_SPACE:
                    stripSpace(xslObject.getAttribute(Names.ELEMENTS_ATTR));
                    break;
                default:
                    return false;
            } //-- switch
            if (callParent) super.appendAction(xslObject);
        }
        catch(XSLException xslException) {
            return false;
        }
        return true;
    } // -- appendAction
    
    /**
     * @param setName the name of the attribute set to return
     * @return the AttributeSet for the given name, 
     * or null if not found.
     * @see org.exolab.adaptx.xslt.AttributeSet
    **/
    public AttributeSet getAttributeSet(String setName) {
        return (AttributeSet) attributeSets.get(setName);
    } //-- getAttributeSet
    
    /**
     * Returns all of the attribute-sets for this stylesheet
     * @return all of the AttributeSets for this stylesheet
    **/
    public Hashtable getAttributeSets() {
        return (Hashtable)attributeSets.clone();
    } //--getAttributeSets
    
    /**
     * Returns the document base for this stylesheet
     * @return the document base for this stylesheet
    **/
	public String getDocumentBase() {
	    if (uriLocation == null) return "";
	    return uriLocation.getBaseURI();
	} //-- getDocumentBase

    /**
     * Returns an enumeration of the declared namespace prefixes 
     * for extension elements
    **/
    public List getExtensionElementPrefixes() {
        return extElementPrefixes;
    } //-- getExtensionElementPrefixes
	
	/**
	 * Returns the URILocation for this stylesheet, or null, if
	 * no location was set.
	 *
	 * @return the URILocation for this stylesheet
	 * @see getHref
	 * @see getDocumentBase
	**/
	public URILocation getURILocation() {
	    return uriLocation;
	} //-- getURILocation
	
	/**
	 * Returns the XSLOutput Object for controlling the serialization 
	 * of Result Documents created using this Stylesheet
	 * @return the Output Object for controlling the serialization 
	 * of Result Documents created using this Stylesheet
	**/
	public XSLOutput getOutput() {
	    return output;
	} //-- getOutput
	
	/**
	 * @return the href for this stylesheet
	**/
	public String getHref() {
	    if (uriLocation == null) return null;
	    return uriLocation.getAbsoluteURI();	    
	} //-- getHref
	
    /**
     * @return a Vector of all the ID attributes for this Stylesheet
    **/
	public Hashtable getIds() {
	    return ids;
	}
	
    /**
     * @return a Vector of the TemplateRule objects for this 
     * stylesheet, does not include imported TemplateRules.
    **/
	public List getLocalTemplates() {
	    return (List) templates.clone();
	} //-- getLocalTemplates
	
    /**
     * Returns the TemplateRule whose name matches the given name argument.
     * Peter Ciuffetti.  Added for WD-xslt-1990421
     * @param name the name of the NamedTemplate to return
     * @return the TemplateRule whose name matches the given name argument
     * @since WD-xslt-19990421
    **/
	public TemplateRule getNamedTemplate(String name) {
	    if (name == null) return null;
	    
	    TemplateRule nmTemplate = (TemplateRule)namedTemplates.get(name);
	    
	    //-- check imported stylesheets (kav)
	    if (nmTemplate == null) {
	        for (int i = imports.size()-1; i >= 0; i--) {
	            XSLTStylesheet xslStyle = (XSLTStylesheet) imports.get(i);
	            nmTemplate = xslStyle.getNamedTemplate(name);
	            if (nmTemplate != null) break;
	        }
	    }
	    
	    return nmTemplate;
	} //-- getNamedTemplate
	
	/**
	 * Returns an enumeration of templates that have names
	 * @return an enumeration of templates that have names
	**/
	public Enumeration getNamedTemplates() {
	    return namedTemplates.elements();
	} //-- getNamedTemplates

	/**
	 * Returns the top-level parameter associated with the given name
	 * @param name the name of the top-level parameter to return
	 * @return the top-level parameter associated with the given name
	**/
    public Param getParameter(String name) {
        if ((parameters == null) || (name == null)) return null;
        return (Param)parameters.get(name);
    } //-- getParameter
	
	/**
	 * Returns an Enumeration of the top-level parameters
	 * @return the Enumeration of top-level parameter declarations
	**/
    public Enumeration getParameters() {
        if (parameters == null)
            return _emptyHash.elements();
        return parameters.elements();
    } //-- getParameters
	
	/**
	 * Returns the variable associated with the given name
	 * @param name the name of the variable to return
	 * @return the variable associated with the given name
	**/
    public Variable getVariable(String name) {
        if ((variables == null) || (name == null)) return null;
        return (Variable)variables.get(name);
    } //-- getVariable
    
	/**
	 * Returns an Enumeration of the top-level variables
	 * @return the Enumeration of top-level variable declarations
	**/
    public Enumeration getVariables() {
        if (variables == null) 
            return _emptyHash.elements();
        return variables.elements();
    } //-- getVariables
    
	
	/**
	 * @return the number of templates contained in this stylesheet,
	 * including imported rules
	**/
	public int countTemplates() {
	    int tc = templates.size();
	    for (int i = 0; i < imports.size(); i++) {
	        tc += ((XSLTStylesheet)imports.get(i)).countTemplates();
	    }
	    return tc;
	} //-- countTemplates
	
    /**
     * Returns the list of namespaces
     * @return the list of namespaces as a Hashtable
    **/
    protected Hashtable getNamespaces() {
        return namespaces;
    } //-- getQuotedNamespaces
    
    /**
     * Returns the list of quoted namespaces
     * @return the list of quoted namespaces as a Hashtable
    **/
    protected Hashtable getQuotedNamespaces() {
        return quotedns;
    } //-- getQuotedNamespaces
    
	/**
	 * Returns the namespace that the given namespace argument
	 * quotes
	 * @param namespace the namespace to resolve
	 * @returns the resolved namespace or the given namespace if
	 * it does not quote any other namespaces.
	**/
	public String getQuotedNamespace(String namespace) {
	    if (namespace == null) return null;
	    String qns = (String) quotedns.get(namespace);
	    if (qns != null) {
	        Enumeration enum = namespaces.keys();
    	    String key;
	        while (enum.hasMoreElements()) {
	            key = (String)enum.nextElement();
	            if (qns.equals(namespaces.get(key)))
	                return key;
	        }
	    }
	    return namespace;
	} //-- getQuotedNamespace
	
	public String getResultNamespace() {
	    return getAttribute(Names.RESULT_NS_ATTR);
	} //-- getResultNamespace
	
	public String getResultDocType() {
	    return resultDocType;
	} //-- getResultDocType
	
    /**
     * Retrieves the set of global level scripts for this stylesheet
     * @return a List of XSLScript Objects
    **/
    public List getScripts() {
        return (List) gScripts.clone();
    } //-- getScripts
    
    /**
     * @return an array of all the TemplateRule objects for this 
     * stylesheet, including imported templates
    **/
	public TemplateRule[] getTemplates() {
	    
	    List allTemplates = new List(countTemplates());
	    
	    // Add imported rules
	    for (int i = 0; i < imports.size(); i++) {
	        copyListInto(allTemplates,
	            ((XSLTStylesheet)imports.get(i)).getTemplates());
	    }
	    // add local rules
	    copyListInto(allTemplates,templates);
	    
	    return (TemplateRule[]) allTemplates.toArray(new 
	        TemplateRule[allTemplates.size()]);
	    
	} //-- getTemplates
	
    /**
     * Returns the XSL Namespace Prefix for this XSL Stylesheet
     * @return the XSL Namespace Prefix for this XSL Stylesheet
    **/
    public String getXSLNSPrefix() {
        return xslNSPrefix;
    } //-- getXSLNSPrefix
    
    /**
     * Imports the given XSLStylesheet to this XSLStylesheet.
     * @param xsl the XSLStylesheet to import
     * @exception XSLException
    **/
    public void importFrom(XSLTStylesheet xsl) throws XSLException {
        importFrom(new XSLImport(xsl));
    } //-- importFrom
        
    
    
    /**
     * Imports the XSLStylesheet referenced by the given XSLImport
     * to this stylesheet.
     * @param xslImport the XSLImport referencing the appropriate
     * XSLStylesheet to import.
     * @exception XSLException
    **/
    public void importFrom(XSLImport xslImport) throws XSLException {
        if (xslImport == null) return;
        XSLTStylesheet xsl = xslImport.getStylesheet();
        String importHref = xsl.getHref();
        if (importHref != null) {
            if (isAllowableImport(importHref)) {
                importWithoutVerify(xslImport);
            }
            else {
                throw new XSLException("Stylesheet " + importHref + " has already been directly or indirectly imported.");
            }
        }
    } //-- importFrom
    
    /**
     * Includes the given XSLStylesheet to this XSLStylesheet. <BR>
     * @param xsl the XSLStylesheet to include
     * @see isAllowableInclusion
     * @exception XSLException
    **/
    public void includeFrom(XSLTStylesheet xsl) throws XSLException {
        if (xsl == null) return;
        String includeHref = xsl.getHref();
        if (includeHref != null) {
            if (isAllowableImport(includeHref)) {
                includeWithoutVerify(xsl);
            }
            else {
                throw new XSLException("Stylesheet '" + includeHref + "' has already been directly or indirectly included.");
            }
        }
        else {
            throw new XSLException("Stylesheet missing URI Location.");
        }
    } //-- includeFrom
    
    /**
     * Checks the given filename against this Stylesheet's href and previously 
     * imported stylesheets to determine if the file represented
     * by the given filename can be imported in this stylesheet.
     * @return true if the given filename is allowed to be included in this
     * XSLStylesheet, otherwise false.
    **/
    public boolean isAllowableImport(String filename) {
        if (uriLocation != null) {
            String href = uriLocation.getAbsoluteURI();
            if (filename.equals(href)) return false;
        }
        XSLTStylesheet xsl;
        for (int i = 0; i < imports.size(); i++) {
            xsl = (XSLTStylesheet) imports.get(i);
            if (!xsl.isAllowableImport(filename)) return false;
        }
        return true;
    } //-- isAllowableImport
    
    /**
     * Determines whether or not whitespace stripping is allowed
     * for Elements with the given name
     * @param name the name of the Element 
     * @see preserveSpace
     * @see stripSpace
    **/
    public boolean isStripSpaceAllowed(String name) {
        
        if (preserveSpaceByDefault) {
            return (whitespaceElements.contains(name));
        }
        else return (!whitespaceElements.contains(name));
    } //-- isStripSpaceAllowed
    
    /**
     * Preserves the whitespace of Elements with the given name.
     * By default all ignorable whitespace is removed for all
     * Elements.
     * @param name the name of the Element to preserve whitespace of.
     * @see stripSpace
    **/
    public void preserveSpace(String name) {
        if (name == null) return;
        
        Tokenizer tokenizer = new Tokenizer(name);
        if (!preserveSpaceByDefault) {
            while (tokenizer.hasMoreTokens())
                whitespaceElements.add(tokenizer.nextToken());
        }
        else {
            while (tokenizer.hasMoreTokens())
                whitespaceElements.remove(tokenizer.nextToken());
        }
        
    } //-- preserveSpace
    
    /**
     * @see org.exolab.adaptx.xslt.XSLElement
    **/
    public void setAttribute(String name, String value) 
        throws XSLException
    {
        
        if (name.indexOf(XMLNS_DECL) == 0) {
            String ns = name.substring(XMLNS_DECL.length());
            namespaces.put(ns, value);
            if (value.indexOf(QUOTE) == 0) {
                quotedns.put(ns,value.substring(QUOTE.length()));
            }
            if (value.startsWith(XSLT_NAMESPACE)) xslNSPrefix = ns;
        }
        else {
            //-- intercept old xsl:stylesheet attribute values
            //-- to populate new xsl:output - kv (19991013)
            name = name.intern();
            if (name == Names.DEFAULT_SPACE_ATTR) {
                preserveSpaceByDefault = Names.PRESERVE_VALUE.equals(value);
            }
            else if (name == Names.INDENT_RESULT_ATTR) {
                output.setIndent("yes".equals(value));
            }
            else if (name == Names.RESULT_NS_ATTR) {
                output.setMethod(value);
            }
        }
        super.setAttribute(name,value);
    } //-- setAttribute
    
    /**
     * Sets the URILocation for this stylesheet
     *
     * @param location the URILocation of this stylesheet
    **/
	public void setURILocation(URILocation location) {
	    this.uriLocation = location;
	} //-- setDocumentBase

    /**
     * Removes the ignorable whitespace from Elements with the given name.
     * By default all ignorable whitespace is removed for all
     * Elements. This method should only be called if preserveSpace
     * was previously called with the same name argument.
     * @param name the name of the Element to strip whitespace from
     * @see preserveSpace
    **/
    public void stripSpace(String name) {
        
        if (name == null) return;
        
        Tokenizer tok = new Tokenizer(name);
        if (preserveSpaceByDefault) {
            while (tok.hasMoreTokens())
                whitespaceElements.add(tok.nextToken());
        }
        else {
            while (tok.hasMoreTokens())
                whitespaceElements.remove(tok.nextToken());
        }
            
    } //-- stripSpace

    /**
     * Imports the given XSLStylesheet to this XSLStylesheet.<BR>
     * <B>Note: This method will include the given Stylesheet regardless of 
     * whether or not the Stylesheet has been previously included.</B>
     * @param xsl the XSLStylesheet to import
     * @exception XSLException
     * @see isAllowableInclusion
    **/
    protected void importWithoutVerify(XSLTStylesheet xsl) throws XSLException {
        importWithoutVerify(new XSLImport(xsl));
    } //-- importWithoutVerify
        
    
    
    /**
     * Imports the XSLStylesheet referenced by the given XSLImport
     * to this stylesheet.<BR>
     * <B>Note: This method will import the given Stylesheet regardless of 
     * whether or not the Stylesheet has been previously imported.</B>
     * @param xslImport the XSLImport referencing the appropriate
     * XSLStylesheet to import.
     * @exception XSLException
     * @see isAllowableInclusion
    **/
    protected void importWithoutVerify(XSLImport xslImport) throws XSLException {
        if (xslImport == null) return;
        
        // Make sure we can add the imported stylesheet
        
        if (!allowImports) {
            throw new XSLException("Imports must occur before all other elements of the stylesheet.");
        }
        // Import stylsheet
        XSLTStylesheet xsl = xslImport.getStylesheet();
        if (xsl == null) return;
        imports.add(xsl);
        copyFromXSLStylesheet(xsl);
    } //-- importWithoutVerify
    
    /**
     * Includes the given XSLStylesheet to this XSLStylesheet. <BR>
     * <B>Note: This method will include the given Stylesheet regardless of 
     * whether or not the Stylesheet has been previously included.</B>
     * @param xsl the XSLStylesheet to include
     * @see isAllowableInclusion
     * @exception XSLException
    **/
    protected void includeWithoutVerify(XSLTStylesheet xsl) throws XSLException {
        if (xsl == null) return;
        
        //includes.add(xsl);
        allowImports = false;
        copyFromXSLStylesheet(xsl);
        // addTemplates
        
        TemplateRule[] inclTemplates = xsl.getTemplates();
        for (int i = 0; i < inclTemplates.length; i++)  {
            addTemplate(inclTemplates[i], true);
        }
        
    } //-- includeWithoutVerify
    
    /*
     * ///-- removed for now (kv)
     * Removes the given XSLElement from this Stylesheet.
     * @return the XSLElement removed, or null if not found
     * 
    **
    protected XSLElement removeXSLObject(XSLElement xslElement) {
        if (xslElement == null) return null;
        
        //try { super.removeChild(xslElement); }
        //catch(DOMException domEx) {};
        
        String name;
        switch (xslElement.getType()) {
            case XSLElement.TEMPLATE:
                if (rules.remove((TemplateRule)xslElement)) {
                    return xslElement;
                }
                break;
            case XSLElement.MACRO:
                return (Macro) macros.remove(((Macro)xslElement).getName());
            case XSLElement.ATTRIBUTE_SET:
                if (attributeSets.remove(((AttributeSet)xslElement).getName()) != null) {
                    return xslElement;
                }
                break;
            case XSLElement.CONSTANT:
                name = xslElement.getAttribute(Names.NAME_ATTR);
                if ((name == null) || (name.length() == 0))
                    return null;
                else {
                    String value = (String) constants.remove(name);
                    if (value != null) {
                        return xslElement;
                    }
                }
                break;
            case XSLElement.ID:
                name = xslElement.getAttribute(Names.ATTRIBUTE_ATTR);
                String id = (String) ids.remove(name);
                if (id != null) {
                    return xslElement;
                }
                break;
            case XSLElement.SCRIPT:
                if (gScripts.remove((XSLScript)xslElement)) {
                    return xslElement;
                }
                break;
            case XSLElement.IMPORT:
                break;
            default:
                break;
        } //-- switch
        return null;
        
    } // -- removeXSLElement
    /* */
    
      //-------------------/
     //- Private Methods -/
    //-------------------/
    
	/**
	 * Adds a new top-level parameter to this stylesheet.
	 * The parameter may, or may not have a default value.
	 * @param param the top-level parameter to add
	 * @param imported, a flag indicating that the parameter is
	 * imported from another stylesheet
	 * @exception XSLException when adding a parameter with the same
	 * name as a previously declared parameter, or variable
	**/
    private void addParam(Param param, boolean imported) 
        throws XSLException
    {
        String name = param.getName();
        
        if ((name == null) || (name.length() == 0)) 
            throw new XSLException("missing name for parameter.");
            
        if ((variables != null) && (variables.contains(name))) {
            String err = "A variable with the same name as the "+
                "given parameter already exists : ";
            throw new XSLException(err + name);
        }
        else if ((parameters != null) && (parameters.contains(name))) {
            String err = "A parameter with the same name as the "+
                "given parameter already exists : ";
            throw new XSLException(err + name);
        }
        else {
            if (parameters == null) 
                parameters = new Hashtable(5);
                
            allowImports = false;
            parameters.put(name, param);
        }
        
        if (!imported) super.appendAction(param);
        
    } //-- addParam
    
	/**
	 * Adds a new TemplateRule to the list of rules for this stylesheet.
	 * @param rule the TemplateRule to add
	 * @exception XSLException when a template already exists with the given
	 * name
	**/
    private void addTemplate(TemplateRule template, boolean imported) 
        throws XSLException 
    {                
        //-- handle named templates
        //-- Peter Ciuffetti.  Added for WD-xslt-1990421
        String name = template.getAttribute(Names.NAME_ATTR);
        if (name != null) {
            if (namedTemplates.get(name) != null) {
                throw new XSLException("Duplicate template name found: " +
                    name);
            }
            namedTemplates.put(name, template);
        }
        //-- end of named template code
        
        templates.add(template);
        allowImports = false;
        if (!imported) super.appendAction(template);
    } //-- addTemplate
    
	/**
	 * Adds the variable to the current set of variables
	 * for this stylesheet.
	 * @param variable the variable to add
	 * @param imported, a flag indicating that the variable is
	 * imported from another stylesheet
	 * @exception XSLException when adding a variable with the same
	 * name as a previously declared variable
	**/
    private void addVariable(Variable variable, boolean imported) 
        throws XSLException
    {
        String name = variable.getName();
        
        if ((name == null) || (name.length() == 0)) 
            throw new XSLException("missing name for variable.");
            
        
        if ((variables != null) && (variables.contains(name))) {
            String err = "A variable with the same name as the "+
                "given variable already exists : ";
            throw new XSLException(err + name);
        }
        else if ((parameters != null) && (parameters.contains(name))) {
            String err = "A parameter with the same name as the "+
                "given variable already exists : ";
            throw new XSLException(err + name);
        }
        else {
            if (variables == null) variables = new Hashtable();
            allowImports = false;
            variables.put(name, variable);
        }
        
        if (!imported) super.appendAction(variable);
    } //-- addVariable
    
    /**
     * Copies the keys and associated values from the source Hashtable to
     * the target Hashtable. 
     * @param target the Hashtable to copy values into
     * @param source the Hashtable to copy values from
     * @param overwrite a boolean indicating that the source objects
     * can overwrite target Objects if a collision occurs
    **/
    private void copyHashtableInto
        (Hashtable target, Hashtable source, boolean overwrite) 
    {
        if ((target == null) || (source == null)) return;
        Object key;
        Enumeration keys = source.keys();
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            if ((overwrite) || (target.get(key) == null))
                target.put(key,source.get(key));
        }

    } //-- copyHashtableInto

    
    /**
     * Copies the elements from the source Array to
     * the target List.
    **/
    private void copyListInto(List target, Object[] source) {
        if ((target == null) || (source == null)) return;
        for (int i = 0; i < source.length; i++) {
            target.add(source[i]);
        }

    } //-- copyListInto
    
    /**
     * Copies the elements from the source List to
     * the target List.
    **/
    private void copyListInto(List target, List source) {
        if ((target == null) || (source == null)) return;
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    } //-- copyListInto
    
    
    /**
     * Copies all related elements from the given
     * XSLStylesheet to this XSLStylesheet. 
     * <BR><B>Note: template rules are not copied</B>
     * @param the XSLStylesheet to copy elements from
     * @see importWithoutVerify
    **/
    
    private void copyFromXSLStylesheet(XSLTStylesheet xsl) {
        
        //-- This method does not copy Rules at this time, it will
        //-- be done by a call to getTemplates.
        
        // combine attribute sets
        copyHashtableInto(attributeSets,xsl.getAttributeSets(), true);
        
        
        // combine global variables and parameters
        try {
            Enumeration enum = xsl.getVariables();
            while (enum.hasMoreElements()) {
                Variable v = (Variable) enum.nextElement();
                Variable tmp = getVariable(v.getName());
                if (tmp != null) variables.remove(tmp);
                addVariable(v, true);
            }
            enum = xsl.getParameters();
            while (enum.hasMoreElements()) {
                Param p = (Param) enum.nextElement();
                Param tmp = getParameter(p.getName());
                if (tmp != null) parameters.remove(tmp);
                addParam(p);
            }
        }
        catch(XSLException xslx) {};
        
        // combine outputs
        mergeOutput(xsl.getOutput());
        
        // combine ids
        copyHashtableInto(ids,xsl.getIds(), true);
        
        // combine namespace information
        copyHashtableInto(namespaces,xsl.getNamespaces(), false);
        copyHashtableInto(quotedns,xsl.getQuotedNamespaces(),false);
        
        // addScripts
        copyListInto(gScripts,xsl.getScripts());
        
    } //-- method: copyFromXSLStylesheet
    
    /**
     * Merges the given XSLOutput with this stylesheet's XSLOutput
     * 
     * @param xslOutput the XSLOutput to merge
    **/
    private void mergeOutput(XSLOutput xslOutput) {
        //-- merge output objects
        String property = null;
                    
        //-- indent
        if (xslOutput.getAttribute(Names.INDENT_ATTR) != null)
            output.setIndent(xslOutput.getIndent());
                    
        //-- method
        property = xslOutput.getMethod();
        if (property != null) output.setMethod(xslOutput.getMethod());
                    
        //-- PublicId
        property = xslOutput.getDoctypePublicId();
        if (property != null) output.setDoctypePublicId(property);
                    
        //-- SystemId
        property = xslOutput.getDoctypeSystemId();
        if (property != null) output.setDoctypeSystemId(property);
                    
        //-- Version
        property = xslOutput.getVersion();
        if (property != null) output.setVersion(property);
    } //-- method: mergeOutput
    
    /* */
} //-- XSLStylesheet
