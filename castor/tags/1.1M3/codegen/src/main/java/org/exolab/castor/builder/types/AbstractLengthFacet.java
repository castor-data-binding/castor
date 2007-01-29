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
 * A base class for types which support the length, whiteSpace and pattern facets.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 6662 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 * @since 1.1
 */
public abstract class AbstractLengthFacet extends AbstractWhiteSpaceFacet {
    //--------------------------------------------------------------------------

    /** The length facet. */
    private int _length = 0;
    
    /** The max length facet. */
    private int _maxLength = -1;
    
    /** The min length facet. */
    private int _minLength = 0;
    
    //--------------------------------------------------------------------------

    /**
     * No-arg constructor. By default only 'collapse' is supported for the whiteSpace facet.
     */
    protected AbstractLengthFacet() {
        super(true);
    }
    
    /**
     * Construct a new AbstractXSLengthFacet optionally allowing the whiteSpace facet to be set
     * to 'replace' and 'preserve' values in addition to 'collaps'.
     *
     * @param whiteSpaceCollapseOnly If set to true only 'collapse' is allowed for the whiteSpace
     *        facet. If set to false 'collaps', 'replace' and 'preserve' values are allowed for
     *        whiteSpace facet.
     */
    protected AbstractLengthFacet(final boolean whiteSpaceCollapseOnly) {
        super(whiteSpaceCollapseOnly);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Returns true if a length has been set.
     * 
     * @return True if a length has been set.
     */
    public final boolean hasLength() {
        return (_length > 0);
    }

    /**
     * Returns the length that this type must have.
     * 
     * @return The length that this type must have.
     */
    public final int getLength() {
        return _length;
    }

    /**
     * Sets the length of this type. While setting the length, the maxLength
     * and minLength are also set up to this length.
     *
     * @param length The length to set.
     */
    public final void setLength(final int length) {
        _length = length;
        
        setMaxLength(length);
        setMinLength(length);
    }

    /**
     * Returns true if a maximum length has been set.
     * 
     * @return True if a maximum length has been set.
     */
    public final boolean hasMaxLength() {
        return (_maxLength >= 0);
    }

    /**
     * Returns the maximum length occurances of this type can be. A negative value denotes
     * no maximum length.
     * 
     * @return The maximum length facet.
     */
    public final int getMaxLength() {
        return _maxLength;
    }

    /**
     * Sets the maximum length of this type. To remove the max length facet,
     * use a negative value.
     *
     * @param maxLength The maximum length for occurances of this type.
     */
    public final void setMaxLength(final int maxLength) {
        _maxLength = maxLength;
    }

    /**
     * Returns true if a minimum length has been set.
     * 
     * @return True if a minimum length has been set.
     */
    public final boolean hasMinLength() {
        return (_minLength > 0);
    }

    /**
     * Returns the minimum length occurances of this type can be.
     * 
     * @return The minimum length facet.
     */
    public final int getMinLength() {
        return _minLength;
    }

    /**
     * Sets the minimum length of this XSString.
     * 
     * @param minLength The minimum length for occurances of this type.
     */
    public final void setMinLength(final int minLength) {
        _minLength = minLength;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    protected final void setFacet(final Facet facet) {
        addPatternFacet(facet);
        setWhiteSpaceFacet(facet);
        setLengthFacet(facet);
    }
    
    /**
     * Transfer given facet if it is one of length, maxLength or minLength.
     *
     * @param facet The facet to transfer.
     */
    protected final void setLengthFacet(final Facet facet) {
        String name = facet.getName();
        if (Facet.LENGTH.equals(name)) {
            setLength(facet.toInt());
        } else if (Facet.MAX_LENGTH.equals(name)) {
            setMaxLength(facet.toInt());
        } else if (Facet.MIN_LENGTH.equals(name)) {
            setMinLength(facet.toInt());
        }
    }

    /**
     * Generate the source code for length, maxLength or minLength facets validation.
     *
     * @param jsc The JSourceCode to fill in.
     * @param validatorName The name of the TypeValidator that the patterns should be added to.
     */
    protected final void codeLengthFacet(final JSourceCode jsc, final String validatorName) {
        if (hasLength()) {
            jsc.add("{0}.setLength({1});", validatorName, Integer.toString(getLength()));
        } else {
            if (hasMaxLength()) {
                jsc.add("{0}.setMaxLength({1});", validatorName, Integer.toString(getMaxLength()));
            }
            if (hasMinLength()) {
                jsc.add("{0}.setMinLength({1});", validatorName, Integer.toString(getMinLength()));
            }
        }
    }

    //--------------------------------------------------------------------------
}
