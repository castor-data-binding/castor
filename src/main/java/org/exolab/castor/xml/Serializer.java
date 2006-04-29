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
package org.exolab.castor.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.xml.sax.DocumentHandler;

/**
 * Interface contract for XML serialization business.
 * @author Werner Guttmann
 */
public interface Serializer {

    /**
     * Sets the OutputCharStream to use.
     * @param out the OutputCharStream to use.
     */
    void setOutputCharStream(Writer out);
    
    /**
     * Returns the {@link DocumentHandler} to use for serialization.
     * @return the DocumentHandler to use for serialization.
     * @throws IOException
     */
    DocumentHandler asDocumentHandler() throws IOException;
    
    /**
     * Sets the {@link OutputFormat} to use during serialization.
     * @param format The output format to use.
     */
    void setOutputFormat (OutputFormat format);
    
    /**
     * Sets the {@link OutputStream} to use. 
     * @param output the OutputStream to use
     */
    void setOutputByteStream(OutputStream output);
}
