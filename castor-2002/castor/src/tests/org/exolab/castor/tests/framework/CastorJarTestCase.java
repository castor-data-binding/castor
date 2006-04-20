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
package org.exolab.castor.tests.framework;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URLClassLoader;

import sun.misc.URLClassPath;

import org.exolab.castor.xml.MarshalException;

import org.exolab.castor.tests.framework.testDescriptor.TestDescriptor;
import org.exolab.castor.tests.framework.testDescriptor.MarshallingTest;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;

/**
 * 
 *
 * @author <a href="mailto:gignoux@intalio.com">Sebastien Gignoux</a>
 * @version $Revision$ $Date$
 */
public class CastorJarTestCase extends TestCase {

    /**
     * The jar file that contain the tests
     */
    private File _jarFile;
    
    /**
     * Class loader to use for the jar
     */
    private ClassLoader _loader;

    /**
     * Name of the test suite. Derived from the name of the jar.
     */
    private String _name;

    /**
     * The test descriptor from the jar
     */
    private TestDescriptor _testDescriptor;

    /**
     * Name of the ressource for the test descriptor XML document in the jar
     */
    private final static String TEST_DESCRIPTOR_PATH = "META-INF/TestDescriptor.xml";

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
    }



    public CastorJarTestCase(String name) {
        super(name);
    }

    public CastorJarTestCase(File file, String outputRoot) {
        super(file.getName());

        _jarFile = file;
        _name = _jarFile.getName().substring(0, _jarFile.getName().lastIndexOf("."));
        _outputRootFile = new File(outputRoot + FILE_SEPARATOR + _name);
        _outputRootFile.mkdirs();
        
        _loader =  new URLClassLoader(URLClassPath.pathToURLs(_jarFile.getAbsoluteFile().toString()),
                                      this.getClass().getClassLoader());
        
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

        InputStream descriptor = _loader.getResourceAsStream(TEST_DESCRIPTOR_PATH);

        if (descriptor == null) {
            return new TestSuite("jar '" + _jarFile.getName() + "' as no testDescriptor.xml");
        }

        try {
            _testDescriptor = TestDescriptor.unmarshal(new InputStreamReader(descriptor));
        } catch (Exception me) {
            me.printStackTrace();
            return null; // TODO : fail better
        }

        String suiteName = _testDescriptor.getName();
        TestSuite suite = new TestSuite(suiteName);

        verbose("Creating '" + suiteName + "' test suite");


        MarshallingTest     mar = _testDescriptor.getMarshallingTest();
        SourceGeneratorTest sg  = _testDescriptor.getSourceGeneratorTest();

        if (mar != null) {
            // Set up marshalling tests
            for (int i=0; i<mar.getUnitTestCaseCount(); ++i) {
                UnitTestCase tc = mar.getUnitTestCase(i);
                MarshallingFrameworkTestCase mftc = new MarshallingFrameworkTestCase(this, tc, mar, _outputRootFile);
                suite.addTest(mftc.suite());
            }
            
        } else if (sg != null) {
            // Set up source generator tests
            for (int i=0; i<sg.getUnitTestCaseCount(); ++i) {
                UnitTestCase tc = sg.getUnitTestCase(i);
                SourceGeneratorTestCase sgtc = new SourceGeneratorTestCase(this, tc, sg, _outputRootFile);
                suite.addTest(sgtc.suite());
            }
        }
        return suite;
    }

    
    public String getName() {
        return _name;
    }

    public ClassLoader getClassLoader() {
        return _loader;
    }

    public void setClassLoader(ClassLoader loader) {
        _loader = loader;
    }

    public File getJarFile() {
        return _jarFile;
    }
    
    /**
     * print the message if in verbose mode.
     */
    private void verbose(String message) {
        if (_verbose)
            System.out.println(message);
    }


}
