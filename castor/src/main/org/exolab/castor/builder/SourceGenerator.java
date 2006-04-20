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
import org.exolab.castor.util.Version;

import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.*;

//--Java IO imports
import java.io.Reader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

//--Java util imports
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.net.URL;

/**
 * A Java Source generation tool which uses XML Schema definitions
 * to create an Object model.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a> - Main author.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a> - Contributions.
 * @author <a href="mailto:nsgreen@thazar.com">Nathan Green</a> - Contributions.
 * @version $Revision$ $Date$
**/
public class SourceGenerator {


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

    private static final String ELEMENT_VALUE = "element";
	private static final String TYPE_VALUE = "type";

    private static final int    ELEMENT_BINDING = 0;
    private static final int    TYPE_BINDING    = 1;

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



    /**
     * Names of properties used in the configuration file.
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
         * Property specifying whether or not to generate source code
         * for extra collection methods. 
         * <pre>
         * org.exolab.castor.builder.extraCollectionMethods
         * </pre>
         */
         public static final String EXTRA_COLLECTION_METHODS =
            "org.exolab.castor.builder.extraCollectionMethods";
            
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
		public static final String NamespacePackagesOld = "org.exolab.castor.builder.nspackages";
		public static final String NamespacePackages = "org.exolab.castor.xml.nspackages";

        /**
         * Property specifying if we want to have the equals method
         * generated for each generated class
         */
        public static final String EqualsMethod = "org.exolab.castor.builder.equalsmethod";

        /**
         * Property specifying if we want to use Wrapper Objects instead
         * of primitives (eg java.lang.Float instead of float)
         */
        public static final String Wrapper = "org.exolab.castor.builder.primitivetowrapper";

        /**
         * Property specifying if we want to have a 'public static final String'
         * generated for each attribute and element name used within a class descriptor
         */
        public static final String ClassDescFieldNames = "org.exolab.castor.builder.classdescfieldnames";


        /**
         * The name of the configuration file.
         * <pre>
         * castor.properties
         * </pre>
         */
        public static final String FileName = "castorbuilder.properties";

