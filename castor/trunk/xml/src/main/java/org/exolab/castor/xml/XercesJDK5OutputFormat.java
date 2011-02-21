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

import org.castor.core.util.Messages;

/**
 * Xerces-specific OutputFormat instance, used with JDK 5.0 only.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6216 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public class XercesJDK5OutputFormat extends BaseXercesOutputFormat implements OutputFormat {
    
    private static final String CLASS_NAME = "com.sun.org.apache.xml.internal.serialize.OutputFormat";

    /**
     * Creates an instance of this class.
     */
    public XercesJDK5OutputFormat() {
        try {
            _outputFormat = Class.forName(CLASS_NAME).newInstance();
        } catch (Exception except) {
            throw new RuntimeException(Messages.format(
                    "conf.failedInstantiateOutputFormat", CLASS_NAME, except));
        }
    }

}
