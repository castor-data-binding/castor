package org.exolab.castor.xml.descriptors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.xml.UnmarshalState;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;

public abstract class BaseDescriptor implements XMLClassDescriptor {
    
    /**
     * Map holding the properties set and read by Natures.
     */
    private Map _properties = new HashMap();
    
    /**
     * Map holding the available natures.
     */
    private Set _natures = new HashSet();

    /**
     * Returns true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given. An XMLClassDescriptor can accept
     * a field if it contains a descriptor that matches the given name and if
     * the given object can hold this field (i.e a value is not already set for
     * this field).
     * <p>
     * This is mainly used for container object (that can contain other
     * objects), in this particular case the implementation returns false.
     *
     * @param name the name of the field to check
     * @param namespace the namespace of the element. This may be null. Note: A
     *        null namespace is not the same as the default namespace unless the
     *        default namespace is also null.
     * @param object the object represented by this XMLCLassDescriptor
     * @return true if the given object represented by this XMLClassDescriptor
     *         can accept a member whose name is given.
     */
    public boolean canAccept(String name, String namespace, Object object) {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#
     *      checkDescriptorForCorrectOrderWithinSequence(org.exolab.castor.xml.XMLFieldDescriptor,
     *      org.exolab.castor.xml.UnmarshalState, java.lang.String)
     */
    public void checkDescriptorForCorrectOrderWithinSequence(
            XMLFieldDescriptor elementDescriptor, UnmarshalState parentState,
            String xmlName) throws ValidationException {
        // no implementation
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#isChoice()
     */
    public boolean isChoice() {
        return false;
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


}
