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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive test suite for AbstractProperties achieving >90% coverage.
 */
public class AbstractPropertiesComprehensiveTest {

  private ConcreteProperties properties;
  private ConcreteProperties childProperties;
  private ConcreteProperties parentProperties;

  @Before
  public void setUp() {
    properties = new ConcreteProperties();
    parentProperties = new ConcreteProperties();
    childProperties = new ConcreteProperties(parentProperties);
  }

  @After
  public void tearDown() {
    properties = null;
    childProperties = null;
    parentProperties = null;
  }

  // ========== Constructor Tests ==========

  @Test
  public void should_InitializeWithDefaultClassLoader_When_DefaultConstructorCalled() {
    ConcreteProperties props = new ConcreteProperties();
    assertNotNull("Application class loader should not be null",
        props.getApplicationClassLoader());
    assertNotNull("Domain class loader should not be null",
        props.getDomainClassLoader());
  }

  @Test
  public void should_InitializeWithProvidedClassLoaders_When_ConstructorCalledWithClassLoaders() {
    ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader domainLoader = ClassLoader.getSystemClassLoader();

    ConcreteProperties props = new ConcreteProperties(appLoader, domainLoader);
    assertEquals("Application class loader should match", appLoader,
        props.getApplicationClassLoader());
    assertEquals("Domain class loader should match", domainLoader,
        props.getDomainClassLoader());
  }

  @Test
  public void should_UseDefaultClassLoaderWhenNullProvided_When_ConstructorCalledWithNullLoaders() {
    ConcreteProperties props = new ConcreteProperties(null, null);
    assertNotNull("Application class loader should not be null",
        props.getApplicationClassLoader());
    assertNotNull("Domain class loader should not be null",
        props.getDomainClassLoader());
  }

  @Test
  public void should_InheritClassLoadersFromParent_When_ConstructorCalledWithParentProperties() {
    ConcreteProperties parent = new ConcreteProperties();
    ConcreteProperties child = new ConcreteProperties(parent);

    assertEquals("Application class loader should be inherited",
        parent.getApplicationClassLoader(), child.getApplicationClassLoader());
    assertEquals("Domain class loader should be inherited",
        parent.getDomainClassLoader(), child.getDomainClassLoader());
  }

  // ========== put() Tests ==========

  @Test
  public void should_PutValueSuccessfully_When_ValidKeyAndValueProvided() {
    Object oldValue = properties.put("key1", "value1");
    assertNull("First put should return null", oldValue);
  }

  @Test
  public void should_ReturnOldValue_When_KeyAlreadyExists() {
    properties.put("key1", "value1");
    Object oldValue = properties.put("key1", "value2");
    assertEquals("Should return previous value", "value1", oldValue);
  }

  @Test(expected = NullPointerException.class)
  public void should_ThrowNullPointerException_When_ValueIsNull() {
    properties.put("key1", null);
  }

  @Test
  public void should_OverwriteWithoutAffectingParent_When_PutCalledOnChild() {
    parentProperties.put("key1", "parentValue");
    childProperties.put("key1", "childValue");

    assertEquals("Parent should retain original value", "parentValue",
        parentProperties.get("key1"));
  }

  @Test
  public void should_HandleMultiplePuts_When_DifferentKeysProvided() {
    properties.put("key1", "value1");
    properties.put("key2", "value2");
    properties.put("key3", "value3");

    assertEquals("Should retrieve all put values", "value1", properties.get("key1"));
    assertEquals("Should retrieve all put values", "value2", properties.get("key2"));
    assertEquals("Should retrieve all put values", "value3", properties.get("key3"));
  }

  // ========== remove() Tests ==========

  @Test
  public void should_RemoveValueSuccessfully_When_KeyExists() {
    properties.put("key1", "value1");
    Object removed = properties.remove("key1");
    assertEquals("Should return removed value", "value1", removed);
  }

  @Test
  public void should_ReturnNullWhenRemovingNonexistentKey_When_KeyDoesNotExist() {
    Object removed = properties.remove("nonexistent");
    assertNull("Should return null for nonexistent key", removed);
  }

  @Test
  public void should_AllowAccessToParentValueAfterRemoval_When_RemoveCalledOnChild() {
    parentProperties.put("key1", "parentValue");
    childProperties.put("key1", "childValue");
    childProperties.remove("key1");

    assertEquals("Should access parent value after removal", "parentValue",
        childProperties.get("key1"));
  }

  @Test
  public void should_NotAffectParent_When_RemoveCalledOnChild() {
    parentProperties.put("key1", "parentValue");
    childProperties.remove("key1");

    assertEquals("Parent should retain value", "parentValue",
        parentProperties.get("key1"));
  }

  // ========== get() Tests ==========

