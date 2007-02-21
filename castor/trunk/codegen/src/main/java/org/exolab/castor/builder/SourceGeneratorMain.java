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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id:  $
 */
package org.exolab.castor.builder;

import java.io.PrintWriter;
import java.util.Properties;

import org.exolab.castor.builder.binding.BindingException;
import org.exolab.castor.builder.binding.BindingLoader;
import org.exolab.castor.util.CommandLineOptions;

/**
 * Main line method for command-line invokation of the source generation tool.
 * Because this class exists only to provide a main(String[]) for command-line
 * use, all methods are static.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a> - Main author.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a> - Contributions.
 * @author <a href="mailto:nsgreen@thazar.com">Nathan Green</a> - Contributions.
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a> - Cut out of SourceGenerator.java
 * @version $Revision: 0000 $ $Date: $
 */
public final class SourceGeneratorMain {
    //--------------------------/
    //- Command line arguments -/
    //--------------------------/

    private static final String ARGUMENT_BINDING_FILENAME          = "binding-file";
    private static final String ARGUMENT_CASE_INSENSITIVE          = "case-insensitive";
    private static final String ARGUMENT_DESTINATION_DIR           = "dest";
    private static final String ARGUMENT_DISABLE_DESCRIPTORS       = "nodesc";
    private static final String ARGUMENT_FORCE                     = "f";
    private static final String ARGUMENT_GENERATE_IMPORTED_SCHEMAS = "generateImportedSchemas";
    private static final String ARGUMENT_GENERATE_MAPPING          = "gen-mapping";
    private static final String ARGUMENT_HELP                      = "h";
    private static final String ARGUMENT_INPUT                     = "i";
    private static final String ARGUMENT_LINE_SEPARATOR            = "line-separator";
    private static final String ARGUMENT_NOMARSHALL                = "nomarshall";
    private static final String ARGUMENT_PACKAGE                   = "package";
    private static final String ARGUMENT_SAX1                      = "sax1";
    private static final String ARGUMENT_TESTABLE                  = "testable";
    private static final String ARGUMENT_TYPES                     = "types";
    private static final String ARGUMENT_TYPES_DEPRECATED          = "type-factory";
    private static final String ARGUMENT_TYPES_JAVA2               = "j2";
    private static final String ARGUMENT_VERBOSE                   = "verbose";
    private static final String ARGUMENT_FAIL_ON_ERROR             = "fail";
    private static final String ARGUMENT_NAME_CONFLICT_STRATEGY    = "nameConflictStrategy";

    private static final String ARG_VALUE_LINE_SEPARATION_MAC      = "mac";
    private static final String ARG_VALUE_LINE_SEPARATION_UNIX     = "unix";
    private static final String ARG_VALUE_LINE_SEPARATION_WIN      = "win";

    //-------------------------/
    //- Command line messages -/
    //-------------------------/

    /** Message used when descriptor creation is disabled. */
    private static final String DISABLE_DESCRIPTORS_MSG =
        "Disabling generation of Class descriptors.";

    /** Message used when marshaling methods creation is disabled. */
    private static final String DISABLE_MARSHALL_MSG =
        "Disabling generation of Marshaling framework methods (marshal, unmarshal, validate).";

    /** Message used when implementing CastorTestable. */
    private static final String CASTOR_TESTABLE_MSG =
        "The generated classes will implement org.castor.xmlctf.CastorTestable.";

    /** Message used when using SAX1. */
    private static final String SAX1_MSG =
        "The generated classes will use SAX 1.";

    private static final String GENERATE_IMPORT_MSG =
        "Imported XML Schemas will be processed automatically.";

    private static final String CASE_INSENSITIVE_MSG =
        "The generated classes will use a case insensitive method "
        + "for looking up enumerated type values.";

    private static final String TYPE_FACTORY_ARG_MSG =
        "The argument '-type-factory' is deprecated; please use '-types' in its place.";

    private static final String SUPPRESS_NON_FATAL_WARN_MSG =
        "Suppressing non fatal warnings.";

    private static final String GENERATING_MAPPING_FILE_MSG =
        "Generating mapping file: ";

    private static final String BINDING_FILE_ERROR1_MSG =
        "Unable to load binding file ";

    private static final String BINDING_FILE_ERROR2_MSG =
        " due to the following Exception:";

    private static final String BINDING_FILE_ERROR3_MSG =
        "No binding file will be used.";

    private static final String INVALID_TYPES_OPTION_MSG =
        "Invalid option for '-types': ";

