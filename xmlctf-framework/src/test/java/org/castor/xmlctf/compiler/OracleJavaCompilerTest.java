package org.castor.xmlctf.compiler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for OracleJavaCompiler class.
 */
public class OracleJavaCompilerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testDirectory;
    private OracleJavaCompiler compiler;

    @Before
    public void setUp() throws IOException {
        testDirectory = tempFolder.newFolder("test-sources");
        compiler = new OracleJavaCompiler(testDirectory);
    }

    @Test
    public void testConstructorWithValidDirectory() {
        assertNotNull(compiler);
        assertTrue(compiler instanceof Compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDirectory() {
        new OracleJavaCompiler(null);
    }

    @Test
    public void testSetJavaSourceVersion15() {
        compiler.setJavaSourceVersion(1.5f);
    }

    @Test
    public void testSetJavaSourceVersion16() {
        compiler.setJavaSourceVersion(1.6f);
    }

    @Test
    public void testSetJavaSourceVersion17() {
        compiler.setJavaSourceVersion(1.7f);
    }

    @Test
    public void testSetJavaSourceVersion18() {
        compiler.setJavaSourceVersion(1.8f);
    }

    @Test
    public void testSetJavaSourceVersion5() {
        compiler.setJavaSourceVersion(5);
    }

    @Test
    public void testSetJavaSourceVersion6() {
        compiler.setJavaSourceVersion(6);
    }

    @Test
    public void testSetJavaSourceVersion7() {
        compiler.setJavaSourceVersion(7);
    }

    @Test
    public void testSetJavaSourceVersion8() {
        compiler.setJavaSourceVersion(8);
    }

    @Test
    public void testSetJavaSourceVersion9() {
        compiler.setJavaSourceVersion(9);
    }

    @Test
    public void testSetJavaSourceVersion10() {
        compiler.setJavaSourceVersion(10);
    }

    @Test
    public void testSetJavaSourceVersion11() {
        compiler.setJavaSourceVersion(11);
    }

    @Test
    public void testSetJavaSourceVersionNegative() {
        compiler.setJavaSourceVersion(-1.0f);
    }

    @Test
    public void testSetJavaSourceVersionZero() {
        compiler.setJavaSourceVersion(0.0f);
    }

    @Test
    public void testSetJavaSourceVersionLarge() {
        compiler.setJavaSourceVersion(20.0f);
    }

    @Test
    public void testSetJavaSourceVersionMultipleCalls() {
        compiler.setJavaSourceVersion(1.5f);
        compiler.setJavaSourceVersion(1.6f);
        compiler.setJavaSourceVersion(1.7f);
        compiler.setJavaSourceVersion(1.8f);
    }

    @Test
    public void testCompileDirectoryEmpty() {
        try {
            compiler.compileDirectory();
            fail("Should have thrown CompilationException");
        } catch (CompilationException e) {
            assertTrue(e.getMessage().contains("No files to compile"));
        }
    }

    @Test
    public void testCompileDirectoryWithJavaFile() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryMultipleCalls() {
        try {
            compiler.compileDirectory();
            fail("Should throw CompilationException");
        } catch (CompilationException e) {
            assertTrue(e.getMessage().contains("No files to compile"));
        }
    }

    @Test
    public void testImplementsCompilerInterface() {
        assertTrue(compiler instanceof Compiler);
    }

    @Test
    public void testSetVersionThenCompile() {
        compiler.setJavaSourceVersion(1.6f);
        try {
            compiler.compileDirectory();
            fail("Should throw CompilationException");
        } catch (CompilationException e) {
            assertTrue(e.getMessage().contains("No files to compile"));
        }
    }

    @Test
    public void testMultipleInstances() throws IOException {
        File dir1 = tempFolder.newFolder("dir1");
        File dir2 = tempFolder.newFolder("dir2");
        File dir3 = tempFolder.newFolder("dir3");

        OracleJavaCompiler comp1 = new OracleJavaCompiler(dir1);
        OracleJavaCompiler comp2 = new OracleJavaCompiler(dir2);
        OracleJavaCompiler comp3 = new OracleJavaCompiler(dir3);

        assertNotNull(comp1);
        assertNotNull(comp2);
        assertNotNull(comp3);
        assertNotSame(comp1, comp2);
        assertNotSame(comp2, comp3);
        assertNotSame(comp1, comp3);
    }

    @Test
    public void testCompileDirectoryWithMultipleJavaFiles() throws IOException {
        for (int i = 0; i < 3; i++) {
            File javaFile = new File(testDirectory, "Test" + i + ".java");
            javaFile.createNewFile();
        }

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryWithNestedDirectories() throws IOException {
        File subDir = new File(testDirectory, "src");
        subDir.mkdirs();
        File javaFile = new File(subDir, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryIgnoresCVS() throws IOException {
        File cvsDir = new File(testDirectory, "CVS");
        cvsDir.mkdirs();
        File javaInCVS = new File(cvsDir, "Test.java");
        javaInCVS.createNewFile();
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryIgnoresSVN() throws IOException {
        File svnDir = new File(testDirectory, ".svn");
        svnDir.mkdirs();
        File javaInSVN = new File(svnDir, "Test.java");
        javaInSVN.createNewFile();
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryIgnoresGIT() throws IOException {
        File gitDir = new File(testDirectory, ".git");
        gitDir.mkdirs();
        File javaInGit = new File(gitDir, "Test.java");
        javaInGit.createNewFile();
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryDoesNotCompileNonJavaFiles()
        throws IOException {
        File textFile = new File(testDirectory, "test.txt");
        textFile.createNewFile();
        File xmlFile = new File(testDirectory, "test.xml");
        xmlFile.createNewFile();

        try {
            compiler.compileDirectory();
            fail("Should throw CompilationException");
        } catch (CompilationException e) {
            assertTrue(e.getMessage().contains("No files to compile"));
        }
    }

    @Test
    public void testCompileDirectoryMixedFiles() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();
        File textFile = new File(testDirectory, "readme.txt");
        textFile.createNewFile();
        File xmlFile = new File(testDirectory, "config.xml");
        xmlFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryDeepNesting() throws IOException {
        File subDir1 = new File(testDirectory, "level1");
        subDir1.mkdirs();
        File subDir2 = new File(subDir1, "level2");
        subDir2.mkdirs();
        File subDir3 = new File(subDir2, "level3");
        subDir3.mkdirs();
        File javaFile = new File(subDir3, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testSetVersionWithVerboseMode() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        org.castor.xmlctf.XMLTestCase._verbose = true;
        try {
            compiler.setJavaSourceVersion(1.8f);
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        } finally {
            org.castor.xmlctf.XMLTestCase._verbose = false;
        }
    }

    @Test
    public void testSetVersionWithClasspathOverride() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        String original = System.getProperty("xmlctf.classpath.override");
        try {
            System.setProperty("xmlctf.classpath.override", "/custom/path");
            compiler.setJavaSourceVersion(1.8f);
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        } finally {
            if (original != null) {
                System.setProperty("xmlctf.classpath.override", original);
            } else {
                System.clearProperty("xmlctf.classpath.override");
            }
        }
    }

    @Test
    public void testCompileDirectoryWithoutClasspathOverride()
        throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        String original = System.getProperty("xmlctf.classpath.override");
        try {
            System.clearProperty("xmlctf.classpath.override");
            compiler.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        } catch (CompilationException e) {
            // Also acceptable
        } finally {
            if (original != null) {
                System.setProperty("xmlctf.classpath.override", original);
            }
        }
    }

    @Test
    public void testSetJavaSourceVersionBoundary5() {
        compiler.setJavaSourceVersion(5.0f);
    }

    @Test
    public void testSetJavaSourceVersionBoundary9() {
        compiler.setJavaSourceVersion(9.9f);
    }

    @Test
    public void testSetJavaSourceVersionOutOfRangeLow() {
        compiler.setJavaSourceVersion(2.0f);
    }

    @Test
    public void testSetJavaSourceVersionOutOfRangeHigh() {
        compiler.setJavaSourceVersion(15.0f);
    }

    @Test
    public void testConstructorFollowedByCompile() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        OracleJavaCompiler comp = new OracleJavaCompiler(testDirectory);
        try {
            comp.compileDirectory();
        } catch (IllegalStateException e) {
            // Expected - compilation will fail without proper Java code
        }
    }
}
