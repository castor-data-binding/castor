/*
 * Copyright 2007 Joachim Grueneis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.xml;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Joachim Grueneis, jgrueneis_at_gmail_dot_com
 * @version $Id$
 */
public class JavaNamingTest extends TestCase {
    
    private static final String[] VALID_NAMES = {"name", "myName", "my_name", "NAME"};

    private static final String[] INVALID_NAMES = {"my-name", "my----name"};
    
    private static final String PS = System.getProperty("file.separator");

    private JavaNaming _javaNaming = new JavaNamingImpl();

    /**
     * @param arg0
     */
    public JavaNamingTest(final String arg0) {
        super(arg0);
    }

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#isKeyword(java.lang.String)}.
     */
    public final void testIsKeyword() {
        Assert.assertFalse("A null String is no keyword", _javaNaming.isKeyword(null));
        Assert.assertFalse("An empty String is no keyword", _javaNaming.isKeyword(""));
        for (int i = 0; i < VALID_NAMES.length; i++) {
            String aName = VALID_NAMES[i];
            Assert.assertFalse(
                    "Name: " + aName + " is no keyword!", _javaNaming.isKeyword(aName));
        }
    }

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#isValidJavaIdentifier(java.lang.String)}.
     */
    public final void testIsValidJavaIdentifier() {
        Assert.assertFalse(
                "A null String is no identifier", _javaNaming.isValidJavaIdentifier(null));
        Assert.assertFalse(
                "An empty String is no identifier", _javaNaming.isValidJavaIdentifier(""));
        for (int i = 0; i < VALID_NAMES.length; i++) {
            String aName = VALID_NAMES[i];
            Assert.assertTrue(
                    "Name: " + aName + " should be valid!", _javaNaming.isValidJavaIdentifier(aName));
        }
        for (int i = 0; i < INVALID_NAMES.length; i++) {
            String aName = INVALID_NAMES[i];
            Assert.assertFalse(
                    "Name: " + aName + " should be invalid!", _javaNaming.isValidJavaIdentifier(aName));
        }
    }

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#toJavaClassName(java.lang.String)}.
     */
    public final void testToJavaClassName() {
        Assert.assertEquals("For null in and out should be equals", null, _javaNaming.toJavaClassName(null));
        Assert.assertEquals("For an empty string in and out should be equals", "", _javaNaming.toJavaClassName(""));
    }

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#toJavaMemberName(java.lang.String)}.
     */
    public final void testToJavaMemberNameString() {
        Assert.assertEquals("For null in and out should be equals", null, _javaNaming.toJavaMemberName(null));
        Assert.assertEquals("For an empty string in and out should be equals", "", _javaNaming.toJavaMemberName(""));
    }

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#toJavaMemberName(java.lang.String, boolean)}.
     */
    public final void testToJavaMemberNameStringBoolean() {
        Assert.assertEquals("For null in and out should be equals", null, _javaNaming.toJavaMemberName(null, true));
        Assert.assertEquals("For an empty string in and out should be equals", "", _javaNaming.toJavaMemberName("", true));
        Assert.assertEquals("For null in and out should be equals", null, _javaNaming.toJavaMemberName(null, false));
        Assert.assertEquals("For an empty string in and out should be equals", "", _javaNaming.toJavaMemberName("", false));
    }

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#packageToPath(java.lang.String)}.
     */
    public final void testPackageToPath() {
        Assert.assertEquals("For null in and out should be equals", null, _javaNaming.packageToPath(null));
        Assert.assertEquals("For an empty string in and out should be equals", "", _javaNaming.packageToPath(""));

        Assert.assertEquals("org" + PS + "castor" + PS + "xml",
                _javaNaming.packageToPath("org.castor.xml"));
        try {
            _javaNaming.packageToPath(".");
            Assert.fail("Invalid package name must not be converted!");
        } catch (IllegalArgumentException e) {
            // expected!!
        }
        Assert.assertEquals("org", _javaNaming.packageToPath("org"));
        try {
            _javaNaming.packageToPath(".org");
            Assert.fail("Invalid package name must not be converted!");
        } catch (IllegalArgumentException e) {
            // expected!!
        }
        try {
            _javaNaming.packageToPath("org.");
            Assert.fail("Invalid package name must not be converted!");
        } catch (IllegalArgumentException e) {
            // expected!!
        }
}

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#getQualifiedFileName(java.lang.String,java.lang.String)}.
     */
    public final void testGetQualifiedFileName() {
        Assert.assertEquals("For null in and out should be equals", null, _javaNaming.getQualifiedFileName(null, null));
        Assert.assertEquals("For an empty string in and out should be equals", "", _javaNaming.getQualifiedFileName("", ""));
        Assert.assertEquals("For an empty string in and out should be equals", null, _javaNaming.getQualifiedFileName(null, ""));
        Assert.assertEquals("For an empty string in and out should be equals", "", _javaNaming.getQualifiedFileName("", null));

        Assert.assertEquals("org" + PS + "castor" + PS + "xml/",
                _javaNaming.getQualifiedFileName("", "org.castor.xml"));
        Assert.assertEquals("org" + PS + "castor" + PS + "xml/A.b",
                _javaNaming.getQualifiedFileName("A.b", "org.castor.xml"));
        Assert.assertEquals("A.b", _javaNaming.getQualifiedFileName("A.b", null));
        Assert.assertEquals("A.b", _javaNaming.getQualifiedFileName("A.b", ""));
        Assert.assertEquals("org/A.b", _javaNaming.getQualifiedFileName("A.b", "org"));
        Assert.assertEquals("org/Anton", _javaNaming.getQualifiedFileName("Anton", "org"));

        try {
            _javaNaming.getQualifiedFileName("A.b", ".");
            Assert.fail("Shouldn't succeed with invalid package name!");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            _javaNaming.getQualifiedFileName("A.b", ".org");
            Assert.fail("Shouldn't succeed with invalid package name!");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            _javaNaming.getQualifiedFileName("A.b", "org.");
            Assert.fail("Shouldn't succeed with invalid package name!");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#getPackageName(java.lang.String)}.
     */
    public final void testGetPackageName() {
        Assert.assertEquals("For null in and out should be equals",
                null, _javaNaming.getPackageName(null));
        Assert.assertEquals("For an empty string in and out should be equals",
                "", _javaNaming.getPackageName(""));

        Assert.assertEquals("Extracted package name doesn't match",
                JavaNamingImpl.class.getPackage().getName(),
                _javaNaming.getPackageName(JavaNamingImpl.class.getName()));
        Assert.assertEquals("Extracted package name doesn't match",
                "", _javaNaming.getPackageName("A"));
    }
    
    /**
     * Test method for
     * {@link org.castor.xml.JavaNamingImpl#isAddMethod(java.lang.reflect.Method)}.
     */
    public final void testIsAddMethod() {
        Assert.assertFalse(_javaNaming.isAddMethod(null));
    }
    
    private class ForTestingPurposes {}

    public final void testGetClassName() {
        Assert.assertNull(_javaNaming.getClassName(null));
        Assert.assertEquals("JavaNamingTest", _javaNaming.getClassName(JavaNamingTest.class));
        Assert.assertEquals("JavaNamingTest$ForTestingPurposes", _javaNaming.getClassName(ForTestingPurposes.class));
    }
}
