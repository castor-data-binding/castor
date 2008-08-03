/*
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

import org.exolab.castor.mapping.FieldHandlerFactory;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MappingException;

/**
 * The default FieldHandlerFactory implementation.  Used by the Introspector.
 * This implementation only supports java.sql.Time and java.sql.Timestamp.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-04-08 22:44:47 -0600 (Thu, 08 Apr 2004) $
 */
public class DefaultFieldHandlerFactory extends FieldHandlerFactory {

    /** The array of supported classes. */
    private static final Class[] SUPPORTED_CLASSES = new Class[] {
        java.sql.Time.class,
        java.sql.Timestamp.class
    };

    /**
     * Returns an array of the supported Class types for this
     * FieldHandlerFactory. The array may be empty, but must not be null.
     *
     * @return an array of supported Class types.
     */
    public Class[] getSupportedTypes() {
        return (Class[]) SUPPORTED_CLASSES.clone();
    } //-- getSupportedTypes

    /**
     * Returns true if the given Class type is supported by this
     * FieldHandlerFactory. If the type is supported, a call to
     * {@link #createFieldHandler} will return a valid FieldHandler. If the type
     * is not supported, a call to createFieldHandler may return null or throw a
     * MappingException.
     *
     * @param type the Class type to determine support for.
     *
     * @return true if the given Class type is supported.
     */
    public boolean isSupportedType(final Class type) {
        for (int i = 0; i < SUPPORTED_CLASSES.length; i++) {
            if (SUPPORTED_CLASSES[i].isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    } //-- isSupportedType

    /**
     * Creates a GeneralizedFieldHandler for the given class type. The method
     * should return a new GeneralizedFieldHandler as an "underlying"
     * FieldHandler will need to be set by the caller.
     * @param type the Class type to create the FieldHandler for.
     * @return a new FieldHandler
     * @throws MappingException if a <code>valueOf</code> method cannot be found.
     */
    public GeneralizedFieldHandler createFieldHandler(final Class type) throws MappingException {
        if (type == null) {
            return null;
        }
        if (java.sql.Time.class.isAssignableFrom(type)) {
            return new SQLTimeFieldHandler();
        }
        if (java.sql.Timestamp.class.isAssignableFrom(type)) {
            return new ValueOfFieldHandler(type);
        }
        return null;
    } //--  createFieldHandler

} //-- DefaultFieldHandlerFactory
