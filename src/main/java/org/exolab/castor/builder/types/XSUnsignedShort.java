/*
 * Copyright 2005 Werner Guttmann
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

/**
 * The XML Schema "unsigned-short" type
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec
 *          2005) $
 */
public final class XSUnsignedShort extends XSInteger {
    public XSUnsignedShort() {
        super();
        setMinInclusive(0);
        setMaxInclusive(65535);
    }

    public XSUnsignedShort(boolean asWrapper) {
        super(asWrapper);
        setMinInclusive(0);
        setMaxInclusive(65535);
    }

}
