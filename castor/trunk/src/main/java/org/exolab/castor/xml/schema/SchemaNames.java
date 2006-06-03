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
 * Copyright 1999-2003 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;


/**
 * A class which holds the names for many of the Schema
 * related components.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
**/
public class SchemaNames {

    //-- packages
    public static final String SCHEMA_PACKAGE =
        "org.exolab.castor.xml.schema";

    //-- element definitions
    public static final String ALL              = "all";
    public static final String ANNOTATION       = "annotation";
    public static final String ANY              = "any";
    public static final String ANYTYPE          = "anyType";
    public static final String ANY_ATTRIBUTE    = "anyAttribute";
    public static final String APPINFO          = "appinfo";
    public static final String ATTRIBUTE        = "attribute";
    public static final String ATTRIBUTE_GROUP  = "attributeGroup";
    public static final String CHOICE           = "choice";
    public static final String COMPLEX_CONTENT  = "complexContent";
    public static final String COMPLEX_TYPE     = "complexType";
    public static final String DOCUMENTATION    = "documentation";
    public static final String ELEMENT          = "element";
    public static final String ENUMERATION      = "enumeration";
    public static final String EXTENSION        = "extension";
    public static final String FIELD            = "field";
    public static final String GROUP            = "group";
    public static final String INCLUDE          = "include";
    public static final String IMPORT           = "import";
    public static final String KEY              = "key";
    public static final String KEYREF           = "keyref";
    public static final String LIST             = "list";
    public static final String MAX_EXCLUSIVE    = "maxExclusive";
    public static final String MAX_INCLUSIVE    = "maxInclusive";
    public static final String MIN_EXCLUSIVE    = "minExclusive";
    public static final String MIN_INCLUSIVE    = "minInclusive";
    public static final String REDEFINE         = "redefine";
    public static final String RESTRICTION      = "restriction";
    public static final String SCHEMA           = "schema";
    public static final String SELECTOR         = "selector";
    public static final String SEQUENCE         = "sequence";
    public static final String SIMPLETYPE_REF   = "simpletypeRef";
    public static final String SIMPLE_CONTENT   = "simpleContent";
    public static final String SIMPLE_TYPE      = "simpleType";
    public static final String UNION            = "union";
    public static final String UNIQUE           = "unique";

    //-- Attr name definitions
    public static final String ABSTRACT            = "abstract";
    public static final String ATTR_FORM_DEFAULT_ATTR = "attributeFormDefault";
    public static final String BASE_ATTR           = "base";
    public static final String BLOCK_ATTR          = "block";
    public static final String BLOCK_DEFAULT_ATTR  = "blockDefault";
    public static final String DEFAULT_ATTR        = "default";
    public static final String ELEM_FORM_DEFAULT_ATTR = "elementFormDefault";
    public static final String ID_ATTR             = "id";
    public static final String ITEM_TYPE_ATTR      = "itemType";
    public static final String FINAL_ATTR          = "final";
    public static final String FINAL_DEFAULT_ATTR  = "finalDefault";
    public static final String FIXED_ATTR          = "fixed";
    public static final String FORM                = "form";
    public static final String MAX_OCCURS_ATTR     = "maxOccurs";
    public static final String MEMBER_TYPES_ATTR   = "memberTypes";
    public static final String MIN_OCCURS_ATTR     = "minOccurs";
    public static final String MIXED               = "mixed";
	public static final String NAME_ATTR           = "name";
    public static final String NAMESPACE           = "namespace";
    public static final String NILLABLE_ATTR       = "nillable";
    public static final String ORDER_ATTR          = "order";
    public static final String PROCESS_CONTENTS    = "processContents";
    public static final String REF_ATTR            = "ref";
    public static final String REFER_ATTR          = "refer";
    public static final String SCHEMALOCATION_ATTR = "schemaLocation";
    public static final String SOURCE_ATTR         = "source";
    public static final String SUBSTITUTION_GROUP_ATTR = "substitutionGroup";
    public static final String TARGET_NS_ATTR      = "targetNamespace";
    public static final String TYPE_ATTR           = "type";
    public static final String USE_ATTR            = "use";
    public static final String VALUE_ATTR          = "value";
    public static final String VERSION_ATTR        = "version";
    public static final String XPATH_ATTR          = "xpath";

    //-- data types
    public static final String INTEGER_TYPE     = "integer";
    public static final String INT_TYPE         = "int";
    public static final String STRING_TYPE      = "string";

