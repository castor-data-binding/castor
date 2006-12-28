/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: CastorSourceGenTask.java 6410 2006-11-17 12:06:24Z wguttmn $
 */

package org.exolab.castor.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;

import org.exolab.castor.builder.FieldInfoFactory;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.xml.schema.reader.SchemaUnmarshaller;
import org.exolab.castor.xml.schema.reader.Sax2ComponentReader;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.XMLException;

import org.exolab.castor.util.LocalConfiguration;

import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

/**
 * An <a href="http://ant.apache.org/">Ant</a> task to call the Castor 
 * Source Generator. It can be passed a file, a directory, a Fileset or all
 * three.
 *
 * @author <a href="mailto:joel.farquhar@montage-dmc.com">Joel Farquhar</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 */
public class CastorSourceGenTask extends MatchingTask {

    private static final String DISABLE_DESCRIPTORS_MSG
            = "Disabling generation of Class descriptors";

    private static final String DISABLE_MARSHALL_MSG
            = "Disabling generation of Marshalling framework methods (marshall, unmarshall, validate).";

    private static final String CASTOR_TESTABLE_MSG
            = "The generated classes will implement org.exolab.castor.tests.CastorTestable";

    private File   file     = null;
    private File   dir      = null;
    private Vector filesets = new Vector();

    // Begin Source Generator parameters
    private String srcpackage;
    private String todir;
    private String bindingfile;
    private String types;
    private String lineseparator;
    // End Source Generator parameters

    private boolean verbose;
    private boolean warnings = true;
    private boolean nodesc;
    private boolean nomarshall;
    private boolean testable;
    
    /**
     * Whether to generate code for imported schemas, too.
     */
    private boolean generateImportedSchemas;
    
    /**
     * Whether to generate SAX-1 compliant code
     */
    private boolean sax1;
    
    /**
     * Whether enumerated type lookup should be performed in a case
     * insensitive manner.
     */
    private boolean caseInsensitive;

    /**
     * CastorBuilderProperties file
     */
    private String properties;

    /**
     * SourceGenerator instance
     */
    SourceGenerator sgen;


