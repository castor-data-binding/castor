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
 * Simple interface which provides a way to resolve "extension" functions
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public interface FunctionResolver {

      //----------------------------/
     //- Public Methods Prototypes -/
    //------------------------------/
    
    /**
     * Returns the namespaces for this FunctionResolver. These are the
     * namespaces which will be used by extension functions 
     * (within the XSLT document) that are to be resolved by this 
     * FunctionResolver,within the XSLT document.
     * <BR />
     * For Example:<BR />
     * <PRE>
     *  &lt;xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform/" 
     *                     xmlns:foo="http://my.example.com"&gt;
     *     ...
     *     &lt;xsl:template match="..."&gt;
     *         ...
     *         &lt;xsl:value-of select="foo:my-function()"&gt;
     *         ...
     *     &lt;/xsl:template&gt;
     *     ...
     *  &lt;/xsl:stylesheet&gt;
     * </PRE>
     * 
     * @return an array of namespaces supported by this FunctionResolver
     *
    **/
    public String[] getNamespaces();
    
    /**
     * Returns true if this FunctionResolver has a function
     * with the given name.
     *
     * @param namespace of the function
     * @param name the name of the function
     *
     * @returns true if this resolver has a function
    **/
    public boolean hasFunction(String namespace, String name);
    
    /**
     * Returns the FunctionCall associated with the given name
     *
     * @param namespace the namespace of the function
     * @param name the name of the function
     * @return the FunctionCall or null if no function could
     * be found.
    **/
    public XSLTFunction resolveFunction(String namespace, String name);
    

} //-- FunctionResolver