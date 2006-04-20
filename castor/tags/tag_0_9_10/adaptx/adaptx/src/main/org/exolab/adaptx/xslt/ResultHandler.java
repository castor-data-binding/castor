/*
 * (C) Copyright Keith Visco 1999-2001 All rights reserved.
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

import org.xml.sax.*;

/**
 * An interface for processing XSLT result trees. This
 * class is a combination of a SAX 1.0 document handler 
 * plus some extra methods for dealing with CDATA
 * comments and entity references. This should be
 * upgraded to a SAX 2.0 Lexical Handler, when they
 * become more prominent
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public interface ResultHandler extends DocumentHandler {
    
    
    /**
     * Signals to receive CDATA characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void cdata(char[] chars, int start, int length);
    
    
    /**
     * Signals to recieve a comment
     * @param data, the content of the comment
    **/
    public void comment(String data);
    
    /**
     * Signals to recieve an entity reference with the given name
     * @param name the name of the entity reference
    **/
    public void entityReference(String name);
        
        
    /**
     * Sets the behavoir of handling character content. If argument is true,
     * character content will be escaped. If false, character content will
     * not be escaped.
     * @param escapeText the flag indicating whether or not to
     * escape character content
    **/
    //public void setEscapeText(boolean escapeText);
    
    /**
     * Sets the indent size for all formatters that perform
     * serialization, in which indentation is applicable.
     * @param indentSize the number of characters to indent
    **/
    public void setIndentSize(short indentSize);
    
    /**
     * Sets the output format information for Formatters that
     * perform serialization.
     * @param format the OutputFormat used to specify properties
     * during serialization
    **/
    public void setOutputFormat(OutputFormat format);
    
    
    /**
     * Signals to receive characters which should not be escaped
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void unescapedCharacters(char[] chars, int start, int length);
    
} //-- ResultHandler    
