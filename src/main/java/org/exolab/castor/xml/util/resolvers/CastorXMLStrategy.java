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
package org.exolab.castor.xml.util.resolvers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.xml.AbstractInternalContext;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.util.ResolverStrategy;

/**
 * The Castor XML resolver strategy implements the resolving behaviour as it had
 * been implmented before this refactoring step. Meaning that:<br/> It uses
 * multiple steps to find a class descriptor for a class.<br/> It uses a cache
 * of class descriptors<br/> A class that couldn't be resolved once is marked
 * as unresolvable and will not be resolved again - even on a second call.<br/>
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class CastorXMLStrategy implements ResolverStrategy {
    /** Logger to be used. */
    private static final Log LOG = LogFactory.getLog(CastorXMLStrategy.class);

    /** The strategy configuration held as map of properties. */
    private Map _properties;

    /**
     * CastorXMLStrategy requires a configuration to be set. Within the constructor the
     * commands building the strategy are instantiated, a command configuration is created
     * and the descriptor cache.
     */
    public CastorXMLStrategy() {
        _properties = new HashMap();
    } //-- CastorXmlStrategy()

    /**
     * {@inheritDoc}
     */
    public void setProperty(final String key, final Object value) {
        if (_properties == null) {
            _properties = new HashMap();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting property: " + key + " to value: " + value);
        }
        _properties.put(key, value);
    } //-- setProperty

    /**
     * {@inheritDoc}
     */
    public ClassDescriptor resolveClass(final ResolverStrategy.ResolverResults resolverResults,
            final String className) throws ResolverException {
        
        if ((className == null) || ("".equals(className))) {
            String message = "Class name to resolve must not be null or empty!";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        XMLClassDescriptor descriptor = this.getDescriptor(resolverResults, className);
        return descriptor;
    } //-- resolve

    /**
     * Gets the XMLClassDescriptor for the class with the given name.<br>
     * 
     * The descriptor is searched in the following resources are search:
     * <ul>
     * <li>The resolver results are checked in the beginning and
     *     after each command executed
     * <li>The MappingLoader
     * <li>The package mapping of the package the given class is located in
     * <li>The CDR file of the package the given class is located in
     * <li>The class file of the corresponding descriptor class (which is
     * className + "Descriptor")
     * </ul>
     * <br/>
     * If any of these resources yield an XMLClassDescriptor it is added to the
     * internal cache and returned as result.
     * 
     * @param resolverResults The resolver results (a Map of className and XMLClassDescriptor)
     * @param className The class to get the descriptor for.
     * @return An <code>XMLClassDescriptor</code> for the given class or
     *         <code>null</code> if no descriptor could be found.
     * @throws ResolverException in case that resolution failed unexpectedly
     */
    private XMLClassDescriptor getDescriptor(final ResolverStrategy.ResolverResults resolverResults,
            final String className) throws ResolverException {
        
        String packageName = ResolveHelpers.getPackageName(className);

        XMLClassDescriptor descriptor = resolverResults.getDescriptor(className);
        if (descriptor != null) {
            return descriptor;
        }
        
        resolverResults.addAllDescriptors(new ByMappingLoader().resolve(className, _properties));
        descriptor = resolverResults.getDescriptor(className);
        if (descriptor != null) {
            return descriptor;
        }
        
        this.resolvePackage(resolverResults, packageName);
        descriptor = resolverResults.getDescriptor(className);
        if (descriptor != null) {
            return descriptor;
        }
        
        resolverResults.addAllDescriptors(new ByDescriptorClass().resolve(className, _properties));
        descriptor = resolverResults.getDescriptor(className);
        if (descriptor != null) {
            return descriptor;
        }
        
        resolverResults.addAllDescriptors(new ByIntrospection().resolve(className, _properties));
        descriptor = resolverResults.getDescriptor(className);
        if (descriptor != null) {
            return descriptor;
        }
        
        // none of the commands have found a match - return null
        resolverResults.addDescriptor(className, null);
        return null;
    } //-- getDescriptor

    /**
     * {@inheritDoc}
     */
    public void resolvePackage(final ResolverResults resolverResults, final String packageName)
    throws ResolverException {
        resolverResults.addAllDescriptors(new ByCDR().resolve(packageName, _properties));
        resolverResults.addAllDescriptors(new ByPackageMapping().resolve(packageName, _properties));
    }
}
