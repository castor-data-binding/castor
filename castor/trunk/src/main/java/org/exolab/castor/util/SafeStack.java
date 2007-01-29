/*
 * Copyright 2006 Edward Kuns
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
 * $Id: SafeStack.java 0000 2006-12-21 22:00:00Z ekuns $
 */
package org.exolab.castor.util;

import java.util.Stack;

/**
 * The {@link Stack#search} method of {@link Stack} can throw a
 * ClassCastException if the items on the stack are not all the same type. We
 * override that method so it compares on identity and not using
 * <code>equals()</code>.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: $
 */
public class SafeStack extends Stack {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4964881847051572321L;

    /**
     * {@inheritDoc}
     * Searches for the given Object in the stack and returns its position
     * relative to the top of the Stack (ie the number of calls to #pop()
     * before the object is returned by #pop())
     */
    public int search(Object object) {
        for (int i = 0; i < this.size(); i++) {
            if (object == this.get(i)) {
                return i + 1;
            }
        }
        return -1;
    }

}