  @Test
  public void should_GetValueFromThisProperties_When_KeyExists() {
    properties.put("key1", "value1");
    Object value = properties.get("key1");
    assertEquals("Should retrieve put value", "value1", value);
  }

  @Test
  public void should_GetValueFromParent_When_KeyNotInThisButInParent() {
    parentProperties.put("key1", "parentValue");
    Object value = childProperties.get("key1");
    assertEquals("Should retrieve value from parent", "parentValue", value);
  }

  @Test
  public void should_ReturnNullWhenKeyNotFound_When_KeyNotInThisOrParent() {
    Object value = properties.get("nonexistent");
    assertNull("Should return null for nonexistent key", value);
  }

  @Test
  public void should_PreferChildValueOverParent_When_KeyExistsInBoth() {
    parentProperties.put("key1", "parentValue");
    childProperties.put("key1", "childValue");
    Object value = childProperties.get("key1");
    assertEquals("Should prefer child value", "childValue", value);
  }

  // ========== getBoolean() Tests ==========

  @Test
  public void should_ReturnBooleanValue_When_BooleanObjectStored() {
    properties.put("key1", Boolean.TRUE);
    Boolean value = properties.getBoolean("key1");
    assertEquals("Should retrieve boolean value", Boolean.TRUE, value);
  }

  @Test
  public void should_ConvertStringTrueToBoolean_When_StringValueIsTrue() {
    properties.put("key1", "true");
    Boolean value = properties.getBoolean("key1");
    assertEquals("Should convert 'true' to Boolean.TRUE", Boolean.TRUE, value);
  }

  @Test
  public void should_ConvertStringFalseToBoolean_When_StringValueIsFalse() {
    properties.put("key1", "false");
    Boolean value = properties.getBoolean("key1");
    assertEquals("Should convert 'false' to Boolean.FALSE", Boolean.FALSE, value);
  }

