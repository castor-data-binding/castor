package org.exolab.javasource;


/**
 * JType sub-class for collections.
 * @since 1.0.4
 */
public class JCollectionType extends JComponentizedType {

    /**
     * Name of the actual collection instance to be used, 
     * e.g. java.util.ArrayList.
     */
    private String _instanceName;

    /**
     * Creates an instance of a collection type, of type 'collectionName'.
     * @param typeName Name of the collection type interface.
     * @param componentType Component type.
     * @param useJava50 True if Java 5.0 should be used.
     */
    public JCollectionType(final String typeName, 
            final JType componentType, 
            final boolean useJava50) {
        super(typeName, componentType, useJava50);
    }
    
    /**
     * Creates an instance of a collection type, of type 'collectionName'.
     * @param typeName Name of the collection type interface.
     * @param instanceName Name of the actual collection type instance.
     * @param componentType Component type.
     * @param useJava50 True if Java 5.0 should be used.
     */
    public JCollectionType(final String typeName, 
            final String instanceName,
            final JType componentType, 
            final boolean useJava50) {
        super(typeName, componentType, useJava50);
        _instanceName = instanceName;
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

    /**
     * Returns the instance name of this collection type.
     * @return the instance name of this collection type.
     */
    public final String getInstanceName() {
        if (_instanceName != null) {
            if (isUseJava50()) {
                if (getComponentType().isPrimitive()) {
                    return _instanceName + "<"
                            + getComponentType().getWrapperName() + ">";
                }
                return _instanceName + "<" + getComponentType().toString()
                        + ">";
            }

            return _instanceName;
        }
        
        return toString();
    } //-- getInstanceName

}
