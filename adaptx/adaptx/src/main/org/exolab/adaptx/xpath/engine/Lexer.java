/*
 * (C) Copyright Keith Visco 1999-2003  All rights reserved.
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


package org.exolab.adaptx.xpath.engine;


/**
 * A Lexical Analizer of XPath patterns and expressions
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Lexer {
    
    
    private static final String INVALID_AXIS_IDENTIFIER
        = "invalid axis identifier";
        
    
    //-- delimiters
    public static final char FORWARD_SLASH   = '/';
    public static final char L_PAREN         = '(';
    public static final char R_PAREN         = ')';
    public static final char L_BRACKET       = '[';
    public static final char R_BRACKET       = ']';
    public static final char PERIOD          = '.';
    public static final char COMMA           = ',';
    public static final char AT_SYMBOL       = '@';
    public static final char DOLLAR_SYMBOL   = '$';
    public static final char S_QUOTE         = '\'';
    public static final char D_QUOTE         = '\"';
    public static final char VERT_BAR        = '|';
    public static final char COLON           = ':';
    
    //-- Expression whitespace
    public static final char SPACE           = ' ';
    public static final char TAB             = '\t';
    public static final char LF              = '\n';
    public static final char CR              = '\r';
    //-- simple operators
    public static final char NEGATION_OP     = '!';
    public static final char EQUALS_OP       = '=';
    public static final char ADDITION_OP     = '+';
    public static final char SUBTRACTION_OP  = '-';
    public static final char LESS_THAN_OP    = '<';
    public static final char GREATER_THAN_OP = '>';
    public static final char MULTIPLY_OP     = '*';
    
    /**
     * delimiter set
    **/
    private static final char[] delimiters = {
        FORWARD_SLASH, VERT_BAR,
        L_PAREN,       R_PAREN, 
        L_BRACKET,     R_BRACKET,
        PERIOD,        COMMA, 
        AT_SYMBOL,     DOLLAR_SYMBOL, 
        S_QUOTE,       D_QUOTE,
        COLON,
        
        //-- whitespace
        SPACE, TAB, CR, LF,
        
        //-- operator tokens
        NEGATION_OP,
        EQUALS_OP, 
        ADDITION_OP,
        SUBTRACTION_OP,
        MULTIPLY_OP,
        LESS_THAN_OP,
        GREATER_THAN_OP
    }; //-- delimiters
    
    /**
     * the delimiter set of an ExprLexer
    **/
    public static final Token[] tokenSet = 
    { 
        
        //-- Operators
        new Token("and", Token.AND_OPNAME),
        new Token("or",  Token.OR_OPNAME),
        new Token("mod", Token.MOD_OPNAME),
        new Token("div", Token.DIV_OPNAME),
        new Token("quo", Token.QUO_OPNAME),

        //-- NodeType tokens
        new Token("comment",                Token.COMMENT),
        new Token("processing-instruction", Token.PI),
        new Token("node",                   Token.NODE),
        new Token("text",                   Token.TEXT),
        
        //-- basic
        new Token("*",   Token.WILDCARD),
        
        //-- Axis Identifiers
        new Token("ancestor",           Token.ANCESTORS_AXIS),
        new Token("ancestor-or-self",   Token.ANCESTORS_OR_SELF),
        new Token("attribute",          Token.ATTRIBUTES_AXIS ),
        new Token("child",              Token.CHILDREN_AXIS),
        new Token("descendant",         Token.DESCENDANTS_AXIS),
        new Token("descendant-or-self", Token.DESCENDANTS_OR_SELF),
        new Token("following",          Token.FOLLOWING_AXIS),
        new Token("following-sibling",  Token.FOLLOWING_SIBLINGS_AXIS),
        new Token("namespace",          Token.NAMESPACE_AXIS),
        new Token("parent",             Token.PARENT_AXIS),
        new Token("preceding",          Token.PRECEDING_AXIS),
        new Token("preceding-sibling",  Token.PRECEDING_SIBLINGS_AXIS),
        new Token("self",               Token.SELF_AXIS),
    };
    
    
    private TokenItem first    = null;
    private TokenItem current  = null;
    private TokenItem prev     = null;
    private TokenItem last     = new TokenItem(new Token(null, Token.NULL));
    
      //---------------/
     //- Contructors -/
    //---------------/
    
    /**
     * Creates a new ExprLexer using the given String
    **/
    public Lexer(String pattern) throws ParseException
    {
        super();
        parse(pattern);
    } //-- ExprLexer

    /**
     * Counts the number of times nextToken can be called without
     * returning null
    **/
    public int countTokens() {
        if (current == null) return 0;
        
        TokenItem item = current;
        
        int c = 1;
        while ((item = item.nextItem) != null) ++c;
        return c;
        
    } //-- countTokens;

    /**
     * Determines if there are any tokens available
     * @return true if there are tokens available, otherwise false
    **/
    public boolean hasMoreTokens() {
        return (current != null);
    } //-- hasMoreTokens

    /**
     * Determines if the specified char is a delimiter
     * @param ch the char to compare to the delimiters
     * @return true if the String argument is a delimiter
    **/
    public boolean isDelimiter(char ch) {
        for (int i = 0; i < delimiters.length; i++)
            if (ch == delimiters[i]) return true;
        return false;
    } //-- isDelimiter
    
    public boolean isOperator(Token token) {
        if (token == null) return false;
        if (isBinaryOp(token)) return true;
        switch (token.type) {
            case Token.PARENT_OP:
            case Token.ANCESTOR_OP:
            case Token.UNION_OP:
                return true;
            default:
                return false;
        }
    } //-- isOperator

    public static boolean isAdditiveOp(Token token) {
        if (token == null) return false;
        switch (token.type) {
            case Token.ADDITION_OP:
            case Token.SUBTRACTION_OP:
                return true;
            default:
                return false;
        }
    } //-- isAdditiveOp
    
    public static boolean isAxisIdentifier(Token token) {
        if (token == null) return false;

        switch(token.type) {
            case Token.ANCESTORS_AXIS:
            case Token.ANCESTORS_OR_SELF:
            case Token.ATTRIBUTES_AXIS:
            case Token.CHILDREN_AXIS:
            case Token.DESCENDANTS_AXIS:
            case Token.DESCENDANTS_OR_SELF:
            case Token.FOLLOWING_AXIS:
            case Token.FOLLOWING_SIBLINGS_AXIS:
            case Token.NAMESPACE_AXIS:
            case Token.PARENT_AXIS:
            case Token.PRECEDING_AXIS:
            case Token.PRECEDING_SIBLINGS_AXIS:
            case Token.SELF_AXIS:
                return true;
            default:
                return false;
        }
    } //-- isAxisIdentifier
    
    public static boolean isBinaryOp(Token token) {
        if (token == null) return false;
        
        switch (token.type) {
            case Token.AND_OPNAME:
            case Token.OR_OPNAME:
            case Token.MOD_OPNAME:
            case Token.DIV_OPNAME:
            case Token.QUO_OPNAME:
            case Token.ADDITION_OP:
            case Token.SUBTRACTION_OP:
            case Token.EQUALS_OP:
            case Token.NOT_EQUALS_OP:
            case Token.MULTIPLY_OP:
            case Token.LESS_THAN_OP:
            case Token.LESS_OR_EQ_OP:
            case Token.GREATER_THAN_OP:
            case Token.GREATER_OR_EQ_OP:
                return true;
            default:
                return false;
        }
        
    } //-- isBinaryOp

    public static boolean isEqualityOp(Token token) {
        if (token == null) return false;
        switch (token.type) {
            case Token.EQUALS_OP:
            case Token.NOT_EQUALS_OP:
                return true;
            default:
                return false;
        }
    } //-- isEqualityOp

    public static boolean isRelationalOp(Token token) {
        if (token == null) return false;
        
        switch (token.type) {
            case Token.LESS_THAN_OP:
            case Token.LESS_OR_EQ_OP:
            case Token.GREATER_THAN_OP:
            case Token.GREATER_OR_EQ_OP:
                return true;
            default:
                return false;
        }
    } //-- isRelationalOp

    public static boolean isMultiplicativeOp(Token token) {
        if (token == null) return false;
        switch (token.type) {
            case Token.MOD_OPNAME:
            case Token.DIV_OPNAME:
            case Token.QUO_OPNAME:
            case Token.MULTIPLY_OP:
                return true;
            default:
                return false;
        }
    } //-- isMultiplicativeOp
    
    /**
     * Returns true if the char argument is a digit
     * @return true if the char argument is a digit
    **/
    public static boolean isDigit(char ch) {
        return ((ch >= '0') && (ch <= '9'));
    } //-- isDigit
    
    /**
     * Returns true if the char argument is a letter
     * @return true if the char argument is a letter
    **/
    public static boolean isLetter(char ch) {
        if ((ch >= 'a' ) && (ch <= 'z' )) return true;
        if ((ch >= 'A' ) && (ch <= 'Z' )) return true;
        return false;
    } //-- isLetter
    
    /**
     * Returns true if the char argument is an NCNameChar,
     * as defined by the XML Namespaces recommendation
     * http://www.w3c.org/TR/1999/REC-xml-names-199904114
     * @return true if the char argument is an NCNameChar
    **/
    public static boolean isNCNameChar(char ch) {
        if (isLetter(ch)) return true;
        if (isDigit(ch))  return true;
        return ((ch == '.') ||(ch == '_') || (ch == '-'));
    } //-- isNCNameChar
    
    /**
     * Returns true if the char argument is an QName character,
     * as defined by the XML Namespaces recommendation
     * http://www.w3c.org/TR/1999/REC-xml-names-199904114
     * @return true if the char argument is an QName character
    **/
    public static boolean isQNameChar(char ch) {
        return (isNCNameChar(ch) || (ch == ':'));
    } //-- isQNameChar
    
    public static boolean isWhitespace(char ch) {

        switch(ch) {
            case SPACE:
            case TAB:
            case LF:
            case CR:
                return true;
            default:
                return false;
        }
    } //-- isWhitespace
    
    
    /**
     * Allows looking ahead for tokens without affecting the
     * token sequence as called by nextToken or previousToken.
     * If offset based on the next token, so an offset of 0 will 
     * @param offset the number of tokens to lookAhead
     * @return the next token
     * @exception IllegalArgumentException if offset is less than 0.
    **/
    public Token lookAhead(int offset) 
        throws IllegalArgumentException
    {
        if (offset < 0) return null;

        TokenItem item = current;
        
        for (int i = 0; i < offset; i++) {
            if (item != null) {
                item = item.nextItem;
            }
            else return null;
        }
        if (item == null) return null;
        return item.token;
    } //-- lookAhead 
    
    /**
     * Retrieves the next available token
     * @return the next available token or null if there are none
    **/
    public Token nextToken() {
        prev = current;
        if (current != null) current = current.nextItem;
        if (prev != null) return prev.token;
        return null;
    } //-- nextToken

    /**
     * Moves the position of this Lexer back one
    **/
    public void pushBack() {
        if (current != first) {
            current = prev;
            prev = prev.prevItem;
        } 
    } //-- pushBack
    
    /**
     * Resets the position of the token pointer to the beginning
    **/
    public void resetPosition() {
        current = first;
    } //-- resetPosition
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        TokenItem item = first;
        while (item != null) {
            sb.append(item.token);
            item = item.nextItem;
        }
        return sb.toString();
    } //-- toString
    
    public String toStringPrevious() {
        StringBuffer sb = new StringBuffer();
        TokenItem item = first;
        while ((item != null) && (item != current)) {
            sb.append(item.token);
            item = item.nextItem;
        }
        return sb.toString();
    } //-- toStringPrevious
    
    public String toStringRemainder() {
        StringBuffer sb = new StringBuffer();
        TokenItem item = current;
        while (item != null) {
            sb.append(item.token);
            item = item.nextItem;
        }
        return sb.toString();
    } //-- toStringRemainder
    
      //-------------------/
     //- Private Methods -/
    //-------------------/

    private void addToken(Token token) {
        
        TokenItem item = new TokenItem(token);
        
        if (first == null) {
            current = item;
            first = item;
            last = item;
        }
        else {
            item.prevItem = last;
            last.nextItem = item;
            last = item;
        }
    } //-- addToken
    
    private Token delimiterToken(char ch) {
        
        switch(ch) {
            case FORWARD_SLASH:
                return new Token(null, Token.PARENT_OP);
            case VERT_BAR:
                return new Token(null, Token.UNION_OP);
            case L_PAREN:
                return new Token(null, Token.L_PAREN);
            case R_PAREN:
                return new Token(null, Token.R_PAREN);
            case L_BRACKET:
                return new Token(null, Token.L_BRACKET);
            case R_BRACKET:
                return new Token(null, Token.R_BRACKET);
            case PERIOD:
                return new Token(null, Token.PERIOD);
            case COMMA:
                return new Token(null, Token.COMMA);
            case AT_SYMBOL:
                return new Token(null, Token.AT_SYMBOL);
            case EQUALS_OP:
                return new Token(null, Token.EQUALS_OP);
            case ADDITION_OP:
                return new Token(null, Token.ADDITION_OP);
            case SUBTRACTION_OP:
                return new Token(null, Token.SUBTRACTION_OP);
            case MULTIPLY_OP:
                return new Token(null, Token.MULTIPLY_OP);
            case LESS_THAN_OP:
                return new Token(null, Token.LESS_THAN_OP);
            case GREATER_THAN_OP:
                return new Token(null, Token.GREATER_THAN_OP);
            default:
                break;
        }
        return null;
    } //-- delimiterToken
    
    private void freeBuffer(StringBuffer buffer, char ch) {

        if (buffer.length() > 0) {
            String tokenString = buffer.toString();
            Token tok = match(tokenString, ch);
            if (tok != null) {
                if (isAxisIdentifier(tok)) {
                    if (ch == COLON) addToken(tok);
                    else tok = null;
                }
                else {
                    //-- If we have a text-based numeric operator, make sure
                    //-- the previous Token is numeric or a function call.
                    //-- otherwise we switch the token to a CNAME
                    if (isMultiplicativeOp(tok)) {
                        if ((last == null) || 
                            (last.token.type == Token.NULL) ||
                            (last.token.type == Token.AT_SYMBOL) ||
                            (last.token.type == Token.L_PAREN)   ||
                            (last.token.type == Token.L_BRACKET) ||
                            isOperator(last.token)) 
                        {
                            tok.type = Token.CNAME;
                        }
                    }
                    //-- If we have a text-based 'and' or 'or' operators
                    //-- make sure we are not in the middle of a path
                    //-- expression, otherwise switch to CNAME
                    else if ((tok.type == Token.AND_OPNAME) || 
                             (tok.type == Token.OR_OPNAME)) 
                    {
                        if ((last == null) || 
                            (last.token.type == Token.NULL) ||
                            isOperator(last.token)) 
                        {
                            tok.type = Token.CNAME;
                        }
                    }
                    else {
                        //-- Check for attribute short-hand
                        if ((last != null)  && (last.token.type == Token.AT_SYMBOL)) {
                            if (tok.type != Token.WILDCARD) {
                                tok.type = Token.CNAME;
                            }
                        }
                    }
                    addToken(tok);
                }
            }
            
            if (tok == null)
                addToken( new Token(tokenString, Token.CNAME));
                
            buffer.setLength(0);
        }
    } //-- freeBuffer

    /**
     * Parses the given String into tokens and adds them into
     * the tokens List
     * @param pattern the String to parse
    **/
    private void parse(String pattern) 
        throws ParseException
    {
        
        boolean inLiteral = false;
        boolean inNumber  = false;
        boolean inVarRef  = false;
        
        StringBuffer tokBuf = new StringBuffer();
        //List masterSet = new List(tokenSet.length);
        
        //for (int i = 0; i < tokenSet.length; i++)
        //    masterSet.add(tokenSet[i]);
         
        //List matches = masterSet;
        
        char ch     = '\0';
        char[] chars = pattern.toCharArray();
        char endQuote = S_QUOTE;
        int currentIdx = 0;
        
        while (currentIdx < chars.length) {
            
            char prevCh = ch;
            ch = chars[currentIdx++];

            //-- handle literals
            if (inLiteral) {
                switch(ch) {
                    case S_QUOTE:
                    case D_QUOTE:
                        if (ch == endQuote) {
                            inLiteral = false;
                            addToken(new Token(tokBuf.toString(), 
                                                    Token.LITERAL));
                            tokBuf.setLength(0);
                            break;
                        }
                        //-- do not break;
                    default:
                        tokBuf.append(ch);
                        break;
                }
            }
            //-- handle current number
            else if (inNumber) {
                if (isDigit(ch) || (ch == '.')) {
                    tokBuf.append(ch);
                }
                else {
                    inNumber = false;
                    addToken(new Token(tokBuf.toString(), Token.NUMBER));
                    tokBuf.setLength(0);
                    --currentIdx;
                    /*
                    if (isWhitespace(ch)) continue;
                    
                    Token tok = delimiterToken(ch);
                    if (tok == null) {
                        addToken(new Token(tokBuf.toString(), Token.ERROR));
                        tokBuf.setLength(0);
                    }
                    else if (isOperator(tok)) addToken(tok);
                    else if (tok.type == Token.R_PAREN) addToken(tok);
                    else if (tok.type == Token.R_BRACKET) addToken(tok);
                    else if (tok.type == Token.COMMA) addToken(tok);
                    else {
                        addToken(new Token(tokBuf.toString(), Token.ERROR));
                        tokBuf.setLength(0);
                        break;
                    }
                    */
                }
            }
            else if (inVarRef) {
                
                if (ch == '-') {
                    //-- lookAhead
                    char next = '\0';
                    if (currentIdx<chars.length) next = chars[currentIdx];
                    if (!isNCNameChar(next)) inVarRef = false;
                    else tokBuf.append(ch);
                }
                else if (isNCNameChar(ch)) tokBuf.append(ch);
                else inVarRef = false;
                
                //-- clean up buffer
                if (!inVarRef) {
                    addToken(new Token(tokBuf.toString(), 
                        Token.VAR_REFERENCE));
                    tokBuf.setLength(0);
                    --currentIdx;
                }
            }
            else if (isDelimiter(ch)) {
                
                Token tok = null;
                
                switch (ch) {
                    
                    //-- ignore whitespace
                    case SPACE:
                    case TAB:
                    case CR:
                    case LF:
                        freeBuffer(tokBuf, ch);
                        break;
                    case L_PAREN:
                        freeBuffer(tokBuf, ch);
                        if (last.token.type == Token.CNAME) {
                            last.token.type = Token.FUNCTION_NAME;
                        }
                        addToken(new Token(null, Token.L_PAREN));
                        break;
                    case R_PAREN:
                        freeBuffer(tokBuf, ch);
                        addToken(new Token(null, Token.R_PAREN));
                        break;
                    case S_QUOTE:
                    case D_QUOTE:
                        freeBuffer(tokBuf, ch);
                        inLiteral = true;
                        endQuote = ch;
                        break;
                    case FORWARD_SLASH: 
                        freeBuffer(tokBuf, ch);
                        switch(last.token.type) {
                            case Token.PARENT_OP:
                                last.token.type = Token.ANCESTOR_OP;
                                break;
                            case Token.ANCESTOR_OP:
                                throw new ParseException(pattern,
                                    "too many '/'", currentIdx);
                            default:
                                addToken(new Token(null, Token.PARENT_OP));
                                break;
                        }
                        break;
                    case PERIOD: 
                    
                        if (tokBuf.length() > 0)
                            tokBuf.append(ch);
                        else {
                            switch (last.token.type) {
                                case Token.SELF_NODE:
                                    last.token.type = Token.PARENT_NODE;
                                    break;
                                case Token.PARENT_NODE:
                                    throw new ParseException(pattern,
                                        "too many '.'", currentIdx);
                                default:
                                    addToken(new Token(null, Token.SELF_NODE));
                                    break;
                            }
                        }
                        break;
                    case COLON:
                        //-- look ahead '::' denotes AxisIdentifier
                        if (chars.length > currentIdx) {
                            char nextChar = chars[currentIdx];
                            if (nextChar == COLON) {
                                freeBuffer(tokBuf, ch);
                                if (isAxisIdentifier(last.token))
                                    ++currentIdx;
                                else {
                                    throw new ParseException(pattern,
                                         INVALID_AXIS_IDENTIFIER, currentIdx);
                                }
                            }
                            else if (nextChar == '*') {
                                tokBuf.append(ch);
                                tokBuf.append('*');
                                ++currentIdx;
                            }
                            else tokBuf.append(ch);
                        }
                        else 
                            throw new ParseException(pattern,
                                "missing remainder of expression", currentIdx);
                        break;
                    case DOLLAR_SYMBOL:
                        freeBuffer(tokBuf, ch);
                        inVarRef = true;
                        //addToken(new Token(null, Token.VAR_REFERENCE));
                        break;
                    case AT_SYMBOL:
                        freeBuffer(tokBuf, ch);
                        addToken(new Token("@", Token.AT_SYMBOL));
                        break;
                    case NEGATION_OP:
                        freeBuffer(tokBuf, ch);
                        
                        //-- look ahead (requires '=')
                        if (chars.length > currentIdx) {
                            char nextChar = chars[currentIdx++];
                            if (nextChar != EQUALS_OP)
                                throw new ParseException(pattern,
                                        "missing '=' after '!'", currentIdx);
                        }
                        else throw new ParseException(pattern,
                                    "illegal end of expression", currentIdx);
                                    
                        addToken(new Token("!=", Token.NOT_EQUALS_OP));
                        break;
                    case EQUALS_OP:
                    
                        if (tokBuf.length() == 0) {
                            if (last.token.type == Token.LESS_THAN_OP) {
                                last.token.type = Token.LESS_OR_EQ_OP;
                                break;
                            }
                            if (last.token.type == Token.GREATER_THAN_OP) {
                                last.token.type = Token.GREATER_OR_EQ_OP;
                                break;
                            }
                        }
                        //-- important no break here
                    case MULTIPLY_OP:
                    case ADDITION_OP:
                    case SUBTRACTION_OP:
                    case LESS_THAN_OP:
                    case GREATER_THAN_OP:

                        if ((tokBuf.length() > 0) && (ch != '-' )) {
                            freeBuffer(tokBuf, ch);
                            addToken( delimiterToken(ch) );
                            break;
                        }
                        
                        //-- Check for operators. An Operator token can
                        //-- only be matched if there is a preceding token,
                        //-- and that token is not @,(,[, or an Operator
                        if ((!isOperator(last.token)) && 
                            (!isAxisIdentifier(last.token))) 
                        {
                            switch (last.token.type) {
                                case Token.AT_SYMBOL:
                                case Token.L_PAREN:
                                case Token.L_BRACKET:
                                case Token.COMMA:
                                case Token.NULL:
                                    tokBuf.append(ch);
                                    break;
                                default:
                                    freeBuffer(tokBuf, ch);
                                    addToken( delimiterToken(ch) );
                                    break;
                            }
                        }
                        else {
                            tokBuf.append(ch);
                            break;
                        }
                        break;
                    default:
                        freeBuffer(tokBuf, ch);
                        //-- add delimiter Token
                        tok = delimiterToken(ch);
                        if (tok != null) addToken(tok);
                        break;
                }
            }
            //-- check for number
            else if (isDigit(ch)) {
                if ((tokBuf.length() == 0) || 
                    ((tokBuf.length() == 1) && (prevCh == '-' )))
                {
                    inNumber = true;
                    tokBuf.append(ch);
                }
                else {
                    
                    Token tok = match(tokBuf.toString(), ch);
                    if ((tok != null) && isOperator(tok)) {
                        //-- need to check prev token here
                        //-- add later
                        addToken(tok);
                        tokBuf.setLength(0);
                        tokBuf.append(ch);
                        inNumber = true;
                    }
                    else tokBuf.append(ch);
                }
            }
            else {
                tokBuf.append(ch);
            }
        } //-- end checking characters
        
        if (inNumber)
            addToken(new Token(tokBuf.toString(), Token.NUMBER));
        else if (inLiteral) {
            throw new ParseException(pattern, 
                "missing remainder of string literal", currentIdx);
        }
        else if (inVarRef) 
            addToken(new Token(tokBuf.toString(), Token.VAR_REFERENCE));
        else 
            freeBuffer(tokBuf, ch);
            
        if ((countTokens() > 1) && isOperator(last.token)) {
            String error = "missing remainder of expression.";
            throw new ParseException(pattern, error, pattern.length());
        }
            
    } //-- parsePattern

    private Token match(String str, char ch) {
        
        if (str == null) return new Token(null, Token.NULL);
        for (int i = 0; i < tokenSet.length; i++) {
            Token token = tokenSet[i];
            if (token.value.equals(str)) {
                //-- important to return a new Token...so that
                //-- the set of *static* tokens don't get changed.
                return new Token(token.value, token.type);
            }
        }
        return null;
    } //-- match
        
    /*  For Debugging */
    /*
    public static void main(String[] args) {

        String pattern = null;
        //pattern = "element[not(position()=1)][1]";
        //pattern = "pattern-test-case/pattern-test-case/@data";
        //pattern = "position-child[@test-att and ";
        //pattern += "(not(position()=1) and ";
        //pattern += "not(position()=last()))]";
        //pattern = "position() mod 2 = 0";
        //pattern = "@*[name(.)!='href']";
        //pattern = "ancestor::*";
        //pattern = "a/div/b/and/c";
        //pattern = "$foo mod 4";
        //pattern = "foo:*";
        //pattern = "div";
        //pattern = "(3)*(4)";
        pattern = "@text";
        
        System.out.println("Expr: "+pattern);
        Lexer exprLexer = null;
        
        try {
            exprLexer = new Lexer(pattern);
        }
        catch(ParseException px) {
            System.out.println(px.toString());
            return;
        }
        
        while(exprLexer.hasMoreTokens()) {
            Token tok = exprLexer.nextToken();
            String ts = tok.toString();
            System.out.print("TOKEN: "+ts);
            int len = 0;
            if (ts != null) len = ts.length();
            for (int i = len; i < 20; i++) System.out.print(" ");
            System.out.println(" type: " + tok.type);
        }
    } //-- main
    /* */
    
} //-- ExprLexer

class TokenItem {
    
    Token token = null;
    TokenItem prevItem = null;
    TokenItem nextItem = null;
    
    public TokenItem() {
        super();
    }
    
    public TokenItem(Token token) {
        super();
        this.token = token;
    }
} //-- TokenItem



