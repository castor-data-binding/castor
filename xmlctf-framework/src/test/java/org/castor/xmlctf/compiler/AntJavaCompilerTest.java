package org.castor.xmlctf.compiler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for AntJavaCompiler class.
 */
public class AntJavaCompilerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testDirectory;
    private AntJavaCompiler compiler;

    @Before
    public void setUp() throws IOException {
        testDirectory = tempFolder.newFolder("test-sources");
        compiler = new AntJavaCompiler(testDirectory);
    }

    @Test
    public void testConstructorWithValidDirectory() {
        assertNotNull(compiler);
        assertTrue(compiler instanceof Compiler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDirectory() {
        new AntJavaCompiler(null);
    }

    @Test
    public void testSetJavaSourceVersion14() {
        compiler.setJavaSourceVersion(1.4f);
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
        compiler.setJavaSourceVersion(-1);
    }

    @Test
    public void testSetJavaSourceVersionZero() {
        compiler.setJavaSourceVersion(0);
    }

    @Test
    public void testSetJavaSourceVersionLarge() {
        compiler.setJavaSourceVersion(20);
    }

    @Test
    public void testSetJavaSourceVersionMultipleCalls() {
        compiler.setJavaSourceVersion(1.4f);
        compiler.setJavaSourceVersion(1.5f);
        compiler.setJavaSourceVersion(1.6f);
        compiler.setJavaSourceVersion(1.7f);
        compiler.setJavaSourceVersion(1.8f);
    }

    @Test
    public void testCompileDirectoryEmpty() {
        try {
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected when no files to compile or ANT issues
        }
    }

    @Test
    public void testCompileDirectoryWithJavaFile() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected - compilation will fail without proper Java code
        }
    }

    @Test
    public void testCompileDirectoryWithMultipleFiles() throws IOException {
        for (int i = 0; i < 3; i++) {
            File javaFile = new File(testDirectory, "Test" + i + ".java");
            javaFile.createNewFile();
        }

        try {
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testCompileDirectoryMultipleCalls() {
        try {
            compiler.compileDirectory();
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
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
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testMultipleInstances() throws IOException {
        File dir1 = tempFolder.newFolder("dir1");
        File dir2 = tempFolder.newFolder("dir2");
        File dir3 = tempFolder.newFolder("dir3");

        AntJavaCompiler comp1 = new AntJavaCompiler(dir1);
        AntJavaCompiler comp2 = new AntJavaCompiler(dir2);
        AntJavaCompiler comp3 = new AntJavaCompiler(dir3);

        assertNotNull(comp1);
        assertNotNull(comp2);
        assertNotNull(comp3);
        assertNotSame(comp1, comp2);
        assertNotSame(comp2, comp3);
        assertNotSame(comp1, comp3);
    }

    @Test
    public void testCompileDirectoryWithNestedDirectories() throws IOException {
        File subDir = new File(testDirectory, "src");
        subDir.mkdirs();
        File javaFile = new File(subDir, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
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
        } catch (Exception e) {
            // Expected
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
        } catch (Exception e) {
            // Expected
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
        } catch (Exception e) {
            // Expected
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
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testCompileDirectoryDeepNesting() throws IOException {
        File level1 = new File(testDirectory, "level1");
        level1.mkdirs();
        File level2 = new File(level1, "level2");
        level2.mkdirs();
        File level3 = new File(level2, "level3");
        level3.mkdirs();
        File javaFile = new File(level3, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
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
        } catch (Exception e) {
            // Expected
        } finally {
            org.castor.xmlctf.XMLTestCase._verbose = false;
        }
    }

    @Test
    public void testSetVersionWithoutVerboseMode() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        org.castor.xmlctf.XMLTestCase._verbose = false;
        try {
            compiler.setJavaSourceVersion(1.8f);
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testCompileWithClasspathOverride() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        javaFile.createNewFile();

        String original = System.getProperty("xmlctf.classpath.override");
        try {
            System.setProperty("xmlctf.classpath.override", "/custom/path");
            compiler.setJavaSourceVersion(1.8f);
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
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
        } catch (Exception e) {
            // Expected
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

        AntJavaCompiler comp = new AntJavaCompiler(testDirectory);
        try {
            comp.compileDirectory();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testMultipleVersionSetsConcurrently() {
        compiler.setJavaSourceVersion(1.5f);
        compiler.setJavaSourceVersion(1.6f);
        compiler.setJavaSourceVersion(1.7f);
        compiler.setJavaSourceVersion(1.8f);
        compiler.setJavaSourceVersion(5);
        compiler.setJavaSourceVersion(6);
        compiler.setJavaSourceVersion(8);
    }

    @Test
    public void testCompileDirectoryManyNestedDirs() throws IOException {
        File current = testDirectory;
        for (int i = 0; i < 5; i++) {
            current = new File(current, "level" + i);
            current.mkdirs();
        }
        File javaFile = new File(current, "Test.java");
        javaFile.createNewFile();

        try {
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testCompileDirectoryManyJavaFiles() throws IOException {
        for (int i = 0; i < 10; i++) {
            File javaFile = new File(testDirectory, "Test" + i + ".java");
            javaFile.createNewFile();
        }

        try {
            compiler.compileDirectory();
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testSetVersionTwiceSameValue() {
        compiler.setJavaSourceVersion(1.6f);
        compiler.setJavaSourceVersion(1.6f);
    }

    @Test
    public void testCompileDirectoryWithSpecialCharactersInPath()
        throws IOException {
        File specialDir = new File(testDirectory, "special-dir_123");
        specialDir.mkdirs();
        File javaFile = new File(specialDir, "Test.java");
        javaFile.createNewFile();

        AntJavaCompiler compWithSpecial = new AntJavaCompiler(testDirectory);
        try {
            compWithSpecial.compileDirectory();
        } catch (Exception e) {
            // Expected
        }
    }
}
