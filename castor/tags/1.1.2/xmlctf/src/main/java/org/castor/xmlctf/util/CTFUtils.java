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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 *
 */
package org.castor.xmlctf.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.castor.xmlctf.xmldiff.XMLDiff;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * This class contains utility methods needed by the CTF.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public class CTFUtils {

    /**
     * The Java primitives.
     */
    public static final String BOOLEAN   = "boolean";
    public static final String BYTE      = "byte";
    public static final String CHARACTER = "character";
    public static final String DOUBLE    = "double";
    public static final String FLOAT     = "float";
    public static final String INT       = "int";
    public static final String LONG      = "long";
    public static final String SHORT     = "short";
    public static final String STRING    = "String";

    private static Map nameMap = new HashMap();
    static {
        nameMap.put(BOOLEAN,   Boolean.TYPE);
        nameMap.put(BYTE,      Byte.TYPE);
        nameMap.put(CHARACTER, Character.TYPE);
        nameMap.put(DOUBLE,    Double.TYPE);
        nameMap.put(FLOAT,     Float.TYPE);
        nameMap.put(INT,       Integer.TYPE);
        nameMap.put(LONG,      Long.TYPE);
        nameMap.put(SHORT,     Short.TYPE);
    }

    /**
     * No-arg constructor.  Private as we're a static utility class.
     */
    private CTFUtils() {
        // Nothing to do
    }

    /**
     * Compares two XML documents located at 2 given URLs, returning the number
     * of differences or 0 if both documents are 'XML equivalent'.
     *
     * @param document1 the URL of the first XML document.
     * @param document2 the URL of the second XML document.
     * @return an int indicating the number of differences or 0 if both
     *         documents are 'XML equivalent'.
     * @throws java.io.IOException if an error occurs reading either XML
     *             document
     */
    public static int compare(final String document1, final String document2)
                                                         throws java.io.IOException {
        XMLDiff diff = new XMLDiff(document1, document2);
        return diff.compare();
    }

    /**
     * Returns the class associated with the given name.
     *
     * @param name the fully qualified name of the class to return. Primitives
     *            are handled through their name and not their class name. For
     *            instance 'boolean' should be used instead of
     *            'java.lang.Boolean.TYPE'.
     * @param loader the ClassLoader to use if the class needs to be loaded
     * @return the class associated with given name.
     * @throws ClassNotFoundException if the given class cannot be loaded using
     *             the provided class loader.
     */
    public static Class getClass(final String name, final ClassLoader loader)
                                                       throws ClassNotFoundException {
        if (name == null) {
            throw new IllegalArgumentException("Name shouldn't be null.");
        }

        Class clazz = (Class) nameMap.get(name);
        if (clazz != null) {
            return clazz;
        }

        return loader.loadClass(name);
    }

   /**
     * Converts the given value to a Java representation that corresponds to the
     * given type.
     *
     * @param value the value to be converted
     * @param type a string representation of the java type.
     * @param loader an optional ClassLoader used in case we need to use the
     *            Unmarshaller to retrieve a complex java object.
     * @return an java object that corresponds to the given value converted to a
     *         java type according to the type passed as parameter.
     * @throws ClassNotFoundException if the type is not a recognized primitive
     *             type and the class loader provided cannot load the type
     * @throws MarshalException if the type is not a recognized primitive type
     *             and no Marshaller can be found for that type
     */
    public static Object instantiateObject(final String type, final String value,
                                           final ClassLoader loader)
                                        throws ClassNotFoundException, MarshalException {
        if (type.equals(STRING) || type.equals(String.class.getName())) {
            return value;
        } else if (type.equals(BOOLEAN) || type.equals(Boolean.class.getName())) {
            return new Boolean(value);
        } else if (type.equals(BYTE) || type.equals(Byte.class.getName())) {
            return new Byte(value);
        } else if (type.equals(CHARACTER) || type.equals(Character.class.getName())) {
            return new Character(value.charAt(0));
        } else if (type.equals(DOUBLE) || type.equals(Double.class.getName())) {
            return new Double(value);
        } else if (type.equals(FLOAT) || type.equals(Float.class.getName())) {
            return new Float(value);
        } else if (type.equals(INT) || type.equals(Integer.class.getName())) {
            return new Integer(value);
        } else if (type.equals(LONG) || type.equals(Long.class.getName())) {
            return new Long(value);
        } else if (type.equals(SHORT) || type.equals(Short.class.getName())) {
            return new Short(value);
        }

        //-- Else we let the unmarshaller get us the class
        try {
            Class clazz = loader.loadClass(type);
            Unmarshaller unmarshaller = new Unmarshaller(clazz);
            return unmarshaller.unmarshal(new StringReader(value));
        } catch (ValidationException e) {
            //--this can't happen, just log it
            e.printStackTrace();
        }

        return null;
    }

}
