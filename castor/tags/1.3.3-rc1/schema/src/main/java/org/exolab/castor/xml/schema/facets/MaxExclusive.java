/*
 * Copyright 2008 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml.schema.facets;

import java.util.Enumeration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SchemaException;

/**
 * An implementation of <b>maxExclusive</b> constraining facet, defined in section
 * "<a href="http://www.w3.org/TR/xmlschema-2/#rf-maxExclusive">4.3.8 maxExclusive</a>"
 * of "<a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Part 2:
 * Datatypes Second Edition</a>" document.
 *
 * <p>[Definition:] <b>maxExclusive</b> is the <i>exclusive upper bound</i>
 * of the <i>value space</i> for a datatype with the <i>ordered</i> property.
 * The value of <b>maxExclusive</b> <i>must</i> be in the <i>value space</i>
 * of the <i>base type</i> or be equal to {value} in {base type definition}.
 *
 * @author <a href="mailto:sergei.ivanov@mail.ru">Sergei Ivanov</a>
 * @version $Revision: 6465 $ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public final class MaxExclusive extends Facet {
    
    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 2343915377123869053L;

    /**
     * Creates an instance of this class.
     * @param value A value for this {@link Facet}.
     */
    public MaxExclusive(final String value) {
        super(Facet.MAX_EXCLUSIVE, value);
    }

    /**
     * Checks whether the current facet overrides a facet of the base data type.
     *
     * <p><i>maxExclusive</i> can override the following facets
     * of the base data type:
     *
     * <ul>
     *   <li><i>maxExclusive</i></li>
     *   <li>or <i>maxInclusive</i></li>
     * </ul>
     *
     * @param baseFacet a facet of the base data type
     * @return <code>true</code>,
     *   if the current facet overrides <code>baseFacet</code>;
     *   <code>false</code>, otherwise.
     * @see #checkConstraints(Enumeration, Enumeration)
     */
    public boolean overridesBase(final Facet baseFacet) {
        final String otherName = baseFacet.getName();
        return otherName.equals(Facet.MAX_EXCLUSIVE)
               || otherName.equals(Facet.MAX_INCLUSIVE);
    }

    /**
     * Validation is performed according to section
     * "<a href="http://www.w3.org/TR/xmlschema-2/#maxExclusive-coss">4.3.8.4
     * Constraints on maxExclusive Schema Components</a>"
     * of "<a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Part 2:
     * Datatypes Second Edition</a>" document.
     *
     * @param localFacets local facets of the data type
     * @param baseFacets merged facets of the base data type
     * @throws SchemaException when the current facet does not satisfy
     *   schema component validation constraints
     */
    public void checkConstraints(
            final Enumeration localFacets, final Enumeration baseFacets)
            throws SchemaException {

        while (localFacets.hasMoreElements()) {
            final Facet other = (Facet) localFacets.nextElement();
            if (this == other) {
                continue; // do not check against self
            }
            final String otherName = other.getName();
            if (otherName.equals(Facet.MAX_INCLUSIVE)) {
                // Schema Component Constraint: maxInclusive and maxExclusive
                throw new SchemaException(
                        "It is an error for both maxInclusive and maxExclusive "
                        + "to be specified in the same derivation step "
                        + "of a datatype definition.");
            } else if (otherName.equals(Facet.MIN_EXCLUSIVE)
                     && other.toBigDecimal().compareTo(this.toBigDecimal()) > 0) {
                // Schema Component Constraint: minExclusive <= maxExclusive
                throw new SchemaException(
                        "It is an error for the value specified "
                        + "for minExclusive to be greater than the value "
                        + "specified for maxExclusive for the same datatype.");
            }
        }
        if (baseFacets != null) {
            while (baseFacets.hasMoreElements()) {
                final Facet other = (Facet) baseFacets.nextElement();
                final String otherName = other.getName();

                // Schema Component Constraint: maxExclusive valid restriction
                //   It is an error if any of the following conditions is true:
                if (otherName.equals(Facet.MAX_EXCLUSIVE)
                        && getOwningType().isNumericType()
                        && this.toBigDecimal().compareTo(other.toBigDecimal()) > 0) {
                    // [1]
                    throw new SchemaException(
                            "It is an error if the following condition is true: "
                            + "maxExclusive is among the members of {facets} "
                            + "of {base type definition} and {value} is greater than "
                            + "the {value} of the parent maxExclusive.");
                } else if (otherName.equals(Facet.MAX_INCLUSIVE)
                        && getOwningType().isNumericType()
                        && this.toBigDecimal().compareTo(other.toBigDecimal()) > 0) {
                    // [2]
                    throw new SchemaException(
                            "It is an error if the following condition is true: "
                            + "maxInclusive is among the members of {facets} "
                            + "of {base type definition} and {value} is greater than "
                            + "the {value} of the parent maxInclusive.");
                } else if (otherName.equals(Facet.MIN_INCLUSIVE)
                        && getOwningType().isNumericType()
                        && this.toBigDecimal().compareTo(other.toBigDecimal()) <= 0) {
                    // [3]
                    throw new SchemaException(
                            "It is an error if the following condition is true: "
                            + "minInclusive is among the members of {facets} "
                            + "of {base type definition} and {value} is less than "
                            + "or equal to the {value} of the parent minInclusive.");
                } else if (otherName.equals(Facet.MIN_EXCLUSIVE)
                        && getOwningType().isNumericType()
                        && this.toBigDecimal().compareTo(other.toBigDecimal()) <= 0) {
                    // [4]
                    throw new SchemaException(
                            "It is an error if the following condition is true: "
                            + "minExclusive is among the members of {facets} "
                            + "of {base type definition} and {value} is less than "
                            + "or equal to the {value} of the parent minExclusive.");
                }
            }
        }
    }
}
