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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.schema;

import java.math.BigDecimal;
import java.util.Enumeration;

import org.exolab.castor.xml.ValidationException;

/**
 * Represents the base type for XML Schema Facets
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
**/
public class Facet extends Annotated {
    /** SerialVersionUID */
    private static final long serialVersionUID = 7821829275720939922L;

    public static final String ENUMERATION      = "enumeration";
    public static final String LENGTH           = "length";
    public static final String PATTERN          = "pattern";
    public static final String PRECISION        = "precision";
    public static final String MAX_EXCLUSIVE    = "maxExclusive";
    public static final String MAX_INCLUSIVE    = "maxInclusive";
    public static final String MIN_EXCLUSIVE    = "minExclusive";
    public static final String MIN_INCLUSIVE    = "minInclusive";
    public static final String MAX_LENGTH       = "maxLength";
    public static final String MIN_LENGTH       = "minLength";
    public static final String WHITESPACE       = "whiteSpace";
    public static final String TOTALDIGITS      = "totalDigits";
    public static final String FRACTIONDIGITS   = "fractionDigits";

    public static final String WHITESPACE_PRESERVE  = "preserve";
    public static final String WHITESPACE_REPLACE   = "replace";
    public static final String WHITESPACE_COLLAPSE  = "collapse";

    private static final String CLASSNAME = Facet.class.getName();

    private static final String NULL_ARGUMENT =
        "A null argument was passed to " + CLASSNAME + "#";

    private static final String ZERO_LENGTH_STRING =
        "A zero-length String was passed to " + CLASSNAME + "#";

    /** The name of this Facet. */
    private final String _name;
    /** The character value of this Facet. */
    private final String  _value;

    /**
     * The owning {@link SimpleType} instance. 
     */
    private SimpleType _owningType;

    /**
     * Creates a new Facet with the given name.
     * @param name the name of the Facet
     * @param value the value of the Facet
    **/
    public Facet(final String name, final String value) {
        if (name == null) {
            String err = NULL_ARGUMENT;
            err += "Facet: 'name' and 'value' must not be null.";
            throw new IllegalArgumentException(err);
        }
        if (name.length() == 0) {
            String err = ZERO_LENGTH_STRING;
            err += "Facet: 'name' and 'value' must not be zero-length.";
            throw new IllegalArgumentException(err);
        }
        this._name  = name;
        this._value = value;
    }

    /**
     * Returns the name of this Facet.
     * @return the name of this Facet
    **/
    public String getName() {
        return _name;
    }

    /**
     * Returns the character (String) representation of this facet.
     * @return the value of this facet
    **/
    public String getValue() {
        return this._value;
    } //-- getValue

    /**
     * Returns true if this Facet can occur more than once, such
     * as the "enumeration" facet.
     * @return true if this Facet can occur more than once.
    **/
    public boolean isMultivalued() {
        return _name.equals(Facet.ENUMERATION) || _name.equals(Facet.PATTERN);
    }

    /**
     * Returns an int representation of the value of this facet.
     * @return an int representation of the value of this facet
     * @throws NumberFormatException if the value fails to parse as a int.
    **/
    public int toInt() throws NumberFormatException {
        return Integer.parseInt(_value);
    }

    /**
     * Returns a long representation of the value of this facet.
     * @return a long representation of the value of this facet
     * @throws NumberFormatException if the value fails to parse as a long.
    **/
    public long toLong() throws NumberFormatException {
        return Long.parseLong(_value);
    }

    /**
     * Returns an short representation of the value of this facet.
     * @return an short representation of the value of this facet
     * @throws NumberFormatException if the value fails to parse as a short.
    **/
    public short toShort() throws NumberFormatException {
        return Short.parseShort(_value);
    }

    /**
     * Returns a double representation of the value of this facet.
     * @return a double representation of the value of this facet
     * @throws NumberFormatException if the value fails to parse as a float.
     */
    public float toFloat() throws NumberFormatException {
        if (_value.equals("INF")) {
            return Float.POSITIVE_INFINITY;
        }
        if (_value.equals("-INF")) {
            return Float.NEGATIVE_INFINITY;
        }
        return Float.valueOf(_value).floatValue();
    }

    /**
     * Returns a double representation of the value of this facet.
     * @return a double representation of the value of this facet
     * @throws NumberFormatException if the value fails to parse as a double.
    **/
    public double toDouble() throws NumberFormatException {
        return Double.valueOf(_value).doubleValue();
    }

    /**
     * Returns a byte representation of the value of this facet.
     * @return a byte representation of the value of this facet
     * @throws NumberFormatException if the value fails to parse as a byte.
    **/
    public byte toByte() throws NumberFormatException {
        return Byte.parseByte(_value);
    }

    /**
     * Returns a {@link BigDecimal} representation of the value of this facet.
     * @return a {@link BigDecimal} representation of the value of this facet
     * @throws NumberFormatException if the value cannot be parsed as number
     */
    public BigDecimal toBigDecimal() throws NumberFormatException {
        return new BigDecimal(_value);
    }

    /**
     * Returns the type of this Schema Structure.
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.FACET;
    }

    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate() throws ValidationException {
        // TODO: shouldn't this be converted to an abstract method ? 
         //-- do nothing for now
    }

    /**
     * Checks whether the current facet overrides a facet of the base data type.
     * This does generally happen when a data type is derived by restriction
     * and it therefore has facet(s), which are more restrictive than the ones
     * of the base data type.
     *
     * <p>This method is used for merging facets of the base and derived types,
     * in order to create an effective set of facets for the derived type.
     *
     * <p>It's important to note that this method does not perform any validity
     * checks. Validation must be generally performed <b>before</b> trying
     * to merge facets of the base and derived types.
     *
     * @param baseFacet a facet of the base data type
     * @return <code>true</code>,
     *         if the current facet overrides <code>baseFacet</code>;
     *         <code>false</code>, otherwise.
     * 
     * @see #checkConstraints(Enumeration,Enumeration)
     * @see SimpleType#getEffectiveFacets()
     */
    public boolean overridesBase(final Facet baseFacet) {
        return getName().equals(baseFacet.getName());
    }

    /**
     * Checks the constraints on the current facet against
     * the other local facets of the same derived data type
     * and facets of the base data type.
     * Validation is performed according to the rules defined in
     * "<a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Part 2:
     * Datatypes Second Edition</a>" document.
     *
     * @param localFacets local facets of the data type
     * @param baseFacets  merged facets of the base data type
     * @throws SchemaException when the current facet does not satisfy
     *                         schema component validation constraints
     */
    public void checkConstraints(
            final Enumeration localFacets, final Enumeration baseFacets)
            throws SchemaException {
        // Does nothing by default
    }

    /**
     * Sets the owning {@link SimpleType} instance.
     * @param owningType The owning {@link SimpleType} instance.
     */
    public void setOwningType(final SimpleType owningType) {
        _owningType = owningType;
    }

    /**
     * Returns the owning {@link SimpleType} instance.
     * @return The owning {@link SimpleType} instance.
     */
    public SimpleType getOwningType() {
        return _owningType;
    }

} //-- Facet
