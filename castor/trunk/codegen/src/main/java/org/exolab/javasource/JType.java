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
 */
package org.exolab.javasource;

/**
 * Represents a primitive or class type.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class JType {
    //--------------------------------------------------------------------------

    /** JType for a boolean (Boolean). */
    public static final JPrimitiveType BOOLEAN = new JPrimitiveType("boolean", "java.lang.Boolean");

    /** JType instance for a byte (Byte). */
    public static final JPrimitiveType BYTE = new JPrimitiveType("byte", "java.lang.Byte");

    /** JType instance for a char (Char). */
    public static final JPrimitiveType CHAR = new JPrimitiveType("char", "java.lang.Character");

    /** JType instance for a double (Double). */
    public static final JPrimitiveType DOUBLE = new JPrimitiveType("double", "java.lang.Double");

    /** JType instance for a float (Float). */
    public static final JPrimitiveType FLOAT = new JPrimitiveType("float", "java.lang.Float");

    /** JType instance for a int (Integer). */
    public static final JPrimitiveType INT = new JPrimitiveType("int", "java.lang.Integer");

    /** JType instance for a long (Long). */
    public static final JPrimitiveType LONG = new JPrimitiveType("long", "java.lang.Long");

    /** JType instance for a short (Short). */
    public static final JPrimitiveType SHORT = new JPrimitiveType("short", "java.lang.Short");

    //--------------------------------------------------------------------------

    /** Fully qualified of the Java type represented. */
    private String _name = null;

    //--------------------------------------------------------------------------

    /**
     * Creates a new JType with the given name.
     *
     * @param name The name of the type.
     */
    protected JType(final String name) {
        super();
        
        _name = name;
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the unqualified Java type name (i.e. without package).
     *
     * @return The unqualified Java type name.
     */
    public final String getLocalName() {
        // -- use getName method in case it's been overloaded
        return JNaming.getLocalNameFromClassName(_name);
    }

    /**
     * Returns the qualified Java type name.
     *
     * @return The qualified Java type name.
     */
    public final String getName() {
        return _name;
    }

    //--------------------------------------------------------------------------
}
