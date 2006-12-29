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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.builder.binding.BindingException;
import org.exolab.castor.builder.binding.BindingLoader;
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.binding.PackageType;
import org.exolab.castor.builder.binding.PackageTypeChoice;
import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.binding.types.BindingType;
import org.exolab.castor.builder.conflictresolution.WarningViaDialogClassNameCRStrategy;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.castor.core.exceptions.CastorRuntimeException;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.NestedIOException;
import org.exolab.castor.util.Version;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ContentModelGroup;
import org.exolab.castor.xml.schema.ContentType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SimpleContent;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Structure;
import org.exolab.castor.xml.schema.XMLType;
import org.exolab.castor.xml.schema.reader.Sax2ComponentReader;
import org.exolab.castor.xml.schema.reader.SchemaUnmarshaller;
import org.exolab.javasource.JClass;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXParseException;

/**
 * A Java Source generation tool which uses XML Schema definitions
 * to create an Object model.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a> - Main author.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a> - Contributions.
 * @author <a href="mailto:nsgreen@thazar.com">Nathan Green</a> - Contributions.
 * @version $Revision$ $Date: 2006-03-30 14:58:45 -0700 (Thu, 30 Mar 2006) $
 */
public class SourceGenerator extends BuilderConfiguration {
    
    /**
     * Jakarta's common-logging logger
     */
    private static final Log LOG = LogFactory.getLog(SourceGenerator.class);
    
    //-------------/
    //- Constants -/
    //-------------/
    /** The application name */
    static final String APP_NAME = "Castor";
    /** The application description */
    static final String APP_DESC = "XML data binder for Java";
    /** The application version */
    static final String VERSION = Version.VERSION;
    /** The application URI */
    static final String APP_URI = "http://www.castor.org";
    /** Warning message to remind users to create source code for imported schema. */
    private static final String IMPORT_WARNING
        = "Warning: Do not forget to generate source code for the following imported schema: ";

    //----------------------/
    //- Instance Variables -/
    //----------------------/

    /** Castor configuration */
    private final Configuration _config;
    /** The XMLBindingComponent used to create Java classes from an XML Schema */
    private final XMLBindingComponent _bindingComponent;
    /** Our object used to generate source for a single source file */
    private final SingleClassGenerator _singleClassGenerator;
    /** The field info factory. */
    private final FieldInfoFactory _infoFactory;
    /** Allows us to ask the user questions */
    private final ConsoleDialog _dialog;
    /** A vector that keeps track of all the schemas processed. */
    private final Vector _schemasProcessed = new Vector(7);

    /** True if we should suppress non-fatal warnings */
    private boolean _suppressNonFatalWarnings = false;
    /** Determines whether or not to print extra messages. */
    private boolean _verbose = false;
    /** A flag indicating whether or not to create
     *  descriptors for the generated classes. */
    private boolean _createDescriptors = true;
    /** A flag indicating whether or not to generate sources
     *  for imported XML Schemas. */
    private boolean _generateImported = false;
    /** The source factory. */
    private SourceFactory _sourceFactory = null;
    /** A flag to indicate that the mapping file should be generated. */
    private boolean _generateMapping = false;
    /** The name of the mapping file to create used with the gen-mapping flag. */
    private String  _mappingFilename = "mapping.xml";
    /** A flag indicating whether or not to generate XML marshalling
     *  framework specific methods. */
    private boolean _createMarshalMethods = true;
    /** A flag indicating whether or not to implement CastorTestable
     *  (used by the Castor Testing Framework). */
    private boolean _testable = false;
    /** A flag indicating that SAX1 should be used when generating the source. */
    private boolean _sax1 = false;
    /** A flag indicating that enumerated types should be constructed to perform
     *  case insensitive lookups based on the values. */
    private boolean _caseInsensitive = false;
    /** A flag indicating, if true, that source generation should fail on the
     * first error*/
    private boolean _failOnFirstError = false;

    /**
     * A GroupNaming helper class used to named anonymous groups.
     */
    private GroupNaming _groupNaming = null;

    /**
     * Strategy for name conflict resolution. 
     */
    private String _nameConflictStrategy = WarningViaDialogClassNameCRStrategy.NAME;

