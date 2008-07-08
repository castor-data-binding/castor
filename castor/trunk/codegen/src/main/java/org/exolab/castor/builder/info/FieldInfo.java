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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the course
 * of employment at Intalio Inc.
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */
package org.exolab.castor.builder.info;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.builder.factory.FieldMemberAndAccessorFactory;
import org.exolab.castor.builder.info.nature.XMLInfoNature;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.JField;
import org.exolab.javasource.JType;

/**
 * A class for representing field members of a class. FieldInfo objects hold all
 * the information required about a member in order to be able to produce
 * XML data binding (marshal/unmarshal) and validation code.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class FieldInfo implements XMLInfo, PropertyHolder {
    /**
     * Map holding the properties set and read by Natures.
     */
    private Map _properties = new HashMap();
    
    /**
     * Set holding applicable natures.
     */
    private Set _natures = new HashSet();
    
    /** The Read / Getter method flag. */
    public static final int READ_METHOD              = 1;
    /** The Write / Setter method flag. */
    public static final int WRITE_METHOD             = 2;
    /** The Read and Write methods flags. */
    public static final int READ_WRITE_METHODS       = 3;

    /** Method prefixes for "Add" methods. */
    protected static final String METHOD_PREFIX_ADD    = "add";
    /** Method prefixes for "Delete" methods. */
    protected static final String METHOD_PREFIX_DELETE = "delete";
    /** Method prefixes for "Get" methods. */
    protected static final String METHOD_PREFIX_GET    = "get";
    /** Method prefixes for "Has" methods. */
    protected static final String METHOD_PREFIX_HAS    = "has";
    /** Method prefixes for "Set" methods. */
    protected static final String METHOD_PREFIX_SET    = "set";
    /** Method prefixes for "Is" methods. */
    protected static final String METHOD_PREFIX_IS     = "is";

    /** The Java name for Members described by this FieldInfo. */
    private String _name       = null;

    /**
     * {@link ClassInfo} instance which 'own' (declares) this
     * {@link FieldInfo}.
     */
    private ClassInfo _declaringClassInfo = null;
    
    /** JavaDoc comment. */
    private String _comment    = null;

    /** The default value for this FieldInfo. */
    private String _default    = null;
    /** The fixed production for this FieldInfo. */
    private String _fixed      = null;
    /** A flag to indicate a final member. */
    private boolean _final     = false;
    /** The methods flags, indicates which methods to create. */
    private int _methods = READ_WRITE_METHODS;
    /** A reference to the FieldInfo instance within the same class. */
    private FieldInfo _fieldInfoReference = null;
    /** A flag to indicate a static member. */
    private boolean _static    = false;
    /** Flags whether or not the a MarshalDescriptor should be created for this FieldInfo. */
    private boolean _transient = false;
    /** A flag to indicate a bound property. */
    private boolean _bound = false;
    /** A flag to indicate a container field. */
    private boolean _isContainer = false;
    /**
     * The fully qualified name of the XMLFieldHandler (if any)
     * to use in the generated descriptor.
     */
    private String _fieldHandler;
    /** A boolean to indicate that this field represents a "nillable" field. */
    private boolean _nillable = false;
    /** The fully qualified name of the Validator (if any) to use in the generated descriptor. */
    private String _validator;
    /** Visibility of this FieldInfo. */
    private String _visibility = "private";
    
    /** 
     * Factory responsible for creating a {@link JField} out of a 
     * {@link FieldInfo}.
     */
    private FieldMemberAndAccessorFactory _memberAndAccessorFactory;
    
    /**
     * Holds the possible substitution groups for this class.
     */
    private List _substitutionGroupMembers = new LinkedList();

    /**
     * Creates a new FieldInfo with the given XML Schema type and the given
     * member name. Adds the {@link XMLInfoNature} for legacy compliance.
     *
     * @param type
     *            the XML Schema type of this member
     * @param name
     *            the name of the member
     * @param memberAndAccessorFactory 
     *            the FieldMemberAndAccessorFactory to be used
     */
    public FieldInfo(final XSType type, final String name, 
            final FieldMemberAndAccessorFactory memberAndAccessorFactory) {
        this._name    = name;
        this._memberAndAccessorFactory = memberAndAccessorFactory;
        this.addNature(XMLInfoNature.class.getName());
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        xmlNature.setSchemaType(type);
    } //-- FieldInfo


    /**
     * Returns the FieldMemberAndAccessorFactory instance to use to create 
     * a JField out of this FieldInfo.
     * @return the suitable FieldMemberAndAccessorFactory
     */
    public FieldMemberAndAccessorFactory getMemberAndAccessorFactory() {
        return _memberAndAccessorFactory;
    }

    /* Returns the default value for this FieldInfo.
     *
     * @return the default value for this FieldInfo, or null if no default value
     *         was set;
     */
    public final String getDefaultValue() {
        return _default;
    } //-- getDefaultValue

    /**
     * Returns the fixed production for this FieldInfo, or null if no fixed
     * value has been specified.
     * <p>
     * NOTE: Fixed values are NOT the same as default values
     *
     * @return the fixed value for this FieldInfo
     */
    public final String getFixedValue() {
        return _fixed;
    } //-- getFixedValue

    /**
     * Returns the name of the delete method for this FieldInfo.
     * @return the name of the delete method for this FieldInfo.
     */
    public final String getDeleteMethodName() {
        return METHOD_PREFIX_DELETE + getMethodSuffix();
    } //-- getDeleteMethodName

    /**
     * Returns the name of the has method for this FieldInfo.
     * @return the name of the has method for this FieldInfo.
     */
    public final String getHasMethodName() {
        return METHOD_PREFIX_HAS + getMethodSuffix();
    } //-- getHasMethodName

    /**
     * Returns the name of the read method for this FieldInfo.
     * @return the name of the read method for this FieldInfo.
     */
    public final String getReadMethodName() {
        return METHOD_PREFIX_GET + getMethodSuffix();
    } //-- getReadMethodName

   /**
    * Returns the fully qualified name of the Validator to use.
    *
    * @return the fully qualified name of the Validator to use.
    */
    public final String getValidator() {
        return _validator;
    }

    /**
     * Returns the name of the write method for this FieldInfo.
     * @return the name of the write method for this FieldInfo.
     */
    public final String getWriteMethodName() {
        if (isMultivalued()) {
            return METHOD_PREFIX_ADD + getMethodSuffix();
        }
        return METHOD_PREFIX_SET + getMethodSuffix();
    } //-- getWriteMethodName
    
    /**
     * Get the 'is' method for this FieldInfo. 
     * 
     * @return the name of the 'is' method for this FieldInfo
     */
    public final String getIsMethodName() {
        return METHOD_PREFIX_IS + getMethodSuffix();
    }

   /**
    * Returns the fully qualified name of the XMLFieldHandler to use.
    *
    * @return the fully qualified name of the XMLFieldHandler to use.
    */
    public final String getXMLFieldHandler() {
        return _fieldHandler;
    }

    /**
     * Returns the comment associated with this Member.
     *
     * @return the comment associated with this Member, or null.
     * if one has not been set.
     */
    public final String getComment() {
        return _comment;
    } //-- getComment

    /**
     * Returns the methods flag that indicates which.
     *
     * methods will be created.
     *
     * @return the methods flag
     */
    public final int getMethods() {
        return _methods;
    } //-- getMethods

    /**
     * Returns the name of this FieldInfo.
     *
     * @return the name of this FieldInfo.
     */
    public final String getName() {
        return this._name;
    } //-- getName

    /**
     * Returns true if this FieldInfo represents a bound property.
     *
     * @return true if this FieldInfo represents a bound property.
     */
    public final boolean isBound() {
        return _bound;
    } //-- isBound

    /**
     * Returns true if this FieldInfo describes a container class. A container
     * class is a class which should not be marshalled as XML, but whose members
     * should be.
     *
     * @return true if this ClassInfo describes a container class.
     */
    public final boolean isContainer() {
        return _isContainer;
    } //-- isContainer

    /**
     * Returns true if the "has" and "delete" methods are needed for the field
     * associated with this FieldInfo.
     *
     * @return true if the has and delete methods are needed.
     */
    public final boolean requiresHasAndDeleteMethods() {
        XSType xsType = getSchemaType();
        JType jType  = xsType.getJType();
        return ((!xsType.isEnumerated()) && jType.isPrimitive());
    } //-- requiresHasAndDeleteMethods

    /**
     * Returns true if this field represents a nillable field. A nillable field
     * is a field that can have null content (see XML Schema 1.0 definition of
     * nillable).
     *
     * @return true if nillable, otherwise false.
     * @see #setNillable(boolean)
     */
     public final boolean isNillable() {
         return _nillable;
     } //-- isNillable

    /**
     * Returns true if this FieldInfo is a transient member. Transient members
     * are members which should be ignored by the Marshalling framework.
     *
     * @return true if this FieldInfo is transient.
     */
    public final boolean isTransient() {
        return (_transient || _final || _static);
    } //-- isTransient

    /**
     * Sets the comment for this Member.
     * @param comment the comment or description for this Member
     */
    public final void setComment(final String comment) {
        _comment = comment;
    } //-- setComment

    /**
     * Returns the ClassInfo to which this Member was declared, for inheritance reasons.
     * @return the ClassInfo to which this Member was declared.
     */
    public final ClassInfo getDeclaringClassInfo() {
        return this._declaringClassInfo;
    } //-- getDeclaringClassInfo

    /**
     * Sets whether or not this FieldInfo represents a bound property.
     *
     * @param bound the flag when true indicates that this FieldInfo represents a
     *        bound property.
     */
    public final void setBound(final boolean bound) {
        _bound = bound;
    } //-- setBound

    /**
     * Sets whether or not this FieldInfo describes a container field. A
     * container field is a field which should not be marshalled directly as
     * XML, but whose members should be. By default this is false.
     *
     * @param isContainer
     *            the boolean value when true indicates this class should be a
     *            container class.
     */
    public final void setContainer(final boolean isContainer) {
        _isContainer = isContainer;
    } //-- setContainer

    public final void setDeclaringClassInfo(final ClassInfo declaringClassInfo) {
        this._declaringClassInfo = declaringClassInfo;
    } //-- setDeclaringClassInfo

    /**
     * Sets the default value for this FieldInfo.
     * @param defaultValue the default value
     */
    public final void setDefaultValue(final String defaultValue) {
        this._default = defaultValue;
    } //-- setDefaultValue;

    /**
     * Sets the "final" status of this FieldInfo. Final members are also
     * transient.
     *
     * @param isFinal
     *            the boolean indicating the final status, if true this
     *            FieldInfo will be treated as final.
     */
    public final void setFinal(final boolean isFinal) {
        this._final = isFinal;
    } //-- isFinal

    /**
     * Sets the fixed value in which instances of this field type must lexically
     * match. NOTE: This is not the same as default value!
     *
     * @param fixedValue
     *            the fixed production for this FieldInfo
     */
    public final void setFixedValue(final String fixedValue) {
        this._fixed = fixedValue;
    } //-- setFixedValue

    /**
     * Sets which methods to create: READ_METHOD, WRITE_METHOD,
     * READ_WRITE_METHODS.
     *
     * @param methods a flag describing which methods to create.
     */
    public final void setMethods(final int methods) {
        _methods = methods;
    } //-- setMethods

    /**
     * Sets whether or not this field can be nillable.
     *
     * @param nillable
     *            a boolean that when true means the field may be nil.
     * @see #isNillable()
     */
    public final void setNillable(final boolean nillable) {
        _nillable = nillable;
    } //-- setNillable

    /**
     * Sets the name of the field within the same class that is a reference to
     * this field.
     *
     * @param fieldInfo
     */
    public final void setFieldInfoReference(final FieldInfo fieldInfo) {
        _fieldInfoReference = fieldInfo;
    } //-- setReference

    /**
     * Sets the "static" status of this FieldInfo. Static members are also
     * transient.
     *
     * @param isStatic the boolean indicating the static status, if true this
     *        FieldInfo will be treated as static
     */
    public final void setStatic(final boolean isStatic) {
        this._static = isStatic;
    } //-- setStatic

    /**
     * Sets the transient status of this FieldInfo.
     *
     * @param isTransient the boolean indicating the transient status, if true this
     *        FieldInfo will be treated as transient
     */
    public final void setTransient(final boolean isTransient) {
        this._transient = isTransient;
    } //-- setTransient

    /**
     * Sets the name of the Validator to use.
     *
     * @param validator the fully qualified name of the validator to use.
     */
    public final void setValidator(final String validator) {
        _validator = validator;
    }

    /**
     * Sets the name of the XMLfieldHandler to use.
     *
     * @param handler the fully qualified name of the handler to use.
     */
    public final void setXMLFieldHandler(final String handler) {
        _fieldHandler = handler;
    }

    /**
     * Returns the method suffix for creating method names.
     *
     * @return the method suffix used when creating method names.
     */
    public String getMethodSuffix() {
        if (_name.startsWith("_")) {
            return _memberAndAccessorFactory.getJavaNaming().toJavaClassName(_name.substring(1));
        }
        return _memberAndAccessorFactory.getJavaNaming().toJavaClassName(_name);
    }

    /**
     * Sets the visibility of this FieldInfo.
     *
     * @param visibility the visibility of this FieldInfo.
     */
    public final void setVisibility(final String visibility) {
        _visibility = visibility;
    }

    /**
     * Sets the possible substitution groups for this class.
     * @param substitutionGroupMembers Possible substitution groups for this class.
     */
    public void setSubstitutionGroupMembers(final List substitutionGroupMembers) {
        this._substitutionGroupMembers = substitutionGroupMembers;
    }

    /**
     * Returns the possible substitution groups for this class.
     * @return the possible substitution groups for this class.
     */
    public List getSubstitutionGroupMembers() {
        return this._substitutionGroupMembers;
    }
    
	public boolean isStatic() {
		return _static;
	}

	public boolean isFinal() {
		return _final;
	}

	public Object getVisibility() {
		return _visibility;
	}

	public FieldInfo getFieldInfoReference() {
		return _fieldInfoReference;
	}
	
	
     
     
     
     

    /**
     * @see org.exolab.castor.builder.info.nature.PropertyHolder#
     *      getProperty(java.lang.String)
     * @param name
     *            of the property
     * @return value of the property
     */
    public final Object getProperty(final String name) {
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
    public final void setProperty(final String name, final Object value) {
        _properties.put(name, value);
    }

    /**
     * @see org.exolab.castor.builder.info.nature.NatureExtendable#
     *      addNature(java.lang.String)
     * @param nature
     *            ID of the Nature
     */
    public final void addNature(final String nature) {
        _natures.add(nature);
    }

    /**
     * @see org.exolab.castor.builder.info.nature.NatureExtendable#
     *      hasNature(java.lang.String)
     * @param nature
     *            ID of the Nature
     * @return true if the Nature ID was added.
     */
    public final boolean hasNature(final String nature) {
        return _natures.contains(nature);
    }
    
    /**
     * Returns the namespace prefix of the object described by this XMLInfo.
     *
     * @return the namespace prefix of the object described by this XMLInfo
     */
    public final String getNamespacePrefix() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.getNamespacePrefix();
    }

    /**
     * Returns the namespace URI of the object described by this XMLInfo.
     *
     * @return the namespace URI of the object described by this XMLInfo
     */
    public final String getNamespaceURI() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.getNamespaceURI();
    }

    /**
     * Returns the XML name for the object described by this XMLInfo.
     *
     * @return the XML name for the object described by this XMLInfo, or null if
     *         no name has been set
     */
    public final String getNodeName() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.getNodeName();
    }

    /**
     * Returns the node type for the object described by this XMLInfo.
     *
     * @return the node type for the object described by this XMLInfo
     */
    public final short getNodeType() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.getNodeType();
    }

    /**
     * Returns the string name of the nodeType, either "attribute", "element" or
     * "text".
     *
     * @return the name of the node-type of the object described by this
     *         XMLInfo.
     */
    public final String getNodeTypeName() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.getNodeTypeName();
    }

    /**
     * Returns the XML Schema type for the described object.
     *
     * @return the XML Schema type.
     */
    public final XSType getSchemaType() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.getSchemaType();
    }

    /**
     * Returns true if XSD is global element or element with anonymous type.
     *
     * @return true if xsd is element
     */
    public final boolean isElementDefinition() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.isElementDefinition();
    }

    /**
     * Return whether or not the object described by this XMLInfo is
     * multi-valued (appears more than once in the XML document).
     *
     * @return true if this object can appear more than once.
     */
    public boolean isMultivalued() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.isMultivalued();
    }

    /**
     * Return true if the XML object described by this XMLInfo must appear at
     * least once in the XML document (or object model).
     *
     * @return true if the XML object must appear at least once.
     */
    public final boolean isRequired() {
        XMLInfoNature xmlNature = new XMLInfoNature(this);
        return xmlNature.isRequired();
    }

}
