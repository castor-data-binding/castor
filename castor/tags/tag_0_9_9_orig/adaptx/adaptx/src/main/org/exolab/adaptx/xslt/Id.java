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


/**
 * Represents an xsl:id element
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Id extends EmptyXSLObject {

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new Id element
    **/
    public Id() {
        super(XSLObject.ID);
    } //-- Id

      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Returns the name of the attribute to use as an element Id
     * @return the name of the attribute to use as an element Id
    **/
    public String getIdAttribute() {
        return getAttribute(Names.ATTRIBUTE_ATTR);
    } //-- getIdAttribute
    
    /**
     * Returns the element type that this Id is for
     * @return the element type that this Id is for
    **/
    public String getElementType() {
        return getAttribute(Names.ELEMENT_ATTR);
    } //-- getIdAttribute

    /**
     * Sets the element type that this Id Attribute is for
     * @param elementType the element type (gi) that this Id is for
     * <PRE>
     *   using the wildcard '*' will match all element types
     * </PRE>
    **/
    public void setElementType(String elementType) {
        try {
            setAttribute(Names.ELEMENT_ATTR, elementType);
        }
        catch(XSLException xslException) {};
    } //-- setElementType
    
    /**
     * Sets the name of the attribute to use as an element Id
     * @param attributeName the name of the attribute to use as an Id
    **/
    public void setIdAttribute(String attributeName) {
        try {
            setAttribute(Names.ATTRIBUTE_ATTR, attributeName);
        }
        catch(XSLException xslException) {};
    } //-- setIdAttribute
} //-- Id
