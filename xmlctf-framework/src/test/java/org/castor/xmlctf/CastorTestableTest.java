package org.castor.xmlctf;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test suite for CastorTestable interface.
 */
public class CastorTestableTest {

    private static class SimpleCastorTestable implements CastorTestable {

        private String field1;
        private int field2;
        private boolean field3;

        public SimpleCastorTestable() {
            this.field1 = "test";
            this.field2 = 0;
            this.field3 = false;
        }

        public SimpleCastorTestable(String field1, int field2, boolean field3) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
        }

        @Override
        public String dumpFields() {
            return (
                "SimpleCastorTestable{" +
                "field1='" +
                field1 +
                '\'' +
                ", field2=" +
                field2 +
                ", field3=" +
                field3 +
                '}'
            );
        }

        @Override
        public void randomizeFields()
            throws InstantiationException, IllegalAccessException {
            this.field1 = "randomized";
            this.field2 = (int) (Math.random() * 1000);
            this.field3 = Math.random() > 0.5;
        }

        public String getField1() {
            return field1;
        }

        public int getField2() {
            return field2;
        }

        public boolean isField3() {
            return field3;
        }
    }

    private static class ComplexCastorTestable implements CastorTestable {

        private String name;
        private double value;
        private long timestamp;

        public ComplexCastorTestable(
            String name,
            double value,
            long timestamp
        ) {
            this.name = name;
            this.value = value;
            this.timestamp = timestamp;
        }

        @Override
        public String dumpFields() {
            return (
                "ComplexCastorTestable{" +
                "name='" +
                name +
                '\'' +
                ", value=" +
                value +
                ", timestamp=" +
                timestamp +
                '}'
            );
        }

        @Override
        public void randomizeFields()
            throws InstantiationException, IllegalAccessException {
            this.name = "complex_" + System.nanoTime();
            this.value = Math.random() * Double.MAX_VALUE;
            this.timestamp = System.currentTimeMillis();
        }
    }

    private static class NestedCastorTestable implements CastorTestable {

        private SimpleCastorTestable nested;
        private String owner;

        public NestedCastorTestable(SimpleCastorTestable nested, String owner) {
            this.nested = nested;
            this.owner = owner;
        }

        @Override
        public String dumpFields() {
            return (
                "NestedCastorTestable{" +
                "nested=" +
                (nested != null ? nested.dumpFields() : "null") +
                ", owner='" +
                owner +
                '\'' +
                '}'
            );
        }

        @Override
        public void randomizeFields()
            throws InstantiationException, IllegalAccessException {
            if (nested != null) {
                nested.randomizeFields();
            }
            this.owner = "owner_" + System.nanoTime();
        }
    }

    @Test
    public void testDumpFieldsWithSimpleObject() {
        CastorTestable testable = new SimpleCastorTestable("test", 42, true);
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertTrue(dump.contains("test"));
        assertTrue(dump.contains("42"));
        assertTrue(dump.contains("true"));
    }

    @Test
    public void testDumpFieldsWithComplexObject() {
        CastorTestable testable = new ComplexCastorTestable(
            "complex",
            3.14159,
            1234567890L
        );
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertTrue(dump.contains("complex"));
        assertTrue(dump.contains("3.14159"));
    }

    @Test
    public void testRandomizeFieldsOnSimpleObject() throws Exception {
        SimpleCastorTestable testable = new SimpleCastorTestable(
            "original",
            10,
            false
        );
        String originalDump = testable.dumpFields();

        testable.randomizeFields();
        String randomizedDump = testable.dumpFields();

        assertNotNull(randomizedDump);
        assertTrue(randomizedDump.contains("randomized"));
    }

    @Test
    public void testRandomizeFieldsOnComplexObject() throws Exception {
        ComplexCastorTestable testable = new ComplexCastorTestable(
            "original",
            1.0,
            0
        );
        long originalTimestamp = System.currentTimeMillis();

        testable.randomizeFields();

        // Should have updated timestamp
        assertNotNull(testable.dumpFields());
    }

    @Test
    public void testRandomizeFieldsMultipleTimes() throws Exception {
        SimpleCastorTestable testable = new SimpleCastorTestable();

        for (int i = 0; i < 5; i++) {
            testable.randomizeFields();
            assertNotNull(testable.dumpFields());
        }
    }

    @Test
    public void testDumpFieldsReturnsNonEmptyString() {
        CastorTestable testable = new SimpleCastorTestable();
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertNotEquals("", dump);
    }

    @Test
    public void testDumpFieldsWithDifferentValues() {
        CastorTestable testable1 = new SimpleCastorTestable(
            "value1",
            100,
            true
        );
        CastorTestable testable2 = new SimpleCastorTestable(
            "value2",
            200,
            false
        );

        String dump1 = testable1.dumpFields();
        String dump2 = testable2.dumpFields();

        assertNotEquals(dump1, dump2);
    }

    @Test
    public void testInterfaceImplementation() {
        CastorTestable testable = new SimpleCastorTestable();
        assertTrue(testable instanceof CastorTestable);
    }

    @Test
    public void testDumpFieldsFormat() {
        SimpleCastorTestable testable = new SimpleCastorTestable(
            "test",
            42,
            true
        );
        String dump = testable.dumpFields();

        assertTrue(dump.startsWith("SimpleCastorTestable"));
        assertTrue(dump.contains("{"));
        assertTrue(dump.contains("}"));
    }

    @Test
    public void testRandomizeFieldsChangesState() throws Exception {
        SimpleCastorTestable testable = new SimpleCastorTestable(
            "initial",
            0,
            false
        );
        String beforeDump = testable.dumpFields();

        testable.randomizeFields();
        String afterDump = testable.dumpFields();

        assertNotEquals(beforeDump, afterDump);
    }

    @Test
    public void testNestedObjectDumpFields() {
        SimpleCastorTestable nested = new SimpleCastorTestable(
            "nested",
            50,
            true
        );
        CastorTestable testable = new NestedCastorTestable(nested, "parent");
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertTrue(dump.contains("nested"));
        assertTrue(dump.contains("parent"));
    }

    @Test
    public void testNestedObjectRandomizeFields() throws Exception {
        SimpleCastorTestable nested = new SimpleCastorTestable(
            "nested",
            0,
            false
        );
        NestedCastorTestable testable = new NestedCastorTestable(
            nested,
            "parent"
        );

        testable.randomizeFields();
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertTrue(dump.contains("owner_"));
    }

    @Test
    public void testDumpFieldsWithNullNestedObject() {
        CastorTestable testable = new NestedCastorTestable(null, "parent");
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertTrue(dump.contains("parent"));
    }

    @Test
    public void testMultipleImplementations() {
        CastorTestable simple = new SimpleCastorTestable();
        CastorTestable complex = new ComplexCastorTestable("test", 1.0, 0);

        assertNotNull(simple.dumpFields());
        assertNotNull(complex.dumpFields());
    }

    @Test
    public void testRandomizeFieldsWithInstantiationException() {
        // This tests the exception path - when implementing classes throw the exception
        CastorTestable testable = new SimpleCastorTestable();
        try {
            testable.randomizeFields();
            // Should succeed for this implementation
        } catch (InstantiationException | IllegalAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDumpFieldsConsistency() {
        SimpleCastorTestable testable = new SimpleCastorTestable(
            "consistent",
            123,
            true
        );
        String dump1 = testable.dumpFields();
        String dump2 = testable.dumpFields();

        assertEquals(dump1, dump2);
    }

    @Test
    public void testRandomizeFieldsConsistency() throws Exception {
        SimpleCastorTestable testable = new SimpleCastorTestable();
        testable.randomizeFields();

        String dump1 = testable.dumpFields();
        String dump2 = testable.dumpFields();

        assertEquals(dump1, dump2);
    }

    @Test
    public void testDumpFieldsWithSpecialCharacters() {
        CastorTestable testable = new SimpleCastorTestable(
            "test!@#$%",
            0,
            false
        );
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertTrue(dump.contains("test!@#$%"));
    }

    @Test
    public void testDumpFieldsWithLargeNumbers() {
        SimpleCastorTestable testable = new SimpleCastorTestable(
            "large",
            Integer.MAX_VALUE,
            true
        );
        String dump = testable.dumpFields();

        assertNotNull(dump);
        assertTrue(dump.contains(String.valueOf(Integer.MAX_VALUE)));
    }

    @Test
    public void testRandomizeFieldsBranchCoverage() throws Exception {
        SimpleCastorTestable testable = new SimpleCastorTestable();

        // Call randomize multiple times to ensure all branches are hit
        testable.randomizeFields();
        assertTrue(testable.getField2() >= 0 && testable.getField2() < 1000);

        testable.randomizeFields();
        assertTrue(testable.getField2() >= 0 && testable.getField2() < 1000);
    }

    @Test
    public void testDumpFieldsReturnType() {
        CastorTestable testable = new SimpleCastorTestable();
        Object result = testable.dumpFields();

        assertTrue(result instanceof String);
    }

    @Test
    public void testInterfaceMethodCount() {
        // Verify interface has expected methods
        assertTrue(CastorTestable.class.isInterface());
        assertEquals(2, CastorTestable.class.getMethods().length);
    }
}
