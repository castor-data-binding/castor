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

/**
 * This class represents an xsl:call-template.
 * @author <a href="mailto:peterc@knowledgecite.com">Peter Ciuffetti</a>
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @since WD-xslt-19990421
**/
public class XSLCallTemplate extends XSLObject {

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new XSLCallTemplate
    **/
    public XSLCallTemplate(String name) {
        super(XSLObject.CALL_TEMPLATE);
        setTemplateName(name);
    } //-- XSLCallTemplate

      //------------------/
     //- Public Methods -/
    //------------------/

	/**
	 * Returns the name of the Template that this XSLCallTemplate references
	 * @return the name of the Template that this XSLCallTemplate references
	**/
	public String getTemplateName() {
	    return getAttribute(Names.NAME_ATTR);
	} //-- getAttributeSetName

	/**
	 * Sets the name of the Template that this XSLCallTemplate references
	 * @param name the name Template that this XSLCallTemplate references
	**/
	public void setTemplateName(String name) {
	    try {
	        setAttribute(Names.NAME_ATTR,name);
	    }
	    catch(XSLException xslException) {};
	} //-- setAttributeSetName

} //-- XSLCallTemplate
