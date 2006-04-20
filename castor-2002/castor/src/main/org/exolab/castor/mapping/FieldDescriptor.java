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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping;


/**
 * Describes the properties of a field. Implementations will extend
 * this inteface to provide additional properties.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see FieldHandler
 */
public interface FieldDescriptor
{

    /**
     * Set the class which contains this field
     */
    public void setContainingClassDescriptor( ClassDescriptor contClsDesc );

    /**
     * @return the class which contains this field
     */
    public ClassDescriptor getContainingClassDescriptor();

    /**
     * Returns the name of the field.
     *
     * @return Field name
     */
    public String getFieldName();


    /**
     * Returns the Java type of the field.
     *
     * @return Field type
     */
    public Class getFieldType();


    /**
     * Returns true if the field is transient. Transient fields are
     * never persisted or marshalled.
     *
     * @return True if transient field
     */
    public boolean isTransient();


    /**
     * Returns true if the field type is immutable.
     *
     * @return True if the field type is immutable
     */
    public boolean isImmutable();


    /**
     * Returns true if the field type is required.
     *
     * @return True if the field type is required
     */
    public boolean isRequired();


    /**
     * Returns true if the field is multi valued (a collection).
     *
     * @return True if the field is multi valued
     */
    public boolean isMultivalued();


    /**
     * Returns the class descriptor related to the field type. If the
     * field type is a class for which a descriptor exists, this
     * descriptor is returned. If the field type is a class for which
     * no mapping is provided, null is returned.
     *
     * @return The class descriptor of the field type, or null
     */
    public ClassDescriptor getClassDescriptor();


    /**
     * Returns the handler of the field. In order to persist or marshal
     * a field descriptor will be associated with a handler.
     *
     * @return The field handler
     */
    public FieldHandler getHandler();


}

