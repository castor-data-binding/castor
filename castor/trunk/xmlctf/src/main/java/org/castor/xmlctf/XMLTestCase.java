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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.castor.xmlctf.util.CTFUtils;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.tests.framework.testDescriptor.CallMethod;
import org.exolab.castor.tests.framework.testDescriptor.Configuration;
import org.exolab.castor.tests.framework.testDescriptor.ConfigurationType;
import org.exolab.castor.tests.framework.testDescriptor.FailureType;
import org.exolab.castor.tests.framework.testDescriptor.ListenerType;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.exolab.castor.tests.framework.testDescriptor.Value;
import org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType;
import org.exolab.castor.tests.framework.testDescriptor.types.TypeType;
import org.exolab.castor.util.NestedIOException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.UnmarshalListener;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * This class encapsulates all the common logic to run the test patterns for
 * Castor XML. Basically it handle the marshalling/marshalling/comparing. This
 * is used to factor the common code for the source generator test and the
 * mapping/introspection tests as only the setup differ in the test patterns.
 * <p>
 * This class is not complete and expects to be extended.
 *
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-26 15:14:53 -0600 (Wed, 26 Apr 2006) $
 */
public abstract class XMLTestCase extends TestCase {

    /** True if we desire a lot of info on what happen. */
    public static boolean _verbose;
    /** True if we dump the stack trace for any exception that occurs during testing. */
    protected static boolean _printStack;

    static {
        String v = System.getProperty(TestCaseAggregator.VERBOSE_PROPERTY);
        _verbose = (v!=null && v.equals(Boolean.TRUE.toString()));

        v = System.getProperty(TestCaseAggregator.PRINT_STACK_TRACE);
        _printStack = (v!=null && v.equals(Boolean.TRUE.toString()));
    }

    /** Name of the test suite to which this test belongs. */
    protected String               _suiteName;
    /** The name of the root class for this test.  Must be set by a concrete class
     * if a test with a random object is used. */
    protected String               _rootClassName;
    /** The root class for this test.  Must be set by a concrete class. */
    protected Class                _rootClass;
    /** If true, the dumpFields() function has been implemented in the root class. */
    protected boolean              _hasDump;
    /** The name of the mapping file to use in a Marshalling Framework Test Case.
     * Must be set by a concrete class if a test with a reference document is used. */
    protected Mapping              _mapping;
    /** A listener for marshalling. */
    protected Object               _listener;
    /** Type of listener test -- Marshal, Unmarshal or Both. */
    protected TypeType             _listenerType;
    /** Gold file for listener. */
    protected String               _listenerGoldFile;
    /** The Configuration the Marshalling Framework. */
    protected Configuration        _configuration;
    /** Name of this test. */
    protected final String         _name;
    /** The unit test case this class represent. */
    protected final UnitTestCase   _unitTest;
    /** Place where the temporary file have to be put. */
    protected final File           _outputRootFile;
    /** True if the test needs to be skipped. */
    protected final boolean        _skip;
    /** The failure object that is not null if the test is expected to fail. */
    protected final FailureType    _failure;
    /** Used only to retrieve the classloader. */
    protected final CastorTestCase _test;

    /**
     * Instantiate a new XMLTestCase with the given name.
     *
     * @param name the name of this test case.
     */
    public XMLTestCase(final String name) {
        super(name);
        _name = cleanup(name);
        throw new IllegalArgumentException("You cannot use the name-only constructor");
    }

    /**
     * Instantiates a new test case that represents the given UnitTestCase.
     *
     * @param test the reference to the jar/directory
     * @param unit the UnitTestCase that wraps the configuration for this XML Test case.
     */
    public XMLTestCase(final CastorTestCase test, final UnitTestCase unit) {
        super(unit.getName());
        _name           = cleanup(unit.getName());
        _unitTest       = unit;
        _skip           = unit.getSkip();
        _failure        = unit.getFailure();
        _test           = test;
        _outputRootFile = test.getOutputRootFile();
    }

