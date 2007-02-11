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
package org.exolab.castor.builder.binding;

import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Structure;

/**
 * Helper class to assemble an XPATH expression to qualify the path of 
 * an XML schema structure from the XML schema root. 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 */
public class XPathHelper {

    /**
     * Initial size of the {@link StringBuffer} used to asseble the XPATH
     * expression.
     */
    private static final int INITIAL_XPATH_SIZE = 50;

    /**
     * Deduces an XPATH expression qualifying the path from the schema root
     * to the given structure in question.
     * @param structure AN XML structure.
     * @param location The XPATH expression to be created.
     */
    public static void getSchemaLocation(
            final Structure structure,
            final StringBuffer location) {
        if (structure == null) {
            throw new IllegalArgumentException("Structure cannot be null");
        }

        if (location == null) {
            throw new IllegalArgumentException("location cannot be null");
        }

        Structure parent = null;
        switch (structure.getStructureType()) {
        case Structure.ELEMENT:
            parent = ((ElementDecl) structure).getParent();
            if (parent.getStructureType() != Structure.SCHEMA) {
                getSchemaLocation(parent, location);
            }
            location.append(ExtendedBinding.PATH_SEPARATOR);
            location.append(((ElementDecl) structure).getName());
            break;

        case Structure.COMPLEX_TYPE:
            ComplexType complexType = (ComplexType) structure;
            parent = (complexType).getParent();
            if (parent.getStructureType() != Structure.SCHEMA) {
                getSchemaLocation(parent, location);
            }
            if (complexType.getName() != null) {
                location.append(ExtendedBinding.PATH_SEPARATOR);
                location.append(ExtendedBinding.COMPLEXTYPE_ID);
                location.append(((ComplexType) structure).getName());
            }
            // else {
            // location.append(PATH_SEPARATOR);
            // location.append(COMPLEXTYPE_ID);
            // location.append("anonymous");
            // }
            break;

        case Structure.SIMPLE_TYPE:
            SimpleType simpleType = (SimpleType) structure;
            parent = simpleType.getParent();
            if (parent != null && parent.getStructureType() != Structure.SCHEMA) {
                getSchemaLocation(parent, location);
            }

            if (parent != null && simpleType.getName() != null) {
                location.append(ExtendedBinding.PATH_SEPARATOR);
                location.append(ExtendedBinding.ENUMTYPE_ID);
                location.append(((SimpleType) structure).getName());
            }
            // else {
            // location.append(PATH_SEPARATOR);
            // location.append(ENUMTYPE_ID);
            // location.append("anonymous");
            // }
            break;

        case Structure.MODELGROUP:
            ModelGroup group = (ModelGroup) structure;
            parent = group.getParent();
            if (parent.getStructureType() != Structure.SCHEMA) {
                getSchemaLocation(parent, location);
            }
            if (group.getName() != null) {
                location.append(ExtendedBinding.PATH_SEPARATOR);
                location.append(ExtendedBinding.GROUP_ID);
                location.append(group.getName());
            }
            break;

        case Structure.ATTRIBUTE:
            parent = ((AttributeDecl) structure).getParent();
            if (parent.getStructureType() != Structure.SCHEMA) {
                getSchemaLocation(parent, location);
            }
            location.append(ExtendedBinding.PATH_SEPARATOR);
            location.append(ExtendedBinding.ATTRIBUTE_PREFIX);
            location.append(((AttributeDecl) structure).getName());
            break;

        case Structure.GROUP:
            // --we are inside a complexType
            getSchemaLocation(((Group) structure).getParent(), location);
            break;

        // case Structure.ATTRIBUTE_GROUP:
        // //handle the real location

        default:
            break;
        }
    }

    /**
     * Returns a string (XPATH) representation of an XML Schema component. This
     * representation is directly adapted from XPath and will used as a key to
     * store the component bindings.
     * <p>
     * The location of a structure is composed of two parts:
     * <ol>
     *   <li>the location of the parent structure</li>
     *   <li>the local location of the structure itself</li>
     * </ol>
     * <p>
     * The local location is defined by:
     * <ul>
     *   <li>If the structure is an <b>Element</b>: the location is the XPath
     *       representation "/element_name"</li>
     *   <li>If the structure is an <b>Attribute</b>: the location is the XPath
     *       representation "/@attribute_name"</li>
     *   <li>If the structure is a <b>ComplexType</b>: the location is
     *       "complexType:complexType_name"</li>
     *   <li>If the structure is a <b>SimpleType</b>: the location is
     *       "simpleType:simpleType_name"</li>
     *   <li>If the structure is a <b>Enumeration</b>: the location is
     *       "enumType:enumType_name"</li>
     *   <li>If the structure is a <b>ModelGroup</b>: the location is
     *       "group:group_name"</li>
     * </ul>
     * Note that only top-level groups and complexTypes are named and thus will
     *
     * @param structure the structure for which to return a representation.
     * @return a string representation of an XML Schema component.
     */
    public static String getSchemaLocation(final Structure structure) {
        if (structure == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer(INITIAL_XPATH_SIZE);
        getSchemaLocation(structure, buffer);
        return buffer.toString();
    }

}
