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
import org.exolab.castor.tests.framework.testDescriptor.Failure;
import org.exolab.castor.tests.framework.testDescriptor.Listener;
import org.exolab.castor.tests.framework.testDescriptor.types.TypeType;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.UnmarshalListener;

import org.exolab.adaptx.xslt.dom.XPNReader;
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xml.XMLDiff;

import org.xml.sax.InputSource;

import java.lang.reflect.Method;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

/**
 * This class encapsulates all the common logic to run the test patterns for
 * Castor XML. Basically it handle the marshalling/marshalling/comparing. This
 * is used to factorize the common code for the source generator test and the
 * mapping/introspection tests as only the setup differ in the test patterns.
 * @todo I18N of error messages.
 *
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public abstract class XMLTestCase extends TestCase {

    /**
     * Name of this test
     */
    protected String _name;

    /**
     * The name of the input file for this test if any.
     */
    protected String _inputName;

    /**
     * The name of the gold file for this test if any.
     */
    protected String _goldFileName;

    /**
     * The unit test case this class represent
     */
    protected UnitTestCase _unitTest;

    /**
     * ???
     */
    protected CastorTestCase _test;

    /**
     * Place where the temporary file have to be put
     */
    protected File _outputRootFile;

    /**
     * The input file for this test if any.
     */
    protected InputStream _input = null;

    /**
     * The name of the root class for this test
     */
    protected String _rootClassName;

    /**
     * The root class for this test
     */
    protected Class  _rootClass;

    /**
     * File for the schema in the temp directory
     */
    protected File _schemaFile = null;

    /**
     * If true, the dumpFields() function has been implemented in the root class.
     */
    protected boolean _hasDump;

   /**
    * The name of the mapping file to use in a Marshalling Framework Test
    * Case.
    */
    protected Mapping _mapping;

   /**
    * If true, the randomize() function has been implemented in the root class.
    */
    protected boolean _hasRandom;

   /**
    * True if the test needs to be skipped.
    */
    protected boolean _skip;

    /**
     * The failure object that is not null is the test intends to fail
     */
    protected Failure _failure;

    /**
     * True if we expect a lot of info on what happen.
     */
    protected static boolean _verbose;

    /**
     * True if we dump the stack trace
     */
    protected static boolean _printStack;

    /**
     * A listener for marshalling
     */
     protected Object _listener;

    /**
     * Type of listener test -- Marshal, Unmarshal or Both
     */
    protected TypeType _listenerType;

    /**
     * Gold file for listener
     */
    protected String _listenerGoldFile;



    /**
     * The ouput the unmarshalling test.
     */
    protected static Object _unmarshallingOutput;
    static {
        String v = System.getProperty(TestCaseAggregator.VERBOSE_PROPERTY);
        if (v!=null && v.equals(Boolean.TRUE.toString()))
            _verbose = true;
        else
            _verbose = false;
        v = System.getProperty(TestCaseAggregator.PRINT_STACK_TRACE);
        if (v!=null && v.equals(Boolean.TRUE.toString()))
            _printStack = true;
        else
            _printStack = false;
    }

   /**
    * Instantiate a new XMLTestCase with the given name.
    *
    * @param name the name of this test case.
    */
    public XMLTestCase(String name) {
        super(name);
        _name = name;
    }

    /**
     * Instantiates a new test case that represents the given UnitTestCase.
     *
     * @param test the reference to the jar/directory
     * @param unit the UnitTestCase that wraps the configuration for this XML Test case.
     * @param outputRoot the directory that contains the files needed for the test
     */
    public XMLTestCase(CastorTestCase test, UnitTestCase unit, File outputRoot) {
        super(unit.getName());
        _name            = unit.getName();
        _unitTest        = unit;
        _outputRootFile  = outputRoot;
        _skip            = unit.getSkip();
        _failure         = unit.getFailure();
        _test  = test;
    }

    /**
     * Instantiates a new test cases that has the same configuration of the given
     * test case.
     *
     * @param name the name of the test case
     * @param tc the XML test case that hold the configuration for this test case
     */
    public XMLTestCase(String name, XMLTestCase tc) {
        super(name);
        _name            = tc._name;
        _unitTest        = tc._unitTest;
        _outputRootFile  = tc._outputRootFile;
        _skip            = tc._skip;
        _failure         = tc._failure;
        _test            = tc._test;
    }

    /**
     * This test follows this sequence:
     *
     * <ol>
     *     <li>Instantiates a random object model using the randomize function.</li>
     *     <li>Marshals it to a file.</li>
     *     <li>Unmarshalls the created file.</li>
     *     <li>Check that the result object is equal to the start object.</li>
     * </ol>
     *
     */
    public void testWithRandomObject() {

        verbose("----------------------------\n");
        verbose("Test with randomly generated object");
        verbose("----------------------------\n");
        if (_skip) {
            verbose("-->Skipping the test");
            return;
        }

        try {
            String outputName = _name.replace(' ', '_') + "-testWithRandomObject";

            // 1. Randomize an object model instance
            verbose("--> Randomize an object model for the root '" + _rootClassName + "'.");
            CastorTestable randomizedObject = ((CastorTestable)_rootClass.newInstance());
            assertNotNull("Randomized object model is null", randomizedObject);

            randomizedObject.randomizeFields();

            // 2. Dump the object in a file if possible
            if (_hasDump) {
                verbose("----> Dump the object to '" + outputName + "-ref.dump" +"'");
                FileWriter writer = new FileWriter(new File(_outputRootFile, outputName + "-ref.dump"));
                writer.write(((CastorTestable)randomizedObject).dumpFields());
                writer.close();
            }

            // 3. Marshal
            verbose("--> Marshalling to: '" + outputName +"'");
            File marshal_output = testMarshal(randomizedObject, outputName + ".xml");

            // 4. Validate against a schema if any
            if (_schemaFile != null) {
                // TODO: Put validation code here
            }

            // 5. Unmarshal
            verbose("--> Unmarshalling '" + marshal_output + "'");

            Object  unmarshalledRandomizedObject = testUnmarshal(marshal_output);
            assertNotNull("Unmarshalling '"+marshal_output.getName()+ "' results in a NULL object.", unmarshalledRandomizedObject);

            // 6. Dump the unmarshalled object in a file if possible
            if (_hasDump) {
                verbose("---->Dump the object to '" + outputName + "-unmar.dump" +"'");
                FileWriter writer = new FileWriter(new File(_outputRootFile, outputName + "-unmar.dump"));
                writer.write(((CastorTestable)unmarshalledRandomizedObject).dumpFields());
                writer.close();
            }

            // 7. compare to initial model instance
            boolean result = unmarshalledRandomizedObject.equals(randomizedObject);
            verbose("----> Compare unmarshalled document to reference object: " + ((result)?"OK":"### Failed ### "));
            assert("The initial randomized object and the one resulting of the marshal/unmarshal process are different", result);
            assert("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));
         } catch (Exception ex) {
            //MarshallException or ValidationException

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
                       else fail("Received:'"+ex+"' but expected:'"+exceptionName+"'.");
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
             fail("Unable to process the test case:"+ex);
             if (_printStack)
                ex.printStackTrace(System.out);
        }
    }//testWithRandomObject

  /**
     * This test follows this sequence:
     *
     * <ol>
     *     <li>Unmarshalls the given input file (if any).</li>
     *     <li>Compare the result object with the provided object model (if any).</li>
     *     <li>Marshals the object to a file.</li>
     *     <li>Unmarshalls the created file.</li>
     *     <li>Check that the result object is equal to the start object.</li>
     * </ol>
     *
     */

    public void testWithReferenceDocument() {

        verbose("----------------------------\n");
        verbose("Test with reference documents");
        verbose("----------------------------\n");
        if (_skip) {
            verbose("-->Skipping the test");
            return;
        }

        String outputName = _name.replace(' ', '_') + "-testWithReferenceDocument.xml";
        try {
            // 1. Unmarshall Input file if any
            Object out = null;

            if (_input != null) {
                verbose("--> Unmarshalling '" + _inputName  + "'");
                out = testUnmarshal(_input);
                assertNotNull("Unmarshalling '"+_inputName+ "' results in a NULL object.", out);
            }

            // 2. Compare with ObjectModelBuilder if any
            String builderClassName = _unitTest.getObjectBuilder();
            Object generated = null;
            if (builderClassName != null) {
                generated = buildObjectModel(builderClassName);
                assertNotNull("The generated object with '" + builderClassName + "' is null", generated);
            }

            if (out != null) {

                if (generated != null) {
                    //the object model must override the equals method.
                    boolean result = generated.equals(out);
                    verbose("----> Compare unmarshalled document to reference object: " + ((result)?"OK":"### Failed ### "));
                    if (result == false)
                        verbose("Make sure the reference object model overrides Object#equals");

                    if ((result == false) && (generated instanceof CastorTestable)) {
                        // try to dump the unmarshalled object and the reference object
                        FileWriter writer = new FileWriter(new File(_outputRootFile, outputName + "-ref.dump"));
                        writer.write(((CastorTestable)generated).dumpFields());
                        writer.close();

                        writer = new FileWriter(new File(_outputRootFile, outputName + "-unmar.dump"));
                        writer.write(((CastorTestable)out).dumpFields());
                        writer.close();
                    }

                    assert("The unmarshalled object differs from the hardcoded object.", result);
                    assert("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));
                }

            } else if (generated != null) {
                // We don't have an input file, but we can use the hardcoded object for the next steps
                out = generated;
            } else {
                // we have no input file and no hardcoded object, we can't continue the tests
                throw new Exception("Unable to have a valid input file or a hardcoded object in '" + _name + "'");
            }

            // 3. Marshall the object
            /////////////////////////
            //change the outputName
            ////////////////////////
            verbose("--> Marshalling to: '" + outputName +"'");
            File marshal_output = testMarshal(out, outputName);

            // 4. Compare with output file if any
            if (_goldFileName != null) {

                int result = CTFUtils.compare(_outputRootFile + "/" +  _goldFileName, marshal_output.getAbsolutePath());

                verbose("----> Compare marshalled document to gold file '" + _goldFileName + "': " + ((result == 0)?"OK":"### Failed ### "));
                if ((_failure != null) && (_failure.getContent() == true))
                    assert(result != 0);
                else {
                    assert("The Marshalled object differ from the gold file", result==0);
                    assert("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));
                }
            }
             // 5.  Marshal the Listener and compare it to the listener gold file, if any.
            if ( _listenerGoldFile != null && _listener != null ) {
                verbose("Compare listener to gold file: " +_listenerGoldFile);

                // Unregister the listener -- if we marshal a MarshalListener,
                // it may end up in an endless loop.  For example, a simple implementation
                // of MarshalListener could log each pre/post marshal invocation on a
                // Vector to allow for later comparisons.  But this means that the
               // object *being marshaled* keeps getting data added to it during the
                // marshaling -- each marshal call creates another object to be marshalled!
                Object listener = _listener;
                _listener = null;

                String listenerOutput = "Listener-" + outputName;

                File outputFile = testMarshal(listener, listenerOutput);
                int result = CTFUtils.compare(
                    _outputRootFile + "/" +  _listenerGoldFile, outputFile.getAbsolutePath());

               verbose("----> Compare marshalled document to gold file '" + _listenerGoldFile + "': " + ((result == 0)?"OK":"### Failed ### "));
               if ((_failure != null) && (_failure.getContent() == true))
                    assert(result != 0);
                else {
                    assert("The Marshalled object differ from the gold file", result==0);
                    assert("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));
                }

            }

            // 6. umarshall output file and compare to ObjectModelBuilder if any
            verbose("--> Unmarshalling '" + marshal_output + "'");
            Object outAgain = testUnmarshal(marshal_output);
            assertNotNull("Unmarshalling '"+marshal_output.getName()+ "' results in a NULL object.", outAgain);
            assert("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));

            if (builderClassName != null) {
                //the equals method must be overriden
                boolean result  = outAgain.equals(out);
                if (result == false)
                    verbose("Make sure the reference object model overrides Object#equals");
                verbose("Compare to reference object: " + ((result)?"OK":" ### Failed ### "));
                assert("The unmarshalled object differs from the hardcoded object.", result);
            }

        } catch (Exception ex) {
            //MarshallException or ValidationException

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
                       else fail("Received:'"+ex+"' but expected:'"+exceptionName+"'.");
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
             fail("Unable to process the test case:"+ex);
             if (_printStack)
                ex.printStackTrace(System.out);
        }
    }

    /**
     * Marshals the object with the configuration of the test.
     *
     * @param object the object to marshall.
     * @param fileName the name of the file where to marshall the object.
     */
    protected File testMarshal(Object object, String fileName)
        throws java.lang.Exception
    {

        File marshalOutput    = new File(_outputRootFile, fileName);
        Marshaller marshaller = new Marshaller(new FileWriter(marshalOutput));

        if (_mapping != null)
            marshaller.setMapping(_mapping);
        if ( _listener != null && _listener instanceof MarshalListener
             && _listenerType.getType() != TypeType.UNMARSHAL_TYPE)
        {
            marshaller.setMarshalListener((MarshalListener)_listener);
        }


        try {
            marshaller.marshal(object);
        } catch (MarshalException e) {
            if (_printStack)
                e.printStackTrace();
            throw e;
        }
        return marshalOutput;
    }


    /**
     * Unmarshals the given file with the configuration of that test.
     *
     * @param file the file containing the xml document to unmarshall.
     * @return the result of the unmarshalling of the given file.
     */
    protected Object testUnmarshal(File file)
        throws java.lang.Exception {

        return testUnmarshal(new FileInputStream(file));
    }


    /**
     * Unmarshals the given input stream with the configuration of that test.
     *
     * @param stream the input stream containing the xml document to unmarshall.
     * @return the result of the unmarshalling of the given file.
     */
    protected Object testUnmarshal(InputStream stream)
        throws java.lang.Exception {

        Unmarshaller unmar;
        Object result = null;
        if (_mapping != null)
            unmar = new Unmarshaller(_mapping);

        else {
            if (_test.getClassLoader() != null)
                unmar = new Unmarshaller(_rootClass, _test.getClassLoader());
            else unmar = new Unmarshaller(_rootClass);
        }

        if ( _listener != null && _listener instanceof UnmarshalListener
             && _listenerType.getType() != TypeType.MARSHAL_TYPE)
        {
            unmar.setUnmarshalListener((UnmarshalListener)_listener);
        }

        unmar.setDebug(_verbose);
        try {
            result =  unmar.unmarshal(new InputSource(stream));
        } catch (MarshalException e) {
            if (_printStack)
                e.printStackTrace();
            throw e;
        }
        return result;
    }

    /**
     * Initialize listeners for marshalling/unmarshalling
     */
    protected void initializeListeners (Listener listener)
        throws java.lang.ClassNotFoundException,
               java.lang.IllegalAccessException,
               java.lang.InstantiationException
   {
        String listenerClassName = listener.getClassName();
        if ( listenerClassName == null || listenerClassName.length() == 0 )
            throw new IllegalArgumentException(
                "ClassName must be provided for Listener element.");

        Class listenerClass;
        if (_test.getClassLoader() != null)
            listenerClass = _test.getClassLoader().loadClass(listenerClassName);
        else
            listenerClass = Thread.currentThread().getContextClassLoader().loadClass(listenerClassName);

        Object o = listenerClass.newInstance();

        if ( o instanceof UnmarshalListener || o instanceof MarshalListener )
            _listener = o;
        else
            _listener = null;

        _listenerGoldFile = listener.getGoldFile();
        _listenerType = listener.getType();
     }

   /**
    * Returns an instance of the object model hardcoded in the given
    * ObjectModelBuilder.
    *
    * @return an instance of the object model hardcoded in the given
    * ObjectModelBuilder.
    */
    protected Object buildObjectModel(String builderName)
        throws java.lang.Exception
    {
        Class builderClass = null;
        if (_test.getClassLoader() != null)
            builderClass = _test.getClassLoader().loadClass(builderName);
        else
            builderClass = this.getClass().getClassLoader().loadClass(builderName);
        ObjectModelBuilder builder = (ObjectModelBuilder)builderClass.newInstance();
        return builder.buildInstance();
    }

    /**
     * print the message if in verbose mode.
     */
    protected void verbose(String message) {
        if (_verbose)
            System.out.println(message);
    }
}
