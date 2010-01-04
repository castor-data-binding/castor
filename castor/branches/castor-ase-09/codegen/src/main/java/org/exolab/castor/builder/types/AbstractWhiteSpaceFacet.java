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
 * A base class for types which support the whiteSpace and pattern facets.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6678 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 * @since 1.1
 */
public abstract class AbstractWhiteSpaceFacet extends AbstractPatternFacet {
    //--------------------------------------------------------------------------

    /** If set to true only 'collapse' is allowed for the whiteSpace facet. If set to false
     *  'collaps', 'replace' and 'preserve' values are allowed for whiteSpace facet. */
    private final boolean _whiteSpaceCollapseOnly;
    
    /** The whiteSpace facet. */
    private String _whiteSpace = Facet.WHITESPACE_COLLAPSE;
    
    //--------------------------------------------------------------------------

    /**
     * No-arg constructor. By default only 'collapse' is supported for the whiteSpace facet.
     */
    protected AbstractWhiteSpaceFacet() {
        this(true);
    }
    
    /**
     * Construct a new AbstractXSPatternFacet optionally allowing the whiteSpace facet to be set
     * to 'replace' and 'preserve' values in addition to 'collaps'.
     *
     * @param whiteSpaceCollapseOnly If set to true only 'collapse' is allowed for the whiteSpace
     *        facet. If set to false 'collaps', 'replace' and 'preserve' values are allowed for
     *        whiteSpace facet.
     */
    protected AbstractWhiteSpaceFacet(final boolean whiteSpaceCollapseOnly) {
        _whiteSpaceCollapseOnly = whiteSpaceCollapseOnly;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Returns true if the whiteSpace facet is used.
     * 
     * @return True if the whiteSpace facet is used.
     */
    public final boolean hasWhiteSpace() {
        return (_whiteSpace != null);
    }

    /**
     * Returns the whiteSpace facet of this type.
     * 
     * @return The whiteSpace facet of this type.
     */
    public final String getWhiteSpace() {
        return _whiteSpace;
    }

    /**
     * Sets the whiteSpace facet of this XSType. The value of the whiteSpace
     * facet must be one of the following:
     * <ul>
     *  <li>preserve</li>
     *  <li>replace</li>
     *  <li>collapse</li>
     * </ul>
     * Any other value will generate a warning and the whiteSpace facet keeps unchanged.
     * 
     * @param value The value for the whiteSpace facet.
     */
    public final void setWhiteSpace(final String value) {
        if (Facet.WHITESPACE_COLLAPSE.equals(value)) {
            _whiteSpace = value;
        } else if (!_whiteSpaceCollapseOnly) {
            if (Facet.WHITESPACE_REPLACE.equals(value)) {
                _whiteSpace = value;
            } else if (Facet.WHITESPACE_PRESERVE.equals(value)) {
                _whiteSpace = value;
            } else {
                throw new IllegalArgumentException(
                        getName() + ": bad entry for whiteSpace facet: " + value);
            }
        } else {
            throw new IllegalArgumentException(
                    getName() + ": only 'collapse' allowed for whiteSpace facet: " + value);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    protected void setFacet(final Facet facet) {
        super.setFacet(facet);
        setWhiteSpaceFacet(facet);
    }

    /**
     * Transfer given facet if it is a whiteSpace.
     *
     * @param facet The facet to transfer.
     */
    protected final void setWhiteSpaceFacet(final Facet facet) {
        if (Facet.WHITESPACE.equals(facet.getName())) { setWhiteSpace(facet.getValue()); }
    }

    /**
     * Generate the source code for pattern facet validation.
     *
     * @param jsc The JSourceCode to fill in.
     * @param validatorName The name of the TypeValidator that the whiteSpace should be added to.
     */
    protected final void codeWhiteSpaceFacet(final JSourceCode jsc, final String validatorName) {
        if (!_whiteSpaceCollapseOnly && hasWhiteSpace()) {
            jsc.add("{0}.setWhiteSpace(\"{1}\");", validatorName, getWhiteSpace());
        }
    }

    //--------------------------------------------------------------------------
}
