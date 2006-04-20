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
import org.exolab.castor.types.TimeDuration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.SimpleTypesFactory;
import org.exolab.castor.xml.schema.Structure;

import java.text.ParseException;


/**
 * A class used to convert XML Schema SimpleTypes into
 * the appropriate XSType
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class TypeConversion {

    private static final String TYPES_PACKAGE = "types";
    
    /**
     * Converts the given Simpletype to the appropriate XSType.
     * @return the XSType which represets the given Simpletype
    **/
    public static XSType convertType(SimpleType simpleType) {
        if (simpleType == null) return null;

        XSType xsType = null;
        //-- determine base type
        SimpleType base = simpleType;

        while ((base != null) && (!base.isBuiltInType())) {
            base = (SimpleType)base.getBaseType();
        }
        if (base == null) {
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
                    return new XSBoolean();
                //--byte
                case SimpleTypesFactory.BYTE_TYPE:
                {
                    XSByte xsByte = new XSByte();
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
                    XSDouble xsDouble = new XSDouble();
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
                    XSFloat xsFloat = new XSFloat();
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
                    XSLong xsLong = new XSLong();
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
                    XSInteger xsInteger = new XSNonPositiveInteger();
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }

                //-- nonNegativeInteger
                case SimpleTypesFactory.NON_NEGATIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSNonNegativeInteger();
                    xsInteger.setFacets(simpleType);
                    return xsInteger;
                }

                //-- negative-integer
                case SimpleTypesFactory.NEGATIVE_INTEGER_TYPE:
                {
                    XSInteger xsInteger = new XSNegativeInteger();
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
                    XSInteger xsInteger = new XSPositiveInteger();
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
                            else {
                                typeName = ((AttributeDecl)parent).getName();
                            }
                            typeName = typeName + "Type";
                        }
                        String className
                            = JavaNaming.toJavaClassName(typeName);
                        
                        String ns = simpleType.getSchema().getTargetNamespace();
                        String pkgName = SourceGenerator.getJavaPackage(ns);
                        if ((pkgName != null) && (pkgName.length() > 0))
                            pkgName = pkgName + '.' + TYPES_PACKAGE;
                        else
                            pkgName = TYPES_PACKAGE;
                        
                        className = pkgName + '.' + className;
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
					XSShort xsShort = new XSShort();
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
                    String warning = "Warning: The W3C datatype '"+simpleType.getName();
                    warning += "' is not currently supported by Castor Source Generator.";
                    System.out.println(warning);
                    String className
                        = JavaNaming.toJavaClassName(simpleType.getName());
                    xsType = new XSClass(new JClass(className));
                    break;
            }
        }
        return xsType;

    } //-- convertType

} //-- TypeConversion
