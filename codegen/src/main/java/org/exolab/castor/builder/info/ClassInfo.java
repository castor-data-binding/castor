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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the course
 * of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005
 * are Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */
package org.exolab.castor.builder.info;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.builder.info.nature.XMLInfoNature;
import org.exolab.javasource.JClass;

/**
 * This class holds the necessary information so that the source generator can
 * properly create the necessary classes for the object model.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 07:37:49 -0600 (Thu, 13 Apr 2006) $
 */
public final class ClassInfo implements XMLInfo, PropertyHolder {
    
    /** The base class. */
    private ClassInfo _baseClass = null;
    /**  A reference to the JClass that this ClassInfo describes. */
    private JClass _class = null;
    /** true if this ClassInfo represents an abstract class. */
    private boolean _abstract    = false;
    
    /** Vector of FieldInfo's for all attributes that are members of this Class. */
    private Vector<FieldInfo> _atts = new Vector<FieldInfo>();
    /** Vector of FieldInfo's for all elements that are members of this Class. */
    private Vector<FieldInfo> _elements = new Vector<FieldInfo>();
    /** if this ClassInfo represents a TextField, this is this TextField's FieldInfo. */
    private FieldInfo _textField = null;
    
    /**
     * Map holding the properties set and read by Natures.
     */
    private Map<String, Object> _properties = new HashMap<String, Object>();
    
    /**
     * Map holding the available natures.
     */
    private Set<String> _natures = new HashSet<String>();

    /**
     * Creates a new ClassInfo. Adds the {@link XMLInfoNature} for legacy compliance.
     * @param jClass the JClass which this ClassInfo describes
     */
    public ClassInfo(final JClass jClass) {
        this.addNature(XMLInfoNature.class.getName());
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        xmlNature.setNodeType(NodeType.ELEMENT);
        if (jClass == null) {
            String err = "JClass passed to constructor of ClassInfo must not be null.";
            throw new IllegalArgumentException(err);
        }
        this._class = jClass;

        // set default GroupInfo instance
        xmlNature.setGroupInfo(new GroupInfo());
    }

    /**
     * Adds the given FieldInfo to this ClassInfo.
     *
     * @param fieldInfo the FieldInfo to add
     */
    public void addFieldInfo(final FieldInfo fieldInfo) {
        if (fieldInfo == null) {
            return;
        }

        fieldInfo.setDeclaringClassInfo(this);

        switch(new XMLInfoNature(fieldInfo).getNodeType()) {
            case ATTRIBUTE:
                if (!_atts.contains(fieldInfo)) {
                    _atts.addElement(fieldInfo);
                }
                break;
            case TEXT:
                _textField = fieldInfo;
                break;
            default:
                if (!_elements.contains(fieldInfo)) {
                    _elements.addElement(fieldInfo);
                }
                break;
        }
    }

    /**
     * Adds the given set of FieldInfos to this ClassInfo.
     *
     * @param fields an Array of FieldInfo objects
     */
    public void addFieldInfo(final FieldInfo[] fields) {
        for (int i = 0; i < fields.length; i++) {
            addFieldInfo(fields[i]);
        }
    }

    /**
     * @return true if Classes created with this ClassInfo allow content
     */
    public boolean allowContent() {
        return _textField != null;
    }

    /**
     * Returns true if the given FieldInfo is contained within this ClassInfo.
     *
     * @param fieldInfo
     *            the FieldInfo to check
     * @return true if the given FieldInfo is contained within this ClassInfo
     */
    public boolean contains(final FieldInfo fieldInfo) {
        if (fieldInfo == null) {
            return false;
        }

        switch (new XMLInfoNature(fieldInfo).getNodeType()) {
            case ATTRIBUTE:
                if (_atts != null) {
                    return _atts.contains(fieldInfo);
                }
                break;
            case TEXT:
                return (fieldInfo == _textField);
            default:
                if (_elements != null) {
                    return _elements.contains(fieldInfo);
                }
                break;
        }

        //if (sourceInfo != null)
        //    return sourceInfo.contains(fieldInfo);

        return false;
    }

    /**
     * Returns an array of XML attribute associated fields.
     * @return an array of XML attribute associated fields.
     */
    public FieldInfo[] getAttributeFields() {
        FieldInfo[] fields = null;
        if (_atts != null) {
            fields = new FieldInfo[_atts.size()];
            _atts.copyInto(fields);
        } else {
            fields = new FieldInfo[0];
        }
        return fields;
    } //-- getAttributeFields

    /**
     * Returns a fieldInfo that corresponds to an attribute with the given node name.
     * A ClassInfo cannot have 2 attributes with the same xml name.
     *
     * @param nodeName the NodeName of the field to get.
     * @return a fieldInfo that corresponds to an attribute with the given node name.
     */
    public FieldInfo getAttributeField(final String nodeName) {
        if (_atts == null) {
            return null;
        }

        for (int i = 0; i < _atts.size(); i++) {
            FieldInfo temp = _atts.get(i);
            if (new XMLInfoNature(temp).getNodeName().equals(nodeName)) {
                return temp;
            }
        }

        return null;
    }

