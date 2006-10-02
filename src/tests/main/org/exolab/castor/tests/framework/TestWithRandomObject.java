/*
 * Copyright 2005 Edward Kuns
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
 */
package org.exolab.castor.tests.framework;

import java.io.File;
import java.io.FileWriter;

import org.exolab.castor.tests.framework.testDescriptor.FailureType;

import junit.framework.TestCase;

/**
 * Implements a test case that tests code written by the XML source generator.
 * This class uses the generated source to write a randomly generated XML
 * element to a file.
 * <p>
 * The test follows this sequence:
 * <ol>
 *   <li>Instantiates a random object model using the randomize function.</li>
 *   <li>Marshals it to a file.</li>
 *   <li>Unmarshalls the created file.</li>
 *   <li>Check that the result object is equal to the start object.</li>
 * </ol>
 * 
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: $ $Date: $
 */
class TestWithRandomObject extends TestCase {
    /** We add this fixed string to the end of our testcase name */
    private static final String RANDOM = "_RandomObject";

    /** We delegate all actions to this test case */
    private final XMLTestCase _delegate;

    /**
     * The failure object that is not null is the test intends to fail
     */
    protected final FailureType _failure;

    /**
     * Blank constructor for this test case.  This contructor is not useful, since
     * no delegate test case is provided
     * @param name Name of our delegate test case
     */
    TestWithRandomObject(String name) {
        super(name+RANDOM);
        _delegate = null;
        _failure  = null;
    }

    /**
     * Constructs a test case that when invoked will delegate to the provided
     * test case.
     * @param name Name of our delegate test case
     * @param tc
     */
    TestWithRandomObject(String name, XMLTestCase tc) {
        super(name+RANDOM);
        _delegate = tc;
        _failure  = tc._failure;
    }

    /**
     * Runs our test case
     */
    public void runTest() {
        if (_delegate == null) {
            throw new IllegalStateException("No test specified to be run.");
        }
        
        verbose("\n------------------------------");
        verbose("Test with randomly generated object");
        verbose("------------------------------\n");
        if (_delegate._skip) {
            verbose("-->Skipping the test");
            return;
        }
        
        try {
            String outputName = _delegate._name.replace(' ', '_') + "-testWithRandomObject";
            
            // 1. Randomize an object model instance
            verbose("--> Randomize an object model for the root '" + _delegate._rootClassName + "'.");
            CastorTestable randomizedObject = ((CastorTestable)_delegate._rootClass.newInstance());
            assertNotNull("Randomized object model is null", randomizedObject);
            
            randomizedObject.randomizeFields();
            
            // 2. Dump the object in a file if possible
            if (_delegate._hasDump) {
                verbose("----> Dump the object to '" + outputName + "-ref.dump" +"'");
                FileWriter writer = new FileWriter(new File(_delegate._outputRootFile, outputName + "-ref.dump"));
                writer.write(randomizedObject.dumpFields());
                writer.close();
            }
            
            // 3. Marshal
            verbose("--> Marshalling to: '" + outputName +"'");
            File marshal_output = _delegate.testMarshal(randomizedObject, outputName + ".xml");
            
            // 4. Validate against a schema if any
            if (_delegate._schemaFile != null) {
                // TODO: Put validation code here
            }
            
            // 5. Unmarshal
            verbose("--> Unmarshalling '" + marshal_output + "'\n");
            
            Object  unmarshalledRandomizedObject = _delegate.testUnmarshal(marshal_output);
            assertNotNull("Unmarshalling '"+marshal_output.getName()+ "' results in a NULL object.", unmarshalledRandomizedObject);
            
            // 6. Dump the unmarshalled object in a file if possible
            if (_delegate._hasDump) {
                verbose("---->Dump the object to '" + outputName + "-unmar.dump" +"'");
                FileWriter writer = new FileWriter(new File(_delegate._outputRootFile, outputName + "-unmar.dump"));
                writer.write(((CastorTestable)unmarshalledRandomizedObject).dumpFields());
                writer.close();
            }
            
            // 7. compare to initial model instance
            boolean result = unmarshalledRandomizedObject.equals(randomizedObject);
            verbose("----> Compare unmarshalled document to reference object: " + ((result)?"OK":"### Failed ### "));
            assertTrue("The initial randomized object and the one resulting of the marshal/unmarshal process are different", result);
            assertTrue("-->The test case should have failed.",_failure == null || _failure.getContent() == false);
        } catch (Exception ex) {
            //the test was intended to fail
            if (_failure != null && _delegate.checkExceptionWasExpected(ex)) {
                assertTrue(_failure.getContent());
                return;
            }
            fail("Unable to process the test case:"+ex);
            if (XMLTestCase._printStack) {
                ex.printStackTrace(System.out);
            }
        }        
    }

    /**
     * Provides setup for our delegated test case, depending on the type of
     * test case we are delegating for.
     * @throws Exception if anything goes wrong during setup
     */
    protected void setUp() throws Exception {
        if (_delegate == null) {
            throw new IllegalStateException("No test specified to set up.");
        }

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
        if (_delegate == null) {
            throw new IllegalStateException("No test specified to tear down.");
        }

        if (_delegate instanceof MarshallingFrameworkTestCase) {
            ((MarshallingFrameworkTestCase)_delegate).tearDown();
        } else if (_delegate instanceof SourceGeneratorTestCase) {
            ((SourceGeneratorTestCase)_delegate).tearDown();
        }
    }

    private void verbose(String message) {
        _delegate.verbose(message);
    }

}
