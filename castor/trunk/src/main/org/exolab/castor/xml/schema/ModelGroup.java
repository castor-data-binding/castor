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
 * Copyright 1999 - 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Vector;
import java.util.Enumeration;

/**
 * An XML Schema ModelGroup : <xsd:group>
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ModelGroup extends Group {
    /** SerialVersionUID */
    private static final long serialVersionUID = -2057934322265672413L;

    /**
     * the name of the ModelGroup referenced
     */
     private String _groupRef = null;

    /**
     * An ordered list of all ModelGroup definitions
    **/
    private Vector _modelDefs;
    
    /**
     * An attribute that indicates if this Group is
     * a redefinition
     */
    private boolean _redefinition = false;


    /**
     * the schema that contains this model group
     */
     private Schema _schema = null;

    /**
     * Creates a new ModelGroup, with no name
    **/
    public ModelGroup() {
        this(null);
    } //-- ModelGroup

    /**
     * Creates a new ModelGroup definition
     * @param schema, the XML Schema to which this ModelGroup
     * belongs
     */
    public ModelGroup(Schema schema) {
        this(null, schema);
    }

    /**
     * Creates a new ModelGroup with the given name
     * @param name of the ModelGroup
    **/
    public ModelGroup(String name, Schema schema) {
        super(name);
        _schema = schema;
        _modelDefs = new Vector();
    } //-- ModelGroup


    /**
     * Adds the given ModelGroup to this ModelGroup
     * @param modelGroup the ModelGroup to add to this ModelGroup
    **/
    public void addModelGroup(ModelGroup modelGroup) {
        if (!_modelDefs.contains(modelGroup)) {
            _modelDefs.addElement(modelGroup);
        }
    } //-- addModelGroup

    /**
     * Returns an enumeration of all the Particles of this
     * ContentModelGroup
     *
     * @return an enumeration of the Particles contained
     * within this ContentModelGroup
    **/
    public Enumeration enumerate() {
        return this.getContentModelGroup().enumerate();
    } //-- enumerate

    /**
     * Returns an ordered Enumeration of all the ContentModelType
     * definitions (element, group, modelGroupRef)+
    **/
    public Enumeration getDeclarations() {
        return _modelDefs.elements();
    } //-- getDeclarations

    /**
     * Returns the ContentModelGroup for this group
     * Only used for a <group> element
     * @return the ContentModelGroup for this group
     */
     public ContentModelGroup getContentModelGroup() {
        if (_groupRef != null)
            return getReference().getContentModelGroup();
        return super.getContentModelGroup();
     }
    /**
     * Returns the name of this ModelGroup.
     * @return the name of this ModelGroup.
    **/
    public String getName() {
        return getName(false);
    } //-- getName

    /**
     * Returns the name of this Model Group definition
     *
     * @param ingoreRef If True the name of the referenced
     * model group (if specified) is returned
     * @return the name of this element declaration
    **/
    public String getName(boolean ignoreRef) {

        if (ignoreRef == false && _groupRef !=null ) {
            String localName = _groupRef;
            //-- check for namespace prefix
            int idx = localName.indexOf(':');
            if (idx > 0) {
                localName = localName.substring(idx+1);
            }
            return localName;
        }
		return super.getName();
    } //-- getName
    
    /**
     * Returns true if this model group definition simply references another
     * model group Definition
     * @return true if this model group definition is a reference
     */
    public boolean isReference() {
        return (_groupRef != null);
    } //-- isReference
    
    /**
     * Sets this Group has redefined. 
     */
    public void setRedefined() {
    	_redefinition = true;
    }
    
    /**
     * Returns true if this group is a redefinition.
     * 
     * @return true if this group is a redefinition.
     */
    public boolean isRedefined() {
    	return _redefinition;
    }
    
    /**
     * Sets the reference for this ModelGroup definition
     * @param reference the name of the ModelGroup that this
     * definition references
    **/
    public void setReference(String reference) {
        this._groupRef = reference;
    } //-- setReference

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.MODELGROUP;
    } //-- getStructureType

    /**
     * Returns the Id used to Refer to this Object
     * @return the Id used to Refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        if (this.getName() != null) return "group:"+this.getName();
        return null;
    } //-- getReferenceId

    /**
     * Returns the reference if any
     * @returns the reference if any
     */
     public ModelGroup getReference() {
        ModelGroup result = null;
        if (_groupRef != null) {
            result =  _schema.getModelGroup(_groupRef);
            
            //--check for redefinition
            if (_schema.getMasterSchema() != null ) {
            	ModelGroup temp = _schema.getMasterSchema().getModelGroup(_groupRef);
            	if (temp != null)
            		result = temp;
            }
            
            if (result == null) {
                String err = "Unable to find group referenced :\" ";
                err += getName();
                err +="\"";
                throw new IllegalStateException(err);
            }
        }
        return result;
    } //-- getReference


     /**
      * Returns true if this ModelGroup is referencing another one
      * @returns true if this ModelGroup is referencing another one
      */
     public boolean hasReference() {
       return (_groupRef != null)
               ? (_groupRef.length() !=0)
               : false;
     }

    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
        //-- check name
        if (getParent() != null && getParent().getStructureType() != Structure.SCHEMA) {
           if (getName(true) != null) {
               String err = "Only top-level model group definition (<group>) can be named.";
               err += getName() + "is not a valid model group definition.";
               throw new ValidationException(err);
           }
        }

        if (getContentModelGroup() == null) {
            String err = "<group> should contains :\" ";
            err += " 'all' or 'sequence' or 'choice'";
            err +="\"";
            throw new ValidationException(err);
        }

        //-- Check for circular references
        //-- Validation related to section 3.8.6 : Constraints on Model Group Schema Components
        //-- Schema Component Constraint: Model Group Correct
        //-- from the W3C XML Schema Recommendation
        for (int i=0; i<getParticleCount(); i++) {
            Structure temp = getParticle(i);
            switch (temp.getStructureType()) {
                case Structure.MODELGROUP:
                    ModelGroup tempGroup = (ModelGroup)temp;
                    String name = null;
                    if (tempGroup.getReference() != null)
                        name = tempGroup.getReference().getName();

                    if (name != null && name.equals(this.getName())) {
                        if (isRedefined()) {
                        	if (getMaxOccurs() != 1 || getMinOccurs() != 1) {
                        		String err = "in the redefined <group> named:"+this.getName();
                        		err +=  "\nThe particle information (minOccurs, maxOccurs) of a circular group must be set to 1.\n";
                        		throw new ValidationException(err);
                        	}

                        }
                    	else {
                    		String err = "in <group> named:"+this.getName();
                            err +=  "\nCircular groups are disallowed.\n";
                            err += "That is, within the {particles} of a group there must not be at any depth a particle whose {term} is the group itself.\n";
                            throw new ValidationException(err);
                    	}
                    }
                    //check cross-reference
                    int j = 0;
                    tempGroup = tempGroup.getReference();
                    while (j < tempGroup.getParticleCount()) {
                        if (tempGroup.getParticle(j).getStructureType() == Structure.MODELGROUP) {
                            ModelGroup referencedGroup = ((ModelGroup)tempGroup.getParticle(j)).getReference();
                            if ((referencedGroup != null) && (referencedGroup.equals(this))) {
                            	if (isRedefined()) {
                            		if (getMaxOccurs() != 1 || getMinOccurs() != 1) {
                            			String err = "in the redefined <group> named:"+this.getName();
                            			err +=  "\nThe particle information (minOccurs, maxOccurs) of a circular group must be set to 1.\n";
                            			throw new ValidationException(err);
                            		}
                            	}
                            	else {
	                            	String err = "Cross reference between <group>:"+this.getName()+" and <group>:"+tempGroup.getName();
	                                err +=  "\nCircular groups are disallowed.\n";
	                                err += "That is, within the {particles} of a group there must not be at any depth a particle whose {term} is the group itself.\n";
	                                throw new ValidationException(err);
                            	}
                            }

                        }
                        j++;

                    }
                    break;
                default:
                    break;

            }
        }
    } //-- validate

    /**
     * Returns the schema that contains this modelGroup definition
     */
    public Schema getSchema() {
       return _schema;
    }
    /**
     * Sets the Schema that contains this group.
     *
     * @param schema the Schema that contains this group.
     */
    public void setSchema(Schema schema) {
        _schema = schema;
    }



} //-- Group