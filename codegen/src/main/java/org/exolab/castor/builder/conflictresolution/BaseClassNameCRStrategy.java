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

import org.exolab.castor.builder.SingleClassGenerator;

/**
 * Abtract base class for all {@link ClassNameCRStrategy} implementations.
 */
public abstract class BaseClassNameCRStrategy implements ClassNameCRStrategy {

    /**
     * The calling {@link SingleClassGenerator} instance.
     */
    private SingleClassGenerator _generator;

    /**
     * Sets the current caller of this strategy implementation. This can be used
     * to change the state of the calling object as a result of a strategy
     * implementation.
     *
     * @param generator the current caller of this strategy implementation.
     * @see org.exolab.castor.builder.conflictresolution.ClassNameCRStrategy
     *      #setSingleClassGenerator(org.exolab.castor.builder.SingleClassGenerator)
     */
    public void setSingleClassGenerator(final SingleClassGenerator generator) {
        this._generator = generator;
    }

    /**
     * Returns the current caller of this strategy implementation. This can be
     * used to change the state of the calling object as a result of a strategy
     * implementation.
     *
     * @return the current caller of this strategy implementation.
     */
    protected SingleClassGenerator getSingleClassGenerator() {
        return this._generator;
    }

}
