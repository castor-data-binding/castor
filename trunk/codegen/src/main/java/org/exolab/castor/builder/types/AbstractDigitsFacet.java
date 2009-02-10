/*
 * Copyright 2007 Ralf Joachim
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

import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JSourceCode;

/**
 * A base class for types which support the digits, range, whiteSpace and pattern facets.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6662 $ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 * @since 1.1
 */
public abstract class AbstractDigitsFacet extends AbstractRangeFacet {
    //--------------------------------------------------------------------------

    /** If set to true only '0' is allowed for the fractionDigits facet. If set to false
     *  all positive values are allowed for fractionDigits facet. */
    private final boolean _fractionDigitsZeroOnly;
    
    /** Total number of digits. */
    private int  _totalDigits = -1;
    
    /** Total number of fraction digits. */
    private int _fractionDigits = -1;

    //--------------------------------------------------------------------------

    /**
     * No-arg constructor. By default only '0' is supported for the fractionDigits facet.
     */
    protected AbstractDigitsFacet() {
        this(true);
    }
    
    /**
     * Construct a new AbstractDigitsFacet optionally allowing the fractionDigits facet to be set
     * to all positive values.
     *
     * @param fractionDigitsZeroOnly If set to true only '0' is allowed for the fractionDigits
     *        facet. If set to false all positive values are allowed for fractionDigits facet.
     */
    protected AbstractDigitsFacet(final boolean fractionDigitsZeroOnly) {
        _fractionDigitsZeroOnly = fractionDigitsZeroOnly;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Returns the totalDigits facet value of this XSType.
     * 
     * @return The totalDigits facet value of this XSType.
     */
    public final int getTotalDigits() {
        return _totalDigits;
    }

    /**
     * Sets the totalDigits facet for this XSType.
     * 
     * @param totalDigits The value of totalDigits (must be >0).
     */
    public final void setTotalDigits(final int totalDigits) {
        if (totalDigits <= 0) {
            throw new IllegalArgumentException(
                    getName() + ": the totalDigits facet must be positive: " + totalDigits);
        }
        _totalDigits = totalDigits;
    }

    /**
     * Returns the fractionDigits facet value of this XSType.
     * 
     * @return The fractionDigits facet value of this XSType.
     */
    public final int getFractionDigits() {
        return _fractionDigits;
    }

    /**
     * Sets the fractionDigits facet for this XSType.
     * 
     * @param fractionDigits The value of fractionDigits (must be >=0).
     */
    public final void setFractionDigits(final int fractionDigits) {
        if (fractionDigits < 0) {
            throw new IllegalArgumentException(
                    getName() + ": the fractionDigits facet must be positive: " + fractionDigits);
        }
        if (_fractionDigitsZeroOnly && (fractionDigits > 0)) {
            throw new IllegalArgumentException(
                    getName() + ": only '0' allowed for fractionDigits facet: " + fractionDigits);
        }
        _fractionDigits = fractionDigits;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    protected final void setFacet(final Facet facet) {
        super.setFacet(facet);
        setDigitsFacet(facet);
    }

    /**
     * Transfer given facet if it is a digits facet.
     *
     * @param facet The facet to transfer.
     */
    protected final void setDigitsFacet(final Facet facet) {
        String name = facet.getName();
        if (Facet.TOTALDIGITS.equals(name)) {
            setTotalDigits(facet.toInt());
        } else if (Facet.FRACTIONDIGITS.equals(name)) {
            setFractionDigits(facet.toInt());
        }
    }

    /**
     * Generate the source code for digits facet validation.
     *
     * @param jsc The JSourceCode to fill in.
     * @param validatorName The name of the TypeValidator that the digits should be added to.
     */
    protected final void codeDigitsFacet(final JSourceCode jsc, final String validatorName) {
        //-- totalDigits
        if (getTotalDigits() != -1) {
            jsc.add("{0}.setTotalDigits({1});",
                    validatorName, Integer.toString(getTotalDigits()));
        }

        //-- fractionDigits
        if (!_fractionDigitsZeroOnly && (getFractionDigits() != -1)) {
            jsc.add("{0}.setFractionDigits({1});",
                    validatorName, Integer.toString(getFractionDigits()));
        }
    }

    //--------------------------------------------------------------------------
}
