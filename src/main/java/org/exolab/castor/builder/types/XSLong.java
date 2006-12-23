/*
 * Copyright 2006 Edward Kuns
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
 *
 * $Id$
 */
package org.exolab.castor.builder.types;

/**
 * The XML Schema "long" type.
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class XSLong extends XSInteger {

    /**
     * No-arg constructor.
     */
    public XSLong() {
        this(false);
    }

    /**
     * Construct a new XSLong optionally using the wrapper class implementation.
     *
     * @param asWrapper
     *            if true, this class will be implemented using a wrapper.
     */
    public XSLong(boolean asWrapper) {
        super(asWrapper, XSType.LONG_TYPE);
    }

} //-- XSNegativeInteger
