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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.xml.schema.reader.*;
import org.exolab.castor.xml.schema.*;

import org.exolab.javasource.*;
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.castor.util.CommandLineOptions;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.xml.JavaNaming;
import org.xml.sax.*;

import java.io.Reader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.net.URL;

/**
 * A Java Source generation tool which uses XML Schema definitions
 * to create an Object model
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SourceGenerator {

    /**
     * The application name
    **/
    static final String appName = "Castor";

    /**
     * The application description
    **/
    static final String appDesc = "XML data binder for Java";

    /**
     * The application version
    **/
    static final String version = "0.9";

    /**
     * The application URI
    **/
    static final String appURI = "http://castor.exolab.org";

    /**
     * The default code header,
     * please leave "$" and "Id" separated with "+" so that the CVS server
     * does not expand it here.
    **/
    private static final String DEFAULT_HEADER =
        "This class was automatically generated with \n"+"<a href=\"" +
        appURI + "\">" + appName + " " + version +
        "</a>, using an XML Schema.\n$" + "Id"+"$";

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

    //----------------------/
    //- Instance Variables -/
    //----------------------/

    private String lineSeparator = null;
    private JComment header = null;

    private boolean warnOnOverwrite = true;

    /**
     * Determines whether or not to print extra messages
    **/
    private boolean verbose         = false;

    private String  destDir = null;

    /**
     * A flag indicating whether or not to create
     * descriptors for the generated classes
    **/
    private boolean _createDescriptors = true;

    /**
	 * A flag indicating whether or not to generate XML marshalling
	 * framework specific methods.
	 */
	 private boolean _createMarshall  = true;

    /**
	 * A flag indicating whether or not to implement
	 * org.exolab.castor.tests.framework.CastorTestable
	 */
	 private boolean _testable  = false;


    /**
     * The field info factory.
    **/
    private FieldInfoFactory infoFactory = null;

    /**
     * The source factory.
    **/
    private SourceFactory sourceFactory = null;

    private ConsoleDialog dialog = null;

    /**
     * Names of properties used in the configuration file.
     *
     */
    public static class Property
    {
        /**
         * Property specifying whether or not to generate source code
         * for bound properties. Currently all properties will be
         * treated as bound properties if this flag is set to true.
         * A value of 'true' enables bound properties.
         * <pre>
         * org.exolab.castor.builder.boundproperties
         * </pre>
         */
         public static final String BOUND_PROPERTIES =
            "org.exolab.castor.builder.boundproperties";

        /**
         * Property specifying the super class for
         * all generated classes
         * <pre>
         * org.exolab.castor.builder.superclass
         * </pre>
         */
         public static final String SUPER_CLASS =
            "org.exolab.castor.builder.superclass";

		/**
         * Property specifying how element's and type's are mapped into a Java
         * class hierarchy by the Source Generator.
         * The value must contain one of the following.
         * 'element' outputs a Java class hierarchy based on element
         * names used in the XML Schema. This is the default.
         * 'type' outputs a Java class hierarchy based on the type
         * information defined in the XML Schema.
         * <pre>
         * org.exolab.castor.builder.javaclassmapping
         * </pre>
         */
        public static final String JavaClassMapping = "org.exolab.castor.builder.javaclassmapping";

		/**
		 * Property listing mapping between XML namespaces and Java packages.
		 */
		public static final String NamespacePackages = "org.exolab.castor.builder.nspackages";

        /**
         * Property specifying if we want to have the equals method
         * generated for each generated class
         */
        public static final String EqualsMethod = "org.exolab.castor.builder.equalsmethod";


        /**
         * The name of the configuration file.
         * <pre>
         * castor.properties
         * </pre>
         */
        public static final String FileName = "castorbuilder.properties";

        static final String ResourceName = "/org/exolab/castor/builder/castorbuilder.properties";

    } //--Property


    // Some static string definitions
	private static final String ELEMENT_VALUE = "element";
	private static final String TYPE_VALUE = "type";

    private static final int    ELEMENT_BINDING = 0;
    private static final int    TYPE_BINDING    = 1;

    /**
     * The flag indicating which type of binding we are
     * configured for
    **/
    private static int  _bindingType = ELEMENT_BINDING;

    /**
     * bound properties flag
    **/
    private static boolean _boundProperties = false;

    /**
     * Set to true if we generate an 'equals' method for each
     * generated class
     */
     private static boolean _equalsMethod = false;

	/**
     * The default properties loaded from the configuration file.
     */
    private static Properties _default;

	/**
	 * Namespace URL to Java package mapping
	 */
	private static java.util.Hashtable _nspackages;


    /**
     * Creates a SourceGenerator using the default FieldInfo factory
     */
    public SourceGenerator() {
        this(null); //-- use default factory
    } //-- SourceGenerator

    /**
     * Creates a SourceGenerator using the specific field info Factory.
     * @param infoFactory the FieldInfoFactory to use.
    */
    public SourceGenerator(FieldInfoFactory infoFactory) {
        super();

        dialog = new ConsoleDialog();

        if (infoFactory == null)
            this.infoFactory = new FieldInfoFactory();
        else
            this.infoFactory = infoFactory;

        this.sourceFactory = new SourceFactory(infoFactory);

        header = new JComment(JComment.HEADER_STYLE);
        header.appendComment(DEFAULT_HEADER);

    } //-- SourceGenerator



    /**
     * Creates Java Source code (Object model) for the given XML Schema
     * @param schema the XML schema to generate the Java sources for
     * @param packageName the package for the generated source files
    **/
    public void generateSource(Schema schema, String packageName) {
        SGStateInfo sInfo = new SGStateInfo();
        sInfo.packageName = packageName;
		if(sInfo.packageName==null)
			sInfo.packageName=SourceGenerator.getJavaPackage(schema.getTargetNamespace());

        sInfo.setPromptForOverwrite(warnOnOverwrite);
        sInfo.setVerbose(verbose);

        createClasses(schema, sInfo);
    } //-- generateSource

     /**
     * Creates Java Source code (Object model) for the given XML Schema
     * @param InputSource - the InputSource representing the XML schema.
     * @param packageName the package for the generated source files
    **/
    public void generateSource(InputSource source, String packageName) {

        //-- get default parser from Configuration
        Parser parser = null;
        try {
	    parser = Configuration.getParser();
        }
        catch(RuntimeException rte) {}
        if (parser == null) {
            System.out.println("fatal error: unable to create SAX parser.");
            return;
        }

        SchemaUnmarshaller schemaUnmarshaller = null;
        try {
           schemaUnmarshaller = new SchemaUnmarshaller();
        } catch (SAXException e) {
          // can never happen since a SAXException is thrown
          // when we are dealing with an included schema
          e.printStackTrace();
        }

        parser.setDocumentHandler(schemaUnmarshaller);
        parser.setErrorHandler(schemaUnmarshaller);

        try {
            parser.parse(source);
        }
        catch(java.io.IOException ioe) {
            System.out.println("error reading XML Schema file");
            return;
        }
        catch(org.xml.sax.SAXException sx) {

            Exception except = sx.getException();
            if (except == null) except = sx;

            if (except instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException)except;
                System.out.println("SAXParseException: " + spe);
                System.out.print(" - occured at line ");
                System.out.print(spe.getLineNumber());
                System.out.print(", column ");
                System.out.println(spe.getColumnNumber());
            }
            else except.printStackTrace();
            return;
        }

        Schema schema = schemaUnmarshaller.getSchema();
        generateSource(schema, packageName);

    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema
     * @param reader the Reader with which to read the XML Schema definition.
     * The caller should close the reader, since thie method will not do so.
     * @param packageName the package for the generated source files
    **/
    public void generateSource(Reader reader, String packageName) {

        InputSource source = new InputSource(reader);
        generateSource(source, packageName);
    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema
     * @param filename the full path to the XML Schema definition
     * @param packageName the package for the generated source files
    **/
    public void generateSource(String filename, String packageName)
        throws java.io.FileNotFoundException
    {
        if (filename.startsWith("./"))
             filename = filename.substring(2);
        if (filename.startsWith("/"))
             filename = filename.substring(1);
        FileReader reader = new FileReader(filename);
        InputSource source = new InputSource(reader);
        source.setSystemId(toURIRepresentation((new File(filename)).getAbsolutePath()));
        generateSource(source, packageName);
        try {
            reader.close();
        }
        catch(java.io.IOException iox) {};


    } //-- generateSource


    /**
     * Returns the version number of this SourceGenerator
     * @return the version number of this SourceGenerator
    **/
    public static String getVersion() {
        return version;
    } //-- getVersion

    public void setSuppressNonFatalWarnings(boolean suppress) {
        warnOnOverwrite = (!suppress);
    } //-- setSuppressNonFatalWarnings

    /**
     * Sets whether or not the source code generator prints
     * additional messages during generating source code
     * @param verbose a boolean, when true indicates to
     * print additional messages
    **/
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
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
     * Sets whether or not to create the XML marshalling framework specific
     * methods (marshall, unmarshall, validate) in the generated classes.
	 * By default, these methods are generated.
	 *
     * @param createMarshalMethods a boolean, when true indicates
     * to generated the marshalling framework methods
     *
     */
    public void setCreateMarshalMethods(boolean createMarshalMethods) {
        if (sourceFactory != null)
		   sourceFactory.setCreateMarshalMethods(createMarshalMethods);
    } //-- setCreateMarshalMethods

   /**
     * Sets whether or not to create the XML marshalling framework specific
     * methods (marshall, unmarshall, validate) in the generated classes.
	 * By default, these methods are generated.
	 *
     * @param createMarshall a boolean, when true indicates
     * to generated the marshalling framework methods
     *
     */
    public void setTestable(boolean testable) {
        if (sourceFactory != null)
		   sourceFactory.setTestable(testable);
    } //-- setTestable

  	public void setDefaultProperties(Properties properties) {
           //don't need to throw a IllegalArgumentException
           if (properties == null)
              return;
           _default = properties;
    }
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

        //-- source generator types name flag
        desc = "Sets the source generator types name (SGTypeFactory)";
        allOptions.addFlag("types", "types", desc, true);

        //-- XXX maintained temporarily
        allOptions.addFlag("type-factory", "classname", "", true);

        //-- no marshalling framework methods
		desc = "Disables the generation of the methods specific to the XML marshalling framework";
		allOptions.addFlag("nomarshall","",desc,true);

        //-- implements org.exolab.castor.tests.CastroTestable?
		desc = "Implements some specific methods to allow the generated classes to be used with Castor Testing Framework";
		allOptions.addFlag("testable","",desc,true);

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
        boolean force           = (options.getProperty("f") != null);
        String  typeFactory     = options.getProperty("types");
        boolean verbose         = (options.getProperty("verbose") != null);

        if (schemaFilename == null) {
            System.out.println(appName);
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
            typeFactory = Configuration.getProperty("org.exolab.castor.builder.type." + typeFactory.toLowerCase(),typeFactory);
            try {
                sgen = new SourceGenerator((FieldInfoFactory)Class.forName(typeFactory).newInstance());
            }
            catch(Exception x) {
                System.out.print("- invalid option for types: ");
                System.out.println(typeFactory);
                System.out.println(x);
                System.out.println("-- using default source generator types");
                sgen = new SourceGenerator(); // default
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

        try {
            sgen.generateSource(schemaFilename, packageName);
        }
        catch(java.io.FileNotFoundException fne) {
            System.out.println("unable to open XML schema file");
            return;
        }

    } //-- main

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

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
        this.lineSeparator = lineSeparator;
    } //-- setLineSeparator

    //-------------------/
    //- Private Methods -/
    //-------------------/

    private void createClasses(Schema schema, SGStateInfo sInfo) {

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


    } //-- createClasses

    private void createClasses(ElementDecl elementDecl, SGStateInfo sInfo) {

		//-- when mapping schema types, only interested in producing classes
		//-- for elements with anonymous complex types
		if (SourceGenerator.mappingSchemaType2Java())
			if (elementDecl.isReference() ||
				(elementDecl.getType()!=null &&
				 elementDecl.getType().getName()!=null))
				return;

		if (sInfo.verbose()) {
		    System.out.print("Creating classes for element: ");
		    System.out.println(elementDecl.getName());
		}
        //-- create classes for sub-elements if necessary
        XMLType xmlType = elementDecl.getType();

        //-- No type definition
        if (xmlType == null) {
            System.out.print("Type not found for element: ");
            System.out.println(elementDecl.getName());
            return;
        }
        //-- ComplexType
        else if (xmlType.isComplexType()) {

			JClass jClass = sourceFactory.createSourceCode(elementDecl,
                                                        sInfo,
                                                        sInfo.packageName);

            processComplexType((ComplexType)xmlType, sInfo);

            processJClass(jClass, sInfo);
        }
        //-- SimpleType
        else {
            processSimpleType((SimpleType)xmlType, sInfo);
        }

    }  //-- createClasses

    /**
     * Processes the given ComplexType and creates all necessary class
     * to support it
     * @param complexType the ComplexType to process
    **/
    private void processComplexType(ComplexType complexType, SGStateInfo sInfo) {

        if (complexType == null) return;

        ClassInfo classInfo = sInfo.resolve(complexType);

        if (classInfo == null) {

            //-- handle top-leve complextypes
            if (complexType.isTopLevel()) {

                JClass jClass
                    = sourceFactory.createSourceCode(complexType,
                                                     sInfo,
                                                     sInfo.packageName);
                processJClass(jClass, sInfo);

            }

            //-- process base complextype if necessary
            XMLType baseType= complexType.getBaseType();
            if (baseType != null &&
				baseType.getSchema() == complexType.getSchema()) {

				if (baseType.isComplexType())
                    processComplexType((ComplexType)baseType, sInfo);
            }

            //-- process AttributeDecl
            processAttributes(complexType, sInfo);

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


    private void processSimpleType(SimpleType simpleType, SGStateInfo sInfo) {

        if (simpleType == null) return;

        String packageName = sInfo.packageName;

        //-- Right now the only time we actually
        //-- generate source for a simpletype is
        //-- when it's an enumeration
        //if (! (simpleType instanceof BuiltInType) ) {
        if (simpleType.hasFacet(Facet.ENUMERATION)) {

            ClassInfo classInfo = sInfo.resolve(simpleType);

            if (classInfo == null) {

                JClass jClass
                    = sourceFactory.createSourceCode(simpleType,
                                                     sInfo,
                                                     packageName);

                processJClass(jClass, sInfo);
            }
            else {
                JClass jClass = classInfo.getJClass();
                if (!sInfo.processed(jClass)) {
                    processJClass(jClass, sInfo);
                }
            }
        }
    } //-- processSimpleType

    /**
     * Processes the attribute declarations for the given complex type
     * @param complexType the ComplexType containing the attribute
     * declarations to process.
     * @param sInfo the current source generator state information
    **/
    private void processAttributes(ComplexType complexType, SGStateInfo sInfo) {

        if (complexType == null) return;

        Enumeration enum = complexType.getAttributeDecls();
        while (enum.hasMoreElements()) {
            AttributeDecl attribute = (AttributeDecl)enum.nextElement();
            processSimpleType(attribute.getSimpleType(), sInfo);
        }

    } //-- processAttributes

    /**
     * Processes the given ContentModelGroup
     * @param cmGroup the ContentModelGroup to process
     * @param sInfo the current source generator state information
    **/
    private void processContentModel(ContentModelGroup cmGroup, SGStateInfo sInfo) {


        Enumeration enum = cmGroup.enumerate();

        while (enum.hasMoreElements()) {

            Structure struct = (Structure)enum.nextElement();

            switch(struct.getStructureType()) {
                case Structure.ELEMENT:
                    ElementDecl eDecl = (ElementDecl)struct;
                    if (eDecl.isReference()) continue;
                    createClasses(eDecl, sInfo);
                    break;
                case Structure.GROUP:
                    processContentModel((Group)struct, sInfo);
                    break;
                default:
                    break;
            }
        }
    } //-- process

    /**
     * Processes the given JClass by creating the
     * corresponding MarshalInfo and print the Java classes
     * @param classInfo the classInfo to process
    **/
    private void processJClass(JClass jClass, SGStateInfo state) {


        boolean allowPrinting = true;

        if (state.promptForOverwrite()) {
            String filename = jClass.getFilename(destDir);
            File file = new File(filename);
            if (file.exists()) {
                String message = filename + " already exists. overwrite?";
                allowPrinting = dialog.confirm(message);
            }
        }
        //-- print class
        if (allowPrinting) {

            //hack for the moment
            //to avoid the compiler complaining with java.util.Date
            jClass.removeImport("org.exolab.castor.types.Date");
            jClass.setHeader(header);
            jClass.print(destDir,lineSeparator);
        }

        //------------------------------------/
        //- Create ClassDescriptor and print -/
        //------------------------------------/

        ClassInfo classInfo = state.resolve(jClass);

        if (_createDescriptors && (classInfo != null)) {

            JClass desc
                = DescriptorSourceFactory.createSource(classInfo);

            allowPrinting = true;
            if (state.promptForOverwrite()) {
                String filename = desc.getFilename(destDir);
                File file = new File(filename);
                if (file.exists()) {
                    String message = filename + " already exists. overwrite?";
                    allowPrinting = dialog.confirm(message);
                }
            }

            if (allowPrinting) {
                desc.setHeader(header);
                desc.print(destDir,lineSeparator);
            }
        }

        state.markAsProcessed(jClass);
    } //-- processJClass


    /**
     * Returns the default configuration file. Changes to the returned
     * properties set will affect all Castor functions relying on the
     * default configuration.
     *
     * @return The default configuration
     */
    public static synchronized Properties getDefault()
    {
        if ( _default == null ) {
            load();
        }
        return _default;
    }

    /**
     * Returns a property from the default configuration file.
     * Equivalent to calling <tt>getProperty</tt> on the result
     * of {@link #getDefault}.
     *
     * @param name The property name
     * @param default The property's default value
     * @return The property's value
     */
    public static String getProperty( String name, String defValue )
    {
        return getDefault().getProperty( name, defValue );
    }

    /**
     * Returns true if bound properties are enabled.
     *
     * Enabling bound properties is controlled via
     * the org.exolab.castor.builder.boundproperties item
     * in the castorbuilder.properties file. The value is
     * either 'true' or 'false'.
     *
     * @return true if bound properties are enabled.
    **/
    public static boolean boundPropertiesEnabled() {
        getDefault();
        return _boundProperties;
    } //-- boundPropertiesEnabled

	/**
     * Returns true if we generate an 'equals' method for
     * each generated class.
     *
     * Enabling this property is controlled via
     * the org.exolab.castor.builder.equalsmethod item
     * in the castorbuilder.properties file. The value is
     * either 'true' or 'false'.
     *
     * @return true if bound properties are enabled.
    **/
    public static boolean equalsMethod() {
        return _equalsMethod;
    } //-- boundPropertiesEnabled

    /**
     * Sets the 'equalsmethod' property
     * @param boolean the value we want to ues
     */
     public static void setEqualsMethod(boolean equals) {
         //ensure the other properties are loaded
         getDefault();
         _equalsMethod = equals;
     }
    /**
	 * Tests the org.exolab.castor.builder.javaclassmapping property for the 'element' value.
	 * @return True if the Source Generator is mapping schema elements to Java classes.
	 */
	public static boolean mappingSchemaElement2Java()
	{
	    //-- call getDefault to ensure we loaded the properties
	    getDefault();

	    return (_bindingType == ELEMENT_BINDING);
	} //-- mappingSchemaElement2Java

	/**
	 * Tests the org.exolab.castor.builder.javaclassmapping property for the 'type' value.
	 * @return True if the Source Generator is mapping schema types to Java classes.
	 */
	public static boolean mappingSchemaType2Java()
	{
	    //-- call getDefault to ensure we loaded the properties
	    getDefault();

	    return (_bindingType == TYPE_BINDING);
	} //-- mappingSchemaType2Java

	/**
	 * Gets a Java package to an XML namespace URL
	 */
	public static String getJavaPackage(String nsURL)
	{
        if (nsURL == null) return "";

		//-- Ensure properties have been loaded
		getDefault();

		// Lookup Java package via NS
		String javaPackage = (String) _nspackages.get(nsURL);
		if(javaPackage==null)
			return "";
		else
			return javaPackage;
	}

	/**
	 * Gets the qualified class name given an XML namespace URL
	 */
	public static String getQualifiedClassName(String nsURL, String className)
	{
		//-- Ensure properties have been loaded
		getDefault();

		// Lookup Java package via NS and append class name
		String javaPackage = getJavaPackage(nsURL);
		if (javaPackage.length()>0)
			javaPackage+='.';
		return javaPackage+className;
	}

    /**
     * Called by {@link #getDefault} to load the configuration the
     * first time. Will not complain about inability to load
     * configuration file from one of the default directories, but if
     * it cannot find the JAR's configuration file, will throw a
     * run time exception.
     */
    protected static void load()
    {
		_default = Configuration.loadProperties( Property.ResourceName, Property.FileName);

		// Parse XML namespace and package list
		_nspackages = new Hashtable();
		String prop = _default.getProperty( Property.NamespacePackages, "");
		StringTokenizer tokens = new StringTokenizer(prop, ",");
		while(tokens.hasMoreTokens())
		{
			String token = tokens.nextToken();
			int comma = token.indexOf('=');
			if(comma==-1)
				continue;
			String ns = token.substring(0,comma).trim();
			String javaPackage = token.substring(comma+1).trim();
			_nspackages.put(ns, javaPackage);
		}

		//-- bound properties
		prop = _default.getProperty( Property.BOUND_PROPERTIES, "");
		_boundProperties = prop.equalsIgnoreCase("true");

        //-- Equals method?
        prop = _default.getProperty( Property.EqualsMethod, "");
		_equalsMethod = prop.equalsIgnoreCase("true");

		initBindingType();

    } //-- load

    /**
     * Called by #load to initialize the binding type
    **/
    protected static void initBindingType() {
		String prop = getDefault().getProperty( Property.JavaClassMapping,  ELEMENT_VALUE);
		if (prop.toLowerCase().equals(TYPE_VALUE))
		    _bindingType = TYPE_BINDING;
    } //-- initBindingType

    /**
     * <p>Returns a string which is the URI of a file.
     * <ul>
     *  <li>file:///DOSpath</li>
     *  <li>file://UnixPath</li>
     * </ul>
     * No validation is done to check wether the file exists or not.
     * This method will be no longer used when the JDK URL.toString() is
     * fixed.
     * @param path the absolute path of the file.
     * @returns a string representing the URI of the file
     *
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
        else result = "file://" + "/" + result;   /*DOS platform*/

        return result;
    }


} //-- SourceGenerator

