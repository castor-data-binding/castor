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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * The XML Schema ComplexType class
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ComplexType extends XMLType
   implements ContentModelGroup, Referable
{

    private Hashtable attributes = null;

    private ContentType content  = ContentType.elemOnly;

    private String type = null;


    /**
     * The base type
    **/
    private String base = null;

    /**
     * The ContentModel for this ComplexType
    **/
    private ContentModelGroup _contentModel = null;

    /**
     * Creates a new Complextype, with no name
     * @param schema the owning Schema document
    **/
    public ComplexType(Schema schema) {
        this(schema,null);
    } //-- Complextype

    /**
     * Creates a new Complextype with the given name
     * @param schema the owning Schema
     * @param name of the Complextype
    **/
    public ComplexType(Schema schema, String name) {
        super();
        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }

        setSchema(schema);
        setName(name);
        attributes   = new Hashtable();
        _contentModel = new ContentModelGroupImpl();
    } //-- Complextype

    /**
     * Adds the given AttributeDecl to this Complextype
     * @param attrDecl the AttributeDecl to add to this Complextype
     * @exception SchemaException when an AttributeDecl already
     * exists with the same name as the given AttributeDecl
    **/
    public void addAttributeDecl(AttributeDecl attrDecl)
        throws SchemaException
    {

        if (attributes.get(attrDecl.getName()) != null) {
            String err = "An attribute declaration with the given name, ";
            err += attrDecl.getName() + ", already exists in this scope.";
            throw new SchemaException(err);
        }

        attributes.put(attrDecl.getName(), attrDecl);

    } //-- addAttributeDecl

    /**
     * Creates an AttributeDecl with the given name. The attribute
     * declaration will still need to be added to this Complextype,
     * or another archetype in the same schema, by making a call
     * to #addAttributeDecl
     * @param name the name of the attribute
     * @return the new AttributeDecl
    **/
    public AttributeDecl createAttributeDecl(String name) {
        return new AttributeDecl(getSchema(), name);
    } //-- createAttributeDecl

    /**
     * Returns the AttributeDecl of associated with the given name
     * @return the AttributeDecl of associated with the given name, or
     *  null if no AttributeDecl with the given name was found.
    **/
    public AttributeDecl getAttributeDecl(String name) {
        return (AttributeDecl)attributes.get(name);
    } //-- getAttributeDecl

    /**
     * Returns an Enumeration of all the AttributeDecl objects
     * declared within this Complextype
     * @return an Enumeration of all the AttributeDecl objects
     * declared within this Complextype
    **/
    public Enumeration getAttributeDecls() {
        return attributes.elements();
    } //-- getAttributeDecls

    /**
     * Returns the content type of this ComplexType
     * @return the content type of this ComplexType
    **/
    public ContentType getContentType() {
        return content;
    } //-- getContentType


    /**
     * Returns the Id used to Refer to this Object
     * @return the Id used to Refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        return "archetype:"+getName();
    } //-- getReferenceId


   /**
     * Returns the base type that this type inherits from.
     * @return the parent type.
    **/
    public XMLType getBaseType() {
        if ( (base != null) && (super.getBaseType() == null) )
            setBaseType( getSchema().getType(base) );
        return super.getBaseType();
    } //-- getBaseType

    /**
     * Returns true if this is a top level Complextype
     * @return true if this is a top level Complextype
    **/
    public boolean isTopLevel() {
        if (getName() == null) return false;
        if (getSchema() == null) return false;
        return (getSchema().getComplexType(getName()) == this);
    } //-- isTopLevel

    /**
     * Sets the content type of this archetype
     * @param contentType the ContentType for this archetype
    **/
    public void setContentType(ContentType contentType)
    {
        this.content = contentType;
    } //-- setContentType


    /**
     * Sets the base type that this type is derived from
     * @param base the type that this type is derived from
    **/
    public void setBase(String base) {
        this.base = base;
    } //-- setBase

    public void useResolver(Resolver resolver) {
        // do nothing for now
    }

    //---------------------------------------/
    //- Implementation of ContentModelGroup -/
    //---------------------------------------/

    /**
     * Adds the given ElementDecl to this ContentModelGroup
     * @param elementDecl the ElementDecl to add
     * @exception SchemaException when an ElementDecl already
     * exists with the same name as the given ElementDecl
    **/
    public void addElementDecl(ElementDecl elementDecl)
        throws SchemaException
    {
        _contentModel.addElementDecl(elementDecl);
    } //-- addElementDecl

    /**
     * Adds the given Group to this ContentModelGroup
     * @param group the Group to add
     * @exception SchemaException when a group with the same name as the
     * specified group already exists in the current scope
    **/
    public void addGroup(Group group)
        throws SchemaException
    {
        _contentModel.addGroup(group);
    } //-- addGroup

    /**
     * Returns an enumeration of all the Particles of this
     * ContentModelGroup
     *
     * @return an enumeration of the Particles contained
     * within this ContentModelGroup
    **/
    public Enumeration enumerate() {
        return _contentModel.enumerate();
    } //-- enumerate

    /**
     * Returns the Particle at the specified index
     * @param index the index of the particle to return
     * @returns the CMParticle at the specified index
    **/
    public Particle getParticle(int index) {
        return _contentModel.getParticle(index);
    } //-- getParticle
    
    /**
     * Returns the number of particles contained within
     * this ContentModelGroup
     *
     * @return the number of particles
    **/
    public int getParticleCount() {
        return _contentModel.getParticleCount();
    } //-- getParticleCount

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.COMPLEX_TYPE;
    } //-- getStructureType


} //-- Complextype
