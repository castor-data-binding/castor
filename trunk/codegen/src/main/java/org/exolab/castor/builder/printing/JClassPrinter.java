/*
 * Copyright 2005-2007 Werner Guttmann
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
package org.exolab.castor.builder.printing;

import org.exolab.javasource.JClass;

/**
 * Interface for printing {@link JClass} instances as Java source code to the 
 * file system.
 */
public interface JClassPrinter {
    
    /**
     * Prints the given {@link JClass} instance to the given output directory. 
     *  
     * @param jClass The JClass to print.
     * @param outputDir The target directory.
     * @param lineSeparator The line separator to use.
     * @param header The standard header to print.
     */
    void printClass(final JClass jClass, final String outputDir, 
            final String lineSeparator, final String header);

}