    /**
     * Instantiates a new test cases that has the same configuration of the given
     * test case.
     *
     * @param name the name of the test case
     * @param tc the XML test case that hold the configuration for this test case
     */
    public XMLTestCase(final String name, final XMLTestCase tc) {
        super(name);
        _name           = cleanup(tc._name);
        _unitTest       = tc._unitTest;
        _outputRootFile = tc._outputRootFile;
        _skip           = tc._skip;
        _failure        = tc._failure;
        _test           = tc._test;
    }

    /**
     * Returns a version of the input name that is suitable for JUnit test case
     * or test suite use. Apparently, JUnit truncates test case names after
     * encountering certain characters, so we scrub those characters from the
     * test case or test suite name.
     * <p>
     * We also translate all whitespace to blanks, remove all leading and
     * trailing whitespace, and collapse consecutive whitespace to a single
     * blank.
     *
     * @param name
     *            the input name
     * @return a name suitable for JUnit test case or test suite use.
     */
    static String cleanup(final String name) {
        return name.replaceAll("[\\*()\\t\\n\\r\\f]", " ").replaceAll(" {2,}", " ").trim();
    }

    /**
     * Sets the name of the test suite to which this test case belongs to.
     *
     * @param suiteName the name of the test suite.
     *
     */
    public void setTestSuiteName(final String suiteName) {
        _suiteName = cleanup(suiteName);
    }

    /**
     * Returns the name of the test suite to which this test case belongs to.
     * @return the name of the test suite to which this test case belongs to.
     */
    public String getTestSuiteName() {
        return _suiteName;
    }

    /**
     * Called when a test case throws an Exception. Returns true if we expected
     * this test to fail (and if this was the correct Exception class, if one is
     * specified, and if this was the correct test step for the failure to
     * occur, if one was specified).
     *
     * @param exception
     *            the Exception that was thrown
     * @param checkStep
     *            the test step that threw an Exception
     * @return true if we pass the test (because we expected this Exception)
     */
    protected boolean checkExceptionWasExpected(final Exception exception, final FailureStepType checkStep) {
        if (_printStack) {
            exception.printStackTrace();
        }
        // If we're not expecting any failure, then we're not expecting any Exception
        if (_failure == null || !_failure.getContent()) {
            return false;
        }
        if (checkStep == null) {
            fail("CTF coding failure ... checkStep should never be null");
        }

        // We expect failure.  Is this the failure we expected?

        String          exceptionName = _failure.getException();
        FailureStepType expectedStep  = _failure.getFailureStep();

        // If no specific step or Exception, then any failure is OK
        if (exceptionName == null && expectedStep == null) {
            return true;
        }

        // If we're expecting an exception, make sure we got the right one
        Exception ex = exception;
        if (ex instanceof NestedIOException) {
            ex = ((NestedIOException)ex).getException();
        }

        if (exceptionName != null) {
            try {
                Class expected = Class.forName(exceptionName);
                if (!expected.isAssignableFrom(ex.getClass())) {
                    String complaint = "Received Exception: '" + ex.getClass().getName()
                            + ": " + ex + "' but expected: '" + exceptionName + "'.";

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.print(complaint);
                    if (_verbose) {
                        pw.println("Stacktrace for the Exception that was thrown:");
                        ex.printStackTrace(pw);
                    }
                    pw.flush();
                    System.out.println(sw.toString());

                    fail(complaint);
                }

            } catch (ClassNotFoundException cnfex) {
                fail("The Exception specified: '" + exceptionName + "' cannot be found in the CLASSPATH");
            }
        }

        // If we're expecting to fail at a specific step, make sure this is that step
        if (expectedStep != null && !expectedStep.equals(checkStep)) {
            fail("We expected to fail at test step '" + expectedStep.toString()
                 + "' but actually failed at step '" + checkStep.toString() + "'");
        }

        // We got the expected failure!  Return true.
        return true;
    }

