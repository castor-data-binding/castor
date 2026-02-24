package org.exolab.castor.xml.schema.util;

import junit.framework.TestCase;

public class DatatypeHandlerTest extends TestCase {

    public void testGuessTypeNull() {
        String result = DatatypeHandler.guessType(null);
        assertNull(result);
    }

    public void testGuessTypeEmptyString() {
        String result = DatatypeHandler.guessType("");
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }

    public void testGuessTypeInteger() {
        String result = DatatypeHandler.guessType("42");
        assertEquals(DatatypeHandler.INTEGER_TYPE, result);
    }

    public void testGuessTypeNegativeInteger() {
        String result = DatatypeHandler.guessType("-42");
        assertEquals(DatatypeHandler.INTEGER_TYPE, result);
    }

    public void testGuessTypeZero() {
        String result = DatatypeHandler.guessType("0");
        assertEquals(DatatypeHandler.INTEGER_TYPE, result);
    }

    public void testGuessTypeLong() {
        String result = DatatypeHandler.guessType("9223372036854775807");
        assertEquals(DatatypeHandler.LONG_TYPE, result);
    }

    public void testGuessTypeLongNegative() {
        String result = DatatypeHandler.guessType("-9223372036854775808");
        assertEquals(DatatypeHandler.LONG_TYPE, result);
    }

    public void testGuessTypeFloat() {
        String result = DatatypeHandler.guessType("3.14f");
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testGuessTypeFloatDecimal() {
        String result = DatatypeHandler.guessType("3.14");
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testGuessTypeDouble() {
        String result = DatatypeHandler.guessType("1.7976931348623157e+308");
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testGuessTypeDoubleNegative() {
        String result = DatatypeHandler.guessType("-123.456");
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testGuessTypeTrue() {
        String result = DatatypeHandler.guessType("true");
        assertEquals(DatatypeHandler.BOOLEAN_TYPE, result);
    }

    public void testGuessTypeFalse() {
        String result = DatatypeHandler.guessType("false");
        assertEquals(DatatypeHandler.BOOLEAN_TYPE, result);
    }

    public void testGuessTypeNotBoolean() {
        String result = DatatypeHandler.guessType("yes");
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }

    public void testGuessTypeDate() {
        String result = DatatypeHandler.guessType("2023-01-15");
        assertEquals(DatatypeHandler.DATE_TYPE, result);
    }

    public void testGuessTypeTime() {
        String result = DatatypeHandler.guessType("14:30:00");
        assertEquals(DatatypeHandler.TIME_TYPE, result);
    }

    public void testGuessTypeDateTime() {
        String result = DatatypeHandler.guessType("2023-01-15T14:30:00.000");
        assertEquals(DatatypeHandler.DATE_TYPE, result);
    }

    public void testGuessTypeDateTimeNoMillis() {
        String result = DatatypeHandler.guessType("2023-01-15T14:30:00");
        assertEquals(DatatypeHandler.DATE_TYPE, result);
    }

    public void testGuessTypeString() {
        String result = DatatypeHandler.guessType("hello world");
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }

    public void testGuessTypeStringWithNumbers() {
        String result = DatatypeHandler.guessType("abc123");
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }

    public void testWhichTypeSameType() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.INTEGER_TYPE,
            DatatypeHandler.INTEGER_TYPE
        );
        assertEquals(DatatypeHandler.INTEGER_TYPE, result);
    }

    public void testWhichTypeStringWithInteger() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.STRING_TYPE,
            DatatypeHandler.INTEGER_TYPE
        );
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }

    public void testWhichTypeIntegerWithString() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.INTEGER_TYPE,
            DatatypeHandler.STRING_TYPE
        );
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }

    public void testWhichTypeIntegerWithLong() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.INTEGER_TYPE,
            DatatypeHandler.LONG_TYPE
        );
        assertEquals(DatatypeHandler.LONG_TYPE, result);
    }

    public void testWhichTypeLongWithInteger() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.LONG_TYPE,
            DatatypeHandler.INTEGER_TYPE
        );
        assertEquals(DatatypeHandler.LONG_TYPE, result);
    }

    public void testWhichTypeIntegerWithFloat() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.INTEGER_TYPE,
            DatatypeHandler.FLOAT_TYPE
        );
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testWhichTypeIntegerWithDouble() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.INTEGER_TYPE,
            DatatypeHandler.DOUBLE_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeLongWithFloat() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.LONG_TYPE,
            DatatypeHandler.FLOAT_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeLongWithDouble() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.LONG_TYPE,
            DatatypeHandler.DOUBLE_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeFloatWithInteger() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.FLOAT_TYPE,
            DatatypeHandler.INTEGER_TYPE
        );
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testWhichTypeFloatWithLong() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.FLOAT_TYPE,
            DatatypeHandler.LONG_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeFloatWithDouble() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.FLOAT_TYPE,
            DatatypeHandler.DOUBLE_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeDoubleWithInteger() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.DOUBLE_TYPE,
            DatatypeHandler.INTEGER_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeDoubleWithLong() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.DOUBLE_TYPE,
            DatatypeHandler.LONG_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeDoubleWithFloat() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.DOUBLE_TYPE,
            DatatypeHandler.FLOAT_TYPE
        );
        assertEquals(DatatypeHandler.DOUBLE_TYPE, result);
    }

    public void testWhichTypeOtherTypes() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.BOOLEAN_TYPE,
            DatatypeHandler.DATE_TYPE
        );
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }

    public void testGuessTypeWithLeadingZeros() {
        String result = DatatypeHandler.guessType("00123");
        assertEquals(DatatypeHandler.INTEGER_TYPE, result);
    }

    public void testGuessTypeScientificNotation() {
        String result = DatatypeHandler.guessType("1.23e10");
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testGuessTypeZeroFloat() {
        String result = DatatypeHandler.guessType("0.0");
        assertEquals(DatatypeHandler.FLOAT_TYPE, result);
    }

    public void testWhichTypeBooleanWithString() {
        String result = DatatypeHandler.whichType(
            DatatypeHandler.BOOLEAN_TYPE,
            DatatypeHandler.STRING_TYPE
        );
        assertEquals(DatatypeHandler.STRING_TYPE, result);
    }
}
