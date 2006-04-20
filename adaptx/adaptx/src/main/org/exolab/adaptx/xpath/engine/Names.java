/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
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


/**
 * XPath Names
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 */
public class Names {
    
      //------------------/
     //- function names -/
    //------------------/
    
    //-- Boolean Functions
    public static final String BOOLEAN_FN          = "boolean";
    public static final String FALSE_FN            = "false";
    public static final String LANG_FN             = "lang";
    public static final String NOT_FN              = "not";
    public static final String TRUE_FN             = "true";
    
    //-- Number Functions
    public static final String CEILING_FN          = "ceiling";
    public static final String FLOOR_FN            = "floor";
    public static final String NUMBER_FN           = "number";
    public static final String ROUND_FN            = "round";
    public static final String SUM_FN              = "sum";
    
    //-- String Functions
    public static final String CONCAT_FN           = "concat";
    public static final String CONTAINS_FN         = "contains";
    public static final String FORMAT_NUMBER_FN    = "format-number";
    public static final String NORMALIZE_FN        = "normalize-space";
    public static final String STARTS_WITH_FN      = "starts-with";
    public static final String STRING_FN           = "string";
    public static final String STRING_LENGTH_FN    = "string-length";
    public static final String SUBSTRING_FN        = "substring";
    public static final String SUBSTRING_AFTER_FN  = "substring-after";
    public static final String SUBSTRING_BEFORE_FN = "substring-before";
    public static final String TRANSLATE_FN        = "translate";
    
    //-- NodeSet Functions
    public static final String COUNT_FN            = "count";
    public static final String DOC_FN              = "doc";
    public static final String DOCREF_FN           = "docref";
    public static final String DOCUMENT_FN         = "document";
    public static final String GENERATE_ID_FN      = "generate-id";
    public static final String ID_FN               = "id";
    public static final String IDREF_FN            = "idref";
    public static final String KEY_FN              = "key";
    public static final String KEYREF_FN           = "keyref";
    public static final String LAST_FN             = "last";
    public static final String LOCAL_NAME_FN       = "local-name";
    public static final String NAMESPACE_FN        = "namespace-uri";
    public static final String POSITION_FN         = "position";
    public static final String NAME_FN             = "name";

    //-- Node Test Functions
    public static final String TEXT_FN             = "text";
    public static final String NODE_FN             = "node";
    public static final String PI_FN               = "pi";
    public static final String COMMENT_FN          = "comment";
    
    //-- Axis Identifiers
    public static final String ANCESTORS_AXIS           = "ancestor";
    public static final String ANCESTORS_OR_SELF_AXIS   = "ancestor-or-self";
    public static final String ATTRIBUTES_AXIS          = "attribute";
    public static final String CHILDREN_AXIS            = "child";
    public static final String DESCENDANTS_AXIS         = "descendant";
    public static final String DESCENDANTS_OR_SELF_AXIS = "descendant-or-self";
    public static final String FOLLOWING_AXIS           = "following";
    public static final String FOLLOWING_SIBLINGS_AXIS  = "following-sibling";
    public static final String PARENT_AXIS              = "parent";
    public static final String PRECEDING_AXIS           = "preceding";
    public static final String PRECEDING_SIBLINGS_AXIS  = "preceding-sibling";
    public static final String SELF_AXIS                = "self";
    public static final String NAMESPACE_AXIS           = "namespace";
    
    //-- System Functions
    public static final String FUNCTION_AVAILABLE_FN  = "function-available";
    public static final String SYSTEM_PROPERTY_FN  = "system-property";
    
      //------------------------/
     //- Expression Operators -/
    //------------------------/
    
    public static final String AND_OPNAME          = "and";
    public static final String OR_OPNAME           = "or";
    public static final String MOD_OPNAME          = "mod";
    public static final String DIV_OPNAME          = "div";
    public static final String QUO_OPNAME          = "quo";
    
    public static final String EQUALS_OP           = "=";
    public static final String LESS_THAN_OP        = "<";
    public static final String GREATER_THAN_OP     = ">";
    public static final String LT_OR_EQUAL_OP      = "<=";
    public static final String GT_OR_EQUAL_OP      = ">=";
    public static final String NOT_EQUAL_OP        = "!=";
    
    public static final String ADDITION_OP         = "+";
    public static final String SUBTRACTION_OP      = "-";
    public static final String MULTIPLY_OP         = "*";
    
    
    //-- Basic Expressions --/
    
    public static final String ROOT_EXPR           = "/";
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * This class is only used for constant name declarations,
     * so make the Constuctor private
    **/
    private Names() {super(); }
}
