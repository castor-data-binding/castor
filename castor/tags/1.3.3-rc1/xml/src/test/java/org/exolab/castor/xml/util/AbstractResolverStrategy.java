/*
 * Copyright 2009 Torsten Juergeleit
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

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;

/**
 * This implementation of {@link ResolverStrategy} implements a strategy for
 * resolving instances {@XMLClassDescriptor} created various ways, e.g. via
 * EasyMock.
 * 
 * @author Torsten Juergeleit
 * @since 1.3.1
 */
public abstract class AbstractResolverStrategy implements ResolverStrategy {

    public ClassDescriptor resolveClass(ResolverResults resolverResults,
            String className) throws ResolverException {
        XMLClassDescriptor descriptor = resolverResults
                .getDescriptor(className);
        if (descriptor == null) {
            descriptor = this.createDescriptor(className);
            resolverResults.addDescriptor(className, descriptor);
        }
        return descriptor;
    }

    public void resolvePackage(ResolverResults resolverResults,
            String packageName) throws ResolverException {
        this.resolveClass(resolverResults, packageName + ".Test1");
    }

    public void setProperty(String key, Object value) {
        // ignore
    }

    protected abstract XMLClassDescriptor createDescriptor(String className);
}
