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
 * Xerces-specific implementation of the {@link XMLSerializerFactory} interface.
 * Returns Xerces-specific instances of the {@link Serializer} and 
 * {@link OutputFormat} interfaces.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6216 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public class XercesJDK5XMLSerializerFactory implements XMLSerializerFactory {
	/**
	 * @see org.exolab.castor.xml.XMLSerializerFactory#getSerializer()
     * {@inheritDoc}
	 */
	public Serializer getSerializer() {
		return new XercesJDK5Serializer();
	}

	/**
	 * @see org.exolab.castor.xml.XMLSerializerFactory#getOutputFormat()
     * {@inheritDoc}
	 */
	public OutputFormat getOutputFormat() {
		return new XercesJDK5OutputFormat();
	}
}
