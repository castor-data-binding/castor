package org.exolab.javasource;

/**
 * JType sub-class for Arrays.
 * @since 1.0.4
 */
public class JArrayType extends JComponentizedType {

    /**
     * Creates an instance of a array type, of type 'name'.
     * @param componentType Component type.
     * @param useJava50 True if Java 5.0 should be generated.
     */
    public JArrayType(final JType componentType, final boolean useJava50) {
        super(componentType.getName(), componentType, useJava50);
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
