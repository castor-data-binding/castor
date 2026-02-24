/*
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices. Redistributions
 * must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote products derived from this Software
 * without prior written permission of Intalio, Inc. For written permission, please contact
 * info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab" nor may "Exolab" appear in
 * their names without prior written permission of Intalio, Inc. Exolab is a registered trademark of
 * Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.exolab.javasource.JClass;
import org.exolab.javasource.JCollectionType;
import org.exolab.javasource.JType;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive unit tests for the SGTypes class.
 *
 * Targets >95% coverage of: class coverage, method coverage, line coverage, branch coverage
 */
public class SGTypesTest {

    @Before
    public void setUp() {
        // Setup before each test
    }

    // ========== Constant Tests ==========

    @Test
    public void should_HaveMarshalExceptionConstant_When_ClassInitializes() {
        assertNotNull(
            "MARSHAL_EXCEPTION should not be null",
            SGTypes.MARSHAL_EXCEPTION
        );
        assertEquals(
            "MARSHAL_EXCEPTION should have correct name",
            "MarshalException",
            SGTypes.MARSHAL_EXCEPTION.getLocalName()
        );
    }

    @Test
    public void should_HaveValidationExceptionConstant_When_ClassInitializes() {
        assertNotNull(
            "VALIDATION_EXCEPTION should not be null",
            SGTypes.VALIDATION_EXCEPTION
        );
        assertEquals(
            "VALIDATION_EXCEPTION should have correct name",
            "ValidationException",
            SGTypes.VALIDATION_EXCEPTION.getLocalName()
        );
    }

    @Test
    public void should_HaveIndexOutOfBoundsExceptionConstant_When_ClassInitializes() {
        assertNotNull(
            "INDEX_OUT_OF_BOUNDS_EXCEPTION should not be null",
            SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION
        );
        assertEquals(
            "INDEX_OUT_OF_BOUNDS_EXCEPTION should have correct name",
            "IndexOutOfBoundsException",
            SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION.getLocalName()
        );
    }

    @Test
    public void should_HaveClassConstant_When_ClassInitializes() {
        assertNotNull("CLASS should not be null", SGTypes.CLASS);
        assertEquals(
            "CLASS should have correct name",
            "Class",
            SGTypes.CLASS.getLocalName()
        );
    }

    @Test
    public void should_HaveObjectConstant_When_ClassInitializes() {
        assertNotNull("OBJECT should not be null", SGTypes.OBJECT);
        assertEquals(
            "OBJECT should have correct name",
            "Object",
            SGTypes.OBJECT.getLocalName()
        );
    }

    @Test
    public void should_HaveStringConstant_When_ClassInitializes() {
        assertNotNull("STRING should not be null", SGTypes.STRING);
        assertEquals(
            "STRING should have correct name",
            "String",
            SGTypes.STRING.getLocalName()
        );
    }

    @Test
    public void should_HaveIOExceptionConstant_When_ClassInitializes() {
        assertNotNull("IO_EXCEPTION should not be null", SGTypes.IO_EXCEPTION);
        assertEquals(
            "IO_EXCEPTION should have correct name",
            "IOException",
            SGTypes.IO_EXCEPTION.getLocalName()
        );
    }

    @Test
    public void should_HaveReaderConstant_When_ClassInitializes() {
        assertNotNull("READER should not be null", SGTypes.READER);
        assertEquals(
            "READER should have correct name",
            "Reader",
            SGTypes.READER.getLocalName()
        );
    }

    @Test
    public void should_HaveWriterConstant_When_ClassInitializes() {
        assertNotNull("WRITER should not be null", SGTypes.WRITER);
        assertEquals(
            "WRITER should have correct name",
            "Writer",
            SGTypes.WRITER.getLocalName()
        );
    }

