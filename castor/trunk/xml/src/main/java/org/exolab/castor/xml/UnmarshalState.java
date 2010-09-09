/**
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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import java.util.HashSet;
import java.util.Set;

/**
 * The state information class for the UnmarshalHandler.
 * 
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-12-11 02:25:45 -0700 (Sat, 11 Dec 2004) $
 */
public class UnmarshalState {
    
    /** Holds on to Constructor arguments. */
    private UnmarshalHandler.Arguments _args = null;
    
    /** Holds the current location path. */
    private String _location = "";
    
    /** Indicates if the xsi:nil='true' attribute was present on the element. */
    private boolean _nil = false;
    
    /** The xml element name of the current object. */
    private String _elementName = null;

    /** Characters read in during unmarshalling. */
    private StringBuffer _buffer = null;

    /** The key for the object. This may be null if no key
     *  or identity has been specified. */
    private Object _key = null;
    
    /** The current that we are unmarshalling to. */
    private Object _object = null;
    
    /** The class of the object, mainly used for primitives. */
    private Class<?> _type = null;

    /** The field descriptor for the Object. */
    private XMLFieldDescriptor _fieldDescriptor = null;

    /** The class descriptor for the Object, in case
     *  FieldDescriptor#getClassDescriptor returns null. */
    private XMLClassDescriptor _classDescriptor = null;

    /** Is the field a primitive or immutable type? */
    private boolean _primitiveOrImmutable = false;

    /** The list of *used* field descriptors. */
    private Set<XMLFieldDescriptor> _markedList = new HashSet<XMLFieldDescriptor>();

    /** Is this a derived field? */
    private boolean _derived = false;
    
    /** Is this a wrapper state? */
    private boolean _wrapper = false;
    
    /** The whitespace preserve flag. */
    private boolean _whitespacePreserving = false;
    
    private boolean _trailingWhitespaceRemoved = false;
    
    /** Index of next expected sequence element; used during validation. */
    private int _expectedIndex = 0;
    
    /** Indicates (during validation) whether the current field descriptor 
     *  points to a multi-valued element. */
    private boolean _withinMultivaluedElement = false;
    
    /** The {@link UnmarshalState} which contains information about the parent object for the object
     *  contained within this state. Used when handling element containers/wrappers. */
    private UnmarshalState _targetState = null;
    
    /** A reference to the parent state. */
    private UnmarshalState _parent = null;

    /**
     * Reinitializes all variables
     */
    void clear() {
        
        setConstructorArguments(null);
        setLocation("");
        setElementName(null);
        setBuffer(null);
        setKey(null);
        setNil(false);
        setObject(null);
        setType(null);
        setFieldDescriptor(null);
        setClassDescriptor(null);
        setPrimitiveOrImmutable(false);
        if (_markedList != null) {
            _markedList.clear();
        }
        setDerived(false);
        setWrapper(false);
        setTargetState(null);
        setWhitespacePreserving(false);
        setParent(null);
        setTrailingWhitespaceRemoved(false);
    } //-- clear

    /**
     * Marks the given XMLFieldDescriptor as having been used.
     * 
     * @param descriptor the XMLFieldDescriptor to mark.
     */
    void markAsUsed(XMLFieldDescriptor descriptor) {
        _markedList.add(descriptor);
    }

    void markAsNotUsed(XMLFieldDescriptor descriptor) {
        if (_markedList == null) { return; }
        _markedList.remove(descriptor);
    }

    boolean isUsed(XMLFieldDescriptor descriptor) {
        if (_markedList == null) { return false; }
        return _markedList.contains(descriptor);
    }

    void setFieldDescriptor(XMLFieldDescriptor fieldDesc) {
        _fieldDescriptor = fieldDesc;
    }

    XMLFieldDescriptor getFieldDescriptor() {
        return _fieldDescriptor;
    }

    void setObject(Object object) {
        _object = object;
    }

    Object getObject() {
        return _object;
    }

    void setKey(Object key) {
        _key = key;
    }

    Object getKey() {
        return _key;
    }

    void setType(Class<?> type) {
        _type = type;
    }

    Class<?> getType() {
        return _type;
    }

    void setClassDescriptor(XMLClassDescriptor classDesc) {
        _classDescriptor = classDesc;
    }

    XMLClassDescriptor getClassDescriptor() {
        return _classDescriptor;
    }

    void setLocation(String location) {
        _location = location;
    }

    String getLocation() {
        return _location;
    }

    void setElementName(String elementName) {
        _elementName = elementName;
    }

    String getElementName() {
        return _elementName;
    }

    void setBuffer(StringBuffer buffer) {
        _buffer = buffer;
    }

    StringBuffer getBuffer() {
        return _buffer;
    }

    void setDerived(boolean derived) {
        _derived = derived;
    }

    boolean isDerived() {
        return _derived;
    }

    void setWrapper(boolean wrapper) {
        _wrapper = wrapper;
    }

    boolean isWrapper() {
        return _wrapper;
    }

    void setWhitespacePreserving(boolean wsPreserve) {
        _whitespacePreserving = wsPreserve;
    }

    boolean isWhitespacePreserving() {
        return _whitespacePreserving;
    }

    void setPrimitiveOrImmutable(boolean primitiveOrImmutable) {
        _primitiveOrImmutable = primitiveOrImmutable;
    }

    boolean isPrimitiveOrImmutable() {
        return _primitiveOrImmutable;
    }

    void setTrailingWhitespaceRemoved(boolean trailingWhitespaceRemoved) {
        _trailingWhitespaceRemoved = trailingWhitespaceRemoved;
    }

    boolean isTrailingWhitespaceRemoved() {
        return _trailingWhitespaceRemoved;
    }

    void setTargetState(UnmarshalState targetState) {
        _targetState = targetState;
    }

    UnmarshalState getTargetState() {
        return _targetState;
    }

    void setParent(UnmarshalState parent) {
        _parent = parent;
    }

    UnmarshalState getParent() {
        return _parent;
    }

    void setNil(boolean nil) {
        _nil = nil;
    }

    boolean isNil() {
        return _nil;
    }

    public void setExpectedIndex(int expectedIndex) {
        _expectedIndex = expectedIndex;
    }

    public int getExpectedIndex() {
        return _expectedIndex;
    }

    public void setWithinMultivaluedElement(boolean withinMultivaluedElement) {
        _withinMultivaluedElement = withinMultivaluedElement;
    }

    public boolean isWithinMultivaluedElement() {
        return _withinMultivaluedElement;
    }

    void setConstructorArguments(UnmarshalHandler.Arguments args) {
        _args = args;
    }

    UnmarshalHandler.Arguments getConstructorArguments() {
        return _args;
    }
}
