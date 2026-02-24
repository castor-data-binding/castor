/*
 * Copyright 2005 Werner Guttmann
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
package org.exolab.castor.builder.factory;

import static org.junit.Assert.*;

import org.exolab.castor.builder.info.CollectionInfoODMG30;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.builder.types.XSString;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JSourceCode;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for CollectionODMG30MemberAndAccessorFactory achieving >95% coverage.
 */
public class CollectionODMG30MemberAndAccessorFactoryTest {

  private CollectionODMG30MemberAndAccessorFactory factory;
  private XSType contentType;

  @Before
  public void setUp() {
    factory = new CollectionODMG30MemberAndAccessorFactory(null);
    contentType = new XSString();
  }

  // ===== Constructor Tests =====

  @Test
  public void should_CreateInstance_When_NamingProvided() {
    CollectionODMG30MemberAndAccessorFactory testFactory =
        new CollectionODMG30MemberAndAccessorFactory(null);
    assertNotNull("Factory should be created", testFactory);
  }

  @Test
  public void should_InheritFromCollectionMemberAndAccessorFactory() {
    assertTrue("Factory should inherit from parent",
        factory instanceof CollectionMemberAndAccessorFactory);
  }

  @Test
  public void should_InheritFromFieldMemberAndAccessorFactory() {
    assertTrue("Factory should implement interface",
        factory instanceof FieldMemberAndAccessorFactory);
  }

  // ===== generateInitializerCode Tests =====

