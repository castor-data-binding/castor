/*
 * Copyright 2006 Ralf Joachim
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
 * JType sub-class for java primitives.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class JPrimitiveType extends JType {
    //--------------------------------------------------------------------------

    /** Only populated for primitive types and indicates the wrapper Object class
     *  name for this primitive type. */
    private String _wrapperName;

    //--------------------------------------------------------------------------

    /**
     * Creates a new JPrimitiveType for a primitive with the given name and wrapper name.
     * This constructor is protected so it can only be used by the primitives defined at JType.
     *
     * @param name The name of the type.
     * @param wrapperName The name of the wrapper Object type for this primitive type.
     */
    protected JPrimitiveType(final String name, final String wrapperName) {
        super(name);
        
        _wrapperName = wrapperName;
    }

    //--------------------------------------------------------------------------

    /**
     * Return the name of the wrapper object for a primitive type, null for
     * non-primitive types.
     *
     * @return The name of the wrapper object for a primitive type, null for
     *         non-primitive types.
     */
    public String getWrapperName() {
        return _wrapperName;
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Returns the String representation of this JType.
     */
    public String toString() {
        return getName();
    }

    //--------------------------------------------------------------------------
}
