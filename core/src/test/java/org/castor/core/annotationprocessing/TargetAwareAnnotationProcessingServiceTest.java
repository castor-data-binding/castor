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
import java.util.Set;

import org.castor.core.nature.BaseNature;
import org.junit.Test;

/**
 * Unit tests for {@link TargetAwareAnnotationProcessingService}.
 */
public class TargetAwareAnnotationProcessingServiceTest {

  /**
   * should_ExtendAnnotationProcessingService_When_InterfaceLoaded
   */
  @Test
  public void should_ExtendAnnotationProcessingService_When_InterfaceLoaded() {
    assertTrue(AnnotationProcessingService.class.isAssignableFrom(TargetAwareAnnotationProcessingService.class));
  }

  /**
   * should_BeInterface_When_ServiceLoaded
   */
  @Test
  public void should_BeInterface_When_ServiceLoaded() {
    assertTrue(TargetAwareAnnotationProcessingService.class.isInterface());
  }

  /**
   * should_DefineProcessAnnotationsWithTargetMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineProcessAnnotationsWithTargetMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessingService.class.getDeclaredMethod("processAnnotations",
            BaseNature.class, Annotation[].class, AnnotatedElement.class);
    assertNotNull(method);
    assertEquals(Annotation[].class, method.getReturnType());
  }

  /**
   * should_DefineProcessAnnotationWithTargetMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineProcessAnnotationWithTargetMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessingService.class.getDeclaredMethod("processAnnotation",
            BaseNature.class, Annotation.class, AnnotatedElement.class);
    assertNotNull(method);
    assertEquals(boolean.class, method.getReturnType());
  }

  /**
   * should_DefineAddTargetAwareAnnotationProcessorMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineAddTargetAwareAnnotationProcessorMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessingService.class.getDeclaredMethod("addAnnotationProcessor",
            TargetAwareAnnotationProcessor.class);
    assertNotNull(method);
    assertEquals(void.class, method.getReturnType());
  }

  /**
   * should_DefineGetTargetAwareAnnotationProcessorsMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGetTargetAwareAnnotationProcessorsMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessingService.class.getDeclaredMethod("getTargetAwareAnnotationProcessors");
    assertNotNull(method);
    assertEquals(Set.class, method.getReturnType());
  }

  /**
   * should_DefineGetAllAnnotationProcessorsMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGetAllAnnotationProcessorsMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessingService.class.getDeclaredMethod("getAllAnnotationProcessors");
    assertNotNull(method);
    assertEquals(Set.class, method.getReturnType());
  }

  /**
   * should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded
   */
  @Test
  public void should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded() {
    assertEquals("org.castor.core.annotationprocessing",
        TargetAwareAnnotationProcessingService.class.getPackage().getName());
  }

  /**
   * should_HaveCorrectSimpleName_When_InterfaceLoaded
   */
  @Test
  public void should_HaveCorrectSimpleName_When_InterfaceLoaded() {
    assertEquals("TargetAwareAnnotationProcessingService",
        TargetAwareAnnotationProcessingService.class.getSimpleName());
  }

  /**
   * should_BePublicInterface_When_InterfaceLoaded
   */
  @Test
  public void should_BePublicInterface_When_InterfaceLoaded() {
    int modifiers = TargetAwareAnnotationProcessingService.class.getModifiers();
    assertTrue("Should be public", java.lang.reflect.Modifier.isPublic(modifiers));
    assertTrue("Should be interface", java.lang.reflect.Modifier.isInterface(modifiers));
  }

  /**
   * should_DefineMethodsThrowingAnnotationTargetException_When_InterfaceLoaded
   */
  @Test
  public void should_DefineMethodsThrowingAnnotationTargetException_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        TargetAwareAnnotationProcessingService.class.getDeclaredMethod("processAnnotations",
            BaseNature.class, Annotation[].class, AnnotatedElement.class);
    Class<?>[] exceptionTypes = method.getExceptionTypes();
    assertTrue("Should throw AnnotationTargetException",
        java.util.Arrays.asList(exceptionTypes).contains(AnnotationTargetException.class));
  }
}
