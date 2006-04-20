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

/**
 * Generates a parse tree for a stream of tokens representing an OQL query. 
 *
 * @author  <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date$
 */
public class Parser implements TokenTypes {

  Lexer _lexer;
  Token _curToken = null;;
  Token _nextToken = null;
  ParseTreeNode _treeRoot;
  
  /**
   * Creates a parser which will generate a parse tree from a series of 
   * tokens.
   *
   * @param oqlQueryString The string representation of the OQL Query
   * @throws InvalidCharException thrown by primeLexer.
   * @throws OQLSyntaxException thrown by primeLexer.
   */
  public Parser(Lexer lexer) throws InvalidCharException, OQLSyntaxException {
    _lexer = lexer;
    primeLexer();
  }

  /**
   * Generates the parse tree for the tokens provided by the Lexer passed 
   * in the constructor.
   *
   * @return a ParseTreeNode representing the query.
   * @throws InvalidCharException thrown by match.
   * @throws OQLSyntaxException thrown by match.
    */
  public ParseTreeNode getParseTree() 
          throws InvalidCharException, OQLSyntaxException {
    
    _treeRoot = match(KEYWORD_SELECT);
    
    if ( _curToken.getTokenType() == KEYWORD_DISTINCT )
      _treeRoot.addChild(match(KEYWORD_DISTINCT));
      
    _treeRoot.addChild(projectionAttributes());
    _treeRoot.addChild(fromClause());
    
    if ( _curToken.getTokenType() == KEYWORD_WHERE ) {
      _treeRoot.addChild(whereClause());
    }

    if ( _curToken.getTokenType() == KEYWORD_ORDER ) {
      _treeRoot.addChild(orderClause());
    }

    if ( _curToken.getTokenType() == KEYWORD_LIMIT ) {
      _treeRoot.addChild(limitClause());
    }
    
    match(END_OF_QUERY);

    return _treeRoot;
  }

  /**
   * Primes the _curToken and _nextToken private members by calling 
   * {@link Lexer.getToken()}.
   *
   * @throws InvalidCharException thrown by the Lexer.
   * @throws OQLSyntaxException if the query contains less than 2 tokens.
   */
  private void primeLexer() throws InvalidCharException, OQLSyntaxException {
    try {
      _curToken = _lexer.nextToken();
      _nextToken = _lexer.nextToken();
    }
    catch (NoMoreTokensException e) {
      throw (new OQLSyntaxException("Incomplete query passed."));
    }
  }

  /**
   * Tests whether the current token has the same token type that was passed.
   * If the test passes, a ParseTreeNode is returned containing the current
   * token, and the _curToken and _nextToken private members are advanced.
   * If the test fails, an exception is thrown.
   *
   * @param tokenType The token type to compare the current token to.
   * @return A ParseTreeNode containing the current Token if the test is 
   *    successful, otherwise throws an exception.
   * @throws InvalidCharException thrown by Lexer.
   * @throws OQLSyntaxException if the token types don't match.
   */
  private ParseTreeNode match(int tokenType) 
            throws InvalidCharException, OQLSyntaxException {
    
    if (_curToken.getTokenType() != tokenType)
      throw (new OQLSyntaxException("An incorrect token type was found near " + 
                    _curToken.getTokenValue()+" ("+
                    String.valueOf( _curToken.getTokenType() )+
                    ", need "+
                    String.valueOf( tokenType ) ));

    ParseTreeNode retNode = new ParseTreeNode(_curToken);
    _curToken = _nextToken;
    try {
      _nextToken = _lexer.nextToken();
    }
    catch (NoMoreTokensException e) {}

    return retNode;
  }

  /**
   * Consumes tokens of projection attributes (the query targets in the select
   * part of an OQL query).  This method also does a transformation.  In OQL, 
   * the following projection attributes are equivalent:
   * <pre>
   *  x as a
   *
   *  a : x
   * </pre>
   * These will both return a tree with root AS, first child x, and second 
   * child a.
   *
   * @return a parse tree containing a single identifier, or a root containing
   *    KEYWORD_AS, with two IDENTIFIER children.
   * @throws InvalidCharException passed through from match.
   * @throws OQLSyntaxException passed through from match.
   */
  private ParseTreeNode projectionAttributes() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = null;
    ParseTreeNode queryTarget = expr();

