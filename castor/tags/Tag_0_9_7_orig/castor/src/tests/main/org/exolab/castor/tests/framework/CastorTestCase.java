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
 * Copyright 2002-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.tests.framework;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;

import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.exolab.castor.tests.framework.testDescriptor.TestDescriptor;
import org.exolab.castor.tests.framework.testDescriptor.MarshallingTest;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;
import org.exolab.castor.tests.framework.testDescriptor.SchemaTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;

/**
 * A class that abstracts a test case in the CTF.
 * A CTF test case can be driven by a directory or by a JAR file.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public class CastorTestCase extends TestCase {

    public static final short DIRECTORY = 0;
    public static final short JAR = 1;

    /**
     * Name of the ressource for the test descriptor XML document.
     */
    public final static String TEST_DESCRIPTOR = "TestDescriptor.xml";

    /**
     * Name of the ressource for the test descriptor XML document if a JAR file is used
     */
    private final static String TEST_DESCRIPTOR_JAR = "META-INF/TestDescriptor.xml";

    /**
     * A flag indicating whether or not the output root directory has
     * been compiled.
     */
    private boolean _compiled = false;
    
    /**
     * The file that contains the tests. This can either be a directory or
     * a jar file.
     */
    private File _testFile;

    /**
     * Class loader to use for the jar
     */
    private ClassLoader _loader;

    /**
     * The test descriptor from the jar
     */
    private TestDescriptor _testDescriptor;

    /**
     * The Type of the test (directory or jar)
     */
    private short _type;

    /**
     * Place where the temporary file have to be put
     */
    private File _outputRootFile;

    /**
     * File separator for this system.
     */
    private final static String FILE_SEPARATOR = System.getProperty("file.separator");

    
    /**
     * True if we expect a lot of info on what happen.
     */
    private static boolean _verbose;

    static {
        String v = System.getProperty(TestCaseAggregator.VERBOSE_PROPERTY);
        if (v!=null && v.equals("true"))
            _verbose = true;
        else
            _verbose = false;
        v = null;
    }

    /**
     * True if we dump the stack trace
     */
    private static boolean _printStack;
    static {
        String v = System.getProperty(TestCaseAggregator.PRINT_STACK_TRACE);
        if (v!=null && v.equals("true"))
            _printStack = true;
        else
            _printStack = false;
        v = null;
    }

    /**
     * Constructs a CTF test case given a name
     * @param name the name of the test case
     */
    public CastorTestCase(String name) {
        super(name);
    }

    /**
     * Constructs a CTF test case given an output path
     * @param file
     * @param outputRoot the ouput root where to print the reports
     */
    public CastorTestCase(File file, String outputRoot) {
        super(file.getName());

        if (file.isDirectory()) {
           _type = DIRECTORY;
           _outputRootFile = new File(outputRoot + FILE_SEPARATOR );
        }
        //else test if it is a JAR FILE
        //and select the name
        else {
            try {
                JarFile jar = new JarFile(file);
                _type = JAR;
                String fileName = file.getName();
                int index = fileName.lastIndexOf(".");
                fileName = fileName.substring(0, index);
                _outputRootFile = new File(outputRoot + FILE_SEPARATOR + fileName);
                jar = null;
            } catch (java.util.zip.ZipException e) {
                throw new IllegalStateException(file.getAbsolutePath()+" is not a valid JAR file.");
           } catch (java.io.IOException ie) {
               throw new IllegalStateException(file.getAbsolutePath()+" is not a valid JAR file.");
           }
        }

        try {
            //append the file (directory or jar) that contains the test case
            //to the classLoader
            URL[] urlList = {file.toURL()};
            _loader =  new URLClassLoader(urlList, this.getClass().getClassLoader());
        } catch (MalformedURLException urle) {
             //should never happen--> failure before
             urle.printStackTrace();
        }
        _testFile = file;
        _outputRootFile.mkdirs();
    }


    /**
     * Assembles and returns a test suite containing all known tests.
     *
     * New tests should be added here!
     *
     * @return A non-null test suite.
     */
    public Test suite() {

        // Get the test descriptor from the jar
        InputStream descriptor = null;
        if (_type == JAR)
            descriptor = _loader.getResourceAsStream(TEST_DESCRIPTOR_JAR);
        else  {
            descriptor = _loader.getResourceAsStream(TEST_DESCRIPTOR);
        }

        if (descriptor == null) {
            verbose("test '" + _testFile.getName() + "' has no testDescriptor.xml");
            return null;
        }

        try {
            _testDescriptor = (TestDescriptor) TestDescriptor.unmarshalTestDescriptor(new InputStreamReader(descriptor));
        } catch (ValidationException ve) {
            verbose("Error reading: "+_testFile.getAbsolutePath());
            verbose("-> " + ve.toString());
            if (_printStack)
                ve.printStackTrace(System.out);
            fail(ve.toString());
        } catch (MarshalException me) {
            verbose("Error reading: "+_testFile.getAbsolutePath());
            verbose("-> " + me.toString());
            if (_printStack)
                me.printStackTrace(System.out);
            fail(me.toString());
        }
        String suiteName = _testDescriptor.getName();
        TestSuite suite = new TestSuite(suiteName);
        
        verbose("Creating '" + suiteName + "' test suite");

        MarshallingTest mar = _testDescriptor.getTestDescriptorChoice().getMarshallingTest();
        SourceGeneratorTest sg  = _testDescriptor.getTestDescriptorChoice().getSourceGeneratorTest();
        SchemaTest schemaTest = _testDescriptor.getTestDescriptorChoice().getSchemaTest();

        if (mar != null) {
            
            // Set up marshalling tests
            for (int i=0; i<mar.getUnitTestCaseCount(); ++i) {
                UnitTestCase tc = mar.getUnitTestCase(i);
                MarshallingFrameworkTestCase mftc = new MarshallingFrameworkTestCase(this, tc, mar, _outputRootFile);
                mftc._configuration = mar.getConfiguration();
                mftc.setTestSuiteName(suiteName);
                suite.addTest(mftc.suite());
            }

        } else if (sg != null) {
            // Set up source generator tests
            for (int i=0; i<sg.getUnitTestCaseCount(); ++i) {
                UnitTestCase tc = sg.getUnitTestCase(i);
                SourceGeneratorTestCase sgtc = new SourceGeneratorTestCase(this, tc, sg, _outputRootFile);
                sgtc.setTestSuiteName(suiteName);
                suite.addTest(sgtc.suite());
            }
        } else if (schemaTest != null) {
            //set up for SchemaTest
            for (int i=0 ; i<schemaTest.getUnitTestCaseCount(); i++) {
                UnitTestCase tc = schemaTest.getUnitTestCase(i);
                SchemaTestCase stc = null;
                //little trick
                //getUnitTestCaseChoice should not be null at this point
                String name = tc.getUnitTestCaseChoice().getSchema();
                if (name.equals("*")) {
                    File[] list = _testFile.listFiles();
                    for (int j=0; j<list.length; ++j) {
                        String fileName = list[j].getName();
                        //kind of hack-->better use a file filter or better
                        //check with the SchemaReader can read the file
                        if (fileName.endsWith(FileServices.XSD)) {
                            tc.setName(suiteName+'#'+fileName);
                            stc = new SchemaTestCase(this, tc, _outputRootFile);
                            stc.setSchemaName(fileName);
                            suite.addTest(stc);
                        }
                    }
                }
                else {
                    tc.setName(suiteName+'#'+name);
                    stc = new SchemaTestCase(this, tc, _outputRootFile);
                    stc.setSchemaName(name);
                    suite.addTest(stc);
                }
            }
        }
        return suite;
    }

    
    public ClassLoader getClassLoader() {
        return _loader;
    }

    public File getTestFile() {
        return _testFile;
    }

    public short getType() {
        return _type;
    }
    
    /**
     * Returns a boolean that when true indicates the output directory has 
     * been compiled. This is useful for preventing the compilation of a directory 
     * multiple times when more than one test case exists in a given directory.
     * 
     * @return true when the output root directory has already been compiled.
     */
    public boolean isDirectoryCompiled() {
        return _compiled;
    } //-- isDirectoryCompiled
    
    
   /**
    * Sets the ClassLoader to use for loading the resources
    * for this test case.
    *
    * @param loader the class loader to use
    */
    public void setClassLoader(ClassLoader loader) {
        _loader = loader;
    }
    
    /**
     * Sets a flag to indicate the output directory has been compiled.
     * This prevents compiling a directory multiple times, uncessarily when
     * more than one test case exists in a given directory.
     */
    public void setDirectoryCompiled(boolean compiled) {
        _compiled = compiled;
    } //-- setDirectoryCompiled
    
    /**
     * print the message if in verbose mode.
     */
    private void verbose(String message) {
        if (_verbose)
            System.out.println(message);
    }


}
