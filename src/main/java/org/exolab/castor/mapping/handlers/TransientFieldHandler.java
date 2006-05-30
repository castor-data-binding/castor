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
 * Copyright 2005 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * 
 * Created on Jan 15, 2005 by kvisco
 *
 */

package org.exolab.castor.mapping.handlers;

import org.exolab.castor.mapping.AbstractFieldHandler;

/**
 * A dummy FieldHandler for "transient" fields. This allows us
 * to create a FieldDescriptor, but not use a real FieldHandler
 * 
 * @author <a href="mailto:kvisco-at-intalio.com">kvisco</a>
 * @revision $Revision$ $Date: 2005-01-17 14:35:07 -0700 (Mon, 17 Jan 2005) $
 */
public class TransientFieldHandler extends AbstractFieldHandler {
    
    public TransientFieldHandler() {
    	super();
    }
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.FieldHandler#getValue(java.lang.Object)
     */
    public Object getValue(Object target) {
    	return null;
    }
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.FieldHandler#newInstance(java.lang.Object)
     */
    public Object newInstance(Object target) {
       return null; 
    }
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.ExtendedFieldHandler#newInstance(java.lang.Object, java.lang.Object[])
     */
    public Object newInstance(Object target, Object[] args) {
    	return null;
    }
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.FieldHandler#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object target, Object value) {
    	//-- do nothing
    }
    
    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.mapping.FieldHandler#resetValue(java.lang.Object)
     */
    public void resetValue(Object target) {
    	//-- do nothing
    }

}
