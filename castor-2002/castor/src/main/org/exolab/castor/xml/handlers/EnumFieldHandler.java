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

package org.exolab.castor.xml.handlers;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Array;

/**
 * A specialized MarshalDescriptor for the XML Schema
 * enumeration types
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class EnumFieldHandler implements FieldHandler {


    private Class _enumType = null;

    private Method valueOf  = null;

    private FieldHandler handler = null;

    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new EnumFieldHandler with the given type and
     * FieldHandler
     * @param type the Class type of the described field
     * @param handler the FieldHandler to delegate to
    **/
    public EnumFieldHandler(Class enumType, FieldHandler handler) {
        this.handler = handler;
        init(enumType);
    } //-- EnumMarshalDescriptor


    private void init(Class type) {

        if (type == null) {
            String err = "The Class argument passed to the " +
                "constructor of EnumMarshalDescriptor cannot be null.";
            throw new IllegalArgumentException(err);
        }

        this._enumType = type;
        Class[] params = new Class[1];
        params[0] = String.class;

        Method method = null;
        try {
            method = type.getMethod("valueOf", params);
        }
        catch(NoSuchMethodException nsme) {
            String err = type.getName() +
                " does not contain the required method: public static " +
                 type.getName() + " valueOf(String);";
            throw new IllegalArgumentException(err);
        }

        int mods = method.getModifiers();

        if (!Modifier.isStatic(mods)) {
            String err = type.getName() +
                " does not contain the required method: public static " +
                type.getName() + " valueOf(String);";
            throw new IllegalArgumentException(err);
        }

        valueOf = method;
        method = null;
    } //-- init

    //------------------/
    //- Public Methods -/
    //------------------/


    /**
     * Returns the value of the field associated with this
     * descriptor from the given target object.
     * @param target the object to get the value from
     * @return the value of the field associated with this
     * descriptor from the given target object.
    **/
    public Object getValue(Object target)
        throws java.lang.IllegalStateException
    {
        Object val = handler.getValue(target);
        Object result = null;
        if (val == null) return val;

        if (val.getClass().isArray()) {
           int size = Array.getLength(val);
            String[] values = new String[size];

            for (int i = 0; i < size; i++) {
                Object obj = Array.get(val, i);
                values[i] = obj.toString();
            }
            result = values;
        } else result = val.toString();
        return result;
    } //-- getValue

    /**
     * Sets the value of the field associated with this descriptor.
     * @param target the object in which to set the value
     * @param value the value of the field
    **/
    public void setValue(Object target, Object value)
        throws java.lang.IllegalStateException
    {
        String[] args = new String[1];
        if (value != null) args[0] = value.toString();
        Object obj = null;
        try {
            obj = valueOf.invoke(null, args);
        }
        catch(java.lang.reflect.InvocationTargetException ite) {
            Throwable toss = ite.getTargetException();
            throw new IllegalStateException(toss.toString());
        }
        catch(java.lang.IllegalAccessException iae) {
            throw new IllegalStateException(iae.toString());
        }
        handler.setValue(target, obj);

    } //-- setValue

    public void resetValue(Object targer)
    {
    }

    /**
     * Checks the field validity. Returns successfully if the field
     * can be stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The field is invalid, is required and
     *  null, or any other validity violation
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler
     *  is not compatiable with the Java object
     */
    public void checkValidity( Object object )
        throws ValidityException, IllegalStateException
    {
        //-- do nothing for now
    } //-- checkValidity


    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        return "";
    } //-- newInstance

} //-- EnumMarshalDescriptor



