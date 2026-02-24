/*
 * Copyright 2008 Tobias Hochwallner
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
 * Unit tests for {@link NatureExtendable}.
 */
public class NatureExtendableTest {

  /**
   * should_BeInterface_When_NatureExtendableLoaded
   */
  @Test
  public void should_BeInterface_When_NatureExtendableLoaded() {
    assertTrue(NatureExtendable.class.isInterface());
  }

  /**
   * should_DefineHasNatureMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineHasNatureMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method = NatureExtendable.class.getDeclaredMethod("hasNature", String.class);
    assertNotNull(method);
    assertEquals(boolean.class, method.getReturnType());
  }

  /**
   * should_DefineAddNatureMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefineAddNatureMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method = NatureExtendable.class.getDeclaredMethod("addNature", String.class);
    assertNotNull(method);
    assertEquals(void.class, method.getReturnType());
  }

  /**
   * should_HaveTwoMethods_When_InterfaceLoaded
   */
  @Test
  public void should_HaveTwoMethods_When_InterfaceLoaded() {
    java.lang.reflect.Method[] methods = NatureExtendable.class.getDeclaredMethods();
    assertEquals("Should have exactly two methods", 2, methods.length);
  }

  /**
   * should_DefinePublicHasNatureMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefinePublicHasNatureMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method = NatureExtendable.class.getDeclaredMethod("hasNature", String.class);
    assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
  }

  /**
   * should_DefinePublicAddNatureMethod_When_InterfaceLoaded
   */
  @Test
  public void should_DefinePublicAddNatureMethod_When_InterfaceLoaded() throws Exception {
    java.lang.reflect.Method method = NatureExtendable.class.getDeclaredMethod("addNature", String.class);
    assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
  }

  /**
   * should_BeInCastorCoreNaturePackage_When_InterfaceLoaded
   */
  @Test
  public void should_BeInCastorCoreNaturePackage_When_InterfaceLoaded() {
    assertEquals("org.castor.core.nature", NatureExtendable.class.getPackage().getName());
  }

  /**
   * should_HaveCorrectSimpleName_When_InterfaceLoaded
   */
  @Test
  public void should_HaveCorrectSimpleName_When_InterfaceLoaded() {
    assertEquals("NatureExtendable", NatureExtendable.class.getSimpleName());
  }

  /**
   * should_BePublicInterface_When_InterfaceLoaded
   */
  @Test
  public void should_BePublicInterface_When_InterfaceLoaded() {
    int modifiers = NatureExtendable.class.getModifiers();
    assertTrue("Should be public", java.lang.reflect.Modifier.isPublic(modifiers));
    assertTrue("Should be interface", java.lang.reflect.Modifier.isInterface(modifiers));
  }

  /**
   * should_DefineMarkerInterface_When_InterfaceLoaded
   */
  @Test
  public void should_DefineMarkerInterface_When_InterfaceLoaded() {
    Class<?>[] interfaces = NatureExtendable.class.getInterfaces();
    assertEquals("Should not extend other interfaces", 0, interfaces.length);
  }
}
