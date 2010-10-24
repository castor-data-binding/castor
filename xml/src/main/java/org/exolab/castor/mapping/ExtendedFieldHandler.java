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

package org.exolab.castor.mapping;

import org.exolab.castor.mapping.loader.FieldHandlerFriend;

/**
 * An extended version of the FieldHandler interface which is used for adding
 * additional functionality while preserving backward compatibility.
 * 
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-08-03 15:11:51 -0600 (Wed, 03 Aug
 *          2005) $
 * @see FieldDescriptor
 * @see FieldHandler
 */
public abstract class ExtendedFieldHandler extends FieldHandlerFriend {

    /**
     * @deprecated No longer supported
     */
    public void checkValidity(Object object) throws ValidityException,
            IllegalStateException {
        // -- do nothing...deprecated method
    }

    /**
     * Creates a new instance of the object described by this field.
     * 
     * @param parent
     *            The object for which the field is created
     * @param args
     *            the set of constructor arguments
     * @return A new instance of the field's value
     * @throws IllegalStateException
     *             This field is a simple type and cannot be instantiated
     */
    public abstract Object newInstance(Object parent, Object[] args)
            throws IllegalStateException;

}
