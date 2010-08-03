/*
 * Copyright 2005 Philipp Erlacher
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
 * This class uses the command pattern. It implements the abstract Command and
 * is used as Command Invoker
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
class PrimitiveObject {

	Class<?> type;
	String value;
	Object object;

	/**
	 * Sets the type and value for the class which should be instantiated
	 * 
	 * @param type
	 *            Class
	 */
	void setType(final Class<?> type) {
		this.type = type;
	}

	/**
	 * Sets the value for the class which should be instantiated
	 * 
	 * @param value
	 *            String
	 */
	void setValue(String value) {
		this.object = this.value = value;
	}

	/**
	 * Object that matches given Class and Value
	 * 
	 * @return Object
	 */
	Object getObject() {
		return this.object;
	}

}
