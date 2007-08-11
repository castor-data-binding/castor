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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.castor.xml;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class converts XML Names to proper Java names. As Java names are not
 * completely defined this implementation is Castor specific.
 * The first implementation was done by <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * but had been changed radically since.
 * 
 * @author <a href="mailto:jgrueneis_at_gmail_dot_com">Joachim Grueneis</a>
 * @version $Id$
 */
public class JavaNamingImpl implements JavaNaming {
    /** Logger of this class. */
    private static final Log LOG = LogFactory.getLog(JavaNamingImpl.class);

    /**
     * The property name to use in the castor.properties file to specify the
     * value of the <code>upperCaseAfterUnderscore</code> variable.
     */
    public static final String UPPER_CASE_AFTER_UNDERSCORE_PROPERTY 
    = "org.exolab.castor.xml.JavaNaming.upperCaseAfterUnderscore";

    /**
     * Used for backward compatibility, if you wish to be backward compatible
     * with 0.9.3.9 and earlier set this boolean to true.
     */
    public static boolean _upperCaseAfterUnderscore = false;
    
    /** the map of substition words for all keywords. */
    private static final Hashtable SUBST = keywordMap();
    /** all known Java keywords. */
    private static final String[] KEYWORDS = {"abstract", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default", "do", "double", "else",
            "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package",
            "private", "protected", "public", "return", "short", "static", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
            "volatile", "while"}; // -- KEYWORDS

    /**
     * private constructor.
     */
    public JavaNamingImpl() {
        super();
    } // -- JavaNaming

    /**
     * Returns true if the given String is a Java keyword which will cause a
     * problem when used as a variable name.
     * @param name the name to check
     * @return true if it is a keyword
     * @see org.castor.xml.JavaNaming#isKeyword(java.lang.String)
     */
    public final boolean isKeyword(final String name) {
        if (name == null) {
            return false;
        }
        for (int i = 0; i < KEYWORDS.length; i++) {
            if (KEYWORDS[i].equals(name)) {
                return true;
            }
        }
        return false;
    } // -- isKeyword

    /**
     * Returns true if the given String matches the production of a valid Java
     * identifier.
     * 
     * @param string
     *            The String to check the production of.
     * @return true if the given String matches the production of a valid Java
     *         name, otherwise false.
     * @see org.castor.xml.JavaNaming#isValidJavaIdentifier(java.lang.String)
     */
    public final boolean isValidJavaIdentifier(final String string) {
        if ((string == null) || (string.length() == 0)) {
            return false;
        }

        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);

            // -- digit
            if (ch == '_') {
                continue;
            }
            if (ch == '$') {
                continue;
            }

            if ((ch >= 'A') && (ch <= 'Z')) {
                continue;
            }
            if ((ch >= 'a') && (ch <= 'z')) {
                continue;
            }
            if ((ch >= '0') && (ch <= '9')) {
                if (i == 0) {
                    return false;
                }
                continue;
            }

