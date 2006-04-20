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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.castor.mapping.loader;

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

/**
 * An extended version of the FieldHandler interface which is
 * used for adding additional functionality while preserving
 * backward compatability.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 * @see FieldDescriptor
 * @see FieldHandler
 */
public abstract class ExtendedFieldHandler implements FieldHandler
{

    
    /**
     * Returns the FieldDescriptor for the field that this 
     * handler is reponsibile for, or null if no FieldDescriptor
     * has been set. This method is useful for implementations
     * of the FieldHandler interface that wish to obtain information
     * about the field in order to make the FieldHandler more generic
     * and reusable, or simply for validation purposes.
     *
     * @return the FieldDescriptor, or null if none exists.
     */
    protected abstract FieldDescriptor getFieldDescriptor();
    
    /**
     * Sets the FieldDescriptor that this FieldHander is
     * responsibile for. By setting the FieldDescriptor, it
     * allows the implementation of the FieldHandler methods 
     * to obtain information about the field itself. This allows
     * a particular implementation to become more generic and
     * reusable.
     *
     * @param fieldDesc the FieldDescriptor to set
     */
    protected abstract void setFieldDescriptor(FieldDescriptor fieldDesc);
    
    //---------------------------------------/
    //- Methods inherited from FieldHandler -/
    //---------------------------------------/
    
    /**
     * @deprecated No longer supported
     */
    public final void checkValidity( Object object )
        throws ValidityException, IllegalStateException
    {
        //-- do nothing...deprecated method
    } //-- checkValidity
    
    /**
     * Returns the value of the field from the object.
     *
     * @param object The object
     * @return The value of the field
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatiable with the Java object
     */
    public abstract Object getValue( Object object )
        throws IllegalStateException;
    

    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public abstract Object newInstance( Object parent )
        throws IllegalStateException;
        
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
    public abstract void resetValue( Object object )
        throws IllegalStateException, IllegalArgumentException;
        
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
    public abstract void setValue( Object object, Object value )
        throws IllegalStateException, IllegalArgumentException;

} //-- ExtendedFieldHandler

