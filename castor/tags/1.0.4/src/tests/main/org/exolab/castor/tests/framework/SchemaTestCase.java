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
 * Copyright 2002-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 * 
 * $Id$
 *
 * Date         Author              Changes
 * -----------------------------------------
 * 01/23/2002   Keith Visco         Created
 * 03/25/2002   Arnaud Blandin      Ported to CTF
 * 10/15/2003   Arnaud Blandin      Improved reporting
 */
package org.exolab.castor.tests.framework;

import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
//-- JUnit imports
import junit.framework.Test;
import junit.framework.TestSuite;

//-- Castor imports (to test SOM)
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.exolab.castor.xml.schema.writer.SchemaWriter;

//-- Adaptx imports (for using XMLDiff)
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xml.XMLDiff;

//-- Java imports
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * A JUnit test case for testing the Castor Schema Object Model.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-26 15:14:53 -0600 (Wed, 26 Apr 2006) $
**/
public class SchemaTestCase extends XMLTestCase {

    private static final String XSD = ".xsd";
    
    /**
     * The name of the schema to test
     */
     private String _schemaName;

    /**
     * Default constructor
     * @param name the name of the test
     */
    public SchemaTestCase(String name) {
        super(name);
    } //-- SchemaTest

    /**
     * Creates a new SchemaTest with the given name
     * @param castorTc the reference to the jar/directory
     * @param tc the UnitTestCase that wraps the configuration for this XML Test case.
     * @param outputFile the directory that contains the files needed for the test
     */
    public SchemaTestCase(CastorTestCase castorTc, UnitTestCase tc, File outputFile) {
        super(castorTc, tc, outputFile);
    } //-- SchemaTest

    /**
     * Sets the name of the XML schema file to test.
     *
     * @param name the name of the XML schema file to test.
     */
    public void setSchemaName(String name) {
        _schemaName = name;
    }

    /**
     * Override this method to run the test and assert its state.
     *
     * @throws Throwable if any exception is thrown
     */
    protected void runTest() throws Throwable {
       verbose("\n================================================");
       verbose("Test suite '"+_test.getName()+"': setting up test '" + _name+"'");
       verbose("================================================\n");

        if (_skip) {
            verbose("-->Skipping the test");
            return;
        }

        String schemaURL = new File (_test.getTestFile() + "/" + _schemaName).toURL().toString();
        XPathNode node1 = null;
        try {
            node1 = CTFUtils.loadXPN(schemaURL);
        } catch(java.io.IOException iox) {
            fail(iox.toString());
            if (_printStack) {
                iox.printStackTrace(System.out);
            }
        }

        //node2 will be null if the read/write fails
        XPathNode node2 = readAndWriteSchema(schemaURL);
        if (node2 != null) {
            XMLDiff diff = new XMLDiff();
            int result = diff.compare(node1, schemaURL, node2, "In-Memory-Result");
            if (_failure != null && _failure.getContent() == true) {
                assertTrue(result != 0);
            } else {
                assertEquals(result, 0);
                assertTrue("-->The test case should have failed.", _failure == null || _failure.getContent() == false);
            }
        }
    } //-- runTest

    /**
     * Reads the XML Schema located at the given URL into the Castor SOM. The
     * Schema is then written from the SOM into an XPathNode.
     *
     * @param url the URL of the Schema to read.
     * @return the XPathNode representation of the XML Schema
     */
    private XPathNode readAndWriteSchema(String url) {
        Schema schema = testReadingSchema(url);
        if (schema == null) {
            return null;
        }
        XPNBuilder builder = testWritingSchema(url, schema);
        return (builder != null) ? builder.getRoot() : null;
    } //-- readAndWriteSchema

    /**
     * Reads and returns the provided XML schema.
     *
     * @param url the schema URL
     * @return the Schema that was read in
     */
    private Schema testReadingSchema(String url) {
        try {
            verbose("--> Reading XML Schema: " + url);
            SchemaReader reader = new SchemaReader(url);
            return reader.read();
        } catch (java.io.IOException iox) {
            if (_failure != null && checkExceptionWasExpected(iox)) {
                assertTrue(_failure.getContent());
                return null;
            }
            if (_printStack) {
                iox.printStackTrace();
            }
            fail("Unable to read Schema: " + url + ";  " + iox.toString());
        }

        return null;
    }

    /**
     * Creates a new XPNBuilder, uses it to make a new SchemaWriter, and writes
     * the provided schema to that SchemaWriter.
     *
     * @param url schema url, used only in diagnostic output
     * @param schema the schema to write
     * @return a new XPNBuilder
     */
    private XPNBuilder testWritingSchema(String url, Schema schema) {
        //-- create XPNBuilder
        XPNBuilder builder = new XPNBuilder();
        builder.setSaveLocation(true);

        //-- write schema to XPNBuilder
        try {
            if (_verbose) {
                String fileName = _schemaName.substring(0,_schemaName.lastIndexOf('.'))+"-output"+XSD;
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
            fail("Unable to write Schema: " + url + ";  " + iox.toString());
            if (_printStack) {
                iox.printStackTrace(System.out);
            }
        } catch(org.xml.sax.SAXException sx) {
            if (_failure != null && checkExceptionWasExpected(sx)) {
                assertTrue(_failure.getContent());
                return null;
            }
            fail("Unable to write schema: " + url + "; " + sx.toString());
            if (_printStack) {
                sx.printStackTrace(System.out);
            }
        }
        return builder;
    }

    /**
     * Cleans up after this unit test (nothing to do except provide output).
     * @throws java.lang.Exception never
     */
    protected void tearDown() throws java.lang.Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': test '" + _name+"' complete.");
        verbose("================================================\n");
    }

    public static Test suite() {
        return new TestSuite();
    } //-- suite

} //-- SchemaTest
