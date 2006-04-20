package jdo.tc167;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class KindEnum implements Cloneable, Comparable, Serializable {
    //------------------------------------------------------------------------

    /** SerialVersionUID */
    private static final long serialVersionUID = -4999799304409658395L;

    private static final Map            KINDS = new HashMap();

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
        return (KindEnum) KINDS.get(kind);
    }

    public static Iterator iterator() {
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
    public int compareTo(final Object other) {
        return _kind.compareTo(((KindEnum) other)._kind);
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
