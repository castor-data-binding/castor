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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.javasource;


/**
 * A utility class used to validate identifiers
 * and class names
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class JNaming {
    
    private static final String[] keywords = { 
        "abstract", 
        "boolean",  
        "break",
        "byte",
        "case",     
        "catch",   
        "char",   
        "class", 
        "const", 
        "continue",
        "default",  
        "do",      
        "double",
        "else",     
        "extends",
        "false",    
        "final",   
        "finally", 
        "float",
        "for",
        "goto",
        "if",
        "implements",
        "import",
        "instanceof",
        "int",
        "interface",
        "long",
        "native",
        "new",
        "null",
        "package",
        "private",
        "protected",
        "public",
        "return",
        "short",
        "static",
        "super",
        "switch",
        "synchronized",
        "this",
        "throw",
        "throws",
        "transient",
        "true",
        "try",
        "void",
        "volatile",
        "while"
    }; //-- keywords

    
    private JNaming() {
        super();
    }

    /**
     * Returns true if the given String is a Java keyword which
     * will cause a problem when used as a variable name
    **/
    public static boolean isKeyword(String name) {
        if (name == null) return false;
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].equals(name)) return true;
        }
        return false;
    } //-- isKeyword
    
    /**
     * Returns true if the given String matches the
     * production of a valid Java identifier
     *
     * @param string, the String to check the production of
     * @return true if the given String matches the
     * production of a valid Java name, otherwise false
    **/
    public static boolean isValidJavaIdentifier(String string) {
        
        if ((string == null) || (string.length() == 0)) 
            return false;
            
        char[] chars = string.toCharArray();
        
        //-- make sure starting character is valid
        if (!Character.isJavaIdentifierStart(chars[0]))
            return false;
            
        for (int i = 1; i < chars.length; i++) {
            if (!Character.isJavaIdentifierPart(chars[i]))
                return false;
        }
        if (isKeyword(string)) return false;
        return true;
    } //-- isValidJavaIdentifier
    
} //-- JNaming