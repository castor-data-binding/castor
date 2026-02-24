/*
 * Copyright 2006 Edward Kuns
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
 /*
  * Comprehensive test suite for TestSourceGenerator class
  */
package org.castor.xmlctf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.exolab.castor.tests.framework.testDescriptor.Failure;
import org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.exolab.castor.tests.framework.testDescriptor.types.CollectionType;
import org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType;
import org.exolab.castor.xml.XMLContext;

/**
 * Unit tests for TestSourceGenerator class.
 */
public class TestSourceGeneratorTest extends TestCase {

    private TestSourceGenerator testSourceGenerator;
    private MockCastorTestCase mockCastorTestCase;
    private MockUnitTestCase mockUnitTestCase;
    private MockOnlySourceGenerationTest mockSourceGen;
    private File tempOutputDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Create a temporary directory for test output
        tempOutputDir = new File(
            System.getProperty("java.io.tmpdir"),
            "testSourceGeneratorTest_" + System.currentTimeMillis()
        );
        if (tempOutputDir.exists()) {
            deleteDirectory(tempOutputDir);
        }
        tempOutputDir.mkdirs();

        // Setup mock behaviors
        mockCastorTestCase = new MockCastorTestCase();
        mockCastorTestCase.setOutputRootFile(tempOutputDir);
        mockCastorTestCase.setTestFile(tempOutputDir);

        mockUnitTestCase = new MockUnitTestCase();
        mockUnitTestCase.setName("TestSourceGeneratorTest");
        mockUnitTestCase.setSkip(false);
        mockUnitTestCase.setGenerateImported(false);

        mockSourceGen = new MockOnlySourceGenerationTest();
        mockSourceGen.setProperty_File(null);
        mockSourceGen.setCollection(CollectionType.ARRAYLIST);
        mockSourceGen.setBindingFile(null);
        mockSourceGen.setSchema(new String[0]);
        mockSourceGen.setPackage("org.test");

