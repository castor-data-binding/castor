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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.ValidationException;

/**
 * Package private class to handles type references.
 *
 * @author <a href="mailto:berry@intalio.com">Arnaud Berry</a>
 * @version $Revision:
**/
class TypeReference extends XMLType {
    /** SerialVersionUID */
    private static final long serialVersionUID = -8707313918028332092L;
    
    /**
     * The referred type (stored in the schema)
     */
    private XMLType referredType= null;

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

        String name = getName();
        if (name == null) return;


        Schema schema = getSchema();
        if (schema == null) {
            //-- ummm we have a problem.
            String error = "Schema is null. Reference  cannot be resolved.";
            throw new IllegalStateException(error);
        }

        //-- is AnyType? resolve namespace if necessary
        //-- and check for 'anyType'.
        String canonicalName = name;
        String nsPrefix = "";
        int colon = name.indexOf(':');
        if (colon >= 0) {
            canonicalName = name.substring(colon + 1);
            nsPrefix = name.substring(0,colon);
        }
        String ns = schema.getNamespace(nsPrefix);

        if (schema.getSchemaNamespace().equals(ns)) {
            if (canonicalName.equals(SchemaNames.ANYTYPE)) {
                referredType = new AnyType(schema);
                return;
            }
        }


        //-- check simpletype first since it has higher precedence
        IllegalArgumentException exception = null;
        try {
            referredType= getSchema().getSimpleType(getName());
        }
        catch (IllegalArgumentException iax) {
            exception = iax;
        }
        if (referredType != null) return; //we found it, return it
        //-- try to find a complex type
        referredType= getSchema().getComplexType(getName());

        if (referredType != null) return; //we found it, return it

        //-- rethrow exception if necessary
        if (exception != null) throw exception;

    } //- resolveTypeReference

    /**
     * Sets the parent for this Schema type
     *
     * @param parent the parent Structure for SchemaType
    **/
    protected void setParent(Structure parent) {
        //-- never used by references
    } //-- setParent

    /**
     * Returns Structure.UNKNOWN
     * (This class should not be seen outside ElementDecl anyway)
    **/
    public short getStructureType() {
        return Structure.UNKNOWN;
    } //-- getStructureType

    /**
     * Checks the validity of this type defintion.
     *
     * @throws ValidationException when this type definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
        //-- Do nothing

    } //-- validate

}


