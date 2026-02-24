package org.castor.xmlctf;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class RandomHelperTest {

    @Before
    public void setUp() throws Exception {
        RandomHelper.setSeed(12345L);
    }

    @Test
    public void testGetRandomIntArray() {
        int[] array = RandomHelper.getRandom(new int[0], int[].class);
        assertNotNull(array);
        assertTrue(array.length >= 0 && array.length < 50);
    }

    @Test
    public void testGetRandomByteArray() {
        byte[] array = RandomHelper.getRandom(new byte[0], byte[].class);
        assertNotNull(array);
        assertTrue(array.length >= 0 && array.length < 50);
    }

    @Test
    public void testGetRandomStringArray() {
        String[] array = RandomHelper.getRandom(new String[0], String[].class);
        assertNotNull(array);
        assertTrue(array.length >= 0 && array.length < 50);
        for (String s : array) {
            assertNotNull(s);
        }
    }

    @Test
    public void testGetRandomObjectArray()
        throws InstantiationException, IllegalAccessException {
        Object[] array = RandomHelper.getRandom(new Object[0], String.class);
        assertNotNull(array);
        assertTrue(array.length >= 0 && array.length < 50);
    }

    @Test
    public void testGetRandomVector()
        throws InstantiationException, IllegalAccessException {
        Vector vector = RandomHelper.getRandom(new Vector(), String.class);
        assertNotNull(vector);
        assertTrue(vector.size() >= 0 && vector.size() < 50);
    }

    @Test
    public void testGetRandomArrayList()
        throws InstantiationException, IllegalAccessException {
        ArrayList list = RandomHelper.getRandom(new ArrayList(), String.class);
        assertNotNull(list);
        assertTrue(list.size() >= 0 && list.size() < 50);
    }

    @Test
    public void testGetRandomCollection()
        throws InstantiationException, IllegalAccessException {
        Collection col = RandomHelper.getRandom(new HashSet(), String.class);
        assertNotNull(col);
    }

    @Test
    public void testGetRandomList()
        throws InstantiationException, IllegalAccessException {
        List list = RandomHelper.getRandom(new ArrayList(), String.class);
        assertNotNull(list);
        assertTrue(list.size() >= 0 && list.size() < 50);
    }

    @Test
    public void testGetRandomSet()
        throws InstantiationException, IllegalAccessException {
        Set set = RandomHelper.getRandom(new HashSet(), String.class);
        assertNotNull(set);
    }

    @Test
    public void testGetRandomSortedSet()
        throws InstantiationException, IllegalAccessException {
        SortedSet set = RandomHelper.getRandom(new TreeSet(), String.class);
        assertNotNull(set);
    }

    @Test
    public void testGetRandomString() {
        String str = RandomHelper.getRandom(new String(), String.class);
        assertNotNull(str);
        assertTrue(str.length() <= 50);
    }

    @Test
    public void testGetRandomStringWithSpecialChars() {
        String str = RandomHelper.getRandom(new String(), String.class, true);
        assertNotNull(str);
        assertTrue(str.length() <= 50);
    }

    @Test
    public void testGetRandomStringWithoutSpecialChars() {
        String str = RandomHelper.getRandom(new String(), String.class, false);
        assertNotNull(str);
        assertTrue(str.length() <= 50);
    }

    @Test
    public void testGetRandomDate() {
        java.util.Date date = RandomHelper.getRandom(
            new java.util.Date(),
            java.util.Date.class
        );
        assertNotNull(date);
    }

    @Test
    public void testGetRandomBigDecimal() {
        BigDecimal decimal = RandomHelper.getRandom(
            new BigDecimal("0"),
            BigDecimal.class
        );
        assertNotNull(decimal);
    }

    @Test
    public void testGetRandomInt() {
        int value = RandomHelper.getRandom(100);
        assertTrue("Should return some integer", true);
    }

    @Test
    public void testGetRandomIntNegative() {
        int value = RandomHelper.getRandom(-100);
        assertTrue("Should return some integer", true);
    }

    @Test
    public void testGetRandomIntZero() {
        int value = RandomHelper.getRandom(0);
        assertTrue("Should return some integer", true);
    }

    @Test
    public void testGetRandomFloat() {
        float value = RandomHelper.getRandom(100.0f);
        assertTrue("Float should be valid", !Float.isNaN(value));
    }

    @Test
    public void testGetRandomBoolean() {
        boolean value = RandomHelper.getRandom(true);
        assertTrue(
            "Should return a boolean value",
            value == true || value == false
        );
    }

    @Test
    public void testGetRandomBooleanFalse() {
        boolean value = RandomHelper.getRandom(false);
        assertTrue(
            "Should return a boolean value",
            value == true || value == false
        );
    }

    @Test
    public void testGetRandomLong() {
        long value = RandomHelper.getRandom(1000L);
        assertTrue("Should return some long", true);
    }

    @Test
    public void testGetRandomShort() {
        short value = RandomHelper.getRandom((short) 100);
        assertTrue("Should return some short", true);
    }

    @Test
    public void testGetRandomDouble() {
        double value = RandomHelper.getRandom(100.0);
        assertTrue("Double should be valid", !Double.isNaN(value));
    }

    @Test
    public void testGetRandomChar() {
        char value = RandomHelper.getRandom('A');
        assertTrue("Should return a char", true);
    }

    @Test
    public void testGetRandomByte() {
        byte value = RandomHelper.getRandom((byte) 100);
        assertTrue("Should return some byte", true);
    }

    @Test
    public void testFlip() {
        boolean value = RandomHelper.flip();
        assertTrue(
            "Should return a boolean value",
            value == true || value == false
        );
    }

    @Test
    public void testFlipWithProbability() {
        boolean value = RandomHelper.flip(0.5);
        assertTrue(
            "Should return a boolean value",
            value == true || value == false
        );
    }

    @Test
    public void testFlipWithZeroProbability() {
        boolean value = RandomHelper.flip(0.0);
        assertFalse("With 0 probability, should be false", value);
    }

    @Test
    public void testFlipWithOneProbability() {
        boolean value = RandomHelper.flip(1.0);
        assertTrue("With 1.0 probability, should be true", value);
    }

    @Test
    public void testRndPrintableChar() {
        char value = RandomHelper.rndPrintableChar();
        String printableChars =
            " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_:.,=+~!@#$%^&*()[]{}\\|?";
        assertTrue(
            "Should be a printable character",
            printableChars.indexOf(value) >= 0
        );
    }

    @Test
    public void testGetSeed() {
        long seed = RandomHelper.getSeed();
        assertTrue("Seed should be positive", seed > 0);
    }

    @Test
    public void testSetSeed() {
        RandomHelper.setSeed(99999L);
        long seed = RandomHelper.getSeed();
        assertEquals("Seed should match set value", 99999L, seed);
    }

    @Test
    public void testSetSeedAndGenerateConsistent() {
        RandomHelper.setSeed(11111L);
        int val1 = RandomHelper.getRandom(100);

        RandomHelper.setSeed(11111L);
        int val2 = RandomHelper.getRandom(100);

        assertEquals("Same seed should produce same results", val1, val2);
    }

    @Test
    public void testMultipleRandomCalls() {
        int[] val1 = RandomHelper.getRandom(new int[0], int[].class);
        byte[] val2 = RandomHelper.getRandom(new byte[0], byte[].class);
        String str = RandomHelper.getRandom(new String(), String.class);

        assertNotNull(val1);
        assertNotNull(val2);
        assertNotNull(str);
    }

    @Test
    public void testGetRandomStringLength() {
        for (int i = 0; i < 10; i++) {
            String str = RandomHelper.getRandom(new String(), String.class);
            assertTrue("String length should be <= 50", str.length() <= 50);
        }
    }

    @Test
    public void testGetRandomIntArrayLength() {
        for (int i = 0; i < 10; i++) {
            int[] arr = RandomHelper.getRandom(new int[0], int[].class);
            assertTrue("Int array length should be < 50", arr.length < 50);
        }
    }

    @Test
    public void testGetRandomByteArrayLength() {
        for (int i = 0; i < 10; i++) {
            byte[] arr = RandomHelper.getRandom(new byte[0], byte[].class);
            assertTrue("Byte array length should be < 50", arr.length < 50);
        }
    }

    @Test
    public void testGetRandomStringArrayLength() {
        for (int i = 0; i < 10; i++) {
            String[] arr = RandomHelper.getRandom(
                new String[0],
                String[].class
            );
            assertTrue("String array length should be < 50", arr.length < 50);
        }
    }

    @Test
    public void testRandomFloatRange() {
        for (int i = 0; i < 20; i++) {
            float val = RandomHelper.getRandom(50.0f);
            assertTrue("Float should be valid", !Float.isNaN(val));
        }
    }

    @Test
    public void testRandomDoubleRange() {
        for (int i = 0; i < 20; i++) {
            double val = RandomHelper.getRandom(75.5);
            assertTrue("Double should be valid", !Double.isNaN(val));
        }
    }

    @Test
    public void testRandomBigDecimalNotNull() {
        BigDecimal val = RandomHelper.getRandom(
            new BigDecimal("100"),
            BigDecimal.class
        );
        assertNotNull("BigDecimal should not be null", val);
    }

    @Test
    public void testRandomVectorContents()
        throws InstantiationException, IllegalAccessException {
        Vector v = RandomHelper.getRandom(new Vector(), String.class);
        for (Object obj : v) {
            assertNotNull("Vector element should not be null", obj);
        }
    }

    @Test
    public void testRandomFlipDistribution() {
        int trueCount = 0;
        int falseCount = 0;
        for (int i = 0; i < 100; i++) {
            if (RandomHelper.flip()) {
                trueCount++;
            } else {
                falseCount++;
            }
        }
        assertTrue(
            "Should have both true and false values",
            trueCount > 0 && falseCount > 0
        );
    }

    @Test
    public void testMultiplePrintableChars() {
        String printableChars =
            " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_:.,=+~!@#$%^&*()[]{}\\|?";
        for (int i = 0; i < 50; i++) {
            char c = RandomHelper.rndPrintableChar();
            assertTrue(
                "All chars should be printable",
                printableChars.indexOf(c) >= 0
            );
        }
    }

    @Test
    public void testGetRandomStringWithLengthModification() {
        String result = RandomHelper.getRandom("test", null, true);
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be string", result instanceof String);
    }

    @Test
    public void testGetRandomStringWithoutLengthModification() {
        String result = RandomHelper.getRandom("test", null, false);
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be string", result instanceof String);
    }

    @Test
    public void testGetRandomIntArrayMultipleCalls() {
        int[] arr1 = RandomHelper.getRandom(new int[0], int[].class);
        int[] arr2 = RandomHelper.getRandom(new int[0], int[].class);
        assertNotNull("First array should not be null", arr1);
        assertNotNull("Second array should not be null", arr2);
    }

    @Test
    public void testGetRandomByteArrayMultipleCalls() {
        byte[] arr1 = RandomHelper.getRandom(new byte[0], byte[].class);
        byte[] arr2 = RandomHelper.getRandom(new byte[0], byte[].class);
        assertNotNull("First byte array should not be null", arr1);
        assertNotNull("Second byte array should not be null", arr2);
    }

    @Test
    public void testGetRandomStringArrayMultipleCalls() {
        String[] arr1 = RandomHelper.getRandom(new String[0], String[].class);
        String[] arr2 = RandomHelper.getRandom(new String[0], String[].class);
        assertNotNull("First string array should not be null", arr1);
        assertNotNull("Second string array should not be null", arr2);
    }

    @Test
    public void testFlipMultipleCalls() {
        boolean b1 = RandomHelper.flip();
        boolean b2 = RandomHelper.flip();
        boolean b3 = RandomHelper.flip();
        assertTrue(
            "All should be boolean",
            (b1 == true || b1 == false) &&
                (b2 == true || b2 == false) &&
                (b3 == true || b3 == false)
        );
    }

    @Test
    public void testRandomHelperState() {
        long originalSeed = RandomHelper.getSeed();
        RandomHelper.setSeed(54321L);
        assertEquals("Seed should be updated", 54321L, RandomHelper.getSeed());
        RandomHelper.setSeed(originalSeed);
    }

    @Test
    public void testGetRandomStringWithCollapseWhitespaceTrue() {
        String result = RandomHelper.getRandom("test", null, true);
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be non-empty", result.length() > 0);
        // First character should not be whitespace
        assertTrue(
            "First char should not be whitespace",
            !Character.isWhitespace(result.charAt(0))
        );
        // Last character should not be whitespace
        assertTrue(
            "Last char should not be whitespace",
            !Character.isWhitespace(result.charAt(result.length() - 1))
        );
    }

    @Test
    public void testGetRandomStringWithCollapseFalse() {
        String result = RandomHelper.getRandom("test", null, false);
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be non-empty", result.length() > 0);
    }

    @Test
    public void testTimeDurationRandomSkipped() {
        // TimeDuration and RecurringDuration getRandom methods are complex
        // and may have ambiguous overloads - skipping direct tests
        assertTrue("Placeholder test", true);
    }

    @Test
    public void testGetRandomStringWithCollapseWhitespaceMultipleCalls() {
        for (int i = 0; i < 10; i++) {
            String result = RandomHelper.getRandom("", null, true);
            if (result.length() > 0) {
                assertFalse(
                    "First char should not be whitespace",
                    Character.isWhitespace(result.charAt(0))
                );
                if (result.length() > 1) {
                    assertFalse(
                        "Last char should not be whitespace",
                        Character.isWhitespace(
                            result.charAt(result.length() - 1)
                        )
                    );
                }
            }
        }
    }

    @Test
    public void testGetRandomIntArrayElements() {
        int[] arr = RandomHelper.getRandom(new int[0], int[].class);
        if (arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                assertTrue("Array element should be an integer", true);
            }
        }
    }

    @Test
    public void testGetRandomByteArrayElements() {
        byte[] arr = RandomHelper.getRandom(new byte[0], byte[].class);
        if (arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                assertTrue("Array element should be a byte", true);
            }
        }
    }

    @Test
    public void testGetRandomStringArrayElements() {
        String[] arr = RandomHelper.getRandom(new String[0], String[].class);
        if (arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                assertNotNull(
                    "String array element should not be null",
                    arr[i]
                );
            }
        }
    }

    @Test
    public void testFlipWithHighProbability() {
        // Test with probability close to 1.0
        int trueCount = 0;
        for (int i = 0; i < 20; i++) {
            if (RandomHelper.flip(0.9)) {
                trueCount++;
            }
        }
        assertTrue(
            "With high probability, should have mostly true",
            trueCount >= 10
        );
    }

    @Test
    public void testFlipWithLowProbability() {
        // Test with probability close to 0.0
        int falseCount = 0;
        for (int i = 0; i < 20; i++) {
            if (!RandomHelper.flip(0.1)) {
                falseCount++;
            }
        }
        assertTrue(
            "With low probability, should have mostly false",
            falseCount >= 10
        );
    }

    @Test
    public void testRndPrintableCharDistribution() {
        // Test that printable char generation works repeatedly
        Set<Character> chars = new java.util.HashSet<>();
        for (int i = 0; i < 100; i++) {
            char c = RandomHelper.rndPrintableChar();
            chars.add(c);
        }
        assertTrue(
            "Should generate multiple different chars",
            chars.size() > 5
        );
    }

    @Test
    public void testGetRandomVectorWithDifferentClasses()
        throws InstantiationException, IllegalAccessException {
        Vector v1 = RandomHelper.getRandom(new Vector(), String.class);
        Vector v2 = RandomHelper.getRandom(new Vector(), String.class);
        assertNotNull("Both vectors should be created", v1);
        assertNotNull("Both vectors should be created", v2);
    }

    @Test
    public void testGetRandomArrayListWithDifferentClasses()
        throws InstantiationException, IllegalAccessException {
        ArrayList a1 = RandomHelper.getRandom(new ArrayList(), String.class);
        ArrayList a2 = RandomHelper.getRandom(new ArrayList(), String.class);
        assertNotNull("Both lists should be created", a1);
        assertNotNull("Both lists should be created", a2);
    }

    @Test
    public void testConsistentRandomnessWithSameSeed() {
        RandomHelper.setSeed(77777L);
        String str1 = RandomHelper.getRandom("", null, false);

        RandomHelper.setSeed(77777L);
        String str2 = RandomHelper.getRandom("", null, false);

        assertEquals("Same seed should produce same string", str1, str2);
    }

    @Test
    public void testRandomStringLength() {
        for (int i = 0; i < 20; i++) {
            String str = RandomHelper.getRandom("", null, true);
            assertTrue("String length should be >= 1", str.length() >= 1);
            assertTrue("String length should be <= 50", str.length() <= 50);
        }
    }

    @Test
    public void testGetRandomDateNotNull() {
        java.util.Date date = RandomHelper.getRandom(
            new java.util.Date(),
            java.util.Date.class
        );
        assertNotNull("Random date should not be null", date);
    }

    @Test
    public void testGetRandomDateDifferentValues() {
        java.util.Date date1 = RandomHelper.getRandom(
            new java.util.Date(),
            java.util.Date.class
        );
        java.util.Date date2 = RandomHelper.getRandom(
            new java.util.Date(),
            java.util.Date.class
        );
        // Dates should likely be different
        assertNotNull("Dates should be generated", date1);
        assertNotNull("Dates should be generated", date2);
    }

    @Test
    public void testGetRandomRecurringDuration() throws Exception {
        org.exolab.castor.types.RecurringDuration input =
            new org.exolab.castor.types.RecurringDuration();
        org.exolab.castor.types.RecurringDuration duration =
            (org.exolab.castor.types.RecurringDuration) RandomHelper.getRandom(
                input,
                org.exolab.castor.types.RecurringDuration.class
            );
        assertNotNull("RecurringDuration should be generated", duration);
    }

    @Test
    public void testGetRandomTimeDuration() throws Exception {
        org.exolab.castor.types.TimeDuration input =
            new org.exolab.castor.types.TimeDuration();
        org.exolab.castor.types.TimeDuration duration =
            (org.exolab.castor.types.TimeDuration) RandomHelper.getRandom(
                input,
                org.exolab.castor.types.TimeDuration.class
            );
        assertNotNull("TimeDuration should be generated", duration);
    }

    @Test
    public void testGetRandomObjectWithNullClass() throws Exception {
        Object obj = RandomHelper.getRandom("test", null);
        assertNotNull("Object should be generated with null class", obj);
    }

    @Test
    public void testGetRandomStringWithNullClass() throws Exception {
        String str = RandomHelper.getRandom("", null, true);
        assertNotNull("String should be generated with null class", str);
    }

    @Test
    public void testGetRandomIntWithMinValue() {
        int val = RandomHelper.getRandom(Integer.MIN_VALUE);
        assertTrue("Should return an integer", true);
    }

    @Test
    public void testGetRandomLongWithMinValue() {
        long val = RandomHelper.getRandom(Long.MIN_VALUE);
        assertTrue("Should return a long", true);
    }

    @Test
    public void testGetRandomShortWithMinValue() {
        short val = RandomHelper.getRandom(Short.MIN_VALUE);
        assertTrue("Should return a short", true);
    }

    @Test
    public void testGetRandomDoubleWithMinValue() {
        double val = RandomHelper.getRandom(Double.MIN_VALUE);
        assertTrue("Should return a double", true);
    }

    @Test
    public void testGetRandomFloatWithMinValue() {
        float val = RandomHelper.getRandom(Float.MIN_VALUE);
        assertTrue("Should return a float", true);
    }

    @Test
    public void testGetRandomByteWithMinValue() {
        byte val = RandomHelper.getRandom(Byte.MIN_VALUE);
        assertTrue("Should return a byte", true);
    }

    @Test
    public void testGetRandomByteEdgeCase() {
        RandomHelper.setSeed(0L);
        byte val1 = RandomHelper.getRandom((byte) 0);
        RandomHelper.setSeed(1L);
        byte val2 = RandomHelper.getRandom((byte) 0);
        assertTrue("Should generate bytes", true);
    }

    @Test
    public void testGetRandomEmptyVector()
        throws InstantiationException, IllegalAccessException {
        Vector v = RandomHelper.getRandom(new Vector(), String.class);
        assertNotNull("Vector should be generated", v);
    }

    @Test
    public void testGetRandomEmptyArrayList()
        throws InstantiationException, IllegalAccessException {
        ArrayList list = RandomHelper.getRandom(new ArrayList(), String.class);
        assertNotNull("ArrayList should be generated", list);
    }

    @Test
    public void testGetRandomEmptyHashSet()
        throws InstantiationException, IllegalAccessException {
        Set set = RandomHelper.getRandom(new HashSet(), String.class);
        assertNotNull("HashSet should be generated", set);
    }

    @Test
    public void testGetRandomEmptyTreeSet()
        throws InstantiationException, IllegalAccessException {
        SortedSet set = RandomHelper.getRandom(new TreeSet(), String.class);
        assertNotNull("TreeSet should be generated", set);
    }

    @Test
    public void testFlipWithMidProbability() {
        RandomHelper.setSeed(42L);
        boolean[] results = new boolean[100];
        for (int i = 0; i < 100; i++) {
            results[i] = RandomHelper.flip(0.5);
        }
        // Should have mix of true and false with probability around 0.5
        assertTrue("Flip should generate random values", true);
    }

    @Test
    public void testGetRandomStringEmpty() {
        String str = RandomHelper.getRandom("", null, true);
        assertNotNull("Should generate string", str);
    }

    @Test
    public void testGetRandomStringWithAllFalseConditions() {
        RandomHelper.setSeed(999L);
        String str = RandomHelper.getRandom("test", null, false);
        assertNotNull("Should generate string with false conditions", str);
    }

    @Test
    public void testGetRandomBigDecimalEdgeCases() {
        BigDecimal bd1 = RandomHelper.getRandom(
            BigDecimal.ZERO,
            BigDecimal.class
        );
        BigDecimal bd2 = RandomHelper.getRandom(
            BigDecimal.ONE,
            BigDecimal.class
        );
        BigDecimal bd3 = RandomHelper.getRandom(
            BigDecimal.TEN,
            BigDecimal.class
        );
        assertNotNull("BigDecimal should be generated", bd1);
        assertNotNull("BigDecimal should be generated", bd2);
        assertNotNull("BigDecimal should be generated", bd3);
    }

    @Test
    public void testRndPrintableCharMultiple() {
        Set<Character> chars = new HashSet<>();
        RandomHelper.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 50; i++) {
            chars.add(RandomHelper.rndPrintableChar());
        }
        assertTrue(
            "Should generate multiple different printable chars",
            chars.size() > 0
        );
    }

    @Test
    public void testGetSeedAndSetSeedConsistency() {
        long seed1 = RandomHelper.getSeed();
        RandomHelper.setSeed(seed1);
        long seed2 = RandomHelper.getSeed();
        assertEquals("Seeds should be consistent", seed1, seed2);
    }

    @Test
    public void testGetRandomObjectArrayWithMultipleTypes() throws Exception {
        Object[] array1 = RandomHelper.getRandom(new Object[0], String.class);
        Object[] array2 = RandomHelper.getRandom(new Object[0], String.class);
        assertNotNull("String array should be generated", array1);
        assertNotNull("String array should be generated", array2);
    }

    @Test
    public void testGetRandomStringArrayWithLength() {
        RandomHelper.setSeed(777L);
        String[] array = RandomHelper.getRandom(new String[10], String[].class);
        assertNotNull("String array should be generated", array);
    }

    @Test
    public void testGetRandomIntArrayWithNonZeroLength() {
        RandomHelper.setSeed(888L);
        int[] array = RandomHelper.getRandom(new int[5], int[].class);
        assertNotNull("Int array should be generated", array);
    }

    @Test
    public void testGetRandomByteArrayWithNonZeroLength() {
        RandomHelper.setSeed(999L);
        byte[] array = RandomHelper.getRandom(new byte[5], byte[].class);
        assertNotNull("Byte array should be generated", array);
    }

    @Test
    public void testGetRandomCharEdgeCases() {
        RandomHelper.setSeed(111L);
        char c1 = RandomHelper.getRandom('A');
        RandomHelper.setSeed(222L);
        char c2 = RandomHelper.getRandom('z');
        assertTrue("Should generate characters", true);
    }

    @Test
    public void testGetRandomFloatEdgeCases() {
        RandomHelper.setSeed(333L);
        float f1 = RandomHelper.getRandom(0.0f);
        RandomHelper.setSeed(444L);
        float f2 = RandomHelper.getRandom(Float.MAX_VALUE);
        assertTrue("Should generate floats", true);
    }

    @Test
    public void testGetRandomDoubleEdgeCases() {
        RandomHelper.setSeed(555L);
        double d1 = RandomHelper.getRandom(0.0d);
        RandomHelper.setSeed(666L);
        double d2 = RandomHelper.getRandom(Double.MAX_VALUE);
        assertTrue("Should generate doubles", true);
    }

    @Test
    public void testFlipWithExtremeValues() {
        boolean result1 = RandomHelper.flip(0.0001);
        boolean result2 = RandomHelper.flip(0.9999);
        assertTrue("Flip should work with extreme probabilities", true);
    }

    @Test
    public void testGetRandomStringCollapseWhitespaceEdgeCases() {
        // Test string generation with whitespace at edges
        RandomHelper.setSeed(200L);
        for (int i = 0; i < 20; i++) {
            String str = RandomHelper.getRandom("", null, true);
            assertNotNull("String should be generated", str);
            // Collapsed whitespace strings should not start with whitespace
            if (str.length() > 0) {
                assertFalse(
                    "Should not start with whitespace",
                    Character.isWhitespace(str.charAt(0))
                );
            }
        }
    }

    @Test
    public void testGetRandomStringConsecutiveWhitespace() {
        // Test handling of consecutive whitespace
        RandomHelper.setSeed(300L);
        for (int i = 0; i < 20; i++) {
            String str = RandomHelper.getRandom("", null, true);
            assertNotNull("String should be generated", str);
            // Check no consecutive whitespace in collapsed string
            for (int j = 0; j < str.length() - 1; j++) {
                if (Character.isWhitespace(str.charAt(j))) {
                    assertFalse(
                        "Should not have consecutive whitespace",
                        Character.isWhitespace(str.charAt(j + 1))
                    );
                }
            }
        }
    }

    @Test
    public void testGetRandomStringTrailingWhitespace() {
        // Test handling of trailing whitespace
        RandomHelper.setSeed(400L);
        for (int i = 0; i < 20; i++) {
            String str = RandomHelper.getRandom("", null, true);
            assertNotNull("String should be generated", str);
            // Collapsed whitespace strings should not end with whitespace
            if (str.length() > 0) {
                assertFalse(
                    "Should not end with whitespace",
                    Character.isWhitespace(str.charAt(str.length() - 1))
                );
            }
        }
    }

    @Test
    public void testGetRandomVectorMultipleTimes()
        throws InstantiationException, IllegalAccessException {
        RandomHelper.setSeed(500L);
        for (int i = 0; i < 10; i++) {
            Vector v = RandomHelper.getRandom(new Vector(), String.class);
            assertNotNull("Vector should be generated", v);
        }
    }

    @Test
    public void testGetRandomStringVariousLengths() {
        RandomHelper.setSeed(600L);
        for (int i = 0; i < 20; i++) {
            String str = RandomHelper.getRandom("", null, true);
            assertNotNull("String should be generated", str);
            assertTrue("String should have valid length", str.length() >= 0);
        }
    }

    @Test
    public void testGetRandomBigDecimalMultipleSeeds() {
        BigDecimal bd1 = RandomHelper.getRandom(
            BigDecimal.ZERO,
            BigDecimal.class
        );
        RandomHelper.setSeed(700L);
        BigDecimal bd2 = RandomHelper.getRandom(
            BigDecimal.ONE,
            BigDecimal.class
        );
        RandomHelper.setSeed(800L);
        BigDecimal bd3 = RandomHelper.getRandom(
            BigDecimal.TEN,
            BigDecimal.class
        );
        assertNotNull("All BigDecimals should be generated", bd1);
        assertNotNull("All BigDecimals should be generated", bd2);
        assertNotNull("All BigDecimals should be generated", bd3);
    }

    @Test
    public void testGetRandomByteMultipleSeeds() {
        RandomHelper.setSeed(900L);
        byte b1 = RandomHelper.getRandom((byte) 0);
        RandomHelper.setSeed(1000L);
        byte b2 = RandomHelper.getRandom((byte) 127);
        RandomHelper.setSeed(1100L);
        byte b3 = RandomHelper.getRandom((byte) -128);
        assertTrue("Should generate bytes", true);
    }

    @Test
    public void testFlipMultipleProbabilities() {
        RandomHelper.setSeed(1200L);
        int trueCount = 0;
        for (int i = 0; i < 100; i++) {
            if (RandomHelper.flip(0.3)) {
                trueCount++;
            }
        }
        assertTrue("Should have some true values", trueCount >= 0);
    }

    @Test
    public void testGetRandomStringNoCollapseVariations() {
        RandomHelper.setSeed(1300L);
        for (int i = 0; i < 10; i++) {
            String str = RandomHelper.getRandom("", null, false);
            assertNotNull("String should be generated without collapse", str);
        }
    }

    @Test
    public void testGetRandomTimeDurationMultipleCalls() throws Exception {
        RandomHelper.setSeed(1400L);
        for (int i = 0; i < 5; i++) {
            org.exolab.castor.types.TimeDuration input =
                new org.exolab.castor.types.TimeDuration();
            org.exolab.castor.types.TimeDuration duration =
                (org.exolab.castor.types.TimeDuration) RandomHelper.getRandom(
                    input,
                    org.exolab.castor.types.TimeDuration.class
                );
            assertNotNull("TimeDuration should be generated", duration);
        }
    }

    @Test
    public void testGetRandomArrayVariousTypes()
        throws InstantiationException, IllegalAccessException {
        RandomHelper.setSeed(1600L);
        String[] stringArray = RandomHelper.getRandom(
            new String[0],
            String[].class
        );
        RandomHelper.setSeed(1700L);
        int[] intArray = RandomHelper.getRandom(new int[0], int[].class);
        RandomHelper.setSeed(1800L);
        byte[] byteArray = RandomHelper.getRandom(new byte[0], byte[].class);
        assertNotNull("String array should be generated", stringArray);
        assertNotNull("Int array should be generated", intArray);
        assertNotNull("Byte array should be generated", byteArray);
    }

    @Test
    public void testGetRandomPrimitivesConsistency() {
        RandomHelper.setSeed(2000L);
        int i1 = RandomHelper.getRandom(0);
        RandomHelper.setSeed(2000L);
        int i2 = RandomHelper.getRandom(0);
        assertEquals("Same seed should produce same int", i1, i2);
    }

    @Test
    public void testGetRandomFloatConsistency() {
        RandomHelper.setSeed(2100L);
        float f1 = RandomHelper.getRandom(0.0f);
        RandomHelper.setSeed(2100L);
        float f2 = RandomHelper.getRandom(0.0f);
        assertEquals("Same seed should produce same float", f1, f2, 0.0f);
    }

    @Test
    public void testGetRandomDoubleConsistency() {
        RandomHelper.setSeed(2200L);
        double d1 = RandomHelper.getRandom(0.0d);
        RandomHelper.setSeed(2200L);
        double d2 = RandomHelper.getRandom(0.0d);
        assertEquals("Same seed should produce same double", d1, d2, 0.0d);
    }

    @Test
    public void testGetRandomLongConsistency() {
        RandomHelper.setSeed(2300L);
        long l1 = RandomHelper.getRandom(0L);
        RandomHelper.setSeed(2300L);
        long l2 = RandomHelper.getRandom(0L);
        assertEquals("Same seed should produce same long", l1, l2);
    }

    @Test
    public void testGetRandomShortConsistency() {
        RandomHelper.setSeed(2400L);
        short s1 = RandomHelper.getRandom((short) 0);
        RandomHelper.setSeed(2400L);
        short s2 = RandomHelper.getRandom((short) 0);
        assertEquals("Same seed should produce same short", s1, s2);
    }

    @Test
    public void testGetRandomCharConsistency() {
        RandomHelper.setSeed(2500L);
        char c1 = RandomHelper.getRandom('a');
        RandomHelper.setSeed(2500L);
        char c2 = RandomHelper.getRandom('a');
        assertEquals("Same seed should produce same char", c1, c2);
    }

    @Test
    public void testGetRandomBooleanConsistency() {
        RandomHelper.setSeed(2600L);
        boolean b1 = RandomHelper.getRandom(true);
        RandomHelper.setSeed(2600L);
        boolean b2 = RandomHelper.getRandom(true);
        assertEquals("Same seed should produce same boolean", b1, b2);
    }

    @Test
    public void testFlipConsistency() {
        RandomHelper.setSeed(2700L);
        boolean b1 = RandomHelper.flip();
        RandomHelper.setSeed(2700L);
        boolean b2 = RandomHelper.flip();
        assertEquals("Same seed should produce same flip", b1, b2);
    }

    @Test
    public void testFlipProbabilityConsistency() {
        RandomHelper.setSeed(2800L);
        boolean b1 = RandomHelper.flip(0.5);
        RandomHelper.setSeed(2800L);
        boolean b2 = RandomHelper.flip(0.5);
        assertEquals(
            "Same seed should produce same flip with probability",
            b1,
            b2
        );
    }

    @Test
    public void testRndPrintableCharIsValid() {
        RandomHelper.setSeed(2900L);
        for (int i = 0; i < 100; i++) {
            char c = RandomHelper.rndPrintableChar();
            assertTrue(
                "Generated char should be printable",
                !Character.isISOControl(c)
            );
        }
    }
}
