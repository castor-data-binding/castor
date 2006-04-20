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
 * Represents an xsl:variable
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Variable extends XSLObject {

      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new Variable
     * @param name the name of the Variable
    **/
    public Variable(String name) {
        this(name, XSLObject.VARIABLE);
    } //-- Variable
    
    /**
     * Creates a new Variable of the given type
     * @param name the name of the Variable
     * @param type the type of the Variable
    **/
    protected Variable(String name, short type) 
    {
        super(type);
        try  {
            super.setAttribute(Names.NAME_ATTR, name);
        }
        catch(XSLException xslException) {
            // this will never occur, but we need to catch it
        }
        // make 'name' read only
        makeAttrReadOnly(Names.NAME_ATTR);
    } //-- Variable
    
    
    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Returns the name of this Variable
     * @return the name of this Variable
    **/
    public String getName() {
        return getAttribute(Names.NAME_ATTR);
    } //-- getName
    
    public String toString() {
        return getName();
    }
} //-- Variable
