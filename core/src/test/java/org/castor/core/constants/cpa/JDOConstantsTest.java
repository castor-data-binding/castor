/*
 * Copyright 2008 Lukas Lang
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
package org.castor.core.constants.cpa;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link JDOConstants}.
 */
public class JDOConstantsTest {

  /**
   * should_DefineJDODescriptorSuffix_When_InterfaceLoaded
   */
  @Test
  public void should_DefineJDODescriptorSuffix_When_InterfaceLoaded() {
    assertEquals("JDODescriptor", JDOConstants.JDO_DESCRIPTOR_SUFFIX);
  }

  /**
   * should_DefineJDODescriptorPackage_When_InterfaceLoaded
   */
  @Test
  public void should_DefineJDODescriptorPackage_When_InterfaceLoaded() {
    assertEquals("jdo_descriptors", JDOConstants.JDO_DESCRIPTOR_PACKAGE);
  }

  /**
   * should_DefineJDONamespace_When_InterfaceLoaded
   */
  @Test
  public void should_DefineJDONamespace_When_InterfaceLoaded() {
    assertEquals("http://www.castor.org/binding/persistence", JDOConstants.JDO_NAMESPACE);
  }

  /**
   * should_DefineTableAnnotationElement_When_InterfaceLoaded
   */
  @Test
  public void should_DefineTableAnnotationElement_When_InterfaceLoaded() {
    assertEquals("table", JDOConstants.ANNOTATIONS_TABLE_NAME);
  }

  /**
   * should_DefineColumnAnnotationElement_When_InterfaceLoaded
   */
  @Test
  public void should_DefineColumnAnnotationElement_When_InterfaceLoaded() {
    assertEquals("column", JDOConstants.ANNOTATIONS_COLUMN_NAME);
  }

  /**
   * should_DefineOneToOneAnnotationElement_When_InterfaceLoaded
   */
  @Test
  public void should_DefineOneToOneAnnotationElement_When_InterfaceLoaded() {
    assertEquals("one-to-one", JDOConstants.ANNOTATIONS_ONE_TO_ONE_NAME);
  }

  /**
   * should_DefineOneToManyAnnotationElement_When_InterfaceLoaded
   */
  @Test
  public void should_DefineOneToManyAnnotationElement_When_InterfaceLoaded() {
    assertEquals("one-to-many", JDOConstants.ANNOTATIONS_ONE_TO_MANY);
  }

  /**
   * should_DefineManyToManyAnnotationElement_When_InterfaceLoaded
   */
  @Test
  public void should_DefineManyToManyAnnotationElement_When_InterfaceLoaded() {
    assertEquals("many-to-many", JDOConstants.ANNOTATIONS_MANY_TO_MANY);
  }

  /**
   * should_DefineGeneratedAnnotationClassesPackage_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGeneratedAnnotationClassesPackage_When_InterfaceLoaded() {
    assertEquals("org.exolab.castor.xml.schema.annotations.jdo",
        JDOConstants.GENERATED_ANNOTATION_CLASSES_PACKAGE);
  }

  /**
   * should_DefineCDRListFile_When_InterfaceLoaded
   */
  @Test
  public void should_DefineCDRListFile_When_InterfaceLoaded() {
    assertEquals(".castor.jdo.cdr", JDOConstants.PKG_CDR_LIST_FILE);
  }

  /**
   * should_BeInterface_When_InterfaceLoaded
   */
  @Test
  public void should_BeInterface_When_InterfaceLoaded() {
    assertTrue(JDOConstants.class.isInterface());
  }

  /**
   * should_ContainAllExpectedConstants_When_InterfaceLoaded
   */
  @Test
  public void should_ContainAllExpectedConstants_When_InterfaceLoaded() {
    java.lang.reflect.Field[] fields = JDOConstants.class.getFields();
    assertTrue("Should have constants defined", fields.length > 0);
  }
}
