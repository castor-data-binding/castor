package org.exolab.castor.builder.descriptors;

import static org.junit.Assert.*;

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JPrimitiveType;
import org.exolab.javasource.JType;
import org.junit.Before;
import org.junit.Test;

public class DescriptorSourceFactoryTest {

    private DescriptorSourceFactory factory;
    private BuilderConfiguration config;

    @Before
    public void setUp() {
        config = new BuilderConfiguration();
        factory = new DescriptorSourceFactory(config);
    }

    // ==================== Constructor Tests ====================

    @Test
    public void shouldThrowIllegalArgumentException_When_ConfigIsNull() {
        try {
            new DescriptorSourceFactory(null);
            fail("Should throw IllegalArgumentException when config is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void shouldInitializeFactory_When_ConfigIsValid() {
        assertNotNull(factory);
    }

    @Test
    public void shouldCreateFactoryWithDefaultConfiguration() {
        BuilderConfiguration cfg = new BuilderConfiguration();
        DescriptorSourceFactory f = new DescriptorSourceFactory(cfg);
        assertNotNull(f);
    }

    // ==================== classType Tests ====================

    @Test
    public void should_ReturnClassType_When_TypeIsPrimitiveInt() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JPrimitiveType intType = JPrimitiveType.INT;
            Object result = method.invoke(null, intType);
            assertEquals("java.lang.Integer.TYPE", result.toString());
        } catch (Exception e) {
            fail(
                "Should invoke classType for int primitive: " + e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsPrimitiveLong() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JPrimitiveType longType = JPrimitiveType.LONG;
            Object result = method.invoke(null, longType);
            assertEquals("java.lang.Long.TYPE", result.toString());
        } catch (Exception e) {
            fail(
                "Should invoke classType for long primitive: " + e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsPrimitiveBoolean() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JPrimitiveType boolType = JPrimitiveType.BOOLEAN;
            Object result = method.invoke(null, boolType);
            assertEquals("java.lang.Boolean.TYPE", result.toString());
        } catch (Exception e) {
            fail(
                "Should invoke classType for boolean primitive: " +
                    e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsPrimitiveDouble() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JPrimitiveType doubleType = JPrimitiveType.DOUBLE;
            Object result = method.invoke(null, doubleType);
            assertEquals("java.lang.Double.TYPE", result.toString());
        } catch (Exception e) {
            fail(
                "Should invoke classType for double primitive: " +
                    e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsPrimitiveFloat() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JPrimitiveType floatType = JPrimitiveType.FLOAT;
            Object result = method.invoke(null, floatType);
            assertEquals("java.lang.Float.TYPE", result.toString());
        } catch (Exception e) {
            fail(
                "Should invoke classType for float primitive: " + e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsPrimitiveByte() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JPrimitiveType byteType = JPrimitiveType.BYTE;
            Object result = method.invoke(null, byteType);
            assertEquals("java.lang.Byte.TYPE", result.toString());
        } catch (Exception e) {
            fail(
                "Should invoke classType for byte primitive: " + e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsPrimitiveShort() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JPrimitiveType shortType = JPrimitiveType.SHORT;
            Object result = method.invoke(null, shortType);
            assertEquals("java.lang.Short.TYPE", result.toString());
        } catch (Exception e) {
            fail(
                "Should invoke classType for short primitive: " + e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsNotPrimitive() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JType objectType = new JClass("java.lang.String");
            Object result = method.invoke(null, objectType);
            assertTrue(result.toString().endsWith(".class"));
        } catch (Exception e) {
            fail("Should invoke classType for object type: " + e.getMessage());
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsCustomClass() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JType customType = new JClass("com.example.CustomType");
            Object result = method.invoke(null, customType);
            assertTrue(result.toString().contains("com.example.CustomType"));
            assertTrue(result.toString().endsWith(".class"));
        } catch (Exception e) {
            fail("Should invoke classType for custom class: " + e.getMessage());
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsList() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JType listType = new JClass("java.util.List");
            Object result = method.invoke(null, listType);
            assertTrue(result.toString().contains("java.util.List"));
            assertTrue(result.toString().endsWith(".class"));
        } catch (Exception e) {
            fail("Should invoke classType for List type: " + e.getMessage());
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsArrayList() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JType arrayListType = new JClass("java.util.ArrayList");
            Object result = method.invoke(null, arrayListType);
            assertTrue(result.toString().endsWith(".class"));
            assertTrue(result.toString().contains("ArrayList"));
        } catch (Exception e) {
            fail(
                "Should invoke classType for ArrayList type: " + e.getMessage()
            );
        }
    }

    @Test
    public void should_ReturnClassType_When_TypeIsMap() {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "classType",
                    JType.class
                );
            method.setAccessible(true);

            JType mapType = new JClass("java.util.Map");
            Object result = method.invoke(null, mapType);
            assertTrue(result.toString().contains("java.util.Map"));
            assertTrue(result.toString().endsWith(".class"));
        } catch (Exception e) {
            fail("Should invoke classType for Map type: " + e.getMessage());
        }
    }

    // ==================== Method Existence Tests ====================

    @Test
    public void should_HaveCreateSourceMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "createSource",
                org.exolab.castor.builder.info.ClassInfo.class
            );
        } catch (NoSuchMethodException e) {
            fail("createSource method not found");
        }
    }

    @Test
    public void should_HaveGetQualifiedDescriptorClassNameMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "getQualifiedDescriptorClassName",
                String.class
            );
        } catch (NoSuchMethodException e) {
            fail("getQualifiedDescriptorClassName method not found");
        }
    }

    @Test
    public void should_HaveCreateRestrictedDescriptorMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "createRestrictedDescriptor",
                org.exolab.castor.builder.info.FieldInfo.class,
                org.exolab.javasource.JSourceCode.class
            );
        } catch (NoSuchMethodException e) {
            fail("createRestrictedDescriptor method not found");
        }
    }

    @Test
    public void should_HaveCreateDescriptorMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "createDescriptor",
                DescriptorJClass.class,
                org.exolab.castor.builder.info.FieldInfo.class,
                String.class,
                String.class,
                org.exolab.javasource.JSourceCode.class
            );
        } catch (NoSuchMethodException e) {
            fail("createDescriptor method not found");
        }
    }

    @Test
    public void should_HaveAddSubstitutionGroupsMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "addSubstitutionGroups",
                org.exolab.castor.builder.info.FieldInfo.class,
                org.exolab.javasource.JSourceCode.class
            );
        } catch (NoSuchMethodException e) {
            fail("addSubstitutionGroups method not found");
        }
    }

    @Test
    public void should_HaveAddSpecialHandlerLogicMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "addSpecialHandlerLogic",
                org.exolab.castor.builder.info.FieldInfo.class,
                org.exolab.castor.builder.types.XSType.class,
                org.exolab.javasource.JSourceCode.class
            );
        } catch (NoSuchMethodException e) {
            fail("addSpecialHandlerLogic method not found");
        }
    }

    @Test
    public void should_HaveAddValidationCodeMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "addValidationCode",
                org.exolab.castor.builder.info.FieldInfo.class,
                org.exolab.javasource.JSourceCode.class
            );
        } catch (NoSuchMethodException e) {
            fail("addValidationCode method not found");
        }
    }

    @Test
    public void should_HaveClassTypeMethod() {
        try {
            DescriptorSourceFactory.class.getDeclaredMethod(
                "classType",
                JType.class
            );
        } catch (NoSuchMethodException e) {
            fail("classType method not found");
        }
    }

    @Test
    public void should_HaveConstructor() {
        try {
            DescriptorSourceFactory.class.getDeclaredConstructor(
                BuilderConfiguration.class
            );
        } catch (NoSuchMethodException e) {
            fail("Constructor not found");
        }
    }

    // ==================== Integration Tests ====================

    @Test
    public void should_ConsistentBehaviorAcrossMultipleCalls() {
        String className = "com.example.TestClass";
        String result1 = getQualifiedDescriptorClassName(className);
        String result2 = getQualifiedDescriptorClassName(className);
        String result3 = getQualifiedDescriptorClassName(className);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    @Test
    public void should_HandleVariousPackageFormats() {
        String[] inputs = {
            "Class",
            "pkg.Class",
            "pkg1.pkg2.Class",
            "a.b.c.d.VeryDeepClass",
        };

        for (String input : inputs) {
            String result = getQualifiedDescriptorClassName(input);
            assertNotNull("Result should not be null for: " + input, result);
            String localName = input.substring(input.lastIndexOf('.') + 1);
            assertTrue(
                "Result should contain class name for: " + input,
                result.contains(localName)
            );
        }
    }

    @Test
    public void should_HandleEdgeCaseNames() {
        String result1 = getQualifiedDescriptorClassName("_Class");
        String result2 = getQualifiedDescriptorClassName("Class_");
        String result3 = getQualifiedDescriptorClassName("Class123");

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
    }

    @Test
    public void should_HandleVeryLongClassName() {
        String longName =
            "org.exolab.castor.builder.descriptors.VeryLongClassNameWithManyCharacters";
        String result = getQualifiedDescriptorClassName(longName);
        assertNotNull(result);
        assertTrue(result.contains("VeryLongClassNameWithManyCharacters"));
    }

    @Test
    public void should_HandleMinimalInput() {
        String result = getQualifiedDescriptorClassName("X");
        assertNotNull(result);
        assertTrue(result.contains("X"));
    }

    @Test
    public void should_ReturnNonNullForAllValidInputs() {
        String[] testInputs = { "A", "AB", "ABC", "A.B", "A.B.C" };
        for (String input : testInputs) {
            String result = getQualifiedDescriptorClassName(input);
            assertNotNull(
                "Result should not be null for input: " + input,
                result
            );
        }
    }

    @Test
    public void should_HandleSingleLevelPackage() {
        String result = getQualifiedDescriptorClassName("pkg.Class");
        assertNotNull(result);
        assertTrue(result.contains("Class"));
    }

    @Test
    public void should_HandleMultipleLevelPackages() {
        String result = getQualifiedDescriptorClassName(
            "org.example.test.Class"
        );
        assertNotNull(result);
        assertTrue(result.contains("Class"));
    }

    @Test
    public void should_HandleNoPackage() {
        String result = getQualifiedDescriptorClassName("SimpleClass");
        assertNotNull(result);
        assertTrue(result.contains("SimpleClass"));
    }

    @Test
    public void should_AllMethodsBeCallableAfterConstruction() {
        assertNotNull(factory);
        String result = getQualifiedDescriptorClassName("test.Class");
        assertNotNull(result);
    }

    // ==================== Helper Methods ====================

    private String getQualifiedDescriptorClassName(String name) {
        try {
            java.lang.reflect.Method method =
                DescriptorSourceFactory.class.getDeclaredMethod(
                    "getQualifiedDescriptorClassName",
                    String.class
                );
            method.setAccessible(true);
            return (String) method.invoke(factory, name);
        } catch (Exception e) {
            fail(
                "Failed to call getQualifiedDescriptorClassName: " +
                    e.getMessage()
            );
            return null;
        }
    }
}