    private static final String DEFAULT_FIELD_INFO_MSG =
        "Using default source generator types.";

    private static final String LINE_SEPARATION_WIN_MSG =
        "Using Windows style line separation.";

    private static final String LINE_SEPARATION_UNIX_MSG =
        "Using UNIX style line separation.";

    private static final String LINE_SEPARATION_MAC_MSG =
        "Using Macintosh style line separation.";

    private static final String DEFAULT_LINE_SEPARATOR_MSG =
        "Using default line separator for this platform";

    private static final String INVALID_LINE_SEPARATOR_MSG =
        "Invalid option for line-separator: ";

    private static final String NAME_CONFLICT_STRATEGY_MSG =
        "Using name conflict strategy ";

    /** The full set of command-line options. */
    private static final CommandLineOptions ALL_OPTIONS     = setupCommandLineOptions();

    /**
     * As a static utility class, we want a private constructor.
     */
    private SourceGeneratorMain() {
        // Private constructor
    }

    //////////////////
    //  MAIN METHOD //
    //////////////////

    /**
     * Parses the command line, converting everything into the proper form for
     * the source generation main class, then invokes source generation.
     *
     * @param args the String[] consisting of the command line arguments
     */
    public static void main(final String[] args) {
        //-- Process the specified command line options
        Properties options = ALL_OPTIONS.getOptions(args);

        //-- check for help option
        if (options.getProperty(ARGUMENT_HELP) != null) {
            PrintWriter pw = new PrintWriter(System.out, true);
            ALL_OPTIONS.printHelp(pw);
            return;
        }

        //-- Make sure we have a schema to work on
        String schemaFilename = options.getProperty(ARGUMENT_INPUT);
        if (schemaFilename == null) {
            System.out.println(SourceGenerator.APP_NAME);
            ALL_OPTIONS.printUsage(new PrintWriter(System.out));
            return;
        }

        // Instantiate our SourceGenerator
        FieldInfoFactory factory = getTypeFactory(options);
        SourceGenerator sgen = (factory == null) ? new SourceGenerator()
                                                 : new SourceGenerator(factory);

        // Everything below here sets options on our SourceGenerator

        sgen.setLineSeparator(getLineSeparator(options.getProperty(ARGUMENT_LINE_SEPARATOR)));
        sgen.setDestDir(options.getProperty(ARGUMENT_DESTINATION_DIR));
        sgen.setVerbose(options.getProperty(ARGUMENT_VERBOSE) != null);
        sgen.setFailOnFirstError(options.getProperty(ARGUMENT_FAIL_ON_ERROR) != null);

        boolean force = (options.getProperty(ARGUMENT_FORCE) != null);
        sgen.setSuppressNonFatalWarnings(force);
        if (force) {
            System.out.print("-- ");
            System.out.println(SUPPRESS_NON_FATAL_WARN_MSG);
        }

        if (options.getProperty(ARGUMENT_DISABLE_DESCRIPTORS) != null) {
            sgen.setDescriptorCreation(false);
            System.out.print("-- ");
            System.out.println(DISABLE_DESCRIPTORS_MSG);
        }

        String mappingFilename = options.getProperty(ARGUMENT_GENERATE_MAPPING);
        if (mappingFilename != null) {
            sgen.setGenerateMappingFile(true);
            if (mappingFilename.length() > 0) {
                sgen.setMappingFilename(mappingFilename);
            }
            System.out.print("-- ");
            System.out.println(GENERATING_MAPPING_FILE_MSG + "'" + mappingFilename + "'");
        }

        if (options.getProperty(ARGUMENT_NOMARSHALL) != null) {
            sgen.setCreateMarshalMethods(false);
            System.out.print("-- ");
            System.out.println(DISABLE_MARSHALL_MSG);
        }

        if (options.getProperty(ARGUMENT_TESTABLE) != null) {
            sgen.setTestable(true);
            System.out.print("-- ");
            System.out.println(CASTOR_TESTABLE_MSG);
        }

        if (options.getProperty(ARGUMENT_SAX1) != null) {
            sgen.setSAX1(true);
            System.out.print("-- ");
            System.out.println(SAX1_MSG);
        }

        if (options.getProperty(ARGUMENT_CASE_INSENSITIVE) != null) {
            sgen.setCaseInsensitive(true);
            System.out.print("-- ");
            System.out.println(CASE_INSENSITIVE_MSG);
        }

        String nameConflictStrategy = options.getProperty(ARGUMENT_NAME_CONFLICT_STRATEGY);
        if (nameConflictStrategy != null) {
            sgen.setNameConflictStrategy(nameConflictStrategy);
            System.out.print("-- ");
            System.out.println(CASE_INSENSITIVE_MSG + nameConflictStrategy);
        }

        String bindingFilename = options.getProperty(ARGUMENT_BINDING_FILENAME);
        if (bindingFilename != null) {
            try {
                sgen.setBinding(BindingLoader.createBinding(bindingFilename));
            } catch (BindingException e) {
                System.out.print("--");
                System.out.println(BINDING_FILE_ERROR1_MSG + "'" + bindingFilename + "'"
                                   + BINDING_FILE_ERROR2_MSG);
                e.printStackTrace();
                System.out.print("--");
                System.out.println(BINDING_FILE_ERROR3_MSG);
            }
        }

        if (options.getProperty(ARGUMENT_GENERATE_IMPORTED_SCHEMAS) != null) {
            sgen.setGenerateImportedSchemas(true);
            System.out.print("-- ");
            System.out.println(GENERATE_IMPORT_MSG);
        }

        try {
            sgen.generateSource(schemaFilename, options.getProperty(ARGUMENT_PACKAGE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } //-- main

    /**
     * Creates and returns a <code>FieldInfoFactory</code> (or null if none
     * requested or if an error is encountered making the requested type) to be
     * used during source generation. If the <code>-types</code> argument is
     * not found, check for the deprecated but still supported
     * <code>-type-factory</code> argument. If we find that, give a
     * deprecation warning.
     * <p>
     * Once we've parsed the command-line arguments, if there are no arguemnts
     * appropriate for a type factory, then we have nothing to do. Otherwise,
     * try to instantiate the requested <code>FieldInfoFactory</code>. If the
     * <code>FieldInfoFactory</code> throws an
     * <code>IllegalArgumentException</code> then try to instantiate the class
     * provided, which allows someone to provide their own implementation of a
     * <code>FieldInfoFactory</code>, which must extend (@link
     * org.exolab.castor.builder.FieldInfoFactory).
     *
     * @param options the full set of command-line options
     * @return a FieldInfoFactory to be used during source generation, or null
     *         if the default should be used
     * @see org.exolab.castor.builder.FieldInfoFactory
     */
    private static FieldInfoFactory getTypeFactory(final Properties options) {
        String typeFactory = options.getProperty(ARGUMENT_TYPES);
        if (typeFactory == null) {
            // This backwards-compatible option is retained temporarily
            typeFactory = options.getProperty(ARGUMENT_TYPES_DEPRECATED);
            if (typeFactory != null) {
                System.out.print("-- ");
                System.out.println(TYPE_FACTORY_ARG_MSG);
            }
        }

        //-- For backwards compatibility
        if (typeFactory != null && typeFactory.equals(ARGUMENT_TYPES_JAVA2)) {
            typeFactory = "arraylist";
        }

        // If no command-line arguments, then we have nothing to do
        if (typeFactory == null) {
            return null;
        }

        FieldInfoFactory factory = null;
        try {
            factory = new FieldInfoFactory(typeFactory);
        } catch (IllegalArgumentException e) {
            try {
                // Allow someone to provide their own FieldInfoFactory implementation
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                factory = (FieldInfoFactory) classLoader.loadClass(typeFactory).newInstance();
            } catch (Exception e2) {
                System.out.println("-- " + INVALID_TYPES_OPTION_MSG + typeFactory);
                System.out.println("-- " + e.getMessage());
                System.out.println("-- " + DEFAULT_FIELD_INFO_MSG);
            }
        }

        return factory;
    }

    /**
     * Parses the command-line argument for line separator style, handles bad
     * values, and returns a value that the source generator will understand.
     *
     * @param lineSepStyle the command-line argument for line separation style
     * @return the line separator string as the source generator understands it.
     */
    private static String getLineSeparator(final String lineSepStyle) {
        String lineSep = System.getProperty("line.separator");
        if (lineSepStyle != null) {
            if (ARG_VALUE_LINE_SEPARATION_WIN.equals(lineSepStyle)) {
                System.out.println("-- " + LINE_SEPARATION_WIN_MSG);
                lineSep = "\r\n";
            } else if (ARG_VALUE_LINE_SEPARATION_UNIX.equals(lineSepStyle)) {
                System.out.println("-- " + LINE_SEPARATION_UNIX_MSG);
                lineSep = "\n";
            } else if (ARG_VALUE_LINE_SEPARATION_MAC.equals(lineSepStyle)) {
                System.out.println("-- " + LINE_SEPARATION_MAC_MSG);
                lineSep = "\r";
            } else {
                System.out.println("-- " + INVALID_LINE_SEPARATOR_MSG + "'" + lineSepStyle + "'");
                System.out.println("-- " + DEFAULT_LINE_SEPARATOR_MSG);
            }
        }
        return lineSep;
    }

    /**
     * Configures our command-line options object with the command line options
     * that we recognize.
     *
     * @return a new CommandLineOptions object fully configured to parse our
     *         command line.
     */
    private static CommandLineOptions setupCommandLineOptions() {
        CommandLineOptions allOptions = new CommandLineOptions();
        String desc;

        //-- filename flag
        desc = "Sets the filename for the schema used as input.";
        allOptions.addFlag(ARGUMENT_INPUT, "schema filename", desc);

        //-- package name flag
        desc = "Sets the package name for generated code.";
        allOptions.addFlag(ARGUMENT_PACKAGE, "package name", desc, true);

        //-- destination directory
        desc = "Sets the destination output directory.";
        allOptions.addFlag(ARGUMENT_DESTINATION_DIR, "destination directory", desc, true);

        //-- line break flag
        desc = "Sets the line separator style for the desired platform.";
        allOptions.addFlag(ARGUMENT_LINE_SEPARATOR, "(unix | mac | win)", desc, true);

        //-- Force flag
        desc = "Suppresses non fatal warnings, such as overwriting files.";
        allOptions.addFlag(ARGUMENT_FORCE, "", desc, true);

        //-- Help flag
        desc = "Displays this help screen.";
        allOptions.addFlag(ARGUMENT_HELP, "", desc, true);

        //-- verbose flag
        desc = "Prints out additional messages when creating source.";
        allOptions.addFlag(ARGUMENT_VERBOSE, "", desc, true);

        //-- fail on first error flag
        desc = "Causes source generation to fail on the first error encountered.";
        allOptions.addFlag(ARGUMENT_FAIL_ON_ERROR, "", desc, true);

        //-- no descriptors flag
        desc = "Disables the generation of the Class descriptors.";
        allOptions.addFlag(ARGUMENT_DISABLE_DESCRIPTORS, "", desc, true);

        //-- mapping file flag
        desc = "Indicates that a mapping file should be generated.";
        allOptions.addFlag(ARGUMENT_GENERATE_MAPPING, "mapping filename", desc, true);

        //-- source generator types name flag
        desc = "Sets the source generator types name (SGTypeFactory).";
        allOptions.addFlag(ARGUMENT_TYPES, "types", desc, true);

        //-- We temporarily maintain backwards compatibility, but this argument is deprecated
        desc = "";
        allOptions.addFlag(ARGUMENT_TYPES_DEPRECATED, "collections class name", desc, true);

        //-- no marshaling framework methods
        desc = "Disables the generation of the methods specific to the XML marshaling framework.";
        allOptions.addFlag(ARGUMENT_NOMARSHALL, "", desc, true);

        //-- implements org.castor.xmlctf.CastorTestable?
        desc = "Implements some specific methods to allow the generated classes"
                + " to be used with Castor Testing Framework.";
        allOptions.addFlag(ARGUMENT_TESTABLE, "", desc, true);

        //-- use SAX1?
        desc = "Uses SAX 1 in the generated code.";
        allOptions.addFlag(ARGUMENT_SAX1, "", desc, true);

        //-- Source Generator Binding
        desc = "Sets the Source Generator Binding File name.";
        allOptions.addFlag(ARGUMENT_BINDING_FILENAME, "filename", desc, true);

        //-- Generates sources for imported XML Schemas
        desc = "Generates sources for imported XML schemas.";
        allOptions.addFlag(ARGUMENT_GENERATE_IMPORTED_SCHEMAS, "", desc, true);

        //-- Sets enumerated type to use a case insensitive lookup
        desc = "Sets enumerated types to use a case insensitive lookup.";
        allOptions.addFlag(ARGUMENT_CASE_INSENSITIVE, "", desc);

        //-- Sets enumerated type to use a case insensitive lookup
        desc = "Sets name conflict strategy to use (possible values are " 
            + "'informViaLog', 'warnViaConsoleDialog').";
        allOptions.addFlag(ARGUMENT_NAME_CONFLICT_STRATEGY, "", desc);

        return allOptions;
    }

}
