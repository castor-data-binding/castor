/*
 * Copyright 2006 Edward Kuns
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: OnlySourceGenerationTestCase.java 0000 2006-10-05 22:00:00Z ekuns $
 */
package org.castor.xmlctf;

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

    /** We add this fixed string to the end of our testcase name. */
    private static final String ONLY_GENERATION = "_OnlySourceGeneration";

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
    public OnlySourceGenerationTestCase(final CastorTestCase test, final UnitTestCase unit,
                                        final OnlySourceGenerationTest sourceGen) {
        super(test, unit);
        _sourceGenerator = new TestSourceGenerator(test, unit, sourceGen);
    }

    /**
     * Create a new SourceGeneratorTestCase with the given name.
     * @param name name for the test case
     */
    public OnlySourceGenerationTestCase(final String name) {
        super(name);
        _sourceGenerator = null;
    }

    /**
     * Returns the test suite for this given test setup.
     * @return the test suite for this given test setup.
     */
    public Test suite() {
        TestSuite suite  = new TestSuite(_name);

        String name = getTestSuiteName();
        name = (name != null) ? name + "#" + _name : _name;
        this.setName(name + ONLY_GENERATION);

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
