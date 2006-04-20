/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 */
package org.exolab.adaptx.xml;

import java.util.Locale;

/**
 * A utility class for parsing XML Namespaces and Locales
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class XMLUtil {
    
    public static final String XMLNS        = "xmlns";
    
    public static final char LOCAL_SEP      = '-';
    public static final char NAME_SPACE_SEP = ':';
    
    public static final String EMPTY_STRING = "";
    
    
    /**
     * Creates a new Locale base on the value of the lang attribute
     * @param lang the lang attribute to use when creating a Locale
     * @return the Local base on the lang attribute
    **/
    public static Locale getLocale(String xmlLang) {
        
        Locale defLocale = Locale.getDefault();
        
        if (xmlLang == null) return defLocale;
        
        int sepIdx = xmlLang.indexOf(LOCAL_SEP);
        
        String language = null;
        String country  = defLocale.getCountry();
       
        // get specified language
        if (sepIdx > 0) {
            language = xmlLang.substring(0,sepIdx);
            if (sepIdx < (xmlLang.length()-1))
                country = xmlLang.substring(sepIdx+1);
            
        }
        else language = xmlLang;
        
        return new Locale(language, country);
        
    } //-- getLocal
    
    /**
     * Returns the local part of the qualified XML name
     * @param qName the qualified XML name
     * @return the local part of the qualified XML name
    **/
    public static String getLocalPart(String qName) {
        if (qName == null) return EMPTY_STRING;
        
        int idx = qName.indexOf(NAME_SPACE_SEP);
        
        if (idx >= 0) {
            return qName.substring(idx+1);
        }
        return qName;
    } //-- getLocalPart
    
    /**
     * Returns the namespace part of the qualified XML name
     * @param qName the qualified XML name
     * @return the namespace part of the qualified XML name
    **/
    public static String getNameSpacePrefix(String qName) {
        
        if (qName == null) return EMPTY_STRING;
        
        int idx = qName.indexOf(NAME_SPACE_SEP);
        
        if (idx > 0) {
            return qName.substring(0,idx);
        }
        return EMPTY_STRING;
    } //-- getNameSpacePrefix
    
    /**
     * Returns true if the given String contains only whitespace
     * characters
     * @return true if the given String contains only whitespace
     * characters, otherwise false.
    **/
    public static boolean isWhitespace(String text) {
        for (int i = 0; i < text.length(); i++) {
            switch (text.charAt(i)) {
                case ' ' :
                case '\n':
                case '\t':
                case '\r':
                    break;
                default:
                    return false;
            }
        }
        return true;
    } //-- isWhitespace
    
    /**
     * Returns true if the given String contains only whitespace
     * characters
     * @return true if the given String contains only whitespace
     * characters, otherwise false.
    **/
    public static boolean isWhitespace(char[] chars, int start, int length) {
        
        //
        int stopIdx = start+length;
        for (int i = start; i < stopIdx; i++) {
            switch (chars[i]) {
                case ' ' :
                case '\n':
                case '\t':
                case '\r':
                    break;
                default:
                    return false;
            }
        }
        return true;
    } //-- isWhitespace
    
} //-- XMLUtil
