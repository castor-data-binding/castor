package org.exolab.castor.xml;

import org.exolab.castor.mapping.FieldDescriptor;

/**
 * Internal class used to save state for reference resolving
**/
class ReferenceInfo {
    
    /**
     * The ID(REF) value of the target object instance
     */
    private String id = null;
    /**
     * The target object referenced by this IDREF instance
     */
    private Object target = null;
    /**
     * XML Field descriptor referenced by this IDREF instance
     */
    private XMLFieldDescriptor descriptor = null;
    /**
     * The 'next' ReferenceInfo instance
     */
    private ReferenceInfo next = null;

    public ReferenceInfo(final String id, final Object target, final XMLFieldDescriptor descriptor)
    {
        this.id = id;
        this.target = target;
        this.descriptor = descriptor;
    }

    /**
     * Sets a refrence to the 'next' ReferenceInfo instance
     * @param info The 'next' ReferenceInfo instance.
     */
    public void setNext(ReferenceInfo info) {
        this.next = info;
    }

    /**
     * Returns the field descriptor referenced by this IDREF instance
     * @return the field descriptor referenced by this IDREF instance
     */
    public FieldDescriptor getDescriptor() {
        return this.descriptor;
    }

    /**
     * Returns the target object referenced by this IDREF instance
     * @return the target object referenced by this IDREF instance
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Returns the next 'ReferenceInfo' instance
     * @return the next 'ReferenceInfo' instance
     */
    public ReferenceInfo getNext() {
        return this.next;
    }

    /**
     * Returns the ID value of the target object instance
     * @return the ID value of the target object instance
     */
    public String getId() {
        return this.id;
    }
}

