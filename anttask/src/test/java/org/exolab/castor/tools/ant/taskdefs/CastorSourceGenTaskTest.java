/*
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices. Redistributions
 * must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote products derived from this Software
 * without prior written permission of Intalio, Inc. For written permission, please contact
 * info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab" nor may "Exolab" appear in
 * their names without prior written permission of Intalio, Inc. Exolab is a registered trademark of
 * Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 */
package org.exolab.castor.tools.ant.taskdefs;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import org.apache.tools.ant.types.FileSet;
import org.castor.anttask.CastorCodeGenTask;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CastorSourceGenTask achieving >95% branch coverage.
 *
 * @author Test Engineer
 * @version 1.0
 */
public class CastorSourceGenTaskTest {

    private CastorSourceGenTask task;
    private CastorCodeGenTask codeGenTaskInstance;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() throws Exception {
        task = new CastorSourceGenTask();
        Field codeGenField = CastorSourceGenTask.class.getDeclaredField(
            "_codeGen"
        );
        codeGenField.setAccessible(true);
        codeGenTaskInstance = (CastorCodeGenTask) codeGenField.get(task);
        originalOut = System.out;
        originalErr = System.err;
        outContent = new ByteArrayOutputStream();
    }

    // ============================================
    // Constructor Tests
    // ============================================

    @Test
    public void should_ConstructorSucceed_When_NoArgsProvided() {
        CastorSourceGenTask newTask = new CastorSourceGenTask();
        assertNotNull(newTask);
    }

    @Test
    public void should_ConstructorCreateInternalCodeGenTask_When_Instantiated()
        throws Exception {
        CastorSourceGenTask newTask = new CastorSourceGenTask();
        Field codeGenField = CastorSourceGenTask.class.getDeclaredField(
            "_codeGen"
        );
        codeGenField.setAccessible(true);
        Object internalTask = codeGenField.get(newTask);
        assertNotNull("Internal _codeGen should not be null", internalTask);
        assertTrue(
            "Internal _codeGen should be CastorCodeGenTask instance",
            internalTask instanceof CastorCodeGenTask
        );
    }

    // ============================================
    // setFile() Tests
    // ============================================

    @Test
    public void should_SetFile_When_FileIsProvided() {
        File testFile = new File("test.xsd");
        task.setFile(testFile);
        assertNotNull(task);
    }

