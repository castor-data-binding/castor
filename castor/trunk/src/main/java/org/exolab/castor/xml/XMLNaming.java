/*
 * Copyright 2007 Joachim Grueneis
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
 * XMLNaming contains all methods required by Castor to build valid XML names.
 * It was originally an abstract class by <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * for which I create this interface.
 * 
 * @author Joachim Grueneis, jgrueneis_at_codehaus_dot_org
 * @version $Id$
 */
public interface XMLNaming {

	/**
	 * Creates the XML Name for the given class The actual
	 * behavior of this method is determined by the
	 * implementation. The only restriction is that the name
	 * returned must be a valid xml name.
	 * 
	 * @param c the Class to create the XML Name for
	 * @return the XML name based on the given class
	 **/
	public abstract String createXMLName(Class c);

	/**
	 * Converts the given String to an XML name. The actual
	 * behavior of this method is determined by the
	 * implementation. The only restriction is that the name
	 * returned must be a valid xml name.
	 *
	 * @return an XML name based on the given String
	 **/
	public abstract String toXMLName(String name);

}