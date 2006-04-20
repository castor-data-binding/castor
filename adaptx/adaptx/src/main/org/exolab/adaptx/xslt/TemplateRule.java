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
import java.util.*;
import org.exolab.adaptx.xpath.functions.ErrorFunctionCall;
import org.exolab.adaptx.xpath.*;

/**
 * A class that Represents an XSLT Template Rule.
 * Section 5 of the W3C XSLT 1.0 Recommendation (19991116).
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class TemplateRule extends XSLObject {
    
	/**
	 * The match Pattern for this template
	**/
	private Pattern _pattern = null;
	
	/**
	 * The name for this template.
	 * (Peter Ciuffetti. added for WD-xslt-19990421)
	**/
	private String _name = null;
	
	
    private ErrorFunctionCall _errorCall = null;
    
    private boolean _allowParamVars = true;
    
    /**
     * A boolean to indicate this template should be
     * disabled. This happens if the template generated
     * an error and should no longer be used.
    **/
    private boolean _disable = false;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new TemplateRule.<BR />
     * By default the new rule will not match any elements.
    **/
	public TemplateRule() {
	    super(XSLObject.TEMPLATE);
    } //-- TemplateRule

      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Calculates the priority for this Template Rule. The priority
     * is calculated using the rules from Section 5.5 of the XSLT 1.0
     * Recommendation.
     * 
     * @return the priority for this Template Rule
    **/
    public float calculatePriority(XPathNode node, XPathContext context) { 
        
        String attValue = getAttribute(Names.PRIORITY_ATTR);
        if ((attValue != null) && (attValue.length() > 0)) {
            try {
                return Float.valueOf(attValue).floatValue();
            }
            catch(NumberFormatException nfe) {};
        }
        
        if (_pattern == null) return -2;
        else {
            try {
                LocationPathPattern locPath = _pattern.getMatchingPattern(node, context);
                return (float) locPath.getDefaultPriority();
            }
            catch (XPathException xpe) {};
        }
        
        return -2;
    } //-- calculatePriority
    
    /**
     * Creates a copy of this TemplateRule
     *
     * @return the new copy of this TemplateRule
    **/
	public XSLObject copy() {
	    TemplateRule tr = null;
	    tr = new TemplateRule();
	    tr.setName(_name);
	    tr.copyActions(this);
	    tr.copyAttributes(this);
	    return tr;
	} //-- copy

    /**
     * Returns the Pattern contained within this template rule that matched the given
     * node using the given context.
     *
     * @param node the XPathNode to match against
     * @param context the XPathContext to match against
     * @return the matching pattern, or null if no patterns matched the given node.
    **/
    public LocationPathPattern getMatchingPattern(XPathNode node, XPathContext context)
        throws XPathException
    {
        if (_pattern == null) return null;
        
        return _pattern.getMatchingPattern(node, context);
        
	} //-- getMatchingPattern
	
	/**
	 * Returns the mode attribute of this Template Rule
	 *
	 * @return the value of the mode attribute.
	**/
    public String getMode() {
        return getAttribute(Names.MODE_ATTR);
    } //-- getMode
    
    /**
     * Returns the name for this Template Rule, or null if no name exists.
     *
     * @returns the name for this rule, or null if no name exists.
    **/
	public String getName() {
	    return _name;
	} //--getName

    /**
     * Returns the match Pattern for this template 
     *
     * @return the match Pattern for this template
    **/
	public Pattern getPattern() {
	    return _pattern;
	} //-- getPattern
	

    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context node. 
     *
     * Note: If there are errors in the XPath expression of this template,
     * the errors are reported on first use of the template. The
     * template will then be disabled and no longer used for
     * XSLT processing.
     *
     * @param node the node to determine a match for
     * @param context the Node which represents the current context
     * @param ps the current ProcessorState
     * @return true if the given node is matched by this MatchExpr
    **/
	public boolean matches(XPathNode node, ProcessorState ps) 
	    throws XSLException
    {
        if (_disable) return false;
        
        boolean result = false;
        try {
            //-- handle errors
            if (_errorCall != null) 
                _errorCall.evaluate( ps );
            
            if (_pattern != null) {
                //-- make sure ProcessorState has this template
                //-- added for namespace resolution
                ps.pushAction(this);
	            result =_pattern.matches(node, ps);
	            //-- remove template
	            ps.popAction();
	        }
	    }
	    catch (XPathException xpe) {
	        _disable = true;
	        //-- clean-up
	        if (_errorCall == null) {
	            //-- remove template
	            ps.popAction();
	        }
	        throw new XSLException(xpe);
	    }
	    return result;
	} //-- matches
	
    /**
     * Sets the attribute with the given name to the given value.
     *
     * @param name the name of the attribute to set
     * @param value the value to set the attribute to
     * @exception XSLException if this XSLObject does not allow attributes
     * with the given name, or if the attribute is read only
    **/
    public void setAttribute(String name, String value) 
        throws XSLException
    {
        if (Names.MATCH_ATTR.equals(name)) {
        
            try {
                _pattern = new Pattern(value);
            }
            catch (PatternException px) {
                String err = "invalid 'match' attribute: " + value;
                err += "; " + px.toString();
                _errorCall = new ErrorFunctionCall();
                _errorCall.setError(err);                
            }
        }
        super.setAttribute(name, value);
    } //-- setAttribute
    
    
    /**
     * Sets the MatchExpr for this TemplateRule
     * @param matchPattern the desired Match Pattern to use for this 
     * template
    **/
    public void setMatchAttr(String matchPattern) {
        if (matchPattern == null) matchPattern = "";
        try {
            setAttribute(Names.MATCH_ATTR, matchPattern);
        }
	    // an XSLException will never be thrown here
	    // since we are setting a valid attribute
        catch(XSLException xsle) {};
        
    } //-- setMatchExpr
    
    public void setModeAttr(String mode) {
        try {
            setAttribute(Names.MODE_ATTR,mode);
        }
	    // an XSLException will never be thrown here
	    // since we are setting a valid attribute
	    catch(XSLException xslException) {};
    } //-- setMode
    
    /**
     * Sets the name for this Template
     *
     * @param name the name to use for this template
    **/
    public void setName(String name) {
        _name = name;
        // XXX
        //if (_pattern == null) _pattern = new Pattern(); 
    } //-- setName

    /**
     * Sets the priority for this TemplateRule
     * @param priority the desired priority of this rule.
     * 0 by default
    **/
	public void setPriority(float priority) {
	    try {
	        setAttribute(Names.PRIORITY_ATTR, String.valueOf(priority));
	    }
	    // an XSLException will never be thrown here
	    // since we are setting a valid attribute
	    catch(XSLException xslException) {};
	} //-- setPriority
    
    /**
     * Overrides handleAction in XSLObject
    **/
    protected boolean handleAction (XSLObject xslObject) {
        if (xslObject == null) return true;
        switch (xslObject.getType()) {
            case XSLObject.PARAM:
                if (_allowParamVars) break;
                else return true;
            default:
                _allowParamVars = false;
                break;
        }
        return false;
    } //-- handleAction
    
} //-- TemplateRule