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
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: $
 */
package org.exolab.castor.tests.framework;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;

/**
 * This class encapsulate all the logic to run the tests patterns for the source
 * generator. It is able to run the source generator by itself and then compile
 * the file that have been generated.  This class does not do anything additional.
 * It only runs the source generator and ensures that the generated source will
 * compile without error.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
public class OnlySourceGenerationTestCase extends XMLTestCase {

    /** Generates and compiles source in a test harness, but does nothing else. */
    private final TestSourceGenerator        _sourceGenerator;

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
    public OnlySourceGenerationTestCase(CastorTestCase test, UnitTestCase unit, OnlySourceGenerationTest sourceGen, File outputRoot) {
        super(test, unit, outputRoot);
        _sourceGenerator = new TestSourceGenerator(test, unit, sourceGen, outputRoot);
    }

    /**
     * Create a new SourceGeneratorTestCase with the given name.
     * @param name name for the test case
     */
    public OnlySourceGenerationTestCase(String name) {
        super(name);
        _sourceGenerator = null;
    }

    /**
     * Returns the test suite for this given test setup.
     * @return the test suite for this given test setup.
     */
    public Test suite() {
        TestSuite suite  = new TestSuite(_name);
        suite.addTest(this);
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
        _sourceGenerator.setUp();
    }

    public void runTest() {
        _sourceGenerator.runTest();
        verbose("-->Done");
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