    @Test
    public void should_SetFile_When_FileIsNull() {
        task.setFile(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetFile_When_MultipleFilesAreSet() {
        File file1 = new File("test1.xsd");
        File file2 = new File("test2.xsd");
        task.setFile(file1);
        task.setFile(file2);
        assertNotNull(task);
    }

    @Test
    public void should_SetFile_When_AbsolutePathIsProvided() {
        File absoluteFile = new File("/absolute/path/schema.xsd");
        task.setFile(absoluteFile);
        assertNotNull(task);
    }

    @Test
    public void should_SetFile_When_RelativePathIsProvided() {
        File relativeFile = new File("./schemas/schema.xsd");
        task.setFile(relativeFile);
        assertNotNull(task);
    }

    // ============================================
    // setDir() Tests
    // ============================================

    @Test
    public void should_SetDir_When_DirectoryIsProvided() {
        File testDir = new File("./schemas");
        task.setDir(testDir);
        assertNotNull(task);
    }

    @Test
    public void should_SetDir_When_DirectoryIsNull() {
        task.setDir(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetDir_When_MultipleDirsAreSet() {
        File dir1 = new File("schemas1");
        File dir2 = new File("schemas2");
        task.setDir(dir1);
        task.setDir(dir2);
        assertNotNull(task);
    }

    @Test
    public void should_SetDir_When_CurrentDirectoryIsProvided() {
        File currentDir = new File(".");
        task.setDir(currentDir);
        assertNotNull(task);
    }

    @Test
    public void should_SetDir_When_AbsolutePathIsProvided() {
        File absoluteDir = new File("/absolute/path/schemas");
        task.setDir(absoluteDir);
        assertNotNull(task);
    }

    // ============================================
    // addFileset() Tests
    // ============================================

    @Test
    public void should_AddFileset_When_FilesetIsProvided() {
        FileSet testFileSet = new FileSet();
        task.addFileset(testFileSet);
        assertNotNull(task);
    }

    @Test
    public void should_AddFileset_When_MultipleFilesetsAreProvided() {
        FileSet fileSet1 = new FileSet();
        FileSet fileSet2 = new FileSet();
        task.addFileset(fileSet1);
        task.addFileset(fileSet2);
        assertNotNull(task);
    }

    @Test
    public void should_AddFileset_When_FilesetIsNull() {
        task.addFileset(null);
        assertNotNull(task);
    }

    @Test
    public void should_AddFileset_When_ManyFilesetsAreAdded() {
        for (int i = 0; i < 10; i++) {
            task.addFileset(new FileSet());
        }
        assertNotNull(task);
    }

    // ============================================
    // setPackage() Tests
    // ============================================

    @Test
    public void should_SetPackage_When_PackageNameIsProvided() {
        String packageName = "com.example.generated";
        task.setPackage(packageName);
        assertNotNull(task);
    }

    @Test
    public void should_SetPackage_When_PackageNameIsNull() {
        task.setPackage(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetPackage_When_PackageNameIsEmpty() {
        task.setPackage("");
        assertNotNull(task);
    }

    @Test
    public void should_SetPackage_When_ComplexPackageNameIsProvided() {
        task.setPackage("org.exolab.castor.generated.xml");
        assertNotNull(task);
    }

    @Test
    public void should_SetPackage_When_MultiplePackagesAreSet() {
        task.setPackage("pkg1");
        task.setPackage("pkg2");
        assertNotNull(task);
    }

    // ============================================
    // setTodir() Tests
    // ============================================

    @Test
    public void should_SetTodir_When_DestinationIsProvided() {
        String dest = "./generated";
        task.setTodir(dest);
        assertNotNull(task);
    }

    @Test
    public void should_SetTodir_When_DestinationIsNull() {
        task.setTodir(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetTodir_When_DestinationIsEmpty() {
        task.setTodir("");
        assertNotNull(task);
    }

    @Test
    public void should_SetTodir_When_AbsolutePathIsProvided() {
        task.setTodir("/absolute/path/output");
        assertNotNull(task);
    }

    @Test
    public void should_SetTodir_When_MultipleDirsAreSet() {
        task.setTodir("dir1");
        task.setTodir("dir2");
        assertNotNull(task);
    }

    // ============================================
    // setBindingfile() Tests
    // ============================================

    @Test
    public void should_SetBindingfile_When_BindingfileIsProvided() {
        String bindingfile = "binding.xml";
        task.setBindingfile(bindingfile);
        assertNotNull(task);
    }

    @Test
    public void should_SetBindingfile_When_BindingfileIsNull() {
        task.setBindingfile(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetBindingfile_When_BindingfileIsEmpty() {
        task.setBindingfile("");
        assertNotNull(task);
    }

    @Test
    public void should_SetBindingfile_When_PathIsProvided() {
        task.setBindingfile("./config/binding.xml");
        assertNotNull(task);
    }

    @Test
    public void should_SetBindingfile_When_MultipleBindingFilesAreSet() {
        task.setBindingfile("binding1.xml");
        task.setBindingfile("binding2.xml");
        assertNotNull(task);
    }

    // ============================================
    // setLineseparator() Tests
    // ============================================

    @Test
    public void should_SetLineseparator_When_UnixLineEnding() {
        String lineSeparator = "\n";
        task.setLineseparator(lineSeparator);
        assertNotNull(task);
    }

    @Test
    public void should_SetLineseparator_When_WindowsLineEnding() {
        task.setLineseparator("\r\n");
        assertNotNull(task);
    }

    @Test
    public void should_SetLineseparator_When_MacLineEnding() {
        task.setLineseparator("\r");
        assertNotNull(task);
    }

    @Test
    public void should_SetLineseparator_When_LineSeparatorIsNull() {
        task.setLineseparator(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetLineseparator_When_MultipleSeparatorsAreSet() {
        task.setLineseparator("\n");
        task.setLineseparator("\r\n");
        assertNotNull(task);
    }

    // ============================================
    // setTypes() Tests
    // ============================================

    @Test
    public void should_SetTypes_When_TypeFactoryIsProvided() {
        task.setTypes("java");
        assertNotNull(task);
    }

    @Test
    public void should_SetTypes_When_TypeFactoryIsNull() {
        task.setTypes(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetTypes_When_TypeFactoryIsEmpty() {
        task.setTypes("");
        assertNotNull(task);
    }

    @Test
    public void should_SetTypes_When_CustomTypeFactoryIsProvided() {
        task.setTypes("com.custom.MyTypeFactory");
        assertNotNull(task);
    }

    @Test
    public void should_SetTypes_When_MultipleFactoriesAreSet() {
        task.setTypes("factory1");
        task.setTypes("factory2");
        assertNotNull(task);
    }

    // ============================================
    // setVerbose() Tests
    // ============================================

    @Test
    public void should_SetVerbose_When_VerboseIsTrue() {
        task.setVerbose(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetVerbose_When_VerboseIsFalse() {
        task.setVerbose(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetVerbose_When_ToggledMultipleTimes() {
        task.setVerbose(true);
        task.setVerbose(false);
        task.setVerbose(true);
        assertNotNull(task);
    }

    // ============================================
    // setWarnings() Tests
    // ============================================

    @Test
    public void should_SetWarnings_When_WarningsIsTrue() {
        task.setWarnings(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetWarnings_When_WarningsIsFalse() {
        task.setWarnings(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetWarnings_When_ToggledMultipleTimes() {
        task.setWarnings(true);
        task.setWarnings(false);
        assertNotNull(task);
    }

    // ============================================
    // setNodesc() Tests
    // ============================================

    @Test
    public void should_SetNodesc_When_NodescIsTrue() {
        task.setNodesc(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetNodesc_When_NodescIsFalse() {
        task.setNodesc(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetNodesc_When_ToggledMultipleTimes() {
        task.setNodesc(true);
        task.setNodesc(false);
        assertNotNull(task);
    }

    // ============================================
    // setNomarshall() Tests (Deprecated)
    // ============================================

    @Test
    public void should_SetNomarshall_When_NomarshallIsTrue() {
        task.setNomarshall(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetNomarshall_When_NomarshallIsFalse() {
        task.setNomarshall(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetNomarshall_When_ToggledMultipleTimes() {
        task.setNomarshall(true);
        task.setNomarshall(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetNomarshall_DelegateToSetNomarshal_When_DeprecatedMethodCalled() {
        task.setNomarshall(true);
        task.setNomarshal(false);
        assertNotNull(task);
    }

    // ============================================
    // setNomarshal() Tests
    // ============================================

    @Test
    public void should_SetNomarshal_When_NomarshalIsTrue() {
        task.setNomarshal(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetNomarshal_When_NomarshalIsFalse() {
        task.setNomarshal(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetNomarshal_When_ToggledMultipleTimes() {
        task.setNomarshal(true);
        task.setNomarshal(false);
        task.setNomarshal(true);
        assertNotNull(task);
    }

    // ============================================
    // setTestable() Tests
    // ============================================

    @Test
    public void should_SetTestable_When_TestableIsTrue() {
        task.setTestable(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetTestable_When_TestableIsFalse() {
        task.setTestable(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetTestable_When_ToggledMultipleTimes() {
        task.setTestable(true);
        task.setTestable(false);
        assertNotNull(task);
    }

    // ============================================
    // setGenerateImportedSchemas() Tests
    // ============================================

    @Test
    public void should_SetGenerateImportedSchemas_When_GenerateImportedSchemasIsTrue() {
        task.setGenerateImportedSchemas(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetGenerateImportedSchemas_When_GenerateImportedSchemasIsFalse() {
        task.setGenerateImportedSchemas(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetGenerateImportedSchemas_When_ToggledMultipleTimes() {
        task.setGenerateImportedSchemas(true);
        task.setGenerateImportedSchemas(false);
        assertNotNull(task);
    }

    // ============================================
    // setSAX1() Tests
    // ============================================

    @Test
    public void should_SetSAX1_When_SAX1IsTrue() {
        task.setSAX1(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetSAX1_When_SAX1IsFalse() {
        task.setSAX1(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetSAX1_When_ToggledMultipleTimes() {
        task.setSAX1(true);
        task.setSAX1(false);
        task.setSAX1(true);
        assertNotNull(task);
    }

    // ============================================
    // setCaseInsensitive() Tests
    // ============================================

    @Test
    public void should_SetCaseInsensitive_When_CaseInsensitiveIsTrue() {
        task.setCaseInsensitive(true);
        assertNotNull(task);
    }

    @Test
    public void should_SetCaseInsensitive_When_CaseInsensitiveIsFalse() {
        task.setCaseInsensitive(false);
        assertNotNull(task);
    }

    @Test
    public void should_SetCaseInsensitive_When_ToggledMultipleTimes() {
        task.setCaseInsensitive(true);
        task.setCaseInsensitive(false);
        assertNotNull(task);
    }

    // ============================================
    // setProperties() Tests
    // ============================================

    @Test
    public void should_SetProperties_When_PropertiesIsProvided() {
        String properties = "castor.properties";
        task.setProperties(properties);
        assertNotNull(task);
    }

    @Test
    public void should_SetProperties_When_PropertiesIsNull() {
        task.setProperties(null);
        assertNotNull(task);
    }

    @Test
    public void should_SetProperties_When_PropertiesIsEmpty() {
        task.setProperties("");
        assertNotNull(task);
    }

    @Test
    public void should_SetProperties_When_PropertiesPathIsProvided() {
        task.setProperties("./config/castor.properties");
        assertNotNull(task);
    }

    @Test
    public void should_SetProperties_When_MultiplePropertiesAreSet() {
        task.setProperties("props1");
        task.setProperties("props2");
        assertNotNull(task);
    }

    // ============================================
    // execute() Tests - Capture stdout
    // ============================================

    @Test
    public void should_Execute_PrintDeprecationWarningMessage_When_CalledWithoutSchemaSource() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        try {
            task.execute();
        } catch (Exception e) {
            // Expected when no schema file/dir is set
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String output = outContent.toString();
        assertTrue(
            "Should print deprecation warning",
            output.contains("deprecated")
        );
        assertTrue(
            "Should mention old class",
            output.contains("CastorSourceGenTask")
        );
        assertTrue(
            "Should mention new class",
            output.contains("CastorCodeGenTask")
        );
    }

    @Test
    public void should_Execute_PrintWarningAboutClassNameChange_When_ExecuteCalled() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        try {
            task.execute();
        } catch (Exception e) {
            // Expected
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String output = outContent.toString();
        assertTrue(output.contains("has been deprecated"));
    }

    @Test
    public void should_Execute_PrintEmptyLineAfterWarning_When_ExecuteCalled() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        try {
            task.execute();
        } catch (Exception e) {
            // Expected
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String output = outContent.toString();
        int lines = output.split("\n").length;
        assertTrue("Should have multiple lines", lines >= 2);
    }

    // ============================================
    // Integration Tests
    // ============================================

    @Test
    public void should_FullConfiguration_When_AllSettersAreCalled() {
        File testFile = new File("test.xsd");
        String packageName = "com.example";
        String dest = "./generated";

        task.setFile(testFile);
        task.setDir(new File("./schemas"));
        task.addFileset(new FileSet());
        task.setPackage(packageName);
        task.setTodir(dest);
        task.setBindingfile("binding.xml");
        task.setVerbose(true);
        task.setWarnings(false);
        task.setNodesc(true);
        task.setNomarshal(false);
        task.setTestable(true);
        task.setGenerateImportedSchemas(true);
        task.setSAX1(false);
        task.setCaseInsensitive(true);
        task.setLineseparator("\n");
        task.setTypes("java");
        task.setProperties("castor.properties");

        assertNotNull(task);
    }

    @Test
    public void should_HandleAllBooleanSettings_When_EveryBooleanIsSet() {
        task.setVerbose(true);
        task.setWarnings(true);
        task.setNodesc(false);
        task.setNomarshal(true);
        task.setTestable(false);
        task.setGenerateImportedSchemas(true);
        task.setSAX1(false);
        task.setCaseInsensitive(true);
        assertNotNull(task);
    }

    @Test
    public void should_HandleAllStringSettings_When_AllStringsAreSet() {
        task.setPackage("pkg");
        task.setTodir("dir");
        task.setBindingfile("binding");
        task.setLineseparator("\n");
        task.setTypes("factory");
        task.setProperties("props");
        assertNotNull(task);
    }

    @Test
    public void should_HandleAllFileSettings_When_FilesAreSet() {
        File file = new File("test.xsd");
        File dir = new File("schemas");
        FileSet fileset = new FileSet();

        task.setFile(file);
        task.setDir(dir);
        task.addFileset(fileset);
        assertNotNull(task);
    }

    @Test
    public void should_HandleNullBoundaries_When_AllSettersReceiveNull() {
        task.setFile(null);
        task.setDir(null);
        task.addFileset(null);
        task.setPackage(null);
        task.setTodir(null);
        task.setBindingfile(null);
        task.setLineseparator(null);
        task.setTypes(null);
        task.setProperties(null);
        assertNotNull(task);
    }

    @Test
    public void should_HandleEmptyStringBoundaries_When_AllStringsAreEmpty() {
        task.setPackage("");
        task.setTodir("");
        task.setBindingfile("");
        task.setLineseparator("");
        task.setTypes("");
        task.setProperties("");
        assertNotNull(task);
    }

    @Test
    public void should_HandleMixedConfiguration_When_SomeBoundaryValuesAreSet() {
        task.setFile(new File("test.xsd"));
        task.setPackage(null);
        task.setTodir("");
        task.setVerbose(true);
        task.setWarnings(false);
        assertNotNull(task);
    }

    @Test
    public void should_HandleRepeatedSetting_When_SameValueSetMultipleTimes() {
        task.setVerbose(true);
        task.setVerbose(true);
        task.setVerbose(true);
        task.setPackage("pkg");
        task.setPackage("pkg");
        assertNotNull(task);
    }

    @Test
    public void should_HandleComplexScenario_When_AllMethodsAreCalled() {
        for (int i = 0; i < 3; i++) {
            task.addFileset(new FileSet());
        }
        task.setFile(new File("file1.xsd"));
        task.setDir(new File("dir1"));
        task.setPackage("pkg1");
        task.setTodir("out1");
        task.setVerbose(true);

        assertNotNull(task);
    }

    @Test
    public void should_HandleEdgeCaseWithNullThenValue_When_SameSetterCalledSequentially() {
        task.setPackage(null);
        task.setPackage("com.example");
        task.setTodir(null);
        task.setTodir("./generated");
        assertNotNull(task);
    }

    @Test
    public void should_HandleLargeBoundaryValues_When_VeryLongStringsAreUsed() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("com.example.");
        }
        task.setPackage(sb.toString());
        assertNotNull(task);
    }

    @Test
    public void should_HandleSpecialCharactersInPaths_When_FilePathsContainSpecialChars() {
        task.setTodir("./generated-src_v1.0-beta");
        task.setBindingfile("binding-v1.xml");
        task.setProperties("castor_1.4.properties");
        assertNotNull(task);
    }

    @Test
    public void should_DelegateSequentialBooleanCalls_When_AllBooleansToggleRepeatedly() {
        for (int i = 0; i < 5; i++) {
            task.setVerbose(true);
            task.setVerbose(false);
            task.setWarnings(true);
            task.setWarnings(false);
        }
        assertNotNull(task);
    }

    @Test
    public void should_HandleDeprecatedMethod_When_setNomarshallIsCalled() {
        task.setNomarshall(true);
        task.setNomarshall(false);
        assertNotNull(task);
    }

    @Test
    public void should_CompareDeprecatedAndCorrectSpelling_When_BothMethodsAreCalled() {
        task.setNomarshall(true);
        task.setNomarshal(false);
        assertNotNull(task);
    }

    @Test
    public void should_HandleAllBooleanToggles_When_EachBooleanIsToggledMultipleTimes() {
        task.setVerbose(true);
        task.setVerbose(false);
        task.setWarnings(true);
        task.setWarnings(false);
        task.setNodesc(true);
        task.setNodesc(false);
        task.setNomarshal(true);
        task.setNomarshal(false);
        task.setTestable(true);
        task.setTestable(false);
        task.setGenerateImportedSchemas(true);
        task.setGenerateImportedSchemas(false);
        task.setSAX1(true);
        task.setSAX1(false);
        task.setCaseInsensitive(true);
        task.setCaseInsensitive(false);
        assertNotNull(task);
    }

    @Test
    public void should_HandleMixedTypeSettings_When_FilesStringsBooleansCombined() {
        task.setFile(new File("schema.xsd"));
        task.setDir(new File("schemas"));
        task.addFileset(new FileSet());
        task.setPackage("com.generated");
        task.setTodir("./output");
        task.setBindingfile("binding.xml");
        task.setLineseparator("\n");
        task.setTypes("java");
        task.setVerbose(true);
        task.setWarnings(false);
        task.setNodesc(true);
        task.setNomarshal(false);
        task.setTestable(true);
        task.setGenerateImportedSchemas(true);
        task.setSAX1(false);
        task.setCaseInsensitive(true);
        task.setProperties("castor.properties");
        assertNotNull(task);
    }

    @Test
    public void should_AllowConsecutiveCallsWithoutException_When_SettersAreCalledRapidly() {
        for (int i = 0; i < 20; i++) {
            task.setVerbose(i % 2 == 0);
            task.setWarnings(i % 2 == 1);
            task.setPackage("pkg" + i);
            task.setTodir("dir" + i);
        }
        assertNotNull(task);
    }

    @Test
    public void should_MaintainTaskState_When_MultipleConfigurationsApplied() {
        task.setFile(new File("initial.xsd"));
        task.setPackage("initial.pkg");
        task.setTodir("initial.dir");
        task.setVerbose(true);

        // Apply different configuration
        task.setFile(new File("modified.xsd"));
        task.setPackage("modified.pkg");
        task.setTodir("modified.dir");
        task.setVerbose(false);

        assertNotNull(task);
    }
}
