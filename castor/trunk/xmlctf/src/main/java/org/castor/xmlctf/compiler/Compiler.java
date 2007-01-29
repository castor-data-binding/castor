/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.xmlctf.compiler;

/**
 * A Compilation interface used by the Castor Testing Framework.
 */
public interface Compiler {

    /**
     * Compiles the content of a directory tree.
     * @throws CompilationException If the build fails.
     */
    void compileDirectory() throws CompilationException;

    /**
     * Configures the compiler to provide the -source argument for compilation.
     * The Java source version is a float that is expected to be a value such as
     * <tt>1.3</tt>, <tt>1.4</tt>, or<tt>1.5</tt>. However, integral
     * values higher than 4 will be converted to the proper value so for example
     * you can provide <tt>5</tt> as the Java source version.
     *
     * @param javaSourceVersion The Java source version.
     */
    void setJavaSourceVersion(float javaSourceVersion);

}
