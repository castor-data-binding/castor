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
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
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
