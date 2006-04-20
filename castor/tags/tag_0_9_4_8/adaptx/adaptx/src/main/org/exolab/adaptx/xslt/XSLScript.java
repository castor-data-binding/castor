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
import org.exolab.adaptx.xml.*;

/**
 * This class represents a xsl:script element. This is
 * currently proprietary to XSLP and not included in
 * the latest W3 XSL Working Draft (19981216)
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XSLScript extends XSLText {
    
    public final String ECMASCRIPT = "ECMAScript";
    
    String language = ECMASCRIPT;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new XSLScript
    **/
    public XSLScript() {
        super(XSLObject.SCRIPT);
    } //-- XSLScript
    
    /**
     * Creates a new XSLScript
     * @param data the default data for this XSLScript
    **/
    public XSLScript(String data) {
        super(XSLObject.SCRIPT);
        if (data != null) appendText(data);
    } //-- XSLScript
    
    
    /**
     * Returns the scripting Language for this XSLScript
     * @return the scripting Language for this XSLScript
    **/
    public String getLanguage() {
        return language;
    } //-- getLanguage
    
    /**
     * Returns the Namespace for this xsl:script
     * @return the Namespace for this xsl:script
    **/
    public String getScriptNameSpace() {
        return getAttribute(Names.NS_ATTR);
    } //-- getScriptNameSpace
    
    public void setLanguage(String lang) {
        this.language = lang;
    } //-- setLanguage
    
} //-- XSLScript
