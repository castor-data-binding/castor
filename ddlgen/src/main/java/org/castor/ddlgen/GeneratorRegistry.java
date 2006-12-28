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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Registry for Generator implementations obtained from the configuration.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class GeneratorRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(GeneratorRegistry.class);
    
    /** Association between name of database engine and generator implementation. */
    private Map _generators = new Hashtable();
    
    //--------------------------------------------------------------------------

    /**
     * Construct an instance of each generator specified in given configuration.
     * 
     * @param config The configuration.
     */
    public GeneratorRegistry(final DDLGenConfiguration config) {
        String prop = config.getStringValue(DDLGenConfiguration.GENERATORS_KEY, "");
        StringTokenizer tokenizer = new StringTokenizer(prop, ",");
        ClassLoader loader = GeneratorRegistry.class.getClassLoader();
        while (tokenizer.hasMoreTokens()) {
            String classname = tokenizer.nextToken().trim();
            try {
                Class cls = loader.loadClass(classname);
                Class[] types = new Class[] {DDLGenConfiguration.class};
                Object[] params = new Object[] {config};
                Constructor cst = cls.getConstructor(types);
                Generator generator = (Generator) cst.newInstance(params);
                _generators.put(generator.getEngineName(), generator);
            } catch (Exception ex) {
                String msg = "Problem instantiating generator: ";
                LOG.error(msg + classname, ex);
            }
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Returns generator with given engine name or null if there is no such generator.
     * 
     * @param engine Name of database engine.
     * @return Generator with given engine name.
     * @throws GeneratorException If no generator for engine can be found.
     */
    public Generator getGenerator(final String engine) throws GeneratorException {
        if (engine == null) {
            throw new GeneratorException("No database engine specified");
        }
        Generator generator = (Generator) _generators.get(engine);
        if (generator == null) {
            throw new GeneratorException("Unknown DDL generator: " + engine);
        }
        return generator;
    }
    
    /**
     * Returns a collection of the current configured generators.
     * 
     * @return Collection of the current configured generators.
     */
    public Collection getGenerators() {
        return Collections.unmodifiableCollection(_generators.values());
    }
    
    /**
     * Returns a collection of the names of current configured database engines.
     *
     * @return Names of the configured database engines.
     */
    public Collection getEngineNames() {
        return Collections.unmodifiableCollection(_generators.keySet());
    }
    
    //--------------------------------------------------------------------------
}
