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
 * An XML Schema Definition
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Schema extends SchemaBase {
    
    public static final String DEFAULT_SCHEMA_NS
        = "http://www.w3.org/TR/1999/09/24-xmlschema";
        
    private String name     = null;
    private String schemaNS = null;
    private String targetNS = null;
    
    
    /**
     * A list of defined architypes
    **/
    private Hashtable archetypes = null;
    
    /**
     * A list of defined elements
    **/
    private Hashtable elements = null;
    
    /**
     * Creates a new SchemaDef
    **/
    public Schema() {
        this(DEFAULT_SCHEMA_NS);
    } //-- ScehamDef

    
    /**
     * Creates a new SchemaDef
    **/
    public Schema(String schemaNS) {
        super();
        archetypes = new Hashtable();
        elements = new Hashtable();
        this.schemaNS = schemaNS;
    } //-- ScehamDef
    
    /**
     * Adds the given Archetype definition to this Schema defintion
     * @param archetype the Archetype to add to this SchemaDef
     * @exception SchemaException if the Archetype does not have
     * a name or if another Archetype already exists with the same name
    **/
    public void addArchetype(Archetype archetype) 
        throws SchemaException 
    {
        
        String name = archetype.getName();
        
        if (name == null) {
            String err = "a global archetype must contain a name.";
            throw new SchemaException(err);
        }
        if (archetypes.get(name) != null) {
            String err = "an archetype already exists with the given name: ";
            throw new SchemaException(err + name);
        }
        
        archetypes.put(name, archetype);
        
    } //-- addArchetype

    /**
     * Adds the given Element declaration to this Schema defintion
     * @param elementDecl the ElementDecl to add to this SchemaDef
     * @exception SchemaException when an ElementDecl already
     * exists with the same name as the given ElementDecl
    **/
    public void addElementDecl(ElementDecl elementDecl) 
        throws SchemaException 
    {
        
        String name = elementDecl.getName();
        
        if (name == null) {
            String err = "an element declaration must contain a name.";
            throw new SchemaException(err);
        }
        if (elements.get(name) != null) {
            String err = "an element declaration already exists with the given name: ";
            throw new SchemaException(err + name);
        }
        
        elements.put(name, elementDecl);
        
        
    } //-- addElementDecl
    
    
    /**
     * Returns the Archetype of associated with the given name
     * @return the Archetypel of associated with the given name, or
     *  null if no Archetype with the given name was found.
    **/
    public Archetype getArchetype(String name) {
        return (Archetype)archetypes.get(name);
    } //-- getArchetype
    
    /**
     * Returns the type of this SchemaBase
     * @return the type of this SchemaBase
     * @see org.exolab.xml.schema.SchemaBase
    **/
    public short getDefType() {
        return SchemaBase.SCHEMA;
    } //-- getDefType
    
    /**
     * Returns the ElementDecl of associated with the given name
     * @return the ElementDecl of associated with the given name, or
     *  null if no ElementDecl with the given name was found.
    **/
    public ElementDecl getElementDecl(String name) {
        return (ElementDecl)elements.get(name);
    } //-- getElementDecl

    /**
     * Returns an Enumeration of all the ElementDecl objects
     * declared at the top level of this SchemaDef
     * @return an Enumeration of all the ElementDecl objects
     * declared at the top level of this SchemaDef
    **/
    public Enumeration getElementDecls() {
        return elements.elements();
    } //-- getElementDecls
    
    /**
     * Sets the name of this Schema definition
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName
    
    /**
     * Checks the validity of this Attribute declaration
     * @exception ValidationException when this Attribute declaration
     * is invalid
    **/
    public void validate() throws ValidationException {
        
    } //-- validate
    
} //-- SchemaDef
    
    
