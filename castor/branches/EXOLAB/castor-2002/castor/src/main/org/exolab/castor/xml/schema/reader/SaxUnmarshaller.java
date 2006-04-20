/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;
import java.io.Reader;
import org.xml.sax.HandlerBase;
import org.exolab.castor.xml.Resolver;

/**
 * The base class for unmarshallers
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public abstract class SaxUnmarshaller extends org.xml.sax.HandlerBase {

    
      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The resolver to be used for resolving id references
    **/
    private Resolver _resolver;

      //----------------/
     //- Constructors -/
    //----------------/

    public SaxUnmarshaller() {
        super();
    } //-- SaxUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the name of the element that this SaxUnmarshaller
     * handles
     * @return the name of the element that this SaxUnmarshaller
     * handles
    **/
    public abstract String elementName();
    
    /**
     * Called to signal an end of unmarshalling. This method should
     * be overridden to perform any necessary clean up by an unmarshaller
    **/
    public void finish() {};
    
    /**
     * Returns the resolver used for resolving id references.
     * @return the resolver used for resolving id references.
    **/
    public Resolver getResolver() {
        return _resolver;
    } //-- getResolver
    
    /**
     * Sets the Resolver to be used for resolving id references
     * @param resolver the Resolver to be used for resolving
     * id references
    **/
    public void setResolver(Resolver resolver) {
        _resolver = resolver;
    } //-- setResolver

    /**
     * Determines if the given sequence of characters consists
     * of whitespace characters
     * @param chars an array of characters to check for whitespace
     * @param start the start index into the character array
     * @param length the number of characters to check
     * @return true if the characters specficied consist only
     * of whitespace characters
    **/
    public static boolean isWhiteSpace(char[] chars, int start, int length) {
        int max = start+length;
        for (int i = start; i < max; i++) {
            char ch = chars[i];
            switch(ch) {
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                    break;
                default:
                    return false;
            }
        }
        return true;
    } //-- isWhiteSpace
   
    /**
     * This method is called when an out of order element is encountered
    **/
    public void outOfOrder(String name) 
        throws SAXException 
    {
        StringBuffer err = new StringBuffer("out of order element <");
        err.append(name);
        err.append("> found in <");
        err.append(elementName());
        err.append(">.");
        throw new SAXException(err.toString());
    }
    
    /**
     * Converts the given String to an int
     * @param str the String to convert to an int
     * @return the int derived from the given String
     * @exception IllegalArgumentException when the given
     * String does not represent a valid int
    **/
    public static int toInt(String str) 
        throws IllegalArgumentException
    {
        try {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException nfe) {
            String err = str+" is not a valid integer. ";
            throw new IllegalArgumentException(err);
        }
    } //-- toInt
    
} //-- Unmarshaller

