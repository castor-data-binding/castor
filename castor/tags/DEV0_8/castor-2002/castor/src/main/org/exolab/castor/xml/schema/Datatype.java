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

import java.util.Enumeration;

/**
 * An XML Schema Datatype
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public class Datatype extends Annotated 
    implements org.exolab.castor.xml.Referable
{

    /**
     * Error message for a null argument
    **/
    private static String NULL_ARGUMENT
        = "A null argument was passed to the constructor of " +
           Datatype.class.getName();
          
          
       
    /**
     * The source datatype reference
    **/
    private String source = null;    
    
    /**
     * The datatype name
    **/
    private String name = null;
    
    /**
     * The constraining facets of this type
    **/
    private FacetList facets     = null;

    /**
     * The owning Schema to which this Datatype belongs
    **/
    private Schema schema = null;
    
    /**
     * Creates a new Datatype with the given name and basetype reference.
     * @param name of the DataType
     * @param schema the Schema to which this Datatype belongs
    **/
    public Datatype(Schema schema, String name) {
        this(schema, name, null);
    } //-- DataType
    
    /**
     * Creates a new Datatype with the given name and basetype reference.
     * @param name of the Datatype
     * @param schema the Schema to which this Datatype belongs
     * @param source the base datatype which this datatype inherits from.
     * If the datatype does not "extend" any other, source may be null.
    **/
    public Datatype(Schema schema, String name, String source) {
        super();
        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        if ((name == null) || (name.length() == 0)) {
            String err = NULL_ARGUMENT + 
                "; 'name' must not be null or zero-length.";
            throw new IllegalArgumentException(err);
        }
        
        this.schema  = schema;
        this.name    = name;
        this.source  = source;
        this.facets  = new FacetList();
    } //-- DataType
    
    /**
     * Adds the given Facet to this Datatype.
     * @param facet the Facet to add to this Datatype
    **/
    public void addFacet(Facet facet) {
        
        if (facet == null) return;
        
        String name = facet.getName();
        
        if (name == null) return;
        
        facets.add(facet);
        
    } //-- addFacet
    
    /**
     * Returns the facets associated with the given name
     * @return the facets associated with the given name
    **/
    public Enumeration getFacets(String name) {
        FacetListEnumerator fle = null;
        Datatype datatype = getSource();
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
        Datatype datatype = getSource();
        if (datatype != null) {
            fle = (FacetListEnumerator)datatype.getFacets();
        }
        fle = new FacetListEnumerator(facets, fle);
        return fle;
    } //-- getFacets
    
    /**
     * Returns the name of this DataType
     * @return the name of this DataType
    **/
    public String getName() {
        return name;
    } //-- getName
    
    
    /**
     * Returns the source Datatype that this Datatype inherits from.
     * If this Datatype does not inherit from any other, or if
     * reference cannot be resolved this will be null.
     * @return the source Datatype that this Datatype inherits from.
    **/
    public Datatype getSource() {
        if (source == null) return null;
        return this.schema.getDatatype(source);
    } //-- getSource
    
    /**
     * Returns the name of the source type for this datatype.
     * If this datatype does not inherit from any other, this
     * will be null.
     * @return the name of the source type for this datatype.
    **/
    public String getSourceRef() {
        return source;
    } //-- getSourceRef
    
    /**
     * Returns the Id used to Refer to this Object. 
     * @return the Id used to Refer to this Object
     * @see org.exolab.castor.xml.Referable
    **/
    public String getReferenceId() {
        return "datatype:"+name;
    } //-- getReferenceId
    
    /**
     * Returns the schema to which this Datatype belongs
     * @return the Schema to which this Datatype belongs
    **/
    public Schema getSchema() {
        return schema;
    } //-- getSchema
    
    /**
     * Returns true if this Datatype has a specified Facet
     * with the given name.
     * @param name the name of the Facet to look for
     * @return true if this Datatype has a specified Facet
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
     * Sets the source type for this datatype
     * @param source the source type which this datatype inherits from
    **/
    public void setSourceRef(String source) {
        this.source = source;
    } //-- setBaseTypeRef
    
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.DATATYPE;
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
    
} //-- DataType