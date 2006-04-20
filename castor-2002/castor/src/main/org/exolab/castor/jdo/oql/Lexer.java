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


package org.exolab.castor.jdo.oql;

import java.lang.String;
import java.lang.StringBuffer;
import java.util.Hashtable;

/**
 * Seperates an input string of OQL into a stream of {@link Token}s. 
 *
 * @author  <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date$
 */
public class Lexer implements TokenTypes {

  String _queryString;
  int _pos;
  Hashtable keywords;
  boolean endOfQueryPassed = false;
  
  /**
   * Creates a lexer which will seperate a string query into a series of 
   * tokens.
   *
   * @param oqlQueryString The string representation of the OQL Query
   */
  public Lexer(String oqlQueryString) {
    _queryString = oqlQueryString;
    
    //Set up the Hashtable of keywords
    keywords = new Hashtable();
    keywords.put("select", new Integer(KEYWORD_SELECT));
    keywords.put("as", new Integer(KEYWORD_AS));
    keywords.put("from", new Integer(KEYWORD_FROM));
    keywords.put("in", new Integer(KEYWORD_IN));
    keywords.put("where", new Integer(KEYWORD_WHERE));
    keywords.put("or", new Integer(KEYWORD_OR));
    keywords.put("and", new Integer(KEYWORD_AND));
    keywords.put("like", new Integer(KEYWORD_LIKE));
    keywords.put("mod", new Integer(KEYWORD_MOD));
    keywords.put("abs", new Integer(KEYWORD_ABS));
    keywords.put("not", new Integer(KEYWORD_NOT));
    keywords.put("nil", new Integer(KEYWORD_NIL));
    keywords.put("undefined", new Integer(KEYWORD_UNDEFINED));
    keywords.put("between", new Integer(KEYWORD_BETWEEN));
    keywords.put("distinct", new Integer(KEYWORD_DISTINCT));
    keywords.put("is_defined", new Integer(KEYWORD_IS_DEFINED));
    keywords.put("is_undefined", new Integer(KEYWORD_IS_UNDEFINED));
    keywords.put("list", new Integer(KEYWORD_LIST));
    keywords.put("order", new Integer(KEYWORD_ORDER));
    keywords.put("by", new Integer(KEYWORD_BY));
    keywords.put("asc", new Integer(KEYWORD_ASC));
    keywords.put("desc", new Integer(KEYWORD_DESC));
    keywords.put("count", new Integer(KEYWORD_COUNT));
    keywords.put("sum", new Integer(KEYWORD_SUM));
    keywords.put("min", new Integer(KEYWORD_MIN));
    keywords.put("max", new Integer(KEYWORD_MAX));
    keywords.put("avg", new Integer(KEYWORD_AVG));
    keywords.put("limit", new Integer(KEYWORD_LIMIT));
        
  }

  /**
   * Checks whether the query strin has been fully consumed.
   * 
   * @return True if there is text in the query which hasn't been tokenized,
   *    otherwise false.
   */
  public boolean hasMoreTokens() {
    return !endOfQueryPassed;
  }

