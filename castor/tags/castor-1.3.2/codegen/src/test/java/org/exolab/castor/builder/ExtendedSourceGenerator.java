/*
 * Copyright 2008 Luaks Lang
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
package org.exolab.castor.builder;

/**
 * Child class of {@link SourceGenerator} that allows access to the 
 * {@link SGStateInfo} class(es) as maintained by the source generator.
 * 
 * @author Sebastian Gabmeyer
 * @since 1.2.1
 * 
 */
public class ExtendedSourceGenerator extends SourceGenerator {

    /**
     * Get the state of the Source Generator.
     * 
     * @return the Source Generator's State Info object.
     * @see SGStateInfo
     */
    public final SGStateInfo getSGStateInfo() {
        return _sInfo;
    }

    /**
     * Get the current state of the {@link SourceFactory} used by this
     * SourceGenerator.
     * 
     * @return the current state of the Source Factory.
     * @see SourceFactory
     */
    public final FactoryState getCurrentFactoryState() {
        return _sInfo.getCurrentFactoryState();
    }
}