            return false;
        }
        if (isKeyword(string)) {
            return false;
        }
        return true;
    } // -- isValidJavaIdentifier

    /**
     * Cuts away a leading namespace prefix (if there is one in place).
     * @param name the XML name to convert to a Java name
     * @return a name which follows Java naming conventions
     * @see org.castor.xml.JavaNaming#toJavaClassName(java.lang.String)
     */
    public final String toJavaClassName(final String name) {

        if ((name == null) || (name.length() <= 0)) {
            // handle error
            return name; // -- for now just return name
        }
        // Remove namespace prefix (Andrew Fawcett, temporary until namespace
        // changes go in)
        int colon = name.indexOf(':');
        if (colon != -1) {
            return toJavaName(name.substring(colon + 1), true);
        }
        return toJavaName(name, true);

    } // -- toJavaClassName

    /**
     * Appends a leading '_' and converts the given name to a java name.
     * @param name the XML name to convert
     * @return a Java member name starting with a leading _
     * @see org.castor.xml.JavaNaming#toJavaMemberName(java.lang.String)
     */
    public final String toJavaMemberName(final String name) {
        return toJavaMemberName(name, true);
    } // -- toJavaMemberName

    /**
     * Appends a leading '_' and converts the given name to a java name.
     * @param name the XML name to convert
     * @param useKeywordSubstitutions set to true to turn on keyword substitution 
     * @return a Java member name starting with a leading _
     * @see org.castor.xml.JavaNaming#toJavaMemberName(java.lang.String,boolean)
     */
    public final String toJavaMemberName(final String name, final boolean useKeywordSubstitutions) {

        if (name == null) {
            return null;
        }

        String memberName = toJavaName(name, false);

        if (isKeyword(memberName) && useKeywordSubstitutions) {
            String mappedName = (String) SUBST.get(memberName);
            if (mappedName != null) {
                memberName = mappedName;
            } else {
                memberName = "_" + memberName;
            }
        }
        return memberName;
    } // -- toJavaMemberName

    /**
     * Checks if the given pacckage name is valid or not. Empty pacakge names
     * are considered valid!
     * 
     * @param packageName
     *            name of package as String with periods
     * @return true if package name is valid
     * @see org.castor.xml.JavaNaming#isValidPackageName(java.lang.String)
     */
    public final boolean isValidPackageName(final String packageName) {
        if ((packageName == null) || (packageName.length() < 1)) {
            return true;
        }
        if (".".equals(packageName)) {
            return false;
        }
        if (packageName.startsWith(".") || (packageName.endsWith("."))) {
            return false;
        }
        boolean valid = true;
        String[] packageNameParts = packageName.split("\\.");
        for (int i = 0; i < packageNameParts.length; i++) {
            String packageNamePart = packageNameParts[i];
            valid &= isValidJavaIdentifier(packageNamePart);
        }
        return valid;
    }

    /**
     * Converts the given Package name to it's corresponding Path. The path will
     * be a relative path.
     * @param packageName the package name to convert
     * @return a String containing the resulting patch
     * @see org.castor.xml.JavaNaming#packageToPath(java.lang.String)
     */
    public final String packageToPath(final String packageName) {
        if (packageName == null) {
            return packageName;
        }
        if (!isValidPackageName(packageName)) {
            String message = "Package name: " + packageName + " is not valid";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        return packageName.replace('.', File.separatorChar);
    } // -- packageToPath

    /**
     * To initialize the keyword map.
     * @return an initialized keyword map
     */
    private static Hashtable keywordMap() {
        Hashtable ht = new Hashtable();
        ht.put("class", "clazz");
        return ht;
    } // -- keywordMap

    /**
     * Converts the given xml name to a Java name.
     * 
     * @param name
     *            the name to convert to a Java Name
     * @param upperFirst
     *            a flag to indicate whether or not the the first character
     *            should be converted to uppercase.
     * @return the resulting Java name
     */
    private String toJavaName(final String name, final boolean upperFirst) {

        int size = name.length();
        char[] ncChars = name.toCharArray();
        int next = 0;

        boolean uppercase = upperFirst;

        // -- initialize lowercase, this is either (!uppercase) or
        // -- false depending on if the first two characters
        // -- are uppercase
        boolean lowercase = (!uppercase);
        if ((size > 1) && lowercase) {
            if (Character.isUpperCase(ncChars[0]) && Character.isUpperCase(ncChars[1])) {
                lowercase = false;
            }
        }

        for (int i = 0; i < size; i++) {
            char ch = ncChars[i];

            switch (ch) {
            case '.':
            case ' ':
                ncChars[next++] = '_';
                break;
            case ':':
            case '-':
                uppercase = true;
                break;
            case '_':
                // -- backward compatibility with 0.9.3.9
                if (_upperCaseAfterUnderscore) {
                    uppercase = true;
                    ncChars[next] = ch;
                    ++next;
                    break;
                }
                // -- for backward compatibility with 0.9.3
                /*
                 * if (replaceUnderscore) { uppercase = true; break; }
                 */
                // --> do not break here for anything greater
                // --> than 0.9.3.9
            default:
                if (uppercase) {
                    ncChars[next] = Character.toUpperCase(ch);
                    uppercase = false;
                } else if (lowercase) {
                    ncChars[next] = Character.toLowerCase(ch);
                    lowercase = false;
                } else {
                    ncChars[next] = ch;
                }
                ++next;
                break;
            }
        }
        return new String(ncChars, 0, next);
    } // -- toJavaName

    /**
     * Qualifies the given <code>fileName</code> with the given
     * <code>packageName</code> and returns the resulting file path.<br>
     * If <code>packageName</code> is <code>null</code> or a zero-length
     * String, this method will return <code>fileName</code>.<br>
     * 
     * @param fileName
     *            The file name to be qualified.
     * @param packageName
     *            The package name to be used for qualifying.
     * @return The qualified file path.
     * @see org.castor.xml.JavaNaming#getQualifiedFileName(java.lang.String,java.lang.String)
     */
    public final String getQualifiedFileName(final String fileName, final String packageName) {
        if ((packageName == null) || (packageName.length() == 0)) {
            return fileName;
        }
        StringBuffer result = new StringBuffer();
        result.append(packageToPath(packageName));
        result.append('/');
        result.append(fileName);
        return result.toString();
    } // -- getQualifiedFileName

    /**
     * Gets the package name of the given class name.
     * 
     * @param className
     *            The class name to retrieve the package name from.
     * @return The package name or the empty String if <code>className</code>
     *         is <code>null</code> or does not contain a package.
     * @see org.castor.xml.JavaNaming#getPackageName(java.lang.String)
     */
    public final String getPackageName(final String className) {
        if ((className == null) || (className.length() < 1)) {
            return className;
        }

        int idx = className.lastIndexOf('.');
        if (idx >= 0) {
            return className.substring(0, idx);
        }
        return "";
    } // -- getPackageName

    /**
     * Extracts the filed name part from the methods name. Mostly it cuts
     * away the method prefix.
     * @param method the Method to process
     * @return the extracted field name
     * @see org.castor.xml.JavaNaming#extractFieldNameFromMethod(java.lang.reflect.Method)
     */
    public final String extractFieldNameFromMethod(final Method method) {
        if (method == null) {
            return null;
        }
        String fieldName = null;
        if (isSetMethod(method)) {
            fieldName = method.getName().substring(METHOD_PREFIX_SET.length());
        } else if (isCreateMethod(method)) {
            fieldName = method.getName().substring(METHOD_PREFIX_CREATE.length());
        } else if (isGetMethod(method)) {
            fieldName = method.getName().substring(METHOD_PREFIX_GET.length());
        } else if (isIsMethod(method)) {
            fieldName = method.getName().substring(METHOD_PREFIX_IS.length());
        } else if (isAddMethod(method)) {
            fieldName = method.getName().substring(METHOD_PREFIX_ADD.length());
        }
        return toJavaMemberName(fieldName);
    }

    /**
     * Checks if the given method is a set method.
     * @param method the Method to check
     * @return true if it is a set method
     * @see org.castor.xml.JavaNaming#isSetMethod(java.lang.reflect.Method)
     */
    public final boolean isSetMethod(final Method method) {
        if (method == null) {
            return false;
        }
        if (!method.getName().startsWith(METHOD_PREFIX_SET)) {
            return false;
        }
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        if ((method.getReturnType() != void.class) && (method.getReturnType() != Void.class)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given method is a create method.
     * @param method the Method to check
     * @return true if it is a create method
     * @see org.castor.xml.JavaNaming#isCreateMethod(java.lang.reflect.Method)
     */
    public final boolean isCreateMethod(final Method method) {
        if (method == null) {
            return false;
        }
        if (!method.getName().startsWith(METHOD_PREFIX_CREATE)) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (method.getReturnType() == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given method is a get method.
     * @param method the Method to check
     * @return true if it is a get method
     * @see org.castor.xml.JavaNaming#isGetMethod(java.lang.reflect.Method)
     */
    public final boolean isGetMethod(final Method method) {
        if (method == null) {
            return false;
        }
        if (!method.getName().startsWith(METHOD_PREFIX_GET)) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (method.getReturnType() == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given method is a 'is' method.
     * @param method the Method to check
     * @return true if it is a 'is' method
     * @see org.castor.xml.JavaNaming#isIsMethod(java.lang.reflect.Method)
     */
    public final boolean isIsMethod(final Method method) {
        if (method == null) {
            return false;
        }
        if (!method.getName().startsWith(METHOD_PREFIX_IS)) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if ((method.getReturnType().isPrimitive()) && (method.getReturnType() != Boolean.TYPE)) {
            return false;
        }
        if ((!method.getReturnType().isPrimitive()) && (method.getReturnType() != Boolean.class)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given method is an add method.
     * @param method the Method to check
     * @return true if it is an add method
     * @see org.castor.xml.JavaNaming#isAddMethod(java.lang.reflect.Method)
     */
    public final boolean isAddMethod(final Method method) {
        if (method == null) {
            return false;
        }
        if (!method.getName().startsWith(METHOD_PREFIX_ADD)) {
            return false;
        }
        if (method.getParameterTypes().length != 1) {
            return false;
        }
        if ((method.getReturnType() != void.class) && (method.getReturnType() != Void.class)) {
            return false;
        }
        return true;
    }

    /**
     * Generates the name of an add method for the given field name.
     * @param fieldName the field name to generate a method name for
     * @return the generated add method name
     */
    public final String getAddMethodNameForField(final String fieldName) {
        return METHOD_PREFIX_ADD + toJavaClassName(fieldName);
    }

    /**
     * Generates the name of a set method for the given field name.
     * @param fieldName the field name to generate a method name for
     * @return the generated set method name
     */
    public final String getCreateMethodNameForField(final String fieldName) {
        return METHOD_PREFIX_CREATE + toJavaClassName(fieldName);
    }

    /**
     * Generates the name of a get method for the given field name.
     * @param fieldName the field name to generate a method name for
     * @return the generated get method name
     */
    public final String getGetMethodNameForField(final String fieldName) {
        return METHOD_PREFIX_GET + toJavaClassName(fieldName);
    }

    /**
     * Generates the name of an is method for the given field name.
     * @param fieldName the field name to generate a method name for
     * @return the generated is method name
     */
    public final String getIsMethodNameForField(final String fieldName) {
        return METHOD_PREFIX_IS + toJavaClassName(fieldName);
    }

    /**
     * Generates the name of a create method for the given field name.
     * @param fieldName the field name to generate a method name for
     * @return the generated create method name
     */
    public final String getSetMethodNameForField(final String fieldName) {
        return METHOD_PREFIX_SET + toJavaClassName(fieldName);
    }
} // -- JavaNaming
