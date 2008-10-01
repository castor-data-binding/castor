/*
 * Copyright 2007 Assaf Arkin, Keith Visco, Ralf Joachim
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

import org.exolab.javasource.JSourceCode;

/**
 * A list type.
 * 
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public final class XSList extends XSListType {

    /** Type number of this XSType. */
    public static final short TYPE = XSType.COLLECTION;
    
    /**
     * Indicates whether this {@link XSList} instance has been created as a result
     * of a <xs:list> definition.
     */
    private boolean _derivedFromXSList;

    //--------------------------------------------------------------------------

    /**
     * Create a XSList.
     *
     * @param colType Type of collection to use.
     * @param contentType Type of the collection members.
     * @param useJava50 If true, the collection will be generated using Java 5
     *        features such as generics.
     */
    public XSList(final String colType, final XSType contentType, final boolean useJava50) {
        super(colType, contentType, useJava50);
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public short getType() { return TYPE; }
    

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        getContentType().validationCode(jsc, fixedValue, validatorInstanceName);
    }

    /**
     * Sets whether this {@link XSList} instance has been created as a result
     * of a <xs:list> definition.
     * @param derivedFromXSList A boolean value, true or false.
     */
    public void setDerivedFromXSList(final boolean derivedFromXSList) {
        _derivedFromXSList = derivedFromXSList;
    }

    /**
     * Indicates whether this {@link XSList} instance has been created as a result
     * of a <xs:list> definition.
     * @param derivedFromXSList True if this XSList instance has been created as a result 
     * of a <xs:list> definition.
     */
    public boolean isDerivedFromXSList() {
        return _derivedFromXSList;
    }
    
    //--------------------------------------------------------------------------
}
