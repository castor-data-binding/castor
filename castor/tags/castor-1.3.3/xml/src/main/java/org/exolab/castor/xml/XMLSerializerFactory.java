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
 * Configurable factory object for XML serialization.
 * 
 * The XML serializer factory is set through the 
 * <tt>org.exolab.castor.xml.serializer.factory</tt> in the
 * <tt>castor.properties</tt> file shipped with the product JAR.
 * By default, this is set to a value of 
 * <tt>org.exolab.castor.xml.XercesJDK5XMLSerializerFactory</tt>. This
 * {@link XMLSerializerFactory} will use the Xerces instance as shipped with 
 * the JRE.
 * 
 * To use another {@link XMLSerializerFactory}, please override this 
 * property in a custom <tt>castor.properties</tt>.
 * 
 * When using Castor XML with JDK 5.0 and above, you may switch to 
 * the {@link XercesXMLSerializerFactory}, which will use a separately 
 * downloaded Xerces instance, and not the one shipped with the JRE
 * itself. 
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr
 *          2006) $
 * 
 * @since 1.0
 */
public interface XMLSerializerFactory {
    
    /**
     * Factory method for obtaining a Serializer instance.
     * 
     * @return A Serializer instance.
     */
    Serializer getSerializer();

    /**
     * Factory method for obtaining a OutputFormat instance.
     * 
     * @return A OutputFormat instance.
     */
    OutputFormat getOutputFormat();
}
