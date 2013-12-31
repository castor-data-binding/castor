/*
 * Copyright 2011, Werner Guttmann
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
package org.exolab.castor.mapping;

/**
 * A {@link FieldHandler} that allows shallow/deep cloning of its state.
 *
 * @author Werner Guttmann
 * @version $Revision:  $
 * @see FieldHandler
 * since 1.3.2
 * @deprecated Use {@link ClonableFieldHandlerMarker} instead.
 */
public interface ClonableFieldHandler<T> extends FieldHandler<T> {

    /**
     * Entry point for shallow/deep 'cloning' of {@link FieldHandler} instances.
     * @return the 'cloned' {@link FieldHandler} instance.
     */
    public FieldHandler<T> copyInstance();

}
