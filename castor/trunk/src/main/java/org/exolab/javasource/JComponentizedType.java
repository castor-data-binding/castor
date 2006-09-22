package org.exolab.javasource;

/**
 * JType sub-class for componentized types, such as array as collections.
 * @since 1.0.4
 */
public class JComponentizedType extends JType {

    /**
     * Indicates the data type contained in this collection.
     */
    private JType _componentType;
    
    /**
     * Indicates whether Java 5.0 compliant code is requird 
     */
    private boolean _useJava50;
    
    /**
     * Creates an instance of a componentized type, of type 'name'.
     * @param name Type name for this componentized type.
     * @param componentType Component type.
     * @param useJava50 true if Java 5.0 should be used.
     */
    public JComponentizedType(final String name, final JType componentType, 
            final boolean useJava50) {
        super (name);
        setComponentType(componentType);
        this._useJava50 = useJava50;
    }

    /**
     * Sets the component type.
     * @param componentType The component type.
     */
    private void setComponentType(final JType componentType) {
        this._componentType = componentType;
    }
    
    /**
     * Returns the component type.
     * @return the component type.
     */
    public JType getComponentType() {
        return this._componentType;
    }

    /**
     * Indicates whether Java 5.0 is used.
     * @return True if Java 5.0 is used
     */
    public boolean isUseJava50() {
        return this._useJava50;
    }


}
