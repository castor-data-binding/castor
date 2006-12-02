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
 * Copyright 1999-2002(C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.types;

import java.util.Enumeration;
import java.util.Hashtable;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The base XML Schema Type class
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
  */
public abstract class XSType {

    ////////////primitive types/////////////////////
    /**
     * The name of the string type
     */
    public static final String STRING_NAME = "string";
    /**
     * The name of the boolean type
     */
    public static final String BOOLEAN_NAME = "boolean";
    /**
     * The name of the float type
     */
    public static final String FLOAT_NAME = "float";
    /**
     * The name of the double type
      */
    public static final String DOUBLE_NAME = "double";
    /**
     * The name of the decimal type
      */
    public static final String DECIMAL_NAME = "decimal";
    /**
     * the name of the duration type
     */
    public static final String DURATION_NAME = "duration";
    /**
     * The name of the the dateTime type
     */
    public static final String DATETIME_NAME = "dateTime";
    /**
     * the name of the time type
     */
    public static final String TIME_NAME = "time";
    /**
     * The name of the date type
     */
    public static final String DATE_NAME = "date";
    /**
     * The name of the gYearMonth type.
     */
    public static final String GYEARMONTH_NAME = "gYearMonth";
    /**
     * The name of the gYear type.
     */
    public static final String GYEAR_NAME = "gYear";
    /**
     * The name of the gMonthDay type.
     */
    public static final String GMONTHDAY_NAME  = "gMonthDay";
    /**
     * The name of the gDay type.
     */
    public static final String GDAY_NAME = "gDay";
    /**
     * The name of the gMonth type.
     */
    public static final String GMONTH_NAME = "gMonth";
    /**
     * The name of the hexBinary type
     */
    public static final String HEXBINARY_NAME = "hexBinary";
    /**
     * The name of the base64Binary type
     */
    public static final String BASE64BINARY_NAME = "base64Binary";
    /**
     * the name of the anyURI type.
     */
    public static final String ANYURI_NAME = "anyURI";
    /**
     * the name of the QName type
     */
    public static final String QNAME_NAME = "QName";
    /**
     * the name of the notation type
     */
    public static final String NOTATION_NAME = "NOTATION";

    /////////////derived types//////////////////////

    /**
     * The name of the normalizedString type.
     */
    public static final String NORMALIZEDSTRING_NAME = "normalizedString";
    /**
     * The name of the token type
     */
    public static final String TOKEN_NAME = "token";
    /**
     * The name of the language type.
     */
    public  static final String LANGUAGE_NAME = "language";
    /**
     * The Name of the IDREFS type
     */
    public static final String IDREFS_NAME = "IDREFS";
    /**
     * The name of the NMTOKEN type
      */
    public static final String NMTOKEN_NAME = "NMTOKEN";
    /**
     * The of the NMTOKENS type
     */
    public static String NMTOKENS_NAME  = "NMTOKENS";
    /**
     * The name of the Name type
     */
    public static final String NAME_NAME = "Name";
    /**
     * The name of the NCName type
     */
    public static final String NCNAME_NAME = "NCName";
    /**
     * The name of the ID type
      */
    public static final String ID_NAME = "ID";
    /**
     * The name of the IDREF type
      */
    public static final String IDREF_NAME = "IDREF";
    /**
     * The name of the integer type
      */
    public static final String INTEGER_NAME = "integer";
    /**
     * The name of the non-positive-integer type
     */
    public static final String NON_NEGATIVE_INTEGER_NAME = "nonNegativeInteger";
    /**
     * The name of the non-positive-integer type
     */
    public static final String NON_POSITIVE_INTEGER_NAME = "nonPositiveInteger";
    /**
     * The name of the negative-integer type
      */
    public static final String NEGATIVE_INTEGER_NAME  = "negativeInteger";
    /**
     * The name of the long type
      */
    public static final String LONG_NAME = "long";
    /**
     * The name of the int type
     */
    public static final String INT_NAME = "int";
    /**
     * The name of the short type
     */
    public static final String SHORT_NAME = "short";
    /**
     * The name of the byte type
     */
    public static final String BYTE_NAME = "byte";
    /**
     * The name of the positive-integer type
      */
    public static final String POSITIVE_INTEGER_NAME  = "positiveInteger";
    /**
     * The name of the unsigned-long type
      */
    public static final String UNSIGNED_LONG_NAME  = "unsignedLong";

    public static final short NULL               = -1;

    //-- this type should change to user-defined or
    //-- something like that
    public static final short CLASS              =  0;

    //--
    //Primitive types
    public static final short STRING_TYPE                   =  1;
    public static final short DURATION_TYPE                 =  2;
    public static final short DATETIME_TYPE                 =  3;
    public static final short TIME_TYPE                     =  4;
    public static final short DATE_TYPE                     =  5;
    public static final short GYEARMONTH_TYPE               =  6;
    public static final short GYEAR_TYPE                    =  7;
    public static final short GMONTHDAY_TYPE                =  8;
    public static final short GDAY_TYPE                     =  9;
    public static final short GMONTH_TYPE                   =  10;
    public static final short BOOLEAN_TYPE                  =  11;
    public static final short BASE64BINARY_TYPE             =  12;
    public static final short HEXBINARY_TYPE                =  13;
    public static final short FLOAT_TYPE                    =  14;
    public static final short DOUBLE_TYPE                   =  15;
    public static final short DECIMAL_TYPE                  =  16;
    public static final short ANYURI_TYPE                   =  17;
    public static final short QNAME_TYPE                    =  18;
    public static final short NOTATION_TYPE                 =  19;
    //Derived datatypes
    public static final short NORMALIZEDSTRING_TYPE         = 20;
    public static final short TOKEN_TYPE                    = 21;
    public static final short LANGUAGE_TYPE                 = 22;
    public static final short NAME_TYPE                     = 23;
    public static final short NCNAME_TYPE                   = 24;
    public static final short ID_TYPE                       = 25;
    public static final short IDREF_TYPE                    = 26;
    public static final short IDREFS_TYPE                   = 27;
    public static final short ENTITY                        = 28;
    public static final short ENTITIES                      = 29;
    public static final short NMTOKEN_TYPE                  = 30;
    public static final short NMTOKENS_TYPE                 = 31;
    public static final short INTEGER_TYPE                  = 32;
    public static final short NON_POSITIVE_INTEGER_TYPE     = 33;
    public static final short NEGATIVE_INTEGER_TYPE         = 34;
    public static final short LONG_TYPE                     = 35;
    public static final short INT_TYPE                      = 36;
    public static final short SHORT_TYPE                    = 37;
    public static final short BYTE_TYPE                     = 38;
    public static final short NON_NEGATIVE_INTEGER_TYPE     = 39;
    public static final short POSITIVE_INTEGER_TYPE         = 44;
    //collection type
    public static final short COLLECTION                    = 45;

    public static final short UNSIGNED_LONG_TYPE            = 46;

    private short   type       = NULL;

    /**
     * Flag signaling an enumerated type
      */
    private boolean enumerated = false;

    /**
     * Creates a new XSType of the given type
      */
    protected XSType(short type) {
        this.type = type;
    } //-- XSType

    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
      */
    public abstract JType getJType();

    /**
     * Reads and sets the facets for XSType
     * @param simpleType the SimpleType containing the facets
     */
    public abstract void setFacets(SimpleType simpleType);

    /**
     * Returns the type of this XSType
     * @return the type of this XSType
      */
    public short getType() {
        return this.type;
    } //-- getType

    /**
     * Returns a list of Facets from the simpleType
     * (duplicate facets due to extension are filtered out)
     * @param simpleType the Simpletype we want the facets for
     * @return Unique list of facets from the simple type
     */
    protected static Enumeration getFacets(SimpleType simpleType) {
        Hashtable hashTable = new Hashtable();
        Enumeration enumeration = simpleType.getFacets();
        while (enumeration.hasMoreElements()) {

            Facet facet = (Facet)enumeration.nextElement();
            String name = facet.getName();
            hashTable.put(name, facet);
        }
        return hashTable.elements();
    }

    /**
     * Returns the String necessary to convert an instance of this XSType
     * to an Object. This method is really only useful for primitive types
     * @param variableName the name of the instance variable
     * @return the String necessary to convert an instance of this XSType
     * to an Object
      */
    public String createToJavaObjectCode(String variableName) {
        return variableName;
    } //-- toJavaObject

    /**
     * Returns the Java code neccessary to create a new instance of the
     * JType associated with this XSType
     */
    public String newInstanceCode() {
        return "new "+getJType().getName()+"();";
    } //-- newInstanceCode

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
      */
    public String createFromJavaObjectCode(String variableName) {
        StringBuffer sb = new StringBuffer();

        JType jType = getJType();
        if (jType != null) {
            sb.append('(');
            sb.append(jType.toString());
            sb.append(") ");
        }
        sb.append(variableName);
        return sb.toString();
    } //-- fromJavaObject

    /**
     * Returns true if this XSType represents an enumerated type
     * @return true if this XSType represents an enumerated type
      */
    public boolean isEnumerated() {
        return enumerated;
    } //-- isEnumerated

    public boolean isPrimitive() {
        switch (type) {
            case BOOLEAN_TYPE:
            case BYTE_TYPE:
            case DOUBLE_TYPE:
            case FLOAT_TYPE:
            case INTEGER_TYPE:
            case LONG_TYPE:
            case NON_NEGATIVE_INTEGER_TYPE:
            case NON_POSITIVE_INTEGER_TYPE:
            case NEGATIVE_INTEGER_TYPE:
            case POSITIVE_INTEGER_TYPE:
            case SHORT_TYPE:
            case INT_TYPE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns true if the XSType represents an
     * XML Schema date/time type
     * @return true if the XSType represents an
     * XML Schema date/time type
     */
    public boolean isDateTime() {
        switch (type) {
            case DATETIME_TYPE:
            case DURATION_TYPE:
            case DATE_TYPE:
            case GDAY_TYPE:
            case GMONTHDAY_TYPE:
            case GMONTH_TYPE:
            case GYEARMONTH_TYPE:
            case GYEAR_TYPE:
            case TIME_TYPE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns the name of this XSType
     * @return the name of this XSType
      */
    public String getName() {
        switch (type) {
            case STRING_TYPE:
                return STRING_NAME;
            case  DURATION_TYPE:
                return DURATION_NAME;
            case DATETIME_TYPE:
                return DATETIME_NAME;
            case TIME_TYPE:
                return TIME_NAME;
            case DATE_TYPE:
                return DATE_NAME;
            case GYEARMONTH_TYPE:
                return GYEARMONTH_NAME;
            case GYEAR_TYPE:
                return GYEAR_NAME;
            case GMONTHDAY_TYPE:
                return GMONTHDAY_NAME;
            case GDAY_TYPE:
                return GDAY_NAME;
            case GMONTH_TYPE:
                return GMONTH_NAME;
            case BOOLEAN_TYPE:
                return BOOLEAN_NAME;
            case BASE64BINARY_TYPE:
                return BASE64BINARY_NAME;
            case HEXBINARY_TYPE:
                return HEXBINARY_NAME;
            case FLOAT_TYPE:
                return FLOAT_NAME;
            case DOUBLE_TYPE:
                return DOUBLE_NAME;
            case DECIMAL_TYPE:
                return DECIMAL_NAME;
            case ANYURI_TYPE:
                return ANYURI_NAME;
            case QNAME_TYPE:
                return QNAME_NAME;
            case NORMALIZEDSTRING_TYPE:
                return NORMALIZEDSTRING_NAME;
            case TOKEN_TYPE:
                return TOKEN_NAME;
            case LANGUAGE_TYPE:
                return LANGUAGE_NAME;
            case NAME_TYPE:
                return NAME_NAME;
            case NCNAME_TYPE:
                return NCNAME_NAME;
            case ID_TYPE:
                return ID_NAME;
            case IDREF_TYPE:
                return IDREF_NAME;
            case IDREFS_TYPE:
                return IDREFS_NAME;
            case NMTOKENS_TYPE:
                return NMTOKENS_NAME;
            case NMTOKEN_TYPE:
                return NMTOKEN_NAME;
            case INTEGER_TYPE:
                return INTEGER_NAME;
            case NON_POSITIVE_INTEGER_TYPE:
                return NON_POSITIVE_INTEGER_NAME;
            case NEGATIVE_INTEGER_TYPE:
                return NEGATIVE_INTEGER_NAME;
            case LONG_TYPE:
                return LONG_NAME;
            case INT_TYPE:
                return INTEGER_NAME;
            case SHORT_TYPE:
                return SHORT_NAME;
            case BYTE_TYPE:
                return BYTE_NAME;
            case NON_NEGATIVE_INTEGER_TYPE:
                return NON_NEGATIVE_INTEGER_NAME;
            case POSITIVE_INTEGER_TYPE:
                return POSITIVE_INTEGER_NAME;
            case UNSIGNED_LONG_TYPE:
                return UNSIGNED_LONG_NAME;
            case COLLECTION:
                short type = ((XSList)this).getContentType().getType();
                if (type == NMTOKEN_TYPE)
                    return NMTOKENS_NAME;
                else if (type == IDREF_TYPE)
                    return IDREFS_NAME;
            default:
                return null;
        }
    } //-- getName

    /**
     * Sets the enumerated flag for this XSClass
     * @param enumerated a boolean indicating whether or not this XSClass
     * represents an enumerated type
      */
    public void setAsEnumerated(boolean enumerated) {
        this.enumerated = enumerated;
    } //-- setAsEnumerated


    /**
     * Creates the validation code for an instance of this XSType. The validation
     * code should if necessary create a newly configured TypeValidator, that
     * should then be added to a FieldValidator instance whose name is provided.
     *
     * @param fixedValue a fixed value to use if any
     * @param jsc the JSourceCode to fill in.
     * @param fieldValidatorInstanceName the name of the FieldValidator
     * that the configured TypeValidator should be added to.
     */
    public abstract void validationCode (JSourceCode jsc, String fixedValue, String fieldValidatorInstanceName);

    /**
     * Escapes special characters in the given String so that it can
     * be printed correctly.
     *
     * @param str the String to escape
     * @return the escaped String, or null if the given String was null.
      */
    protected static String escapePattern(String str) {
        if (str == null) return str;

        //-- make sure we have characters to escape
        if (str.indexOf('\\') < 0 && str.indexOf('\"') < 0) return str;

        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '\\') sb.append(ch);
            if (ch == '\"') sb.append('\\');
            sb.append(ch);
        }
        return sb.toString();
    } //-- escape

} //-- XSType
