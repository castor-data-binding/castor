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
package org.exolab.castor.xml;


/**
 * Xerces-specific implementation of the Serializer interface for AIX, used for
 * JDK 5 on AIX only where Xerces has been integrated with the core code base.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6216 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3.2
 * 
 * @see com.sun.org.apache.xml.internal.serialize.Serializer
 */
public class AIXXercesJDK5Serializer extends BaseXercesJDK5Serializer {
    
    private static final String PACKAGE_NAME = "com.apache.xml.serialize";

    @Override
    protected String getPackageName() {
        return PACKAGE_NAME;
    }
    
    
    
}
