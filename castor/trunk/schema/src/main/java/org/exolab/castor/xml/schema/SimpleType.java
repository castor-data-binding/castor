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

import java.util.Enumeration;

import org.exolab.castor.xml.ValidationException;

/**
 * An XML Schema SimpleType.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
**/

public abstract class SimpleType extends XMLType
    implements Referable {

    /**
     * The value of the final attribute used for 
     * blocking all types of derivation.
    **/
    public static final String FINAL_ALL         = "#all";
    
    /**
     * The value of the final attribute used for 
     * blocking list derivation.
    **/
    public static final String FINAL_LIST        = "list";
    
    /**
     * The value of the final attribute used for 
     * blocking union derivation.
    **/
    public static final String FINAL_UNION       = "union";
    
    /**
     * The value of the final attribute used for 
     * blocking restriction derivation.
    **/
    public static final String FINAL_RESTRICTION = "restriction";
    
    /**
     * The constraining facets of this type.
    **/
    private FacetList _facets = null;

    /**
     * The value of the final attribute (optional).
    **/
    private String _final = null;
    
    /**
     * The parent structure of this {@link SimpleType}.
     * (Schema, AttributeDecl or ElementDecl)
    **/
    private Structure _parent = null;


    /**
     * The code for this simple type.
     * (As defined by SimpleTypesFactory)
    **/
    private int _typeCode = SimpleTypesFactory.INVALID_TYPE;

    /**
     * An attribute that indicates if this {@link SimpleType} is
     * a redefinition.
     */
    private boolean _redefinition = false;
    
    /**
     * Default constructor.
     */
    public SimpleType() {
        super();
        this._facets  = new FacetList();
    }


    /**
     * Adds the given Facet to this Simpletype.
     * @param facet the Facet to add to this Simpletype
    **/
    public void addFacet(final Facet facet) {

        if (facet == null) {
            return;
        }

        String name = facet.getName();

        if (name == null) {
            return;
        }

        _facets.add(facet);

    }

    /**
     * Returns the first facet associated with the given name.
     * @param name the name of the Facet to look for
     * @return the first facet associated with the given name
    **/
    public Facet getFacet(final String name) {
        Enumeration facets = getFacets(name);

        if (facets == null) {
            return null;
        }

        return (Facet) facets.nextElement();
    }

    /**
     * Returns the facets associated with the given name.
     * @param name the name of the Facet to look for
     * @return the facets associated with the given name
    **/
    public Enumeration getFacets(final String name) {
        FacetListEnumerator fle = null;
        SimpleType datatype = (SimpleType) getBaseType();
        if (datatype != null) {
            fle = (FacetListEnumerator) datatype.getFacets(name);
        }
        fle = new FacetListEnumerator(_facets, fle);
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
        SimpleType datatype = (SimpleType) getBaseType();
        if (datatype != null) {
            fle = (FacetListEnumerator) datatype.getFacets();
        }
        fle = new FacetListEnumerator(_facets, fle);
        return fle;
    }


    /**
     * Returns the value of the 'final' property, indicating which
     * types of derivation are not allowed, or null if the final property
     * has not been set.
     *
     * @return the value of the final property or null if no value has
     * been set
    **/
    public String getFinal() {
        return _final;
    }
    
    /**
     * Returns the facets of this type (without the parent's facets).
     * @return the local facets of this type.
     */
    public Enumeration getLocalFacets()    {
        if (_facets == null) {
            return null;
        }
        return _facets.enumerate();
    }

    /**
     * Returns an enumeration of the effective facets for this type.
     * A set of effective facets contains all local facets
     * and only those inherited facets that are not overridden
     * by the local facets.
     *
     * @return an enumeration of the effective facets for this type.
     *
     * @see #getLocalFacets()
     * @see #getFacets() 
     */
    public Enumeration getEffectiveFacets() {
        final Enumeration localFacets = getLocalFacets();
        final SimpleType baseType = (SimpleType) getBaseType();
        if (baseType == null) {
            // There's no base type ==> return local facets
            return localFacets;
        }
        final Enumeration effectiveBaseFacets = baseType.getEffectiveFacets();
        if (localFacets == null) {
            // There's a base type, but no local facets ==> return
            // effective facets of the base type
            return effectiveBaseFacets;
        }
        // There are both local and inherited facets ==> merge them
        final FacetList filteredBaseFacets = new FacetList();
        OUTER:
        while (effectiveBaseFacets.hasMoreElements()) {
            final Facet baseFacet = (Facet) effectiveBaseFacets.nextElement();
            // Check whether one of the local facets
            // overrides the inherited facet
            for (int i = 0; i < _facets.size(); i++) {
                final Facet localFacet = _facets.get(i);
                if (localFacet.overridesBase(baseFacet)) {
                    continue OUTER;
                }
            }
            // Base facet is not overridden ==> keep it in the list
            filteredBaseFacets.add(baseFacet);
        }
        return new FacetListEnumerator(
                _facets,
                (FacetListEnumerator) filteredBaseFacets.enumerate());
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
        return _parent;
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
    public boolean hasFacet(final String name) {
        if (name == null) {
            return false;
        }
        for (int i = 0; i < _facets.size(); i++) {
            Facet facet = _facets.get(i);
            if (name.equals(facet.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if this SimpleType is a built in type.
     * @return true if this SimpleType is a built in type
    **/
    public boolean isBuiltInType() {
        return SimpleTypesFactory.isBuiltInType(_typeCode);
    }
    
    /**
     * Indicates whether this {@link SimpleType} is a numeric type.
     * @return True if this SimpleType is a numeric type
    **/
    public boolean isNumericType() {
        if (!isBuiltInType()) {
            return ((SimpleType) getBaseType()).isNumericType();
        }
        return false;
    }    

    /**
     * Indicates whether this {@link SimpleType} is a date/time type.
     * @return True if this SimpleType is a date/time type
    **/
    public boolean isDateTimeType() {
        return SimpleTypesFactory.isDateTimeType(_typeCode);
    }

    /**
     * Returns true if this simpleType is a redefinition.
     * 
     * @return true if this simpleType is a redefinition.
     */
    public boolean isRedefined() {
        return _redefinition;
    }
    

    /**
     * Sets this Group has redefined. 
     */
    public void setRedefined() {
        _redefinition = true;
    }
    
    /**
     * Gets the code for this simple type. 
     * (as defined in SimpleTypesFactory)
     *
     * @return the type code for this simple type
    **/
    public int getTypeCode() { 
        return _typeCode; 
    }


    /** 
     * Package private setter of the code for this simple type. 
     **/
    void setTypeCode(final int code) { 
        _typeCode = code;
    }



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
    } //-- getMaxLength
    
    /**
     * Removes the given Facet from this SimpleType.
     * Returns true if this SimpleType actually contains
     * the given facet.
     *
     * <p>Removes only local facets.</p>
     *
     * @param facet the Facet to remove
     * @return true if the specified Facet has been removed
     */
    public boolean removeFacet(Facet facet) {
        if (facet == null) return false;
        return _facets.remove(facet);
    } //-- removeFacet
    
    
    /**
     * Removes the facet with the given name from this SimpleType.
     * Returns true if this Simpletype has a facet with the given
     * name and it is successfully removed.
     *
     * <p>Removes only local facets.</p>
     *
     * @param name the name of the Facet to remove
     * @return true if the specified Facet has been removed
     */
    public boolean removeFacet(String name) {
        if (name == null) return false;
        for (int i = _facets.size()-1; i > 0; i--) {
            Facet facet = _facets.get(i);
            if (name.equals(facet.getName())) {
                _facets.remove(i);
                return true;
            }
        }
        return false;
    } //-- removeFacet
    
    /**
     * Sets the value of the 'final' property, indicating which
     * types of derivation are not allowed. A null value will indicate
     * all types of derivation (list, restriction, union) are allowed.
     *
     * @param finalValue the value of the final property.
     * @exception IllegalArgumentException when the value is not a valid value.
    **/
    public void setFinal(String finalValue) {
        if ((finalValue == null) ||
            finalValue.equals(FINAL_ALL) ||
            finalValue.equals(FINAL_UNION) ||
            finalValue.equals(FINAL_LIST) ||
            finalValue.equals(FINAL_RESTRICTION)) 
        {
            _final = finalValue;
        }
        else {
            String err = "The value '" + finalValue + "' is not a valid"
                + "value of the final property.";
            throw new IllegalArgumentException(err);
        }
    } //-- setFinal


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

    /**
     * Checks the validity of this SimpleType definition.
     *
     * @throws ValidationException when this SimpleType definition
     * is invalid.
    **/
    public void validate() throws ValidationException {
        final Enumeration localFacets = getLocalFacets();
        final SimpleType datatype = (SimpleType) getBaseType();
        if (localFacets != null) {
            while (localFacets.hasMoreElements()) {
                final Facet facet = (Facet) localFacets.nextElement();
                Enumeration baseFacets = null;
                if (datatype != null) {
                    baseFacets = datatype.getFacets();
                }
                try {
                    facet.checkConstraints(getLocalFacets(), baseFacets);
                } catch (SchemaException e) {
                    throw new ValidationException(
                            "Facet validation failed for type '" + getName() + "'", e);
                }
            }
        }

        //-- TODO: NOT YET FULLY IMPLEMENTED

    }

    //-- protected Methods -/

    /**
     * A helper method for classes which extend SimpleType. This method
     * allows creating a reference to a SimpleType.
     *
     * @return the reference to the SimpleType.
    **/
    protected SimpleType createReference(String name) {
        return new SimpleTypeReference(getSchema(), name);
    } //-- createReference

    /**
     * A helper method for classes which extend SimpleType. This method
     * allows resolving a SimpleType reference to a SimpleType.
     *
     * @return the resolved SimpleType.
     * @see #createReference
    **/
    protected static SimpleType resolveReference(SimpleType simpleType) {
        return (SimpleType) simpleType.getType();
    } //-- createReference
    
    /**
     * Sets the parent for this SimpleType
     * @param parent the Structure that contains this SimpleType.
     * Currently this should only be Schema, ElementDecl or AttributeDecl.
    **/
    protected void setParent(Structure parent) {
        this._parent = parent;
    } //-- setParent

    /**
     * Copy this type's facets to the target type.
     *
     * @param target the SimpleType to copy facets to
     */
    protected void copyFacets(SimpleType target) { 
        target._facets.add(_facets); 
    } //-- copyFacets
    
    /**
     * Returns the number of facets named 'name' within the list of facets of this simple type.
     * @param name Name (type) of the facet. 
     * @return number of facets named 'name'
     */
    public int getNumberOfFacets(final String name) {
        int counter = 0;
        for (Enumeration enumerator = getFacets(); enumerator.hasMoreElements(); ) {
            Facet facet = (Facet) enumerator.nextElement();
            if (facet.getName().equals(name)) {
                counter++;
            }
        }
        return counter;
    }
    
} //-- SimpleType
