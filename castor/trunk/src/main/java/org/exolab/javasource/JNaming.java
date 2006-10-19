/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.javasource;

/**
 * A utility class used to validate identifiers and class names.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class JNaming {

    /**
     * Reserved keywords in Java as of Java 5.
     */
    private static final String[] KEYWORDS = {
        "abstract",
        "boolean",
        "break",
        "byte",
        "case",
        "catch",
        "char",
        "class",
        "const",
        "continue",
        "default",
        "do",
        "double",
        "else",
        "enum",
        "extends",
        "false",
        "final",
        "finally",
        "float",
        "for",
        "goto",
        "if",
        "implements",
        "import",
        "instanceof",
        "int",
        "interface",
        "long",
        "native",
        "new",
        "null",
        "package",
        "private",
        "protected",
        "public",
        "return",
        "short",
        "static",
        "super",
        "switch",
        "synchronized",
        "this",
        "throw",
        "throws",
        "transient",
        "true",
        "try",
        "void",
        "volatile",
        "while"
    }; //-- keywords

    /**
     * Collection classes in Java as of Java 5.
     */
    private static final String[] COLLECTIONS = {
        "ArrayList",
        "List",
        "Set",
        "Collection",
        "Vector",
        "Hashtable",
        "Map",
        "HashMap",
        "HashSet",
        "TreeSet",
        "Enumeration",
        "Iterator",
        "ListIterator",
        "SortedSet",
        "SortedMap",
        "Queue",
        "EnumSet",
        "EnumMap",
        "IdentityHashMap",
        "LinkedHashMap",
        "LinkedHashSet",
        "LinkedList",
        "Stack",
        "TreeMap",
        "WeakHashMap"
    }; //-- Collections

    /**
     * Classes in java.lang.* as of Java 5.
     */
    private static final String[] JAVA_LANG = {
        // Interfaces in java.lang.*
        "Appendable",
        "CharSequence",
        "Cloneable",
        "Comparable",
        "Iterable",
        "Readable",
        "Runnable",
        // Classes in java.lang.*
        "Boolean",
        "Byte",
        "Character",
        "Class",
        "ClassLoader",
        "Compiler",
        "Double",
        "Enum",
        "Float",
        "InheritableThreadLocal",
        "Integer",
        "Long",
        "Math",
        "Number",
        "Object",
        "Package",
        "Process",
        "ProcessBuilder",
        "Runtime",
        "RuntimePermission",
        "SecurityManager",
        "Short",
        "StackTraceElement",
        "StrictMath",
        "String",
        "StringBuffer",
        "StringBuilder",
        "System",
        "Thread",
        "ThreadGroup",
        "ThreadLocal",
        "Throwable",
        "Void",
        // Exceptions in java.lang.*
        "ArithmeticException",
        "ArrayIndexOutOfBoundsException",
        "ArrayStoreException",
        "ClassCastException",
        "ClassNotFoundException",
        "CloneNotSupportedException",
        "EnumConstantNotPresentException",
        "Exception",
        "IllegalAccessException",
        "IllegalArgumentException",
        "IllegalMonitorStateException",
        "IllegalStateException",
        "IllegalThreadStateException",
        "IndexOutOfBoundsException",
        "InstantiationException",
        "InterruptedException",
        "NegativeArraySizeException",
        "NoSuchFieldException",
        "NoSuchMethodException",
        "NullPointerException",
        "NumberFormatException",
        "RuntimeException",
        "SecurityException",
        "StringIndexOutOfBoundsException",
        "TypeNotPresentException",
        "UnsupportedOperationException",
        // Errors in java.lang.*
        "AbstractMethodError",
        "AssertionError",
        "ClassCircularityError",
        "ClassFormatError",
        "Error",
        "ExceptionInInitializerError",
        "IllegalAccessError",
        "IncompatibleClassChangeError",
        "InstantiationError",
        "InternalError",
        "LinkageError",
        "NoClassDefFoundError",
        "NoSuchFieldError",
        "NoSuchMethodError",
        "OutOfMemoryError",
        "StackOverflowError",
        "ThreadDeath",
        "UnknownError",
        "UnsatisfiedLinkError",
        "UnsupportedClassVersionError",
        "VerifyError",
        "VirtualMachineError",
        // Annotation types in java.lang.*
        "Deprecated",
        "Override",
        "SuppressWarnings"
    };

    /**
     * Field names used within Castor that prevent a user from using the same
     * name as an element without using a mapping file. Some fields that Castor
     * uses depend on the contents of the schema, so we only warn.
     */
    private static final String[] CASTOR_RESERVED = {
        "Content"
    };

    /**
     * As we're a static utility class, our constructor is private.
     */
    private JNaming() {
        super();
    }

    /**
     * Returns true if the given String is a Java keyword which will cause a
     * problem when used as a variable name.
     *
     * @param name the String to check against the list of keywords
     * @return true if the given String is a Java keyword which will cause a
     *         problem when used as a variable name.
     */
    public static boolean isKeyword(final String name) {
        if (name == null) { return false; }
        for (int i = 0; i < KEYWORDS.length; i++) {
            if (KEYWORDS[i].equals(name)) { return true; }
        }
        return false;
    } //-- isKeyword

    /**
     * Returns true if the given String is a parameterized Java collection.
     * object keyword which will cause a problem when used as a variable name
     *
     * @param name the String to check as a parameterized Java collection
     * @return true if the given String is a parameterized Java collection
     *         object keyword which will cause a problem when used as a variable
     *         name.
     */
    public static boolean isParameterizedCollectionsObject(final String name) {
        if (name == null) { return false; }
        for (int i = 0; i < COLLECTIONS.length; i++) {
            if (name.indexOf(COLLECTIONS[i]) != -1) { return true; }
        }
        return false;
    } //-- isParameterizedCollectionsObject

    /**
     * Returns true if the given String is a Java class in java.lang.* which
     * will cause a problem when used as a variable name.
     *
     * @param name the String to check against the list of keywords
     * @return true if the given String is a Java class in java.lang.* which
     *         will cause a problem when used as a variable name.
     */
    public static boolean isInJavaLang(final String name) {
        if (name == null) { return false; }
        for (int i = 0; i < JAVA_LANG.length; i++) {
            if (JAVA_LANG[i].equals(name)) {
                return true;
            }
        }
        return false;
    } //-- isKeyword

    /**
     * Returns true if the given String is a reserved name by Castor which may
     * cause a problem when used as a variable name. Some fields that Castor
     * uses depend on the contents of the schema, so we only warn.
     *
     * @param name
     *            the String to check against the list of keywords
     * @return true if the given String is a reserved name by Castor which may
     *         cause a problem when used as a variable name.
     */
    public static boolean isReservedByCastor(final String name) {
        if (name == null) { return false; }
        for (int i = 0; i < CASTOR_RESERVED.length; i++) {
            if (CASTOR_RESERVED[i].equals(name)) {
                return true;
            }
        }
        return false;
    } //-- isKeyword

    /**
     * Returns true if the given String matches the production of a valid Java
     * identifier.
     *
     * @param string the String to check the production of
     * @return true if the given String matches the production of a valid Java
     *         name, otherwise false.
     */
    public static boolean isValidJavaIdentifier(final String string) {
        if ((string == null) || (string.length() == 0)) { return false; }

        char[] chars = string.toCharArray();

        if (isParameterizedCollectionsObject(string)) {
            return true;
        }

        //-- make sure starting character is valid
        if (!Character.isJavaIdentifierStart(chars[0])) { return false; }

        for (int i = 1; i < chars.length; i++) {
            if (!Character.isJavaIdentifierPart(chars[i])) { return false; }
        }
        if (isKeyword(string)) { return false; }
        return true;
    } //-- isValidJavaIdentifier

} //-- JNaming
