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
package org.exolab.castor.xml.util;

import java.util.Map;

import org.exolab.castor.xml.ResolverException;

/**
 * A command used by a resolver strategy to get class descriptors. A command
 * may return none, a single class descriptor or multiple class descriptors and even
 * if class descriptors are returned, is not garanteed that the requested descriptor
 * is within. No exceptions are thrown if the class could not be resolved - this is
 * a possible result!
 * Commands are planned to be something like a service which might get invoked
 * multiple times in parallel. So no resolve specific states are allowed to be
 * stored in the class state!
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public interface ResolverClassCommand {
	/**
	 * The one and only purpose resolver commands are good for ;-) . It can
     * be called with className and clazz set, so the command decides which
     * suites it best or at least one of the two arguments set.
     * 
     * @param className the name of the class to resolve
     * @param p the Properties to be used at resolve
     * @return a Map of className and XMLClassDescriptor
     * @throws IllegalArgumentException if both parameters are null
     * @throws ResolverException in case that resolving fails fatally
	 */
	public Map resolve(String className, Map p)
    throws ResolverException;
}
