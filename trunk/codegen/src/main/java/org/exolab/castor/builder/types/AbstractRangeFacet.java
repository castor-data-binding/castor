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
 * A base class for types which support the range, whiteSpace and pattern facets.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6623 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 * @since 1.1
 */
public abstract class AbstractRangeFacet extends AbstractWhiteSpaceFacet {
    //--------------------------------------------------------------------------

    /** Maximum Date (exclusive). */
    private String _maxExclusive;
    
    /** Maximum Date (inclusive). */
    private String _maxInclusive;
    
    /** Minimum Date (exclusive). */
    private String _minExclusive;

    /** Minimum Date (inclusive). */
    private String _minInclusive;
    
    //--------------------------------------------------------------------------

    /**
     * Returns true if a maximum (inclusive or exclusive) has been set.
     * 
     * @return True if a maximum (inclusive or exclusive) has been set.
     */
    public final boolean hasMaximum() {
        return (_maxInclusive != null) || (_maxExclusive != null);
    }

    /**
     * Returns the maximum exclusive value that this XSDate can hold.
     * 
     * @return The maximum exclusive value that this XSDate can hold. If
     *         no maximum exclusive value has been set, Null will be returned.
     */
    public final String getMaxExclusive() {
        return _maxExclusive;
    }

    /**
     * Sets the maximum exclusive value that this XSDate can hold.
     * 
     * @param max The maximum exclusive value this XSDate can be.
     */
    public final void setMaxExclusive(final String max) {
        _maxExclusive = max;
        _maxInclusive = null;
    }

    /**
     * Returns the maximum inclusive value that this XSDate can hold.
     * 
     * @return The maximum inclusive value that this XSDate can hold. If
     *         no maximum inclusive value has been set, Null will be returned.
     */
    public final String getMaxInclusive() {
        return _maxInclusive;
    }

    /**
     * Sets the maximum inclusive value that this XSDate can hold.
     * 
     * @param max The maximum inclusive value this XSDate can be.
     */
    public final void setMaxInclusive(final String max) {
        _maxInclusive = max;
        _maxExclusive = null;
    }

    /**
     * Returns true if a minimum (inclusive or exclusive) has been set.
     * 
     * @return True if a minimum (inclusive or exclusive) has been set.
     */
    public final boolean hasMinimum() {
        return (_minInclusive != null) || (_minExclusive != null);
    }

    /**
     * Returns the minimum exclusive value that this XSDate can hold.
     * 
     * @return The minimum exclusive value that this XSDate can hold. If
     *         no minimum exclusive value has been set, Null will be returned.
     */
    public final String getMinExclusive() {
        return _minExclusive;
    }

    /**
     * Sets the minimum exclusive value that this XSDate can hold.
     * 
     * @param min The minimum exclusive value this XSDate can be.
     */
    public final void setMinExclusive(final String min) {
        _minExclusive = min;
        _minInclusive = null;
    }

    /**
     * Returns the minimum inclusive value that this XSDate can hold.
     * 
     * @return The minimum inclusive value that this XSDate can be.
     */
    public final String getMinInclusive() {
        return _minInclusive;
    }

    /**
     * Sets the minimum inclusive value that this XSDate can hold.
     * 
     * @param min The minimum inclusive value this XSDate can be.
     */
    public final void setMinInclusive(final String min) {
        _minInclusive = min;
        _minExclusive = null;
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    protected void setFacet(final Facet facet) {
        addPatternFacet(facet);
        setWhiteSpaceFacet(facet);
        setRangeFacet(facet);
    }

    /**
     * Transfer given facet if it is a range.
     *
     * @param facet The facet to transfer.
     */
    protected final void setRangeFacet(final Facet facet) {
        String name = facet.getName();
        if (Facet.MAX_EXCLUSIVE.equals(name)) {
            setMaxExclusive(facet.getValue());
        } else if (Facet.MAX_INCLUSIVE.equals(name)) {
            setMaxInclusive(facet.getValue());
        } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
            setMinExclusive(facet.getValue());
        } else if (Facet.MIN_INCLUSIVE.equals(name)) {
            setMinInclusive(facet.getValue());
        }
    }

    /**
     * Generate the source code for pattern facet validation.
     *
     * @param jsc The JSourceCode to fill in.
     * @param validatorName The name of the TypeValidator that the range should be added to.
     */
    protected final void codeRangeFacet(final JSourceCode jsc, final String validatorName) {
        // maxInclusive / maxExclusive facets (only one or the other, never both)
        if (_maxInclusive != null) {
            jsc.add("{0}.setMaxInclusive(\"{1}\");", validatorName, getMaxInclusive());
        } else if (_maxExclusive != null) {
            jsc.add("{0}.setMaxExclusive(\"{1}\");", validatorName, getMaxExclusive());
        }

        // minInclusive / minExclusive facets (only one or the other, never both)
        if (_minInclusive != null) {
            jsc.add("{0}.setMinInclusive(\"{1}\");", validatorName, getMinInclusive());
        } else if (_minExclusive != null) {
            jsc.add("{0}.setMinExclusive(\"{1}\");", validatorName, getMinExclusive());
        }
    }

    //--------------------------------------------------------------------------
}