    if (_curToken.getTokenType() == KEYWORD_AS) {
      retNode = match(KEYWORD_AS);
      retNode.addChild(queryTarget);
      retNode.addChild(match(IDENTIFIER));
    }
    else if (_curToken.getTokenType() == COLON) {
      if ( queryTarget.getToken().getTokenType() != IDENTIFIER )
        throw new OQLSyntaxException( "When using the ':' in projection attributes (select part of query) the token before the ':' must be an identifier." );
      match(COLON);
      retNode = new ParseTreeNode(new Token(KEYWORD_AS, "as"));
      retNode.addChild( expr() );
      retNode.addChild(queryTarget);
    }

    if (retNode == null)
      return queryTarget;
    else
      return retNode;
  }
  
  /**
   * Consumes tokens of from clause. 
   *
   * @return a parse tree with a root containing KEYWORD_FROM, and a child
   *    containing a tree returned from iteratorDef.
   * @throws InvalidCharException passed through from match.
   * @throws OQLSyntaxException passed through from match.
   */
  private ParseTreeNode fromClause() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = match(KEYWORD_FROM);
    retNode.addChild(iteratorDef());
    
    return retNode;
  }
  
  /**
   * Consumes tokens of iteratorDef (the tables in the from part of an OQL 
   * query).  The EBNF grammar for iteratorDef looks like this:
   * <pre>
   *  iteratorDef             ::= identifier{.identifier} [ [as ] identifier ]
   *                            | identifier in identifier{.identifier}
   * </pre>
   * This method also does a transformation.  In OQL, the following 
   * iteratorDefs are equivalent:
   * <pre>
   *  x as a
   *
   *  x a
   *
   *  a in x
   * </pre>
   * These will all return a tree with root AS, first child x, and second 
   * child a.
   *
   * @return a Parse tress containing a single identifier or a root containing
   *    KEYWORD_AS with two IDENTIFIER children.
   * @throws InvalidCharException passed through from match.
   * @throws OQLSyntaxException passed through from match.
   */
  private ParseTreeNode iteratorDef() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = null;
    ParseTreeNode tableIdentifier = null;

    if ( _nextToken.getTokenType() == DOT ) {
      tableIdentifier = new ParseTreeNode( new Token( DOT, "." ) );
      tableIdentifier.addChild( match( IDENTIFIER ) );
    }
    else
      tableIdentifier = match(IDENTIFIER);

    while ( _curToken.getTokenType() == DOT ) {
      match( DOT );
      tableIdentifier.addChild( match( IDENTIFIER ) );
    }

    if (_curToken.getTokenType() == KEYWORD_AS) {
      retNode = match(KEYWORD_AS);
      retNode.addChild(tableIdentifier);
      retNode.addChild(match(IDENTIFIER));
    }
    else if (_curToken.getTokenType() == IDENTIFIER) {
      retNode = new ParseTreeNode(new Token(KEYWORD_AS, "as"));
      retNode.addChild(tableIdentifier);
      retNode.addChild(match(IDENTIFIER));
    }
    else if (_curToken.getTokenType() == KEYWORD_IN) {
      if ( tableIdentifier.getChildCount() > 0 ) 
        throw new OQLSyntaxException( "Only the class name in the from clause can contain dots." );
      match(KEYWORD_IN);
      retNode = new ParseTreeNode(new Token(KEYWORD_AS, "as"));
      
      ParseTreeNode classNode = null;
      if ( _nextToken.getTokenType() == DOT ) {
        classNode = new ParseTreeNode ( new Token( DOT, "." ) );
        classNode.addChild( match( IDENTIFIER ) );
      }
      else
        classNode = match( IDENTIFIER );
        
      while ( _curToken.getTokenType() == DOT ) {
        match( DOT );
        classNode.addChild( match( IDENTIFIER ) );
      }
      
      retNode.addChild( classNode );
      retNode.addChild(tableIdentifier);
    }

    if (retNode == null)
      return tableIdentifier;
    else
      return retNode;
  }
  
  /**
   * Consumes tokens of where clause. 
   *
   * @return a Parse tree with a root containing KEYWORD_WHERE, and one child
   *    containing a tree returned by expr.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode whereClause() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = match(KEYWORD_WHERE);
    retNode.addChild(expr());
    
    return retNode;
  }
  
  /**
   * Consumes tokens of expr clause. 
   *
   * @return a parse tree containing the return value of orExpr.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode expr() 
            throws InvalidCharException, OQLSyntaxException {
    
    return orExpr();
    
  }
  
  /**
   * Consumes tokens of orExpr clause. 
   *
   * @return a Parse tree containing a single andExpr tree, or a root 
   *    containing KEYWORD_OR with two andExpr tree children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode orExpr() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode tmpNode = null;
    ParseTreeNode leftSide = andExpr();

    // consume all sequential OR's
    while (_curToken.getTokenType() == KEYWORD_OR) {
      tmpNode = match(KEYWORD_OR);
      tmpNode.addChild(leftSide);
      tmpNode.addChild(andExpr());
      leftSide = tmpNode;
    }
    
    return leftSide;
  }
  
  /**
   * Consumes tokens of andExpr clause. 
   *
   * @return a Parse tree containing a single equalityExpr tree, or a root 
   *    containing KEYWORD_AND with two equalityExpr tree children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode andExpr() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode tmpNode = null;
    ParseTreeNode leftSide = equalityExpr();

    // consume all sequential AND's
    while (_curToken.getTokenType() == KEYWORD_AND) {
      tmpNode = match(KEYWORD_AND);
      tmpNode.addChild(leftSide);
      tmpNode.addChild(equalityExpr());
      leftSide = tmpNode;
    }
        
    return leftSide;
  }
  
  /**
   * Consumes tokens of equalityExpr clause. 
   *
   * @return a Parse tree containing a single relationalExpr tree, or a root 
   *    containing EQUAL, NOT_EQUAL, or KEYWORD_LIKE, with two relationalExpr 
   *    tree children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode equalityExpr() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = null;
    ParseTreeNode leftSide = relationalExpr();

    int tokenType = _curToken.getTokenType();
    switch (tokenType) {
      case EQUAL:
      case NOT_EQUAL:
      case KEYWORD_LIKE:
        retNode = match(tokenType);
        retNode.addChild(leftSide);
        retNode.addChild(relationalExpr());
    }
    
    if (retNode == null)
      return leftSide;
    else
      return retNode;
  }
  
  /**
   * Consumes tokens of relationalExpr clause. 
   *
   * @return a Parse tree containing a single additiveExpr tree, or a root 
   *    containing GT, GTE, LT, or LTE, with two additiveExpr tree children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode relationalExpr() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = null;
    ParseTreeNode leftSide = additiveExpr();

    int tokenType = _curToken.getTokenType();
    switch (tokenType) {
      case GT:
      case GTE:
      case LT:
      case LTE:
        retNode = match(tokenType);
        retNode.addChild(leftSide);
        retNode.addChild(additiveExpr());
        break;
      case KEYWORD_BETWEEN:
        retNode = match(KEYWORD_BETWEEN);
        retNode.addChild(leftSide);
        retNode.addChild(additiveExpr());
        match(KEYWORD_AND);
        retNode.addChild(additiveExpr());
    }
    
    if (retNode == null)
      return leftSide;
    else
      return retNode;
  }

  /**
   * Consumes tokens of additiveExpr clause. 
   *
   * @return a Parse tree containing a single multiplicativeExpr tree, or a 
   *    root containing PLUS, MINUS, or CONCAT, with two multiplicativeExpr 
   *    tree children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode additiveExpr() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = null;
    ParseTreeNode leftSide = multiplicativeExpr();

    int tokenType = _curToken.getTokenType();
    switch (tokenType) {
      case PLUS:
      case MINUS:
      case CONCAT:
        retNode = match(tokenType);
        retNode.addChild(leftSide);
        retNode.addChild(multiplicativeExpr());
    }
    
    if (retNode == null)
      return leftSide;
    else
      return retNode;
  }

  /**
   * Consumes tokens of multiplicativeExpr clause. 
   *
   * @return a Parse tree containing a single unaryExpr tree, or a 
   *    root containing TIMES, DIVIDE, or KEYWORD_MOD, with two unaryExpr 
   *    tree children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode multiplicativeExpr() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = null;
    ParseTreeNode leftSide = inExpr();

    int tokenType = _curToken.getTokenType();
    switch (tokenType) {
      case TIMES:
      case DIVIDE:
      case KEYWORD_MOD:
        retNode = match(tokenType);
        retNode.addChild(leftSide);
        retNode.addChild(inExpr());
    }
    
    if (retNode == null)
      return leftSide;
    else
      return retNode;
  }

  /**
   * Consumes tokens of inExpr clause. 
   *
   * @return a Parse tree containing a single unaryExpr tree, or a 
   *    root containing KEYWORD_IN, with two unaryExpr 
   *    tree children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode inExpr() 
            throws InvalidCharException, OQLSyntaxException {
    
    ParseTreeNode retNode = null;
    ParseTreeNode leftSide = unaryExpr();

    if ( _curToken.getTokenType() == KEYWORD_IN ) {
      retNode = match(KEYWORD_IN);
      retNode.addChild(leftSide);
      retNode.addChild(unaryExpr());
    }
    
    if (retNode == null)
      return leftSide;
    else
      return retNode;
  }
  

  /**
   * Consumes tokens of unaryExpr clause. 
   *
   * @return a Parse tree containing a single primaryExpr tree, or a unary 
   *    operator root, with a single unaryExpr tree child.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode unaryExpr() 
            throws InvalidCharException, OQLSyntaxException {
            
    ParseTreeNode retNode = null;

    int tokenType = _curToken.getTokenType();
    switch (tokenType) {
      case PLUS:
      case MINUS:
      case KEYWORD_ABS:
      case KEYWORD_NOT:
        retNode = match(tokenType);
        retNode.addChild(unaryExpr());
    }
    
    if (retNode == null)
      return postfixExpr();
    else
      return retNode;
  }

  /**
   * Consumes tokens of postfixExpr. This method also performs a transformation
   * returning a tree with DOT as the root and two IDENTIFIERS as children for
   * both <code>IDENTIFIER.IDENTIFIER</code> and 
   * <code>IDENTIFIER-&gt;IDENTIFIER</code>
   *
   * @return a Parse tree containing a single primaryExpr tree, or a DOT root 
   *    with two IDENTIFIER children.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match().
   */
  private ParseTreeNode postfixExpr() 
            throws InvalidCharException, OQLSyntaxException {
            
    ParseTreeNode retNode = null;

    int curTokenType = _curToken.getTokenType();
    int nextTokenType = 0;
    if (_nextToken != null)
      nextTokenType = _nextToken.getTokenType();
    
    if ( (curTokenType == IDENTIFIER) && 
         ( (nextTokenType == DOT) || (nextTokenType == ARROW) ) ) {
      
      retNode = new ParseTreeNode(new Token(DOT, "."));
      while ( (curTokenType == IDENTIFIER) && 
              ( (nextTokenType == DOT) || (nextTokenType == ARROW) ) ) {
        retNode.addChild(match(IDENTIFIER));
        match(nextTokenType);  //the dot or arrow
        curTokenType = _curToken.getTokenType();
        if (_nextToken != null)
          nextTokenType = _nextToken.getTokenType();
        else
          nextTokenType = 0;
      }
      retNode.addChild(match(IDENTIFIER));
    }
    
    if (retNode == null)
      return primaryExpr();
    else
      return retNode;
  }

  /**
   * Consumes tokens of primaryExpr clause. 
   *
   * @return a Parse tree containing a single primaryExpr tree, which is either
   *    an expr, a queryParam, an identifier, or a literal.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match(), or if an unknown
   *    token is encountered here.
   */
  private ParseTreeNode primaryExpr() 
            throws InvalidCharException, OQLSyntaxException {
            
    ParseTreeNode retNode = null;

    int tokenType = _curToken.getTokenType();
    switch (tokenType) {
      case LPAREN:
        retNode = match(LPAREN);
        retNode.addChild(expr());
        match(RPAREN);
        break;
      case KEYWORD_IS_DEFINED:
      case KEYWORD_IS_UNDEFINED:
        retNode = undefinedExpr();
        break;
      case KEYWORD_LIST:
        retNode = collectionExpr();
        break;
      case KEYWORD_COUNT:
      case KEYWORD_SUM:
      case KEYWORD_MIN:
      case KEYWORD_MAX:
      case KEYWORD_AVG:
        retNode = aggregateExpr();
        break;
      case DOLLAR:
        retNode = queryParam();
        break;
      case IDENTIFIER:
        if ( _nextToken.getTokenType() == LPAREN )
          retNode = functionCall();
        else
          retNode = match( IDENTIFIER );
        break;
      case KEYWORD_NIL:
      case KEYWORD_UNDEFINED:
      case BOOLEAN_LITERAL:
      case LONG_LITERAL:
      case DOUBLE_LITERAL:
      case CHAR_LITERAL:
      case STRING_LITERAL:
      case DATE_LITERAL:
      case TIME_LITERAL:
      case TIMESTAMP_LITERAL:
        retNode = match(tokenType);
        break;
      default:
        throw (new OQLSyntaxException("An inapropriate token ("+
                      String.valueOf( tokenType )+
                      ") was encountered in an expression."));
    }
    
    if (retNode == null)
      return primaryExpr();
    else
      return retNode;
  }

  /**
   * Consumes tokens of a function call. 
   *
   * @return a Parse tree containing an identifier root which is the name of 
   *      the function, child LPAREN with children the arguments.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match(), or if an unknown
   *    token is encountered here.
   */
  private ParseTreeNode functionCall() 
            throws InvalidCharException, OQLSyntaxException {

    int tokenType = _curToken.getTokenType();
    int nextTokenType = _nextToken.getTokenType();

    if ( tokenType != IDENTIFIER || nextTokenType != LPAREN )
      throw new OQLSyntaxException( "Expected a function call and did not find one, near " + _curToken.getTokenValue() );

    ParseTreeNode retNode = match( IDENTIFIER );
    ParseTreeNode parNode = match( LPAREN );
    parNode.addChild( unaryExpr() );
    while ( _curToken.getTokenType() == COMMA ) {
        match( COMMA );
        parNode.addChild( unaryExpr() );
    }
    retNode.addChild( parNode );
    retNode.addChild( match( RPAREN ) );
/*    retNode.addChild( match( LPAREN ) );

    tokenType = _curToken.getTokenType();
    while ( tokenType != RPAREN )
      retNode.addChild( expr() );

    match( RPAREN );*/

    return retNode;
  }

  /**
   * Consumes tokens of collectionExpr.
   *
   * @return Parse Tree with the root containing the function call and
   *    the children containing the parameters
   * @throws InvalidCharException passed through from match()
   * @throws OQLSyntaxException passed through from match() or if an
   *    unexpected token is encountered here.
   */
  private ParseTreeNode collectionExpr()
            throws InvalidCharException, OQLSyntaxException {
    
    if ( _curToken.getTokenType() == KEYWORD_LIST ) 
    {
      ParseTreeNode retNode = match( KEYWORD_LIST );
      
      match( LPAREN );
      
      retNode.addChild(expr());
      
      while ( _curToken.getTokenType() == COMMA ) {
        match( COMMA );
        retNode.addChild( expr() );
      }

      match( RPAREN );
      
      return( retNode );
    }

    throw new OQLSyntaxException( "Expected collectionExpr and didn't find it at or near: " + _curToken.getTokenValue() );
  }

  /**
   * Consumes tokens of aggregateExpr.
   *
   * @return Parse Tree with the root containing the function call and 
   *    the child containing the parameter.
   * @throws InvalidCharException passed through from match()
   * @throws OQLSyntaxException passed through from match() or if an
   *    unexpected token is encountered here.
   */
  private ParseTreeNode aggregateExpr()
            throws InvalidCharException, OQLSyntaxException {
    
    int tokenType = _curToken.getTokenType();
    ParseTreeNode retNode = null;
    
    switch ( tokenType ) {
      case KEYWORD_SUM:
      case KEYWORD_MIN:
      case KEYWORD_MAX:
      case KEYWORD_AVG:
        retNode = match( tokenType );
        match( LPAREN );
        retNode.addChild( expr() );
        match( RPAREN );
        break;
      case KEYWORD_COUNT:
        //special case because it supports count(*)
        retNode = match( KEYWORD_COUNT );
        match( LPAREN );
        
        if ( _curToken.getTokenType() == TIMES )
          retNode.addChild( match( TIMES ) );
        else
          retNode.addChild( expr() );
          
        match( RPAREN );
        break;
      default:
        throw new OQLSyntaxException( "Expected aggregateExpr and didn't find it at or near: " + _curToken.getTokenValue() );
    }
      
    return( retNode );

  }

  /**
   * Consumes tokens of undefinedExpr.
   *
   * @return Parse Tree with the root containing the function call and 
   *    the child containing the parameter
   * @throws InvalidCharException passed through from match()
   * @throws OQLSyntaxException passed through from match() or if an
   *    unexpected token is encountered here.
   */
  private ParseTreeNode undefinedExpr()
            throws InvalidCharException, OQLSyntaxException {
    
    int tokenType = _curToken.getTokenType();
    if ( tokenType == KEYWORD_IS_DEFINED || 
         tokenType == KEYWORD_IS_UNDEFINED ) 
    {
      ParseTreeNode retNode = match( tokenType );
      match( LPAREN );
      if ( _nextToken.getTokenType() == DOT ) {
        ParseTreeNode childNode = new ParseTreeNode(new Token( DOT, "." ));
        childNode.addChild( match( IDENTIFIER ) );
        while ( _curToken.getTokenType() == DOT ) {
          match( DOT );
          childNode.addChild( match( IDENTIFIER ) );
        }
        retNode.addChild( childNode );
      }
      else
        retNode.addChild( match( IDENTIFIER ) );
      match( RPAREN );
      return( retNode );
    }

    throw new OQLSyntaxException( "Expected undefinedExpr and didn't find it at or near: " + _curToken.getTokenValue() );
  }

  /**
   * Consumes tokens of queryParam. 
   *
   * @return a Parse tree containing DOLLAR as the root, and either one child
   *    which is a LONG_LITERAL, or one child which is an IDENTIFIER, and one 
   *    child which is a LONG_LITERAL.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match(), or if an unknown
   *    token is encountered here.
   */
  private ParseTreeNode queryParam() 
            throws InvalidCharException, OQLSyntaxException {
            
    ParseTreeNode retNode = match(DOLLAR);

    int tokenType = _curToken.getTokenType();
    switch (tokenType) {
      case LPAREN:
        match(LPAREN);
        retNode.addChild(match(IDENTIFIER));
        match(RPAREN);
        retNode.addChild(match(LONG_LITERAL));
        break;
      case LONG_LITERAL:
        retNode.addChild(match(LONG_LITERAL));
        break;
      default:
        throw (new OQLSyntaxException("An inapropriate token was encountered in a query parameter."));
    }
    
    return retNode;
  }

  /**
   * Consumes tokens of orderClause. 
   *
   * @return a Parse tree containing ORDER as the root, with children 
   *    as order parameters.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match(), or if an 
   *    unknown token is encountered here.
   */
  private ParseTreeNode orderClause() 
            throws InvalidCharException, OQLSyntaxException {
            
    ParseTreeNode retNode = match(KEYWORD_ORDER);
    match(KEYWORD_BY);

    ParseTreeNode curExpression = null;
    ParseTreeNode curOrder = null;

    curExpression = expr();

    int tokenType = _curToken.getTokenType();
    if ( tokenType == KEYWORD_ASC || tokenType == KEYWORD_DESC ) {
      curOrder = match( tokenType );
      curOrder.addChild( curExpression );
      retNode.addChild( curOrder );
    }
    else {
      retNode.addChild( curExpression );
    }

    while ( _curToken.getTokenType() == COMMA ) {
      match( COMMA );
      
      curExpression = expr();

      tokenType = _curToken.getTokenType();
      if ( tokenType == KEYWORD_ASC || tokenType == KEYWORD_DESC ) {
        curOrder = match( tokenType );
        curOrder.addChild( curExpression );
        retNode.addChild( curOrder );
      }
      else {
        retNode.addChild( curExpression );
      }

    }
    
    return retNode;
  }

  /**
   * Consumes tokens of limitClause.
   *
   * @return a Parse tree containing LIMIT as the root, with children
   *    as limit parameters.
   * @throws InvalidCharException passed through from match().
   * @throws OQLSyntaxException passed through from match(), or if an
   *    unknown token is encountered here.
   */
  private ParseTreeNode limitClause()
            throws InvalidCharException, OQLSyntaxException {

    ParseTreeNode retNode = match(KEYWORD_LIMIT);

    retNode.addChild(queryParam());
      if ( _curToken.getTokenType() == COMMA )
    {
      retNode.addChild( match( COMMA ) );
      retNode.addChild( queryParam() );
    }

    return retNode;
  }

}

