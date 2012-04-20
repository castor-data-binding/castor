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
 * $Id: TestWithCustomTest.java 0000 2006-10-19 22:00:00Z ekuns $
 */
package org.castor.xmlctf;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.exolab.castor.tests.framework.testDescriptor.ConfigurationType;
import org.exolab.castor.tests.framework.testDescriptor.CustomTest;
import org.exolab.castor.tests.framework.testDescriptor.FailureType;
import org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType;
import org.exolab.castor.xml.XMLContext;

/**
 * Implements a test case that tests code written by the XML source generator.
 * This class uses a user-provided test class to test the generated source.
 * <p>
 * Each user-provided test is allowed to return a Boolean, either a primitive or
 * a java.lang.Boolean -- it does not matter. If the user-provided test returns
 * a Boolean and it is false, then the test is considered to have failed. If the
 * user-provided test throws <i>or returns</i> a Throwable, it is considered to
 * have failed. If the user-provided test returns <b>anything else</b>
 * (including void) then the test is considered to have passed.
 * <p>
 * Note: Returning Throwable is a little bit cleaner than throwing an Exception,
 * but either is acceptable as a sign of test failure. This is because when a
 * Throwable is returned, if -printStack is in effect, then the CORRECT stack
 * trace can be displayed and not a stack dump from the refective invocation.
 * <p>
 * There is no requirement that the user-provided test implement any interface,
 * nor any requirement that the user-provided test return anything at all.
 * However, a test that returns "void" and that never throws an Exception is not
 * a very useful test as it can never fail.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
class TestWithCustomTest extends TestCase {
    /** We add this fixed string to the end of our testcase name. */
    private static final String CUSTOM = "_CustomTest";

    /** We delegate all actions to this test case. */
    private final XMLTestCase   _delegate;

    /** The failure object that is not null is the test intends to fail. */
    protected final FailureType _failure;
    /** True if the test is supposed to return failure or throw an Exception. */
    protected final boolean     _failureExpected;

    /**
     * Blank constructor for this test case. This contructor is not useful,
     * since no delegate test case is provided.
     *
     * @param name
     *            Name of our delegate test case
     */
    TestWithCustomTest(final String name) {
        super(name + CUSTOM);
        throw new IllegalArgumentException("You cannot use the name-only constructor");
    }

    /**
     * Constructs a test case that when invoked will delegate to the provided
     * test case.
     * @param name Name of our delegate test case
     * @param tc
     */
    TestWithCustomTest(final String name, final XMLTestCase tc) {
        super(name + CUSTOM);
        _delegate        = tc;
        _failure         = tc._failure;
        _failureExpected = _failure != null && _failure.getContent();
    }

    /**
     * Provides setup for our delegated test case, depending on the type of
     * test case we are delegating for.
     * @throws Exception if anything goes wrong during setup
     */
    protected void setUp() throws Exception {
        _delegate.setXMLContext(new XMLContext());

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
     * @throws Exception when anything goes wrong (this is temporary)
     */
    public void runTest() {
        verbose("\n------------------------------");
        verbose("Run the custom test case");
        verbose("------------------------------\n");
        if (_delegate._skip) {
            verbose("-->Skipping the test");
            return;
        }

        List returnValues = null;

        try {
            CustomTest customTest = _delegate._unitTest.getCustomTest();
            ConfigurationType testConfig = customTest.getMethods();
            Object object = getTestObject(customTest.getTestClass());
            returnValues = _delegate.invokeEnumeratedMethods(object, testConfig);
        } catch (Exception e) {
            if (!_delegate.checkExceptionWasExpected(e, FailureStepType.CUSTOM_TEST)) {
                fail("Exception running the custom test " + e);
            }
            return;
        }

        // Loop over all our return values ... any FALSE or Throwable means a failure
        int count = 0;
        boolean testFailed = false;

        for (Iterator i = returnValues.iterator(); i.hasNext(); count++) {
            Object o = i.next();

            // If this test returned false and we DID NOT expect to fail, error!!!
            if (o instanceof Boolean && !((Boolean)o).booleanValue() && !_failureExpected) {
                // Mark failure, complain, but keep checking so we give ALL complaints
                testFailed = true;
                System.out.println("Custom test #" + count + " was expected to succeed, but returned false");
            }

            // If this test *returned* (not threw) an Exception, consider that a failure
            if (o instanceof Throwable && !_failureExpected) {
                testFailed = true;
                System.out.println("Custom test #" + count + " was expected to succeed, but returned Throwable");
                if (XMLTestCase._printStack) {
                    ((Throwable)o).printStackTrace();
                }
            }
        }

        // Did we fail to meet our expected result, either success or failure?
        if (testFailed ^ _failureExpected) {
            if (testFailed) {
                fail("The custom test failed");
            } else {
                fail("The custom test was expected to fail, but succeeded");
            }
        }
    }

    /**
     * Gets an instance of our test object, as configured.
     * @param testClassName name of the test class
     * @return an instance of our test object
     * @throws ClassNotFoundException when the test object's class cannot be found
     * @throws IllegalAccessException when the test object's constructor is private or protected
     * @throws InstantiationException when the test object is abstract or an interface
     */
    protected Object getTestObject(final String testClassName) throws ClassNotFoundException,
                                                 IllegalAccessException, InstantiationException {
        Class testObject = null;
        if (_delegate._test.getClassLoader() != null) {
            testObject = _delegate._test.getClassLoader().loadClass(testClassName);
        } else {
            testObject = this.getClass().getClassLoader().loadClass(testClassName);
        }
        return testObject.newInstance();
    }

    /**
     * print the message if in verbose mode.
     * @param message the message to print
     */
    private void verbose(final String message) {
        _delegate.verbose(message);
    }

}
