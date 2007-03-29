/*
 * Copyright 2007 Werner Guttmann
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
package org.exolab.castor.builder;

import java.util.StringTokenizer;

import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.javasource.JClass;

/**
 * Base class for class name conflict resolver implementations.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 */
public abstract class BaseClassNameConflictResolver implements ClassNameConflictResolver {
    
    /**
     * Calling {@link SourceGenerator} instance, used to retrieve configuration
     * information.
     */
    private SourceGenerator _sourceGenerator;

    /**
     * Changes the JClass' internal class name, as a result of an XPATH
     * expression uniquely identifying an XML artefact within an XML schema.
     * 
     * @param jClass
     *            The {@link JClass} instance whose local name should be
     *            changed.
     * @param xpath
     *            XPATH expression used to defer the new local class name
     * @param typedXPath
     *            typed XPATH expression used to defer the new local class name
     * @param annotated {@link Annotated} instance
     */
    public abstract void changeClassInfoAsResultOfConflict(final JClass jClass,
            final String xpath, final String typedXPath, final Annotated annotated);
    

    /**
     * Calculate XPath prefix.
     * @param xpath The XPath to be transformed into a class name prefix
     * @return The class name prefix to use.
     */
    protected String calculateXPathPrefix(final String xpath) {
        String prefix = "";
        StringTokenizer stringTokenizer = new StringTokenizer(xpath, "/.");
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            // break on last token
            if (!stringTokenizer.hasMoreTokens()) {
                break;
            }
            if (token.startsWith(ExtendedBinding.COMPLEXTYPE_ID)
                    || token.startsWith(ExtendedBinding.SIMPLETYPE_ID)
                    || token.startsWith(ExtendedBinding.ENUMTYPE_ID)
                    || token.startsWith(ExtendedBinding.GROUP_ID)) {
                token = token.substring(token.indexOf(":") + 1);
            }
            prefix += JavaNaming.toJavaClassName(token);
        }
        return prefix;
    }
    
    /**
     * Sets the calling {@link SourceGenerator} instance.
     * @param sourceGenerator The calling {@link SourceGenerator} instance.
     */
    public void setSourceGenerator(final SourceGenerator sourceGenerator) {
        _sourceGenerator = sourceGenerator;
    }


    /**
     * Returns the calling {@link SourceGenerator} instance.
     * @return the calling {@link SourceGenerator} instance
     */
    public SourceGenerator getSourceGenerator() {
        return _sourceGenerator;
    }

}
