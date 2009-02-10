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
package org.castor.xml;

import org.castor.mapping.BindingType;
import org.exolab.castor.xml.ClassDescriptorResolverFactory;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.XMLClassDescriptorResolver;
import org.exolab.castor.xml.XMLContext;
import org.exolab.castor.xml.util.ResolverStrategy;
import org.exolab.castor.xml.util.resolvers.CastorXMLStrategy;

/**
 * As the name already expresses: this class is there for backward compatibility
 * and should be removed from Castor with a future release. 
 * 
 * Normally the internal context is created by XMLContext exclusivly and then handed down
 * the call chain. This very class is used in all cases, which fail to be on a call
 * chain started with {@link XMLContext}.
 * 
 * Ideally, all usage of this class should disappear and be replaced with proper
 * usage of {@link XMLContext}.
 * 
 * @see XMLContext
 * @author Joachim Grueneis, jgrueneis_at_gmail_dot_com
 * @version $Id$
 */
public class BackwardCompatibilityContext extends AbstractInternalContext implements InternalContext{
    /**
     * Initializes InternalContext with default values.
     */
    public BackwardCompatibilityContext() {
        setClassLoader(getClass().getClassLoader());
        
        XMLClassDescriptorResolver cdr = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory
            .createClassDescriptorResolver(BindingType.XML);
        cdr.setInternalContext(this);
        setXMLClassDescriptorResolver(cdr);

        Introspector introspector = new Introspector();
        introspector.setInternalContext(this);
        setIntrospector(introspector);
        cdr.setIntrospector(introspector);
        
        ResolverStrategy resolverStrategy = new CastorXMLStrategy();
        setResolverStrategy(resolverStrategy);
        cdr.setResolverStrategy(resolverStrategy);
    }
}
