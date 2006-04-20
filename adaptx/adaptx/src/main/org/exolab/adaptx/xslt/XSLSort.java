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

import org.w3c.dom.*;

import org.exolab.adaptx.xpath.*;

/**
 * Represents an xsl:sort element
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XSLSort extends XSLObject {
    
    /**
     * The case-order attribute
    **/
    public static final String CASE_ORDER_ATTR  = "case-order";

    /**
     * The data-type attribute
    **/
    public static final String DATA_TYPE_ATTR   = "data-type";

    /**
     * The lang attribute
    **/
    public static final String LANG_ATTR        = "lang";

    /**
     * The order attribute
    **/
    public static final String ORDER_ATTR       = "order";

    /**
     * The select attribute
    **/
    public static final String SELECT_ATTR      = "select";


    /**
     * The ascending order value
    **/
    public static final String ASCENDING_ORDER  = "ascending";

    /**
     * The descending order value
    **/
    public static final String DESCENDING_ORDER  = "descending";


    /**
     * The number data-type value
    **/
    public static final String NUMBER_TYPE  = "number";

    /**
     * The text data-type value
    **/
    public static final String TEXT_TYPE  = "text";
    

    private XPathExpression select = null;
    private String pattern    = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new XSLSort
    **/
    public XSLSort() {
        super(XSLObject.SORT);
        // Set default attributes
        try {
            setAttribute(Names.SELECT_ATTR,".");
        }
        catch(XSLException xslException) {};
    } //-- XSLSort

      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Retrieves the selectExpr for this XSLSort.
    **/
    public XPathExpression getSelectExpr() throws XPathException {
        
        String attValue = getAttribute(Names.SELECT_ATTR);
        if (attValue == null) attValue = "";
        
        // make sure we synchronize select pattern
        if ((!attValue.equals(pattern)) || (select == null)) {
            pattern = attValue;
            // Check for empty string to solve changes from 
            // XML4J 1.1.9 to 1.1.14 as suggested by Domagoj Cosic.
            if ((attValue != null) && (attValue.length() > 0))
                select = createSelectExpression(attValue);
        }
        return select;
    } //-- getSelectExpr
    
    public void setAttribute(String name, String value) 
        throws XSLException
    {
        if (name == null) return;
        if (name.equals(Names.SELECT_ATTR)) {
            try {
                setSelectExpr(createSelectExpression(value));
            }
            catch(XPathException xpe) {
                throw new XSLException("Invalid SelectExpr in xsl:sort - " + 
                    xpe.getMessage());
            }
        }
        
        super.setAttribute(name,value);
    }
    /**
     * sets the SelectExpr for this XSLSort
    **/
    public void setSelectExpr(XPathExpression selectExpr) {
        
        if (selectExpr == null) {
            try { this.select = createSelectExpression("."); }
            catch(XPathException xpe) {};
        }
        else select = selectExpr;
    } //-- setSelectExpr

} //-- XSLSort
