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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the course
 * of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005
 * are Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import java.util.Vector;

import org.exolab.javasource.JClass;

/**
 * This class holds the necessary information so that the source generator can
 * properly create the necessary classes for the object model.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 07:37:49 -0600 (Thu, 13 Apr 2006) $
 */
public class ClassInfo extends XMLInfo {

    /** Vector of FieldInfo's for all attributes that are members of this Class */
    private Vector    _atts      = null;
    /** Vector of FieldInfo's for all elements that are members of this Class */
    private Vector    _elements  = null;
    /** if this ClassInfo represents a TextField, this is this TextField's FieldInfo */
    private FieldInfo _textField = null;
    /** The base class */
    private ClassInfo _baseClass = null;
    /**  A reference to the JClass that this ClassInfo describes */
    private JClass _class = null;
    /** The group information for this ClassInfo */
    private GroupInfo _groupInfo = null;
    /**
     * true if this ClassInfo describes a container class. That is, a class
     * which should not be marshalled as XML, but whose members should be.
     */
    private boolean _isContainer = false;
    /** true if this ClassInfo represents an abstract class */
    private boolean _abstract    = false;

    /**
     * Creates a new ClassInfo.
     * @param jClass the JClass which this ClassInfo describes
     */
    public ClassInfo(final JClass jClass) {
        super(XMLInfo.ELEMENT_TYPE);
        if (jClass == null) {
            String err = "JClass passed to constructor of ClassInfo must not be null.";
            throw new IllegalArgumentException(err);
        }
        this._class = jClass;

        _groupInfo = new GroupInfo();
    } //-- ClassInfo

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Adds the given FieldInfo to this ClassInfo
     * @param fieldInfo the FieldInfo to add
     */
    public void addFieldInfo(final FieldInfo fieldInfo) {
        if (fieldInfo == null) {
            return;
        }

        fieldInfo.setDeclaringClassInfo(this);

        switch(fieldInfo.getNodeType()) {
            case XMLInfo.ATTRIBUTE_TYPE:
                if (_atts == null) {
                    _atts = new Vector(3);
                }
                if (!_atts.contains(fieldInfo)) {
                    _atts.addElement(fieldInfo);
                }
                break;
            case XMLInfo.TEXT_TYPE:
                _textField = fieldInfo;
                break;
            default:
                if (_elements == null) {
                    _elements = new Vector(5);
                }
                if (!_elements.contains(fieldInfo)) {
                    _elements.addElement(fieldInfo);
                }
                break;
        }
    } //-- addFieldInfo

    /**
     * Adds the given set of FieldInfos to this ClassInfo
     * @param fields an Array of FieldInfo objects
     */
    public void addFieldInfo(final FieldInfo[] fields) {
        for (int i = 0; i < fields.length; i++) {
            addFieldInfo(fields[i]);
        }
    } //-- addFieldInfo

    /**
     * @return true if Classes created with this ClassInfo allow content
     */
    public boolean allowContent() {
        return _textField != null;
    } //-- allowsTextContent

    /**
     * Returns true if the given FieldInfo is contained within this ClassInfo
     *
     * @param fieldInfo
     *            the FieldInfo to check
     * @return true if the given FieldInfo is contained within this ClassInfo
     */
    public boolean contains(final FieldInfo fieldInfo) {
        if (fieldInfo == null) {
            return false;
        }

        switch (fieldInfo.getNodeType()) {
            case XMLInfo.ATTRIBUTE_TYPE:
                if (_atts != null) {
                    return _atts.contains(fieldInfo);
                }
                break;
            case XMLInfo.TEXT_TYPE:
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
    } //-- contains

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
            FieldInfo temp = (FieldInfo) _atts.get(i);
            if (temp.getNodeName().equals(nodeName)) {
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
                FieldInfo temp = (FieldInfo) _elements.get(i);
                String elementNodeName = temp.getNodeName();
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
     * Returns the GroupInfo for this ClassInfo
     *
     * @return the GroupInfo for this ClassInfo
     */
    public GroupInfo getGroupInfo() {
        return _groupInfo;
    } //-- getGroupInfo

    /**
     * Returns the JClass described by this ClassInfo
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
     * Returns true if the JClass represented by this ClassInfo is abstract
     * @return true if the JClass represented by this ClassInfo is abstract
     */
    public boolean isAbstract() {
        return _abstract;
    }

    /**
     * Returns true if the compositor of this GroupInfo is a choice
     *
     * @return true if the compositor of this GroupInfo is a choice
     */
    public boolean isChoice() {
        return _groupInfo.isChoice();
    } //-- isChoice

    /**
     * Returns true if this ClassInfo describes a container class. A container
     * class is a class which should not be marshalled as XML, but whose members
     * should be.
     *
     * @return true if this ClassInfo describes a container class.
     */
    public boolean isContainer() {
        return _isContainer;
    } //-- isContainer

    /**
     * Returns true if the compositor of this GroupInfo is a sequence
     *
     * @return true if the compositor of this GroupInfo is a sequence
     */
    public boolean isSequence() {
        return _groupInfo.isSequence();
    } //-- isSequence

    /**
     * Sets the class of this ClassInfo to be abstract of
     * <code>abstractClass</code> is true, false otherwise.
     *
     * @param abstractClass
     *            true if the class represented by this ClassInfo is abstract
     */
    public void setAbstract(final boolean abstractClass) {
        _abstract = abstractClass;
    }

    /**
     * Sets the base class of this classInfo. A classInfo can indeed extend
     * another classInfo to reflect the extension mechanism used in the XML
     * Schema
     *
     * @param base
     *            the base class of this classInfo.
     */
    public void setBaseClass(final ClassInfo base) {
        _baseClass = base;
    }

    /**
     * Sets whether or not this ClassInfo describes a container class. A
     * container class is a class which should not be marshalled as XML, but
     * whose members should be. By default this is false.
     *
     * @param isContainer
     *            the boolean value when true indicates this class should be a
     *            container class.
     */
    public void setContainer(final boolean isContainer) {
        _isContainer = isContainer;
    } //-- setContainer

} //-- ClassInfo
