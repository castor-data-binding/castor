/*
 * Copyright 2009 Werner Guttmann
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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.castor.core.nature.BaseNature;
import org.junit.Test;

/**
 * Unit tests for {@link TargetAwareAnnotationProcessor}.
 */
public class TargetAwareAnnotationProcessorTest {

  /**
   * should_BeInterface_When_ProcessorLoaded
   */
  @Test
  public void should_BeInterface_When_ProcessorLoaded() {
    assertTrue(TargetAwareAnnotationProcessor.class.isInterface());
  }

  /**
   * should_ExtendAnnotationProcessor_When_InterfaceLoaded
   */
  @Test
  public void should_ExtendAnnotationProcessor_When_InterfaceLoaded() {
    Class<?>[] interfaces = TargetAwareAnnotationProcessor.class.getInterfaces();
    boolean extendsAP = false;
    for (Class<?> iface : interfaces) {
      if (AnnotationProcessor.class.equals(iface)) {
        extendsAP = true;
        break;
      }
    }
    assertTrue("Should extend AnnotationProcessor", extendsAP);
  }

  /**
   * should_DefineProcessAnnotationWithTargetMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineProcessAnnotationWithTargetMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessor.class.getDeclaredMethod("processAnnotation",
            BaseNature.class, Annotation.class, AnnotatedElement.class);
    assertNotNull(method);
    assertEquals(boolean.class, method.getReturnType());
  }

  /**
   * should_DefineMethodThrowingAnnotationTargetException_When_InterfaceLoaded
   */
  @Test
  public void should_DefineMethodThrowingAnnotationTargetException_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessor.class.getDeclaredMethod("processAnnotation",
            BaseNature.class, Annotation.class, AnnotatedElement.class);
    Class<?>[] exceptionTypes = method.getExceptionTypes();
    assertTrue("Should throw AnnotationTargetException",
        java.util.Arrays.asList(exceptionTypes).contains(AnnotationTargetException.class));
  }

  /**
   * should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded
   */
  @Test
  public void should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded() {
    assertEquals("org.castor.core.annotationprocessing",
        TargetAwareAnnotationProcessor.class.getPackage().getName());
  }

  /**
   * should_HaveCorrectSimpleName_When_InterfaceLoaded
   */
  @Test
  public void should_HaveCorrectSimpleName_When_InterfaceLoaded() {
    assertEquals("TargetAwareAnnotationProcessor", TargetAwareAnnotationProcessor.class.getSimpleName());
  }

  /**
   * should_BePublicInterface_When_InterfaceLoaded
   */
  @Test
  public void should_BePublicInterface_When_InterfaceLoaded() {
    int modifiers = TargetAwareAnnotationProcessor.class.getModifiers();
    assertTrue("Should be public", java.lang.reflect.Modifier.isPublic(modifiers));
    assertTrue("Should be interface", java.lang.reflect.Modifier.isInterface(modifiers));
  }

  /**
   * should_InheritForAnnotationClassMethod_When_ExtendingAnnotationProcessor
   */
  @Test
  public void should_InheritForAnnotationClassMethod_When_ExtendingAnnotationProcessor() throws Exception {
    java.lang.reflect.Method method = TargetAwareAnnotationProcessor.class.getMethod("forAnnotationClass");
    assertNotNull(method);
    assertEquals(Class.class, method.getReturnType());
  }

  /**
   * should_AcceptAnnotatedElementParameter_When_ProcessAnnotationMethodCalled
   */
  @Test
  public void should_AcceptAnnotatedElementParameter_When_ProcessAnnotationMethodCalled() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessor.class.getDeclaredMethod("processAnnotation",
            BaseNature.class, Annotation.class, AnnotatedElement.class);
    java.lang.reflect.Parameter[] params = method.getParameters();
    assertEquals("Should have 3 parameters", 3, params.length);
    assertEquals("Third parameter should be AnnotatedElement", AnnotatedElement.class, params[2].getType());
  }

  /**
   * should_SupportFieldAsAnnotatedElement_When_ProcessAnnotationMethodCalled
   */
  @Test
  public void should_SupportFieldAsAnnotatedElement_When_ProcessAnnotationMethodCalled() {
    assertTrue("Field should implement AnnotatedElement", AnnotatedElement.class.isAssignableFrom(Field.class));
  }
}
