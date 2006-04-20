/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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


import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.expressions.EqualityExpr;
import org.exolab.adaptx.xpath.expressions.FunctionCall;
import org.exolab.adaptx.xpath.expressions.LocationStep;
import org.exolab.adaptx.xpath.expressions.MatchExpression;
import org.exolab.adaptx.xpath.expressions.NodeExpression;
import org.exolab.adaptx.xpath.expressions.PathExpr;
import org.exolab.adaptx.xpath.expressions.PrimaryExpr;
import org.exolab.adaptx.xpath.expressions.UnionExpr;
import org.exolab.adaptx.xpath.functions.*;
import java.util.Stack;

/**
 * A class for parsing expression strings
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Parser {

    private static final String PATTERN_SEP         = "|";

    private static final WildCardExpr wildCardExpr = new WildCardExpr();
        
      //------------------/
     //- Public Methods -/
    //------------------/

    /**
     * Creates an Expr from the given String
     * @param exprString the String to create the Expr from
     * @return the new Expr
     * @exception InvalidExprException when a parsing error occurs
    **/
    public static XPathExpression createExpr(String exprString)
        throws XPathException
    {
        Lexer lexer = null;
        
        try {
            lexer = new Lexer(exprString);
        }
        catch(ParseException px) {
            return new ErrorExpr(px.getMessage());
        }
        return createExpr(lexer);
        
    } //-- createExpr

    /**
     * Creates a literal expression, which simply evaluates
     * to the given string literal.
     *
     * @param literal the string literal the literal expression
     * should evaluate to.
     * @return an XPathExpression which will evaluate to the given
     * string literal.
    **/
    public static XPathExpression createLiteralExpr(String literal) {
        return new LiteralExpr(literal);
    } //-- createLiteralExpr
    
    /**
     * Creates an Expression using the given ExprLexer
     * @param lexer the ExprLexer to get Tokens from
     * @return the new Expr
    **/
    private static XPathExpression createExpr(Lexer lexer)
        throws XPathException
    {
        if (!lexer.hasMoreTokens()) missingExpr(lexer.toString(), null);

        XPathExpression  expr  = null;
        Stack  exprs      = new Stack();
        Stack  ops        = new Stack();
        
        boolean cFlag = true;
        
        while(lexer.hasMoreTokens() && cFlag) {

            Token tok = lexer.nextToken();

            //System.out.println("tok: " + tok);

            if (lexer.isBinaryOp(tok)) {
                if (!exprs.empty()) {
                    if (!hasGreaterPrecedence(tok, (Token)ops.peek())) {
                        expr = createBinaryExpr((XPathExpression)exprs.pop(), expr, 
                            (Token)ops.pop());
                    }
                }
                exprs.push(expr);
                ops.push(tok);
                //-- clear expr
                expr = null;
                continue;
            }
            switch (tok.type) {
                case Token.R_PAREN:
                case Token.R_BRACKET:
                case Token.COMMA:
                    lexer.pushBack();
                    cFlag = false;
                    break;
                case Token.LITERAL:
                    expr = new LiteralExpr(tok.value);
                    break;
                case Token.UNION_OP:

                    if (expr == null) {
                        String err = "'|' cannot start an expr";
                        throw new XPathException(err);
                    }
                    else if (expr instanceof PathExprImpl ) {
                        UnionExprImpl unionExpr = new UnionExprImpl((PathExprImpl)expr);
                        unionExpr.setUnionExpr((UnionExprImpl)createUnionExpr(lexer));
                        expr = unionExpr;
                    }
                    else {
                        throw new XPathException("#parse error: "+
                            lexer.toString());
                    }
                    break;
                case Token.ANCESTOR_OP:
                case Token.PARENT_OP:

                    if (expr == null) {
                        //-- fixed for absolute PathExpr
                        //-- submitted by Rolande Kendal
                        lexer.pushBack();
                        expr = createPathExpr(lexer);
                    }
                    else if (expr.getExprType() == XPathExpression.PRIMARY) {
                        
                        PrimaryExpr px = (PrimaryExpr) expr;
                        switch(px.getType()) {
                            case PrimaryExpr.VARIABLE_REFERENCE:
                            case PrimaryExpr.FUNCTION_CALL:
                                FilterExpr fx = new FilterExpr( px );
                                lexer.pushBack();
                                expr = new PathExprImpl(fx, createPathExpr(lexer));
                                break;
                            default:
                                String err = "Primary expr used as part of "+
                                    "a path expression.";
                                throw new XPathException(err);
                        }
                    }
                    else if (expr instanceof PathExpr) {
                        lexer.pushBack();
                        ((PathExprImpl)expr).setSubPath(createPathExpr(lexer));
                    }
                    else {
                        String err = "Invalid path expression: ";
                        err += lexer.toString();
                        throw new XPathException(err);
                    }
                    break;
                default:

                    lexer.pushBack();
                    //-- try for PathExpr
                    if (isLocationStepToken(tok)) {
                        
                        if (expr == null) {
                            expr = createPathExpr(lexer);
                        }
                        else if (expr instanceof PathExpr) {
                            ((PathExprImpl)expr).setSubPath(createPathExpr(lexer));
                        }
                        else {
                            throw new XPathException("invalid expr: "+
                            lexer.toString());
                        }
                        break;
                    }
                    
                    PrimaryExpr primaryExpr = createPrimaryExpr(lexer);
                    //-- try FilterExpr
                    //-- look ahead for predicate list
                    Token nextTok = lexer.lookAhead(0);
                    if ((nextTok != null) &&
                        (nextTok.type == Token.L_BRACKET)) {
                        PathExpr pathExpr = null;
                        FilterExpr filter = new FilterExpr( primaryExpr );
                        parsePredicates(filter, lexer);
                        pathExpr = new PathExprImpl(filter);
                        expr = pathExpr;
                    }
                    else expr = primaryExpr;
                    break;
            } //-- end switch
        } //-- end while more tokens
        
        //-- finish Binary expressions
        while (!exprs.empty()) {
            expr = createBinaryExpr(
                (XPathExpression)exprs.pop(), expr,(Token)ops.pop());
        }

        return expr;

    } //-- createExpr

    /**
     * @returns true if op1 has greater precedence than op2
    **/
    private static boolean hasGreaterPrecedence(Token op1, Token op2) {
        
        if (Lexer.isMultiplicativeOp(op1)) {
            if (Lexer.isMultiplicativeOp(op2)) return false;
            return true;
        }
        else if (Lexer.isAdditiveOp(op1)) {
            if (Lexer.isAdditiveOp(op2)) return false;
            return !Lexer.isMultiplicativeOp(op2);
        }
        else if (Lexer.isRelationalOp(op1)) {
            if (Lexer.isRelationalOp(op2)) return false;
            else if (Lexer.isAdditiveOp(op2)) return false;
            return !Lexer.isMultiplicativeOp(op2);
        }
        else if (Lexer.isEqualityOp(op1)) {
            if (Lexer.isEqualityOp(op2)) return false;
            else if (Lexer.isRelationalOp(op2)) return false;
            else if (Lexer.isAdditiveOp(op2)) return false;
            else return !Lexer.isMultiplicativeOp(op2);
        }
        else if (op1.type == Token.AND_OPNAME) {
            if (op2.type == Token.AND_OPNAME) return false;
            return (op2.type == Token.OR_OPNAME);
        }
        if (op2.type == Token.OR_OPNAME) return false;
        return true;
    } //-- hasGreaterPrecedence
    
    /**
     * Creates a new BinaryExpr
    **/
    private static XPathExpression createBinaryExpr(XPathExpression left, XPathExpression right, Token op)
        throws XPathException
    {
        if (left == null)
            throw new XPathException("Missing left side of expression: ");
        if (right == null)
            throw new XPathException("Missing right side of expression: ");

        switch (op.type) {
            case Token.OR_OPNAME:
                return new OrExpr(left, right);
            case Token.AND_OPNAME:
                return new AndExpr(left, right);
            case Token.NOT_EQUALS_OP:
                return new EqualityExprImpl(left, right, EqualityExpr.NOT_EQUAL);
            case Token.EQUALS_OP:
                return new EqualityExprImpl(left, right, EqualityExpr.EQUAL);
            case Token.LESS_THAN_OP:
                return new EqualityExprImpl(left, right, EqualityExpr.LESS_THAN);
            case Token.GREATER_THAN_OP:
                return new EqualityExprImpl(left, right, EqualityExpr.GREATER_THAN);
            case Token.LESS_OR_EQ_OP:
                return new EqualityExprImpl(left, right, EqualityExpr.LT_OR_EQUAL);
            case Token.GREATER_OR_EQ_OP:
                return new EqualityExprImpl(left, right, EqualityExpr.GT_OR_EQUAL);
            case Token.ADDITION_OP:
                return new AdditionExpr(left, right);
            case Token.SUBTRACTION_OP:
                return new SubtractionExpr(left, right);
            case Token.MULTIPLY_OP:
                return new MultiplicativeExpr(left, right,
                    MultiplicativeExpr.MULTIPLY);
            case Token.MOD_OPNAME:
                return new MultiplicativeExpr(left, right,
                    MultiplicativeExpr.MODULUS);
            case Token.DIV_OPNAME:
                return new MultiplicativeExpr(left, right,
                    MultiplicativeExpr.DIVIDE);
            case Token.QUO_OPNAME:
                return new MultiplicativeExpr(left, right,
                    MultiplicativeExpr.QUOTIENT);
            default:
                break;
        }

        throw new XPathException
            ("Invalid binary expr: " + left + op + right);
    } //-- createBinaryExpr

    /**
     * Creates a PrimaryExpr using the given String
     * @param exprString the String to create the PrimaryExpr from
     * @return the new PrimaryExpr
     * @exception XSLException
    **/
    private static PrimaryExpr createPrimaryExpr(Lexer lexer)
        throws XPathException
    {
        PrimaryExpr pExpr  = null;

        if (!lexer.hasMoreTokens())
            missingExpr(lexer.toString(), null);

        Token tok = lexer.nextToken();
        

        //-- Grouped Expression '(' Expr ')'
        switch (tok.type) {

            case Token.L_PAREN:
                pExpr = new InnerExpr(createExpr(lexer));
                tok = lexer.nextToken();
                if (tok == null) {
                    //-- missing closing parenthesis ')'
                    String err = "missing closing parenthesis \')\' !";
                    String exprString = lexer.toString();
                    throw new ParseException(exprString, err, exprString.length()); 
                }
                if (tok.type != Token.R_PAREN) {
                    unexpectedToken(lexer.toStringPrevious(), tok);
                }
                break;
            case Token.VAR_REFERENCE:
                pExpr = new VariableReference(tok.value);
                break;
            case Token.LITERAL:
                pExpr = new LiteralExpr(tok.value);
                break;
            case Token.FUNCTION_NAME:
                String name = tok.value;
                FunctionCallImpl fnCall = (FunctionCallImpl)createFunctionCall(name);
                parseParams(fnCall, lexer);
                pExpr = fnCall;
                break;
            case Token.NUMBER:
                try {
                    Double dbl = Double.valueOf(tok.value);
                    pExpr = new NumberExpr(dbl.doubleValue());
                }
                catch (NumberFormatException nfe) {
                    pExpr = new LiteralExpr(tok.value);
                }
                break;
        }
        if (pExpr == null)
            throw new XPathException("Invalid PrimaryExpr: " +
                lexer.toStringPrevious()+ " ->{"+tok.toString()+"}");

        return pExpr;

    } //-- createPrimaryExpr


    /**
     * Creates a FilterExpr from the given string.
     * @param tokenizer the set of tokens to create the FilterExpr from
     * @param tokenStack the current Stack of group tokens
     * @returns the new FilterExpr
     * @exception InvalidExprException
    **/
    private static FilterExpr createFilterExpr( Lexer lexer, int ancestryOp )
        throws XPathException
    {
        PrimaryExpr primaryExpr = createPrimaryExpr(lexer);
        if ( primaryExpr == null )
            throw new XPathException( lexer.toString() );
        FilterExpr filterExpr = new FilterExpr( primaryExpr, ancestryOp );
        parsePredicates( filterExpr, lexer );
        return filterExpr;
    } //-- createFilterExpr


    /**
     * Creates a LocationStep from the given string.
     * @param pattern the String to create the LocationStep from
     * @returns the new LocationStep
     * @exception InvalidExprException
    **/
    private static boolean isLocationStepToken( Token token ) {

        if (token == null) return false;
        if (Lexer.isAxisIdentifier(token)) return true;
        return isNodeTypeToken(token);

    } //-- isLocationStep;

