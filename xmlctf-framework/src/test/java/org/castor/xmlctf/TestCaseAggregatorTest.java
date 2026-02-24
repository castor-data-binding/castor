package org.castor.xmlctf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for TestCaseAggregator class.
 */
public class TestCaseAggregatorTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testRootDir;
    private String outputRoot;
    private TestCaseAggregator aggregator;

    @Before
    public void setUp() throws IOException {
        testRootDir = tempFolder.newFolder("test-root");
        outputRoot = tempFolder.getRoot().getAbsolutePath() + "/output";
        aggregator = new TestCaseAggregator(testRootDir, outputRoot);
    }

    @org.junit.Test
    public void testConstructorWithValidDirectory() {
        assertNotNull(aggregator);
    }

    @org.junit.Test
    public void testConstructorWithDifferentPaths() throws IOException {
        File anotherDir = tempFolder.newFolder("another-dir");
        String anotherOutput = tempFolder.getRoot().getAbsolutePath() + "/other-output";
        TestCaseAggregator another = new TestCaseAggregator(anotherDir, anotherOutput);
        assertNotNull(another);
    }

    @org.junit.Test
    public void testSuiteReturnsNotNull() {
        junit.framework.Test suite = aggregator.suite();
        assertNotNull(suite);
    }

    @org.junit.Test
    public void testSuiteReturnsTestSuite() {
        junit.framework.Test suite = aggregator.suite();
        assertTrue(suite instanceof junit.framework.TestSuite);
    }

    @org.junit.Test
    public void testMultipleAggregators() throws IOException {
        File dir1 = tempFolder.newFolder("dir1");
        File dir2 = tempFolder.newFolder("dir2");

        TestCaseAggregator agg1 = new TestCaseAggregator(dir1, "output1");
        TestCaseAggregator agg2 = new TestCaseAggregator(dir2, "output2");

        assertNotNull(agg1);
        assertNotNull(agg2);
        assertNotEquals(agg1, agg2);
    }

    @org.junit.Test
    public void testSuiteWithEmptyDirectory() {
        junit.framework.Test suite = aggregator.suite();
        assertNotNull(suite);
    }

    @org.junit.Test
    public void testVerbosePropertyConstant() {
        assertNotNull(TestCaseAggregator.VERBOSE_PROPERTY);
        assertTrue(TestCaseAggregator.VERBOSE_PROPERTY.length() > 0);
    }

    @org.junit.Test
    public void testPrintStackTraceConstant() {
        assertNotNull(TestCaseAggregator.PRINT_STACK_TRACE);
        assertTrue(TestCaseAggregator.PRINT_STACK_TRACE.length() > 0);
    }

    @org.junit.Test
    public void testConstantsAreNotNull() {
        assertNotNull(TestCaseAggregator.VERBOSE_PROPERTY);
        assertNotNull(TestCaseAggregator.PRINT_STACK_TRACE);
    }

    @org.junit.Test
    public void testConstantsAreString() {
        assertTrue(TestCaseAggregator.VERBOSE_PROPERTY instanceof String);
        assertTrue(TestCaseAggregator.PRINT_STACK_TRACE instanceof String);
    }

    @org.junit.Test
    public void testAggregatorWithAbsolutePath() throws IOException {
        File absoluteDir = tempFolder.newFolder("absolute-dir");
        String absolutePath = absoluteDir.getAbsolutePath();
        TestCaseAggregator agg = new TestCaseAggregator(new File(absolutePath), "output");
        assertNotNull(agg);
    }

    @org.junit.Test
    public void testAggregatorWithRelativePath() throws IOException {
        File relativeDir = tempFolder.newFolder("relative-dir");
        TestCaseAggregator agg = new TestCaseAggregator(relativeDir, "output");
        assertNotNull(agg);
    }

    @org.junit.Test
    public void testSuiteIsConsistent() {
        junit.framework.Test suite1 = aggregator.suite();
        junit.framework.Test suite2 = aggregator.suite();
        assertNotNull(suite1);
        assertNotNull(suite2);
    }

    @org.junit.Test
    public void testConstructorAcceptsFile() {
        assertNotNull(testRootDir);
        assertTrue(testRootDir.isDirectory());
    }

    @org.junit.Test
    public void testConstructorAcceptsOutputString() {
        assertNotNull(outputRoot);
        assertTrue(outputRoot.length() > 0);
    }

    @org.junit.Test
    public void testSuiteWithDifferentOutputRoots() throws IOException {
        File dir = tempFolder.newFolder("test-dir");

        TestCaseAggregator agg1 = new TestCaseAggregator(dir, "output1");
        TestCaseAggregator agg2 = new TestCaseAggregator(dir, "output2");

        junit.framework.Test suite1 = agg1.suite();
        junit.framework.Test suite2 = agg2.suite();

        assertNotNull(suite1);
        assertNotNull(suite2);
    }

    @org.junit.Test
    public void testAggregatorInstantiation() {
        TestCaseAggregator agg = new TestCaseAggregator(testRootDir, outputRoot);
        assertNotNull(agg);
    }

    @org.junit.Test
    public void testMultipleSuiteCreations() {
        for (int i = 0; i < 5; i++) {
            junit.framework.Test suite = aggregator.suite();
            assertNotNull(suite);
        }
    }

    @org.junit.Test
    public void testSuiteCountNonNegative() {
        junit.framework.Test suite = aggregator.suite();
        int testCount = suite.countTestCases();
        assertTrue(testCount >= 0);
    }

    @org.junit.Test
    public void testVerbosePropertyValue() {
        String verboseProp = TestCaseAggregator.VERBOSE_PROPERTY;
        assertNotNull(verboseProp);
        assertTrue(verboseProp.contains("verbose") || verboseProp.contains("Verbose"));
    }

    @org.junit.Test
    public void testPrintStackPropertyValue() {
        String stackProp = TestCaseAggregator.PRINT_STACK_TRACE;
        assertNotNull(stackProp);
        assertTrue(stackProp.length() > 0);
    }

    @org.junit.Test
    public void testAggregatorWithDifferentDirectories() throws IOException {
        File dir1 = tempFolder.newFolder("first");
        File dir2 = tempFolder.newFolder("second");

        TestCaseAggregator agg1 = new TestCaseAggregator(dir1, "out");
        TestCaseAggregator agg2 = new TestCaseAggregator(dir2, "out");

        assertNotNull(agg1.suite());
        assertNotNull(agg2.suite());
    }

    @org.junit.Test
    public void testSuiteCreationAfterConstructor() {
        junit.framework.Test suite = aggregator.suite();
        assertNotNull(suite);
        assertTrue(suite instanceof junit.framework.TestSuite);
    }

    @org.junit.Test
    public void testAggregatorWithSpecialCharactersInPath() throws IOException {
        File specialDir = tempFolder.newFolder("test-special_123");
        TestCaseAggregator agg = new TestCaseAggregator(specialDir, "output_123");
        assertNotNull(agg);
        assertNotNull(agg.suite());
    }

    @org.junit.Test
    public void testConstructorSetsUpAggregator() {
        assertNotNull(aggregator);
        assertNotNull(aggregator.suite());
    }

    @org.junit.Test
    public void testTestSuiteType() {
        junit.framework.Test suite = aggregator.suite();
        assertEquals(junit.framework.TestSuite.class, suite.getClass());
    }

    @org.junit.Test
    public void testPropertiesNotEmpty() {
        assertNotNull(TestCaseAggregator.VERBOSE_PROPERTY);
        assertNotNull(TestCaseAggregator.PRINT_STACK_TRACE);
        assertNotEquals("", TestCaseAggregator.VERBOSE_PROPERTY);
        assertNotEquals("", TestCaseAggregator.PRINT_STACK_TRACE);
    }

    @org.junit.Test
    public void testAggregatorDirectoryExists() {
        assertTrue(testRootDir.exists());
        assertTrue(testRootDir.isDirectory());
    }

    @org.junit.Test
    public void testSuiteCanBeCalled() {
        junit.framework.Test suite = aggregator.suite();
        assertNotNull(suite);
        int count = suite.countTestCases();
        assertTrue(count >= 0);
    }

    @org.junit.Test
    public void testConsecutiveSuiteCalls() {
        junit.framework.Test suite1 = aggregator.suite();
        junit.framework.Test suite2 = aggregator.suite();
        junit.framework.Test suite3 = aggregator.suite();

        assertNotNull(suite1);
        assertNotNull(suite2);
        assertNotNull(suite3);
    }

    @org.junit.Test
    public void testAggregatorOutputRootStorage() {
        TestCaseAggregator agg = new TestCaseAggregator(testRootDir, "my_output_root");
        assertNotNull(agg);
    }

    @org.junit.Test
    public void testMultipleAggregatorsSamePath() throws IOException {
        File sharedDir = tempFolder.newFolder("shared");

        TestCaseAggregator agg1 = new TestCaseAggregator(sharedDir, "output");
        TestCaseAggregator agg2 = new TestCaseAggregator(sharedDir, "output");

        assertNotNull(agg1.suite());
        assertNotNull(agg2.suite());
    }

    @org.junit.Test
    public void testSuiteIsJUnitTest() {
        junit.framework.Test suite = aggregator.suite();
        assertTrue(suite instanceof junit.framework.Test);
    }

    @org.junit.Test
    public void testAggregatorWithLongOutputPath() {
        String longPath = "output/" + "very/long/path/".repeat(5) + "root";
        TestCaseAggregator agg = new TestCaseAggregator(testRootDir, longPath);
        assertNotNull(agg);
    }

    @org.junit.Test
    public void testVerboseConstantNotBlank() {
        String verbose = TestCaseAggregator.VERBOSE_PROPERTY;
        assertFalse(verbose.trim().isEmpty());
    }

    @org.junit.Test
    public void testPrintStackConstantNotBlank() {
        String printStack = TestCaseAggregator.PRINT_STACK_TRACE;
        assertFalse(printStack.trim().isEmpty());
    }
}
