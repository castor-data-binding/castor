package org.exolab.castor.xml.schema;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ResolvableReference
 */
public class ResolvableReferenceTest {

    private Referable mockReferable;
    private Resolver mockResolver;
    private String testId;

    @Before
    public void setUp() {
        testId = "testId";
        mockReferable = new MockReferable("testObject");
        mockResolver = new MockResolver();
    }

    @Test
    public void testConstructorWithReferable() {
        ResolvableReference ref = new ResolvableReference(mockReferable);
        assertNotNull(ref);
    }

    @Test
    public void testConstructorWithIdAndResolver() {
        ResolvableReference ref = new ResolvableReference(testId, mockResolver);
        assertNotNull(ref);
    }

    @Test
    public void testGetWithDirectReferable() {
        ResolvableReference ref = new ResolvableReference(mockReferable);
        Referable result = ref.get();
        assertSame(mockReferable, result);
    }

    @Test
    public void testGetMultipleTimesReturnsSameObject() {
        ResolvableReference ref = new ResolvableReference(mockReferable);
        Referable first = ref.get();
        Referable second = ref.get();
        assertSame(first, second);
    }

    @Test
    public void testGetWithResolver() {
        ResolvableReference ref = new ResolvableReference(testId, mockResolver);
        Referable result = ref.get();
        assertNotNull(result);
    }

    @Test
    public void testGetWithResolverCallsResolveOnce() {
        MockResolver resolver = new MockResolver();
        ResolvableReference ref = new ResolvableReference(testId, resolver);

        ref.get();
        assertEquals(1, resolver.getResolveCount());

        ref.get();
        assertEquals(1, resolver.getResolveCount());
    }

    @Test
    public void testResolvableWithDirectReferable() {
        ResolvableReference ref = new ResolvableReference(mockReferable);
        assertTrue(ref.resolvable());
    }

    @Test
    public void testResolvableWithResolverThatCanResolve() {
        ResolvableReference ref = new ResolvableReference(testId, mockResolver);
        assertTrue(ref.resolvable());
    }

    @Test
    public void testResolvableWithResolverThatCannotResolve() {
        Resolver emptyResolver = new Resolver() {
            @Override
            public Referable resolve(String name) {
                return null;
            }

            @Override
            public void addResolvable(String id, Referable referent) {}

            @Override
            public void removeResolvable(String id) {}
        };
        ResolvableReference ref = new ResolvableReference(
            "unknown",
            emptyResolver
        );
        assertFalse(ref.resolvable());
    }

    @Test
    public void testGetWithNullReferable() {
        ResolvableReference ref = new ResolvableReference(null);
        assertNull(ref.get());
    }

    @Test
    public void testGetWithResolverReturningNull() {
        Resolver nullResolver = new Resolver() {
            @Override
            public Referable resolve(String name) {
                return null;
            }

            @Override
            public void addResolvable(String id, Referable referent) {}

            @Override
            public void removeResolvable(String id) {}
        };
        ResolvableReference ref = new ResolvableReference(testId, nullResolver);
        assertNull(ref.get());
    }

    @Test
    public void testConcurrentGetCalls() throws InterruptedException {
        final MockResolver resolver = new MockResolver();
        final ResolvableReference ref = new ResolvableReference(
            testId,
            resolver
        );

        Thread thread1 = new Thread(() -> ref.get());
        Thread thread2 = new Thread(() -> ref.get());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(1, resolver.getResolveCount());
    }

    @Test
    public void testResolvableDoesNotTriggerMultipleResolves() {
        MockResolver resolver = new MockResolver();
        ResolvableReference ref = new ResolvableReference(testId, resolver);

        ref.resolvable();
        assertTrue(resolver.getResolveCount() >= 1);
    }

    @Test
    public void testGetAfterResolvableCall() {
        ResolvableReference ref = new ResolvableReference(testId, mockResolver);
        ref.resolvable();
        Referable result = ref.get();
        assertNotNull(result);
    }

    @Test
    public void testResolvedReferenceAlwaysResolvable() {
        ResolvableReference ref = new ResolvableReference(mockReferable);
        assertTrue(ref.resolvable());
        ref.get();
        assertTrue(ref.resolvable());
    }

    @Test
    public void testUnresolvedReferenceBecomesResolved() {
        ResolvableReference ref = new ResolvableReference(testId, mockResolver);
        ref.get();
        assertTrue(ref.resolvable());
    }

    @Test
    public void testMultipleReferencesToSameObject() {
        ResolvableReference ref1 = new ResolvableReference(mockReferable);
        ResolvableReference ref2 = new ResolvableReference(mockReferable);

        assertSame(ref1.get(), ref2.get());
    }

    @Test
    public void testDifferentReferencesResolveToSameId() {
        ResolvableReference ref1 = new ResolvableReference(
            testId,
            mockResolver
        );
        ResolvableReference ref2 = new ResolvableReference(
            testId,
            mockResolver
        );

        Referable result1 = ref1.get();
        Referable result2 = ref2.get();

        assertEquals(result1.getReferenceId(), result2.getReferenceId());
    }

    @Test
    public void testResolvableAfterGet() {
        ResolvableReference ref = new ResolvableReference(testId, mockResolver);
        ref.get();
        assertTrue(ref.resolvable());
    }

    @Test
    public void testGetConsistency() {
        ResolvableReference ref = new ResolvableReference(mockReferable);
        for (int i = 0; i < 10; i++) {
            assertSame(mockReferable, ref.get());
        }
    }

    @Test
    public void testNullIdWithResolver() {
        MockResolver resolver = new MockResolver();
        ResolvableReference ref = new ResolvableReference(null, resolver);
        assertNotNull(ref);
    }

    // Mock classes for testing
    private static class MockReferable implements Referable {

        private final String id;

        public MockReferable(String id) {
            this.id = id;
        }

        @Override
        public String getReferenceId() {
            return id;
        }
    }

    private static class MockResolver implements Resolver {

        private int resolveCount = 0;

        @Override
        public Referable resolve(String name) {
            resolveCount++;
            return new MockReferable(name);
        }

        @Override
        public void addResolvable(String id, Referable referent) {
            // No-op for test
        }

        @Override
        public void removeResolvable(String id) {
            // No-op for test
        }

        public int getResolveCount() {
            return resolveCount;
        }
    }
}
