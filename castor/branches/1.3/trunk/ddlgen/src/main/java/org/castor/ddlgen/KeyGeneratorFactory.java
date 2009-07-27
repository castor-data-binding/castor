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

import org.castor.ddlgen.schemaobject.KeyGenerator;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;

/**
 * Interface to be implemented by all key generator factories.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public interface KeyGeneratorFactory {
    //--------------------------------------------------------------------------

    /**
     * Get name of key generator algorithm.
     * 
     * @return Name of key generator algorithm.
     */
    String getAlgorithmName();
    
    /**
     * Does the algorithm has mandatory parameters and therefore requires a key generator
     * definition in mapping file?
     * 
     * @return <code>true</code> if algorithm has mandatory parameters and therefore
     *         requires a key generator definition in mapping file. <code>false</code>
     *         if algorithm does not require parameters or has only optional parameters.
     */
    boolean hasMandatoryParameters();
    
    /**
     * Create a default key generator instance with the algorithm the factory is
     * responsible for. This is only possible for those algorithms that do not require
     * mandatory parameters.
     * 
     * @return A default key generator instance with the algorithm the factory is
     *         responsible for.
     * @throws GeneratorException If creation of default key generator is not possible
     *         due to required mandatory parameters.
     */
    KeyGenerator createKeyGenerator() throws GeneratorException;
    
    /**
     * Create a key generator instance from the given definition with the algorithm the
     * factory is responsible for.
     * 
     * @param definition The definition to initialize the key generator.
     * @return A key generator instance initialized with given definition with the
     *         algorithm the factory is responsible for.
     * @throws GeneratorException If failed to create a key generator instance.
     */
    KeyGenerator createKeyGenerator(final KeyGeneratorDef definition)
    throws GeneratorException;

    //--------------------------------------------------------------------------
}
