/*
 * Copyright 2006 Thierry Guerin
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
package org.exolab.castor.xml.wls8;

import org.exolab.castor.xml.OutputFormat;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.XMLSerializerFactory;

/**
 * "Weblogic's refactored Xerces"-specific implementation of the
 * XMLSerializerFactory interface. Returns Xerces-specific instances of the
 * {@link Serializer} and {@link OutputFormat} interfaces.
 * 
 * @author Thierry Guerin
 */
public class WeblogicXercesXMLSerializerFactory implements XMLSerializerFactory {

    /**
     * {@inheritDoc}
     */
    public Serializer getSerializer() {
        return new WeblogicXercesSerializer();
    }

    /**
     * {@inheritDoc}
     */
    public OutputFormat getOutputFormat() {
        return new WeblogicXercesOutputFormat();
    }
}
