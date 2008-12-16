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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.binding.ExtendedBinding;
import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.binding.XPathHelper;
import org.exolab.castor.builder.binding.xml.Exclude;
import org.exolab.castor.builder.conflict.strategy.ClassNameConflictResolver;
import org.exolab.castor.builder.conflict.strategy.XPATHClassNameConflictResolver;
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
    private Set<String> _globalElements = new HashSet<String>();

    /**
     * Registry for mapping an XPATH location to a {@link JClass} instance
     * generated for the XML artefact uniquely identified by the XPATH.
     */
    private Map<String, JClass> _xpathToJClass = new HashMap<String, JClass>();

    /**
     * Registry for recording naming collisions, keyed by the local part of an
     * otherwise rooted XPATH.
     */
    private Map<String, List<String>> _localNames = new HashMap<String, List<String>>();

    /**
     * Registry for recording naming collisions, keyed by the typed local part of an
     * otherwise rooted XPATH.
     */
    private Map<String, String> _typedLocalNames = new HashMap<String, String>();

    /** 
     * JavaNaming to be used. 
     * @since 1.1.3
     */
    private JavaNaming _javaNaming;


    /**
     * Class name conflict resolver.
     */
    private ClassNameConflictResolver _classNameConflictResolver = 
        new XPATHClassNameConflictResolver();

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
     * Creates an instance of this class, providing the class anme conflict 
     * resolver to be used during automatic class name conflict resolution
     * (for local element conflicts).
     * @param resolver {@link ClassNameConflictResolver} instance to be used
     * @param javaNaming the {@link JavaNaming} to use (must not be null).
     */
    public JClassRegistry(final ClassNameConflictResolver resolver, final JavaNaming javaNaming) {
        _classNameConflictResolver = resolver;
        _javaNaming = javaNaming;
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
        
        // String xPath = XPathHelper.getSchemaLocation(annotated);
        String xPath = XPathHelper.getSchemaLocation(annotated, true);
        String localXPath = getLocalXPath(xPath); 
        String untypedXPath = xPath;
        
        //TODO: it could happen that we silently ignore a custom package as defined in a binding - FIX !
        
        // get local name
        String localName = getLocalName(xPath);
        String typedLocalName = localName;
        
        if (annotated instanceof ElementDecl) {
            ElementDecl element = (ElementDecl) annotated;
            String typexPath = XPathHelper.getSchemaLocation(element.getType());
            xPath += "[" + typexPath  + "]";
            typedLocalName += "[" + typexPath  + "]";
        } else if (annotated instanceof Group) {
            Group group = (Group) annotated;
            if (group.getOrder() == Order.choice 
                    && !_globalElements.contains("/" + localXPath)) {
                xPath += "/#choice";
            }
        }
        
        ExtendedBinding binding = component.getBinding();
        if (binding != null) {
            // deal with explicit exclusions
            if (binding.existsExclusion(typedLocalName)) {
                Exclude exclusion = binding.getExclusion(typedLocalName);
                if (exclusion.getClassName() != null) {
                    LOG.info("Dealing with exclusion for local element " + xPath 
                            + " as per binding file.");
                    jClass.changeLocalName(exclusion.getClassName());
                }
                return;
            }

            // deal with explicit forces
            if (binding.existsForce(localName)) {

                List<String> localNamesList = _localNames.get(localName);
                memorizeCollision(xPath, localName, localNamesList);

                LOG.info("Changing class name for local element " + xPath
                        + " as per binding file (force).");
                checkAndChange(jClass, annotated, untypedXPath, typedLocalName);
                return;
            }
        }
                
        
        String jClassLocalName = jClass.getLocalName();
        String expectedClassNameDerivedFromXPath = _javaNaming.toJavaClassName(localName);
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
                    Enumeration<?> possibleSubstitutes = referredElement
                            .getSubstitutionGroupMembers();
                    if (possibleSubstitutes.hasMoreElements()) {
                        XMLType referredType = referredElement.getType();
                        String xPathType = XPathHelper.getSchemaLocation(referredType);
                        JClass typeJClass = _xpathToJClass.get(xPathType);
                        if (typeJClass != null) {
                            jClass.changeLocalName(typeJClass.getLocalName());
                        } else {
                            // manually deriving class name for referenced type
                            XMLBindingComponent temp = component;
                            temp.setView(referredType);
                            jClass.changeLocalName(temp.getJavaClassName());
                            component.setView(annotated);
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
            JClass jClassAlreadyProcessed = _xpathToJClass.get(xPath);
            jClass.changeLocalName(jClassAlreadyProcessed.getLocalName());
            return;
        }

        // register JClass instance for XPATH
        _xpathToJClass.put(xPath, jClass);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Binding JClass[" + jClass.getName() + "] for XML schema structure " + xPath);
        }

        // global elements don't need to change
        final boolean isGlobalElement = _globalElements.contains(untypedXPath);
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
                Enumeration<?> possibleSubstitutes = referredElement
                        .getSubstitutionGroupMembers();
                if (possibleSubstitutes.hasMoreElements()) {
                    String typeXPath = XPathHelper
                            .getSchemaLocation(referredElement);
                    JClass referredJClass = _xpathToJClass
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
            LOG.info("Resolving conflict for local element " + xPath + " against global element.");

            checkAndChange(jClass, annotated, untypedXPath, typedLocalName);

            // remember that we had a collision for this local element
            List<String> localNamesList = _localNames.get(localName);
            memorizeCollision(xPath, localName, localNamesList);
            return;
        }

        List<String> localNamesList = _localNames.get(localName);
        memorizeCollision(xPath, localName, localNamesList);
        if (localNamesList == null) {
            String typedJClassName = _typedLocalNames.get(typedLocalName);
            if (typedJClassName == null) {
                _typedLocalNames.put(typedLocalName, jClass.getName());
            }
        } else {
            LOG.info("Resolving conflict for local element " + xPath 
                    + " against another local element of the same name.");
            checkAndChange(jClass, annotated, untypedXPath, typedLocalName);
        }
    }

    /**
     * Memorize that we have a collision for the 'local name' given. 
     * @param xPath Full (typed) XPATH identifier for the local element definition.
     * @param localName Local element name
     * @param localNamesList Collection store for collisions for that 'local name'.
     */
    private void memorizeCollision(final String xPath, final String localName, 
            final List<String> localNamesList) {
        // resolve conflict with another element
        if (localNamesList == null) {
            // this name never occured before
            List<String> arrayList = new ArrayList<String>();
            arrayList.add(xPath);
            _localNames.put(localName, arrayList);
            
        } else {
            // this entry should be renamed
            if (!localNamesList.contains(xPath)) {
                localNamesList.add(xPath);
            }
        }
    }

    /**
     * Check and change (suggested) class name.
     * @param jClass {@link JClass} instance
     * @param annotated {@link Annotated} instance
     * @param untypedXPath blah
     * @param typedLocalName blah
     */
    private void checkAndChange(final JClass jClass, final Annotated annotated, 
            final String untypedXPath, final String typedLocalName) {
        
        // check whether we have seen that typed local name already
        String typedJClassName = _typedLocalNames.get(typedLocalName);
        if (typedJClassName != null) {
            // if so, simple re-use it by changing the local class name
            String localClassName = 
                typedJClassName.substring(typedJClassName.lastIndexOf(".") + 1);
            jClass.changeLocalName(localClassName);
        } else {
            // 'calculate' a new class name
            changeClassInfoAsResultOfConflict(jClass, untypedXPath, typedLocalName, annotated);

            // store it for further use
            _typedLocalNames.put(typedLocalName, jClass.getName());
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
     * @param typedXPath
     *            Typed XPATH expression used to defer the new local class name
     * @param annotated {@link Annotated} instance
     */
    private void changeClassInfoAsResultOfConflict(final JClass jClass,
        final String xpath, final String typedXPath, final Annotated annotated) {
            _classNameConflictResolver.changeClassInfoAsResultOfConflict(jClass, xpath, 
                    typedXPath, annotated);
    }

    /**
     * Sets the {@link ClassNameConflictResolver} insatnce to be used.
     * @param conflictResolver {@link ClassNameConflictResolver} insatnce to be used. 
     */
    public void setClassNameConflictResolver(final ClassNameConflictResolver conflictResolver) {
        _classNameConflictResolver = conflictResolver;
    }

    /**
     * Utility method to gather and output statistical information about naming 
     * collisions occurred during source code generation.
     * @param binding {@link XMLBindingComponent} instance
     */
    public void printStatistics(final XMLBindingComponent binding) {
        Iterator<String> keyIterator = _localNames.keySet().iterator();
        LOG.info("*** Summary ***");
        if (binding.getBinding() != null 
                && binding.getBinding().getForces() != null 
                && binding.getBinding().getForces().size() > 0) {
            Iterator<String> forceIterator = binding.getBinding().getForces().iterator();
            LOG.info("The following 'forces' have been enabled:");
            while (forceIterator.hasNext()) {
                String forceValue = forceIterator.next();
                LOG.info(forceValue);
            }
        }
        if (keyIterator.hasNext()) {
            LOG.info("Local name conflicts encountered for the following element definitions");
            while (keyIterator.hasNext()) {
                String localName = keyIterator.next();
                List<String> collisions = _localNames.get(localName);
                if (collisions.size() > 1 && !ofTheSameType(collisions)) {
                    LOG.info(localName 
                            + ", with the following (element) definitions being involved:");
                    for (Iterator<String> iter = collisions.iterator(); iter.hasNext(); ) {
                        String xPath = iter.next();
                        LOG.info(xPath);
                    }
                }
            }
        }
        
        keyIterator = _localNames.keySet().iterator();
        if (keyIterator.hasNext()) {
            StringBuilder xmlFragment = new StringBuilder(32);
            xmlFragment.append("<forces>\n");
            while (keyIterator.hasNext()) {
                String localName = keyIterator.next();
                List<String> collisions = _localNames.get(localName);
                if (collisions.size() > 1 && !ofTheSameType(collisions)) {
                    xmlFragment.append("   <force>");
                    xmlFragment.append(localName);
                    xmlFragment.append("</force>\n");
                }
            }
            xmlFragment.append("</forces>");
            
            LOG.info(xmlFragment.toString());
        }
        
    }

    /**
     * Indicates whether all XPATH entries within the list of collisions are of the same type.
     * @param collisions The list of XPATH (collidings) for a local element name
     * @return True if all are of the same type.
     */
    private boolean ofTheSameType(final List<String> collisions) {
        boolean allSame = true;
        Iterator<String> iterator = collisions.iterator();
        String typeString = null;
        while (iterator.hasNext()) {
            String xPath = iterator.next();
            String newTypeString = xPath.substring(xPath.indexOf("[") + 1, 
                    xPath.indexOf("]"));
            if (typeString != null) {
                if (!typeString.equals(newTypeString)) {
                    allSame = false;
                    break;
                }
            } else {
                typeString = newTypeString;
            }
        }
        return allSame; 
    }

}
