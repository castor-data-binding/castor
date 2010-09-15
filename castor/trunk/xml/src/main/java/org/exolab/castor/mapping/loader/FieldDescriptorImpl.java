package org.exolab.castor.mapping.loader;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.xml.types.FieldMappingCollectionType;

public class FieldDescriptorImpl implements FieldDescriptor {

    /** A reference to the containing class descriptor (parent). */
    private ClassDescriptor _parent;

    /** The name of the field. */
    private String _fieldName;

    /** The type of the field. */
    private Class<?> _fieldType;

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

    /** True if access to field is direct. */
    private boolean _direct;
    
    /** True if access to field is lazy. */
    private boolean _lazy;
    
    /** True if the object described by this descriptor is multi-valued. */
    private boolean _multivalued;
    
    /** True if the field is part of the identity of the class. */
    private boolean _identity;

    /** The name of a class that implements the {@link Comparator} interface. */
    private String _comparator;

    /** The name of the 'getter' method used to obtain the value of a property. */
    private String _getMethod;

    /** The name of the 'setter' method used to set the value of a property. */
    private String _setMethod;

    /** The name of the 'create' method used to create an instance of a property. */
    private String _createMethod;
    
    /** The type of the collection, if the field is multi-valued. */
    private FieldMappingCollectionType _collection;

    /**
     * Map holding the properties set and read by natures.
     */
    private Map<String, Object> _properties = new HashMap<String, Object>();

    /**
     * Map holding the available natures.
     */
    private Set<String> _natures = new HashSet<String>();
    
    /**
     * Creates a default instance of a field descriptor.
     */
    protected FieldDescriptorImpl() { }

    /**
     * Constructs a new field descriptor.
     *
     * @param fieldName The field name
     * @param typeInfo The field type information
     * @param handler The field handler (may be null)
     * @param isTransitive True if the field is transient
     */
    public FieldDescriptorImpl(final String fieldName, 
            final TypeInfo typeInfo,
            final FieldHandler handler, 
            final boolean isTransitive) {
        
        if (fieldName == null) {
            throw new IllegalArgumentException("Internal error. Field name not specified.");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Internal error. No FieldHandler provided.");
        }
        
        setFieldName(fieldName);
        setFieldType(typeInfo.getFieldType());
        setHandler(handler);
        setTransient(isTransitive);
        setImmutable(typeInfo.isImmutable());
        setRequired(typeInfo.isRequired());
        setMultivalued(typeInfo.getCollectionHandler() != null);
    }

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
    public final void setFieldType(final Class<?> fieldType) {
        _fieldType = fieldType;
    }

    /**
     * @see org.exolab.castor.mapping.FieldDescriptor#getFieldType()
     * {@inheritDoc}
     */
    public final Class<?> getFieldType() {
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
    
    /**
     * Set wether the described field is part of the identity of the class it belongs to.
     * 
     * @param identity <code>true</code> if field is part of the classes identity.
     */
    public final void setIdentity(final boolean identity) {
        _identity = identity;
    }
    
    /**
     * Is the described field is part of the identity of the class it belongs to?
     * 
     * @return <code>true</code> if field is part of the classes identity.
     */
    public final boolean isIdentity() {
        return _identity;
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

    public String getComparator() {
        return _comparator;
    }

    public void setComparator(String comparator) {
        _comparator = comparator;
    }

    public FieldMappingCollectionType getCollection() {
        return _collection;
    }

    public void setCollection(FieldMappingCollectionType collection) {
        _collection = collection;
    }

    public boolean isDirect() {
        return _direct;
    }

    public void setDirect(boolean direct) {
        _direct = direct;
    }

    public String getGetMethod() {
        return _getMethod;
    }

    public void setGetMethod(String getMethod) {
        _getMethod = getMethod;
    }

    public String getSetMethod() {
        return _setMethod;
    }

    public void setSetMethod(String setMethod) {
        _setMethod = setMethod;
    }

    public String getCreateMethod() {
        return _createMethod;
    }

    public void setCreateMethod(String createMethod) {
        _createMethod = createMethod;
    }

    public boolean isLazy() {
        return _lazy;
    }

    public void setLazy(boolean lazy) {
        _lazy = lazy;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getFieldName() + "(" + getFieldType().getName() + ")");
        // TODO[WG]: find a way to emit additional nature-specific information.
//        try {
//            Class natureClass = Class.forName("org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature");
//            if (hasNature(natureClass.getName())) {
//                FieldDescriptorJDONature jdoNature = new FieldDescriptorJDONature(this);
//                buffer.append(jdoNature.getSQLName() == null ? "" : " AS " 
//                    + jdoNature.getSQLName()[0]);
//            }
//        } catch (ClassNotFoundException e) {
//            // ignore
//        }
        return buffer.toString();
    }

}
