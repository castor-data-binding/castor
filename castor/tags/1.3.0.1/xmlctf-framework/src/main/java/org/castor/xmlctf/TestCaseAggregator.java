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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: TestCaseAggregator.java 6787 2007-01-29 06:00:49Z ekuns $
 */
package org.castor.xmlctf;

import java.io.File;

import org.castor.xmlctf.util.FileServices;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is used to inspect recursively a hierarchy of directories that
 * contain CTF tests (jars or directories). A JUnit TestSuite is created for
 * each directory.
 *
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision: 6787 $ $Date: 2006-04-26 15:14:53 -0600 (Wed, 26 Apr 2006) $
 */
public class TestCaseAggregator extends TestCase {

    /** File separator for this system. */
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    /** Name of the system property to set up the verbose mode. */
    public static final String VERBOSE_PROPERTY = "org.exolab.castor.tests.Verbose";
    /** Name of the system property to set up the printStackTrace mode. */
    public static final String PRINT_STACK_TRACE = "org.exolab.castor.tests.printStack";
    /** True if we desire a lot of info on what happen. */
    private static boolean _verbose;

    static {
        String v = System.getProperty(VERBOSE_PROPERTY);
        _verbose = (v != null && v.equals("true"));
    }

    /** The directory that contains CTF test cases. */
    private final File _directory;
    /** Location of the temporary files when the tests are run. */
    private final String _testOutputRoot;
    /** String containing the directory path from the test root to here. */
    private final String _directoryToHere;

    /**
     * Creates a new TestCaseAggregator with the given name.
     *
     * @param name the name of this TestCaseAggregator
     */
    public TestCaseAggregator(final String name) {
        super(name);
        _directory       = null;
        _directoryToHere = null;
        _testOutputRoot  = null;
    }

    /**
     * Create a new TestCaseAggregator which will inspect the directory given in
     * parameter.
     *
     * @param directory the directory to inspect for test case and subdirectory
     * @param testOutputRoot the path to the directory where the test in this
     *            directory can put there temporary files.
     */
    public TestCaseAggregator(final File directory, final String testOutputRoot) {
        super(directory.getName());
        _directory       = directory;
        _directoryToHere = "";
        _testOutputRoot  = testOutputRoot;
    }

    /**
     * Create a new TestCaseAggregator which will inspect the directory given in
     * parameter.
     *
     * @param directory the directory to inspect for test case and subdirectory
     * @param directoryToHere the directory path above "directory"
     * @param testOutputRoot the path to the directory where the test in this
     *            directory can put there temporary files.
     */
    protected TestCaseAggregator(final File directory, final String directoryToHere,
                                 final String testOutputRoot) {
        super(directory.getName());
        _directory       = directory;
        _directoryToHere = directoryToHere + "/";
        _testOutputRoot  = testOutputRoot;
    }

    /**
     * Assembles and returns a test suite containing all known tests.
     *
     * @return A non-null test suite.
     */
    public Test suite() {
        final TestSuite suite = new TestSuite();
        final String suiteName = (_directoryToHere.length() == 0) ? _directory.getName()
                                                                  : _directoryToHere;
        suite.setName(suiteName);

        if (!_directory.isDirectory()) {
            // Maybe it is a jar file, it happens if we run the
            // CastorTestSuiteRunner with just one jar in param
            if (_directory.getName().endsWith(".jar")) {
                CastorTestCase tc = new CastorTestCase(_directory, _directoryToHere, _testOutputRoot);
                return tc.suite();
            }
            // If not a jar file, just return the empty TestSuite
            return suite;
        }

        String outputRoot = _testOutputRoot;
        if (outputRoot.endsWith("/") || outputRoot.endsWith(FILE_SEPARATOR)) {
            outputRoot = outputRoot + _directory.getName();
        } else {
            outputRoot = outputRoot + FILE_SEPARATOR + _directory.getName();
        }

        verbose("\n==================================================================");
        verbose("Processing directory:\n" + _directory.getAbsolutePath());
        verbose("==================================================================\n");

        final File[] list = _directory.listFiles();
        for (int i = 0; i < list.length; ++i) {
            processOneFileOrDirectory(suite, outputRoot, list[i]);
        }

        return suite;
    }

    /**
     * Processes a single file or directory, recurses into directories and for
     * files, if a JAR file or a test descriptor XML file, create a new test
     * case and add it to our test suite.
     *
     * @param suite the test suite to add new tests to
     * @param outputRoot output directory for temporary files in our tests
     * @param file the file or directory to process
     */
    private void processOneFileOrDirectory(final TestSuite suite, final String outputRoot,
                                           final File file) {
        String name = file.getName();

        // If a directory (and not a source control directory), recurse
        if (file.isDirectory()) {
            // Directories that contain TestDescriptor.xml do not get recursed any deeper
            if (!FileServices.isScmDirectory(name) && !((new File(file.getParentFile(), "TestDescriptor.xml")).exists())) {
                //look for jars or testDescriptor files inside the directory
                Test test = new TestCaseAggregator(file, _directoryToHere + file.getName(), outputRoot).suite();
                if (test != null) {
                    suite.addTest(test);
                }
            }
            return;
        }

        // Otherwise make a test case if a JAR file or test descriptor

        Test test = null;
        if (name.endsWith(".jar")) {
            test = (new CastorTestCase(file, _directoryToHere, outputRoot)).suite();
        } else if (name.endsWith(CastorTestCase.TEST_DESCRIPTOR)) {
            test = (new CastorTestCase(_directory, _directoryToHere, outputRoot)).suite();
        }

        if (test != null) {
            suite.addTest(test);
        }
    }

    /**
     * Prints the provided message if verbose is true.
     * @param message the message to print
     */
    private void verbose(final String message) {
        if (_verbose) {
            System.out.println(message);
        }
    }

}
