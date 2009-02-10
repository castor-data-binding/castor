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
 * A collection of NMToken type.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 * @version $Revision: 6729 $ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class XSNMTokens extends XSListType {

    /** name of the NMTOKENS type. */
    public static final String NMTOKENS_NAME  = "NMTOKENS";

    /**
     * Create a XSNMTokens instance.
     *
     * @param colType Type of collection to use.
     * @param useJava50 If true, the collection will be generated using Java 5
     *        features such as generics.
     */
    public XSNMTokens(final String colType, final boolean useJava50) {
        super(colType, new XSNMToken(), useJava50);
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return NMTOKENS_NAME;
    }
    
    /**
     * {@inheritDoc}
     */
    public short getType() { 
        return XSType.NMTOKENS_TYPE; 
    }

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc, 
            final String fixedValue, 
            final String validatorInstanceName) {
        // no special code
    }
    
}
