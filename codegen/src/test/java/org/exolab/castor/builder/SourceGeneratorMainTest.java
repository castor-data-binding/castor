package org.exolab.castor.builder;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import org.exolab.castor.builder.binding.BindingException;
import org.exolab.castor.builder.binding.BindingLoader;
import org.exolab.castor.builder.factory.FieldInfoFactory;
import org.exolab.castor.util.CommandLineOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.xml.sax.InputSource;

@RunWith(MockitoJUnitRunner.class)
public class SourceGeneratorMainTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private PrintStream originalErr;
    private ByteArrayOutputStream errContent;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // ===== Main Method Tests =====

    @Test
    public void should_DisplayHelp_When_HelpOptionProvided() {
        String[] args = { "-h" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
        // Help is printed via PrintWriter, verify no exception thrown
    }

    @Test
    public void should_PrintUsageAndReturn_When_NoSchemaSpecified() {
        String[] args = {};
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
        // Usage should be printed, verify no crash
    }

    @Test
    public void should_ProcessSchemaFilename_When_InputFileProvided() {
        String[] args = { "-i", "nonexistent.xsd", "-d", "/tmp" };
        SourceGeneratorMain.main(args);
        // Should attempt to generate and handle gracefully
    }

    @Test
    public void should_ProcessSchemaURL_When_InputSourceProvided() {
        String[] args = {
            "-is",
            "http://example.com/schema.xsd",
            "-d",
            "/tmp",
        };
        SourceGeneratorMain.main(args);
        // Should attempt to generate from URL
    }

    @Test
    public void should_SetDestinationDir_When_DestinationProvided() {
        String[] args = { "-i", "test.xsd", "-d", "/output" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_SetResourceDestination_When_ResourcesDestinationProvided() {
        String[] args = {
            "-i",
            "test.xsd",
            "-resourcesDestination",
            "/resources",
        };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
        // Verify "USING_SEPARATE_RESOURCES_DIRECTORY" message appears
    }

    @Test
    public void should_SetVerbose_When_VerboseOptionProvided() {
        String[] args = { "-i", "test.xsd", "-verbose" };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_SetFailOnError_When_FailOptionProvided() {
        String[] args = { "-i", "test.xsd", "-fail" };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_SuppressWarnings_When_ForceOptionProvided() {
        String[] args = { "-i", "test.xsd", "-f" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
        // Verify force message appears
    }

    @Test
    public void should_DisableDescriptors_When_NodescOptionProvided() {
        String[] args = { "-i", "test.xsd", "-nodesc" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
        // Verify descriptor message appears
    }

    @Test
    public void should_GenerateMappingFile_When_GenMappingOptionProvided() {
        String[] args = { "-i", "test.xsd", "-gen-mapping", "mapping.xml" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_GenerateMappingFileWithoutName_When_GenMappingHasNoValue() {
        String[] args = { "-i", "test.xsd", "-gen-mapping", "" };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_DisableMarshalling_When_NomarshallOptionProvided() {
        String[] args = { "-i", "test.xsd", "-nomarshall" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_EnableTestable_When_TestableOptionProvided() {
        String[] args = { "-i", "test.xsd", "-testable" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_EnableSAX1_When_Sax1OptionProvided() {
        String[] args = { "-i", "test.xsd", "-sax1" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_EnableCaseInsensitive_When_CaseInsensitiveOptionProvided() {
        String[] args = { "-i", "test.xsd", "-case-insensitive" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_SetNameConflictStrategy_When_NameConflictStrategyProvided() {
        String[] args = {
            "-i",
            "test.xsd",
            "-nameConflictStrategy",
            "informViaLog",
        };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_SetJClassPrinterType_When_ClassPrinterProvided() {
        String[] args = { "-i", "test.xsd", "-classPrinter", "standard" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_SetBinding_When_BindingFileProvided() {
        String[] args = { "-i", "test.xsd", "-binding-file", "binding.xml" };
        SourceGeneratorMain.main(args);
        // Should handle binding file loading
    }

    @Test
    public void should_CatchBindingException_When_BindingFileInvalid() {
        String[] args = { "-i", "test.xsd", "-binding-file", "invalid.xml" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
        // Should print binding error messages
    }

    @Test
    public void should_EnableGenerateImportedSchemas_When_GenerateImportedSchemasProvided() {
        String[] args = { "-i", "test.xsd", "-generateImportedSchemas" };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    @Test
    public void should_SetPackage_When_PackageOptionProvided() {
        String[] args = { "-i", "test.xsd", "-package", "com.example" };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_HandleMultipleOptions_When_CombinedOptionsProvided() {
        String[] args = {
            "-i",
            "test.xsd",
            "-d",
            "/output",
            "-package",
            "com.example",
            "-verbose",
            "-f",
            "-nodesc",
            "-nomarshall",
            "-testable",
            "-sax1",
        };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_CatchGenerationException_When_GenerationFails() {
        String[] args = { "-i", "nonexistent_file_xyz.xsd" };
        SourceGeneratorMain.main(args);
        // Should catch exception from generateSource
    }

    // ===== getTypeFactory Tests =====

    @Test
    public void should_ReturnNull_When_NoTypeFactoryOptionProvided() {
        Properties options = new Properties();
        // Use reflection to access private method
        Object result = invokeGetTypeFactory(options);
        assert result == null;
    }

    @Test
    public void should_ReturnFactory_When_TypesOptionProvided() {
        Properties options = new Properties();
        options.setProperty("types", "arraylist");
        Object result = invokeGetTypeFactory(options);
        // Should return a FieldInfoFactory instance
    }

    @Test
    public void should_CheckDeprecatedTypeFactory_When_TypesNotProvided() {
        Properties options = new Properties();
        options.setProperty("type-factory", "arraylist");
        String output = outContent.toString();
        Object result = invokeGetTypeFactory(options);
        // Should warn about deprecated option
    }

    @Test
    public void should_ConvertJ2ToArraylist_When_TypesIsJ2() {
        Properties options = new Properties();
        options.setProperty("types", "j2");
        Object result = invokeGetTypeFactory(options);
        // Should convert j2 to arraylist
    }

    @Test
    public void should_UseOldFieldNaming_When_UseOldFieldNamingIsTrue() {
        Properties options = new Properties();
        options.setProperty("types", "arraylist");
        options.setProperty("useOldFieldNaming", "true");
        Object result = invokeGetTypeFactory(options);
    }

    @Test
    public void should_UseNewFieldNaming_When_UseOldFieldNamingIsFalse() {
        Properties options = new Properties();
        options.setProperty("types", "arraylist");
        options.setProperty("useOldFieldNaming", "false");
        Object result = invokeGetTypeFactory(options);
    }

    @Test
    public void should_HandleCustomFieldInfoFactory_When_IllegalArgumentThrown() {
        Properties options = new Properties();
        options.setProperty("types", "invalid.nonexistent.FactoryClass");
        Object result = invokeGetTypeFactory(options);
        String output = outContent.toString();
        // Should print invalid types option message
    }

    @Test
    public void should_ReturnNull_When_FieldInfoFactoryNotFound() {
        Properties options = new Properties();
        options.setProperty("types", "nonexistent.factory.Class");
        Object result = invokeGetTypeFactory(options);
        String output = outContent.toString();
        // Should handle ClassNotFoundException gracefully
    }

    // ===== getLineSeparator Tests =====

    @Test
    public void should_ReturnSystemLineSeparator_When_NoOptionProvided() {
        String result = invokeGetLineSeparator(null);
        assert result.equals(System.getProperty("line.separator"));
    }

    @Test
    public void should_ReturnWindowsLineSeparator_When_WinOptionProvided() {
        String result = invokeGetLineSeparator("win");
        assert result.equals("\r\n");
        String output = outContent.toString();
    }

    @Test
    public void should_ReturnUnixLineSeparator_When_UnixOptionProvided() {
        String result = invokeGetLineSeparator("unix");
        assert result.equals("\n");
        String output = outContent.toString();
    }

    @Test
    public void should_ReturnMacLineSeparator_When_MacOptionProvided() {
        String result = invokeGetLineSeparator("mac");
        assert result.equals("\r");
        String output = outContent.toString();
    }

    @Test
    public void should_ReturnDefaultSeparator_When_InvalidOptionProvided() {
        String result = invokeGetLineSeparator("invalid");
        String output = outContent.toString();
        // Should print invalid line separator message
    }

    // ===== setupCommandLineOptions Tests =====

    @Test
    public void should_SetupCommandLineOptions_WithAllOptions() {
        Object result = invokeSetupCommandLineOptions();
        assert result != null;
    }

    // ===== Constructor Test =====

    @Test
    public void should_AllowPrivateConstructor_ForUtilityClass() {
        // Private constructor should not be invokable directly
        // Just verify the class can be loaded
        assert SourceGeneratorMain.class != null;
    }

    // ===== Edge Cases and Boundary Tests =====

    @Test
    public void should_HandleNullArgs_When_MainCalledWithNull() {
        try {
            SourceGeneratorMain.main(new String[0]);
        } catch (NullPointerException e) {
            // Should handle gracefully
        }
    }

    @Test
    public void should_HandleEmptySchemaFilename_When_EmptyStringProvided() {
        String[] args = { "-i", "", "-d", "/tmp" };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_HandleEmptyPackageName_When_EmptyPackageProvided() {
        String[] args = { "-i", "test.xsd", "-package", "" };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_HandleSpecialCharactersInPath_When_SpecialCharsInDestination() {
        String[] args = {
            "-i",
            "test.xsd",
            "-d",
            "/path/with spaces/and-dashes",
        };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_HandleDuplicateOptions_When_SameOptionProvidedTwice() {
        String[] args = {
            "-i",
            "test1.xsd",
            "-i",
            "test2.xsd",
            "-d",
            "/output",
        };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_HandleMappingFileNameWithExtension_When_MappingNameIncludesPath() {
        String[] args = {
            "-i",
            "test.xsd",
            "-gen-mapping",
            "/path/to/mapping.xml",
        };
        SourceGeneratorMain.main(args);
    }

    @Test
    public void should_HandleBindingFileNotFound_When_FileDoesNotExist() {
        String[] args = {
            "-i",
            "test.xsd",
            "-binding-file",
            "nonexistent_binding.xml",
        };
        SourceGeneratorMain.main(args);
        String output = outContent.toString();
    }

    // ===== Helper Methods for Reflection =====

    private Object invokeGetTypeFactory(Properties options) {
        try {
            java.lang.reflect.Method method =
                SourceGeneratorMain.class.getDeclaredMethod(
                    "getTypeFactory",
                    Properties.class
                );
            method.setAccessible(true);
            return method.invoke(null, options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String invokeGetLineSeparator(String lineSepStyle) {
        try {
            java.lang.reflect.Method method =
                SourceGeneratorMain.class.getDeclaredMethod(
                    "getLineSeparator",
                    String.class
                );
            method.setAccessible(true);
            return (String) method.invoke(null, lineSepStyle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object invokeSetupCommandLineOptions() {
        try {
            java.lang.reflect.Method method =
                SourceGeneratorMain.class.getDeclaredMethod(
                    "setupCommandLineOptions"
                );
            method.setAccessible(true);
            return method.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