    /**
     * Returns the base class of this classInfo if any. A classInfo can indeed
     * extend another classInfo to reflect the extension mechanism used in the
     * XML Schema.
     *
     * @return the base class of this classInfo if any.
     */
    public ClassInfo getBaseClass() {
        return _baseClass;
    }

    /**
     * Returns an array of XML element associated fields.
     *
     * @return an array of XML element associated fields.
     */
    public FieldInfo[] getElementFields() {
        FieldInfo[] members = null;
        if (_elements != null) {
            members = new FieldInfo[_elements.size()];
            _elements.copyInto(members);
        } else {
            members = new FieldInfo[0];
        }
        return members;
    } //-- getElementFields

    /**
     * Returns a fieldInfo that corresponds to an element with the given node name.
     * A ClassInfo cannot have 2 elements with the same xml name.
     *
     * @param nodeName the NodeName of the field to get.
     * @return a fieldInfo that corresponds to an element with the given node name.
     */
    public FieldInfo getElementField(final String nodeName) {
        if (_elements != null) {
            for (int i = 0; i < _elements.size(); i++) {
                FieldInfo temp = _elements.get(i);
                String elementNodeName = new XMLInfoNature(temp).getNodeName();
                if (elementNodeName != null && elementNodeName.equals(nodeName)) {
                    return temp;
                }
            }
        }
        return null;
    }

    /**
     * Returns the number of FieldInfo definitions for this ClassInfo.
     *
     * @return the number of FieldInfo definitions for this ClassInfo.
     */
    public int getFieldCount() {
        int count = 0;
        if (_atts != null) {
            count += _atts.size();
        }
        if (_elements != null) {
            count += _elements.size();
        }
        if (_textField != null) {
            ++count;
        }
        return count;
    } //-- getFieldCount

    /**
     * Returns the JClass described by this ClassInfo.
     *
     * @return the JClass which is described by this ClassInfo
     */
    public JClass getJClass() {
        return _class;
    } //-- getJClass

    /**
     * Returns the FieldInfo for the XML text associated field.
     *
     * @return the FieldInfo for the text content associated field, this may be
     *         null.
     */
    public FieldInfo getTextField() {
        return _textField;
    } //-- getTextField

    /**
     * Returns true if the JClass represented by this ClassInfo is abstract.
     *
     * @return true if the JClass represented by this ClassInfo is abstract
     */
    public boolean isAbstract() {
        return _abstract;
    }

    /**
     * Sets the class of this ClassInfo to be abstract of
     *
     * <code>abstractClass</code> is true, false otherwise.
     *
     * @param abstractClass true if the class represented by this ClassInfo is
     *        abstract
     */
    public void setAbstract(final boolean abstractClass) {
        _abstract = abstractClass;
    }

    /**
     * Sets the base class of this classInfo. A classInfo can indeed extend
     * another classInfo to reflect the extension mechanism used in the XML
     * Schema
     *
     * @param base the base class of this classInfo.
     */
    public void setBaseClass(final ClassInfo base) {
        _baseClass = base;
    }

    /**
     * @see org.exolab.castor.builder.info.nature.PropertyHolder#
     *      getProperty(java.lang.String)
     * @param name
     *            of the property
     * @return value of the property
     */
    public Object getProperty(final String name) {
        return _properties.get(name);
    }

    /**
     * @see org.exolab.castor.builder.info.nature.PropertyHolder#
     *      setProperty(java.lang.String, java.lang.Object)
     * @param name
     *            of the property
     * @param value
     *            of the property
     */
    public void setProperty(final String name, final Object value) {
        _properties.put(name, value);
    }

    /**
     * @see org.exolab.castor.builder.info.nature.NatureExtendable#
     *      addNature(java.lang.String)
     * @param nature
     *            ID of the Nature
     */
    public void addNature(final String nature) {
        _natures.add(nature);
    }

    /**
     * @see org.exolab.castor.builder.info.nature.NatureExtendable#
     *      hasNature(java.lang.String)
     * @param nature
     *            ID of the Nature
     * @return true if the Nature ID was added.
     */
    public boolean hasNature(final String nature) {
        return _natures.contains(nature);
    }

    /**
     * Returns all attribute {@link FieldInfo}s as a collection.
     * @return attribute fields.
     */
    public Collection<FieldInfo> getAttributeFieldsAsCollection() {
        return this._atts;
    }

    /**
     * Returns all element {@link FieldInfo}s as a collection.
     * @return element fields.
     */
    public Collection<FieldInfo> getElementFieldsAsCollection() {
        return this._elements;
    }

}
