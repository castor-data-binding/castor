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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * The XML Schema Archetype class
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Archetype extends ContentModelGroup 
    implements Referable
{

    /**
     * Error message for a null argument
    **/
    private static String NULL_ARGUMENT
        = "A null argument was passed to the constructor of " +
           Archetype.class.getName();
    
    private String name = null;
    
    private Hashtable attributes = null;
    
    private ContentType content  = ContentType.elemOnly;
    
    private String type = null;
    
    /**
     * The owning schema document
    **/
    private Schema schema = null;
    
    /**
     * The source type
    **/
    private String source = null;
    
    /**
     * Creates a new Archetype, with no name
     * @param schema the owning Schema document
    **/
    public Archetype(Schema schema) {
        this(schema,null);
    } //-- Archetype
    
    /**
     * Creates a new Archetype with the given name
     * @param schema the owning Schema
     * @param name of the Archetype
    **/
    public Archetype(Schema schema, String name) {
        super();
        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        this.schema = schema;
        this.name = name;
        attributes   = new Hashtable();
    } //-- Archetype
    
    /**
     * Adds the given AttributeDecl to this Archetype
     * @param attrDecl the AttributeDecl to add to this Archetype
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
     * declaration will still need to be added to this Archetype,
     * or another archetype in the same schema, by making a call
     * to #addAttributeDecl
     * @param name the name of the attribute
     * @return the new AttributeDecl
    **/
    public AttributeDecl createAttributeDecl(String name) {
        return new AttributeDecl(this.schema, name);
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
     * declared within this Archetype
     * @return an Enumeration of all the AttributeDecl objects
     * declared within this Archetype
    **/
    public Enumeration getAttributeDecls() {
        return attributes.elements();
    } //-- getAttributeDecls

    /**
     * Returns the content type of this archetype
     * @return the content type of this archetype
    **/
    public ContentType getContent() {
        return content;
    } //-- getContent
    
    /**
     * Returns the name of this Archetype, or null if no name was defined
     * @return the name of this Archetype, or null if no name was defined
    **/
    public String getName() {
        return name;
    } //-- getName
    
    /**
     * Returns the Id used to Refer to this Object
     * @return the Id used to Refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        return "archetype:"+name;
    } //-- getReferenceId


    /**
     * Returns the Schema with which this Archetype belongs
     * @return the Schema with which this Archetype belongs
    **/
    public Schema getSchema() {
        return this.schema;
    } //-- getSchema
    
    /**
     * Returns the source type with which this type extends,
     * or null if this type is not a derived type.
     * @return the source type with which this type extends,
     * or null if this type is not a derived type.
    **/
    public String getSource() {
        return source;
    } //-- getSource
    
    /**
     * Returns true if this is a top level Archetype
     * @return true if this is a top level Archetype
    **/
    public boolean isTopLevel() {
        if (name == null) return false;
        if (schema == null) return false;
        return (schema.getArchetype(name) == this);
    } //-- isTopLevel
    
    /**
     * Sets the content type of this archetype
     * @param contentType the ContentType for this archetype
    **/
    public void setContent(ContentType contentType) 
    {
        this.content = contentType;
    } //-- setContent
    

    /**
     * Sets the name of this Archetype
     * @param name the new name for this Archetype
    **/
    public void setName(String name) {
        this.name = name;
    } //--setName
    
    /**
     * Sets the source type that this type is derived from
     * @param source the type that this type is derived from
    **/
    public void setSource(String source) {
        this.source = source;
    } //-- setSource
    
    public void useResolver(Resolver resolver) {
        // do nothing for now
    }
    
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ARCHETYPE;
    } //-- getStructureType
    
    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
        //-- do nothing
    } //-- validate
        
} //-- Archetype