    //-- namespaces
    public static final String NAMESPACE_ANY     ="##any";
    public static final String NAMESPACE_LOCAL   ="##local";
    public static final String NAMESPACE_OTHER   ="##other";
    public static final String NAMESPACE_TARGET  ="##targetNamespace";

    //-- processContents
    public static final String LAX              = "lax";
    public static final String SKIP             = "skip";
    public static final String STRICT           = "strict";

    //-- Wildcard name
    public static final String WILDCARD         = "wildcard";
    
    
    /**
     * Returns the name for the Schema Structure.
     *
     * @param structure the Structure to return the name for
     * @return the Structure name
     */
    public static String getStructureName(Structure structure) {
        
        if (structure == null) return null;
        
        switch (structure.getStructureType()) {
            case Structure.ANYTYPE:
                return ANYTYPE;
            case Structure.ANNOTATION:
                return ANNOTATION;
            case Structure.APPINFO:
                return APPINFO;
            case Structure.ATTRIBUTE:
                return ATTRIBUTE;
            case Structure.ATTRIBUTE_GROUP: 
                return ATTRIBUTE_GROUP;
            case Structure.COMPLEX_CONTENT:
                return COMPLEX_CONTENT;
            case Structure.COMPLEX_TYPE:
                return COMPLEX_TYPE;
            case Structure.DOCUMENTATION:
                return DOCUMENTATION;
            case Structure.ELEMENT:
                return ELEMENT;
            case Structure.FACET:
                Facet facet = (Facet)structure;
                return facet.getName();
            case Structure.GROUP:
                Group group = (Group)structure;
                Order order = group.getOrder();
                return order.toString();
            case Structure.IDENTITY_FIELD:
                return FIELD;
            case Structure.IDENTITY_SELECTOR:
                return SELECTOR;
            case Structure.KEY:
                return KEY;
            case Structure.KEYREF:
                return KEYREF;
            case Structure.LIST:
                return LIST;
            case Structure.MODELGROUP:
                return GROUP;
            case Structure.MODELGROUP_REF:
                return GROUP;
            case Structure.REDEFINE:
                return REDEFINE;
            case Structure.SCHEMA:
                return SCHEMA;
            case Structure.SIMPLE_CONTENT:
                return SIMPLE_CONTENT;
            case Structure.SIMPLE_TYPE:
                return SIMPLE_TYPE;
            case Structure.UNION:
                return UNION;
            case Structure.UNIQUE:
                return UNIQUE;
            case Structure.WILDCARD:
                return ANY;
            default:
                break;
        }
        return null;        
    } //-- getStructureName
    
    /**
     * Determines whether or not the given name is the name
     * of an XML Schema group structure.
     * @param name the name to test
     * @return true if the given name is the name of a schema group
    **/
    public static boolean isGroupName(String name) {
        return (SchemaNames.GROUP.equals(name)    ||
                SchemaNames.SEQUENCE.equals(name) ||
                SchemaNames.CHOICE.equals(name)   ||
                SchemaNames.ALL.equals(name));
    } //-- isGroupName

    /**
     * Determines whether or not the given name is the name
     * of an XML Schema namespace attributes.
     * Note: it assumes that the URI used are valid URL
     * @todo: change the above restriction
     * @param name the name to test
     * @return true if the given name is the name of a schema group
    **/
    public static boolean isNamespaceName(String name) {
        boolean result = (SchemaNames.NAMESPACE_ANY.equals(name)    ||
                SchemaNames.NAMESPACE_LOCAL.equals(name) ||
                SchemaNames.NAMESPACE_OTHER.equals(name)   ||
                SchemaNames.NAMESPACE_TARGET.equals(name));
        if (result) return result;
        try {
            new java.net.URL(name);
            result = true;
        } catch (java.net.MalformedURLException e){
            //invalid URL
            result = false;
        }
        return result;
    } //-- isNamespaceName

    /**
     * Determines whether or not the given name is the name
     * of an XML Schema Wildcard processContents attribute.
     * @param name the name to test
     * @return true if the given name is a valid name of a processContents attribute
    **/
    public static boolean isProcessName(String name) {
        return (SchemaNames.LAX.equals(name)    ||
                SchemaNames.SKIP.equals(name) ||
                SchemaNames.STRICT.equals(name));
    } //-- isProcessName


} //-- SchemaNames
