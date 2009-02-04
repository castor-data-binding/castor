/*
 * Copyright 2009 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.javasource;

/**
 * A class which holds information about a constant. Modeled closely after the
 * Java Reflection API. This class is part of package which is used to create
 * source code in memory.
 *
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3
 */
public final class JConstant extends AbstractJField {

    /**
     * Creates a new JConstant.
     * 
     * @param type JType of this new constant.
     * @param name Name of this new constant.
     */
    public JConstant(final JType type, final String name) {
        this(type, name, false);
    }


    /**
     * Creates a new JConstant.
     * 
     * @param type JType of this new constant.
     * @param name Name of this new constant.
     * @param makePrivate True if constant definition should have private visibility.
     */
    public JConstant(final JType type, final String name, final boolean makePrivate) {
        super(type, name);
        
        JModifiers modifiers = getModifiers();
        if (makePrivate) {
            modifiers.makePrivate();
        } else {
            modifiers.makePublic();
        }
        
        modifiers.setFinal(true);
        modifiers.setStatic(true);
        
        JDocComment comment = new JDocComment();
        comment.appendComment("Constant " + name + ".");
        setComment(comment);
    }

}