  /**
   * Returns the next {@link Token} from the stream.
   *
   * @return The next token in the stream.
   * @throws NoMoreTokensException if the last character was already consumed.
   * @throws InvalidCharException if an invalid character is found while 
   *        generating the next Token.
   */
  public Token nextToken() throws NoMoreTokensException, InvalidCharException {
  
    char curChar;
    try {
      curChar = _queryString.charAt(_pos);
    }
    catch (IndexOutOfBoundsException e) {
      if (!endOfQueryPassed) {
        endOfQueryPassed = true;
        return (new Token(END_OF_QUERY, ""));
      }
      else
        throw (new NoMoreTokensException());
    }
  
    //consume white space
    while (isWhiteSpace(curChar)) {
      _pos++;
      try {
        curChar = _queryString.charAt(_pos);
      }
      catch (IndexOutOfBoundsException e) {
        if (!endOfQueryPassed) {
          endOfQueryPassed = true;
          return (new Token(END_OF_QUERY, ""));
        }
      }
    }
  
    if ( isDigit(curChar) ) 
      return numericLiteral();

    if ( isLetter(curChar) )
      return identifier();
    
    char nextChar = 0;
    try {
      nextChar = _queryString.charAt(_pos + 1);
    }
    catch (IndexOutOfBoundsException e) {}

    if (curChar == '!' && nextChar == '=') {
      _pos += 2;
      return new Token( NOT_EQUAL, "!=" );
    }

    if (curChar == '>' && nextChar == '=') {
      _pos += 2;
      return new Token( GTE, ">=" );
    }

    if (curChar == '<' && nextChar == '=') {
      _pos += 2;
      return new Token( LTE, "<=" );
    }

    if (curChar == '|' && nextChar == '|') {
      _pos += 2;
      return new Token( CONCAT, "||" );
    }

    if (curChar == '-' && nextChar == '>') {
      _pos += 2;
      return new Token( ARROW, "->" );
    }

    
    Token retToken = null;
    
    switch (curChar) {
      case '\'':
        return charLiteral();
      case '"':
        return stringLiteral();
      case ':':
        retToken = new Token( COLON, ":" );
        break;
      case '=':
        retToken = new Token( EQUAL, "=" );
        break;
      case '<':
        retToken = new Token( LT, "<" );
        break;
      case '>':
        retToken = new Token( GT, ">" );
        break;
      case '+':
        retToken = new Token( PLUS, "+" );
        break;
      case '-':
        retToken = new Token( MINUS, "-" );
        break;
      case '*':
        retToken = new Token( TIMES, "*" );
        break;
      case '/':
        retToken = new Token( DIVIDE, "/" );
        break;
      case '(':
        retToken = new Token( LPAREN, "(" );
        break;
      case ')':
        retToken = new Token( RPAREN, ")" );
        break;
      case '.':
        retToken = new Token( DOT, "." );
        break;
      case '$':
        retToken = new Token( DOLLAR, "$" );
        break;
      case ',':
        retToken = new Token( COMMA, "," );
        break;
    }
    if ( retToken == null ) 
      throw (new InvalidCharException("An invalid character was found in the query at position " + _pos));
    else {
      _pos++;
      return retToken;
    } 
  }

