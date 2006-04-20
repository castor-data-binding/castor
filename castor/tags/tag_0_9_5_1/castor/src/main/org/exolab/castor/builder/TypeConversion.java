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
 * Copyright 1999-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.*;
import org.exolab.castor.types.TimeDuration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.SimpleTypesFactory;
import org.exolab.castor.xml.schema.Structure;
import org.exolab.castor.xml.schema.Union;

import java.text.ParseException;
import java.util.Enumeration;

/**
 * A class used to convert XML Schema SimpleTypes into
 * the appropriate XSType
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class TypeConversion {

    private static final String TYPES_PACKAGE = "types";

    private BuilderConfiguration _config = null;
    
    /**
     * Creates a new TypeConversion instance
     *
     * @param config the BuilderConfiguration instance (must not be null).
     */
    public TypeConversion(BuilderConfiguration config) {
        if (config == null) {
            String error = "The argument 'config' must not be null.";
            throw new IllegalArgumentException(error);
        }
        _config = config;
    } //-- TypeConversion
    
    /**
     * Converts the given Simpletype to the appropriate XSType.
     *
     * @param simpleType the SimpleType to convert to an XSType instance
     * @return the XSType which represets the given Simpletype
     */
    public XSType convertType(SimpleType simpleType) {
        return convertType(simpleType, _config.usePrimitiveWrapper(), null);
    }

    /**
     * Converts the given Simpletype to the appropriate XSType.
     *
     * @param simpleType the SimpleType to convert to an XSType instance
     * @param packageName the packageName for any new class types
     * @return the XSType which represets the given Simpletype
     */
    public XSType convertType(SimpleType simpleType, String packageName) {
         return convertType(simpleType, _config.usePrimitiveWrapper(), packageName);
    }
    
    /**
     * Converts the given Simpletype to the appropriate XSType.
     *
     * @param simpleType the SimpleType to convert to an XSType instance
     * @param useWrapper a boolean that when true indicates that primitive wrappers
     * be used instead of the actual primitives (e.g. java.lang.Integer instead of int)
     * @param packageName the packageName for any new class types
     * @return the XSType which represets the given Simpletype
     */
    public XSType convertType(SimpleType simpleType, boolean useWrapper, String packageName) {
        if (simpleType == null) return null;

        XSType xsType = null;
        //-- determine base type
        SimpleType base = simpleType;

        while ((base != null) && (!base.isBuiltInType())) {
            base = (SimpleType)base.getBaseType();
        }

        if (simpleType.getStructureType() == Structure.UNION) {
            SimpleType common = findCommonType((Union)simpleType);
            if (common == null) {
                return new XSClass(SGTypes.Object);
            }
            return convertType(common, useWrapper,packageName);
        }
        else if (base == null) {
            String className
                = JavaNaming.toJavaClassName(simpleType.getName());
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
                //-- NMTOKEN
                case SimpleTypesFactory.NMTOKEN_TYPE:
                    return new XSNMToken();
                //-- NMTOKENS
                case SimpleTypesFactory.NMTOKENS_TYPE:
                    return new XSList(new XSNMToken());


                //--AnyURI
                case SimpleTypesFactory.ANYURI_TYPE:
                    return new XSAnyURI();
                //-- base64Bbinary
                case SimpleTypesFactory.BASE64BINARY_TYPE:
                    return new XSBinary(XSType.BASE64BINARY_TYPE);
                //-- hexBinary
                case SimpleTypesFactory.HEXBINARY_TYPE:
                     return new XSBinary(XSType.HEXBINARY_TYPE);
                //-- boolean
                case SimpleTypesFactory.BOOLEAN_TYPE:
                    return new XSBoolean(useWrapper);
                //--byte
                case SimpleTypesFactory.BYTE_TYPE:
                {
                    XSByte xsByte = new XSByte(useWrapper);
                    if (!simpleType.isBuiltInType())
                        xsByte.setFacets(simpleType);
                    return xsByte;
                }
                //-- date
                case SimpleTypesFactory.DATE_TYPE:
                {
                     XSDate xsDate = new XSDate();
                    if (!simpleType.isBuiltInType())
                        xsDate.setFacets(simpleType);
                    return xsDate;
                }
                //-- dateTime
                case SimpleTypesFactory.DATETIME_TYPE:
                    return new XSDateTime();
                //-- double
                case SimpleTypesFactory.DOUBLE_TYPE:
                 {
                    XSDouble xsDouble = new XSDouble(useWrapper);
                    if (!simpleType.isBuiltInType())
                        xsDouble.setFacets(simpleType);
                    return xsDouble;
                }
                 //-- duration
                case SimpleTypesFactory.DURATION_TYPE:
                {
					XSDuration xsDuration = new XSDuration();
					if (!simpleType.isBuiltInType())
					   xsDuration.setFacets(simpleType);
                    return xsDuration;
                }
                //-- decimal
                case SimpleTypesFactory.DECIMAL_TYPE:
                {
                    XSDecimal xsDecimal = new XSDecimal();
                    if (!simpleType.isBuiltInType())
					   xsDecimal.setFacets(simpleType);
                    return xsDecimal;
                }

                //-- float
                case SimpleTypesFactory.FLOAT_TYPE:
                {
                    XSFloat xsFloat = new XSFloat(useWrapper);
                    if (!simpleType.isBuiltInType())
                        xsFloat.setFacets(simpleType);
                    return xsFloat;
                }
                //--GDay
                case SimpleTypesFactory.GDAY_TYPE:
                {
                    XSGDay xsGDay = new XSGDay();
                    if (!simpleType.isBuiltInType())
                        xsGDay.setFacets(simpleType);
                    return xsGDay;
                }
                //--GMonthDay
                case SimpleTypesFactory.GMONTHDAY_TYPE:
                {
                    XSGMonthDay xsGMonthDay = new XSGMonthDay();
                    if (!simpleType.isBuiltInType())
                        xsGMonthDay.setFacets(simpleType);
                    return xsGMonthDay;
                }
                //--GMonth
                case SimpleTypesFactory.GMONTH_TYPE:
                {
                    XSGMonth xsGMonth = new XSGMonth();
                    if (!simpleType.isBuiltInType())
                        xsGMonth.setFacets(simpleType);
                    return xsGMonth;
                }
                //--GYearMonth
                case SimpleTypesFactory.GYEARMONTH_TYPE:
                {
                    XSGYearMonth xsGYearMonth = new XSGYearMonth();
                    if (!simpleType.isBuiltInType())
                        xsGYearMonth.setFacets(simpleType);
                    return xsGYearMonth;
                }
                //--GYear
                case SimpleTypesFactory.GYEAR_TYPE:
                {
                    XSGYear xsGYear = new XSGYear();
                    if (!simpleType.isBuiltInType())
                        xsGYear.setFacets(simpleType);
                    return xsGYear;
                }

                //-- integer
                case SimpleTypesFactory.INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSInteger(useWrapper);
                    if (!simpleType.isBuiltInType())
                        xsInteger.setFacets(simpleType);
                    return xsInteger;
                }
                //-- int
				case SimpleTypesFactory.INT_TYPE:
				{
					XSInt xsInt = new XSInt(useWrapper);
					if (!simpleType.isBuiltInType())
					    xsInt.setFacets(simpleType);
                    return xsInt;
                }
                case SimpleTypesFactory.LANGUAGE_TYPE:
                {
                    //-- since we don't actually support this type, yet,
                    //-- we'll simply treat it as a string, but warn the
                    //-- user.
                    String warning = "Warning: The W3C datatype '"+simpleType.getName();
                    warning += "' is not currently supported by Castor Source Generator.";
                    warning += " It will be treated as a java.lang.String.";
                    System.out.println(warning);
                    return new XSString();
                }
                //-- long
                case SimpleTypesFactory.LONG_TYPE:
                {
                    XSLong xsLong = new XSLong(useWrapper);
                    if (!simpleType.isBuiltInType())
                        xsLong.setFacets(simpleType);
                    return xsLong;
                }
                //--NCName
                case SimpleTypesFactory.NCNAME_TYPE:
                    return new XSNCName();
                //-- nonPositiveInteger
                case SimpleTypesFactory.NON_POSITIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSNonPositiveInteger(useWrapper);
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }

                //-- nonNegativeInteger
                case SimpleTypesFactory.NON_NEGATIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSNonNegativeInteger(useWrapper);
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }

                //-- negative-integer
                case SimpleTypesFactory.NEGATIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSNegativeInteger(useWrapper);
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }
                //-- normalizedString
                case SimpleTypesFactory.NORMALIZEDSTRING_TYPE:
                {
                    XSNormalizedString xsNormalString = new XSNormalizedString();
                    if (!simpleType.isBuiltInType())
                        xsNormalString.setFacets(simpleType);
                    return xsNormalString;
                }

                //-- positive-integer
                case SimpleTypesFactory.POSITIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSPositiveInteger(useWrapper);
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }
                //-- QName
                case SimpleTypesFactory.QNAME_TYPE:
                {
                    XSQName xsQName = new XSQName();
                    xsQName.setFacets(simpleType);
                    return xsQName;
                }
                //-- string
                case SimpleTypesFactory.STRING_TYPE:
                {
                    //-- Enumeration ?
                    if (simpleType.hasFacet(Facet.ENUMERATION)) {

                        String typeName = simpleType.getName();

                        //-- anonymous type
                        if (typeName == null) {
                            Structure parent = simpleType.getParent();
                            if (parent instanceof ElementDecl) {
                                typeName = ((ElementDecl)parent).getName();
                            }
                            else if (parent instanceof AttributeDecl) {
                                typeName = ((AttributeDecl)parent).getName();
                            }
                            typeName = typeName + "Type";
                        }
                        String className
                            = JavaNaming.toJavaClassName(typeName);

                        if (packageName == null) {
                            String ns = simpleType.getSchema().getTargetNamespace();
                            packageName = _config.lookupPackageByNamespace(ns);
                        }
                        if ((packageName  != null) && (packageName .length() > 0))
                            packageName  = packageName  + '.' + TYPES_PACKAGE;
                        else
                            packageName  = TYPES_PACKAGE;

                        className = packageName  + '.' + className;
                        xsType = new XSClass(new JClass(className));
                        xsType.setAsEnumertated(true);
                    } //- End Enumeration
                    else {
                        XSString xsString = new XSString();
                        if (!simpleType.isBuiltInType()) {
                            xsString.setFacets(simpleType);
                        }
                        xsType = xsString;
                    }
                    break;
                }
                //-- short
                case SimpleTypesFactory.SHORT_TYPE:
                {
					XSShort xsShort = new XSShort(useWrapper);
					if (!simpleType.isBuiltInType())
                        xsShort.setFacets(simpleType);
                    return xsShort;
                }
                case SimpleTypesFactory.TIME_TYPE:
                {
                    XSTime xsTime = new XSTime();
                    if (!simpleType.isBuiltInType())
                        xsTime.setFacets(simpleType);
                    return xsTime;
                }
                default:
                    //-- error
                    String name = simpleType.getName();
                    if (name == null || name.length() ==0) {
                       //--we know it is a restriction
                       name = simpleType.getBuiltInBaseType().getName();
                    }

                    String warning = "Warning: The W3C datatype '"+name;
                    warning += "' is not currently supported by Castor Source Generator.";
                    System.out.println(warning);
                    String className
                        = JavaNaming.toJavaClassName(name);
                        xsType = new XSClass(new JClass(className));
                    break;
            }
        }
        return xsType;

    } //-- convertType

    /**
     * Returns the XSType that corresponds to the given javaType
     * @param javaType
     * @return
     */
    public static XSType convertType(String javaType) {
        if (javaType == null)
            return null;
        //--Boolean
        if (javaType.equals(TypeNames.BOOLEAN_OBJECT)) {
            return new XSBoolean(true);
        } else
        if (javaType.equals(TypeNames.BOOLEAN_PRIMITIVE)) {
            return new XSBoolean(false);
        } else
        //--Byte
        if (javaType.equals(TypeNames.BYTE_OBJECT)) {
            return new XSByte(true);
        } else
        if (javaType.equals(TypeNames.BYTE_PRIMITIVE)) {
            return new XSBoolean(false);
        } else
        //--Castor date/Time implementation
        if (javaType.equals(TypeNames.CASTOR_DATE)) {
            return new XSDateTime();
        } else
        if (javaType.equals(TypeNames.CASTOR_DURATION)) {
            return new XSDuration();
        } else
        if (javaType.equals(TypeNames.CASTOR_GDAY)) {
            return new XSGDay();
        } else
        if (javaType.equals(TypeNames.CASTOR_GMONTH)) {
            return new XSGMonth();
        } else
        if (javaType.equals(TypeNames.CASTOR_GMONTHDAY)) {
            return new XSGMonthDay();
        } else
        if (javaType.equals(TypeNames.CASTOR_GYEAR)) {
            return new XSGYear();
        } else
        if (javaType.equals(TypeNames.CASTOR_GYEARMONTH)) {
            return new XSGYearMonth();
        } else
        if (javaType.equals(TypeNames.CASTOR_TIME)) {
            return new XSTime();
        } else
        //--java date
        if (javaType.equals(TypeNames.DATE)) {
            return new XSDate();
        } else
        //--decimal
        if (javaType.equals(TypeNames.DECIMAL)) {
            return new XSDecimal();
        } else
        //--Double
        if (javaType.equals(TypeNames.DOUBLE_OBJECT)) {
            return new XSDouble(true);
        } else
        if (javaType.equals(TypeNames.DOUBLE_PRIMITIVE)) {
            return new XSDouble(false);
        } else
        //--Float
        if (javaType.equals(TypeNames.FLOAT_OBJECT)) {
            return new XSFloat(true);
        } else
        if (javaType.equals(TypeNames.FLOAT_PRIMITIVE)) {
            return new XSDouble(false);
        } else
        //--Integer
        if (javaType.equals(TypeNames.INTEGER)) {
            return new XSInteger(true);
        } else
        if (javaType.equals(TypeNames.INT)) {
            return new XSInt();
        } else
        //--Short
        if (javaType.equals(TypeNames.SHORT_OBJECT)) {
            return new XSShort(true);
        } else
        if (javaType.equals(TypeNames.SHORT_PRIMITIVE)) {
            return new XSShort(false);
        } else
        //--String
        if (javaType.equals(TypeNames.STRING)) {
            return new XSString();
        } else
        //--no XSType implemented for it we return a XSClass
        return new XSClass(new JClass(javaType));
    }

    /**
     * Returns the common type for the Union, or null if no
     * common type exists among the members of the Union.
     *
     * @param union the Union to return the common type for
     * @return the common SimpleType for the Union.
     */
    private static SimpleType findCommonType(Union union) {
        SimpleType common = null;
        Enumeration enum = union.getMemberTypes();
        while (enum.hasMoreElements()) {
            SimpleType type = (SimpleType)enum.nextElement();
            type = type.getBuiltInBaseType();
            if (common == null) common = type;
            else {
                common = compare(common, type);
                //-- no common types
                if (common == null) break;
            }
        }
        return common;
    } //-- findCommonType


    /**
     * Compares the two SimpleTypes. The common ancestor of
     * both types will be returned, otherwise null is returned
     * if the types are not compatable.
     */
    private static SimpleType compare(SimpleType aType, SimpleType bType) {
        int type1 = aType.getTypeCode();
        int type2 = bType.getTypeCode();

        if (type1 == type2) return aType;

        //-- add comparison code
        if (isNumeric(aType)) {
            if (isNumeric(bType)) {
                //-- compare numbers
                //-- XXXX *To be added*
            }
        }
        else if (isString(aType)) {
            if (isString(bType)) {
                //-- compare string types
                //-- XXXX *To be added*
            }
        }
        //-- Just return string for now, as all simpleTypes can
        //-- fit into a string
        Schema schema = aType.getSchema();
        return schema.getSimpleType("string",
            schema.getSchemaNamespace());
    }

    private static boolean isNumeric(SimpleType type) {
        int code = type.getTypeCode();
        switch (code) {
            case SimpleTypesFactory.BYTE_TYPE:
            case SimpleTypesFactory.DOUBLE_TYPE:
            case SimpleTypesFactory.DECIMAL_TYPE:
            case SimpleTypesFactory.FLOAT_TYPE:
            case SimpleTypesFactory.INTEGER_TYPE:
			case SimpleTypesFactory.INT_TYPE:
            case SimpleTypesFactory.LONG_TYPE:
            case SimpleTypesFactory.NON_POSITIVE_INTEGER_TYPE:
            case SimpleTypesFactory.NON_NEGATIVE_INTEGER_TYPE:
            case SimpleTypesFactory.NEGATIVE_INTEGER_TYPE:
            case SimpleTypesFactory.POSITIVE_INTEGER_TYPE:
            case SimpleTypesFactory.SHORT_TYPE:
                return true;
            default:
                return false;
        }
    } //-- isNumeric

    private static boolean isString(SimpleType type) {
        int code = type.getTypeCode();
        switch (code) {
            //-- string types
            case SimpleTypesFactory.ANYURI_TYPE:
            case SimpleTypesFactory.ID_TYPE:
            case SimpleTypesFactory.IDREF_TYPE:
            case SimpleTypesFactory.IDREFS_TYPE:
            case SimpleTypesFactory.LANGUAGE_TYPE:
            case SimpleTypesFactory.NCNAME_TYPE:
            case SimpleTypesFactory.NMTOKEN_TYPE:
            case SimpleTypesFactory.NMTOKENS_TYPE:
            case SimpleTypesFactory.NORMALIZEDSTRING_TYPE:
            case SimpleTypesFactory.STRING_TYPE:
            case SimpleTypesFactory.QNAME_TYPE:
                return true;
            default:
                return false;
        }
    } //-- isString

    private static boolean isDateOrTime(SimpleType type) {
        int code = type.getTypeCode();
        switch (code) {
            //-- dates/times
            case SimpleTypesFactory.DATE_TYPE:
            case SimpleTypesFactory.DATETIME_TYPE:
            case SimpleTypesFactory.DURATION_TYPE:
            case SimpleTypesFactory.GDAY_TYPE:
            case SimpleTypesFactory.GMONTHDAY_TYPE:
            case SimpleTypesFactory.GMONTH_TYPE:
            case SimpleTypesFactory.GYEARMONTH_TYPE:
            case SimpleTypesFactory.GYEAR_TYPE:
            case SimpleTypesFactory.TIME_TYPE:
                return true;
            default:
                return false;
        }
    } //-- isDateOrTime

    class TypeNames {
        protected static final String BOOLEAN_PRIMITIVE = "boolean";
        protected static final String BOOLEAN_OBJECT = "java.lang.Boolean";
        protected static final String BYTE_PRIMITIVE = "byte";
        protected static final String BYTE_OBJECT = "java.lang.Byte";
        protected static final String DATE = "java.util.Date";
        protected static final String CASTOR_DATE = "org.exolab.castor.types.Date";
        protected static final String CASTOR_TIME = "org.exolab.castor.types.Time";
        protected static final String CASTOR_DURATION = "org.exolab.castor.types.Guration";
        protected static final String CASTOR_GMONTH = "org.exolab.castor.types.GMonth";
        protected static final String CASTOR_GMONTHDAY = "org.exolab.castor.types.GMonthDay";
        protected static final String CASTOR_GYEAR = "org.exolab.castor.types.GYear";
        protected static final String CASTOR_GYEARMONTH = "org.exolab.castor.types.GYearMonth";
        protected static final String CASTOR_GDAY = "org.exolab.castor.types.GDay";
        protected static final String DECIMAL = "java.math.BigDecimal";
        protected static final String DOUBLE_PRIMITIVE = "double";
        protected static final String DOUBLE_OBJECT = "java.lang.Double";
        protected static final String FLOAT_PRIMITIVE = "float";
        protected static final String FLOAT_OBJECT = "java.lang.Float";
        protected static final String INT = "int";
        protected static final String INTEGER = "java.lang.Integer";
        protected static final String SHORT_PRIMITIVE = "short";
        protected static final String SHORT_OBJECT = "java.lang.Short";
        protected static final String STRING = "java.lang.String";
    }
} //-- TypeConversion
