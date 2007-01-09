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

/**
 * A base class for all list types.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 6678 $ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public abstract class XSListType extends XSType {
    //--------------------------------------------------------------------------

    /** Content type of the collection. */
    private final XSType _contentType;
    
    /** Maximum size of this list. If set to -1 the maximum size is undefined. */
    private int _maxSize = -1;
    
    /** Minimum size of this list. */
    private int _minSize = 0;

    //--------------------------------------------------------------------------

    /**
     * Create a AbstractXSList.
     *
     * @param contentType Type of the collection members.
     */
    public XSListType(final XSType contentType) {
        super();
        
        _contentType = contentType;
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the type contained in the list.
     * 
     * @return The type contained in the list.
     */
    public final XSType getContentType() {
        return _contentType;
    }

    /**
     * Returns the maximum allowed size for this list.
     * 
     * @return The maximum allowed size for this list.
     */
    public final int getMaximumSize() {
        return _maxSize;
    }

    /**
     * Sets the maximum allowed size for this list.
     * 
     * @param size New maximum size for this list
     */
    public final void setMaximumSize(final int size) {
        _maxSize = size;
    }

    /**
     * Returns the minimum allowed size for this list.
     * 
     * @return The minimum allowed size for this list.
     */
    public final int getMinimumSize() {
        return _minSize;
    }

    /**
     * Sets the minimum allowed size for this list.
     * 
     * @param size New minimum size for this list
     */
    public final void setMinimumSize(final int size) {
        _minSize = size;
    }

    //--------------------------------------------------------------------------
}
