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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.tests.framework;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.exolab.castor.tests.framework.ObjectModelBuilder;
import org.exolab.castor.tests.framework.CastorTestable;
import org.exolab.castor.tests.framework.testDescriptor.MarshallingTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.exolab.castor.tests.framework.testDescriptor.Root_Object;
import org.exolab.castor.tests.framework.testDescriptor.Listener;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;

import org.xml.sax.InputSource;

import java.lang.reflect.Method;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * This class encapsulate all the logic to run the test patterns for the
 * marshalling framework of castor. This include introspection and mapping.
 *
 * @author <a href="mailto:gignoux@kernelcenter.com">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public class MarshallingFrameworkTestCase extends XMLTestCase {

    /**
     * Contains the configuration for this
     * test case. The configuration is directly read
     * for the test descriptor file located in a jar or
     * in a directory.
     */
    protected MarshallingTest _marshallingConf;


    /**
     * Creates a CTF test case for the Marshalling framework.
     */
    public MarshallingFrameworkTestCase(CastorTestCase test, UnitTestCase unit, MarshallingTest marshalling, File outputRoot) {
        super(test, unit, outputRoot);
        _marshallingConf = marshalling;
        _hasRandom       = _marshallingConf.getRoot_Object().getRandom();
    }

    /**
     * Create a new test case with the same setup than the
     * MarshallingFrameworkTestCase given in parameter.
     */
    public MarshallingFrameworkTestCase(String name, MarshallingFrameworkTestCase mftc) {
        super(name, mftc);
        _marshallingConf = mftc._marshallingConf;
    }

    /**
     * Create a new MarshallingFrameworkTestCase with the given name.
     */
    public MarshallingFrameworkTestCase(String name) {
        super(name);
        _name = name;
    }

    /**
     * Returns the test suite for this given test setup.
     */
    public Test suite() {

        TestSuite suite  = new TestSuite(_name);

        // Use the default test implemented in XMLTestCase
        suite.addTest(new MarshallingFrameworkTestCase("testWithReferenceDocument", this));

        if (_hasRandom)
            suite.addTest(new MarshallingFrameworkTestCase("testWithRandomObject", this));

        return suite;
    }


    /**
     * Setup this test suite. Load the mapping file if any.
     */
    protected void setUp()
        throws java.lang.Exception {

        verbose("========================================\n");
        verbose("Setting up test for '" + _name + "' from '" + _test.name() + "'");

        //copy the support files--> needed for AdaptX XML Diff
        FileServices.copySupportFiles(_test.getTestFile(),_outputRootFile);

        _inputName  = _unitTest.getInput();
        _goldFileName = _unitTest.getGoldFile();

        if (_inputName != null)
            _input  = _test.getClassLoader().getResourceAsStream(_inputName);

        assert("The input file specified:"+_inputName+" cannot be found.", _input != null);

        Root_Object rootType = _marshallingConf.getRoot_Object();
        _rootClassName = rootType.getContent();
        _hasDump =   rootType.getDump();
        _hasRandom = rootType.getRandom();

        if (_rootClassName == null)
            throw new Exception("No Root Object found in test descriptor");

        _rootClass =  _test.getClassLoader().loadClass(_rootClassName);

        // Try to load the mapping file if any, else we will use the introspector

        String mappingFilePath = null;
        if (_unitTest.getUnitTestCaseChoice() != null)
            mappingFilePath = _unitTest.getUnitTestCaseChoice().getMapping_File();

        if (mappingFilePath == null) {
            verbose("##### TESTING INTROSPECTION #####");
            _mapping = null;
        } else {
            verbose("##### TESTING MAPPING #####");
            verbose("Mapping file: " + mappingFilePath);
            InputStream mappingFile = _test.getClassLoader().getResourceAsStream(mappingFilePath);

            if (mappingFile == null)
                throw new Exception("Unable to locate the mapping file '" + mappingFilePath + "' for the test '" + _test.name() + "'");

            _mapping = new Mapping(_test.getClassLoader());
            InputSource source = new InputSource(mappingFile);
            source.setSystemId(mappingFilePath);
            try {
                _mapping.loadMapping(source);
            } catch (org.exolab.castor.mapping.MappingException ex) {
                //the test was intended to fail
                if (_failure != null) {
                    //1--the exception is specified
                    String exceptionName = _failure.getException();
                    if (exceptionName != null) {
                       try {
                           Class expected = Class.forName(exceptionName);
                           if (expected.isAssignableFrom(ex.getClass())) {
                               assert(_failure.getContent() == true);
                               return;
                           }
                        } catch (ClassNotFoundException cnfex) {
                            //Class#forName
                            fail("The exception specified:"+exceptionName+" cannot be found in the CLASSPATH");
                        }
                    }
                    //2--No exception specified --> the test is a success.
                    else {
                        assert(_failure.getContent() == true);
                        return;
                    }
                }
                else throw new Exception(ex.toString());
            } //--mapping

            Listener listener = _unitTest.getListener();
            if ( listener != null ) {
                String listenerName = listener.getClassName();
                try {
                    // See if we can load the class...
                    initializeListeners(listener);
                } catch (ClassNotFoundException cnfex) {
                    //Class#forName
                    fail("The listener specified:"+listenerName+" cannot be found in the CLASSPATH");
                } catch (InstantiationException iex) {
                    fail("The listener specified:"+listenerName+" cannot be instantiated");
                } catch (IllegalAccessException iaex) {
                    fail("Constructing a :"+listenerName+" failed: " + iaex);
                }
                verbose("##### TESTING LISTENER CLASS " + listenerName + " #####");
        } // listener != null;


        }
    }


    /**
     * Clean up the tests.
     */
    protected void tearDown()
        throws java.lang.Exception {
        verbose("Test for '" + _name + "' complete");
        verbose("========================================");
    }

}