    /**
     * Creates a SourceGenerator using the default FieldInfo factory
     */
    public SourceGenerator() {
        this(null);
        _groupNaming = new GroupNaming();
    } //-- SourceGenerator

    /**
     * Creates a SourceGenerator using the specific field info Factory.
     *
     * @param infoFactory the FieldInfoFactory to use.
     */
    public SourceGenerator(final FieldInfoFactory infoFactory) {
        this(infoFactory, null);
        _groupNaming = new GroupNaming();
    }

    /**
     * Creates a SourceGenerator using the specific field info Factory and the
     * given Binding element.
     *
     * @param infoFactory the FieldInfoFactory to use.
     * @param binding the binding element to use.
     */
    public SourceGenerator(final FieldInfoFactory infoFactory, final ExtendedBinding binding) {
        super();

        _config = LocalConfiguration.getInstance();
        _dialog = new ConsoleDialog();
        _infoFactory = (infoFactory == null) ? new FieldInfoFactory() : infoFactory;

        super.load();
        
        _groupNaming = new GroupNaming();

        _singleClassGenerator = new SingleClassGenerator(_dialog, this, _nameConflictStrategy);
        _bindingComponent = new XMLBindingComponent(this, _groupNaming);
        setBinding(binding);
    } //-- SourceGenerator

    /**
     * Sets the filename of the mapping file.
     * @param filename filename of the mapping file
     */
    public void setMappingFilename(final String filename) {
        _mappingFilename = filename;
    }

    public void setNameConflictStrategy(final String nameConflictStrategy) {
        _nameConflictStrategy = nameConflictStrategy;
        _singleClassGenerator.setNameConflictStrategy(nameConflictStrategy);
    }
    /**
     * Returns the version number of this SourceGenerator
     *
     * @return the version number of this SourceGenerator
     */
    public static String getVersion() {
        return VERSION;
    } //-- getVersion

    /**
     * Set to true if SAX1 should be used in the marshal method
     * @param sax1 true if SAX1 should be used in the marshal method
     */
    public void setSAX1(final boolean sax1) {
        _sax1 = sax1;
    }

    /**
     * Set to true if enumerated type lookups should be performed in a case
     * insensitive manner.
     *
     * @param caseInsensitive when true, enumerated type lookups will be
     *        performed in a case insensitive manner.
     */
    public void setCaseInsensitive(final boolean caseInsensitive) {
        _caseInsensitive = caseInsensitive;
    }

    /**
     * If true, the source generator will fail on the first error encountered.
     * Otherwise, the source generator will continue on certain errors.
     *
     * @param failOnFirstError
     *            if true, the source generator will fail on the first error
     *            encountered.
     */
    public void setFailOnFirstError(final boolean failOnFirstError) {
        _failOnFirstError = failOnFirstError;
    }

    /**
     * Sets whether or not to suppress non-fatal warnings encountered during
     * source generation.
     * @param suppress true if non-fatal warnings should be suppressed.
     */
    public void setSuppressNonFatalWarnings(final boolean suppress) {
        _singleClassGenerator.setPromptForOverwrite(!suppress);
        _suppressNonFatalWarnings = suppress;
    } //-- setSuppressNonFatalWarnings

    /**
     * Sets whether or not the source code generator prints additional messages
     * during generating source code
     *
     * @param verbose
     *            a boolean, when true indicates to print additional messages
     */
    public void setVerbose(final boolean verbose) {
        _verbose = verbose;
    } //-- setVerbose

    /**
     * Sets whether or not to create ClassDescriptors for the generated classes.
     * By default, descriptors are generated.
     *
     * @param createDescriptors
     *            a boolean, when true indicates to generated ClassDescriptors
     *
     */
    public void setDescriptorCreation(final boolean createDescriptors) {
        _createDescriptors = createDescriptors;
        _singleClassGenerator.setDescriptorCreation(createDescriptors);
    } //-- setDescriptorCreation

    /**
     * Sets the destination directory.
     *
     * @param destDir the destination directory.
     */
    public void setDestDir(final String destDir) {
        _singleClassGenerator.setDestDir(destDir);
    }

