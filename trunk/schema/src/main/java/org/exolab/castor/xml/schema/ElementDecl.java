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
 * Copyright 1999-2003 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;
import org.exolab.castor.xml.validators.ValidationUtils;

import java.util.Enumeration;
import java.util.Vector;

/**
 * An XML Schema ElementDecl
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
**/
public class ElementDecl extends Particle implements Referable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -7804351635137964219L;

   //-------------------/
   //- Class Variables -/
   //-------------------/

    /**
     * Error message for a null argument
    **/
    private static String NULL_ARGUMENT
        = "A null argument was passed to the constructor of " +
           ElementDecl.class.getName();


    //--------------------/
    //- Member Variables -/
    //--------------------/

    /**
     * The block attribute for this element definition.
    **/
    private BlockList _block = null;

    /**
     * collection of Identity Constraints
    **/
    private Vector _constraints = null;

    /**
     * The default value for this element definition. Only
     * useful for simpleContent.
    **/
    private String _default = null;

    /**
     * The name of a reference to a top-level element declaration
    **/
    private String _elementRefName = null;

    /**
     * The top-level element declaration this element reference points to
    **/
    private ElementDecl _referencedElement = null;

    /**
     * The final value for this element definition.
    **/
    private FinalList _final = null;

    /**
     * The fixed value for this element definition. Only
     * used for simpleContent.
    **/
	private String _fixed = null;

    /**
     * The form type for this element definition.
     * Specifies whether names should be qualified or unqualified.
     * Uses the default Form from the parent Schema if unspecified.
    **/
    private Form _form = null;

    /**
     * The unique ID for this element definition (optional).
    **/
    private String _id = null;

    /**
     * Flag indicating whether or not this Element declaration is
     * abstract
    **/
    private boolean _isAbstract = false;

    /**
     * The element name
    **/
    private String _name = null;

    /**
     * Flag indicating whether or not the element value may be null.
    **/
    private boolean _nillable = false;

    /**
     * The parent for this ElementDecl
    **/
    private Structure _parent = null;

    /**
     * The parent schema that this element declaration belongs to
    **/
    private Schema _schema = null;

    /**
     * The substitutionGroup for this element definition.
    **/
    private String _substitutionGroup = null;

    /**
     * The XMLType for this element declaration
    **/
    private XMLType _xmlType = null;

    /**
     * Creates a new default element definition
     * @param schema the XML Schema to which this element declaration
     * belongs
     * <BR />This element definition will not be valid until a name has
     * been set
    **/
    public ElementDecl(Schema schema) {
        this(schema, null);
    } //-- ElementDecl

    /**
     * Creates a new default element definition
     * @param schema the XML Schema to which this Element Declaration
     * belongs
     * @param name the name of the Element being declared
    **/
    public ElementDecl(Schema schema, String name) {
        super();
        setName(name);
        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        setSchema(schema);
        _constraints = new Vector(3);
    } //-- ElementDecl

    /**
     * Adds the given IdentityConstraint to this element definition.
     *
     * @param constraint the IdentityConstraint to add.
    **/
    public void addIdentityConstraint(IdentityConstraint constraint) {
        if (constraint == null) return;
        _constraints.addElement(constraint);
    } //-- addIdentityConstraint

	/**
	 * Returns the value of the 'block' attribute for this element
	 *
	 * @return the value of the block attribute.
	**/
	public BlockList getBlock() {
		return _block;
	} //-- getBlock

    /**
     * Returns the default value of this element definition.
     *
     * @return the default value of this element definition,
     * or null if no default was specified.
    **/
    public String getDefaultValue() {
        if (isReference()) {
        	ElementDecl elem = getReference();
            if (elem != null)
                return elem.getDefaultValue();
        }
        return _default;
    } //-- getDefaultValue

	/**
	 * Returns the value of the 'final' attribute for this element
	 * definition.
	 *
	 * @return the FinalList for this element definition.
	**/
	public FinalList getFinal() {
		return _final;
	} //-- getFinal

    /**
     * Returns the fixed value of this element definition.
     *
     * @return the fixed value of this element definition,
     * or null if no default was specified.
     */
    public String getFixedValue() {
        if (isReference()) {
            ElementDecl elem = getReference();
            if (elem != null)
                return elem.getFixedValue();
        }
        return _fixed;
    } //-- getFixedValue

    /**
     * Returns the Form for this element definition. The Form object species
     * whether or not names are qualified or unqualified in the scope of
     * this element definition. If null, the Form should be obtained from the
     * parent Schema.
     *
     * @return the Form for this element definition, or null if not set.
    **/
    public Form getForm() {
        return _form;
    } //-- getForm

    /**
     * Returns the 'id' for this element definition.
     *
     * @return the 'id' for this element definition.
    **/
    public String getId() {
        return _id;
    } //-- getId

    /**
     * Returns an Enumeration of IdentityConstraint objects contained within
     * this element definition.
     *
     * @return an Enumeration of IdentityConstraint objects contained within
     * this element definition.
    **/
    public Enumeration getIdentityConstraints() {
        return _constraints.elements();
    } //-- getIdentityConstraints

    /**
     * Returns the name of this Element declaration. The name of the
     * referenced element is returned if the 'ref' attribute was used.
     * The name returned will be an NCName (no namespace prefix will
     * be included with the name).
     *
     * @return the name of this element declaration
     */
    public String getName() {
		return getName(false);
	} //-- getName

    /**
     * Returns the name of this Element declaration. The name
     * returned, if not null, will be an NCName.
     *
     * @param ignoreRef if false the name of the referenced
     * element (if specified) is returned, otherwise the
     * localname (may be null).
     *
     * @return the name of this element declaration
    **/
    public String getName(boolean ignoreRef) {
        if (isReference() && ignoreRef == false) {
            String localName = _elementRefName;
            //-- check for namespace prefix
            int idx = localName.indexOf(':');
            if (idx > 0) {
                localName = localName.substring(idx+1);
            }
            return localName;
        }
		return _name;
    } //-- getName

    /**
     * Returns the parent of this ElementDecl, this value may be null if
     * no parent has been set.
     *
     * @return the parent Structure of this ElementDecl
    **/
    public Structure getParent() {
        return _parent;
    } //-- getParent
    /**
     * Returns the XMLType (ComplexType or SimpleType) of this ElementDecl.
     * @return the XMLType of this ElementDecl
    **/
    public XMLType getType() {
        
    	XMLType result = null;
        
        if (isReference()) {
            ElementDecl element = getReference();
            if (element != null) {
            	return element.getType();
            }
            return null;
        }

        
        
        if (_xmlType == null) return null;
        //1--Anonymous types
        if (_xmlType.getName() == null) return _xmlType.getType();
        //--we look in the parent schema if we have redefinitions of types.
        result = _xmlType.getType();
        
        //-- the current XML schema might have a MasterSchema where all the
        //-- type definitions have a higher priority [this is useful when
        //-- resolving redefined types for instance].
        if (result != null) {
        	Schema tempSchema = result.getSchema().getMasterSchema();
        	if (tempSchema != null) {
        		XMLType tempType = tempSchema.getType(result.getName());
        		if (tempType != null) {
        			result = tempType;
        		}
        	}
        }
        return result;
    } //-- getXMLType

    /**
     * Returns the ElementDecl that this element definition references.
     * This will return null if this element definition does not reference
     * a different element definition.
     * @return the ElementDecl that this element definition references
    **/
    public ElementDecl getReference() {
        if (_referencedElement != null) {
            return _referencedElement;
        }
        
        ElementDecl result = null;
        if (_elementRefName != null) {
            result = _schema.getElementDecl(_elementRefName);
            if (result == null) {
                String err = "Unable to find element referenced :\" ";
                err += getName();
                err += "\"";
                throw new IllegalStateException(err);
            }
            _referencedElement = result;
        }
        return result;
    } //-- getReference

    /**
     * Returns the actual reference name of this AttributeDecl, or null
     * if this AttributeDecl is not a reference. The name returned, if not
     * null, will be a QName, possibly containing the namespace prefix.
     * 
     * @return the reference name
     */
    public String getReferenceName() {
        return _elementRefName;
    } //-- getReference
    
    /**
     * Returns the Id used to Refer to this Object
     *
     * @return the Id used to Refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        if (_name != null) return "element:"+_name;
        return null;
    } //-- getReferenceId

    /**
     * Returns the XML Schema to which this element declaration belongs.
     * @return the XML Schema to which this element declaration belongs.
    **/
    public Schema getSchema() {
        return _schema;
    } //-- getSchema

    /**
     * Returns the substitutionGroup for this element declaration, or
     * null if it's absent; if this {@link ElementDecl} instance is a reference
     * to a global element definition, return its substitution group
     *
     * @return the substitutionGroup membership for this element
     * declaration, or null if absent.
    **/
    public String getSubstitutionGroup() {
        if (isReference()) {
            return getReference().getSubstitutionGroup();
        }
       return _substitutionGroup;
    } //-- getSubstitutionGroup
    
    /**
     * Returns an enumeration of the elements that can be substitute to 
     * this element declaration. 
     * @return an enumeration of the elements that can be substitute to 
     * this element declaration.
     */
    public Enumeration getSubstitutionGroupMembers() {
    	Vector result = new Vector();
    	Enumeration enumeration = _schema.getElementDecls();
    	while (enumeration.hasMoreElements()) {
    		ElementDecl temp  = (ElementDecl)enumeration.nextElement();
    		String subName = temp.getSubstitutionGroup();
    		if (subName != null) {
                // no namespace(s) or default namespace in use
                if (subName.equals(_name)) { 
                    result.add(temp);
                }
                // namespace(s) incl. prefix in use
                // TODO: find a better way of dealing with a namespace prefix
                else if (subName.endsWith(_name) && subName.indexOf(":") > 0) {
                    result.add(temp);
                }
            }
    	}
    	return result.elements();
    }
    
    /**
     * Returns true if this element definition is abstract
     * @return true if this element definition is abstract
    **/
    public boolean isAbstract() {
        if (isReference()) {
            return _referencedElement.isAbstract();
        }
        return _isAbstract;
    } //-- isAbstract

    /**
     * Returns whether or not instances of this element definition
     * may appear with no content.
     *
     * @return true if instances of this element definition
     * may appear with no content, otherwise false.
    **/
    public boolean isNillable() {
        if (isReference()) {
            return _referencedElement.isNillable();
        }
        return _nillable;
    } //-- isNullable

    /**
     * Returns true if this element definition simply references another
     * element Definition
     * @return true if this element definition is a reference
    **/
    public boolean isReference() {
        return (_elementRefName != null);
    } //-- isReference

    /**
     * Sets whether or not this element definition is abstract
     * @param isAbstract a boolean when true indicates that this
     * element definition should be abstract
    **/
    public void setAbstract(boolean isAbstract) {
        _isAbstract = isAbstract;
    } //-- isAbstract

    /**
     * Returns true if this element has children (i.e if it
     * holds attributes or elements).
     * @return true if this element has children (i.e if it
     * holds attributes or elements).
     */
    public boolean hasChildren() {
        XMLType type = getType();
        if (type instanceof SimpleType)
           return false;

        if (type instanceof ComplexType) {
            //complexContent ->sure to have children
            if (((ComplexType)type).isComplexContent())
                 return true;
            //else check for contentModel group
            else if ( ((ComplexType)type).getParticleCount() != 0 )
               return true;
            //else check for attributes
            else {
                java.util.Enumeration temp = ((ComplexType)type).getAttributeDecls();
                return temp.hasMoreElements();
            }
        }

        return false;
    } //-- hasChildren

    /**
     * Removes the given IdentityConstraint from this element definition.
     *
     * @param constraint the IdentityConstraint to remove.
     * @return true if the IdentityConstraint was contained within this
     * element defintion.
    **/
    public boolean removeIdentityConstraint(IdentityConstraint constraint)
    {
        if (constraint ==  null) return false;
        return _constraints.removeElement(constraint);
    } //-- removeIdentityConstraint


	/**
	 * Sets the value of the 'block' attribute for this element
	 *
	 * @param block the value of the block attribute for this
	 * element definition.
	**/
	public void setBlock(BlockList block) {
	    _block = block;
	} //-- setBlock

	/**
	 * Sets the value of the 'block' attribute for this element
	 *
	 * @param block the value of the block attribute for this
	 * element definition.
	**/
	public void setBlock(String block) {
	    if (block == null)
	        _block = null;
	    else
	        _block = new BlockList(block);
	} //-- setBlock

    /**
     * Sets the default value for this element definition.
     *
     * @param value the default value for this element definition.
    **/
    public void setDefaultValue(String value) {
        this._default = value;
    } //-- setDefaultValue

	/**
	 * Sets the value of the 'final' attribute for this element
	 * definition.
	 *
	 * @param finalList the value of the final attribute for this
	 * element definition.
	**/
	public void setFinal(FinalList finalList) {
	        _final = finalList;
	} //-- setFinal

	/**
	 * Sets the value of the 'final' attribute for this element
	 * definition.
	 *
	 * @param finalValue the value of the final attribute for this
	 * element definition.
	**/
	public void setFinal(String finalValue) {
	    if (finalValue == null)
	        _final = null;
	    else
	        _final = new FinalList(finalValue);
	} //-- setFinal

    /**
     * Sets the fixed value for this element definition.
     *
     * @param value the fixed value for this element definition.
    **/
    public void setFixedValue(String value) {
        this._fixed = value;
    } //-- setDefaultValue

    /**
     * Sets the Form for this element definition. The Form object species
     * whether or not names are qualified or unqualified in the scope of
     * this element definition. If null, the Form is to be obtained from the
     * parent Schema.
     *
     * @param form the Form type for this element definition.
    **/
    public void setForm(Form form) {
        _form = form;
    } //-- setForm

    /**
     * Sets the Id for this element definition.
     *
     * @param id the Id for this element definition.
    **/
    public void setId(String id) {
        _id = id;
    } //-- setId

    /**
     * Sets the name of the element that this Element definition defines.
     *
     * @param name the name of the defined element
    **/
    public void setName(String name) {

        if ((name == null) || (ValidationUtils.isNCName(name))) {
            _name = name;
        }
        else {
            String err = "error: '" + name + "' is not a valid NCName.";
            throw new IllegalArgumentException(err);
        }
    } //-- setName

    /**
     * Sets whether or not instances of this element definition may
     * contain empty content
     *
     * @param nillable the flag when true indicates that instances
     * of this element definition may appear with empty content
    **/
    public void setNillable(boolean nillable) {
        _nillable = nillable;
    } //-- setNillable

    /**
     * Sets the parent for this ElementDecl.
     *
     * @param parent the parent Structure for this ElementDecl
    **/
    protected void setParent(Structure parent) {
        if (parent != null) {
            switch (parent.getStructureType()) {
                case Structure.GROUP:
                case Structure.MODELGROUP:
                case Structure.SCHEMA:
                    break;
                default:
                    String error = "Invalid parent for element.";
                    throw new IllegalArgumentException(error);
            }
        }
        _parent = parent;
    } //-- setParent
    /**
     * Sets the reference for this element definition
     * @param reference the Element definition that this definition references
    **/
    public void setReference(ElementDecl reference) {
        if (reference == null) {
            _elementRefName = null;
            _referencedElement = null;
        }
        else {
            if (reference.getSchema() == this.getSchema()) {
                _elementRefName = reference.getName();
                _referencedElement = reference;
            }
            else {
                String qName = reference.getName();
                String nsURI = reference.getSchema().getTargetNamespace();
                if (nsURI != null) {
                    String prefix = getSchema().getNamespacePrefix(nsURI);
                    if ((prefix != null) && (prefix.length() > 0))
                        qName = prefix + ":" + qName;
                }
                _elementRefName = qName;
                _referencedElement = reference;
            }
        }
    } //-- setReference

    /**
     * Sets the name which this element declaration refers to
     * @param referenceName the name of the element definition that this
     * definition references
    **/
    public void setReferenceName(String referenceName) {
        if ((referenceName == null) || (ValidationUtils.isQName(referenceName))) {
            _elementRefName = referenceName;
        }
        else {
            String err = "error: '" + referenceName + "' is not a valid QName.";
            throw new IllegalArgumentException(err);
        }
    } //-- setReference

    /**
     * Sets the substitutionGroup for this element definition.
     *
     * @param substitutionGroup the substitutionGroup for this
     * element definition.
    **/
    public void setSubstitutionGroup(String substitutionGroup) {
        _substitutionGroup = substitutionGroup;
    } //-- setSubstitutionGroup

    /**
     * Sets the XMLType for this Element declaration.
     * @param type the XMLType for this element declaration.
     * <BR />
     * <B>Note:</B> This method is mutually exclusive with
     * #setTypeReference, if a reference has previously been
     * set it will be ignored.
    **/
    public void setType(XMLType type)
    {
        //-- reset parent of current type
        if (_xmlType != null) {
            _xmlType.setParent(null);
        }
        if (type != null) {
            type.setParent(this);
        }

        _xmlType = type;
    } //-- setType


    /**
     * Sets the type of this element to be a reference.
     */
    public void setTypeReference(String name)
    {
        TypeReference reference= new TypeReference();
        reference.setName(name);
        reference.setSchema(_schema);
        setType(reference);
    }

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ELEMENT;
    } //-- getStructureType

    /**
     * Checks the validity of this element definition.
     *
     * @throws ValidationException when this element definition
     * is invalid.
    **/
    public void validate() throws ValidationException {

        //-- If this element merely references another element definition
        //-- just check that we can resolve the reference
        if (_elementRefName != null) {
            if (_schema.getElementDecl(_elementRefName) == null) {
                String err = "<element ref=\"" + _elementRefName + "\"> "+
                    "is not resolvable.";
                throw new ValidationException(err);
            }
            return;
        }

        if (_name == null)  {
            String err = "<element> is missing required 'name' or " +
                "'ref' attribute.";
            throw new ValidationException(err);
        }

        //--check that the particle information is not present on top level 
        //--element
        
        //--do you really allow parent to be null???
        if (getParent() != null) {
            if (getParent().getStructureType() == Structure.SCHEMA) {
                if (isMinOccursSet()) {
                    String err = "'minOccurs' declaration is prohibited on top level element '/" + getName() + "'.";
                    throw new ValidationException(err);
                }
                if (isMaxOccursSet()) {
                    String err = "'maxOccurs' declaration is prohibited on top level element '/" + getName() + "'.";
                    throw new ValidationException(err);
                }
            }
        }
        
        //-- If type is anonymous, make sure the type is valid.
        //-- To prevent excess validation, we ONLY validate
        //-- if the type is anonymous, because otherwise
        //-- the Schema itself will validate the type.
        XMLType type = getType();
        if (type != null) {
            if (type.isComplexType()) {
                ComplexType complexType = (ComplexType)type;
                if (!complexType.isTopLevel()) {
                    complexType.validate();
                }
            }
            else if (type.isSimpleType()) {
                SimpleType simpleType = (SimpleType)type;
                if (simpleType.getParent() != simpleType.getSchema()) {
                    simpleType.validate();
                }
                //-- print warning message if ID, IDREF, IDREFS, NMTOKEN, NTOKENS are
                //-- used as element type
                int typeCode =  simpleType.getTypeCode();
                switch (typeCode) {
                    case SimpleTypesFactory.ID_TYPE:
                    case SimpleTypesFactory.IDREF_TYPE:
                    case SimpleTypesFactory.IDREFS_TYPE:
                    case SimpleTypesFactory.NMTOKENS_TYPE:
                    case SimpleTypesFactory.NMTOKEN_TYPE:
                        String err = "Warning : For XML Compatibility " +
                                simpleType.getName()+" should be used only as attributes\n.";
                        //--Future versions will log the message
                        System.out.println(err);
                        break;
                    default:
                        break;

                }
            }
            //-- anyType
            else {
                //-- nothing to validate anyType is always valid.
            }
            
        }

    } //-- validate
    
    /**
     * Sets the XMl schema to where this element has been defined
     * @param schema The defining XML schema
     */
    private void setSchema(final Schema schema) {
        _schema = schema;
    }

    /**
     * Indicates whether a type is set for this element definiion. 
     * @return True if a type is set.
     */
    public boolean hasXMLType() {
        return (_xmlType != null);
    }
    
   
} //-- Element