    public CastorSourceGenTask() {
        // Nothing needed
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    public void setPackage(String pack) {
        this.srcpackage = pack;
    }

    public void setTodir(String dest) {
        this.todir = dest;
    }

    public void setBindingfile(String bindingfile) {
        this.bindingfile = bindingfile;
    }

    public void setLineseparator(String ls) {
        this.lineseparator = ls;
    }

    public void setTypes(String tf) {
        this.types = (tf.equals("j2")) ? "arraylist" : tf;
    }

    public void setVerbose(boolean b) {
        this.verbose = b;
    }

    public void setWarnings(boolean b) {
        this.warnings = b;
    }

    public void setNodesc(boolean b) {
        this.nodesc = b;
    }

    public void setNomarshall(boolean b) {
        this.nomarshall = b;
    }

    public void setTestable(boolean b) {
        this.testable = b;
    }

    /**
     * Controls whether to generate code for imported schemas as well. 
     * @param generateImportedSchemas True if code should be generated for imported schemas.
     */
    public void setGenerateImportedSchemas(final boolean generateImportedSchemas) {
        this.generateImportedSchemas = generateImportedSchemas; 
    }

    /**
     * Controls whether to generate SAX-1 compliant code. 
     * @param sax1 True if SAX-1 compliant code should be generated.
     */
    public void setSAX1(final boolean sax1) {
        this.sax1 = sax1; 
    }

    /**
     * Controls whether enumerated type lookup should be performed in a case
     * insensitive manner.
     * @param caseInsensitive True if enumerated type lookup should be performed in a case
     * insensitive manner
     */
    public void setCaseInsensitive(final boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive; 
    }

    /**
     * Sets the file to use for castor builder properties
     *
     * @param properties the properties to use
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }

    private void config() throws BuildException {
        // Create Source Generator with appropriate type factory
        if (types != null) {
            try {
                FieldInfoFactory factory = new FieldInfoFactory(types);
                sgen = new CastorSourceGeneratorWrapper(factory);
            } catch (Exception e) {
                try {
                    sgen = new CastorSourceGeneratorWrapper((FieldInfoFactory) Class.forName(types).newInstance());
                } catch (Exception e2) {
                    throw new BuildException("Invalid types \"" + types + "\": " + e.getMessage());
                }
            }
        } else {
            sgen = new CastorSourceGeneratorWrapper(); // default
        }

        // Set Line Separator
        String lineSep = System.getProperty("line.separator");
        if (lineseparator != null) {
            if ("win".equals(lineseparator)) {
                log("Using Windows style line separation.");
                lineSep = "\r\n";
            } else if ("unix".equals(lineseparator)) {
                log("Using UNIX style line separation.");
                lineSep = "\n";
            } else if ("mac".equals(lineseparator)) {
                log("Using Macintosh style line separation.");
                lineSep = "\r";
            } else {
                throw new BuildException("Invalid value for lineseparator, must be win, unix, or mac.");
            }
        }
        sgen.setLineSeparator(lineSep);

        sgen.setDestDir(todir);

        if (bindingfile != null) {
            sgen.setBinding(bindingfile);
        }

        sgen.setVerbose(verbose);
        sgen.setSuppressNonFatalWarnings(!warnings);

        sgen.setDescriptorCreation(!nodesc);
        if (nodesc) {
            log(DISABLE_DESCRIPTORS_MSG);
        }

        sgen.setCreateMarshalMethods(!nomarshall);
        if (nomarshall) {
            log(DISABLE_MARSHALL_MSG);
        }
        
        sgen.setGenerateImportedSchemas(generateImportedSchemas); 
        
        sgen.setSAX1(sax1);
        
        sgen.setCaseInsensitive(caseInsensitive);

        sgen.setTestable(testable);
        if (this.testable) {
            log(CASTOR_TESTABLE_MSG);
        }

        // Set Builder Properties;
        if (properties != null) {
            String filePath = new File(properties).getAbsolutePath();
            Properties customProperties = new Properties();
            try {
                customProperties.load(new FileInputStream(filePath));
            } catch (FileNotFoundException e) {
                throw new BuildException("Properties file \"" + filePath + "\" not found");
            } catch (IOException e) {
                throw new BuildException("Can't read properties file \"" + filePath + "\": " + e);
            }
            sgen.setDefaultProperties(customProperties);
        }
    }

    /**
     * Run source generation
     */
    private void processFile(String filePath) throws BuildException {
        log("Processing " + filePath);
        try {
            sgen.generateSource(filePath, srcpackage);
        } catch (FileNotFoundException e) {
            String message = "XML Schema file \"" + file.getAbsolutePath() + "\" not found.";
            log(message);
            throw new BuildException(message);
        } catch (Exception iox) {
            throw new BuildException(iox);
        }
    }

    public void execute() throws BuildException {
        // Must have something to run the source generator on
        if (file == null && dir == null && filesets.size() == 0) {
            throw new BuildException("At least one of the file or dir attributes, or a fileset element, must be set.");
        }

        try {
            config();

            // Run source generator on file
            if (file != null) {
                processFile(file.getAbsolutePath());
            }

            // Run source generator on all files in directory
            if (dir != null && dir.exists() && dir.isDirectory()) {
                DirectoryScanner ds = this.getDirectoryScanner(dir);

                String[] files = ds.getIncludedFiles();
                for (int i = 0; i < files.length; i++) {
                    String filePath = dir.getAbsolutePath() + File.separator + files[i];
                    processFile(filePath);
                }
            }

            // Run source generator on all files in FileSet
            for (int i = 0; i < filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                File dir = fs.getDir(getProject());

                String[] files = ds.getIncludedFiles();
                for (int j = 0; j < files.length; j++) {
                    String filePath = dir.getAbsolutePath() + File.separator + files[j];
                    processFile(filePath);
                }
            }
        } finally {
            sgen = null;
        }
    }

    /**
     * Override Castor's SourceGenerator to inject exception handling.
     * Code based on castor-0.9.5.3-xml.jar
     */
    private class CastorSourceGeneratorWrapper extends SourceGenerator {
        public CastorSourceGeneratorWrapper() {
            super();
        }

        public CastorSourceGeneratorWrapper(FieldInfoFactory fieldInfoFactory) {
            super(fieldInfoFactory);
        }

        public CastorSourceGeneratorWrapper(FieldInfoFactory fieldInfoFactory, ExtendedBinding extendedBinding) {
            super(fieldInfoFactory, extendedBinding);
        }

        public void generateSource(InputSource source, String packageName) throws BuildException {
            Parser parser = null;
            try {
                parser = LocalConfiguration.getInstance().getParser();
            } catch (RuntimeException e) {
                throw new BuildException("Unable to create SAX parser.", e);
            }
            if (parser == null) {
                throw new BuildException("Unable to create SAX parser.");
            }

            SchemaUnmarshaller schemaUnmarshaller = null;
            try {
                schemaUnmarshaller = new SchemaUnmarshaller();
            } catch (XMLException e) {
                throw new BuildException("Unable to create schema unmarshaller.", e);
            }

            Sax2ComponentReader handler = new Sax2ComponentReader(schemaUnmarshaller);
            parser.setDocumentHandler(handler);
            parser.setErrorHandler(handler);
            try {
                parser.parse(source);
            } catch (IOException e) {
                throw new BuildException("Can't read input file " + source.getSystemId() + ".\n" + e, e);
            } catch (SAXException e) {
                throw new BuildException("Can't parse input file " + source.getSystemId() + ".\n" + e, e);
            }
            Schema schema = schemaUnmarshaller.getSchema();
            try {
                generateSource(schema, packageName);
            } catch (Exception iox) {
                throw new BuildException(iox);
            }
        }
    }
}
