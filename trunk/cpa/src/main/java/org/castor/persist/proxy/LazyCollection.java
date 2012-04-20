/*
 * Copyright 2009 Thomas Yip, Ralf Joachim
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

import java.util.Collection;
import java.util.List;

import org.exolab.castor.persist.TxSynchronizable;
import org.exolab.castor.persist.spi.Identity;

/**
 * <tt>Lazy</tt> is a place holder interface to indicate that a data object or
 * a Collection in a data object is an Lazy instance. In other word, 
 * values of the object is not stored in the object and the persistence 
 * storage will be accessed when the values are needed.
 * <p>
 * IllegalStateException maybe thrown if the object is accessed out
 * of a transaction context.
 * 
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public interface LazyCollection<T> extends Collection<T>, TxSynchronizable {
    List<Identity> getIdsList();
    
    List<Identity> getRemovedIdsList();

    List<T> getAddedEntitiesList();
}
