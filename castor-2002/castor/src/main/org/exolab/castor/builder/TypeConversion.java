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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;
import org.exolab.javasource.*;
import org.exolab.castor.util.OrderedMap;
import org.exolab.castor.types.TimeDuration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.JavaXMLNaming;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.SimpleTypesFactory;

import java.util.Enumeration;
import java.util.Hashtable;
import java.text.ParseException;


/**
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class TypeConversion {


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
     * Converts the given Simpletype to the appropriate XSType.
     * @return the XSType which represets the given Simpletype
    **/
    public static XSType convertType(SimpleType simpleType) {
        if (simpleType == null) return null;

        XSType xsType = null;

        //-- enumerated types
        if (simpleType.hasFacet("enumeration")) {
            String className = JavaXMLNaming.toJavaClassName(simpleType.getName());
			className = SourceGenerator.getQualifiedClassName(
							simpleType.getSchema().getTargetNamespace(),
							"types."+className);
            XSClass xsClass = new XSClass(new JClass(className));
            xsClass.setAsEnumertated(true);
            return xsClass;
        }

        //-- determine base type
        SimpleType base = simpleType;

        while ((base != null) && (!base.isBuiltInType())) {
            base = (SimpleType)base.getBaseType();
        }
        if (base == null) {
            String className
                = JavaXMLNaming.toJavaClassName(simpleType.getName());
            xsType = new XSClass(new JClass(className));
        }
        else {
            switch ( base.getTypeCode() ) {

                //-- ID
                case SimpleTypesFactory.ID_TYPE:
                    return new XSId();
                //-- IDREF
                case SimpleTypesFactory.IDREF_TYPE:
                    return new XSIdRef();
                //-- IDREFS
                case SimpleTypesFactory.IDREFS_TYPE:
                    return new XSList(new XSIdRef());
                //--URIREFERENCE
                case SimpleTypesFactory.URIREFERENCE_TYPE:
                    return new XSUriReference();
                //-- NCName
                case SimpleTypesFactory.NCNAME_TYPE:
                    return new XSNCName();
                //-- NMTOKEN
                case SimpleTypesFactory.NMTOKEN_TYPE:
                    return new XSNMToken();
                //-- binary
                case SimpleTypesFactory.BINARY_TYPE:
                    return new XSBinary();
                //-- boolean
                case SimpleTypesFactory.BOOLEAN_TYPE:
                    return new XSBoolean();
                //-- century
                case SimpleTypesFactory.CENTURY_TYPE:
                    return new XSCentury();
                //-- date
                case SimpleTypesFactory.DATE_TYPE:
                    return new XSDate();
                //-- double
                case SimpleTypesFactory.DOUBLE_TYPE:
                    return new XSReal();
                //-- integer
                case SimpleTypesFactory.INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSInteger();
                    if (!simpleType.isBuiltInType())
                        xsInteger.setFacets(simpleType);
                    return xsInteger;
                }
                //-- int
				case SimpleTypesFactory.INT_TYPE:
				{
					XSInt xsInt = new XSInt();
					if (!simpleType.isBuiltInType())
					    xsInt.setFacets(simpleType);
                    return xsInt;
                }
                //-- long
                case SimpleTypesFactory.LONG_TYPE:
                {
                    XSLong xsLong = new XSLong();
                    if (!simpleType.isBuiltInType())
                        xsLong.setFacets(simpleType);
                    return xsLong;
                }
                //-- month
                case SimpleTypesFactory.MONTH_TYPE:
                    return new XSMonth();
                //-- negative-integer
                case SimpleTypesFactory.NEGATIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSNegativeInteger();
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }
                //-- positive-integer
                case SimpleTypesFactory.POSITIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSPositiveInteger();
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }
                //recurringDuration
                case SimpleTypesFactory.RECURRING_DURATION_TYPE:
                {
                    XSRecurringDuration xsrecduration = new XSRecurringDuration();
                    xsrecduration.setFacets(simpleType);
                     if ((xsrecduration.getDuration()==null) ||
                        (xsrecduration.getPeriod()==null)) {
                            String err = "It is an error for recurringDuration to be";
                            err += " used directly in a Schema \n";
                            err += "you must set the duration facet AND the period one";
                            System.out.println(err);
                            return null;
                            // later don't forget to
                            //throw new ValidationException(err);
                     }
                    return xsrecduration;
                }
                //-- string
                case SimpleTypesFactory.STRING_TYPE:
                {
                    XSString xsString = new XSString();
                    if (!simpleType.isBuiltInType())
                        xsString.setFacets(simpleType);
                    return xsString;
                }
                //-- time
                case SimpleTypesFactory.TIME_TYPE:
                    return new XSTime();
                //-- timeInstant
                case SimpleTypesFactory.TIME_INSTANT_TYPE:
                    return new XSTimeInstant();
                //-- Time duration
                case SimpleTypesFactory.TIME_DURATION_TYPE:
                {
					XSTimeDuration xsTimeD = new XSTimeDuration();
					if (!simpleType.isBuiltInType())
					   xsTimeD.setFacets(simpleType);
                    return xsTimeD;
                }
                    //return new XSLong();
                //-- timePeriod
                case SimpleTypesFactory.TIME_PERIOD_TYPE:
                {
                    XSTimePeriod xsTimeP = new XSTimePeriod();
                    if (!simpleType.isBuiltInType())
                        xsTimeP.setFacets(simpleType);
                    return xsTimeP;
                }
                //-- year
                case SimpleTypesFactory.YEAR_TYPE:
                    return new XSYear();
                //-- decimal
                case SimpleTypesFactory.DECIMAL_TYPE:
                {
                    XSDecimal xsDecimal = new XSDecimal();
                    if (!simpleType.isBuiltInType())
					   xsDecimal.setFacets(simpleType);
                    return xsDecimal;
                }
                //-- short
                case SimpleTypesFactory.SHORT_TYPE:
                {
					XSShort xsShort = new XSShort();
					xsShort.setFacets(simpleType);
                    return xsShort;
                }
                default:
                    //-- error
                    String className
                        = JavaXMLNaming.toJavaClassName(simpleType.getName());
                    xsType = new XSClass(new JClass(className));
                    break;

            }
        }
        return xsType;

    } //-- convertType


    /**
     * Determines if the given type is a built in Schema simpletype
    **/
    public static boolean isBuiltInType(String type) {
        return (sjNameMap.get(type) != null);
    } //-- isBuiltInType

    public static String getSchemaTypeName(String javaTypeName) {
        return sjNameMap.getNameByObject(javaTypeName);
    } //-- getSchemaTypeNam

      //-------------------/
     //- Private Methods -/
    //-------------------/



    /**
     * Creates the naming table for type conversion
    **/
    private static OrderedMap iCreateNameMap() {

        OrderedMap nameMap = new OrderedMap(24);

        //-- #IDREF...temporary this will be changed, once
        //-- I add in the Resolver code
        nameMap.put("IDREF",        "java.lang.String");

        // Choose the right collection
        nameMap.put("IDREFS",       "java.lang.Vector");

        //-- type mappings
        nameMap.put("ID",                  "java.lang.String");
        nameMap.put("NCName",              "java.lang.String");
        nameMap.put("NMTOKEN",             "java.lang.String");
        nameMap.put("uriReference",         "java.lang.String");
        nameMap.put("binary",              "byte[]");
        nameMap.put("boolean",             "boolean");
        nameMap.put("century",                "org.exolab.castor.types.Century");
        //nameMap.put("date",                "java.util.date");
        nameMap.put("date",                "org.exolab.castor.types.Date");
        nameMap.put("integer",             "int");
        nameMap.put("month",                "org.exolab.castor.types.Month");
        nameMap.put("negativeInteger",     "int");
        nameMap.put("positiveInteger",     "int");
        nameMap.put("real",                "double");
        nameMap.put("recuuringDuration",    "org.exolab.castor.types.RecurringDuration");
        nameMap.put("string",              "java.lang.String");
        //nameMap.put("time",                "java.sql.Time");
        nameMap.put("time",                "org.exolab.castor.types.Time");
        nameMap.put("timeDuration",        "org.exolab.castor.types.TimeDuration");
       // nameMap.put("timeDuration",        "long");
        nameMap.put("timeInstant",         "java.util.Date");
        nameMap.put("year",                "org.exolab.castor.types.Year");
        nameMap.put("decimal",             "java.math.BigDecimal");
        nameMap.put("short",               "short");
        nameMap.put("int",		           "int");

        return nameMap;
    } //-- iCreateNameMap

} //-- TypeConversion
