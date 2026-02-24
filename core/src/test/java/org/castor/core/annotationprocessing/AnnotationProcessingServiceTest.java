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
import java.util.Set;

import org.castor.core.nature.BaseNature;
import org.junit.Test;

/**
 * Unit tests for {@link AnnotationProcessingService}.
 */
public class AnnotationProcessingServiceTest {

  /**
   * should_BeInterface_When_ServiceLoaded
   */
  @Test
  public void should_BeInterface_When_ServiceLoaded() {
    assertTrue(AnnotationProcessingService.class.isInterface());
  }

  /**
   * should_DefineProcessAnnotationsMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineProcessAnnotationsMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        AnnotationProcessingService.class.getDeclaredMethod("processAnnotations",
            BaseNature.class, Annotation[].class);
    assertNotNull(method);
  }

  /**
   * should_DefineProcessAnnotationMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineProcessAnnotationMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        AnnotationProcessingService.class.getDeclaredMethod("processAnnotation",
            BaseNature.class, Annotation.class);
    assertNotNull(method);
  }

  /**
   * should_DefineAddAnnotationProcessorMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineAddAnnotationProcessorMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        AnnotationProcessingService.class.getDeclaredMethod("addAnnotationProcessor",
            AnnotationProcessor.class);
    assertNotNull(method);
  }

  /**
   * should_DefineGetAnnotationProcessorsMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGetAnnotationProcessorsMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method =
        AnnotationProcessingService.class.getDeclaredMethod("getAnnotationProcessors");
    assertNotNull(method);
    assertEquals(Set.class, method.getReturnType());
  }

  /**
   * should_HaveFourPublicMethods_When_InterfaceLoaded
   */
  @Test
  public void should_HaveFourPublicMethods_When_InterfaceLoaded() {
    java.lang.reflect.Method[] methods = AnnotationProcessingService.class.getDeclaredMethods();
    assertEquals("Should have exactly 4 methods", 4, methods.length);
  }

  /**
   * should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded
   */
  @Test
  public void should_BeInCastorCoreAnnotationProcessingPackage_When_InterfaceLoaded() {
    assertEquals("org.castor.core.annotationprocessing",
        AnnotationProcessingService.class.getPackage().getName());
  }

  /**
   * should_HaveCorrectSimpleName_When_InterfaceLoaded
   */
  @Test
  public void should_HaveCorrectSimpleName_When_InterfaceLoaded() {
    assertEquals("AnnotationProcessingService", AnnotationProcessingService.class.getSimpleName());
  }

  /**
   * should_BePublicInterface_When_InterfaceLoaded
   */
  @Test
  public void should_BePublicInterface_When_InterfaceLoaded() {
    int modifiers = AnnotationProcessingService.class.getModifiers();
    assertTrue("Should be public", java.lang.reflect.Modifier.isPublic(modifiers));
    assertTrue("Should be interface", java.lang.reflect.Modifier.isInterface(modifiers));
  }

  /**
   * should_DefineGenericProcessAnnotationsMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGenericProcessAnnotationsMethod_When_InterfaceLoaded() {
    java.lang.reflect.Method[] methods = AnnotationProcessingService.class.getDeclaredMethods();
    boolean foundGenericMethod = false;
    for (java.lang.reflect.Method method : methods) {
      if ("processAnnotations".equals(method.getName())) {
        foundGenericMethod = true;
        break;
      }
    }
    assertTrue("Should define processAnnotations method", foundGenericMethod);
  }
}