    /**
     * Sets whether or not to create the XML marshalling framework specific
     * methods (marshall, unmarshall, validate) in the generated classes.
     * By default, these methods are generated.
     *
     * @param createMarshalMethods a boolean, when true indicates
     * to generated the marshalling framework methods
     *
     */
    public void setCreateMarshalMethods(final boolean createMarshalMethods) {
        _createMarshalMethods = createMarshalMethods;
    } //-- setCreateMarshalMethods

    /**
     * Sets whether or not to generate Java sources for imported XML Schema.
     * By default Java sources for imported XML schemas are not generated.
     *
     * @param generate true to generate the java classes for the imported XML Schema
     */
    public void setGenerateImportedSchemas(final boolean generate) {
        _generateImported = generate;
    }

    /**
     * Sets whether or not a mapping file should be generated, this is false by
     * default. Note that this will only be used when generation of descriptors
     * has been disabled.
     *
     * @param generateMapping
     *            a flag that indicates whether or not a mapping file should be
     *            generated.
     */
    public void setGenerateMappingFile(final boolean generateMapping) {
        _generateMapping = generateMapping;
    } //-- setGenerateMappingFile

    /**
     * Sets whether or not to implement CastorTestable
     *
     * @param testable
     *            a boolean, when true indicates to implement CastorTestable
     */
    public void setTestable(final boolean testable) {
        _testable = testable;
    } //-- setTestable

    /**
     * Sets the binding to use with this instance of the SourceGenerator.
     *
     * @param binding
     *            the binding to use, null indicates that the default binding
     *            will be used.
     */
    public void setBinding(final ExtendedBinding binding) {
        if (binding != null) {
            processNamespaces(binding.getPackage());
        }
        //--initialize the XMLBindingComponent
        _bindingComponent.setBinding(binding);
    } //-- setBinding

    /**
     * Sets the binding to use given the path name of a Castor Binding File.
     *
     * @param fileName the file that represents a Binding
     */
    public void setBinding(final String fileName) {
        try {
            ExtendedBinding binding = BindingLoader.createBinding(fileName);
            setBinding(binding);
        } catch (BindingException e) {
            String err = "Unable to load a binding file due to the following:\n"
                    + e.getMessage()
                    + "\nThe Source Generator will continue with no binding file.";
            _dialog.notify(err);
        }
    }

    /**
     * Sets the binding to use given an InputSource identifying
     * a Castor Binding File.
     *
     * @param source an InputSource identifying a Castor Binding File.
     */
    public void setBinding(final InputSource source) {
        try {
            ExtendedBinding binding = BindingLoader.createBinding(source);
            setBinding(binding);
        } catch (BindingException e) {
            String err = "unable to load a binding file due to the following:\n"
                    + e.getMessage()
                    + "\nThe Source Generator will continue with no binding file.";
            _dialog.notify(err);
        }
    }

    /**
     * Sets the line separator to use when printing the source code.
     * <p>
     * <B>Note:</B>This can be any string, so be careful. I recommend either
     * using the default or using one of the following:
     * <PRE>
     * windows systems use: "\r\n"
     * unix systems use: "\n"
     * mac systems use: "\r"
     * </PRE>
     *
     * @param lineSeparator
     *            the line separator to use when printing the source code. This
     *            method is useful if you are generating source on one platform,
     *            but will be compiling the source on a different platform.
     *
     */
    public void setLineSeparator(final String lineSeparator) {
        _singleClassGenerator.setLineSeparator(lineSeparator);
    } //-- setLineSeparator

    /**
     * Tests the org.exolab.castor.builder.javaclassmapping property for the 'element' value.
     *
     * @return True if the Source Generator is mapping schema elements to Java classes.
     */
    public boolean mappingSchemaElement2Java() {
        if (_bindingComponent != null) {
            ExtendedBinding binding = _bindingComponent.getBinding();
            if (binding != null) {
                BindingType type = binding.getDefaultBindingType();
                if (type != null) {
                    return (type.getType() == BindingType.ELEMENT_TYPE);
                }
            }
        }
        return super.mappingSchemaElement2Java();
    } //-- mappingSchemaElement2Java

