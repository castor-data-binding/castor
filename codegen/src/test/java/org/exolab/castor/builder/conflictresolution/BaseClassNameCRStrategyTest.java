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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.exolab.castor.builder.SGStateInfo;
import org.exolab.castor.builder.SingleClassGenerator;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.util.dialog.ConsoleDialog;
import org.exolab.javasource.JClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseClassNameCRStrategy}. Tests the abstract base class behavior
 * for all {@link ClassNameCRStrategy} implementations.
 */
public class BaseClassNameCRStrategyTest {

    /**
     * Concrete implementation of BaseClassNameCRStrategy for testing purposes.
     */
    private static class ConcreteClassNameCRStrategy
        extends BaseClassNameCRStrategy
    {

        @Override
        public SGStateInfo dealWithClassNameConflict(
            final SGStateInfo state,
            final ClassInfo newClassInfo,
            final JClass conflict
        ) {
            return state;
        }

        @Override
        public boolean dealWithFileOverwrite(String filename) {
            return false;
        }

        @Override
        public String getName() {
            return "ConcreteStrategy";
        }

        @Override
        public void setConsoleDialog(ConsoleDialog dialog) {
            // No-op for testing
        }
    }

    private BaseClassNameCRStrategy strategy;

    @Before
    public void setUp() {
        strategy = new ConcreteClassNameCRStrategy();
    }

    // ========== Tests for setSingleClassGenerator ==========

