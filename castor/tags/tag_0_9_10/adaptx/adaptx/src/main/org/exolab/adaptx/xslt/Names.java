/*
 * (C) Copyright Keith Visco 1998, 1999  All rights reserved.
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

package org.exolab.adaptx.xslt;

/**
 * XSL Names
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Names {
    
    // Attributes
    public static final String AMOUNT_ATTR             = "amount";
    public static final String ATTRIBUTE_ATTR          = "attribute";
    public static final String COUNT_ATTR              = "count";
    public static final String DEFAULT_ATTR            = "default";
    public static final String DEFAULT_SPACE_ATTR      = "default-space";
    public static final String DISABLE_ESCAPING_ATTR   = "disable-output-escaping";
    public static final String DOCTYPE_PUBLIC_ATTR     = "doctype-public";
    public static final String DOCTYPE_SYSTEM_ATTR     = "doctype-system";
    public static final String ELEMENT_ATTR            = "element";
    public static final String ELEMENTS_ATTR           = "elements";
    public static final String ENCODING_ATTR           = "encoding";
    public static final String EXT_ELEMENT_PREFIXES    = "extension-element-prefixes";
    public static final String FORMAT_ATTR             = "format";
    public static final String FROM_ATTR               = "from";
    public static final String HREF_ATTR               = "href";
    public static final String ID_ATTR                 = "id";
    public static final String INDENT_RESULT_ATTR      = "indent-result";    
    public static final String INDENT_ATTR             = "indent";
    public static final String LANGUAGE_ATTR           = "language";
    public static final String LEVEL_ATTR              = "level";
    public static final String MACRO_ATTR              = "macro";
    public static final String MATCH_ATTR              = "match";
    public static final String METHOD_ATTR             = "method";
    public static final String MEDIA_TYPE_ATTR         = "media-type";
    public static final String MODE_ATTR               = "mode";
    public static final String NAME_ATTR               = "name";
    public static final String NS_ATTR                 = "ns";    
    public static final String OMIT_XML_DECL_ATTR      = "omit-xml-declaration";
    public static final String POSITION_ATTR           = "position";
    public static final String PRIORITY_ATTR           = "priority";
    public static final String RESULT_NS_ATTR          = "result-ns";
    public static final String SELECT_ATTR             = "select";
    public static final String STANDALONE_ATTR         = "standalone";
    public static final String TEST_ATTR               = "test";
    public static final String TYPE_ATTR               = "type";
    public static final String USE_ATTRIBUTE_SETS_ATTR = "use-attribute-sets";    
    public static final String VALUE_ATTR              = "value";
    public static final String VERSION_ATTR            = "version";
    
    public static final String XMLLANG_ATTR        = "xml:lang";
    public static final String XMLSPACE_ATTR       = "xml:space";

    // Attribute Values
    public static final String ANY_VALUE          = "any";
    public static final String DEFAULT_VALUE      = "default";
    public static final String INHERIT_VALUE      = "inherit";
    public static final String MULTIPLE_VALUE     = "multiple";
    public static final String NO_VALUE           = "no";
    public static final String PRESERVE_VALUE     = "preserve";
    public static final String SINGLE_VALUE       = "single";
    public static final String YES_VALUE          = "yes";
    public static final String WILD_CARD          = "*";

    // Stylesheet Elements
    public static final String APPLY_IMPORTS      = "apply-imports";
    public static final String APPLY_TEMPLATES    = "apply-templates";
    public static final String ATTRIBUTE_SET      = "attribute-set";
    public static final String ATTRIBUTE          = "attribute";
    public static final String CALL_TEMPLATE      = "call-template";  // Added for WD-xslt-19990421
    public static final String CHOOSE             = "choose";
    public static final String COMMENT            = "comment";
    public static final String CONSTANT           = "constant";
    public static final String CONTENTS           = "contents";
    public static final String COPY               = "copy";
    public static final String COPY_OF            = "copy-of";
    public static final String DECIMAL_FORMAT     = "decimal-format";
    public static final String ELEMENT            = "element";
    public static final String FOR_EACH           = "for-each";
    public static final String FUNCTIONS          = "functions";
    public static final String ID                 = "id";
    public static final String IF                 = "if";
    public static final String IMPORT             = "import";
    public static final String INCLUDE            = "include";
    public static final String KEY                = "key";
    public static final String LOCALE             = "locale";
    public static final String MESSAGE            = "message";
    public static final String NAMESPACE_ALIAS    = "namespace-alias";
    public static final String NUMBER             = "number";
    public static final String OTHERWISE          = "otherwise";
    public static final String OUTPUT             = "output";
    public static final String PARAM              = "param";
    public static final String PI                 = "processing-instruction";
    public static final String PRESERVE_SPACE     = "preserve-space";
    public static final String SORT               = "sort";
    public static final String STRIP_SPACE        = "strip-space";
    public static final String STYLESHEET         = "stylesheet";
    public static final String TEMPLATE           = "template";
    public static final String TRANSFORM          = "transform";
    public static final String TEXT               = "text";
    public static final String VALUE_OF           = "value-of";
    public static final String VARIABLE           = "variable";
    public static final String WHEN               = "when";
    public static final String WITH_PARAM         = "with-param";
    

    // Namespace Handling
    public static final String QUOTE               = "quote:";
    public static final String XMLNS               = "xmlns:";

    // Proprietary xsl elements
    public static final String CDATA              = "cdata";
    public static final String ENTITY_REF         = "entity-ref";
    public static final String SCRIPT             = "script";


    //-- Output methods
    public static final String HTML_OUTPUT        = "html";
    public static final String XML_OUTPUT         = "text";
    public static final String TEXT_OUTPUT        = "xml";
    
    
      //------------------/
     //- function names -/
    //------------------/
    
    //-- XSLT extensions to XPath functions
    public static final String CURRENT_FN             = "current";
    public static final String DOC_FN                 = "doc";
    public static final String DOCREF_FN              = "docref";
    public static final String DOCUMENT_FN            = "document";
    public static final String GENERATE_ID_FN         = "generate-id";
    public static final String FUNCTION_AVAILABLE_FN  = "function-available";
    public static final String SYSTEM_PROPERTY_FN     = "system-property";
    
    //-- XSL:P extensions to XPath functions
    //-- when used in the document, these functions must be
    //-- prefixed with the XSLP_EXTENSION_PREFIX 
    //-- <currently no extention functions>
    
    //-- deprecated //remove these
    public static final String NAME_FN                = "name";
    
    
    //-- the namespace used by XSL:P supplied extensions
    public static final String XSLP_EXTENSION_NAMESPACE  
        = "http://www.exolab.org/xslp";
    
    //-- XSL:P specific extensions
    public static final String RTF_2_NODESET_FN  = "rtf-2-nodeset";
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * This class is only used for constant name declarations,
     * so make the Constuctor private
    **/
    private Names() {super(); }
}
