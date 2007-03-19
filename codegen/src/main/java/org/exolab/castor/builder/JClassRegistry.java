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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.binding.XPathHelper;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.XMLType;
import org.exolab.javasource.JClass;

/**
 * A registry for maintaing information about {@link JClass} instances already
 * processed.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 */
public class JClassRegistry {

    /**
     * Logger instance used for all logging functionality.
     */
    private static final Log LOG = LogFactory.getLog(JClassRegistry.class);

    /**
     * Registry for holding a set of global element definitions.
     */
    private Set _globalElements = new HashSet();

    /**
     * Registry for mapping an XPATH location to a {@link JClass} instance
     * generated for the XML artefact uniquely identified by the XPATH.
     */
    private Map _xpathToJClass = new HashMap();

    /**
     * Registry for recording naming collisions, keyed by the local part of an
     * otherwise rooted XPATH.
     */
    private Map _localNames = new HashMap();

    /**
     * Registers the XPATH identifier for a global element definition for
     * further use.
     * 
     * @param xpath
     *            The XPATH identifier of a global element.
     */
    public void prebindGlobalElement(final String xpath) {
        _globalElements.add(xpath);
    }

    /**
     * Registers a {@link JClass} instance for a given XPATH.
     * 
     * @param jClass
     *            The {@link JClass} instance to register.
     * @param component 
     *            Container for the {@link Annotated} instance referred to by the XPATH.
     * @param mode Whether we register JClass instances in 'field' or 'class'mode.
     */
    public void bind(final JClass jClass, final XMLBindingComponent component,
            final String mode) {

        Annotated annotated = component.getAnnotated();
        
        String xPath = XPathHelper.getSchemaLocation(annotated);
        String localXPath = getLocalXPath(xPath); 
        String untypedXPath = xPath;
        
        // TODO: it could happen that we silently ignore a custom package as defined in a binding - FIX !
        
        // get local name
        String localName = getLocalName(xPath);
        
        if (annotated instanceof ElementDecl) {
            ElementDecl element = (ElementDecl) annotated;
            String typexPath = XPathHelper.getSchemaLocation(element.getType());
            xPath += "[" + typexPath  + "]";
        } else if (annotated instanceof Group) {
            Group group = (Group) annotated;
            if (group.getOrder().getType() == Order.CHOICE 
                    && !_globalElements.contains("/" + localXPath)) {
                xPath += "/#choice";
            }
        }
        
        String jClassLocalName = jClass.getLocalName();
        String expectedClassNameDerivedFromXPath = JavaNaming.toJavaClassName(localName);
        if (!jClassLocalName.equals(expectedClassNameDerivedFromXPath)) {
            if (component.createGroupItem()) {
                xPath += "/#item";
            }
            _xpathToJClass.put(xPath, jClass);
            return;
        }

        if (mode.equals("field")) {
            if (annotated instanceof ModelGroup) {
                ModelGroup group = (ModelGroup) annotated;
                final boolean isReference = group.isReference();
                if (isReference) {
                    return;
                }
            }
                
            if (annotated instanceof ElementDecl) {
                ElementDecl element = (ElementDecl) annotated;

                final boolean isReference = element.isReference();
                if (isReference) {
                    ElementDecl referredElement = element.getReference();
                    // if that global element definition is a substitution head,
                    // we now
                    // need to do work out the global element's type, and use
                    // its
                    // JClass instance to defer the type of the member currently
                    // processed
                    Enumeration possibleSubstitutes = referredElement
                            .getSubstitutionGroupMembers();
                    if (possibleSubstitutes.hasMoreElements()) {
                        XMLType referredType = referredElement.getType();
                        String xPathType = XPathHelper.getSchemaLocation(referredType);
                        JClass typeJClass = (JClass) _xpathToJClass
                            .get(xPathType);
                        if (typeJClass != null) {
                            jClass.changeLocalName(typeJClass.getLocalName());
                        } else {
                            // manually deriving class name for referenced type
                            XMLBindingComponent temp = component;
                            temp.setView(referredType);
                            jClass.changeLocalName(temp.getJavaClassName());
                        }
//                        String typeXPath = XPathHelper
//                                .getSchemaLocation(referredElement);
//                        JClass referredJClass = (JClass) _xpathToJClass
//                                .get(typeXPath + "_class");
//                        jClass.changeLocalName(referredJClass.getSuperClass()
//                                .getLocalName());
                    }
                    return;
                }
            }
        }

        final boolean alreadyProcessed = _xpathToJClass.containsKey(xPath);

        // if already processed, change the JClass instance accordingly
        if (alreadyProcessed) {
            JClass jClassAlreadyProcessed = (JClass) _xpathToJClass.get(xPath);
            jClass.changeLocalName(jClassAlreadyProcessed.getLocalName());
            return;
        }

        // register JClass instance for XPATH
        _xpathToJClass.put(xPath, jClass);

        LOG.debug("Binding JClass[" + jClass.getName() + "] for XML schema structure " + xPath);

        // global elements don't need to change
        final boolean isGlobalElement = _globalElements.contains(xPath);
        if (isGlobalElement) {
            return;
        }

        // resolve references to global elements
        if (mode.equals("field") && annotated instanceof ElementDecl) {
            ElementDecl element = (ElementDecl) annotated;
            final boolean isReference = element.isReference();
            if (isReference) {
                ElementDecl referredElement = element.getReference();
                // if that global element definition is a substitution head, we
                // now
                // need to do work out the global element's type, and use its
                // JClass instance to defer the type of the member currently
                // processed
                Enumeration possibleSubstitutes = referredElement
                        .getSubstitutionGroupMembers();
                if (possibleSubstitutes.hasMoreElements()) {
                    String typeXPath = XPathHelper
                            .getSchemaLocation(referredElement);
                    JClass referredJClass = (JClass) _xpathToJClass
                            .get(typeXPath + "_class");
                    jClass.changeLocalName(referredJClass.getSuperClass()
                            .getLocalName());
                }
                return;
            }
        }

        // resolve conflict with a global element
        final boolean conflictExistsWithGlobalElement = _globalElements
                .contains("/" + localXPath);
        if (conflictExistsWithGlobalElement) {
            LOG.info("resolving global element conflict for " + xPath);
            changeClassInfoAsResultOfConflict(jClass, untypedXPath);
            return;
        }

        // resolve conflict with another element
        List localNamesList = (List) _localNames.get(localName);
        if (localNamesList == null) {
            // this name never occured before
            ArrayList arrayList = new ArrayList();
            arrayList.add(xPath);
            _localNames.put(localName, arrayList);
        } else {
            // this entry should be renamed
            changeClassInfoAsResultOfConflict(jClass, untypedXPath);
        }
    }

