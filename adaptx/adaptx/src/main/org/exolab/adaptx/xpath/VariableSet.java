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


package org.exolab.adaptx.xpath;


/**
 * An abstract class which allows the use of variables when
 * evaluating XPath expressions. A variable is a binding 
 * between a string name and an XPathResult.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 * @see XPathResult
 * @see XPathExpression
 */
public abstract class VariableSet 
    implements java.io.Serializable
{
    
    /**
     * Returns the value of a variable. Returns null if a variable
     * with this name was not found in this variable bindings.
     *
     * @param name The variable name
     * @return The variable's value as an XPathResult, or null
    **/
    public abstract XPathResult getVariable( String name );


} //-- VariableSet

