package org.castor.xmlctf.util;

import static org.junit.Assert.*;

import org.exolab.castor.xml.MarshalException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for CTFUtils class.
 */
public class CTFUtilsTest {

    @Before
    public void setUp() {
        // Nothing to initialize
    }

    // ===== Test getClass() method =====

    @Test(expected = IllegalArgumentException.class)
    public void testGetClassWithNullName() throws ClassNotFoundException {
        CTFUtils.getClass(null, ClassLoader.getSystemClassLoader());
    }

    @Test
    public void testGetClassWithBooleanPrimitive()
        throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "boolean",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Boolean.TYPE, clazz);
    }

    @Test
    public void testGetClassWithBytePrimitive() throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "byte",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Byte.TYPE, clazz);
    }

    @Test
    public void testGetClassWithCharacterPrimitive()
        throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "character",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Character.TYPE, clazz);
    }

    @Test
    public void testGetClassWithDoublePrimitive()
        throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "double",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Double.TYPE, clazz);
    }

    @Test
    public void testGetClassWithFloatPrimitive() throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "float",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Float.TYPE, clazz);
    }

    @Test
    public void testGetClassWithIntPrimitive() throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "int",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Integer.TYPE, clazz);
    }

    @Test
    public void testGetClassWithLongPrimitive() throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "long",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Long.TYPE, clazz);
    }

    @Test
    public void testGetClassWithShortPrimitive() throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "short",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Short.TYPE, clazz);
    }

    @Test
    public void testGetClassWithStringClass() throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "java.lang.String",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(String.class, clazz);
    }

    @Test
    public void testGetClassWithIntegerClass() throws ClassNotFoundException {
        Class<?> clazz = CTFUtils.getClass(
            "java.lang.Integer",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Integer.class, clazz);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetClassWithNonExistentClass()
        throws ClassNotFoundException {
        CTFUtils.getClass(
            "non.existent.Class",
            ClassLoader.getSystemClassLoader()
        );
    }

    // ===== Test instantiateObject() with primitive types =====

    @Test
    public void testInstantiateObjectString()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "String",
            "test value",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals("test value", obj);
    }

    @Test
    public void testInstantiateObjectStringFullName()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.String",
            "test value",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals("test value", obj);
    }

    @Test
    public void testInstantiateObjectBoolean()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "boolean",
            "true",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Boolean.TRUE, obj);
    }

    @Test
    public void testInstantiateObjectBooleanFalse()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "boolean",
            "false",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Boolean.FALSE, obj);
    }

    @Test
    public void testInstantiateObjectBooleanClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Boolean",
            "true",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Boolean.TRUE, obj);
    }

    @Test
    public void testInstantiateObjectByte()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "byte",
            "127",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Byte.valueOf((byte) 127), obj);
    }

    @Test
    public void testInstantiateObjectByteClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Byte",
            "127",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Byte.valueOf((byte) 127), obj);
    }

    @Test
    public void testInstantiateObjectCharacter()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "character",
            "A",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Character.valueOf('A'), obj);
    }

    @Test
    public void testInstantiateObjectCharacterClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Character",
            "B",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Character.valueOf('B'), obj);
    }

    @Test
    public void testInstantiateObjectDouble()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "double",
            "3.14",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Double.valueOf(3.14), obj);
    }

    @Test
    public void testInstantiateObjectDoubleClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Double",
            "2.71",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Double.valueOf(2.71), obj);
    }

    @Test
    public void testInstantiateObjectFloat()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "float",
            "1.5",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Float.valueOf(1.5f), obj);
    }

    @Test
    public void testInstantiateObjectFloatClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Float",
            "2.5",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Float.valueOf(2.5f), obj);
    }

    @Test
    public void testInstantiateObjectInt()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "int",
            "42",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Integer.valueOf(42), obj);
    }

    @Test
    public void testInstantiateObjectIntClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Integer",
            "123",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Integer.valueOf(123), obj);
    }

    @Test
    public void testInstantiateObjectLong()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "long",
            "9999999999",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Long.valueOf(9999999999L), obj);
    }

    @Test
    public void testInstantiateObjectLongClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Long",
            "1234567890",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Long.valueOf(1234567890L), obj);
    }

    @Test
    public void testInstantiateObjectShort()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "short",
            "999",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Short.valueOf((short) 999), obj);
    }

    @Test
    public void testInstantiateObjectShortClass()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "java.lang.Short",
            "500",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Short.valueOf((short) 500), obj);
    }

    // ===== Test instantiateObject() with edge cases =====

    @Test
    public void testInstantiateObjectByteNegative()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "byte",
            "-128",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Byte.valueOf((byte) -128), obj);
    }

    @Test
    public void testInstantiateObjectIntNegative()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "int",
            "-42",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Integer.valueOf(-42), obj);
    }

    @Test
    public void testInstantiateObjectLongNegative()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "long",
            "-9999999999",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Long.valueOf(-9999999999L), obj);
    }

    @Test
    public void testInstantiateObjectDoubleZero()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "double",
            "0.0",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Double.valueOf(0.0), obj);
    }

    @Test
    public void testInstantiateObjectFloatZero()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "float",
            "0.0",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Float.valueOf(0.0f), obj);
    }

    @Test
    public void testInstantiateObjectStringEmpty()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "String",
            "",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals("", obj);
    }

    @Test
    public void testInstantiateObjectStringWithSpaces()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "String",
            "  test value  ",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals("  test value  ", obj);
    }

    @Test
    public void testInstantiateObjectCharacterFirstChar()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "character",
            "test",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Character.valueOf('t'), obj);
    }

    // ===== Test constants =====

    @Test
    public void testConstantBoolean() {
        assertEquals("boolean", CTFUtils.BOOLEAN);
    }

    @Test
    public void testConstantByte() {
        assertEquals("byte", CTFUtils.BYTE);
    }

    @Test
    public void testConstantCharacter() {
        assertEquals("character", CTFUtils.CHARACTER);
    }

    @Test
    public void testConstantDouble() {
        assertEquals("double", CTFUtils.DOUBLE);
    }

    @Test
    public void testConstantFloat() {
        assertEquals("float", CTFUtils.FLOAT);
    }

    @Test
    public void testConstantInt() {
        assertEquals("int", CTFUtils.INT);
    }

    @Test
    public void testConstantLong() {
        assertEquals("long", CTFUtils.LONG);
    }

    @Test
    public void testConstantShort() {
        assertEquals("short", CTFUtils.SHORT);
    }

    @Test
    public void testConstantString() {
        assertEquals("String", CTFUtils.STRING);
    }

    // ===== Test instantiateObject() with complex scenarios =====

    @Test
    public void testInstantiateObjectMultipleCalls()
        throws ClassNotFoundException, MarshalException {
        Object obj1 = CTFUtils.instantiateObject(
            "int",
            "100",
            ClassLoader.getSystemClassLoader()
        );
        Object obj2 = CTFUtils.instantiateObject(
            "int",
            "200",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Integer.valueOf(100), obj1);
        assertEquals(Integer.valueOf(200), obj2);
        assertNotEquals(obj1, obj2);
    }

    @Test
    public void testInstantiateObjectMixedTypes()
        throws ClassNotFoundException, MarshalException {
        Object str = CTFUtils.instantiateObject(
            "String",
            "text",
            ClassLoader.getSystemClassLoader()
        );
        Object num = CTFUtils.instantiateObject(
            "int",
            "42",
            ClassLoader.getSystemClassLoader()
        );
        Object flag = CTFUtils.instantiateObject(
            "boolean",
            "true",
            ClassLoader.getSystemClassLoader()
        );

        assertEquals("text", str);
        assertEquals(Integer.valueOf(42), num);
        assertEquals(Boolean.TRUE, flag);
    }

    @Test
    public void testInstantiateObjectWithWhitespace()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "String",
            "  ",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals("  ", obj);
    }

    @Test
    public void testInstantiateObjectDoubleScientificNotation()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "double",
            "1.5e2",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Double.valueOf(1.5e2), obj);
    }

    @Test
    public void testInstantiateObjectFloatScientificNotation()
        throws ClassNotFoundException, MarshalException {
        Object obj = CTFUtils.instantiateObject(
            "float",
            "1.5e2",
            ClassLoader.getSystemClassLoader()
        );
        assertEquals(Float.valueOf(1.5e2f), obj);
    }
}
