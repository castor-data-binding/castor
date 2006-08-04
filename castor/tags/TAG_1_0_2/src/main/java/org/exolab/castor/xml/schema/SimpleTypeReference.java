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
 * Copyright 1999-2001 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;


/**
 * Package private class to handles simple-type references.
 *
 * @author <a href="mailto:berry@intalio.com">Arnaud Berry</a>
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
**/
class SimpleTypeReference extends SimpleType {
    /** SerialVersionUID */
    private static final long serialVersionUID = 1020507618887404978L;

    /**
     * The referred type (stored in the schema)
     */
    private SimpleType referredType= null;

    /**
     * Creates a new SimpleTypeReference.
     *
     * @param name the name of the simple type being referenced,
     *  must not be null.
     * @param schema the parent Schema of the simple type being referenced, 
     * must not be null.
    **/
    SimpleTypeReference(Schema schema, String name) {
        if (schema == null) {
            String err = "The schema argument to the constructor of " +
             "SimpleTypeReference is not allowed to be null.";
            throw new IllegalArgumentException(err);
        }
        if (name == null) {
            String err = "The name argument to the constructor of " +
             "SimpleTypeReference is not allowed to be null.";
            throw new IllegalArgumentException(err);
        }
        super.setName(name);
        super.setSchema(schema);
    } //-- SimpleTypeReference
    
    /**
     * Returns the referred type
     */
    XMLType getType()
    {
        if (referredType == null)
            resolveTypeReference();

        return referredType;
    }

    /**
     * Resolves the type reference.
     */
    void resolveTypeReference()
    {
        referredType= getSchema().getSimpleType(getName());
    }

    /**
     * Returns Structure.UNKNOWN
     * (This class should not be seen outside AttributeDecl (and ElementDecl ?) anyway)
    **/
    public short getStructureType() {
        return Structure.UNKNOWN;
    } //-- getStructureType

    /**
     * Sets the Schema for this Union. This method overloads the 
     * SimpleType#setSchema method to prevent the Schema from being
     * changed.
     *
     * @param the schema that this Union belongs to.
    **/
    public void setName(String name) {
        if (name != getName()) {
            String err = "The name of a SimpleTypeReference cannot be changed.";
            throw new IllegalStateException(err);
        }
    } //-- void setName
    
    /**
     * Sets the Schema for this SimpleTypeReference. This method overloads the 
     * SimpleType#setSchema method to prevent the Schema from being
     * changed.
     *
     * @param the schema that this SimpleType that is being referenced 
     * belongs to.
    **/
    public void setSchema(Schema schema) {
        if (schema != getSchema()) {
            String err = "The Schema of a SimpleTypeReference cannot be changed.";
            throw new IllegalStateException(err);
        }
    } //-- void setSchema
    
}


