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


import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MappingException;


/**
 * An implementation of GeneralizedFieldHandler that simply
 * calls Object#toString() in the conversion methods
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-28 17:53:23 -0700 (Mon, 28 Feb 2005) $
 */
public class ToStringFieldHandler extends GeneralizedFieldHandler
{
    
    /**
     * The class type for this FieldHandler
     */
    private Class _type = null;
    
    
    /**
     * Creates a new ToStringFieldHandler
     *
     * @param type the class type to create the FieldHandler for
     */
    public ToStringFieldHandler(Class type) 
        throws MappingException
    {
        super();
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' must not be null.");
        }
        
        _type = type;
    } //-- ToStringFieldHandler
    
    /**
     * Creates a new ToStringFieldHandler
     *
     * @param type the class type to create the FieldHandler for
     * @param handler the FieldHandler to wrap
     */
    public ToStringFieldHandler(Class type, FieldHandler handler) 
        throws MappingException
    {
        super();
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' must not be null.");
        }
        _type = type;
        if (handler != null) {
        	setFieldHandler(handler);
        }
    } //-- ToStringFieldHandler
    
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponGet(java.lang.Object)
     */
    public Object convertUponGet(Object value) {
        if (value == null) return null;
        return value.toString();
    } //-- convertUponGet

    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponSet(java.lang.Object)
     */
    public Object convertUponSet(Object value) 
    {
        if (value == null) return null;
        return value.toString();
        
    } //-- convertUponSet;
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.GeneralizedFieldHandler#getFieldType()
     */
    public Class getFieldType() {
        return _type;
    } //-- getFieldType
    
    
} //-- ValueOfFieldHandler

