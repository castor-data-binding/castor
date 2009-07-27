/*
 * Copyright 2008 Le Duc Bao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.xml.schema.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;

import org.castor.xmlctf.xmldiff.XMLDiff;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.writer.SchemaWriter;

import junit.framework.TestCase;

/**
 * This class aims to set up a test environment and to provide a skeleton for
 * testing Schema API. A typical scenarios test is
 * <li>Create Schema related to targeted test case</li>
 * <li>Generate Schema fragment by calling SchemaWriter</li>
 * <li>Load expected schema file</li>
 * <li>Compare generated schema vs expected schema</li>
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 */
public abstract class AbstractSchemaTest extends TestCase {
	
	/** Test result */
	public enum TestResult { IDENTICAL, DIFFERENCE };

    /**
     * Path of the expected result pattern files.
     */
    // private static final String EXPECTED_PATH = "..";
    /**
     * Handle targeted schema
     */
    protected Schema _schema = null;

    /**
     * Constructor for BaseGeneratorTest
     * 
     * @param testcase
     *            test case
     */
    public AbstractSchemaTest(final String testcase) {
        super(testcase);
    }

    /**
     * create a new schema instance
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        _schema = new Schema();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        _schema = null;
    }

    /**
     * This function aims to generate schema fragment, load expected schema and
     * compare them.
     * 
     * @param expectedFilename
     *            expected schema filename.
     * @return 0, if no differences are found, otherwise a positive number
     *         indicating the number of differences.
     * @see org.castor.xmlctf.xmldiff.XMLDiff#compare()
     * @throws Exception
     *             if any
     */
    protected final TestResult doTest(String expected) throws Exception {
        // To reuse the existent source code from XMLCTF Framework, all schemas
        // will be represented as a XML content. They are inputed to
        // org.castor.xmlctf.xmldiff.XMLDiff to get the final result.

        // 1. generate schema and product a xml content, write it in a temporary file
        File targetedOutput = File.createTempFile("doTest", "xmlctf");
        Writer writer = new BufferedWriter(new FileWriter(targetedOutput));
        SchemaWriter swriter = new SchemaWriter(writer);
        swriter.write(_schema);

        // 2. load expected schema
        URL expectedUrl = this.getClass().getResource(expected);
        File expectedSchemaFile = new File(expectedUrl.toURI());

        // 3. compare using org.castor.xmlctf.xmldiff.XMLDiff
        XMLDiff diff = new XMLDiff(targetedOutput.getAbsolutePath(), expectedSchemaFile.getAbsolutePath());
        int result = diff.compare();
        TestResult testResult = result == 0 ? TestResult.IDENTICAL : TestResult.DIFFERENCE;

        // 4. delete temporary file
        targetedOutput.delete();

        return testResult;
    }
}