    /**
     * Marshals the object with the configuration of the test.
     *
     * @param object the object to marshall.
     * @param fileName the name of the file where to marshall the object.
     * @return a file containing marshalled output.
     */
    protected File testMarshal(final Object object, final String fileName) {
        verbose("--> Marshaling to: '" + fileName + "'");

        File marshalOutput = new File(_outputRootFile, fileName);

        try {
            Marshaller marshaller = createMarshaler(marshalOutput);
            marshaller.marshal(object);
        } catch (Exception e) {
            if (!checkExceptionWasExpected(e, FailureStepType.MARSHAL_TO_DISK)) {
                fail("Exception marshaling to disk " + e);
            }
            return null;
        }

        if (_failure != null && _failure.getContent() && _failure.getFailureStep() != null &&
             _failure.getFailureStep().equals(FailureStepType.MARSHAL_TO_DISK)) {
            fail("Unmarshaling was expected to fail, but succeeded");
        }
        return marshalOutput;
    }

    private Marshaller createMarshaler(final File marshalOutput) throws Exception {
        Marshaller marshaller = new Marshaller(new FileWriter(marshalOutput));

        //-- Configuration for marshaler?  Use config from unit test case if available
        Configuration config = _unitTest.getConfiguration();
        if (config == null) {
            config = _configuration;
        }

        if (config != null) {
            ConfigurationType marshal = config.getMarshal();
            List returnValues = invokeEnumeratedMethods(marshaller, marshal);
            returnValues.clear(); // We don't care about the return values
        }//-- config != null

        if (_mapping != null) {
            marshaller.setMapping(_mapping);
        }

        if (_listener != null && _listener instanceof MarshalListener
                && _listenerType.getType() != TypeType.UNMARSHAL_TYPE) {
            marshaller.setMarshalListener((MarshalListener)_listener);
        }
        return marshaller;
    }

    /**
     * Unmarshals the given file with the configuration of that test. If we
     * unmarshal null, complain and fail the test case.
     *
     * @param file the file containing the xml document to unmarshall.
     * @return the result of the unmarshalling of the given file.
     * @throws Exception if anything goes wrong during the test
     */
    protected Object testUnmarshal(final File file) throws Exception {
        verbose("--> Unmarshaling '" + file.getName() + "'\n");
        InputStream inputStream = new FileInputStream(file);
        Object unmarshaledObject = testUnmarshal(inputStream);
        inputStream.close();
        assertNotNull("Unmarshaling '" + file.getName() + "' results in a NULL object.",
                      unmarshaledObject);
        return unmarshaledObject;
    }

    /**
     * Unmarshals the given input stream with the configuration of that test.
     *
     * @param stream
     *            the input stream containing the xml document to unmarshall.
     * @return the result of the unmarshalling of the given file, null if
     *         anything went wrong.
     * @throws Exception if anything goes wrong during the test
     */
    protected Object testUnmarshal(final InputStream stream) throws Exception {
        Object result = null;
        final Unmarshaller unmar = createUnmarshaler();
        result = unmar.unmarshal(new InputSource(stream));
        return result;
    }

    private Unmarshaller createUnmarshaler() throws Exception {
        //-- Configuration for unmarshaler?  Use config from unit test case if available
        Configuration config = _unitTest.getConfiguration();
        if (config == null) {
            config = _configuration;
        }

        final Unmarshaller unmar;
        if (_mapping != null) {
            unmar = new Unmarshaller(_mapping);
        } else {
            if (_test.getClassLoader() != null) {
                unmar = new Unmarshaller(_rootClass, _test.getClassLoader());
            } else {
                unmar = new Unmarshaller(_rootClass);
            }
        }

        if (_listener != null && _listener instanceof UnmarshalListener
                && _listenerType.getType() != TypeType.MARSHAL_TYPE) {
            unmar.setUnmarshalListener((UnmarshalListener)_listener);
        }

        unmar.setDebug(_verbose);

        if (config != null) {
            ConfigurationType unmarshal = config.getUnmarshal();
            List returnValues = invokeEnumeratedMethods(unmar, unmarshal);
            returnValues.clear(); // We don't care about the return values
        }

        return unmar;
    }

