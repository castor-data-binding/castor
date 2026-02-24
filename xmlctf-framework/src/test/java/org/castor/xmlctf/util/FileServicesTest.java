package org.castor.xmlctf.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for FileServices class.
 */
public class FileServicesTest {

    private Path tempDir;

    @Before
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("fileservices_test");
    }

    @After
    public void tearDown() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            deleteDirectory(tempDir.toFile());
        }
    }

    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        dir.delete();
    }

    // ===== Test copySupportFiles() with null arguments =====

    @Test(expected = IllegalArgumentException.class)
    public void testCopySupportFilesWithNullFile() throws IOException {
        File root = new File(tempDir.toFile(), "root");
        FileServices.copySupportFiles(null, root);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopySupportFilesWithNullRoot() throws IOException {
        File file = new File(tempDir.toFile(), "file.jar");
        FileServices.copySupportFiles(file, null);
    }

    // ===== Test copySupportFiles() with directory =====

    @Test
    public void testCopySupportFilesFromDirectory() throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        sourceDir.mkdirs();

        // Create test files
        File xsdFile = new File(sourceDir, "test.xsd");
        File xmlFile = new File(sourceDir, "test.xml");
        File javaFile = new File(sourceDir, "Test.java");
        File txtFile = new File(sourceDir, "readme.txt");

        xsdFile.createNewFile();
        xmlFile.createNewFile();
        javaFile.createNewFile();
        txtFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        assertTrue(new File(destDir, "test.xsd").exists());
        assertTrue(new File(destDir, "test.xml").exists());
        assertTrue(new File(destDir, "Test.java").exists());
        assertFalse(new File(destDir, "readme.txt").exists());
    }

    @Test
    public void testCopySupportFilesFromDirectoryRecursive()
        throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        File subDir = new File(sourceDir, "subdir");
        subDir.mkdirs();

        // Create nested files
        File xsdFile = new File(subDir, "nested.xsd");
        File xmlFile = new File(subDir, "nested.xml");
        xsdFile.createNewFile();
        xmlFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        assertTrue(new File(destDir, "subdir/nested.xsd").exists());
        assertTrue(new File(destDir, "subdir/nested.xml").exists());
    }

    @Test
    public void testCopySupportFilesEmptyDirectory() throws IOException {
        File sourceDir = new File(tempDir.toFile(), "empty");
        sourceDir.mkdirs();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);
        // Should not throw any exception
        assertTrue(destDir.exists());
    }

    // ===== Test copySupportFiles() with JAR file =====

    @Test
    public void testCopySupportFilesFromJarFile() throws IOException {
        // Create a simple JAR-like file for testing
        File jarFile = new File(tempDir.toFile(), "test.jar");
        jarFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        // This will attempt to process as JAR, but since it's empty, no files will be extracted
        try {
            FileServices.copySupportFiles(jarFile, destDir);
        } catch (Exception e) {
            // Expected for invalid JAR
        }
    }

    // ===== Test copySupportFiles() with non-JAR, non-directory file =====

    @Test
    public void testCopySupportFilesWithUnsupportedFileType()
        throws IOException {
        File sourceFile = new File(tempDir.toFile(), "test.txt");
        sourceFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceFile, destDir);
        // Should not throw exception but no files should be copied
    }

    // ===== Test isScmDirectory() =====

    @Test
    public void testIsScmDirectoryCVS() {
        assertTrue(FileServices.isScmDirectory("CVS"));
    }

    @Test
    public void testIsScmDirectorySVN() {
        assertTrue(FileServices.isScmDirectory(".svn"));
    }

    @Test
    public void testIsScmDirectoryGIT() {
        assertFalse(FileServices.isScmDirectory(".git"));
    }

    @Test
    public void testIsScmDirectoryRegular() {
        assertFalse(FileServices.isScmDirectory("src"));
    }

    @Test
    public void testIsScmDirectoryEmpty() {
        assertFalse(FileServices.isScmDirectory(""));
    }

    @Test
    public void testIsScmDirectoryCVSVariant() {
        assertFalse(FileServices.isScmDirectory("cVS"));
    }

    // ===== Test constants =====

    @Test
    public void testConstantsCVS() {
        assertEquals("CVS", FileServices.CVS);
    }

    @Test
    public void testConstantsSVN() {
        assertEquals(".svn", FileServices.SVN);
    }

    @Test
    public void testConstantsGIT() {
        assertEquals(".git", FileServices.GIT);
    }

    @Test
    public void testConstantsXSD() {
        assertEquals(".xsd", FileServices.XSD);
    }

    @Test
    public void testConstantsXML() {
        assertEquals(".xml", FileServices.XML);
    }

    @Test
    public void testConstantsJAVA() {
        assertEquals(".java", FileServices.JAVA);
    }

    @Test
    public void testConstantsJAR() {
        assertEquals(".jar", FileServices.JAR);
    }

    // ===== Test file extension handling =====

    @Test
    public void testCopySupportFilesWithXSDExtension() throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        sourceDir.mkdirs();

        File xsdFile = new File(sourceDir, "schema.xsd");
        xsdFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        assertTrue(new File(destDir, "schema.xsd").exists());
    }

    @Test
    public void testCopySupportFilesWithXMLExtension() throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        sourceDir.mkdirs();

        File xmlFile = new File(sourceDir, "data.xml");
        xmlFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        assertTrue(new File(destDir, "data.xml").exists());
    }

    @Test
    public void testCopySupportFilesWithJAVAExtension() throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        sourceDir.mkdirs();

        File javaFile = new File(sourceDir, "Main.java");
        javaFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        assertTrue(new File(destDir, "Main.java").exists());
    }

    @Test
    public void testCopySupportFilesIgnoresUnsupportedFiles()
        throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        sourceDir.mkdirs();

        File txtFile = new File(sourceDir, "readme.txt");
        File docFile = new File(sourceDir, "doc.pdf");
        File binFile = new File(sourceDir, "binary.bin");

        txtFile.createNewFile();
        docFile.createNewFile();
        binFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        assertFalse(new File(destDir, "readme.txt").exists());
        assertFalse(new File(destDir, "doc.pdf").exists());
        assertFalse(new File(destDir, "binary.bin").exists());
    }

    @Test
    public void testCopySupportFilesPreservesFileContent() throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        sourceDir.mkdirs();

        File sourceXml = new File(sourceDir, "test.xml");
        String testContent = "<?xml version=\"1.0\"?><root/>";
        Files.write(sourceXml.toPath(), testContent.getBytes());

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        File destXml = new File(destDir, "test.xml");
        assertTrue(destXml.exists());
        String copiedContent = new String(Files.readAllBytes(destXml.toPath()));
        assertEquals(testContent, copiedContent);
    }

    @Test
    public void testCopySupportFilesWithMultipleLevelsOfNesting()
        throws IOException {
        File sourceDir = new File(tempDir.toFile(), "source");
        File level1 = new File(sourceDir, "level1");
        File level2 = new File(level1, "level2");
        File level3 = new File(level2, "level3");
        level3.mkdirs();

        File xsdFile = new File(level3, "deep.xsd");
        xsdFile.createNewFile();

        File destDir = new File(tempDir.toFile(), "dest");
        destDir.mkdirs();

        FileServices.copySupportFiles(sourceDir, destDir);

        assertTrue(new File(destDir, "level1/level2/level3/deep.xsd").exists());
    }

    @Test
    public void testIsScmDirectoryWithPathSeparators() {
        // endsWith will match "path/CVS" because it ends with "CVS"
        assertTrue(FileServices.isScmDirectory("path/CVS"));
    }
}