    /**
     * Tests the org.exolab.castor.builder.javaclassmapping property for the 'type' value.
     *
     * @return True if the Source Generator is mapping schema types to Java classes.
     */
    public boolean mappingSchemaType2Java() {
        if (_bindingComponent != null) {
            ExtendedBinding binding = _bindingComponent.getBinding();
            if (binding != null) {
                BindingType type = binding.getDefaultBindingType();
                if (type != null) {
                    return (type.getType() == BindingType.TYPE_TYPE);
                }
            }
        }
        return super.mappingSchemaType2Java();
    } //-- mappingSchemaType2Java

    /**
     * Creates Java Source code (Object model) for the given XML Schema. If the
     * file exists, opens a FileReader and passes control to
     * {@link #generateSource(InputSource, String)}.
     *
     * @param filename
     *            the full path to the XML Schema definition
     * @param packageName
     *            the package for the generated source files
     * @throws IOException
     *             if an IOException occurs writing the new source files
     */
    public void generateSource(final String filename, final String packageName) throws IOException {
        final File schemaFile;
        if (filename.startsWith("./")) {
            schemaFile = new File(filename.substring(2));
        } else {
            schemaFile = new File(filename);
        }

        FileReader reader = new FileReader(schemaFile);

        try {
            InputSource source = new InputSource(reader);
            source.setSystemId(toURIRepresentation(schemaFile.getAbsolutePath()));
            generateSource(source, packageName);
        } finally {
            try { reader.close(); } catch (java.io.IOException iox) { }
        }
    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema. This
     * method just passes control to
     * {@link #generateSource(InputSource, String)}.
     *
     * @param reader
     *            the Reader with which to read the XML Schema definition. The
     *            caller should close the reader, since thie method will not do
     *            so.
     * @param packageName
     *            the package for the generated source files
     * @throws IOException
     *             if an IOException occurs writing the new source files
     */
    public void generateSource(final Reader reader, final String packageName) throws IOException {
        InputSource source = new InputSource(reader);
        generateSource(source, packageName);
    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema. Parses
     * the schema provided by the InputSource and then calls
     * {@link #generateSource(Schema, String)} to actually generate the source.
     *
     * @param source -
     *            the InputSource representing the XML schema.
     * @param packageName
     *            the package for the generated source files
     * @throws IOException
     *             if an IOException occurs writing the new source files
     */
    public void generateSource(final InputSource source, final String packageName)
                                                                   throws IOException {
        // -- get default parser from Configuration
        Parser parser = null;
        try {
            parser = _config.getParser();
        } catch (RuntimeException rte) {
            // ignore
        }

        if (parser == null) {
            _dialog.notify("fatal error: unable to create SAX parser.");
            return;
        }

        SchemaUnmarshaller schemaUnmarshaller = null;
        try {
           schemaUnmarshaller = new SchemaUnmarshaller();
        } catch (XMLException e) {
            //--The default constructor cannot throw exception so this should never happen
            //--just log the exception
            e.printStackTrace();
            System.exit(1);
        }

        Sax2ComponentReader handler = new Sax2ComponentReader(schemaUnmarshaller);
        parser.setDocumentHandler(handler);
        parser.setErrorHandler(handler);

        try {
            parser.parse(source);
        } catch (java.io.IOException ioe) {
            _dialog.notify("error reading XML Schema file");
            if (_failOnFirstError) {
                throw ioe;
            }
            return;
        } catch (org.xml.sax.SAXException sx) {
            Exception except = sx.getException();
            if (except == null) {
                except = sx;
            }

            if (except instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException) except;
                _dialog.notify("SAXParseException: " + spe);
                _dialog.notify(" - occured at line ");
                _dialog.notify(Integer.toString(spe.getLineNumber()));
                _dialog.notify(", column ");
                _dialog.notify(Integer.toString(spe.getColumnNumber()));
            } else {
                except.printStackTrace();
            }
            if (_failOnFirstError) {
                String msg = "Source Generator: schema parser threw an Exception";
                throw new CastorRuntimeException(msg, sx);
            }
            return;
        }

        Schema schema = schemaUnmarshaller.getSchema();
        generateSource(schema, packageName);
    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema.
     * Convenience methods exist if you don't have a
     * {@link org.exolab.castor.xml.schema.Schema} already parsed.
     *
     * @param schema
     *            the XML schema to generate the Java sources for.
     * @param packageName
     *            the package for the generated source files.
     * @throws IOException
     *             if this Exception occurs while generating source
     * @see #generateSource(String, String) to provide the schema filename
     * @see #generateSource(Reader, String) to provide a Reader for the schema
     * @see #generateSource(InputSource, String) to provide an InputSource for
     *      the schema
     */
    public void generateSource(final Schema schema, final String packageName) throws IOException {
        if (schema == null) {
            throw new IllegalArgumentException("The argument 'schema' must not be null.");
        }

        //--make sure the XML Schema is valid
        try {
            schema.validate();
        } catch (ValidationException ve) {
            String err = "The schema: " + schema.getSchemaLocation() + " is not valid.\n"
                         + ve.getMessage();
            throw new IllegalArgumentException(err);
        }

        // Now that we're ready to generate source and we know our configuration
        // has been fully parsed, create our SourceFactory.  (See CASTOR-1346.)
        // We will reuse this SourceFactory if we are invoked multiple times.
        if (_sourceFactory == null) {
            _sourceFactory = new SourceFactory(this, _infoFactory, _groupNaming);
            _sourceFactory.setCreateMarshalMethods(_createMarshalMethods);
            _sourceFactory.setTestable(_testable);
            _sourceFactory.setSAX1(_sax1);
            _sourceFactory.setCaseInsensitive(_caseInsensitive);
        }

        //-- create a new Source Generation State object for this invocation
        SGStateInfo sInfo = new SGStateInfo(schema, this);
        sInfo._packageName = packageName;
        sInfo.setDialog(_dialog);
        sInfo.setVerbose(_verbose);
        sInfo.setSuppressNonFatalWarnings(_suppressNonFatalWarnings);

        //--map the schemaLocation of the schema with the packageName defined
        if (packageName != null) {
            super.setLocationPackageMapping(schema.getSchemaLocation(), packageName);
        }

        //--We start with a blank list of schemas processed
        _schemasProcessed.clear();

        generateAllClassFiles(schema, sInfo);

        //-- TODO Cleanup integration (what does this comment mean?)
        if (!_createDescriptors && _generateMapping) {
            generateMappingFile(packageName, sInfo);
        }
    } //-- generateSource

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Generate all class files for the provided schema.  Before processing
     * the current schema, process the schemas imported by this one (unless our
     * configuration says not to).
     * 
     * @param schema the schema whose imported schemas to process
     * @param sInfo source generator state information
     * @throws IOException if this Exception occurs while processing an import schema
     */
    private void generateAllClassFiles(final Schema schema, final SGStateInfo sInfo)
                                                                 throws IOException {
        // Before processing the current schema, process its imported schemas
        if (!_suppressNonFatalWarnings || _generateImported) {
            processImportedSchemas(schema, sInfo);
        }

        //-- ** Generate code for all TOP-LEVEL structures **

        Enumeration structures;

        //-- handle all top-level element declarations
        for (structures = schema.getElementDecls(); structures.hasMoreElements(); ) {
            createClasses((ElementDecl) structures.nextElement(), sInfo);
        }

        //-- handle all top-level complextypes
        for (structures = schema.getComplexTypes(); structures.hasMoreElements(); ) {
            processComplexType((ComplexType) structures.nextElement(), sInfo);
        }

        //-- handle all top-level simpletypes
        for (structures = schema.getSimpleTypes(); structures.hasMoreElements(); ) {
            processSimpleType((SimpleType) structures.nextElement(), sInfo);
        }

        //-- handle all top-level groups
        for (structures = schema.getModelGroups(); structures.hasMoreElements(); ) {
            createClasses((ModelGroup) structures.nextElement(), sInfo);
        }

        //-- clean up any remaining JClasses which need printing
        _singleClassGenerator.processIfNotAlreadyProcessed(sInfo.keys(), sInfo);

        //-- handle cdr files
        for (Enumeration cdrFiles = sInfo.getCDRFilenames(); cdrFiles.hasMoreElements(); ) {
            String filename = (String) cdrFiles.nextElement();
            Properties props = sInfo.getCDRFile(filename);
            props.store(new FileOutputStream(new File(filename)), null);
        }
    } //-- createClasses

    /**
     * Look at each schema imported by the given schema.  Either warn that the
     * invoker needs to separately generate source from that schema or process
     * that schema, depending on settings.
     * @param schema the schema whose imported schemas to process
     * @param sInfo source generator state information
     * @throws IOException if this Exception occurs while processing an import schema
     */
    private void processImportedSchemas(final Schema schema, final SGStateInfo sInfo)
                                                                    throws IOException {
        Enumeration enumeration = schema.getImportedSchema();
        while (enumeration.hasMoreElements()) {
            Schema importedSchema = (Schema) enumeration.nextElement();
            if (!_generateImported) {
                LOG.warn(IMPORT_WARNING + importedSchema.getSchemaLocation());
            } else {
                _schemasProcessed.add(schema);
                if (!_schemasProcessed.contains(importedSchema)) {
                    SGStateInfo importedSInfo  = new SGStateInfo(importedSchema, this);
                    importedSInfo._packageName = sInfo._packageName;
                    generateAllClassFiles(importedSchema, importedSInfo);

                    //--'store' the imported JClass instances
                    sInfo.storeImportedSourcesByName(importedSInfo.getSourcesByName());
                    sInfo.storeImportedSourcesByName(importedSInfo.getImportedSourcesByName());
                    //--discard the SGStateInfo
                    importedSInfo = null;
                }
            }
        }
    }

    /**
     * Generates the mapping file.
     * @param packageName Package name to be generated
     * @param sInfo Source Generator current state
     * @throws IOException if this Exception occurs while generating the mapping file
     */
    private void generateMappingFile(final String packageName, final SGStateInfo sInfo)
                                                                      throws IOException {
        String pkg = (packageName != null) ? packageName : "";
        MappingRoot mapping = sInfo.getMapping(pkg);
        if (mapping == null) {
            return;
        }

        FileWriter writer = new FileWriter(_mappingFilename);
        try {
            Marshaller marshaller = new Marshaller(writer);
            marshaller.setSuppressNamespaces(true);
            marshaller.marshal(mapping);
        } catch (Exception ex) {
            throw new NestedIOException(ex);
        } finally {
            writer.flush();
            writer.close();
        }
    }

    /**
     * Processes the given Element declaration and creates all necessary classes
     * to support it.
     * 
     * @param elementDecl
     *            the Element declaration to process
     * @param sInfo
     *            our state information
     * @throws IOException
     *             if this exception occurs while writing source files
     */
    private void createClasses(final ElementDecl elementDecl, final SGStateInfo sInfo)
                                                                     throws IOException {
        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS || elementDecl == null) {
            return;
        }

        //-- when mapping schema types, only interested in producing classes
        //-- for elements with anonymous complex types
        XMLType xmlType = elementDecl.getType();
        if (mappingSchemaType2Java()) {
            if (elementDecl.isReference() || ((xmlType != null) && (xmlType.getName() != null))) {
                return;
            }
        }

        //--create component
        _bindingComponent.setView(elementDecl);

        //-- already processed --> just return
        ClassInfo cInfo = sInfo.resolve(elementDecl);
        if (cInfo != null && cInfo.getJClass() != null) {
            JClass jClass = cInfo.getJClass();
            if (sInfo.processed(jClass)) {
                return;
            }
            jClass = null;
        }

        //-- No type definition
        if (xmlType == null) {
            if (sInfo.verbose()) {
                String msg = "No type found for element: " + elementDecl.getName();
                sInfo.getDialog().notify(msg);
            }
            return;
        } else if (xmlType.isComplexType()) {
            //-- ComplexType
            JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
            if (!_singleClassGenerator.process(classes, sInfo)) {
                return;
            }

            //only create classes for types that are not imported
            if (xmlType.getSchema() == _bindingComponent.getSchema()) {
                processComplexType((ComplexType) xmlType, sInfo);
            }
        } else if (xmlType.isSimpleType()) {
            //-- SimpleType
            processSimpleType((SimpleType) xmlType, sInfo);
        } else {
            //-- AnyType
            //-- no processing needed for 'anyType'
        }
    }  //-- createClasses

    /**
     * Processes the given Group and creates all necessary classes to support
     * it.
     * 
     * @param group
     *            the Group to process
     * @param sInfo
     *            our state information
     * @throws IOException
     *             if this exception occurs while writing source files
     */
    private void createClasses(final Group group, final SGStateInfo sInfo)
                                                         throws IOException {
        if (group == null) {
            return;
        }

        //-- don't generate classes for empty groups
        if (group.getParticleCount() == 0) {
            if (group instanceof ModelGroup) {
                ModelGroup mg = (ModelGroup) group;
                if (mg.isReference()) {
                    mg = mg.getReference();
                    if (mg.getParticleCount() == 0) {
                        return;
                    }
                }
            } else {
                return;
            }
        }

        _bindingComponent.setView(group);

        JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
        processContentModel(group, sInfo);
        _singleClassGenerator.process(classes, sInfo);
    } //-- createClasses

    /**
     * Processes the given ComplexType and creates all necessary classes to
     * support it
     *
     * @param complexType
     *            the ComplexType to process
     * @param sInfo
     *            our state information
     * @throws IOException if this exception occurs while writing source files
     */
    private void processComplexType(final ComplexType complexType, final SGStateInfo sInfo)
                                                                           throws IOException {
        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS || complexType == null) {
            return;
        }

        _bindingComponent.setView(complexType);

        ClassInfo classInfo = sInfo.resolve(complexType);
        if (classInfo == null) {
            //-- handle top-level complextypes
            if (complexType.isTopLevel()) {
                JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
                if (!_singleClassGenerator.process(classes, sInfo)) {
                    return;
                }
            }

            //-- process AttributeDecl
            processAttributes(complexType, sInfo);

            //--process content type if necessary
            ContentType temp = complexType.getContentType();
            if (temp.getType() == ContentType.SIMPLE) {
                processSimpleType(((SimpleContent) temp).getSimpleType(), sInfo);
            }

            //-- process ContentModel
            processContentModel(complexType, sInfo);
        } else {
            JClass jClass = classInfo.getJClass();
            if (!sInfo.processed(jClass)) {
                //-- process AttributeDecl
                processAttributes(complexType, sInfo);
                //-- process ContentModel
                processContentModel(complexType, sInfo);
                _singleClassGenerator.process(jClass, sInfo);
            }
        }
    } //-- processComplexType

    /**
     * Processes the attribute declarations for the given complex type
     *
     * @param complexType
     *            the ComplexType containing the attribute declarations to
     *            process.
     * @param sInfo
     *            the current source generator state information
     * @throws IOException if this Exception occurs while generating source files
     */
    private void processAttributes(final ComplexType complexType, final SGStateInfo sInfo)
                                                                         throws IOException {
        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS || complexType == null) {
            return;
        }

        Enumeration enumeration = complexType.getAttributeDecls();
        while (enumeration.hasMoreElements()) {
            AttributeDecl attribute = (AttributeDecl) enumeration.nextElement();
            processSimpleType(attribute.getSimpleType(), sInfo);
        }
    } //-- processAttributes

    /**
     * Processes the given ContentModelGroup
     *
     * @param cmGroup the ContentModelGroup to process
     * @param sInfo the current source generator state information
     * @throws IOException if this Exception occurs while generating source files
     */
    private void processContentModel(final ContentModelGroup cmGroup, final SGStateInfo sInfo)
                                                                             throws IOException {
        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS || cmGroup == null) {
            return;
        }

        //Some special code to handle the fact that the enumerate method will simply skip
        //the first group is the number of particle is one

        Enumeration enumeration = cmGroup.enumerate();

        while (enumeration.hasMoreElements()) {
            Structure struct = (Structure) enumeration.nextElement();
            switch(struct.getStructureType()) {
                case Structure.ELEMENT:
                    ElementDecl eDecl = (ElementDecl) struct;
                    if (eDecl.isReference()) {
                        continue;
                    }
                    createClasses(eDecl, sInfo);
                    break;
                case Structure.GROUP:
                    processContentModel((Group) struct, sInfo);
                    //handle nested groups
                    if (!((cmGroup instanceof ComplexType) || (cmGroup instanceof ModelGroup))) {
                        createClasses((Group) struct, sInfo);
                    }
                    break;
                default:
                    break;
            }
        }
    } //-- processContentModel

