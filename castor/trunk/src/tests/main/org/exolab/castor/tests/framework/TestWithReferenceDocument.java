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
 * $Id: TestWithReferenceDocument.java 0000 2006-10-19 22:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.exolab.castor.tests.framework.testDescriptor.FailureType;

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
 *   <li>Unmarshals the given input file (if any).</li>
 *   <li>Compare the result object with the provided object model (if any).</li>
 *   <li>Marshals the object to a file.</li>
 *   <li>Unmarshals the created file.</li>
 *   <li>Check that the result object is equal to the start object.</li>
 * </ol>
 *
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
class TestWithReferenceDocument extends TestCase {
    /** We add this fixed string to the end of our testcase name. */
    private static final String REFERENCE = "_ReferenceDocument";

    /** We delegate all actions to this test case. */
    private final XMLTestCase      _delegate;
    /** Used only to retrieved the classloader. */
    protected final CastorTestCase _test;
    /** The failure object that is not null is the test intends to fail. */
    protected final FailureType    _failure;
    /** Class name of the ObjectModelBuilder. */
    protected final String         _builderClassName;
    /** Header of the name of all our output files ... marshaled and dumped. */
    protected final String         _outputName;
    protected final String         _inputName;
    protected       String         _goldFileName; // FIXME: TEMPORARILY not final

    /**
     * Blank constructor for this test case.  This contructor is not useful, since
     * no delegate test case is provided.
     * @param name Name of our delegate test case
     */
    TestWithReferenceDocument(String name) {
        super(name+REFERENCE);
        _delegate         = null;
        _test             = null;
        _failure          = null;
        _outputName       = name.replace(' ', '_') + "-testWithReferenceDocument.xml";
        _builderClassName = null;
        _inputName        = null;
        _goldFileName     = null;
    }

