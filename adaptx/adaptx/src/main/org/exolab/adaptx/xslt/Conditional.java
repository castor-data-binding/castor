/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The software is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by 
 * you as a result of using the software. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or 
 * lost profits even if the Copyright owner has been advised of the 
 * possibility of their occurrence.  
 */
package org.exolab.adaptx.xslt;

import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.BooleanResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathNode;

/**
 * Represents a an interface for conditional based xsl elements
 * such as xsl:if and xsl:choose{xsl:when}
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public interface Conditional {
        
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Evaluates this Conditional using the given XPathContext.
     *
     * @param context the XPathContext to evaluate this conditional with.
     * @return the resulting BooleanResult
    **/
    public BooleanResult evaluate(XPathContext context)
        throws XPathException;
    
        

} //-- Conditional