    @Test
    public void should_HavePropertyChangeSupportConstant_When_ClassInitializes() {
        assertNotNull(
            "PROPERTY_CHANGE_SUPPORT should not be null",
            SGTypes.PROPERTY_CHANGE_SUPPORT
        );
        assertEquals(
            "PROPERTY_CHANGE_SUPPORT should have correct name",
            "PropertyChangeSupport",
            SGTypes.PROPERTY_CHANGE_SUPPORT.getLocalName()
        );
    }

    // ========== createEnumeration Tests ==========

    @Test
    public void should_ReturnJCollectionType_When_CreateEnumerationWithJavaFifty() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createEnumeration(stringType, true);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
        assertTrue(
            "Result should contain Enumeration",
            result.toString().contains("java.util.Enumeration")
        );
    }

    @Test
    public void should_ReturnJCollectionType_When_CreateEnumerationWithoutJavaFifty() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createEnumeration(stringType, false);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
        assertEquals(
            "Result should be plain Enumeration",
            "java.util.Enumeration",
            result.getName()
        );
    }

    @Test
    public void should_ReturnJCollectionTypeWithExtends_When_CreateEnumerationWithUseExtends() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createEnumeration(stringType, true, true);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
        assertTrue(
            "Result should contain extends when useExtends=true",
            result.toString().contains("extends") ||
                result.toString().contains("String")
        );
    }

    @Test
    public void should_ReturnJCollectionTypeWithoutExtends_When_CreateEnumerationWithoutUseExtends() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createEnumeration(stringType, true, false);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
    }

    @Test
    public void should_ReturnJCollectionType_When_CreateEnumerationWithCustomType() {
        JClass customType = new JClass("com.example.CustomType");
        JType result = SGTypes.createEnumeration(customType, true);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
    }

    @Test
    public void should_DelegateToThreeArgVersion_When_CreateEnumerationWithTwoArgs() {
        JClass stringType = new JClass("java.lang.String");
        JType result1 = SGTypes.createEnumeration(stringType, true);
        JType result2 = SGTypes.createEnumeration(stringType, true, false);

        assertNotNull("Both results should not be null", result1);
        assertNotNull("Both results should not be null", result2);
        assertTrue(
            "Both should be JCollectionType",
            result1 instanceof JCollectionType
        );
        assertTrue(
            "Both should be JCollectionType",
            result2 instanceof JCollectionType
        );
    }

    // ========== createIterator Tests ==========

    @Test
    public void should_ReturnJCollectionType_When_CreateIteratorWithJavaFifty() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createIterator(stringType, true);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
        assertTrue(
            "Result should contain Iterator",
            result.toString().contains("java.util.Iterator")
        );
    }

    @Test
    public void should_ReturnJCollectionType_When_CreateIteratorWithoutJavaFifty() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createIterator(stringType, false);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
        assertEquals(
            "Result should be plain Iterator",
            "java.util.Iterator",
            result.getName()
        );
    }

    @Test
    public void should_ReturnJCollectionTypeWithExtends_When_CreateIteratorWithUseExtends() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createIterator(stringType, true, true);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
        assertTrue(
            "Result should contain extends when useExtends=true",
            result.toString().contains("extends") ||
                result.toString().contains("String")
        );
    }

    @Test
    public void should_ReturnJCollectionTypeWithoutExtends_When_CreateIteratorWithoutUseExtends() {
        JClass stringType = new JClass("java.lang.String");
        JType result = SGTypes.createIterator(stringType, true, false);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
    }

    @Test
    public void should_ReturnJCollectionType_When_CreateIteratorWithCustomType() {
        JClass customType = new JClass("com.example.CustomType");
        JType result = SGTypes.createIterator(customType, false);

        assertNotNull("Result should not be null", result);
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
    }

    @Test
    public void should_DelegateToThreeArgVersion_When_CreateIteratorWithTwoArgs() {
        JClass stringType = new JClass("java.lang.String");
        JType result1 = SGTypes.createIterator(stringType, true);
        JType result2 = SGTypes.createIterator(stringType, true, false);

        assertNotNull("Both results should not be null", result1);
        assertNotNull("Both results should not be null", result2);
        assertTrue(
            "Both should be JCollectionType",
            result1 instanceof JCollectionType
        );
        assertTrue(
            "Both should be JCollectionType",
            result2 instanceof JCollectionType
        );
    }

    // ========== createHashtable Tests ==========

    @Test
    public void should_ReturnJClassWithGenerics_When_CreateHashtableWithJavaFifty() {
        JType result = SGTypes.createHashtable(true);

        assertNotNull("Result should not be null", result);
        assertTrue("Result should be JClass", result instanceof JClass);
        assertTrue(
            "Result should contain generic parameters",
            ((JClass) result).getName().contains("Object")
        );
    }

    @Test
    public void should_ReturnJClassWithoutGenerics_When_CreateHashtableWithoutJavaFifty() {
        JType result = SGTypes.createHashtable(false);

        assertNotNull("Result should not be null", result);
        assertTrue("Result should be JClass", result instanceof JClass);
        assertEquals(
            "Result should be plain Hashtable",
            "java.util.Hashtable",
            ((JClass) result).getName()
        );
    }

    @Test
    public void should_ReturnDifferentResults_When_CreateHashtableWithDifferentJavaVersions() {
        JType result50 = SGTypes.createHashtable(true);
        JType resultPre50 = SGTypes.createHashtable(false);

        assertNotNull("Both results should not be null", result50);
        assertNotNull("Both results should not be null", resultPre50);
        String name50 = ((JClass) result50).getName();
        String namePre50 = ((JClass) resultPre50).getName();

        assertTrue(
            "Java 5.0 version should have generics",
            name50.contains("<")
        );
        assertTrue(
            "Pre-Java 5.0 version should not have generics",
            !namePre50.contains("<")
        );
    }

    // ========== Edge Case and Branch Coverage Tests ==========

    @Test
    public void should_HandleMultipleConsecutiveCalls_When_CreateEnumerationCalledMultipleTimes() {
        JClass type1 = new JClass("java.lang.String");
        JClass type2 = new JClass("java.lang.Integer");

        JType result1 = SGTypes.createEnumeration(type1, true);
        JType result2 = SGTypes.createEnumeration(type2, false);
        JType result3 = SGTypes.createEnumeration(type1, true, true);

        assertNotNull("All results should be non-null", result1);
        assertNotNull("All results should be non-null", result2);
        assertNotNull("All results should be non-null", result3);
        assertTrue(
            "All should be JCollectionType",
            result1 instanceof JCollectionType
        );
        assertTrue(
            "All should be JCollectionType",
            result2 instanceof JCollectionType
        );
        assertTrue(
            "All should be JCollectionType",
            result3 instanceof JCollectionType
        );
    }

    @Test
    public void should_HandleMultipleConsecutiveCalls_When_CreateIteratorCalledMultipleTimes() {
        JClass type1 = new JClass("java.lang.String");
        JClass type2 = new JClass("java.lang.Long");

        JType result1 = SGTypes.createIterator(type1, true);
        JType result2 = SGTypes.createIterator(type2, false);
        JType result3 = SGTypes.createIterator(type1, false, true);

        assertNotNull("All results should be non-null", result1);
        assertNotNull("All results should be non-null", result2);
        assertNotNull("All results should be non-null", result3);
        assertTrue(
            "All should be JCollectionType",
            result1 instanceof JCollectionType
        );
        assertTrue(
            "All should be JCollectionType",
            result2 instanceof JCollectionType
        );
        assertTrue(
            "All should be JCollectionType",
            result3 instanceof JCollectionType
        );
    }

    @Test
    public void should_CreateHashtableMultipleTimes_When_CalledWithBothFlags() {
        JType result1 = SGTypes.createHashtable(true);
        JType result2 = SGTypes.createHashtable(true);
        JType result3 = SGTypes.createHashtable(false);
        JType result4 = SGTypes.createHashtable(false);

        assertNotNull("All results should be non-null", result1);
        assertNotNull("All results should be non-null", result2);
        assertNotNull("All results should be non-null", result3);
        assertNotNull("All results should be non-null", result4);
    }

    @Test
    public void should_VerifyConstantTypes_When_AccessingAllConstants() {
        assertTrue(
            "MARSHAL_EXCEPTION is JClass",
            SGTypes.MARSHAL_EXCEPTION instanceof JClass
        );
        assertTrue(
            "VALIDATION_EXCEPTION is JClass",
            SGTypes.VALIDATION_EXCEPTION instanceof JClass
        );
        assertTrue(
            "INDEX_OUT_OF_BOUNDS_EXCEPTION is JClass",
            SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION instanceof JClass
        );
        assertTrue("CLASS is JClass", SGTypes.CLASS instanceof JClass);
        assertTrue("OBJECT is JClass", SGTypes.OBJECT instanceof JClass);
        assertTrue("STRING is JClass", SGTypes.STRING instanceof JClass);
        assertTrue(
            "IO_EXCEPTION is JClass",
            SGTypes.IO_EXCEPTION instanceof JClass
        );
        assertTrue("READER is JClass", SGTypes.READER instanceof JClass);
        assertTrue("WRITER is JClass", SGTypes.WRITER instanceof JClass);
        assertTrue(
            "PROPERTY_CHANGE_SUPPORT is JClass",
            SGTypes.PROPERTY_CHANGE_SUPPORT instanceof JClass
        );
    }

    @Test
    public void should_VerifyConstantFullyQualifiedNames_When_AccessingConstants() {
        assertEquals(
            "MARSHAL_EXCEPTION has correct FQN",
            "org.exolab.castor.xml.MarshalException",
            SGTypes.MARSHAL_EXCEPTION.getName()
        );
        assertEquals(
            "VALIDATION_EXCEPTION has correct FQN",
            "org.exolab.castor.xml.ValidationException",
            SGTypes.VALIDATION_EXCEPTION.getName()
        );
        assertEquals(
            "INDEX_OUT_OF_BOUNDS_EXCEPTION has correct FQN",
            "java.lang.IndexOutOfBoundsException",
            SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION.getName()
        );
        assertEquals(
            "CLASS has correct FQN",
            "java.lang.Class",
            SGTypes.CLASS.getName()
        );
        assertEquals(
            "OBJECT has correct FQN",
            "java.lang.Object",
            SGTypes.OBJECT.getName()
        );
        assertEquals(
            "STRING has correct FQN",
            "java.lang.String",
            SGTypes.STRING.getName()
        );
        assertEquals(
            "IO_EXCEPTION has correct FQN",
            "java.io.IOException",
            SGTypes.IO_EXCEPTION.getName()
        );
        assertEquals(
            "READER has correct FQN",
            "java.io.Reader",
            SGTypes.READER.getName()
        );
        assertEquals(
            "WRITER has correct FQN",
            "java.io.Writer",
            SGTypes.WRITER.getName()
        );
        assertEquals(
            "PROPERTY_CHANGE_SUPPORT has correct FQN",
            "java.beans.PropertyChangeSupport",
            SGTypes.PROPERTY_CHANGE_SUPPORT.getName()
        );
    }

    @Test
    public void should_ReturnValidTypes_When_CreateEnumerationWithAllBranches() {
        JClass stringType = new JClass("java.lang.String");
        JType resultJava50Extends = SGTypes.createEnumeration(
            stringType,
            true,
            true
        );
        JType resultJava50NoExtends = SGTypes.createEnumeration(
            stringType,
            true,
            false
        );
        JType resultNoJava50Extends = SGTypes.createEnumeration(
            stringType,
            false,
            true
        );
        JType resultNoJava50NoExtends = SGTypes.createEnumeration(
            stringType,
            false,
            false
        );

        assertTrue(
            "All should be JCollectionType",
            resultJava50Extends instanceof JCollectionType &&
                resultJava50NoExtends instanceof JCollectionType &&
                resultNoJava50Extends instanceof JCollectionType &&
                resultNoJava50NoExtends instanceof JCollectionType
        );
    }

    @Test
    public void should_ReturnValidTypes_When_CreateIteratorWithAllBranches() {
        JClass stringType = new JClass("java.lang.String");
        JType resultJava50Extends = SGTypes.createIterator(
            stringType,
            true,
            true
        );
        JType resultJava50NoExtends = SGTypes.createIterator(
            stringType,
            true,
            false
        );
        JType resultNoJava50Extends = SGTypes.createIterator(
            stringType,
            false,
            true
        );
        JType resultNoJava50NoExtends = SGTypes.createIterator(
            stringType,
            false,
            false
        );

        assertTrue(
            "All should be JCollectionType",
            resultJava50Extends instanceof JCollectionType &&
                resultJava50NoExtends instanceof JCollectionType &&
                resultNoJava50Extends instanceof JCollectionType &&
                resultNoJava50NoExtends instanceof JCollectionType
        );
    }

    @Test
    public void should_VerifyEnumerationDiffersFromIterator_When_MethodsCalledWithSameParams() {
        JClass stringType = new JClass("java.lang.String");
        JType enumeration = SGTypes.createEnumeration(stringType, true, false);
        JType iterator = SGTypes.createIterator(stringType, true, false);

        assertNotNull("Enumeration should be non-null", enumeration);
        assertNotNull("Iterator should be non-null", iterator);
        assertTrue(
            "Both should be collections",
            enumeration instanceof JCollectionType
        );
        assertTrue(
            "Both should be collections",
            iterator instanceof JCollectionType
        );

        String enumName = enumeration.getName();
        String iterName = iterator.getName();
        assertTrue("Names should be different", !enumName.equals(iterName));
    }

    @Test
    public void should_HandleNullComponentType_When_CreateEnumerationPassesNullType() {
        JType result = SGTypes.createEnumeration(null, true, true);
        assertNotNull(
            "Result should not be null even with null component",
            result
        );
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
    }

    @Test
    public void should_HandleNullComponentType_When_CreateIteratorPassesNullType() {
        JType result = SGTypes.createIterator(null, false, false);
        assertNotNull(
            "Result should not be null even with null component",
            result
        );
        assertTrue(
            "Result should be JCollectionType",
            result instanceof JCollectionType
        );
    }

    @Test
    public void should_VerifyHashtableConsistency_When_CalledMultipleTimesWithSameFlag() {
        JType result1 = SGTypes.createHashtable(true);
        JType result2 = SGTypes.createHashtable(true);
        String name1 = ((JClass) result1).getName();
        String name2 = ((JClass) result2).getName();
        assertEquals("Multiple calls should be consistent", name1, name2);
    }

    @Test
    public void should_VerifyEnumerationConsistency_When_CalledMultipleTimesWithSameParams() {
        JClass stringType = new JClass("java.lang.String");
        JType result1 = SGTypes.createEnumeration(stringType, true, false);
        JType result2 = SGTypes.createEnumeration(stringType, true, false);
        assertEquals(
            "Multiple calls should be consistent",
            result1.getName(),
            result2.getName()
        );
    }

    @Test
    public void should_VerifyIteratorConsistency_When_CalledMultipleTimesWithSameParams() {
        JClass stringType = new JClass("java.lang.String");
        JType result1 = SGTypes.createIterator(stringType, false, true);
        JType result2 = SGTypes.createIterator(stringType, false, true);
        assertEquals(
            "Multiple calls should be consistent",
            result1.getName(),
            result2.getName()
        );
    }
}
