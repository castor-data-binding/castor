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

import org.exolab.adaptx.xslt.ActionTemplate;
import org.exolab.adaptx.xslt.ActionIterator;
import org.exolab.adaptx.xslt.XSLObject;
import org.exolab.adaptx.xslt.XSLText;

/**
 * The default implementation of ActionTemplate.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ActionTemplateImpl implements ActionTemplate {
    
    private ActionItem start = null;
    private ActionItem last = null;
    
    private int size = 0;
    
    /**
     * Creates an empty ActionTemplateImpl
    **/
    public ActionTemplateImpl() {
        super();
    } //-- ActionTemplateImpl
    
    
    public ActionIterator actions() {
        return new ActionTemplateIterator(start);
    } //-- getActions
    
    
    /**
     * Adds the given XSLObject to this ActionTemplate's list of
     * actions. 
     * @param xslObject the XSLObject to add
     * @return true if the given XSLObject has been added to this
     * XSLObject otherwise false 
    **/
    public boolean addAction(XSLObject xslObject) {
        
        if (xslObject == null) return false;
        
        // normalize text
        if (xslObject.getType() == XSLObject.TEXT) {
            XSLText xslText = (XSLText)xslObject;
            // check last action added
            if (last != null) {
                if (last.xslObject.getType() == XSLObject.TEXT) {
                    XSLText lastText = (XSLText)last.xslObject;
                    if (lastText.disableOutputEscaping() ==
                        xslText.disableOutputEscaping()) 
                    {
                        lastText.appendText(xslText.getText());
                        return true;
                    }
                }
            }
        }
        
        ActionItem item = new ActionItem();
        item.xslObject = xslObject;
        
        if (last == null) {
            start = last = item;
        }
        else {
            last = (last.next = item);
        }
        ++size;
        return true;
    } //-- addAction
    
    /** 
     * Returns the last XSLObject of this ActionTemplate
     * @return the last XSLObject of this ActionTemplate
    **/
    public XSLObject lastAction() {
        if (last != null) return last.xslObject;
        return null;
    } //-- lastAction
    
    /**
     * Returns the number of actions in this template
     * @return the number of actions in this template
    **/
    public int size() {
        return size;
    } //-- size

    
} //-- ActionTemplate

/**
 * A class which holds the actual XSLObject references
 * and points to the next ActionItem
**/
class ActionItem {
    
    ActionItem next = null;
    XSLObject xslObject = null;    
    
    /**
     * Creates a new ActionItem;
    **/
    ActionItem() {
        super();
    } //-- ActionItem
    
} //-- ActionItem


/**
 * An implementation of Enumeration for ActionTemplate
**/
class ActionTemplateIterator implements ActionIterator {
    
    private ActionItem next = null;
    
    ActionTemplateIterator(ActionItem action) {
        next = action;
    } //-- ActionTemplateIterator
    
    //--------------------------------------------/
    //- Implementation of ActionTemplateIterator -/
    //--------------------------------------------/

    public boolean hasNext() {
        return (next != null);
    } //-- hasNext
    
    public XSLObject next() {
        XSLObject obj = null;
        if (next != null) {
            obj = next.xslObject;
            next = next.next;
        }
        return obj;
    } //-- nextElement
    
} //-- ActionTemplateEnumeration