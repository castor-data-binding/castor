/*
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
 * Copyright 2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.binding;

//--Castor imports
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.builder.binding.xml.AutomaticNamingType;
import org.exolab.castor.builder.binding.xml.Binding;
import org.exolab.castor.builder.binding.xml.ComponentBindingType;
import org.exolab.castor.builder.binding.xml.Exclude;
import org.exolab.castor.builder.binding.xml.Excludes;
import org.exolab.castor.builder.binding.xml.Forces;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Structure;

/**
 * This class adds the necessary logic to a Binding Object to bring the gap
 * between the XML Schema Object Model and the Binding File. It queries the
 * Binding Object to retrieve the the associated ComponentBinding.
 * <p>
 * An "XPath like" representation of an XML Schema structure is built to lookup
 * the component bindings in their storage structure. The algorithm used to
 * build the "XPath like" representation is summarized in the following example:
 * Given the XML schema declaration:
 *
 * <pre>
 *        &lt;xsd:element name=&quot;foo&quot;&gt;
 *            &lt;xsd:complextype&gt;
 *                &lt;xsd:attribute name=&quot;bar&quot; type=&quot;xsd:string&quot;/&gt;
 *            &lt;/xsd:complextype&gt;
 *        &lt;/xsd:element&gt;
 * </pre>
 *
 * The path to identify the attribute 'bar' will be:
 *
 * <pre>
 *        /foo/@bar
 * </pre>
 *
 * The keywords <tt>complexType</tt> and <tt>group</tt> are used to identify
 * respectively an XML Schema ComplexType and a Model Group <b>definition</b>.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class ExtendedBinding extends Binding {

    /**
     * Constants needed to create the XPath.
     */
    protected static final String PATH_SEPARATOR   = "/";
    /**
     * Prefix used to identify an attribute.
     */
    protected static final String ATTRIBUTE_PREFIX = "@";
    /**
     * Prefix used to identify a complexType.
     */
    public static final String COMPLEXTYPE_ID = "complexType:";
    /**
     * Prefix used to identity a simplyType.
     */
    public static final String SIMPLETYPE_ID = "simpleType:";
    /**
     * Prefix used to identify an enumeration.
     */
    public static final String ENUMTYPE_ID = "enumType:";
    /**
     * Prefix used to identify a model group.
     */
    public static final String GROUP_ID = "group:";

    private static final short ATTRIBUTE   = 10;
    private static final short ELEMENT     = 11;
    private static final short COMPLEXTYPE = 12;
    private static final short GROUP       = 13;
    private static final short ENUM_TYPE   = 14;
    private static final short SIMPLETYPE = 15;

    /**
     * The hashtables that contain the different componentBindings.
     */
    private Hashtable<String, ComponentBindingType> _componentBindings;

    /**
     * A flag that indicates if the component bindings of that Binding have been
     * processed.
     */
    private boolean _bindingProcessed = false;
    
    /**
     * Maintains a list of element names where automatic name conflict resolution should be 
     * used all times, incl. the first one.
     */
    private Set<String> _automaticNameResolutionForced = new HashSet<String>();
    
    /**
     * Maintains a map of exclusions from the automatic name conflict.  
     */
   private Map<String, Exclude> _automaticNameResolutionExcludes = new HashMap<String, Exclude>();

    /**
     * Default constructor.
     * @see java.lang.Object#Object()
     */
    public ExtendedBinding() {
        super();
        _componentBindings = new Hashtable<String, ComponentBindingType>();
    }

    /**
     * Returns the ComponentBinding that corresponds to the given Annotated XML
     * Schema structure An Schema location will be built for the given Annotated
     * XML schema structure.
     *
     * @param annotated the XML Schema annotated structure for which to query
     *        the Binding object for a ComponentBinding.
     *
     * @return the ComponentBinding that corresponds to the given Annotated XML
     *         Schema structure.
     */
    public ComponentBindingType getComponentBindingType(final Annotated annotated) {
        if (annotated == null) {
            return null;
        }

        //--no binding can be defined for a GROUP
        if (annotated.getStructureType() == Structure.GROUP) {
           return null;
        }

        if (!_bindingProcessed) {
            processBindingComponents();
        }

        String xPath = XPathHelper.getSchemaLocation(annotated);
        ComponentBindingType result = lookupComponentBindingType(xPath);
        if (result == null) {
            //--handle reference
            switch (annotated.getStructureType()) {

                case Structure.ELEMENT :
                    //--handle reference: if the element referred a
                    //--global element then we use the global binding
                    if (result == null) {
                        ElementDecl element = (ElementDecl) annotated;
                        if (element.isReference()) {
                            xPath = XPathHelper.getSchemaLocation(element.getReference());
                            result = lookupComponentBindingType(xPath);
                        }
                        //--discard the element
                        element = null;
                    }
                    break;

                case Structure.ATTRIBUTE :
                    if (result == null) {
                        //--handle reference: if the element referred a
                        //--global element then we use the global binding
                        AttributeDecl attribute = (AttributeDecl) annotated;
                        if (attribute.isReference()) {
                            xPath = XPathHelper.getSchemaLocation(attribute.getReference());
                            result = lookupComponentBindingType(xPath);
                        }
                        attribute = null;
                    }
                    break;

                default :
                    break;
            }
        } //--result == null

        return result;
    }

    /**
     * Returns the ComponentBindingType that corresponds to the given Schema
     * location XPath. This is a direct lookup in the hashtable, null is
     * returned if no ComponentBindingType corresponds to the given Schema
     * Location XPath.
     *
     * @param xPath the schema location xpath
     * @return The ComponentBindingType that correspond to the given Schema
     *         Location XPath, Null is returned when no ComponentBindingType is
     *         found.
     * @see org.exolab.castor.builder.binding.XPathHelper#getSchemaLocation(Structure)
     */
    private ComponentBindingType lookupComponentBindingType(final String xPath) {
        if (xPath == null) {
            return null;
        }
        ComponentBindingType componentBinding = 
            _componentBindings.get(xPath);
        
        // if no component binding has been found, retry with xpath without namespaces
        // this is to ensure backwards compatibility
        if (componentBinding == null) {
            int occurence = xPath.indexOf('{');
            String xPathNoNamespaces = xPath;
            if (occurence > 0) {
                while (occurence > 0) {
                    String xPathOld = xPathNoNamespaces;
                    xPathNoNamespaces = xPathOld.substring(0, occurence);
                    int closingOccurence = xPathOld.indexOf('}');
                    xPathNoNamespaces += xPathOld.substring(closingOccurence + 1);
                    occurence = xPathNoNamespaces.indexOf('{');
                }
                componentBinding = lookupComponentBindingType(xPathNoNamespaces);
            }
        }
        return componentBinding;
    }

    /**
     * Process the top-level Binding Component definitions and their children.
     * Processing a binding component is a 2-step process:
     * <ul>
     *    <li>Create a key for the component for direct lookup.</li>
     *    <li>Process its children</li>
     * </ul>
     */
    private void processBindingComponents() {
        ComponentBindingType temp;
        ComponentBindingType[] tempBindings = getAttributeBinding();

        //1--attributes
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            //--top-level attribute --> no location computation
            handleComponent(temp, null, ATTRIBUTE);
        }

        //2--complexTypes
        tempBindings = getComplexTypeBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, null, COMPLEXTYPE);
        }

        //3--elements
        tempBindings = getElementBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, null, ELEMENT);
        }

        //4--groups
        tempBindings = getGroupBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, null, GROUP);
        }

        //5--enums
        tempBindings = getEnumBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, null, ENUM_TYPE);
        }

        //6--simpleTypes
        tempBindings = getSimpleTypeBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, null, SIMPLETYPE);
        }

        temp = null;
        tempBindings = null;

        _bindingProcessed = true;
    }

    /**
     * Process automatic name conflict resolution section, and memorize definitions.
     * @param type {@link AutomaticNamingType} instance
     */
    void handleAutomaticNaming(final AutomaticNamingType type) {
        Forces forcesOuter = type.getForces();
        if (forcesOuter != null) {
            String[] forces = forcesOuter.getForce();
            for (int i = 0; i < forces.length; i++) {
                String elementName = forces[i];     
                _automaticNameResolutionForced.add(elementName);

            }
        }
        
        Excludes excludesOuter = type.getExcludes();
        if (excludesOuter != null) {
            Exclude[] excludes = excludesOuter.getExclude();
            for (int i = 0; i < excludes.length; i++) {
                Exclude exclude = excludes[i];
                _automaticNameResolutionExcludes.put(exclude.getName(), exclude);
            }
        }
    }

    /**
     * Processes the given ComponentBindingType given its type.
     *
     * @param binding the ComponentBindingType for which we want to process the
     *        children.
     * @param xPath the current XPath location that points to the parent of the
     *        given ComponentBindingType.
     * @param type an integer that indicates the type of the given
     *        ComponentBindingType
     */
    private void handleComponent(
            final ComponentBindingType binding, final String xPath, final int type) {
        if (binding == null) {
            return;
        }

        String currentPath = xPath;
        if (currentPath == null) {
            currentPath = new String();
        }

        String name = binding.getName();
        boolean xpathUsed = (name.indexOf("/") != -1);

        switch (type) {
            case ATTRIBUTE :
                //--handle attributes
                if (!xpathUsed) {
                    currentPath = currentPath + PATH_SEPARATOR + ATTRIBUTE_PREFIX;
                }
                currentPath += name;
                _componentBindings.put(currentPath, binding);
                break;

            case SIMPLETYPE :
                //--handle simpleType
                if (!xpathUsed) {
                    currentPath += SIMPLETYPE_ID;
                }
                currentPath += name;
                _componentBindings.put(currentPath, binding);
                break;

            case ELEMENT :
                //--handle element
                if (!xpathUsed) {
                    currentPath += PATH_SEPARATOR;
                }
                currentPath += name;
                _componentBindings.put(currentPath, binding);
                break;

            case COMPLEXTYPE :
                //--handle complexType
                if (!xpathUsed) {
                    currentPath += COMPLEXTYPE_ID;
                } else {
                    if (!name.substring(1, 12).equals("complexType")) {
                        currentPath += PATH_SEPARATOR + COMPLEXTYPE_ID;
                        currentPath += name.substring(1);
                    } else {
                        currentPath += name;
                    }
                }
                _componentBindings.put(currentPath, binding);
                break;

            case ENUM_TYPE :
                //--handle enum
                if (!xpathUsed) {
                    currentPath += ENUMTYPE_ID;
                }
                currentPath += name;
                _componentBindings.put(currentPath, binding);
                break;

            case GROUP :
                //--handle group
                if (!xpathUsed) {
                    currentPath += GROUP_ID;
                }
                currentPath += name;
                _componentBindings.put(currentPath, binding);
                break;

            default :
                //--there's a problem somewhere
                throw new IllegalStateException("Invalid ComponentBindingType: the"
                        + " type (attribute, element, complextype or group) is unknown");
        }

        //--process children
        ComponentBindingType temp;
        ComponentBindingType[] tempBindings = binding.getAttributeBinding();

        //1--attributes
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            //--top-level attribute --> no location computation
            handleComponent(temp, currentPath, ATTRIBUTE);
        }

        //2--complexTypes
        tempBindings = binding.getComplexTypeBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, currentPath, COMPLEXTYPE);
        }

        //X--simpleTypes
        tempBindings = binding.getSimpleTypeBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, currentPath, SIMPLETYPE);
        }

        //3--elements
        tempBindings = binding.getElementBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, currentPath, ELEMENT);
        }

        //4--groups
        tempBindings = binding.getGroupBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, currentPath, GROUP);
        }

        //5--enums
        tempBindings = binding.getEnumBinding();
        for (int i = 0; i < tempBindings.length; i++) {
            temp = tempBindings[i];
            handleComponent(temp, currentPath, ENUM_TYPE);
        }

        //
        temp = null;
        tempBindings = null;
    }
    
    /**
     * Indicates whether an &lt;exclude&gt; element has been specified in a binding
     * file for the given 'local name' of an element definition.
     * @param localName 'local name' of an element definition
     * @return True if an &lt;exclude&gt; element has been specified
     */
    public boolean existsExclusion(final String localName) {
        return _automaticNameResolutionExcludes.containsKey(localName);
    }

    /**
     * Returns the {@link Exclude} instance for the element identified by the given local name.
     * @param localName Local name for an element (definition).
     * @return The {@link Exclude} instance.
     */
    public Exclude getExclusion(final String localName) {
        return _automaticNameResolutionExcludes.get(localName);
    }

    /**
     * Indicates whether an &lt;force&gt; element has been specified in a binding
     * file for the given 'local name' of an element definition.
     * @param localName 'local name' of an element definition
     * @return True if an &lt;force&gt; element has been specified
     */
    public boolean existsForce(final String localName) {
        return _automaticNameResolutionForced.contains(localName);
    }
    
    /**
     * Returns all &lt;force&gt; elements defined in the binding file.
     * @return all &lt;force&gt; elements defined in the binding file
     */
    public Set<String> getForces() {
        return _automaticNameResolutionForced;
    }

}
