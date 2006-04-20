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

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * This class handle Processor Instructions for the XSL processor
 * @version $Revision$ $Date$
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class XSLPIHandler {

    public static String STYLESHEET_PI_OLD = "xml:stylesheet";
    public static String STYLESHEET_PI     = "xml-stylesheet";
    public static String HREF_ATTR         = "href";
    
    private static String EQUALS = "=";
    private static String QUOTE = "\"";
    private static String SPACE = " ";

    private String href = null;
    private String documentBase = null;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Create an XSLPIHandler with the specified XSLIterpreter
     * @see ser.nexus.XSLInterpreter
    **/
    public XSLPIHandler () {
        super();
    } //-- XSLPIHandler 


      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Handles the given PI
     * @param pi the ProcessingInstruction to handle
    **/
    public void handlePI(String target, String data) {
        if (target.equals(STYLESHEET_PI) || target.equals(STYLESHEET_PI_OLD)) {
            Hashtable ht = parseAttributes(data);            
            this.href = (String) ht.get(HREF_ATTR);
        }

    }
    
    /**
     * Parses the PI data into attribute components (name-value pairs)
     * @param data the PI data string
     * @return the Hashtable of components
    **/
    private Hashtable parseAttributes(String data) {
        Hashtable ht = new Hashtable();
        StringTokenizer st = new StringTokenizer(data, SPACE+EQUALS, true);
        String nTok;
        String name = null;
        String value = null;
        boolean needsValue = false;
        while (st.hasMoreTokens()) {
            nTok = st.nextToken();
            if (nTok.equals(SPACE)) continue;
            else if (nTok.equals(EQUALS)) { 
                needsValue = true;
                continue;
            }
            else {
                // if we have not found name...
                if (name == null) name = nTok;
                // else look for value...or implied attribute
                else {
                    // if we saw an equals...we need a value
                    if (needsValue) {
                        value = nTok;
                        if (value.startsWith(QUOTE) &&
                            value.endsWith(QUOTE)) {
                            value = value.substring(1, value.length()-1);
                        }
                        needsValue = false;
                        ht.put(name,value);
                        name = null;
                    }
                    // else implied value
                    else {
                        // set value to empty string
                        value = "";
                        ht.put(name,value);
                        // save current token as name of next att
                        name = nTok;
                    }
                }// end else
            } 
        } // end While more tokens        
        return ht;
    } // end parseAttributes

    public String getStylesheetHref() {
        return href;
    } //-- getStylesheetHref
    
    public String getDocumentBase() {
        return documentBase;
    } //-- getDocumentBase
    
    public void setDocumentBase(String documentBase) {
        this.documentBase = documentBase;
    } //-- setDocumentBase
}
