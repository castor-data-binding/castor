/*
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

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.builder.types.XSAnyURI;
import org.exolab.castor.builder.types.XSBase64Binary;
import org.exolab.castor.builder.types.XSBoolean;
import org.exolab.castor.builder.types.XSByte;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.castor.builder.types.XSDate;
import org.exolab.castor.builder.types.XSDateTime;
import org.exolab.castor.builder.types.XSDecimal;
import org.exolab.castor.builder.types.XSDouble;
import org.exolab.castor.builder.types.XSDuration;
import org.exolab.castor.builder.types.XSFloat;
import org.exolab.castor.builder.types.XSGDay;
import org.exolab.castor.builder.types.XSGMonth;
import org.exolab.castor.builder.types.XSGMonthDay;
import org.exolab.castor.builder.types.XSGYear;
import org.exolab.castor.builder.types.XSGYearMonth;
import org.exolab.castor.builder.types.XSHexBinary;
import org.exolab.castor.builder.types.XSId;
import org.exolab.castor.builder.types.XSIdRef;
import org.exolab.castor.builder.types.XSIdRefs;
import org.exolab.castor.builder.types.XSInt;
import org.exolab.castor.builder.types.XSInteger;
import org.exolab.castor.builder.types.XSLong;
import org.exolab.castor.builder.types.XSNCName;
import org.exolab.castor.builder.types.XSNMToken;
import org.exolab.castor.builder.types.XSNMTokens;
import org.exolab.castor.builder.types.XSNegativeInteger;
import org.exolab.castor.builder.types.XSNonNegativeInteger;
import org.exolab.castor.builder.types.XSNonPositiveInteger;
import org.exolab.castor.builder.types.XSNormalizedString;
import org.exolab.castor.builder.types.XSPositiveInteger;
import org.exolab.castor.builder.types.XSQName;
import org.exolab.castor.builder.types.XSShort;
import org.exolab.castor.builder.types.XSString;
import org.exolab.castor.builder.types.XSTime;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.builder.types.XSUnsignedByte;
import org.exolab.castor.builder.types.XSUnsignedInt;
import org.exolab.castor.builder.types.XSUnsignedLong;
import org.exolab.castor.builder.types.XSUnsignedShort;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.SimpleTypesFactory;
import org.exolab.castor.xml.schema.Structure;
import org.exolab.castor.xml.schema.Union;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JNaming;
import org.exolab.javasource.JType;

/**
 * A class used to convert XML Schema SimpleTypes into the appropriate XSType.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-01-21 04:43:28 -0700 (Sat, 21 Jan 2006) $
 */
public final class TypeConversion {

    /** Jakarta's common-logging logger. */
    private static final Log LOG = LogFactory.getLog(TypeConversion.class);

    /** Configuration for our source generator. */
    private final BuilderConfiguration _config;

