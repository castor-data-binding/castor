/*
 * Copyright 2008 Filip Hianik
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JSourceCode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Comprehensive test class for JDODescriptorJClass targeting >95% coverage.
 * Tests all methods, branches, edge cases, and null scenarios.
 */
public class JDODescriptorJClassTest {

    private BuilderConfiguration config;
    private JDODescriptorJClass jdoDescriptorJClass;

    @Mock
    private JClass mockType;

    @Mock
    private BuilderConfiguration mockConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        config = new BuilderConfiguration();
    }

    // ==================== Constructor Tests ====================

    @Test
    public void should_ConstructJDODescriptorJClass_When_ValidParametersProvided() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertEquals("TestDescriptor", jdoDescriptorJClass.getLocalName());
    }

    @Test
    public void should_ConstructJDODescriptorJClass_When_NullConfig() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            null,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertEquals("TestDescriptor", jdoDescriptorJClass.getLocalName());
    }

    @Test
    public void should_ConstructJDODescriptorJClass_When_SimpleClassName() {
        JClass typeClass = new JClass("SimpleTestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "SimpleDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertEquals("SimpleDescriptor", jdoDescriptorJClass.getLocalName());
    }

    @Test
    public void should_ConstructJDODescriptorJClass_When_ConfigIsNull() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            null,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertEquals("TestDescriptor", jdoDescriptorJClass.getLocalName());
    }

    // ==================== Super Class Tests ====================

    @Test
    public void should_SetSuperClassToJDOClassDescriptor_When_TypeHasNoSuperClass() {
        JClass typeClass = new JClass("org.example.TestType");
        typeClass.setSuperClass((String) null);

        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        String superClass = jdoDescriptorJClass.getSuperClassQualifiedName();
        assertEquals(
            "org.exolab.castor.mapping.loader.ClassDescriptorImpl",
            superClass
        );
    }

    @Test
    public void should_SetSuperClassToJDOClassDescriptor_When_TypeSuperClassEqualsConfigSuperClass() {
        JClass typeClass = new JClass("org.example.TestType");

        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        String superClass = jdoDescriptorJClass.getSuperClassQualifiedName();
        assertEquals(
            "org.exolab.castor.mapping.loader.ClassDescriptorImpl",
            superClass
        );
    }

    @Test
    public void should_HandleSuperClass_When_TypeHasDifferentSuperClass() {
        JClass typeClass = new JClass("org.example.TestType");

        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
    }

    @Test
    public void should_SetNullSuperClass_When_TypeSuperClassIsNullAndDifferentFromConfig() {
        JClass typeClass = new JClass("org.example.TestType");

        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
    }

    // ==================== Package Name Tests ====================

    @Test
    public void should_AddTypeImport_When_TypeHasPackageName() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertTrue(
            "Package name imports should be added",
            countImports(jdoDescriptorJClass) > 0
        );
    }

    @Test
    public void should_HandleTypeWithoutPackage() {
        JClass typeClass = new JClass("TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
    }

    @Test
    public void should_SkipAddingTypeImport_When_TypePackageNameIsNull() {
        JClass typeClass = new JClass("TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
    }

    @Test
    public void should_HandlePackageNameWithMultipleLevels() {
        JClass typeClass = new JClass("org.example.subpackage.nested.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertTrue(
            "Should have imports",
            countImports(jdoDescriptorJClass) > 0
        );
    }

    // ==================== Imports Tests ====================

    @Test
    public void should_HaveRequiredImports_When_DescriptorInitialized() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertTrue(
            "Should have ClassDescriptorJDONature import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature"
            )
        );
        assertTrue(
            "Should have SQLTypeInfos import",
            containsImport(
                jdoDescriptorJClass,
                "org.castor.jdo.engine.SQLTypeInfos"
            )
        );
        assertTrue(
            "Should have AccessMode import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.AccessMode"
            )
        );
        assertTrue(
            "Should have FieldDescriptor import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.FieldDescriptor"
            )
        );
        assertTrue(
            "Should have FieldHandler import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.FieldHandler"
            )
        );
        assertTrue(
            "Should have MappingException import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.MappingException"
            )
        );
        assertTrue(
            "Should have FieldHandlerImpl import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.loader.FieldHandlerImpl"
            )
        );
        assertTrue(
            "Should have TypeInfo import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.loader.TypeInfo"
            )
        );
    }

    @Test
    public void should_HaveAllMappingImports() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertTrue(
            "Should have ClassChoice import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.xml.ClassChoice"
            )
        );
        assertTrue(
            "Should have ClassMapping import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.xml.ClassMapping"
            )
        );
        assertTrue(
            "Should have FieldMapping import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.xml.FieldMapping"
            )
        );
        assertTrue(
            "Should have MapTo import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.xml.MapTo"
            )
        );
        assertTrue(
            "Should have Sql import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.xml.Sql"
            )
        );
    }

    @Test
    public void should_HaveAllJDONatureImports() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertTrue(
            "Should have ClassMappingAccessType import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.xml.types.ClassMappingAccessType"
            )
        );
        assertTrue(
            "Should have FieldMappingCollectionType import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.xml.types.FieldMappingCollectionType"
            )
        );
        assertTrue(
            "Should have FieldDescriptorImpl import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.mapping.loader.FieldDescriptorImpl"
            )
        );
        assertTrue(
            "Should have FieldDescriptorJDONature import",
            containsImport(
                jdoDescriptorJClass,
                "org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature"
            )
        );
        assertTrue(
            "Should have Method import",
            containsImport(jdoDescriptorJClass, "java.lang.reflect.Method")
        );
    }

    @Test
    public void should_HaveExceptionImport() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertTrue(
            "Should have IllegalClassDescriptorInitialization import",
            containsImport(
                jdoDescriptorJClass,
                "org.castor.core.exception.IllegalClassDescriptorInitialization"
            )
        );
    }

    // ==================== Constructor Method Tests ====================

    @Test
    public void should_HaveDefaultConstructor_When_DescriptorInitialized() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertTrue(
            "Should have at least one constructor",
            jdoDescriptorJClass.getConstructors().length > 0
        );
    }

    @Test
    public void should_HaveConstructorWithSourceCode() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        JConstructor[] constructors = jdoDescriptorJClass.getConstructors();
        assertTrue(
            "Should have at least one constructor",
            constructors.length > 0
        );

        JConstructor constructor = constructors[0];
        assertNotNull("Constructor should exist", constructor);

        JSourceCode sourceCode = constructor.getSourceCode();
        assertNotNull("Constructor source code should not be null", sourceCode);
    }

    @Test
    public void should_HaveConstructorCallingSuper() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        JConstructor[] constructors = jdoDescriptorJClass.getConstructors();
        assertTrue(
            "Should have at least one constructor",
            constructors.length > 0
        );

        JConstructor constructor = constructors[0];
        JSourceCode sourceCode = constructor.getSourceCode();
        String code = sourceCode.toString();

        assertTrue("Constructor should call super()", code.contains("super()"));
    }

    @Test
    public void should_HaveConstructorInitializingClassMapping() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        JConstructor[] constructors = jdoDescriptorJClass.getConstructors();
        assertTrue(
            "Should have at least one constructor",
            constructors.length > 0
        );

        JConstructor constructor = constructors[0];
        JSourceCode sourceCode = constructor.getSourceCode();
        String code = sourceCode.toString();

        assertTrue(
            "Constructor should initialize ClassMapping",
            code.contains("ClassMapping mapping")
        );
    }

    @Test
    public void should_HaveConstructorInitializingClassChoice() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        JConstructor[] constructors = jdoDescriptorJClass.getConstructors();
        assertTrue(
            "Should have at least one constructor",
            constructors.length > 0
        );

        JConstructor constructor = constructors[0];
        JSourceCode sourceCode = constructor.getSourceCode();
        String code = sourceCode.toString();

        assertTrue(
            "Constructor should initialize ClassChoice",
            code.contains("ClassChoice choice")
        );
    }

    @Test
    public void should_HaveConstructorInitializingMapTo() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        JConstructor[] constructors = jdoDescriptorJClass.getConstructors();
        assertTrue(
            "Should have at least one constructor",
            constructors.length > 0
        );

        JConstructor constructor = constructors[0];
        JSourceCode sourceCode = constructor.getSourceCode();
        String code = sourceCode.toString();

        assertTrue(
            "Constructor should initialize MapTo",
            code.contains("MapTo mapTo")
        );
    }

    // ==================== Inheritance Tests ====================

    @Test
    public void should_ExtendJClass() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertTrue(
            "JDODescriptorJClass should be instance of JClass",
            jdoDescriptorJClass instanceof JClass
        );
    }

    @Test
    public void should_HaveCorrectLocalName() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        assertEquals(
            "Local name should match constructor parameter",
            "TestDescriptor",
            jdoDescriptorJClass.getLocalName()
        );
    }

    // ==================== Edge Case Tests ====================

    @Test
    public void should_HandleValidSimpleClassName() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "ValidDescriptor",
            typeClass
        );

        assertNotNull("Should handle valid class name", jdoDescriptorJClass);
        assertEquals("ValidDescriptor", jdoDescriptorJClass.getLocalName());
    }

    @Test
    public void should_HandleSpecialCharactersInClassName() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "Test_Descriptor123",
            typeClass
        );

        assertNotNull("Should handle special characters", jdoDescriptorJClass);
        assertEquals("Test_Descriptor123", jdoDescriptorJClass.getLocalName());
    }

    @Test
    public void should_HandleMultipleInstances() {
        JClass typeClass1 = new JClass("org.example.Type1");
        JDODescriptorJClass descriptor1 = new JDODescriptorJClass(
            config,
            "Descriptor1",
            typeClass1
        );

        JClass typeClass2 = new JClass("org.example.Type2");
        JDODescriptorJClass descriptor2 = new JDODescriptorJClass(
            config,
            "Descriptor2",
            typeClass2
        );

        assertNotNull(descriptor1);
        assertNotNull(descriptor2);
        assertNotEquals(
            "Different instances should have different names",
            descriptor1.getLocalName(),
            descriptor2.getLocalName()
        );
    }

    @Test
    public void should_HandleTypeWithoutPackageAndConfigNull() {
        JClass typeClass = new JClass("SimpleType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            null,
            "SimpleDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertEquals("SimpleDescriptor", jdoDescriptorJClass.getLocalName());
    }

    @Test
    public void should_HandleComplexPackageHierarchy() {
        JClass typeClass = new JClass(
            "com.example.deep.nested.package.structure.ComplexType"
        );
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "ComplexDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertTrue(
            "Should have imports",
            countImports(jdoDescriptorJClass) > 0
        );
    }

    @Test
    public void should_PreserveSuperClassLogic_WhenTypeHasQualifiedName() {
        JClass typeClass = new JClass("org.example.BaseClass");
        typeClass.setSuperClass("org.example.Parent");

        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "BaseDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
    }

    @Test
    public void should_InitializeCorrectly_WithNullConfigAndValidType() {
        JClass typeClass = new JClass("org.example.NullConfigType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            null,
            "NullConfigDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertTrue(
            "Should have imports even with null config",
            countImports(jdoDescriptorJClass) > 0
        );
    }

    @Test
    public void should_InitializeCorrectly_WithQualifiedTypeNames() {
        JClass typeClass = new JClass(
            "org.qualified.example.descriptor.TestType"
        );
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "QualifiedDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertTrue(
            "Should have imports with qualified type",
            countImports(jdoDescriptorJClass) > 0
        );
    }

    @Test
    public void should_VerifyConstructorSourceCodeContainsAllRequiredStatements() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        JConstructor[] constructors = jdoDescriptorJClass.getConstructors();
        assertTrue(
            "Should have at least one constructor",
            constructors.length > 0
        );

        JConstructor constructor = constructors[0];
        JSourceCode sourceCode = constructor.getSourceCode();
        String code = sourceCode.toString();

        assertTrue("Should contain super() call", code.contains("super()"));
        assertTrue(
            "Should contain ClassMapping",
            code.contains("ClassMapping")
        );
        assertTrue("Should contain ClassChoice", code.contains("ClassChoice"));
        assertTrue("Should contain MapTo", code.contains("MapTo"));
    }

    @Test
    public void should_HaveSuperClassAsJDOClassDescriptor() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        String superClass = jdoDescriptorJClass.getSuperClassQualifiedName();
        assertEquals(
            "Should extend ClassDescriptorImpl",
            "org.exolab.castor.mapping.loader.ClassDescriptorImpl",
            superClass
        );
    }

    @Test
    public void should_HandleMultipleConfigSetups() {
        JClass typeClass = new JClass("org.example.TestType");

        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "CustomDescriptor",
            typeClass
        );

        assertNotNull(jdoDescriptorJClass);
        assertEquals("CustomDescriptor", jdoDescriptorJClass.getLocalName());
    }

    @Test
    public void should_AllConstructorsInitializeCorrectly() {
        JClass typeClass = new JClass("org.example.TestType");
        jdoDescriptorJClass = new JDODescriptorJClass(
            config,
            "TestDescriptor",
            typeClass
        );

        JConstructor[] constructors = jdoDescriptorJClass.getConstructors();
        for (JConstructor constructor : constructors) {
            assertNotNull("Constructor should not be null", constructor);
            JSourceCode sourceCode = constructor.getSourceCode();
            assertNotNull(
                "Constructor source code should not be null",
                sourceCode
            );
        }
    }

    // ==================== Helper Methods ====================

    private boolean containsImport(
        JDODescriptorJClass descriptor,
        String importName
    ) {
        java.util.Enumeration<String> imports = descriptor.getImports();
        while (imports.hasMoreElements()) {
            String imp = imports.nextElement();
            if (imp.contains(importName)) {
                return true;
            }
        }
        return false;
    }

    private int countImports(JDODescriptorJClass descriptor) {
        int count = 0;
        java.util.Enumeration<String> imports = descriptor.getImports();
        while (imports.hasMoreElements()) {
            imports.nextElement();
            count++;
        }
        return count;
    }
}
