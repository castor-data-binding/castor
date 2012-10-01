/*
 * Copyright 2010 Werner Guttmann
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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.xml.XMLProperties;
import org.exolab.castor.mapping.ExtendedFieldHandler;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.xml.MarshalFramework.InheritanceMatch;
import org.exolab.castor.xml.MarshalFramework.InternalXMLClassDescriptor;
import org.exolab.castor.xml.UnmarshalHandler.Arguments;
import org.exolab.castor.xml.UnmarshalHandler.ArrayHandler;
import org.exolab.castor.xml.util.ContainerElement;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A processor that assists {@link UnmarshalHandler} in dealing with the SAX 2
 * {@link ContentHandler#startElement(String, String, String, org.xml.sax.Attributes)}
 * callback method.
 * 
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public class StartElementProcessor {

    /**
     * Standard logger to use.
     */
    private static final Log LOG = LogFactory
            .getLog(StartElementProcessor.class);

    /**
     * resource bundle
     */
    protected static ResourceBundle resourceBundle;

    /**
     * Callback {@link UnmarshalHandler} reference to set the actual state on
     * this instance.
     */
    private final UnmarshalHandler _unmarshalHandler;

    static {
        resourceBundle = ResourceBundle.getBundle("UnmarshalHandlerMessages",
                Locale.getDefault());
    }

    /**
     * Creates an instance of this class, with a reference to the actual
     * {@link UnmarshalHandler} for which this processor deals with the SAX 2
     * startElement() callback method.
     * 
     * @param unmarshalHandler
     *            The {@link UnmarshalHandler} instance on which the results of
     *            processing the startElement method will be 'persisted'/set.
     */
    public StartElementProcessor(final UnmarshalHandler unmarshalHandler) {
        super();
        _unmarshalHandler = unmarshalHandler;
    }

    public void compute(String name, String namespace, AttributeSet atts)
            throws SAXException {

        UnmarshalState state = null;
        String xmlSpace = null;

        // -- handle special atts
        if (atts != null) {
            // -- xml:space
            xmlSpace = atts.getValue(UnmarshalHandler.XML_SPACE,
                    Namespaces.XML_NAMESPACE);
            if (xmlSpace == null) {
                xmlSpace = atts.getValue(
                        UnmarshalHandler.XML_SPACE_WITH_PREFIX, "");
            }
        }

        if (_unmarshalHandler.getStateStack().isEmpty()) {
            // -- Initialize since this is the first element
            _unmarshalHandler.processFirstElement(name, namespace, atts,
                    xmlSpace);
            return;
        } // --rootElement

        // -- get MarshalDescriptor for the given element
        UnmarshalState parentState = _unmarshalHandler.getStateStack()
                .getLastState();

        // Test if we can accept the field in the parentState
        // in case the parentState fieldDesc is a container
        // -- This following logic tests to see if we are in a
        // -- container and we need to close out the container
        // -- before proceeding:
        boolean canAccept = false;
        while ((parentState.getFieldDescriptor() != null)
                && (parentState.getFieldDescriptor().isContainer() && !canAccept)) {
            XMLClassDescriptor tempClassDesc = parentState.getClassDescriptor();

            // -- Find ClassDescriptor for Parent
            if (tempClassDesc == null) {
                tempClassDesc = (XMLClassDescriptor) parentState.getFieldDescriptor()
                        .getClassDescriptor();
                if (tempClassDesc == null)
                    tempClassDesc = _unmarshalHandler
                            .getClassDescriptor(parentState.getObject().getClass());
            }

            canAccept = tempClassDesc.canAccept(name, namespace,
                    parentState.getObject());

            if (!canAccept) {
                // -- Does container class even handle this field?
                if (tempClassDesc.getFieldDescriptor(name, namespace,
                        NodeType.Element) != null) {
                    if (!parentState.getFieldDescriptor().isMultivalued()) {
                        String error = MessageFormat
                                .format(resourceBundle
                                        .getString("unmarshalHandler.error.container.full"),
                                        new Object[] {
                                                tempClassDesc.getJavaClass()
                                                        .getName(), name });
                        ValidationException vx = new ValidationException(error);
                        throw new SAXException(vx);
                    }
                }
                _unmarshalHandler.endElement(parentState.getElementName());
                parentState = _unmarshalHandler.getStateStack().getLastState();
            }
            tempClassDesc = null;
        }

        // -- create new state object
        state = new UnmarshalState();
        state.setElementName(name);
        state.setParent(parentState);

        if (xmlSpace != null) {
            state.setWhitespacePreserving(UnmarshalHandler.PRESERVE.equals(xmlSpace));
        } else {
            state.setWhitespacePreserving(parentState.isWhitespacePreserving());
        }

        _unmarshalHandler.getStateStack().pushState(state);

        // -- make sure we should proceed
        if (parentState.getObject() == null) {
            if (!parentState.isWrapper()) {
                return;
            }
        }

        Class cls = null;

        // -- Find ClassDescriptor for Parent
        XMLClassDescriptor classDesc = parentState.getClassDescriptor();
        if (classDesc == null) {
            classDesc = (XMLClassDescriptor) parentState.getFieldDescriptor().getClassDescriptor();
            if (classDesc == null)
                classDesc = _unmarshalHandler.getClassDescriptor(parentState.getObject().getClass());
        } else {
            // classDesc.resetElementCount();
        }

        // ----------------------------------------------------/
        // - Find FieldDescriptor associated with the element -/
        // ----------------------------------------------------/

        // -- A reference to the FieldDescriptor associated
        // -- the the "current" element
        XMLFieldDescriptor descriptor = null;

        // -- inherited class descriptor
        // -- (only needed if descriptor cannot be found directly)
        XMLClassDescriptor cdInherited = null;

        // -- loop through stack and find correct descriptor
        // int pIdx = _stateInfo.size() - 2; //-- index of parentState
        UnmarshalState targetState = parentState;
        String path = "";
        StringBuffer pathBuf = null;
        int count = 0;
        boolean isWrapper = false;
        XMLClassDescriptor oldClassDesc = classDesc;
        while (descriptor == null) {

            // -- NOTE (kv 20050228):
            // -- we need to clean this code up, I made this
            // -- fix to make sure the correct descriptor which
            // -- matches the location path is used
            if (path.length() > 0) {
                String tmpName = path + "/" + name;
                descriptor = classDesc.getFieldDescriptor(tmpName, namespace,
                        NodeType.Element);
            }
            // -- End Patch

            if (descriptor == null) {
                descriptor = classDesc.getFieldDescriptor(name, namespace,
                        NodeType.Element);
            }

            // -- Namespace patch, should be moved to XMLClassDescriptor, but
            // -- this is the least intrusive patch at the moment. kv - 20030423
            if ((descriptor != null) && (!descriptor.isContainer())) {
                if (StringUtils.isNotEmpty(namespace)) {
                    if (!MarshalFramework.namespaceEquals(namespace,
                            descriptor.getNameSpaceURI())) {
                        // -- if descriptor namespace is not null, then we must
                        // -- have a namespace match, so set descriptor to null,
                        // -- or if descriptor is not a wildcard we can also
                        // -- set to null.
                        if ((descriptor.getNameSpaceURI() != null)
                                || (!descriptor.matches("*"))) {
                            descriptor = null;
                        }

                    }
                }
            }
            // -- end namespace patch

            /*
             * If descriptor is null, we need to handle possible inheritence,
             * which might not be described in the current ClassDescriptor. This
             * can be a slow process...for speed use the match attribute of the
             * xml element in the mapping file. This logic might not be
             * completely necessary, and perhaps we should remove it.
             */
            // handle multiple level locations (where count > 0) (CASTOR-1039)
            // if ((descriptor == null) && (count == 0) &&
            // (!targetState.wrapper)) {
            if ((descriptor == null) && (!targetState.isWrapper())) {
                MarshalFramework.InheritanceMatch[] matches = null;
                try {
                    matches = _unmarshalHandler.searchInheritance(name,
                            namespace, classDesc); // TODO: Joachim,
                                                   // _cdResolver);
                } catch (MarshalException rx) {
                    // -- TODO:
                }
                if (matches.length != 0) {
                    InheritanceMatch match = null;
                    // It may be the case that this class descriptor can
                    // appear under multiple parent field descriptors. Look
                    // for the first match whose parent file descriptor XML
                    // name matches the name of the element we are under
                    for (int i = 0; i < matches.length; i++) {
                        if (parentState.getElementName()
                                .equals(matches[i].parentFieldDesc
                                        .getLocationPath())) {
                            match = matches[i];
                            break;
                        }
                    }
                    if (match == null)
                        match = matches[0];
                    descriptor = match.parentFieldDesc;
                    cdInherited = match.inheritedClassDesc;
                    break; // -- found
                }
                /* */

                // handle multiple level locations (where count > 0)
                // (CASTOR-1039)
                // isWrapper = (isWrapper || hasFieldsAtLocation(name,
                // classDesc));
                StringBuffer tmpLocation = new StringBuffer();
                if (count > 0) {
                    tmpLocation.append(path + "/" + name);
                } else {
                    tmpLocation.append(name);
                }
                isWrapper = (isWrapper || MarshalFramework.hasFieldsAtLocation(
                        tmpLocation.toString(), classDesc));
            } else if (descriptor != null) {
                String tmpPath = descriptor.getLocationPath();
                if (path.equals(StringUtils.defaultString(tmpPath)))
                    break; // -- found
                descriptor = null; // -- not found, try again
            } else {
                if (pathBuf == null)
                    pathBuf = new StringBuffer();
                else
                    pathBuf.setLength(0);
                pathBuf.append(path);
                pathBuf.append('/');
                pathBuf.append(name);
                isWrapper = (isWrapper || MarshalFramework.hasFieldsAtLocation(
                        pathBuf.toString(), classDesc));
            }

            // -- Make sure there are more parent classes on stack
            // -- otherwise break, since there is nothing to do
            // if (pIdx == 0) break;
            if (targetState == _unmarshalHandler.getTopState())
                break;

            // -- adjust name and try parent
            if (count == 0)
                path = targetState.getElementName();
            else {
                if (pathBuf == null)
                    pathBuf = new StringBuffer();
                else
                    pathBuf.setLength(0);
                pathBuf.append(targetState.getElementName());
                pathBuf.append('/');
                pathBuf.append(path);
                path = pathBuf.toString();
            }

            // -- get
            // --pIdx;
            // targetState = (UnmarshalState)_stateInfo.elementAt(pIdx);
            targetState = targetState.getParent();
            classDesc = targetState.getClassDescriptor();
            count++;
        }

        if (descriptor != null
                && _unmarshalHandler.isValidating()
                && !_unmarshalHandler.getInternalContext()
                        .getLenientSequenceOrder()) {
            try {
                classDesc.checkDescriptorForCorrectOrderWithinSequence(
                        descriptor, parentState, name);
            } catch (ValidationException e) {
                throw new SAXException(e);
            }
        }

        // -- The field descriptor is still null, we face a problem
        if (descriptor == null) {

            // -- reset classDesc
            classDesc = oldClassDesc;

            // -- isWrapper?
            if (isWrapper) {
                state.setClassDescriptor(new XMLClassDescriptorImpl(
                        ContainerElement.class, name));
                state.setWrapper(true);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("wrapper-element: " + name);
                }
                // -- process attributes
                _unmarshalHandler.processWrapperAttributes(atts);
                return;
            }

            String error = MessageFormat.format(resourceBundle
                    .getString("unmarshalHandler.error.find.field.descriptor"),
                    new Object[] { name, classDesc.getXMLName() });

            // -- unwrap classDesc, if necessary, for the check
            // -- Introspector.introspected done below
            if (classDesc instanceof InternalXMLClassDescriptor) {
                classDesc = ((InternalXMLClassDescriptor) classDesc)
                        .getClassDescriptor();
            }

            // -- If we are skipping elements that have appeared in the XML but
            // for
            // -- which we have no mapping, increase the ignore depth counter
            // and return
            boolean lenientElementStrictnessForIntrospection = _unmarshalHandler
                    .getInternalContext()
                    .getBooleanProperty(
                            XMLProperties.LENIENT_INTROSPECTED_ELEMENT_STRICTNESS)
                    .booleanValue();
            // checks if the element could be skipped
            if (_unmarshalHandler.getStrictElementHandler().skipStartElementIgnoringDepth()) {
                // -- remove the StateInfo we just added
                _unmarshalHandler.getStateStack().removeLastState();
                // drop Namespace instance as well
                _unmarshalHandler.getNamespaceHandling()
                        .removeCurrentNamespaceInstance();
                if (LOG.isDebugEnabled()) {
                    String debug = MessageFormat.format(resourceBundle
                            .getString("unmarshalHandler.log.debug.ignore.extra.element"),
                            new Object[] { error });
                    LOG.debug(debug);
                }
                return;
            }
            // if we have no field descriptor and
            // the class descriptor was introspected
            // just log it
            else if (lenientElementStrictnessForIntrospection
                    && Introspector.introspected(classDesc)) {
                LOG.warn(error);
                return;
            }
            // -- otherwise report error since we cannot find a suitable
            // -- descriptor
            else {
                throw new SAXException(error);
            }
        } // -- end null descriptor

        // / DEBUG: System.out.println("path: " + path);

        // -- Save targetState (used in endElement)
        if (targetState != parentState) {
            state.setTargetState(targetState);
            parentState = targetState; // -- reassign
        }

        Object object = parentState.getObject();
        // --container support
        if (descriptor.isContainer()) {
            // create a new state to set the container as the object
            // don't save the current state, it will be recreated later

            if (LOG.isDebugEnabled()) {
                LOG.debug("#container: " + descriptor.getFieldName());
            }

            // -- clear current state and re-use for the container
            state.clear();
            // -- inherit whitespace preserving from the parentState
            state.setWhitespacePreserving(parentState.isWhitespacePreserving());
            state.setParent(parentState);

            // here we can hard-code a name or take the field name
            state.setElementName(descriptor.getFieldName());
            state.setFieldDescriptor(descriptor);
            state.setClassDescriptor((XMLClassDescriptor) descriptor
                    .getClassDescriptor());
            Object containerObject = null;

            // 1-- the container is not multivalued (not a collection)
            if (!descriptor.isMultivalued()) {
                // Check if the container object has already been instantiated
                FieldHandler handler = descriptor.getHandler();
                containerObject = handler.getValue(object);
                if (containerObject != null) {
                    if (state.getClassDescriptor() != null) {
                        if (state.getClassDescriptor().canAccept(name, namespace,
                                containerObject)) {
                            // remove the descriptor from the used list
                            parentState.markAsNotUsed(descriptor);
                        }
                    } else {
                        // remove the descriptor from the used list
                        parentState.markAsNotUsed(descriptor);
                    }
                } else {
                    containerObject = handler.newInstance(object);
                }

            }
            // 2-- the container is multivalued
            else {
                Class containerClass = descriptor.getFieldType();
                try {
                    containerObject = containerClass.newInstance();
                } catch (Exception ex) {
                    throw new SAXException(ex);
                }
            }
            state.setObject(containerObject);
            state.setType(containerObject.getClass());

            // we need to recall startElement()
            // so that we can find a more appropriate descriptor in for the
            // given name
            _unmarshalHandler.getNamespaceHandling().createNamespace();
            _unmarshalHandler.startElementProcessing(name, namespace, atts);
            return;
        }
        // --End of the container support

        // -- Find object type and create new Object of that type
        state.setFieldDescriptor(descriptor);

        /*
         * <update> we need to add this code back in, to make sure we have
         * proper access rights.
         * 
         * if (!descriptor.getAccessRights().isWritable()) { if (debug) {
         * buf.setLength(0); buf.append("The field for element '");
         * buf.append(name); buf.append("' is read-only.");
         * message(buf.toString()); } return; }
         */

        // -- Find class to instantiate
        // -- check xml names to see if we should look for a more specific
        // -- ClassDescriptor, otherwise just use the one found in the
        // -- descriptor
        classDesc = null;
        if (cdInherited != null)
            classDesc = cdInherited;
        else if (!name.equals(descriptor.getXMLName()))
            classDesc = _unmarshalHandler.resolveByXMLName(name, namespace,
                    null);

        if (classDesc == null)
            classDesc = (XMLClassDescriptor) descriptor.getClassDescriptor();
        FieldHandler handler = descriptor.getHandler();
        boolean useHandler = true;

        try {

            // -- Get Class type...first use ClassDescriptor,
            // -- since it could be more specific than
            // -- the FieldDescriptor
            if (classDesc != null) {
                cls = classDesc.getJavaClass();

                // -- XXXX This is a hack I know...but we
                // -- XXXX can't use the handler if the field
                // -- XXXX types are different
                if (descriptor.getFieldType() != cls) {
                    state.setDerived(true);
                }
            } else {
                cls = descriptor.getFieldType();
            }

            // -- This *shouldn't* happen, but a custom implementation
            // -- could return null in the XMLClassDesctiptor#getJavaClass
            // -- or XMLFieldDescriptor#getFieldType. If so, just replace
            // -- with java.lang.Object.class (basically "anyType").
            if (cls == null) {
                cls = java.lang.Object.class;
            }

            // Retrieving the xsi:type attribute, if present
            String currentPackage = _unmarshalHandler
                    .getJavaPackage(parentState.getType());
            String instanceType = _unmarshalHandler.getInstanceType(atts,
                    currentPackage);
            if (instanceType != null) {

                Class instanceClass = null;
                try {

                    XMLClassDescriptor instanceDesc = _unmarshalHandler
                            .getClassDescriptor(instanceType,
                                    _unmarshalHandler.getClassLoader());

                    boolean loadClass = true;

                    if (instanceDesc != null) {
                        instanceClass = instanceDesc.getJavaClass();
                        classDesc = instanceDesc;
                        if (instanceClass != null) {
                            loadClass = (!instanceClass.getName().equals(
                                    instanceType));
                        }
                    }

                    if (loadClass) {
                        instanceClass = _unmarshalHandler.loadClass(
                                instanceType, null);
                        // the FieldHandler can be either an XMLFieldHandler
                        // or a FieldHandlerImpl
                        FieldHandler tempHandler = descriptor.getHandler();

                        boolean collection = false;
                        if (tempHandler instanceof FieldHandlerImpl) {
                            collection = ((FieldHandlerImpl) tempHandler)
                                    .isCollection();
                        } else {
                            collection = Introspector
                                    .isCollection(instanceClass);
                        }

                        if ((!collection)
                                && !cls.isAssignableFrom(instanceClass)) {
                            if (!MarshalFramework.isPrimitive(cls)) {
                                String err = MessageFormat
                                        .format(resourceBundle
                                                .getString("unmarshalHandler.error.not.subclass"),
                                                new Object[] {
                                                        instanceClass.getName(),
                                                        cls.getName() });
                                throw new SAXException(err);
                            }
                        }
                    }
                    cls = instanceClass;
                    useHandler = false;
                } catch (Exception ex) {
                    String err = MessageFormat
                            .format(resourceBundle
                                    .getString("unmarshalHandler.error.unable.instantiate.exception"),
                                    new Object[] { instanceType,
                                            ex.getMessage() });
                    throw new SAXException(err, ex);
                }

            }

            // -- Handle ArrayHandler
            if (cls == Object.class) {
                if (parentState.getObject() instanceof ArrayHandler)
                    cls = ((ArrayHandler) parentState.getObject()).componentType();
            }

            // -- Handle support for "Any" type

            if (cls == Object.class) {
                Class pClass = parentState.getType();
                ClassLoader loader = pClass.getClassLoader();
                // -- first look for a descriptor based
                // -- on the XML name
                classDesc = _unmarshalHandler.resolveByXMLName(name, namespace,
                        loader);
                // -- if null, create classname, and try resolving
                String cname = null;
                if (classDesc == null) {
                    // -- create class name
                    cname = _unmarshalHandler.getJavaNaming().toJavaClassName(
                            name);
                    classDesc = _unmarshalHandler.getClassDescriptor(cname,
                            loader);
                }
                // -- if still null, try using parents package
                if (classDesc == null) {
                    // -- use parent to get package information
                    String pkg = pClass.getName();
                    int idx = pkg.lastIndexOf('.');
                    if (idx > 0) {
                        pkg = pkg.substring(0, idx + 1);
                        cname = pkg + cname;
                        classDesc = _unmarshalHandler.getClassDescriptor(cname,
                                loader);
                    }
                }

                if (classDesc != null) {
                    cls = classDesc.getJavaClass();
                    useHandler = false;
                } else {
                    // we are dealing with an AnyNode
                    state.setObject(_unmarshalHandler.getAnyNodeHandler()
                            .commonStartElement(name, namespace,
                                    state.isWhitespacePreserving()));
                    state.setType(cls);
                    return;
                }
            }

            boolean byteArray = false;
            if (cls.isArray())
                byteArray = (cls.getComponentType() == Byte.TYPE);

            // -- check for immutable
            if (MarshalFramework.isPrimitive(cls) || descriptor.isImmutable()
                    || byteArray) {
                state.setObject(null);
                state.setPrimitiveOrImmutable(true);
                // -- handle immutable types, such as java.util.Locale
                if (descriptor.isImmutable()) {
                    if (classDesc == null)
                        classDesc = _unmarshalHandler.getClassDescriptor(cls);
                    state.setClassDescriptor(classDesc);
                    Arguments args = _unmarshalHandler.processConstructorArgs(
                            atts, classDesc);
                    if ((args != null) && (args.size() > 0)) {
                        state.setConstructorArguments(args);
                    }
                }
            } else {
                if (classDesc == null)
                    classDesc = _unmarshalHandler.getClassDescriptor(cls);

                // -- XXXX should remove this test once we can
                // -- XXXX come up with a better solution
                if ((!state.isDerived()) && useHandler) {

                    boolean create = true;
                    if (_unmarshalHandler.isReuseObjects()) {
                        state.setObject(handler.getValue(parentState.getObject()));
                        create = (state.getObject() == null);
                    }
                    if (create) {
                        Arguments args = _unmarshalHandler
                                .processConstructorArgs(atts, classDesc);
                        if ((args.getValues() != null) && (args.getValues().length > 0)) {
                            if (handler instanceof ExtendedFieldHandler) {
                                ExtendedFieldHandler efh = (ExtendedFieldHandler) handler;
                                state.setObject(efh.newInstance(
                                        parentState.getObject(), args.getValues()));
                            } else {
                                String err = resourceBundle
                                        .getString("unmarshalHandler.error.constructor.arguments");
                                throw new SAXException(err);
                            }
                        } else {
                            state.setObject(handler
                                    .newInstance(parentState.getObject()));
                        }
                    }
                }
                // -- reassign class in case there is a conflict
                // -- between descriptor#getFieldType and
                // -- handler#newInstance...I should hope not, but
                // -- who knows
                if (state.getObject() != null) {
                    cls = state.getObject().getClass();
                    if (classDesc != null) {
                        if (classDesc.getJavaClass() != cls) {
                            classDesc = null;
                        }
                    }
                } else {
                    try {
                        if (cls.isArray()) {
                            state.setObject(new ArrayHandler(
                                    cls.getComponentType()));
                            cls = ArrayHandler.class;
                        } else {
                            Arguments args = _unmarshalHandler
                                    .processConstructorArgs(atts, classDesc);
                            state.setObject(_unmarshalHandler.createInstance(
                                    cls, args));
                            // state.object = _class.newInstance();
                        }
                    } catch (java.lang.Exception ex) {
                        String err = MessageFormat
                                .format(resourceBundle
                                        .getString("unmarshalHandler.error.unable.instantiate.exception"),
                                        new Object[] {
                                                _unmarshalHandler
                                                        .className(cls),
                                                ex.getMessage() });
                        throw new SAXException(err, ex);
                    }
                }
            }
            state.setType(cls);
        } catch (java.lang.IllegalStateException ise) {
            LOG.error(ise.toString());
            throw new SAXException(ise);
        }

        // -- At this point we should have a new object, unless
        // -- we are dealing with a primitive type, or a special
        // -- case such as byte[]
        if (classDesc == null) {
            classDesc = _unmarshalHandler.getClassDescriptor(cls);
        }
        state.setClassDescriptor(classDesc);

        if ((state.getObject() == null) && (!state.isPrimitiveOrImmutable())) {
            String err = MessageFormat.format(resourceBundle
                    .getString("unmarshalHandler.error.unable.unmarshal"),
                    new Object[] { name, _unmarshalHandler.className(cls) });
            throw new SAXException(err);
        }

        // -- assign object, if incremental

        if (descriptor.isIncremental()) {
            if (LOG.isDebugEnabled()) {
                String debug = MessageFormat.format(resourceBundle
                        .getString("unmarshalHandler.log.debug.process.incrementally"),
                        new Object[] { name });
                LOG.debug(debug);
            }
            try {
                handler.setValue(parentState.getObject(), state.getObject());
            } catch (java.lang.IllegalStateException ise) {
                String err = MessageFormat
                        .format(resourceBundle
                                .getString("unmarshalHandler.error.unable.add.element"),
                                new Object[] { name,
                                        parentState.getFieldDescriptor().getXMLName(),
                                        ise.getMessage() });
                throw new SAXException(err, ise);
            }
        }

        if (state.getObject() != null) {
            // --The object has just been initialized
            // --notify the listener
            Object stateObject = state.getObject();
            Object parentObject = (state.getParent() == null) ? null
                    : state.getParent().getObject();
            _unmarshalHandler.getDelegateUnmarshalListener().initialized(
                    stateObject, parentObject);
            _unmarshalHandler.processAttributes(atts, classDesc);
            _unmarshalHandler.getDelegateUnmarshalListener().attributesProcessed(
                    stateObject, parentObject);
            _unmarshalHandler.getNamespaceHandling().processNamespaces(classDesc,
                    _unmarshalHandler.getStateStack().getLastState().getObject());
        } else if ((state.getType() != null) && (!state.isPrimitiveOrImmutable())) {
            if (atts != null) {
                _unmarshalHandler.processWrapperAttributes(atts);
                String warn = MessageFormat.format(resourceBundle
                        .getString("unmarshalHandler.log.warn.process.attribute.as.location"),
                        new Object[] { name });
                LOG.warn(warn);
            }
        } else {
            // -- check for special attributes, such as xsi:nil
            if (atts != null) {
                String nil = atts.getValue(MarshalFramework.NIL_ATTR,
                        MarshalFramework.XSI_NAMESPACE);
                state.setNil("true".equals(nil));
                _unmarshalHandler.processWrapperAttributes(atts);
            }
        }
    }

}
