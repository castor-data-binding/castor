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

/**
 * An XML Schema ElementDecl
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ElementDecl extends ContentModelType 
    implements Referable
{
    
    /**
     * The maximum number of occurances of that elements of this type
     * may appear as children of it's context
    **/
    private int maxOccurs = 0;
    
    /**
     * The minimum number of occurances of this element that must
     * exist in it's parent context
    **/
    private int minOccurs = 1;
    
    
    /**
     * The element name
    **/
    private String name = null;

         
    /**
     * The type of this element
    **/
    private String typeRef = null;
    
    
    /**
     * the element definition that this element definition references
    **/
    ResolvableReference reference = null;
    
    /**
     * The reference id for the resolvable reference, I need
     * this since ResolvableReference will not give me
     * access to the ID
    **/
    String ref = null;
    
    
    /**
     * The Schema URI for this Element Declaration
    **/
    private String schemaName = null;
    
    /**
     * The Schema Abbreviation for the Schema name
    **/
    private String schemaAbbrev = null;
    
    /**
     * The Archetype of this ElementDecl
    **/
    private Archetype archetype = null;
    
    Resolver _resolver = null;
    
    /**
     * Creates a new default element definition
     * <BR />This element definition will not be valid until a name has
     * been set
    **/
    public ElementDecl() {
        super();
    } //-- ElementDecl
    
    /**
     * Creates a new default element definition
     * <BR />This element definition will not be valid until a name has
     * been set
    **/
    public ElementDecl(String name) {
        super();
        this.name = name;
    } //-- ElementDecl
    
    
    /**
     *
    **/
    public void setArchetype(Archetype archetype) {
        this.archetype = archetype;
    } //-- addAttribute
    
    /**
     * Returns the maximum number of occurances that this element
     * must appear within it's parent context
     * @return the maximum number of occurances that this element
     * must appear within it's parent context
    **/
    public int getMaximumOccurance() {
        return maxOccurs;
    } //-- getMaximumOccurance

    /**
     * Returns the minimum number of occurances that this element
     * must appear within it's parent context
     * @return the minimum number of occurances that this element
     * must appear within it's parent context
    **/
    public int getMinimumOccurance() {
        return minOccurs;
    } //-- getMinimumOccurance
    
    public String getName() {
        if (isReference()) {
            return ref;
        }
        else return name;
    } //-- getName
    
    public Archetype getArchetype() {
        if (archetype == null) {
            //-- try resolving
            if (_resolver != null) {
                return (Archetype) _resolver.resolve("archetype:"+typeRef);
            }
        }
        return archetype;
    } //-- getAttributes

    
    /**
     * Returns the type of this SchemaBase
     * @return the type of this SchemaBase
     * @see org.exolab.xml.schema.SchemaBase
    **/
    public short getDefType() {
        return SchemaBase.ELEMENT;
    } //-- getDefType
    
    /**
     * Returns the Id used to Refer to this Object
     * @return the Id used to Refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        if (ref != null) return "node:"+ref;
        return "node:"+name;
    } //-- getReferenceId
    
    /**
     * Returns the ElementDecl that this element definition references.
     * This will return null if this element definition does not reference
     * a different element definition.
     * @return the ElementDecl that this element definition references
    **/
    public ElementDecl getReference() {
        return (ElementDecl)reference.get();
    } //-- getReference
    
    /**
     * Returns the Schema Abbreviation for the Schema name
     * @return the Schema Abbreviation for the Schema name
    **/
    public String getSchemaAbbrev() {
        return this.schemaAbbrev;
    } //-- getSchemaAbbrev
    
    /**
     * Returns the Schema Name for this Element declaration.
     * @return the Schema Name for this Element declaration.
    **/
    public String getSchemaName() {
        return this.schemaName;
    } //-- getSchemaName
    
    /**
     * Returns true if this element definition simply references another
     * element Definition
     * @return true if this element definition is a reference
    **/
    public boolean isReference() {
        return (reference != null);
    } //-- isReference
    
    /** 
     * Sets the maximum number of occurances that this element must
     * appear within it's parent context
     * @param maxOccurs the maximum number of occurances that this 
     * element may appear within it's parent context
    **/
    public void setMaximumOccurance(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    } //-- setMaximumOccurance
    
    /** 
     * Sets the minimum number of occurances that this element must
     * appear within it's parent context
     * @param minOccurs the number of occurances that this element must
     * appeae within it's parent context
    **/
    public void setMinimumOccurance(int minOccurs) {
        this.minOccurs = minOccurs;
    } //-- setMinimumOccurance
    
    /**
     * Sets the name of the element that this Element definition defines
     * @param name the name of the defined element
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName
    
    /**
     * Sets the reference for this element definition. 
     * @param id the id of the element definition in which to reference
     * @param resovler the Resolver in which to resolve the reference
     * when the getReference method is invoked. If this is null, the 
     * default resolver will be used. 
     * @see useResolver
    **/
    public void setReference(String id, Resolver resolver) {
        if (resolver == null) resolver = _resolver;
        
        //-- create new reference...add namespace "node:" to id
        
        this.reference = new ResolvableReference("node:"+id, resolver);
        this.ref = id;
    } //-- setReference
    
    
    /**
     * Sets the reference for this element definition
     * @param reference the Element definition that this definition references
    **/
    public void setReference(ElementDecl reference) {
        if (reference == null) this.reference = null;
        else {
            //-- safegaurd agaist referencing a reference
            //-- which could get ugly, this will never happen
            //-- if reading from an XML Schema since you
            //-- cannot express a reference in such a fashion
            //-- but someone could programmitcally try to do it
            if (reference.isReference()) {
                reference = reference.getReference();
                setReference(reference);
            }
            else {
                this.reference = new ResolvableReference(reference);
                this.ref = reference.getName();
            }
        }
    } //-- setReference
    
    /**
     * Sets the Schema Abbreviation for the Schema name
     * @param abbrev the Schema Abbreviation for the Schema name
     * @see #setSchemaName
    **/
    public void setSchemaAbbrev(String abbrev) {
        this.schemaAbbrev = abbrev;
    } //-- setSchemaAbbrev
    
    /**
     * Sets the Schema Name for this Element declaration.
     * @param uri, the Schema Name, which is a URI for this element 
     * declaration
     * @see #setSchemaAbbrev
    **/
    public void setSchemaName(String uri) {
        this.schemaName = uri;
    } //-- setSchemaName
    
    
    /**
     * Sets the type reference for this element (either archetype or
     * datatype)
    **/
    public void setTypeRef(String typeRef) {
        this.typeRef = typeRef;
    } //-- setTypeRef
    
    public String getTypeRef() {
        return typeRef;
    } //-- getTypeRef
    
    /**
     * Sets the Resolver to be used for resolving IDREFs
    **/
    public void useResolver(Resolver resolver) {
        _resolver = resolver;
    } //-- useResolver
    
    /**
     * Checks the validity of this element definition
     * @exception ValidationException when this element definition
     * is invalid
    **/
    public void validate() throws ValidationException {
        
        //-- if this merely references another element definition
        //-- just check that we can resolve the reference
        if (reference != null) {
            if (!reference.resolvable()) {
                String err = "<element ref=\"" + ref + "\">  is not resolvable.";
                throw new ValidationException(err);
            }
        }
        else if (name == null)  {
            String err = "<element> is missing required 'name' or 'ref' attribute.";
            throw new ValidationException(err);
        }
    } //-- validate
    
    
} //-- Element