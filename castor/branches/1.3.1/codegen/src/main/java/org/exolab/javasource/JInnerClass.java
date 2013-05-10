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
 * A representation of the Java Source code for a Java inner class.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6668 $ $Date: 2005-05-08 12:32:06 -0600 (Sun, 08 May 2005) $
 * @since 1.1
 */
public class JInnerClass extends JClass {
    //--------------------------------------------------------------------------

    /**
     * Creates a new JInnerClass with the given name.
     *
     * @param name The name of the JClass to create
     */
    protected JInnerClass(final String name) {
        super(name);
    }

    //--------------------------------------------------------------------------
}
