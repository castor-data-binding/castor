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
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.castor.xmlctf;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.tests.framework.testDescriptor.ListenerType;
import org.exolab.castor.tests.framework.testDescriptor.MarshallingTest;
import org.exolab.castor.tests.framework.testDescriptor.RootType;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.xml.sax.InputSource;

/**
 * This class encapsulates all the logic to run the test patterns for the Castor
 * marshalling framework. This include introspection and mapping.
 *
 * @author <a href="mailto:gignoux@kernelcenter.com">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2004-09-10 18:23:03 -0600 (Fri, 10 Sep 2004) $
 */
public class MarshallingFrameworkTestCase extends XMLTestCase {

    /**
     * Contains the configuration for this test case. The configuration is
     * directly read for the test descriptor file located in a jar or in a
     * directory.
     */
    protected final MarshallingTest _marshallingConf;
    /** If true, the randomize() function has been implemented in the root class. */
    protected final boolean         _hasRandom;

    /**
     * Creates a CTF test case for the Marshalling framework.
     *
     * @param test A Test Case
     * @param unit A configuration element for a test case from a TestDescriptor
     *            configuration file
     * @param marshalling a Marshalling test definition from a TestDescriptor
     *            configuration file
     */
    public MarshallingFrameworkTestCase(final CastorTestCase test, final UnitTestCase unit,
                                        final MarshallingTest marshalling) {
        super(test, unit);
        _marshallingConf = marshalling;

        RootType rootType = _marshallingConf.getRoot_Object();
        if (rootType == null) {
            throw new IllegalArgumentException("You must give a root object for a Marshaling Test: "
                    + _outputRootFile + ", " +  getName());
        }

        _rootClassName = rootType.getContent();
        if (_rootClassName == null) {
            throw new IllegalArgumentException("You must give a root object for a Marshaling Test:"
                    + _outputRootFile + ", " +  getName());
        }

        _hasRandom = rootType.getRandom();
        _hasDump   = rootType.getDump();
    }

    /**
     * Create a new MarshallingFrameworkTestCase with the given name and a null
     * marshalling configuration.  This constructor should not be used!
     *
     * @param name Name for the MarshallingFrameworkTestCase
     */
    public MarshallingFrameworkTestCase(final String name) {
        super(name);
        throw new IllegalArgumentException("You cannot use the name-only constructor");
    }

    /**
     * Returns the test suite for this given test setup.
     * @return the test suite for this given test setup.
     */
    public Test suite() {
        TestSuite suite = new TestSuite(_name);

        String name = getTestSuiteName();
        name = (name != null) ? name + "#" + _name : _name;

        if (_unitTest.getCustomTest() != null) {
            suite.addTest(new TestWithCustomTest(name, this));
        } else {
            suite.addTest(new TestWithReferenceDocument(name, this));
            if (_hasRandom) {
                suite.addTest(new TestWithRandomObject(name, this));
            }
        }
        return suite;
    }

    /**
     * Sets up this test suite. Loads the mapping file if any.
     * <p>
     * Nothing in this setUp() method should ever be expected to fail. Thus,
     * there are no checks against _failure. If anything goes wrong here, there
     * is a problem with the individual test case configuration.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    protected void setUp() throws java.lang.Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': setting up test '" + _name+"'");
        verbose("================================================\n");

        FileServices.copySupportFiles(_test.getTestFile(),_outputRootFile);

        // Compile the source directory for this test, if not already done
        if (!_test.isDirectoryCompiled()) {
            verbose("-->Compiling any necessary source files in " + _outputRootFile);
            Compiler compiler = new SunJavaCompiler(_outputRootFile);
            if (_unitTest.hasJavaSourceVersion()) {
                compiler.setJavaSourceVersion(_unitTest.getJavaSourceVersion());
            }
            try {
                compiler.compileDirectory();
                _test.setDirectoryCompiled(true);
            } catch (CompilationException e) {
                if (_printStack) {
                    e.printStackTrace(System.out);
                }
                fail("Build Failed: " + e.getMessage());
            }
        }

        //-- Add outputRoot to classpath
        ClassLoader loader = _test.getClassLoader();
        loader = new URLClassLoader(new URL[] { _outputRootFile.toURL() }, loader);
        _test.setClassLoader(loader);

        verbose("Root class specified in TestDescriptor...");
        verbose("Loading class: " + _rootClassName);
        _rootClass = loader.loadClass(_rootClassName);

        // Try to load the mapping file if any, else we will use the introspector
        String mappingFilePath = null;
        if (_unitTest.getUnitTestCaseChoice() != null) {
            mappingFilePath = _unitTest.getUnitTestCaseChoice().getMapping_File();
        }

        if (mappingFilePath != null) {
            configureMapping(loader, mappingFilePath);
        } else {
            verbose("##### TESTING INTROSPECTION #####");
            _mapping = null;
        }
    }

    /**
     * Clean up after a test -- nothing to do except display output
     * @throws Exception if anything goes wrong
     */
    protected void tearDown() throws java.lang.Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': test '" + _name+"' complete.");
        verbose("================================================\n");
    }

    /**
     * For a test case with a mapping, load the mapping. If there is a listener,
     * initialize it.
     *
     * @param loader ClassLoader for this test case
     * @param mappingFilePath Path to the mapping file
     * @throws Exception if anything goes wrong during the test
     */
    private void configureMapping(final ClassLoader loader, final String mappingFilePath) throws Exception {
        verbose("##### TESTING MAPPING #####");
        verbose("Mapping file: " + mappingFilePath);
        InputStream mappingFile = loader.getResourceAsStream(mappingFilePath);

        if (mappingFile == null) {
            throw new FileNotFoundException("Unable to locate the mapping file '"
                     + mappingFilePath + "' for the test '" + _test.getName() + "'");
        }

        _mapping = new Mapping(loader);
        InputSource source = new InputSource(mappingFile);
        source.setSystemId(mappingFilePath);
        _mapping.loadMapping(source);
//      mappingFile.close(); // FIXME:  We never close this file!  (But cannot close it here)

        ListenerType listener = _unitTest.getListener();
        if (listener != null) {
            String listenerName = listener.getClassName();
            try {
                // See if we can load the class...
                initializeListeners(listener);
            } catch (ClassNotFoundException cnfex) {
                //Class#forName
                fail("The listener '" + listenerName + "' cannot be found in the CLASSPATH");
            } catch (InstantiationException iex) {
                fail("The listener '" + listenerName + "' cannot be instantiated");
            } catch (IllegalAccessException iaex) {
                fail("Constructing a '"+listenerName+"' failed: " + iaex);
            }
            verbose("##### TESTING LISTENER CLASS " + listenerName + " #####");
        } // listener != null;
    }

}
