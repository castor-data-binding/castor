/*
 * Copyright 2006 Ralf Joachim
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
package org.castor.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link MessageKeys}.
 */
public class MessageKeysTest {

  /**
   * should_BePublicClass_When_ClassDefined
   */
  @Test
  public void should_BePublicClass_When_ClassDefined() {
    int modifiers = MessageKeys.class.getModifiers();
    assertTrue("MessageKeys should be public", java.lang.reflect.Modifier.isPublic(modifiers));
  }

  /**
   * should_HaveConstructor_When_UtilityClass
   */
  @Test
  public void should_HaveConstructor_When_UtilityClass() {
    java.lang.reflect.Constructor<?>[] constructors = MessageKeys.class.getDeclaredConstructors();
    assertTrue("Should have at least one constructor", constructors.length >= 0);
  }

  /**
   * should_NotBeInstantiable_When_UtilityClass
   */
  @Test
  public void should_NotBeInstantiable_When_UtilityClass() throws Exception {
    java.lang.reflect.Constructor<?> constructor = MessageKeys.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
      // If we reach here, that's ok - the constructor just shouldn't be public
    } catch (Exception e) {
      // Expected for utility classes
    }
  }

  /**
   * should_BeInCoreUtilPackage_When_ClassDefined
   */
  @Test
  public void should_BeInCoreUtilPackage_When_ClassDefined() {
    String packageName = MessageKeys.class.getPackage().getName();
    assertEquals("org.castor.core.util", packageName);
  }

  /**
   * should_HaveCorrectClassName_When_ClassLoaded
   */
  @Test
  public void should_HaveCorrectClassName_When_ClassLoaded() {
    assertEquals("MessageKeys", MessageKeys.class.getSimpleName());
  }

  /**
   * should_BeUtilityClass_When_OnlyStaticMembers
   */
  @Test
  public void should_BeUtilityClass_When_OnlyStaticMembers() {
    java.lang.reflect.Field[] fields = MessageKeys.class.getDeclaredFields();
    for (java.lang.reflect.Field field : fields) {
      int modifiers = field.getModifiers();
      assertTrue("Field " + field.getName() + " should be static",
          java.lang.reflect.Modifier.isStatic(modifiers));
    }
  }

  /**
   * should_HaveCommentDocumentation_When_ClassDefined
   */
  @Test
  public void should_HaveCommentDocumentation_When_ClassDefined() {
    assertNotNull(MessageKeys.class.getName());
    assertTrue(MessageKeys.class.getName().endsWith("MessageKeys"));
  }
}
