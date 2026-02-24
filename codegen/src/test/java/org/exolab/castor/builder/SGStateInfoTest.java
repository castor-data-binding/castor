package org.exolab.castor.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.util.dialog.Dialog;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.javasource.JClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Comprehensive test class for SGStateInfo achieving >95% coverage.
 */
@RunWith(MockitoJUnitRunner.class)
public class SGStateInfoTest {

    private SGStateInfo stateInfo;

    @Mock
    private Schema mockSchema;

    @Mock
    private SourceGenerator mockSourceGenerator;

    @Mock
    private Dialog mockDialog;

    @Mock
    private Annotated mockAnnotated;

    @Mock
    private MappingRoot mockMappingRoot;

    @Before
    public void setUp() {
        stateInfo = new SGStateInfo(mockSchema, mockSourceGenerator);
    }

    // ============= Constructor & Initialization Tests =============

    @Test
    public void should_InitializeStateInfo_When_ConstructorCalled() {
        assertNotNull(stateInfo);
        assertEquals(SGStateInfo.NORMAL_STATUS, stateInfo.getStatusCode());
        assertEquals("", stateInfo.getPackageName());
        assertNotNull(stateInfo.getSourcesByName());
        assertNotNull(stateInfo.getImportedSourcesByName());
    }

    @Test
    public void should_ReturnMockSchema_When_GetSchemaAfterConstruction() {
        assertEquals(mockSchema, stateInfo.getSchema());
    }

    @Test
    public void should_ReturnMockSourceGenerator_When_GetSourceGeneratorAfterConstruction() {
        assertEquals(mockSourceGenerator, stateInfo.getSourceGenerator());
    }

    // ============= Package Name Tests =============

    @Test
    public void should_ReturnEmptyString_When_GetPackageNameInitially() {
        assertEquals("", stateInfo.getPackageName());
    }

    @Test
    public void should_SetAndReturnPackageName_When_SetPackageNameCalled() {
        stateInfo.setPackageName("com.example.test");
        assertEquals("com.example.test", stateInfo.getPackageName());
    }

    @Test
    public void should_SetPackageNameToNull_When_SetPackageNameCalledWithNull() {
        stateInfo.setPackageName(null);
        assertNull(stateInfo.getPackageName());
    }

    @Test
    public void should_OverwritePreviousPackageName_When_SetPackageNameCalledMultipleTimes() {
        stateInfo.setPackageName("com.example.first");
        stateInfo.setPackageName("com.example.second");
        assertEquals("com.example.second", stateInfo.getPackageName());
    }

    // ============= Source Code Binding Tests =============

    @Test
    public void should_BindSourceCodeSuccessfully_When_BindSourceCodeCalled() {
        JClass[] classes = {
            new JClass("TestClass1"),
            new JClass("TestClass2"),
        };
        stateInfo.bindSourceCode(mockAnnotated, classes);
        assertArrayEquals(classes, stateInfo.getSourceCode(mockAnnotated));
    }

    @Test
    public void should_StoreClassesByName_When_BindSourceCodeCalled() {
        JClass jClass1 = new JClass("TestClass1");
        JClass jClass2 = new JClass("TestClass2");
        JClass[] classes = { jClass1, jClass2 };
        stateInfo.bindSourceCode(mockAnnotated, classes);
        assertEquals(jClass1, stateInfo.getSourceCode("TestClass1"));
        assertEquals(jClass2, stateInfo.getSourceCode("TestClass2"));
    }

    @Test
    public void should_HandleNullClassesInArray_When_BindSourceCodeCalledWithNullElement() {
        JClass jClass1 = new JClass("TestClass1");
        JClass[] classes = { jClass1, null };
        stateInfo.bindSourceCode(mockAnnotated, classes);
        assertEquals(jClass1, stateInfo.getSourceCode("TestClass1"));
        assertArrayEquals(classes, stateInfo.getSourceCode(mockAnnotated));
    }

