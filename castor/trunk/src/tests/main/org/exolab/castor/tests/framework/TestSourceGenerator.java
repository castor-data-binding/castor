/*
 * Copyright 2006 Edward Kuns
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: TestSourceGenerator.java 0000 2006-10-25 00:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.exolab.castor.builder.FieldInfoFactory;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType;
import org.xml.sax.InputSource;

/**
 * This class encapsulate all the logic needed to run the source generator by
 * itself and then compile the file that have been generated. This class does
 * not do anything additional. It only runs the source generator and ensures
 * that the generated source will compile without error.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
public class TestSourceGenerator extends XMLTestCase {

    /** Name of the property file to use, null if none. */
    private final String   _propertyFileName;
    /** Name of the collection to use by default, null if we rely on the default behavior. */
    private final String   _fieldInfoFactoryName;
    /** Name of the binding file. */
    private final String   _bindingFileName;
    /** Array of schemas we'll process. */
    private final String[] _schemas;
    /** Package name for generated source. */
    private final String   _package;

    public TestSourceGenerator(final CastorTestCase test, final UnitTestCase unit, final OnlySourceGenerationTest sourceGen) {
        super(test, unit);
        _propertyFileName     = sourceGen.getProperty_File();
        _fieldInfoFactoryName = sourceGen.getCollection().toString();
        _bindingFileName      = sourceGen.getBindingFile();
        _schemas              = sourceGen.getSchema();
        _package              = sourceGen.getPackage();
    }

    public TestSourceGenerator(final CastorTestCase test, final UnitTestCase unit, final SourceGeneratorTest sourceGen) {
        super(test, unit);
        _propertyFileName     = sourceGen.getProperty_File();
        _fieldInfoFactoryName = sourceGen.getCollection().toString();
        _bindingFileName      = sourceGen.getBindingFile();
        _schemas              = sourceGen.getSchema();
        _package              = sourceGen.getPackage();
    }

    /**
     * Sets up this test suite.
     * @throws java.lang.Exception if anything goes wrong
     */
    protected void setUp() throws java.lang.Exception {
        try {
            FileServices.copySupportFiles(_test.getTestFile(), _outputRootFile);
        } catch (IOException e) {
            fail("IOException copying support files " + e);
        }
    }

    /**
     * Cleans up after this unit test.
     * @throws java.lang.Exception if anything goes wrong
     */
    protected void tearDown() throws java.lang.Exception {
        // Nothing to do
    }

    /**
     * Runs our source generation test. Creates, configures, and executes the
     * source generator on each schema we have to test. Compiles the generated
     * code. Loads classes via the appropriate class loader.
     */
    public void runTest() {
        if (_skip) {
            verbose("-->Skipping the test");
            return;
        }

        // 1. Run the source generator
        verbose("-->Running the source generator");

        try {
            final SourceGenerator sourceGen = createSourceGenerator();

            for (int i=0; i<_schemas.length; i++) {
                String schemaName = _schemas[i];
                File   schemaFile = new File(_outputRootFile, schemaName);

                if (!schemaFile.exists()) {
                    assertNotNull("Unable to find the schema: ", schemaName);
                }

                InputSource source = new InputSource(new FileReader(schemaFile));
                source.setSystemId(schemaFile.getAbsolutePath());
                sourceGen.generateSource(source, _package);
            }
        } catch (Exception e) {
            if (!checkExceptionWasExpected(e, FailureStepType.SOURCE_GENERATION)) {
                fail("Source Generator threw an Exception: " + e.getMessage());
            }
            return;
        }

        if (_failure != null && _failure.getContent() && _failure.getFailureStep() != null &&
            _failure.getFailureStep().equals(FailureStepType.SOURCE_GENERATION)) {
            fail("Source Generator was expected to fail, but succeeded");
            return;
        }

        // 2. Compile the files generated by the source generator
        verbose("-->Compiling the files in " + _outputRootFile);
        try {
            Compiler compiler = new SunJavaCompiler(_outputRootFile);
            if (_unitTest.hasJavaSourceVersion()) {
                compiler.setJavaSourceVersion(_unitTest.getJavaSourceVersion());
            }
            compiler.compileDirectory();
        } catch (CompilationException e) {
            if (!checkExceptionWasExpected(e, FailureStepType.SOURCE_COMPILATION)) {
                fail("Compiling generated source failed: " + e.getMessage());
            }
            return;
        }

        if (_failure != null && _failure.getContent() && _failure.getFailureStep() != null &&
            _failure.getFailureStep().equals(FailureStepType.SOURCE_COMPILATION)) {
            fail("Compilation was expected to fail, but succeeded");
            return;
        }

        // 3. Nest the class loader to look into the tmp dir (don't forget previous path)
        verbose("-->Set up the class loader");
        try {
            URL[] urlList = {_test.getTestFile().toURL(), _outputRootFile.toURL()};
            ClassLoader loader =  new URLClassLoader(urlList, _test.getClass().getClassLoader());
            _test.setClassLoader(loader);
        } catch (Exception e) {
            if (!checkExceptionWasExpected(e, FailureStepType.LOAD_GENERATED_CLASSES)) {
                fail("Unable to process the test case:" + e);
            }
            return;
        }

        if (_failure != null && _failure.getContent() && _failure.getFailureStep() != null &&
            _failure.getFailureStep().equals(FailureStepType.LOAD_GENERATED_CLASSES)) {
            fail("Loading the generated classes was expected to fail, but succeeded");
        }
    }

    /**
     * Creates and provides initial configuration for our Source Generator.
     *
     * @return a new SourceGenerator configured for out test
     * @throws IOException
     *             if any IOException occurs preparing the source generator.
     */
    private SourceGenerator createSourceGenerator() throws IOException {
        // Create our source generator
        final SourceGenerator sourceGen;
        if (_fieldInfoFactoryName != null) {
            FieldInfoFactory factory = new FieldInfoFactory(_fieldInfoFactoryName);
            sourceGen = new SourceGenerator(factory);
        } else {
            sourceGen = new SourceGenerator();
        }

        // Do we have a castorbuilder.properties file?
        if (_propertyFileName != null) {
            Properties prop = new Properties();
            prop.load(_test.getClassLoader().getResourceAsStream(_propertyFileName));
            sourceGen.setDefaultProperties(prop);
        } else {
            //don't forget to reset the properties
            sourceGen.setDefaultProperties(null);
        }

        // Do we have a binding file?
        if (_bindingFileName != null && _bindingFileName.length() >0) {
            File bindingFile = new File(_outputRootFile, _bindingFileName);

            if ( !bindingFile.exists()) {
                fail("Unable to find the specified binding file: " + _bindingFileName);
            }

            verbose("using binding file: " + bindingFile.getAbsolutePath());
            InputSource source = new InputSource(new FileReader(bindingFile));
            source.setSystemId(bindingFile.getAbsolutePath());
            sourceGen.setBinding(source);
        }

        // Final configuration of our source generator
        sourceGen.setEqualsMethod(true);
        sourceGen.setTestable(true);
        sourceGen.setSuppressNonFatalWarnings(true);
        sourceGen.setFailOnFirstError(true);
//      sourceGen.setGenerateImportedSchemas(true);
        sourceGen.setDestDir(_outputRootFile.getAbsolutePath());
        return sourceGen;
    }

}
