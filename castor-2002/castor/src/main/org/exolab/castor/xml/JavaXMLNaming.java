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

package org.exolab.castor.xml;

import java.io.File;
import java.util.Hashtable;

/**
 * This class converts XML Names to proper Java names.
 * Also see Unmarshaller and Marshaller since they use some
 * of their own methods for now.
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JavaXMLNaming {
    
    
    private static final Hashtable subst = keywordMap();
    
    private static final String[] keywords = { 
        "class", "static"
    };
        
    /**
     * private constructor
    **/
    private JavaXMLNaming() {
        super();
    } //-- JavaXMLNaming
    
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
    
    public static String toJavaClassName(String name) {
                
        if ((name == null) && (name.length() <= 0)) {
           // handle error 
           return name; //-- for now just return name
        }        
        return toJavaName(name, true);
        
    } //-- toJavaClassName

    public static String toJavaMemberName(String name) {
        
        if (name == null) return null;
        
        String memberName = toJavaName(name, false);
        if (isKeyword(memberName)) {
            String mappedName = (String) subst.get(memberName);
            if (mappedName != null) memberName = mappedName;
            else memberName = "_"+memberName;
        }
        return memberName;
    } //-- toJavaMemberName
    
    /**
     * Converts the given Package name to it's corresponding
     * Path. The path will be a relative path.
    **/
    public static String packageToPath(String packageName) {
        if (packageName == null) return packageName;
        return packageName.replace('.',File.separatorChar);
    } //-- packageToPath
    
    private static Hashtable keywordMap() {
        Hashtable ht = new Hashtable();
        ht.put("class", "type");
        return ht;
    } //-- keywordMap
    
    /**
     * Converts the given xml name to a Java name.
     * @param name the name to convert to a Java Name
     * @param upperFirst a flag to indicate whether or not the
     * the first character should be converted to uppercase. 
    **/
    private static String toJavaName(String name, boolean upperFirst) {
        
        int size = name.length();
        char[] ncChars = name.toCharArray();
        int next = 0;
        
        boolean uppercase = upperFirst;
        
        for (int i = 0; i < size; i++) {
            char ch = ncChars[i];
            
            switch(ch) {
                case ':':
                case '-':
                case '_':
                    uppercase = true;
                    break;
                default:
                    if (uppercase) {
                        ncChars[next] = Character.toUpperCase(ch);
                        uppercase = false;
                    }
                    else ncChars[next] = ch;
                    ++next;
                    break;
            }
        }
        return new String(ncChars,0,next);
    } //-- toJavaName
    

    /* for debuging *
    public static void main(String[] args) {
        
        String[] names = new String[4];
        names[0] = "name";
        names[1] = "myName";
        names[2] = "my-name";
        names[3] = "my----name";
        
        System.out.println("JavaXMLNaming Tests: ");
        System.out.println();
        for (int i = 0; i < names.length; i++) {
            System.out.println();
            System.out.print("Test #");
            System.out.println(i+1);
            System.out.print("toJavaClassName(\"");
            System.out.print(names[i]);
            System.out.print("\") ==> \"");
            System.out.print(toJavaClassName(names[i]));
            System.out.println("\"");
            System.out.println();
            System.out.print("toJavaMemberName(\"");
            System.out.print(names[i]);
            System.out.print("\") ==> \"");
            System.out.print(toJavaMemberName(names[i]));
            System.out.println("\"");
            
        }
    } //-- main /* */
    
} //-- JavaXMLNaming
