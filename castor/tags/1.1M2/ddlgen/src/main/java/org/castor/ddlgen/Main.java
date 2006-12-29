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

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.util.CommandLineOptions;

/**
 * Main Program.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class Main {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Main.class);
    
    //--------------------------------------------------------------------------

    /**
     * @param args options
     */
    public static void main(final String[] args) {
        CommandLineOptions allOptions = new CommandLineOptions();
        
        allOptions.addFlag(
                "m", "mapping.xml", "input mapping file.");
        allOptions.addFlag(
                "c", "ddl.properties", "configuration file.", true);
        allOptions.addFlag(
                "d", "mysql.properties", "specific database configuration file.", true);
        allOptions.addFlag(
                "e", "MySQL", "database engine, for example MySQL, Oracle", true);
        allOptions.addFlag(
                "o", "output.sql", "output ddl file", true);
        allOptions.addFlag(
                "h", "", "Displays this help screen.", true);

        // Process the specified command line options.
        Properties options = allOptions.getOptions(args);
        
        String  mappingName     = options.getProperty("m");
        String  ddlName         = options.getProperty("o");
        String  globalConfig    = options.getProperty("c");
        String  specificConfig  = options.getProperty("d");
        String  engine  = options.getProperty("e");
        
        if (options.getProperty("h") != null || mappingName == null) {
            PrintWriter pw = new PrintWriter(System.out, true);
            allOptions.printHelp(pw);
            pw.flush();
            System.exit(0);
        }
        
        // Verify and adjust output file.
        if (ddlName == null) {
            ddlName = mappingName.replaceAll(".xml", ".sql");
        }        

        LOG.info("mapping file: " + mappingName);
        LOG.info("ddl output file: " + ddlName);
        LOG.info("global configuration file: " + globalConfig);
        LOG.info("specific database configuration file: " + specificConfig);
        LOG.info("database: " + engine);
        
        // Create generator and generate ddl.
        try {
            Generator generator = GeneratorFactory.createDDLGenerator(
                    engine, globalConfig, specificConfig);

            Mapping mapping = new Mapping();
            mapping.loadMapping(mappingName);
            new MappingUnmarshaller().loadMappingOnly(mapping);
            
            generator.setMapping(mapping);
            generator.setPrinter(new PrintStream(new FileOutputStream(ddlName)));
            generator.generateDDL();            
        } catch (Exception ex) {
            LOG.error("Error: " + ex.getMessage(), ex);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Hide utility class constructor.
     */
    private Main() {
        super();
    }

    //--------------------------------------------------------------------------
}