    /**
     * Constructs a test case that when invoked will delegate to the provided
     * test case.
     * @param name Name of our delegate test case
     * @param tc
     */
    TestWithReferenceDocument(String name, XMLTestCase tc) {
        super(name+REFERENCE);
        _delegate         = tc;
        _test             = tc._test;
        _failure          = tc._failure;
        _outputName       = tc._name.replace(' ', '_') + "-testWithReferenceDocument.xml";
        _builderClassName = tc._unitTest.getObjectBuilder();
        _inputName        = tc._unitTest.getInput();
        _goldFileName     = tc._unitTest.getGoldFile();

        // FIXME: TEMPORARILY suppress GOLDFILE for sourcegen tests as a few tests are broken
        if (!(_delegate instanceof MarshallingFrameworkTestCase)) {
            _goldFileName = null;
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

    /**
     * Runs our test case using our delegate object where necessary.
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

        try {
            // 1. Get reference document from unmarshaler + input file (if input file is provided)
            Object refUnmarshal = getUnmarshaledReference();

            // 2. Get reference document from builder (if builder is provided)
            Object refGenerated = getBuilderReference();

            // 3. If we have two reference objects, make sure they are the same
            if (refUnmarshal != null && refGenerated != null) {
                compareReferenceObjects(refUnmarshal, refGenerated);
            }

            // 4. Pick our reference object (at least one should be non-null)
            final Object ref = (refUnmarshal != null) ? refUnmarshal : refGenerated;
            if (ref == null) {
                throw new Exception("There is no valid input file or hardcoded object in '" + _delegate._name + "'");
            }

            // 5. Marshal our reference object to disk
            File marshal_output = _delegate.testMarshal(ref, _outputName);

            // 6. Compare marshaled document with gold file (if one was provided)
            if (_goldFileName != null) {
                int result = CTFUtils.compare(_delegate._outputRootFile + "/" +  _goldFileName, marshal_output.getAbsolutePath());
                verbose("----> Compare marshaled document to gold file '" + _goldFileName + "': " + ((result == 0)?"OK":"### Failed ### "));

                if (_failure != null && _failure.getContent() == true)
                    assertTrue(result != 0);
                else {
                    assertEquals("The Marshaled object differ from the gold file", 0, result);
                }
            }

            // 7. Marshal the Listener and compare it to the listener gold file, if any
            compareListenerToItsGoldFile();

            // 8. Unmarshal the output file
            Object unmarshaledOutput = _delegate.testUnmarshal(marshal_output);

            // 9. Compare unmarshaled output file to ObjectModelBuilder if any.
            // TODO: Fix the tests that fail this comparison!
            // Right now many test classes (under xml/MasterTestSuite) do not override equals.
            // We could check "(ref instanceof CastorTestable)" except that several srcgen
            // tests fails this check.  (Probably bugs!)  For now we have this bogus
            // _builderClassName check.  We ideally want to ALWAYS do this comparison.
            if (_builderClassName != null) {
                // the equals method must be overriden
                boolean result  = unmarshaledOutput.equals(ref);
                if (result == false) {
                    verbose("Make sure the reference object model overrides Object#equals");
                }
                verbose("Compare to reference object: " + ((result)?"OK":" ### Failed ### "));
                assertTrue("The unmarshaled object differs from the hardcoded object.", result);
            }

            // 10. If everything above succeeded but we are supposed to fail, fail now
            assertTrue("-->The test case should have failed.", _failure == null || _failure.getContent() == false);

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
     * Returns a reference document as generated by unmarshaling the input
     * document provided, if one was provided.
     * @return a reference document from an unmarshaled input document
     * @throws Exception if anything goes wrong loading the input document
     */
    private Object getUnmarshaledReference() throws Exception {
        InputStream _input = null;
        if (_inputName != null) {
            _input = _test.getClassLoader().getResourceAsStream(_inputName);
            assertNotNull("The input file '" + _inputName + "' cannot be found.", _input);
        }

        Object refUnmarshal = null;
        if (_input != null) {
            verbose("--> Unmarshaling '" + _inputName  + "'\n");
            refUnmarshal = _delegate.testUnmarshal(_input);
            assertNotNull("Unmarshaling '" + _inputName + "' results in a NULL object.", refUnmarshal);
        }
        return refUnmarshal;
    }

    /**
     * Returns a reference document as generated by the hard-coded
     * ObjectModelBuilder.
     *
     * @return a reference document as generated by the hard-coded
     *         ObjectModelBuilder
     * @throws Exception if anything goes wrong executing creating the
     *             ObjectModelBuilder or using it to create the reference
     *             document.
     */
    private Object getBuilderReference() throws Exception {
        Object generated = null;
        if (_builderClassName != null) {
            generated = _delegate.buildObjectModel(_builderClassName);
            assertNotNull("The generated object with '" + _builderClassName + "' is null", generated);
        }
        return generated;
    }

    /**
     * Make sure the ObjectModelBuilder object and the unmarshaled input document
     * created the same object.
     * @param refUnmarshal reference object created from unmarshaling an input document
     * @param refGenerated reference object created by an ObjectModelBuilder
     * @throws IOException if we get an error dumping the objects to disk.
     */
    private void compareReferenceObjects(Object refUnmarshal, Object refGenerated) throws IOException {
        //the object model must override the equals method.
        boolean result = refGenerated.equals(refUnmarshal);
        verbose("----> Compare unmarshaled document to reference object: " + ((result)?"OK":"### Failed ### "));
        if (result == false) {
            verbose("Make sure the reference object model overrides Object#equals");
        }

        if (result == false && refGenerated instanceof CastorTestable) {
            // try to dump the unmarshaled object and the reference object
            FileWriter writer = new FileWriter(new File(_delegate._outputRootFile, _outputName + "-refModel.dump"));
            writer.write(((CastorTestable)refGenerated).dumpFields());
            writer.close();

            writer = new FileWriter(new File(_delegate._outputRootFile, _outputName + "-refUnmarshal.dump"));
            writer.write(((CastorTestable)refUnmarshal).dumpFields());
            writer.close();
        }

        assertTrue("The unmarshaled reference object differs from the hardcoded reference object.", result);
    }

    /**
     * Tests that the listener saw what it was supposed to see. If we have a
     * listener configured, marshal it to disk (now that it listened to the
     * previous marshal) and compare it to the listener gold file, if any.
     * <p>
     * We have to unregister the listener before we do anything. When we marshal
     * the MarshalListener, for every item marshaled the listener may grow and
     * may have more to marshal, and thus we may end up in an endless loop. For
     * example, a simple implementation of MarshalListener could log each
     * pre/post marshal invocation on a Vector to allow for later comparisons.
     * But this means that the object *being marshaled* keeps getting data added
     * to it during the marshaling!
     *
     * @throws Exception if anything goes wrong during the test
     */
    private void compareListenerToItsGoldFile() throws Exception {
        if (_delegate._listenerGoldFile == null || _delegate._listener == null) {
            return;
        }

        verbose("Compare listener to gold file: " + _delegate._listenerGoldFile);

        // Get the listener (before we unregister it!)
        Object listener = _delegate._listener;

        // Unregister the listener
        _delegate._listener = null;

        File outputFile = _delegate.testMarshal(listener, "Listener-" + _outputName);

        int result = CTFUtils.compare(_delegate._outputRootFile + "/" +  _delegate._listenerGoldFile, outputFile.getAbsolutePath());
        verbose("----> Compare marshaled document to gold file '" + _delegate._listenerGoldFile + "': " + ((result == 0)?"OK":"### Failed ### "));

        if (_failure != null && _failure.getContent() == true) {
            assertTrue(result != 0);
        } else {
            assertEquals("The Marshaled object differs from the gold file", 0, result);
        }
    }

    private void verbose(String message) {
        _delegate.verbose(message);
    }

}
