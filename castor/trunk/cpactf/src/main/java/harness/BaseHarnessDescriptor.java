package harness;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.UnmarshalState;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;

/**
 * Base class for all descriptor classes of the JDO test harness.
 * 
 * @since 1.2
 */
public abstract class BaseHarnessDescriptor implements XMLClassDescriptor {

    /**
     * Set of {@link XMLFieldDescriptor}s for all members that should be marshalled 
     * as XML elements.
     */
    protected org.exolab.castor.xml.XMLFieldDescriptor[] _elementDescriptors;

    /**
     * Set of {@link XMLFieldDescriptor}s for all members that should be marshalled 
     * as XML attributes.
     */
    protected org.exolab.castor.xml.XMLFieldDescriptor[] _attributeDescriptors;

    /**
     * Namespace prefix.
     */
    protected java.lang.String _nsPrefix;

    /**
     * Namespace URI. 
     */
    protected java.lang.String _nsURI;

    /**
     * XML name for the class being described.
     */
    protected java.lang.String _xmlName;

    /**
     * {@link XMLFieldDescriptor} for the member that should be marshalled as text content.
     */
    protected org.exolab.castor.xml.util.XMLFieldDescriptorImpl _contentDescriptor;

    /**
     * TODO.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;

    /**
     * Map holding the properties set and read by Natures.
     */
    private Map _properties = new HashMap();
    
    /**
     * Map holding the available natures.
     */
    private Set _natures = new HashSet();

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.XMLClassDescriptor#getAttributeDescriptors()
     */
    public org.exolab.castor.xml.XMLFieldDescriptor[] getAttributeDescriptors() {
        return _attributeDescriptors;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#getElementDescriptors()
     */
    public org.exolab.castor.xml.XMLFieldDescriptor[] getElementDescriptors() {
        return _elementDescriptors;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#getNameSpaceURI()
     */
    public java.lang.String getNameSpaceURI() {
        return _nsURI;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#getXMLName()
     */
    public java.lang.String getXMLName() {
        return _xmlName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#getNameSpacePrefix()
     */
    public java.lang.String getNameSpacePrefix() {
        return _nsPrefix;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#checkDescriptorForCorrectOrderWithinSequence(org.exolab.castor.xml.XMLFieldDescriptor, org.exolab.castor.xml.UnmarshalState, java.lang.String)
     */
    public void checkDescriptorForCorrectOrderWithinSequence(
            final XMLFieldDescriptor elementDescriptor, 
            final UnmarshalState parentState,
            final String xmlName) throws ValidationException {
        // nothing to check
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.XMLClassDescriptor#getContentDescriptor()
     */
    public org.exolab.castor.xml.XMLFieldDescriptor getContentDescriptor() {
        return _contentDescriptor;
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.mapping.ClassDescriptor#getIdentity()
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return _identity;
    }

    /**
     * Returns the current {@link AccessMode} instance.
     * @return The current access mode.
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.mapping.ClassDescriptor#getExtends()
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.XMLClassDescriptor#getValidator()
     */
    public TypeValidator getValidator() {
        return null;
    }

    /**
     * <p>Returns true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given.
     * An XMLClassDescriptor can accept a field if it contains a descriptor that matches
     * the given name and if the given object can hold this field (i.e a value is not
     * already set for this field).
     * <p>This is mainly used for container object (that can contains other object), in 
     * this particular case the implementation will return null.
     * @param name the xml name of the field to check
     * @param namespace the namespace URI
     * @param object the object represented by this XMLCLassDescriptor
     * @return true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given.
     */
    public boolean canAccept(final String name, final String namespace, final Object object) {
         return false;
    }

    /**
     * TODO.
     */
    public void resetElementCount() {
        // nothing to reset
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.mapping.ClassDescriptor#getFields()
     */
    public org.exolab.castor.mapping.FieldDescriptor[] getFields() {
        int size = _attributeDescriptors.length + _elementDescriptors.length;
        if (_contentDescriptor != null) {
            ++size;
        }
        
        FieldDescriptor[] fields = new FieldDescriptor[size];
        int c = 0;
        for (int i = 0; i < _attributeDescriptors.length; i++) {
            fields[c++] = _attributeDescriptors[i];
        }
        
        for (int i = 0; i < _elementDescriptors.length; i++) {
            fields[c++] = _elementDescriptors[i];
        }
        
        if (_contentDescriptor != null) {
            fields[c] = _contentDescriptor;
        }
        
        return fields;
    } //-- org.exolab.castor.mapping.FieldDescriptor[] getFields() 

    /**
     * Returns the XML field descriptor matching the given
     * xml name and nodeType. If NodeType is null, then
     * either an AttributeDescriptor, or ElementDescriptor
     * may be returned. Null is returned if no matching
     * descriptor is available.
     *
     * @param name the xml name to match against
     * @param namespace the xml namspace to match against
     * @param nodeType the NodeType to match against, or null if
     * the node type is not known.
     * @return the matching descriptor, or null if no matching
     * descriptor is available.
     *
    **/
    public XMLFieldDescriptor getFieldDescriptor(final String name,
            final String namespace, final NodeType nodeType) {
        
        boolean wild = (nodeType == null);
        
        if (wild || (nodeType == NodeType.Element)) {
            XMLFieldDescriptor desc = null;
            for (int i = 0; i < _elementDescriptors.length; i++) {
                desc = _elementDescriptors[i];
                if (desc == null) {
                    continue;
                }
                if (desc.matches(name, namespace)) {
                    return desc;
                }
            }
        }
        
        if (wild || (nodeType == NodeType.Attribute)) {
            XMLFieldDescriptor desc = null;
            for (int i = 0; i < _attributeDescriptors.length; i++) {
                desc = _attributeDescriptors[i];
                if (desc == null) {
                    continue;
                }
                if (desc.matches(name, namespace)) {
                    return desc;
                }
            }
        }
        
        return null;
        
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

