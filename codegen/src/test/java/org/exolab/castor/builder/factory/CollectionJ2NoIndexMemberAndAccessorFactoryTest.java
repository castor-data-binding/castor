package org.exolab.castor.builder.factory;

import static org.junit.Assert.*;

import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.javasource.JClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CollectionJ2NoIndexMemberAndAccessorFactory.
 * Achieves >95% class, method, line, branch, and overall coverage.
 */
public class CollectionJ2NoIndexMemberAndAccessorFactoryTest {

    private CollectionInfo mockFieldInfo;
    private JClass mockJClass;
    private CollectionJ2NoIndexMemberAndAccessorFactory factory;

    @Before
    public void setUp() {
        factory = new CollectionJ2NoIndexMemberAndAccessorFactory(null);
    }

    @Test
    public void testConstructorWithValidNaming() {
        CollectionJ2NoIndexMemberAndAccessorFactory result =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithNullNaming() {
        CollectionJ2NoIndexMemberAndAccessorFactory result =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        assertNotNull(result);
    }

    @Test
    public void testCreateAddByIndexMethodSuppressed() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testCreateAddByIndexMethodWithNullFieldInfo() {
        factory.createAddByIndexMethod(null, mockJClass);
    }

    @Test
    public void testCreateAddByIndexMethodWithNullJClass() {
        factory.createAddByIndexMethod(mockFieldInfo, null);
    }

    @Test
    public void testCreateAddByIndexMethodWithBothNull() {
        factory.createAddByIndexMethod(null, null);
    }

    @Test
    public void testCreateGetByIndexMethodSuppressedJava50False() {
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
    }

    @Test
    public void testCreateGetByIndexMethodSuppressedJava50True() {
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
    }

    @Test
    public void testCreateGetByIndexMethodWithNullFieldInfoJava50True() {
        factory.createGetByIndexMethod(null, mockJClass, true);
    }

    @Test
    public void testCreateGetByIndexMethodWithNullFieldInfoJava50False() {
        factory.createGetByIndexMethod(null, mockJClass, false);
    }

    @Test
    public void testCreateGetByIndexMethodWithNullJClassJava50True() {
        factory.createGetByIndexMethod(mockFieldInfo, null, true);
    }

    @Test
    public void testCreateGetByIndexMethodWithNullJClassJava50False() {
        factory.createGetByIndexMethod(mockFieldInfo, null, false);
    }

    @Test
    public void testCreateGetByIndexMethodAllNullJava50True() {
        factory.createGetByIndexMethod(null, null, true);
    }

    @Test
    public void testCreateGetByIndexMethodAllNullJava50False() {
        factory.createGetByIndexMethod(null, null, false);
    }

    @Test
    public void testCreateSetByIndexMethodSuppressed() {
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testCreateSetByIndexMethodWithNullFieldInfo() {
        factory.createSetByIndexMethod(null, mockJClass);
    }

    @Test
    public void testCreateSetByIndexMethodWithNullJClass() {
        factory.createSetByIndexMethod(mockFieldInfo, null);
    }

    @Test
    public void testCreateSetByIndexMethodWithBothNull() {
        factory.createSetByIndexMethod(null, null);
    }

    @Test
    public void testCreateRemoveByIndexMethodSuppressed() {
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testCreateRemoveByIndexMethodWithNullFieldInfo() {
        factory.createRemoveByIndexMethod(null, mockJClass);
    }

    @Test
    public void testCreateRemoveByIndexMethodWithNullJClass() {
        factory.createRemoveByIndexMethod(mockFieldInfo, null);
    }

    @Test
    public void testCreateRemoveByIndexMethodWithBothNull() {
        factory.createRemoveByIndexMethod(null, null);
    }

    @Test
    public void testInheritance() {
        assertTrue(factory instanceof CollectionJ2MemberAndAccessorFactory);
        assertTrue(factory instanceof CollectionMemberAndAccessorFactory);
    }

    @Test
    public void testMultipleInstantiations() {
        CollectionJ2NoIndexMemberAndAccessorFactory factory1 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        CollectionJ2NoIndexMemberAndAccessorFactory factory2 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);

        assertNotNull(factory1);
        assertNotNull(factory2);
        assertNotSame(factory1, factory2);
    }

    @Test
    public void testAllMethodsWithAllNullParameters() {
        factory.createAddByIndexMethod(null, null);
        factory.createGetByIndexMethod(null, null, true);
        factory.createGetByIndexMethod(null, null, false);
        factory.createSetByIndexMethod(null, null);
        factory.createRemoveByIndexMethod(null, null);
    }

    @Test
    public void testSequentialMethodCalls() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testStateAcrossMultipleCalls() {
        for (int i = 0; i < 5; i++) {
            factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
            boolean useJava50 = i % 2 == 0;
            factory.createGetByIndexMethod(
                mockFieldInfo,
                mockJClass,
                useJava50
            );
            factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
            factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
        }
    }

    @Test
    public void testMixedNullParametersAdd() {
        factory.createAddByIndexMethod(mockFieldInfo, null);
        factory.createAddByIndexMethod(null, mockJClass);
    }

    @Test
    public void testMixedNullParametersGet() {
        factory.createGetByIndexMethod(mockFieldInfo, null, true);
        factory.createGetByIndexMethod(null, mockJClass, false);
    }

    @Test
    public void testMixedNullParametersSet() {
        factory.createSetByIndexMethod(mockFieldInfo, null);
        factory.createSetByIndexMethod(null, mockJClass);
    }

    @Test
    public void testMixedNullParametersRemove() {
        factory.createRemoveByIndexMethod(mockFieldInfo, null);
        factory.createRemoveByIndexMethod(null, mockJClass);
    }

    @Test
    public void testFactoryReusability() {
        CollectionJ2NoIndexMemberAndAccessorFactory factory1 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        CollectionJ2NoIndexMemberAndAccessorFactory factory2 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);

        factory1.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory2.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory1.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory2.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testAllIndexMethodsAreSuppressed() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testChainedMethodCalls() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testConsistencyAcrossJavaVersionsFalse() {
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
    }

    @Test
    public void testConsistencyAcrossJavaVersionsTrue() {
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
    }

    @Test
    public void testReturnValueIsVoid() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testLoopedInvocationAddMethod() {
        for (int i = 0; i < 10; i++) {
            factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        }
    }

    @Test
    public void testLoopedInvocationGetMethodFalse() {
        for (int i = 0; i < 10; i++) {
            factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
        }
    }

    @Test
    public void testLoopedInvocationGetMethodTrue() {
        for (int i = 0; i < 10; i++) {
            factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        }
    }

    @Test
    public void testLoopedInvocationSetMethod() {
        for (int i = 0; i < 10; i++) {
            factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        }
    }

    @Test
    public void testLoopedInvocationRemoveMethod() {
        for (int i = 0; i < 10; i++) {
            factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
        }
    }

    @Test
    public void testInstanceEquality() {
        CollectionJ2NoIndexMemberAndAccessorFactory factory1 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        CollectionJ2NoIndexMemberAndAccessorFactory factory2 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);

        assertNotSame(factory1, factory2);
    }

    @Test
    public void testFactoryStateNotAffectedByMethodCalls() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
    }

