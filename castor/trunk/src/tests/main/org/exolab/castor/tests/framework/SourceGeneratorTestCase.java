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
package org.exolab.castor.tests.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.exolab.castor.tests.framework.testDescriptor.RootType;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;

/**
 * This class encapsulate all the logic to run the tests patterns for the source
 * generator. It is able to run the source generator by itself and then compile
 * the file that have been generated.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @version $Revision$ $Date: 2005-02-28 17:22:46 -0700 (Mon, 28 Feb 2005) $
 */
public class SourceGeneratorTestCase extends XMLTestCase {

    /** Contains the information for the configuration for all the tests in this jar. */
    protected final SourceGeneratorTest _sourceGenConf;
    /** If true, the randomize() function has been implemented in the root class. */
    protected final boolean             _hasRandom;
    /** Generates and compiles source in a test harness, but does nothing else. */
    private final TestSourceGenerator   _sourceGenerator;

    /**
     * Creates a new test case for the given setup.
     *
     * @param test the reference to the jar/directory
     * @param unit the UnitTestCase that wraps the configuration for this XML
     *            Test case.
     * @param sourceGen the Source Generator test to be executed
     * @param outputRoot the directory that contains the files needed for the
     *            test
     */
    public SourceGeneratorTestCase(final CastorTestCase test, final UnitTestCase unit,
                                   final SourceGeneratorTest sourceGen) {
        super(test, unit);
        _sourceGenConf   = sourceGen;
        _sourceGenerator = new TestSourceGenerator(test, unit, sourceGen);

        RootType rootType = _sourceGenConf.getRoot_Object();
        if (rootType == null) {
            throw new IllegalArgumentException("You must give a root object for a Source Generator Test"
                    + _outputRootFile + ", " +  getName());
        }

        _rootClassName    = rootType.getContent();
        if (_rootClassName == null) {
            throw new IllegalArgumentException("You must give a root object for a Source Generator Test"
                    + _outputRootFile + ", " +  getName());
        }

        _hasRandom = rootType.getRandom();
        _hasDump   = rootType.getDump();
    }

    /**
     * Create a new SourceGeneratorTestCase with the given name.
     * @param name name for the test case
     */
    public SourceGeneratorTestCase(final String name) {
        super(name);
        throw new IllegalArgumentException("You cannot use the name-only constructor");
    }

    /**
     * Returns the test suite for this given test setup.
     * @return the test suite for this given test setup.
     */
    public Test suite() {
        TestSuite suite  = new TestSuite(_name);

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
     * Sets up this test suite.
     * @throws java.lang.Exception if anything goes wrong
     */
    protected void setUp() throws java.lang.Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': setting up test '" + _name+"'");
        verbose("================================================\n");

        // Set up and run the source generator so we can test using the generated source
        _sourceGenerator.setUp();
        _sourceGenerator.runTest();

        // Set up the root class
        _rootClass = _test.getClassLoader().loadClass(_rootClassName);
    }

    /**
     * Cleans up after this unit test (nothing to do except provide output).
     * @throws java.lang.Exception never
     */
    protected void tearDown() throws java.lang.Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': test '" + _name+"' complete.");
        verbose("================================================\n");
        _sourceGenerator.tearDown();
    }

}
