/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder.types;

import org.exolab.javasource.JSourceCode;

/**
 * A collection of IDREF type.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 * @version $Revision: 6729 $ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class XSIdRefs extends XSListType {

    /** Name of the IDREFS type. */
    public static final String IDREFS_NAME = "IDREFS";

    /**
     * Create a XSNMTokens instance.
     *
     * @param colType Type of collection to use.
     * @param useJava50 If true, the collection will be generated using Java 5
     *        features such as generics.
     */
    public XSIdRefs(final String colType, final boolean useJava50) {
        super(colType, new XSIdRef(), useJava50);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return IDREFS_NAME;
    }
    
    /**
     * {@inheritDoc}
     */
    public short getType() { 
        return XSType.IDREFS_TYPE; 
    }
    
    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc, 
            final String fixedValue, 
            final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.IdRefsValidator typeValidator = "
                + "new org.exolab.castor.xml.validators.IdRefsValidator();");
        jsc.add("fieldValidator.setValidator(typeValidator);");
        jsc.add("desc.setValidator(fieldValidator);");
    }
    
    
    
}
