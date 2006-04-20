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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Enumeration;

/**
 * The XML Schema ComplexType class
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ComplexType extends XMLType
   implements ContentModelGroup, Referable
{


    /**
     * The abstract flag for this ComplexType
    **/
    private boolean _abstract = false;

    /**
     * A wildcard that represents an <anyAttribute> element if any.
     * Only one <anyAttribute> can appear inside the global scope of
     * a complexType
    **/
    private Wildcard _anyAttribute = null;

    /**
     * The attribute declarations for this ComplexType.
    **/
    private AttributeGroupDecl _attributes = null;

	/**
     * The name of the base type used in <restriction> or <extension>
    **/
    private String _baseType = null;

    /**
     * The value of the 'block' property for this ComplexType. This
     * value may be null.
    **/
    private BlockList _block = null;

	/**
	 * a flag set to true if this complexType is a complexContent
	 */
	private boolean _complexContent = true;

    /**
     * The content type ("mixed", "simpleType","elemOnly") for this ComplexType.
    **/
    private ContentType _content  = ContentType.elemOnly;

    /**
     * The ContentModel for this ComplexType
    **/
    private ContentModelGroup _contentModel = null;

    /**
     * The final property for this ComplexType. This value may be null.
    **/
    private FinalList _final = null;

    /**
     * The parent structure for this ComplexType
     * (either an ElementDecl or a Schema)
    **/
    private Structure _parent = null;

    /**
	 * a flag set to true if this complexType is a restriction
	 */
	private boolean _restricted = false;

	/**
	 * An attribute that indicates if this ComplexType is
	 * a redefinition
	 */
	private boolean _redefinition = false;
	
    //------------------/
    //- Constructor(s) -/
    //------------------/

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
        _attributes = new AttributeGroupDecl(schema);
        _contentModel = new ContentModelGroupImpl();
    } //-- Complextype

    /**
     * Adds the given AttributeDecl to this ComplexType
     *
     * @param attrDecl the AttributeDecl to add to this ComplexType
     * @exception SchemaException when an AttributeDecl already
     * exists with the same name as the given AttributeDecl
    **/
    public void addAttributeDecl(AttributeDecl attrDecl)
        throws SchemaException
    {
        _attributes.addAttribute(attrDecl);

        //--set the parent
        attrDecl.setParent(this);
    } //-- addAttributeDecl

    /**
     * Removes the given AttributeDecl from this ComplexType
     * @param attrDecl the AttributeDecl to remove.
     */
     public void removeAttributeDecl(AttributeDecl attrDecl) {
        _attributes.removeAttribute(attrDecl);
     }

    /**
     * Adds the given AttributeGroupReference to this ComplexType
     *
     * @param attrGroupRef the AttributeGroupReference to add to this
     * ComplexType
    **/
    public void addAttributeGroupReference
        (AttributeGroupReference attrGroupRef)
    {
        _attributes.addReference(attrGroupRef);
    } //-- addAttributeGroupReference


    /**
     * Removes the given AttributeGroupReference from this ComplexType
     * @param attrGroupRef the AttributeGroupReference to remove.
     */
     public void removeAttributeGroupReference(AttributeGroupReference attrGroupRef) {
        _attributes.removeReference(attrGroupRef);
     }

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
     * Returns the wilcard used in this complexType (can be null)
     * @return the wilcard used in this complexType (can be null)
     */
    public Wildcard getAnyAttribute() {
        return _anyAttribute;
    }

    /**
     * Returns the AttributeDecl associated with the given name
     * @return the AttributeDecl associated with the given name, or
     *  null if no AttributeDecl with the given name was found.
    **/
    public AttributeDecl getAttributeDecl(String name) {
        AttributeDecl result = _attributes.getAttribute(name);
	    return result;
    } //-- getAttributeDecl

    /**
     * Returns an Enumeration of *all* the AttributeDecl objects
     * declared within this ComplexType. The Enumeration 
     * will contain all AttributeDecl from AttributeGroup
     * references as well. To return only locally declared
     * attributes make a call to 
     * <code>getLocalAttributeDecls</code>.
     *
     * @return an Enumeration of all the AttributeDecl objects
     * declared within this Complextype
     */
    public Enumeration getAttributeDecls() {
        return _attributes.getAttributes();
    } //-- getAttributeDecls

    /**
     * Returns an Enumeration of *all* locally defined AttributeDecl
     * declared within this ComplexType. The Enumeration 
     * will not contain any AttributeDecl from AttributeGroup
     * references. 
     *
     * @return an Enumeration of all locally declared AttributeDecl.
     */
    public Enumeration getLocalAttributeDecls() {
        return _attributes.getLocalAttributes();
    } //-- getLocalAttributeDecls

    /**
     * Returns an Enumeration of all the AttributeGroup that are referenced
     * within this ComplexType.
     *
     * @return an Enumeration of all the AttributeGroup that are referenced
     * within this ComplexType.
     */
    public Enumeration getAttributeGroupReferences() {
        return _attributes.getLocalAttributeGroupReferences();
    }

    /**
     * Returns the base type that this type inherits from.
     *
     * @return the base type (also called super type).
     */
    public XMLType getBaseType() {
        if ( (_baseType != null) && (super.getBaseType() == null) ) {
            XMLType baseType = getSchema().getType(_baseType);
            setBaseType( baseType );
        }
        return super.getBaseType();
    } //-- getBaseType

	/**
	 * Returns the value of the 'block' attribute for this element
	 *
     * @return the value of the 'block' attribute for this element
	 */
	public BlockList getBlock() {
		return _block;
	} //-- getBlock

    /**
     * Returns the content type of this ComplexType.
     * The Content Type holds the information about the content of the complexType.
     * For instance, if this complexType is a simpleContent then the simpleType information
     * will be hold in the content type.
     * 
     * @return the content type of this ComplexType
    **/
    public ContentType getContentType() {
        return _content;
    } //-- getContentType

    /**
     * Returns the list of values for the final property for this
     * ComplexType, or null if no final values have been set.
     *
     * @return the FinalList for this ComplexType
    **/
    public FinalList getFinal() {
        return _final;
    } //-- getFinal

    /**
     * Returns the parent of this ComplexType, this value may be null if
     * no parent has been set.
     *
     * @return the parent Structure of this ComplexType.
    **/
    public Structure getParent() {
        return _parent;
    } //-- getParent

    /**
     * Returns the Id used to Refer to this Object
     * @return the Id used to Refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        return "complexType:"+getName();
    } //-- getReferenceId


   /**
    * A helper method that returns true if this complexType
    * contains an <any> element.
    * @return  method that returns true if this complexType
    * contains an <any> element.
    */
    public boolean hasAny() {
        boolean result = false;
        Enumeration enumeration = _contentModel.enumerate();
        while (enumeration.hasMoreElements() && !result) {
            Structure struct = (Structure)enumeration.nextElement();
            switch (struct.getStructureType()) {
                case Structure.ELEMENT:
                    break;
                case Structure.GROUP:
                case Structure.MODELGROUP:
                    result = ((Group)struct).hasAny();
                    break;
                case Structure.WILDCARD:
                    result = true;
                    break;
                default:
                    break;
            }
        }
        return result;
    }
    /**
     * Returns true if this ComplexType has been marked as Abstract.
     *
     * @return true if this ComplexType is "abstract".
    **/
    public boolean isAbstract() {
        return _abstract;
    } //-- isAbstract

    /**
     * Returns true if this complexType is a redefinition.
     * 
     * @return true if this complexType is a redefinition.
     */
    public boolean isRedefined() {
    	return _redefinition;
    }
    
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
	 * Returns true if this complexType is a 'complexContent'
	 * @returns true if this complexType is a 'complexContent'
	 */
	public boolean isComplexContent() {
	       return _complexContent;
	}

	/**
	 * Returns true if this complexType is a 'simpleContent'
	 * @returns true if this complexType is a 'simpleContent'
	 */
	public boolean isSimpleContent() {
	       return (!_complexContent);
	}

	/**
	 * Returns true if this complexType is a restriction
	 * @returns true if this complexType is a restriction
	 */
	public boolean isRestricted() {
        return _restricted;
	}

    /**
     * Sets whether or not this ComplexType should be abstract.
     *
     * @param isAbstract a boolean that when true makes this ComplexType
     * abstract.
    **/
    public void setAbstract(boolean isAbstract) {
        _abstract = isAbstract;
    } //-- setAbstract

    /**
     * Sets the wildcard (anyAttribute) of the complexType
     * @exception SchemaException thrown when a wildcard as already be set
     * or when the wildCard is not an <anyAttribute>.
     */
     public void setAnyAttribute(Wildcard wildcard)
            throws SchemaException
     {
        if (wildcard != null) {
           if (_anyAttribute != null) {
              String err = "<anyAttribute> already set in this complexType: "
                           + this.getName();
              throw new SchemaException(err);
           }

           if (!wildcard.isAttributeWildcard()){
              String err = "In complexType, "+this.getName()
                            +"the wildcard must be an <anyAttribute>";
               throw new SchemaException(err);
           }
        }
        _anyAttribute = wildcard;
     }


    /**
     * Removes the given Wildcard from this Group.
     * @param wilcard the Wildcard to remove.
     * @return true if the wildcard has been successfully removed, false otherwise.
     */
     public boolean removeWildcard(Wildcard wildcard) {
         if (wildcard == null)
            return false;
         if (wildcard.equals(_anyAttribute)) {
             _anyAttribute = null;
             return true;
         }
         return false;

     }

     public void addWildcard(Wildcard wildcard)
         throws SchemaException
     {
        setAnyAttribute(wildcard);
     }


    /**
     * Sets the base type that this type is derived from
     * @param base the type that this type is derived from
    **/
    public void setBase(String base) {
        _baseType = base;
    } //-- setBase

    /**
     * Sets the base type for this ComplexType
     *
     * @param baseType the base type which this ComplexType 
     * extends or restricts
     */
    public void setBaseType(XMLType baseType) {
        super.setBaseType(baseType);
        if (baseType != null) {
            if (baseType.isSimpleType()) {
                _complexContent = false;
                _content = new SimpleContent((SimpleType)baseType);
            }
            else if (baseType.isComplexType()) {
                ComplexType complexType = (ComplexType)baseType;
                if (complexType.isSimpleContent()) {
                    _complexContent = false;
                    _content = ((SimpleContent)complexType.getContentType()).copy();
                }
                else _complexContent = true;
            }
            else {
                //-- assuming anyType
                _complexContent = true;
            }
        }
    } //-- setBaseType
    
	/**
	 * Sets the value of the 'block' attribute for this ComplexType.
	 *
	 * @param block the value of the block attribute for this
	 * ComplexType definition.
	**/
	public void setBlock(BlockList block) {

	    if (block != null) {
	        if (block.hasSubstitution()) {
	            String err = "'substitution' is an illegal value of the "+
	                "'block' attribute for a complexType definition.";
	            throw new IllegalArgumentException(err);
	        }
	    }
	    _block = block;
	} //-- setBlock

	/**
	 * Sets the value of the 'block' attribute for this ComplexType.
	 *
	 * @param block the value of the block attribute for this
	 * ComplexType definition.
	**/
	public void setBlock(String block) {
	    if (block == null)
	        _block = null;
	    else {
	        setBlock(new BlockList(block));
	    }
	} //-- setBlock

	/**
	 * Sets whether or not this complexType is a 'complexContent'
	 * @param complexContent true if this complexType is a 'complexContent'
	 */
	public void setComplexContent(boolean complexContent) {
	       this._complexContent = complexContent;
	}

	/**
     * Sets the content type of this complexType.
     * The Content Type holds the information about the content of the complexType.
     * For instance, if this complexType is a simpleContent then the simpleType information
     * will be hold in the content type.
     * @param contentType the ContentType for this complexType
    **/
    public void setContentType(ContentType contentType)
    {
        _content = contentType;
    } //-- setContentType

	/**
	 * Sets the value of the 'final' attribute for this ComplexType
	 * definition.
	 *
	 * @param finalList the value of the final attribute for this
	 * ComplexType definition.
	**/
	public void setFinal(FinalList finalList) {
	        _final = finalList;
	} //-- setFinal

	/**
	 * Sets the value of the 'final' attribute for this ComplexType
	 * definition.
	 *
	 * @param finalValue the value of the final attribute for this
	 * ComplexType definition.
	**/
	public void setFinal(String finalValue) {
	    if (finalValue == null)
	        _final = null;
	    else
	        _final = new FinalList(finalValue);
	} //-- setFinal


	/**
	 * Sets this Group has redefined. 
	 */
	public void setRedefined() {
		_redefinition = true;
	}
	/**
	 * Sets whether or not this complexType is a 'simpleContent'
	 * @param complexContent true if this complexType is a 'simpleContent'
	 */
	public void setSimpleContent(boolean simpleContent) {
	    _complexContent = (!simpleContent);
	}

	/**
	 * Sets whether or not this complexType is a restriction
	 * @param complexContent true if this complexType is a restriction
	 */
	public void setRestriction(boolean restricted) {
	       this._restricted = restricted;
	}


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

         //--set the parent
        elementDecl.setParent(this);
    } //-- addElementDecl

    /**
     * Removes the given ElementDecl from this ContentModelGroup.
     * @param elementDecl the ElementDecl to remove.
     * @return true if the element has been successfully removed, false otherwise.
     */
     public boolean removeElementDecl(ElementDecl element) {
         return _contentModel.removeElementDecl(element);
     }



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

        //-- set reference to parent
        group.setParent(this);
    } //-- addGroup


    /**
     * Removes the given Group from this ContentModelGroup.
     * @param group the Group to remove.
     * @return true if the group has been successfully removed, false otherwise.
     */
     public boolean removeGroup(Group group){
        boolean result = _contentModel.removeGroup(group);
        group.setParent(null);
        return result;
     }

    /**
     * Adds the given ModelGroup Definition to this ContentModelGroup
     * @param group the ModelGroup to add
     * @exception SchemaException when a group with the same name as the
     * specified group already exists in the current scope
    **/
    public void addGroup(ModelGroup group)
        throws SchemaException
    {
        _contentModel.addGroup(group);

        //-- set reference to parent
        group.setParent(this);
    } //-- addGroup


    /**
     * Removes the given ModelGroup Definition from this ContentModelGroup.
     * @param group the ModelGroup Definition to remove.
     * @return true if the group has been successfully removed, false otherwise.
     */
     public boolean removeGroup(ModelGroup group) {
         boolean result = _contentModel.removeGroup(group);
         group.setParent(null);
         return result;
     }

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
     * Returns the element declaration with the given name, or null if no
     * element declaration with that name exists in this ContentModelGroup.
     *
     * @param name the name of the element.
     * @return the ElementDecl with the given name, or null if no
     * ElementDecl exists in this ContentModelGroup.
    **/
    public ElementDecl getElementDecl(String name) {
        ElementDecl result = _contentModel.getElementDecl(name);
        return result;
    } //-- getElementDecl

    /**
     * Returns the maximum number of occurances that this ContentModelGroup
     * may appear
     * @return the maximum number of occurances that this ContentModelGroup
     * may appear.
     * A non positive (n < 1) value indicates that the
     * value is unspecified (ie. unbounded).
    **/
    public int getMaxOccurs() {

        if (_contentModel.getParticleCount() > 0) {
            Particle particle = _contentModel.getParticle(0);
            if (particle instanceof ContentModelGroup) {
                return particle.getMaxOccurs();
            }
        }

        return _contentModel.getMaxOccurs();
    } //-- getMaxOccurs

    /**
     * Returns the minimum number of occurances that this ContentModelGroup
     * must appear
     * @return the minimum number of occurances that this ContentModelGroup
     * must appear
     * A negative (n < 0) value indicates that the value is unspecified.
    **/
    public int getMinOccurs() {
        if (_contentModel.getParticleCount() > 0) {
            Particle particle = _contentModel.getParticle(0);
            if (particle instanceof ContentModelGroup) {
                return particle.getMinOccurs();
            }
        }
        return _contentModel.getMinOccurs();
    } //-- getMinOccurs

    /**
     * Returns the Particle at the specified index
     * @param index the index of the particle to return
     * @returns the CMParticle at the specified index
    **/
    public Particle getParticle(int index) {
        Particle result = _contentModel.getParticle(index);
        return result;
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

    /**
     * Checks the validity of this ComplexType defintion.
     *
     * @throws ValidationException when this ComplexType definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
        //-- check name
        if (_parent != null && _parent.getStructureType() != Structure.SCHEMA) {
           if (getName() != null) {
               String err = "Only top-level complexTypes can be named.";
               err += getName() + "is not a valid complexType.";
               throw new ValidationException(err);
           }
        }
        //-- check attributes
        _attributes.validate();

        //-- check content model
        Enumeration enumeration = _contentModel.enumerate();
        while (enumeration.hasMoreElements()) {
            ((Structure)enumeration.nextElement()).validate();
        }

        //-- make sure baseType is accessible
        XMLType type = getBaseType();
        if ((type == null) && (_baseType != null)) {
            String error = "The base type '" +  _baseType +
                "' was not found.";
            throw new ValidationException(error);
        }
        if (type != null) {
            if (type.getStructureType() == Structure.SIMPLE_TYPE) {
                if (_restricted) {
                    String name = getName();
                    if (name == null) {
                        name = "anonymous-complexType-for-element: ";
                        if (_parent != null) {
                            //-- parent should be an element if name is null, but
                            //-- we'll check the type to be on the safe side
                            if (_parent.getStructureType() == Structure.ELEMENT)
                                name += ((ElementDecl)_parent).getName();
                            else 
                                name += _parent.toString();
                        }
                    }
                    String err ="complexType: " + name;
                    err += "; A complex type cannot be a restriction"+
                           " of a simpleType:";
                    err += type.getName();
                    throw new ValidationException(err);
                }
            }
		    else if (type.getStructureType() == Structure.COMPLEX_TYPE) {
             
                if (!_complexContent) {
   			        //we are now sure that the base is a ComplexType
                    //but is the base of this complexType a simpleType? (see 4.3.3->simpleContent->content type)
                    if ( ((ComplexType)type).getContentType().getType() != ContentType.SIMPLE) 
                    {
                        String name = getName();
                        if (name == null) {
                            name = "anonymous-complexType-for-element: ";
                            if (_parent != null) {
                                //-- parent should be an element if name is null, but
                                //-- we'll check the type to be on the safe side
                                if (_parent.getStructureType() == Structure.ELEMENT)
                                    name += ((ElementDecl)_parent).getName();
                                else 
                                    name += _parent.toString();
                            }
                        }
                        String err ="complexType: " + name;
                        err += "; When a complexType is a restriction of simpleContent the base type"+
                               " must be a complexType whose base is also simpleContent.";
                        throw new ValidationException(err);
                    }
                }
		    }
        }

    } //-- validate

    //---------------------/
    //- Protected Methods -/
    //---------------------/

    /**
     * Sets the parent for this ComplexType
     *
     * @param parent the parent Structure for this ComplexType
    **/
    protected void setParent(Structure parent) {
        if (parent != null) {
            switch (parent.getStructureType()) {
                case Structure.SCHEMA:
                case Structure.ELEMENT:
                    break;
                default:
                    String error = "Invalid parent for ComplexType";
                    throw new IllegalArgumentException(error);
            }
        }
        _parent = parent;
    } //-- setParent


} //-- Complextype
