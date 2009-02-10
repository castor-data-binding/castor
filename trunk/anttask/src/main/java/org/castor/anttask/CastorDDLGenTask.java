/*
 * Copyright 2007 Werner Guttmann
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
package org.castor.anttask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.castor.ddlgen.Generator;
import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.GeneratorFactory;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

/**
 * An <a href="http://ant.apache.org/">Ant</a> task to call the Castor
 * DDL Generator. It can be passed a file, a directory, a Fileset or all
 * three.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6543 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class CastorDDLGenTask extends MatchingTask {
    //--------------------------------------------------------------------------

    /** Error message -- no mapping to run ddl generator on. */
    private static final String NO_MAPPING_MSG =
        "A mapping file must be provided for DDL generation.";

    //--------------------------------------------------------------------------

    /** If processing one schema file, this lists the file. */
    private File _mappingFile = null;
    
    /** If processing all mapping files in a directory, this lists the directory. */
    private File _mappingDir = null;
    
    /** If processing a fileset, this lists the fileset. */
    private Vector _mappingFilesets = new Vector();

    // Begin DDL Generator parameters
    
    /** The package that generated code will belong to. */
    private String _ddlFileName;
    
    /** Name of database engine to generate DDL for. */
    private String _databaseEngine;
    
    /** Global properties file to be used during DDL generation. */
    private String _globalProperties;
    
    /** Database specific properties file to be used during DDL generatrion. */
    private String _databaseEngineProperties;
    
    /** OutputStream used for writing the generated DDL statements. */
    private OutputStream _outputStream; 

    //--------------------------------------------------------------------------

    /**
     * Sets the individual schema that will have code generated for it.
     * 
     * @param file One schema file.
     */
    public void setFile(final File file) {
        _mappingFile = file;
    }

    /**
     * Sets the directory such that all schemas in this directory will have code
     * generated for them.
     * 
     * @param dir The directory containing schemas to process.
     */
    public void setDir(final File dir) {
        _mappingDir = dir;
    }

    /**
     * Adds a fileset to process that contains schemas to process.
     * 
     * @param set An individual file set containing schemas.
     */
    public void addFileset(final FileSet set) {
        _mappingFilesets.addElement(set);
    }

    /**
     * Specifies the name of database engine to generate DDL for.
     * @param databaseEngine Name of the database engine
     */
    public void setDatabaseEngine(final String databaseEngine) {
        _databaseEngine = databaseEngine;
    }

    /**
     * Specifies the name of a database specific properties file to be used 
     * during DDL generation.
     * @param databaseEngineProperties Database specific properties
     */
    public void setDatabaseEngineProperties(final String databaseEngineProperties) {
        _databaseEngineProperties = databaseEngineProperties;
    }

    /**
     * Specifies the name of the DDL file to be generated.
     * @param ddlFileName Name of the DDL file to be generated
     */
    public void setDdlFileName(final String ddlFileName) {
        _ddlFileName = ddlFileName;
    }

    /**
     * Specifies the name of a global properties file to be used during 
     * DDL generation.
     * @param globalProperties Custom global properties for DDL generation.
     */
    public void setGlobalProperties(final String globalProperties) {
        _globalProperties = globalProperties;
    }

    //--------------------------------------------------------------------------

    /**
     * Configured the code generator. If anything goes wrong during configuration of the
     * Ant task a BuildException will be thrown.
     */
    private void config() {
        if (_databaseEngine == null) {
            throw new BuildException("No database engine specified.");
        }
        
        try {
            _outputStream = new FileOutputStream(_ddlFileName);
        } catch (IOException e) {
            throw new BuildException("Problem finding the Castor JDO mapping file " 
                    + _mappingFile.getAbsolutePath(), e);
        }
    }

    /**
     * Runs source generation. If anything goes wrong during source generation a
     * BuildException will be thrown.
     * 
     * @param filePath an individual Schema to generate code for.
     */
    private void processFile(final String filePath) {
        log("Processing " + filePath);
        try {
            Generator ddlgen = GeneratorFactory.createDDLGenerator(
                    _databaseEngine, _globalProperties, _databaseEngineProperties);
            
            Mapping mapping = new Mapping();
            mapping.loadMapping(filePath);
            new MappingUnmarshaller().loadMappingOnly(mapping);
            // TODO: Joachim 2007-09-07 the InternalContext should be set into the unmarshaller!
            
            ddlgen.setMapping(mapping);
            ddlgen.generateDDL(_outputStream);
        } catch (IOException e) {
            throw new BuildException ("Problem finding the Castor JDO mapping file " 
                    + _mappingFile.getAbsolutePath(), e);
        } catch (MappingException e) {
            throw new BuildException ("Problem loading the Castor JDO mapping file " 
                    + _mappingFile.getAbsolutePath(), e);
        } catch (GeneratorException e) {
            throw new BuildException ("Problem generating DDL script for " 
                    + _mappingFile.getAbsolutePath(), e);
        }
    }

    /**
     * Public execute method -- entry point for the Ant task.  Loops over all
     * schema that need code generated and creates needed code generators, then
     * executes them. If anything goes wrong during execution of the Ant task a
     * BuildException will be thrown.
     * 
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute() {
        // Must have something to run the source generator on
        if (_mappingFile == null && _mappingDir == null && _mappingFilesets.size() == 0) {
            throw new BuildException(NO_MAPPING_MSG);
        }

        config();

        // Run DDL generator on file
        if (_mappingFile != null) {
            processFile(_mappingFile.getAbsolutePath());
        }

        // Run source generator on all files in directory
        if (_mappingDir != null && _mappingDir.exists() && _mappingDir.isDirectory()) {
            DirectoryScanner ds = this.getDirectoryScanner(_mappingDir);

            String[] files = ds.getIncludedFiles();
            for (int i = 0; i < files.length; i++) {
                String filePath = _mappingDir.getAbsolutePath() + File.separator + files[i];
                processFile(filePath);
            }
        }

        // Run source generator on all files in FileSet
        for (int i = 0; i < _mappingFilesets.size(); i++) {
            FileSet fs = (FileSet) _mappingFilesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            File subdir = fs.getDir(getProject());

            String[] files = ds.getIncludedFiles();
            for (int j = 0; j < files.length; j++) {
                String filePath = subdir.getAbsolutePath() + File.separator + files[j];
                processFile(filePath);
            }
        }
    }
}
