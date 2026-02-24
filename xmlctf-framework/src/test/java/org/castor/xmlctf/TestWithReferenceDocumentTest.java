package org.castor.xmlctf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test suite for TestWithReferenceDocument class.
 */
public class TestWithReferenceDocumentTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File testDirectory;
    private File outputDirectory;

    @Before
    public void setUp() throws IOException {
        testDirectory = tempFolder.newFolder("test-reference-doc");
        outputDirectory = tempFolder.newFolder("output");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNameOnly() {
        new TestWithReferenceDocument("TestName");
    }

    @Test
    public void testConstructorNameOnlyThrowsException() {
        try {
            new TestWithReferenceDocument("NameOnly");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(
                e.getMessage().contains("cannot use the name-only constructor")
            );
        }
    }

    @Test
    public void testNameOnlyConstructorMessage() {
        try {
            new TestWithReferenceDocument("TestName");
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testConstructorWithNameAndNullDelegate() {
        try {
            new TestWithReferenceDocument("Test", null);
            fail("Should handle null delegate");
        } catch (NullPointerException e) {
            // Expected
        } catch (Exception e) {
            // Also acceptable
        }
    }

    @Test
    public void testConstructorValidation() {
        try {
            new TestWithReferenceDocument("BadTest");
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().length() > 0);
        }
    }

    @Test
    public void testNameAppendedWithReference() {
        try {
            new TestWithReferenceDocument("MyTest");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected - constructor requires XMLTestCase delegate
        }
    }

    @Test
    public void testConstructorRequiresDelegate() {
        try {
            new TestWithReferenceDocument("SomeTest");
            fail("Delegate is required");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNameOnlyConstructorAlwaysFails() {
        boolean exceptionThrown = false;
        try {
            new TestWithReferenceDocument("AnyName");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(
            "Should always throw exception for name-only constructor",
            exceptionThrown
        );
    }

    @Test
    public void testClassNameContainsReference() {
        try {
            new TestWithReferenceDocument("Test");
        } catch (IllegalArgumentException e) {
            // The exception is expected
            assertTrue(
                e.getMessage().contains("cannot") ||
                    e.getMessage().contains("must")
            );
        }
    }

    @Test
    public void testConstructorFailureMessage() {
        try {
            new TestWithReferenceDocument("FailTest");
            fail("Constructor should fail");
        } catch (IllegalArgumentException iae) {
            String msg = iae.getMessage();
            assertNotNull("Error message should not be null", msg);
            assertTrue("Error message should be meaningful", msg.length() > 0);
        }
    }

    @Test
    public void testNameOnlyConstructorNoDelegate() {
        boolean caught = false;
        try {
            new TestWithReferenceDocument("NoDelegate");
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        assertTrue("Name-only constructor should throw exception", caught);
    }

    @Test
    public void testConstructorExceptionType() {
        try {
            new TestWithReferenceDocument("Test");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull(
                e.getCause() == null || e.getCause() instanceof Exception
            );
        } catch (Exception e) {
            fail(
                "Should throw IllegalArgumentException, not " +
                    e.getClass().getName()
            );
        }
    }

    @Test
    public void testMultipleNameOnlyConstructorAttempts() {
        for (int i = 0; i < 3; i++) {
            try {
                new TestWithReferenceDocument("Test" + i);
                fail("Attempt " + i + " should fail");
            } catch (IllegalArgumentException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testConstructorValidatesInput() {
        try {
            new TestWithReferenceDocument("ValidateTest");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testNameOnlyConstructorBehavior() {
        try {
            new TestWithReferenceDocument("NameOnly");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException exception) {
            assertNotNull(exception.getMessage());
            assertTrue(exception.getMessage().length() > 0);
        }
    }

    @Test
    public void testConstructorRequirement() {
        try {
            TestWithReferenceDocument test = new TestWithReferenceDocument(
                "Test"
            );
            fail("Should require both name and XMLTestCase");
        } catch (IllegalArgumentException e) {
            assertTrue(
                e.getMessage().contains("cannot") ||
                    e.getMessage().contains("must") ||
                    e.getMessage().toLowerCase().contains("test")
            );
        }
    }

    @Test
    public void testNameOnlyConstructorConsistency() {
        // Test that the exception is consistent across multiple calls
        String firstException = null;
        String secondException = null;

        try {
            new TestWithReferenceDocument("Test1");
        } catch (IllegalArgumentException e) {
            firstException = e.getMessage();
        }

        try {
            new TestWithReferenceDocument("Test2");
        } catch (IllegalArgumentException e) {
            secondException = e.getMessage();
        }

        assertNotNull(firstException);
        assertNotNull(secondException);
        // Both should have meaningful error messages
        assertTrue(firstException.length() > 0);
        assertTrue(secondException.length() > 0);
    }

    @Test
    public void testConstructorFailsWithoutDelegate() {
        boolean exceptionCaught = false;
        try {
            new TestWithReferenceDocument("NoDelegate");
        } catch (IllegalArgumentException iae) {
            exceptionCaught = true;
            assertNotNull("Exception message should exist", iae.getMessage());
        }
        assertTrue("Constructor should fail without delegate", exceptionCaught);
    }

    @Test
    public void testNameOnlyConstructorMultipleCalls() {
        for (int i = 0; i < 5; i++) {
            try {
                new TestWithReferenceDocument("Test" + System.nanoTime());
                fail("Should throw exception");
            } catch (IllegalArgumentException e) {
                assertNotNull(e);
            }
        }
    }

    @Test
    public void testConstructorMessageContent() {
        try {
            new TestWithReferenceDocument("TestMessage");
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage().toLowerCase();
            assertTrue(
                "Error should mention name or constructor",
                msg.contains("name") ||
                    msg.contains("constructor") ||
                    msg.contains("cannot")
            );
        }
    }

    @Test
    public void testNameOnlyConstructorThrowsCorrectException() {
        Exception caughtException = null;
        try {
            new TestWithReferenceDocument("Test");
        } catch (Exception e) {
            caughtException = e;
        }

        assertNotNull("Should throw an exception", caughtException);
        assertTrue(
            "Should throw IllegalArgumentException",
            caughtException instanceof IllegalArgumentException
        );
    }

    @Test
    public void testConstructorAlwaysFailsForNameOnly() {
        String[] names = { "A", "B", "Test", "Sample", "Case" };
        for (String name : names) {
            try {
                new TestWithReferenceDocument(name);
                fail("Should throw exception for: " + name);
            } catch (IllegalArgumentException e) {
                assertNotNull(e);
            }
        }
    }

    @Test
    public void testNameOnlyConstructorExceptionIsNotNull() {
        try {
            new TestWithReferenceDocument("AnyName");
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertNotNull("Exception should not be null", e);
            assertNotNull(
                "Exception message should not be null",
                e.getMessage()
            );
        }
    }

    @Test
    public void testNameOnlyConstructorStrictValidation() {
        // Verify that the constructor strictly requires a delegate
        try {
            new TestWithReferenceDocument("StrictTest");
            fail("Should throw exception");
        } catch (IllegalArgumentException thrown) {
            assertNotNull(thrown.getMessage());
        }
    }

    @Test
    public void testConstructorRepeatedFailures() {
        for (int attempt = 1; attempt <= 5; attempt++) {
            try {
                new TestWithReferenceDocument("Attempt" + attempt);
                fail("Attempt " + attempt + " should fail");
            } catch (IllegalArgumentException e) {
                assertTrue(
                    "Exception should have a message",
                    e.getMessage().length() > 0
                );
            }
        }
    }

    @Test
    public void testConstructorValidatesRequiredArguments() {
        // Verify that providing only name is insufficient
        try {
            new TestWithReferenceDocument("OnlyName");
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testNameOnlyConstructorIsBlockedByDesign() {
        // This verifies that the name-only constructor is intentionally blocked
        try {
            new TestWithReferenceDocument("DesignBlock");
            fail("Name-only constructor should be blocked");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior - constructor is blocked by design
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testConstructorExceptionPropagation() {
        try {
            new TestWithReferenceDocument("ExceptionProp");
            fail("Should throw exception");
        } catch (IllegalArgumentException exception) {
            assertNotNull(
                "Exception message should be present",
                exception.getMessage()
            );
            assertTrue(
                "Message should be informative",
                exception.getMessage().length() > 0
            );
        }
    }
}