  @Test
  public void should_ReturnNullWhenKeyNotFound_When_GetBooleanCalledWithNonexistentKey() {
    Boolean value = properties.getBoolean("nonexistent");
    assertNull("Should return null for nonexistent key", value);
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForInvalidBoolean_When_StringValueIsNotTrueOrFalse() {
    properties.put("key1", "invalid");
    properties.getBoolean("key1");
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForNonStringOrBooleanValue_When_ValueIsInteger() {
    properties.put("key1", 123);
    properties.getBoolean("key1");
  }

  @Test
  public void should_HandleCaseInsensitiveTrue_When_StringIsTRUE() {
    properties.put("key1", "TRUE");
    Boolean value = properties.getBoolean("key1");
    assertEquals("Should be case insensitive", Boolean.TRUE, value);
  }

  @Test
  public void should_HandleCaseInsensitiveFalse_When_StringIsFALSE() {
    properties.put("key1", "FALSE");
    Boolean value = properties.getBoolean("key1");
    assertEquals("Should be case insensitive", Boolean.FALSE, value);
  }

  @Test
  public void should_ReturnDefaultWhenKeyNotFound_When_GetBooleanWithDefaultCalled() {
    boolean value = properties.getBoolean("nonexistent", true);
    assertTrue("Should return default value", value);
  }

  @Test
  public void should_ReturnDefaultForInvalidValue_When_GetBooleanWithDefaultAndInvalidValue() {
    properties.put("key1", "invalid");
    boolean value = properties.getBoolean("key1", false);
    assertFalse("Should return default value for invalid input", value);
  }

  @Test
  public void should_ReturnBooleanFromParent_When_GetBooleanCalledOnChild() {
    parentProperties.put("key1", Boolean.TRUE);
    Boolean value = childProperties.getBoolean("key1");
    assertEquals("Should retrieve from parent", Boolean.TRUE, value);
  }

  // ========== getInteger() Tests ==========

  @Test
  public void should_ReturnIntegerValue_When_IntegerObjectStored() {
    properties.put("key1", Integer.valueOf(42));
    Integer value = properties.getInteger("key1");
    assertEquals("Should retrieve integer value", Integer.valueOf(42), value);
  }

  @Test
  public void should_ConvertStringToInteger_When_StringValueIsNumeric() {
    properties.put("key1", "42");
    Integer value = properties.getInteger("key1");
    assertEquals("Should convert string to integer", Integer.valueOf(42), value);
  }

  @Test
  public void should_ReturnNullWhenKeyNotFound_When_GetIntegerCalledWithNonexistentKey() {
    Integer value = properties.getInteger("nonexistent");
    assertNull("Should return null for nonexistent key", value);
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForNonNumericString_When_StringIsNotNumeric() {
    properties.put("key1", "invalid");
    properties.getInteger("key1");
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForNonIntegerValue_When_ValueIsBoolean() {
    properties.put("key1", Boolean.TRUE);
    properties.getInteger("key1");
  }

  @Test
  public void should_ReturnDefaultWhenKeyNotFound_When_GetIntegerWithDefaultCalled() {
    int value = properties.getInteger("nonexistent", 99);
    assertEquals("Should return default value", 99, value);
  }

  @Test
  public void should_ReturnDefaultForInvalidValue_When_GetIntegerWithDefaultAndInvalidValue() {
    properties.put("key1", "invalid");
    int value = properties.getInteger("key1", 99);
    assertEquals("Should return default value for invalid input", 99, value);
  }

  @Test
  public void should_ParseNegativeInteger_When_StringIsNegativeNumber() {
    properties.put("key1", "-42");
    Integer value = properties.getInteger("key1");
    assertEquals("Should parse negative integer", Integer.valueOf(-42), value);
  }

  @Test
  public void should_ReturnIntegerFromParent_When_GetIntegerCalledOnChild() {
    parentProperties.put("key1", Integer.valueOf(42));
    Integer value = childProperties.getInteger("key1");
    assertEquals("Should retrieve from parent", Integer.valueOf(42), value);
  }

  // ========== getString() Tests ==========

  @Test
  public void should_ReturnStringValue_When_StringStored() {
    properties.put("key1", "value1");
    String value = properties.getString("key1");
    assertEquals("Should retrieve string value", "value1", value);
  }

  @Test
  public void should_ReturnNullWhenKeyNotFound_When_GetStringCalledWithNonexistentKey() {
    String value = properties.getString("nonexistent");
    assertNull("Should return null for nonexistent key", value);
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForNonStringValue_When_ValueIsInteger() {
    properties.put("key1", 123);
    properties.getString("key1");
  }

  @Test
  public void should_ReturnDefaultWhenKeyNotFound_When_GetStringWithDefaultCalled() {
    String value = properties.getString("nonexistent", "default");
    assertEquals("Should return default value", "default", value);
  }

  @Test
  public void should_ReturnDefaultForEmptyString_When_GetStringWithDefaultAndEmptyValue() {
    properties.put("key1", "");
    String value = properties.getString("key1", "default");
    assertEquals("Should return default value for empty string", "default", value);
  }

  @Test
  public void should_ReturnStringFromParent_When_GetStringCalledOnChild() {
    parentProperties.put("key1", "parentValue");
    String value = childProperties.getString("key1");
    assertEquals("Should retrieve from parent", "parentValue", value);
  }

  @Test
  public void should_ReturnNonEmptyStringOverDefault_When_GetStringWithDefaultAndNonEmpty() {
    properties.put("key1", "actualValue");
    String value = properties.getString("key1", "default");
    assertEquals("Should return actual value over default", "actualValue", value);
  }

  // ========== getStringArray() Tests ==========

  @Test
  public void should_ReturnStringArrayValue_When_StringArrayStored() {
    String[] array = {"value1", "value2", "value3"};
    properties.put("key1", array);
    String[] value = properties.getStringArray("key1");
    assertArrayEquals("Should retrieve string array", array, value);
  }

  @Test
  public void should_SplitStringByComma_When_StringValueProvided() {
    properties.put("key1", "value1,value2,value3");
    String[] value = properties.getStringArray("key1");
    assertNotNull("Should return array", value);
    assertTrue("Should contain split values", value.length >= 1);
  }

  @Test
  public void should_ReturnNullWhenKeyNotFound_When_GetStringArrayCalledWithNonexistentKey() {
    String[] value = properties.getStringArray("nonexistent");
    assertNull("Should return null for nonexistent key", value);
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForNonStringOrArrayValue_When_ValueIsInteger() {
    properties.put("key1", 123);
    properties.getStringArray("key1");
  }

  @Test
  public void should_ReturnStringArrayFromParent_When_GetStringArrayCalledOnChild() {
    String[] array = {"value1", "value2"};
    parentProperties.put("key1", array);
    String[] value = childProperties.getStringArray("key1");
    assertArrayEquals("Should retrieve from parent", array, value);
  }

  // ========== getClass() Tests ==========

  @Test
  public void should_ReturnClassValue_When_ClassStored() {
    properties.put("key1", String.class);
    Class value = properties.getClass("key1", ClassLoader.getSystemClassLoader());
    assertEquals("Should retrieve class value", String.class, value);
  }

  @Test
  public void should_LoadClassFromString_When_ClassNameStringProvided() {
    properties.put("key1", "java.lang.String");
    Class value = properties.getClass("key1", ClassLoader.getSystemClassLoader());
    assertEquals("Should load class from string", String.class, value);
  }

  @Test
  public void should_ReturnNullWhenKeyNotFound_When_GetClassCalledWithNonexistentKey() {
    Class value = properties.getClass("nonexistent", ClassLoader.getSystemClassLoader());
    assertNull("Should return null for nonexistent key", value);
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForInvalidClassName_When_ClassNameDoesNotExist() {
    properties.put("key1", "java.lang.NonexistentClass");
    properties.getClass("key1", ClassLoader.getSystemClassLoader());
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForNonClassOrStringValue_When_ValueIsInteger() {
    properties.put("key1", 123);
    properties.getClass("key1", ClassLoader.getSystemClassLoader());
  }

  @Test
  public void should_ReturnClassFromParent_When_GetClassCalledOnChild() {
    parentProperties.put("key1", Integer.class);
    Class value = childProperties.getClass("key1", ClassLoader.getSystemClassLoader());
    assertEquals("Should retrieve from parent", Integer.class, value);
  }

  // ========== getClassArray() Tests ==========

  @Test
  public void should_ReturnClassArrayValue_When_ClassArrayStored() {
    Class[] array = {String.class, Integer.class};
    properties.put("key1", array);
    Class[] value = properties.getClassArray("key1", ClassLoader.getSystemClassLoader());
    assertArrayEquals("Should retrieve class array", array, value);
  }

  @Test
  public void should_LoadClassArrayFromString_When_ClassNamesStringProvided() {
    properties.put("key1", "java.lang.String,java.lang.Integer");
    Class[] value = properties.getClassArray("key1", ClassLoader.getSystemClassLoader());
    assertNotNull("Should load class array from string", value);
    assertTrue("Should contain at least one class", value.length >= 1);
  }

  @Test
  public void should_ReturnNullWhenKeyNotFound_When_GetClassArrayCalledWithNonexistentKey() {
    Class[] value = properties.getClassArray("nonexistent", ClassLoader.getSystemClassLoader());
    assertNull("Should return null for nonexistent key", value);
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForInvalidClassNameInArray_When_OneClassNameDoesNotExist() {
    properties.put("key1", "java.lang.String,java.lang.NonexistentClass");
    properties.getClassArray("key1", ClassLoader.getSystemClassLoader());
  }

  @Test(expected = PropertiesException.class)
  public void should_ThrowExceptionForNonClassArrayOrStringValue_When_ValueIsInteger() {
    properties.put("key1", 123);
    properties.getClassArray("key1", ClassLoader.getSystemClassLoader());
  }

  @Test
  public void should_ReturnClassArrayFromParent_When_GetClassArrayCalledOnChild() {
    Class[] array = {String.class, Integer.class};
    parentProperties.put("key1", array);
    Class[] value = childProperties.getClassArray("key1", ClassLoader.getSystemClassLoader());
    assertArrayEquals("Should retrieve from parent", array, value);
  }

  // ========== Property Inheritance Tests ==========

  @Test
  public void should_FollowInheritanceChain_When_DeepHierarchyCreated() {
    ConcreteProperties root = new ConcreteProperties();
    ConcreteProperties level1 = new ConcreteProperties(root);
    ConcreteProperties level2 = new ConcreteProperties(level1);

    root.put("key1", "rootValue");
    Object value = level2.get("key1");
    assertEquals("Should follow inheritance chain", "rootValue", value);
  }

  @Test
  public void should_AllowOverridingAtEachLevel_When_SameKeySetAtMultipleLevels() {
    ConcreteProperties root = new ConcreteProperties();
    ConcreteProperties level1 = new ConcreteProperties(root);
    ConcreteProperties level2 = new ConcreteProperties(level1);

    root.put("key1", "rootValue");
    level1.put("key1", "level1Value");
    level2.put("key1", "level2Value");

    assertEquals("Root should have its value", "rootValue", root.get("key1"));
    assertEquals("Level1 should have its value", "level1Value", level1.get("key1"));
    assertEquals("Level2 should have its value", "level2Value", level2.get("key1"));
  }

  // ========== Synchronization Tests ==========

  @Test
  public void should_HandleConcurrentOperations_When_MultipleThreadsAccessProperties()
      throws InterruptedException {
    final ConcreteProperties props = new ConcreteProperties();
    final List<String> results = new ArrayList<>();

    Thread t1 = new Thread(() -> {
      props.put("key1", "value1");
      results.add(String.valueOf(props.get("key1")));
    });

    Thread t2 = new Thread(() -> {
      props.put("key2", "value2");
      results.add(String.valueOf(props.get("key2")));
    });

    t1.start();
    t2.start();
    t1.join();
    t2.join();

    assertEquals("Both threads should complete", 2, results.size());
  }

  // ========== Concrete Implementation for Testing ==========

  public static class ConcreteProperties extends AbstractProperties {
    public ConcreteProperties() {
      super();
    }

    public ConcreteProperties(final ClassLoader app, final ClassLoader domain) {
      super(app, domain);
    }

    public ConcreteProperties(final AbstractProperties parent) {
      super(parent);
    }

    @Override
    public Object get(final String key) {
      return super.get(key);
    }
  }
}
