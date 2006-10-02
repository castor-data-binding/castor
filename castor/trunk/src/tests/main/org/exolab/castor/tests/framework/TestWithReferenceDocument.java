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
import java.io.InputStream;

import org.exolab.castor.tests.framework.testDescriptor.FailureType;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;

import junit.framework.TestCase;

/**
 * Implements a test case that tests code written by the XML source generator.
 * This class uses the generated source to read and write an XML document,
 * comparing the XML document written against the reference document that was
 * originally read in.
 * <p>
 * The test follows this sequence:
 * 
 * <ol>
 *   <li>Unmarshalls the given input file (if any).</li>
 *   <li>Compare the result object with the provided object model (if any).</li>
 *   <li>Marshals the object to a file.</li>
 *   <li>Unmarshalls the created file.</li>
 *   <li>Check that the result object is equal to the start object.</li>
 * </ol>
 * 
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: $ $Date: $
 */
class TestWithReferenceDocument extends TestCase {
    /** We add this fixed string to the end of our testcase name */
    private static final String REFERENCE = "_ReferenceDocument";

    /** We delegate all actions to this test case */
    private final XMLTestCase _delegate;

    /**
     * The unit test case this class represent
     */
    protected final UnitTestCase _unitTest;

    /**
     * The name of the input file for this test if any.
     */
    protected String _inputName;

    /**
     * The name of the gold file for this test if any.
     */
    protected String _goldFileName;

    /**
     * The input file for this test if any.
     */
    protected InputStream _input = null;

    /**
     * Used only to retrieved the classloader.
     */
    protected final CastorTestCase _test;

    /**
     * The failure object that is not null is the test intends to fail
     */
    protected final FailureType _failure;

    /**
     * Blank constructor for this test case.  This contructor is not useful, since
     * no delegate test case is provided
     * @param name Name of our delegate test case
     */
    TestWithReferenceDocument(String name) {
        super(name+REFERENCE);
        _delegate = null;
        _test     = null;
        _unitTest = null;
        _failure  = null;
    }

    /**
     * Constructs a test case that when invoked will delegate to the provided
     * test case.
     * @param name Name of our delegate test case
     * @param tc
     */
    TestWithReferenceDocument(String name, XMLTestCase tc) {
        super(name+REFERENCE);
        _delegate = tc;
        _test     = tc._test;
        _unitTest = tc._unitTest;
        _failure  = tc._failure;
    }

