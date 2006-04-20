/*
 * (C) Copyright Keith Visco 1998  All rights reserved.
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


/**
 * A class for handling XML Whitespace
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class  Whitespace {
    
    /**
     * Null character
    **/
    public final static char NULL  = '\u0000';
    
    /**
     * Single space character
    **/
    public final static char SPACE = '\u0020';
    
    /**
     * Tab character
    **/
    public final static char TAB   = '\u0009';
    
    /**
     * Carriage Return character
     * Changed to '\r' instead of '\u000D' to make compatible
     * with MS J++
     * Mohan Embar
    **/
    public final static char CR    = '\r';
    
    /**
     * Linefeed character
    **/
    public final static char LF    = '\n';
    
    /**
     * Empty String
    **/
    public final static String EMPTY = "";
    
      //------------------/
     //- Public Methods -/
    //------------------/

    /**
     * Strips whitespace from the given String.
     * Newlines (#xD), tabs (#x9), and consecutive spaces (#x20) are
     * converted to a single space (#x20).
     * This method is useful for processing consective Strings since
     * any leading spaces will be converted to a single space.
     * @param data the String to strip whitespace from
    **/
    public static String stripSpace(String data) {
        return stripSpace(data, false, false);
    }
    
    /**
     * Strips whitespace from the given String.
     * Newlines (#xD), tabs (#x9), and consecutive spaces (#x20) are
     * converted to a single space (#x20).
     * @param data the String to strip whitespace from
     * @param stripAllLeadSpace, a boolean indicating whether or not to
     * strip all leading space. If true all whitespace from the start of the
     * given String will be stripped. If false, all whitespace from the start 
     * of the given String will be converted to a single space.
     * @param stripAllTrailSpace, a boolean indicating whether or not to
     * strip all trailing space. If true all whitespace at the end of the
     * given String will be stripped. If false, all whitespace at the end 
     * of the given String will be converted to a single space.
    **/
    public static String stripSpace (String data, 
        boolean stripAllLeadSpace, boolean stripAllTrailSpace) 
    {
        
        if (data == null) return data;
        
        char lastToken, token;
        char[] oldChars = data.toCharArray();
        char[] newChars = new char[oldChars.length];
        
        lastToken = NULL;
        int total = 0;
        
        // indicates we have seen at least one
        // non whitespace charater
        boolean validChar = false; 
        
        for (int i = 0; i < oldChars.length; i++) {
            token = oldChars[i];
            
            switch(token) {
                case SPACE:
                case TAB:
                    if (stripAllLeadSpace && (!validChar)) break;
                    
                    if ( (TAB != lastToken) && (SPACE != lastToken) )
                        newChars[total++] = SPACE;
                    lastToken = SPACE;
                    break;
                case CR:
                case LF:
                    if (stripAllLeadSpace && (!validChar)) break;
                    
                    //-- fix from Majkel Kretschmar
                    if ( (TAB != lastToken) && (SPACE != lastToken) )
                        newChars[total++] = SPACE;
                    //-- end fix    
                    lastToken = SPACE;
                    break;
                default:
                    newChars[total++] = token;
                    lastToken = token;
                    //-- added 19990318 to make sure we don't have
                    //-- empty text nodes
                    validChar = true;
                    break;
            }
        }
        
        //-- remove last trailing space if necessary
        if (stripAllTrailSpace) 
           if ((total > 0) && (newChars[total-1] == SPACE)) --total;
            
        if (validChar) return new String(newChars,0,total);
        else return EMPTY;
    } //-- stripSpace

    /**
     * Strips whitespace from the given String.
     * Newlines (#xD), tabs (#x9), and consecutive spaces (#x20) are
     * converted to a single space (#x20).
     * @param data the chars to strip whitespace from
     * @param stripAllLeadSpace, a boolean indicating whether or not to
     * strip all leading space. If true all whitespace from the start of the
     * given String will be stripped. If false, all whitespace from the start 
     * of the given String will be converted to a single space.
     * @param stripAllTrailSpace, a boolean indicating whether or not to
     * strip all trailing space. If true all whitespace at the end of the
     * given String will be stripped. If false, all whitespace at the end 
     * of the given String will be converted to a single space.
     * @return the new length of the array
    **/
    public static int stripSpace
        (char[] data, boolean stripAllLeadSpace, boolean stripAllTrailSpace) 
    {
        
        if (data == null) return 0;
        
        char ch, prev;
        
        prev = NULL;
        
        
        // indicates we have seen at least one
        // non whitespace charater
        boolean validChar = false; 
        
        int total = 0;
        for (int i = 0; i < data.length; i++) {
            
            ch = data[i];
            
            switch(ch) {
                case SPACE:
                case TAB:
                    if (stripAllLeadSpace && (!validChar)) break;
                    
                    if ( (TAB != prev) && (SPACE != prev) )
                        data[total++] = SPACE;
                    prev = SPACE;
                    break;
                case CR:
                case LF:
                    if (stripAllLeadSpace && (!validChar)) break;
                    
                    //-- fix from Majkel Kretschmar
                    if ( (TAB != prev) && (SPACE != prev) )
                        data[total++] = SPACE;
                    //-- end fix    
                    prev = SPACE;
                    break;
                default:
                    data[total++] = ch;
                    prev = ch;
                    //-- added 19990318 to make sure we don't have
                    //-- empty text nodes
                    validChar = true;
                    break;
            }
        }
        
        //-- remove last trailing space if necessary
        if (stripAllTrailSpace) 
           if ((total > 0) && (data[total-1] == SPACE)) --total;
            
        if (validChar) 
            return total;
        else 
            return 0;
    } //-- stripSpace
    
} //-- Whitespace