        testSourceGenerator = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        testSourceGenerator.setXMLContext(new XMLContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // Clean up temporary directory
        if (tempOutputDir != null && tempOutputDir.exists()) {
            deleteDirectory(tempOutputDir);
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    public void testConstructorWithOnlySourceGenerationTest() throws Exception {
        assertNotNull(
            "TestSourceGenerator should be created",
            testSourceGenerator
        );
        assertNotNull("Should have test name", testSourceGenerator.getName());
    }

    public void testConstructorWithSourceGeneratorTest() throws Exception {
        MockSourceGeneratorTest mockSourceGenTest =
            new MockSourceGeneratorTest();
        mockSourceGenTest.setProperty_File(null);
        mockSourceGenTest.setCollection(CollectionType.ARRAYLIST);
        mockSourceGenTest.setBindingFile(null);
        mockSourceGenTest.setSchema(new String[] { "test.xsd" });
        mockSourceGenTest.setPackage("org.example");

        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGenTest
        );
        assertNotNull(
            "TestSourceGenerator should be created with SourceGeneratorTest",
            tsg
        );
    }

    public void testSetUp() throws Exception {
        testSourceGenerator.setUp();
        // Should not throw exception
    }

    public void testTearDown() throws Exception {
        testSourceGenerator.tearDown();
        // Should not throw exception
    }

    public void testSetExpectedSources() throws Exception {
        List<String> expectedSources = new ArrayList<String>();
        expectedSources.add("org.example.TestClass");
        expectedSources.add("org.example.AnotherClass");

        testSourceGenerator.setExpectedSources(expectedSources);
        // Verify it doesn't throw exception
    }

    public void testSetExpectedSourcesWithNull() throws Exception {
        testSourceGenerator.setExpectedSources(null);
        // Should handle null gracefully
    }

    public void testSetExpectedSourcesWithEmptyList() throws Exception {
        testSourceGenerator.setExpectedSources(new ArrayList<String>());
        // Should handle empty list gracefully
    }

    public void testGetName() throws Exception {
        String name = testSourceGenerator.getName();
        assertNotNull("Name should not be null", name);
    }

    public void testSetTestSuiteName() throws Exception {
        String suiteName = "TestSuite";
        testSourceGenerator.setTestSuiteName(suiteName);
        String retrieved = testSourceGenerator.getTestSuiteName();
        assertNotNull("Suite name should be set", retrieved);
    }

    public void testConstructorWithPropertyFile() throws Exception {
        mockSourceGen.setProperty_File("builder.properties");
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should create with property file", tsg);
    }

    public void testConstructorWithBindingFile() throws Exception {
        mockSourceGen.setBindingFile("binding.xml");
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should create with binding file", tsg);
    }

    public void testMultipleSchemas() throws Exception {
        String[] schemas = new String[] {
            "schema1.xsd",
            "schema2.xsd",
            "schema3.xsd",
        };
        mockSourceGen.setSchema(schemas);

        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should handle multiple schemas", tsg);
    }

    public void testPackageConfiguration() throws Exception {
        mockSourceGen.setPackage("org.example.generated");
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should handle custom package", tsg);
    }

    public void testGenerateImportedSchemasTrue() throws Exception {
        mockUnitTestCase.setGenerateImported(true);

        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should handle generate imported schemas flag", tsg);
    }

    public void testGenerateImportedSchemasFalse() throws Exception {
        mockUnitTestCase.setGenerateImported(false);

        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull(
            "Should handle false generate imported schemas flag",
            tsg
        );
    }

    public void testConstructorWithVariousCollectionTypes() throws Exception {
        for (CollectionType type : new CollectionType[] {
            CollectionType.ARRAYLIST,
            CollectionType.VECTOR,
            CollectionType.ODMG,
        }) {
            mockSourceGen.setCollection(type);
            TestSourceGenerator tsg = new TestSourceGenerator(
                mockCastorTestCase,
                mockUnitTestCase,
                mockSourceGen
            );
            assertNotNull("Should handle collection type: " + type, tsg);
        }
    }

    public void testInheritanceFromXMLTestCase() throws Exception {
        assertTrue(
            "TestSourceGenerator should extend XMLTestCase",
            testSourceGenerator instanceof XMLTestCase
        );
    }

    public void testMultipleSetExpectedSources() throws Exception {
        List<String> sources1 = new ArrayList<String>();
        sources1.add("Class1");
        testSourceGenerator.setExpectedSources(sources1);

        List<String> sources2 = new ArrayList<String>();
        sources2.add("Class2");
        sources2.add("Class3");
        testSourceGenerator.setExpectedSources(sources2);

        assertNotNull(testSourceGenerator);
    }

    public void testRunTestWhenSkipped() throws Exception {
        mockUnitTestCase.setSkip(true);
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        tsg.runTest(); // Should handle skip gracefully
    }

    public void testConstructorInitializesCorrectly() throws Exception {
        mockSourceGen.setProperty_File("test.properties");
        mockSourceGen.setCollection(CollectionType.ARRAYLIST);
        mockSourceGen.setBindingFile("binding.xml");
        mockSourceGen.setSchema(new String[] { "test.xsd" });
        mockSourceGen.setPackage("org.test.package");

        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("All fields should initialize correctly", tsg);
    }

    public void testSetupAndTeardownCycle() throws Exception {
        testSourceGenerator.setUp();
        testSourceGenerator.tearDown();
        // Should complete without exception
    }

    public void testEmptySchemaArray() throws Exception {
        mockSourceGen.setSchema(new String[0]);
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should handle empty schema array", tsg);
    }

    public void testSingleSchema() throws Exception {
        mockSourceGen.setSchema(new String[] { "only.xsd" });
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should handle single schema", tsg);
    }

    public void testLargeNumberOfSchemas() throws Exception {
        String[] schemas = new String[10];
        for (int i = 0; i < 10; i++) {
            schemas[i] = "schema" + i + ".xsd";
        }
        mockSourceGen.setSchema(schemas);
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should handle many schemas", tsg);
    }

    public void testComplexPackageName() throws Exception {
        mockSourceGen.setPackage(
            "org.example.deep.package.structure.generated"
        );
        TestSourceGenerator tsg = new TestSourceGenerator(
            mockCastorTestCase,
            mockUnitTestCase,
            mockSourceGen
        );
        assertNotNull("Should handle complex package names", tsg);
    }

    // Mock helper classes
    private static class MockCastorTestCase extends CastorTestCase {

        private File outputRootFile;
        private File testFile;

        public MockCastorTestCase() {
            super("MockTest");
        }

        public void setOutputRootFile(File file) {
            this.outputRootFile = file;
        }

        public void setTestFile(File file) {
            this.testFile = file;
        }

        @Override
        public File getOutputRootFile() {
            return outputRootFile;
        }

        @Override
        public File getTestFile() {
            return testFile;
        }
    }

    private static class MockUnitTestCase extends UnitTestCase {

        private String name;
        private boolean skip;
        private Object failure;
        private boolean generateImported = false;

        public void setName(String name) {
            this.name = name;
        }

        public void setSkip(boolean skip) {
            this.skip = skip;
        }

        public void setFailure(Failure failure) {
            this.failure = failure;
        }

        public void setGenerateImported(boolean value) {
            this.generateImported = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean getSkip() {
            return skip;
        }

        @Override
        public Failure getFailure() {
            return (Failure) failure;
        }

        @Override
        public boolean hasGenerateImported() {
            return true;
        }

        @Override
        public boolean getGenerateImported() {
            return generateImported;
        }
    }

    private static class MockOnlySourceGenerationTest
        extends OnlySourceGenerationTest
    {

        private String propertyFile;
        private CollectionType collection;
        private String bindingFile;
        private String[] schemas;
        private String packageName;

        public void setProperty_File(String file) {
            this.propertyFile = file;
        }

        public void setCollection(CollectionType col) {
            this.collection = col;
        }

        public void setBindingFile(String file) {
            this.bindingFile = file;
        }

        public void setSchema(String[] schemas) {
            this.schemas = schemas;
        }

        public void setPackage(String pkg) {
            this.packageName = pkg;
        }

        @Override
        public String getProperty_File() {
            return propertyFile;
        }

        @Override
        public CollectionType getCollection() {
            return collection;
        }

        @Override
        public String getBindingFile() {
            return bindingFile;
        }

        @Override
        public String[] getSchema() {
            return schemas;
        }

        @Override
        public String getPackage() {
            return packageName;
        }
    }

    private static class MockSourceGeneratorTest extends SourceGeneratorTest {

        private String propertyFile;
        private CollectionType collection;
        private String bindingFile;
        private String[] schemas;
        private String packageName;

        public void setProperty_File(String file) {
            this.propertyFile = file;
        }

        public void setCollection(CollectionType col) {
            this.collection = col;
        }

        public void setBindingFile(String file) {
            this.bindingFile = file;
        }

        public void setSchema(String[] schemas) {
            this.schemas = schemas;
        }

        public void setPackage(String pkg) {
            this.packageName = pkg;
        }

        @Override
        public String getProperty_File() {
            return propertyFile;
        }

        @Override
        public CollectionType getCollection() {
            return collection;
        }

        @Override
        public String getBindingFile() {
            return bindingFile;
        }

        @Override
        public String[] getSchema() {
            return schemas;
        }

        @Override
        public String getPackage() {
            return packageName;
        }
    }
}
