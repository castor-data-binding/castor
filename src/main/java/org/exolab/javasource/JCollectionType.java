package org.exolab.javasource;

import org.exolab.castor.builder.BuilderConfiguration;

/**
 * JType sub-class for collections.
 * @since 1.0.4
 */
public class JCollectionType extends JComponentizedType {

    /**
     * Creates an instance of a collection type, of type 'collectionName'.
     * @param collectionName Name of the collection type.
     * @param componentType Component type.
     */
    public JCollectionType(final String collectionName, final JType componentType) {
        super(collectionName, componentType);
    }

    /**
     * Returns the String representation of this JType, which is simply the name
     * of this type.
     * 
     * @return the String representation of this JType
     */
    public final String toString() {
        if (BuilderConfiguration.createInstance().useJava50()) {
          if (getComponentType().isPrimitive()) {
            return getName() + "<" + getComponentType().getWrapperName() + ">";
          } 
          return getName() + "<" + getComponentType().toString() + ">";
        }
        
        return super.toString();
    } //-- toString

}
