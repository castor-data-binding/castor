/*
 * Copyright 2010 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test85;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Ignore;

@Ignore
public final class KindEnumStyleClassSameName
implements Cloneable, Comparable<KindEnumStyleClassSameName>, Serializable {
    //------------------------------------------------------------------------

    /** SerialVersionUID */
    private static final long serialVersionUID = -4999799304409658395L;

    private static final Map<String, KindEnumStyleClassSameName> KINDS =
    	new HashMap<String, KindEnumStyleClassSameName>();

    public static final KindEnumStyleClassSameName MOUSE =
    	new KindEnumStyleClassSameName("MOUSE");
    public static final KindEnumStyleClassSameName KEYBOARD =
    	new KindEnumStyleClassSameName("KEYBOARD");
    public static final KindEnumStyleClassSameName COMPUTER =
    	new KindEnumStyleClassSameName("COMPUTER");
    public static final KindEnumStyleClassSameName PRINTER =
    	new KindEnumStyleClassSameName("PRINTER");
    public static final KindEnumStyleClassSameName MONITOR =
    	new KindEnumStyleClassSameName("MONITOR");

    private final String _name;

    //------------------------------------------------------------------------

    private KindEnumStyleClassSameName(final String name) {
        _name = name;
        KINDS.put(name, this);
    }

    public static KindEnumStyleClassSameName valueOf(final String name) {
        return KINDS.get(name);
    }

    public static Iterator<KindEnumStyleClassSameName> iterator() {
        return KINDS.values().iterator();
    }

    //------------------------------------------------------------------------

    /**
     * Returns the String representation of this kind.
     *
     * @return String representation of this kind.
     */
    public String name() {
        return _name;
    }

    /**
     * Returns the String representation of this kind.
     *
     * @return String representation of this kind.
     */
    public String toString() {
        return _name;
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
        return _name.hashCode();
    }

    /**
     * Compares {@link #_name} against {@link #_name} of the specified
     * object. So this method is inconsistent with {@link #equals}.
     *
     * @param other Object to be compared with this instance.
     * @return A negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     */
    public int compareTo(final KindEnumStyleClassSameName other) {
        return _name.compareTo(other._name);
    }

    /**
     * Called during deserialization.
     *
     * @return The existing instance of the enum. <br>So you can use '=='
     *         like 'equals' even if you use a deserialized Enum.
     */
    protected Object readResolve() {
        return KINDS.get(_name);
    }

    //------------------------------------------------------------------------
}
