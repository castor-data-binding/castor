/*
 * Copyright 2010 Philipp Erlacher
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.xml.UnmarshalHandler.ArrayHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * A processor that assists {@link UnmarshalHandler} in dealing with the SAX 2
 * {@link ContentHandler#endElement(String, String, String)} callback method.
 * 
 * @author <a href=" mailto:philipp.erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * @since 1.3.2
 */
public class EndElementProcessor {

    /**
     * Standard logger to use.
     */
    private static final Log LOG = LogFactory.getLog(EndElementProcessor.class);

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
     * endElement() callback method.
     * 
     * @param unmarshalHandler
     *            The {@link UnmarshalHandler} instance on which the results of
     *            processing the endElement method will be 'persisted'/set.
     */
    public EndElementProcessor(final UnmarshalHandler unmarshalHandler) {
        _unmarshalHandler = unmarshalHandler;
    }

    public void compute(String name) throws org.xml.sax.SAXException {
        if (LOG.isTraceEnabled()) {
            String trace = MessageFormat.format(resourceBundle
                    .getString("unmarshalHandler.log.trace.endElement"),
                    new Object[] { name });
            LOG.trace(trace);
        }

        // -- If we are skipping elements that have appeared in the XML but for
        // -- which we have no mapping, decrease the ignore depth counter and
        // return
        if (_unmarshalHandler.getStrictElementHandler().skipEndElement()) {
            return;
        }

        // -- Do delagation if necessary
        if (_unmarshalHandler.getAnyNodeHandler().hasAnyUnmarshaller()) {
            _unmarshalHandler.getAnyNodeHandler().endElement(name);
            // we are back to the starting node
            if (_unmarshalHandler.getAnyNodeHandler().isStartingNode()) {
                _unmarshalHandler.setAnyNode(_unmarshalHandler
                        .getAnyNodeHandler().getStartingNode());
            } else
                return;
        }

        if (_unmarshalHandler.getStateStack().isEmpty()) {
            String err = MessageFormat.format(resourceBundle
                    .getString("unmarshalHandler.error.missing.startElement"),
                    new Object[] { name });
            throw new SAXException(err);
        }

        // -- * Begin Namespace Handling
        // -- XXX Note: This code will change when we update the XML event API

        int idx = name.indexOf(':');
        if (idx >= 0) {
            name = name.substring(idx + 1);
        }
        // -- * End Namespace Handling

        UnmarshalState state = _unmarshalHandler.getStateStack()
                .removeLastState();

        // -- make sure we have the correct closing tag
        XMLFieldDescriptor descriptor = state._fieldDesc;

        if (!state._elementName.equals(name)) {

            // maybe there is still a container to end
            if (descriptor.isContainer()) {
                _unmarshalHandler.getStateStack().pushState(state);
                // -- check for possible characters added to
                // -- the container's state that should
                // -- really belong to the parent state
                StringBuffer tmpBuffer = null;
                if (state._buffer != null) {
                    if (!UnmarshalHandler.isWhitespace(state._buffer)) {
                        if (state._classDesc.getContentDescriptor() == null) {
                            tmpBuffer = state._buffer;
                            state._buffer = null;
                        }
                    }
                }
                // -- end container
                _unmarshalHandler.endElement(state._elementName);

                if (tmpBuffer != null) {
                    state = _unmarshalHandler.getStateStack().getLastState();
                    if (state._buffer == null)
                        state._buffer = tmpBuffer;
                    else
                        state._buffer.append(tmpBuffer.toString());
                }
                _unmarshalHandler.endElement(name);
                return;
            }
            String err = MessageFormat
                    .format(resourceBundle
                            .getString("unmarshalHandler.error.different.endElement.expected"),
                            new Object[] { state._elementName, name });
            throw new SAXException(err);
        }

        // -- clean up current Object
        Class<?> type = state._type;

        if (type == null) {
            if (!state._wrapper) {
                // -- this message will only show up if debug
                // -- is turned on...how should we handle this case?
                // -- should it be a fatal error?
                String info = MessageFormat
                        .format(resourceBundle
                                .getString("unmarshalHandler.log.info.no.Descriptor.found"),
                                new Object[] { state._elementName });
                LOG.info(info);
            }

            // -- handle possible location text content
            // -- TODO: cleanup location path support.
            // -- the following code needs to be improved as
            // -- for searching descriptors in this manner can
            // -- be slow
            StringBuffer tmpBuffer = null;
            if (state._buffer != null) {
                if (!UnmarshalHandler.isWhitespace(state._buffer)) {
                    tmpBuffer = state._buffer;
                    state._buffer = null;
                }
            }
            if (tmpBuffer != null) {
                UnmarshalState targetState = state;
                String locPath = targetState._elementName;
                while ((targetState = targetState._parent) != null) {
                    if ((targetState._wrapper)
                            || (targetState._classDesc == null)) {
                        locPath = targetState._elementName + "/" + locPath;
                        continue;
                    }

                    XMLFieldDescriptor tmpDesc = targetState._classDesc
                            .getContentDescriptor();
                    if (tmpDesc != null
                            && locPath.equals(tmpDesc.getLocationPath())) {
                        if (targetState._buffer == null)
                            targetState._buffer = tmpBuffer;
                        else
                            targetState._buffer.append(tmpBuffer.toString());
                    }
                }
            }

            // -- remove current namespace scoping
            _unmarshalHandler.getNamespaceHandling()
                    .removeCurrentNamespaceInstance();
            return;
        }

        // -- check for special cases
        boolean byteArray = false;
        if (type.isArray()) {
            byteArray = (type.getComponentType() == Byte.TYPE);
        }

        // -- If we don't have an instance object and the Class type
        // -- is not a primitive or a byte[] we must simply return
        if ((state._object == null) && (!state._primitiveOrImmutable)) {
            // -- remove current namespace scoping
            _unmarshalHandler.getNamespaceHandling()
                    .removeCurrentNamespaceInstance();
            return;
        }

        // / DEBUG System.out.println("end: " + name);

        if (state._primitiveOrImmutable) {

            String str = null;

            if (state._buffer != null) {
                str = state._buffer.toString();
                state._buffer.setLength(0);
            }

            if (type == String.class
                    && !((XMLFieldDescriptorImpl) descriptor)
                            .isDerivedFromXSList()) {
                if (str != null)
                    state._object = str;
                else if (state._nil) {
                    state._object = null;
                } else {
                    state._object = "";
                }
            }
            // -- special handling for byte[]
            else if (byteArray && !descriptor.isDerivedFromXSList()) {
                if (str == null)
                    state._object = new byte[0];
                else {
                    state._object = _unmarshalHandler.decodeBinaryData(
                            descriptor, str);
                }
            } else if (state._args != null) {
                state._object = _unmarshalHandler.createInstance(state._type,
                        state._args);
            } else if (descriptor.isMultivalued()
                    && descriptor.getSchemaType() != null
                    && descriptor.getSchemaType().equals("list")
                    && ((XMLFieldDescriptorImpl) descriptor)
                            .isDerivedFromXSList()) {
                StringTokenizer attrValueTokenizer = new StringTokenizer(str);
                List<Object> primitives = new ArrayList<Object>();
                while (attrValueTokenizer.hasMoreTokens()) {
                    String tokenValue = attrValueTokenizer.nextToken();
                    if (MarshalFramework.isPrimitive(descriptor.getFieldType())) {
                        primitives.add(_unmarshalHandler.toPrimitiveObject(
                                type, tokenValue, state._fieldDesc));
                    } else {
                        Class<?> valueType = descriptor.getFieldType();
                        // -- handle base64/hexBinary
                        if (valueType.isArray()
                                && (valueType.getComponentType() == Byte.TYPE)) {
                            primitives.add(_unmarshalHandler.decodeBinaryData(
                                    descriptor, tokenValue));
                        }
                    }

                }
                state._object = primitives;
            } else {
                if (state._nil) {
                    state._object = null;
                } else {
                    state._object = _unmarshalHandler.toPrimitiveObject(type,
                            str, state._fieldDesc);
                }
            }
        } else if (ArrayHandler.class.isAssignableFrom(state._type)) {
            state._object = ((ArrayHandler) state._object).getObject();
            state._type = state._object.getClass();

        }

        // -- check for character content
        if ((state._buffer != null) && (state._buffer.length() > 0)
                && (state._classDesc != null)) {
            XMLFieldDescriptor cdesc = state._classDesc.getContentDescriptor();
            if (cdesc != null) {
                Object value = state._buffer.toString();
                if (MarshalFramework.isPrimitive(cdesc.getFieldType()))
                    value = _unmarshalHandler.toPrimitiveObject(
                            cdesc.getFieldType(), (String) value,
                            state._fieldDesc);
                else {
                    Class<?> valueType = cdesc.getFieldType();
                    // -- handle base64/hexBinary
                    if (valueType.isArray()
                            && (valueType.getComponentType() == Byte.TYPE)) {
                        value = _unmarshalHandler.decodeBinaryData(descriptor,
                                (String) value);
                    }
                }

                try {
                    FieldHandler handler = cdesc.getHandler();
                    boolean addObject = true;
                    if (_unmarshalHandler.isReuseObjects()) {
                        // -- check to see if we need to
                        // -- add the object or not
                        Object tmp = handler.getValue(state._object);
                        if (tmp != null) {
                            // -- Do not add object if values
                            // -- are equal
                            addObject = (!tmp.equals(value));
                        }
                    }
                    if (addObject)
                        handler.setValue(state._object, value);
                } catch (java.lang.IllegalStateException ise) {
                    String err = MessageFormat
                            .format(resourceBundle
                                    .getString("unmarshalHandler.error.unable.add.text"),
                                    new Object[] { descriptor.getXMLName(),
                                            ise.toString() });
                    throw new SAXException(err, ise);
                }
            }
            // -- Handle references
            else if (descriptor.isReference()) {
                UnmarshalState pState = _unmarshalHandler.getStateStack()
                        .getLastState();
                _unmarshalHandler.processIDREF(state._buffer.toString(),
                        descriptor, pState._object);
                _unmarshalHandler.getNamespaceHandling()
                        .removeCurrentNamespaceInstance();
                return;
            } else {
                // -- check for non-whitespace...and report error
                if (!UnmarshalHandler.isWhitespace(state._buffer)) {
                    String err = MessageFormat.format(resourceBundle
                            .getString("unmarshalHandler.error.illegal.text"),
                            new Object[] { name, state._buffer });
                    throw new SAXException(err);
                }
            }
        }

        // -- We're finished processing the object, so notify the
        // -- Listener (if any).
        Object stateObject = state._object;
        Object parentObject = (state._parent == null) ? null
                : state._parent._object;
        _unmarshalHandler.getDelegateUnmarshalListener().unmarshalled(
                stateObject, parentObject);

        // -- if we are at root....just validate and we are done
        if (_unmarshalHandler.getStateStack().isEmpty()) {
            if (_unmarshalHandler.isValidating()) {
                ValidationException first = null;
                ValidationException last = null;

                // -- check unresolved references
                if (_unmarshalHandler.getResolveTable() != null
                        && !_unmarshalHandler.getInternalContext()
                                .getLenientIdValidation()) {
                    Enumeration enumeration = _unmarshalHandler
                            .getResolveTable().keys();
                    while (enumeration.hasMoreElements()) {
                        Object ref = enumeration.nextElement();
                        // if
                        // (ref.toString().startsWith(MapItem.class.getName()))
                        // continue;
                        String msg = "unable to resolve reference: " + ref;
                        if (first == null) {
                            first = new ValidationException(msg);
                            last = first;
                        } else {
                            last.setNext(new ValidationException(msg));
                            last = last.getNext();
                        }
                    }
                }
                try {
                    Validator validator = new Validator();
                    ValidationContext context = new ValidationContext();
                    context.setInternalContext(_unmarshalHandler
                            .getInternalContext());
                    validator.validate(state._object, context);
                    if (!_unmarshalHandler.getInternalContext()
                            .getLenientIdValidation()) {
                        validator.checkUnresolvedIdrefs(context);
                    }
                    context.cleanup();
                } catch (ValidationException vEx) {
                    if (first == null)
                        first = vEx;
                    else
                        last.setNext(vEx);
                }
                if (first != null) {
                    throw new SAXException(first);
                }
            }
            return;
        }

        // -- Add object to parent if necessary

        if (descriptor.isIncremental()) {
            // -- remove current namespace scoping
            _unmarshalHandler.getNamespaceHandling()
                    .removeCurrentNamespaceInstance();
            return; // -- already added
        }

        Object val = state._object;

        // --special code for AnyNode handling
        if (_unmarshalHandler.getAnyNode() != null) {
            val = _unmarshalHandler.getAnyNode();
            _unmarshalHandler.setAnyNode(null);
        }

        // -- save fieldState
        UnmarshalState fieldState = state;

        // -- have we seen this object before?
        boolean firstOccurance = false;

        // -- get target object
        state = _unmarshalHandler.getStateStack().getLastState();
        if (state._wrapper) {
            state = fieldState._targetState;
        }

        // -- check to see if we have already read in
        // -- an element of this type.
        // -- (Q: if we have a container, do we possibly need to
        // -- also check the container's multivalued status?)
        if (!descriptor.isMultivalued()) {

            if (state.isUsed(descriptor)) {

                String location = name;
                while (!_unmarshalHandler.getStateStack().isEmpty()) {
                    UnmarshalState tmpState = _unmarshalHandler.getStateStack()
                            .removeLastState();
                    if (!tmpState._wrapper) {
                        if (tmpState._fieldDesc.isContainer())
                            continue;
                    }
                    location = state._elementName + "/" + location;
                }

                String err = MessageFormat
                        .format(resourceBundle
                                .getString("unmarshalHandler.error.element.occurs.more.than.once"),
                                new Object[] { name, state._type.getName(),
                                        location });

                ValidationException vx = new ValidationException(err);

                throw new SAXException(vx);
            }
            state.markAsUsed(descriptor);
            // -- if this is the identity then save id
            if (state._classDesc.getIdentity() == descriptor) {
                state._key = val;
            }
        } else {
            // -- check occurance of descriptor
            if (!state.isUsed(descriptor)) {
                firstOccurance = true;
            }

            // -- record usage of descriptor
            state.markAsUsed(descriptor);
        }

        try {
            FieldHandler handler = descriptor.getHandler();
            // check if the value is a QName that needs to
            // be resolved (ns:value -> {URI}value)
            String valueType = descriptor.getSchemaType();
            if ((valueType != null)
                    && (valueType.equals(MarshalFramework.QNAME_NAME))) {
                val = _unmarshalHandler.getNamespaceHandling()
                        .resolveNamespace(val);
            }

            boolean addObject = true;
            if (_unmarshalHandler.isReuseObjects()
                    && fieldState._primitiveOrImmutable) {
                // -- check to see if we need to
                // -- add the object or not
                Object tmp = handler.getValue(state._object);
                if (tmp != null) {
                    // -- Do not add object if values
                    // -- are equal
                    addObject = (!tmp.equals(val));
                }
            }

            // -- special handling for mapped objects
            if (descriptor.isMapped()) {
                if (!(val instanceof MapItem)) {
                    MapItem mapItem = new MapItem(fieldState._key, val);
                    val = mapItem;
                } else {
                    // -- make sure value exists (could be a reference)
                    MapItem mapItem = (MapItem) val;
                    if (mapItem.getValue() == null) {
                        // -- save for later...
                        addObject = false;
                        _unmarshalHandler.addReference(mapItem.toString(),
                                state._object, descriptor);
                    }
                }
            }

            if (addObject) {
                // -- clear any collections if necessary
                if (firstOccurance && _unmarshalHandler.isClearCollections()) {
                    handler.resetValue(state._object);
                }

                if (descriptor.isMultivalued()
                        && descriptor.getSchemaType() != null
                        && descriptor.getSchemaType().equals("list")
                        && ((XMLFieldDescriptorImpl) descriptor)
                                .isDerivedFromXSList()) {
                    List<Object> values = (List<Object>) val;
                    for (Object value : values) {
                        // -- finally set the value!!
                        handler.setValue(state._object, value);

                        // If there is a parent for this object, pass along
                        // a notification that we've finished adding a child
                        _unmarshalHandler.getDelegateUnmarshalListener()
                                .fieldAdded(descriptor.getFieldName(),
                                        state._object, fieldState._object);
                    }
                } else {

                    // -- finally set the value!!
                    handler.setValue(state._object, val);

                    // If there is a parent for this object, pass along
                    // a notification that we've finished adding a child
                    _unmarshalHandler.getDelegateUnmarshalListener()
                            .fieldAdded(descriptor.getFieldName(),
                                    state._object, fieldState._object);
                }
            }

        }
        /*
         * catch(java.lang.reflect.InvocationTargetException itx) {
         * 
         * Throwable toss = itx.getTargetException(); if (toss == null) toss =
         * itx;
         * 
         * String err = "unable to add '" + name + "' to <"; err +=
         * state.descriptor.getXMLName(); err +=
         * "> due to the following exception: " + toss; throw new
         * SAXException(err); }
         */
        catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.flush();
            String err = MessageFormat
                    .format(resourceBundle
                            .getString("unmarshalHandler.error.unable.add.element"),
                            new Object[] { name, state._fieldDesc.getXMLName(),
                                    sw.toString() });
            throw new SAXException(err, ex);
        }

        // -- remove current namespace scoping
        _unmarshalHandler.getNamespaceHandling()
                .removeCurrentNamespaceInstance();

        // remove additional (artifical aka container) state introduced for
        // single-valued (iow maxOccurs="1") choices.
        if (state._fieldDesc.isContainer() && state._classDesc.isChoice()
                && !state._fieldDesc.isMultivalued()) {
            _unmarshalHandler.endElement(state._elementName);
        }

    }
}
