package org.exolab.javasource;

/**
 * JType sub-class for Arrays.
 * @since 1.0.4
 */
public class JArrayType extends JComponentizedType {

    /**
     * Creates an instance of a array type, of type 'name'.
     * @param componentType Component type.
     */
    public JArrayType(final JType componentType) {
        super(componentType.getName(), componentType);
    }

    /**
     * Returns the String representation of this JType, which is simply the name
     * of this type.
     * 
     * @return the String representation of this JType
     */
    public final String toString() {
        return getComponentType().toString() + "[]";
    } //-- toString

}