/*
    private static boolean isAxisIdentifierToken(Token token) {
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
            case Token.PARENT_AXIS:
            case Token.PRECEDING_AXIS:
            case Token.PRECEDING_SIBLINGS_AXIS:
            case Token.SELF_AXIS:
            case Token.NAMESPACE_AXIS:
                return true;
            default:
                return false;
        }

    } //--  isAxisIdentifierToken
*/

    private static boolean isNodeTypeToken(Token token) {
        if (token == null) return false;
        switch(token.type) {
            case Token.TEXT:
            case Token.COMMENT:
            case Token.PI:
            case Token.NODE:
            case Token.AT_SYMBOL:
            case Token.SELF_NODE:
            case Token.PARENT_NODE:
            case Token.WILDCARD:
            case Token.CNAME:
            case Token.NAMESPACE_PREFIX:
                return true;
            default:
                return false;
        }
    } //-- isNodeTypeToken

    /**
     * Creates a LocationStep from the given string.
     * @param tokenizer the set of tokens to create the LocationStep from
     * @param tokenStack the current Stack of group tokens
     * @returns the new LocationStep
     * @exception InvalidExprException
    **/
    private static LocationStepImpl createLocationStep( Lexer lexer, int ancestryOp ) 
        throws XPathException
    {
        LocationStepImpl locationStep = new LocationStepImpl( ancestryOp );
        Token cTok = lexer.lookAhead(0);
        
        short axisIdentifier = LocationStep.CHILDREN_AXIS;

        int offset = 1;
        switch (cTok.type) {
            case Token.ANCESTORS_AXIS:
                axisIdentifier = LocationStep.ANCESTORS_AXIS;
                break;
            case Token.ANCESTORS_OR_SELF:
                axisIdentifier = LocationStep.ANCESTORS_OR_SELF_AXIS;
                break;
            case Token.ATTRIBUTES_AXIS:
                axisIdentifier = LocationStep.ATTRIBUTES_AXIS;
                break;
            case Token.CHILDREN_AXIS:
                axisIdentifier = LocationStep.CHILDREN_AXIS;
                break;
            case Token.DESCENDANTS_AXIS:
                axisIdentifier = LocationStep.DESCENDANTS_AXIS;
                break;
            case Token.DESCENDANTS_OR_SELF:
                axisIdentifier = LocationStep.DESCENDANTS_OR_SELF_AXIS;
                break;
            case Token.FOLLOWING_AXIS:
                axisIdentifier = LocationStep.FOLLOWING_AXIS;
                break;
            case Token.FOLLOWING_SIBLINGS_AXIS:
                axisIdentifier = LocationStep.FOLLOWING_SIBLINGS_AXIS;
                break;
            case Token.PARENT_AXIS:
                axisIdentifier = LocationStep.PARENT_AXIS;
                break;
            case Token.PRECEDING_AXIS:
                axisIdentifier = LocationStep.PRECEDING_AXIS;
                break;
            case Token.PRECEDING_SIBLINGS_AXIS:
                axisIdentifier = LocationStep.PRECEDING_SIBLINGS_AXIS;
                break;
            case Token.SELF_AXIS:
                axisIdentifier = LocationStep.SELF_AXIS;
                break;
            case Token.NAMESPACE_AXIS:
                axisIdentifier = LocationStep.NAMESPACE_AXIS;
                break;
            default:
                offset = 0;
        }
        //if (offset == 1) {
        //    if (lexer.lookAhead(1).type == Token.L_PAREN) offset = 2;
       //}

        for (int i = 0; i < offset; i++) lexer.nextToken();

        NodeExpression nodeExpr = createNodeExpr(lexer, axisIdentifier);
        if (nodeExpr == null) {
            String err = "error creating node expression from: ";
            throw new XPathException(err+lexer.toString());
        }
        locationStep.setNodeExpr(nodeExpr);
        parsePredicates(locationStep, lexer);
        
        //if (offset > 0) {
        //    cTok = lexer.nextToken();
        //    if (cTok.type != Token.R_PAREN)
        //        throw new InvalidExprException("missing closing parenthesis ')'");
        //}
        
        //-- if no axis identifier was specified...
        if (offset == 0) {
            //-- adjust axis identifer if necessary
            switch (nodeExpr.getNodeExprType()) {
                case NodeExpression.IDENTITY_EXPR:
                    axisIdentifier = LocationStep.SELF_AXIS;
                    break;
                case NodeExpression.PARENT_EXPR:
                    axisIdentifier = LocationStep.PARENT_AXIS;
                    break;
                case NodeExpression.ATTRIBUTE_EXPR:
                    axisIdentifier = LocationStep.ATTRIBUTES_AXIS;
                    break;
                case NodeExpression.NAMESPACE_EXPR:
                    axisIdentifier = LocationStep.NAMESPACE_AXIS;
                    break;
                default:
                    break;

            }
        }
        locationStep.setAxisIdentifier(axisIdentifier);

        return locationStep;

    } //-- createLocationStep

    /**
     * Parses the set of predicates for the given FilterBase
     * @param filterBase the FilterBase to add Predicates to
     * @param tokenizer the set of tokens to create the Predicates from
     * @param tokenStack the current Stack of group tokens
     * @exception InvalidExprException
    **/
    private static void parsePredicates
        (FilterBase filterBase, Lexer lexer)
            throws XPathException
    {
        //-- handle predicates

        while ( lexer.hasMoreTokens() &&
                (lexer.lookAhead(0).type == Token.L_BRACKET) ) {

            lexer.nextToken(); //-- remove '['
            XPathExpression expr = createExpr(lexer);
            filterBase.addPredicate(expr);
            //-- make sure we have end of predicate ']'
            Token tok = lexer.nextToken();
            if ((tok == null) || (tok.type != Token.R_BRACKET))
                unexpectedToken(lexer.toString(), tok);
        }

    } //-- parsePredicates

    /**
     * Creates the appropriate FunctionCall based on the given name
     * @param name the name of the function to call
     * @param params the List of Expr paramaters for the function call
     * @return the new FunctionCall
    **/
    public static FunctionCall createFunctionCall(String name)
            throws XPathException
    {
        FunctionCall fnCall = null;

        name = name.intern();

        // BooleanFunctionCall
        if (name == Names.TRUE_FN)
            return new TrueFunctionCall();
        else if (name == Names.FALSE_FN)
            return new FalseFunctionCall();
        else if (name == Names.NOT_FN)
            fnCall = new NotFunctionCall();
        else if (name == Names.BOOLEAN_FN)
            fnCall = new BooleanFunctionCall();
        else if (name == Names.LANG_FN)
            fnCall = new LangFunctionCall();

        // NodeSetFunctionCall

        else if (name == Names.POSITION_FN)
            return new PositionFunctionCall();
        else if (name == Names.LAST_FN)
            return new LastFunctionCall();
        else if (name == Names.COUNT_FN)
            fnCall = new CountFunctionCall();
        else if (name == Names.LOCAL_NAME_FN)
            fnCall = new XMLNamesFunctionCall(XMLNamesFunctionCall.LOCAL_PART);
        else if (name == Names.NAMESPACE_FN) 
            fnCall = new XMLNamesFunctionCall(XMLNamesFunctionCall.NAMESPACE);
        else if (name == Names.NAME_FN)
            fnCall = new XMLNamesFunctionCall();
        else if (name == Names.ID_FN)
            fnCall = new IdFunctionCall();
        else if (name == Names.IDREF_FN)
            fnCall = new IdRefFunctionCall();

        // NodeTest

        else if (name == Names.TEXT_FN)
            return new TextFunctionCall();

        // String Functions
        else if (name == Names.CONCAT_FN) {
            fnCall = new Concat();
        }
        else if (name == Names.CONTAINS_FN) {
            fnCall = new Contains();
        }
        else if (name == Names.FORMAT_NUMBER_FN) {
            fnCall = new FormatNumber();
        }
        else if (name == Names.NORMALIZE_FN) {
            fnCall = new Normalize();
        }
        else if (name == Names.STARTS_WITH_FN) {
            fnCall = new StartsWith();
        }
        else if (name == Names.STRING_FN) {
            fnCall = new StringFunctionCall();
        }
        // -- string-length() new for XSLT WD 19990709 
        else if (name == Names.STRING_LENGTH_FN) {
            fnCall = new StringLength();
        }
        // -- substring new for XSLT WD 19990709 
        else if (name == Names.SUBSTRING_FN) {
            fnCall = new Substring();
        }
        else if (name == Names.SUBSTRING_BEFORE_FN) {
            fnCall = new SubstringBefore();
        }
        else if (name == Names.SUBSTRING_AFTER_FN) {
            fnCall = new SubstringAfter();
        }
        else if (name == Names.TRANSLATE_FN) {
            fnCall = new Translate();
        }

        //-- NumberFunctionCalls
        else if (name == Names.SUM_FN) {
            fnCall = new SumFunctionCall();
        }
        else if (name == Names.NUMBER_FN) {
            fnCall = new NumberFunctionCall();
        }
        else if (name == Names.FLOOR_FN) {
            fnCall = new NumberFunctionCall(NumberFunctionCall.FLOOR);
        }
        else if (name == Names.CEILING_FN) {
            fnCall = new NumberFunctionCall(NumberFunctionCall.CEILING);
        }
        else if (name == Names.ROUND_FN) {
            fnCall = new NumberFunctionCall(NumberFunctionCall.ROUND);
        }


        if (fnCall == null) {
            fnCall = new ExtensionFunctionCall(name);
        }
        return fnCall;

    } //-- createFunctionCall

    /**
     * Parses the a pattern String into a MatchExpr
     * @param matchString the pattern string to create the MatchExpr from
     * @return the new MatchExpr
     * @exception InvalidExprException
    **/
    public static MatchExpression createMatchExpr(String matchString)
        throws XPathException
    {
        MatchExpression matchExpr = createUnionExpr(matchString);
        return matchExpr;
    } //-- createMatchExpr

    /**
     * creates a NodeExpr from the given string argument
     * @param pattern the string to create the RefrencePattern from
     * @return the NodeExpr or null if the string was not a
     *  valid NodeExpr
     * @exception InvalidExprException
    **/
    private static NodeExpression createNodeExpr
        (Lexer lexer, short axisIdentifier) throws XPathException
    {
        NodeExpression nodeExpr = null;

        Token tok = lexer.nextToken();
        
        if (tok == null) return null;

        if (lexer.hasMoreTokens() &&
            (lexer.lookAhead(0).type == Token.L_PAREN)) {

            MatchExpression matchExpr = null;
            lexer.nextToken();

            //-- read param
            Token pTok = lexer.nextToken();
            if (pTok == null)
                unexpectedToken(lexer.toString(), null);

            String param = null;
            while (pTok.type != Token.R_PAREN) {

                if (param == null) param = pTok.value;
                else param += pTok.value;

                pTok = lexer.nextToken();
                if (pTok == null)
                    unexpectedToken(lexer.toString(), null);
            }

            switch (tok.type) {

                case Token.TEXT:
                    nodeExpr = new TextExpression();
                    break;
                case Token.COMMENT:
                    nodeExpr = new CommentExpr();
                    break;
                case Token.PI:
                    nodeExpr = new PIExpr(param);
                    break;
                case Token.NODE:
                    nodeExpr = new AnyNodeExpr();
                    break;
                default:
                    throw new XPathException("Not a valid NodeExpr token: "
                        + tok.value);
            }
        }
        else {
            switch (tok.type) {
                case Token.NAMESPACE_PREFIX:
                {
                    Token nTok = lexer.nextToken();
                    switch (nTok.type) {
                        case Token.CNAME:
                        case Token.WILDCARD:
                            break;
                        default:
                            unexpectedToken(lexer.toString(), nTok);
                            break;
                    }
                    String prefix = tok.value; //-- add support later
                    nodeExpr = new ElementExpr(nTok.value);
                    break;
                }
                case Token.AT_SYMBOL:
                {
                    Token nTok = lexer.nextToken();
                    switch (nTok.type) {
                        case Token.CNAME:
                        case Token.WILDCARD:
                            break;
                        default:
                            unexpectedToken(lexer.toString(), nTok);
                            break;
                    }
                    nodeExpr = new AttributeExpr(nTok.value);
                    break;
                }
                case Token.ATTRIBUTES_AXIS:
                    nodeExpr = new AttributeExpr(tok.value);
                    break;
                case Token.NAMESPACE_AXIS:
                    nodeExpr = new NamespaceExpr(tok.value);
                    break;
                case Token.SELF_NODE:
                    nodeExpr = new IdentityExpr();
                    break;
                case Token.PARENT_NODE:
                    nodeExpr = new ParentExpr();
                    break;
                case Token.WILDCARD:
                    if (axisIdentifier == LocationStep.ATTRIBUTES_AXIS)
                        nodeExpr = new AttributeExpr(tok.value);
                    else if (axisIdentifier == LocationStep.NAMESPACE_AXIS)
                        nodeExpr = new NamespaceExpr(tok.value);
                    else
                        nodeExpr = wildCardExpr;
                    break;
                case Token.NUMBER:
                    throw new XPathException("Primary expr out of place");
                default:
                    if (axisIdentifier == LocationStep.ATTRIBUTES_AXIS)
                        nodeExpr = new AttributeExpr(tok.value);
                    else if (axisIdentifier == LocationStep.NAMESPACE_AXIS)
                        nodeExpr = new NamespaceExpr(tok.value);
                    else
                        nodeExpr = new ElementExpr(tok.value);
                    break;
            }
        }

        return nodeExpr;

    } // -- createNodeExpr


    /**
     * Creates a PathExpr from the string argument.
     * @param pattern the string to create the PathExpr from
     * @return the new PathExpr
     * @exception InvalidExprException
    **/
    public static PathExpr createPathExpr(String pattern) 
        throws XPathException
    {
        Lexer lexer = null;
        
        try {
            lexer = new Lexer(pattern);
        }
        catch(ParseException px) {
            return new PathExprImpl(new ErrorExpr(px.getMessage()));
        }
        
        return createPathExpr(lexer);
    } //-- createPathExpr;
    
    /**
     * Creates a PathExpr from the string argument.
     * @param lexer the ExprLexer to create the PathExpr from
     * @return the new PathExpr
     * @exception InvalidExprException
    **/
    private static PathExprImpl createPathExpr(Lexer lexer) 
        throws XPathException 
    {
        PathExprImpl pathExpr = new PathExprImpl();
        
        PathExprImpl tempExpr = null;
        //-- look for empty PathExpr
        if (!lexer.hasMoreTokens()) return pathExpr;
        
        int ancestryOp = FilterBase.NO_OP;
        
        Token tok = lexer.lookAhead(0);
        
        //-- look for ancestry operator, or RootExpr
        if (tok.type == Token.PARENT_OP) {
            //-- eat token
            lexer.nextToken();
            if (!lexer.hasMoreTokens()) return new RootExpr();
            ancestryOp = FilterBase.PARENT_OP;
        }
        else if (tok.type == Token.ANCESTOR_OP) {
            ancestryOp = FilterBase.PARENT_OP;
            //-- eat token
            lexer.nextToken();
            tempExpr = createDescendantOrSelf();
        }
        
        
        if (!lexer.hasMoreTokens()) {
            if (tempExpr != null) return tempExpr;
            else return pathExpr;
        }
        
        tok = lexer.lookAhead(0);
        FilterBase filterBase = null;
        //-- try to create a LocationStep
        if (isLocationStepToken(tok)) {
            //filterBase = createLocationStep(lexer, LocationStep.NO_OP);
            filterBase = createLocationStep(lexer, ancestryOp);
        }
        else 
            filterBase = createFilterExpr( lexer, ancestryOp );
                    
        //-- add filterbase to pathExpr
        pathExpr.setFilter(filterBase);
        
        if (lexer.hasMoreTokens()) {
            tok = lexer.lookAhead(0);
            if (!lexer.isBinaryOp(tok)) {
                switch (tok.type) {
                    case Token.COMMA:
                    case Token.R_PAREN:
                    case Token.R_BRACKET:
                    case Token.UNION_OP:
                        break;
                    default:
                        pathExpr.setSubPath(createPathExpr(lexer));
                        break;
                }
            }
        
        }
        
        if (tempExpr != null) {
            tempExpr.setSubPath(pathExpr);
            pathExpr = tempExpr;
        }
        return pathExpr;
    } // -- createPathExpr
    
    /**
     * Parses the a pattern String into a SelectExpr
     * @param selectString the pattern string to create the SelectExpr from
     * @return the new SelectExpr
     * @exception XSLException
    **/
    public static SelectExpr createSelectExpr(String selectString) 
        throws XPathException
    {
        SelectExpr selectExpr = new SelectExpr();
        selectExpr.setUnionExpr(createUnionExpr(selectString));
        return selectExpr;
    } //-- createSelectExpr

    /**
     * Creates a UnionExpr from the given string argument.
     * @param pattern the string to create the UnionExpr from
     * @return the new UnionExpr
     * @exception InvalidExprException
    **/
    public static UnionExpr createUnionExpr(String pattern) 
        throws XPathException
    {
        Lexer lexer = null;
        
        try {
            lexer = new Lexer(pattern);
        }
        catch(ParseException px) {
            return new UnionExprImpl(new ErrorExpr(px.getMessage()));
        }
        return createUnionExpr(lexer);
    } //-- createUnionExpr
    
    /**
     * Creates a new Path Expression which selects the current node
     * and all descendants. This method is used for expanding the
     * ancestor operator "//".
     *
     * @return the new descendant-or-self PathExpr.
    **/
    private static PathExprImpl createDescendantOrSelf() {
        LocationStepImpl locationStep = new LocationStepImpl(FilterBase.PARENT_OP);
        locationStep.setAxisIdentifier(LocationStep.DESCENDANTS_OR_SELF_AXIS);
        locationStep.setNodeExpr(new AnyNodeExpr());        
        PathExprImpl pathExpr = new PathExprImpl();
        pathExpr.setFilter(locationStep);
        return pathExpr;
    } //-- createDescendantOrSelf
    
    /**
     * Creates a UnionExpr from the given string argument.
     * @param lexer the ExprLexer to create the UnionExpr from
     * @return the new UnionExpr
     * @exception InvalidExprException
    **/
    private static UnionExprImpl createUnionExpr(Lexer lexer) 
        throws XPathException
    {
        UnionExprImpl unionExpr = new UnionExprImpl(createPathExpr(lexer));
        
        //-- look for '|'
        if (lexer.hasMoreTokens()) {
            Token tok = lexer.nextToken();
            if (tok.type == Token.UNION_OP) {
                unionExpr.setUnionExpr(createUnionExpr(lexer));
            }
            else {
                lexer.pushBack();
            }
        }
        return unionExpr;

    } //-- createUnionExpr
    
    
    
      //-------------------/
     //- Private Methods -/
    //-------------------/
    
    /**
     * Parses a list of parameters 
    **/
    private static void parseParams (FunctionCallImpl fnCall, Lexer lexer) 
        throws XPathException
    {
        Token tok = lexer.nextToken();
        
        //-- look for start of parameter list '('
        if (tok.type != Token.L_PAREN)
            missingExpr(lexer.toString(), tok);

        //-- read all parameters
        
        while (true) {
            if (!lexer.hasMoreTokens())
                missingExpr(lexer.toString(), tok);
                
            //-- look for ending of parameter list
            tok = lexer.lookAhead(0);
            if (tok.type == Token.R_PAREN) {
                lexer.nextToken();
                break;
            }
            //-- create parameter
            fnCall.addParameter(createExpr(lexer));
            
            tok = lexer.nextToken();
            if (tok != null) {
                if (tok.type == Token.R_PAREN) break;
                else if (tok.type != Token.COMMA)
                    unexpectedToken(lexer.toString(),tok);
            }
                    
        }
    } //-- parseParams
    

    private static final String INVALID_EXPR = "Invalid expression: ";


    /**
     * This method will throws and InvalidExprException. It is used
     * when an Non Unary Expr is missing an Expr component.
     * @param exprString the current expression String being parsed
     * @param op the binary operator
     * @exception InvalidExprException always
    **/
    private static void missingExpr(String exprString, Token token) 
        throws XPathException
    {
        StringBuffer error = new StringBuffer(INVALID_EXPR);
        error.append(exprString);
        error.append("; '");
        if (token != null) error.append(token.value);
        error.append("' -> missing remainder of expression.");
        throw new XPathException(error.toString());
    } //-- missingExpr

    /**
     * This method will throws and InvalidExprException. It is used
     * when an Expr operator appears in an unexpected place.
     * @param exprString the current expression String being parsed
     * @param op the binary operator
     * @exception InvalidExprException always
    **/
    private static void unexpectedToken(String exprString, Token token) 
        throws XPathException
    {
        StringBuffer error = new StringBuffer(INVALID_EXPR);
        error.append(exprString);
        error.append("; '");
        if (token != null) error.append(token.value);
        else error.append("NULL");
        error.append("' is unexpected.");
        throw new XPathException(error.toString());
    } //-- unexpectedToken
    
    
    /* For Debugging */
    /* */
    public static void main(String[] args) 
        throws XPathException
    {
        
        String[] UNION_EXPRS = {
            "*",
            "foo:*",
            "//pi()",
            "cellphone.e-catalog",
            "//elementA/elementB",
            "../elementA[@name=$name][@type='foo']",
            "elementA",
            "elementA | elementB",
            "element-A | element-B | element-C",
            "element-A[@attr and (not(position() = 1))]",
            "$my-variable/child",
            "/*",
            "node()",
            "a/div/b/and/c",
            "foo//bar"
        };

        String[] EXPRS = {
            "position() != last()",
            "position() mod 2 = 0",
            "(position() mod 2) = 0",
            "count((//@name)[1])",
            "4+5*3+(8-3)*6*2+3",
            "$my-variable/child",
            "@*[name(.)!='href']",
            "vin/text() = $process/order/orderRef/text()",
            "$booklist[author=$authorlist]/title",
            "$foo mod 4",
            "cname mod 4",
            "(9)*(7)"
        };
        
        //-- UnionExpr tests
        System.out.println("UnionExpr Tests\n");
        
        for (int i = 0; i < UNION_EXPRS.length; i++) {
            System.out.println("Test:   " + UNION_EXPRS[i]);
            System.out.print("Result: ");
            System.out.println(createUnionExpr(UNION_EXPRS[i]));
        }
        System.out.println();
        
        //-- Expr tests
        
        System.out.println("Expr Tests\n");
        for (int i = 0; i < EXPRS.length; i++) {
            System.out.println("Test:   " + EXPRS[i]);
            XPathExpression expr = createExpr(EXPRS[i]);
            System.out.print("Result: ");
            System.out.println(expr);
            System.out.println("Expr Type: " + expr.getExprType());
        }
       
        
    } //-- main
    /* */
    
} //-- ExpressionParser
