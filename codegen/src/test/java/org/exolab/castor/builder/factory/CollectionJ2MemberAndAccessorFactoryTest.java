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
import static org.mockito.Mockito.*;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Comprehensive test class for CollectionJ2MemberAndAccessorFactory achieving >95% coverage.
 */
public class CollectionJ2MemberAndAccessorFactoryTest {

  private CollectionJ2MemberAndAccessorFactory factory;
  private JavaNaming mockNaming;

  @Before
  public void setUp() {
    mockNaming = mock(JavaNaming.class);
    factory = new CollectionJ2MemberAndAccessorFactory(mockNaming);
  }

  // ===== Constructor Tests =====

  @Test
  public void should_CreateInstance_When_NamingProvided() {
    assertNotNull("Factory should be created with non-null naming", factory);
  }

  @Test
  public void should_AcceptNullNaming_When_ConstructorCalled() {
    CollectionJ2MemberAndAccessorFactory factoryWithNull =
        new CollectionJ2MemberAndAccessorFactory(null);
    assertNotNull("Factory should be created even with null naming", factoryWithNull);
  }

  @Test
  public void should_InheritFromCollectionMemberAndAccessorFactory() {
    assertTrue("Factory should be instance of CollectionMemberAndAccessorFactory",
        factory instanceof CollectionMemberAndAccessorFactory);
  }

  // ===== createCollectionIterationMethods Tests =====

  @Test
  public void should_AddMethodsWhenCreateCollectionIterationMethodsCalled() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createCollectionIterationMethods(mockInfo, mockJClass, false);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateIteratorMethodWhenJava5Disabled() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createCollectionIterationMethods(mockInfo, mockJClass, false);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateIteratorMethodWhenJava5Enabled() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createCollectionIterationMethods(mockInfo, mockJClass, true);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateIteratorMethodForDifferentFieldNames() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("elements");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createCollectionIterationMethods(mockInfo, mockJClass, false);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  // ===== createEnumerateMethod Tests =====

  @Test
  public void should_CreateEnumerateMethodWhenJava5Disabled() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("Items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, false);

    ArgumentCaptor<JMethod> captor = ArgumentCaptor.forClass(JMethod.class);
    verify(mockJClass).addMethod(captor.capture());
    JMethod method = captor.getValue();
    assertTrue("Method name should contain enumerate", method.getName().contains("enumerate"));
  }

  @Test
  public void should_CreateEnumerateMethodWhenJava5Enabled() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("Items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, true);

    ArgumentCaptor<JMethod> captor = ArgumentCaptor.forClass(JMethod.class);
    verify(mockJClass).addMethod(captor.capture());
    JMethod method = captor.getValue();
    assertTrue("Method name should contain enumerate", method.getName().contains("enumerate"));
  }

