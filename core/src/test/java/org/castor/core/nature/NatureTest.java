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
package org.castor.core.nature;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link Nature}.
 */
public class NatureTest {

  /**
   * should_BeInterface_When_NatureLoaded
   */
  @Test
  public void should_BeInterface_When_NatureLoaded() {
    assertTrue(Nature.class.isInterface());
  }

  /**
   * should_DefineGetIdMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineGetIdMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method = Nature.class.getDeclaredMethod("getId");
    assertNotNull(method);
    assertEquals(String.class, method.getReturnType());
  }

  /**
   * should_HaveSingleMethod_When_InterfaceLoaded
   */
  @Test
  public void should_HaveSingleMethod_When_InterfaceLoaded() {
    java.lang.reflect.Method[] methods = Nature.class.getDeclaredMethods();
    assertEquals("Should have exactly one method", 1, methods.length);
    assertEquals("getId", methods[0].getName());
  }

  /**
   * should_DefinePublicGetIdMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefinePublicGetIdMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method = Nature.class.getDeclaredMethod("getId");
    assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
  }

  /**
   * should_ReturnQualifiedNatureName_When_GetIdCalled
   */
  @Test
  public void should_ReturnQualifiedNatureName_When_GetIdCalled() throws Exception {
    java.lang.reflect.Method method = Nature.class.getDeclaredMethod("getId");
    assertNotNull(method.getReturnType());
    assertEquals(String.class, method.getReturnType());
  }

  /**
   * should_BeInCastorCorePackage_When_InterfaceLoaded
   */
  @Test
  public void should_BeInCastorCorePackage_When_InterfaceLoaded() {
    assertEquals("org.castor.core.nature", Nature.class.getPackage().getName());
  }

  /**
   * should_HavePackageLevelAccess_When_InterfaceLoaded
   */
  @Test
  public void should_HavePackageLevelAccess_When_InterfaceLoaded() {
    int modifiers = Nature.class.getModifiers();
    assertFalse("Should not be public", java.lang.reflect.Modifier.isPublic(modifiers));
  }
}
