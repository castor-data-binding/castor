/*
 * Copyright 2007 Joachim Grueneis
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
package org.castor.core.annotationprocessing;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;

import org.castor.core.nature.BaseNature;
import org.junit.Test;

/**
 * Unit tests for {@link AnnotationProcessor}.
 */
public class AnnotationProcessorTest {

  /**
   * should_BeInterface_When_ProcessorLoaded
   */
  @Test
  public void should_BeInterface_When_ProcessorLoaded() {
    assertTrue(AnnotationProcessor.class.isInterface());
  }

  /**
   * should_DefineForAnnotationClassMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineForAnnotationClassMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        AnnotationProcessor.class.getDeclaredMethod("forAnnotationClass");
    assertNotNull(method);
    assertEquals(Class.class, method.getReturnType());
  }

  /**
   * should_DefineProcessAnnotationMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineProcessAnnotationMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        AnnotationProcessor.class.getDeclaredMethod("processAnnotation", BaseNature.class, Annotation.class);
    assertNotNull(method);
    assertEquals(boolean.class, method.getReturnType());
  }

  /**
   * should_DefineTwoMethods_When_InterfaceLoaded
   */
  @Test
  public void should_DefineTwoMethods_When_InterfaceLoaded() {
    java.lang.reflect.Method[] methods = AnnotationProcessor.class.getDeclaredMethods();
    assertEquals("Should have exactly 2 methods", 2, methods.length);
  }

  /**
   * should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded
   */
  @Test
  public void should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded() {
    assertEquals("org.castor.core.annotationprocessing",
        AnnotationProcessor.class.getPackage().getName());
  }

  /**
   * should_HaveCorrectSimpleName_When_InterfaceLoaded
   */
  @Test
  public void should_HaveCorrectSimpleName_When_InterfaceLoaded() {
    assertEquals("AnnotationProcessor", AnnotationProcessor.class.getSimpleName());
  }

  /**
   * should_BePublicInterface_When_InterfaceLoaded
   */
  @Test
  public void should_BePublicInterface_When_InterfaceLoaded() {
    int modifiers = AnnotationProcessor.class.getModifiers();
    assertTrue("Should be public", java.lang.reflect.Modifier.isPublic(modifiers));
    assertTrue("Should be interface", java.lang.reflect.Modifier.isInterface(modifiers));
  }

  /**
   * should_DefineGenericProcessAnnotationMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGenericProcessAnnotationMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        AnnotationProcessor.class.getDeclaredMethod("processAnnotation", BaseNature.class, Annotation.class);
    assertTrue("Method should be generic", method.getTypeParameters().length >= 0);
  }

  /**
   * should_ReturnAnnotationClassType_When_ForAnnotationClassMethodCalled
   */
  @Test
  public void should_ReturnAnnotationClassType_When_ForAnnotationClassMethodCalled() throws Exception {
    java.lang.reflect.Method method = AnnotationProcessor.class.getDeclaredMethod("forAnnotationClass");
    assertNotNull(method);
    assertTrue(Class.class.isAssignableFrom(method.getReturnType()));
  }
}
