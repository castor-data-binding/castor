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
 * Copyright 1999-2005 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.mapping.handlers;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A specialized FieldHandler for the type-safe enum
 * style classes.
 * 
 * Adapted from org.exolab.castor.xml.handlers.EnumFieldHandler
 * which is used for the generated source code.
 * 
 * 
 * @author <a href="kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class EnumFieldHandler extends GeneralizedFieldHandler {


    /**
     * Class type for the type-safe enum 
     */
    private Class _enumType = null;

    /**
     * The create method (eg: #valueOf(String))
     */
    private Method _createMethod  = null;

    /**
     * The underlying FieldHandler
     */
    private FieldHandler _handler = null;
    

    /**
     * Creates a new EnumFieldHandler with the given type and
     * FieldHandler
     * 
     * @param enumType the Class type of the described field
     * @param handler the FieldHandler to delegate to
     */
    public EnumFieldHandler(Class enumType, FieldHandler handler, Method createMethod) {

        if (enumType == null) {
            String err = "The argument 'enumType' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        if (handler == null) {
            String err = "The argument 'handler' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        if (createMethod == null) {
            String err = "The argument 'createMethod' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        _handler = handler;
        
        //-- pass handler to superclass
        setFieldHandler(handler);

        _enumType = enumType;
        _createMethod = createMethod;
        
        int mods = createMethod.getModifiers();

        if (!Modifier.isStatic(mods)) {
            String err = "The factory create method specified for " + enumType.getName() 
            + " must be static";
            throw new IllegalArgumentException(err);
        }

    } //-- EnumFieldHandler

    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponGet(java.lang.Object)
     */
    public Object convertUponGet(Object value)
    {
        //-- no conversion for getter
        return value;

    } //-- getValue

    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponSet(java.lang.Object)
     */
    public Object convertUponSet(Object value)
        throws java.lang.IllegalStateException
    {
        String[] args = new String[1];
        Object obj = null;
        if (value != null) {
            args[0] = value.toString();
            try {
                obj = _createMethod.invoke(null, (Object[]) args);
            }
            catch(java.lang.reflect.InvocationTargetException ite) {
                Throwable toss = ite.getTargetException();
                throw new IllegalStateException(toss.toString());
            }
            catch(java.lang.IllegalAccessException iae) {
                throw new IllegalStateException(iae.toString());
            }
        }
        return obj;

    } //-- setValue
    
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#getFieldType()
     */
    public Class getFieldType() {
    	return _enumType;
    }


    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.FieldHandler#newInstance(java.lang.Object)
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        return "";
    } //-- newInstance
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.ExtendedFieldHandler#newInstance(java.lang.Object, java.lang.Object[])
     */
    public Object newInstance( Object parent, Object[] args )
        throws IllegalStateException
    {
        return "";
    }
    

    /**
     * Returns true if the given object is an XMLFieldHandler that
     * is equivalent to the delegated handler. An equivalent XMLFieldHandler
	 * is an XMLFieldHandler that is an instances of the same class.
     *
     * @return true if the given object is an XMLFieldHandler that
     * is equivalent to this one.
    **/
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof FieldHandler)) return false;
        return (_handler.getClass().isInstance(obj) ||
                getClass().isInstance(obj));
    } //-- equals

} //-- EnumFieldHandler



