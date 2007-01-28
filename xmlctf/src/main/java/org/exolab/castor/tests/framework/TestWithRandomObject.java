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
 * $Id: TestWithRandomDocument.java 0000 2006-10-19 22:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework;

import java.io.File;
import java.io.FileWriter;

import junit.framework.TestCase;

import org.exolab.castor.tests.framework.testDescriptor.FailureType;
import org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType;

/**
 * Implements a test case that tests code written by the XML source generator.
 * This class uses the generated source to write a randomly generated XML
 * element to a file.
 * <p>
 * The test follows this sequence:
 * <ol>
 *   <li>Instantiates a random object model using the randomize function.</li>
 *   <li>Marshals it to a file.</li>
 *   <li>Unmarshals the created file.</li>
 *   <li>Check that the result object is equal to the start object.</li>
 * </ol>
 *
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
class TestWithRandomObject extends TestCase {
    /** We add this fixed string to the end of our testcase name. */
    private static final String RANDOM = "_RandomObject";

    /** We delegate all actions to this test case. */
    private final XMLTestCase   _delegate;

    /** The failure object that is not null is the test intends to fail. */
    protected final FailureType _failure;
    /** File name of our marshaled output. */
    protected final String      _outputName;

    /**
     * Blank constructor for this test case.  This contructor is not useful, since
     * no delegate test case is provided
     * @param name Name of our delegate test case
     */
    TestWithRandomObject(String name) {
        super(name+RANDOM);
        throw new IllegalArgumentException("You cannot use the name-only constructor");
    }

    /**
     * Constructs a test case that when invoked will delegate to the provided
     * test case.
     * @param name Name of our delegate test case
     * @param tc
     */
    TestWithRandomObject(String name, XMLTestCase tc) {
        super(name+RANDOM);
        _delegate   = tc;
        _failure    = tc._failure;
        _outputName = _delegate._name.replace(' ', '_') + "-testWithRandomObject";
    }

    /**
     * Provides setup for our delegated test case, depending on the type of
     * test case we are delegating for.
     * @throws Exception if anything goes wrong during setup
     */
    protected void setUp() throws Exception {
        if (_delegate instanceof MarshallingFrameworkTestCase) {
            ((MarshallingFrameworkTestCase)_delegate).setUp();
        } else if (_delegate instanceof SourceGeneratorTestCase) {
            ((SourceGeneratorTestCase)_delegate).setUp();
        }
    }

    /**
     * Provides tear down for our delegated test case, depending on the type of
     * test case we are delegating for.
     * @throws Exception if anything goes wrong during teardown
     */
    protected void tearDown() throws Exception {
        if (_delegate instanceof MarshallingFrameworkTestCase) {
            ((MarshallingFrameworkTestCase)_delegate).tearDown();
        } else if (_delegate instanceof SourceGeneratorTestCase) {
            ((SourceGeneratorTestCase)_delegate).tearDown();
        }
    }

    /**
     * Runs our test case using our delegate object where necessary.
     */
    public void runTest() throws Exception { // FIXME - temporarily throws Exception
        verbose("\n------------------------------");
        verbose("Test with randomly generated object");
        verbose("------------------------------\n");
        if (_delegate._skip) {
            verbose("-->Skipping the test");
            return;
        }

        // Randomize an object model instance
        CastorTestable randomizedObject = getRandomizedReference();

        // Dump the new random object to a file
        if (_delegate._hasDump) {
            verbose("----> Dump the object to '" + _outputName + "-ref.dump" +"'");
            FileWriter writer = new FileWriter(new File(_delegate._outputRootFile, _outputName + "-ref.dump"));
            writer.write(randomizedObject.dumpFields());
            writer.close();
        }

        // Marshal our reference object to disk
        File marshal_output = _delegate.testMarshal(randomizedObject, _outputName + ".xml");
        if (marshal_output == null) {
            return;
        }

        // Unmarshal from disk
        Object unmarshaledRandomizedObject;
        try {
            unmarshaledRandomizedObject = _delegate.testUnmarshal(marshal_output);
        } catch (Exception e) {
            if (!_delegate.checkExceptionWasExpected(e, FailureStepType.SECOND_UNMARSHAL)) {
                fail("Exception Unmarshaling from disk " + e);
            }
            return;
        }

        if (_failure != null && _failure.getContent() && _failure.getFailureStep() != null &&
            _failure.getFailureStep().equals(FailureStepType.SECOND_UNMARSHAL)) {
            fail("Unmarshaling the marshaled random document was expected to fail, but succeeded");
        }

        // Dump the unmarshaled object to a file
        if (_delegate._hasDump) {
            verbose("---->Dump the object to '" + _outputName + "-unmar.dump" +"'");
            FileWriter writer = new FileWriter(new File(_delegate._outputRootFile, _outputName + "-unmar.dump"));
            writer.write(((CastorTestable)unmarshaledRandomizedObject).dumpFields());
            writer.close();
        }

        // Compare unmarshaled output file to our initial randomized instance
        boolean result = unmarshaledRandomizedObject.equals(randomizedObject);
        verbose("----> Compare unmarshaled document to reference object: " + ((result)?"OK":"### Failed ### "));

        final FailureStepType step = _failure != null ? _failure.getFailureStep() : null;
        final boolean expectedToFail = _failure != null && _failure.getContent()
                   && (step == null || step.equals(FailureStepType.COMPARE_TO_REFERENCE));

        if (_failure == null || !_failure.getContent()) {
            assertTrue("The initial randomized object and the one resulting of the" +
                       " marshal/unmarshal process are different", result);
        } else if (expectedToFail) {
            assertFalse("Comparing the random object to the marshal+unmarshaled one" +
                        " was expected to fail, but succeeded", result);
        }

        if (expectedToFail ^ result) {
            return;
        }

        if (_failure != null && _failure.getContent()) {
            fail("The test with random document was expected to fail, but passed");
        }
    }

    /**
     * Creates and returns a new instance of the root object set with random values.
     * @return a new instance of the root object set with random values.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private CastorTestable getRandomizedReference() throws InstantiationException, IllegalAccessException {
        verbose("--> Randomize an object model for the root '" + _delegate._rootClassName + "'.");
        CastorTestable randomizedObject = ((CastorTestable)_delegate._rootClass.newInstance());
        assertNotNull("Randomized object model is null", randomizedObject);
        randomizedObject.randomizeFields();
        return randomizedObject;
    }

    private void verbose(String message) {
        _delegate.verbose(message);
    }

}
