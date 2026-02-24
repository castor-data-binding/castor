package org.exolab.castor.builder.descriptors;

import static org.junit.Assert.*;

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.xml.XMLConstants;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JField;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JSourceCode;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for DescriptorJClass targeting >95% coverage.
 */
public class DescriptorJClassTest {

    private BuilderConfiguration config;
    private DescriptorJClass descriptorJClass;

    @Before
    public void setUp() {
        config = new BuilderConfiguration();
    }

    // Test 1: Basic constructor
    @Test
    public void should_ConstructDescriptorJClass_When_ValidParametersProvided() {
        JClass typeClass = new JClass("org.example.TestType");
        descriptorJClass = new DescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(descriptorJClass);
        assertEquals("TestDescriptor", descriptorJClass.getLocalName());
    }

    // Test 2: With different package
    @Test
    public void should_ConstructDescriptorJClass_When_ValidTypeWithPackage() {
        JClass type = new JClass("com.mycompany.MyClass");
        descriptorJClass = new DescriptorJClass(
            config,
            "MyClassDescriptor",
            type
        );

        assertNotNull(descriptorJClass);
        assertEquals("MyClassDescriptor", descriptorJClass.getLocalName());
    }

    // Test 3: Type without package
    @Test
    public void should_ConstructDescriptorJClass_When_TypeWithoutPackage() {
        JClass typeClass = new JClass("SimpleClass");
        descriptorJClass = new DescriptorJClass(
            config,
            "SimpleDescriptor",
            typeClass
        );

        assertNotNull(descriptorJClass);
        assertEquals("SimpleDescriptor", descriptorJClass.getLocalName());
    }

    // Test 4: Superclass defaults to XMLClassDescriptorImpl
    @Test
    public void should_SetSuperClassToXMLClassDescriptorImpl_When_NoSuperClass() {
        JClass type = new JClass("org.test.TestClass");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertTrue(
            "Should extend XMLClassDescriptorImpl",
            descriptorJClass
                .getSuperClassQualifiedName()
                .contains("XMLClassDescriptorImpl")
        );
    }

    // Test 5: Extended superclass handling
    @Test
    public void should_UseSuperClassName_When_TypeHasExtendedSuperClass() {
        JClass type = new JClass("org.extended.ExtendedClass");
        type.setSuperClass("org.base.BaseClass");

        descriptorJClass = new DescriptorJClass(
            config,
            "ExtendedDescriptor",
            type
        );
        String superClassName = descriptorJClass.getSuperClassQualifiedName();

        assertTrue(
            "Should contain descriptor suffix",
            superClassName.contains("Descriptor")
        );
    }

    // Test 6: Different package superclass
    @Test
    public void should_HandleSuperClassWithDifferentPackage() {
        JClass type = new JClass("org.myapp.MyClass");
        type.setSuperClass("org.parent.ParentClass");

        descriptorJClass = new DescriptorJClass(
            config,
            "MyClassDescriptor",
            type
        );

        assertNotNull(descriptorJClass);
        String superClassName = descriptorJClass.getSuperClassQualifiedName();
        assertNotNull(superClassName);
    }