        static final String ResourceName = "/org/exolab/castor/builder/castorbuilder.properties";

    } //--Property

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
     * Castor configuration
     */
    private Configuration _config = null;
    
    /**
     * Set to true if we generate an 'equals' method for each
     * generated class
     */
     private static boolean _equalsMethod = false;
     private static boolean _hasLocalEquals = false;
     
     /**
      * Boolean to indicate extra collection methods such as setXXX(Vector)
      * should be generated.
      */
     private static boolean _generateExtraCollectionMethods = false;
     


    /**
     * Set to true if we generate a 'public static final String'
     * for each element and attribute in a class descriptor
     */
    private static boolean _classDescFieldNames = false;
    private static boolean _hasLocalClassField = false;

     /**
      * Set to true if you want to use Object instead of
      * primitive (eg java.lang.Float instead of float).
      */
    private static boolean _wrapper = false;
    private static boolean _hasLocalWrapper = false;

	/**
     * The default properties loaded from the configuration file.
     */
    private static Properties _default;
    
    /**
     * A boolean indicated whether or not the properties have been loaded
     */
    private static boolean _loaded = false;
    
    /**
    * The XMLBindingComponent used to create Java classes from an XML Schema
    */
    private XMLBindingComponent _bindingComponent = new XMLBindingComponent();

	/**
	 * Namespace URL to Java package mapping
	 */
	private static java.util.Hashtable _nspackages = null;
    static {
        _nspackages = new Hashtable();
    }

	/**
	 * schemaLocation to Java package mapping
	 */
	private static java.util.Hashtable _locpackages = null;
    static {
        _locpackages = new Hashtable();
    }

        //----------------------/
    //- Instance Variables -/
    //----------------------/

    private String _lineSeparator = null;
    private JComment _header = null;

    private boolean _warnOnOverwrite = true;
	private boolean _suppressNonFatalWarnings = false;

    /**
     * Determines whether or not to print extra messages
    **/
    private boolean _verbose         = false;

    private String  _destDir = null;

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
    private FieldInfoFactory _infoFactory = null;

    /**
     * The source factory.
    **/
    private SourceFactory _sourceFactory = null;

   
    private ConsoleDialog _dialog = null;


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
        //--reset static properties that can be set through the API
        _hasLocalClassField = false;
        _hasLocalEquals = false;
        _hasLocalWrapper =false;
        
        _sourceFactory = new SourceFactory(infoFactory);
        _header = new JComment(JComment.HEADER_STYLE);
        _header.appendComment(DEFAULT_HEADER);
        //--set the binding 
        setBinding(binding);

    } //-- SourceGenerator

    /**
     * Creates Java Source code (Object model) for the given XML Schema.
     *
     * @param schema the XML schema to generate the Java sources for.
     * @param packageName the package for the generated source files.
    **/
    public void generateSource(Schema schema, String packageName) {
        
        if (schema == null) {
            String err = "The argument 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
            
        SGStateInfo sInfo = new SGStateInfo(schema);
        //--make sure the XML Schema is valid
        try {
            schema.validate();
        } catch (ValidationException ve) {
            String err = "The schema:"+schema.getSchemaLocation()+" is not valid.\n";
            err += ve.getMessage();
            throw new IllegalArgumentException(err);
        }
        //--This code shouldn't be needed anymore since XMLBindingComponent
        //--should handle the lookup(Arnaud 20030120)
        /*if (packageName == null) {
            String schemaLocation = schema.getSchemaLocation();
            packageName = lookupPackageLocation(schemaLocation);
            if (packageName == null) {
                String targetNamespace = schema.getTargetNamespace();
                packageName = lookupPackageNamespace(targetNamespace);
            }
        }*/
        
        //--map the targetNamespace of the schema with the packageName defined
        if (packageName != null) {
            String targetNamespace = schema.getTargetNamespace();
            //-- adjust targetNamespace
            if (targetNamespace == null)
                targetNamespace = "";
            _nspackages.put(targetNamespace, packageName);
        }
        sInfo.packageName = packageName;
        sInfo.setDialog(_dialog);
        sInfo.setPromptForOverwrite(_warnOnOverwrite);
        sInfo.setVerbose(_verbose);
        sInfo.setSuppressNonFatalWarnings(_suppressNonFatalWarnings);

        createClasses(schema, sInfo);
    } //-- generateSource

     /**
     * Creates Java Source code (Object model) for the given XML Schema
     *
     * @param InputSource - the InputSource representing the XML schema.
     * @param packageName the package for the generated source files
    **/
    public void generateSource(InputSource source, String packageName) {

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
                _dialog.notify(new Integer(spe.getLineNumber()).toString());
                _dialog.notify(", column ");
                _dialog.notify(new Integer(spe.getColumnNumber()).toString());
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
    public void generateSource(Reader reader, String packageName) {
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
        throws java.io.FileNotFoundException
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
        } catch(java.io.IOException iox) {};


    } //-- generateSource


    /**
     * Returns the default configuration file. Changes to the returned
     * properties set will affect all Castor functions relying on the
     * default configuration.
     *
     * @return The default configuration
     */
    public static synchronized Properties getDefault() {
        if (_default == null)
            load();
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
    public static String getProperty( String name, String defValue ) {
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
        //--ensure the property is loaded
        //--this is needed when specifying properties at run-time by setDefaultProperties
        boolean bound = getDefault().getProperty(Property.BOUND_PROPERTIES,"").equalsIgnoreCase("true");
        return  _boundProperties || bound;
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
        //--ensure the property is loaded
        //--this is needed when specifying properties at run-time by setDefaultProperties
        boolean equals = getDefault().getProperty(Property.EqualsMethod,"").equalsIgnoreCase("true");
        if (_hasLocalEquals)
            return _equalsMethod;
        else 
            return equals;
    } //-- equalsMethod

    /**
     * Sets the 'equalsmethod' property
     * @param boolean the value we want to use
     */
     public static void setEqualsMethod(boolean equals) {
         _equalsMethod = equals;
         _hasLocalEquals = true;
     }

	/**
     * Returns true if we generate a 'public static final String' for the
     * name of each attribute and element described by the class descriptor
     *
     * Enabling this property is controlled via
     * the org.exolab.castor.builder.classdescfieldnames item
     * in the castorbuilder.properties file. The value is
     * either 'true' or 'false'.
     *
     * @return true if bound properties are enabled.
    **/
    public static boolean classDescFieldNames() {
        //--ensure the property is loaded
        //--this is needed when specifying properties at run-time by setDefaultProperties
        boolean classDesc = getDefault().getProperty(Property.ClassDescFieldNames,"").equalsIgnoreCase("true");
        if (_hasLocalClassField)
            return _classDescFieldNames;
        else 
            return classDesc;
    } //-- classDescFieldNames

    /**
     * Sets the 'classDescFieldNames' property
     * @param boolean the value we want to ues
     */
     public static void setClassDescFieldNames(boolean classDescFieldNames) {
         //ensure the other properties are loaded
         getDefault();
         _classDescFieldNames = classDescFieldNames;
         _hasLocalClassField = true;
     } //-- setClassDescFieldNames

     /**
      * Returns true if primitive types have to be used
      * as Objects (eg. replacing float by java.lang.Float).
      */
    public static boolean usePrimitiveWrapper() {
        //--ensure the property is loaded
       //--this is needed when specifying properties at run-time by setDefaultProperties
        boolean wrapper = getDefault().getProperty("org.exolab.castor.builder.primitivetowrapper","").equalsIgnoreCase("true");
        if (_hasLocalWrapper)
            return _wrapper;
        else 
            return wrapper;
    }
     /**
      * Sets the 'primitivetowrapper' property
      * @param boolean the value we want to use.
      */
      public static void setPrimitiveWrapper(boolean wrapper) {
          _wrapper = wrapper;
          _hasLocalWrapper = true;
      }

    /**
	 * Tests the org.exolab.castor.builder.javaclassmapping property for the 'element' value.
     *
	 * @return True if the Source Generator is mapping schema elements to Java classes.
	 */
	public static boolean mappingSchemaElement2Java() {
	    //-- call getDefault to ensure we loaded the properties
	    getDefault();

	    return (_bindingType == ELEMENT_BINDING);
	} //-- mappingSchemaElement2Java

	/**
	 * Tests the org.exolab.castor.builder.javaclassmapping property for the 'type' value.
     *
	 * @return True if the Source Generator is mapping schema types to Java classes.
	 */
	public static boolean mappingSchemaType2Java() {
	    //-- call getDefault to ensure we loaded the properties
	    getDefault();

	    return (_bindingType == TYPE_BINDING);
	} //-- mappingSchemaType2Java

	/**
	 * Gets a Java package to an XML namespace URL
	 */
	public static String lookupPackageNamespace(String nsURL) {
        if (nsURL == null) nsURL = "";

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
	 * Gets a Java package to a schema location.
	 */
	public static String lookupPackageLocation(String schemaLocation)
	{
        if (schemaLocation == null) return "";

		// Lookup Java package via schemaLocation
		//--Full path
        String javaPackage = (String) _locpackages.get(schemaLocation);
		if(javaPackage==null) {
			//--maybe a relative schemaLocation was given
            while(schemaLocation.startsWith(".")) {
                 if (schemaLocation.startsWith("./")) {
                     schemaLocation = schemaLocation.substring(2);
                 } else if (schemaLocation.startsWith("../")) {
                     schemaLocation = schemaLocation.substring(3);
                 }
            }
            Enumeration keys = _locpackages.keys();
            boolean found = false;
            while (keys.hasMoreElements() && !found) {
                String key = (String)keys.nextElement();
                if (key.endsWith(schemaLocation)) {
                    javaPackage = (String) _locpackages.get(key);
                    found = true;
                }
            }
            if (javaPackage == null)
            javaPackage = "";
		}

        return javaPackage;
	}

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
        if (_sourceFactory != null)
            _sourceFactory.setSAX1(sax1);

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
        if (_sourceFactory != null)
		   _sourceFactory.setCreateMarshalMethods(createMarshalMethods);
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
        if (_sourceFactory != null)
		   _sourceFactory.setTestable(testable);
    } //-- setTestable

    /**
     * Override the properties
     */
    public static void setDefaultProperties(Properties properties) {
        _loaded = false;
        _default = properties;
        load();
    }
  	
 
   /**
    * Sets the binding to use with this instance of the SourceGenerator.
    *
    * @param binding the binding to use, null indicates that the default
    * binding will be used.
    */
    public void setBinding(ExtendedBinding binding) {
        if (binding != null) {
            processNamespaces(binding.getPackage());
            BindingType type = binding.getDefaultBindingType();
            if (type != null ) {
                if (type.getType() == BindingType.ELEMENT_TYPE)
                    _bindingType = ELEMENT_BINDING;
                else if (type.getType() == BindingType.TYPE_TYPE)
                    _bindingType = TYPE_BINDING;
            }
        }
        //--initialize the XMLBindingComponent
        _bindingComponent.setBinding(binding);
    }

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




        try {
            sgen.generateSource(schemaFilename, packageName);
        } catch(java.io.FileNotFoundException fne) {
            System.out.println("unable to open XML schema file");
            return;
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

    private void createClasses(Schema schema, SGStateInfo sInfo) {

        //-- ** print warnings for imported schemas **
		if (!_suppressNonFatalWarnings)
		{
			Enumeration enum = schema.getImportedSchema();
			while (enum.hasMoreElements()) {
				Schema importedSchema = (Schema)enum.nextElement();
				System.out.println();
				System.out.println(IMPORT_WARNING +
					importedSchema.getSchemaLocation());
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
        }

    } //-- createClasses

    private void createClasses(ElementDecl elementDecl, SGStateInfo sInfo) {

	    if (elementDecl == null) return;
        //-- when mapping schema types, only interested in producing classes
	    //-- for elements with anonymous complex types
	    XMLType xmlType = elementDecl.getType();
        if (SourceGenerator.mappingSchemaType2Java()) {
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
            for (int i = 0; i < classes.length; i++)
                processJClass(classes[i], sInfo);
            
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

      private void createClasses(Group group, SGStateInfo sInfo) {
        if (group == null)
           return;
        _bindingComponent.setView(group);
        JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
        processContentModel(group, sInfo);
        for (int i = 0; i < classes.length; i++)
            processJClass(classes[i], sInfo);
    } //-- createClasses

    /**
     * Processes the given ComplexType and creates all necessary class
     * to support it
     * @param complexType the ComplexType to process
    **/
    private void processComplexType(ComplexType complexType, SGStateInfo sInfo) {

        if (complexType == null) return;

        _bindingComponent.setView(complexType);
        ClassInfo classInfo = sInfo.resolve(complexType);
        if (classInfo == null) {
            //-- handle top-level complextypes
            if (complexType.isTopLevel()) {
                JClass[] classes = _sourceFactory.createSourceCode(_bindingComponent, sInfo);
                for (int i = 0; i < classes.length; i++)
                    processJClass(classes[i], sInfo);
            }

            
            //-- process AttributeDecl
            processAttributes(complexType, sInfo);
            //--process content type if necessary
            ContentType temp = complexType.getContentType();
            if (temp.getType() == ContentType.SIMPLETYPE) {
                processSimpleType(temp.getSimpleType(), sInfo);
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

        if (cmGroup == null)
            return;

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
     */
    private void processSimpleType(SimpleType simpleType, SGStateInfo sInfo) {

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
    * @param Package[] the array of package element
    */
    private void processNamespaces(PackageType[] packages) {
        if (packages.length == 0)
            return;
        for (int i=0; i<packages.length; i++) {
            PackageType temp = packages[i];
            PackageTypeChoice choice = temp.getPackageTypeChoice();
            if (choice.getNamespace() != null) {
                _nspackages.put(choice.getNamespace(), temp.getName());
            }
            else if (choice.getSchemaLocation() != null) {
                _locpackages.put(choice.getSchemaLocation(), temp.getName());
            }
        }
    }

    /**
     * Processes the given JClass by creating the
     * corresponding MarshalInfo and print the Java classes
     *
     * @param classInfo the classInfo to process
    **/
    private void processJClass(JClass jClass, SGStateInfo state) {


        if (state.processed(jClass))
           return;

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

        ClassInfo classInfo = state.resolve(jClass);

        if (_createDescriptors && (classInfo != null)) {

            JClass desc
                = DescriptorSourceFactory.createSource(classInfo);

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
                desc.setHeader(_header);
                desc.print(_destDir,_lineSeparator);
            }
        }

        state.markAsProcessed(jClass);
    } //-- processJClass



   
    /**
     * Returns true if extra methods for collection fields should
     * be generated. Such methods include set/get methods for
     * the actual collection in addition to the array methods.
     *
     * Enabling extra collection methods is controlled via
     * the org.exolab.castor.builder.extraCollectionMethods 
     * property in the castorbuilder.properties file. The value is
     * either 'true' or 'false'.
     *
     * @return true if extra collection methods are enabled.
    **/
    public static boolean generateExtraCollectionMethods() {
        if (_default != null) {
            boolean result = getDefault().getProperty(Property.EXTRA_COLLECTION_METHODS,"").equalsIgnoreCase("true");
            _generateExtraCollectionMethods = result;
        }
        return  _generateExtraCollectionMethods;
    } //-- generateExtraCollectionMethods
	

    /**
     * Called by {@link #getDefault} to load the configuration the
     * first time. Will not complain about inability to load
     * configuration file from one of the default directories, but if
     * it cannot find the JAR's configuration file, will throw a
     * run time exception.
     */
    protected static synchronized void load()
    {
        
		if (_default == null) {
		    //-- load defaults from JAR
  		    _default = Configuration.loadProperties( Property.ResourceName, Property.FileName);
  		    //-- load local defaults
        
            // Get overriding configuration from the classpath,
            // ignore if not found. If found, merge any existing
            // properties.
            try {      
                InputStream is = SourceGenerator.class.getResourceAsStream("/" + Property.FileName);
                if (is != null) {
                    _default.load( is );
                }      
            } catch ( Exception except ) {
                // Do nothing
            }
  		    
  		}
  		
  		Configuration rtconf =  LocalConfiguration.getInstance();

        // Parse XML namespace and package list from both castor.properties and
        // castorbuilder.properties
		processNamespacePackageMappings(rtconf.getProperty( Property.NamespacePackagesOld, ""));
		processNamespacePackageMappings(rtconf.getProperty( Property.NamespacePackages, ""));
		processNamespacePackageMappings(_default.getProperty( Property.NamespacePackagesOld, ""));
		processNamespacePackageMappings(_default.getProperty( Property.NamespacePackages, ""));
		

		//-- bound properties
		String prop = _default.getProperty( Property.BOUND_PROPERTIES, "");
		_boundProperties = prop.equalsIgnoreCase("true");


        //-- Equals method?
        prop = _default.getProperty( Property.EqualsMethod, "");
		if (!_hasLocalEquals)
  		    _equalsMethod = prop.equalsIgnoreCase("true");

		//-- Extra Collection Methods?
		prop = _default.getProperty( Property.EXTRA_COLLECTION_METHODS, "");
		_generateExtraCollectionMethods = prop.equalsIgnoreCase("true");

        //-- Primitive to Wrapper
        prop = _default.getProperty(Property.Wrapper, "");
        if (!_hasLocalWrapper)
            _wrapper = _wrapper;

		//-- Generate public field names in class descriptors?
		prop = _default.getProperty( Property.ClassDescFieldNames, "");
		if (!_hasLocalClassField)
		    _classDescFieldNames = _classDescFieldNames;
          
        //-- backward compatibility with 0.9.3.9
        prop = _default.getProperty(JavaNaming.UPPER_CASE_AFTER_UNDERSCORE_PROPERTY, null);
        if (prop != null) {
            JavaNaming.upperCaseAfterUnderscore = Boolean.valueOf(prop).booleanValue();
        }
        
        initBindingType();
        _loaded = true;

    } //-- load

    protected static void processNamespacePackageMappings(String mappings) {
        if (mappings == null) return;
		StringTokenizer tokens = new StringTokenizer(mappings, ",");
		while(tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			int comma = token.indexOf('=');
			if(comma==-1)
				continue;
			String ns = token.substring(0,comma).trim();
			String javaPackage = token.substring(comma+1).trim();
			_nspackages.put(ns, javaPackage);
		}
    }
    
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
        else
            result = "file:///" + result;   /*DOS platform*/

        return result;
    }


} //-- SourceGenerator

