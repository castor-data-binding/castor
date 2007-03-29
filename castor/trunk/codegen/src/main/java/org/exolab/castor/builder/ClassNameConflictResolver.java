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

import org.exolab.castor.xml.schema.Annotated;
import org.exolab.javasource.JClass;

/**
 * Class name conflict resolver.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 */
public interface ClassNameConflictResolver {

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
     *            XPATH expression used to defer the new local class name
     * @param annotated {@link Annotated} instance
     */
    void changeClassInfoAsResultOfConflict(final JClass jClass,
            final String xpath, String typedXPath, Annotated annotated);

    /**
     * Sets the calling {@link SourceGenerator} instance.
     * @param sourceGenerator The calling {@link SourceGenerator} instance.
     */
    void setSourceGenerator(final SourceGenerator sourceGenerator);

}
