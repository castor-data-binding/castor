package org.exolab.javasource;


/**
 * JType sub-class for collections.
 * @since 1.0.4
 */
public class JCollectionType extends JComponentizedType {

    /**
     * Creates an instance of a collection type, of type 'collectionName'.
     * @param collectionName Name of the collection type.
     * @param componentType Component type.
     * @param useJava50 True if Java 5.0 should be used.
     */
    public JCollectionType(final String collectionName, 
            final JType componentType, 
            final boolean useJava50) {
        super(collectionName, componentType, useJava50);
    }

    /**
     * Returns the String representation of this JType, which is simply the name
     * of this type.
     * 
     * @return the String representation of this JType
     */
    public final String toString() {
        if (isUseJava50()) {
          if (getComponentType().isPrimitive()) {
            return getName() + "<" + getComponentType().getWrapperName() + ">";
          } 
          return getName() + "<" + getComponentType().toString() + ">";
        }
        
        return super.toString();
    } //-- toString

}
