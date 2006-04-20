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
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.validators;


/**
 * A class for performing simple validation
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ValidationUtils {
    
    
    //----------------/
    //- Constructors -/
    //----------------/
    
    private ValidationUtils() {
        super();
    }
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    
    /**
     * Checks the given character to determine if it is a valid
     * CombiningChar as defined by the W3C XML 1.0 Recommendation
     * @return true if the given character is a CombiningChar
    **/
    public static boolean isCombiningChar(char ch) {
        
        //-- NOTE: THIS METHOD IS NOT COMPLETE
        
        return false;
        
    } //-- isCombiningChar
    
    /**
     * @param ch the character to check
     * @return true if the given character is a digit
    **/
    public static boolean isDigit(char ch) {
        return Character.isDigit(ch);
    } //-- isDigit
    
    
    /**
     * @param ch the character to check 
     * @return true if the given character is a letter
    **/
    public static boolean isLetter(char ch) {
        return Character.isLetter(ch);
    } //-- isLetter
    
    /**
     * Checks the characters of the given String to determine if they
     * syntactically match the production of an NCName as defined
     * by the W3C XML Namespaces recommendation
     * @param str the String to check
     * @return true if the given String follows the Syntax of an NCName
    **/
    public static boolean isNCName(String str) {
        
        if ((str == null) || (str.length() == 0)) return false;
        
        
        char[] chars = str.toCharArray();
        
        char ch = chars[0];
        
        //-- make sure String starts with a letter or '_'
        if ((!isLetter(ch)) && (ch != '_')) 
            return false;
        
        for (int i = 1; i < chars.length; i++) {
            if (!isNCNameChar(chars[i])) return false;
        }
        return true;
    } //-- isNCName
    
    /**
     * Checks the the given character to determine if it is
     * a valid NCNameChar as defined by the W3C XML
     * Namespaces recommendation
     * @param ch the char to check
     * @return true if the given char is an NCNameChar
    **/
    public static boolean isNCNameChar(char ch) {
        if (isLetter(ch) || isDigit(ch)) return true;
        if (isExtender(ch) || isCombiningChar(ch)) return true;
        switch(ch) {
            case '.':
            case '-':
            case '_':
                return true;
            default:
                return false;
        }
    } //-- isNCNameChar
    
    
    /**
     * Checks the characters of the given String to determine if they
     * syntactically match the production of an NMToken
     * @param str the String to check
     * @return true if the given String follows the Syntax of an NMToken
    **/
    public static boolean isNMToken(String str) {
        
        if (str == null) return false;
        char[] chars = str.toCharArray();
        
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (isLetter(ch) || isDigit(ch)) continue;
            if (isExtender(ch) || isCombiningChar(ch)) continue;
            switch(ch) {
                case '.':
                case '-':
                case '_':
                case ':':
                    break;
                default:
                    return false;
            }
        }
        return true;
    } //-- isNMToken
    
    /**
     * Returns true if the given character is a valid XML Extender
     * character, according to the XML 1.0 specification
     * @param ch the character to check
     * @return true if the character is a valid XML Extender character
    **/
    public static boolean isExtender(char ch) {
        
        if ((ch >= 0x3031) && (ch <= 0x3035)) return true;
        if ((ch >= 0x30FC) && (ch <= 0x30FE)) return true;
        
        switch(ch) {
            case 0x00B7:
            case 0x02D0:
            case 0x02D1:
            case 0x0387:
            case 0x0640:
            case 0x0E46:
            case 0x0EC6:
            case 0x3005:
            case 0x309D:
            case 0x309E:
                return true;
            default:
                break;
        }
        return false;
    } //-- isExtender
    
    /**
     * Test 
    **
    public static void main(String[] args) {
        System.out.println("0x00B7: " + (char)0x00B7);
    }
    /* */
    
} //-- Validator


