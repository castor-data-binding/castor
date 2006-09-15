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

//--Binding file imports
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.binding.PackageType;
import org.exolab.castor.builder.binding.PackageTypeChoice;
import org.exolab.castor.builder.binding.BindingException;
import org.exolab.castor.builder.binding.BindingLoader;
import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.binding.types.BindingType;

//--Castor SOM import
import org.exolab.castor.xml.schema.reader.*;
import org.exolab.castor.xml.schema.*;

import org.exolab.javasource.*;

//--Utils imports
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.castor.util.CommandLineOptions;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.NestedIOException;
import org.exolab.castor.util.Version;

import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLException;
import org.xml.sax.*;

//--Java IO imports
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//--Java util imports
import java.util.Enumeration;
//import java.util.Hashtable;
import java.util.Vector;
import java.util.Properties;

/**
 * A Java Source generation tool which uses XML Schema definitions
 * to create an Object model.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a> - Main author.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a> - Contributions.
 * @author <a href="mailto:nsgreen@thazar.com">Nathan Green</a> - Contributions.
 * @version $Revision$ $Date: 2006-03-30 14:58:45 -0700 (Thu, 30 Mar 2006) $
**/
public class SourceGenerator extends BuilderConfiguration {
    //-------------/
    //- Constants -/
    //-------------/
    /**
     * The application name
    **/
    static final String APP_NAME = "Castor";

    /**
     * The application description
    **/
    static final String APP_DESC = "XML data binder for Java";

    /**
     * The application version
    **/

    static final String VERSION = Version.VERSION;

    /**
     * The application URI
    **/
    static final String APP_URI = "http://www.castor.org";

    /**
     * The default code header,
     * please leave "$" and "Id" separated with "+" so that the CVS server
     * does not expand it here.
    **/
    private static final String DEFAULT_HEADER =
        "This class was automatically generated with \n"+"<a href=\"" +
        APP_URI + "\">" + APP_NAME + " " + VERSION +
        "</a>, using an XML Schema.\n$" + "Id"+"$";

    private static final String CDR_FILE = ".castor.cdr";
    
    //-------------------------/
    //- Command line messages -/
    //------------------------/
    /**
     * Message used when descriptor creation is disabled
    **/
    private static final String DISABLE_DESCRIPTORS_MSG
        = "Disabling generation of Class descriptors";

    /**
     * Message used when marshalling methods creation is disabled
    **/
    private static final String DISABLE_MARSHALL_MSG
        = "Disabling generation of Marshalling framework methods (marshall, unmarshall, validate).";

	/**
     * Message used when implementing CastorTestable
    **/
    private static final String CASTOR_TESTABLE_MSG
        = "The generated classes will implement org.exolab.castor.tests.CastorTestable";

    /**
     * Message used when using SAX1
     */
    private static final String SAX1_MSG
        = "The generated classes will use SAX 1";
    /**
     * Warning message to remind users to create source
     * code for imported schema.
    **/
    private static final String IMPORT_WARNING
        = "Warning: Do not forget to generate source code for the following imported schema: ";
        
    private static final String GENERATE_IMPORT_MSG 
        = "Imported XML Schemas will be processed automatically.";
    
    private static final String CASE_INSENSITIVE_MSG = 
        "The generated classes will use a case insensitive method for looking up enumerated type values.";
          

    /**
     * Castor configuration
     */
    private Configuration _config = null;
    
    /**
     * The XMLBindingComponent used to create Java classes from an XML Schema
     */
    private XMLBindingComponent _bindingComponent = null;



    //----------------------/
    //- Instance Variables -/
    //----------------------/

    private String _lineSeparator = null;
    
    private JComment _header = null;

    private boolean _warnOnOverwrite = true;
    
	private boolean _suppressNonFatalWarnings = false;

    /** Determines whether or not to print extra messages. */
    private boolean _verbose = false;

    private String  _destDir = null;

    /** A flag indicating whether or not to create
     *  descriptors for the generated classes. */
    private boolean _createDescriptors = true;

    /** A flag indicating whether or not to generate sources 
     *  for imported XML Schemas. */
    private boolean _generateImported = false;

    /** The DescriptorSourceFactory instance. */
    private DescriptorSourceFactory _descSourceFactory = null;
    
    private MappingFileSourceFactory _mappingSourceFactory = null;
    
    /** The field info factory. */
    private FieldInfoFactory _infoFactory = null;

    /** The source factory. */
    private SourceFactory _sourceFactory = null;

   
    private ConsoleDialog _dialog = null;
    
    /** A vector that keeps track of all the schemas processed. */
    private Vector _schemasProcessed = null;

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

    /**
     * Creates a SourceGenerator using the default FieldInfo factory
     */
    public SourceGenerator() {
        //-- use default factory
        this(null);
    } //-- SourceGenerator

    /**
     * Creates a SourceGenerator using the specific field info Factory.
     *
     * @param infoFactory the FieldInfoFactory to use.
    */
    public SourceGenerator(FieldInfoFactory infoFactory) {
        this(infoFactory, null);
    }

