package org.castor.xmlctf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for CastorTestCase class.
 */
public class CastorTestCaseTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testDirectory;

    @Before
    public void setUp() throws IOException {
        testDirectory = tempFolder.newFolder("castor-test-case");
    }

    @org.junit.Test
    public void testConstructorWithNameOnly() {
        CastorTestCase testCase = new CastorTestCase("TestName");
        assertNotNull(testCase);
        assertEquals("TestName", testCase.getName());
        assertNull(testCase.getTestFile());
        assertNull(testCase.getOutputRootFile());
        assertEquals("", testCase.getDirectoryToHere());
        assertEquals(CastorTestCase.UNKNOWN, testCase.getType());
    }

    @org.junit.Test
    public void testConstructorWithNameAndDirectory() throws IOException {
        File testDir = tempFolder.newFolder("test-input");
        String directoryPath = "some/path";
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            directoryPath,
            outputRoot
        );

        assertNotNull(testCase);
        assertNotNull(testCase.getTestFile());
        assertEquals(testDir, testCase.getTestFile());
        assertEquals(CastorTestCase.DIRECTORY, testCase.getType());
        assertEquals(directoryPath, testCase.getDirectoryToHere());
        assertNotNull(testCase.getOutputRootFile());
    }

    @org.junit.Test
    public void testConstructorWithJarFile() throws IOException {
        File jarFile = new File(tempFolder.getRoot(), "test.jar");
        jarFile.createNewFile();

        try {
            String directoryPath = "test/path";
            String outputRoot = tempFolder.getRoot().getAbsolutePath();

            CastorTestCase testCase = new CastorTestCase(
                jarFile,
                directoryPath,
                outputRoot
            );

            fail("Should have thrown IllegalStateException for invalid JAR");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("not a valid JAR file"));
        }
    }

    @org.junit.Test
    public void testGetClassLoader() throws IOException {
        File testDir = tempFolder.newFolder("loader-test");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertNotNull(testCase.getClassLoader());
    }

    @org.junit.Test
    public void testSetClassLoader() throws IOException {
        File testDir = tempFolder.newFolder("set-loader");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        ClassLoader originalLoader = testCase.getClassLoader();
        ClassLoader newLoader = ClassLoader.getSystemClassLoader();
        testCase.setClassLoader(newLoader);

        assertEquals(newLoader, testCase.getClassLoader());
        assertNotEquals(originalLoader, testCase.getClassLoader());
    }

    @org.junit.Test
    public void testGetTestFile() throws IOException {
        File testDir = tempFolder.newFolder("get-file");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertEquals(testDir, testCase.getTestFile());
    }

    @org.junit.Test
    public void testGetType() throws IOException {
        File testDir = tempFolder.newFolder("get-type");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertEquals(CastorTestCase.DIRECTORY, testCase.getType());
    }

    @org.junit.Test
    public void testGetDirectoryToHere() throws IOException {
        File testDir = tempFolder.newFolder("dir-path");
        String directoryPath = "org/castor/test";
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            directoryPath,
            outputRoot
        );

        assertEquals(directoryPath, testCase.getDirectoryToHere());
    }

    @org.junit.Test
    public void testGetOutputRootFile() throws IOException {
        File testDir = tempFolder.newFolder("output-test");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertNotNull(testCase.getOutputRootFile());
        assertTrue(testCase.getOutputRootFile().exists());
    }

    @org.junit.Test
    public void testIsDirectoryCompiled() throws IOException {
        File testDir = tempFolder.newFolder("compiled-test");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertFalse(testCase.isDirectoryCompiled());
    }

    @org.junit.Test
    public void testSetDirectoryCompiled() throws IOException {
        File testDir = tempFolder.newFolder("set-compiled");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertFalse(testCase.isDirectoryCompiled());
        testCase.setDirectoryCompiled(true);
        assertTrue(testCase.isDirectoryCompiled());
        testCase.setDirectoryCompiled(false);
        assertFalse(testCase.isDirectoryCompiled());
    }

    @org.junit.Test
    public void testMultipleInstances() throws IOException {
        File testDir1 = tempFolder.newFolder("dir1");
        File testDir2 = tempFolder.newFolder("dir2");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase1 = new CastorTestCase(
            testDir1,
            "path1",
            outputRoot
        );
        CastorTestCase testCase2 = new CastorTestCase(
            testDir2,
            "path2",
            outputRoot
        );

        assertNotNull(testCase1);
        assertNotNull(testCase2);
        assertNotSame(testCase1, testCase2);
        assertNotEquals(testCase1.getTestFile(), testCase2.getTestFile());
    }

    @org.junit.Test
    public void testConstructorWithEmptyDirectoryToHere() throws IOException {
        File testDir = tempFolder.newFolder("empty-dir-path");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertEquals("", testCase.getDirectoryToHere());
    }

    @org.junit.Test
    public void testConstructorWithComplexDirectoryPath() throws IOException {
        File testDir = tempFolder.newFolder("complex-path-test");
        String complexPath = "org/exolab/castor/tests/framework/marshalling";
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            complexPath,
            outputRoot
        );

        assertEquals(complexPath, testCase.getDirectoryToHere());
    }

    @org.junit.Test
    public void testOutputRootFileCreation() throws IOException {
        File testDir = tempFolder.newFolder("output-creation");
        File outputRoot = tempFolder.newFolder("output-dest");

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            "test",
            outputRoot.getAbsolutePath()
        );

        assertTrue(testCase.getOutputRootFile().exists());
        assertTrue(testCase.getOutputRootFile().isDirectory());
    }

    @org.junit.Test
    public void testNameOnlyConstructorProperties() {
        CastorTestCase testCase = new CastorTestCase("SimpleTest");

        assertNull(testCase.getTestFile());
        assertNull(testCase.getOutputRootFile());
        assertEquals("", testCase.getDirectoryToHere());
        assertEquals(CastorTestCase.UNKNOWN, testCase.getType());
        assertEquals("SimpleTest", testCase.getName());
    }

    @org.junit.Test
    public void testClassLoaderForDirectory() throws IOException {
        File testDir = tempFolder.newFolder("loader-dir");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        assertNotNull(testCase.getClassLoader());
        assertNotNull(testCase.getClassLoader().getParent());
    }

    @org.junit.Test
    public void testSetCompiledTwice() throws IOException {
        File testDir = tempFolder.newFolder("set-compiled-twice");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        testCase.setDirectoryCompiled(true);
        assertTrue(testCase.isDirectoryCompiled());
        testCase.setDirectoryCompiled(true);
        assertTrue(testCase.isDirectoryCompiled());
    }

    @org.junit.Test
    public void testConstructorPersistentProperties() throws IOException {
        File testDir = tempFolder.newFolder("persistent");
        String dirPath = "path/to/test";
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            dirPath,
            outputRoot
        );

        assertEquals(testDir, testCase.getTestFile());
        assertEquals(dirPath, testCase.getDirectoryToHere());
        assertEquals(CastorTestCase.DIRECTORY, testCase.getType());

        testCase.setDirectoryCompiled(true);
        assertTrue(testCase.isDirectoryCompiled());

        assertEquals(testDir, testCase.getTestFile());
        assertEquals(dirPath, testCase.getDirectoryToHere());
    }

    @org.junit.Test
    public void testNestedDirectoryCreation() throws IOException {
        File testDir = tempFolder.newFolder("nested-dir");
        File nestedOutput = new File(
            tempFolder.getRoot().getAbsolutePath() + "/nested/output"
        );

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            "test",
            nestedOutput.getAbsolutePath()
        );

        assertTrue(testCase.getOutputRootFile().exists());
    }

    @org.junit.Test
    public void testConstantsValues() {
        assertEquals(-1, CastorTestCase.UNKNOWN);
        assertEquals(0, CastorTestCase.DIRECTORY);
        assertEquals(1, CastorTestCase.JAR);
    }

    @org.junit.Test
    public void testTestDescriptorConstant() {
        assertNotNull(CastorTestCase.TEST_DESCRIPTOR);
        assertEquals("TestDescriptor.xml", CastorTestCase.TEST_DESCRIPTOR);
    }

    @org.junit.Test
    public void testMultipleSetClassLoader() throws IOException {
        File testDir = tempFolder.newFolder("multi-loader");
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(testDir, "", outputRoot);

        ClassLoader loader1 = ClassLoader.getSystemClassLoader();
        ClassLoader loader2 = CastorTestCaseTest.class.getClassLoader();

        testCase.setClassLoader(loader1);
        assertEquals(loader1, testCase.getClassLoader());

        testCase.setClassLoader(loader2);
        assertEquals(loader2, testCase.getClassLoader());
    }

    @org.junit.Test
    public void testConstructorWithSpecialCharactersInPath()
        throws IOException {
        File testDir = tempFolder.newFolder("special-chars");
        String specialPath = "path/with-dash_underscore/and.dots";
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            specialPath,
            outputRoot
        );

        assertEquals(specialPath, testCase.getDirectoryToHere());
    }

    @org.junit.Test
    public void testConstructorWithLongPath() throws IOException {
        File testDir = tempFolder.newFolder("long-path");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append("level").append(i).append("/");
        }
        String longPath = sb.toString();
        String outputRoot = tempFolder.getRoot().getAbsolutePath();

        CastorTestCase testCase = new CastorTestCase(
            testDir,
            longPath,
            outputRoot
        );

        assertEquals(longPath, testCase.getDirectoryToHere());
    }
}
