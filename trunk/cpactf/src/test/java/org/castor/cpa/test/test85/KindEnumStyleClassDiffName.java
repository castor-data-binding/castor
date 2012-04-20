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
public final class KindEnumStyleClassDiffName
implements Cloneable, Comparable<KindEnumStyleClassDiffName>, Serializable {
    //------------------------------------------------------------------------

    /** SerialVersionUID */
    private static final long serialVersionUID = -4999799304409658395L;

    private static final Map<String, KindEnumStyleClassDiffName> KINDS =
        new HashMap<String, KindEnumStyleClassDiffName>();

    public static final KindEnumStyleClassDiffName MOUSE =
        new KindEnumStyleClassDiffName("MOUSE", "Maus");
    public static final KindEnumStyleClassDiffName KEYBOARD =
        new KindEnumStyleClassDiffName("KEYBOARD", "Tastatur");
    public static final KindEnumStyleClassDiffName COMPUTER =
        new KindEnumStyleClassDiffName("COMPUTER", "Computer");
    public static final KindEnumStyleClassDiffName PRINTER =
        new KindEnumStyleClassDiffName("PRINTER", "Drucker");
    public static final KindEnumStyleClassDiffName MONITOR =
        new KindEnumStyleClassDiffName("MONITOR", "Bildschirm");

    private final String _name;
    private final String _translation;

    //------------------------------------------------------------------------

    private KindEnumStyleClassDiffName(final String name, final String translation) {
        _name = name;
        _translation = translation;
        KINDS.put(name, this);
    }

    public static KindEnumStyleClassDiffName valueOf(final String name) {
        return KINDS.get(name);
    }

    public static Iterator<KindEnumStyleClassDiffName> iterator() {
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
        return _translation;
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
    public int compareTo(final KindEnumStyleClassDiffName other) {
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
