/*
 * Copyright 2006 Werner Guttmann
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
package org.exolab.castor.builder.conflictresolution;

import org.exolab.castor.builder.SGStateInfo;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.util.ConsoleDialog;
import org.exolab.javasource.JClass;

/**
 * Strategy pattern for dealing with class name conflicts
 * during source code generation. The various implementations of this stragtegy
 * will implement individual approaches for dealing with such a naming 
 * collision.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 1.1
 */
public interface ClassNameCRStrategy {

    /**
     * Implements a specific strategy for dealing with class name conflicts 
     * @param state The current source generator state.
     * @param newClassInfo The {@link CLassInfo} for the new class to be generated.
     * @param conflict The {@link JClass} instance representing the potential conflict.
     * @return
     */
    SGStateInfo dealWithClassNameConflict(final SGStateInfo state,
            final ClassInfo newClassInfo, final JClass conflict);
    
    /**
     * Implements a specific strategy for dealing with the fact that - for a given file
     * name - an artefact with the same name already exists 
     * @param filename The name of the file to be overwritten.
     * @param promptForOverwrite General builder setting for overwriting files 
     * @return True of the file should be overwritten.
     */
    boolean dealWithFileOverwrite(String filename, boolean promptForOverwrite);    

    /**
     * Returns the name of this strategy.
     * @return The name of this strategy.
     */
    String getName();
    
    /**
     * Sets the {@link ConsoleDialog} instance to use (if required).
     * @param dialog the {@link ConsoleDialog} instance to use (if required).
     */
    void setConsoleDialog(ConsoleDialog dialog);

}
