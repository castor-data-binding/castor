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
 * A command used by a resolver strategy to get class descriptors for a package.
 * The number of class descriptors returned will vary between zero and many...
 * Commands are planned to be something like a service which might get invoked
 * multiple times in parallel. So no resolve specific states are allowed to be
 * stored in the class state!
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public interface ResolverPackageCommand {
	/**
	 * The one and only purpose resolver commands are good for ;-) . Resolving
     * the package giving and returning (a maybe empty) list of descriptors found.
     * The descriptors are put into a Map of String (className) and Class (descriptor
     * class).
     * 
     * @param packageName the name of the package to resolve
     * @param p the Properties to be used at resolve
     * @return a Map of className and XMLClassDescriptor
     * @throws IllegalArgumentException if package name is null or empty
     * @throws ResolverException in case that resolving fails fatally
	 */
	public Map resolve(String packageName, Map p)
    throws ResolverException;
}
