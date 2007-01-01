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
 * JType sub-class for Arrays.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttman</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
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
     * {@inheritDoc}
     */
    public final boolean isArray() {
        return true;
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