    /**
     * Returns the local name of rooted XPATH expression.
     * 
     * @param xPath
     *            An (rooted) XPATH expression
     * @return the local name
     */
    private String getLocalName(final String xPath) {
        String localName = xPath.substring(xPath.lastIndexOf("/") + 1);
        if (localName.startsWith(ExtendedBinding.COMPLEXTYPE_ID)
                || localName.startsWith(ExtendedBinding.SIMPLETYPE_ID)
                || localName.startsWith(ExtendedBinding.ENUMTYPE_ID)
                || localName.startsWith(ExtendedBinding.GROUP_ID)) {
            localName = localName.substring(localName.indexOf(":") + 1);
        }
        return localName;
    }

    /**
     * Returns the local part of rooted XPATH expression.
     * 
     * @param xPath
     *            An (rooted) XPATH expression
     * @return the local part 
     */
    private String getLocalXPath(final String xPath) {
        return xPath.substring(xPath.lastIndexOf("/") + 1);
    }

    /**
     * Changes the JClass' internal class name, as a result of an XPATH
     * expression uniquely identifying an XML artefact within an XML schema.
     * 
     * @param jClass
     *            The {@link JClass} instance whose local name should be
     *            changed.
     * @param xpath
     *            XPATH expression used to defer the new local class name
     */
    private void changeClassInfoAsResultOfConflict(final JClass jClass,
            final String xpath) {
        StringTokenizer stringTokenizer = new StringTokenizer(xpath, "/.");
        String prefix = "";
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

        // set new classname
        String newClassName = prefix + jClass.getLocalName();
        jClass.changeLocalName(newClassName);
    }

}
