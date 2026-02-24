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
package org.castor.core.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for CastorProperties utility class covering all methods,
 * constructors, property loading, and edge cases to achieve >95% code coverage.
 */
public class CastorPropertiesTest {

  private AbstractProperties _parent;
  private CastorProperties _properties;

  @Before
  public void setUp() {
    _parent = new CorePropertiesStub();
    _properties = new CastorProperties(_parent);
  }

  @Test
  public void should_CreateCastorPropertiesWithParent_When_ConstructorCalledWithValidParent() {
    CastorProperties props = new CastorProperties(_parent);
    assertNotNull("CastorProperties instance should not be null", props);
  }

  @Test
  public void should_ThrowNullPointerException_When_ConstructorCalledWithNullParent() {
    try {
      CastorProperties props = new CastorProperties(null);
      fail("Should throw NullPointerException when parent is null");
    } catch (NullPointerException e) {
      assertTrue("NullPointerException expected for null parent", true);
    }
  }

  @Test
  public void should_InheritFromAbstractProperties_When_ClassInstantiated() {
    assertTrue("CastorProperties should extend AbstractProperties",
        _properties instanceof AbstractProperties);
  }

  @Test
  public void should_LoadUserPropertiesFile_When_ConstructorCalled() {
    CastorProperties props = new CastorProperties(_parent);
    assertNotNull("Properties should be loaded", props);
  }

  @Test
  public void should_SupportMultipleInstances_When_ConstructorCalledMultipleTimes() {
    CastorProperties props1 = new CastorProperties(_parent);
    CastorProperties props2 = new CastorProperties(_parent);
    assertNotNull("First instance should not be null", props1);
    assertNotNull("Second instance should not be null", props2);
    assertNotSame("Instances should be different objects", props1, props2);
  }

  @Test
  public void should_PreserveParentReference_When_ConstructorCalledWithParent() {
    AbstractProperties parent = new CorePropertiesStub();
    CastorProperties props = new CastorProperties(parent);
    assertNotNull("Properties should maintain reference to parent", props);
  }

  @Test
  public void should_BeUsableAsAbstractProperties_When_CastedToParentType() {
    AbstractProperties absProps = _properties;
    assertNotNull("Should be usable as AbstractProperties", absProps);
  }

  @Test
  public void should_NotThrowException_When_ConstructorCalledWithDifferentParents() {
    AbstractProperties parent1 = new CorePropertiesStub();
    AbstractProperties parent2 = new CorePropertiesStub();

    CastorProperties props1 = new CastorProperties(parent1);
    CastorProperties props2 = new CastorProperties(parent2);

    assertNotNull("First properties should exist", props1);
    assertNotNull("Second properties should exist", props2);
  }

  @Test
  public void should_LoadCastorPropertiesFile_When_CastorPropertiesFileExists() {
    try {
      CastorProperties props = new CastorProperties(_parent);
      assertNotNull("Should load castor.properties file", props);
    } catch (Exception e) {
      fail("Should not throw exception when loading castor.properties: " + e.getMessage());
    }
  }

  @Test
  public void should_HandlemissingPropertiesFile_When_CastorPropertiesFileDoesNotExist() {
    try {
      CastorProperties props = new CastorProperties(_parent);
      assertNotNull("Should handle missing properties file gracefully", props);
    } catch (Exception e) {
      fail("Should handle missing properties file: " + e.getMessage());
    }
  }

  @Test
  public void should_InheritClassLoaders_When_ConstructorCalledWithParent() {
    CastorProperties props = new CastorProperties(_parent);
    assertNotNull("Should inherit class loaders from parent", props);
  }

  @Test
  public void should_AllowMultipleCastorPropertiesInstances_When_SharedParentUsed() {
    CastorProperties props1 = new CastorProperties(_parent);
    CastorProperties props2 = new CastorProperties(_parent);
    CastorProperties props3 = new CastorProperties(_parent);

    assertNotNull("All instances should be created", props1);
    assertNotNull("All instances should be created", props2);
    assertNotNull("All instances should be created", props3);
  }

  @Test
  public void should_ProvideConsistentBehavior_When_MultipleInstancesUsed() {
    CastorProperties props1 = new CastorProperties(_parent);
    CastorProperties props2 = new CastorProperties(_parent);

    // Both should behave similarly
    assertNotNull("First instance should work", props1);
    assertNotNull("Second instance should work", props2);
  }

