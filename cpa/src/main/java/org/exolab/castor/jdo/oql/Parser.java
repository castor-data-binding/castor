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
 * Generates a parse tree for a stream of tokens representing an OQL query. 
 *
 * @author  <a href="nissim@nksystems.com">Nissim Karpenstein</a>
 * @version $Revision$ $Date: 2006-01-03 17:47:48 -0700 (Tue, 03 Jan 2006) $
 */
public final class Parser {
    private static final HashMap TOKEN_TYPES = new HashMap();

    static {
        TOKEN_TYPES.put(new Integer(TokenType.END_OF_QUERY), "END_OF_QUERY");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_SELECT), "KEYWORD_SELECT");
        TOKEN_TYPES.put(new Integer(TokenType.IDENTIFIER), "IDENTIFIER");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_AS), "KEYWORD_AS");
        TOKEN_TYPES.put(new Integer(TokenType.COLON), "COLON");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_FROM), "KEYWORD_FROM");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_IN), "KEYWORD_IN");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_WHERE), "KEYWORD_WHERE");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_OR), "KEYWORD_OR");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_AND), "KEYWORD_AND");
        TOKEN_TYPES.put(new Integer(TokenType.EQUAL), "EQUAL");
        TOKEN_TYPES.put(new Integer(TokenType.NOT_EQUAL), "NOT_EQUAL");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_LIKE), "KEYWORD_LIKE");
        TOKEN_TYPES.put(new Integer(TokenType.LT), "LT");
        TOKEN_TYPES.put(new Integer(TokenType.LTE), "LTE");
        TOKEN_TYPES.put(new Integer(TokenType.GT), "GT");
        TOKEN_TYPES.put(new Integer(TokenType.GTE), "GTE");
        TOKEN_TYPES.put(new Integer(TokenType.PLUS), "PLUS");
        TOKEN_TYPES.put(new Integer(TokenType.MINUS), "MINUS");
        TOKEN_TYPES.put(new Integer(TokenType.CONCAT), "CONCAT");
        TOKEN_TYPES.put(new Integer(TokenType.TIMES), "MULTIPLY");
        TOKEN_TYPES.put(new Integer(TokenType.DIVIDE), "DIVIDE");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_MOD), "KEYWORD_MOD");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_ABS), "KEYWORD_ABS");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_NOT), "KEYWORD_NOT");
        TOKEN_TYPES.put(new Integer(TokenType.LPAREN), "LEFT_PAREN");
        TOKEN_TYPES.put(new Integer(TokenType.RPAREN), "RIGHT_PAREN");
        TOKEN_TYPES.put(new Integer(TokenType.DOLLAR), "DOLLAR");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_NIL), "KEYWORD_NIL");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_UNDEFINED), "KEYWORD_UNDEFINED");
        TOKEN_TYPES.put(new Integer(TokenType.DOT), "DOT");
        TOKEN_TYPES.put(new Integer(TokenType.ARROW), "ARROW");
        TOKEN_TYPES.put(new Integer(TokenType.BOOLEAN_LITERAL), "BOOLEAN_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.LONG_LITERAL), "LONG_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.DOUBLE_LITERAL), "DOUBLE_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.CHAR_LITERAL), "CHAR_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.STRING_LITERAL), "STRING_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.DATE_LITERAL), "DATE_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.TIME_LITERAL), "TIME_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.TIMESTAMP_LITERAL), "TIMESTAMP_LITERAL");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_BETWEEN), "KEYWORD_BETWEEN");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_DISTINCT), "KEYWORD_DISTINCT");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_IS_DEFINED), "KEYWORD_IS_DEFINED");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_IS_UNDEFINED), "KEYWORD_IS_UNDEFINED");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_LIST), "KEYWORD_LIST");
        TOKEN_TYPES.put(new Integer(TokenType.COMMA), "COMMA");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_ORDER), "KEYWORD_ORDER");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_BY), "KEYWORD_BY");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_ASC), "KEYWORD_ASC");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_DESC), "KEYWORD_DESC");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_COUNT), "KEYWORD_COUNT");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_SUM), "KEYWORD_SUM");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_MIN), "KEYWORD_MIN");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_MAX), "KEYWORD_MAX");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_AVG), "KEYWORD_AVG");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_LIMIT), "KEYWORD_LIMIT");
        TOKEN_TYPES.put(new Integer(TokenType.KEYWORD_OFFSET), "KEYWORD_OFFSET");
    }

    private Lexer _lexer;
    private Token _curToken;
    private Token _nextToken;
    private ParseTreeNode _treeRoot;

    /**
     * Creates a parser which will generate a parse tree from a series of tokens.
     * 
     * @param lexer Lexer instance.
     * @throws InvalidCharException thrown by primeLexer.
     * @throws OQLSyntaxException thrown by primeLexer.
     */
    public Parser(final Lexer lexer) throws InvalidCharException {
        _lexer = lexer;
        primeLexer();
    }

    /**
     * Generates the parse tree for the tokens provided by the Lexer passed in
     * the constructor.
     * 
     * @return a ParseTreeNode representing the query.
     * @throws InvalidCharException thrown by match.
     * @throws OQLSyntaxException thrown by match.
     */
    public ParseTreeNode getParseTree() throws InvalidCharException, OQLSyntaxException {
        _treeRoot = match(TokenType.KEYWORD_SELECT);

        if (_curToken.getTokenType() == TokenType.KEYWORD_DISTINCT) {
            _treeRoot.addChild(match(TokenType.KEYWORD_DISTINCT));
        }

        _treeRoot.addChild(projectionAttributes());
        _treeRoot.addChild(fromClause());

        if (_curToken.getTokenType() == TokenType.KEYWORD_WHERE) {
            _treeRoot.addChild(whereClause());
        }

        if (_curToken.getTokenType() == TokenType.KEYWORD_ORDER) {
            _treeRoot.addChild(orderClause());
        }

        if (_curToken.getTokenType() == TokenType.KEYWORD_LIMIT) {
            _treeRoot.addChild(limitClause());

            if (_curToken.getTokenType() == TokenType.KEYWORD_OFFSET) {
                _treeRoot.addChild(offsetClause());
            }
        }

        match(TokenType.END_OF_QUERY);

        return _treeRoot;
    }

    /**
     * Primes the _curToken and _nextToken private members by calling
     * {@link Lexer#nextToken()}.
     * 
     * @throws InvalidCharException thrown by the Lexer.
     */
    private void primeLexer() throws InvalidCharException {
        _curToken = _lexer.nextToken();
        _nextToken = _lexer.nextToken();
    }

    /**
     * Tests whether the current token has the same token type that was passed.
     * If the test passes, a ParseTreeNode is returned containing the current
     * token, and the _curToken and _nextToken private members are advanced.
     * If the test fails, an exception is thrown.
     *
     * @param tokenType The token type to compare the current token to.
     * @return A ParseTreeNode containing the current Token if the test is 
     *         successful, otherwise throws an exception.
     * @throws InvalidCharException thrown by Lexer.
     * @throws OQLSyntaxException if the token types don't match.
     */
    private ParseTreeNode match(final int tokenType)
    throws InvalidCharException, OQLSyntaxException {
        if (_curToken.getTokenType() != tokenType) {
            throw new OQLSyntaxException("An incorrect token type was found near "
                    + _curToken.getTokenValue() + " (found "
                    + (String) TOKEN_TYPES.get(new Integer(_curToken.getTokenType()))
                    + ", but expected " + (String) TOKEN_TYPES.get(new Integer(tokenType))
                    + ")");
        }

        ParseTreeNode retNode = new ParseTreeNode(_curToken);
        _curToken = _nextToken;
        _nextToken = _lexer.nextToken();

        return retNode;
    }

    /**
     * Consumes tokens of projection attributes (the query targets in the select
     * part of an OQL query). This method also does a transformation. In OQL, 
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
     *         KEYWORD_AS, with two IDENTIFIER children.
     * @throws InvalidCharException passed through from match.
     * @throws OQLSyntaxException passed through from match.
     */
    private ParseTreeNode projectionAttributes()
    throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;
        ParseTreeNode queryTarget = expr();

        if (_curToken.getTokenType() == TokenType.KEYWORD_AS) {
            retNode = match(TokenType.KEYWORD_AS);
            retNode.addChild(queryTarget);
            retNode.addChild(match(TokenType.IDENTIFIER));
        } else if (_curToken.getTokenType() == TokenType.COLON) {
            if (queryTarget.getToken().getTokenType() != TokenType.IDENTIFIER) {
                throw new OQLSyntaxException("When using the ':' in projection "
                        + "attributes (select part of query) the token before the ':' "
                        + "must be an identifier.");
            }
            match(TokenType.COLON);
            retNode = new ParseTreeNode(Token.KEYWORD_AS);
            retNode.addChild(expr());
            retNode.addChild(queryTarget);
        }

        if (retNode == null) { return queryTarget; }
        return retNode;
    }
  
    /**
     * Consumes tokens of from clause. 
     *
     * @return a parse tree with a root containing KEYWORD_FROM, and a child
     *         containing a tree returned from iteratorDef.
     * @throws InvalidCharException passed through from match.
     * @throws OQLSyntaxException passed through from match.
     */
    private ParseTreeNode fromClause() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = match(TokenType.KEYWORD_FROM);
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
     *         KEYWORD_AS with two IDENTIFIER children.
     * @throws InvalidCharException passed through from match.
     * @throws OQLSyntaxException passed through from match.
     */
    private ParseTreeNode iteratorDef() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;
        ParseTreeNode tableIdentifier = null;

        if (_nextToken.getTokenType() == TokenType.DOT) {
            tableIdentifier = new ParseTreeNode(Token.DOT);
            tableIdentifier.addChild(match(TokenType.IDENTIFIER));
        } else {
            tableIdentifier = match(TokenType.IDENTIFIER);
        }

        while (_curToken.getTokenType() == TokenType.DOT) {
            match(TokenType.DOT);
            tableIdentifier.addChild(match(TokenType.IDENTIFIER));
        }

        if (_curToken.getTokenType() == TokenType.KEYWORD_AS) {
            retNode = match(TokenType.KEYWORD_AS);
            retNode.addChild(tableIdentifier);
            retNode.addChild(match(TokenType.IDENTIFIER));
        } else if (_curToken.getTokenType() == TokenType.IDENTIFIER) {
            retNode = new ParseTreeNode(Token.KEYWORD_AS);
            retNode.addChild(tableIdentifier);
            retNode.addChild(match(TokenType.IDENTIFIER));
        } else if (_curToken.getTokenType() == TokenType.KEYWORD_IN) {
            if (tableIdentifier.getChildCount() > 0) {
                throw new OQLSyntaxException("Only the class name in the from clause "
                        + "can contain dots.");
            }
            match(TokenType.KEYWORD_IN);
            retNode = new ParseTreeNode(Token.KEYWORD_AS);
            
            ParseTreeNode classNode = null;
            if (_nextToken.getTokenType() == TokenType.DOT) {
                classNode = new ParseTreeNode(Token.DOT);
                classNode.addChild(match(TokenType.IDENTIFIER));
            } else {
                classNode = match(TokenType.IDENTIFIER);
            }
              
            while (_curToken.getTokenType() == TokenType.DOT) {
                match(TokenType.DOT);
                classNode.addChild(match(TokenType.IDENTIFIER));
            }
            
            retNode.addChild(classNode);
            retNode.addChild(tableIdentifier);
        }

        if (retNode == null) { return tableIdentifier; }
        return retNode;
    }
  
    /**
     * Consumes tokens of where clause. 
     *
     * @return a Parse tree with a root containing KEYWORD_WHERE, and one child
     *         containing a tree returned by expr.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode whereClause() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = match(TokenType.KEYWORD_WHERE);
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
    private ParseTreeNode expr() throws InvalidCharException, OQLSyntaxException {
        return orExpr();
    }
  
    /**
     * Consumes tokens of orExpr clause. 
     *
     * @return a Parse tree containing a single andExpr tree, or a root 
     *         containing KEYWORD_OR with two andExpr tree children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode orExpr() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode tmpNode = null;
        ParseTreeNode leftSide = andExpr();

        // consume all sequential OR's
        while (_curToken.getTokenType() == TokenType.KEYWORD_OR) {
            tmpNode = match(TokenType.KEYWORD_OR);
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
     *         containing KEYWORD_AND with two equalityExpr tree children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode andExpr() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode tmpNode = null;
        ParseTreeNode leftSide = equalityExpr();

        // consume all sequential AND's
        while (_curToken.getTokenType() == TokenType.KEYWORD_AND) {
            tmpNode = match(TokenType.KEYWORD_AND);
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
     *         containing EQUAL, NOT_EQUAL, or KEYWORD_LIKE, with two relationalExpr 
     *         tree children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode equalityExpr() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;
        ParseTreeNode leftSide = relationalExpr();

        int tokenType = _curToken.getTokenType();
        switch (tokenType) {
        case TokenType.EQUAL:
        case TokenType.NOT_EQUAL:
        case TokenType.KEYWORD_LIKE:
            retNode = match(tokenType);
            retNode.addChild(leftSide);
            retNode.addChild(relationalExpr());
        default:
            break;
        }
        
        if (retNode == null) { return leftSide; }
        return retNode;
    }
  
    /**
     * Consumes tokens of relationalExpr clause. 
     *
     * @return a Parse tree containing a single additiveExpr tree, or a root 
     *         containing GT, GTE, LT, or LTE, with two additiveExpr tree children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode relationalExpr()
    throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;
        ParseTreeNode leftSide = additiveExpr();

        int tokenType = _curToken.getTokenType();
        switch (tokenType) {
        case TokenType.GT:
        case TokenType.GTE:
        case TokenType.LT:
        case TokenType.LTE:
            retNode = match(tokenType);
            retNode.addChild(leftSide);
            retNode.addChild(additiveExpr());
            break;
        case TokenType.KEYWORD_BETWEEN:
            retNode = match(TokenType.KEYWORD_BETWEEN);
            retNode.addChild(leftSide);
            retNode.addChild(additiveExpr());
            match(TokenType.KEYWORD_AND);
            retNode.addChild(additiveExpr());
            break;
        default:
            break;
        }
        
        if (retNode == null) { return leftSide; }
        return retNode;
    }

    /**
     * Consumes tokens of additiveExpr clause. 
     *
     * @return a Parse tree containing a single multiplicativeExpr tree, or a 
     *         root containing PLUS, MINUS, or CONCAT, with two multiplicativeExpr 
     *         tree children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode additiveExpr() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;
        ParseTreeNode leftSide = multiplicativeExpr();

        int tokenType = _curToken.getTokenType();
        switch (tokenType) {
        case TokenType.PLUS:
        case TokenType.MINUS:
        case TokenType.CONCAT:
            retNode = match(tokenType);
            retNode.addChild(leftSide);
            retNode.addChild(multiplicativeExpr());
            break;
        default:
            break;
        }
        
        if (retNode == null) { return leftSide; }
        return retNode;
    }

    /**
     * Consumes tokens of multiplicativeExpr clause. 
     *
     * @return a Parse tree containing a single unaryExpr tree, or a 
     *         root containing TIMES, DIVIDE, or KEYWORD_MOD, with two unaryExpr 
     *         tree children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode multiplicativeExpr()
    throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;
        ParseTreeNode leftSide = inExpr();

        int tokenType = _curToken.getTokenType();
        switch (tokenType) {
        case TokenType.TIMES:
        case TokenType.DIVIDE:
        case TokenType.KEYWORD_MOD:
            retNode = match(tokenType);
            retNode.addChild(leftSide);
            retNode.addChild(inExpr());
            break;
        default:
            break;
        }
        
        if (retNode == null) { return leftSide; }
        return retNode;
    }

    /**
     * Consumes tokens of inExpr clause. 
     *
     * @return a Parse tree containing a single unaryExpr tree, or a 
     *         root containing KEYWORD_IN, with two unaryExpr 
     *         tree children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode inExpr() throws InvalidCharException, OQLSyntaxException {
      ParseTreeNode retNode = null;
      ParseTreeNode leftSide = unaryExpr();

      if (_curToken.getTokenType() == TokenType.KEYWORD_IN) {
          retNode = match(TokenType.KEYWORD_IN);
          retNode.addChild(leftSide);
          retNode.addChild(unaryExpr());
      }
      
      if (retNode == null) { return leftSide; }
      return retNode;
    }
  
    /**
     * Consumes tokens of unaryExpr clause. 
     *
     * @return a Parse tree containing a single primaryExpr tree, or a unary 
     *         operator root, with a single unaryExpr tree child.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode unaryExpr() throws InvalidCharException, OQLSyntaxException {
      ParseTreeNode retNode = null;

      int tokenType = _curToken.getTokenType();
      switch (tokenType) {
      case TokenType.PLUS:
      case TokenType.MINUS:
      case TokenType.KEYWORD_ABS:
      case TokenType.KEYWORD_NOT:
          retNode = match(tokenType);
          retNode.addChild(unaryExpr());
          break;
      default:
          break;
      }
      
      if (retNode == null) { return postfixExpr(); }
      return retNode;
    }

    /**
     * Consumes tokens of postfixExpr. This method also performs a transformation
     * returning a tree with DOT as the root and two IDENTIFIERS as children for
     * both <code>IDENTIFIER.IDENTIFIER</code> and 
     * <code>IDENTIFIER-&gt;IDENTIFIER</code>
     *
     * @return a Parse tree containing a single primaryExpr tree, or a DOT root 
     *         with two IDENTIFIER children.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match().
     */
    private ParseTreeNode postfixExpr() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;

        int curTokenType = _curToken.getTokenType();
        int nextTokenType = 0;
        if (_nextToken != null) {
            nextTokenType = _nextToken.getTokenType();
        }
        
        if ((curTokenType == TokenType.IDENTIFIER)
                && ((nextTokenType == TokenType.DOT)
                || (nextTokenType == TokenType.ARROW))) {
          
            retNode = new ParseTreeNode(Token.DOT);
            while ((curTokenType == TokenType.IDENTIFIER)
                    && ((nextTokenType == TokenType.DOT)
                    || (nextTokenType == TokenType.ARROW))) {
                
                retNode.addChild(match(TokenType.IDENTIFIER));
                match(nextTokenType);  //the dot or arrow
                curTokenType = _curToken.getTokenType();
                if (_nextToken != null) {
                    nextTokenType = _nextToken.getTokenType();
                } else {
                    nextTokenType = 0;
                }
            }
            retNode.addChild(match(TokenType.IDENTIFIER));
        }
        
        if (retNode == null) { return primaryExpr(); }
        return retNode;
    }

    /**
     * Consumes tokens of primaryExpr clause. 
     *
     * @return a Parse tree containing a single primaryExpr tree, which is either
     *         an expr, a queryParam, an identifier, or a literal.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match(), or if an unknown
     *         token is encountered here.
     */
    private ParseTreeNode primaryExpr() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = null;

        int tokenType = _curToken.getTokenType();
        switch (tokenType) {
        case TokenType.LPAREN:
            retNode = match(TokenType.LPAREN);
            retNode.addChild(expr());
            match(TokenType.RPAREN);
            break;
        case TokenType.KEYWORD_IS_DEFINED:
        case TokenType.KEYWORD_IS_UNDEFINED:
            retNode = undefinedExpr();
            break;
        case TokenType.KEYWORD_LIST:
            retNode = collectionExpr();
            break;
        case TokenType.KEYWORD_COUNT:
        case TokenType.KEYWORD_SUM:
        case TokenType.KEYWORD_MIN:
        case TokenType.KEYWORD_MAX:
        case TokenType.KEYWORD_AVG:
            retNode = aggregateExpr();
            break;
        case TokenType.DOLLAR:
            retNode = queryParam();
            break;
        case TokenType.IDENTIFIER:
            if (_nextToken.getTokenType() == TokenType.LPAREN) {
                retNode = functionCall();
            } else {
                retNode = match(TokenType.IDENTIFIER);
            }
            break;
        case TokenType.KEYWORD_NIL:
        case TokenType.KEYWORD_UNDEFINED:
        case TokenType.BOOLEAN_LITERAL:
        case TokenType.LONG_LITERAL:
        case TokenType.DOUBLE_LITERAL:
        case TokenType.CHAR_LITERAL:
        case TokenType.STRING_LITERAL:
        case TokenType.DATE_LITERAL:
        case TokenType.TIME_LITERAL:
        case TokenType.TIMESTAMP_LITERAL:
            retNode = match(tokenType);
            break;
        default:
            throw new OQLSyntaxException("An inapropriate token ("
                    + String.valueOf(tokenType) + ") was encountered in an expression.");
        }
        
        if (retNode == null) { return primaryExpr(); }
        return retNode;
    }

    /**
     * Consumes tokens of a function call. 
     *
     * @return a Parse tree containing an identifier root which is the name of 
     *         the function, child LPAREN with children the arguments.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match(), or if an unknown
     *         token is encountered here.
     */
    private ParseTreeNode functionCall() throws InvalidCharException, OQLSyntaxException {
        int tokenType = _curToken.getTokenType();
        int nextTokenType = _nextToken.getTokenType();

        if ((tokenType != TokenType.IDENTIFIER) || (nextTokenType != TokenType.LPAREN)) {
            throw new OQLSyntaxException("Expected a function call and did not find "
                    + "one, near " + _curToken.getTokenValue());
        }

        ParseTreeNode retNode = match(TokenType.IDENTIFIER);
        ParseTreeNode parNode = match(TokenType.LPAREN);
        parNode.addChild(unaryExpr());
        while (_curToken.getTokenType() == TokenType.COMMA) {
            match(TokenType.COMMA);
            parNode.addChild(unaryExpr());
        }
        retNode.addChild(parNode);
        retNode.addChild(match(TokenType.RPAREN));

        return retNode;
    }

    /**
     * Consumes tokens of collectionExpr.
     *
     * @return Parse Tree with the root containing the function call and
     *         the children containing the parameters
     * @throws InvalidCharException passed through from match()
     * @throws OQLSyntaxException passed through from match() or if an
     *         unexpected token is encountered here.
     */
    private ParseTreeNode collectionExpr()
    throws InvalidCharException, OQLSyntaxException {
      if (_curToken.getTokenType() == TokenType.KEYWORD_LIST) {
        ParseTreeNode retNode = match(TokenType.KEYWORD_LIST);
        
        match(TokenType.LPAREN);
        
        retNode.addChild(expr());
        
        while (_curToken.getTokenType() == TokenType.COMMA) {
          match(TokenType.COMMA);
          retNode.addChild(expr());
        }

        match(TokenType.RPAREN);
        
        return retNode;
      }

      throw new OQLSyntaxException("Expected collectionExpr and didn't find it at "
              + "or near: " + _curToken.getTokenValue());
    }

    /**
     * Consumes tokens of aggregateExpr.
     *
     * @return Parse Tree with the root containing the function call and 
     *         the child containing the parameter.
     * @throws InvalidCharException passed through from match()
     * @throws OQLSyntaxException passed through from match() or if an
     *         unexpected token is encountered here.
     */
    private ParseTreeNode aggregateExpr()
    throws InvalidCharException, OQLSyntaxException {
        int tokenType = _curToken.getTokenType();
        ParseTreeNode retNode = null;
        
        switch (tokenType) {
        case TokenType.KEYWORD_SUM:
        case TokenType.KEYWORD_MIN:
        case TokenType.KEYWORD_MAX:
        case TokenType.KEYWORD_AVG:
            retNode = match(tokenType);
            match(TokenType.LPAREN);
            retNode.addChild(expr());
            match(TokenType.RPAREN);
            break;
        case TokenType.KEYWORD_COUNT:
            //special case because it supports count(*)
            retNode = match(TokenType.KEYWORD_COUNT);
            match(TokenType.LPAREN);
            
            if (_curToken.getTokenType() == TokenType.TIMES) {
                retNode.addChild(match(TokenType.TIMES));
            } else if (_curToken.getTokenType() == TokenType.KEYWORD_DISTINCT) {
                retNode.addChild(match(TokenType.KEYWORD_DISTINCT));
                retNode.addChild(expr());
            } else {
                retNode.addChild(expr());
            }
              
            match(TokenType.RPAREN);
            break;
        default:
            throw new OQLSyntaxException("Expected aggregateExpr and didn't find it "
                    + "at or near: " + _curToken.getTokenValue());
        }
          
        return retNode;
    }

    /**
     * Consumes tokens of undefinedExpr.
     *
     * @return Parse Tree with the root containing the function call and 
     *         the child containing the parameter
     * @throws InvalidCharException passed through from match()
     * @throws OQLSyntaxException passed through from match() or if an
     *         unexpected token is encountered here.
     */
    private ParseTreeNode undefinedExpr()
    throws InvalidCharException, OQLSyntaxException {
        int tokenType = _curToken.getTokenType();
        if ((tokenType == TokenType.KEYWORD_IS_DEFINED)
                || (tokenType == TokenType.KEYWORD_IS_UNDEFINED)) {
            ParseTreeNode retNode = match(tokenType);
            match(TokenType.LPAREN);
            if (_nextToken.getTokenType() == TokenType.DOT) {
                ParseTreeNode childNode = new ParseTreeNode(Token.DOT);
                childNode.addChild(match(TokenType.IDENTIFIER));
                while (_curToken.getTokenType() == TokenType.DOT) {
                    match(TokenType.DOT);
                    childNode.addChild(match(TokenType.IDENTIFIER));
                }
                retNode.addChild(childNode);
            } else {
                retNode.addChild(match(TokenType.IDENTIFIER));
            }
            match(TokenType.RPAREN);
            return retNode;
        }

        throw new OQLSyntaxException("Expected undefinedExpr and didn't find it "
                + "at or near: " + _curToken.getTokenValue());
    }

    /**
     * Consumes tokens of queryParam. 
     *
     * @return a Parse tree containing DOLLAR as the root, and either one child
     *         which is a LONG_LITERAL, or one child which is an IDENTIFIER, and one 
     *         child which is a LONG_LITERAL.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match(), or if an unknown
     *         token is encountered here.
     */
    private ParseTreeNode queryParam() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = match(TokenType.DOLLAR);

        int tokenType = _curToken.getTokenType();
        switch (tokenType) {
        case TokenType.LPAREN:
            match(TokenType.LPAREN);
            retNode.addChild(match(TokenType.IDENTIFIER));
            match(TokenType.RPAREN);
            retNode.addChild(match(TokenType.LONG_LITERAL));
            break;
        case TokenType.LONG_LITERAL:
            retNode.addChild(match(TokenType.LONG_LITERAL));
            break;
        default:
            throw new OQLSyntaxException("An inapropriate token was encountered in "
                    + "a query parameter.");
        }
        
        return retNode;
    }

    /**
     * Consumes tokens of orderClause. 
     *
     * @return a Parse tree containing ORDER as the root, with children 
     *         as order parameters.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match(), or if an 
     *         unknown token is encountered here.
     */
    private ParseTreeNode orderClause() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = match(TokenType.KEYWORD_ORDER);
        match(TokenType.KEYWORD_BY);

        ParseTreeNode curExpression = null;
        ParseTreeNode curOrder = null;

        curExpression = expr();

        int tokenType = _curToken.getTokenType();
        if ((tokenType == TokenType.KEYWORD_ASC)
                || (tokenType == TokenType.KEYWORD_DESC)) {
            
            curOrder = match(tokenType);
            curOrder.addChild(curExpression);
            retNode.addChild(curOrder);
        } else {
            retNode.addChild(curExpression);
        }

        while (_curToken.getTokenType() == TokenType.COMMA) {
            match(TokenType.COMMA);
            
            curExpression = expr();

            tokenType = _curToken.getTokenType();
            if ((tokenType == TokenType.KEYWORD_ASC)
                    || (tokenType == TokenType.KEYWORD_DESC)) {
                
                curOrder = match(tokenType);
                curOrder.addChild(curExpression);
                retNode.addChild(curOrder);
            } else {
                retNode.addChild(curExpression);
            }
        }
        
        return retNode;
    }

    /**
     * Consumes tokens of limitClause.
     *
     * @return a Parse tree containing LIMIT as the root, with children
     *         as limit parameters.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match(), or if an
     *         unknown token is encountered here.
     */
    private ParseTreeNode limitClause() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = match(TokenType.KEYWORD_LIMIT);
        retNode.addChild(queryParam());
        return retNode;
    }

    /**
     * Consumes tokens of limitClause.
     *
     * @return a Parse tree containing LIMIT as the root, with children
     *         as limit parameters.
     * @throws InvalidCharException passed through from match().
     * @throws OQLSyntaxException passed through from match(), or if an
     *         unknown token is encountered here.
     */
    private ParseTreeNode offsetClause() throws InvalidCharException, OQLSyntaxException {
        ParseTreeNode retNode = match(TokenType.KEYWORD_OFFSET);
        retNode.addChild(queryParam());
        return retNode;
    }
}
