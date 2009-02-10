/*
 * Copyright 2006 Werner Guttmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.javasource;

/**
 * JType sub-class for collections.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttman</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0.4
 */
public final class JCollectionType extends JComponentizedType {
    //--------------------------------------------------------------------------

    /** Name of the actual collection instance to be used, e.g. java.util.ArrayList. */
    private String _instanceName;

    /**
     * Indicates whether (for generics) '? extends' should be emitted.
     */
    private boolean _useExtends;
    //--------------------------------------------------------------------------

    /**
     * Creates an instance of a collection type, of type 'collectionName'.
     * 
     * @param typeName Name of the collection type interface.
     * @param componentType Component type.
     * @param useJava50 True if Java 5.0 should be used.
     */
    public JCollectionType(final String typeName, 
            final JType componentType, 
            final boolean useJava50) {
        super(typeName, componentType, useJava50);
        _useExtends = false;
    }
    
    /**
     * Creates an instance of a collection type, of type 'collectionName'.
     * 
     * @param typeName Name of the collection type interface.
     * @param componentType Component type.
     * @param useJava50 True if Java 5.0 should be used.
     * @param useExtends True if '? extends' should be emitted for generics (Java 5.0 ff only).
     */
    public JCollectionType(final String typeName, 
            final JType componentType, 
            final boolean useJava50,
            final boolean useExtends) {
        super(typeName, componentType, useJava50);
        _useExtends = useExtends;
    }
    
    /**
     * Creates an instance of a collection type, of type 'collectionName'.
     * 
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

    //--------------------------------------------------------------------------

    /**
     * Returns the instance name of this collection type.
     * 
     * @return The instance name of this collection type.
     */
    public String getInstanceName() {
        if (_instanceName != null) {
            if (isUseJava50()) {
                if (getComponentType().isPrimitive()) {
                    JPrimitiveType primitive = (JPrimitiveType) getComponentType();
                    return _instanceName + "<" + primitive.getWrapperName() + ">";
                }
                if (_useExtends) {
                    return _instanceName + "<? extends " + getComponentType().toString() + ">";
                }
                return _instanceName + "<" + getComponentType().toString() + ">";
            }

            return _instanceName;
        }
        
        return toString();
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Returns the String representation of this JType.
     */
    public String toString() {
        if (isUseJava50()) {
            if (getComponentType().isPrimitive()) {
                JPrimitiveType primitive = (JPrimitiveType) getComponentType();
                return getName() + "<" + primitive.getWrapperName() + ">";
            } 
            if (_useExtends) {
                return getName() + "<? extends " + getComponentType().toString() + ">";
            }
            return getName() + "<" + getComponentType().toString() + ">";
        }
        
        return getName();
    }

    //--------------------------------------------------------------------------
}
