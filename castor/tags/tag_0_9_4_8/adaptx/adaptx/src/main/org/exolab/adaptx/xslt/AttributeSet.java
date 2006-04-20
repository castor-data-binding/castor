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

import org.w3c.dom.*;

/**
 * Represents an xsl:attribute-set
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeSet extends XSLObject {
    
      //----------------/
     //- Constructors -/
    //----------------/
      
    /**
     * Creates a new AttributeSet
    **/
    public AttributeSet(String name) {
        super(XSLObject.ATTRIBUTE_SET);
        try {
            setAttribute(Names.NAME_ATTR, name);
        }
        catch(XSLException xslException) {};
        makeAttrReadOnly(Names.NAME_ATTR);
    } //-- AttributeSet
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
	public XSLObject copy() {
	    AttributeSet attSet = new AttributeSet(getName());
	    attSet.copyActions(this);
	    return attSet;
	} //-- copy
    
	/**
	 * Returns the name attribute of this AttributeSet
	 * @return the name of the AttributeSet
	**/
    public String getName() {
        return getAttribute(Names.NAME_ATTR);
    } //-- getName
    
    /**
     * Overrides appendAction in XSLObject to only allow
     * valid XSLObjects for this XSLObject
    **/
    public boolean appendAction(XSLObject xslObj) {
        if (isValidChild(xslObj)) {
            return super.appendAction(xslObj);
        }
        return false;
    } //-- appendAction
    
    /**
     * Returns the value of the 'use-attribute-sets' attribute, or
     * null of no value has been set.
     *
     * @return the value of the 'use-attribute-sets' attribute.
    **/
    public String getUseAttributeSets() {
        return getAttribute(Names.USE_ATTRIBUTE_SETS_ATTR);
    } //-- getUseAttributeSets

    /**
     * Sets the value of the 'use-attribute-sets' attribute.
     *
     * @param useAtts the value of the 'use-attribute-sets' attribute.
     * @see #getUseAttributeSets
    **/
    public void setUseAttributeSets(String useAtts) {
        if (useAtts == null) useAtts = "";
        try {
            setAttribute(Names.USE_ATTRIBUTE_SETS_ATTR, useAtts);
        }
        catch(XSLException xslException) {};
    } //-- getUseAttributeSets
    
    /**
     * Determines if the given element is a valid child of this XSLObject
     * @param element the element to determine validity of
     * @return true if the given element is a valid child of this XSLObject,
     * otherwise false
    **/
    protected boolean isValidChild(XSLObject xslObj) {
        switch (xslObj.getType()) {
            case XSLObject.ATTRIBUTE:
                return true;
            default:
                return false;
        }
    } //-- isValidChild
    
} //-- AttributeSet