    /**
     * Runs our delegated test case.
     */
    public void runTest() {
        if (_delegate == null) {
            throw new IllegalStateException("No test specified to be run.");
        }

        verbose("\n------------------------------");
        verbose("Test with reference documents");
        verbose("------------------------------\n");
        if (_delegate._skip) {
            verbose("-->Skipping the test");
            return;
        }

        _inputName = _unitTest.getInput();
        if (_delegate instanceof MarshallingFrameworkTestCase) {
            _goldFileName = _unitTest.getGoldFile();
        } else if (_delegate instanceof SourceGeneratorTestCase) {
            _goldFileName = _unitTest.getOutput();
        } else {
            _goldFileName = null;
        }

        if (_inputName != null) {
            _input = _test.getClassLoader().getResourceAsStream(_inputName);
        }

        if (_delegate instanceof MarshallingFrameworkTestCase) {
            assertNotNull("The input file specified:"+_inputName+" cannot be found.", _input);
        }

        String outputName = _delegate._name.replace(' ', '_') + "-testWithReferenceDocument.xml";

        try {
            // 1. Unmarshall Input file if any
            Object out = null;

            if (_input != null) {
                verbose("--> Unmarshalling '" + _inputName  + "'\n");
                out = _delegate.testUnmarshal(_input);
                assertNotNull("Unmarshalling '"+ _inputName + "' results in a NULL object.", out);
            }

            // 2. Compare with ObjectModelBuilder if any
            String builderClassName = _delegate._unitTest.getObjectBuilder();
            Object generated = null;
            if (builderClassName != null) {
                generated = _delegate.buildObjectModel(builderClassName);
                assertNotNull("The generated object with '" + builderClassName + "' is null", generated);
            }

            if (out != null) {

                if (generated != null) {
                    //the object model must override the equals method.
                    boolean result = generated.equals(out);
                    verbose("----> Compare unmarshalled document to reference object: " + ((result)?"OK":"### Failed ### "));
                    if (result == false) {
                        verbose("Make sure the reference object model overrides Object#equals");
                    }

                    if (result == false && generated instanceof CastorTestable) {
                        // try to dump the unmarshalled object and the reference object
                        FileWriter writer = new FileWriter(new File(_delegate._outputRootFile, outputName + "-ref.dump"));
                        writer.write(((CastorTestable)generated).dumpFields());
                        writer.close();

                        writer = new FileWriter(new File(_delegate._outputRootFile, outputName + "-unmar.dump"));
                        writer.write(((CastorTestable)out).dumpFields());
                        writer.close();
                    }

                    assertTrue("The unmarshalled object differs from the hardcoded object.", result);
                    assertTrue("-->The test case should have failed.", _failure == null || _failure.getContent() == false);
                }

            } else if (generated != null) {
                // We don't have an input file, but we can use the hardcoded object for the next steps
                out = generated;
            } else {
                // we have no input file and no hardcoded object, we can't continue the tests
                throw new Exception("There is no valid input file or hardcoded object in '" + _delegate._name + "'");
            }

            // 3. Marshall the object
            /////////////////////////
            //change the outputName
            ////////////////////////
            verbose("--> Marshalling to: '" + outputName +"'\n");
            File marshal_output = _delegate.testMarshal(out, outputName);

            // 4. Compare with output file if any
            if (_goldFileName != null) {
                int result = CTFUtils.compare(_delegate._outputRootFile + "/" +  _goldFileName, marshal_output.getAbsolutePath());

                verbose("----> Compare marshalled document to gold file '" + _goldFileName + "': " + ((result == 0)?"OK":"### Failed ### "));
                if ((_failure != null) && (_failure.getContent() == true))
                    assertTrue(result != 0);
                else {
                    assertEquals("The Marshalled object differ from the gold file", 0, result);
                    assertTrue("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));
                }
            }

            // 5.  Marshal the Listener and compare it to the listener gold file, if any.
            if ( _delegate._listenerGoldFile != null && _delegate._listener != null ) {
                verbose("Compare listener to gold file: " + _delegate._listenerGoldFile);

                // Unregister the listener -- if we marshal a MarshalListener,
                // it may end up in an endless loop.  For example, a simple implementation
                // of MarshalListener could log each pre/post marshal invocation on a
                // Vector to allow for later comparisons.  But this means that the
                // object *being marshaled* keeps getting data added to it during the
                // marshaling -- each marshal call creates another object to be marshalled!
                Object listener = _delegate._listener;
                _delegate._listener = null;

                String listenerOutput = "Listener-" + outputName;

                File outputFile = _delegate.testMarshal(listener, listenerOutput);
                int result = CTFUtils.compare(_delegate._outputRootFile + "/" +  _delegate._listenerGoldFile, outputFile.getAbsolutePath());

                verbose("----> Compare marshalled document to gold file '" + _delegate._listenerGoldFile + "': " + ((result == 0)?"OK":"### Failed ### "));
                if ((_failure != null) && (_failure.getContent() == true)) {
                    assertTrue(result != 0);
                } else {
                    assertEquals("The Marshalled object differ from the gold file", 0, result);
                    assertTrue("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));
                }
            }

            // 6. umarshall output file and compare to ObjectModelBuilder if any
            verbose("--> Unmarshalling '" + marshal_output + "'\n");
            Object outAgain = _delegate.testUnmarshal(marshal_output);
            assertNotNull("Unmarshalling '"+marshal_output.getName()+ "' results in a NULL object.", outAgain);
            assertTrue("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));

            if (builderClassName != null) {
                //the equals method must be overriden
                boolean result  = outAgain.equals(out);
                if (result == false) {
                    verbose("Make sure the reference object model overrides Object#equals");
                }
                verbose("Compare to reference object: " + ((result)?"OK":" ### Failed ### "));
                assertTrue("The unmarshalled object differs from the hardcoded object.", result);
            }

        } catch (Exception ex) {
            if (_failure != null && _delegate.checkExceptionWasExpected(ex)) {
                assertTrue(_failure.getContent());
                return;
            }
            if (XMLTestCase._printStack) {
                ex.printStackTrace(System.out);
            }
            fail("Unable to process the test case:" + ex);
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