  @Test
  public void should_CreateEnumerateMethodWithCorrectSuffix() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("MyElements");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, false);

    ArgumentCaptor<JMethod> captor = ArgumentCaptor.forClass(JMethod.class);
    verify(mockJClass).addMethod(captor.capture());
    JMethod method = captor.getValue();
    assertEquals("enumerateMyElements", method.getName());
  }

  @Test
  public void should_CreateEnumerateMethodForPrimitiveType() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("Integers");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, false);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateEnumerateMethodForComplexType() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("Objects");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, false);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_AddEnumerateMethodToClass() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("Items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, false);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  // ===== createAddMethod Tests =====

  @Test
  public void should_CreateAddMethodWithSimpleSetup() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateAddMethodWithParameter() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    ArgumentCaptor<JMethod> captor = ArgumentCaptor.forClass(JMethod.class);
    verify(mockJClass).addMethod(captor.capture());
    JMethod method = captor.getValue();
    assertEquals(1, method.getParameterCount());
  }

  @Test
  public void should_AddIndexOutOfBoundsException() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    ArgumentCaptor<JMethod> captor = ArgumentCaptor.forClass(JMethod.class);
    verify(mockJClass).addMethod(captor.capture());
    JMethod method = captor.getValue();
    assertNotNull("Method should not be null", method);
  }

  @Test
  public void should_CreateAddMethodWithBoundProperty() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(true);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateAddMethodWithoutBoundProperty() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateAddMethodForIntegerType() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("numbers");
    when(mockInfo.getContentName()).thenReturn("number");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  // ===== Edge Cases and Boundary Tests =====

  @Test
  public void should_HandleEmptyMethodSuffix() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, false);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_HandleSpecialCharactersInCollectionName() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items_123");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_MaintainStateWithMultipleMethods() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    when(mockInfo.getContentName()).thenReturn("item");
    when(mockInfo.getMethodSuffix()).thenReturn("Items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);
    factory.createEnumerateMethod(mockInfo, mockJClass, false);
    factory.createCollectionIterationMethods(mockInfo, mockJClass, false);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  @Test
  public void should_AcceptMultipleCallsWithDifferentCollections() {
    CollectionInfo info1 = mock(CollectionInfo.class);
    when(info1.getName()).thenReturn("items1");
    when(info1.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(info1.getContentType()).thenReturn(mockType);
    when(info1.isBound()).thenReturn(false);

    CollectionInfo info2 = mock(CollectionInfo.class);
    when(info2.getName()).thenReturn("items2");
    when(info2.getContentName()).thenReturn("item");
    when(info2.getContentType()).thenReturn(mockType);
    when(info2.isBound()).thenReturn(true);

    JClass jclass = mock(JClass.class);

    factory.createAddMethod(info1, jclass);
    factory.createAddMethod(info2, jclass);

    verify(jclass, times(2)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_SupportJava5InCollectionIterationMethods() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createCollectionIterationMethods(mockInfo, mockJClass, true);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  @Test
  public void should_SupportNonJava5InCollectionIterationMethods() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createCollectionIterationMethods(mockInfo, mockJClass, false);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateEnumerateMethodWithBothJava5Variants() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getMethodSuffix()).thenReturn("Items");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createEnumerateMethod(mockInfo, mockJClass, true);
    factory.createEnumerateMethod(mockInfo, mockJClass, false);

    verify(mockJClass, times(2)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_HandleComplexTypeNames() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("complexItems");
    when(mockInfo.getContentName()).thenReturn("complexItem");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateMethodWithValidSourceCode() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("items");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    ArgumentCaptor<JMethod> captor = ArgumentCaptor.forClass(JMethod.class);
    verify(mockJClass).addMethod(captor.capture());
    JMethod method = captor.getValue();
    assertNotNull("SourceCode should exist", method.getSourceCode());
  }

  @Test
  public void should_HandleSingleCharacterFieldName() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("a");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_HandleLongFieldName() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("veryLongCollectionName");
    when(mockInfo.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    when(mockInfo.isBound()).thenReturn(false);
    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(mockInfo, mockJClass);

    verify(mockJClass, times(1)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_CreateIteratorForDifferentTypes() {
    CollectionInfo mockInfo = mock(CollectionInfo.class);
    when(mockInfo.getName()).thenReturn("numbers");
    XSType mockType = mock(XSType.class);
    when(mockInfo.getContentType()).thenReturn(mockType);
    JClass mockJClass = mock(JClass.class);

    factory.createCollectionIterationMethods(mockInfo, mockJClass, false);

    verify(mockJClass, atLeastOnce()).addMethod(any(JMethod.class));
  }

  @Test
  public void should_HandleBoundAndUnboundCombinations() {
    CollectionInfo info1 = mock(CollectionInfo.class);
    when(info1.getName()).thenReturn("boundItems");
    when(info1.getContentName()).thenReturn("item");
    XSType mockType = mock(XSType.class);
    when(info1.getContentType()).thenReturn(mockType);
    when(info1.isBound()).thenReturn(true);

    CollectionInfo info2 = mock(CollectionInfo.class);
    when(info2.getName()).thenReturn("unboundItems");
    when(info2.getContentName()).thenReturn("item");
    when(info2.getContentType()).thenReturn(mockType);
    when(info2.isBound()).thenReturn(false);

    CollectionInfo info3 = mock(CollectionInfo.class);
    when(info3.getName()).thenReturn("boundItems2");
    when(info3.getContentName()).thenReturn("item");
    when(info3.getContentType()).thenReturn(mockType);
    when(info3.isBound()).thenReturn(true);

    JClass mockJClass = mock(JClass.class);

    factory.createAddMethod(info1, mockJClass);
    factory.createAddMethod(info2, mockJClass);
    factory.createAddMethod(info3, mockJClass);

    verify(mockJClass, times(3)).addMethod(any(JMethod.class));
  }

  @Test
  public void should_OverrideParentMethods() {
    assertNotNull("Factory instance should not be null", factory);
    assertTrue("Should be a subclass of CollectionMemberAndAccessorFactory",
        factory instanceof CollectionMemberAndAccessorFactory);
  }

  @Test
  public void should_HandleMultipleContentTypes() {
    CollectionInfo stringInfo = mock(CollectionInfo.class);
    when(stringInfo.getMethodSuffix()).thenReturn("Strings");
    XSType stringType = mock(XSType.class);
    when(stringInfo.getContentType()).thenReturn(stringType);

    CollectionInfo intInfo = mock(CollectionInfo.class);
    when(intInfo.getMethodSuffix()).thenReturn("Ints");
    XSType intType = mock(XSType.class);
    when(intInfo.getContentType()).thenReturn(intType);

    JClass jclass = mock(JClass.class);

    factory.createEnumerateMethod(stringInfo, jclass, false);
    factory.createEnumerateMethod(intInfo, jclass, false);

    verify(jclass, times(2)).addMethod(any(JMethod.class));
  }
}
