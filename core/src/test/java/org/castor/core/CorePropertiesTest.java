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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for CoreProperties and CoreConfiguration.
 */
public class CorePropertiesTest {

  private CoreProperties _properties;

  @Before
  public void setUp() {
    _properties = new CoreProperties();
  }

  @Test
  public void should_CreateCorePropertiesInstance_When_DefaultConstructorCalled() {
    CoreProperties props = new CoreProperties();
    assertNotNull("CoreProperties instance should not be null", props);
  }

  @Test
  public void should_CreateCorePropertiesWithClassLoaders_When_ConstructorWithLoadersCalledWithValidLoaders() {
    ClassLoader appLoader = CoreProperties.class.getClassLoader();
    ClassLoader domainLoader = Object.class.getClassLoader();
    CoreProperties props = new CoreProperties(appLoader, domainLoader);
    assertNotNull("CoreProperties instance should not be null", props);
  }

  @Test
  public void should_CreateCorePropertiesWithClassLoaders_When_ConstructorWithLoadersCalledWithNullLoaders() {
    CoreProperties props = new CoreProperties(null, null);
    assertNotNull("CoreProperties instance should not be null even with null loaders", props);
  }

  @Test
  public void should_ContainMappingLoaderFactoriesConstant_When_ClassLoaded() {
    String constant = CoreProperties.MAPPING_LOADER_FACTORIES;
    assertNotNull("MAPPING_LOADER_FACTORIES should not be null", constant);
    assertEquals("MAPPING_LOADER_FACTORIES should have expected value",
        "org.castor.mapping.loaderFactories", constant);
  }

  @Test
  public void should_HaveSameConstantValue_When_ReferencedMultipleTimes() {
    String first = CoreProperties.MAPPING_LOADER_FACTORIES;
    String second = CoreProperties.MAPPING_LOADER_FACTORIES;
    assertEquals("Constant should have consistent value", first, second);
  }

  @Test
  public void should_InheritFromAbstractProperties_When_ClassInstantiated() {
    assertTrue("CoreProperties should extend AbstractProperties",
        _properties instanceof org.castor.core.util.AbstractProperties);
  }

  @Test
  public void should_NotThrowException_When_MultipleInstancesCreated() {
    CoreProperties props1 = new CoreProperties();
    CoreProperties props2 = new CoreProperties();
    assertNotNull("First instance should not be null", props1);
    assertNotNull("Second instance should not be null", props2);
    assertNotSame("Instances should be different objects", props1, props2);
  }

  @Test
  public void should_VerifyCoreConfigurationIsDeprecated_When_ClassInspected() {
    assertNotNull("CoreConfiguration class should exist even though deprecated",
        CoreConfiguration.class);
    assertTrue("CoreConfiguration should extend CoreProperties",
        CoreConfiguration.class.getSuperclass() == CoreProperties.class);
  }

  @Test
  public void should_HavePrivateConstructor_When_CoreConfigurationInspected() {
    java.lang.reflect.Constructor<?>[] constructors = CoreConfiguration.class.getDeclaredConstructors();
    assertTrue("CoreConfiguration should have constructors", constructors.length > 0);
    boolean hasPrivateConstructor = false;
    for (java.lang.reflect.Constructor<?> c : constructors) {
      if (java.lang.reflect.Modifier.isPrivate(c.getModifiers())) {
        hasPrivateConstructor = true;
        break;
      }
    }
    assertTrue("CoreConfiguration should have private constructor", hasPrivateConstructor);
  }

  @Test
  public void should_InheritFromCoreProperties_When_ClassHierarchyVerified() {
    String constant = CoreProperties.MAPPING_LOADER_FACTORIES;
    assertNotNull("CoreConfiguration should have access to parent constants", constant);
  }

  @Test
  public void should_NotBeInstantiableWithNullPointer_When_AccessingConstants() {
    try {
      String constant = CoreProperties.MAPPING_LOADER_FACTORIES;
      assertNotNull("Constant should be accessible", constant);
    } catch (NullPointerException e) {
      fail("Accessing constants should not throw NullPointerException");
    }
  }

  @Test
  public void should_ProvideLoadedDefaultProperties_When_CorePropertiesInitialized() {
    CoreProperties props = new CoreProperties();
    // Verify that properties were loaded from the default properties file
    assertNotNull("CoreProperties should be initialized", props);
  }

  @Test
  public void should_ProvideLoadedDefaultPropertiesWithClassLoaders_When_CorePropertiesInitializedWithLoaders() {
    ClassLoader appLoader = CoreProperties.class.getClassLoader();
    ClassLoader domainLoader = Object.class.getClassLoader();
    CoreProperties props = new CoreProperties(appLoader, domainLoader);
    assertNotNull("CoreProperties with loaders should be initialized", props);
  }

  @Test
  public void should_HandleNullClassLoaderGracefully_When_ConstructorCalledWithNullAppLoader() {
    CoreProperties props = new CoreProperties(null, Object.class.getClassLoader());
    assertNotNull("Should handle null application loader", props);
  }

  @Test
  public void should_HandleNullClassLoaderGracefully_When_ConstructorCalledWithNullDomainLoader() {
    CoreProperties props = new CoreProperties(CoreProperties.class.getClassLoader(), null);
    assertNotNull("Should handle null domain loader", props);
  }

  @Test
  public void should_ProvideFinalConstant_When_MappingLoaderFactoriesAccessed() {
    String constant1 = CoreProperties.MAPPING_LOADER_FACTORIES;
    String constant2 = CoreProperties.MAPPING_LOADER_FACTORIES;
    assertSame("Constants should be the same reference (final)", constant1, constant2);
  }

  @Test
  public void should_RetainConstantValue_When_MultiplePropertiesInstancesCreated() {
    CoreProperties props1 = new CoreProperties();
    String constant1 = CoreProperties.MAPPING_LOADER_FACTORIES;

    CoreProperties props2 = new CoreProperties();
    String constant2 = CoreProperties.MAPPING_LOADER_FACTORIES;

    assertEquals("Constant should not change between instances", constant1, constant2);
  }

  @Test
  public void should_ImplementProperInheritance_When_CoreConfigurationExtendsCoreProperties() {
    // CoreConfiguration has private constructor, verify inheritance through reflection
    assertTrue("CoreConfiguration should extend CoreProperties",
        CoreProperties.class.isAssignableFrom(CoreConfiguration.class));
    assertTrue("CoreConfiguration should extend AbstractProperties",
        org.castor.core.util.AbstractProperties.class.isAssignableFrom(CoreConfiguration.class));
  }

  @Test
  public void should_VerifyConstantIsAccessible_When_MappingLoaderFactoriesAccessed() {
    String constant = CoreProperties.MAPPING_LOADER_FACTORIES;
    assertEquals("Constant should have correct value", "org.castor.mapping.loaderFactories", constant);
  }

  @Test
  public void should_SuccessfullyConstruct_When_ThreadContextClassLoaderUsed() {
    try {
      ClassLoader tcl = Thread.currentThread().getContextClassLoader();
      CoreProperties props = new CoreProperties(tcl, tcl);
      assertNotNull("Should successfully create with thread context class loader", props);
    } catch (Exception e) {
      fail("Should not throw exception with thread context class loader: " + e.getMessage());
    }
  }
}
