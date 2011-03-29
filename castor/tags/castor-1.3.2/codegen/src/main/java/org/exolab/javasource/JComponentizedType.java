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
 * JType sub-class for componentized types, such as array as collections.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttman</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0.4
 */
public class JComponentizedType extends JType {
    //--------------------------------------------------------------------------

    /** Indicates the data type contained in this collection. */
    private JType _componentType;

    /** Indicates whether Java 5.0 compliant code is required. */
    private boolean _useJava50;

    //--------------------------------------------------------------------------

    /**
     * Creates an instance of a componentized type, of type 'name'.
     * 
     * @param name Type name for this componentized type.
     * @param componentType Component type.
     * @param useJava50 True if Java 5.0 should be used.
     */
    protected JComponentizedType(final String name, final JType componentType,
            final boolean useJava50) {
        super (name);

        _componentType = componentType;
        _useJava50 = useJava50;
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the component type.
     * 
     * @return The component type.
     */
    public final JType getComponentType() {
        return _componentType;
    }

    /**
     * Indicates whether Java 5.0 is used.
     * 
     * @return True if Java 5.0 is used.
     */
    public final boolean isUseJava50() {
        return _useJava50;
    }

    //--------------------------------------------------------------------------
}