    /**
     * Invokes all requested methods on the object we are supplied.  Keeps track
     * of the objects returned from each invocation and returns a List containing
     * all of the return values.  Some of the return values may be null.  This
     * is not an error!
     *
     * @param objectInvoked the object on which to invoke the methods
     * @param config configuration object listing the methods we are to call
     * @throws java.lang.Exception if anything goes wrong during the test
     */
    protected List invokeEnumeratedMethods(final Object objectInvoked, final ConfigurationType config)
                                                                  throws java.lang.Exception {
        final List returnValues = new LinkedList();

        if (config == null) {
            return returnValues;
        }

        Enumeration methods = config.enumerateCallMethod();
        //-- For each method defined, we invoke it on marshaller just created.
        while (methods.hasMoreElements()) {
            CallMethod method = (CallMethod) methods.nextElement();
            //-- search for the method to invoke
            Method toBeinvoked = getInvokeMethod(objectInvoked.getClass(), method.getName(), method.getValue());
            //-- construct the objects representing the arguments of the method
            Object[] arguments = getArguments(method.getValue());
            //-- Invoke the method and keep a copy of the returned object
            returnValues.add(toBeinvoked.invoke(objectInvoked, arguments));
        }

        return returnValues;
    }

    /**
     * Initialize listeners for marshalling/unmarshalling
     *
     * @param listener the listener to initialize
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    protected void initializeListeners(final ListenerType listener) throws ClassNotFoundException,
                                                                     IllegalAccessException,
                                                                     InstantiationException {
        String listenerClassName = listener.getClassName();
        if (listenerClassName == null || listenerClassName.length() == 0) {
            throw new IllegalArgumentException("ClassName must be provided for Listener element.");
        }

        final Class listenerClass;
        if (_test.getClassLoader() != null) {
            listenerClass = _test.getClassLoader().loadClass(listenerClassName);
        } else {
            listenerClass = Thread.currentThread().getContextClassLoader().loadClass(listenerClassName);
        }

        Object o = listenerClass.newInstance();
        if (o instanceof UnmarshalListener || o instanceof MarshalListener) {
            _listener = o;
        } else {
            _listener = null;
        }

        _listenerGoldFile = listener.getGoldFile();
        _listenerType = listener.getType();
    }

    /**
     * Returns an instance of the object model hardcoded in the given
     * ObjectModelBuilder.
     *
     * @param builderName the name of the class used as a builder
     * @return an instance of the object model hardcoded in the given
     *         ObjectModelBuilder.
     * @throws java.lang.Exception if anything goes wrong during the test
     */
    protected Object buildObjectModel(final String builderName) throws java.lang.Exception {
        Class builderClass = null;
        if (_test.getClassLoader() != null) {
            builderClass = _test.getClassLoader().loadClass(builderName);
        } else {
            builderClass = this.getClass().getClassLoader().loadClass(builderName);
        }
        ObjectModelBuilder builder = (ObjectModelBuilder)builderClass.newInstance();
        return builder.buildInstance();
    }

    private Method getInvokeMethod(final Class dataBinder, final String methodName,
                                   final Value[] values)
                                 throws ClassNotFoundException, NoSuchMethodException {
        if (methodName == null) {
            throw new IllegalArgumentException("The name of the method to invoke is null");
        }
        Class[] argumentsClass = null;

        //-- the value object represents the arguments of the method if any
        if (values != null && values.length > 0) {
            argumentsClass = new Class[values.length];
            for (int i = 0; i < values.length; i++) {
                Value value = values[i];
                argumentsClass[i] = CTFUtils.getClass(value.getType(), _test.getClassLoader());
            }
        }
        return dataBinder.getMethod(methodName, argumentsClass);
    }

    private Object[] getArguments(final Value[] values) throws ClassNotFoundException, MarshalException {
        Object[] result = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            Value value = values[i];
            result[i] = CTFUtils.instantiateObject(value.getType(), value.getContent(), _test.getClassLoader());
        }
        return result;
    }

    /**
     * print the message if in verbose mode.
     * @param message the message to print
     */
    protected void verbose(final String message) {
        if (_verbose) {
            System.out.println(message);
        }
    }

}
