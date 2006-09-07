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
 
package org.exolab.castor.mapping;

import org.exolab.castor.mapping.loader.CollectionHandlers;
import java.util.Enumeration;

/**
 * An extended version of the FieldHandler interface which is
 * used for making generic libraries of FieldHandlers which 
 * can be used for more than one field or class, but have
 * similar conversion algorithms.
 *
 * @author <a href="kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-01-18 17:29:45 -0700 (Tue, 18 Jan 2005) $
 * @see FieldDescriptor
 * @see FieldHandler
 */
public abstract class GeneralizedFieldHandler 
    extends AbstractFieldHandler
{

    /**
     * Error message when a null FieldHandler is encountered
     */
    private static final String NULL_HANDLER_ERR 
        = "A call to #setFieldHandler (with a non-null value) must be " +
          "made before calling this method.";
        
    /**
     * The actual FieldHandler to delegate to
     */
    private FieldHandler _handler = null;
    
    /**
     * The flag controlling automatic collection iteration
     * during convertUponGet
     */
    private boolean _autoCollectionIteration = true;
    
    
    /** 
     * Creates a new default GeneralizedFieldHandler. This method
     * should be called by all extending classes so that any
     * important initialization code will be executed.
     */
    protected GeneralizedFieldHandler() {
        super();
        //-- currently nothing to do, but initialization
        //-- code may be needed in the future
    } //-- GeneralizedFieldHandler
    
    
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
    public abstract Object convertUponGet(Object value);

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
    public abstract Object convertUponSet(Object value);
    
    /**
     * Returns the class type for the field that this GeneralizedFieldHandler
     * converts to and from. This should be the type that is used in the
     * object model.
     *
     * @return the class type of of the field
     */
    public abstract Class getFieldType();
    
    /**
     * Sets the FieldHandler that this FieldHander delegates to.
     * A call to this method must be made with a non-null
     * FieldHandler before this GeneralizedFieldHandler can be used.
     *
     * @param handler the FieldHandler to delegate to
     */
    public final void setFieldHandler(FieldHandler handler) {
        _handler = handler;
    } //-- setFieldHandler

    /**
     * Sets whether or not this GeneralizedFieldHandler should automatically
     * iterate over the collection returned by the target object and pass 
     * only the items (one by one) to the convertUponGet method. 
     * 
     * As of Castor 0.9.6 this is true by default. 
     * 
     * @param autoCollectionIteration a boolean that when true indicates
     * that this GeneralizedFieldHandler should automatically iterate over
     * a collection and pass only collection items to the convertUponGet
     * method.
     */
    public void setCollectionIteration(boolean autoCollectionIteration) {
        _autoCollectionIteration = autoCollectionIteration;
    } //-- setCollectionIteration

    //-----------------------------------------------/
    //- Methods inherited from AbstractFieldHandler -/
    //-----------------------------------------------/
    
    /**
     * Returns the value of the field from the object.
     *
     * @param object The object
     * @return The value of the field
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatiable with the Java object
     */
    public final Object getValue( Object object )
        throws IllegalStateException
    {
        if (_handler == null) {
            throw new IllegalStateException(NULL_HANDLER_ERR);
        }
        
        Object value = _handler.getValue(object);
        if ((_autoCollectionIteration) && (value != null)) {
            
            if (value instanceof java.util.Enumeration) {
            	return new GFHConverterEnumeration(this, (Enumeration)value);
            }
            //-- other collection type?
            if (CollectionHandlers.hasHandler(value.getClass())) {
                CollectionHandler colHandler = null;
                try {
                    colHandler = CollectionHandlers.getHandler(value.getClass());
                }
                catch(MappingException mx) {
                	throw new IllegalStateException(mx.getMessage());
                }
                return new GFHConverterEnumeration(this, colHandler.elements(value));
            }
        }
        
        return convertUponGet(value);
    } //-- getValue
    

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
        if (_handler == null) {
            throw new IllegalStateException(NULL_HANDLER_ERR);
        }
        return _handler.newInstance(parent);
    }
    
    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @param args the set of constructor arguments
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent, Object[] args )
        throws IllegalStateException 
    {
        if (_handler instanceof ExtendedFieldHandler) {
            return ((ExtendedFieldHandler)_handler).newInstance(parent, args);
        }
        
        //-- backward compatibility: ignore arguments
        return newInstance( parent );
    }
        
    /**
     * Sets the value of the field to a default value.
     * <p>
     * Reference fields are set to null, primitive fields are set to
     * their default value, collection fields are emptied of all
     * elements.
     *
     * @param object The object
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatiable with the Java object
     */
    public final void resetValue( Object object )
        throws IllegalStateException, IllegalArgumentException
    {
        if (_handler == null) {
            throw new IllegalStateException(NULL_HANDLER_ERR);
        }
        _handler.resetValue(object);
    }
        
    /**
     * Sets the value of the field on the object.
     *
     * @param object The object
     * @param value The new value
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatiable with the Java object
     * @thorws IllegalArgumentException The value passed is not of
     *  a supported type
     */
    public final void setValue( Object object, Object value )
        throws IllegalStateException, IllegalArgumentException
    {
        if (_handler == null) {
            throw new IllegalStateException(NULL_HANDLER_ERR);
        }
        _handler.setValue(object, convertUponSet(value));
    }
    
    static class GFHConverterEnumeration implements Enumeration {
        
        Enumeration _enumeration = null;
        GeneralizedFieldHandler _handler = null;
        
        GFHConverterEnumeration(GeneralizedFieldHandler handler, Enumeration enumeration) {
            _enumeration = enumeration;
            _handler = handler;
        }
        
        public boolean hasMoreElements() {
            return _enumeration.hasMoreElements();
        }
        
        public Object nextElement() {
            Object value = _enumeration.nextElement();
            return _handler.convertUponGet(value);
        }
        
        
    }

} //-- GeneralizedFieldHandler

