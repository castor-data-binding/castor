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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.castor.xml.handlers;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import org.exolab.castor.xml.util.ContainerElement;

/**
 * The FieldHandler for ContainerElement
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @see FieldDescriptor
 * @see FieldHandler
 */
public final class ContainerFieldHandler implements FieldHandler
{

    public static final int MODE_AUTO        = 0;
    public static final int MODE_PARENT_LINK = 1;
    public static final int MODE_CHILD_LINK  = 2;
    
    /**
     * The actual FieldHandler to delegate to
     */
    private FieldHandler _handler = null;
    
    private int _mode = MODE_AUTO;
    
    /** 
     * Creates a new ContainerFieldHandler with the given FieldHandler.
     */
    public ContainerFieldHandler(FieldHandler handler) {
        super();
        _handler = handler;
    } //-- ContainerFieldHandler
    

    //-----------------------------/
    //- Methods from FieldHandler -/
    //-----------------------------/
    
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
        int mode = _mode;
        if (mode == MODE_AUTO) {
            if ( object instanceof ContainerElement )
                mode = MODE_CHILD_LINK;
            else
                mode = MODE_PARENT_LINK;
        }
        Object value;
        if (mode == MODE_CHILD_LINK) {
            ContainerElement container = (ContainerElement)object;
            value = _handler.getValue(container.getParent());
        }
        // MODE_PARENT_LINK
        else {
            value = _handler.getValue(object);
            ContainerElement container = new ContainerElement(value);     
            container.setParent(object);
            value = container;
        }
        return value;
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
        ContainerElement container = null;
        //-- MODE_CHILD_LINK
        if (parent instanceof ContainerElement) {
            container = (ContainerElement)parent;
            return _handler.newInstance(container.getParent());
        }
        container = new ContainerElement();
        container.setParent( parent );
        return container;
    } //-- newInstance
        
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
        //-- delgate
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
        if ((_mode == MODE_AUTO) || (_mode == MODE_CHILD_LINK)) {
            if (object instanceof ContainerElement) {
                ContainerElement container = (ContainerElement)object;
                _handler.setValue(container.getParent(), value);
            }
        }
        //-- MODE_PARENT_LINK
        //-- do nothing, a container is not actually part of the
        //-- object model
    } //-- setValue


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
        //-- deprecated...do nothing
        
    } //-- checkValidity

} //-- ContainerFieldHandler