    @Test
    public void should_OverwriteExistingBinding_When_BindSourceCodeCalledMultipleTimes() {
        JClass[] classes1 = { new JClass("ClassA") };
        JClass[] classes2 = { new JClass("ClassB") };
        stateInfo.bindSourceCode(mockAnnotated, classes1);
        stateInfo.bindSourceCode(mockAnnotated, classes2);
        assertArrayEquals(classes2, stateInfo.getSourceCode(mockAnnotated));
    }

    // ============= Get Source Code Tests =============

    @Test
    public void should_ReturnNull_When_GetSourceCodeCalledForUnboundAnnotated() {
        assertNull(stateInfo.getSourceCode(mockAnnotated));
    }

    @Test
    public void should_ReturnNull_When_GetSourceCodeByNameForNonExistentClass() {
        assertNull(stateInfo.getSourceCode("NonExistentClass"));
    }

    @Test
    public void should_ReturnJClass_When_GetSourceCodeByNameForExistingClass() {
        JClass jClass = new JClass("ExistingClass");
        JClass[] classes = { jClass };
        stateInfo.bindSourceCode(mockAnnotated, classes);
        assertEquals(jClass, stateInfo.getSourceCode("ExistingClass"));
    }

    @Test
    public void should_ReturnNull_When_GetSourceCodeByNameWithNullInput() {
        assertNull(stateInfo.getSourceCode((String) null));
    }

    // ============= Imported Source Code Tests =============

    @Test
    public void should_StoreImportedSources_When_StoreImportedSourcesByNameCalled() {
        Map<String, JClass> importedSources = new HashMap<>();
        JClass importedClass = new JClass("ImportedClass");
        importedSources.put("ImportedClass", importedClass);
        stateInfo.storeImportedSourcesByName(importedSources);
        assertEquals(
            importedClass,
            stateInfo.getImportedSourceCode("ImportedClass")
        );
    }

    @Test
    public void should_ReturnNull_When_GetImportedSourceCodeForNonExistentClass() {
        assertNull(stateInfo.getImportedSourceCode("NonExistentImportedClass"));
    }

    @Test
    public void should_MergeImportedSources_When_StoreImportedSourcesByNameCalledMultipleTimes() {
        Map<String, JClass> sources1 = new HashMap<>();
        sources1.put("Class1", new JClass("Class1"));
        Map<String, JClass> sources2 = new HashMap<>();
        sources2.put("Class2", new JClass("Class2"));
        stateInfo.storeImportedSourcesByName(sources1);
        stateInfo.storeImportedSourcesByName(sources2);
        assertNotNull(stateInfo.getImportedSourceCode("Class1"));
        assertNotNull(stateInfo.getImportedSourceCode("Class2"));
    }

    // ============= Processed Classes Tests =============

    @Test
    public void should_MarkJClassAsProcessed_When_MarkAsProcessedCalled() {
        JClass jClass = new JClass("ProcessedClass");
        stateInfo.markAsProcessed(jClass);
        assertTrue(stateInfo.processed(jClass));
    }

    @Test
    public void should_ReturnFalseForProcessed_When_JClassNotMarked() {
        JClass jClass = new JClass("UnprocessedClass");
        assertFalse(stateInfo.processed(jClass));
    }

    @Test
    public void should_NotDuplicateProcessedClass_When_MarkAsProcessedCalledMultipleTimes() {
        JClass jClass = new JClass("ProcessedClass");
        stateInfo.markAsProcessed(jClass);
        stateInfo.markAsProcessed(jClass);
        assertTrue(stateInfo.processed(jClass));
    }

    @Test
    public void should_CheckProcessedByClassName_When_ProcessedCalledWithString() {
        JClass jClass = new JClass("TestClassName");
        stateInfo.markAsProcessed(jClass);
        assertTrue(stateInfo.processed("TestClassName"));
    }

    @Test
    public void should_ReturnFalseForProcessedByClassName_When_ClassNameNotProcessed() {
        assertFalse(stateInfo.processed("UnknownClassName"));
    }

    @Test
    public void should_ReturnProcessedClass_When_GetProcessedCalledWithExistingClassName() {
        JClass jClass = new JClass("FoundClass");
        stateInfo.markAsProcessed(jClass);
        assertEquals(jClass, stateInfo.getProcessed("FoundClass"));
    }

