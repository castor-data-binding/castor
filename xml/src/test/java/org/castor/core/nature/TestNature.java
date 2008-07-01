package org.castor.core.nature;

public class TestNature extends BaseNature {

    /**
     * Returns the id of the Nature. Implementation returns the fully qualified
     * class name.
     * 
     * @return the id.
     * @see org.exolab.castor.builder.info.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }
    
    /**
     * Creates an instance of this class.
     * @param holder The {@link PropertyHolder} instance upon this 'view' should 
     *     be applied.
     */
    protected TestNature(PropertyHolder holder) {
        super(holder);
    }
    
}