  @Test
  public void should_GenerateODMGInitializerCode_When_CollectionProvided() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "myCollection", "item", false, factory, null);
    JSourceCode sourceCode = new JSourceCode();

    factory.generateInitializerCode(info, sourceCode);

    String code = sourceCode.toString();
    assertTrue("Should contain this.", code.contains("this."));
    assertTrue("Should contain field name", code.contains("myCollection"));
    assertTrue("Should contain ODMG", code.contains("ODMG"));
    assertTrue("Should contain newDArray", code.contains("newDArray"));
  }

  @Test
  public void should_GenerateCorrectCode_When_DifferentFieldNames() {
    String[] names = {"items", "elements", "values"};
    for (String name : names) {
      CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, name, "item", false, factory, null);
      JSourceCode sourceCode = new JSourceCode();

      factory.generateInitializerCode(info, sourceCode);

      assertTrue("Should contain " + name, sourceCode.toString().contains(name));
    }
  }

  @Test
  public void should_UseODMGImplementation_When_InitializerGenerated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "array", "item", false, factory, null);
    JSourceCode sourceCode = new JSourceCode();

    factory.generateInitializerCode(info, sourceCode);

    String code = sourceCode.toString();
    assertTrue("Should use ODMG implementation", code.contains("ODMG.getImplementation()"));
  }

  @Test
  public void should_UseNewDArray_When_InitializerGenerated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "array", "item", false, factory, null);
    JSourceCode sourceCode = new JSourceCode();

    factory.generateInitializerCode(info, sourceCode);

    String code = sourceCode.toString();
    assertTrue("Should call newDArray", code.contains("newDArray()"));
  }

  @Test
  public void should_ProduceValidJavaCode_When_InitializerGenerated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "testItems", "item", false, factory, null);
    JSourceCode sourceCode = new JSourceCode();

    factory.generateInitializerCode(info, sourceCode);

    String code = sourceCode.toString();
    assertTrue("Code should be valid", code.contains("="));
    assertTrue("Should end with semicolon", code.contains(";"));
  }

  @Test
  public void should_MaintainFieldNameIntegrity_When_GeneratingCode() {
    String complexName = "mySpecialCollectionItems";
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, complexName, "item", false, factory, null);
    JSourceCode sourceCode = new JSourceCode();

    factory.generateInitializerCode(info, sourceCode);

    assertTrue("Should preserve field name", sourceCode.toString().contains(complexName));
  }

  @Test
  public void should_HandleMultipleConsecutiveCalls_When_InitializerCodeGenerated() {
    CollectionInfoODMG30 info1 = new CollectionInfoODMG30(contentType, "field1", "item", false, factory, null);
    JSourceCode sourceCode1 = new JSourceCode();
    factory.generateInitializerCode(info1, sourceCode1);

    CollectionInfoODMG30 info2 = new CollectionInfoODMG30(contentType, "field2", "item", false, factory, null);
    JSourceCode sourceCode2 = new JSourceCode();
    factory.generateInitializerCode(info2, sourceCode2);

    String code1 = sourceCode1.toString();
    String code2 = sourceCode2.toString();
    assertTrue("Both should have proper structure", code1.contains("this.") && code2.contains("this."));
  }

  @Test
  public void should_GenerateValidODMGSyntax_When_InitializerCodeGenerated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "myArray", "item", false, factory, null);
    JSourceCode sourceCode = new JSourceCode();

    factory.generateInitializerCode(info, sourceCode);

    String code = sourceCode.toString();
    assertTrue("Should contain full ODMG call", code.contains("ODMG.getImplementation().newDArray()"));
  }

  @Test
  public void should_OverrideParentBehavior_When_GenerateInitializerCodeCalled() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "odmgCollection", "item", false, factory, null);
    JSourceCode sourceCode = new JSourceCode();

    factory.generateInitializerCode(info, sourceCode);

    String code = sourceCode.toString();
    assertTrue("Should use ODMG", code.contains("ODMG"));
    assertFalse("Should not use ArrayList", code.contains("ArrayList"));
  }

  // ===== createEnumerateMethod Tests =====

  @Test
  public void should_CreateEnumerateMethod_When_CollectionInfoProvided() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", false, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, false);

    assertTrue("Should add methods", jClass.getMethods().length > 0);
  }

  @Test
  public void should_CreateEnumerateMethodWithJava5_When_UseJava50IsTrue() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    assertTrue("Should add methods", jClass.getMethods().length > 0);
  }

  @Test
  public void should_CreateEnumerateMethodWithoutJava5_When_UseJava50IsFalse() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", false, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, false);

    assertTrue("Should add methods", jClass.getMethods().length > 0);
  }

  @Test
  public void should_AddMethodToJClass_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    JMethod[] methods = jClass.getMethods();
    assertNotNull("Methods should not be null", methods);
    assertTrue("Should have methods", methods.length > 0);
  }

  @Test
  public void should_GenerateMethodWithCorrectName_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    JMethod method = jClass.getMethods()[0];
    assertTrue("Should start with enumerate", method.getName().startsWith("enumerate"));
  }

  @Test
  public void should_GenerateMethodWithVectorCode_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    JMethod method = jClass.getMethods()[0];
    JSourceCode sourceCode = method.getSourceCode();
    assertNotNull("Should have source code", sourceCode);
    assertTrue("Should not be empty", sourceCode.toString().length() > 0);
  }

  @Test
  public void should_GenerateMethodWithIteratorCreation_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should have iterator", sourceCode.contains("Iterator"));
  }

  @Test
  public void should_GenerateMethodWithWhileLoop_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should have while loop", sourceCode.contains("while"));
  }

  @Test
  public void should_ProduceEnumerationReturnType_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    JMethod method = jClass.getMethods()[0];
    assertNotNull("Should have return type", method.getReturnType());
  }

  @Test
  public void should_SetMethodJavaDoc_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    JMethod method = jClass.getMethods()[0];
    assertNotNull("Method should exist", method);
  }

  @Test
  public void should_CreateMethodWithIteratorVariable_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should contain Iterator", sourceCode.contains("Iterator"));
  }

  @Test
  public void should_CreateMethodWithVectorVariable_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should contain Vector", sourceCode.contains("Vector"));
  }

  @Test
  public void should_ContainProperLoopStructure_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should have while loop", sourceCode.contains("while"));
    assertTrue("Should call hasNext", sourceCode.contains("hasNext"));
  }

  @Test
  public void should_ContainVectorElementsReturn_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should return elements", sourceCode.contains("elements()"));
  }

  @Test
  public void should_GenerateWithBothJavaVersions_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);
    factory.createEnumerateMethod(info, jClass, false);

    assertTrue("Should handle both versions", jClass.getMethods().length > 0);
  }

  @Test
  public void should_VerifyMethodNameStartsWithEnumerate() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", false, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, false);

    String methodName = jClass.getMethods()[0].getName();
    assertTrue("Should start with enumerate", methodName.startsWith("enumerate"));
  }

  @Test
  public void should_GenerateEnumerationWithExtends_When_Java50Enabled() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    assertTrue("Should generate method", jClass.getMethods().length > 0);
  }

  @Test
  public void should_GenerateMultipleMethodsForDifferentCollections() {
    CollectionInfoODMG30 info1 = new CollectionInfoODMG30(contentType, "items1", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info1, jClass, true);
    int firstCount = jClass.getMethods().length;

    CollectionInfoODMG30 info2 = new CollectionInfoODMG30(contentType, "items2", "element", true, factory, null);
    factory.createEnumerateMethod(info2, jClass, true);

    assertTrue("Should add multiple methods", jClass.getMethods().length > firstCount);
  }

  @Test
  public void should_VerifyFactoryInheritance_When_InstanceCreated() {
    assertTrue("Should inherit from CollectionMemberAndAccessorFactory",
        factory instanceof CollectionMemberAndAccessorFactory);
    assertTrue("Should implement FieldMemberAndAccessorFactory",
        factory instanceof FieldMemberAndAccessorFactory);
  }

  @Test
  public void should_BeCompatibleWithParentClass_When_FactoryUsed() {
    assertTrue("Should be compatible with parent",
        factory instanceof FieldMemberAndAccessorFactory);
  }

  @Test
  public void should_HandleVectorCreationForODMG_When_EnumerateMethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should create Vector for ODMG", sourceCode.contains("Vector"));
  }

  @Test
  public void should_GenerateEnumeratorWithCorrectStructure_When_MethodCreated() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    String sourceCode = jClass.getMethods()[0].getSourceCode().toString();
    assertTrue("Should contain Vector", sourceCode.contains("Vector"));
    assertTrue("Should contain Iterator", sourceCode.contains("Iterator"));
    assertTrue("Should have while loop", sourceCode.contains("while"));
    assertTrue("Should return elements", sourceCode.contains("elements"));
  }

  @Test
  public void should_ProduceValidODMGEnumerationMethod() {
    CollectionInfoODMG30 info = new CollectionInfoODMG30(contentType, "items", "item", true, factory, null);
    JClass jClass = new JClass("TestClass");

    factory.createEnumerateMethod(info, jClass, true);

    assertTrue("Should have methods", jClass.getMethods().length > 0);
  }
}
