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

import org.exolab.castor.xml.ValidationException;

/**
 * An XML Schema Attribute Group Definition
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public abstract class AttributeGroup extends Annotated {

    /**
     * The id of this AttributeGroup
    **/
    private String _id = null;

    /**
     * Returns the anyAttribute set in this attribute group if any.
     * @return the anyAttribute set in this attribute group if any.
     */
    public abstract Wildcard getAnyAttribute();

    /**
     * Returns the AttributeDecl associated with the given name
     * @return the AttributeDecl associated with the given name, or
     *  null if no AttributeDecl with the given name was found.
    **/
    public abstract AttributeDecl getAttribute(String name);

    /**
     * Returns an Enumeration of all the attributes of this
     * attribute group. The enumeration includes attributes from
     * all AttributeGroupReferences contained in this AttributeGroup.
     *
     * @return an Enumeration of all the attributes of this
     * attribute group.
    **/
    public abstract Enumeration getAttributes();

    /**
     * Returns the id of this AttributeGroup
     * @return the id of this AttributeGroup, or null, if
     * no id was defined.
    **/
    public String getId() {
        return _id;
    } //-- getId

    /**
     * Returns true if this AttributeGroup does not contain any
     * AttributeDecls or any non-empty AttributeGroupReferences
     *
     * @return true if this AttributeGroup does not contain any
     * AttributeDecls or any non-empty AttributeGroupReferences
    **/
    public abstract boolean isEmpty();

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ATTRIBUTE_GROUP;
    } //-- getStructureType

    /**
     * Sets the id of this AttributeGroup
     * @param id the id of this AttributeGroup
    **/
    public void setId(String id) {
        this._id = id;
    } //-- setId

    /**
     * Checks the validity of this Attribute declaration
     * @exception ValidationException when this Attribute declaration
     * is invalid
    **/
    public void validate()
        throws ValidationException
    {
        //-- no validation currently needed

    } //-- validate


} //-- AttributeGroup