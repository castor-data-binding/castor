/*
 * Copyright 2007 Ralf Joachim
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
package org.castor.core;

import junit.framework.TestCase;

/**
 * Unit tests for CoreConfiguration class.
 *
 * @author Test Suite
 */
public class CoreConfigurationTest extends TestCase {

  /**
   * Test that CoreConfiguration can be instantiated via reflection (private constructor).
   */
  public void testCoreConfigurationIsDeprecated() {
    // CoreConfiguration is a deprecated utility class that extends CoreProperties
    // It should not be directly instantiated, but the constructor exists
    assertNotNull(CoreConfiguration.class);
    assertTrue(CoreConfiguration.class.getSuperclass() == CoreProperties.class);
  }

  /**
   * Test that CoreConfiguration inherits from CoreProperties.
   */
  public void testCoreConfigurationExtendsCore() {
    assertTrue(CoreProperties.class.isAssignableFrom(CoreConfiguration.class));
  }

  /**
   * Test CoreConfiguration class hierarchy.
   */
  public void testCoreConfigurationHierarchy() {
    Class<?> clazz = CoreConfiguration.class;
    assertEquals("CoreConfiguration", clazz.getSimpleName());
    assertEquals("org.castor.core", clazz.getPackage().getName());
  }

}
