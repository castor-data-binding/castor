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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.util.XMLInstance2Schema;
import org.xml.sax.SAXException;

/**
 * An <a href="http://ant.apache.org/">Ant</a> task to call the Castor {@link XMLInstance2Schema}
 * tool. It can be passed a file, a directory, a Fileset or all three.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6543 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class XMLInstance2SchemaTask extends MatchingTask {
    //--------------------------------------------------------------------------

    /** 
     * Error message -- no input provided. 
     */
    private static final String NO_XML_DOCUMENT_MSG =
        "At least one XML document instance must be provided.";

    //--------------------------------------------------------------------------

    /** 
    * Enlists the XML file to process. 
     */
    private File _xmlInstanceFile;
    
    /** 
     * Enlists a directory, for which the user wants to process all XML instance files. 
     **/
    private File _xmlInstanceDir;
    
    /** 
     * Enlists the fileset the user wants to process. 
     */
    private Vector _xmlInstanceFileSets = new Vector();

    // Begin application-specific parameters
    
    /** 
     * Name of the XML schema file to which the output should be written. 
     */
    private String _xmlSchemaFileName;
    
    /**
     * Default grouping to be <xsd:ALL/>.
     */
    private boolean _defaultGroupingAsAll;
    
    //--------------------------------------------------------------------------

    /**
     * Sets the individual schema that will have code generated for it.
     * 
     * @param file One schema file.
     */
    public void setFile(final File file) {
        _xmlInstanceFile = file;
    }

    /**
     * Sets the directory such that all schemas in this directory will have code
     * generated for them.
     * 
     * @param dir The directory containing schemas to process.
     */
    public void setDir(final File dir) {
        _xmlInstanceDir = dir;
    }

    /**
     * Adds a fileset to process that contains schemas to process.
     * 
     * @param set An individual file set containing schemas.
     */
    public void addFileset(final FileSet set) {
        _xmlInstanceFileSets.addElement(set);
    }

    /**
     * Specifies the name of the DDL file to be generated.
     * @param ddlFileName Name of the DDL file to be generated
     */
    public void setXmlSchemaFileName(final String ddlFileName) {
        _xmlSchemaFileName = ddlFileName;
    }
    
    /**
     * Specifies the default grouping to be <xsd:all/>.
     * @param defaultGroupingAsAll Default grouping to be used
     */
    public void setDefaultGrouping(final String defaultGroupingAsAll) {
        _defaultGroupingAsAll = true;
    }

    //--------------------------------------------------------------------------

    /**
     * Configured the code generator. If anything goes wrong during configuration of the
     * Ant task a BuildException will be thrown.
     */
    private void config() {
    }

    /**
     * Runs source generation. If anything goes wrong during source generation a
     * BuildException will be thrown.
     * 
     * @param filePath an individual Schema to generate code for.
     * @param outputFilePath Name of the output file to create.
     */
    private void processFile(final String filePath, final String outputFilePath) {
        log("Processing " + filePath);
        try {
            XMLInstance2Schema schemaGenerator = new XMLInstance2Schema();
            if (_defaultGroupingAsAll) {
                schemaGenerator.setDefaultGroupingAsAll();
            }
            Schema schema = schemaGenerator.createSchema(filePath);
            String outputFileName = outputFilePath;
            if (outputFileName == null) {
                outputFileName = deriveOutputFilePath(filePath);
            }
           FileWriter dstWriter = new FileWriter(outputFileName);
            schemaGenerator.serializeSchema(dstWriter, schema);
        } catch (IOException e) {
            throw new BuildException ("Problem writing to the given putput sink " 
                    + _xmlInstanceFile.getAbsolutePath(), e);
        } catch (SAXException e) {
            throw new BuildException ("Problem streaming the generated XML schema instance " 
                    + "to the given file.", e); 
        }
    }

    /**
     * Derives the XML schema file name from the XML instance document name.
     * @param outputFileName Name of the XML instance document.
     * @return The name of the XML schema instance.
     */
    private String deriveOutputFilePath(final String outputFileName) {
        return outputFileName + ".xsd";
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
        if (_xmlInstanceFile == null && _xmlInstanceDir == null 
                && _xmlInstanceFileSets.size() == 0) {
            throw new BuildException(NO_XML_DOCUMENT_MSG);
        }

        config();

        // process just the file specified
        if (_xmlInstanceFile != null) {
            processFile(_xmlInstanceFile.getAbsolutePath(), _xmlSchemaFileName);
        }

        // process all files in the given directory
        if (_xmlInstanceDir != null && _xmlInstanceDir.exists() && _xmlInstanceDir.isDirectory()) {
            log("Given XML schema file name will be ignored.");
            DirectoryScanner ds = this.getDirectoryScanner(_xmlInstanceDir);

            String[] files = ds.getIncludedFiles();
            for (int i = 0; i < files.length; i++) {
                String filePath = _xmlInstanceDir.getAbsolutePath() + File.separator + files[i];
                processFile(filePath, null);
            }
        }

        // process all files of the given FileSet
        for (int i = 0; i < _xmlInstanceFileSets.size(); i++) {
            log("Given XML schema file name will be ignored.");
            FileSet fs = (FileSet) _xmlInstanceFileSets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            File subdir = fs.getDir(getProject());

            String[] files = ds.getIncludedFiles();
            for (int j = 0; j < files.length; j++) {
                String filePath = subdir.getAbsolutePath() + File.separator + files[j];
                processFile(filePath, null);
            }
        }
    }
}