  /**
   * Tests whether a character is white space.
   *
   * @param theChar The character to test
   * @return true if the character is whitespace, otherwise false
   */
  private boolean isWhiteSpace(char theChar) {
    switch (theChar) {
      case ' ': case '\t': case '\r': case '\n':
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
  private boolean isDigit(char theChar) {
    return ( theChar >= '0' && theChar <= '9' );
  }

  /**
   * Tests whether a character is a letter.
   *
   * @param theChar The character to test
   * @return true if the character is a letter, otherwise false
   */
  private boolean isLetter(char theChar) {
    return ( ( theChar >= 'A' && theChar <= 'Z' ) || 
             ( theChar >= 'a' && theChar <= 'z' ) );
  }

  /**
   * Consumes characters of a string literal. Throws exceptions if quotes 
   * aren't matched properly.
   *
   * @return A Token with STRING_LITERAL type containing the string literal
   * @throws NoMoreTokensException if the method is erroniously called when 
   *    all of the characters in the query have already been consumed.
   * @throws InvalidCharException if the first char is not a double quote (") 
   *    or a matching end quote is not found and all the characters are 
   *    consumed.
   */
  private Token stringLiteral() throws NoMoreTokensException, InvalidCharException {
  
    char curChar;
    try {
      curChar = _queryString.charAt(_pos);
    }
    catch (IndexOutOfBoundsException e) {
      throw (new NoMoreTokensException());
        }
      
    if (curChar != '"')
      throw (new InvalidCharException("stringLiteral() was called when the next character was not a double quote.  Position: " + _pos));
    
    StringBuffer sb = new StringBuffer("\"");
    _pos++;
    try {
      curChar = _queryString.charAt(_pos);
      while (curChar != '"') {
        sb.append(curChar);
        //if the current char is backslash, don't check append the next char 
        //without checking whether it's a quote.
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
    }
    catch (IndexOutOfBoundsException e) {
      throw ( new InvalidCharException( "End of query string reached, but \" expected." ) );
        }
    
    return ( new Token( STRING_LITERAL, sb.toString() ) );
  }

  /**
   * Consumes characters of a char literal. Throws exceptions if quotes 
   * aren't matched properly.
   *
   * @return A Token with CHAR_LITERAL type containing the char literal
   * @throws NoMoreTokensException if the method is erroniously called when 
   *    all of the characters in the query have already been consumed.
   * @throws InvalidCharException if the first char is not a single quote (') 
   *    or a matching end quote is not found after a single character is 
   *    consumed.
   */
  private Token charLiteral() throws NoMoreTokensException, InvalidCharException {
  
    char curChar;
    try {
      curChar = _queryString.charAt(_pos);
    }
    catch (IndexOutOfBoundsException e) {
      throw (new NoMoreTokensException());
        }
      
    if (curChar != '\'')
      throw (new InvalidCharException("charLiteral() was called when the next character was not a double quote.  Position: " + _pos));
    
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
      }
      else 
        throw ( new InvalidCharException( "Character literals may only contain a single character or escape sequence.  Position: " + _pos ) );
    }
    catch (IndexOutOfBoundsException e) {
      throw ( new InvalidCharException( "End of query string reached, but \' expected." ) );
    }

    return ( new Token( CHAR_LITERAL, sb.toString() ) );
  }

  /**
   * Returns the character at the current position in the queryString. 
   *
   * @return The character in the queryString at the current position or 0 if
   *      the current position is passed the last char
   */
  private char getChar() {
    try {
      return (_queryString.charAt(_pos));
    }
    catch (IndexOutOfBoundsException e) {
      return 0;
    }
  }

  /**
   * Consumes characters of a numeric literal. 
   *
   * @return A Token with LONG_LITERAL type containing the long literal if 
   *    there is no decimal point, otherwise a token with DOUBLE_LITERAL type
   *    containing the double literal.
   * @throws NoMoreTokensException if the method is erroniously called when 
   *    all of the characters in the query have already been consumed.
   * @throws InvalidCharException if the first char is not a digit or if a 
   *    decimal point is not followed by a digit or if an E or e exponent char
   *    is not followed by a +, - or digit, etc.
   */
  private Token numericLiteral() throws NoMoreTokensException, InvalidCharException {
  
    char curChar;
    try {
      curChar = _queryString.charAt(_pos);
    }
    catch (IndexOutOfBoundsException e) {
      throw (new NoMoreTokensException());
        }
      
    if ( ! isDigit(curChar) )
      throw (new InvalidCharException("numericLiteral() was called when the next character was not a digit.  Position: " + _pos));
    
    StringBuffer sb = new StringBuffer().append(curChar);
    _pos++;
    curChar = getChar();
          
    while ( isDigit(curChar) ) {
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
            
      if ( ! isDigit(curChar) )
        throw (new InvalidCharException( "Digit expected after decimal point in double literal. Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = getChar();
      }
      
      if (curChar == 'E' || curChar == 'e') {
        sb.append(curChar);
        _pos++;
        curChar = getChar();
        
        boolean isExponentSigned = false;
        if (curChar == '+' || curChar == '-') {
          isExponentSigned = true;
          sb.append(curChar);
          _pos++;
          curChar = getChar();
        }
        
        if ( ! isDigit(curChar) ) 
          if (isExponentSigned)
            throw (new InvalidCharException( "Digit expected after sign in exponent (double literal).  Position: " + _pos ));
          else
            throw (new InvalidCharException( "Digit expected after exponent character (double literal).  Position: " + _pos ));
        else {
          sb.append(curChar);
          _pos++;
          curChar = getChar();
          while ( isDigit(curChar) ) {
            sb.append(curChar);
            _pos++;
            curChar = getChar();
          }
        }
      }
      
      return (new Token( DOUBLE_LITERAL, sb.toString() ) );
    }
    else
      //it's a long
      return (new Token( LONG_LITERAL, sb.toString() ) );
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
   * @throws NoMoreTokensException if the method is erroniously called when 
   *    all of the characters in the query have already been consumed.
   * @throws InvalidCharException if the first char is not a letter. 
   */
  private Token identifier() throws NoMoreTokensException, InvalidCharException {
  
    char curChar;
    try {
      curChar = _queryString.charAt(_pos);
    }
    catch (IndexOutOfBoundsException e) {
      throw (new NoMoreTokensException());
        }
      
    if ( ! isLetter(curChar) )
      throw (new InvalidCharException("identifier() was called when the next character was not a letter.  Position: " + _pos));
    
    StringBuffer sb = new StringBuffer().append(curChar);
    _pos++;
    curChar = getChar();
          
    while ( isDigit(curChar) || isLetter(curChar) || curChar == '_' ) {
      sb.append(curChar);
      _pos++;
      curChar = getChar();
    }

    String ident = sb.toString().toLowerCase();

    if (ident.equals("date"))
      return dateLiteral(sb.toString());
    if (ident.equals("time"))
      return timeLiteral(sb.toString());
    if (ident.equals("timestamp"))
      return timeStampLiteral(sb.toString());
      
    if (ident.equals("true") || ident.equals("false"))
      return (new Token( BOOLEAN_LITERAL, sb.toString() ));

    if (keywords.containsKey(ident))
      return (new Token( ((Integer)keywords.get(ident)).intValue(), 
                         sb.toString() ));

    return (new Token( IDENTIFIER, sb.toString() ));

  }

  /**
   * Consumes characters of a date literal.  The date keyword was already 
   * consumed by the {@link identifier} function, and is passed as a parameter.
   *
   * @param alreadyConsumed the part of the date literal already consumed
   *    by the calling function.  This should always be "date".
   * @return A Token with DATE_LITERAL type containing the entire date 
   *    literal text.
   * @throws InvalidCharException if the first char is not a quote, or the 
   *    date literal is otherwise not of the proper form: 
   *    date 'longLiteral-longLiteral-longLiteral'. 
   */
  private Token dateLiteral(String alreadyConsumed) throws InvalidCharException {
  
    StringBuffer sb = new StringBuffer(alreadyConsumed);
    
    char curChar = getChar();
    try {
      
      curChar = _queryString.charAt(_pos);
      
      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '\'' )
        throw (new InvalidCharException("The date keyword must be followed by a single quote.  Position: " + _pos));
   
      sb.append(" '");
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in date literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '-' )
        throw (new InvalidCharException("- expected.  Fields in date literal must be separated by a dash.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in date literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '-' )
        throw (new InvalidCharException("- expected.  Fields in date literal must be separated by a dash.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in date literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '\'' )
        throw (new InvalidCharException("' expected.  Date literal must be enclosed by a single quotes.  Position: " + _pos));

      sb.append(curChar);
      _pos++;

      return (new Token( DATE_LITERAL, sb.toString() ));
 

    }
    catch (IndexOutOfBoundsException e) {
      throw (new InvalidCharException("End of query encountered in the middle of date literal."));
        }
  }

  /**
   * Consumes characters of a time literal.  The time keyword was already 
   * consumed by the {@link identifier} function, and is passed as a parameter.
   *
   * @param alreadyConsumed the part of the time literal already consumed
   *    by the calling function.  This should always be "time".
   * @return A Token with TIME_LITERAL type containing the entire time 
   *    literal text.
   * @throws InvalidCharException if the first char is not a quote, or the 
   *    time literal is otherwise not of the proper form: 
   *    time 'longLiteral:longLiteral:longLiteral'. 
   */
  private Token timeLiteral(String alreadyConsumed) throws InvalidCharException {
  
    StringBuffer sb = new StringBuffer(alreadyConsumed);
    
    char curChar = getChar();
    try {
      
      curChar = _queryString.charAt(_pos);
      
      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '\'' )
        throw (new InvalidCharException("The time keyword must be followed by a single quote.  Position: " + _pos));
   
      sb.append(" '");
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in time literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != ':' )
        throw (new InvalidCharException(": expected.  Fields in time literal must be separated by a colon.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in time literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != ':' )
        throw (new InvalidCharException(": expected.  Fields in time literal must be separated by a colon.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in time literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '\'' )
        throw (new InvalidCharException("' expected.  Time literal must be enclosed by a single quotes.  Position: " + _pos));

      sb.append(curChar);
      _pos++;

      return (new Token( TIME_LITERAL, sb.toString() ));
 

    }
    catch (IndexOutOfBoundsException e) {
      throw (new InvalidCharException("End of query encountered in the middle of time literal."));
        }
  }


  /**
   * Consumes characters of a time stamp literal.  The timestamp keyword was 
   * already consumed by the {@link identifier} function, and is passed as a 
   * parameter.
   *
   * @param alreadyConsumed the part of the timestamp literal already consumed
   *    by the calling function.  This should always be "timestamp".
   * @return A Token with TIMESTAMP_LITERAL type containing the entire 
   *    timestamp literal text.
   * @throws InvalidCharException if the first char is not a quote, or the 
   *    timestamp literal is otherwise not of the proper form: 
   *    timestamp 'longLiteral-longLiteral-longLiteral 
   *               longLiteral:longLiteral:longLiteral'. 
   */
  private Token timeStampLiteral(String alreadyConsumed) throws InvalidCharException {
  
    StringBuffer sb = new StringBuffer(alreadyConsumed);
    
    char curChar = getChar();
    try {
      
      curChar = _queryString.charAt(_pos);
      
      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '\'' )
        throw (new InvalidCharException("The timestamp keyword must be followed by a single quote.  Position: " + _pos));
   
      sb.append(" '");
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in timestamp literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '-' )
        throw (new InvalidCharException("- expected.  Fields in date part of timestamp literal must be separated by a dash.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in timestamp literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '-' )
        throw (new InvalidCharException("- expected.  Fields in date part of timestamp literal must be separated by a dash.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in timestamp literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      if ( ! isWhiteSpace(curChar) )
        throw (new InvalidCharException("White space expected in timestamp literal.  Position: " + _pos));
       
      curChar = consumeWhiteSpace(curChar);

      sb.append(' ');
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in timestamp literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != ':' )
        throw (new InvalidCharException(": expected.  Fields in time part of timestamp literal must be separated by a colon.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in timestamp literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != ':' )
        throw (new InvalidCharException(": expected.  Fields in time part of timestamp literal must be separated by a colon.  Position: " + _pos));
   
      sb.append(curChar);
      _pos++;
      curChar = _queryString.charAt(_pos);
           
      curChar = consumeWhiteSpace(curChar);
      
      if ( ! isDigit(curChar) ) 
        throw (new InvalidCharException("Digit expected in timestamp literal.  Position: " + _pos));
        
      while ( isDigit(curChar) ) {
        sb.append(curChar);
        _pos++;
        curChar = _queryString.charAt(_pos);
      }

      curChar = consumeWhiteSpace(curChar);
      
      if ( curChar != '\'' )
        throw (new InvalidCharException("' expected.  Timestamp literal must be enclosed by a single quotes.  Position: " + _pos));

      sb.append(curChar);
      _pos++;

      return (new Token( TIMESTAMP_LITERAL, sb.toString() ));
 

    }
    catch (IndexOutOfBoundsException e) {
      throw (new InvalidCharException("End of query encountered in the middle of time literal."));
        }
  }

  /**
   * Consumes whitespace characters.
   *
   * @param @curChar the current character before the call
   * @return The current character after whitespace is consumed
   */
  private char consumeWhiteSpace(char curChar) {
    while (isWhiteSpace(curChar)) {
      _pos++;
      curChar = _queryString.charAt(_pos);
    }
    return (curChar);
  }

}
