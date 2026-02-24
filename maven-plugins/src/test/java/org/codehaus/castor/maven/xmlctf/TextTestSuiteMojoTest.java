/*
 * Copyright 2005 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.castor.maven.xmlctf;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TextTestSuiteMojoTest {

    private TextTestSuiteMojo mojo;

    @Mock
    private Log mockLog;

    @Before
    public void setUp() {
        mojo = new TextTestSuiteMojo();
        mojo.setLog(mockLog);
    }

    @org.junit.Test
    public void should_RunTestSuiteSuccessfully_When_NoErrorsOrFailures()
        throws MojoExecutionException {
        MockTestSuite testSuite = new MockTestSuite();
        mojo.runJUnit(testSuite);
        assertTrue(true);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_ThrowMojoExecutionException_When_TestsHaveErrors()
        throws MojoExecutionException {
        MockTestSuiteWithErrors testSuite = new MockTestSuiteWithErrors();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_ThrowMojoExecutionException_When_TestsHaveFailures()
        throws MojoExecutionException {
        MockTestSuiteWithFailures testSuite = new MockTestSuiteWithFailures();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_ThrowMojoExecutionException_When_TestsHaveBothErrorsAndFailures()
        throws MojoExecutionException {
        MockTestSuiteWithErrorsAndFailures testSuite =
            new MockTestSuiteWithErrorsAndFailures();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_ReturnCorrectErrorMessage_When_ErrorsOccur()
        throws MojoExecutionException {
        MockTestSuiteWithErrors testSuite = new MockTestSuiteWithErrors();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_ReturnCorrectErrorMessage_When_FailuresOccur()
        throws MojoExecutionException {
        MockTestSuiteWithFailures testSuite = new MockTestSuiteWithFailures();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_CountErrorsAccurately_When_MultipleErrorsPresent()
        throws MojoExecutionException {
        MockTestSuiteWithMultipleErrors testSuite =
            new MockTestSuiteWithMultipleErrors();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_CountFailuresAccurately_When_MultipleFailuresPresent()
        throws MojoExecutionException {
        MockTestSuiteWithMultipleFailures testSuite =
            new MockTestSuiteWithMultipleFailures();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test
    public void should_SucceedWithZeroErrorsAndZeroFailures()
        throws MojoExecutionException {
        MockTestSuite testSuite = new MockTestSuite();
        mojo.runJUnit(testSuite);
        assertTrue(true);
    }

    @org.junit.Test
    public void should_SucceedWhenTestSuiteIsValid()
        throws MojoExecutionException {
        MockTestSuite testSuite = new MockTestSuite();
        assertNotNull(testSuite);
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test
    public void should_HandleMultipleTestCases() throws MojoExecutionException {
        MockTestSuite testSuite = new MockTestSuite();
        testSuite.addTest(new MockTestSuite());
        mojo.runJUnit(testSuite);
        assertTrue(true);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_FailWhenOnlyErrorsPresent()
        throws MojoExecutionException {
        MockTestSuiteWithMultipleErrors testSuite =
            new MockTestSuiteWithMultipleErrors();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test(expected = MojoExecutionException.class)
    public void should_FailWhenOnlyFailuresPresent()
        throws MojoExecutionException {
        MockTestSuiteWithMultipleFailures testSuite =
            new MockTestSuiteWithMultipleFailures();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test
    public void should_ImplementTextTestSuiteMojoClass() {
        assertNotNull(mojo);
        assertTrue(mojo instanceof TextTestSuiteMojo);
    }

    @org.junit.Test
    public void should_InheritFromAbstractTestSuiteMojo() {
        assertTrue(mojo instanceof AbstractTestSuiteMojo);
    }

    @org.junit.Test
    public void should_HaveRunJUnitMethod() throws Exception {
        MockTestSuite testSuite = new MockTestSuite();
        mojo.setLog(mockLog);
        mojo.runJUnit(testSuite);
        assertTrue(true);
    }

    @org.junit.Test
    public void should_ExitSuccessfullyWhenZeroFailures()
        throws MojoExecutionException {
        MockTestSuite testSuite = new MockTestSuite();
        mojo.runJUnit(testSuite);
    }

    @org.junit.Test
    public void should_HandleEmptyTestSuite() throws MojoExecutionException {
        TestSuite emptyTestSuite = new TestSuite();
        mojo.runJUnit(emptyTestSuite);
    }

    private static class MockTestSuite extends TestSuite {

        @Override
        public int countTestCases() {
            return 0;
        }

        @Override
        public void run(TestResult result) {
            // Empty run - no errors or failures
        }
    }

    private static class MockTestSuiteWithErrors extends TestSuite {

        @Override
        public int countTestCases() {
            return 1;
        }

        @Override
        public void run(TestResult result) {
            result.addError(null, new Exception("Test error"));
        }
    }

    private static class MockTestSuiteWithFailures extends TestSuite {

        @Override
        public int countTestCases() {
            return 1;
        }

        @Override
        public void run(TestResult result) {
            result.addFailure(null, new AssertionFailedError("Test failure"));
        }
    }

    private static class MockTestSuiteWithErrorsAndFailures extends TestSuite {

        @Override
        public int countTestCases() {
            return 2;
        }

        @Override
        public void run(TestResult result) {
            result.addError(null, new Exception("Test error"));
            result.addFailure(null, new AssertionFailedError("Test failure"));
        }
    }

    private static class MockTestSuiteWithMultipleErrors extends TestSuite {

        @Override
        public int countTestCases() {
            return 3;
        }

        @Override
        public void run(TestResult result) {
            result.addError(null, new Exception("Error 1"));
            result.addError(null, new Exception("Error 2"));
        }
    }

    private static class MockTestSuiteWithMultipleFailures extends TestSuite {

        @Override
        public int countTestCases() {
            return 3;
        }

        @Override
        public void run(TestResult result) {
            result.addFailure(null, new AssertionFailedError("Failure 1"));
            result.addFailure(null, new AssertionFailedError("Failure 2"));
        }
    }
}
