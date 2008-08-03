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

/**
 * Output format contract for XML serialization.
 * @author Werner Guttmann
 */
public interface OutputFormat {
    
    /**
     * Default serialization method, being XML 
     */
    public static final String XML = "xml";

    /**
     * Set serialization method.
     * @param method Required serialization method.
     */
    void setMethod(String method);
    
    /**
     * Specify whether indentation is required
     * @param indent true if XML output should be intended.
     */
    void setIndenting(boolean indent);
    
    /**
     * Indicate whether white space should be preserved.
     * @param preserveSpace True if white space should be preserved
     */
    void setPreserveSpace(boolean preserveSpace); 
    
    /**
     * Returns the (underlying) OutputFormat instance
     * @return the (underlying) OutputFormat instance
     */
    Object getFormat();

    /**
     * Defines the doc types to use.
     * @param type1 Public DOC type.
     * @param type2 System doc type.
     */
    void setDoctype (String type1, String type2);
    
    /**
     * Indicates whether to omit XML declaration.
     * @param omitXMLDeclaration True if XMl declaration should be omitted.
     */
    void setOmitXMLDeclaration (boolean omitXMLDeclaration);
    
    /**
     * Indicates whether to omit DOCTYPE definition.
     * @param omitDocumentType True if DOCTYPE definition should be omitted.
     */
    void setOmitDocumentType (boolean omitDocumentType);
    
    /**
     * Sets the encoding to use.
     * @param encoding The encoding to use.
     */
    void setEncoding(String encoding);
    
}
