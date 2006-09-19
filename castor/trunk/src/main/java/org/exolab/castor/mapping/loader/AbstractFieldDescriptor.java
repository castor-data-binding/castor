package org.exolab.castor.mapping.loader;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;

public abstract class AbstractFieldDescriptor implements FieldDescriptor {
    //--------------------------------------------------------------------------

    /** A reference to the containing class descriptor (parent). */
    private ClassDescriptor _parent;

    /** The name of the field. */
    private String _fieldName;

    /** The type of the field. */
    private Class _fieldType;

    /** The type class descriptor, if this field is of a type known by a descriptor. */
    private ClassDescriptor _classDescriptor;
    
    /** The field handler for get/set field value. */
    private FieldHandler _handler;

    /** True if the field is transient and should not be saved/stored. */
    private boolean _transient;

    /** True if the described field type is immutable. */
    private boolean _immutable;
    
    /** True if the field type is required. */
    private boolean _required;

    /** True if the object described by this descriptor is multivalued. */
    private boolean _multivalued;

    //--------------------------------------------------------------------------

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#setContainingClassDescriptor(
     *      org.exolab.castor.mapping.ClassDescriptor)
     * {@inheritDoc}
     */
    public final void setContainingClassDescriptor(final ClassDescriptor parent) {
        _parent = parent;
    }
    
    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#getContainingClassDescriptor()
     * {@inheritDoc}
     */
    public final ClassDescriptor getContainingClassDescriptor() {
        return _parent;
    }
    
    /**
     * Set the name of the field.
     * 
     * @param fieldName Field name.
     */
    public final void setFieldName(final String fieldName) {
        _fieldName = fieldName;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#getFieldName()
     * {@inheritDoc}
     */
    public final String getFieldName() {
        return _fieldName;
    }

    /**
     * Set the type of the field.
     * 
     * @param fieldType Field type.
     */
    public final void setFieldType(final Class fieldType) {
        _fieldType = fieldType;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#getFieldType()
     * {@inheritDoc}
     */
    public final Class getFieldType() {
        return _fieldType;
    }

    /**
     * Set the ClassDescriptor for the described field.
     *
     * @param classDescriptor The ClassDescriptor for the described field.
     */
    public final void setClassDescriptor(final ClassDescriptor classDescriptor) {
        _classDescriptor = classDescriptor;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#getClassDescriptor()
     * {@inheritDoc}
     */
    public final ClassDescriptor getClassDescriptor() {
        return _classDescriptor;
    }

    /**
     * Set the FieldHandler for the field being described by this FieldDescriptor.
     *
     * @param handler The FieldHandler for the field being described.
     */
    public final void setHandler(final FieldHandler handler) {
        _handler = handler;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#getHandler()
     * {@inheritDoc}
     */
    public final FieldHandler getHandler() {
        return _handler;
    }

    /**
     * Sets whether or not the describled field is 'transient'.
     * 
     * @param isTransient The flag indicating if the described field is 'transient'.
     */
    public final void setTransient(final boolean isTransient) {
        _transient = isTransient;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#isTransient()
     * {@inheritDoc}
     */
    public final boolean isTransient() {
        return _transient;
    }

    /**
     * Set the immutable flag which indicates that changes to this Field result in a
     * new Object to be created, such as java.lang.String. It serves to identify fields
     * which should not be constructed until all the data is available.
     * 
     * @param immutable Flag which if true indicates that the field is immutable.
     */
    public final void setImmutable(final boolean immutable) {
        _immutable = immutable;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#isImmutable()
     * {@inheritDoc}
     */
    public final boolean isImmutable() {
        return _immutable;
    }

    /**
     * Set whether or not the described field is required.
     * 
     * @param required The flag indicating whether or not the described field is required.
     */
    public final void setRequired(final boolean required) {
        _required = required;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#isRequired()
     * {@inheritDoc}
     */
    public final boolean isRequired() {
        return _required;
    }

    /**
     * Set wheter the object described by this descriptor is multivalued or not.
     * 
     * @param multivalued True if the object described by this descriptor is multivalued.
     */
    public final void setMultivalued(final boolean multivalued) {
        _multivalued = multivalued;
    }
    
    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#isMultivalued()
     * {@inheritDoc}
     */
    public final boolean isMultivalued() {
        return _multivalued;
    }

    //--------------------------------------------------------------------------
}
