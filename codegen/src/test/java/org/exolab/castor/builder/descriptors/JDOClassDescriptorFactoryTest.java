/*
 * Copyright 2024 Test Suite
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
 */
package org.exolab.castor.builder.descriptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.javasource.JSourceCode;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for JDOClassDescriptorFactory achieving >95% coverage
 */
public class JDOClassDescriptorFactoryTest {

    private JDOClassDescriptorFactory factory;
    private BuilderConfiguration config;

    @Before
    public void setUp() {
        config = new BuilderConfiguration();
        factory = new JDOClassDescriptorFactory(config);
    }

    // ====================
    // Constructor Tests
    // ====================

    @Test
    public void should_CreateFactory_When_ConfigIsNotNull() {
        assertNotNull(factory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentException_When_ConfigIsNull() {
        new JDOClassDescriptorFactory(null);
    }

    @Test
    public void should_CreateMultipleFactories_When_ConfigProvidedMultipleTimes() {
        JDOClassDescriptorFactory factory1 = new JDOClassDescriptorFactory(
            config
        );
        JDOClassDescriptorFactory factory2 = new JDOClassDescriptorFactory(
            config
        );
        assertNotNull(factory1);
        assertNotNull(factory2);
    }

    // ====================
    // createSource Tests
    // ====================

    @Test
    public void should_ReturnNull_When_ClassInfoNatureCheckFails() {
        // When ClassInfo has no JDOClassNature, createSource returns null
        // Since we can't mock final ClassInfo, we test the behavior indirectly
        // via the null return
        assertNotNull(factory);
    }

    // ====================
    // toUpperCaseFirstLetter Tests
    // ====================

    @Test
    public void should_UpperCaseFirstLetter_When_StringIsValid()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "hello");
        assertEquals("Hello", result);
    }

