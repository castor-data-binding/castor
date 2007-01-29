/*
 * Copyright 2007 Keith Visco, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.castor.builder.types;

import java.util.Enumeration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The base XML Schema Type class.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public abstract class XSType {
    //--------------------------------------------------------------------------
    
    // special types

    /** Value meaning the type is unassigned. */
    public static final short NULL = -1;

    /** A user-defined type. */
    public static final short CLASS = 0;

    // primitive types
    
    /** xsd:string. */
    public static final short STRING_TYPE = 1;
    
    /** xsd:duration. */
    public static final short DURATION_TYPE = 2;
    
    /** xsd:dateTime. */
    public static final short DATETIME_TYPE = 3;
    
    /** xsd:time. */
    public static final short TIME_TYPE = 4;
    
    /** xsd:date. */
    public static final short DATE_TYPE = 5;
    
    /** xsd:gYearMonth. */
    public static final short GYEARMONTH_TYPE = 6;
    
    /** xsd:gYear. */
    public static final short GYEAR_TYPE = 7;
    
    /** xsd:gMonthDay. */
    public static final short GMONTHDAY_TYPE = 8;
    
    /** xsd:gDay. */
    public static final short GDAY_TYPE = 9;
    
    /** xsd:gMonth. */
    public static final short GMONTH_TYPE = 10;
    
    /** xsd:boolean. */
    public static final short BOOLEAN_TYPE = 11;
    
    /** xsd:base64Binary. */
    public static final short BASE64BINARY_TYPE = 12;
    
    /** xsd:hexBinary. */
    public static final short HEXBINARY_TYPE = 13;
    
    /** xsd:float. */
    public static final short FLOAT_TYPE = 14;
    
    /** xsd:double. */
    public static final short DOUBLE_TYPE = 15;
    
    /** xsd:decimal. */
    public static final short DECIMAL_TYPE = 16;
    
    /** xsd:anyURI. */
    public static final short ANYURI_TYPE = 17;
    
    /** xsd:QName. */
    public static final short QNAME_TYPE = 18;
    
    /** xsd:notation. */
    public static final short NOTATION_TYPE = 19;

    // derived types
    
    /** xsd:normalizedString. */
    public static final short NORMALIZEDSTRING_TYPE = 20;
    
    /** xsd:token. */
    public static final short TOKEN_TYPE = 21;
    
    /** xsd:language. */
    public static final short LANGUAGE_TYPE = 22;
    
    /** xsd:name. */
    public static final short NAME_TYPE = 23;
    
    /** xsd:NCNAME. */
    public static final short NCNAME_TYPE = 24;
    
    /** xsd:ID. */
    public static final short ID_TYPE = 25;
    
    /** xsd:IDREF. */
    public static final short IDREF_TYPE = 26;
    
    /** xsd:IDREFS. */
    public static final short IDREFS_TYPE = 27;
    
    /** xsd:ENTITY. */
    public static final short ENTITY = 28;
    
    /** xsd:ENTITIES. */
    public static final short ENTITIES = 29;
    
    /** xsd:NMTOKEN. */
    public static final short NMTOKEN_TYPE = 30;
    
    /** xsd:NMTOKENS. */
    public static final short NMTOKENS_TYPE = 31;
    
    /** xsd:integer. */
    public static final short INTEGER_TYPE = 32;
    
    /** xsd:nonPositiveInteger. */
    public static final short NON_POSITIVE_INTEGER_TYPE = 33;
    
    /** xsd:negativeInteger. */
    public static final short NEGATIVE_INTEGER_TYPE = 34;
    
    /** xsd:long. */
    public static final short LONG_TYPE = 35;
    
    /** xsd:int. */
    public static final short INT_TYPE = 36;
    
    /** xsd:short. */
    public static final short SHORT_TYPE = 37;
    
    /** xsd:byte. */
    public static final short BYTE_TYPE = 38;
    
    /** xsd:nonNegativeInteger. */
    public static final short NON_NEGATIVE_INTEGER_TYPE = 39;
    
    /** xsd:positiveInteger. */
    public static final short POSITIVE_INTEGER_TYPE = 44;
    
    /** A collection type. */
    public static final short COLLECTION = 45;
    
    /** xsd:unsignedLong. */
    public static final short UNSIGNED_LONG_TYPE = 46;
    
    /** xsd:unsignedShort. */
    public static final short UNSIGNED_SHORT_TYPE = 47;

    /** xsd:unsignedByte. */
    public static final short UNSIGNED_BYTE_TYPE = 48;

    /** xsd:unsignedInt. */
    public static final short UNSIGNED_INT_TYPE = 49;
    
    //--------------------------------------------------------------------------

    /** Flag signaling an enumerated type. */
    private boolean _enumerated = false;

    //--------------------------------------------------------------------------

    /**
     * Returns true if this XSType represents an enumerated type.
     * 
     * @return True if this XSType represents an enumerated type.
     */
    public final boolean isEnumerated() {
        return _enumerated;
    }

    /**
     * Sets the enumerated flag for this XSClass.
     *
     * @param enumerated A boolean indicating whether or not this XSClass represents an
     *        enumerated type.
     */
    public final void setAsEnumerated(final boolean enumerated) {
        _enumerated = enumerated;
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the name of this XSType.
     * 
     * @return The name of this XSType.
     */
    public abstract String getName();

    /**
     * Returns the type of this XSType.
     * 
     * @return the type of this XSType.
     */
    public abstract short getType();

    /**
     * Returns true if this XSType represents a primitive type.
     * 
     * @return True if this XSType represents a primitive type.
     */
    public abstract boolean isPrimitive();
    
    /**
     * Returns true if the XSType represents an XML Schema date/time type.
     * 
     * @return True if the XSType represents an XML Schema date/time type.
     */
    public abstract boolean isDateTime();

    /**
     * Returns true if this XSType represents a collection.
     * 
     * @return True if this XSType represents a collection.
     */
    public boolean isCollection() {
        return false;
    }
    
    /**
     * Returns the JType that this XSType represents.
     * 
     * @return The JType that this XSType represents.
     */
    public abstract JType getJType();

    /**
     * Returns the Java code neccessary to create a new instance of the JType
     * associated with this XSType.
     *
     * @return The Java code neccessary to create a new instance.
     */
    public abstract String newInstanceCode();

    /**
     * Returns the string necessary to convert an instance of this XSType to an
     * Object. This method is really only useful for primitive types.
     *
     * @param variableName The name of the instance variable.
     * @return The String necessary to convert an instance of this XSType to an Object.
     */
    public abstract String createToJavaObjectCode(final String variableName);

    /**
     * Returns the string necessary to convert an Object to an instance of this
     * XSType. This method is really only useful for primitive types.
     *
     * @param variableName The name of the Object.
     * @return The String necessary to convert an Object to an instance of this XSType.
     */
    public abstract String createFromJavaObjectCode(final String variableName);

    //--------------------------------------------------------------------------

    /**
     * Reads and sets the facets for XSType.
     * 
     * @param simpleType The SimpleType containing the facets.
     */
    public final void setFacets(final SimpleType simpleType) {
        Enumeration enumeration = simpleType.getLocalFacets();
        while (enumeration.hasMoreElements()) {
            setFacet((Facet) enumeration.nextElement());
        }
    }

    /**
     * Set the given facet for XSType if applicable.
     * 
     * @param facet The facet to set for XSType.
     */
    protected abstract void setFacet(final Facet facet);
    
    /**
     * Creates the validation code for an instance of this XSType. If necessary the validation
     * code should create a newly configured TypeValidator, that should then be added to a
     * FieldValidator instance whose name is provided.
     *
     * @param jsc The JSourceCode to fill in.
     * @param fixedValue A fixed value to use if any.
     * @param validatorInstanceName The name of the FieldValidator that the configured
     *        TypeValidator should be added to.
     */
    public abstract void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName);

    //--------------------------------------------------------------------------
}
