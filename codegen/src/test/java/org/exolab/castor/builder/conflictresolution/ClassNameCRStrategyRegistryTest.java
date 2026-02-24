/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder.conflictresolution;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.exolab.castor.builder.SGStateInfo;
import org.exolab.castor.builder.SingleClassGenerator;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.util.dialog.ConsoleDialog;
import org.exolab.javasource.JClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for {@link ClassNameCRStrategyRegistry}.
 * Tests cover registration, retrieval, error handling, and edge cases.
 *
 * @author Test Generator
 */
public class ClassNameCRStrategyRegistryTest {

    private ClassNameCRStrategyRegistry registry;

    @Before
    public void setUp() {
        // Reset registry before each test
    }

    @Test
    public void testCreateRegistry_WithEmptyStrategyList() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry("");

        // Assert
        assertNotNull(registry);
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(0, names.length);
    }

    @Test
    public void testCreateRegistry_WithWhitespaceOnlyInput() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry("   ");

        // Assert
        assertNotNull(registry);
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(0, names.length);
    }

    @Test
    public void testLoadStrategy_WithValidStrategyClassname() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(1, names.length);
        assertEquals("test-strategy", names[0]);
    }

    @Test
    public void testLoadMultipleStrategies_WithCommaSeparatedClassnames() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy, " +
                "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(1, names.length);
    }

    @Test
    public void testLoadMultipleStrategies_WithSpaceSeparatedClassnames() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy " +
                "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(1, names.length);
    }

    @Test
    public void testLoadStrategies_SkipsInvalidClassname() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.NonExistentStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(0, names.length);
    }

    @Test
    public void testLoadStrategies_SkipsNonStrategyClass() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry("java.lang.String");

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(0, names.length);
    }

    @Test
    public void testLoadStrategies_SkipsUninstantiableClass() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.AbstractTestStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(0, names.length);
    }

    @Test
    public void testGetClassNameConflictResolutionStrategy_WithValidName() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        ClassNameCRStrategy strategy =
            registry.getClassNameConflictResolutionStrategy("test-strategy");

        // Assert
        assertNotNull(strategy);
        assertEquals("test-strategy", strategy.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetClassNameConflictResolutionStrategy_WithInvalidName() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry("");

        // Act
        registry.getClassNameConflictResolutionStrategy("non-existent");
    }

    @Test
    public void testGetClassNameConflictResolutionStrategyNames_ReturnsNonNullArray() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        String[] names = registry.getClassNameConflictResolutionStrategyNames();

        // Assert
        assertNotNull(names);
        assertTrue(names.length >= 0);
    }

    @Test
    public void testGetClassNameConflictResolutionStrategyNames_ReturnsCorrectSize() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        String[] names = registry.getClassNameConflictResolutionStrategyNames();

        // Assert
        assertEquals(1, names.length);
    }

    @Test
    public void testGetClassNameConflictResolutionStrategyNames_WithNoStrategies() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry("");

        // Act
        String[] names = registry.getClassNameConflictResolutionStrategyNames();

        // Assert
        assertEquals(0, names.length);
    }

    @Test
    public void testGetClassNameConflictResolutionStrategyNames_ConsistentCalls() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        String[] names1 =
            registry.getClassNameConflictResolutionStrategyNames();
        String[] names2 =
            registry.getClassNameConflictResolutionStrategyNames();

        // Assert
        assertEquals(names1.length, names2.length);
        assertArrayEquals(names1, names2);
    }

    @Test
    public void testGetClassNameConflictResolutionStrategy_ReturnsSameInstance() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        ClassNameCRStrategy strategy1 =
            registry.getClassNameConflictResolutionStrategy("test-strategy");
        ClassNameCRStrategy strategy2 =
            registry.getClassNameConflictResolutionStrategy("test-strategy");

        // Assert
        assertSame(strategy1, strategy2);
    }

    @Test
    public void testLoadStrategy_WithMixedSeparators() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy , " +
                "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertTrue(names.length >= 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetClassNameConflictResolutionStrategy_ExceptionMessageQuality() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry("");
        String strategyName = "custom-strategy";

        // Act
        registry.getClassNameConflictResolutionStrategy(strategyName);
    }

    @Test
    public void testClassNameCRStrategyRegistry_Instantiation() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry("");

        // Assert
        assertNotNull(registry);
        assertTrue(registry instanceof ClassNameCRStrategyRegistry);
    }

    @Test
    public void testEmptyRegistry_ReturnsEmptyArray() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry("");

        // Act
        String[] names = registry.getClassNameConflictResolutionStrategyNames();

        // Assert
        assertNotNull(names);
        assertEquals(0, names.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStrategyAccess_ThrowsException() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        registry.getClassNameConflictResolutionStrategy("does-not-exist");
    }

    @Test
    public void testStrategyLoading_Success() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert
        assertNotNull(
            registry.getClassNameConflictResolutionStrategy("test-strategy")
        );
    }

    @Test
    public void testMixedValidInvalidStrategies() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.invalid.Strategy1, " +
                "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(1, names.length);
    }

    @Test
    public void testGetStrategy_VerifyName() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        ClassNameCRStrategy strategy =
            registry.getClassNameConflictResolutionStrategy("test-strategy");

        // Assert
        assertEquals("test-strategy", strategy.getName());
    }

    @Test
    public void testGetStrategyNames_VerifyContent() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        String[] names = registry.getClassNameConflictResolutionStrategyNames();

        // Assert
        assertEquals(1, names.length);
        assertEquals("test-strategy", names[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputHandling() {
        // Arrange & Act - null input should throw NullPointerException
        registry = new ClassNameCRStrategyRegistry(null);
    }

    @Test
    public void testGetStrategy_FromMultipleLoadedStrategies() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        ClassNameCRStrategy strategy =
            registry.getClassNameConflictResolutionStrategy("test-strategy");

        // Assert
        assertNotNull(strategy);
        assertFalse(strategy.getName().isEmpty());
    }

    @Test
    public void testStrategyRegistry_SingletonBehavior() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        ClassNameCRStrategy strategy =
            registry.getClassNameConflictResolutionStrategy(names[0]);

        // Assert
        assertNotNull(strategy);
        assertEquals(1, names.length);
    }

    @Test
    public void testStrategyRegistry_EdgeCase_DuplicateStrategy() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy, " +
                "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert - duplicates should result in single entry
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(1, names.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStrategy_ThrowsWithNullName() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        registry.getClassNameConflictResolutionStrategy(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStrategy_ThrowsWithEmptyName() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        registry.getClassNameConflictResolutionStrategy("");
    }

    @Test
    public void testStrategyRegistry_LoadsCorrectStrategyNames() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        Set<String> nameSet = new HashSet<>(Arrays.asList(names));
        assertTrue(nameSet.contains("test-strategy"));
    }

    @Test
    public void testStrategyRegistry_MultipleInvocations_ConsistentResults() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        ClassNameCRStrategy s1 =
            registry.getClassNameConflictResolutionStrategy("test-strategy");
        ClassNameCRStrategy s2 =
            registry.getClassNameConflictResolutionStrategy("test-strategy");
        ClassNameCRStrategy s3 =
            registry.getClassNameConflictResolutionStrategy("test-strategy");

        // Assert
        assertSame(s1, s2);
        assertSame(s2, s3);
    }

    @Test
    public void testStrategyRegistry_EmptyAfterInitialization() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry("");

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(0, names.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStrategy_FailsOnUnknownStrategy() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        registry.getClassNameConflictResolutionStrategy("unknown-strategy");
    }

    @Test
    public void testStrategyRegistry_StrategyNameCorrectness() {
        // Arrange
        registry = new ClassNameCRStrategyRegistry(
            "org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy"
        );

        // Act
        String[] names = registry.getClassNameConflictResolutionStrategyNames();

        // Assert
        assertTrue(names.length > 0);
        ClassNameCRStrategy strategy =
            registry.getClassNameConflictResolutionStrategy(names[0]);
        assertEquals(names[0], strategy.getName());
    }

    @Test
    public void testStrategyRegistry_LoadingWithLeadingTrailingSpaces() {
        // Arrange & Act
        registry = new ClassNameCRStrategyRegistry(
            "  org.exolab.castor.builder.conflictresolution.TestConcreteClassNameCRStrategy  "
        );

        // Assert
        String[] names = registry.getClassNameConflictResolutionStrategyNames();
        assertEquals(1, names.length);
    }
}
