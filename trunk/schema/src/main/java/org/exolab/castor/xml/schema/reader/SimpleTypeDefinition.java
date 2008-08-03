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
 * Copyright 2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.simpletypes.UrType;

import java.util.Enumeration;

/**
 * A simple class used when unmarshalling simpleTypes
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-10-01 07:25:46 -0600 (Fri, 01 Oct 2004) $
**/
public class SimpleTypeDefinition {


    private String      _name           = null;
    private String      _id             = null;
    private String      _final          = null;
    private Schema      _schema         = null;
    private SimpleType  _baseType       = null;
    private String      _baseTypeName   = null;
    private Annotation  _annotation     = null;
    private FacetList   _facets         = null;
    
    public SimpleTypeDefinition(Schema schema, String name, String id) {
        super();
        this._schema = schema;
        this._name   = name;
        this._id     = id;
        
        _facets = new FacetList();
        
    } //-- SimpleTypeDefinition
    
    /**
     * Adds the given Facet to the list of Facets for this 
     * SimpleTypeDefinition
     *
     * @param facet the Facet to add
    **/
    public void addFacet(Facet facet) {
        _facets.add(facet);
    } //-- addFacet
    
    /**
     * Copies the name, facets and annotations of this SimpleTypeDefinition
     * into the given SimpleType.
     *
     * @param simpleType the SimpleType to copy into.
    **/
    void copyInto(SimpleType simpleType) {
        
        //-- set name
        simpleType.setName(_name);
        
        //-- set Schema
        simpleType.setSchema(_schema);
        
        //-- @id
        simpleType.setId(_id);
        
        //-- @final
        simpleType.setFinal(_final);
        
        //-- copy Facets
        Enumeration enumeration = _facets.enumerate();
        while (enumeration.hasMoreElements())
            simpleType.addFacet((Facet)enumeration.nextElement());
            
        if (_annotation != null)
            simpleType.addAnnotation(_annotation);
    } //-- copyInto
    
    /**
     * Creates the SimpleType instance which represents this 
     * SimpleTypeDefinition
     *
     * @return the new SimpleType instance.
    **/
    public SimpleType createSimpleType() {
        SimpleType simpleType = null;
        
        if (_baseType != null)
            simpleType = _schema.createSimpleType(_name, _baseType);
        else if (_baseTypeName != null) 
            simpleType = _schema.createSimpleType(_name, 
                                                  _baseTypeName, 
                                                  "restriction");
        else {
            simpleType = new UrType();
        }
        
        
        //-- @id
        simpleType.setId(_id);
        
        //-- @final
        simpleType.setFinal(_final);
        
        //-- copy Facets
        Enumeration enumeration = _facets.enumerate();
        while (enumeration.hasMoreElements())
            simpleType.addFacet((Facet)enumeration.nextElement());
            
        if (_annotation != null)
            simpleType.addAnnotation(_annotation);
            
        return simpleType;
        
    } //-- createSimpleType
    
    /**
     * Returns the Schema for this SimpleTypeDefinition
     *
     * @return the Schema for this SimpleTypeDefinition
    **/
    Schema getSchema() {
        return _schema;
    } //-- getSchema
    
    /**
     * Sets the annotation for this SimpleTypeDefinition
     *
     * @param annotation the Annotation for this SimpleTypeDefinition
    **/
    void setAnnotation(Annotation annotation) {
        _annotation = annotation;
    } //-- setAnnotation
    
    /**
     * Sets the base type for this SimpleTypeDefinition. This
     * method is mutually exclusive with #setBaseTypeName
     * 
     * @param baseType the base type for this SimpleTypeDefinition
    **/
    public void setBaseType(SimpleType baseType) {
        _baseType = baseType;
        _baseTypeName = null;
    } //-- setBaseType

    /**
     * Sets the base type for this SimpleTypeDefinition. This
     * method is mutually exclusive with #setBaseType
     * 
     * @param baseTypeName the base type for this SimpleTypeDefinition
    **/
    void setBaseTypeName(String baseTypeName) {
        _baseTypeName = baseTypeName;
        _baseType = null;
    } //-- setBaseTypeName
    
    /**
     * Sets the value of the 'final' property, indicating which
     * types of derivation are not allowed. A null value will indicate
     * all types of derivation (list, restriction, union) are allowed.
     *
     * @param finalValue the value of the final property.
    **/
    public void setFinal(String finalValue) {
        _final = finalValue;
    }

} //-- SimpleTypeDefinition


