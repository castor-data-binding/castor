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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;

import org.exolab.castor.mapping.AbstractFieldHandler;

/**
 * This FieldHandler is used in the generated descriptors.
 * <p>
 * A field handler knows how to perform various operations on the
 * field that require access to the field value.
 * </p>
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
 * @see org.exolab.castor.mapping.FieldDescriptor
 */
public class XMLFieldHandler
    extends AbstractFieldHandler
{

    /**
     * Creates a new default XMLFieldHandler
     */
    public XMLFieldHandler() {
        super();
    } //-- XMLFieldHandler
    
    
    /**
     * Returns true if the given object is an XMLFieldHandler that
     * is equivalent to this one. An equivalent XMLFieldHandler is
     * an XMLFieldHandler that is an instances of the same class. 
     * This method can be overwritten to provide more advanced 
     * equivalence tests.
     *
     * @return true if the given object is an XMLFieldHandler that
     * is equivalent to this one.
    **/
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof XMLFieldHandler)) return false;
        return getClass().isInstance(obj);
    } //-- equals
    
    /**
     * Returns the value of the field from the object.
     *
     * @param object The object
     * @return The value of the field
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatiable with the Java object
     */
    public Object getValue( Object object ) 
        throws IllegalStateException
    {
        //-- Do nothing, this method is overloaded by the
        //-- source code generator
        return null;
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
        //-- Do nothing, this method is overloaded by the
        //-- source code generator
        return null;
    } //-- newInstance
        
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
        //-- backward compatability...ignore args
        return newInstance(parent);
        
    } //-- newInstance

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
    public void setValue( Object object, Object value )
        throws IllegalStateException, IllegalArgumentException
    {
        //-- Do nothing, this method is overloaded by the
        //-- source code generator
        
    } //-- setValue


    public void resetValue( Object object )
        throws IllegalStateException, IllegalArgumentException
    {
        //-- Do nothing, this method is overloaded by the
        //-- source code generator
    }
     
} //-- XMLFieldHandler

