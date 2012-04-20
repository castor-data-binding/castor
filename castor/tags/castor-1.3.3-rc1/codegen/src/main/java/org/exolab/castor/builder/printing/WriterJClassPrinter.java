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
import org.exolab.javasource.JComment;

/**
 * Prints a given {@link JClass} to the file system using the
 * {@link JClass#print(org.exolab.javasource.JSourceWriter)} method.
 */
public class WriterJClassPrinter implements JClassPrinter {

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.builder.printing.JClassPrinter#printClass(org.exolab.javasource.JClass,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public void printClass(final JClass jClass, final String outputDir,
            final String lineSeparator, final String header) {
        
        // hack for the moment
        // to avoid the compiler complaining with java.util.Date
        jClass.removeImport("org.exolab.castor.types.Date");

        // add header
        JComment comment = new JComment(JComment.HEADER_STYLE);
        comment.appendComment(header);
        jClass.setHeader(comment);

        // print
        jClass.print(outputDir, lineSeparator);
    }
}
