/*
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
package org.castor.xmlctf;

import java.lang.reflect.Array;

/**
 * Assists in the comparison of objects.  This method is used by generated
 * code but is not used within the CTF directly.
 *
 * @author <a href="mailto:gignoux@intalio.com">Sebastien Gignoux</a>
 * @version $Revision$ $Date: 2003-10-15 09:17:49 -0600 (Wed, 15 Oct 2003) $
 */
public class CompareHelper {

    /**
     * Compare two objects. Return true if they are both null or if they are
     * equal. This comparison method has special handling for arrays: For
     * arrays, each element is compared.
     * <p>
     * Warning: We will throw a NullPointerException if any element of either
     * array is null.
     *
     * @param o1 first object
     * @param o2 second object
     * @return true if both objects are both null or otherwise are equal
     */
    public static boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if ((o1 != null && o2 == null) || (o1 == null && o2 != null)) {
            return false;
        }

        // From now we can safely assume (o1 != null && o2 != null)

        if (! o1.getClass().equals(o2.getClass())) {
            return false;
        }

        if (o1.getClass().isArray()) {
            return compareArray(o1, o2);
        }

        return o1.equals(o2);
    }

    /**
     * Compares two arrays, returning true if the arrays contain the same
     * values.
     * <p>
     * Warning: We will throw a NullPointerException if any element of either
     * array is null.
     *
     * @param o1 The first array
     * @param o2 The second array
     * @return true if the two objects represent arrays with the same values
     */
    private static boolean compareArray(Object o1, Object o2) {
        int size = Array.getLength(o1);

        if (size != Array.getLength(o2))
            return false;

        Class component = o1.getClass().getComponentType();

        if ( ! component.equals(o2.getClass().getComponentType()))
            return false;

        if (component.isPrimitive()) {
            return comparePrimitiveArray(o1, o2);
        }

        for (int i=0; i < size; ++i) {
            if (! Array.get(o1, i).equals(Array.get(o2, i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two arrays of primitive values. The caller should have tested
     * that the two array have the same length and that the component type are
     * equal.
     *
     * @param o1 The first array
     * @param o2 The second array
     * @return true if the two objects represent arrays of the same primitive
     *         values
     */
    public static boolean comparePrimitiveArray(Object o1, Object o2) {
        Class component = o1.getClass().getComponentType();
        int size = Array.getLength(o1);

        if (component.equals(Boolean.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getBoolean(o1, i) != Array.getBoolean(o2, i)) {
                    return false;
                }
            }

            return true;
        } else if (component.equals(Byte.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getByte(o1, i) != Array.getByte(o2, i)) {
                    return false;
                }
            }

            return true;
        } else if (component.equals(Character.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getChar(o1, i) != Array.getChar(o2, i)) {
                    return false;
                }
            }

            return true;
        } else if (component.equals(Double.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getDouble(o1, i) != Array.getDouble(o2, i)) {
                    return false;
                }
            }

            return true;
        } else if (component.equals(Float.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getFloat(o1, i) != Array.getFloat(o2, i)) {
                    return false;
                }
            }

            return true;
        } else if (component.equals(Integer.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getInt(o1, i) != Array.getInt(o2, i)) {
                    return false;
                }
            }

            return true;
        } else if (component.equals(Long.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getLong(o1, i) != Array.getLong(o2, i)) {
                    return false;
                }
            }

            return true;
        } else if (component.equals(Short.TYPE)) {
            for (int i=0; i<size; ++i) {
                if (Array.getShort(o1, i) != Array.getShort(o2, i)) {
                    return false;
                }
            }

            return true;
        } else {
            throw new IllegalArgumentException("Unexpected primitive type " + component.toString());
        }
    }

}
