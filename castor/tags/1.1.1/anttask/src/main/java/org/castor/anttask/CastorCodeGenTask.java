/*
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
package org.castor.anttask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.factory.FieldInfoFactory;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.reader.Sax2ComponentReader;
import org.exolab.castor.xml.schema.reader.SchemaUnmarshaller;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

/**
 * An <a href="http://ant.apache.org/">Ant</a> task to call the Castor
 * Source Generator. It can be passed a file, a directory, a Fileset or all
 * three.
 *
 * @author <a href="mailto:joel.farquhar@montage-dmc.com">Joel Farquhar</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @version $Revision: 6543 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class CastorCodeGenTask extends MatchingTask {
    //--------------------------------------------------------------------------

    /** Msg indicating class descriptor generation has been disabled. */
    private static final String DISABLE_DESCRIPTORS_MSG =
        "Disabling generation of Class descriptors";
    
    /** Msg indicating marshaling framework code will not be generated. */
    private static final String DISABLE_MARSHAL_MSG =
        "Disabling generation of Marshaling framework methods (marshal, unmarshal, validate).";
    
    /** Msg indicating that castor-testable code will be generated. */
    private static final String CASTOR_TESTABLE_MSG =
        "The generated classes will implement org.exolab.castor.tests.CastorTestable";
    
    /** Error message -- invalid line seperator. */
    private static final String INVALID_LINESEP_MSG =
        "Invalid value for lineseparator, must be win, unix, or mac.";
    
    /** Error message -- no schemas to run code generator on. */
    private static final String NO_SCHEMA_MSG =
        "At least one of the file or dir attributes, or a fileset element, must be set.";

    //--------------------------------------------------------------------------

    /** If processing one schema file, this lists the file. */
    private File _schemaFile = null;
    
    /** If processing all schemas in a directory, this lists the directory. */
    private File _schemaDir = null;
    
    /** If processing a fileset, this lists the fileset. */
    private Vector _schemaFilesets = new Vector();

    // Begin Source Generator parameters
    /** The package that generated code will belong to. */
    private String _srcpackage;
    
    /** The directory that code will be generated into. */
    private String _todir;
    
    /** Binding file for the code generator. */
    private String _bindingfile;
    
    /** Information about how to generate collections. */
    private String _types;
    
    /** Line seperator to use for generated code. */
    private String _lineseparator;
    
    /** If true, the code generator will be verbose. */
    private boolean _verbose;
    
    /** If true, non-fatal warnings will be suppressed.  Also, existing source
     *  files will be silently overwritten. */
    private boolean _warnings = true;
    
    /** If true, class descriptors will not be generated. */
    private boolean _nodesc;
    
    /** If true, marshaling code will not be generated. */
    private boolean _nomarshal;
    
    /** If true, Castor's CTF testable framework code will be generated. */
    private boolean _testable;
    
    /** Whether to generate code for imported schemas, too. */
    private boolean _generateImportedSchemas;
    
    /** Whether to generate SAX-1 compliant code. */
    private boolean _sax1;
    
    /** Whether enumerated type lookup should be performed in a case insensitive manner. */
    private boolean _caseInsensitive;
    
    /** CastorBuilderProperties file. */
    private String _properties;
    
    /** The name conflict strategy to use */
    private String _nameConflictStrategy = "warnViaConsoleDialog";
    
    /** Name of the automatic clas name conflict strategy to use */
    private String _automaticConflictStrategy = "xpath";
    
    /** SourceGenerator instance. */
    private SourceGenerator _sgen;

    //--------------------------------------------------------------------------

    /**
     * No-arg constructor.
     */
    public CastorCodeGenTask() {
        // Nothing needed
    }

    //--------------------------------------------------------------------------

    /**
     * Sets the individual schema that will have code generated for it.
     * 
     * @param file One schema file.
     */
    public void setFile(final File file) {
        _schemaFile = file;
    }

    /**
     * Sets the directory such that all schemas in this directory will have code
     * generated for them.
     * 
     * @param dir The directory containing schemas to process.
     */
    public void setDir(final File dir) {
        _schemaDir = dir;
    }

    /**
     * Adds a fileset to process that contains schemas to process.
     * 
     * @param set An individual file set containing schemas.
     */
    public void addFileset(final FileSet set) {
        _schemaFilesets.addElement(set);
    }

    /**
     * Sets the package that generated code will belong to.
     * 
     * @param pack The package that generated code will belong to.
     */
    public void setPackage(final String pack) {
        _srcpackage = pack;
    }

    /**
     * Sets the directory into which code will be generated.
     * 
     * @param dest The directory into which code will be generated.
     */
    public void setTodir(final String dest) {
        _todir = dest;
    }

    /**
     * Sets the binding file to be used for code generation.
     * 
     * @param bindingfile The binding file to be used for code generation.
     */
    public void setBindingfile(final String bindingfile) {
        _bindingfile = bindingfile;
    }

    /**
     * Sets the line seperator to use for code generation.
     * 
     * @param ls The line seperator to use for code generation.
     */
    public void setLineseparator(final String ls) {
        _lineseparator = ls;
    }

    /**
     * Sets the type factory for code generation.
     * 
     * @param tf The type factory to use for code generation.
     */
    public void setTypes(final String tf) {
        _types = (tf.equals("j2")) ? "arraylist" : tf;
    }

    /**
     * Sets whether or not code generation gives extra information about its work.
     * 
     * @param b If true, the code generator will be verbose.
     */
    public void setVerbose(final boolean b) {
        _verbose = b;
    }

    /**
     * Sets the name conflict strategy to use.
     * 
     * @param nameConflictStrategy The name conflict strategy to use
     */
    public void setNameConflictStrategy(final String nameConflictStrategy) {
        _nameConflictStrategy = nameConflictStrategy;
    }

    /**
     * Sets the name conflict strategy to use.
     * 
     * @param automaticConflictStrategy The automatic class name conflict strategy to use
     */
    public void setAutomaticConflictStrategy(final String automaticConflictStrategy) {
        _automaticConflictStrategy = automaticConflictStrategy;
    }

    
    /**
     * Sets whether or not non-fatal warnings should be suppressed.
     * 
     * @param b If true, non-fatal warnings will be suppressed. This additionally
     *        means that existing source files will be silently overwritten.
     */
    public void setWarnings(final boolean b) {
        _warnings = b;
    }

    /**
     * Sets whether or not class descriptors are generated.
     * 
     * @param b If true, class descriptors are generated.
     */
    public void setNodesc(final boolean b) {
        _nodesc = b;
    }

    /**
     * Sets whether or not marshaling methods are generated.
     * 
     * @param b If true, marshaling methods are generated.
     */
    public void setNomarshal(final boolean b) {
        _nomarshal = b;
    }

    /**
     * Sets whether CTF framework code is generated.
     * 
     * @param b If true, the generated code will be instrumented for the CTF.
     */
    public void setTestable(final boolean b) {
        _testable = b;
    }

    /**
     * Controls whether to generate code for imported schemas as well.
     * 
     * @param generateImportedSchemas True if code should be generated for imported schemas.
     */
    public void setGenerateImportedSchemas(final boolean generateImportedSchemas) {
        _generateImportedSchemas = generateImportedSchemas;
    }

    /**
     * Controls whether to generate SAX-1 compliant code.
     * 
     * @param sax1 True if SAX-1 compliant code should be generated.
     */
    public void setSAX1(final boolean sax1) {
        _sax1 = sax1;
    }

    /**
     * Controls whether enumerated type lookup should be performed in a case
     * insensitive manner.
     * 
     * @param caseInsensitive True if enumerated type lookup should be performed in a case
     *        insensitive manner
     */
    public void setCaseInsensitive(final boolean caseInsensitive) {
        _caseInsensitive = caseInsensitive;
    }

    /**
     * Sets the file to use for castor builder properties.
     *
     * @param properties The properties to use.
     */
    public void setProperties(final String properties) {
        _properties = properties;
    }

    //--------------------------------------------------------------------------

    /**
     * Configured the code generator. If anything goes wrong during configuration of the
     * Ant task a BuildException will be thrown.
     */
    private void config() {
        // Create Source Generator with appropriate type factory
        if (_types != null) {
            FieldInfoFactory factory;
            try {
                factory = new FieldInfoFactory(_types);
                _sgen = new CastorSourceGeneratorWrapper(factory);
            } catch (Exception e) {
                try {
                    factory = (FieldInfoFactory) Class.forName(_types).newInstance();
                    _sgen = new CastorSourceGeneratorWrapper(factory);
                } catch (Exception e2) {
                    throw new BuildException("Invalid types \"" + _types + "\": " + e.getMessage());
                }
            }
        } else {
            // default
            _sgen = new CastorSourceGeneratorWrapper();
        }

        // Set Line Separator
        String lineSep = System.getProperty("line.separator");
        if (_lineseparator != null) {
            if ("win".equals(_lineseparator)) {
                log("Using Windows style line separation.");
                lineSep = "\r\n";
            } else if ("unix".equals(_lineseparator)) {
                log("Using UNIX style line separation.");
                lineSep = "\n";
            } else if ("mac".equals(_lineseparator)) {
                log("Using Macintosh style line separation.");
                lineSep = "\r";
            } else {
                throw new BuildException(INVALID_LINESEP_MSG);
            }
        }
        _sgen.setLineSeparator(lineSep);

        _sgen.setDestDir(_todir);

        if (_bindingfile != null) { _sgen.setBinding(_bindingfile); }

        _sgen.setVerbose(_verbose);
        _sgen.setSuppressNonFatalWarnings(!_warnings);

        _sgen.setDescriptorCreation(!_nodesc);
        if (_nodesc) { log(DISABLE_DESCRIPTORS_MSG); }

        _sgen.setCreateMarshalMethods(!_nomarshal);
        if (_nomarshal) { log(DISABLE_MARSHAL_MSG); }

        _sgen.setGenerateImportedSchemas(_generateImportedSchemas);

        _sgen.setSAX1(_sax1);

        _sgen.setCaseInsensitive(_caseInsensitive);
        
        _sgen.setNameConflictStrategy(_nameConflictStrategy);
        
        _sgen.setClassNameConflictResolver(_automaticConflictStrategy);

        _sgen.setTestable(_testable);
        if (this._testable) { log(CASTOR_TESTABLE_MSG); }

        // Set Builder Properties;
        if (_properties != null) {
            String filePath = new File(_properties).getAbsolutePath();
            Properties customProperties = new Properties();
            try {
                customProperties.load(new FileInputStream(filePath));
            } catch (FileNotFoundException e) {
                throw new BuildException("Properties file \"" + filePath + "\" not found");
            } catch (IOException e) {
                throw new BuildException("Can't read properties file \"" + filePath + "\": " + e);
            }
            _sgen.setDefaultProperties(customProperties);
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
            _sgen.generateSource(filePath, _srcpackage);
        } catch (FileNotFoundException e) {
            String message = "XML Schema file \"" + _schemaFile.getAbsolutePath() + "\" not found.";
            log(message);
            throw new BuildException(message);
        } catch (Exception iox) {
            throw new BuildException(iox);
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
        if (_schemaFile == null && _schemaDir == null && _schemaFilesets.size() == 0) {
            throw new BuildException(NO_SCHEMA_MSG);
        }

        try {
            config();

            // Run source generator on file
            if (_schemaFile != null) {
                processFile(_schemaFile.getAbsolutePath());
            }

            // Run source generator on all files in directory
            if (_schemaDir != null && _schemaDir.exists() && _schemaDir.isDirectory()) {
                DirectoryScanner ds = this.getDirectoryScanner(_schemaDir);

                String[] files = ds.getIncludedFiles();
                for (int i = 0; i < files.length; i++) {
                    String filePath = _schemaDir.getAbsolutePath() + File.separator + files[i];
                    processFile(filePath);
                }
            }

            // Run source generator on all files in FileSet
            for (int i = 0; i < _schemaFilesets.size(); i++) {
                FileSet fs = (FileSet) _schemaFilesets.elementAt(i);
                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                File subdir = fs.getDir(getProject());

                String[] files = ds.getIncludedFiles();
                for (int j = 0; j < files.length; j++) {
                    String filePath = subdir.getAbsolutePath() + File.separator + files[j];
                    processFile(filePath);
                }
            }
        } finally {
            _sgen = null;
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Override Castor's SourceGenerator to inject exception handling.
     * Code based on castor-0.9.5.3-xml.jar.
     */
    private final class CastorSourceGeneratorWrapper extends SourceGenerator {
        /**
         * No-arg constructor.
         */
        public CastorSourceGeneratorWrapper() {
            super();
        }

        /**
         * Constructs a source generator with the provided FieldInfoFactory.
         * 
         * @param fieldInfoFactory A FieldInfoFactory to use for collections.
         */
        public CastorSourceGeneratorWrapper(final FieldInfoFactory fieldInfoFactory) {
            super(fieldInfoFactory);
        }

        /**
         * Constructs a source generator with the provided FieldInfoFactory and
         * binding file.
         * 
         * @param fieldInfoFactory a FieldInfoFactory to use for collections.
         * @param extendedBinding binding information for the code generator.
         */
        public CastorSourceGeneratorWrapper(
                final FieldInfoFactory fieldInfoFactory, final ExtendedBinding extendedBinding) {
            super(fieldInfoFactory, extendedBinding);
        }

        /**
         * Generate source. If anything goes wrong during generation of source a
         * BuildException will be thrown.
         *
         * @param source an individual schema to process.
         * @param packageName the package name for generated code.
         * 
         * @see org.exolab.castor.builder.SourceGenerator
         *      #generateSource(org.xml.sax.InputSource, java.lang.String)
         */
        public void generateSource(final InputSource source, final String packageName) {
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
                String msg = "Can't read input file " + source.getSystemId() + ".\n" + e;
                throw new BuildException(msg, e);
            } catch (SAXException e) {
                String msg = "Can't parse input file " + source.getSystemId() + ".\n" + e;
                throw new BuildException(msg, e);
            }
            Schema schema = schemaUnmarshaller.getSchema();
            try {
                generateSource(schema, packageName);
            } catch (Exception iox) {
                throw new BuildException(iox);
            }
        }
    }

    //--------------------------------------------------------------------------
}
