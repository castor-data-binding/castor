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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.schema;

import java.util.Enumeration;

/**
 * An XML Schema Attribute Group Definition
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class AttributeGroupReference extends AttributeGroup {
    /** SerialVersionUID */
    private static final long serialVersionUID = -6283626049554689747L;

    /**
     * Error message for a null argument
    **/
    private static String NULL_ARGUMENT
        = "A null argument was passed to the constructor of " +
           "AttributeGroupReference";

    /**
     * The Schema to which this AttributeDecl belongs
    **/
    private Schema _schema = null;

    private String _reference = null;

    /**
     * Creates a new AttributeGroup definition
     * @param schema the Schema that this AttributeGroup
     * belongs to.
    **/
    public AttributeGroupReference(Schema schema, String reference) {
        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        if (reference == null) {
            String err = NULL_ARGUMENT + "; 'reference' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _schema    = schema;
        _reference = reference;
    } //-- AttributeGroup

    /**
     * Returns the anyAttribute set in this attribute group if any.
     * @return the anyAttribute set in this attribute group if any.
     */
    public Wildcard getAnyAttribute() {
        return resolveReference().getAnyAttribute();
    }


    /**
     * Gets the name of the attribute group this class refers to.
     */
    public String getReference() { return _reference; }

    /**
     * Resolves the attribute group reference
     * @return the attribute group defined at the schema level that is refered to by this class.
     */
    public AttributeGroup resolveReference() {
        AttributeGroup attrGroup = null;
        //--check if there is no definition in the 
        //--master schema
        if (_schema.getMasterSchema() != null) {
        	attrGroup = _schema.getMasterSchema().getAttributeGroup(_reference);
        }
        
        if (attrGroup == null)
            attrGroup = _schema.getAttributeGroup(_reference);

        if (attrGroup == null) {
            throw new IllegalStateException("Invalid AttributeGroupReference");
        }
        return attrGroup;
    }

    /**
     * Returns the AttributeDecl associated with the given name
     * @return the AttributeDecl associated with the given name, or
     *  null if no AttributeDecl with the given name was found.
    **/
    public AttributeDecl getAttribute(String name) {
        return resolveReference().getAttribute(name);

    } //-- getAttribute


    /**
     * Returns an enumeration of the AttributeDecls and AttributeGroups
     * of this AttributeGroup
     *
     * @return an Enumeration of the AttributeDecls and AttributeGroups
     * of this AttributeGroup
    **/
    public Enumeration getAttributes() {
        return resolveReference().getAttributes();
    } //-- getAttributes

    /**
     * Returns true if this AttributeGroup does not contain any
     * AttributeDecls or any non-empty AttributeGroups
     *
     * @return true if this AttributeGroup does not contain any
     * AttributeDecls or any non-empty AttributeGroups
    **/
    public boolean isEmpty() {
        return resolveReference().isEmpty();

    } //-- isEmpty

} //-- AttributeGroupReference