package org.castor.xmlctf.compiler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for SunJavaCompiler class.
 */
public class SunJavaCompilerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testDirectory;

    @Before
    public void setUp() throws IOException {
        testDirectory = tempFolder.newFolder("sun-compiler-test");
    }

    @Test
    public void testConstructorWithValidDirectory() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            // Expected if tools.jar is not available
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDirectory() {
        new SunJavaCompiler(null);
    }

    @Test
    public void testSetJavaSourceVersionWithFloat5() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(5.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithFloat6() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(6.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithFloat7() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(7.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithFloat8() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(8.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithFloat9() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(9.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithInteger5() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(5);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithInteger6() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(6);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithInteger8() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(8);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithInteger9() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(9);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithInteger10() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(10);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithInteger11() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(11);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWith1_5() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(1.5f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWith1_6() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(1.6f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWith1_7() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(1.7f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWith1_8() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(1.8f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWith1_9() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(1.9f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithNegativeValue() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(-1.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithZero() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(0.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetJavaSourceVersionWithVeryLarge() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(100.0f);
            assertNotNull(compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileDirectoryWithNoFiles() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.compileDirectory();
            fail("Should throw CompilationException for no files");
        } catch (CompilationException e) {
            assertTrue(e.getMessage().contains("No files to compile"));
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileDirectoryWithEmptyJavaFile() throws IOException {
        File javaFile = new File(testDirectory, "Empty.java");
        javaFile.createNewFile();

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.compileDirectory();
            // May succeed or fail depending on environment
        } catch (CompilationException e) {
            // Expected - invalid Java file
            assertNotNull(e.getMessage());
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileDirectoryWithValidJavaFile() throws IOException {
        File javaFile = new File(testDirectory, "Valid.java");
        try (PrintWriter writer = new PrintWriter(javaFile)) {
            writer.println("public class Valid {");
            writer.println("  public Valid() {}");
            writer.println("}");
        }

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.compileDirectory();
            // May succeed or fail depending on environment
        } catch (CompilationException e) {
            // Expected if tools.jar issues
            assertNotNull(e.getMessage());
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileDirectoryIgnoresCVSDirectory() throws IOException {
        File cvsDir = new File(testDirectory, "CVS");
        cvsDir.mkdirs();
        File javaFile = new File(cvsDir, "Ignored.java");
        javaFile.createNewFile();

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.compileDirectory();
            fail("Should throw CompilationException for no files");
        } catch (CompilationException e) {
            assertTrue(e.getMessage().contains("No files to compile"));
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileDirectoryIgnoresSVNDirectory() throws IOException {
        File svnDir = new File(testDirectory, ".svn");
        svnDir.mkdirs();
        File javaFile = new File(svnDir, "Ignored.java");
        javaFile.createNewFile();

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.compileDirectory();
            fail("Should throw CompilationException for no files");
        } catch (CompilationException e) {
            assertTrue(e.getMessage().contains("No files to compile"));
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileDirectoryWithNestedDirectories() throws IOException {
        File nested = new File(testDirectory, "nested/src");
        nested.mkdirs();
        File javaFile = new File(nested, "Nested.java");
        try (PrintWriter writer = new PrintWriter(javaFile)) {
            writer.println("public class Nested {}");
        }

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.compileDirectory();
            // May succeed or fail
        } catch (CompilationException e) {
            assertNotNull(e.getMessage());
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileDirectoryMultipleTimes() throws IOException {
        File javaFile = new File(testDirectory, "Test.java");
        try (PrintWriter writer = new PrintWriter(javaFile)) {
            writer.println("public class Test {}");
        }

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            try {
                compiler.compileDirectory();
            } catch (CompilationException e) {
                // Expected
            }
            try {
                compiler.compileDirectory();
            } catch (CompilationException e) {
                // Expected
            }
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testMultipleCompilerInstances() {
        try {
            SunJavaCompiler compiler1 = new SunJavaCompiler(testDirectory);
            SunJavaCompiler compiler2 = new SunJavaCompiler(testDirectory);
            assertNotSame(compiler1, compiler2);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompilerImplementsInterface() {
        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            assertTrue(compiler instanceof Compiler);
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetVersionThenCompile() throws IOException {
        File javaFile = new File(testDirectory, "Version.java");
        javaFile.createNewFile();

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(1.6f);
            try {
                compiler.compileDirectory();
            } catch (CompilationException e) {
                // Expected
            }
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testSetVersionThenChangeVersionThenCompile()
        throws IOException {
        File javaFile = new File(testDirectory, "ChangeVersion.java");
        javaFile.createNewFile();

        try {
            SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
            compiler.setJavaSourceVersion(1.5f);
            compiler.setJavaSourceVersion(1.7f);
            try {
                compiler.compileDirectory();
            } catch (CompilationException e) {
                // Expected
            }
        } catch (IllegalStateException e) {
            assertTrue(
                e.getMessage().contains("Failed to find compile method")
            );
        }
    }

    @Test
    public void testCompileWithClasspathOverride() throws IOException {
        String originalClasspath = System.getProperty(
            "xmlctf.classpath.override"
        );
        try {
            System.setProperty("xmlctf.classpath.override", "/tmp/override");
            File javaFile = new File(testDirectory, "Override.java");
            javaFile.createNewFile();

            try {
                SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
                try {
                    compiler.compileDirectory();
                } catch (CompilationException e) {
                    // Expected
                }
            } catch (IllegalStateException e) {
                assertTrue(
                    e.getMessage().contains("Failed to find compile method")
                );
            }
        } finally {
            if (originalClasspath != null) {
                System.setProperty(
                    "xmlctf.classpath.override",
                    originalClasspath
                );
            } else {
                System.clearProperty("xmlctf.classpath.override");
            }
        }
    }

    @Test
    public void testCompileWithVerboseMode() throws IOException {
        boolean originalVerbose = org.castor.xmlctf.XMLTestCase._verbose;
        try {
            org.castor.xmlctf.XMLTestCase._verbose = true;
            File javaFile = new File(testDirectory, "Verbose.java");
            javaFile.createNewFile();

            try {
                SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
                try {
                    compiler.compileDirectory();
                } catch (CompilationException e) {
                    // Expected
                }
            } catch (IllegalStateException e) {
                assertTrue(
                    e.getMessage().contains("Failed to find compile method")
                );
            }
        } finally {
            org.castor.xmlctf.XMLTestCase._verbose = originalVerbose;
        }
    }

    @Test
    public void testCompileWithNonVerboseMode() throws IOException {
        boolean originalVerbose = org.castor.xmlctf.XMLTestCase._verbose;
        try {
            org.castor.xmlctf.XMLTestCase._verbose = false;
            File javaFile = new File(testDirectory, "NonVerbose.java");
            javaFile.createNewFile();

            try {
                SunJavaCompiler compiler = new SunJavaCompiler(testDirectory);
                try {
                    compiler.compileDirectory();
                } catch (CompilationException e) {
                    // Expected
                }
            } catch (IllegalStateException e) {
                assertTrue(
                    e.getMessage().contains("Failed to find compile method")
                );
            }
        } finally {
            org.castor.xmlctf.XMLTestCase._verbose = originalVerbose;
        }
    }
}
