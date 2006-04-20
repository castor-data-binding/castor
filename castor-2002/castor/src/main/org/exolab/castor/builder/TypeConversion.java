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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;
import org.exolab.javasource.*;
import org.exolab.castor.util.OrderedMap;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.JavaXMLNaming;
import org.exolab.castor.xml.schema.Datatype;
import org.exolab.castor.xml.schema.types.*;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class TypeConversion {
    
    
    public static final String TIME_FORMAT 
        = "CCYY-MM-DDThh:mm:ss.sss";
        
    private static OrderedMap sjNameMap = iCreateNameMap();
    
    /**
     * Returns the Java type name based on the given Schema
     * type name
    **/
    public static String getJavaTypeName(String schemaTypeName) {
        if (schemaTypeName == null) return null;
        String mappedName = (String) sjNameMap.get(schemaTypeName);
        if (mappedName != null) return mappedName;
        else return schemaTypeName;
    } //-- getJavaTypeName
    
    /*
    public static XSType createXSType(String schemaType) {
        
        XSType xsType = null;
        
        //-- string
        if ("string".equals(schemaType)) {
            xsType = new XSString();
        }
        //-- integer
        else if ("integer".equals(schemaType)) {
            xsType = new XSInteger();
        }
        else if ("binary".equals(schemaType)) {
            xsType = new XSBinary();
        }
        else if ("boolean".equals(schemaType)) {
            xsType = new XSBoolean();
        }
        //-- positive-integer
        else if ("negative-integer".equals(schemaType)) {
            xsType = new XSNegativeInteger();
        }
        //-- positive-integer
        else if ("positive-integer".equals(schemaType)) {
            xsType = new XSPositiveInteger();
        }
        //-- real
        else if ("real".equals(schemaType)) {
            xsType = new XSReal();
        }
        else if ("NCName".equals(schemaType)) {
            xsType = new XSNCName();
        }
        //-- NMTOKEN
        else if ("NMTOKEN".equals(schemaType)) {
            xsType = new XSNMToken();
        }
        else if ("timeInstant".equals(schemaType)) {
            xsType = new XSTimeInstant();
        }
        else {
            xsType = new XSClass(new JClass(getJavaTypeName(schemaType)));
        }
        return xsType;
    } //-- createXSType
    */
    
    
    /**
     * Converts the given Datatype to the appropriate XSType.
     * @return the XSType which represets the given Datatype
    **/
    public static XSType convertType(Datatype datatype) {
        
        if (datatype == null) return null;
        
        XSType xsType = null;
        
        
        //-- enumerated types
        if (datatype.hasFacet("enumeration")) {
            String className 
                = JavaXMLNaming.toJavaClassName(datatype.getName());
                
            XSClass xsClass = new XSClass(new JClass(className));
            xsClass.setAsEnumertated(true);
            return xsClass;
        }
        
        //-- determine base type
        Datatype base = datatype;
        while ((base != null) && (!(base instanceof BuiltInType))) {
            base = base.getSource();
        }
        if (base == null) {
            String className 
                = JavaXMLNaming.toJavaClassName(datatype.getName());
            xsType = new XSClass(new JClass(className));
        }
        else {
            switch ( ((BuiltInType)base).getType() ) {
                
                //-- ID
                case BuiltInType.ID_TYPE:
                    return new XSId();
                //-- IDREF
                case BuiltInType.IDREF_TYPE:
                    return new XSIdRef();
                //-- NCName
                case BuiltInType.NCNAME_TYPE:
                    return new XSNCName();
                //-- NMTOKEN
                case BuiltInType.NMTOKEN_TYPE:
                    return new XSNMToken();
                //-- binary
                case BuiltInType.BINARY_TYPE:
                    return new XSBinary();
                //-- boolean
                case BuiltInType.BOOLEAN_TYPE:
                    return new XSBoolean();
                //-- double
                case BuiltInType.DOUBLE_TYPE:
                    return new XSReal();
                //-- integer
                case BuiltInType.INTEGER_TYPE:
                    return toXSInteger(datatype);
                case BuiltInType.LONG_TYPE:
                    return new XSLong();
                //-- string
                case BuiltInType.STRING_TYPE:
                    return toXSString(datatype);
                //-- timeInstant
                case BuiltInType.TIME_INSTANT_TYPE:
                    return new XSTimeInstant();
                default:
                    //-- error
                    String className 
                        = JavaXMLNaming.toJavaClassName(datatype.getName());
                    xsType = new XSClass(new JClass(className));
                    break;
                
            }
        }
        return xsType;
        
    } //-- convertType
    
        
    /**
     * Determines if the given type is a built in Schema datatype
    **/
    public static boolean isBuiltInType(String type) {
        return (sjNameMap.get(type) != null);
    } //-- isBuiltInType
    
    public static String getSchemaTypeName(String javaTypeName) {
        return sjNameMap.getNameByObject(javaTypeName);
    } //-- getSchemaTypeName;
        
        
      //-------------------/
     //- Private Methods -/
    //-------------------/
    
    /**
     * Converts the given datatype to an XSInteger
     * @param datatype the Datatype to convert
     * @return the XSInteger representation of the given Datatype
    **/
    private static XSInteger toXSInteger(Datatype datatype) {
        XSInteger xsInteger = new XSInteger();
        
        //-- copy valid facets
        Enumeration enum = datatype.getFacets();
        while (enum.hasMoreElements()) {
            
            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();
            
            //-- maxExclusive
            if (Facet.MAX_EXCLUSIVE.equals(name))
                xsInteger.setMaxExclusive(facet.toInt());
            //-- maxInclusive
            else if (Facet.MAX_INCLUSIVE.equals(name))
                xsInteger.setMaxInclusive(facet.toInt());
            //-- minExclusive
            else if (Facet.MIN_EXCLUSIVE.equals(facet.getName()))
                xsInteger.setMinExclusive(facet.toInt());
            //-- minInclusive
            else if (Facet.MIN_INCLUSIVE.equals(facet.getName()))
                xsInteger.setMinInclusive(facet.toInt());
            
        }
        
        return xsInteger;
    } //-- toXSInteger
    
    /**
     * Converts the given datatype to an XSString
     * @param datatype the Datatype to convert
     * @return the XSString representation of the given Datatype
    **/
    private static XSString toXSString(Datatype datatype) {
        XSString xsString = new XSString();
        return xsString;
    } //-- toXSString
    
    /**
     * Creates the naming table for type conversion
    **/
    private static OrderedMap iCreateNameMap() {
        
        OrderedMap nameMap = new OrderedMap(10);
        
        //-- #IDREF...temporary this will be changed, once
        //-- I add in the Resolver code
        nameMap.put("IDREF",        "java.lang.String");
        
        //-- type mappings
        nameMap.put("ID",                  "java.lang.String");
        nameMap.put("NCName",              "java.lang.String");
        nameMap.put("NMTOKEN",             "java.lang.String");
        nameMap.put("binary",              "byte[]");
        nameMap.put("boolean",             "boolean");
        nameMap.put("integer",             "int");
        nameMap.put("negative-integer",    "int");
        nameMap.put("positive-integer",    "int");
        nameMap.put("real",                "double");
        nameMap.put("string",              "java.lang.String");
        nameMap.put("timeInstant",         "java.util.Date");
        
        return nameMap;
    } //-- iCreateNameMap
    
} //-- TypeConversion
