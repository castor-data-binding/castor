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
 * Copyright 2002-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id: SchemaTestCase.java 6787 2007-01-29 06:00:49Z ekuns $
 *
 * Date         Author              Changes
 * -----------------------------------------
 * 01/23/2002   Keith Visco         Created
 * 03/25/2002   Arnaud Blandin      Ported to CTF
 * 10/15/2003   Arnaud Blandin      Improved reporting
 */
package org.castor.xmlctf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.xmlctf.util.CTFUtils;
import org.castor.xmlctf.util.FileServices;
import org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType;
import org.exolab.castor.xml.XMLContext;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaContextImpl;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.exolab.castor.xml.schema.writer.SchemaWriter;
import org.xml.sax.InputSource;

/**
 * A JUnit test case for testing the Castor Schema Object Model.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision: 6787 $ $Date: 2006-04-26 15:14:53 -0600 (Wed, 26 Apr 2006) $
 */
public class SchemaTestCase extends XMLTestCase {

    /** The name of the schema to test. */
    private String         _schemaName;
    /** The count of differences expected for the file-reference comparison. */
    private final int      _differenceCountReference;
    /** Gold file to compare a schema against.  Optional.  Only needed if
     * schema changes during parsing, i.e., due to redefine element. */
    protected final String _goldFileName;

    /**
     * Default constructor
     * @param name the name of the test
     */
    public SchemaTestCase(final String name) {
        super(name);
        throw new IllegalArgumentException("You cannot use the name-only constructor");
    } //-- SchemaTest

    /**
     * Creates a new SchemaTest with the given name
     * @param castorTc the reference to the jar/directory
     * @param tc the UnitTestCase that wraps the configuration for this XML Test case.
     */
    public SchemaTestCase(final CastorTestCase castorTc, final UnitTestCase tc) {
        super(castorTc, tc);
        _differenceCountReference = getSchemaDifferenceCount(tc, FailureStepType.COMPARE_TO_REFERENCE);
        _goldFileName             = tc.getGoldFile();
    } //-- SchemaTest

    /**
     * Looks for and returns the difference count for the given step
     * @param tc the UnitTestCase that wraps the configuration for this XML Test case.
     * @param step the step to look for
     * @return the difference count for the given step
     */
    private int getSchemaDifferenceCount(final UnitTestCase tc, final FailureStepType step) {
        SchemaDifferences[] diff = tc.getSchemaDifferences();
        for (int i = 0; i < diff.length; i++) {
            if (diff[i].getFailureStep().equals(step)) {
                return diff[i].getContent();
            }
        }
        return 0;
    }

    /**
     * Sets the name of the XML schema file to test.
     *
     * @param name the name of the XML schema file to test.
     */
    public void setSchemaName(final String name) {
        _schemaName = name;
    }

    public static Test suite() {
        return new TestSuite();
    } //-- suite

    protected void setUp() throws Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': setting up test '" + _name+"'");
        verbose("================================================\n");

        try {
            FileServices.copySupportFiles(_test.getTestFile(), _outputRootFile);
        } catch (IOException e) {
            fail("IOException copying support files " + e);
        }
        if (getXMLContext() == null) {
         // not wrapped inside a TestWithXy test!
            setXMLContext(new XMLContext());
        }
    }

    /**
     * Cleans up after this unit test (nothing to do except provide output).
     * @throws java.lang.Exception never
     */
    protected void tearDown() throws Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': test '" + _name+"' complete.");
        verbose("================================================\n");
    }

    /**
     * Override this method to run the test and assert its state.
     *
     * @throws Throwable if any exception is thrown
     */
    public void runTest() throws Throwable {
        if (_skip) {
            verbose("-->Skipping the test");
            return;
        }

        File schemaFile = new File(_test.getTestFile() + "/" + _schemaName);
        String schemaURL = schemaFile.toURL().toString();

        Schema schema = testReadingSchema(schemaURL);
        if (schema == null) {
            return;
        }
        testWritingSchema(schemaURL, schema);

        // Compare marshaled schema to gold file if provided, otherwise to input file
        compareSchemaFiles(schemaFile);

        if (_failure != null && _failure.getContent()) {
            fail("The schema test was expected to fail, but passed");
        }
    } //-- runTest

    private void compareSchemaFiles(final File schemaFile) throws IOException {
        File file = new File(_outputRootFile, _schemaName.substring(0,_schemaName.lastIndexOf('.'))
                              + "-output" + FileServices.XSD);

        String goldFileName = (_goldFileName != null) ? _outputRootFile + "/" +  _goldFileName
                                                      : schemaFile.getAbsolutePath();

        int result = CTFUtils.compare(goldFileName, file.getAbsolutePath());
        verbose("----> Compare marshaled schema to gold file '" + _goldFileName + "': " + ((result == 0)?"OK":"### Failed ### "));

        final FailureStepType step = _failure != null ? _failure.getFailureStep() : null;
        final boolean expectedToFail= _failure != null && _failure.getContent()
                                      && (step == null || step.equals(FailureStepType.COMPARE_TO_REFERENCE));

        if (_failure == null || !_failure.getContent()) {
            assertEquals("The Marshaled schema differs from the gold file", _differenceCountReference, result);
        } else if (expectedToFail) {
            assertTrue("The Marshaled schema was expected to differ from the" +
                       " gold file, but did not", result != _differenceCountReference);
        }
    }

    /**
     * Reads and returns the provided XML schema.
     *
     * @param url the schema URL
     * @return the Schema that was read in
     */
    private Schema testReadingSchema(final String url) {
        verbose("--> Reading XML Schema: " + url);
        try {
            SchemaReader reader = new SchemaReader();
            
            reader.setSchemaContext(new SchemaContextImpl());
            reader.setInputSource(new InputSource(url));
            
            Schema returnValue  = reader.read();
            if (_failure != null && _failure.getContent() && _failure.getFailureStep() != null &&
                 _failure.getFailureStep().equals(FailureStepType.PARSE_SCHEMA)) {
                fail("Reading/Parsing the schema was expected to fail, but succeeded");
            }
            return returnValue;
        } catch (Exception e) {
            if (!checkExceptionWasExpected(e, FailureStepType.PARSE_SCHEMA)) {
                fail("Unable to read Schema '" + url + "': " + e.toString());
            }
        }

        return null;
    }

    /**
     * Writes the provided schema to disk.
     *
     * @param url schema url, used only in diagnostic output
     * @param schema the schema to write
     */
    private void testWritingSchema(final String url, final Schema schema) {
        // First write the schema to disk
        try {
            String fileName = _schemaName.substring(0,_schemaName.lastIndexOf('.'))
                              + "-output" + FileServices.XSD;
            verbose("--> Writing XML Schema: " + fileName);

            File         output = new File(_outputRootFile, fileName);
            FileWriter   writer = new FileWriter(output);
            
            SchemaWriter schemaWriter = new SchemaWriter();
            schemaWriter.setSchemaContext(new SchemaContextImpl());
            schemaWriter.setDocumentHandler(new PrintWriter(writer, true));
            
            schemaWriter.write(schema);
            writer.close();
        } catch (Exception e) {
            if (!checkExceptionWasExpected(e, FailureStepType.WRITE_SCHEMA)) {
                fail("Failed to write Schema '" + url + "' to disk: " + e.toString());
            }
            return;
        }

        if (_failure != null && _failure.getContent() && _failure.getFailureStep() != null &&
            _failure.getFailureStep().equals(FailureStepType.WRITE_SCHEMA)) {
            fail("Writing the schema was expected to fail, but succeeded");
        }
    }

} //-- SchemaTest
