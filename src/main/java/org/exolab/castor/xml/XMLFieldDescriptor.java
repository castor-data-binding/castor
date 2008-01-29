/*
 * Copyright 2006 Assaf Arkin, Keith Visco, Ralf Joachim
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
package org.exolab.castor.xml;

import java.util.List;

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * XML field descriptor. Wraps {@link FieldDescriptor} and adds
 * XML-related information, type conversion, and so on.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2004-09-17 00:47:41 -0600 (Fri, 17 Sep 2004) $
 */
public interface XMLFieldDescriptor extends FieldDescriptor {
    /** The xml:space property. */
    String PROPERTY_XML_SPACE = "xml:space";

    /** The xml:lang property. */
    String PROPERTY_XML_LANG = "xml:lang";

    /**
     * Returns the index within the constructor argument array where the value
     * of this field should be. A value less than zero indicates that the value
     * of this field is set via a normal setter method and not via the
     * constructor.
     * <p>
     * Note: This only applies to attribute mapped fields at this time.
     *
     * @return the index within the constructor argument array for this field.
     * @see #isConstructorArgument()
     */
    int getConstructorArgumentIndex();

    /**
     * Returns true if the value of the field represented by this descriptor
     * should be set via the constructor of the containing class. This is only
     * valid for attribute mapped fields.
     *
     * @return true if the value of the field represented by this descriptor
     *         should be set via the constructor of the containing class.
     */
    boolean isConstructorArgument();

    /**
     * Returns the "relative" XML path for the field being described.
     * <p>
     * In most cases, this will be null. However sometimes a field may be mapped
     * to a nested element. In which case the value returned by this method
     * should be the nested element name. If more than one level of nesting is
     * needed each nested element name should be separated by by a path
     * separator (forward slash '/').
     * <p>
     * The location path name is "relative" to the parent Class. The name of the
     * parent should not be included in the path.
     * <p>
     * For example, give the following two classes:
     * <pre>
     *    class Root {
     *        Bar bar;
     *    }
     *
     *    class Bar {
     *       String value;
     *    }
     * </pre>
     *
     * And the following XML:
     *
     * <pre>
     *    &lt;root&gt;
     *       &lt;foo&gt;
     *          &lt;bar&gt; value of bar &lt;/bar&gt;
     *       &lt;/foo&gt;
     *    &lt;/root&gt;
     * </pre>
     *
     * Since foo has no associated class, the path for 'bar' would be: "foo"
     *
     * @return The "relative" XML path for the field being described.
     */
    String getLocationPath();

    /**
     * Return the "suggested" namespace prefix to use when marshaling as XML.
     *
     * @return the "suggested" namespace prefix.
     */
    String getNameSpacePrefix();

    /**
     * Returns the namespace URI to be used when marshaling and unmarshaling as
     * XML.
     *
     * @return the namespace URI.
     */
    String getNameSpaceURI();

    /**
     * Returns the NodeType of the Field being described. The NodeType
     * represents the Type of Node that the Field will be marshaled into XML as.
     *
     * @return the NodeType of the Field being described.
     */
    NodeType getNodeType();

    /**
     * Returns the value property with the given name or null if no such
     * property exists. This method is useful for future evolutions of this
     * interface as well as for user-defined extensions. See class declared
     * properties for built-in properties.
     *
     * @param propertyName the name of the property whose value should be
     *        returned.
     *
     * @return the value of the property, or null.
     */
    String getProperty(String propertyName);

    /**
     * Returns the XML Schema type of the XML field being described.
     *
     * @return the XML Schema type of the XML field being described.
     */
    String getSchemaType();

    /**
     * Returns a specific validator for the field described by this descriptor.
     * A null value may be returned if no specific validator exists.
     *
     * @return the field validator for the described field
     */
    FieldValidator getValidator();

    /**
     * Returns the XML Name for the field being described.
     *
     * @return the XML name.
     */
    String getXMLName();

    /**
     * Returns true if the field described by this descriptor is a container
     * field. A container is a field that is not a first-class object,
     * and should therefore have no XML representation.
     *
     * @return true if the field is a container
     */
    boolean isContainer();

    /**
     * Returns the incremental flag which when true indicates that this member
     * may be safely added before the unmarshaler is finished unmarshaling it.
     *
     * @return true if the Object can safely be added before the unmarshaler is
     *         finished unmarshaling the Object.
     */
    boolean isIncremental();

    /**
     * Returns true if the field described by this descriptor is Map or
     * Hashtable. If this method returns true, it must also return true for any
     * call to {@link #isMultivalued}.
     *
     * @return true if the field described by this desciptor is a Map or
     *         Hashtable, otherwise false.
     */
    boolean isMapped();

    /**
     * Returns true if the field described by this descriptor can contain more
     * than one value.
     *
     * @return true if the field described by this descriptor can contain more
     *         than one value
     */
    boolean isMultivalued();

    /**
     * Returns true if the field described by this descriptor may be nillable. A
     * nillable field is one that may have empty content and still be valid.
     * Please see the XML Schema 1.0 Recommendation for more information on
     * nillable.
     *
     * @return true if the field may be nillable.
     */
    boolean isNillable();

    /**
     * Returns true if the field described by this descriptor is a reference
     * (ie. IDREF) to another object in the "Object Model" (e.g., XML tree).
     *
     * @return true if the field described by this descriptor is a reference to
     *         another object in the "Object Model"
     */
    boolean isReference();

    /**
     * Returns true if this descriptor can be used to handle elements or
     * attributes with the given XML name. By default this method simply
     * compares the given XML name with the internal XML name. This method can
     * be overridden to provide more complex matching.
     *
     * @param xmlName the XML name to compare
     * @return true if this descriptor can be used to handle elements or
     *         attributes with the given XML name.
     */
    boolean matches(String xmlName);

    /**
     * Returns true if this descriptor can be used to handle elements or
     * attributes with the given XML name. By default this method simply
     * compares the given XML name with the internal XML name. This method can
     * be overridden to provide more complex matching.
     *
     * @param xmlName the XML name to compare
     * @param namespace the namespace URI
     *
     * @return true if this descriptor can be used to handle elements or
     *         attributes with the given XML name.
     */
    boolean matches(String xmlName, String namespace);
    
    /**
     * Returns the possible substitution groups for this class.
     * @return the possible substitution groups for this class.
     */
    List getSubstitutes();

    /**
     * Sets the possible substitution groups for this class.
     * @param substitutes Possible substitution groups for this class.
     */
    void setSubstitutes(List substitutes); 

    /**
     * Sets whether the field described by this {@link XMLFieldDescriptorImpl} is
     * created as a result of a <xs:list> definition.
     * @param derivedFromXSList A boolean value, true or false.
     */
    void setDerivedFromXSList(boolean derivedFromXSList);

    /**
     * Indicates whether the field described by this {@link XMLFieldDescriptorImpl} is
     * created as a result of a <xs:list> definition.
     * @param derivedFromXSList Trueif the field described by this {@link XMLFieldDescriptorImpl} is
     * created as a result of a <xs:list> definition.
.
     */
    boolean isDerivedFromXSList();
    
    /**
     * Returns the component type of this {@link XMLFieldDescriptor} instance, if 
     * the field described represents a collection of values.
     * @return The collection's component type.
     * @see XMLFieldDescriptor#getSchemaType()
     */
    String getComponentType();

    
}
