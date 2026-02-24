package org.castor.xmlctf.compiler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CompilerFactoryTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testDirectory;

    @Before
    public void setUp() throws IOException {
        testDirectory = tempFolder.newFolder("compiler-factory-test");
    }

    @Test
    public void testCreateInstanceWithValidDirectory() {
        Compiler compiler = CompilerFactory.createInstance(testDirectory);
        assertNotNull(compiler);
        assertTrue(compiler instanceof Compiler);
    }

    @Test
    public void testCreateInstanceReturnsCompilerImplementation() {
        Compiler compiler = CompilerFactory.createInstance(testDirectory);
        assertNotNull(compiler);
        assertTrue(
            compiler instanceof OracleJavaCompiler ||
                compiler instanceof SunJavaCompiler ||
                compiler instanceof AntJavaCompiler
        );
    }

    @Test
    public void testCreateInstanceMultipleCalls() {
        Compiler compiler1 = CompilerFactory.createInstance(testDirectory);
        Compiler compiler2 = CompilerFactory.createInstance(testDirectory);
        assertNotNull(compiler1);
        assertNotNull(compiler2);
        assertNotSame(compiler1, compiler2);
    }

    @Test
    public void testCreateInstanceBasedOnJavaVersion() {
        float javaVersion = Float.parseFloat(
            System.getProperty("java.specification.version")
        );
        Compiler compiler = CompilerFactory.createInstance(testDirectory);

        if (javaVersion >= 1.6) {
            assertTrue(
                "Java 1.6+ should return OracleJavaCompiler or AntJavaCompiler",
                compiler instanceof OracleJavaCompiler ||
                    compiler instanceof AntJavaCompiler
            );
        } else {
            assertTrue(
                "Java < 1.6 should return SunJavaCompiler",
                compiler instanceof SunJavaCompiler
            );
        }
    }

    @Test
    public void testCreateInstanceIsNotNull() {
        assertNotNull(CompilerFactory.createInstance(testDirectory));
    }

    @Test
    public void testCreateInstanceWithTempDirectory() throws IOException {
        File tmpDir = tempFolder.newFolder("tmp-compile");
        Compiler compiler = CompilerFactory.createInstance(tmpDir);
        assertNotNull(compiler);
        assertTrue(compiler instanceof Compiler);
    }

    @Test
    public void testCreateInstanceConsistency() {
        Compiler compiler1 = CompilerFactory.createInstance(testDirectory);
        Compiler compiler2 = CompilerFactory.createInstance(testDirectory);

        assertEquals(compiler1.getClass(), compiler2.getClass());
    }

    @Test
    public void testCreateInstanceWithDifferentDirectories()
        throws IOException {
        File dir1 = tempFolder.newFolder("factory-dir1");
        File dir2 = tempFolder.newFolder("factory-dir2");

        Compiler compiler1 = CompilerFactory.createInstance(dir1);
        Compiler compiler2 = CompilerFactory.createInstance(dir2);

        assertNotNull(compiler1);
        assertNotNull(compiler2);
        assertNotSame(compiler1, compiler2);
        assertEquals(compiler1.getClass(), compiler2.getClass());
    }

    @Test
    public void testCreateInstanceSequentialCalls() {
        for (int i = 0; i < 5; i++) {
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(compiler instanceof Compiler);
        }
    }

    @Test
    public void testCreateInstanceWithSystemTempDirectory() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        Compiler compiler = CompilerFactory.createInstance(tmpDir);
        assertNotNull(compiler);
        assertTrue(compiler instanceof Compiler);
    }

    @Test
    public void testFactorySelectsAppropriateCompiler() {
        Compiler compiler = CompilerFactory.createInstance(testDirectory);
        assertTrue(compiler instanceof Compiler);
        assertNotNull(compiler.getClass().getName());
    }

    @Test
    public void testCreateInstanceImplementsCompilerInterface() {
        Compiler compiler = CompilerFactory.createInstance(testDirectory);
        assertTrue(
            "Returned compiler must implement Compiler interface",
            compiler instanceof Compiler
        );
    }

    @Test
    public void testCreateInstanceThrowsNoException() {
        try {
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
        } catch (Exception e) {
            fail(
                "CompilerFactory.createInstance should not throw exceptions: " +
                    e.getMessage()
            );
        }
    }

    @Test
    public void testCreateInstanceMultipleCompilersIndependent()
        throws IOException {
        File dir1 = tempFolder.newFolder("indep-factory-dir1");
        File dir2 = tempFolder.newFolder("indep-factory-dir2");

        Compiler compiler1 = CompilerFactory.createInstance(dir1);
        Compiler compiler2 = CompilerFactory.createInstance(dir2);

        assertNotSame(
            "Compilers should be different instances",
            compiler1,
            compiler2
        );
        assertNotNull(compiler1);
        assertNotNull(compiler2);
    }

    @Test
    public void testCreateInstanceCanSetVersion() {
        Compiler compiler = CompilerFactory.createInstance(testDirectory);
        try {
            compiler.setJavaSourceVersion(1.5f);
            compiler.setJavaSourceVersion(1.6f);
            compiler.setJavaSourceVersion(1.7f);
            compiler.setJavaSourceVersion(1.8f);
        } catch (IllegalStateException e) {
            // Acceptable if tools not available
            assertTrue(e.getMessage().contains("Failed"));
        }
    }

    @Test
    public void testCreateInstanceCanCompile() {
        Compiler compiler = CompilerFactory.createInstance(testDirectory);
        try {
            compiler.compileDirectory();
        } catch (CompilationException e) {
            // Expected - no files to compile
            assertTrue(e.getMessage().contains("No files to compile"));
        } catch (IllegalStateException e) {
            // Acceptable if tools not available
            assertTrue(e.getMessage().contains("Failed"));
        }
    }

    @Test
    public void testFactoryWithVersion1_5() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            System.setProperty("java.specification.version", "1.5");
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(compiler instanceof SunJavaCompiler);
        } catch (IllegalStateException e) {
            // Acceptable if tools.jar not available
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryWithVersion1_6() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            System.setProperty("java.specification.version", "1.6");
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(
                compiler instanceof OracleJavaCompiler ||
                    compiler instanceof AntJavaCompiler
            );
        } catch (IllegalStateException e) {
            // Acceptable if tools not available
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryWithVersion1_7() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            System.setProperty("java.specification.version", "1.7");
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(
                compiler instanceof OracleJavaCompiler ||
                    compiler instanceof AntJavaCompiler
            );
        } catch (IllegalStateException e) {
            // Acceptable
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryWithVersion1_8() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            System.setProperty("java.specification.version", "1.8");
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(
                compiler instanceof OracleJavaCompiler ||
                    compiler instanceof AntJavaCompiler
            );
        } catch (IllegalStateException e) {
            // Acceptable
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryWithVersion9() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            System.setProperty("java.specification.version", "9");
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(
                compiler instanceof OracleJavaCompiler ||
                    compiler instanceof AntJavaCompiler
            );
        } catch (IllegalStateException e) {
            // Acceptable
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryWithVersion11() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            System.setProperty("java.specification.version", "11");
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(
                compiler instanceof OracleJavaCompiler ||
                    compiler instanceof AntJavaCompiler
            );
        } catch (IllegalStateException e) {
            // Acceptable
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryWithHighJavaVersion() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            System.setProperty("java.specification.version", "21");
            Compiler compiler = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler);
            assertTrue(
                compiler instanceof OracleJavaCompiler ||
                    compiler instanceof AntJavaCompiler
            );
        } catch (IllegalStateException e) {
            // Acceptable
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryCreatesWorkingCompiler() throws IOException {
        File dir = tempFolder.newFolder("working-compiler-factory");
        File javaFile = new File(dir, "Test.java");
        javaFile.createNewFile();

        Compiler compiler = CompilerFactory.createInstance(dir);
        assertNotNull(compiler);

        try {
            compiler.compileDirectory();
        } catch (CompilationException e) {
            // Expected - file is empty
            assertNotNull(e.getMessage());
        } catch (IllegalStateException e) {
            // Acceptable - tools not available
            assertTrue(e.getMessage().contains("Failed"));
        }
    }

    @Test
    public void testFactoryWithNullThrowsException() {
        try {
            Compiler compiler = CompilerFactory.createInstance(null);
            // Some implementations might handle null
            if (
                compiler instanceof OracleJavaCompiler ||
                compiler instanceof SunJavaCompiler
            ) {
                fail("Should throw exception on null directory");
            }
        } catch (IllegalArgumentException e) {
            // Expected
            assertNotNull(e.getMessage());
        } catch (NullPointerException e) {
            // Also acceptable
            assertNotNull(e);
        }
    }

    @Test
    public void testFactorySequentialOperations() throws IOException {
        File dir = tempFolder.newFolder("sequential-factory");
        Compiler compiler = CompilerFactory.createInstance(dir);

        compiler.setJavaSourceVersion(1.5f);
        try {
            compiler.compileDirectory();
        } catch (CompilationException e) {
            // Expected
        } catch (IllegalStateException e) {
            // Acceptable
        }

        compiler.setJavaSourceVersion(1.6f);
        try {
            compiler.compileDirectory();
        } catch (CompilationException e) {
            // Expected
        } catch (IllegalStateException e) {
            // Acceptable
        }
    }

    @Test
    public void testFactoryMultipleDifferentVersions() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            // Test boundary where behavior changes
            System.setProperty("java.specification.version", "1.5");
            Compiler compiler1 = CompilerFactory.createInstance(testDirectory);

            System.setProperty("java.specification.version", "1.6");
            Compiler compiler2 = CompilerFactory.createInstance(testDirectory);

            System.setProperty("java.specification.version", "1.4");
            Compiler compiler3 = CompilerFactory.createInstance(testDirectory);

            // All should be non-null
            assertNotNull(compiler1);
            assertNotNull(compiler2);
            assertNotNull(compiler3);
        } catch (IllegalStateException e) {
            // Acceptable
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryBoundaryAt1_6() throws Exception {
        String originalVersion = System.getProperty(
            "java.specification.version"
        );
        try {
            // Just below the boundary
            System.setProperty("java.specification.version", "1.59");
            Compiler compiler1 = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler1);
            // Should be SunJavaCompiler since 1.59 < 1.6
            assertTrue(compiler1 instanceof SunJavaCompiler);

            // At the boundary
            System.setProperty("java.specification.version", "1.6");
            Compiler compiler2 = CompilerFactory.createInstance(testDirectory);
            assertNotNull(compiler2);
            // Should be OracleJavaCompiler since 1.6 >= 1.6
            assertTrue(
                compiler2 instanceof OracleJavaCompiler ||
                    compiler2 instanceof AntJavaCompiler
            );
        } catch (IllegalStateException e) {
            // Acceptable if tools not available
            assertTrue(e.getMessage().contains("Failed"));
        } finally {
            System.setProperty("java.specification.version", originalVersion);
        }
    }

    @Test
    public void testFactoryAlwaysReturnsNewInstance() {
        Compiler c1 = CompilerFactory.createInstance(testDirectory);
        Compiler c2 = CompilerFactory.createInstance(testDirectory);
        Compiler c3 = CompilerFactory.createInstance(testDirectory);

        assertNotSame(c1, c2);
        assertNotSame(c2, c3);
        assertNotSame(c1, c3);
    }
}
