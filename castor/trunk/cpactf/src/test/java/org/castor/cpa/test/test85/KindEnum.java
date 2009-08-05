/*
 * Copyright 2008 Udai Gupta
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
public final class KindEnum implements Cloneable, Comparable<KindEnum>, Serializable {
    //------------------------------------------------------------------------

    /** SerialVersionUID */
    private static final long serialVersionUID = -4999799304409658395L;

    private static final Map<String, KindEnum> KINDS = new HashMap<String, KindEnum>();

    public static final KindEnum MOUSE = new KindEnum("Mouse");
    public static final KindEnum KEYBOARD = new KindEnum("Keyboard");
    public static final KindEnum COMPUTER = new KindEnum("Computer");
    public static final KindEnum PRINTER = new KindEnum("Printer");
    public static final KindEnum MONITOR = new KindEnum("Monitor");

    private final String _kind;

    //------------------------------------------------------------------------

    private KindEnum(final String kind) {
        _kind = kind;
        KINDS.put(kind, this);
    }

    public static KindEnum valueOf(final String kind) {
        return KINDS.get(kind);
    }

    public static Iterator<KindEnum> iterator() {
        return KINDS.values().iterator();
    }

    //------------------------------------------------------------------------

    /**
     * Returns the String representation of this kind.
     *
     * @return String representation of this kind.
     */
    public String toString() {
        return _kind;
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
        return _kind.hashCode();
    }

    /**
     * Compares {@link #_kind} against {@link #_kind} of the specified
     * object. So this method is inconsistent with {@link #equals}.
     *
     * @param other Object to be compared with this instance.
     * @return A negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     */
    public int compareTo(final KindEnum other) {
        return _kind.compareTo(other._kind);
    }

    /**
     * Called during deserialization.
     *
     * @return The existing instance of the enum. <br>So you can use '=='
     *         like 'equals' even if you use a deserialized Enum.
     */
    protected Object readResolve() {
        return KINDS.get(_kind);
    }

    //------------------------------------------------------------------------
}
