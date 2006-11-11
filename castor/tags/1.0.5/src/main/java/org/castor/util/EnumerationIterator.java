/*
 * Copyright 2006 Ralf Joachim
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
package org.castor.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Convert an enumeration to an iterator.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0.4
 */
public final class EnumerationIterator implements Iterator {
    /** Enumeration to iterate over. */
    private final Enumeration _enumeration;
    
    /**
     * Construct an iterator for given enumeration.
     * 
     * @param enumeration Enumeration to iterate over.
     */
    public EnumerationIterator(final Enumeration enumeration) {
        _enumeration = enumeration;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
        return _enumeration.hasMoreElements();
    }

    /**
     * {@inheritDoc}
     */
    public Object next() {
        return _enumeration.nextElement();
    }

    /**
     * {@inheritDoc}
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
