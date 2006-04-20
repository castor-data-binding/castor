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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.xml.schema.reader.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.types.BuiltInType;

import org.exolab.javasource.*;
import org.exolab.castor.util.CommandLineOptions;
import org.exolab.castor.util.Configuration;

import org.xml.sax.*;
      
import java.io.Reader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A Java Source generation tool which uses XML Schema definitions
 * to create an Object model
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
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
    static final String version = "0.8 (20000324)";

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
        "</a>, using an XML Schema.\n$" + "Id";
        
    private String lineSeparator = null;
    private JComment header = null;
    
    private boolean warnOnOverwrite = true;
    
    public SourceGenerator() {
        super();    
        
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
        createClasses(schema, sInfo);
    } //-- generateSource

    /**
     * Creates Java Source code (Object model) for the given XML Schema 
     * @param reader the Reader with which to read the XML Schema definition. 
     * The caller should close the reader, since thie method will not do so.
     * @param packageName the package for the generated source files
    **/
    public void generateSource(Reader reader, String packageName) {
        
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
        
        SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller();
        parser.setDocumentHandler(schemaUnmarshaller);
        parser.setErrorHandler(schemaUnmarshaller);
        
        try {
            parser.parse(new InputSource(reader));
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
     * @param filename the full path to the XML Schema definition
     * @param packageName the package for the generated source files
    **/
    public void generateSource(String filename, String packageName) 
        throws java.io.FileNotFoundException
    {
        
        FileReader reader = new FileReader(new File(filename));
        generateSource(reader, packageName);
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
     * main class used for command line invocation
    **/
    public static void main(String[] args) {
        
        SourceGenerator sgen = new SourceGenerator();
        
        
        CommandLineOptions allOptions = new CommandLineOptions();
        
        //-- filename flag
        allOptions.addFlag("i", "filename", "Sets the input filename");
        
        //-- package name flag
        allOptions.addFlag("package", "package-name", "Sets the package name");
        allOptions.setOptional("package", true);
        
        //-- line break flag
        String desc = "Sets the line separator style for the desired platform";
        allOptions.addFlag("line-separator", "( unix | mac | win)", desc);
        allOptions.setOptional("line-separator", true);
        
        desc = "Suppress non fatal warnings, such as overwritting files.";
        allOptions.addFlag("f", "", desc);
        
        //-- Process the specified command line options
        Properties options = allOptions.getOptions(args);
        
        String schemaFilename = options.getProperty("i");
        String packageName    = options.getProperty("package");
        String lineSepStyle   = options.getProperty("line-separator");
        boolean force         = (options.getProperty("f") != null);
        
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
        sgen.setLineSeparator(lineSep);
        sgen.setSuppressNonFatalWarnings(force);
        if (force) System.out.println("Suppressing non fatal warnings.");
        
        if (schemaFilename == null) {
            System.out.println(appName);
            allOptions.printUsage(new PrintWriter(System.out));
            return;
        }
        
        try {
            sgen.generateSource(schemaFilename, packageName);
        }
        catch(java.io.FileNotFoundException fne) {
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
            
        //-- handle all top-level archetypes
        structures = schema.getArchetypes();
        while (structures.hasMoreElements())
            processArchetype((Archetype)structures.nextElement(), sInfo);
        
        //-- handle all top-level datatypes
        structures = schema.getDatatypes();
        while (structures.hasMoreElements())
            processDatatype((Datatype)structures.nextElement(), sInfo);
        
        
    } //-- createClasses
    
    private void createClasses(ElementDecl elementDecl, SGStateInfo sInfo) {
        
        
        //-- create classes for sub-elements if necessary
        Archetype archetype = elementDecl.getArchetype();
            
        if (archetype != null) {
            JClass jClass = SourceFactory.createSourceCode(elementDecl,
                                                        sInfo,
                                                        sInfo.packageName);
            processArchetype(archetype, sInfo);
            
            processJClass(jClass, sInfo);
        }
        else {
            Datatype datatype = elementDecl.getDatatype();
            if (datatype == null) {
                String typeRef = elementDecl.getTypeRef();
                System.out.print("'type' or 'datatype' with name '" + typeRef);
                System.out.print("' not found for element: ");
                System.out.println(elementDecl.getName());
                return;
            }
            processDatatype(datatype, sInfo);
        } 
        
    }  //-- createClasses
    
    /**
     * Processes the given Archetype and creates all necessary class
     * to support it
     * @param archetype the Archetype to process
    **/
    private void processArchetype(Archetype archetype, SGStateInfo sInfo) {
        
        if (archetype == null) return;
        
        
        ClassInfo classInfo = sInfo.resolve(archetype);
        
        if (classInfo == null) {
            
            //-- handle top-leve archetypes
            if (archetype.isTopLevel()) {
                
                JClass jClass 
                    = SourceFactory.createSourceCode(archetype, 
                                                     sInfo, 
                                                     sInfo.packageName);
                processJClass(jClass, sInfo);                                                    
                
            }
            
            //-- process source archetype if necessary
            String source = archetype.getSource();
            if (source != null) {
                Schema schema = archetype.getSchema();
                processArchetype(schema.getArchetype(source), sInfo);
            }
            
            process(archetype, sInfo);
            
        }
        else {
            JClass jClass = classInfo.getJClass();
            if (!sInfo.processed(jClass)) {
                process(archetype, sInfo);
                processJClass(jClass, sInfo);
            }
        }
    } //-- processArchetype
    
    
    private void processDatatype(Datatype datatype, SGStateInfo sInfo) {
        
        if (datatype == null) return;
        
        String packageName = sInfo.packageName;
        //-- modify package name so we don't have
        //-- name collisions, since XML Schema uses
        //-- separate namespaces for elements and datatypes
        if (packageName == null) packageName = "types";
        else packageName += ".types";
            
        if (! (datatype instanceof BuiltInType) ) {
                                
            ClassInfo classInfo = sInfo.resolve(datatype);
            
            if (classInfo == null) {
                
                JClass jClass 
                    = SourceFactory.createSourceCode(datatype, 
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
    } //-- processDatatype
    
    private void process(ContentModelGroup cmGroup, SGStateInfo sInfo) {
        
        
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
                    process((Group)struct, sInfo);
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
        
        //-- print class
        jClass.setHeader(header);
        jClass.print(lineSeparator);
        
        //-- create MarshalInfo and print
        
        ClassInfo classInfo = state.resolve(jClass);
        if (classInfo != null) {
            JClass desc 
                = DescriptorSourceFactory.createSource(classInfo);
            desc.setHeader(header);
            desc.print(lineSeparator);
        }
        
        state.markAsProcessed(jClass);
    } //-- processClassInfo
    
} //-- SourceGenerator