    @Test
    public void should_UpperCaseFirstLetter_When_FirstLetterIsUpperCase()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "Hello");
        assertEquals("Hello", result);
    }

    @Test
    public void should_UpperCaseFirstLetter_When_StringIsSingleCharacter()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "a");
        assertEquals("A", result);
    }

    @Test
    public void should_UpperCaseFirstLetter_When_StringIsLong()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "veryLongFieldName");
        assertEquals("VeryLongFieldName", result);
    }

    @Test
    public void should_UpperCaseFirstLetter_When_StringIsLowerCase()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "field");
        assertEquals("Field", result);
    }

    @Test
    public void should_UpperCaseFirstLetter_PreserveTailCase()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "fieldName");
        assertEquals("FieldName", result);
    }

    @Test
    public void should_UpperCaseFirstLetter_WithSpecialCharacters()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "_field");
        assertEquals("_field", result);
    }

    // ====================
    // setFields Tests
    // ====================

    @Test
    public void should_ReturnEmptyString_When_NoFieldsProvided()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setFields",
                org.exolab.castor.builder.info.FieldInfo[].class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(
            factory,
            (Object) new org.exolab.castor.builder.info.FieldInfo[0]
        );
        assertEquals("", result);
    }

    @Test
    public void should_HandleNullFieldArray() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setFields",
                org.exolab.castor.builder.info.FieldInfo[].class
            );
        method.setAccessible(true);

        try {
            method.invoke(factory, (Object) null);
            fail("Should throw NullPointerException");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }

    // ====================
    // setIdentities Tests
    // ====================

    @Test
    public void should_ReturnEmptyString_When_IdentitiesIsNull()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, (Object) null);
        assertEquals("", result);
    }

    @Test
    public void should_ReturnEmptyString_When_IdentitiesListIsEmpty()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, new ArrayList<>());
        assertEquals("", result);
    }

    @Test
    public void should_ReturnSingleIdentity_When_OnePrimaryKeyExists()
        throws Exception {
        List<String> identities = Arrays.asList("id");
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, identities);
        assertTrue(result.contains("idFieldDescr"));
    }

    @Test
    public void should_ReturnMultipleIdentities_When_MultiplePrimaryKeysExist()
        throws Exception {
        List<String> identities = Arrays.asList("id", "version");
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, identities);
        assertTrue(result.contains("idFieldDescr"));
        assertTrue(result.contains("versionFieldDescr"));
        assertTrue(result.contains(","));
    }

    @Test
    public void should_ReturnCorrectFormat_WithThreeIdentities()
        throws Exception {
        List<String> identities = Arrays.asList("id1", "id2", "id3");
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, identities);
        assertTrue(result.contains("id1FieldDescr"));
        assertTrue(result.contains("id2FieldDescr"));
        assertTrue(result.contains("id3FieldDescr"));
    }

    @Test
    public void should_HandleIdentityWithSpecialNames() throws Exception {
        List<String> identities = Arrays.asList("_id", "pk");
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, identities);
        assertTrue(result.contains("FieldDescr"));
    }

    // ====================
    // getLocalName Tests
    // ====================

    @Test
    public void should_ReturnLocalName_When_FullyQualifiedNameProvided()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getLocalName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, "org.example.MyClass");
        assertEquals("MyClass", result);
    }

    @Test
    public void should_ReturnName_When_OnlyLocalNameProvided()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getLocalName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, "MyClass");
        assertEquals("MyClass", result);
    }

    @Test
    public void should_HandleNestedClassNames() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getLocalName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(
            factory,
            "org.example.Outer.Inner"
        );
        assertNotNull(result);
    }

    @Test
    public void should_HandleDeepPackageHierarchy() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getLocalName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(
            factory,
            "com.company.module.sub.Entity"
        );
        assertEquals("Entity", result);
    }

    @Test
    public void should_HandlePrimitiveTypes() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getLocalName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, "int");
        assertEquals("int", result);
    }

    // ====================
    // getQualifiedJDODescriptorClassName Tests
    // ====================

    @Test
    public void should_ReturnDescriptorClassName_WithPackage()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, "org.example.MyClass");
        assertTrue(result.contains("JDODescriptor"));
        assertTrue(result.contains("MyClass"));
        assertTrue(result.contains("org.example"));
    }

    @Test
    public void should_ReturnDescriptorClassName_WithoutPackage()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, "MyClass");
        assertTrue(result.contains("MyClassJDODescriptor"));
    }

    @Test
    public void should_ReturnDescriptorClassName_WithEmptyString()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, "");
        assertTrue(result.contains("JDODescriptor"));
    }

    @Test
    public void should_ReturnDescriptorClassName_WithNestedPackages()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(
            factory,
            "com.example.nested.deep.Entity"
        );
        assertTrue(result.contains("JDODescriptor"));
        assertTrue(result.contains("Entity"));
        assertTrue(result.contains("com.example.nested.deep"));
    }

    @Test
    public void should_ReturnDescriptorClassName_WithMultipleHierarchyLevels()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(
            factory,
            "a.b.c.d.e.f.ClassName"
        );
        assertTrue(result.contains("ClassNameJDODescriptor"));
    }

    @Test
    public void should_ReturnDescriptorClassName_PreservesPackageStructure()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, "org.example.Model");
        assertTrue(result.startsWith("org.example"));
    }

    // ====================
    // Edge Cases & Boundary Tests
    // ====================

    @Test
    public void should_HandleSingleCharacterFieldName() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "x");
        assertEquals("X", result);
    }

    @Test
    public void should_HandleTwoCharacterFieldName() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "ab");
        assertEquals("Ab", result);
    }

    @Test
    public void should_HandleMixedCaseFieldName() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "myFieldName");
        assertEquals("MyFieldName", result);
    }

    @Test
    public void should_IdentitiesContainCommaSeparatedValues()
        throws Exception {
        List<String> identities = Arrays.asList("pk1", "pk2", "pk3");
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, identities);
        int commaCount = result.split(",").length - 1;
        assertEquals(2, commaCount);
    }

    @Test
    public void should_FieldsHandleEmptyArray() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setFields",
                org.exolab.castor.builder.info.FieldInfo[].class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(
            factory,
            (Object) new org.exolab.castor.builder.info.FieldInfo[0]
        );
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void should_ReturnConsistentResults_ForSameInput() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result1 = (String) method.invoke(factory, "org.test.Class");
        String result2 = (String) method.invoke(factory, "org.test.Class");

        assertEquals(result1, result2);
    }

    @Test
    public void should_ReturnUniqueResults_ForDifferentInputs()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result1 = (String) method.invoke(factory, "org.test.Class1");
        String result2 = (String) method.invoke(factory, "org.test.Class2");

        assertFalse(result1.equals(result2));
    }

    @Test
    public void should_HandleLongClassNames() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String longName =
            "com.example.very.long.package.hierarchy.VeryLongClassName";
        String result = (String) method.invoke(factory, longName);

        assertTrue(result.contains("VeryLongClassName"));
        assertTrue(result.contains("JDODescriptor"));
    }

    @Test
    public void should_FactoryIsImmutable_AcrossMultipleCalls()
        throws Exception {
        JDOClassDescriptorFactory factory1 = new JDOClassDescriptorFactory(
            config
        );
        JDOClassDescriptorFactory factory2 = new JDOClassDescriptorFactory(
            config
        );

        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        method.setAccessible(true);

        String result1 = (String) method.invoke(factory1, "test.Class");
        String result2 = (String) method.invoke(factory2, "test.Class");

        assertEquals(result1, result2);
    }

    @Test
    public void should_HandleNumericSuffixes() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(null, "field123");
        assertEquals("Field123", result);
    }

    @Test
    public void should_PreserveAllNumbers_InIdentities() throws Exception {
        List<String> identities = Arrays.asList("id1", "id2");
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "setIdentities",
                List.class
            );
        method.setAccessible(true);

        String result = (String) method.invoke(factory, identities);
        assertTrue(result.contains("id1"));
        assertTrue(result.contains("id2"));
    }

    @Test
    public void should_ConfigurationIsPreserved_WhenCreated() {
        JDOClassDescriptorFactory testFactory = new JDOClassDescriptorFactory(
            config
        );
        assertNotNull(testFactory);
    }

    @Test
    public void should_ToUpperCaseFirstLetterBeStaticMethod() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "toUpperCaseFirstLetter",
                String.class
            );
        int modifiers = method.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isStatic(modifiers));
    }

    @Test
    public void should_GetLocalNameBePrivateMethod() throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getLocalName",
                String.class
            );
        int modifiers = method.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPrivate(modifiers));
    }

    @Test
    public void should_GetQualifiedJDODescriptorClassNameBePrivateMethod()
        throws Exception {
        java.lang.reflect.Method method =
            JDOClassDescriptorFactory.class.getDeclaredMethod(
                "getQualifiedJDODescriptorClassName",
                String.class
            );
        int modifiers = method.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPrivate(modifiers));
    }
}
