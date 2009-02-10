/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.ddlgen.schemaobject.KeyGenerator;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;

/**
 * Registry for key generator factory implementations obtained from the configuration.
 * In addition this is also a registry for key generators that holds default key
 * generators as well as key generators defined through mapping.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class KeyGeneratorRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(KeyGeneratorRegistry.class);
    
    //--------------------------------------------------------------------------

    /** Configuration to be used for all key generators. */
    private final DDLGenConfiguration _config;
    
    /** Association between algorithm name and key generator factory implementation. */
    private final Map _factories = new Hashtable();
    
    /** Association between key generator name and key generator implementation. */
    private final Map _generators = new Hashtable();
    
    //--------------------------------------------------------------------------

    /**
     * Construct an instance of each key generator factory specified in given
     * configuration. If key generator does not require mandatory parameters a default
     * instance of the key generator will also be created.
     * 
     * @param config The configuration to obtain the key generator factory classes from.
     *        Also used for all key generator instances to obtain configuration
     *        properties from.
     */
    public KeyGeneratorRegistry(final DDLGenConfiguration config) {
        _config = config;
        
        String prop = config.getStringValue(DDLGenConfiguration.KEYGEN_FACTORIES_KEY, "");
        StringTokenizer tokenizer = new StringTokenizer(prop, ",");
        ClassLoader loader = KeyGeneratorRegistry.class.getClassLoader();
        while (tokenizer.hasMoreTokens()) {
            String classname = tokenizer.nextToken().trim();
            try {
                Class cls = loader.loadClass(classname);
                KeyGeneratorFactory factory = (KeyGeneratorFactory) cls.newInstance();
                _factories.put(factory.getAlgorithmName(), factory);
                
                if (!factory.hasMandatoryParameters()) {
                    KeyGenerator generator = factory.createKeyGenerator();
                    _generators.put(generator.getAlias(), generator);
                }
            } catch (Exception ex) {
                String msg = "Problem instantiating key generator factory: ";
                LOG.error(msg + classname, ex);
            }
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * Create an instance of the key generator specifed by given definiton. The name
     * of the definition is used as algorithm to lookup the key generator factory. The
     * is used to create a key generator initialized with the given definition and the
     * configuration that has been passed to the constructor of this registry. At least
     * the key generator is added to the registry assoziated by its alias.
     * 
     * @param definition The definition to initialize the key generator.
     * @return A key generator instance initialized with given definition.
     * @throws GeneratorException If failed to create a key generator instance.
     */
    public KeyGenerator createKeyGenerator(final KeyGeneratorDef definition)
    throws GeneratorException {
        String algorithm = definition.getName();
        KeyGeneratorFactory factory = getKeyGeneratorFactory(algorithm);
        if (factory == null) {
            LOG.warn("Unknown KeyGeneratorFactory: " + algorithm);
            throw new GeneratorException("Unknown KeyGeneratorFactory: " + algorithm);
        }
        
        KeyGenerator generator = factory.createKeyGenerator(definition);
        generator.setConfiguration(_config);
        _generators.put(generator.getAlias(), generator);
        return generator;
    }

    //--------------------------------------------------------------------------

    /**
     * Returns key generator factory with given algorithm.
     * 
     * @param algorithm Algorithm of the key generator factory.
     * @return Key generator factory with algorithm.
     * @throws GeneratorException If no key generator factory with given algorithm can
     *         be found.
     */
    public KeyGeneratorFactory getKeyGeneratorFactory(final String algorithm) 
    throws GeneratorException {
        if (algorithm == null) {
            throw new GeneratorException("No algorithm specified");
        }
        KeyGeneratorFactory factory = (KeyGeneratorFactory) _factories.get(algorithm);
        if (factory == null) {
            LOG.warn("Unknown KeyGeneratorFactory: " + algorithm);
            throw new GeneratorException("Unknown KeyGeneratorFactory: " + algorithm);
        }
        return factory;
    }
    
    /**
     * Returns a collection of the current configured key generator factories.
     * 
     * @return Collection of the current configured key generator factories.
     */
    public Collection getKeyGeneratorFactories() {
        return Collections.unmodifiableCollection(_factories.values());
    }
    
    /**
     * Returns a collection of the algorithms of current configured key generator
     * factories.
     *
     * @return Algorithms of current configured key generator factories.
     */
    public Collection getKeyGeneratorFactoryAlgorithms() {
        return Collections.unmodifiableCollection(_factories.keySet());
    }
    
    //--------------------------------------------------------------------------

    /**
     * Returns key generator with given alias.
     * 
     * @param alias Alias of the key generator.
     * @return Generator with given alias.
     * @throws GeneratorException If no generator for alias can be found.
     */
    public KeyGenerator getKeyGenerator(final String alias) 
        throws GeneratorException {
        if (alias == null) {
            throw new GeneratorException("No keygenerator alias specified");
        }
        KeyGenerator generator = (KeyGenerator) _generators.get(alias);
        if (generator == null) {
            LOG.warn("Unknown KeyGenerator: " + alias);
            throw new GeneratorException("Unknown KeyGenerator: " + alias);
        }
        return generator;
    }
    
    /**
     * Returns a collection of the current registered key generators.
     * 
     * @return Collection of the current registered key generators.
     */
    public Collection getKeyGenerators() {
        return Collections.unmodifiableCollection(_generators.values());
    }
    
    /**
     * Returns a collection of the aliasses of current registered key generators.
     *
     * @return Aliasses of current registered key generators.
     */
    public Collection getKeyGeneratorAliases() {
        return Collections.unmodifiableCollection(_generators.keySet());
    }
    
    //--------------------------------------------------------------------------
}
