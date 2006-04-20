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
package org.exolab.adaptx.xslt;

import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.net.URIResolver;
import org.exolab.adaptx.util.*;
import org.exolab.adaptx.xml.*;
import org.exolab.adaptx.xml.parser.DOMParser;
import org.exolab.adaptx.xpath.*;
import org.exolab.adaptx.xslt.dom.Root;
import org.exolab.adaptx.xslt.functions.*;

import org.exolab.adaptx.xslt.util.ResultFormatter;
import org.exolab.adaptx.xslt.util.ScopedVariableSet;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.xml.sax.*;

/**
 * The current RuleProcessor environment.
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ProcessorState extends XPathContext {

    private String DEFAULT_SCRIPT_HANDLER 
        = "org.exolab.adaptx.scripting.ECMAScriptHandler";
    private String DEFAULT_LANGUAGE = ECMASCRIPT;
    
    public static final String ECMASCRIPT = "ECMASCRIPT";
    public static final String JPYTHON    = "JPYTHON";
    
    /**
     * the name of the result tree document element,
     * if one is missing
    **/
    private static final String RESULT_NAME = "xslp:result";
    private static final String RESULT_NS_ATTR = "xmlns:xslp";
    private static final String RESULT_NS =
        "http://xslp.kvisco.com/xslp/1.0";
    
    private static final String NO_DOC_ELEMENT = 
        "xslp:result has been added, because nodes were being "+
        "added to the result tree, but no document element " +
        "was present. XSLT result tree's must be well-formed. ";
        
    private static final String MULITPLE_DOC_ELEMENTS = 
        "xslp:result has been added, because an element was being "+
        "added to the result tree at the document level, but a " +
        "document element already existed. " +
        "XSLT result tree's must be well-formed. ";
        
        
    private static final String NO_NAMESPACE_ERR =
        "No namespace has been defined for the prefix: ";
        
    
      //----------------------/
     //- Instance Variables -/
    //----------------------/

    /**
     * Stack for called Templates
    **/
    private QuickStack calledTemplates   = null;
    
    /**
     * defined constants
    **/
    private Hashtable constants          = null;
    
    /**
     * ProcessorCallback for calling back into the RuleProcessor
    **/
    private ProcessorCallback processorCallback = null;
    
    private HashMap   avtCache          = null;
    
    /**
     * The source tree being processed
    **/
    private XPathNode _source          = null;
    
    private XSLTStylesheet stylesheet    = null;
    
    private VariableSet globalVars      = null;
    private List        cyclicVarCheck  = null;
    
    /**
     * The set of template parameter bindings
    **/
    private ScopedVariableSet _parameters      = null;
    
    /**
     * The set of variable bindings
    **/
    private ScopedVariableSet _variables       = null;
    
    private QuickStack  modes           = null;
    private QuickStack  xmlSpaceModes   = null;
    private QuickStack  handlers        = null;
    private QuickStack  nodeSetStack    = null;
    private QuickStack  nodes           = null;
    private QuickStack  _contextStack   = null;
    
    private List        actions         = null;
    
    private HashMap     namespaces      = null;
    
    private List        fnResolvers     = null;
    
    IDIndexer          _idIndexer       = null;
    
    /** 
     * a flag indicating whether debugging is enabled
    **/
    private boolean _debug              = false;
    
    
    private URIResolver _uriResolver    = null;
    
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new ProcessorState
    **/
    protected ProcessorState 
        (RuleProcessor ruleProcessor, XPathNode source,
         XSLTStylesheet xslStylesheet, ResultHandler handler)
    {
        super(source);
        
        if (handler == null) {
            String err = "The ResultHandler passed as an argument to the " +
                " constructor of ProcessorState may not be null.";
            throw new IllegalArgumentException(err);
        }
        
        this._uriResolver = ruleProcessor.getURIResolver();
        
        this.processorCallback = new ProcessorCallback(ruleProcessor, this);
        
        _source = source;
        
        this.stylesheet = xslStylesheet;
        
        //-- since I use the VariableSetPool for both parameters
        //-- and variables I am doubling the initial size
        actions           = new List();
        avtCache          = new HashMap(1000);
        calledTemplates   = new QuickStack();
        _contextStack     = new QuickStack();
        _idIndexer        = new IDIndexer();
        handlers        = new QuickStack();
        namespaces        = new HashMap();
        nodeSetStack      = new QuickStack();
        nodes             = new QuickStack();
        xmlSpaceModes     = new QuickStack();
        cyclicVarCheck    = new List();
        _variables        = new ScopedVariableSet();
        _parameters       = new ScopedVariableSet();
        
        //-- copy Stylesheet Ids
        Hashtable idAtts = xslStylesheet.getIds();
        synchronized (idAtts) {
            Enumeration idAttrNames = idAtts.keys();
            while (idAttrNames.hasMoreElements()) {
                String name = (String)idAttrNames.nextElement();
                String type = (String)idAtts.get(name);
                _idIndexer.addIdAttribute(name, type);
            }
        }
        //-- add handler to ResultHandler stack
        pushHandler(handler);
        
        //-- keep handle to global variables;
        globalVars = _variables.current();
        
        //-- add default xml:space mode
        xmlSpaceModes.push(Names.DEFAULT_VALUE);
        
        //-- add XSLT namespace
        String prefix = xslStylesheet.getXSLNSPrefix();
        if (prefix == null) prefix = "";
        namespaces.put(prefix, XSLTStylesheet.XSLT_NAMESPACE);

    } //-- ProcessorState

      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Associates a namespace with the given prefix,
     * @param nsPrefix the namespace prefix
     * @param nsURI the namespace URI to associate
    **/
    public void associateNamespace(String nsPrefix, String nsURI) {
        if ((nsPrefix == null) || (nsURI == null)) return;
        namespaces.put(nsPrefix, nsURI);
    } //-- associateNamespace
    
    /**
     * Creates a unique identifier for the given node
     * @return the String that is a unique identifier for the
     * given node
    **/
    public String generateId(XPathNode node) {
        StringBuffer genId = new StringBuffer("id");
        genId.append(System.identityHashCode(node.getRootNode()));
        
        if (node.getNodeType() == XPathNode.ROOT)
            return genId.toString();
            
        /*
        int[] order = getDocumentOrder(node);
        //-- skip first node, since it's the document node
        for (int i = 1; i < order.length; i++) {
            genId.append('_');
            genId.append(order[i]);
        }
        */
        
        return genId.toString();
    } //-- generateId
    
    /**
     * Returns the "current" node 
     * The current node, is different than the context node, as
     * the context node may change during the evaluation of an
     * xpath expression, the current node does not. Basically 
     * it's the first context node during the beginning of
     * an xpath evalation.
     *
     * @return the "current" node
     * @see popCurrentNode
     * @see pushCurrentNode
    **/
    public XPathNode getCurrentNode() {
        if (nodes.empty()) return null;
        return (XPathNode) nodes.peek();
    } //-- getCurrentNode
    
    /**
     * Returns the ErrorObserver to report errors to
     * @return the ErrorObserver to report errors to
    **/
    public ErrorObserver getErrorObserver() {
        return processorCallback;
    } //-- getErrorObserver
    
    public URILocation getStylesheetLocation() {
        return stylesheet.getURILocation();
    } //-- getStylesheetHref()
    
    
    /**
     * Returns the current set of attributes being processed
     * @return the current set of attributes being processed
    **/
    //AttributeListImpl getAttributes() {
    //    if (atts == null) {
    //        atts = new AttributeListImpl();
    //    }
    //    return atts;
    //} //-- getAttributes
    
    /**
     * Returns the current ResultFormatter being used to construct
     * the result tree.
     *
     * @return the ResultFormatter used for processing the result tree.
    **/
    ResultFormatter getResultFormatter() {
        if (handlers.empty()) return null;
        return (ResultFormatter) handlers.peek();
    } //-- getResultFormatter
    
    
    /**
     * Returns the parameter value associated with the given name.
     * @param name the name of the parameter to retrieve the value of
     * @return the parameter value associated with the given name.
    **/
    public String getParameter(String name) {
        return processorCallback.getParameter(name);
    } //-- getParameter
    
    
    /**
     * Returns the Property value associated with the given name.
     * All property names without a namespace are defaulted to the
     * System evironment scope.
     * @param name the name of the property to get the value of
     * @return the Property value associated with the given name.
    **/
    public String getProperty(String name) {
        if (name == null) return "";
        
        String ns = XMLUtil.getNameSpacePrefix(name);
        String lp = XMLUtil.getLocalPart(name);
        
        String value = null;
        
        
        //-- Environment Property
        if (ns.length() == 0) {
            value = System.getProperty(lp, "");
        }
        //-- XSL property
        else if (ns.equals(stylesheet.getXSLNSPrefix())) {
            value = processorCallback.getProperty(lp);
        }
        //-- user defined?
        else {
            value = "";
        }
        return value;
    } //-- getProperty
    
    /**
     * Returns the stack of XML space modes
    **/
    public QuickStack getXMLSpaceModes() {
        return xmlSpaceModes;
    } //-- getXMLSpaceModes
    
    /**
     * Returns the namespace associated with the given prefix.
     * A namespace can be associated with a given prefix via
     * the XSLT document being processed, or using #associateNamespace
     * method 
     * @param prefix the prefix of the namespace to return
     * @return the namespace prefix or null if none found.
    **/
    public String getNamespaceURI(String prefix) {
        String ns = null;
        for (int i = actions.size()-1; i >= 0; i--) {
            XSLObject action = (XSLObject) actions.get(i);
            ns = action.resolveNamespace(prefix);
            if (ns != null) break;
        }
        return ns;
    } //-- getNamespace
    
    /**
     * Returns the URIResolver for resolving all URIs.
     *
     * @return the URIResolver for resolving all URIs.
    **/
    public URIResolver getURIResolver() {
        return _uriResolver;
    } //-- setURIResolver
    
    
    /**
     * Returns true if a function with the given name exists
     * within the given namespace.
     *
     * @return true if a function with the given name exists
    **/
    public boolean isFunctionAvailable(String name, String namespace) {
        System.out.println("#isFunctionAvailable");
        return false;
    } //-- isFunctionAvailable
    
    /**
     * Determines if the given prefix or URI maps to the XSLT namespace
     * @param prefixOrURI the prefix or URI to compare with the 
     * XSLT namespace
     * @return true if the given prefix or URI maps to the XSLT namespace
    **/
    public boolean isXSLTNamespace(String prefixOrURI) {
        
        if (prefixOrURI == null) return false;
        
        String ns = prefixOrURI;
        String tmp = ns;
        while ((tmp = (String)namespaces.get(ns)) != null) {
            ns = tmp;
        }
        
        return stylesheet.XSLT_NAMESPACE.equals(ns);

    } //-- isXSLTNamespace
    
    /**
     * Removes and returns the ResultHandler from the top of the
     * ResultHandler stack.
     *
     * @return the ResultHandler that was at the top of the stack.
    **/
    public ResultHandler popHandler() {
        if (handlers.empty()) return null;
        ResultFormatter rf = (ResultFormatter) handlers.pop();
        rf.flush();
        return rf.getResultHandler();
    } //-- popHandler
    
    /**
     * Pushes a new ResultHandler to the top of the ResultHandler stack.
     *
     * @param handler the ResultHandler to push to the top of the stack.
    **/
    public void pushHandler(ResultHandler handler) {
        ResultFormatter rf = new ResultFormatter(handler);
        rf.addErrorObserver(processorCallback);
        handlers.push(rf);
    } //-- pushHandler(ResultHandler);
    
    /**
     * Removes the current node from the top of the stack
     * The current node, is different than the context node, as
     * the context node may change during the evaluation of an
     * xpath expression, the current node does not. Basically 
     * it's the first context node during the beginning of
     * an xpath evalation.
     *
     * @return the current node.
     * @see pushCurrentNode
     * @see getCurrentNode
    **/
    public XPathNode popCurrentNode() {
        if (nodes.empty()) return null;
        return (XPathNode) nodes.pop();
    }
    
    /**
     * Adds the given node to the top of the "current" node Stack.
     * The current node, is different than the context node, as
     * the context node may change during the evaluation of an
     * xpath expression, the current node does not. Basically 
     * it's the first context node during the beginning of
     * an xpath evalation.
     *
     * @param node the new current node
     * @see popCurrentNode
     * @see getCurrentNode
    **/
    public void pushCurrentNode(XPathNode node) {
        nodes.push(node);
    } //-- pushCurrentNode
    
    
    /**
     * Pushes the given nodeSet onto the context stack
     *
     * @param nodeSet the nodeSet ot push onto the stack
    **/
    public void pushNodeSet(NodeSet nodeSet) {
        XPathContext xpc = newContext(nodeSet, 0);
        _contextStack.push(xpc);
    } //-- pushNodeSet
    
    /**
     * Removes and returns the current NodeSet from the context stack
     *
     * @return the current NodeSet from the context stack
    **/
    public NodeSet popNodeSet() {
        if (_contextStack.empty()) return null;
        XPathContext xpc = (XPathContext)_contextStack.pop();
        return xpc.getNodeSet();
    } //-- popNodeSet
    
    
      //---------------------/
     //- Protected Methods -/
    //---------------------/
    
    /**
	 * Returns the value of the given String as an AttributeValueTemplate
	 * @returns the value of the given String as an AttributeValueTemplate
	 * @exception InvalidExprException when the String argument is not a valid 
	 * AttrubueValueTemplate
	**/
    public AttributeValueTemplate getAttributeValueTemplate(String avtString) 
        throws XPathException
    {
        
        AttributeValueTemplate avt = null;
        
        if (avtString != null) {
            // look in cache first
            avt = (AttributeValueTemplate) avtCache.get(avtString);
            if (avt == null) {
                avt = new AttributeValueTemplate(avtString);
                // add to cache for performace
                avtCache.put(avtString, avt);
            }
        }
       return avt;
    } //-- getAttributeValueTemplate

    /**
     * Returns the XPathFunction with the given name and belonging
     * to the given namespace.
     *
     * @param namespace the namespace of the function
     * @param name the name of the function
     * @return the XPathFunction with the given name 
    **/
    public XPathFunction getFunction(String name) {
        return getFunction(null, name);
    } //-- getFunction
        
    public ScopedVariableSet getParameters() {
        return _parameters;
    } //-- getParameters
    

    //-----------------------------/
    //- Methods from XPathContext -/
    //-----------------------------/
    
    /**
     * Returns the context node of this XPathContext
     *
     * @return the context node
    **/
    public XPathNode getNode() {
        if (_contextStack.empty()) {
            return super.getNode();
        }
        else {
            return ((XPathContext)_contextStack.peek()).getNode();
        }
    } //-- getNode

    /**
     * Returns the position of the context node. The position is
     * a value between zero and the context size minus one.
     * One must be added in order to obtain a value XPath position.
     *
     * @return The position of the context node
     * @see #getSize
     */
    public int getPosition() {
        if (_contextStack.empty()) {
            return super.getPosition();
        }
        else {
            return ((XPathContext)_contextStack.peek()).getPosition();
        }
    } //-- getPosition


    /**
     * Returns the size of the context.
     *
     * @return The size of the context
     */
    public int getSize()
    {
        if (_contextStack.empty()) {
            return super.getSize();
        }
        else {
            return ((XPathContext)_contextStack.peek()).getSize();
        }
    } //-- getSize

    /**
     * Returns the current context node-set.
     *
     * @return The current context node-set
    **/
    public NodeSet getNodeSet() {
        if (_contextStack.empty()) {
            return super.getNodeSet();
        }
        else {
            return ((XPathContext)_contextStack.peek()).getNodeSet();
        }
    } //-- getNodeSet


    /**
     * Sets the given node-set as the context node-set for
     * this XPathContext
     *
     * @param nodeSet the node-set to use as the context node-set.
     * @param position the position of the context node
    **/
    public void setNodeSet( NodeSet nodeSet, int position )
    {
        if (_contextStack.empty()) {
            super.setNodeSet(nodeSet, position);
        }
        else {
            XPathContext xpc = (XPathContext)_contextStack.peek();
            xpc.setNodeSet(nodeSet, position);
        }
    } //-- setNodeSet
    
    /**
     * Returns the document URI of the given XPathNode,
     * or null if it's not found
     */
    public String getDocumentURI(XPathNode node) {
        if (node == null) return null;
        XPathNode root = node.getRootNode();
        if (root != null) {
            if (root instanceof Root) {
                return ((Root)root).getDocumentURI();
            }
        }
        return null;
    } //-- getDocumentURI
    

    /**
     * Returns the document order of the given node.
     *
     * @return The document order of the given node
    **/
    public int[] getDocumentOrder( XPathNode node )
    {
        int[] order = null;
        
        if (node.getNodeType() == XPathNode.ROOT) {
            order = new int[1];
            order[1] = 0;
            return order;
        }
        
        
        //-- calculate array size
        int size = 1;
        XPathNode parent = node.getParentNode();
        while ((parent != null) && (parent.getNodeType() != XPathNode.ROOT)) {
            ++size;
            parent = parent.getParentNode();
        }
        
        order = new int[size];
        
        //-- calculate order
        parent = node;        
        if (node.getNodeType() == XPathNode.ATTRIBUTE) {
            --size;
            order[size] = 0;
            parent = node.getParentNode();
        }
        
        while ((parent != null) && (parent.getNodeType() != XPathNode.ROOT)) {
            int childNumber = 1;
            XPathNode sibling = parent.getPrevious();
            while (sibling != null) {
                ++childNumber;
                sibling = sibling.getPrevious();
            }
            --size;
            order[size] = childNumber;
            parent = parent.getParentNode();
        }
        return order;
        
    } //-- getDocumentOrder

    
    /**
     * Returns the element associated with the given identifier.
     * Locates the element underneath the specified root node.
     *
     * @param root The root node
     * @param id The element's identifier
     * @return The first element in document order with the given
     * identifier, or null if no such element was found
    **/
    public XPathNode getElementById( XPathNode root, String id ) {
        return _idIndexer.getElementById(root, id);
    } //-- getElementById

    
    /**
     * Returns the XPath result bound to the given variable name.
     * Returns null if the variable was not set.
     *
     * @param name The variable name
     * @return The variable's value
    **/
    public XPathResult getVariable( String name )
    {
        
        XPathResult result = null;
        //-- check scoped variable set
        result = _variables.getVariable(name);
        
        if (result == null) {
            //-- check for unbound global definition
            Variable variable = stylesheet.getVariable(name);
            
            //-- check for unbound global param
            if (variable == null) variable = stylesheet.getParameter(name);
            
            if (variable != null) {
                if (!cyclicVarCheck.contains(name)) {
                    cyclicVarCheck.add(name);
                    result = processorCallback.processVariable(variable,this);
                    cyclicVarCheck.remove(name);
                }
                else {
                    String err = "#cyclic variable definition: " + name;
                    result = new StringResult(err);
                }
            }
        }
        return result;
        
    } //-- getVariable
    
    /**
     * Returns the XPathFunction with the given name and belonging
     * to the given namespace.
     *
     * @param namespace the namespace of the function
     * @param name the name of the function
     * @return the XPathFunction with the given name 
    **/
    public XPathFunction getFunction( String namespace, String name) {
        
        //-- make sure name is not null
        if (name == null) return null;
        
        //-- name cleanup
        int idx = name.indexOf(':'); 
        String localName = name;
        if (idx >= 0) {
            String prefix = name.substring(0, idx);
            localName = name.substring(idx+1);
            if (namespace == null)
                namespace = getNamespaceURI(prefix);
        }
        
        if (namespace == null) {
            if (idx < 0) {
                namespace = stylesheet.XSLT_NAMESPACE;
            }
            else {
                String prefix = name.substring(0, idx);
                processorCallback.receiveError(NO_NAMESPACE_ERR + prefix);
            }
        }
        //-- Handle XSLT built-in functions
        if (stylesheet.XSLT_NAMESPACE.equals(namespace)) {
            
            if (Names.GENERATE_ID_FN.equals(localName))
                return new GenerateIDFunctionCall(this);
            else if (Names.CURRENT_FN.equals(localName))
                return new CurrentFunctionCall(this);
            else if (Names.DOCUMENT_FN.equals(localName))
                return new DocumentFunctionCall(this);
            else if (Names.DOC_FN.equals(localName))
                return new DocumentFunctionCall(this);
            else if (Names.SYSTEM_PROPERTY_FN.equals(localName))
                return new SystemFunctionCall(this);
            else if (Names.FUNCTION_AVAILABLE_FN.equals(localName))
                return new SystemFunctionCall(this, 
                                    SystemFunctionCall.FUNCTION_AVAILABLE);
            else {
                String error = localName + "() is either not supported, or " +
                    "an invalid function call.";
                 ////throw new XPathException(error);
                getErrorObserver().receiveError(error);
                return null;
            }
        }
        //-- check namespace prefix for XSL:P specific 
        //-- extensions
        else if (Names.XSLP_EXTENSION_NAMESPACE.equals(namespace)) {
            if (Names.RTF_2_NODESET_FN.equals(localName))
                return new RTF2NodeSetFunctionCall(this);
            return new ExtensionFunctionCall(localName, this);
        }
        else {
            //-- Look for a FunctionResolver for the
            //-- namespace
            XPathFunction fn = null;
            if (fnResolvers != null) {
                for (int i = 0; i < fnResolvers.size(); i++) {
                    FunctionResolver fnResolver = 
                        (FunctionResolver)fnResolvers.get(i);
                    fn = fnResolver.resolveFunction(namespace, localName);
                    if (fn != null) return fn;
                }
            }
        }
        return new ExtensionFunctionCall(localName, this);
    } //-- getFunction

    /**
     * Sets the position of the context node within the context
     * node-set
     *
     * @param the position of the context node within the
     * context node-set
     * @exception IndexOutOfBoundsException when the position
     * is not within the bounds of the context node-set.
    **/
    public void setPosition(int position) {
        if (_contextStack.empty()) {
            super.setPosition(position);
        }
        else {
            ((XPathContext)_contextStack.peek()).setPosition(position);
        }
    } //-- setPosition
    


    //---------------------/
    //- Protected Methods -/
    //---------------------/
    
    protected void freeParameterScope() {
        
        _parameters.remove();
        
    } //-- freeParameterScope

    protected void freeVariableScope() {
        
        _variables.remove();
        
    } //-- freeVariableScope
    
    protected VariableSet newParameterScope() {
        
        _parameters.add();
        return _parameters.current();
        
    } //-- newParameterScope
    
    protected VariableSet newVariableScope() {
        
        _variables.add();
        return _variables.current();
    } //-- newVariableScope
    
    protected ScopedVariableSet getVariables() {
        return _variables;
    } //-- getVariables
    
    /**
     * Returns the current called templates stack being used by 
     * this ProcessorState
     * @return the current called templates stack being used by 
     * this ProcessorState
    **/
    protected QuickStack getInvocationStack() {
        return this.calledTemplates;
    } //-- getInvocationStack    
    
    /**
     * Sets the current action being processed
     * @param xslObject the XSLObject currently being processed
    **/
    protected void pushAction(XSLObject xslObject) {
        actions.add(xslObject);
    } //-- pushAction
    
    /**
     * Removes and returns the XSLObject from the top
     * of the current XSL actions being processed
     *
     * @return the XSLObject removed from the top of the stack
    **/
    protected XSLObject popAction() {
        return (XSLObject)actions.remove(actions.size()-1);
    } //-- popAction
    
    /**
     * Sets the list of FunctionResolvers
     * @param fnResolvers the set of FunctionResolvers
    **/
    protected void setFunctionResolvers(List fnResolvers) {
        this.fnResolvers = fnResolvers;
    } //-- setFunctionResolvers
               
} //-- ProcessorState