    // Test 7: Required fields count
    @Test
    public void should_HaveRequiredFields_When_DescriptorInitialized() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        int fieldCount = descriptorJClass.getFieldCount();
        assertEquals("Should have exactly 5 fields", 5, fieldCount);
    }

    // Test 8: Field names verification
    @Test
    public void should_HaveFieldsWithCorrectNames() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        JField[] fields = descriptorJClass.getFields();
        boolean hasElementDef = false;
        boolean hasNsPrefix = false;
        boolean hasNsURI = false;
        boolean hasXmlName = false;
        boolean hasIdentity = false;

        for (JField field : fields) {
            String name = field.getName();
            if ("_elementDefinition".equals(name)) hasElementDef = true;
            if ("_nsPrefix".equals(name)) hasNsPrefix = true;
            if ("_nsURI".equals(name)) hasNsURI = true;
            if ("_xmlName".equals(name)) hasXmlName = true;
            if ("_identity".equals(name)) hasIdentity = true;
        }

        assertTrue("Should have _elementDefinition", hasElementDef);
        assertTrue("Should have _nsPrefix", hasNsPrefix);
        assertTrue("Should have _nsURI", hasNsURI);
        assertTrue("Should have _xmlName", hasXmlName);
        assertTrue("Should have _identity", hasIdentity);
    }

    // Test 9: Constructor exists
    @Test
    public void should_HaveDefaultConstructor_When_DescriptorInitialized() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertTrue(
            "Should have at least one constructor",
            descriptorJClass.getConstructors().length >= 1
        );
        JConstructor constructor = descriptorJClass.getConstructor(0);
        assertNotNull("Constructor should exist", constructor);
    }

    // Test 10: Constructor has source code
    @Test
    public void should_HaveConstructorWithSourceCode() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        JConstructor constructor = descriptorJClass.getConstructor(0);
        assertNotNull("Constructor should exist", constructor);
        JSourceCode code = constructor.getSourceCode();
        assertNotNull("Constructor should have source code", code);
    }

    // Test 11: Required methods exist
    @Test
    public void should_HaveAllRequiredMethods() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        JMethod[] methods = descriptorJClass.getMethods();
        boolean hasIsElementDefinition = false;
        boolean hasGetNameSpacePrefix = false;
        boolean hasGetNameSpaceURI = false;
        boolean hasGetValidator = false;
        boolean hasGetXMLName = false;
        boolean hasGetAccessMode = false;
        boolean hasGetIdentity = false;
        boolean hasGetJavaClass = false;

        for (JMethod method : methods) {
            String name = method.getName();
            if ("isElementDefinition".equals(name)) hasIsElementDefinition =
                true;
            if ("getNameSpacePrefix".equals(name)) hasGetNameSpacePrefix = true;
            if ("getNameSpaceURI".equals(name)) hasGetNameSpaceURI = true;
            if ("getValidator".equals(name)) hasGetValidator = true;
            if ("getXMLName".equals(name)) hasGetXMLName = true;
            if ("getAccessMode".equals(name)) hasGetAccessMode = true;
            if ("getIdentity".equals(name)) hasGetIdentity = true;
            if ("getJavaClass".equals(name)) hasGetJavaClass = true;
        }

        assertTrue("Should have isElementDefinition", hasIsElementDefinition);
        assertTrue("Should have getNameSpacePrefix", hasGetNameSpacePrefix);
        assertTrue("Should have getNameSpaceURI", hasGetNameSpaceURI);
        assertTrue("Should have getValidator", hasGetValidator);
        assertTrue("Should have getXMLName", hasGetXMLName);
        assertTrue("Should have getAccessMode", hasGetAccessMode);
        assertTrue("Should have getIdentity", hasGetIdentity);
        assertTrue("Should have getJavaClass", hasGetJavaClass);
    }

    // Test 12: Method count
    @Test
    public void should_HaveMultipleMethods_When_DescriptorInitialized() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        int methodCount = descriptorJClass.getMethodCount();
        assertTrue("Should have at least 8 methods", methodCount >= 8);
    }

    // Test 13: Null package name
    @Test
    public void should_HandleNullPackageName() {
        JClass type = new JClass("TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);
        assertNotNull(descriptorJClass);
    }

    // Test 14: Complex package name
    @Test
    public void should_HandleValidPackageName() {
        JClass type = new JClass("com.example.model.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);
        assertNotNull(descriptorJClass);
    }

    // Test 15: Complete descriptor initialization
    @Test
    public void should_InitializeCompleteDescriptor_When_AllParametersValid() {
        JClass type = new JClass("org.complete.CompleteType");
        descriptorJClass = new DescriptorJClass(
            config,
            "CompleteDescriptor",
            type
        );

        assertTrue(
            "Should extend XMLClassDescriptorImpl",
            descriptorJClass
                .getSuperClassQualifiedName()
                .contains("XMLClassDescriptorImpl")
        );
        assertTrue("Should have fields", descriptorJClass.getFieldCount() >= 5);
        assertTrue(
            "Should have at least one constructor",
            descriptorJClass.getConstructors().length >= 1
        );
        assertTrue(
            "Should have methods",
            descriptorJClass.getMethodCount() >= 8
        );
    }

    // Test 16: Descriptor with extended class
    @Test
    public void should_InitializeDescriptorWithExtendedClass() {
        JClass type = new JClass("org.extended.ExtendedType");
        type.setSuperClass("org.base.BaseType");

        descriptorJClass = new DescriptorJClass(
            config,
            "ExtendedDescriptor",
            type
        );

        String superClassName = descriptorJClass.getSuperClassQualifiedName();
        assertTrue(
            "Should have descriptor suffix",
            superClassName.contains("Descriptor")
        );
        assertTrue(
            "Should have constructor",
            descriptorJClass.getConstructors().length >= 1
        );
    }

    // Test 17: Instance type check
    @Test
    public void should_BeInstanceOfJClass() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertTrue(
            "DescriptorJClass should be instance of JClass",
            descriptorJClass instanceof JClass
        );
    }

    // Test 18: Local name check
    @Test
    public void should_HaveCorrectClassName() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertEquals("TestDescriptor", descriptorJClass.getLocalName());
    }

    // Test 19: Method source code
    @Test
    public void should_VerifyMethodHasSourceCode() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        JMethod[] methods = descriptorJClass.getMethods();
        assertTrue("Should have methods", methods.length > 0);

        for (JMethod method : methods) {
            JSourceCode code = method.getSourceCode();
            assertNotNull(
                "Method should have source code: " + method.getName(),
                code
            );
        }
    }

    // Test 20: Descriptor suffix generation
    @Test
    public void should_GenerateSuperClassNameWithDescriptorSuffix() {
        JClass type = new JClass("org.custom.CustomType");
        type.setSuperClass("org.custom.base.CustomBase");

        descriptorJClass = new DescriptorJClass(
            config,
            "CustomDescriptor",
            type
        );
        String superClassName = descriptorJClass.getSuperClassQualifiedName();

        assertTrue(
            "Should contain Descriptor suffix",
            superClassName.contains(XMLConstants.DESCRIPTOR_SUFFIX)
        );
    }

    // Test 21: Different type names
    @Test
    public void should_WorkWithDifferentTypeNames() {
        JClass type1 = new JClass("org.test.FirstType");
        JClass descriptor1 = new DescriptorJClass(
            config,
            "FirstTypeDescriptor",
            type1
        );
        assertEquals("FirstTypeDescriptor", descriptor1.getLocalName());

        JClass type2 = new JClass("org.test.SecondType");
        JClass descriptor2 = new DescriptorJClass(
            config,
            "SecondTypeDescriptor",
            type2
        );
        assertEquals("SecondTypeDescriptor", descriptor2.getLocalName());
    }

    // Test 22: Exact field count
    @Test
    public void should_VerifyFieldCountMatches() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        int fieldCount = descriptorJClass.getFieldCount();
        assertEquals("Should have exactly 5 fields", 5, fieldCount);
    }

    // Test 23: Extends JClass
    @Test
    public void should_InstanceBeExtensionOfJClass() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertTrue(
            "Should be subclass of JClass",
            descriptorJClass instanceof JClass
        );
        assertNotNull(
            "Should have non-null superclass",
            descriptorJClass.getSuperClassQualifiedName()
        );
    }

    // Test 24: Multiple super class scenarios
    @Test
    public void should_HandleMultipleSuperClassScenarios() {
        JClass type1 = new JClass("org.test.Type1");
        DescriptorJClass desc1 = new DescriptorJClass(
            config,
            "Type1Descriptor",
            type1
        );
        assertNotNull(desc1);
        assertTrue(
            "Should extend XMLClassDescriptorImpl",
            desc1
                .getSuperClassQualifiedName()
                .contains("XMLClassDescriptorImpl")
        );

        JClass type2 = new JClass("org.test.Type2");
        type2.setSuperClass("org.parent.Parent");
        DescriptorJClass desc2 = new DescriptorJClass(
            config,
            "Type2Descriptor",
            type2
        );
        assertTrue(
            "Should contain Descriptor",
            desc2.getSuperClassQualifiedName().contains("Descriptor")
        );
    }

    // Test 25: Multiple instances
    @Test
    public void should_VerifyMultipleInstances() {
        JClass type1 = new JClass("org.app1.Class1");
        DescriptorJClass desc1 = new DescriptorJClass(config, "Desc1", type1);
        assertNotNull(desc1);

        JClass type2 = new JClass("org.app2.Class2");
        DescriptorJClass desc2 = new DescriptorJClass(config, "Desc2", type2);
        assertNotNull(desc2);

        assertNotEquals(
            "Should have different local names",
            desc1.getLocalName(),
            desc2.getLocalName()
        );
    }

    // Test 26: Complex package names
    @Test
    public void should_HandleComplexPackageNames() {
        JClass type = new JClass(
            "com.example.application.module.submodule.ComplexType"
        );
        descriptorJClass = new DescriptorJClass(
            config,
            "ComplexTypeDescriptor",
            type
        );

        assertNotNull(descriptorJClass);
        assertEquals("ComplexTypeDescriptor", descriptorJClass.getLocalName());
    }

    // Test 27: Field existence check
    @Test
    public void should_VerifyFieldsExist() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertNotNull(
            "Should have _elementDefinition field",
            descriptorJClass.getField("_elementDefinition")
        );
        assertNotNull(
            "Should have _nsPrefix field",
            descriptorJClass.getField("_nsPrefix")
        );
        assertNotNull(
            "Should have _nsURI field",
            descriptorJClass.getField("_nsURI")
        );
        assertNotNull(
            "Should have _xmlName field",
            descriptorJClass.getField("_xmlName")
        );
        assertNotNull(
            "Should have _identity field",
            descriptorJClass.getField("_identity")
        );
    }

    // Test 28: Constructor invocation
    @Test
    public void should_HaveInvokableConstructor() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertTrue(
            "Should have constructor",
            descriptorJClass.getConstructors().length > 0
        );
        JConstructor constructor = descriptorJClass.getConstructor(0);
        assertNotNull("Constructor should not be null", constructor);
    }

    // Test 29: Method count validation
    @Test
    public void should_HaveMinimumRequiredMethods() {
        JClass type = new JClass("org.test.TestType");
        descriptorJClass = new DescriptorJClass(config, "TestDescriptor", type);

        assertTrue(
            "Should have at least 8 methods",
            descriptorJClass.getMethodCount() >= 8
        );
    }

    // Test 30: Full initialization check
    @Test
    public void should_FullyInitializeDescriptor() {
        JClass type = new JClass("org.test.FullType");
        descriptorJClass = new DescriptorJClass(config, "FullDescriptor", type);

        assertNotNull("Descriptor should not be null", descriptorJClass);
        assertNotNull(
            "Superclass should not be null",
            descriptorJClass.getSuperClassQualifiedName()
        );
        assertTrue("Should have fields", descriptorJClass.getFieldCount() > 0);
        assertTrue(
            "Should have methods",
            descriptorJClass.getMethodCount() > 0
        );
        assertTrue(
            "Should have constructors",
            descriptorJClass.getConstructors().length > 0
        );
    }
}
