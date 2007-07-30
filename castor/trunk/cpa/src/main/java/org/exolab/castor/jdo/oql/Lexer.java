/*
 * Copyright 2005 Nissim Karpenstein, Stein M. Hugubakken
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.jdo.oql;

import java.util.HashMap;

/**
 * Seperates an input string of OQL into a stream of {@link Token}s. 
 *
 * @author  <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class Lexer {
    private static final HashMap KEYWORDS = new HashMap();

    static {
        KEYWORDS.put("select", new Integer(TokenType.KEYWORD_SELECT));
        KEYWORDS.put("as", new Integer(TokenType.KEYWORD_AS));
        KEYWORDS.put("from", new Integer(TokenType.KEYWORD_FROM));
        KEYWORDS.put("in", new Integer(TokenType.KEYWORD_IN));
        KEYWORDS.put("where", new Integer(TokenType.KEYWORD_WHERE));
        KEYWORDS.put("or", new Integer(TokenType.KEYWORD_OR));
        KEYWORDS.put("and", new Integer(TokenType.KEYWORD_AND));
        KEYWORDS.put("like", new Integer(TokenType.KEYWORD_LIKE));
        KEYWORDS.put("mod", new Integer(TokenType.KEYWORD_MOD));
        KEYWORDS.put("abs", new Integer(TokenType.KEYWORD_ABS));
        KEYWORDS.put("not", new Integer(TokenType.KEYWORD_NOT));
        KEYWORDS.put("nil", new Integer(TokenType.KEYWORD_NIL));
        KEYWORDS.put("undefined", new Integer(TokenType.KEYWORD_UNDEFINED));
        KEYWORDS.put("between", new Integer(TokenType.KEYWORD_BETWEEN));
        KEYWORDS.put("distinct", new Integer(TokenType.KEYWORD_DISTINCT));
        KEYWORDS.put("is_defined", new Integer(TokenType.KEYWORD_IS_DEFINED));
        KEYWORDS.put("is_undefined", new Integer(TokenType.KEYWORD_IS_UNDEFINED));
        KEYWORDS.put("list", new Integer(TokenType.KEYWORD_LIST));
        KEYWORDS.put("order", new Integer(TokenType.KEYWORD_ORDER));
        KEYWORDS.put("by", new Integer(TokenType.KEYWORD_BY));
        KEYWORDS.put("asc", new Integer(TokenType.KEYWORD_ASC));
        KEYWORDS.put("desc", new Integer(TokenType.KEYWORD_DESC));
        KEYWORDS.put("count", new Integer(TokenType.KEYWORD_COUNT));
        KEYWORDS.put("sum", new Integer(TokenType.KEYWORD_SUM));
        KEYWORDS.put("min", new Integer(TokenType.KEYWORD_MIN));
        KEYWORDS.put("max", new Integer(TokenType.KEYWORD_MAX));
        KEYWORDS.put("avg", new Integer(TokenType.KEYWORD_AVG));
        KEYWORDS.put("limit", new Integer(TokenType.KEYWORD_LIMIT));
        KEYWORDS.put("offset", new Integer(TokenType.KEYWORD_OFFSET));
    }

    private int _pos;
    private String _queryString;
    private boolean _noMoreTokens = false;

    /**
     * Creates a lexer which will seperate a string query into a series of 
     * tokens.
     *
     * @param oqlQueryString The string representation of the OQL Query
     */
    public Lexer(final String oqlQueryString) {
        _queryString = oqlQueryString;
    }

    /**
     * Checks whether the query strin has been fully consumed.
     * 
     * @return True if there is text in the query which hasn't been tokenized,
     *    otherwise false.
     */
    public boolean hasMoreTokens() {
        return !_noMoreTokens;
    }

    /**
     * Returns the next {@link Token} from the stream.
     *
     * @return The next token in the stream.
     * @throws InvalidCharException if an invalid character is found while 
     *         generating the next Token.
     */
    public Token nextToken() throws InvalidCharException {
        char oldChar;
        if (_pos > 0) {
            oldChar = _queryString.charAt(_pos - 1);
        } else {
            oldChar = '\0';
        }

        char curChar;
        if (_pos < _queryString.length()) {
            curChar = _queryString.charAt(_pos);
        } else {
            _noMoreTokens = true;
            return Token.END_OF_QUERY;
        }
      
        //consume white space
        while (isWhiteSpace(curChar)) {
            oldChar = curChar;
            _pos++;
            if (_pos < _queryString.length()) {
                curChar = _queryString.charAt(_pos);
            } else {
                _noMoreTokens = true;
                return Token.END_OF_QUERY;
            }
        }
      
        if (isDigit(curChar)) { return numericLiteral(); }

        if (isLetter(curChar)) { return identifier(oldChar); }
        
        char nextChar = 0;
        if ((_pos + 1) < _queryString.length()) {
            nextChar = _queryString.charAt(_pos + 1);
        }

        if ((curChar == '!') && (nextChar == '=')) {
            _pos += 2;
            return Token.NOT_EQUAL;
        }

        if ((curChar == '>') && (nextChar == '=')) {
            _pos += 2;
            return Token.GTE;
        }

        if ((curChar == '<') && (nextChar == '=')) {
            _pos += 2;
            return Token.LTE;
        }

        if ((curChar == '|') && (nextChar == '|')) {
            _pos += 2;
            return Token.CONCAT;
        }

        if ((curChar == '-') && (nextChar == '>')) {
            _pos += 2;
            return Token.ARROW;
        }
        
        Token retToken = null;
        
        switch (curChar) {
        case '\'':
            return charLiteral();
        case '"':
            return stringLiteral();
        case ':':
            retToken = Token.COLON;
            break;
        case '=':
            retToken = Token.EQUAL;
            break;
        case '<':
            retToken = Token.LT;
            break;
        case '>':
            retToken = Token.GT;
            break;
        case '+':
            retToken = Token.PLUS;
            break;
        case '-':
            retToken = Token.MINUS;
            break;
        case '*':
            retToken = Token.TIMES;
            break;
        case '/':
            retToken = Token.DIVIDE;
            break;
        case '(':
            retToken = Token.LPAREN;
            break;
        case ')':
            retToken = Token.RPAREN;
            break;
        case '.':
            retToken = Token.DOT;
            break;
        case '$':
            retToken = Token.DOLLAR;
            break;
        case ',':
            retToken = Token.COMMA;
            break;
        }
        
        if (retToken == null) {
            throw (new InvalidCharException(
                    "An invalid character was found in the query at position " + _pos));
        }
        _pos++;
        return retToken; 
    }

    /**
     * Tests whether a character is white space.
     *
     * @param theChar The character to test
     * @return true if the character is whitespace, otherwise false
     */
    private boolean isWhiteSpace(final char theChar) {
        switch (theChar) {
        case ' ':
        case '\t':
        case '\r':
        case '\n':
            return true;
        default:
            return false;
        }
    }

    /**
     * Tests whether a character is a digit.
     *
     * @param theChar The character to test
     * @return true if the character is a digit, otherwise false
     */
    private boolean isDigit(final char theChar) {
        return Character.isDigit(theChar);
    }

    /**
     * Tests whether a character is a letter.
     *
     * @param theChar The character to test
     * @return true if the character is a letter, otherwise false
     */
    private boolean isLetter(final char theChar) {
        return Character.isLetter(theChar);
    }

    /**
     * Consumes characters of a string literal. Throws exceptions if quotes 
     * aren't matched properly.
     *
     * @return A Token with STRING_LITERAL type containing the string literal
     * @throws InvalidCharException if the first char is not a double quote (") 
     *         or a matching end quote is not found and all the characters are 
     *         consumed.
     */
    private Token stringLiteral() throws InvalidCharException {
        char curChar;
        if (_pos < _queryString.length()) {
            curChar = _queryString.charAt(_pos);
        } else {
            return null;
        }
        
        if (curChar != '"') {
            throw new InvalidCharException("stringLiteral() was called when the next "
                    + "character was not a double quote.  Position: " + _pos);
        }
        
        StringBuffer sb = new StringBuffer("\"");
        _pos++;
        try {
            curChar = _queryString.charAt(_pos);
            while (curChar != '"') {
                sb.append(curChar);
                // if the current char is backslash, don't check append the next char 
                // without checking whether it's a quote.
                if (curChar == '\\') {
                    _pos++;
                    curChar = _queryString.charAt(_pos);
                    sb.append(curChar);
                }
                _pos++;
                curChar = _queryString.charAt(_pos);
            }
            //append and consume the closing quote
            sb.append(curChar);
            _pos++;
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCharException(
                    "End of query string reached, but \" expected.");
        }
        
        return new Token(TokenType.STRING_LITERAL, sb.toString());
    }

    /**
     * Consumes characters of a char literal. Throws exceptions if quotes 
     * aren't matched properly.
     *
     * @return A Token with CHAR_LITERAL type containing the char literal
     * @throws InvalidCharException if the first char is not a single quote (') 
     *         or a matching end quote is not found after a single character is 
     *         consumed.
     */
    private Token charLiteral() throws InvalidCharException {
        char curChar;
        if (_pos < _queryString.length()) {
            curChar = _queryString.charAt(_pos);
        } else {
            return null;
        }
          
        if (curChar != '\'') {
            throw new InvalidCharException("charLiteral() was called when the next "
                    + "character was not a double quote.  Position: " + _pos);
        }
        
        StringBuffer sb = new StringBuffer("'");
        _pos++;
        try {
            curChar = _queryString.charAt(_pos);
            //append the first char after the '
            sb.append(curChar);
            _pos++;
            
            //if the current char is backslash, consume escaped char
            if (curChar == '\\') {
                curChar = _queryString.charAt(_pos);
                sb.append(curChar);
                _pos++;
            }
            curChar = _queryString.charAt(_pos);
            if (curChar == '\'') {
                sb.append(curChar);
                _pos++;
            } else {
                throw new InvalidCharException("Character literals may only contain "
                        + "a single character or escape sequence.  Position: " + _pos);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCharException(
                    "End of query string reached, but \' expected.");
        }

        return new Token(TokenType.CHAR_LITERAL, sb.toString());
    }

    /**
     * Returns the character at the current position in the queryString. 
     *
     * @return The character in the queryString at the current position or 0 if
     *      the current position is passed the last char
     */
    private char getChar() {
        if (_pos < _queryString.length()) {
            return _queryString.charAt(_pos);
        }
        return 0;
    }

    /**
     * Consumes characters of a numeric literal. 
     *
     * @return A Token with LONG_LITERAL type containing the long literal if 
     *         there is no decimal point, otherwise a token with DOUBLE_LITERAL type
     *         containing the double literal.
     * @throws InvalidCharException if the first char is not a digit or if a 
     *         decimal point is not followed by a digit or if an E or e exponent char
     *         is not followed by a +, - or digit, etc.
     */
    private Token numericLiteral() throws InvalidCharException {
        char curChar;
        if (_pos < _queryString.length()) {
            curChar = _queryString.charAt(_pos);
        } else {
            return null;
        }
          
        if (!isDigit(curChar)) {
            throw new InvalidCharException("numericLiteral() was called when the next "
                    + "character was not a digit.  Position: " + _pos);
        }
        
        StringBuffer sb = new StringBuffer().append(curChar);
        _pos++;
        curChar = getChar();
        while (isDigit(curChar)) {
            sb.append(curChar);
            _pos++;
            curChar = getChar();
        }

        //is this a long or a double
        if (curChar == '.') {
            //it's a double
            sb.append('.');
            _pos++;
            curChar = getChar();
                  
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected after decimal point in "
                        + "double literal. Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = getChar();
            }
            
            if ((curChar == 'E') || (curChar == 'e')) {
                sb.append(curChar);
                _pos++;
                curChar = getChar();
                
                boolean isExponentSigned = false;
                if ((curChar == '+') || (curChar == '-')) {
                    isExponentSigned = true;
                    sb.append(curChar);
                    _pos++;
                    curChar = getChar();
                }
                
                if (!isDigit(curChar)) { 
                    if (isExponentSigned) {
                        throw new InvalidCharException("Digit expected after sign in "
                                + "exponent (double literal).  Position: " + _pos);
                    }
                    throw new InvalidCharException("Digit expected after exponent "
                            + "character (double literal).  Position: " + _pos);
                }
                sb.append(curChar);
                _pos++;
                curChar = getChar();
                while (isDigit(curChar)) {
                    sb.append(curChar);
                    _pos++;
                    curChar = getChar();
                }
            }
            
            return new Token(TokenType.DOUBLE_LITERAL, sb.toString());
        }
        //it's a long
        return new Token(TokenType.LONG_LITERAL, sb.toString());
    }

    /**
     * Consumes characters of a token which starts with a letter.  This may end 
     * up being a token of type IDENTIFIER, KEYWORD, BOOLEAN_LITERAL, 
     * DATE_LITERAL, TIME_LITERAL, or TIMESTAMP_LITERAL.  This is determined by 
     * comparing the text to a list of keywords, and calling the appropriate 
     * function if necessary (e.g. to consume the rest of the text in a DATE, TIME
     * or TIMESTAMP literal).
     *
     * @return A Token with one of the token types described above.
     * @throws InvalidCharException if the first char is not a letter. 
     */
    private Token identifier(final char oldChar) throws InvalidCharException {
        char curChar;
        if (_pos < _queryString.length()) {
            curChar = _queryString.charAt(_pos);
        } else {
            return null;
        }
          
        if (!isLetter(curChar)) {
            throw (new InvalidCharException("identifier() was called when the next "
                    + "character was not a letter.  Position: " + _pos));
        }
        
        StringBuffer sb = new StringBuffer().append(curChar);
        _pos++;
        curChar = getChar();
              
        while (isDigit(curChar) || isLetter(curChar) || curChar == '_') {
            sb.append(curChar);
            _pos++;
            curChar = getChar();
        }

        String ident = sb.toString().toLowerCase();

        // If there's a dot before, we'll assume that it's an identifier in any case.
        if (oldChar == '.') {
            return new Token(TokenType.IDENTIFIER, sb.toString());
        }

        if (ident.equals("date")) {
            return dateLiteral(sb.toString());
        } else if (ident.equals("time")) {
            return timeLiteral(sb.toString());
        } else if (ident.equals("timestamp")) {
            return timeStampLiteral(sb.toString());
        } else if (ident.equals("true") || ident.equals("false")) {
            return new Token(TokenType.BOOLEAN_LITERAL, sb.toString());
        }

        if (KEYWORDS.containsKey(ident)) {
            return new Token(((Integer) KEYWORDS.get(ident)).intValue(), sb.toString());
        }

        return new Token(TokenType.IDENTIFIER, sb.toString());
    }

    /**
     * Consumes characters of a date literal.  The date keyword was already 
     * consumed by the {@link #identifier(char)} function, and is passed as a parameter.
     *
     * @param alreadyConsumed the part of the date literal already consumed
     *        by the calling function.  This should always be "date".
     * @return A Token with DATE_LITERAL type containing the entire date 
     *         literal text.
     * @throws InvalidCharException if the first char is not a quote, or the 
     *         date literal is otherwise not of the proper form: 
     *         date 'longLiteral-longLiteral-longLiteral'. 
     */
    private Token dateLiteral(final String alreadyConsumed) throws InvalidCharException {
        StringBuffer sb = new StringBuffer(alreadyConsumed);
        
        char curChar = getChar();
        try {
            curChar = _queryString.charAt(_pos);
            
            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != '\'') {
                throw new InvalidCharException("The date keyword must be followed by "
                        + "a single quote.  Position: " + _pos);
            }
         
            sb.append(" '");
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in date literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != '-') {
                throw new InvalidCharException("- expected.  Fields in date literal "
                        + "must be separated by a dash.  Position: " + _pos);
            }
         
            sb.append(curChar);
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in date literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != '-') {
                throw new InvalidCharException("- expected.  Fields in date literal "
                        + "must be separated by a dash.  Position: " + _pos);
            }
         
            sb.append(curChar);
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in date literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != '\'') {
                throw new InvalidCharException("' expected.  Date literal must be "
                        + "enclosed by a single quotes.  Position: " + _pos);
            }

            sb.append(curChar);
            _pos++;

            return new Token(TokenType.DATE_LITERAL, sb.toString());
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCharException("End of query encountered in the middle "
                    + "of date literal.");
        }
    }

    /**
     * Consumes characters of a time literal.  The time keyword was already 
     * consumed by the {@link #identifier} function, and is passed as a parameter.
     *
     * @param alreadyConsumed the part of the time literal already consumed
     *        by the calling function.  This should always be "time".
     * @return A Token with TIME_LITERAL type containing the entire time 
     *         literal text.
     * @throws InvalidCharException if the first char is not a quote, or the 
     *         time literal is otherwise not of the proper form: 
     *         time 'longLiteral:longLiteral:longLiteral'. 
     */
    private Token timeLiteral(final String alreadyConsumed) throws InvalidCharException {
        StringBuffer sb = new StringBuffer(alreadyConsumed);
        
        char curChar = getChar();
        try {
          curChar = _queryString.charAt(_pos);
          
          curChar = consumeWhiteSpace(curChar);
          
          if (curChar != '\'') {
              throw new InvalidCharException("The time keyword must be followed by "
                      + "a single quote.  Position: " + _pos);
          }
       
          sb.append(" '");
          _pos++;
          curChar = _queryString.charAt(_pos);
               
          curChar = consumeWhiteSpace(curChar);
          
          if (!isDigit(curChar)) {
              throw new InvalidCharException("Digit expected in time literal.  "
                      + "Position: " + _pos);
          }
            
          while (isDigit(curChar)) {
              sb.append(curChar);
              _pos++;
              curChar = _queryString.charAt(_pos);
          }

          curChar = consumeWhiteSpace(curChar);
          
          if (curChar != ':') {
              throw new InvalidCharException(": expected.  Fields in time literal must "
                      + "be separated by a colon.  Position: " + _pos);
          }
       
          sb.append(curChar);
          _pos++;
          curChar = _queryString.charAt(_pos);
               
          curChar = consumeWhiteSpace(curChar);
          
          if (!isDigit(curChar)) {
              throw new InvalidCharException("Digit expected in time literal.  "
                      + "Position: " + _pos);
          }
            
          while (isDigit(curChar)) {
              sb.append(curChar);
              _pos++;
              curChar = _queryString.charAt(_pos);
          }

          curChar = consumeWhiteSpace(curChar);
          
          if (curChar != ':') {
              throw new InvalidCharException(": expected.  Fields in time literal must "
                      + "be separated by a colon.  Position: " + _pos);
          }
       
          sb.append(curChar);
          _pos++;
          curChar = _queryString.charAt(_pos);
               
          curChar = consumeWhiteSpace(curChar);
          
          if (!isDigit(curChar)) {
              throw new InvalidCharException("Digit expected in time literal.  "
                      + "Position: " + _pos);
          }
            
          while (isDigit(curChar)) {
              sb.append(curChar);
              _pos++;
              curChar = _queryString.charAt(_pos);
          }

          // millisecond part of the time
          if (curChar == '.') {
              sb.append(curChar);
              _pos++;
              curChar = _queryString.charAt(_pos);

              if (!isDigit(curChar)) {
                  throw new InvalidCharException("Digit expected in time literal.  "
                          + "Position: " + _pos);
              }

              while (isDigit(curChar)) {
                  sb.append(curChar);
                  _pos++;
                  curChar = _queryString.charAt(_pos);
              }
          }

          curChar = consumeWhiteSpace(curChar);
          
          if (curChar != '\'') {
              throw new InvalidCharException("' expected.  Time literal must be "
                      + "enclosed by a single quotes.  Position: " + _pos);
          }

          sb.append(curChar);
          _pos++;

          return new Token(TokenType.TIME_LITERAL, sb.toString());
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCharException("End of query encountered in the middle of "
                    + "time literal.");
        }
    }

    /**
     * Consumes characters of a time stamp literal.  The timestamp keyword was 
     * already consumed by the {@link #identifier} function, and is passed as a 
     * parameter.
     *
     * @param alreadyConsumed the part of the timestamp literal already consumed
     *        by the calling function.  This should always be "timestamp".
     * @return A Token with TIMESTAMP_LITERAL type containing the entire 
     *         timestamp literal text.
     * @throws InvalidCharException if the first char is not a quote, or the 
     *         timestamp literal is otherwise not of the proper form: 
     *         timestamp 'longLiteral-longLiteral-longLiteral 
     *                    longLiteral:longLiteral:longLiteral'. 
     */
    private Token timeStampLiteral(final String alreadyConsumed)
    throws InvalidCharException {
        StringBuffer sb = new StringBuffer(alreadyConsumed);
        
        char curChar = getChar();
        try {
            curChar = _queryString.charAt(_pos);
            
            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != '\'') {
                throw new InvalidCharException("The timestamp keyword must be followed "
                        + "by a single quote.  Position: " + _pos);
            }
         
            sb.append(" '");
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in timestamp literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != '-') {
                throw new InvalidCharException("- expected.  Fields in date part of "
                        + "timestamp literal must be separated by a dash.  "
                        + "Position: " + _pos);
            }
         
            sb.append(curChar);
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in timestamp literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != '-') {
                throw new InvalidCharException("- expected.  Fields in date part of "
                        + "timestamp literal must be separated by a dash.  "
                        + "Position: " + _pos);
            }
         
            sb.append(curChar);
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in timestamp literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            if (!isWhiteSpace(curChar)) {
                throw new InvalidCharException("White space expected in timestamp "
                        + "literal.  Position: " + _pos);
            }
             
            curChar = consumeWhiteSpace(curChar);

            sb.append(' ');
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in timestamp literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != ':') {
                throw new InvalidCharException(": expected.  Fields in time part of "
                        + "timestamp literal must be separated by a colon.  "
                        + "Position: " + _pos);
            }
         
            sb.append(curChar);
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in timestamp literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            curChar = consumeWhiteSpace(curChar);
            
            if (curChar != ':') {
                throw new InvalidCharException(": expected.  Fields in time part of "
                        + "timestamp literal must be separated by a colon.  "
                        + "Position: " + _pos);
            }
         
            sb.append(curChar);
            _pos++;
            curChar = _queryString.charAt(_pos);
                 
            curChar = consumeWhiteSpace(curChar);
            
            if (!isDigit(curChar)) {
                throw new InvalidCharException("Digit expected in timestamp literal.  "
                        + "Position: " + _pos);
            }
              
            while (isDigit(curChar)) {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);
            }

            // millisecond part of the timestamp
            if (curChar == '.') {
                sb.append(curChar);
                _pos++;
                curChar = _queryString.charAt(_pos);

                if (!isDigit(curChar)) {
                    throw (new InvalidCharException("Digit expected in timestamp literal.  "
                            + "Position: " + _pos));
                }

                while (isDigit(curChar)) {
                    sb.append(curChar);
                    _pos++;
                    curChar = _queryString.charAt(_pos);
                }
            }

            curChar = consumeWhiteSpace(curChar);

            if (curChar != '\'') {
                throw new InvalidCharException("' expected.  Timestamp literal must be "
                        + "enclosed by a single quotes.  Position: " + _pos);
            }

            sb.append(curChar);
            _pos++;

            return new Token(TokenType.TIMESTAMP_LITERAL, sb.toString());
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidCharException("End of query encountered in the middle of "
                    + "time literal.");
        }
    }

    /**
     * Consumes whitespace characters.
     *
     * @param currentChar the current character before the call
     * @return The current character after whitespace is consumed
     */
    private char consumeWhiteSpace(final char curChar) {
        char currentChar = curChar;
        while (isWhiteSpace(currentChar)) {
            _pos++;
            currentChar = _queryString.charAt(_pos);
        }
        return currentChar;
    }
}
