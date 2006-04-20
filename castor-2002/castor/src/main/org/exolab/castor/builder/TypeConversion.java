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
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.SimpleTypesFactory;

import java.text.ParseException;


/**
 * A class used to convert XML Schema SimpleTypes into
 * the appropriate XSType
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class TypeConversion {


    /**
     * Converts the given Simpletype to the appropriate XSType.
     * @return the XSType which represets the given Simpletype
    **/
    public static XSType convertType(SimpleType simpleType) {
        if (simpleType == null) return null;

        XSType xsType = null;

        //-- enumerated types
        if (simpleType.hasFacet("enumeration")) {
            String className = JavaNaming.toJavaClassName(simpleType.getName());
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
                //--URIREFERENCE
                case SimpleTypesFactory.URIREFERENCE_TYPE:
                    return new XSUriReference();
                //-- NCName
                case SimpleTypesFactory.NCNAME_TYPE:
                    return new XSNCName();
                //-- NMTOKEN
                case SimpleTypesFactory.NMTOKEN_TYPE:
                    return new XSNMToken();
                //-- NMTOKENS
                case SimpleTypesFactory.NMTOKENS_TYPE:
                    return new XSList(new XSNMToken());
                //-- binary
                case SimpleTypesFactory.BINARY_TYPE:
                    return new XSBinary();
                //-- boolean
                case SimpleTypesFactory.BOOLEAN_TYPE:
                    return new XSBoolean();
                //-- century
                case SimpleTypesFactory.CENTURY_TYPE:
                    return new XSCentury();
                //-- CDATA
                case SimpleTypesFactory.CDATA_TYPE:
                {
                    XSCData xsCdata = new XSCData();
                    if (!simpleType.isBuiltInType())
                        xsCdata.setFacets(simpleType);
                    return xsCdata;
                }

                //-- date
                case SimpleTypesFactory.DATE_TYPE:
                    return new XSDate();
                //-- double
                case SimpleTypesFactory.DOUBLE_TYPE:
                 {
                    XSReal xsReal = new XSReal();
                    if (!simpleType.isBuiltInType())
                        xsReal.setFacets(simpleType);
                    return xsReal;
                }

                //-- float
                case SimpleTypesFactory.FLOAT_TYPE:
                {
                    XSFloat xsFloat = new XSFloat();
                    if (!simpleType.isBuiltInType())
                        xsFloat.setFacets(simpleType);
                    return xsFloat;
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
                    String warning = "Warning: The W3C datatype "+simpleType.getName();
                    warning += " is not currently supported by Castor Source Generator.";
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