    /**
     * Handle simpleTypes
     *
     * @param simpleType the SimpleType to be processed
     * @param sInfo the current source generator state information
     * @throws IOException if this Exception occurs while generating source files
     */
    private void processSimpleType(final SimpleType simpleType, final SGStateInfo sInfo)
                                                                       throws IOException {
        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS || simpleType == null
                || simpleType.getSchema() != sInfo.getSchema()) {
            return;
        }

        //-- Right now the only time we actually generate source for a simpletype is
        //-- when it's an enumeration
        if (simpleType.hasFacet(Facet.ENUMERATION)) {
            ClassInfo classInfo = sInfo.resolve(simpleType);
            if (classInfo == null) {
                JClass jClass = _sourceFactory.createSourceCode(_bindingComponent.getBinding(), simpleType, sInfo);
                _singleClassGenerator.process(jClass, sInfo);
            } else {
                JClass jClass = classInfo.getJClass();
                _singleClassGenerator.process(jClass, sInfo);
            }
        }
    } //-- processSimpleType

   /**
     * Called by setBinding to fill in the mapping between namespaces and Java
     * packages.
     *
     * @param packages
     *            the array of package element
     */
    private void processNamespaces(final PackageType[] packages) {
        if (packages.length == 0) {
            return;
        }

        for (int i = 0; i < packages.length; i++) {
            PackageType temp = packages[i];
            PackageTypeChoice choice = temp.getPackageTypeChoice();
            if (choice.getNamespace() != null) {
                super.setNamespacePackageMapping(choice.getNamespace(), temp.getName());
            } else if (choice.getSchemaLocation() != null) {
                //1--Handle relative locations
                String tempLocation = choice.getSchemaLocation();
                String currentDir = System.getProperty("user.dir");
                currentDir = currentDir.replace('\\', '/');
                if (tempLocation.startsWith("./")) {
                    tempLocation = tempLocation.substring(1);
                    tempLocation = currentDir + tempLocation;
                } else if (tempLocation.startsWith("../")) {
                     tempLocation = tempLocation.substring(3);
                     int lastDir = currentDir.lastIndexOf('/');
                     currentDir = currentDir.substring(0, lastDir + 1);
                     tempLocation = currentDir + tempLocation;
                }
                super.setLocationPackageMapping(tempLocation, temp.getName());
                currentDir = null;
                tempLocation = null;
            }
        }
    }

    /**
     * Returns a string which is the URI of a file.
     * <ul>
     * <li>file:///DOSpath</li>
     * <li>file://UnixPath</li>
     * </ul>
     * No validation is done to check whether the file exists or not. This
     * method will be no longer used when the JDK URL.toString() is fixed.
     *
     * @param path The absolute path of the file.
     * @return A string representing the URI of the file.
     */
    public static String toURIRepresentation(final String path) {
        String result = path;
        if (!new File(result).isAbsolute()) {
           throw new IllegalArgumentException("The parameter must represent an absolute path.");
        }

        if (File.separatorChar != '/') {
            result = result.replace(File.separatorChar, '/');
        }

        if (result.startsWith("/")) {
            result = "file://" + result;    /*Unix platform*/
        } else {
            result = "file:///" + result;   /*DOS platform*/
        }

        return result;
    }

    /**
     * For backwards compability, when we are called as the main() routine,
     * delegate the command-line usage to the proper class.
     *
     * @param args our command line arguments.
     * @deprecated Please use {@link SourceGeneratorMain#main(String[])}
     */
    public static void main(final String[] args) {
        LOG.info("org.exolab.castor.builder.SourceGenerator.main() is deprecated. "
                + "Please use org.exolab.castor.builder.SourceGeneratorMain#main() instead.");

        SourceGeneratorMain.main(args);
    }

} //-- SourceGenerator