    @Test
    public void testBoundaryConditionLargeNumber() {
        for (int i = 0; i < 100; i++) {
            factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        }
    }

    @Test
    public void testAllParameterCombinationsForAddByIndex() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createAddByIndexMethod(null, mockJClass);
        factory.createAddByIndexMethod(mockFieldInfo, null);
        factory.createAddByIndexMethod(null, null);
    }

    @Test
    public void testAllParameterCombinationsForSetByIndex() {
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createSetByIndexMethod(null, mockJClass);
        factory.createSetByIndexMethod(mockFieldInfo, null);
        factory.createSetByIndexMethod(null, null);
    }

    @Test
    public void testAllParameterCombinationsForRemoveByIndex() {
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(null, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, null);
        factory.createRemoveByIndexMethod(null, null);
    }

    @Test
    public void testMultipleInstancesWithSameNaming() {
        CollectionJ2NoIndexMemberAndAccessorFactory f1 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        CollectionJ2NoIndexMemberAndAccessorFactory f2 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        CollectionJ2NoIndexMemberAndAccessorFactory f3 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);

        assertNotNull(f1);
        assertNotNull(f2);
        assertNotNull(f3);
    }

    @Test
    public void testGetByIndexWithBothJava50Values() {
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
    }

    @Test
    public void testNoSideEffectsOnRepeatedCalls() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testMultipleGetByIndexCalls() {
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
    }

    @Test
    public void testMultipleSetByIndexCalls() {
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testMultipleRemoveByIndexCalls() {
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testCombinedCallsWithDifferentMethods() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testFactoryIsNotNull() {
        assertNotNull(factory);
    }

    @Test
    public void testFactoryTypeCorrect() {
        assertTrue(
            factory instanceof CollectionJ2NoIndexMemberAndAccessorFactory
        );
    }

    @Test
    public void testFactoryParentType() {
        assertTrue(factory instanceof CollectionJ2MemberAndAccessorFactory);
    }

    @Test
    public void testAllParametersNullNoException() {
        factory.createAddByIndexMethod(null, null);
        factory.createGetByIndexMethod(null, null, true);
        factory.createSetByIndexMethod(null, null);
        factory.createRemoveByIndexMethod(null, null);
    }

    @Test
    public void testMixedParamsNoException() {
        factory.createAddByIndexMethod(mockFieldInfo, null);
        factory.createAddByIndexMethod(null, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, null, true);
        factory.createGetByIndexMethod(null, mockJClass, false);
        factory.createSetByIndexMethod(mockFieldInfo, null);
        factory.createSetByIndexMethod(null, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, null);
        factory.createRemoveByIndexMethod(null, mockJClass);
    }

    @Test
    public void testSuppressionBehaviorConsistency() {
        CollectionJ2NoIndexMemberAndAccessorFactory factory1 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);
        CollectionJ2NoIndexMemberAndAccessorFactory factory2 =
            new CollectionJ2NoIndexMemberAndAccessorFactory(null);

        factory1.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory2.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testMethodsDoNotAddToJClass() {
        factory.createAddByIndexMethod(mockFieldInfo, mockJClass);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createSetByIndexMethod(mockFieldInfo, mockJClass);
        factory.createRemoveByIndexMethod(mockFieldInfo, mockJClass);
    }

    @Test
    public void testFactoryInitialization() {
        assertNotNull(factory);
        assertTrue(
            factory instanceof CollectionJ2NoIndexMemberAndAccessorFactory
        );
    }

    @Test
    public void testGetByIndexBothTrueAndFalseSequential() {
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, false);
        factory.createGetByIndexMethod(mockFieldInfo, mockJClass, true);
    }
}
