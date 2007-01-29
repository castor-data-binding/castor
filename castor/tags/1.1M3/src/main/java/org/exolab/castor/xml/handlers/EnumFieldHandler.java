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
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */
package org.exolab.castor.xml.handlers;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

/**
 * A specialized FieldHandler for the XML Schema enumeration types.
 *
 * @author <a href="keith AT kvisco  DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public class EnumFieldHandler implements FieldHandler {

    /** Used to find the method "valueOf" taking an argument of type String. */
    private static final Class[] STRING_ARGS = new Class[] {String.class};
    /** The Factory Method name. */
    private static final String  METHOD_NAME = "valueOf";

    /** The <code>valueOf(String)</code> method for the provided enumtype. */
    private final Method _valueOf;
    /** The field handler to which we delegate. */
    private final FieldHandler _handler;

    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new EnumFieldHandler with the given type and FieldHandler.
     *
     * @param enumType the Class type of the described field
     * @param handler the FieldHandler to delegate to
     */
    public EnumFieldHandler(final Class enumType, final FieldHandler handler) {
        this._handler = handler;
        this._valueOf = getStaticValueOfMethod(enumType);
    } //-- EnumFieldHandler

    /**
     * Reflectively finds the <code>valueOf(String)</code> method for the
     * provided class type.
     *
     * @param type the Class for which to locate the valueOf method.
     * @return the Method <code>valueOf(String)</code>
     */
    private Method getStaticValueOfMethod(final Class type) {
        if (type == null) {
            String err = "The Class argument passed to the "
                    + "constructor of EnumMarshalDescriptor cannot be null.";
            throw new IllegalArgumentException(err);
        }

        Method method = null;
        try {
            method = type.getMethod(METHOD_NAME, STRING_ARGS);
        } catch (NoSuchMethodException nsme) {
            String err = type.getName()
                    + " does not contain the required method: public static "
                    + type.getName() + " valueOf(String);";
            throw new IllegalArgumentException(err);
        }

        if (!Modifier.isStatic(method.getModifiers())) {
            String err = type.getName() + " public " + type.getName()
                    + " valueOf(String); exists but is not static";
            throw new IllegalArgumentException(err);
        }
        return method;
    } //-- init

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Returns the value of the field associated with this descriptor from the
     * given target object.
     *
     * @param target the object to get the value from
     * @return the value of the field associated with this descriptor from the
     *         given target object.
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatible with the Java object
     */
    public Object getValue(final Object target) throws java.lang.IllegalStateException {
        Object val = _handler.getValue(target);
        if (val == null) {
            return val;
        }

        Object result = null;
        if (val.getClass().isArray()) {
           int size = Array.getLength(val);
            String[] values = new String[size];

            for (int i = 0; i < size; i++) {
                Object obj = Array.get(val, i);
                values[i] = obj.toString();
            }
            result = values;
        } else {
            result = val.toString();
        }
        return result;
    } //-- getValue

    /**
     * Sets the value of the field associated with this descriptor.
     *
     * @param target the object in which to set the value
     * @param value the value of the field
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatible with the Java object.
     */
    public void setValue(final Object target, final Object value)
                                        throws java.lang.IllegalStateException {
        Object[] args = new String[1];
        Object obj = null;
        if (value != null) {
            args[0] = value.toString();
            try {
                obj = _valueOf.invoke(null, args);
            } catch (java.lang.reflect.InvocationTargetException ite) {
                Throwable toss = ite.getTargetException();
                throw new IllegalStateException(toss.toString());
            } catch (java.lang.IllegalAccessException iae) {
                throw new IllegalStateException(iae.toString());
            }
        }
        _handler.setValue(target, obj);
    } //-- setValue

    /**
     * Sets the value of the field to a default value -- for enum, no action
     * needed.
     *
     * @param target The object.
     */
    public void resetValue(final Object target) {
        // No action needed
    }

    /**
     * Checks the field validity. Returns successfully if the field can be
     * stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The field is invalid, is required and null, or
     *         any other validity violation
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatiable with the Java object
     */
    public void checkValidity(final Object object) throws ValidityException, IllegalStateException {
        //-- do nothing for now
    } //-- checkValidity

    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and cannot be
     *         instantiated
     */
    public Object newInstance(final Object parent) throws IllegalStateException {
        return "";
    } //-- newInstance

    /**
     * Returns true if the given object is an XMLFieldHandler that is equivalent
     * to the delegated handler. An equivalent XMLFieldHandler is an
     * XMLFieldHandler that is an instances of the same class.
     *
     * @return true if the given object is an XMLFieldHandler that is equivalent
     *         to this one.
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FieldHandler)) {
            return false;
        }
        return (_handler.getClass().isInstance(obj) || getClass().isInstance(obj));
    } //-- equals

} //-- EnumFieldHandler
