/*
 * (C) Copyright Keith Visco 1998-2003.  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * except in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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

import org.exolab.adaptx.info.AppInfo;
import org.exolab.adaptx.net.URIResolver;
import org.exolab.adaptx.net.impl.URIResolverImpl;
import org.exolab.adaptx.xpath.*;
import org.exolab.adaptx.xml.*;
import org.exolab.adaptx.xml.parser.DOMParser;

import org.exolab.adaptx.xslt.util.*;
import org.exolab.adaptx.xslt.dom.XPNBuilder;

import org.exolab.adaptx.util.*;
import org.mitre.tjt.xsl.XslNumberFormat;

//-- 3rd Party Classes
import org.xml.sax.AttributeList;

//-- JDK Classes
import java.io.PrintWriter;
import java.io.Writer;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This class is the "template" rule processor which processes an XML document 
 * using it's XSLStylesheet. It's primary method #process starts with the 
 * "document" rule ("/"), specified or default, and runs through the XML 
 * Source tree processing all rules until there is nothing left to process. 
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class RuleProcessor extends ErrorObserverAdapter {

    /**
     * The XSL version property name
    **/
    public static final String XSL_VERSION = "version";

    /**
     * The XSL vendor property name
    **/
    public static final String XSL_VENDOR = "vendor";

    /**
     * The XSL vendor-url property name
    **/
    public static final String XSL_VENDOR_URL = "vendor-url";
    
    /**
     * Rule Conflict error message
    **/
    private static final String RULE_CONFLICT 
        = "There is a rule conflict between the following two " + 
          "template match expressions: ";

    /**
     * Endless loop warning
    **/
    private static final String ENDLESS_LOOP_WARNING
        = "A possible endless loop has been encountered during " +
          "processing of template: ";
         
    private static final String NS_DEFAULT_DECL = "xmlns";
    private static final String NS_DECL_PREFIX  = "xmlns:";
    private static final String NAMESPACE_ATTR  = "namespace";
    
    
    /**
     * the namespace separator
    **/
    private final String NS_SEP = ":";

        
    /** 
     * The stylesheet that this RuleProcessor will process
    **/
    private XSLTStylesheet stylesheet     = null;
    
    /**
     * Handle to all the rules of the stylesheet
    **/
    private TemplateRule[] allRules      = null;
    
    /**
     * The default rule
    **/
    private TemplateRule defaultRule     = null;
    
    /**
     * The default action of the default rule
    **/
    private Selection selectChildren     = null;
    
    
    private Properties xslSysProps         = null;
    
    /**
     * parameters which were passed in to this RuleProcessor
     * and are accessible via the XSLT stylesheet
    **/
    private Properties params              = null;
    
    /**
     * The list of message observers
    **/
    List msgObservers = null;
    
    
    /**
     * The list of Extension Function Resolvers
    **/
    List fnResolvers = null;
    
    private URIResolver _uriResolver = null;
    
    private XPathParser _parser = new XPathParser();
    
    /**
     * Create a RuleProcessor for the given XSL stylesheet
     * @param xsl the XSLStylesheet to process
     * @param domParser the DOMParser to use when creating
     * the result tree
    **/
	public RuleProcessor(XSLTStylesheet xsl) {
	    super();
	    this.stylesheet = xsl;
	    
	    //errMsgBuffer = new StringBuffer();
	    msgObservers = new List(3);
	    fnResolvers  = new List(3);
	    
	    // Create DefaultRule
	    defaultRule = new TemplateRule();
	    selectChildren = new Selection(XSLObject.APPLY_TEMPLATES);
	    defaultRule.appendAction(selectChildren);
	    defaultRule.setParent(xsl);
	    
	    //-- set xsl propertites
	    xslSysProps = new Properties();
	    xslSysProps.put(XSL_VERSION, AppInfo.VERSION);
	    xslSysProps.put(XSL_VENDOR, AppInfo.VENDOR);
	    xslSysProps.put(XSL_VENDOR_URL, AppInfo.VENDOR_URL);
	    
	    params = new Properties();
	    
        // assign rules
        allRules = xsl.getTemplates();
        
        _parser.setUseErrorExpr(true);
        
    } //-- RuleProcessor
    
    /**
     * Adds the given FunctionResolver used for resovling
     * extension functions.
     * @param fnResolver the FunctionResolver to add
     * @see org.exolab.adaptx.xpath.FunctionResolver
    **/
    public void addFunctionResolver(FunctionResolver fnResolver) {
        if (fnResolver != null) {
            fnResolvers.add(fnResolver);
        }
    } //-- addFunctionResolver
    
    /**
     * Adds the given MessageObserver to this processors list
     * of MessageObservers
     * @param msgObserver the MessageObserver to add to this processors
     * list of MessageObservers
    **/
    public void addMessageObserver(MessageObserver msgObserver) {
        msgObservers.add(msgObserver);
    } //-- addMessageObserver
    
    /**
     * Returns the parameter value associated with the given name.
     * A call to #setParameter will associate a parameter
     * @param name the name of the parameter to retrieve the value of
     * @return the parameter value associated with the given name.
    **/
    public String getParameter(String name) {
        if (name == null) return null;
        return params.getProperty(name);
    } //-- getParameter
    
    /**
     * Returns the property value that is associated with the given name.
     * @param name the name of the property to retrieve the value of
     * @return the property value that is associated with the given name.
    **/
    public String getProperty(String name) {
        return xslSysProps.getProperty(name, "");
    } //-- getProperty
    
    /**
     * Returns the URIResolver for resolving all URIs.
     *
     * @return the URIResolver for resolving all URIs.
    **/
    public URIResolver getURIResolver() {
        if (_uriResolver == null)
            _uriResolver = new URIResolverImpl();
        return _uriResolver;
    } //-- setURIResolver
    
    /**
     * Processes the given XML Document using this processors stylesheet.
     *
     * @param source the XPathNode to process
     * @param handler the ResultHandler for the result tree
     * @return the result tree as a DOM NodeList
    **/
    public void process(XPathNode source, ResultHandler handler) {
        
        if (source == null) {
            receiveError("null source node, nothing to do.");
            return;
        }
        
        if (handler == null) {
            receiveError("null ResultHandler, it's pointless to proceed.");
            return;
        }
        
        //-- Performance Testing Setup
        /* *
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        System.out.println("Free Memory: " + rt.freeMemory());
        long stime = System.currentTimeMillis();
        /* */
        
        // initialize...
        ProcessorState ps 
            = new ProcessorState(this, source, stylesheet, handler);
            
        ps.setFunctionResolvers(fnResolvers);
        
        ResultFormatter rf = ps.getResultFormatter();
            
        Enumeration enum = null;
        //-- copy top-level declared namespaces
        /*
        Hashtable namespaces = stylesheet.getNamespaces();
        enum = namespaces.keys();
        while (enum.hasMoreElements()) {
            String prefix = (String)enum.nextElement();
            if (ps.isXSLTNamespace(prefix)) continue;
            String uri    = (String)namespaces.get(prefix);
            rf.declareNamespace(prefix, uri);
        }
        */
            
        // Process global scripts
        //-- no longer supported
        /*
        List scripts = stylesheet.getScripts();
        Object obj;
        ScriptHandler scriptHandler = null;
        String language = null;
        String scriptNS = null;
        XSLScript xslScript;
        for (int i = 0; i < scripts.size(); i++) {
            xslScript = (XSLScript)scripts.get(i);
            language = xslScript.getAttribute(Names.LANGUAGE_ATTR);
            scriptHandler = ps.getScriptHandler(language);
            if (scriptHandler != null) {
                
                //-- create namespace
                scriptNS = xslScript.getAttribute(Names.NS_ATTR);
                if (scriptNS != null) 
                    scriptHandler.createNamespace(scriptNS);
                    
                //-- evaluate script
                obj = scriptHandler.eval(xslScript, xmlDocument, scriptNS);
                if (obj != null) receiveError(obj.toString());
            }
        }
        */
        
        // Process top-level parameters
        ScopedVariableSet vars = ps.getVariables();
        enum = stylesheet.getParameters();
        while (enum.hasMoreElements()) {
            Param param = (Param)enum.nextElement();
            //-- first check passed in parameters
            String value = getParameter(param.getName());
            if (value != null) {
                vars.setVariable(param.getName(), new StringResult(value));
            }
            else {
                //-- process as a variable
                XPathResult value2 = processVariable(param, ps);
                if (value2 != null) {
                    vars.setVariable(param.getName(), value2);
                }
            }
        }
        
        //-- Process top-level variables
        enum = stylesheet.getVariables();
        while (enum.hasMoreElements()) {
            Variable variable = (Variable)enum.nextElement();
            XPathResult value = processVariable(variable, ps);
            if (value != null) {
                vars.setVariable(variable.getName(), value);
            }
        }
        
        
        
    	// Process the Root rule
    	TemplateRule rootRule = null;
    	try {
    	    rootRule = findTemplate(allRules, source, null, ps);
    	}
    	catch (Exception ex) {
    	    receiveError(ex);
    	}
    	try {
        	if (rootRule != null) {
        		processTemplate(rootRule.getActions(), ps);
        	}
        	else { // use default root rule
        		processTemplate(defaultRule.getActions(), ps);
        	}
        	rf.flush();
    	}
    	catch(Exception ex) {
    	    receiveError(ex);
    	}
    	//-- Performance Testing
    	/* *
    	long etime = System.currentTimeMillis();
    	System.out.println("RuleProcessor Time: " + (etime - stime) + "(ms)");
    	/* */
        
        
    } //-- process
    
    /**
     * Removes the given MessageObserver from this processors list
     * of MessageObservers
     * @param msgObserver the MessageObserver to remove from this processors
     * list of MessageObservers
     * @return the given MessageObserver if it was removed from the list,
     * otherwise return null
    **/
    public MessageObserver removeMessageObserver
        (MessageObserver msgObserver) 
    {
        if (msgObservers.remove(msgObserver)) return msgObserver;
        else return null;
    } //-- addMessageObserver
	
    /**
     * Sets a property which may be accessed from the XSLT
     * stylesheet via a call to the xslp:param extension function
     * @param name the name of the parameter
     * @param value the value of the parameter
    **/
    public void setParameter(String name, String value) {
        if ((name == null) || (value == null)) return;
        if (name.length() > 0) {
            params.put(name, value);
        }
    } //-- setParameter
	
    /**
     * Sets the URIResolver for resolving all URIs. If null,
     * the default URIResolver will be used.
     *
     * @param resolver the URIResolver to use
    **/
    public void setURIResolver(URIResolver resolver) {
        if (resolver != null) 
            _uriResolver = resolver;
        else
            _uriResolver = new URIResolverImpl();
    } //-- setURIResolver
    
      //---------------------/
     //- Protected Methods -/
    //---------------------/

    /**
     * Binds the given xsl:variable to the top most variable set scope
     * within ProcessorState using the variable name attribute
     * @param the xsl variable to bind
     * @param context the current context node for evaluating the variable's
     * template
     * @param ps the current ProcessorState
    **/
    protected void bindVariable(Variable var, ProcessorState ps) 
    {
        ScopedVariableSet varSet = ps.getVariables();
        
        XPathResult result = null;
        
        //-- check for <xsl:param> default definition
        //-- in case an <xsl:with-param> has not been processed
        if (var.getType() == XSLObject.PARAM) {
            ScopedVariableSet params = ps.getParameters();
            if (params != null) {
                result = params.getVariable(var.getName());
            }
        }
        if (result == null) result = processVariable(var, ps);
        //-- check for duplicate variable names
            /* add later */
        //-- add variable to VariableSet
        if (result != null) {
            varSet.setVariable(var.getName(), result);
        }
        
    } //-- bindVariable
    
    
    /** 
     * Finds a TemplateRule that matches the given xml node
     *
     * @param rules the set of TemplateRules to search
     * @param node the XML Node to find a rule for.
     * @param mode the current processing mode for template rules
     * @param rpState the current ProcessorState
     * @return the matching TemplateRule, or null if no match is found
    **/
    protected TemplateRule findTemplate
        (TemplateRule[] rules, 
         XPathNode node, 
         String mode, 
         ProcessorState ps)
        throws XSLException
    {
        
		if (node == null) return null;
		
		TemplateRule matchingRule = null;
		float priority = (float)0.0;
		
		if (node.getNodeType() == XPathNode.ROOT) {
		    matchingRule = defaultRule;
		    priority = (float)0.5;
		}
		
		// Find the matching rule with the hightest priority
		for (int i = 0; i < rules.length; i++) {
		    TemplateRule rule = rules[i];
		    
			//-- make sure modes match
			String rMode = rule.getMode();
			if ((rMode == null) && (mode != null)) continue;
			if ((rMode != null) && (mode == null)) continue;
			if ((rMode != null) && (mode != null)) {
			    if (! rMode.equals(mode)) continue;
			}
			
			//-- make sure rule matches
			if (!rule.matches(node,ps)) continue;
			    
			//-- check for highest priority			    
			if (matchingRule == null) {
			    matchingRule = rule;
			    priority = rule.calculatePriority(node, ps);
			}
			else {
			    
			    float tmpPriority = rule.calculatePriority(node, ps);
			    
			    if (priority < tmpPriority) {
			        matchingRule = rule;
			        priority = tmpPriority;
			    }
			    else if (priority == tmpPriority) {
					    
					if ((defaultRule != matchingRule) &&
					    (matchingRule.getStylesheet() == rule.getStylesheet()))
					{
						StringBuffer err = new StringBuffer(RULE_CONFLICT);
						err.append("\n 1." + matchingRule.getPattern());
						err.append(" mode='" + matchingRule.getMode() + "' {");
						err.append(matchingRule.getStylesheet().getHref());
						err.append("}\n 2." + rule.getPattern());
						err.append(" mode='" + rule.getMode() + "' {");
						err.append(rule.getStylesheet().getHref());
						err.append("}\n");
						throw new XSLException(err.toString());
					}
					else {
					    matchingRule = rule;
					    priority = tmpPriority;
					}
				}
			}
		}
		
		/* <debug> 
		if (matchingRule != null)
		    System.out.println("Match: " + matchingRule.getMatchExpr());
		else System.out.println("no match");
		/* </debug> */
		
		return matchingRule;   
    } //-- findTemplate
    
      //-------------------/
     //- Private Methods -/
    //-------------------/
    
    /**
     * Binds the given xsl:variable by Processing it's Expr or Template
     * with the given context and state     
     * @param the xsl variable to bind
     * @param context the current context node for evaluating the variable's
     * template
     * @param ps the current ProcessorState
     * @return the new ExprResult
    **/
    XPathResult processVariable (Variable var, ProcessorState ps) {
        
        String exprAtt = var.getAttribute(Names.SELECT_ATTR);
        
        XPathResult result = null;
        
        if ((exprAtt != null) && (exprAtt.length() > 0)) {
            try {
                XPathExpression expr = _parser.createExpression(exprAtt);
                result = expr.evaluate(ps);
            }
            catch(XPathException xpe) {
                String err = "variable binding error: '" + var.getName();
                err += "'";
                receiveError(xpe, err);
            }
        }
        else {
            XPNBuilder xpnHandler = new XPNBuilder();
            //-- push ResultHandler
            ps.pushHandler(xpnHandler);
            XPathNode fragment = xpnHandler.startFragment();
            processTemplate(var.getActions(), ps);
            XPathNode node = fragment.getFirstChild();
            
            if (node == null) {
                result = new TreeFragmentResult();
            }
            else if (node.getNext() == null) {
                result = new TreeFragmentResult(node);
            }
            else {
                NodeSet nodes = new NodeSet(node);
                while ((node = node.getNext()) != null) {
                    nodes.add(node);
                }
                result = new TreeFragmentResult(nodes);
            }
            //-- remove ResultHandler
            ps.popHandler();
        }
        //-- check for duplicate variable names
            /* add later */
        //-- add variable to VariableSet
        
        return result;
        
    } //-- processVariable
     
    /**
     * processes the list of attributes for the given literal 
     * element and adds the attributes to the element currently
     * being processed
    **/
    private void processAttributes
        (XSLObject xslObject, ProcessorState ps, boolean useAttributeSets) 
    {
        AttributeList atts = xslObject.getAttributes();
        
	    AttributeValueTemplate avt = null;
	    String name, value;
	    
	    ResultFormatter formatter = ps.getResultFormatter();
	    
	    for (int i = 0; i < atts.getLength(); i++) {
	        name = atts.getName(i);
	        value = atts.getValue(i);
	        
  	        String prefix = XMLUtil.getNameSpacePrefix(name);
  	        
  	        String ns = null;
  	        if (prefix.length() > 0) {
  	            ns = ps.getNamespaceURI(prefix);
  	        }
	        if (ps.isXSLTNamespace(prefix)) {
	            if (!useAttributeSets) continue;
	            String localName = XMLUtil.getLocalPart(name);
	            if (Names.USE_ATTRIBUTE_SETS_ATTR.equals(localName)) {
	                processAttributeSets(value, ps);
	            }
	        }
	        //-- if no attribute value template
	        else if (value.indexOf('{') < 0) {
	            formatter.attribute(name, value, ns);
	        }
	        //-- otherwise attribute value template
	        else {
	            try {
	                String attValue = atts.getValue(i);
	                avt = ps.getAttributeValueTemplate(attValue);
                }
                catch (XPathException xpe) {
                    avt = null;
                }
                if (avt != null) {
                    try {
                        value = avt.evaluate(ps);
                    }
                    catch(XSLException ex) {
                        receiveError(ex);
                    }
                    value = Whitespace.stripSpace(value, true, true);
                }
                else value = "";
                formatter.attribute(name, value,ns);
            }
        }
    } //--processAttributes
    
    /**
     * Processes the AttributeSet's listed in the space separated
     * "useAttributeSets" argument.
    **/
    private void processAttributeSets
        (String useAttributeSets, ProcessorState ps)
    {
        if ((useAttributeSets == null) || (useAttributeSets.length() == 0))
            return;
            
        Tokenizer tokenizer = new Tokenizer(useAttributeSets);
        while (tokenizer.hasMoreTokens()) {
            String setName = tokenizer.nextToken();
 			AttributeSet attSet = stylesheet.getAttributeSet(setName);
 			
 			if (attSet == null) continue;
 			
 			//-- cascade attribute sets
 			String attrName = Names.USE_ATTRIBUTE_SETS_ATTR;
 			String value = attSet.getAttribute(attrName);
 			if (value == null) {
 			    String prefix = stylesheet.getXSLNSPrefix();
 			    if ((prefix != null) && (prefix.length() > 0)) {
 			        attrName = prefix + ':' + attrName;
 			        value = attSet.getAttribute(attrName);
 			    }
 			}
 			if (value != null) processAttributeSets(value, ps);
 			
 			//-- process AttributeSet
 			if (attSet != null) {
  			    processTemplate(attSet.getActions(), ps);
 			}
        }
    } //-- processAttributeSets
    
    /**
     * Processes the Parameters for the given XSLObject
     * @param xslObject the XSLObject to process the parameters of
     * @param context, the current Node context in which to process the
     * parameters
     * @param ps the current ProcessorState
    **/
    private void processParameters(XSLObject xslObject, ProcessorState ps) {
        
        ScopedVariableSet varSet = ps.getParameters();
        
        ActionTemplate tmpl = xslObject.getActions();
        
        if (tmpl.size() > 0) {
            ActionIterator actions = tmpl.actions();
            while (actions.hasNext()) {
                XSLObject action = actions.next();
                if (action.getType() == XSLObject.WITH_PARAM) {
                    Variable var = (Variable)action;
                    XPathResult result = processVariable(var, ps);
                    varSet.setVariable(var.getName(), result);
                }
                else break;
            }
        }
        
    } //-- processParameters
    
    /**
     * processes the given template using the given ProcessorState
     *
     * @param template the Template to process
     * @param ps the current ProcessorState
    **/
    private void processTemplate(ActionTemplate template, ProcessorState ps)
    {
        
        
        XPathNode node = ps.getNode();
        
        //-- handle xml:space
        boolean hasXMLSpace = false;
        if ((node != null) && (node.getNodeType() == XPathNode.ELEMENT)) {
            //-- check whitespace att
            String xmlSpace = node.getAttribute(null, Names.XMLSPACE_ATTR);
            if ((xmlSpace != null) && (xmlSpace.length() > 0)) {
                ps.getXMLSpaceModes().push(xmlSpace);
                hasXMLSpace = true;
            }
        }
        
        /* process template */
        
        if (template != null) {
            if (template.size() > 0) {
                //-- push a new variableSet onto stack
                ps.newVariableScope();
                ActionIterator iter = template.actions();
                while (iter.hasNext()) {
                    processAction(iter.next(), ps);
                }
                
                //-- freeVariableScope
                ps.freeVariableScope();
            }
        }
        else {
            // Do Built-in Rules
            if (node == null) return;
    		ResultFormatter formatter = ps.getResultFormatter();
            
            switch (node.getNodeType()) {
                case XPathNode.ROOT:
                case XPathNode.ELEMENT:
                    //...simply process all children
    			    processAction(selectChildren, ps);
    			    break;
    		    case XPathNode.ATTRIBUTE:
    		        //-- copy value
    		        formatter.characters(node.getStringValue());
    		        break;
    		    //case Node.CDATA_SECTION_NODE:
    		    //    formatter.cdata( xmlNode.getNodeValue() );
    		    //    break;
    		    case XPathNode.TEXT:
    		        // handle whitespace stripping
    		        if (isStripSpaceAllowed(node, ps))
    		            stripSpace(node, formatter);
    		        else 
    		            formatter.characters(node.getStringValue());
			        break;
			    default:
			        // do nothing;
			        break;
			}
		}
		if (hasXMLSpace) ps.getXMLSpaceModes().pop();
    } //-- processTemplate
        
    
    /**
     * Processes the given XSLNumber for the current element
    **/
    private void processXSLNumber
        (XPathNode current, XSLNumber xslNumber, ProcessorState ps) 
    {
        String result = xslNumber.getFormattedNumber(current, ps);
        ps.getResultFormatter().characters(result);
    } //-- processXSLNumber
    

    /**
     * Processes the given XSLT Action against given Node using the
     * specified template (NodeList)
     *
     * @param xslObject the XSLT Action to process
     * @param template 
     * @param rpState the current ProcessorState
    **/
	private void processAction(XSLObject xslObject, ProcessorState rpState) 
	{
	    
	    //-- <performance-testing>
	    //long stime = System.currentTimeMillis();
	    //-- </performance-testing>
	    
	    XPathNode current           = rpState.getNode();
	    XPathNode node              = null;
	    String    name              = null;
	    String    value             = null;
	    
	    TemplateRule[] templates = allRules;
	    
	    NodeSet matches = null;
	    Selection selection;
	    
	    rpState.pushAction(xslObject);
	    rpState.pushCurrentNode(current);
	    
        switch (xslObject.getType()) {
            // <xsl:apply-templates>
            case XSLObject.APPLY_IMPORTS:
                /** getTemplates is slow...we need to optimize that **/
                templates = xslObject.getStylesheet().getTemplates();
                /* do not break here */
            case XSLObject.APPLY_TEMPLATES:
            { //-- add local scoping 
            
                selection = (Selection)xslObject;
                
                try {
                    matches = selection.selectNodes(rpState);
                }
                catch(XPathException xpe) {
                    receiveError(xpe);
                    break;
                }
                
                if (matches.size() == 0) break; //-- nothing to do
                
                // sort nodes if necessary
                if ((matches.size() > 1) && selection.hasSortKeys()) {
                    XSLSort[] sortKeys = selection.getSortKeys();
                    if (sortKeys.length > 0) {
                        try {
                            matches = NodeSorter.sort(matches,
                                                    sortKeys,
                                                    rpState);
                        }
                        catch(XSLException xslx) {
                            receiveError(xslx);
                        }
                    }
                }
                
                String mode = selection.getAttribute(Names.MODE_ATTR);

                //-- process parameters
                rpState.newParameterScope();
                processParameters(xslObject, rpState);
                
                rpState.pushNodeSet(matches);
                for (int j = 0; j < matches.size(); j++) {
                    rpState.setPosition(j);
                    node = matches.item(j);
                    ActionTemplate actions = null;
                    TemplateRule tr = null;
                    try {
                        tr = findTemplate(templates,node,mode,rpState);
                    }
                    catch (Exception ex) {
                        receiveError(ex); 
                    }
                    if (tr != null) actions = tr.getActions();
                    if (node != current) {
                        processTemplate(actions,rpState);
                    }
                    else {
                        //-- Simple loop detection:
                        //-- make sure we do not use same rule, with
                        //-- same node, otherwise possible error.
                        
                        //-- first check for for-each, which would make the
                        //-- current node assigned to the for-each
                        XSLObject xsle = 
                            selection.getNearestAncestor(XSLObject.FOR_EACH);
                        
                        //-- find current template
                        if (xsle == null) {
                            xsle = 
                                selection.getNearestAncestor(XSLObject.TEMPLATE);
                        }
                        
                        if (tr == xsle) {
                            String warning = ENDLESS_LOOP_WARNING +
                                tr.getPattern().toString();
                            this.receiveError(warning, ErrorObserver.WARNING);
                        }
                        processTemplate(actions,rpState);
                    }
                }
                rpState.popNodeSet();
                rpState.freeParameterScope();
                break;
            }
            
			// <xsl:call-template>
			// Peter Ciuffetti
			// Added for WD-xslt-19990421
			case XSLObject.CALL_TEMPLATE:
			{ 
			
			        
			    //-- handle parameters (kv)
			    rpState.newParameterScope();
			    processParameters(xslObject, rpState);
			        
			    //-- process named template
			    name = xslObject.getAttribute(Names.NAME_ATTR);
			    TemplateRule tr = stylesheet.getNamedTemplate(name);
			    if (tr != null)
			        processTemplate(tr.getActions(), rpState);
                else
			        receiveError("No named template found: " + name);
			        
			    //-- remove parameters from stack
			    rpState.freeParameterScope();
			    break;
			}
            // <xsl:for-each>
			case XSLObject.FOR_EACH:
                selection = (Selection)xslObject;
                //process each select pattern
                try {
                    matches = selection.selectNodes(rpState);
                }
                catch(XPathException xpe) {
                    receiveError(xpe);
                    break;
                }
                
                if (matches.size() == 0) break;
                
                // sort nodes if necessary
                if ((matches.size() > 1) && selection.hasSortKeys()) {
                    XSLSort[] sortKeys = selection.getSortKeys();
                    if (sortKeys.length > 0) {
                        try {
                            matches = NodeSorter.sort(matches, sortKeys, rpState);
                        }
                        catch(XSLException xslx) {
                            receiveError(xslx);
                        }
                    }
                }
                
                rpState.pushNodeSet(matches);
                for (int j = 0; j < matches.size();j++) {
                    rpState.setPosition(j);
                    node = matches.item(j);
                    if (node != current) 
                        processTemplate(selection.getActions(), rpState);
                }
                rpState.popNodeSet();
                break;
            // <xsl:if>
			case XSLObject.IF:
			{
                Conditional conditional = (Conditional)xslObject;
                BooleanResult br = null;
                try {
                    br = conditional.evaluate(rpState);
                }
                catch(XPathException xpe) {
                    receiveError(xpe);
                    break;
                }
                if (br.booleanValue()) {                        
                    processTemplate(xslObject.getActions(), rpState);
                }
                break;
            }
            // <xsl:choose>
            case XSLObject.CHOOSE:
            {
		        // find first matching xsl:when or xsl:otherwise
		        ActionIterator actions = xslObject.getActions().actions();
		        BooleanResult br = null;
		        while (actions.hasNext()) {
		            
		            XSLObject xslWhen = actions.next();
		            Conditional condition = (Conditional)xslWhen;
                    try {
                        br = condition.evaluate(rpState);
                    }
                    catch(XPathException xpe) {
                        receiveError(xpe);
                        break;
                    }
                    catch(NullPointerException npe) {
                        npe.printStackTrace();
                    }

                    if (br.booleanValue()) {                        
                        processTemplate(xslWhen.getActions(), rpState);
                        break;
                    }
		        } //-- end for each child selection
                break;
            }
            // <xsl:message>
            case XSLObject.MESSAGE:
            {
                XPNBuilder xpnHandler = new XPNBuilder();
                //-- push handler
                rpState.pushHandler(xpnHandler);
                XPathNode fragment = xpnHandler.startFragment();
                processTemplate(xslObject.getActions(), rpState);
                //-- pop handler
                rpState.popHandler();
                value = fragment.getStringValue();
                for (int i = 0; i < msgObservers.size(); i++) {
                    ((MessageObserver)msgObservers.get(i)).receiveMessage(value);
                }
                break;
            }
		    // <xsl:number>
		    case XSLObject.NUMBER: 
	    	    processXSLNumber( current, (XSLNumber)xslObject, rpState);
    		    break;
	    	// <xsl:value-of>
    		case XSLObject.VALUE_OF:
    		{
		        value = ((ValueOf)xslObject).getValue(rpState);
		        
		        if (value == null) break;
		        
		        char[] chars = value.toCharArray();
		        int length = chars.length;
		        
		        if (isStripSpaceAllowed(current, rpState))
		            length = Whitespace.stripSpace(chars, true, true);
		            
		        if (length > 0)
		            rpState.getResultFormatter().characters(chars, 0, length);
		            
		        //-- clean up value for gc
		        value = null;
			    break;
			}
			// <xsl:variable>
			// <xsl:param>
			case XSLObject.VARIABLE:
			case XSLObject.PARAM:
			    bindVariable((Variable)xslObject, rpState);
			    break;
			
			// <xsl:attribute>
			// <xsl:comment>
			// <xsl:copy>
			// <xsl:entity-ref> PROPRIETARY EntityReference creation
			// <xsl:element>
			// <xsl:copy>
			// <xsl:pi>
			case XSLObject.ATTRIBUTE:
			case XSLObject.COMMENT:
			case XSLObject.COPY:
			case XSLObject.ELEMENT:
			case XSLObject.PI:
			case XSLObject.ENTITY_REF:
			    try {
    			    createNode(xslObject, rpState);
			    }
			    catch(XSLException ex) {
			        receiveError(ex);
			    }
			    break;
			// <xsl:copy-of>
		    case XSLObject.COPY_OF:
		        CopyOf copyOf = (CopyOf)xslObject;
			    try {
			        NodeSet nodes = copyOf.selectNodes(rpState);
			        for (int i = 0; i < nodes.size(); i++) {
			            copyOf(nodes.item(i), rpState);
			        }
			    }
			    catch(XPathException xpe) {
			        receiveError(xpe);
			    }
			    break;
			// <xsl:text>
            case XSLObject.TEXT:
            { // local scope data
                XSLText xslText = (XSLText)xslObject;
                ResultFormatter formatter = rpState.getResultFormatter();
                char[] chars = xslText.getCharArray();
                if (xslText.disableOutputEscaping())
                    formatter.unescapedCharacters(chars, 0, chars.length);
                else
                    formatter.characters(chars, 0, chars.length);
                break;
            }
            case XSLObject.CDATA:
            { // local scope data
                String data = ((XSLCData)xslObject).getText();
                rpState.getResultFormatter().cdata(data);
                break;
            }
    		// <xsl:script>
    		// Scripting is proprietary
    		/* Scripting is no longer supported
    		case XSLObject.SCRIPT:
    		    XSLScript xslScript = (XSLScript)xslObject;
    		    String language = xslScript.getAttribute(Names.LANGUAGE_ATTR);
    		    ScriptHandler scriptHandler = rpState.getScriptHandler(language);
    		    if (scriptHandler != null) {
    			    Object obj = scriptHandler.evalAsFunction(xslScript, xmlNode);
    			    if (obj != null)
    			        rpState.getResultFormatter().cdata(obj.toString());
                }
                else {
                    receiveError("Warning no ScriptHandler found for "+
                        "language: " + language);
                }
    			break;
            */
		    // XML Literals, Formatting Objects
    		case XSLObject.LITERAL:
    		{
    		    ResultFormatter formatter = rpState.getResultFormatter();
    		    name = getResolvedName(xslObject.getTypeName());
                String prefix = "";
                int idx = name.indexOf(':');
                if (idx >= 0) {
                    prefix = name.substring(0, idx);
                    name   = name.substring(idx+1);
                }
                String ns = xslObject.getNamespace();
                if (ns == null) {
                    ns = rpState.getNamespaceURI(prefix);
                }
                if (!formatter.isNamespaceDeclared(ns))
                    formatter.declareNamespace(prefix, ns);
                formatter.startElement(name, ns);
		    	processAttributes(xslObject, rpState, true);
		    	
		    	ActionTemplate tmpl = xslObject.getActions();
		    	
		    	if (tmpl.size() > 0) {
		    	    ActionIterator actions = tmpl.actions();
		    	    //-- add variable set scoping
                    rpState.newVariableScope();
                    while (actions.hasNext()) {
                        processAction(actions.next(), rpState);
                    }
                    rpState.freeVariableScope();
                }
		    	formatter.endElement(name, ns);
				break;
		    }
		    default:
		        break;
		} //-- end switch
		
		
	    rpState.popAction();
	    rpState.popCurrentNode();
	    
	} //-- processAction
	
	    
    /**
     * Copies the Node based on the rules in the XSL WD for xsl:copy-of
     *
    **/
    private void copyOf(XPathNode node, ProcessorState ps) {
        
        String data;
        ResultFormatter formatter = ps.getResultFormatter();
        
        switch (node.getNodeType()) {
            case XPathNode.ROOT:
                break;
            case XPathNode.ATTRIBUTE: 
            {
                formatter.attribute(node.getLocalName(), 
                                    node.getStringValue(), 
                                    node.getNamespaceURI());
                break;
            }
            case XPathNode.COMMENT:
                formatter.comment(node.getStringValue());
                break;
            case XPathNode.ELEMENT:
            {
                String name = node.getLocalName();
                String ns = node.getNamespaceURI();
                if (!formatter.isNamespaceDeclared(ns)) {
                    String prefix = node.getNamespacePrefix(ns);
                    formatter.declareNamespace(prefix, ns);                    
                }
                formatter.startElement(name, ns);
                
                //-- copy namespace declarations
                XPathNode nsDecl = node.getFirstNamespace();
                while (nsDecl != null) {
                    formatter.declareNamespace(nsDecl.getLocalName(), 
                                               nsDecl.getStringValue());
                    nsDecl = nsDecl.getNext();
                }
                //-- copy attribute nodes
                XPathNode attr = node.getFirstAttribute();
                while (attr != null) {
                    formatter.attribute(attr.getLocalName(), 
                                        attr.getStringValue(), 
                                        attr.getNamespaceURI());
                    attr = attr.getNext();
                }
                
                //-- copy child nodes
                XPathNode child = node.getFirstChild();
                while (child != null) {
                    copyOf(child, ps);
                    child = child.getNext();
                }
                formatter.endElement(name, ns);
                break;
            }
            case XPathNode.TEXT:
                formatter.characters(node.getStringValue());
                break;
            case XPathNode.PI:
                formatter.processingInstruction(node.getLocalName(), 
                                                node.getStringValue());
                break;
            default:
                break;
        }
    } //-- copyOf
    
    /**
     * Copies the Node based on the rules in the XSLT 1.0 
     * Recommendation for xsl:copy.
     *
     * @param node the XPathNode to copy to the result tree
     * @param xslObject the xsl:copy object to use as the template
     * @param ps the current ProcessorState
    **/
    private void copy(XPathNode node, XSLObject xslObject, ProcessorState ps) {
        
        if (node.getNodeType() == XPathNode.ELEMENT) {
            
            ResultFormatter formatter = ps.getResultFormatter();
            
            String elementName = node.getLocalName();
            
            String ns = node.getNamespaceURI();
            
            //-- declare prefix/namespace if necessary
            if (!formatter.isNamespaceDeclared(ns)) {
                String prefix = node.getNamespacePrefix(ns);
                formatter.declareNamespace(prefix, ns);
            }
            
            formatter.startElement(elementName, ns);
            
            //-- Copy Namespace Nodes
            XPathNode nsNode = node.getFirstNamespace();
            while (nsNode != null) {
                String prefix = nsNode.getLocalName();
                String uri    = nsNode.getStringValue();
                formatter.declareNamespace(prefix, uri);
                nsNode = nsNode.getNext();
            }
            
            //-- use-attribute-sets
            String useAtts 
                = xslObject.getAttribute(Names.USE_ATTRIBUTE_SETS_ATTR);
            if ((useAtts != null) && (useAtts.length() > 0)) {
                this.processAttributeSets(useAtts, ps);
            }
            
            //-- process template
            ps.pushNodeSet(new NodeSet(node));
			processTemplate(xslObject.getActions(), ps);
			ps.popNodeSet();
            formatter.endElement(elementName, ns);
        }
        else copyOf(node, ps);
        
    } //-- copy
    
    /**
     * Creates a new Node based on the type of XSLObject
     * @param context the DOM Node to use as the current context for
     * evaluating any expressions or scripts
     * @return the new Node
    **/
    private void createNode(XSLObject xslObject, ProcessorState ps) 
        throws XSLException 
    {
        
        String     name    = null;
        String     value   = null;
        XPathNode  node    = null;
        
        
        AttributeValueTemplate nodeName = null;
        
        String attValue = xslObject.getAttribute(Names.NAME_ATTR);
        
        try {
            nodeName = ps.getAttributeValueTemplate(attValue);
        }
        catch(XPathException xpe) {
            throw new XSLException(xpe);
        }
        
        if (nodeName != null)
            name = nodeName.evaluate(ps);
        else name = "";
            
        
        
        // get value
        int type = xslObject.getType();
        
        
        //-- processes template
        switch (type) {
            case XSLObject.COPY:
            case XSLObject.ELEMENT:
            case XSLObject.ENTITY_REF:
                break;
            default:
            {
                XPNBuilder xpnHandler = new XPNBuilder();
                //-- push Handler
                ps.pushHandler(xpnHandler);
                ResultFormatter formatter = ps.getResultFormatter();
                formatter.startElement(xslObject.getTypeName(), null);
                processTemplate(xslObject.getActions(), ps);
                formatter.flush();
                node = xpnHandler.getCurrentNode();
                formatter.endElement(xslObject.getTypeName(), null);
                //-- put back original ResultHandler
                ps.popHandler();
                value = node.getStringValue();
            }
        }
        
        ResultFormatter formatter = ps.getResultFormatter();
        
        switch (type) {
            // xsl:attribute
            case XSLObject.ATTRIBUTE:
            {
                //if (NS_DEFAULT_DECL
                String namespace = null;
                if (name.equals(NS_DEFAULT_DECL)) {
                    String error = "Illegal attribute name: xmlns";
                    error += "; 'xmlns' is a reserved word";
                    throw new XSLException(error);
                }
                int idx = name.indexOf(':');
                if (idx >= 0) {
                    String prefix = name.substring(0, idx);
                    if (prefix.equals(NS_DEFAULT_DECL)) {
                        String error = "Illegal attribute name: " + name;
                        error += "; The 'xmlns' prefix is reserved.";
                        throw new XSLException(error);
                    }
                    namespace = xslObject.getAttribute(NAMESPACE_ATTR);
                    if (namespace == null)
                        namespace = ps.getNamespaceURI(prefix);
                }
                value = Whitespace.stripSpace(value);
                formatter.attribute(name, value, namespace);
                break;
            }
            // xsl:comment
            case XSLObject.COMMENT:
                formatter.comment(value);
                break;
            // xsl:copy
            case XSLObject.COPY:
                copy(ps.getNode(), xslObject, ps);
                break;
            // xsl:element
            case XSLObject.ELEMENT:
            {
                String prefix = "";
                int idx = name.indexOf(':');
                if (idx >= 0) {
                    prefix = name.substring(0, idx);
                    name   = name.substring(idx+1);
                }
                String ns = xslObject.getAttribute(NAMESPACE_ATTR);
                if (ns == null) ns = ps.getNamespaceURI(prefix);
                if (!formatter.isNamespaceDeclared(ns))
                    formatter.declareNamespace(prefix, ns);                    
                formatter.startElement(name, ns);
                String useAttributeSets 
                    = xslObject.getAttribute(Names.USE_ATTRIBUTE_SETS_ATTR);
                processAttributeSets(useAttributeSets, ps);
                processTemplate(xslObject.getActions(),ps);
                formatter.endElement(name, ns);
                break;
            }
            // xsl:entity-ref
            case XSLObject.ENTITY_REF: // Proprietary to XSL:P
                formatter.entityReference(name);
                break;
            // xsl:pi
            case XSLObject.PI:
                formatter.processingInstruction(name, value);
                break;
            default:
                break;
        }
    } //-- createNode

    private void duplicateIdError(String id) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("warning: multiple elements with the same id value: ");
	    sb.append(id);
	    sb.append("\n -- processing will continue with first occurance only.");
	    receiveError(sb.toString());
    } //-- duplicateIdError
    
    /**
     * Returns the Namespace associated with the given 
     * namespace prefix
     *
     * @param prefix the namespace prefix to resolve
     * @param element, the current context element
     * @return the namespace associated with the given namespace
     * prefix, or null if no namespace is found.
    **
    public static String getNamespace(String prefix, Element element) {
        
        String attName = null;
        
        if ((prefix == null) || (prefix.length() == 0))
            attName = "xmlns";
        else 
            attName = "xmlns:" + prefix;

        while (element != null) {
            NamedNodeMap atts = element.getAttributes();
            Node attr = atts.getNamedItem(attName);
            if (attr != null) return attr.getNodeValue();
            Node node = element.getParentNode();
            if (node.getNodeType() != Node.ELEMENT_NODE)
                break;
            element = (Element) node;
        }
        
        return null;
        
    } //-- getNamespace
    
    /**
     * Returns the name with any quoted namespaces resolved
     * @param elementName the element name to resolve
     * @return the resolved name
    **/
    private String getResolvedName(String elementName) {
	    int idx = 0;
	    if ((idx = elementName.indexOf(NS_SEP)) > 0) {
	        String ns = stylesheet.getQuotedNamespace(elementName.substring(0,idx));
	        return ns + elementName.substring(idx);
	    }
	    return elementName;
    } //-- getResolvedName
    
    /**
     * Strips Whitespace from the Text Node, and sends the
     * characters to the given formatter
    **/
    private void stripSpace(XPathNode node, ResultFormatter formatter) {
        if (node.getNodeType() != XPathNode.TEXT) return;
        
        //-- set strip leading space flag
        boolean stripLWS = (node.getPrevious() == null);
        //-- set strip trailing space flag
        boolean stripTWS = (node.getNext() == null);
        
        String data = node.getStringValue();
        
        if ((data != null) && (data.length() > 0)) {
            char[] chars = data.toCharArray();
            int length = Whitespace.stripSpace(chars, stripLWS, stripTWS);
            formatter.characters(chars, 0, length);
        }
    } //-- stripSpace
    
    private boolean isStripSpaceAllowed(XPathNode node, ProcessorState ps) {
        if (node == null) return true;
        if (ps.getXMLSpaceModes().peek().equals(Names.PRESERVE_VALUE))
            return false;
            
        XPathNode context = node;
        switch (node.getNodeType()) {
            case XPathNode.ROOT:
                return true;
            case XPathNode.ELEMENT:
                break;
            default:
                context = node.getParentNode();
                //-- check for null parent (Ray Powell)
                if (context == null) return false;
                if (context.getNodeType() != XPathNode.ELEMENT)
                    return true;
                break;
        }
        return stylesheet.isStripSpaceAllowed(context.getLocalName());
    } //-- isStripSpaceAllowed
    
} //-- RuleProcessor