    /**
     * Creates a SourceGenerator using the specific field info Factory and the
     * given Binding element .
     *
     * @param infoFactory the FieldInfoFactory to use.
     * @param binding the binding element to use.
    */
    public SourceGenerator(FieldInfoFactory infoFactory, ExtendedBinding binding) {
        super();

        _config = LocalConfiguration.getInstance();
        
        _dialog = new ConsoleDialog();

        if (infoFactory == null)
            _infoFactory = new FieldInfoFactory();
        else
            _infoFactory = infoFactory;
        
        load();
        
        // do this later (CASTOR-1346)
        // _sourceFactory = new SourceFactory(this, _infoFactory);
        _descSourceFactory = new DescriptorSourceFactory(this);
        _mappingSourceFactory = new MappingFileSourceFactory(this);
        
        _header = new JComment(JComment.HEADER_STYLE);
        _header.appendComment(DEFAULT_HEADER);
        _bindingComponent = new XMLBindingComponent(this);
        //--set the binding 
        setBinding(binding);

    } //-- SourceGenerator

    /**
     * Creates Java Source code (Object model) for the given XML Schema.
     *
     * @param schema the XML schema to generate the Java sources for.
     * @param packageName the package for the generated source files.
    **/
    public void generateSource(Schema schema, String packageName) 
        throws IOException
    {
        // by this time the properties have been set. if the sourceFactory
        // is null then create one using this for configuration. if this is
        // done before reading in the configuration there is a problem (CASTOR-1346)
        if (_sourceFactory == null) {
            _sourceFactory = new SourceFactory(this, _infoFactory);
            _sourceFactory.setCreateMarshalMethods(_createMarshalMethods);
            _sourceFactory.setTestable(_testable);
            _sourceFactory.setSAX1(_sax1);
            _sourceFactory.setCaseInsensitive(_caseInsensitive);
        }
 
        if (schema == null) {
            String err = "The argument 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        //-- reset the vector, most of the time only one schema to process
        SGStateInfo sInfo = new SGStateInfo(schema, this);
        //--make sure the XML Schema is valid
        try {
            schema.validate();
        } catch (ValidationException ve) {
            String err = "The schema:"+schema.getSchemaLocation()+" is not valid.\n";
            err += ve.getMessage();
            throw new IllegalArgumentException(err);
        }
                
        //--map the schemaLocation of the schema with the packageName defined
        if (packageName != null) {
            setLocationPackageMapping(schema.getSchemaLocation(), packageName);
        }
        
        sInfo.packageName = packageName;
        sInfo.setDialog(_dialog);
        sInfo.setPromptForOverwrite(_warnOnOverwrite);
        sInfo.setVerbose(_verbose);
        sInfo.setSuppressNonFatalWarnings(_suppressNonFatalWarnings);

        createClasses(schema, sInfo);
        
        //-- TODO Cleanup integration :
        if (!_createDescriptors && _generateMapping) {
            String pkg = (packageName != null) ? packageName : "";
            MappingRoot mapping = sInfo.getMapping(pkg);
            if (mapping != null) {
                FileWriter writer = new FileWriter(_mappingFilename);
                Marshaller mars = new Marshaller(writer);
                mars.setSuppressNamespaces(true);
                try {
                    mars.marshal(mapping);
                }
                catch(Exception ex) {
                    throw new NestedIOException(ex);
                }
                writer.flush();
                writer.close();
            }
        }
        //--reset the vector of schemas processed
        _schemasProcessed = null;
    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema
     *
     * @param source - the InputSource representing the XML schema.
     * @param packageName the package for the generated source files
     */
    public void generateSource(InputSource source, String packageName) 
        throws IOException
    {

        //-- get default parser from Configuration
        Parser parser = null;
        try {
	        parser = _config.getParser();
        } catch(RuntimeException rte) {}
        if (parser == null) {
            _dialog.notify("fatal error: unable to create SAX parser.");
            return;
        }

        SchemaUnmarshaller schemaUnmarshaller = null;
        try {
           schemaUnmarshaller = new SchemaUnmarshaller();
        } catch (XMLException e) {
            //--The default constructor cannot throw
            //--exception so this should never happen
            //--just log the exception
            e.printStackTrace();
            System.exit(1);
        }

        Sax2ComponentReader handler = new Sax2ComponentReader(schemaUnmarshaller);
        parser.setDocumentHandler(handler);
        parser.setErrorHandler(handler);

        try {
            parser.parse(source);
        } catch(java.io.IOException ioe) {
            _dialog.notify("error reading XML Schema file");
            return;
        } catch(org.xml.sax.SAXException sx) {

            Exception except = sx.getException();
            if (except == null) except = sx;

            if (except instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException)except;
                _dialog.notify("SAXParseException: " + spe);
                _dialog.notify(" - occured at line ");
                _dialog.notify(Integer.toString(spe.getLineNumber()));
                _dialog.notify(", column ");
                _dialog.notify(Integer.toString(spe.getColumnNumber()));
            }
            else except.printStackTrace();
            return;
        }

        Schema schema = schemaUnmarshaller.getSchema();
        generateSource(schema, packageName);

    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema.
     *
     * @param reader the Reader with which to read the XML Schema definition.
     * The caller should close the reader, since thie method will not do so.
     * @param packageName the package for the generated source files
    **/
    public void generateSource(Reader reader, String packageName) 
        throws IOException
    {
        InputSource source = new InputSource(reader);
        generateSource(source, packageName);
    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema.
     *
     * @param filename the full path to the XML Schema definition
     * @param packageName the package for the generated source files
    **/
    public void generateSource(String filename, String packageName)
        throws FileNotFoundException, IOException
    {
        //--basic cleanup
        if (filename.startsWith("./"))
             filename = filename.substring(2);
        FileReader reader = new FileReader(filename);
        InputSource source = new InputSource(reader);
        source.setSystemId(toURIRepresentation((new File(filename)).getAbsolutePath()));
        generateSource(source, packageName);
        try {
            reader.close();
        } catch(java.io.IOException iox) {}


    } //-- generateSource


    /**
     * Returns the version number of this SourceGenerator
     *
     * @return the version number of this SourceGenerator
    **/
    public static String getVersion() {
        return VERSION;
    } //-- getVersion

    /**
     * Set to true if SAX1 should be used in the marshall method
     */
    public void setSAX1(boolean sax1) {
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

    public void setSuppressNonFatalWarnings(boolean suppress) {
        _warnOnOverwrite = (!suppress);
		_suppressNonFatalWarnings = suppress;
    } //-- setSuppressNonFatalWarnings

    /**
     * Sets whether or not the source code generator prints
     * additional messages during generating source code
     * @param verbose a boolean, when true indicates to
     * print additional messages
    **/
    public void setVerbose(boolean verbose) {
        _verbose = verbose;
    } //-- setVerbose

    /**
     * Sets whether or not to create ClassDescriptors for
     * the generated classes. By default, descriptors are
     * generated.
     *
     * @param createDescriptors a boolean, when true indicates
     * to generated ClassDescriptors
     *
    **/
    public void setDescriptorCreation(boolean createDescriptors) {
        _createDescriptors = createDescriptors;
    } //-- setDescriptorCreation

    /**
     * Sets the destination directory.
     * 
     * @param destDir the destination directory.
     */
    public void setDestDir(String destDir) {
       _destDir = destDir;
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
    public void setCreateMarshalMethods(boolean createMarshalMethods) {
        _createMarshalMethods = createMarshalMethods;
    } //-- setCreateMarshalMethods
    
    /**
     * Sets whether or not to generate Java sources for imported XML Schema.
     * By default Java sources for imported XML schemas are not generated.
     * 
     * @param generate true to generate the java classes for the imported XML Schema
     */
    public void setGenerateImportedSchemas(boolean generate) {
        _generateImported = generate;
    }
    
    /**
     * Sets whether or not a mapping file should be generated, this
     * is false by default. Note that this will only be used
     * when generation of descriptors has been disabled.
     * 
     * @param generateMapping a flag that indicates whether or
     * not a mapping file should be generated.
     */
    public void setGenerateMappingFile(boolean generateMapping) 
    {
        _generateMapping = generateMapping;
    } //-- setGenerateMappingFile

   /**
     * Sets whether or not to implement CastorTestable
	 *
     * @param testable a boolean, when true indicates
     * to implement CastorTestable
     */
    public void setTestable(boolean testable) {
        _testable = testable;
    } //-- setTestable

   /**
    * Sets the binding to use with this instance of the SourceGenerator.
    *
    * @param binding the binding to use, null indicates that the default
    * binding will be used.
    */
    public void setBinding(ExtendedBinding binding) {
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
    public void setBinding(String fileName) {
        try {
            ExtendedBinding binding = BindingLoader.createBinding(fileName);
            setBinding(binding);
        } catch (BindingException e) {
           //log these messages
            String err= "unable to load a binding file due to the following:\n";
            err +=e.getMessage();
            err += "\nThe Source Generator will continue with no binding file.";
            _dialog.notify(err);
        }
    }

   /**
    * Sets the binding to use given an InputSource identifying
    * a Castor Binding File.
    *
    * @param source an InputSource identifying a Castor Binding File.
    */
    public void setBinding(InputSource source) {
        try {
            ExtendedBinding binding = BindingLoader.createBinding(source);
            setBinding(binding);
        } catch (BindingException e) {
           //log these messages
            String err= "unable to load a binding file due to the following:\n";
            err +=e.getMessage();
            err += "\nThe Source Generator will continue with no binding file.";
            _dialog.notify(err);
        }
    }

   //////////////////
   //  MAIN METHOD //
   //////////////////
    /**
     * main class used for command line invocation
     * @param args the String[] consisting of the command line arguments
    **/
    public static void main(String[] args) {


        CommandLineOptions allOptions = new CommandLineOptions();

        //-- filename flag
        allOptions.addFlag("i", "filename", "Sets the input filename");

        //-- package name flag
        allOptions.addFlag("package", "package-name", "Sets the package name", true);

        //-- destination directory
        String desc = "Sets the destination output directory";
        allOptions.addFlag("dest", "dest-dir", desc, true);

        //-- line break flag
        desc = "Sets the line separator style for the desired platform";
        allOptions.addFlag("line-separator", "( unix | mac | win)", desc, true);

        //-- Force flag
        desc = "Suppresses non fatal warnings, such as overwriting files.";
        allOptions.addFlag("f", "", desc, true);

        //-- Help flag
        desc = "Displays this help screen.";
        allOptions.addFlag("h", "", desc, true);

        //-- verbose flag
        desc = "Prints out additional messages when creaing source";
        allOptions.addFlag("verbose", "", desc, true);

        //-- no descriptors flag
        desc = "Disables the generation of the Class descriptors";
        allOptions.addFlag("nodesc", "", desc, true);
        
        //-- no descriptors flag
        desc = "Indicates that a mapping file should be generated";
        allOptions.addFlag("gen-mapping", "filename", desc, true);
        

        //-- source generator types name flag
        desc = "Sets the source generator types name (SGTypeFactory)";
        allOptions.addFlag("types", "types", desc, true);

        //-- XXX maintained temporarily
        allOptions.addFlag("type-factory", "classname", "", true);

        //-- no marshalling framework methods
		desc = "Disables the generation of the methods specific to the XML marshalling framework";
		allOptions.addFlag("nomarshall","",desc,true);

        //-- implements org.exolab.castor.tests.CastorTestable?
		desc = "Implements some specific methods to allow the generated classes to be used with Castor Testing Framework";
		allOptions.addFlag("testable","",desc,true);

        //-- use SAX1?
		desc = "Uses SAX 1 in the generated code.";
		allOptions.addFlag("sax1","",desc,true);

        //-- Source Generator Binding
        desc = "Sets the Source Generator Binding File name";
        allOptions.addFlag("binding-file","filename",desc,true);
        
        //-- Generates sources for imported XML Schemas
        desc = "Generates sources for imported XML schemas";
        allOptions.addFlag("generateImportedSchemas","",desc,true);
        
        //-- Sets enumerated type to use a case insensitive lookup
        desc = "Sets enumerated types to use a case insensitive lookup";
        allOptions.addFlag("case-insensitive", "", desc);

        //-- Process the specified command line options
        Properties options = allOptions.getOptions(args);

        //-- check for help option
        if (options.getProperty("h") != null) {
            PrintWriter pw = new PrintWriter(System.out, true);
            allOptions.printHelp(pw);
            pw.flush();
            return;
        }

        String  schemaFilename  = options.getProperty("i");
        String  packageName     = options.getProperty("package");
        String  lineSepStyle    = options.getProperty("line-separator");
        boolean force          = (options.getProperty("f") != null);
        String  typeFactory     = options.getProperty("types");
        boolean verbose        = (options.getProperty("verbose") != null);

        if (schemaFilename == null) {
            System.out.println(APP_NAME);
            allOptions.printUsage(new PrintWriter(System.out));
            return;
        }

        // -- XXX maintained temporarily
        if (typeFactory == null)
            typeFactory = options.getProperty("type-factory");

        String lineSep = System.getProperty("line.separator");
        if (lineSepStyle != null) {
            if ("win".equals(lineSepStyle)) {
                System.out.println(" - using Windows style line separation.");
                lineSep = "\r\n";
            }
            else if ("unix".equals(lineSepStyle)) {
                System.out.println(" - using UNIX style line separation.");
                lineSep = "\n";
            }
            else if ("mac".equals(lineSepStyle)) {
                System.out.println(" - using Macintosh style line separation.");
                lineSep = "\r";
            }
            else {
                System.out.print("- invalid option for line-separator: ");
                System.out.println(lineSepStyle);
                System.out.println("-- using default line separator for this platform");
            }
        }

        SourceGenerator sgen = null;
        if (typeFactory != null) {
            //--Backward compatibility
            if (typeFactory.equals("j2"))
                typeFactory = "arraylist";

            try {

                FieldInfoFactory factory = new FieldInfoFactory(typeFactory);
                sgen = new SourceGenerator(factory);
            }
            catch(Exception x) {
                //--one might want to use its own FieldInfoFactory
                try {
                    sgen = new SourceGenerator((FieldInfoFactory)Thread.currentThread().getContextClassLoader().loadClass( typeFactory).newInstance());
                } catch (Exception e) {
                
                    System.out.print("- invalid option for types: ");
                    System.out.println(typeFactory);
                    System.out.println(x);
                    System.out.println("-- using default source generator types");
                    sgen = new SourceGenerator(); // default
                }
            }
        }
        else {
            sgen = new SourceGenerator(); // default
        }

        sgen.setDestDir(options.getProperty("dest"));
        sgen.setLineSeparator(lineSep);
        sgen.setSuppressNonFatalWarnings(force);
        sgen.setVerbose(verbose);

        if (force)
            System.out.println("-- Suppressing non fatal warnings.");

        if (options.getProperty("nodesc") != null) {
            sgen.setDescriptorCreation(false);
            System.out.print("-- ");
            System.out.println(DISABLE_DESCRIPTORS_MSG);
		}
        
        
        if (options.getProperty("gen-mapping") != null) {
            sgen.setGenerateMappingFile(true);
            String filename = options.getProperty("gen-mapping");
            if (filename.length() > 0) {
                sgen._mappingFilename = filename;
            }
            System.out.print("-- generating mapping file: " + filename);
        }

		if (options.getProperty("nomarshall") != null) {
		    sgen.setCreateMarshalMethods(false);
		    System.out.print("-- ");
            System.out.println(DISABLE_MARSHALL_MSG);
		}

        if (options.getProperty("testable") != null) {
		    sgen.setTestable(true);
		    System.out.print("-- ");
            System.out.println(CASTOR_TESTABLE_MSG);
		}

        if (options.getProperty("sax1") != null) {
            sgen.setSAX1(true);
            System.out.print("-- ");
            System.out.println(SAX1_MSG);
        }
        
        if (options.getProperty("case-insensitive") != null) {
            sgen.setCaseInsensitive(true);
            System.out.print("-- ");
            System.out.println(CASE_INSENSITIVE_MSG);
        }

        if (options.getProperty("binding-file") != null) {
            ExtendedBinding binding = null;
            try {
               binding =  BindingLoader.createBinding(options.getProperty("binding-file"));
            } catch (BindingException e) {
                 System.out.print("--");
                 System.out.println("Unable to load a binding file due to the following Exception:");
                 e.printStackTrace();
                 System.out.println("-- No binding file will be used");
            }
            sgen.setBinding(binding);
        }
        
        if (options.getProperty("generateImportedSchemas") != null) {
            sgen.setGenerateImportedSchemas(true);
            System.out.print("-- ");
            System.out.println(GENERATE_IMPORT_MSG);
        }




        try {
            sgen.generateSource(schemaFilename, packageName);
        } 
        catch(Exception ex) {
            ex.printStackTrace();
        }

    } //-- main

    /**
     * Sets the line separator to use when printing the source code
     * @param lineSeparator the line separator to use when printing
     * the source code. This method is useful if you are generating
     * source on one platform, but will be compiling the source
     * on a different platform.
     * <BR />
     * <B>Note:</B>This can be any string, so be careful. I recommend
     * either using the default or using one of the following:<BR />
     * <PRE>
     *   windows systems use: "\r\n"
     *   unix systems use: "\n"
     *   mac systems use: "\r"
     * </PRE>
    **/
    public void setLineSeparator(String lineSeparator) {
        _lineSeparator = lineSeparator;
    } //-- setLineSeparator

    //-------------------/
    //- Private Methods -/
    //-------------------/

    private void createClasses(Schema schema, SGStateInfo sInfo) 
        throws IOException
    {

        //-- ** print warnings for imported schemas **
		if (!_suppressNonFatalWarnings || _generateImported)
		{
            Enumeration enumeration = schema.getImportedSchema();
			while (enumeration.hasMoreElements()) {
				Schema importedSchema = (Schema)enumeration.nextElement();
                if (!_generateImported) {
                    System.out.println();
                    System.out.println(IMPORT_WARNING +
                        importedSchema.getSchemaLocation());
                } else {
                    if (_schemasProcessed == null)
                        _schemasProcessed = new Vector(7); 
                    _schemasProcessed.add(schema);
                    if (!_schemasProcessed.contains(importedSchema)) {
                        SGStateInfo importedSInfo = new SGStateInfo(importedSchema, this);
                        importedSInfo.packageName = sInfo.packageName;
                        createClasses(importedSchema, importedSInfo);
                        //--discard the SGStateInfo
                        importedSInfo = null;
                    }
                }
            }
		}

        //-- ** Generate code for all TOP-LEVEL structures **

        Enumeration structures = schema.getElementDecls();

        //-- handle all top-level element declarations
        while (structures.hasMoreElements())
            createClasses((ElementDecl)structures.nextElement(), sInfo);

        //-- handle all top-level complextypes
        structures = schema.getComplexTypes();
        while (structures.hasMoreElements())
            processComplexType((ComplexType)structures.nextElement(), sInfo);

        //-- handle all top-level simpletypes
        structures = schema.getSimpleTypes();
        while (structures.hasMoreElements())
            processSimpleType((SimpleType)structures.nextElement(), sInfo);

        //-- handle all top-level groups
        structures = schema.getModelGroups();
        while (structures.hasMoreElements())
            createClasses((ModelGroup)structures.nextElement(), sInfo);

        //-- clean up any remaining JClasses which need printing
        Enumeration keys = sInfo.keys();
        while (keys.hasMoreElements()) {
            ClassInfo cInfo = sInfo.resolve(keys.nextElement());
            JClass jClass = cInfo.getJClass();
            if (!sInfo.processed(jClass)) {
                processJClass(jClass, sInfo);
            }
            if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS)
                break;
        }
        
        //-- handle cdr files
        Enumeration cdrFiles = sInfo.getCDRFilenames();
        while (cdrFiles.hasMoreElements()) {
            String filename = (String) cdrFiles.nextElement();
            Properties props = sInfo.getCDRFile(filename);
            props.store(new FileOutputStream(new File(filename)),null);        
        }

    } //-- createClasses

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
                if (type != null ) {
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
                if (type != null ) {
                    return (type.getType() == BindingType.TYPE_TYPE);
                }
            }
	    }
	    return super.mappingSchemaType2Java();
	} //-- mappingSchemaType2Java


    private void createClasses(ElementDecl elementDecl, SGStateInfo sInfo) throws FileNotFoundException, IOException {

        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS) return;

	    if (elementDecl == null) return;
        //-- when mapping schema types, only interested in producing classes
	    //-- for elements with anonymous complex types
	    XMLType xmlType = elementDecl.getType();
        if (mappingSchemaType2Java()) {
            if (elementDecl.isReference() ||
               ((xmlType != null) && (xmlType.getName() != null)))
                return;            
	    }
        //--create component
        _bindingComponent.setView(elementDecl);
        
        //-- already processed --> just return
        ClassInfo cInfo = sInfo.resolve(elementDecl);
        if (cInfo != null && cInfo.getJClass()!=null) {
            JClass jClass = cInfo.getJClass();
            if (sInfo.processed(jClass))
                return;
            jClass = null;
        }

        //-- No type definition
        if (xmlType == null) {
             if (sInfo.verbose()) {
                String msg = "No type found for element: ";
                sInfo.getDialog().notify(msg + elementDecl.getName());
            }
            return;
        }
        //-- ComplexType
        else if (xmlType.isComplexType()) {

		    JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
            for (int i = 0; i < classes.length; i++) {
                processJClass(classes[i], sInfo);
                if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS)
                    return;
            }
            
            //only create classes for types that are not imported
            if (xmlType.getSchema() == _bindingComponent.getSchema())
                 processComplexType((ComplexType)xmlType, sInfo);
        }
        //-- SimpleType
        else if (xmlType.isSimpleType()) {
            processSimpleType((SimpleType)xmlType, sInfo);
        }
        //-- AnyType
        else {
            //-- no processing needed for 'anyType'
        }
    }  //-- createClasses

      private void createClasses(Group group, SGStateInfo sInfo) throws FileNotFoundException, IOException {
        if (group == null)
           return;
           
        
        //-- don't generate classes for empty groups
        if (group.getParticleCount() == 0) {
            if (group instanceof ModelGroup) {
                ModelGroup mg = (ModelGroup)group;
                if (mg.isReference()) {
                    mg = mg.getReference();
                    if (mg.getParticleCount() == 0)
                        return;
                }
            }
            else return;
        }
            
        _bindingComponent.setView(group);
        JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
        processContentModel(group, sInfo);
        for (int i = 0; i < classes.length; i++) {
            processJClass(classes[i], sInfo);
            if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS)
                return;
        }
    } //-- createClasses

