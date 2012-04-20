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

package org.exolab.castor.mapping;

import java.util.Properties;

/**
 * An extended version of the FieldHandler interface which is used for adding
 * additional functionality while preserving backward compatability.
 * 
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-08-03 15:11:51 -0600 (Wed, 03 Aug
 *          2005) $
 * @see FieldDescriptor
 * @see FieldHandler
 */
public abstract class AbstractFieldHandler<T> extends ExtendedFieldHandler<T> implements ConfigurableFieldHandler<T> {

   /**
    * The FieldDescriptor for the field that this handler is responsible for.
    */
   private FieldDescriptor _descriptor = null;

   /**
    * Configuration that can be used by subclasses when needed.
    */
   protected Properties _properties;

   /**
    * {@inheritDoc}
    */
   protected final FieldDescriptor getFieldDescriptor() {
      return _descriptor;
   }

   /**
    * {@inheritDoc}
    */
   public void setFieldDescriptor(FieldDescriptor fieldDesc) {
      _descriptor = fieldDesc;
   }

   /**
    * Returns true if the "handled" field has a value within the given object.
    * <p>
    * By default this just checks for null. Normally this method is needed for
    * checking if a value has been set via a call to the setValue method, or if
    * the primitive value has been initialised by the JVM.
    * </p>
    * <p>
    * This method should be overloaded for improved value checking.
    * </p>
    * 
    * @return true if the given object has a value for the handled field
    */
   public boolean hasValue(final Object object) {
      return (getValue(object) != null);
   }

   /**
    * Empty implementation of the {@link ConfigurableFieldHandler} interface,
    * for convenience purpose. Subclasses that want to use any configuration
    * should override this method.
    * 
    * @param config
    *           The configuration as specified in the mapping file.
    */
   public void setConfiguration(final Properties config) throws ValidityException {
   }
}
