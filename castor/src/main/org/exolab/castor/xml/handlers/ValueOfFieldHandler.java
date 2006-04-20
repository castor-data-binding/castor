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
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.castor.xml.handlers;


import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MappingException;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * An implementation of GeneralizedFieldHandler for
 * classes that have a built-in valueOf(String) factory 
 * method, such as type-safe enumeration classes, java.sql.Timestamp,
 * etc.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 * @see FieldDescriptor
 * @see FieldHandler
 */
public class ValueOfFieldHandler extends GeneralizedFieldHandler
{
    
    /**
     * The Class[] holding the argument types of
     * the factory method
     */
    private static final Class[] ARGS = new Class[] {
        String.class
    };
    
    /**
     * The Factory Method name
     */
    private static final String METHOD_NAME = "valueOf";
    
    /**
     * The class type for this FieldHandler
     */
    private Class _type = null;
    
    private Method _valueOf = null;
    
    /**
     * Creates a new ValueOfFieldHandler
     *
     * @param type the class type to create the FieldHandler for
     */
    public ValueOfFieldHandler(Class type) 
        throws MappingException
    {
        super();
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' must not be null.");
        }
        
        _type = type;
        
        Method method = null;
        try {
            method = type.getMethod(METHOD_NAME, ARGS);
        }
        catch(java.lang.NoSuchMethodException nsme) {
            throw new MappingException(nsme);
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            String err = "No static method '" + METHOD_NAME;
            err += "' found in class: " + type.getName();
            throw new MappingException(err);
        }
        
        //-- check return type?
        
        _valueOf = method;
        
    } //-- ValueOfFieldHandler
    
    
    /**
     * This method is used to convert the value when the getValue method
     * is called. The getValue method will obtain the actual field value 
     * from given 'parent' object. This convert method is then invoked
     * with the field's value. The value returned from this
     * method will be the actual value returned by getValue method.
     *
     * @param value the object value to convert after performing a get
     * operation
     * @return the converted value.
     */
    public Object convertUponGet(Object value) {
        //-- no conversion necessary for marshalling
        return value;
    } //-- convertUponGet

    /**
     * This method is used to convert the value when the setValue method
     * is called. The setValue method will call this method to obtain
     * the converted value. The converted value will then be used as
     * the value to set for the field.
     *
     * @param value the object value to convert before performing a set
     * operation
     * @return the converted value.
     */
    public Object convertUponSet(Object value) 
    {
        
        Object[] args = new Object[1];
        
        if (value != null) {
            args[0] = value.toString();
        }
        
        Object result = null;
        
        try {
            result =_valueOf.invoke(null, args);        
        }
        catch(java.lang.IllegalAccessException iae) {
            throw new IllegalStateException(iae.getMessage());
        }
        catch(java.lang.reflect.InvocationTargetException ite) {
            throw new IllegalStateException(ite.getMessage());            
        }
        
        return result;
    } //-- convertUponSet;
    
    /**
     * Returns the class type for the field that this GeneralizedFieldHandler
     * converts to and from. This should be the type that is used in the
     * object model.
     *
     * @return the class type of of the field
     */
    public Class getFieldType() {
        return _type;
    } //-- getFieldType
    
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
        return null;
    }
    
    
    
} //-- ValueOfFieldHandler

