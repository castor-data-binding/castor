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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 *
 * Date         Author              Changes
 * -----------------------------------------
 * 01/23/2002   Keith Visco         Created
 * 03/25/2002   Arnaud Blandin      Ported to CTF
 */

package org.exolab.castor.tests.framework;

//-- CTF imports
import org.exolab.castor.tests.framework.testDescriptor.SchemaTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.exolab.castor.tests.framework.testDescriptor.TestDescriptor;

//-- JUnit imports
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

//-- Castor imports (to test SOM)
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.exolab.castor.xml.schema.writer.SchemaWriter;
import org.exolab.castor.util.NestedIOException;

//-- Adaptx imports (for using XMLDiff)
import org.exolab.adaptx.xslt.dom.XPNReader;
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xml.XMLDiff;

//-- Java imports
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;


/**
 * A JUnit test case for testing the Castor Schema Object Model.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
**/
public class SchemaTestCase extends XMLTestCase {

    /**
     * The name of the schema to test
     */
     private String _name;

     private static final String XSD = ".xsd";

    /**
     * Default constructor
     * @param name the name of the test
    **/
    public SchemaTestCase(String name) {
        super(name);
    } //-- SchemaTest

    /**
     * Creates a new SchemaTest with the given name
     *
     * @param name the name of the test.
    **/
	public SchemaTestCase( CastorTestCase castorTc, UnitTestCase tc, File outputFile) {
		super(castorTc, tc, outputFile);
        //getUnitTestCaseChoice should not be null at this point
        _name = tc.getName();
	} //-- SchemaTest

    /**
	 * Override to run the test and assert its state.
	 * @exception Throwable if any exception is thrown
    **/
    protected void runTest() throws Throwable {
        verbose("\n========================================");
        verbose("Setting up test for '" + _name + "' from '" + _test.name() + "'");
        verbose("\n========================================");

        if (_skip) {
            verbose("-->Skipping the test");
            return;
        }
        String schemaURL = new File (_test.getTestFile() + "/" + _name).toURL().toString();
        XPathNode node1 = null;
        try {
            node1 = CTFUtils.loadXPN(schemaURL);
	    } catch(java.io.IOException iox) {
            fail(iox.toString());
            if (_printStack)
                iox.printStackTrace(System.out);
        }
        XPathNode node2 = readAndWriteSchema(schemaURL);
        //node2 can be null if the read/write fails
        if (node2 != null) {
	        XMLDiff diff = new XMLDiff();
	        int result = diff.compare(node1, schemaURL, node2, "In-Memory-Result");
	        if ((_failure != null) && (_failure.getContent() == true))
                assert(result != 0);
            else {
                 assert(result == 0);
                 assert("-->The test case should have failed.",((_failure == null) || (_failure.getContent() == false)));
            }
        }
    } //-- runTest

    /**
     * Reads the XML Schema located at the given URL into the Castor SOM.
     * The Schema is then written from the SOM into an XPathNode.
     *
     * @param url the URL of the Schema to read.
     * @return the XPathNode representation of the XML Schema
    **/
    private XPathNode readAndWriteSchema(String url) {

        //-- read input schema
	    Schema schema = null;
	    try {
           verbose("--> Reading XML Schema: " + url);
            SchemaReader reader = new SchemaReader((String)url);
            schema = reader.read();
        } catch (java.io.IOException iox) {
            //Handling the failure feature
            if (_failure != null) {
                Class exception = iox.getClass();
                if (iox instanceof NestedIOException)
                    exception = ((NestedIOException)iox).getException().getClass();
                String exceptionName = _failure.getException();
                if (exceptionName != null) {
                    try {
                        Class expected = Class.forName(exceptionName);
                        if (expected.isAssignableFrom(exception) ) {
                            assert(_failure.getContent() == true);
                            return null;
                        }
                        else fail("Received: '"+exception+"' but expected:'"+exceptionName+"'.");
                    } catch (ClassNotFoundException ex) {
                        //Class#forName
                        fail("The exception specified:"+exceptionName+" cannot be found in the CLASSPATH");
                    }
                }
                else {
                    assert(_failure.getContent() == true);
                    return null;
                }
            }
            if (_printStack)
                iox.printStackTrace();
            fail("Unable to read Schema: " + url + ";  " + iox.toString());
        }

	    //-- create XPNBuilder
	    XPNBuilder builder = new XPNBuilder();
	    builder.setSaveLocation(true);

	    //-- write schema to XPNBuilder
	    try {
	        if (_verbose) {
                String fileName = _name.substring(0,_name.lastIndexOf('.'))+"-output"+XSD;
                verbose("--> Writing XML Schema: " + fileName);
                File output = new File(_outputRootFile, fileName);
                FileWriter writer = new FileWriter(output);
                output = null;
                PrintWriter pw = new PrintWriter(writer, true);
	            SchemaWriter sw = new SchemaWriter(pw);
	            sw.write(schema);
            }
	        SchemaWriter sw = new SchemaWriter(builder);
	        sw.write(schema);
        } catch (java.io.IOException iox) {
            fail("Unable to write Schema: " + url + ";  " +
                  iox.toString());
            if (_printStack)
                iox.printStackTrace(System.out);
        } catch(org.xml.sax.SAXException sx) {
	         //the test was intended to fail
             if (_failure != null) {
                //1--the exception is specified
                String exceptionName = _failure.getException();
                if (exceptionName != null) {
                   try {
                       Class expected = Class.forName(exceptionName);
                       if (expected.isAssignableFrom(sx.getClass())) {
                           assert(_failure.getContent() == true);
                           return null;
                       }
                       else fail("Received:'"+sx+"' but expected:'"+exceptionName+"'.");
                   } catch (ClassNotFoundException ex) {
                        //Class#forName
                        fail("The exception specified:"+exceptionName+" cannot be found in the CLASSPATH");

                    }
                }
                //2--No exception specified --> the test is a success.
                else {
                    assert(_failure.getContent() == true);
                    return null;
                }
             }
             fail("Unable to write schema: " + url + "; "
	               + sx.toString());
            if (_printStack)
                sx.printStackTrace(System.out);
        }

	    return builder.getRoot();
    } //-- readAndWriteSchema

   /**
     * Clean up the tests.
     */
    protected void tearDown()
        throws java.lang.Exception {

        verbose("Test for '" + _name + "' complete");
        verbose("========================================");

    }

    public static Test suite() {
        TestSuite suite= new TestSuite();
	    return suite;
	} //-- suite

} //-- SchemaTest