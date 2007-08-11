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

import java.lang.reflect.Method;

/**
 * JavaNaming is a service which collects all methods that are related to
 * create (modify) Java names. E.g. convert from XML name to Java name,
 * get a Java member name or such. These rules can be exchanged by a different
 * implementation to get a different naming style for e.g. JAXB.
 * 
 * @author <a href="mailto:jgrueneis_at_gmail_dot_com">Joachim Grueneis</a>
 * @version $Id$
 */
public interface JavaNaming {
    /** Add methods start with: add. */
    public static final String METHOD_PREFIX_ADD = "add";

    /** Get methods start with: get. */
    public static final String METHOD_PREFIX_GET = "get";

    /** Is methods start with: is. */
    public static final String METHOD_PREFIX_IS = "is";

    /** Set methods start with: set. */
    public static final String METHOD_PREFIX_SET = "set";

    /** Create methods start with: create. */
    public static final String METHOD_PREFIX_CREATE = "create";

    /**
     * Returns true if the given String is a Java keyword which will cause a
     * problem when used as a variable name.
     * 
     * @param name The name to check.
     * @return true if it is a keyword.
     */
    boolean isKeyword(String name);
    
    /**
     * Returns true if the given String matches the production of a valid Java
     * identifier.
     * 
     * @param string The String to check the production of.
     * @return true if the given String matches the production of a valid Java
     *         name, otherwise false.
     */
    boolean isValidJavaIdentifier(String string);
    
    /**
     * Cuts away a leading namespace prefix (if there is one in place).
     * 
     * @param name The XML name to convert to a Java name.
     * @return A name which follows Java naming conventions.
     */
    String toJavaClassName(String name);
    
    /**
     * Appends a leading '_' and converts the given name to a java name.
     * 
     * @param name the XML name to convert.
     * @return A Java member name starting with a leading '_'.
     */
    String toJavaMemberName(String name);
    
    /**
     * Appends a leading '_' and converts the given name to a java name.
     * 
     * @param name The XML name to convert.
     * @param useKeywordSubstitutions Set to true to turn on keyword substitution.
     * @return A Java member name starting with a leading '_'.
     */
    String toJavaMemberName(String name, boolean useKeywordSubstitutions);
    
    /**
     * Checks if the given pacckage name is valid or not. Empty pacakge names
     * are considered valid!
     * 
     * @param packageName Name of package as String with periods.
     * @return true if package name is valid.
     */
    boolean isValidPackageName(String packageName);
    
    /**
     * Converts the given Package name to it's corresponding Path. The path will
     * be a relative path.
     * 
     * @param packageName The package name to convert.
     * @return A String containing the resulting patch.
     */
    String packageToPath(String packageName);
    
    /**
     * Qualifies the given <code>fileName</code> with the given
     * <code>packageName</code> and returns the resulting file path.<br>
     * If <code>packageName</code> is <code>null</code> or a zero-length
     * String, this method will return <code>fileName</code>.<br>
     * 
     * @param fileName The file name to be qualified.
     * @param packageName The package name to be used for qualifying.
     * @return The qualified file path.
     */
    String getQualifiedFileName(String fileName, String packageName);
    
    /**
     * Gets the package name of the given class name.
     * 
     * @param className The class name to retrieve the package name from.
     * @return The package name or the empty String if <code>className</code>
     *         is <code>null</code> or does not contain a package.
     */
    String getPackageName(String className);
    /**
     * Extracts the filed name part from the methods name. Mostly it cuts
     * away the method prefix.
     * 
     * @param method The Method to process.
     * @return The extracted field name.
     */
    String extractFieldNameFromMethod(Method method);
    
    /**
     * Checks if the given method is a set method.
     * 
     * @param method The Method to check
     * @return true if it is a set method
     */
    boolean isSetMethod(Method method);
    /**
     * Checks if the given method is a create method.
     * 
     * @param method The Method to check.
     * @return true if it is a create method.
     */
    boolean isCreateMethod(Method method);
    
    /**
     * Checks if the given method is a get method.
     * 
     * @param method The Method to check.
     * @return true if it is a get method.
     */
    boolean isGetMethod(Method method);
    
    /**
     * Checks if the given method is an is method.
     * 
     * @param method The Method to check.
     * @return true if it is an is method.
     */
    boolean isIsMethod(Method method);
    
    /**
     * Checks if the given method is an add method.
     * 
     * @param method The Method to check.
     * @return true if it is an add method.
     */
    boolean isAddMethod(Method method);
    /**
     * Generates the name of an add method for the given field name.
     * 
     * @param fieldName The field name to generate a method name for.
     * @return The generated add method name.
     */
    String getAddMethodNameForField(String fieldName);
    
    /**
     * Generates the name of a set method for the given field name.
     * 
     * @param fieldName The field name to generate a method name for.
     * @return The generated set method name.
     */
    String getSetMethodNameForField(String fieldName);
    
    /**
     * Generates the name of a get method for the given field name.
     * 
     * @param fieldName The field name to generate a method name for.
     * @return The generated get method name.
     */
    String getGetMethodNameForField(String fieldName);
    
    /**
     * Generates the name of an is method for the given field name.
     * 
     * @param fieldName The field name to generate a method name for.
     * @return The generated is method name.
     */
    String getIsMethodNameForField(String fieldName);
    
    /**
     * Generates the name of a create method for the given field name.
     * 
     * @param fieldName The field name to generate a method name for.
     * @return The generated create method name.
     */
    String getCreateMethodNameForField(String fieldName);
}