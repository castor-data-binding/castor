/*
 * Copyright 2011 Werner Guttmann
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
package org.castor.core.constants.solrj;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link SOLRJConstants}.
 */
public class SOLRJConstantsTest {

  /**
   * should_DefineSOLRJNamespace_When_InterfaceLoaded
   */
  @Test
  public void should_DefineSOLRJNamespace_When_InterfaceLoaded() {
    assertEquals("http://www.castor.org/binding/solrj", SOLRJConstants.NAMESPACE);
  }

  /**
   * should_DefineFieldAnnotationElement_When_InterfaceLoaded
   */
  @Test
  public void should_DefineFieldAnnotationElement_When_InterfaceLoaded() {
    assertEquals("field", SOLRJConstants.ANNOTATIONS_FIELD_NAME);
  }

  /**
   * should_DefineIdAnnotationElement_When_InterfaceLoaded
   */
  @Test
  public void should_DefineIdAnnotationElement_When_InterfaceLoaded() {
    assertEquals("id", SOLRJConstants.ANNOTATIONS_ID_NAME);
  }

  /**
   * should_DefineGeneratedAnnotationClassesPackage_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGeneratedAnnotationClassesPackage_When_InterfaceLoaded() {
    assertEquals("org.exolab.castor.xml.schema.annotations.solrj",
        SOLRJConstants.GENERATED_ANNOTATION_CLASSES_PACKAGE);
  }

  /**
   * should_BeInterface_When_InterfaceLoaded
   */
  @Test
  public void should_BeInterface_When_InterfaceLoaded() {
    assertTrue(SOLRJConstants.class.isInterface());
  }

  /**
   * should_ContainAllExpectedConstants_When_InterfaceLoaded
   */
  @Test
  public void should_ContainAllExpectedConstants_When_InterfaceLoaded() {
    java.lang.reflect.Field[] fields = SOLRJConstants.class.getFields();
    assertTrue("Should have constants defined", fields.length > 0);
  }

  /**
   * should_HaveValidNamespaceFormat_When_InterfaceLoaded
   */
  @Test
  public void should_HaveValidNamespaceFormat_When_InterfaceLoaded() {
    assertTrue(SOLRJConstants.NAMESPACE.startsWith("http://"));
    assertTrue(SOLRJConstants.NAMESPACE.contains("castor"));
  }

  /**
   * should_HaveValidPackageFormat_When_InterfaceLoaded
   */
  @Test
  public void should_HaveValidPackageFormat_When_InterfaceLoaded() {
    assertTrue(SOLRJConstants.GENERATED_ANNOTATION_CLASSES_PACKAGE.contains("org.exolab"));
    assertTrue(SOLRJConstants.GENERATED_ANNOTATION_CLASSES_PACKAGE.contains("solrj"));
  }
}
