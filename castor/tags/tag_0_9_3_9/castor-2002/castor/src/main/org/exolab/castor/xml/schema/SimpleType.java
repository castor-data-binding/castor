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

import java.util.Enumeration;

/**
 * An XML Schema SimpleType
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/

public abstract class SimpleType extends XMLType
    implements Referable
{

    /**
     * The constraining facets of this type
    **/
    private FacetList facets     = null;


   /**
     * The parent structure of this SimpleType
     * (Schema, AttributeDecl or ElementDecl)
    **/
    private Structure parent = null;


    /**
     * The code for this simple type
     * (As defined by SimpleTypesFactory)
    **/
    private int typeCode= SimpleTypesFactory.INVALID_TYPE;


    /**
     * Default constructor
     */
    public SimpleType() {
        super();
        this.facets  = new FacetList();
    }


    /**
     * Adds the given Facet to this Simpletype.
     * @param facet the Facet to add to this Simpletype
    **/
    public void addFacet(Facet facet) {

        if (facet == null) return;

        String name = facet.getName();

        if (name == null) return;

        facets.add(facet);

    } //-- addFacet

    /**
     * Returns the first facet associated with the given name
     * @return the first facet associated with the given name
    **/
    public Facet getFacet(String name) {
        Enumeration facets= getFacets(name);

        if (facets == null) return null;

        return (Facet)facets.nextElement();
    } //-- getFacet

    /**
     * Returns the facets associated with the given name
     * @return the facets associated with the given name
    **/
    public Enumeration getFacets(String name) {
        FacetListEnumerator fle = null;
        SimpleType datatype = (SimpleType)getBaseType();
        if (datatype != null) {
            fle = (FacetListEnumerator)datatype.getFacets(name);
        }
        fle = new FacetListEnumerator(facets, fle);
        fle.setMask(name);
        return fle;
    } //-- getFacets

    /**
     * Returns an Enumeration of all the Facets (including inherited)
     * facets for this type.
     * @return an Enumeration of all the Facets for this type
    **/
    public Enumeration getFacets() {
        FacetListEnumerator fle = null;
        SimpleType datatype = (SimpleType)getBaseType();
        if (datatype != null) {
            fle = (FacetListEnumerator)datatype.getFacets();
        }
        fle = new FacetListEnumerator(facets, fle);
        return fle;
    } //-- getFacets


    /**
     * Returns the facets of this type (without the parent's facets)
     */
    public Enumeration getLocalFacets()    {
        if (facets == null) return null;
        return facets.enumerate();
    }


    /**
     * Returns the built in type this type is derived from.
     */
    public SimpleType getBuiltInBaseType()
    {
        SimpleType base = this;
        while ((base != null) && ( ! SimpleTypesFactory.isBuiltInType( base.getTypeCode() ) )) {
            base = (SimpleType)base.getBaseType();
        }
        return base;
    }

    /**
     * Returns the parent Structure that contains this SimpleType.
     * This can be either a Schema, AttributeDecl or ElementDecl.
     * @return the parent of this SimpleType
    **/
    public Structure getParent() {
        return parent;
    } //-- getParent

    /**
     * Returns the Id used to Refer to this Object.
     * @return the Id used to Refer to this Object
     * @see org.exolab.castor.xml.schema.Referable
    **/
    public String getReferenceId() {
        return "datatype:"+getName();
    } //-- getReferenceId


    /**
     * Returns true if this Simpletype has a specified Facet
     * with the given name.
     * @param name the name of the Facet to look for
     * @return true if this Simpletype has a specified Facet
     * with the given name
    **/
    public boolean hasFacet(String name) {
        if (name == null) return false;
        for (int i = 0; i < facets.size(); i++) {
            Facet facet = (Facet) facets.get(i);
            if (name.equals(facet.getName())) return true;
        }
        return false;
    } //-- hasFacet


    /**
     * Returns true if this SimpleType is a built in type
     * @return true if this SimpleType is a built in type
    **/
    public boolean isBuiltInType() {
        return SimpleTypesFactory.isBuiltInType( typeCode );
    } //-- isBuiltInType
    
    /**
     * Gets the code for this simple type 
     * (as defined in SimpleTypesFactory)
     *
     * @return the type code for this simple type
    **/
    public int getTypeCode() { return typeCode; }


    /** Package private setter of the code for this simple type **/
    void setTypeCode(int code) { typeCode= code; }



    /////////////////////////////////////////////////////////
    // Helpers to get the min/max/length facets
    // (so that they are shared between listType
    // and binary, uriref, string
    //

    /**
     *  Returns the value of the length facet
     *  result can be null
    **/
    public Long getLength()
    {
        Facet lengthFacet= getFacet(Facet.LENGTH);
        if (lengthFacet == null) return null;

        try
        {
            return new Long(lengthFacet.toLong());
        }
        catch (java.lang.Exception e)
        {
            return null;
        }
    }

    /**
     *  Returns the value of the minlength facet
     *  result can be null
    **/
    public Long getMinLength()
    {
        Facet minLengthFacet= getFacet(Facet.MIN_LENGTH);
        if (minLengthFacet == null) return null;

        try
        {
            return new Long(minLengthFacet.toLong());
        }
        catch (java.lang.Exception e)
        {
            return null;
        }
    }

    /**
     *  Returns the value of the maxlength facet
     *  result can be null
    **/
    public Long getMaxLength()
    {
        Facet maxLengthFacet= getFacet(Facet.MAX_LENGTH);
        if (maxLengthFacet == null) return null;

        try
        {
            return new Long(maxLengthFacet.toLong());
        }
        catch (java.lang.Exception e)
        {
            return null;
        }
    }



    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.SIMPLE_TYPE;
    } //-- getStructureType


    //-- protected Methods -/

    /**
     * Sets the parent for this SimpleType
     * @param parent the Structure that contains this SimpleType.
     * Currently this should only be Schema, ElementDecl or AttributeDecl.
    **/
    protected void setParent(Structure parent) {
        this.parent = parent;
    } //-- setParent

    /**
     * Copy this type's facet in the other type
     * (other.facets= facets; )
     */
    protected void copyFacets(SimpleType other) { other.facets= facets; }

} //-- DataType