    /**
     * Processes the given ComplexType and creates all necessary class
     * to support it
     * @param complexType the ComplexType to process
     * @throws IOException 
     * @throws FileNotFoundException 
    **/
    private void processComplexType(ComplexType complexType, SGStateInfo sInfo) throws FileNotFoundException, IOException {
        
        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS)
            return;
        
        if (complexType == null) return;
        _bindingComponent.setView(complexType);
        
        ClassInfo classInfo = sInfo.resolve(complexType);
        if (classInfo == null) {
            //-- handle top-level complextypes
            if (complexType.isTopLevel()) {
                JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
                for (int i = 0; i < classes.length; i++) {
                    processJClass(classes[i], sInfo);
                    if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS)
                        return;
                }
            }

            
            //-- process AttributeDecl
            processAttributes(complexType, sInfo);
            //--process content type if necessary
            ContentType temp = complexType.getContentType();
            if (temp.getType() == ContentType.SIMPLE) {
                processSimpleType(((SimpleContent)temp).getSimpleType(), sInfo);
            }
            
            //-- process ContentModel
            processContentModel(complexType, sInfo);


        }
        else {
            JClass jClass = classInfo.getJClass();
            if (!sInfo.processed(jClass)) {
                //-- process AttributeDecl
                processAttributes(complexType, sInfo);
                //-- process ContentModel
                processContentModel(complexType, sInfo);
                processJClass(jClass, sInfo);
            }
        }
    } //-- processComplexType


    /**
     * Processes the attribute declarations for the given complex type
     * @param complexType the ComplexType containing the attribute
     * declarations to process.
     * @param sInfo the current source generator state information
     * @throws IOException 
     * @throws FileNotFoundException 
    **/
    private void processAttributes(ComplexType complexType, SGStateInfo sInfo) 
    throws FileNotFoundException, IOException {

        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS) return;

        if (complexType == null) return;
        Enumeration enumeration = complexType.getAttributeDecls();
        while (enumeration.hasMoreElements()) {
            AttributeDecl attribute = (AttributeDecl)enumeration.nextElement();
            processSimpleType(attribute.getSimpleType(), sInfo);
        }

    } //-- processAttributes

    /**
     * Processes the given ContentModelGroup
     * @param cmGroup the ContentModelGroup to process
     * @param sInfo the current source generator state information
     * @throws IOException 
     * @throws FileNotFoundException 
    **/
    private void processContentModel(ContentModelGroup cmGroup, SGStateInfo sInfo) 
    throws FileNotFoundException, IOException {
        
        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS) return;


        if (cmGroup == null)
            return;
        //Some special code to handle the fact that the enumerate method will simply skip 
        //the first group is the number of particle is one
    
        Enumeration enumeration = cmGroup.enumerate();

        while (enumeration.hasMoreElements()) {

            Structure struct = (Structure)enumeration.nextElement();
            switch(struct.getStructureType()) {
                case Structure.ELEMENT:
                    ElementDecl eDecl = (ElementDecl)struct;
                    if (eDecl.isReference()) continue;
                    createClasses(eDecl, sInfo);
                    break;
                case Structure.GROUP:
                    processContentModel((Group)struct, sInfo);
                    //handle nested groups
                    if (!( (cmGroup instanceof ComplexType) ||
                           (cmGroup instanceof ModelGroup)))
                    {
                        createClasses((Group)struct, sInfo);
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
     * @param simpleType
     * @param sInfo
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    private void processSimpleType(SimpleType simpleType, SGStateInfo sInfo) 
    throws FileNotFoundException, IOException {

        if (sInfo.getStatusCode() == SGStateInfo.STOP_STATUS) return;

        if (simpleType == null) 
            return;

        if (simpleType.getSchema() != sInfo.getSchema()) 
            return;
            
        //-- Right now the only time we actually
        //-- generate source for a simpletype is
        //-- when it's an enumeration
        //if (! (simpleType instanceof BuiltInType) ) {
        if (simpleType.hasFacet(Facet.ENUMERATION)) {

            ClassInfo classInfo = sInfo.resolve(simpleType);
            if (classInfo == null) {
                JClass jClass = _sourceFactory.createSourceCode(simpleType, sInfo);
                processJClass(jClass, sInfo);
            } else {
                JClass jClass = classInfo.getJClass();
                processJClass(jClass, sInfo);
            }
        }
    } //-- processSimpleType


   /**
    * Called by setBinding to fill in the mapping between namespaces and
    * Java packages.
    * @param packages the array of package element
    */
    private void processNamespaces(PackageType[] packages) {
        if (packages.length == 0)
            return;
        for (int i=0; i<packages.length; i++) {
            PackageType temp = packages[i];
            PackageTypeChoice choice = temp.getPackageTypeChoice();
            if (choice.getNamespace() != null) {
                setNamespacePackageMapping(choice.getNamespace(), temp.getName());
            }
            else if (choice.getSchemaLocation() != null) {
                //1--Handle relative locations
                String tempLocation = choice.getSchemaLocation(); 
                String currentDir = System.getProperty("user.dir");
                currentDir = currentDir.replace('\\', '/');
                if (tempLocation.startsWith("./")) {
                    tempLocation = tempLocation.substring(1);
                    tempLocation = currentDir+tempLocation;
                }
                else if (tempLocation.startsWith("../")) {
                     tempLocation = tempLocation.substring(3);
                     int lastDir = currentDir.lastIndexOf('/');
                     currentDir = currentDir.substring(0, lastDir+1);
                     tempLocation = currentDir + tempLocation;
                }
                setLocationPackageMapping(tempLocation, temp.getName());
                currentDir = null;
                tempLocation = null;
            }
        }
    }

    /**
     * Processes the given JClass by creating the
     * corresponding MarshalInfo and print the Java classes
     *
     * @param jClass the classInfo to process
     * @throws IOException 
     * @throws FileNotFoundException 
    **/
    private void processJClass(JClass jClass, SGStateInfo state) 
    throws FileNotFoundException, IOException {

        if (state.getStatusCode() == SGStateInfo.STOP_STATUS) return;
        
        if (state.processed(jClass))
            return;
                        
        ClassInfo classInfo = state.resolve(jClass);
            
        //-- check for class name conflicts
        JClass conflict = state.getProcessed(jClass.getName());
        
        if ((conflict != null) && (!state.getSuppressNonFatalWarnings())) {
            ClassInfo temp = state.resolve(conflict);
            
            //-- if the ClassInfo are equal, we can just return
            if (temp == classInfo) return;
            
            //-- Find the Schema structures that are conflicting
            Annotated a1 = null;
            Annotated a2 = null;
            
            Enumeration enumeration = state.keys();
            while (enumeration.hasMoreElements()) {
                Object key = enumeration.nextElement();
                if (!(key instanceof Annotated)) continue; 
                ClassInfo cInfo = state.resolve(key);
                if (classInfo == cInfo) a1 = (Annotated)key;
                else if (temp == cInfo) a2 = (Annotated)key;
                
                if ((a1 != null) && (a2 != null)) break;                
            }
            
            
            StringBuffer error = new StringBuffer();
            error.append("Warning: A class name generation conflict has occured between ");
            if (a1 != null) {
                error.append(SchemaNames.getStructureName(a1));
                error.append(" '");
                error.append(ExtendedBinding.getSchemaLocation(a1));
            }
            else {                
                error.append(classInfo.getNodeTypeName());
                error.append(" '");
                error.append(classInfo.getNodeName());
            }
            error.append("' and ");
            if (a2 != null) {
                error.append(SchemaNames.getStructureName(a2));
                error.append(" '");
                error.append(ExtendedBinding.getSchemaLocation(a2));
            }
            else {
                error.append(temp.getNodeTypeName());
                error.append(" '");
                error.append(temp.getNodeName());
            }
            error.append("'. Please use a Binding file to solve this problem.");
            error.append("Continue anyway [not recommended] ");
            
            char ch = _dialog.confirm(error.toString(), "yn", "y = yes, n = no");
            if (ch == 'n') state.setStatusCode(SGStateInfo.STOP_STATUS);
            return;
        }
        
        state.markAsProcessed(jClass);

        boolean allowPrinting = true;

        if (state.promptForOverwrite()) {
            String filename = jClass.getFilename(_destDir);
            File file = new File(filename);
            if (file.exists()) {
                String message = filename + " already exists. overwrite";
                char ch = _dialog.confirm(message, "yna",
                    "y = yes, n = no, a = all");
                if (ch == 'a') {
                    state.setPromptForOverwrite(false);
                    allowPrinting = true;
                }
                else if (ch == 'y')
                    allowPrinting = true;
                else
                    allowPrinting = false;
            }
        }
        //-- print class
        if (allowPrinting) {

            //hack for the moment
            //to avoid the compiler complaining with java.util.Date
            jClass.removeImport("org.exolab.castor.types.Date");
            jClass.setHeader(_header);
            jClass.print(_destDir,_lineSeparator);
        }

        //------------------------------------/
        //- Create ClassDescriptor and print -/
        //------------------------------------/


        if (_createDescriptors && (classInfo != null)) {

            JClass desc = _descSourceFactory.createSource(classInfo);
            
            allowPrinting = true;
            if (state.promptForOverwrite()) {
                String filename = desc.getFilename(_destDir);
                File file = new File(filename);
                if (file.exists()) {
                    String message = filename + " already exists. overwrite";
                    char ch = _dialog.confirm(message, "yna",
                        "y = yes, n = no, a = all");
                    if (ch == 'a') {
                        state.setPromptForOverwrite(false);
                        allowPrinting = true;
                    }
                    else if (ch == 'y')
                        allowPrinting = true;
                    else
                        allowPrinting = false;
                }
            }

            if (allowPrinting) {
                updateCDRFile(jClass,desc, state);
                desc.setHeader(_header);
                desc.print(_destDir,_lineSeparator);
            }
        }
        //-- TODO: cleanup mapping file integration
        else if (classInfo != null) {
            //-- create a class mapping
            String pkg = state.packageName;
            if (pkg == null) pkg = "";
            MappingRoot mapping = state.getMapping(pkg);
            if (mapping == null) {
                mapping = new MappingRoot();
                state.setMapping(pkg, mapping);
            }
            mapping.addClassMapping(_mappingSourceFactory.createMapping(classInfo));
        }

    } //-- processJClass

    /**
     * <p>Returns a string which is the URI of a file.
     * <ul>
     *  <li>file:///DOSpath</li>
     *  <li>file://UnixPath</li>
     * </ul>
     * No validation is done to check wether the file exists or not. This method will be
     * no longer used when the JDK URL.toString() is fixed.
     * 
     * @param path The absolute path of the file.
     * @return A string representing the URI of the file.
     */
    public static String toURIRepresentation( String path ) {
        String result = path;
        if (!new File(result).isAbsolute())
           throw new IllegalArgumentException("The parameter must represent an absolute path.");
        if (File.separatorChar != '/')
            result = result.replace(File.separatorChar, '/');

        if (result.startsWith("/"))
            /*Unix platform*/
            result = "file://" + result;
        else
            result = "file:///" + result;   /*DOS platform*/

        return result;
    }

    /**
     * Updates the CDR (ClassDescriptorResolver) file with the
     * classname->descriptor mapping.
     * 
     * @param jClass JClass instance describing the entity class
     * @param jDesc JClass instance describing is *Descriptor class
     * @param sInfo state info
     * @throws IOException If an already existing '.castor.cdr' file can not be loaded
     * @throws FileNotFoundException  If an already existing '.castor.cdr' file can not be found
     */
    private void updateCDRFile(JClass jClass, JClass jDesc, SGStateInfo sInfo) 
        throws FileNotFoundException, IOException 
    {
        String entityFilename = jClass.getFilename(_destDir);
        File file = new File(entityFilename);
        File parentDirectory = file.getParentFile();
        File cdrFile = new File(parentDirectory, CDR_FILE);
        String cdrFilename = cdrFile.getAbsolutePath();
        
        Properties props = sInfo.getCDRFile(cdrFilename);
        
        if (props == null) {
            
            // check for existing .castor.xml file 
            props = new Properties();
            if (cdrFile.exists()) {
                props.load(new FileInputStream(cdrFile));
            }
            sInfo.setCDRFile(cdrFilename, props);
        }
        props.setProperty(jClass.getName(), jDesc.getName());
    } //-- updateCDRFile
    

} //-- SourceGenerator

