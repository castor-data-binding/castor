package org.castor.xmlctf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for CastorTestSuiteRunner class.
 */
public class CastorTestSuiteRunnerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private CastorTestSuiteRunner testRunner;

    @Before
    public void setUp() {
        testRunner = new CastorTestSuiteRunner("TestName");
    }

    @org.junit.Test
    public void testConstructorWithName() {
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner("TestName");
        assertNotNull(runner);
        assertEquals("TestName", runner.getName());
    }

    @org.junit.Test
    public void testConstructorWithEmptyName() {
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner("");
        assertNotNull(runner);
        assertEquals("", runner.getName());
    }

    @org.junit.Test
    public void testConstructorWithNullName() {
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner(null);
        assertNotNull(runner);
        assertNull(runner.getName());
    }

    @org.junit.Test
    public void testConstructorWithSpecialCharacters() {
        String specialName = "Test@#$%^&*()_+-=[]{}|;:',.<>?/";
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner(specialName);
        assertNotNull(runner);
        assertEquals(specialName, runner.getName());
    }

    @org.junit.Test
    public void testConstructorWithLongName() {
        String longName = "A".repeat(500);
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner(longName);
        assertNotNull(runner);
        assertEquals(longName, runner.getName());
    }

    @org.junit.Test
    public void testConstructorMultipleInstances() {
        CastorTestSuiteRunner runner1 = new CastorTestSuiteRunner("Test1");
        CastorTestSuiteRunner runner2 = new CastorTestSuiteRunner("Test2");
        CastorTestSuiteRunner runner3 = new CastorTestSuiteRunner("Test3");

        assertNotNull(runner1);
        assertNotNull(runner2);
        assertNotNull(runner3);

        assertEquals("Test1", runner1.getName());
        assertEquals("Test2", runner2.getName());
        assertEquals("Test3", runner3.getName());
    }

    @org.junit.Test
    public void testTestOutputRootConstantValue() {
        assertEquals(
            "../xmlctf/build/tests/output/",
            CastorTestSuiteRunner.TEST_OUTPUT_ROOT
        );
    }

    @org.junit.Test
    public void testTestOutputRootConstantNotNull() {
        assertNotNull(CastorTestSuiteRunner.TEST_OUTPUT_ROOT);
    }

    @org.junit.Test
    public void testTestOutputRootConstantContainsPath() {
        assertTrue(CastorTestSuiteRunner.TEST_OUTPUT_ROOT.contains("tests"));
        assertTrue(CastorTestSuiteRunner.TEST_OUTPUT_ROOT.contains("output"));
    }

    @org.junit.Test
    public void testInheritanceFromTestCase() {
        assertTrue(
            CastorTestSuiteRunner.class.getSuperclass().equals(
                junit.framework.TestCase.class
            )
        );
    }

    @org.junit.Test
    public void testInstanceOfTestCase() {
        assertTrue(testRunner instanceof junit.framework.TestCase);
    }

    @org.junit.Test
    public void testConstructorWithNamePreservation() {
        String originalName = "OriginalTestName";
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner(originalName);
        assertEquals(originalName, runner.getName());
    }

    @org.junit.Test
    public void testConstructorWithNumericName() {
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner("Test123456");
        assertNotNull(runner);
        assertEquals("Test123456", runner.getName());
    }

    @org.junit.Test
    public void testConstructorWithWhitespace() {
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner(
            "Test Name With Spaces"
        );
        assertNotNull(runner);
        assertEquals("Test Name With Spaces", runner.getName());
    }

    @org.junit.Test
    public void testConstructorWithNewlines() {
        String nameWithNewlines = "Test\nWith\nNewlines";
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner(
            nameWithNewlines
        );
        assertNotNull(runner);
        assertEquals(nameWithNewlines, runner.getName());
    }

    @org.junit.Test
    public void testConstructorNameNotModified() {
        String name = "UnmodifiedTestName";
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner(name);
        String retrievedName = runner.getName();
        assertEquals(name, retrievedName);
        // Verify it's the exact same string content
        assertTrue(name.equals(retrievedName));
    }

    @org.junit.Test
    public void testSuiteMethodWithValidTestRoot() throws IOException {
        File testRootDir = tempFolder.newFolder("test-root");
        System.setProperty(
            "org.exolab.castor.tests.TestRoot",
            testRootDir.getAbsolutePath()
        );

        try {
            junit.framework.Test suite = CastorTestSuiteRunner.suite();
            assertNotNull(suite);
        } finally {
            System.clearProperty("org.exolab.castor.tests.TestRoot");
        }
    }

    @org.junit.Test
    public void testSuiteMethodCreatesConcreteSuite() throws IOException {
        File testRootDir = tempFolder.newFolder("test-root-2");
        System.setProperty(
            "org.exolab.castor.tests.TestRoot",
            testRootDir.getAbsolutePath()
        );

        try {
            junit.framework.Test suite = CastorTestSuiteRunner.suite();
            assertNotNull(suite);
            assertTrue(suite instanceof junit.framework.TestSuite);
        } finally {
            System.clearProperty("org.exolab.castor.tests.TestRoot");
        }
    }

    @org.junit.Test
    public void testConstructorLoop() {
        for (int i = 0; i < 5; i++) {
            CastorTestSuiteRunner runner = new CastorTestSuiteRunner(
                "Test_" + i
            );
            assertNotNull(runner);
            assertEquals("Test_" + i, runner.getName());
        }
    }

    @org.junit.Test
    public void testMultipleConstructorsWithDifferentNames() {
        String[] names = { "Alpha", "Beta", "Gamma", "Delta", "Epsilon" };
        CastorTestSuiteRunner[] runners =
            new CastorTestSuiteRunner[names.length];

        for (int i = 0; i < names.length; i++) {
            runners[i] = new CastorTestSuiteRunner(names[i]);
            assertNotNull(runners[i]);
            assertEquals(names[i], runners[i].getName());
        }
    }

    @org.junit.Test
    public void testConstructorReturnsSameNameObject() {
        CastorTestSuiteRunner runner = new CastorTestSuiteRunner("SameName");
        String name1 = runner.getName();
        String name2 = runner.getName();

        assertEquals(name1, name2);
        assertEquals("SameName", name1);
        assertEquals("SameName", name2);
    }

    @org.junit.Test
    public void testTestOutputRootIsString() {
        Object constant = CastorTestSuiteRunner.TEST_OUTPUT_ROOT;
        assertTrue(constant instanceof String);
    }

    @org.junit.Test
    public void testTestOutputRootEndsWithSlash() {
        assertTrue(CastorTestSuiteRunner.TEST_OUTPUT_ROOT.endsWith("/"));
    }
}