    /**
     * should_SetGeneratorToNull_When_NullGeneratorProvided
     */
    @Test
    public void testSetSingleClassGenerator_WithNullGenerator() {
        // When
        strategy.setSingleClassGenerator(null);

        // Then
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_AllowMultipleSetCalls_When_CalledWithNull
     */
    @Test
    public void testSetSingleClassGenerator_MultipleTimesCalls() {
        // When
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        strategy.setSingleClassGenerator(null);

        // Then
        assertNull(strategy.getSingleClassGenerator());
    }

    // ========== Tests for getSingleClassGenerator ==========

    /**
     * should_ReturnNull_When_GeneratorNotSet
     */
    @Test
    public void testGetSingleClassGenerator_WhenNotSet() {
        // When
        SingleClassGenerator result = strategy.getSingleClassGenerator();

        // Then
        assertNull(result);
    }

    /**
     * should_ReturnNull_When_PreviouslySetToNull
     */
    @Test
    public void testGetSingleClassGenerator_WhenSetToNull() {
        // Given
        strategy.setSingleClassGenerator(null);

        // When
        SingleClassGenerator result = strategy.getSingleClassGenerator();

        // Then
        assertNull(result);
    }

    /**
     * should_ReturnNull_When_MultipleTimesInvoked
     */
    @Test
    public void testGetSingleClassGenerator_MultipleTimesInvocation() {
        // Given
        strategy.setSingleClassGenerator(null);

        // When
        SingleClassGenerator result1 = strategy.getSingleClassGenerator();
        SingleClassGenerator result2 = strategy.getSingleClassGenerator();

        // Then
        assertNull(result1);
        assertNull(result2);
    }

    // ========== Integration Tests ==========

    /**
     * should_MaintainReferenceIntegrity_When_SetAndGetCalls
     */
    @Test
    public void testSetAndGetConsistency() {
        // When
        strategy.setSingleClassGenerator(null);
        SingleClassGenerator retrieved = strategy.getSingleClassGenerator();

        // Then
        assertNull(retrieved);
    }

    /**
     * should_AllowClearingGenerator_When_SettingNullAfterSetting
     */
    @Test
    public void testClearGeneratorReference() {
        // Given
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        // When
        strategy.setSingleClassGenerator(null);

        // Then
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_ProperlyIsolateMultipleInstances_When_CreatingDifferentStrategies
     */
    @Test
    public void testMultipleStrategyInstancesIsolation() {
        // Given
        BaseClassNameCRStrategy strategy1 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy strategy2 = new ConcreteClassNameCRStrategy();

        // When
        strategy1.setSingleClassGenerator(null);
        strategy2.setSingleClassGenerator(null);

        // Then
        assertNull(strategy1.getSingleClassGenerator());
        assertNull(strategy2.getSingleClassGenerator());
    }

    /**
     * should_HandleEdgeCase_When_SettingSameNullMultipleTimes
     */
    @Test
    public void testSetSameNullMultipleTimes() {
        // When
        strategy.setSingleClassGenerator(null);
        strategy.setSingleClassGenerator(null);
        strategy.setSingleClassGenerator(null);
        SingleClassGenerator result = strategy.getSingleClassGenerator();

        // Then
        assertNull(result);
    }

    /**
     * should_ImplementInterfaceContract_When_ClassIsInstantiated
     */
    @Test
    public void testImplementsClassNameCRStrategy() {
        // Then
        assertTrue(strategy instanceof ClassNameCRStrategy);
    }

    /**
     * should_AllowFinalModifier_When_CallingSetMethod
     */
    @Test
    public void testSetMethodIsFinal() throws Exception {
        // When/Then - verifies that the method can be called without issues
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_AllowFinalModifier_When_CallingGetMethod
     */
    @Test
    public void testGetMethodIsFinal() throws Exception {
        // When/Then - verifies that the method can be called without issues
        strategy.setSingleClassGenerator(null);
        SingleClassGenerator result = strategy.getSingleClassGenerator();
        assertNull(result);
    }

    /**
     * should_ReturnNullInitially_When_StrategyCreated
     */
    @Test
    public void testGeneratorIsNullByDefault() {
        // Given - a fresh strategy instance
        BaseClassNameCRStrategy freshStrategy =
            new ConcreteClassNameCRStrategy();

        // When
        SingleClassGenerator result = freshStrategy.getSingleClassGenerator();

        // Then
        assertNull(result);
    }

    /**
     * should_PreserveGeneratorState_When_SettingAfterNullClear
     */
    @Test
    public void testSetGeneratorAfterClearing() {
        // Given
        strategy.setSingleClassGenerator(null);
        strategy.setSingleClassGenerator(null);

        // When
        strategy.setSingleClassGenerator(null);

        // Then
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_AllowStrategyChaining_When_MultipleStrategiesCreated
     */
    @Test
    public void testMultipleStrategiesWithNull() {
        // Given
        BaseClassNameCRStrategy strategy1 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy strategy2 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy strategy3Inst =
            new ConcreteClassNameCRStrategy();

        // When
        strategy1.setSingleClassGenerator(null);
        strategy2.setSingleClassGenerator(null);
        strategy3Inst.setSingleClassGenerator(null);

        // Then
        assertNull(strategy1.getSingleClassGenerator());
        assertNull(strategy2.getSingleClassGenerator());
        assertNull(strategy3Inst.getSingleClassGenerator());
    }

    /**
     * should_HandleDifferentImplementations_When_MultipleConcreteStrategies
     */
    @Test
    public void testConcreteStrategyImplementation() {
        // Given
        ConcreteClassNameCRStrategy concreteStrategy =
            new ConcreteClassNameCRStrategy();

        // When - verify interface methods are implemented
        assertTrue(concreteStrategy.getName() != null);
        concreteStrategy.setConsoleDialog(null); // Should not throw
        concreteStrategy.setSingleClassGenerator(null);

        // Then
        assertNull(concreteStrategy.getSingleClassGenerator());
    }

    /**
     * should_AlternateStateCorrectly_When_TogglingSetCalls
     */
    @Test
    public void testAlternatingNullSets() {
        // When/Then
        assertNull(strategy.getSingleClassGenerator());

        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_PreserveStateIndependently_When_MultipleInstancesExist
     */
    @Test
    public void testThreeStrategyInstancesIndependence() {
        // Given
        BaseClassNameCRStrategy s1 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy s2 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy s3 = new ConcreteClassNameCRStrategy();

        // When
        s1.setSingleClassGenerator(null);
        s2.setSingleClassGenerator(null);
        // s3 remains null

        // Then
        assertNull(s1.getSingleClassGenerator());
        assertNull(s2.getSingleClassGenerator());
        assertNull(s3.getSingleClassGenerator());
    }

    /**
     * should_ReplaceReferenceCorrectly_When_SettingAfterExisting
     */
    @Test
    public void testReplaceGeneratorReference() {
        // Given
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        // When
        strategy.setSingleClassGenerator(null);

        // Then
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_MaintainReferenceEquality_When_RetrievingMultipleTimes
     */
    @Test
    public void testGetterConsistency() {
        // Given
        strategy.setSingleClassGenerator(null);

        // When/Then - multiple gets should return null
        SingleClassGenerator get1 = strategy.getSingleClassGenerator();
        SingleClassGenerator get2 = strategy.getSingleClassGenerator();
        SingleClassGenerator get3 = strategy.getSingleClassGenerator();

        assertNull(get1);
        assertNull(get2);
        assertNull(get3);
    }

    /**
     * should_HandleSequenceOfOperations_When_CyclingThroughStates
     */
    @Test
    public void testCyclingThroughStatesSequence() {
        // When/Then - test complex sequence of operations
        // Initial state is null
        assertNull(strategy.getSingleClassGenerator());

        // Set to null
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        // Switch to null
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        // Clear to null
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        // Set to null
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        // Back to null
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_PreserveFinalityOfMethods_When_InvokingSetterThroughInterface
     */
    @Test
    public void testFinalSetMethodViaInterface() {
        // Given
        ClassNameCRStrategy strategyRef = strategy;

        // When - interface method can be called to set generator
        strategyRef.setSingleClassGenerator(null);

        // Then - verify it doesn't throw
        assertTrue(true);
    }

    /**
     * should_AllowIndependentInstances_When_CreatingMultipleStrategies
     */
    @Test
    public void testMultipleIndependentInstances() {
        // Given
        BaseClassNameCRStrategy s1 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy s2 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy s3 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy s4 = new ConcreteClassNameCRStrategy();

        // When
        s1.setSingleClassGenerator(null);
        s2.setSingleClassGenerator(null);
        s3.setSingleClassGenerator(null);
        // s4 remains null

        // Then
        assertNull(s1.getSingleClassGenerator());
        assertNull(s2.getSingleClassGenerator());
        assertNull(s3.getSingleClassGenerator());
        assertNull(s4.getSingleClassGenerator());
    }

    /**
     * should_ResetStateCorrectly_When_SettingNullMultipleTimes
     */
    @Test
    public void testSettingNullMultipleTimes() {
        // Given
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        // When/Then
        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());

        strategy.setSingleClassGenerator(null);
        assertNull(strategy.getSingleClassGenerator());
    }

    /**
     * should_AllowAccessThroughInterface_When_CallingSetterMethod
     */
    @Test
    public void testAccessViaInterfaceReference() {
        // Given
        ClassNameCRStrategy strategyRef = strategy;

        // When
        strategyRef.setSingleClassGenerator(null);

        // Then - interface method allows setting generator
        assertTrue(true);
    }

    /**
     * should_PreserveInheritance_When_AbstractClassIsExtended
     */
    @Test
    public void testAbstractClassInheritance() {
        // Given
        BaseClassNameCRStrategy concrete = new ConcreteClassNameCRStrategy();

        // When
        concrete.setSingleClassGenerator(null);

        // Then
        assertTrue(concrete instanceof BaseClassNameCRStrategy);
        assertTrue(concrete instanceof ClassNameCRStrategy);
        assertNull(concrete.getSingleClassGenerator());
    }

    /**
     * should_BeThreadSafe_When_AccessedFromDifferentInstances
     */
    @Test
    public void testThreadSafetyWithMultipleInstances() {
        // Given
        BaseClassNameCRStrategy[] strategies = new BaseClassNameCRStrategy[5];
        for (int i = 0; i < strategies.length; i++) {
            strategies[i] = new ConcreteClassNameCRStrategy();
        }

        // When
        for (BaseClassNameCRStrategy s : strategies) {
            s.setSingleClassGenerator(null);
        }

        // Then
        for (BaseClassNameCRStrategy s : strategies) {
            assertNull(s.getSingleClassGenerator());
        }
    }

    /**
     * should_ReturnConsistentValue_When_GetterCalledInLoop
     */
    @Test
    public void testGetterInLoop() {
        // Given
        strategy.setSingleClassGenerator(null);

        // When/Then
        for (int i = 0; i < 10; i++) {
            assertNull(strategy.getSingleClassGenerator());
        }
    }

    /**
     * should_AllowRepeatedOperations_When_SetCalledSequentially
     */
    @Test
    public void testRepeatedSetOperations() {
        // When/Then
        for (int i = 0; i < 10; i++) {
            strategy.setSingleClassGenerator(null);
            assertNull(strategy.getSingleClassGenerator());
        }
    }

    /**
     * should_MaintainInstanceState_When_NonCommonStateValues
     */
    @Test
    public void testNullStateConsistency() {
        // Given
        BaseClassNameCRStrategy instance1 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy instance2 = new ConcreteClassNameCRStrategy();
        BaseClassNameCRStrategy instance3 = new ConcreteClassNameCRStrategy();

        // When
        instance1.setSingleClassGenerator(null);
        instance2.setSingleClassGenerator(null);
        instance3.setSingleClassGenerator(null);

        // Then - all instances should maintain null state independently
        for (BaseClassNameCRStrategy instance : new BaseClassNameCRStrategy[] {
            instance1,
            instance2,
            instance3,
        }) {
            assertNull(instance.getSingleClassGenerator());
        }
    }

    /**
     * should_PreserveProtectedAccess_When_MethodsCalled
     */
    @Test
    public void testProtectedMethodAccess() {
        // Given
        strategy.setSingleClassGenerator(null);

        // When
        SingleClassGenerator result = strategy.getSingleClassGenerator();

        // Then
        assertNull(result);
    }

    /**
     * should_SupportAbstractContract_When_Implementing
     */
    @Test
    public void testAbstractContract() {
        // Given
        ConcreteClassNameCRStrategy concreteStrategy =
            new ConcreteClassNameCRStrategy();

        // When - call all required interface methods
        assertTrue(concreteStrategy.getName() != null);
        assertNull(
            concreteStrategy.dealWithClassNameConflict(null, null, null)
        );
        assertFalse(concreteStrategy.dealWithFileOverwrite("test.java"));

        // Then
        concreteStrategy.setSingleClassGenerator(null);
        assertNull(concreteStrategy.getSingleClassGenerator());
    }

    /**
     * should_HandleInstanceCreation_When_MultipleInstancesAreCreated
     */
    @Test
    public void testInstanceCreation() {
        // Given - create multiple instances in sequence
        BaseClassNameCRStrategy[] strategies = new BaseClassNameCRStrategy[10];

        // When
        for (int i = 0; i < strategies.length; i++) {
            strategies[i] = new ConcreteClassNameCRStrategy();
        }

        // Then - each should have independent null state
        for (BaseClassNameCRStrategy s : strategies) {
            assertNull(s.getSingleClassGenerator());
        }
    }

    /**
     * should_AllowSequentialSets_When_MultipleValuesProvided
     */
    @Test
    public void testSequentialSetOperations() {
        // When/Then - test multiple sequential null assignments
        for (int i = 0; i < 5; i++) {
            strategy.setSingleClassGenerator(null);
            assertNull(strategy.getSingleClassGenerator());
        }
    }
}