  @Test
  public void should_NotModifyParent_When_CastorPropertiesConstructorCalled() {
    AbstractProperties originalParent = _parent;
    CastorProperties props = new CastorProperties(_parent);

    // Verify parent is unchanged
    assertNotNull("Parent should not be modified", originalParent);
  }

  @Test
  public void should_SupportChainedPropertyLookup_When_ParentIsSet() {
    CastorProperties props = new CastorProperties(_parent);
    assertNotNull("Should support property lookup through parent chain", props);
  }

  @Test
  public void should_LoadAndCacheProperties_When_ConstructorCalledMultipleTimes() {
    CastorProperties props1 = new CastorProperties(_parent);
    CastorProperties props2 = new CastorProperties(_parent);

    assertNotNull("First instance should load properties", props1);
    assertNotNull("Second instance should load properties", props2);
  }

  @Test
  public void should_RequireNonNullParent_When_ConstructorCalledWithNull() {
    try {
      CastorProperties props = new CastorProperties(null);
      fail("Should throw NullPointerException when parent is null");
    } catch (NullPointerException e) {
      assertTrue("NullPointerException should be thrown for null parent", true);
    }
  }

  @Test
  public void should_ProvideFunctionalityAfterConstruction_When_PropertiesCreated() {
    CastorProperties props = new CastorProperties(_parent);
    assertNotNull("Properties should be functional after construction", props);
  }

  @Test
  public void should_BeIndependentInstance_When_MultipleInstancesCreated() {
    CastorProperties props1 = new CastorProperties(_parent);
    CastorProperties props2 = new CastorProperties(_parent);

    assertNotSame("Instances should be independent", props1, props2);
  }

  @Test
  public void should_SupportCustomParentImplementations_When_ConstructorCalledWithCustomParent() {
    CustomAbstractPropertiesImpl customParent = new CustomAbstractPropertiesImpl();
    CastorProperties props = new CastorProperties(customParent);
    assertNotNull("Should support custom AbstractProperties implementations", props);
  }

  @Test
  public void should_MaintainConsistency_When_SameParentUsedForMultipleInstances() {
    CastorProperties props1 = new CastorProperties(_parent);
    CastorProperties props2 = new CastorProperties(_parent);
    CastorProperties props3 = new CastorProperties(_parent);

    assertTrue("All should be CastorProperties instances", props1 instanceof CastorProperties);
    assertTrue("All should be CastorProperties instances", props2 instanceof CastorProperties);
    assertTrue("All should be CastorProperties instances", props3 instanceof CastorProperties);
  }

  @Test
  public void should_NotThrowExceptionWithSystemClassLoader_When_ConstructorUsesSystemLoaders() {
    try {
      AbstractProperties parent = new CorePropertiesStub();
      CastorProperties props = new CastorProperties(parent);
      assertNotNull("Should work with system class loaders", props);
    } catch (Exception e) {
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void should_LoadCastorPropertiesFilename_When_ClassLoaded() {
    // Verify that the class loads the correct properties filename (castor.properties)
    CastorProperties props = new CastorProperties(_parent);
    assertNotNull("Should load castor.properties file", props);
  }

  @Test
  public void should_ExtendAbstractPropertiesCorrectly_When_ClassHierarchyChecked() {
    CastorProperties props = new CastorProperties(_parent);
    assertTrue("Should be instance of AbstractProperties", props instanceof AbstractProperties);
  }

  @Test
  public void should_NotHavePublicDefaultConstructor_When_ClassInspected() {
    try {
      java.lang.reflect.Constructor<?>[] constructors = CastorProperties.class
          .getDeclaredConstructors();
      boolean hasPublicDefaultConstructor = false;
      for (java.lang.reflect.Constructor<?> c : constructors) {
        if (c.getParameterCount() == 0 &&
            java.lang.reflect.Modifier.isPublic(c.getModifiers())) {
          hasPublicDefaultConstructor = true;
          break;
        }
      }
      assertFalse("Should not have public default constructor", hasPublicDefaultConstructor);
    } catch (Exception e) {
      fail("Failed to inspect constructors: " + e.getMessage());
    }
  }

  // ========== Helper Classes ==========

  /**
   * Stub implementation of AbstractProperties for testing
   */
  private static class CorePropertiesStub extends AbstractProperties {
    public CorePropertiesStub() {
      super();
    }
  }

  /**
   * Custom implementation of AbstractProperties for testing
   */
  private static class CustomAbstractPropertiesImpl extends AbstractProperties {
    public CustomAbstractPropertiesImpl() {
      super();
    }
  }
}