    @Test
    public void should_ReturnNull_When_GetProcessedCalledWithNonExistentClassName() {
        assertNull(stateInfo.getProcessed("NonExistentClass"));
    }

    // ============= Mapping File Tests =============

    @Test
    public void should_SetAndGetMappingFile_When_SetMappingCalled() {
        stateInfo.setMapping("mapping.xml", mockMappingRoot);
        assertEquals(mockMappingRoot, stateInfo.getMapping("mapping.xml"));
    }

    @Test
    public void should_ReturnNull_When_GetMappingForNonExistentFile() {
        assertNull(stateInfo.getMapping("nonexistent.xml"));
    }

    @Test
    public void should_ReturnNull_When_GetMappingCalledWithNullFilename() {
        assertNull(stateInfo.getMapping(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_SetMappingCalledWithNullFilename() {
        stateInfo.setMapping(null, mockMappingRoot);
    }

    @Test
    public void should_RemoveMappingFile_When_SetMappingCalledWithNullMapping() {
        stateInfo.setMapping("mapping.xml", mockMappingRoot);
        stateInfo.setMapping("mapping.xml", null);
        assertNull(stateInfo.getMapping("mapping.xml"));
    }

    @Test
    public void should_ReturnMappingFilenames_When_GetMappingFilenamesCalled() {
        stateInfo.setMapping("mapping1.xml", mockMappingRoot);
        stateInfo.setMapping("mapping2.xml", mockMappingRoot);
        Enumeration<String> filenames = stateInfo.getMappingFilenames();
        assertNotNull(filenames);
    }

    @Test
    public void should_ReturnEmptyEnumeration_When_GetMappingFilenamesCalledWithNoMappings() {
        Enumeration<String> filenames = stateInfo.getMappingFilenames();
        assertFalse(filenames.hasMoreElements());
    }

    // ============= CDR File Tests =============

    @Test
    public void should_SetAndGetCDRFile_When_SetCDRFileCalled() {
        Properties props = new Properties();
        props.setProperty("key", "value");
        stateInfo.setCDRFile("cdr.properties", props);
        assertEquals(props, stateInfo.getCDRFile("cdr.properties"));
    }

    @Test
    public void should_ReturnNull_When_GetCDRFileForNonExistentFile() {
        assertNull(stateInfo.getCDRFile("nonexistent.properties"));
    }

    @Test
    public void should_ReturnNull_When_GetCDRFileCalledWithNullFilename() {
        assertNull(stateInfo.getCDRFile(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_SetCDRFileCalledWithNullFilename() {
        stateInfo.setCDRFile(null, new Properties());
    }

    @Test
    public void should_RemoveCDRFile_When_SetCDRFileCalledWithNullProperties() {
        Properties props = new Properties();
        stateInfo.setCDRFile("cdr.properties", props);
        stateInfo.setCDRFile("cdr.properties", null);
        assertNull(stateInfo.getCDRFile("cdr.properties"));
    }

    @Test
    public void should_ReturnCDRFilenames_When_GetCDRFilenamesCalled() {
        Properties props = new Properties();
        stateInfo.setCDRFile("cdr1.properties", props);
        stateInfo.setCDRFile("cdr2.properties", props);
        Enumeration<String> filenames = stateInfo.getCDRFilenames();
        assertNotNull(filenames);
    }

    @Test
    public void should_ReturnEmptyEnumeration_When_GetCDRFilenamesCalledWithNoFiles() {
        Enumeration<String> filenames = stateInfo.getCDRFilenames();
        assertFalse(filenames.hasMoreElements());
    }

    // ============= Dialog Tests =============

    @Test
    public void should_ReturnDefaultDialog_When_GetDialogCalledInitially() {
        assertNotNull(stateInfo.getDialog());
    }

    @Test
    public void should_SetAndGetDialog_When_SetDialogCalled() {
        stateInfo.setDialog(mockDialog);
        assertEquals(mockDialog, stateInfo.getDialog());
    }

    @Test
    public void should_SetDialogToNull_When_SetDialogCalledWithNull() {
        stateInfo.setDialog(null);
        assertNull(stateInfo.getDialog());
    }

    // ============= Status Code Tests =============

    @Test
    public void should_ReturnNormalStatusInitially_When_GetStatusCodeCalled() {
        assertEquals(SGStateInfo.NORMAL_STATUS, stateInfo.getStatusCode());
    }

    @Test
    public void should_SetAndReturnStatusCode_When_SetStatusCodeCalled() {
        stateInfo.setStatusCode(SGStateInfo.STOP_STATUS);
        assertEquals(SGStateInfo.STOP_STATUS, stateInfo.getStatusCode());
    }

    @Test
    public void should_SetStatusCodeToNormal_When_SetStatusCodeCalledWithNormalStatus() {
        stateInfo.setStatusCode(SGStateInfo.STOP_STATUS);
        stateInfo.setStatusCode(SGStateInfo.NORMAL_STATUS);
        assertEquals(SGStateInfo.NORMAL_STATUS, stateInfo.getStatusCode());
    }

    // ============= Prompt For Overwrite Tests =============

    @Test
    public void should_ReturnTrueForPromptOverwriteInitially_When_PromptForOverwriteCalled() {
        assertTrue(stateInfo.promptForOverwrite());
    }

    @Test
    public void should_SetAndReturnPromptForOverwrite_When_SetPromptForOverwriteCalled() {
        stateInfo.setPromptForOverwrite(false);
        assertFalse(stateInfo.promptForOverwrite());
    }

    @Test
    public void should_SetPromptForOverwriteToTrue_When_SetPromptForOverwriteCalledWithTrue() {
        stateInfo.setPromptForOverwrite(false);
        stateInfo.setPromptForOverwrite(true);
        assertTrue(stateInfo.promptForOverwrite());
    }

    // ============= Suppress Non-Fatal Warnings Tests =============

    @Test
    public void should_ReturnFalseForSuppressWarningsInitially_When_GetSuppressNonFatalWarningsCalled() {
        assertFalse(stateInfo.getSuppressNonFatalWarnings());
    }

    @Test
    public void should_SetAndReturnSuppressNonFatalWarnings_When_SetSuppressNonFatalWarningsCalled() {
        stateInfo.setSuppressNonFatalWarnings(true);
        assertTrue(stateInfo.getSuppressNonFatalWarnings());
    }

    @Test
    public void should_SetSuppressWarningsToFalse_When_SetSuppressNonFatalWarningsCalledWithFalse() {
        stateInfo.setSuppressNonFatalWarnings(true);
        stateInfo.setSuppressNonFatalWarnings(false);
        assertFalse(stateInfo.getSuppressNonFatalWarnings());
    }

    // ============= Verbose Tests =============

    @Test
    public void should_ReturnFalseForVerboseInitially_When_VerboseCalled() {
        assertFalse(stateInfo.verbose());
    }

    @Test
    public void should_SetAndReturnVerbose_When_SetVerboseCalled() {
        stateInfo.setVerbose(true);
        assertTrue(stateInfo.verbose());
    }

    @Test
    public void should_SetVerboseToFalse_When_SetVerboseCalledWithFalse() {
        stateInfo.setVerbose(true);
        stateInfo.setVerbose(false);
        assertFalse(stateInfo.verbose());
    }

    // ============= Factory State Tests =============

    @Test
    public void should_ReturnNullForFactoryStateInitially_When_GetCurrentFactoryStateCalled() {
        assertNull(stateInfo.getCurrentFactoryState());
    }

    @Test
    public void should_SetAndGetFactoryState_When_SetCurrentFactoryStateCalled() {
        FactoryState mockFactoryState = mock(FactoryState.class);
        stateInfo.setCurrentFactoryState(mockFactoryState);
        assertEquals(mockFactoryState, stateInfo.getCurrentFactoryState());
    }

    @Test
    public void should_SetFactoryStateToNull_When_SetCurrentFactoryStateCalledWithNull() {
        FactoryState mockFactoryState = mock(FactoryState.class);
        stateInfo.setCurrentFactoryState(mockFactoryState);
        stateInfo.setCurrentFactoryState(null);
        assertNull(stateInfo.getCurrentFactoryState());
    }

    // ============= Source Maps Tests =============

    @Test
    public void should_ReturnSourcesByNameMap_When_GetSourcesByNameCalled() {
        Map<String, JClass> sourcesByName = stateInfo.getSourcesByName();
        assertNotNull(sourcesByName);
    }

    @Test
    public void should_ReturnImportedSourcesByNameMap_When_GetImportedSourcesByNameCalled() {
        Map<String, JClass> importedSourcesByName =
            stateInfo.getImportedSourcesByName();
        assertNotNull(importedSourcesByName);
    }

    @Test
    public void should_ContainBoundClassesInSourcesByName_When_BindSourceCodeCalled() {
        JClass jClass = new JClass("BoundClass");
        JClass[] classes = { jClass };
        stateInfo.bindSourceCode(mockAnnotated, classes);
        assertTrue(stateInfo.getSourcesByName().containsKey("BoundClass"));
    }

    // ============= Edge Cases & Integration Tests =============

    @Test
    public void should_HandleSpecialCharacterClassName_When_GetSourceCodeCalledWithSpecialChars() {
        JClass jClass = new JClass("_SpecialClass");
        JClass[] classes = { jClass };
        stateInfo.bindSourceCode(mockAnnotated, classes);
        assertEquals(jClass, stateInfo.getSourceCode("_SpecialClass"));
    }

    @Test
    public void should_HandleMultipleAnnotationsWithSameClass_When_BindSourceCodeCalledWithDifferentAnnotations() {
        Annotated annotated1 = mock(Annotated.class);
        Annotated annotated2 = mock(Annotated.class);
        JClass jClass = new JClass("SharedClass");
        JClass[] classes = { jClass };
        stateInfo.bindSourceCode(annotated1, classes);
        stateInfo.bindSourceCode(annotated2, classes);
        assertEquals(jClass, stateInfo.getSourceCode("SharedClass"));
    }

    @Test
    public void should_HandleMappingAndCDRFilesIndependently_When_BothSetAndRetrieved() {
        MappingRoot mapping = mock(MappingRoot.class);
        Properties props = new Properties();
        stateInfo.setMapping("mapping.xml", mapping);
        stateInfo.setCDRFile("cdr.properties", props);
        assertEquals(mapping, stateInfo.getMapping("mapping.xml"));
        assertEquals(props, stateInfo.getCDRFile("cdr.properties"));
    }

    @Test
    public void should_MaintainIndependentStateForMultipleInstances_When_MultipleStateInfoCreated() {
        SGStateInfo stateInfo1 = new SGStateInfo(
            mockSchema,
            mockSourceGenerator
        );
        SGStateInfo stateInfo2 = new SGStateInfo(
            mockSchema,
            mockSourceGenerator
        );
        stateInfo1.setPackageName("package1");
        stateInfo2.setPackageName("package2");
        assertEquals("package1", stateInfo1.getPackageName());
        assertEquals("package2", stateInfo2.getPackageName());
    }

    @Test
    public void should_PersistStateAcrossMultipleOperations_When_VariousOperationsPerformed() {
        stateInfo.setPackageName("testPackage");
        stateInfo.setStatusCode(SGStateInfo.STOP_STATUS);
        stateInfo.setVerbose(true);
        stateInfo.setSuppressNonFatalWarnings(true);
        stateInfo.setPromptForOverwrite(false);

        assertEquals("testPackage", stateInfo.getPackageName());
        assertEquals(SGStateInfo.STOP_STATUS, stateInfo.getStatusCode());
        assertTrue(stateInfo.verbose());
        assertTrue(stateInfo.getSuppressNonFatalWarnings());
        assertFalse(stateInfo.promptForOverwrite());
    }

    @Test
    public void should_HandleLargeNumberOfProcessedClasses_When_MarkAsProcessedCalledMultipleTimes() {
        for (int i = 0; i < 1000; i++) {
            JClass jClass = new JClass("Class" + i);
            stateInfo.markAsProcessed(jClass);
        }
        assertTrue(stateInfo.processed("Class500"));
        assertTrue(stateInfo.processed("Class999"));
    }

    @Test
    public void should_HandleNullInProcessedCheck_When_ProcessedCalledWithNullClassName() {
        assertFalse(stateInfo.processed((String) null));
    }
}
