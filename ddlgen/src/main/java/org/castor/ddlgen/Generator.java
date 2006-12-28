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

import java.io.PrintStream;

import org.exolab.castor.mapping.Mapping;

/**
 * Generator is the interface for various generators.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public interface Generator {
    //--------------------------------------------------------------------------

    /** Global configuration file path. */
    String GLOBAL_CONFIG_PATH = "org/castor/ddlgen/";
    
    /** Global configuration file name. */
    String GLOBAL_CONFIG_NAME = "ddlgen.properties";

    //--------------------------------------------------------------------------

    /**
     * Initialize generator.
     */
    void initialize();
    
    //--------------------------------------------------------------------------

    /**
     * Get engine name.
     * 
     * @return Engine name
     */
    String getEngineName();
    
    /**
     * Get engine configuration file path.
     * 
     * @return Engine configuration file path
     */
    String getEngineConfigPath();

    /**
     * Get engine configuration file name.
     * 
     * @return Engine configuration file name
     */
    String getEngineConfigName();
    
    //--------------------------------------------------------------------------
    
    /**
     * Set mapping document.
     * 
     * @param mappingDoc Mapping document.
     */
    void setMapping(Mapping mappingDoc);

    /**
     * Set key generator registry.
     * 
     * @param keygenRegistry Key generator registry.
     */
    void setKeyGenRegistry(KeyGeneratorRegistry keygenRegistry);

    /**
     * Set print stream for output.
     * 
     * @param printer Print stream for output.
     */
    void setPrinter(PrintStream printer);

    //--------------------------------------------------------------------------

    /**
     * Generate DDL for a mapping document.
     * 
     * @throws GeneratorException If failed to generate DDL.
     */
    void generateDDL() throws GeneratorException;

    //--------------------------------------------------------------------------
}
