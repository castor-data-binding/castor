/*
 * Copyright 2005 Werner Guttmann
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
 *
 * $Id$
 */
package org.castor.persist.proxy;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * <tt>Lazy</tt> is a place holder interface to indicate that a data object or
 * a Collection in a data object is an Lazy instance. In other words, instances
 * of the objects are not retrieved from the persistence store at load time, but
 * will be materialized when the instance is actually needed/accessed.
 * 
 * <p>
 * Implementations of this class need to provide a writeReplace() methor
 * required during Java serialization.
 * </p>
 * 
 * <p>
 * 
 * @author <a href="mailto:werner DOT guttmann @ gmx DOT net">Werner Guttmann
 *         </a>
 * 
 */
public interface LazyCGLIB extends Serializable {

    /**
     * Implementation of writeReplace specific to lazy loading.
     * 
     * @return The real object.
     * @throws ObjectStreamException If the replace activity failed.
     */
    Object writeReplace() throws ObjectStreamException;

    /**
     * Returns the Class instance for the class to be intercepted.
     * 
     * @return Class instance for the intercepted class.
     */
    Class interceptedClass();

    /**
     * Returns the identity of the object (class) intercepted.
     * 
     * @return identity of the object (class) intercepted.
     */
    Object interceptedIdentity();
}
