/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.mapping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The access mode for a class. This object is used by class
 * descriptors to specify the access mode for a class.
 * <p>
 * In persistent storage each class is defined as having one of three
 * access modes:
 * <ul>
 * <li>Read only
 * <li>Shared (aka optimistic locking)
 * <li>Exclusive (aka pessimistic locking)
 * <li>DbLocked (database lock)
 * </ul>
 * Transactions typically access objects based on the specified access
 * mode. A transaction may be requested to access any object as read
 * only or exclusive, but may not access exclusive objects as shared.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public class AccessMode implements Cloneable, Comparable, Serializable {
    //-------------------------------------------------------------------------
    
    /** SerialVersionUID */
    private static final long serialVersionUID = -7113303922354626951L;

    private static final Map    IDS = new HashMap(7);
    private static final Map    NAMES = new HashMap(7);
    
    //-------------------------------------------------------------------------

    /**
     * Read only access. Objects can be read but are not made
     * persistent and changes to objects are not reflected in
     * persistent storage.
     */
    public static final AccessMode ReadOnly 
        = new AccessMode((short) 0, "read-only");

    /**
     * Shared access. Objects can be read by multiple concurrent
     * transactions. Equivalent to optimistic locking.
     */
    public static final AccessMode Shared 
        = new AccessMode((short) 1, "shared");

    /**
     * Exclusive access. Objects can be access by a single transaction
     * at any given time. Equivalent to pessimistic locking.
     */
    public static final AccessMode Exclusive 
        = new AccessMode((short) 2, "exclusive");

    /**
     * DbLocked access. Objects can be access by a single transaction
     * at any given time, and a lock is acquired in the database.
     */
    public static final AccessMode DbLocked 
        = new AccessMode((short) 3, "db-locked");

    //-------------------------------------------------------------------------

    /**
     * Returns the access mode from the name. If <tt>accessMode</tt>
     * is null, return the default access mode ({@link #Shared}).
     * Otherwise returns the named access mode.
     *
     * @param accessMode The access mode name
     * @return The access mode
     */
    public static AccessMode valueOf(final String accessMode) {
        AccessMode mode = (AccessMode) NAMES.get(accessMode);
        if (mode != null) {
            return mode;
        }
        throw new IllegalArgumentException("Unrecognized access mode");
    }

    public static AccessMode valueOf(final short accessMode) {
        AccessMode mode = (AccessMode) IDS.get(new Short(accessMode));
        if (mode != null) {
            return mode;
        }
        throw new IllegalArgumentException("Unrecognized access mode");
    }

    //-------------------------------------------------------------------------

    /**
     * The id of this access mode as originally used at Database.load() and
     * Query.execute().
     */
    private short _id;

    /**
     * The name of this access mode as it would appear in a mapping file.
     */
    private String _name;

    //-------------------------------------------------------------------------

    private AccessMode(final short id, final String name) {
        _id = id;
        _name = name;

        IDS.put(new Short(id), this);
        NAMES.put(name, this);
    }

    public short getId() { return _id; }
    public String getName() { return _name; }

    //-------------------------------------------------------------------------

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
        return _id;
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
        return compareTo((AccessMode) other);
    }

    public int compareTo(final AccessMode other) {
        return _id - other._id;
    }

    /**
     * Called during deserialization.
     *
     * @return The existing instance of the enum. <br>So you can use '=='
     *         like 'equals' even if you use a deserialized Enum.
     */
    protected Object readResolve() {
        return NAMES.get(_name);
    }
    
    //-------------------------------------------------------------------------
}
