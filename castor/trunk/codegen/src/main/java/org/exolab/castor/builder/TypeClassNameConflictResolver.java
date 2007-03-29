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
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.XMLType;
import org.exolab.javasource.JClass;

/**
 * Class name conflict resolver implementation, adding a By&lt;Type&gt; 
 * suffix to the suggested class name.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 */
public class TypeClassNameConflictResolver 
extends BaseClassNameConflictResolver 
implements ClassNameConflictResolver {

    /**
     * Changes the JClass' internal class name, appedning a type suffix to the 
     * suggested class name.
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
    public void changeClassInfoAsResultOfConflict(final JClass jClass,
            final String xpath, final String typedXPath, final Annotated annotated) {
        String typeString = typedXPath.substring(typedXPath.indexOf("[") + 1, 
                typedXPath.indexOf("]"));
        if (annotated instanceof ElementDecl) {
            ElementDecl element = (ElementDecl) annotated;
            if (element.getParent() == element.getSchema()) {
                return;
            }
            XMLType xmlType = element.getType();
            if (xmlType.isComplexType() && xmlType.getName() == null) {
                typeString = "/complexType:anon";
            }
        }
        if (typeString.startsWith("/complexType:anon")) {
            // set new classname
            String newClassName = calculateXPathPrefix(xpath) + jClass.getLocalName();
            jClass.changeLocalName(newClassName);
        } else if (typeString.startsWith("/complexType:")) {
            // set new classname
            String newClassName = jClass.getLocalName() 
            + getSourceGenerator().getAutomaticConflictResolutionTypeSuffix() 
            + typeString.substring(typeString.indexOf(":") + 1);
            jClass.changeLocalName(newClassName);
        }
    }

}
