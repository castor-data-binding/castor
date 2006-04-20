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
import org.exolab.castor.xml.schema.types.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * An XML Schema Definition. This class also contains the Factory methods for
 * creating Top-Level structures.
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Schema extends Structure {
    
    public static final String DEFAULT_SCHEMA_NS
        = "http://www.w3.org/TR/1999/09/24-xmlschema";
        
        
    private static final String NULL_ARGUMENT
        = "A null argument was passed to " + 
           Schema.class.getName() + "#";
           
    private String name     = null;
    private String schemaNS = null;
    private String targetNS = null;
    
    
    /**
     * A list of defined architypes
    **/
    private Hashtable archetypes = null;
    
    
    /**
     * A list of defined datatypes
    **/
    private Hashtable datatypes = null;

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
        datatypes  = new Hashtable();
        elements   = new Hashtable();
        this.schemaNS = schemaNS;
        init();
    } //-- ScehamDef
    
    private void init() {
        
        //-- create default built-in types for this Schema
       
        try {
            //-- ID
            addDatatype(new IDType(this));
            //-- IDREF
            addDatatype(new IDREFType(this));
            //-- NCName
            addDatatype(new NCNameType(this));
            //-- NMTOKEN
            addDatatype(new NMTokenType(this));
            
            //-- binary
            addDatatype(new BinaryType(this));
            //-- boooean
            addDatatype(new BooleanType(this));
            //-- double
            addDatatype(new DoubleType(this));
            //-- integer
            addDatatype(new IntegerType(this));
            //-- long
            addDatatype(new LongType(this));
            //-- string
            addDatatype(new StringType(this));
            //-- timeInstant
            addDatatype(new TimeInstantType(this));
        }
        catch (SchemaException sx) {
            //-- will never be thrown here since we
            //-- are not adding invalid datatypes
        }
    } //-- init
    
    /**
     * Adds the given Archetype definition to this Schema defintion
     * @param archetype the Archetype to add to this Schema
     * @exception SchemaException if the Archetype does not have
     * a name or if another Archetype already exists with the same name
    **/
    public synchronized void addArchetype(Archetype archetype) 
        throws SchemaException 
    {
        
        String name = archetype.getName();
        
        if (name == null) {
            String err = "a global archetype must contain a name.";
            throw new SchemaException(err);
        }
        if (archetype.getSchema() != this) {
            String err = "invalid attempt to add an archetype which ";
            err += "belongs to a different Schema; type name: " + name;
        }
        if (archetypes.get(name) != null) {
            String err = "an archetype already exists with the given name: ";
            throw new SchemaException(err + name);
        }
        archetypes.put(name, archetype);
        
    } //-- addArchetype

    /**
     * Adds the given Datatype definition to this Schema defintion
     * @param datatype the Datatype to add to this Schema
     * @exception SchemaException if the Archetype does not have
     * a name or if another Archetype already exists with the same name
    **/
    public synchronized void addDatatype(Datatype datatype) 
        throws SchemaException 
    {
        
        String name = datatype.getName();
        
        if (datatype.getSchema() != this) {
            String err = "invalid attempt to add a datatype which ";
            err += "belongs to a different Schema; type name: " + name;
        }
        if (datatypes.get(name) != null) {
            String err = "a datatype already exists with the given name: ";
            throw new SchemaException(err + name);
        }
        datatypes.put(name, datatype);
        
    } //-- addDatatype

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
     * Creates a new Archetype using this Schema as the owning Schema
     * document. A call to #addArchetype must still be made in order
     * to add the archetype to this Schema.
     * @return the new Archetype
    **/
    public Archetype createArchetype() {
        return new Archetype(this);
    } //-- createArchetype
    
    /**
     * Creates a new Archetype using this Schema as the owning Schema
     * document. A call to #addArchetype must still be made in order
     * to add the archetype to this Schema.
     * @param name the name of the Archetype 
     * @return the new Archetype
    **/
    public Archetype createArchetype(String name) {
        return new Archetype(this, name);
    } //-- createArchetype
    
    /**
     * Creates a new Datatype using this Schema as the owning Schema
     * document. A call to #addDatatype must till be made in order
     * to add the Datatype to this Schema.
     * @param name the name of the Datatype
     * @return the new Datatype.
    **/
    public Datatype createDatatype(String name) {
        return new Datatype(this, name);
    } //-- createDatatype(String)
    
    /**
     * Returns the Archetype of associated with the given name
     * @return the Archetypel of associated with the given name, or
     *  null if no Archetype with the given name was found.
    **/
    public Archetype getArchetype(String name) {
        if (name == null)  {
            String err = NULL_ARGUMENT + "getArchetype: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }
        return (Archetype)archetypes.get(name);
    } //-- getArchetype
    
    /**
     * Returns an Enumeration of all top-level Archetype declarations
     * @return an Enumeration of all top-level Archetype declarations
    **/
    public Enumeration getArchetypes() {
        return archetypes.elements();
    } //-- getArchetypes
    
    /**
     * Returns the Datatype associated with the given name,
     * or null if no such Datatype exists.
     * @return the Datatype associated with the given name,
     * or null if no such Datatype exists.
    **/
    public Datatype getDatatype(String name) {
        if (name == null)  {
            String err = NULL_ARGUMENT + "getDatatype: ";
            err += "'name' cannot be null.";
            throw new IllegalArgumentException(err);
        }
        return (Datatype)datatypes.get(name);
    } //-- getDatatype
    
    /**
     * Returns an Enumeration of all Datatype declarations
     * @return an Enumeration of all Datatype declarations
    **/
    public Enumeration getDatatypes() {
        return datatypes.elements();
    } //-- getDatatypes
    
    /**
     * Returns the ElementDecl of associated with the given name
     * @return the ElementDecl of associated with the given name, or
     *  null if no ElementDecl with the given name was found.
    **/
    public ElementDecl getElementDecl(String name) {
        return (ElementDecl)elements.get(name);
    } //-- getElementDecl

    /**
     * Returns an Enumeration of all top-level element declarations
     * @return an Enumeration of all top-level element declarations
    **/
    public Enumeration getElementDecls() {
        return elements.elements();
    } //-- getElementDecls
    
    /**
     * Returns the target namespace for this Schema, or null if no
     * namespace has been defined.
     * @return the target namespace for this Schema, or null if no
     * namespace has been defined
    **/
    public String getTargetNamespace() {
        return this.targetNS;
    } //-- getTargetNamespace
    
    /**
     * Removes the given top level Archetype from this Schema
     * @param archetype the Archetype to remove
     * @return true if the archetype has been removed, or
     * false if the archetype wasn't top level or
     * didn't exist in this Schema
    **/
    public boolean removeArchetype(Archetype archetype) {
        if (archetype.isTopLevel()) {
            if (archetypes.contains(archetype)) {
                archetypes.remove(archetype);
                return true;
            }
        }
        return false;
    } //-- removeArchetype
    
    /**
     * Sets the name of this Schema definition
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName
    
    /**
     * Sets the target namespace for this Schema
     * @param targetNamespace the target namespace for this Schema
     * @see <B>&sect; 2.7 XML Schema Part 1: Structures</B>
    **/
    public void setTargetNamespace(String targetNamespace) {
        this.targetNS = targetNamespace;
    } //-- setTargetNamespace

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.SCHEMA;
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
    
} //-- SchemaDef
    
    
