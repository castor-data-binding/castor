/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.mapping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class BindingType implements Cloneable, Comparable, Serializable {
    //------------------------------------------------------------------------

    /** SerialVersionUID */
    private static final long serialVersionUID = -2116844968191798202L;

    private static final Map        TYPES = new HashMap();

    public static final BindingType JDO = new BindingType("jdo");
    public static final BindingType XML = new BindingType("xml");

    private final String _type;

    //------------------------------------------------------------------------

    private BindingType(final String kind) {
        _type = kind;
        TYPES.put(kind, this);
    }

    public static BindingType valueOf(final String kind) {
        return (BindingType) TYPES.get(kind);
    }

    public static Iterator iterator() {
        return TYPES.values().iterator();
    }

    //------------------------------------------------------------------------

    /**
     * Returns the String representation of this kind.
     *
     * @return String representation of this kind.
     */
    public String toString() {
        return _type;
    }

    /**
     * Clone only returns the one and only instance of this kind.
     *
     * @return The original instance.
     */
    public Object clone() {
        return this;
    }

    /**
     * Returns if the specified object and this are one and the same instance.
     *
     * @param other Object to be compared with this instance.
     * @return <code>true</code> if other equals this else <code>false</code>.
     */
    public boolean equals(final Object other) {
        return (this == other);
    }

    /**
     * Returns the hash code of this object.
     *
     * @return Hash code of this object.
     */
    public int hashCode() {
        return _type.hashCode();
    }

    /**
     * Compares {@link #_type} against {@link #_type} of the specified
     * object. So this method is inconsistent with {@link #equals}.
     *
     * @param other Object to be compared with this instance.
     * @return A negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     */
    public int compareTo(final Object other) {
        return _type.compareTo(((BindingType) other)._type);
    }

    /**
     * Called during deserialization.
     *
     * @return The existing instance of the enum. <br>So you can use '=='
     *         like 'equals' even if you use a deserialized Enum.
     */
    protected Object readResolve() {
        return TYPES.get(_type);
    }

    //------------------------------------------------------------------------
}