    /**
     * Creates a new TypeConversion instance.
     *
     * @param config the BuilderConfiguration instance (must not be null).
     */
    public TypeConversion(final BuilderConfiguration config) {
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
     * @param useJava50 true if source code is to be generated for Java 5
     * @return the XSType which represets the given Simpletype
     */
    public XSType convertType(final SimpleType simpleType, final boolean useJava50) {
        return convertType(simpleType, null, useJava50);
    }

    /**
     * Converts the given Simpletype to the appropriate XSType.
     *
     * @param simpleType the SimpleType to convert to an XSType instance
     * @param packageName the packageName for any new class types
     * @param useJava50 true if source code is to be generated for Java 5
     * @return the XSType which represets the given Simpletype
     */
    public XSType convertType(final SimpleType simpleType, final String packageName,
                              final boolean useJava50) {
         return convertType(simpleType, packageName, _config.usePrimitiveWrapper(), useJava50, null);
    }

    /**
     * Converts the given Simpletype to the appropriate XSType.
     *
     * @param simpleType the SimpleType to convert to an XSType instance
     * @param packageName the packageName for any new class types
     * @param useWrapper a boolean that when true indicates that primitive
     *        wrappers be used instead of the actual primitives (e.g.
     *        java.lang.Integer instead of int)
     * @param useJava50 true if source code is to be generated for Java 5
     * @param javaClassBindingName valid java Class Name specified by corresponding 
     *        binding component
     * @return the XSType which represets the given Simpletype
     */
    public XSType convertType(final SimpleType simpleType, final String packageName,
            final boolean useWrapper, final boolean useJava50, 
            final String javaClassBindingName) {
        if (simpleType == null) {
            return null;
        }

        XSType xsType = null;
        //-- determine base type
        SimpleType base = simpleType;

        while ((base != null) && (!base.isBuiltInType())) {
            base = (SimpleType) base.getBaseType();
        }

        // try to find a common type for UNIONs, and use it; if not,
        // use 'java.lang.Object' instead.
        if (simpleType.getStructureType() == Structure.UNION) {
            return convertUnion(simpleType, packageName, useWrapper, useJava50);
        } else if (base == null) {
            String className = _config.getJavaNaming().toJavaClassName(simpleType.getName());
            return new XSClass(new JClass(className));
        }

        xsType = findXSTypeForEnumeration(simpleType, packageName, javaClassBindingName);
        if (xsType != null) {
            return xsType;
        }

        // If we don't have the XSType yet, we have to look at the Type Code

        switch (base.getTypeCode()) {
            case SimpleTypesFactory.ID_TYPE:             //-- ID
                return new XSId();
            case SimpleTypesFactory.IDREF_TYPE:          //-- IDREF
                return new XSIdRef();
            case SimpleTypesFactory.IDREFS_TYPE:         //-- IDREFS
                return new XSIdRefs(SourceGeneratorConstants.FIELD_INFO_VECTOR, useJava50);
            case SimpleTypesFactory.NMTOKEN_TYPE:        //-- NMTOKEN
                XSNMToken xsNMToken = new XSNMToken();
                xsNMToken.setFacets(simpleType);
                return xsNMToken;
            case SimpleTypesFactory.NMTOKENS_TYPE:       //-- NMTOKENS
                return new XSNMTokens(SourceGeneratorConstants.FIELD_INFO_VECTOR, useJava50);
            case SimpleTypesFactory.ANYURI_TYPE:         //--AnyURI
                return new XSAnyURI();
            case SimpleTypesFactory.BASE64BINARY_TYPE:   //-- base64Bbinary
                return new XSBase64Binary(useJava50);
            case SimpleTypesFactory.HEXBINARY_TYPE:      //-- hexBinary
                return new XSHexBinary(useJava50);
            case SimpleTypesFactory.BOOLEAN_TYPE:        //-- boolean
                return new XSBoolean(useWrapper);
            case SimpleTypesFactory.BYTE_TYPE:           //--byte
                XSByte xsByte = new XSByte(useWrapper);
                if (!simpleType.isBuiltInType()) {
                    xsByte.setFacets(simpleType);
                }
                return xsByte;
            case SimpleTypesFactory.DATE_TYPE:           //-- date
                XSDate xsDate = new XSDate();
                if (!simpleType.isBuiltInType()) {
                    xsDate.setFacets(simpleType);
                }
                return xsDate;
            case SimpleTypesFactory.DATETIME_TYPE:       //-- dateTime
                XSDateTime xsDateTime = new XSDateTime();
                if (!simpleType.isBuiltInType()) {
                    xsDateTime.setFacets(simpleType);
                }
                return xsDateTime;
            case SimpleTypesFactory.DOUBLE_TYPE:         //-- double
                XSDouble xsDouble = new XSDouble(useWrapper);
                if (!simpleType.isBuiltInType()) {
                    xsDouble.setFacets(simpleType);
                }
                return xsDouble;
            case SimpleTypesFactory.DURATION_TYPE:       //-- duration
                XSDuration xsDuration = new XSDuration();
                if (!simpleType.isBuiltInType()) {
                    xsDuration.setFacets(simpleType);
                }
                return xsDuration;
            case SimpleTypesFactory.DECIMAL_TYPE:        //-- decimal
                XSDecimal xsDecimal = new XSDecimal();
                if (!simpleType.isBuiltInType()) {
                    xsDecimal.setFacets(simpleType);
                }
                return xsDecimal;
            case SimpleTypesFactory.FLOAT_TYPE:          //-- float
                XSFloat xsFloat = new XSFloat(useWrapper);
                if (!simpleType.isBuiltInType()) {
                    xsFloat.setFacets(simpleType);
                }
                return xsFloat;
            case SimpleTypesFactory.GDAY_TYPE:           //--GDay
                XSGDay xsGDay = new XSGDay();
                if (!simpleType.isBuiltInType()) {
                    xsGDay.setFacets(simpleType);
                }
                return xsGDay;
            case SimpleTypesFactory.GMONTHDAY_TYPE:      //--GMonthDay
                XSGMonthDay xsGMonthDay = new XSGMonthDay();
                if (!simpleType.isBuiltInType()) {
                    xsGMonthDay.setFacets(simpleType);
                }
                return xsGMonthDay;
            case SimpleTypesFactory.GMONTH_TYPE:         //--GMonth
                XSGMonth xsGMonth = new XSGMonth();
                if (!simpleType.isBuiltInType()) {
                    xsGMonth.setFacets(simpleType);
                }
                return xsGMonth;
            case SimpleTypesFactory.GYEARMONTH_TYPE:     //--GYearMonth
                XSGYearMonth xsGYearMonth = new XSGYearMonth();
                if (!simpleType.isBuiltInType()) {
                    xsGYearMonth.setFacets(simpleType);
                }
                return xsGYearMonth;
            case SimpleTypesFactory.GYEAR_TYPE:          //--GYear
                XSGYear xsGYear = new XSGYear();
                if (!simpleType.isBuiltInType()) {
                    xsGYear.setFacets(simpleType);
                }
                return xsGYear;
            case SimpleTypesFactory.INTEGER_TYPE:        //-- integer
                XSInteger xsInteger = new XSInteger(useWrapper);
                if (!simpleType.isBuiltInType()) {
                    xsInteger.setFacets(simpleType);
                }
                return xsInteger;
            case SimpleTypesFactory.INT_TYPE:            //-- int
                XSInt xsInt = new XSInt(useWrapper);
                if (!simpleType.isBuiltInType()) {
                    xsInt.setFacets(simpleType);
                }
                return xsInt;
            case SimpleTypesFactory.LANGUAGE_TYPE:       //-- Language
                //-- since we don't actually support this type, yet, we'll simply treat
                //-- it as a string, but warn the user.
                LOG.warn("Warning: Currently, the W3C datatype '" + simpleType.getName()
                        + "' is supported only as a String by Castor Source Generator.");
                return new XSString();
            case SimpleTypesFactory.LONG_TYPE:           //-- long
                XSLong xsLong = new XSLong(useWrapper);
                if (!simpleType.isBuiltInType()) {
                    xsLong.setFacets(simpleType);
                }
                return xsLong;
            case SimpleTypesFactory.NCNAME_TYPE:         //--NCName
                return new XSNCName();
            case SimpleTypesFactory.NON_POSITIVE_INTEGER_TYPE: //-- nonPositiveInteger
                XSNonPositiveInteger xsNPInteger = new XSNonPositiveInteger(useWrapper);
                xsNPInteger.setFacets(simpleType);
                return xsNPInteger;
            case SimpleTypesFactory.NON_NEGATIVE_INTEGER_TYPE: //-- nonNegativeInteger
                XSNonNegativeInteger xsNNInteger = new XSNonNegativeInteger(useWrapper);
                xsNNInteger.setFacets(simpleType);
                return xsNNInteger;
            case SimpleTypesFactory.NEGATIVE_INTEGER_TYPE:     //-- negative-integer
                XSNegativeInteger xsNInteger = new XSNegativeInteger(useWrapper);
                xsNInteger.setFacets(simpleType);
                return xsNInteger;
            case SimpleTypesFactory.UNSIGNED_INT_TYPE:     //-- unsigned-integer
                XSUnsignedInt xsUnsignedInt = new XSUnsignedInt(useWrapper);
                xsUnsignedInt.setFacets(simpleType);
                return xsUnsignedInt;
            case SimpleTypesFactory.UNSIGNED_SHORT_TYPE:     //-- unsigned-short
                XSUnsignedShort xsUnsignedShort = new XSUnsignedShort(useWrapper);
                xsUnsignedShort.setFacets(simpleType);
                return xsUnsignedShort;
            case SimpleTypesFactory.UNSIGNED_BYTE_TYPE:     //-- unsigned-byte
                XSUnsignedByte xsUnsignedByte = new XSUnsignedByte(useWrapper);
                xsUnsignedByte.setFacets(simpleType);
                return xsUnsignedByte;
            case SimpleTypesFactory.UNSIGNED_LONG_TYPE:     //-- unsigned-long
                XSUnsignedLong xsUnsignedLong = new XSUnsignedLong();
                xsUnsignedLong.setFacets(simpleType);
                return xsUnsignedLong;
            case SimpleTypesFactory.NORMALIZEDSTRING_TYPE:     //-- normalizedString
                XSNormalizedString xsNormalString = new XSNormalizedString();
                if (!simpleType.isBuiltInType()) {
                    xsNormalString.setFacets(simpleType);
                }
                return xsNormalString;
            case SimpleTypesFactory.POSITIVE_INTEGER_TYPE:     //-- positive-integer
                XSPositiveInteger xsPInteger = new XSPositiveInteger(useWrapper);
                xsPInteger.setFacets(simpleType);
                return xsPInteger;
            case SimpleTypesFactory.QNAME_TYPE:                //-- QName
                XSQName xsQName = new XSQName();
                xsQName.setFacets(simpleType);
                return xsQName;
            case SimpleTypesFactory.STRING_TYPE:               //-- string
                xsType = findXSTypeForEnumeration(simpleType, packageName, javaClassBindingName);
                if (xsType == null) {
                    // Not an enumerated String type
                    XSString xsString = new XSString();
                    if (!simpleType.isBuiltInType()) {
                        xsString.setFacets(simpleType);
                    }
                    xsType = xsString;
                }
                break;
            case SimpleTypesFactory.SHORT_TYPE:               //-- short
                XSShort xsShort = new XSShort(useWrapper);
                if (!simpleType.isBuiltInType()) {
                    xsShort.setFacets(simpleType);
                }
                return xsShort;
            case SimpleTypesFactory.TIME_TYPE:                //-- time
                XSTime xsTime = new XSTime();
                if (!simpleType.isBuiltInType()) {
                    xsTime.setFacets(simpleType);
                }
                return xsTime;
            case SimpleTypesFactory.TOKEN_TYPE:               //-- token
                //-- since we don't actually support this type, yet, we'll simply treat
                //-- it as a string, but warn the user.
                LOG.warn("Warning: Currently, the W3C datatype 'token'"
                        + " is supported only as a String by Castor Source Generator.");
                XSString xsString = new XSString();
                if (!simpleType.isBuiltInType()) {
                    xsString.setFacets(simpleType);
                }
                return xsString;
            default:                                          //-- error
                String name = simpleType.getName();
                if (name == null || name.length() == 0) {
                    //--we know it is a restriction
                    name = simpleType.getBuiltInBaseType().getName();
                }

                LOG.warn("Warning: The W3C datatype '" + name + "' "
                        + "is not currently supported by Castor Source Generator.");
                String className = _config.getJavaNaming().toJavaClassName(name);
                xsType = new XSClass(new JClass(className));
                break;
        }

        return xsType;
    } //-- convertType

    /**
     * Converts the given XML schema Union type to the appropriate XSType.
     *
     * @param simpleType the SimpleType to convert to an XSType instance
     * @param packageName the packageName for any new class types
     * @param useJava50 true if source code is to be generated for Java 5
     * @param useWrapper indicates whether primitive wrappers should be used instead 
     *   of the actual primitives (e.g. java.lang.Integer instead of int)
     * @return the XSType which represets the given Simpletype
     */
    private XSType convertUnion(final SimpleType simpleType, 
            final String packageName, 
            final boolean useWrapper, 
            final boolean useJava50) {
        SimpleType currentUnion = simpleType;
        SimpleType common = findCommonType((Union) currentUnion);
        // look at type hierarchy (if any), and try to 
        // find a common type recursively 
        while (common == null 
                && currentUnion.getBaseType() != null
                && currentUnion.getBaseType().getStructureType() == Structure.UNION)
        {
            currentUnion = (SimpleType) currentUnion.getBaseType();
            common = findCommonType((Union) currentUnion);
        }
        if (common == null) {
            return new XSClass(SGTypes.OBJECT);
        }
        
        XSType convertedType = convertType(common, packageName, useWrapper, useJava50, null);
        Union unionType = (Union) simpleType;
        Enumeration<SimpleType> memberTypes = unionType.getMemberTypes();
        while (memberTypes.hasMoreElements()) {
            SimpleType memberType = memberTypes.nextElement();
            convertedType.setFacets(memberType);
        }
        return convertedType;
    }

    /**
     * Returns the XSType that corresponds to the given javaType.
     * @param javaType name of the Java type for which to look up the XSType
     * @return XSType that corresponds to the given javaType
     */
    public static XSType convertType(final String javaType) {
        if (javaType == null) {
            return null;
        }

        //--Boolean
        if (javaType.equals(TypeNames.BOOLEAN_OBJECT)) {
            return new XSBoolean(true);
        } else if (javaType.equals(TypeNames.BOOLEAN_PRIMITIVE)) {
            return new XSBoolean(false);
        } else if (javaType.equals(TypeNames.BYTE_OBJECT)) {
            return new XSByte(true);
        } else if (javaType.equals(TypeNames.BYTE_PRIMITIVE)) {
            return new XSBoolean(false);
        } else if (javaType.equals(TypeNames.CASTOR_DATE)) {
            return new XSDateTime();
        } else if (javaType.equals(TypeNames.CASTOR_DURATION)) {
            return new XSDuration();
        } else if (javaType.equals(TypeNames.CASTOR_GDAY)) {
            return new XSGDay();
        } else if (javaType.equals(TypeNames.CASTOR_GMONTH)) {
            return new XSGMonth();
        } else if (javaType.equals(TypeNames.CASTOR_GMONTHDAY)) {
            return new XSGMonthDay();
        } else if (javaType.equals(TypeNames.CASTOR_GYEAR)) {
            return new XSGYear();
        } else if (javaType.equals(TypeNames.CASTOR_GYEARMONTH)) {
            return new XSGYearMonth();
        } else if (javaType.equals(TypeNames.CASTOR_TIME)) {
            return new XSTime();
        } else if (javaType.equals(TypeNames.DATE)) {
            return new XSDate();
        } else if (javaType.equals(TypeNames.DECIMAL)) {
            return new XSDecimal();
        } else if (javaType.equals(TypeNames.DOUBLE_OBJECT)) {
            return new XSDouble(true);
        } else if (javaType.equals(TypeNames.DOUBLE_PRIMITIVE)) {
            return new XSDouble(false);
        } else if (javaType.equals(TypeNames.FLOAT_OBJECT)) {
            return new XSFloat(true);
        } else if (javaType.equals(TypeNames.FLOAT_PRIMITIVE)) {
            return new XSDouble(false);
        } else if (javaType.equals(TypeNames.INTEGER)) {
            return new XSInteger(true);
        } else if (javaType.equals(TypeNames.INT)) {
            return new XSInt();
        } else if (javaType.equals(TypeNames.SHORT_OBJECT)) {
            return new XSShort(true);
        } else if (javaType.equals(TypeNames.SHORT_PRIMITIVE)) {
            return new XSShort(false);
        } else if (javaType.equals(TypeNames.STRING)) {
            return new XSString();
        }

        //--no XSType implemented for it we return a XSClass
        return new XSClass(new JClass(javaType));
    }

    /**
     * Returns an XSType for an enumerated type. For non-enumerated types,
     * returns null.
     * 
     * @param simpleType
     *            the SimpleType being inspected
     * @param packageName
     *            current package name
     * @param javaClassBindingName
     *            valid Java Class Name specified by corresponding binding
     *            component
     * @return an XSType for an enumerated type, null for a non-enumerated type.
     */
    private XSType findXSTypeForEnumeration(final SimpleType simpleType, 
            final String packageName, final String javaClassBindingName) {
        if (!simpleType.hasFacet(Facet.ENUMERATION)) {
            return null;
        }

        XSType xsType = null;
        String typeName = simpleType.getName();

        //-- anonymous type
        if (typeName == null) {
            if (javaClassBindingName != null) {
                // handled by binding file
                if (!JNaming.isInJavaLang(javaClassBindingName) 
                        && !JNaming.isReservedByCastor(javaClassBindingName) 
                        && !JNaming.isReservedByWindows(javaClassBindingName)) {
                    typeName = javaClassBindingName;
//                    typeName += "Type";
                }
            }
        }
               
        if (typeName == null) {
            // not handled by binding file
            Structure parent = simpleType.getParent();
            if (parent instanceof ElementDecl) {
                typeName = ((ElementDecl) parent).getName();
            } else if (parent instanceof AttributeDecl) {
                typeName = ((AttributeDecl) parent).getName();
            }
                
            typeName = typeName + "Type";
        }

        String className = _config.getJavaNaming().toJavaClassName(typeName);
        
        // Get the appropriate package name for this type
        String typePackageName = packageName;
        if (typePackageName == null) {
            String ns = simpleType.getSchema().getTargetNamespace();
            typePackageName = _config.lookupPackageByNamespace(ns);
        }

        if (typePackageName != null && typePackageName.length() > 0) {
            typePackageName = typePackageName  + '.' + SourceGeneratorConstants.TYPES_PACKAGE;
        } else {
            typePackageName = SourceGeneratorConstants.TYPES_PACKAGE;
        }

        className = typePackageName  + '.' + className;

        xsType = new XSClass(new JClass(className));
        xsType.setAsEnumerated(true);

        return xsType;
    }

    /**
     * Returns the common type for the Union, or null if no common type exists
     * among the members of the Union.
     *
     * @param union
     *            the Union to return the common type for
     * @return the common SimpleType for the Union.
     */
    private static SimpleType findCommonType(final Union union) {
        SimpleType common = null;
        Enumeration<SimpleType> enumeration = union.getMemberTypes();
        while (enumeration.hasMoreElements()) {
            SimpleType type = enumeration.nextElement();
            type = type.getBuiltInBaseType();
            if (common == null) {
                common = type;
            } else {
                common = findCommonType(common, type);
                //-- no common types
                if (common == null) {
                    break;
                }
            }
        }
        return common;
    } //-- findCommonType

    /**
     * Compares the two SimpleTypes. The lowest common ancestor of both types
     * will be returned, otherwise null is returned if the types are not
     * compatible.
     *
     * @param aType
     *            the first type to compare
     * @param bType
     *            the second type to compare
     * @return the common ancestor of both types if there is one, null if the
     *         types are not compatible.
     */
    private static SimpleType findCommonType(final SimpleType aType, final SimpleType bType) {
        int type1 = aType.getTypeCode();
        int type2 = bType.getTypeCode();

        if (type1 == type2) {
            return aType;
        }

        if (isNumeric(aType) && isNumeric(bType)) {
            // TODO Finish this so we can implement Unions
        } else if (isString(aType) && isString(bType)) {
            // TODO Finish this so we can implement Unions
        }

        //-- Just return string for now, as all simpleTypes can fit into a string
        Schema schema = aType.getSchema();
        return schema.getSimpleType("string", schema.getSchemaNamespace());
    }

    /**
     * Returns true if this simpletype is numeric.
     * @param type the type to be examined
     * @return true if this simpletype is numeric.
     */
    private static boolean isNumeric(final SimpleType type) {
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

    /**
     * Returns true if this simpletype is a String.
     * @param type the type to be examined
     * @return true if this simpletype is String.
     */
    private static boolean isString(final SimpleType type) {
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

    /**
     * Constants.
     */
    protected static class TypeNames {
        protected static final String BOOLEAN_PRIMITIVE = JType.BOOLEAN.getName();
        protected static final String BOOLEAN_OBJECT    = JType.BOOLEAN.getWrapperName();
        protected static final String BYTE_PRIMITIVE    = JType.BYTE.getName();
        protected static final String BYTE_OBJECT       = JType.BYTE.getWrapperName();
        protected static final String DATE              = "java.util.Date";
        protected static final String CASTOR_DATE       = "org.exolab.castor.types.Date";
        protected static final String CASTOR_TIME       = "org.exolab.castor.types.Time";
        protected static final String CASTOR_DURATION   = "org.exolab.castor.types.Duration";
        protected static final String CASTOR_GMONTH     = "org.exolab.castor.types.GMonth";
        protected static final String CASTOR_GMONTHDAY  = "org.exolab.castor.types.GMonthDay";
        protected static final String CASTOR_GYEAR      = "org.exolab.castor.types.GYear";
        protected static final String CASTOR_GYEARMONTH = "org.exolab.castor.types.GYearMonth";
        protected static final String CASTOR_GDAY       = "org.exolab.castor.types.GDay";
        protected static final String DECIMAL           = "java.math.BigDecimal";
        protected static final String DOUBLE_PRIMITIVE  = JType.DOUBLE.getName();
        protected static final String DOUBLE_OBJECT     = JType.DOUBLE.getWrapperName();
        protected static final String FLOAT_PRIMITIVE   = JType.FLOAT.getName();
        protected static final String FLOAT_OBJECT      = JType.FLOAT.getWrapperName();
        protected static final String INT               = JType.INT.getName();
        protected static final String INTEGER           = JType.INT.getWrapperName();
        protected static final String SHORT_PRIMITIVE   = JType.SHORT.getName();
        protected static final String SHORT_OBJECT      = JType.SHORT.getWrapperName();
        protected static final String STRING            = "java.lang.String";
    }

} //-- TypeConversion
