package org.exolab.castor.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Properties;
import org.castor.xml.JavaNaming;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuilderConfigurationTest {

    private BuilderConfiguration config;

    @Mock
    private JavaNaming mockJavaNaming;

    @Mock
    private AnnotationBuilder mockAnnotationBuilder;

    @Before
    public void setUp() {
        config = new BuilderConfiguration();
    }

    @Test
    public void should_CreateDefaultConfiguration_When_NoArgsConstructor() {
        assertNotNull(config);
        assertNotNull(config.getDefault());
    }

    @Test
    public void should_ReturnNotNull_When_GetDefaultCalledTwice() {
        Properties props1 = config.getDefault();
        Properties props2 = config.getDefault();
        assertNotNull(props1);
        assertNotNull(props2);
        assertSame(props1, props2);
    }

    @Test
    public void should_ReturnPropertyValue_When_GetPropertyCalled() {
        String value = config.getProperty(
            "org.exolab.castor.builder.boundproperties",
            "default"
        );
        assertNotNull(value);
    }

    @Test
    public void should_ReturnDefaultValue_When_PropertyNotExists() {
        String value = config.getProperty("nonexistent.property", "mydefault");
        assertEquals("mydefault", value);
    }

    @Test
    public void should_ReturnFalse_When_BoundPropertiesNotEnabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.boundPropertiesEnabled());
    }

    @Test
    public void should_ReturnTrue_When_BoundPropertiesEnabled() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.boundproperties", "true");
        config.setDefaultProperties(props);
        assertTrue(config.boundPropertiesEnabled());
    }

    @Test
    public void should_HandleCaseInsensitive_When_BoundPropertiesCheckingCase() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.boundproperties", "TRUE");
        config.setDefaultProperties(props);
        assertTrue(config.boundPropertiesEnabled());
    }

    @Test
    public void should_ReturnTrue_When_EqualsMethodEnabled() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.equalsmethod", "true");
        config.setDefaultProperties(props);
        assertTrue(config.equalsMethod());
    }

    @Test
    public void should_ReturnFalse_When_EqualsMethodDisabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.equalsMethod());
    }

    @Test
    public void should_SetEqualsMethodProperty_When_SetEqualMethodCalled() {
        config.setEqualsMethod(true);
        assertTrue(config.equalsMethod());
        config.setEqualsMethod(false);
        assertFalse(config.equalsMethod());
    }

    @Test
    public void should_ReturnTrue_When_ClassDescFieldNamesEnabled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.classdescfieldnames",
            "true"
        );
        config.setDefaultProperties(props);
        assertTrue(config.classDescFieldNames());
    }

    @Test
    public void should_ReturnFalse_When_ClassDescFieldNamesDisabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.classDescFieldNames());
    }

    @Test
    public void should_SetClassDescFieldNamesProperty_When_SetClassDescFieldNamesCalled() {
        config.setClassDescFieldNames(true);
        assertTrue(config.classDescFieldNames());
        config.setClassDescFieldNames(false);
        assertFalse(config.classDescFieldNames());
    }

    @Test
    public void should_ReturnTrue_When_ExtraCollectionMethodsEnabled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.extraCollectionMethods",
            "true"
        );
        config.setDefaultProperties(props);
        assertTrue(config.generateExtraCollectionMethods());
    }

    @Test
    public void should_ReturnFalse_When_ExtraCollectionMethodsDisabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.generateExtraCollectionMethods());
    }

    @Test
    public void should_ReturnTrue_When_PrimitiveWrapperEnabled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.primitivetowrapper",
            "true"
        );
        config.setDefaultProperties(props);
        assertTrue(config.usePrimitiveWrapper());
    }

    @Test
    public void should_ReturnFalse_When_PrimitiveWrapperDisabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.usePrimitiveWrapper());
    }

    @Test
    public void should_SetPrimitiveWrapperProperty_When_SetPrimitiveWrapperCalled() {
        config.setPrimitiveWrapper(true);
        assertTrue(config.usePrimitiveWrapper());
        config.setPrimitiveWrapper(false);
        assertFalse(config.usePrimitiveWrapper());
    }

    @Test
    public void should_ReturnTrue_When_EnumeratedTypeInterfaceEnabled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.enumTypeAccessInterface",
            "true"
        );
        config.setDefaultProperties(props);
        assertTrue(config.useEnumeratedTypeInterface());
    }

    @Test
    public void should_ReturnFalse_When_EnumeratedTypeInterfaceDisabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.useEnumeratedTypeInterface());
    }

    @Test
    public void should_SetEnumeratedTypeInterfaceProperty_When_SetUseEnumeratedTypeInterfaceCalled() {
        config.setUseEnumeratedTypeInterface(true);
        assertTrue(config.useEnumeratedTypeInterface());
        config.setUseEnumeratedTypeInterface(false);
        assertFalse(config.useEnumeratedTypeInterface());
    }

    @Test
    public void should_ReturnFalse_When_Java50NotSet() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.useJava50());
    }

    @Test
    public void should_ReturnTrue_When_Java50Set() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.javaVersion", "5.0");
        config.setDefaultProperties(props);
        assertTrue(config.useJava50());
    }

    @Test
    public void should_SetJava50Property_When_ForceUseJava50Called() {
        config.forceUseJava50();
        assertTrue(config.useJava50());
    }

    @Test
    public void should_ReturnFalse_When_Java5EnumsNotConfigured() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.javaVersion", "1.4");
        config.setDefaultProperties(props);
        assertFalse(config.useJava5Enums());
    }

    @Test
    public void should_ReturnTrue_When_Java5EnumsEnabled() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.javaVersion", "5.0");
        props.setProperty("org.exolab.castor.builder.forceJava4Enums", "false");
        config.setDefaultProperties(props);
        assertTrue(config.useJava5Enums());
    }

    @Test
    public void should_ReturnFalse_When_Java4EnumsForced() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.javaVersion", "5.0");
        props.setProperty("org.exolab.castor.builder.forceJava4Enums", "true");
        config.setDefaultProperties(props);
        assertFalse(config.useJava5Enums());
    }

    @Test
    public void should_ReturnTrue_When_ExtraDocumentationMethodsEnabled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.extraDocumentationMethods",
            "true"
        );
        config.setDefaultProperties(props);
        assertTrue(config.generateExtraDocumentationMethods());
    }

    @Test
    public void should_ReturnFalse_When_ExtraDocumentationMethodsDisabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.generateExtraDocumentationMethods());
    }

    @Test
    public void should_ReturnTrue_When_CycleBreakerEnabled() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.useCycleBreaker", "true");
        config.setDefaultProperties(props);
        assertTrue(config.useCycleBreaker());
    }

    @Test
    public void should_ReturnTrue_When_CycleBreakerNotSet() {
        config.setDefaultProperties(new Properties());
        assertTrue(config.useCycleBreaker());
    }

    @Test
    public void should_ReturnFalse_When_CycleBreakerDisabled() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.useCycleBreaker", "false");
        config.setDefaultProperties(props);
        assertFalse(config.useCycleBreaker());
    }

    @Test
    public void should_ReturnDefaultValue_When_MaxConstantsNotSet() {
        config.setDefaultProperties(new Properties());
        assertEquals(1000, config.getMaximumNumberOfConstants());
    }

    @Test
    public void should_ReturnCustomValue_When_MaxConstantsSet() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.maxNumberOfConstants",
            "500"
        );
        config.setDefaultProperties(props);
        assertEquals(500, config.getMaximumNumberOfConstants());
    }

    @Test
    public void should_ReturnTrue_When_MappingSchemaElement() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.javaclassmapping",
            "element"
        );
        config.setDefaultProperties(props);
        assertTrue(config.mappingSchemaElement2Java());
        assertFalse(config.mappingSchemaType2Java());
    }

    @Test
    public void should_ReturnTrue_When_MappingSchemaType() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.javaclassmapping", "type");
        config.setDefaultProperties(props);
        assertTrue(config.mappingSchemaType2Java());
        assertFalse(config.mappingSchemaElement2Java());
    }

    @Test
    public void should_ReturnFalse_When_NoMappingSet() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.mappingSchemaElement2Java());
        assertFalse(config.mappingSchemaType2Java());
    }

    @Test
    public void should_SetDefaultPropertiesWithNull_When_SetDefaultPropertiesCalledWithNull() {
        config.setDefaultProperties(null);
        assertNotNull(config.getDefault());
    }

    @Test
    public void should_MergeProperties_When_SetDefaultPropertiesCalledWithProperties() {
        Properties props = new Properties();
        props.setProperty("testkey", "testvalue");
        config.setDefaultProperties(props);
        String value = config.getProperty("testkey", "");
        assertEquals("testvalue", value);
    }

    @Test
    public void should_SetNamespacePackageMapping_When_SetNamespacePackageMappingCalled() {
        config.setNamespacePackageMapping("http://example.com", "com.example");
        String result = config.lookupPackageByNamespace("http://example.com");
        assertEquals("com.example", result);
    }

    @Test
    public void should_ReturnEmptyString_When_NamespaceNotFound() {
        config.setDefaultProperties(new Properties());
        String result = config.lookupPackageByNamespace(
            "http://nonexistent.com"
        );
        assertEquals("", result);
    }

    @Test
    public void should_ReturnEmptyString_When_LookupNamespaceWithNull() {
        config.setDefaultProperties(new Properties());
        String result = config.lookupPackageByNamespace(null);
        assertEquals("", result);
    }

    @Test
    public void should_SetLocationPackageMapping_When_SetLocationPackageMappingCalled() {
        config.setLocationPackageMapping("schema.xsd", "com.schema");
        String result = config.lookupPackageByLocation("schema.xsd");
        assertEquals("com.schema", result);
    }

    @Test
    public void should_ReturnEmptyString_When_LocationNotFound() {
        config.setDefaultProperties(new Properties());
        String result = config.lookupPackageByLocation("nonexistent.xsd");
        assertEquals("", result);
    }

    @Test
    public void should_ReturnEmptyString_When_LookupLocationWithNull() {
        config.setDefaultProperties(new Properties());
        String result = config.lookupPackageByLocation(null);
        assertEquals("", result);
    }

    @Test
    public void should_MatchEndingPath_When_LookupLocationWithRelativePath() {
        config.setLocationPackageMapping("schema.xsd", "com.schema");
        String result = config.lookupPackageByLocation("./subdir/schema.xsd");
        assertEquals("com.schema", result);
    }

    @Test
    public void should_MatchParentPath_When_LookupLocationWithParentDirectory() {
        config.setLocationPackageMapping("schema.xsd", "com.schema");
        String result = config.lookupPackageByLocation("../schema.xsd");
        assertEquals("com.schema", result);
    }

    @Test
    public void should_ProcessNamespaceMappings_When_ValidMappingString() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.xml.nspackages",
            "http://example.com=com.example,http://test.com=com.test"
        );
        config.setDefaultProperties(props);
        assertEquals(
            "com.example",
            config.lookupPackageByNamespace("http://example.com")
        );
        assertEquals(
            "com.test",
            config.lookupPackageByNamespace("http://test.com")
        );
    }

    @Test
    public void should_IgnoreInvalidMappings_When_MappingStringMalformed() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.xml.nspackages",
            "invalidmapping,http://example.com=com.example"
        );
        config.setDefaultProperties(props);
        assertEquals(
            "com.example",
            config.lookupPackageByNamespace("http://example.com")
        );
    }

    @Test
    public void should_ReturnTrue_When_AutomaticConflictResolutionEnabled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.automaticConflictResolution",
            "true"
        );
        config.setDefaultProperties(props);
        assertTrue(config.isAutomaticConflictResolution());
    }

    @Test
    public void should_ReturnFalse_When_AutomaticConflictResolutionDisabled() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.isAutomaticConflictResolution());
    }

    @Test
    public void should_ReturnTypeSuffix_When_GetAutomaticConflictResolutionTypeSuffixCalled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.automaticConflictResolutionTypeSuffix",
            "ByType"
        );
        config.setDefaultProperties(props);
        assertEquals(
            "ByType",
            config.getAutomaticConflictResolutionTypeSuffix()
        );
    }

    @Test
    public void should_ReturnEmptyString_When_TypeSuffixNotSet() {
        config.setDefaultProperties(new Properties());
        assertEquals("", config.getAutomaticConflictResolutionTypeSuffix());
    }

    @Test
    public void should_SetJavaNaming_When_SetJavaNamingCalled() {
        config.setJavaNaming(mockJavaNaming);
        assertSame(mockJavaNaming, config.getJavaNaming());
    }

    @Test
    public void should_ReturnNull_When_JavaNamingNotSet() {
        config.setDefaultProperties(new Properties());
        config.setJavaNaming(mockJavaNaming);
        assertNotNull(config.getJavaNaming());
    }

    @Test
    public void should_AddAnnotationBuilder_When_AddAnnotationBuilderCalled() {
        config.addAnnotationBuilder(mockAnnotationBuilder);
        AnnotationBuilder[] builders = config.getAnnotationBuilders();
        assertTrue(builders.length > 0);
        assertTrue(containsBuilder(builders, mockAnnotationBuilder));
    }

    @Test
    public void should_ReturnMultipleAnnotationBuilders_When_MultipleAdded() {
        AnnotationBuilder mock1 = mock(AnnotationBuilder.class);
        AnnotationBuilder mock2 = mock(AnnotationBuilder.class);
        config.addAnnotationBuilder(mock1);
        config.addAnnotationBuilder(mock2);
        AnnotationBuilder[] builders = config.getAnnotationBuilders();
        assertEquals(2, builders.length);
    }

    @Test
    public void should_ReturnJClassPrinterFactories_When_GetJClassPrinterFactoriesCalled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.jclassPrinterFactories",
            "factory1,factory2"
        );
        config.setDefaultProperties(props);
        String factories = config.getJClassPrinterFactories();
        assertEquals("factory1,factory2", factories);
    }

    @Test
    public void should_ReturnNull_When_JClassPrinterFactoriesNotSet() {
        config.setDefaultProperties(new Properties());
        String factories = config.getJClassPrinterFactories();
        assertNull(factories);
    }

    @Test
    public void should_ReturnTrue_When_OldFieldNamingEnabled() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.field-naming.old", "true");
        config.setDefaultProperties(props);
        assertTrue(config.useOldFieldNaming());
    }

    @Test
    public void should_ReturnFalse_When_OldFieldNamingDisabled() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.field-naming.old",
            "false"
        );
        config.setDefaultProperties(props);
        assertFalse(config.useOldFieldNaming());
    }

    @Test
    public void should_ReturnFalse_When_OldFieldNamingNotSet() {
        config.setDefaultProperties(new Properties());
        assertFalse(config.useOldFieldNaming());
    }

    @Test
    public void should_HandleCaseInsensitiveOldFieldNaming_When_CheckingProperty() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.field-naming.old", "TRUE");
        config.setDefaultProperties(props);
        assertTrue(config.useOldFieldNaming());
    }

    @Test
    public void should_LoadPropertiesFromResource_When_LoadPropertiesCalled() {
        Properties props = BuilderConfiguration.loadProperties(
            "/org/exolab/castor/builder/castorbuilder.properties",
            "castorbuilder.properties"
        );
        assertNotNull(props);
    }

    @Test(expected = RuntimeException.class)
    public void should_ThrowException_When_ResourceNotFound() {
        BuilderConfiguration.loadProperties(
            "/nonexistent/resource.properties",
            "nonexistent.properties"
        );
    }

    @Test
    public void should_SynchronizedGetDefault_When_CalledConcurrently()
        throws InterruptedException {
        BuilderConfiguration config1 = new BuilderConfiguration();
        Properties props1 = config1.getDefault();
        Properties props2 = config1.getDefault();
        assertSame(props1, props2);
    }

    @Test
    public void should_HandleEmptyPropertyValue_When_PropertyEmpty() {
        Properties props = new Properties();
        props.setProperty("org.exolab.castor.builder.boundproperties", "");
        config.setDefaultProperties(props);
        assertFalse(config.boundPropertiesEnabled());
    }

    @Test
    public void should_ReturnEmptyStringArray_When_NoAnnotationBuildersAdded() {
        AnnotationBuilder[] builders = config.getAnnotationBuilders();
        assertNotNull(builders);
    }

    @Test
    public void should_TrimWhitespaceInMappings_When_ProcessingNamespaceMappings() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.xml.nspackages",
            "  http://example.com  =  com.example  "
        );
        config.setDefaultProperties(props);
        assertEquals(
            "com.example",
            config.lookupPackageByNamespace("http://example.com")
        );
    }

    @Test
    public void should_AllowMultipleMappingsWithSameName_When_SettingDifferentNamespaces() {
        config.setNamespacePackageMapping("ns1", "pkg1");
        config.setNamespacePackageMapping("ns2", "pkg2");
        assertEquals("pkg1", config.lookupPackageByNamespace("ns1"));
        assertEquals("pkg2", config.lookupPackageByNamespace("ns2"));
    }

    @Test
    public void should_OverwritePreviousMapping_When_SettingSameNamespaceTwice() {
        config.setNamespacePackageMapping("ns1", "pkg1");
        config.setNamespacePackageMapping("ns1", "pkg2");
        assertEquals("pkg2", config.lookupPackageByNamespace("ns1"));
    }

    @Test
    public void should_ProcessOldNamespaceProperty_When_LegacyPropertyUsed() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.nspackages",
            "http://example.com=com.example"
        );
        config.setDefaultProperties(props);
        assertEquals(
            "com.example",
            config.lookupPackageByNamespace("http://example.com")
        );
    }

    @Test
    public void should_PreferNewNamespaceProperty_When_BothPropertiesSet() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.nspackages",
            "http://example.com=com.old"
        );
        props.setProperty(
            "org.exolab.castor.xml.nspackages",
            "http://example.com=com.new"
        );
        config.setDefaultProperties(props);
        String result = config.lookupPackageByNamespace("http://example.com");
        assertNotNull(result);
    }

    @Test
    public void should_ReturnCorrectTypeForMaxConstants_When_ParsedAsInteger() {
        Properties props = new Properties();
        props.setProperty(
            "org.exolab.castor.builder.maxNumberOfConstants",
            "2000"
        );
        config.setDefaultProperties(props);
        int value = config.getMaximumNumberOfConstants();
        assertTrue(value > 0);
        assertEquals(2000, value);
    }

    private boolean containsBuilder(
        AnnotationBuilder[] builders,
        AnnotationBuilder target
    ) {
        for (AnnotationBuilder builder : builders) {
            if (builder == target) {
                return true;
            }
        }
        return false;
    }
}
