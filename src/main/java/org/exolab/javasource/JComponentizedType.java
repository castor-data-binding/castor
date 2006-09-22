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
     * Creates an instance of a componentized type, of type 'name'.
     * @param name Type name for this componentized type.
     * @param componentType Component type.
     */
    public JComponentizedType(final String name, final JType componentType) {
        super (name);
        setComponentType(componentType);
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


}
