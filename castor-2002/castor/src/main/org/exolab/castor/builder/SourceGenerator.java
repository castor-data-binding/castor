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

import org.exolab.javasource.*;
import org.exolab.castor.util.CommandLineOptions;

import org.xml.sax.*;
import org.xml.sax.helpers.ParserFactory;
      
import java.io.Reader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A Java Source generation tool using XML Schema definitions
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SourceGenerator {

    static final String appName = "XML Data Binder for Java";
    static String DEFAULT_PARSER_CLASS = "org.apache.xerces.parsers.SAXParser";
    
    private static final String version = "1.0 alpha (19991025)";
    
    private String lineSeparator = null;
    
    public SourceGenerator() {
        super();    
    } //-- SourceGenerator
    
    
    /**
     * Creates Java Source code for the Schema with the 
     * associated filename
     * @param filename the full path to the XML Schema definition
     * @param packageName the package for the generated source files
    **/
    public void generateSource(String filename, String packageName) {
        
        SGStateInfo sInfo = new SGStateInfo();
        sInfo.packageName = packageName;
        
        
        String parserClass = System.getProperty("org.xml.sax.parser");
        if ((parserClass == null) || (parserClass.length() == 0))
            parserClass = DEFAULT_PARSER_CLASS;
            
        Parser parser = null;
        try {
            parser = ParserFactory.makeParser(parserClass);
        }
        catch(java.lang.IllegalAccessException iae) {}
        catch(java.lang.ClassNotFoundException cnfe) {}
        catch(java.lang.InstantiationException ie) {};
        if (parser == null) {
            System.out.println("unable to create SAX parser");
            return;
        }
        
        SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller();
        parser.setDocumentHandler(schemaUnmarshaller);
        parser.setErrorHandler(schemaUnmarshaller);
        
        
        
        File schemaFile = new File(filename);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(schemaFile);
        }
        catch(java.io.FileNotFoundException fne) {
            System.out.println("unable to open XML schema file");
            return;
        }
        
        try {
            parser.parse(new InputSource(fileReader));
            fileReader.close();
        }
        catch(java.io.IOException ioe) {
            System.out.println("error reading XML Schema file");
            return;
        }
        catch(org.xml.sax.SAXException se) {
            if (se instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException)se;
                System.out.println("SAXParseException: " + spe);
                System.out.print(" - occured at line ");
                System.out.print(spe.getLineNumber());
                System.out.print(", column ");
                System.out.println(spe.getColumnNumber());
            }
            else System.out.println("SAXException: " + se);
            se.printStackTrace();
            return;
        }
        
        Schema schema = schemaUnmarshaller.getSchema();
        createClasses(schema, sInfo);
        
    } //-- generateSource
    
    
    /**
     * Returns the version number of this SourceGenerator
     * @return the version number of this SourceGenerator
    **/
    public static String getVersion() {
        return version;
    } //-- getVersion
    
    /**
     * main class used for command line invocation
    **/
    public static void main(String[] args) {
        
        SourceGenerator sgen = new SourceGenerator();
        
        
        CommandLineOptions cmdLineOpts = new CommandLineOptions();
        
        //-- filename flag
        cmdLineOpts.addFlag("i", "filename", "Sets the input filename");
        
        //-- package name flag
        cmdLineOpts.addFlag("package", "package-name", "Sets the package name");
        cmdLineOpts.setOptional("package", true);
        
        //-- line break flag
        String desc = "Sets the line separator style for the desired platform";
        cmdLineOpts.addFlag("line-separator", "( unix | mac | win)", desc);
        cmdLineOpts.setOptional("line-separator", true);
        
        //-- Process command line options
        Properties options = cmdLineOpts.getOptions(args);
        
        String schemaFilename = options.getProperty("i");
        String packageName = options.getProperty("package");
        String lineSepStyle = options.getProperty("line-separator");
        
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
        
        if (schemaFilename == null) {
            System.out.println(appName);
            cmdLineOpts.printUsage(new PrintWriter(System.out));
            return;
        }
        sgen.generateSource(schemaFilename, packageName);
        
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
        
        Enumeration elems = schema.getElementDecls();
        while (elems.hasMoreElements()) {
            ElementDecl eDecl = (ElementDecl) elems.nextElement();
            createClasses(eDecl, sInfo);
        }
    } //-- createClasses
    
    private void createClasses(ElementDecl elementDecl, SGStateInfo sInfo) {
        
        
        //-- create classes for sub-elements if necessary
        Archetype archetype = elementDecl.getArchetype();
            
        ClassInfo classInfo = new ClassInfo(elementDecl, sInfo.packageName);
        JClass jClass = null;
        
        if (archetype != null) {
            boolean alreadyGenerated = sInfo.sourceGenerated.contains(archetype);
            if (!alreadyGenerated) {
                process(archetype, sInfo);
                sInfo.sourceGenerated.addElement(archetype);
            }
            
            jClass = SourceFactory.createClassSource(classInfo);
            jClass.print(lineSeparator);
            jClass = MarshalInfoSourceFactory.createSource(classInfo);
            jClass.print(lineSeparator);
        }
        else {
            String typeRef = elementDecl.getTypeRef();
            if (typeRef != null) {
                if (TypeConversion.isBuiltInType(typeRef)) {
                    jClass = MarshalInfoSourceFactory.createSource(classInfo);
                    jClass.print(lineSeparator);
                }
                else {
                    System.out.print("Archetype or DataType '");
                    System.out.print(typeRef);
                    System.out.print("' not found for element: ");
                    System.out.println(elementDecl.getName());
                }

            }
        }
        
        
    }  //-- createClasses
    
    private void process(ContentModelGroup cmGroup, SGStateInfo sInfo) {
        Enumeration enum = cmGroup.enumerate();
        
        boolean alreadyGenerated = sInfo.sourceGenerated.contains(cmGroup);
        
        while (enum.hasMoreElements()) {
                
            SchemaBase base = (SchemaBase)enum.nextElement();
                
            switch(base.getDefType()) {
                    
                case SchemaBase.ELEMENT:
                    ElementDecl eDecl = (ElementDecl)base;
                    if (eDecl.isReference()) continue;
                        
                    if (!alreadyGenerated) {
                        createClasses(eDecl, sInfo);
                    }
                    break;
                case SchemaBase.GROUP:
                    process((Group)base, sInfo);
                    break;
                default:
                    break;
            }
        }
    }
} //-- SourceGenerator

