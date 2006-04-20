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
 * A Token class for the XPath 1.0 lexer
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 */
public class Token {
        
    //public static final short ERROR          = -1;
    public static final short NULL           = 0;
    public static final short L_PAREN        = 1;
    public static final short R_PAREN        = 2;
    public static final short L_BRACKET      = 3;
    public static final short R_BRACKET      = 4;
    public static final short PERIOD         = 5;
    public static final short COMMA          = 6;        
    public static final short AT_SYMBOL      = 7;
    
    //-- WildcardName
    public static final short WILDCARD       = 101;
    public static final short CNAME          = 102;
        
    //-- NodeType
    public static final short NODE           = 201;
    public static final short COMMENT        = 202;
    public static final short TEXT           = 203;
    public static final short PI             = 204;
    

    //-- Operators
    public static final short AND_OPNAME       = 301; // 'and'
    public static final short  OR_OPNAME       = 302; // 'or'
    public static final short MOD_OPNAME       = 303; // 'mod'
    public static final short DIV_OPNAME       = 304; // 'div'
    public static final short QUO_OPNAME       = 305; // 'quo'

    public static final short PARENT_OP        = 306; // '/'
    public static final short ANCESTOR_OP      = 307; // '//'
    public static final short UNION_OP         = 308;
    public static final short ADDITION_OP      = 309;
    public static final short SUBTRACTION_OP   = 310;
    public static final short MULTIPLY_OP      = 311;
    public static final short EQUALS_OP        = 312;
    public static final short NOT_EQUALS_OP    = 313;
    public static final short LESS_THAN_OP     = 314;
    public static final short GREATER_THAN_OP  = 315;
    public static final short LESS_OR_EQ_OP    = 316;
    public static final short GREATER_OR_EQ_OP = 317;
        
    public static final short NUMBER        = 401;
    public static final short FUNCTION_NAME = 501;
        
    //-- Axis Identifiers
    public static final short ANCESTORS_AXIS          = 601;
    public static final short ANCESTORS_OR_SELF       = 602;
    public static final short ATTRIBUTES_AXIS         = 603;
    public static final short CHILDREN_AXIS           = 604;
    public static final short DESCENDANTS_AXIS        = 605;
    public static final short DESCENDANTS_OR_SELF     = 606;
    public static final short FOLLOWING_AXIS          = 607;
    public static final short FOLLOWING_SIBLINGS_AXIS = 608;
    public static final short PARENT_AXIS             = 609;
    public static final short PRECEDING_AXIS          = 610;
    public static final short PRECEDING_SIBLINGS_AXIS = 611;
    public static final short SELF_AXIS               = 612;
    public static final short NAMESPACE_AXIS          = 613;
        
    //-- String literals
    public static final short LITERAL       = 701;
        
    public static final short VAR_REFERENCE    = 801;
    public static final short SELF_NODE        = 802;
    public static final short PARENT_NODE      = 803;
    public static final short NAMESPACE_PREFIX = 804;
    
        
    public String value = null;
    public short  type  = -1;
        
    
    protected Token() {};
    
    protected Token (String text, short ttype) {
        this.value = text;
        this.type  = ttype;
    }
    
    
    public String toString() { 
        
        switch(type) {
            case L_PAREN:
                return "(";
            case R_PAREN:
                return ")";
            case L_BRACKET:
                return "[";
            case R_BRACKET:
                return "]";
            case COMMA:
                return ",";
            case PERIOD:
            case SELF_NODE:
                return ".";
            case PARENT_NODE:
                return "..";
            case WILDCARD:
                return "*";
            //-- operators
            case AND_OPNAME:
                return "and";
            case OR_OPNAME:
                return "or";
            case MOD_OPNAME:
                return "mod";
            case DIV_OPNAME:
                return "div";
            case QUO_OPNAME:
                return "quo";
            case MULTIPLY_OP:
                return "*";
            case ADDITION_OP:
                return "+";
            case SUBTRACTION_OP:
                return "-";
            case PARENT_OP:
                return "/";
            case ANCESTOR_OP:
                return "//";
            case EQUALS_OP:
                return "=";
            case LESS_THAN_OP:
                return "<";
            case GREATER_THAN_OP:
                return ">";
            case LESS_OR_EQ_OP:
                return "<=";
            case GREATER_OR_EQ_OP:
                return ">=";
            case UNION_OP:
                return "|";
            //-- var ref
            case VAR_REFERENCE:
                return "$"+this.value;
            case LITERAL:
                StringBuffer sb = new StringBuffer("'");
                sb.append(this.value);
                sb.append("'");
                return sb.toString();
            default:
                break;
        }
        
        return this.value; 
    }
} //-- Token
