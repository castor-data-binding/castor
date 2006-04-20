/*
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
 * The Original Code is XSL:P XSLT processor.
 * 
 * The Initial Developer of the Original Code is Keith Visco.
 * Portions created by Keith Visco (C) 1998-2001 Keith Visco.
 * All Rights Reserved..
 *
 * Contributor(s): 
 * Keith Visco, kvisco@ziplink.net
 *    -- original author. 
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.adaptx.util;

/**
 * Splits a String into tokens using a specified set of
 * String delimiters. Delimeters are searched for in the
 * that they appear in the delimiter list.
 * If one delimiter is part of another delimiter,
 * make sure you add them in the proper order for correct behavoir
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Tokenizer {

      //-------------------/
     //- Class Variables -/
    //-------------------/
    
    /**
     * The set of delimiters to use, if none are specified
    **/
    public static final String[] DEFAULT_DELIMITERS =
        {" ", "\n", "\r","\t"};

      //----------------------/
     //- Instance Variables -/
    //----------------------/
    
    /**
     * the delimiters of this Tokenizer
    **/
    private String[] delimiters;

    /**
     * the tokens of this Tokenizer
    **/
    protected List tokens;

    /**
     * the current index into the tokens List
    **/
    private int currentIndex = 0;

    private String ntoken = null;
    
    
      //---------------/
     //- Contructors -/
    //---------------/

    /**
     * Creates a new Tokenizer using the given String and the
     * default set of delimiters. The default delimiters are:
     *   " ", "\n", "\r", and "\t";
    **/
    public Tokenizer(String pattern) {
        super();
        tokens = new List();
        this.delimiters = DEFAULT_DELIMITERS;
        parsePattern(pattern);

    } //-- Tokenizer

    /**
     * Creates a new Tokenizer using the given String and the
     * default set of delimiters
    **/
    public Tokenizer(String pattern, String[] delimiters) {
        super();
        tokens = new List();
        this.delimiters = delimiters;
        parsePattern(pattern);

    } //-- Tokenizer

      //------------------/
     //- Public Methods -/
    //------------------/

    /**
     * Advances the index of this tokenizer ahead by the given offset
    **/
    public void advance(int offset) {
        int idx = currentIndex+offset;
        if ((idx >= 0) && (idx < tokens.size())) 
            currentIndex = idx;
        else
            currentIndex = tokens.size();
    } //-- lookAhead 

    /**
     * Counts the number of times nextToken can be called without
     * returning null
    **/
    public int countTokens() {
        return tokens.size() - currentIndex;
    }
    
    public int getPosition() {
        return currentIndex;
    } //-- getPosition

    /**
     * Determines if there are any tokens available
     * @return true if there are tokens available, otherwise false
    **/
    public boolean hasMoreTokens() {
        return (countTokens() > 0);
    } //-- hasMoreTokens

    /**
     * Determines if the specified token is contained in
     * the token list of this Tokenizer
     * @param token the String to look for in the token list
     * @return true if the String argument is contained
     *  in this Tokenizer's token list, otherwise false
    **/
    public boolean hasToken(String token) {
        return (tokens.contains(token));
    } //-- hasMoreTokens

    /**
     * Determines if the specified token is a delimiter for
     * this Tokenizer
     * @param token the String to compare to the delimiters
     * @return true if the String argument is a delimiter
    **/
    public boolean isDelimiter(String token) {
        if (token != null) {
            for (int i = 0; i < delimiters.length; i++) {
                if (token.equals(delimiters[i]))
                    return true;
            }
        }
        return false;
    } //-- isDelimiter
    
    /**
     * Allows looking ahead for tokens without affecting the
     * token sequence as called by nextToken or previousToken
    **/
    public String lookAhead(int offset) {
        int idx = currentIndex+offset;
        if ((idx >= 0) && (idx < tokens.size())) 
            return (String)tokens.get(idx);
        else
            return null;
    } //-- lookAhead 
    
    /**
     * Retrieves the next available token
     * @return the next available token or null if there are none
    **/
    public String nextToken() {
        if (currentIndex < tokens.size()) {
            return (String) tokens.get(currentIndex++);
        }
        return null;
    }

    /**
     * Retrieves the previous token
     * @return the previous token or null if a previous token is
     * not available
    **/
    public String previousToken() {
        if (currentIndex > 0) {
            return (String) tokens.get(--currentIndex);
        }
        return null;
    }
    
    /**
     * Resets the position of the token pointer to the beginning
    **/
    public void resetPosition() {
        currentIndex = 0;
    } //-- resetPosition
    
    /**
     * Sets the current position of this tokenizer. 
     * @param position the index value to set the current position to.
     * if position is greater than the number of tokens, the tokenizer
     * is advanced to the end. If the given position is less than 0,
     * the tokenizer is positioned at the beginning
    **/
    public void setPosition(int position) {
        if ((position >= 0) && (position < tokens.size())) 
            currentIndex = position;
        else if (position < 0) {
            currentIndex = 0;
        }
        else
            currentIndex = tokens.size();
    } //-- setPosition
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tokens.size(); i++) {
            sb.append(tokens.get(i));
        }
        return sb.toString();
    } //-- toString
    
      //---------------------/
     //- Protected Methods -/
    //---------------------/

    protected void setTokens(List newTokens) {
        currentIndex = 0;
        this.tokens = newTokens;
    } //-- setTokens
    
      //-------------------/
     //- Private Methods -/
    //-------------------/


    private int indexOfNearestToken(String tokenString) {
        int index = -1;
        if (tokenString == null) return index;
        
        String delim;
        String pDelim = "";
        int idx;
        for (int i = 0; i < delimiters.length; i++) {
            delim = delimiters[i];
            idx = tokenString.indexOf(delim);
            if (idx >= 0) {
                if ((index < 0) || (idx < index)) {
                    index = idx;
                    pDelim = delim;
                }
                else if (idx == index) {
                    // choose bigger...if equal..take previous
                    if (delim.length() > pDelim.length()) 
                        pDelim = delim;
                }
            }
        } //--
        
        // set ntoken
        ntoken = pDelim;
        
        // return index
        return index;
    } //-- indexOfNearestToken

    /**
     * Parses the given String into tokens and adds them into
     * the tokens List
    **/
    private void parsePattern(String pattern) {
        
        if (pattern == null) return;
        
        int index = -1;

        while((index = indexOfNearestToken(pattern)) >= 0) {
            // Found Token
            if (index != 0) {
                tokens.add(pattern.substring(0,index));
            }
            tokens.add(ntoken);
            pattern = pattern.substring(index+ntoken.length());
        }
        // add remaining pattern
        if (pattern.length() > 0) tokens.add(pattern);
        
    } //-- parsePattern
    
} //-- Tokenizer



