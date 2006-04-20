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

package org.exolab.castor.builder;

// imported classes and packages
import org.xml.sax.*;
import org.xml.sax.helpers.ParserFactory;

import java.io.Reader;
import org.xml.sax.HandlerBase;
import org.exolab.castor.xml.Resolver;
import org.xml.sax.AttributeList;

/**
 * The base class for unmarshallers
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public abstract class Unmarshaller extends org.xml.sax.HandlerBase {

    
    public static String DEFAULT_PARSER_CLASS = "com.ibm.xml.parsers.SAXParser";
    
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

    public Unmarshaller() {
    }

      //-----------/
     //- Methods -/
    //-----------/

    public static Parser getDefaultParser() {
        Parser parser = null;
        String parserClass = System.getProperty("org.xml.sax.parser");
        try {
            if ((parserClass == null) || (parserClass.length() == 0))
                parserClass = DEFAULT_PARSER_CLASS;
                
            parser = ParserFactory.makeParser(parserClass);
        }
        catch(java.lang.IllegalAccessException iae) {}
        catch(java.lang.ClassNotFoundException cnfe) {}
        catch(java.lang.InstantiationException ie) {};
        if (parser == null) {
            System.out.print("unable to create SAX parser: ");
            System.out.println(parserClass);
            return null;
        }
        
        return parser;
    } //-- getDefaultParser
    
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
     * Unmarshals the XML resource which is read from the given
     * reader
     * @param reader the Reader to read the XML resource from
     * @return the Object representation of the XML resource
    **/
    public abstract Object unmarshal(Reader reader) 
        throws UnmarshalException, java.io.IOException;

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

