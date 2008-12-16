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
 * Copyright 1999, 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import java.util.Enumeration;

import org.exolab.castor.xml.ValidationException;

/**
 * An XML Schema Group
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public class Group extends Particle implements ContentModelGroup, Referable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 3133443973681261845L;

    /**
     * the implementation of ContentModelGroup.
     **/
    private ContentModelGroup _contentModel = null;

    /**
     * The name of this Group.
     **/
    private String _name = null;

    /**
     * The Compositor for the Group.
     **/
    private Order _order = Order.sequence;

    /**
     * ID of this Group (if present at all).
     */
    private String _id = null;

    /**
     * True if was created for a group tag, false otherwise (all, choice,
     * sequence).
     */
    private boolean _isModelGroupDefinition = false;

    /**
     * The parent for this Group (either another Group or a ComplexType).
     **/
    private Structure _parent = null;

    /**
     * Creates a new {@link Group}, with no name.
    **/
    public Group() {
        this(null);
    }

    /**
     * Creates a new {@link Group} with the given name.
     * @param name of the {@link Group}
    **/
    public Group(final String name) {
        super();
        this._name  = name;
        _contentModel = new ContentModelGroupImpl();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addWildcard(org.exolab.castor.xml.schema.Wildcard)
     */
    public void addWildcard(final Wildcard wildcard) throws SchemaException {
         if (wildcard.isAttributeWildcard()) {
            throw new SchemaException("only <any> should be add in a group.");
        }
        _contentModel.addWildcard(wildcard);
     }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeWildcard(org.exolab.castor.xml.schema.Wildcard)
     */
    public boolean removeWildcard(final Wildcard wildcard) {
        if (wildcard == null) {
            return false;
        }
        return _contentModel.removeWildcard(wildcard);
     }

    /**
     * Returns the {@link ContentModelGroup} for this group.
     * Only used for a <group/> element
     * @return the ContentModelGroup for this group
     */
    public ContentModelGroup getContentModelGroup() {
        return _contentModel;
    }
     
    /**
     * Returns the ID for this {@link Group}.
     * @return the ID for this {@link Group}, or null if no ID is present
     **/
    public String getId() {
        return _id;
    }

    /**
     * Returns the name of this {@link Group}, or null if no name was defined.
     * @return the name of this {@link Group}, or null if no name was defined
    **/
    public String getName() {
        return _name;
    }

    /**
     * Returns the compositor for this {@link Group}.
     * @return the compositor for this {@link Group}
    **/
    public Order getOrder() {

        //-- Return proper compositor...
        //-- according to XML Schema spec 20000407 section 4.3.5

        //-- Note: it's important not to simply call
        //-- #getParticleCount or #getParticle because those
        //-- methods also perform some trickery
        if (_contentModel.getParticleCount() == 1) {
            Particle particle = _contentModel.getParticle(0);
            if (particle.getStructureType() == Structure.GROUP) {
                if ((getMinOccurs() == 1) && (getMaxOccurs() == 1)) {
                    return ((Group) particle).getOrder();
                }
            }
        }
        return this._order;
    }


    /**
     * Returns the parent of this Group, this value may be null if
     * no parent has been set.
     *
     * @return the parent Structure of this Group.
    **/
    public Structure getParent() {
        return _parent;
    }

    /**
     * Sets if the group is a model group definition.
     * @deprecated Since Castor 0.9.2, to handle properly the <group/>
     * element the class ModelGroup has been created
     * @see ModelGroup
     */
    public void setIsModelGroupDefinition(final boolean isModelGroupDefinition) {
        _isModelGroupDefinition = isModelGroupDefinition;
    }

    /**
     * Tells if the group is a model group definition.
     * @return true if the group is a model group definition (<group/> tag), false
     * otherwise {@literal <all/>}, <choice/>, or <sequence/> tags.
     * @deprecated Since Castor 0.9.2, to handle properly the <group/>
     * element the class {@link ModelGroup} has been created
     * @see ModelGroup

     */
    public boolean isModelGroupDefinition() {
        return _isModelGroupDefinition;
    }


    /**
     * Returns the Id used to refer to this Object.
     * @return the Id used to refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        if (_name != null) {
            return "group:" + _name;
        }
        return null;
    }

    /**
     * Sets the name of this {@link Group}.
     * @param name the new name for this {@link Group}
     **/
    public void setName(final String name) {
        this._name = name;
    }

    /**
     * Sets the ID for this {@link Group}.
     * @param id the ID for this {@link Group}
     **/
    public void setId(final String id) {
        _id = id;
    }

    /**
     * Sets the {@link Order} for this {@link Group}.
     * @param order the type of {@link Order} that this {@link Group} is restricted to
    **/
    public void setOrder(final Order order) {
        if (order == null) {
            this._order = Order.all;
        } else {
            this._order = order;
        }
    }

    //---------------------------------------/
    //- Implementation of ContentModelGroup -/
    //---------------------------------------/

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addElementDecl(org.exolab.castor.xml.schema.ElementDecl)
     */
    public void addElementDecl(final ElementDecl elementDecl)
            throws SchemaException {
        _contentModel.addElementDecl(elementDecl);
        // --set the parent
        elementDecl.setParent(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeElementDecl(org.exolab.castor.xml.schema.ElementDecl)
     */
    public boolean removeElementDecl(final ElementDecl element) {
         return _contentModel.removeElementDecl(element);
     }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addGroup(org.exolab.castor.xml.schema.Group)
     */
    public void addGroup(final Group group) throws SchemaException {
        _contentModel.addGroup(group);

        // -- set reference to parent
        group.setParent(this);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeGroup(org.exolab.castor.xml.schema.Group)
     */
    public boolean removeGroup(final Group group){
        boolean result = _contentModel.removeGroup(group);
        group.setParent(null);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#addGroup(org.exolab.castor.xml.schema.ModelGroup)
     */
    public void addGroup(final ModelGroup group) throws SchemaException {
        _contentModel.addGroup(group);

        // -- set reference to parent
        group.setParent(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#removeGroup(org.exolab.castor.xml.schema.ModelGroup)
     */
    public boolean removeGroup(final ModelGroup group) {
        boolean result = _contentModel.removeGroup(group);
        group.setParent(null);
        return result;
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.schema.ContentModelGroup#enumerate()
     */
    public Enumeration<Annotated> enumerate() {
        // -- Some trickery to properly handle
        // -- XML Schema spec 20000407 section 4.3.5

        if (_contentModel.getParticleCount() == 1) {
            Particle particle = _contentModel.getParticle(0);
            if (particle.getStructureType() == Structure.GROUP) {
                Group temp = (Group) particle;
                if (((getMinOccurs() == 1) && (getMaxOccurs() == 1))
                        && ((temp.getMinOccurs() == 1) && (temp.getMaxOccurs() == 1))) {
                    return temp.enumerate();
                }
            }
        }
        return _contentModel.enumerate();
    } //-- enumerate
    
     
    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getElementDecl(java.lang.String)
     */
    public ElementDecl getElementDecl(final String name) {
        return _contentModel.getElementDecl(name);
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getParticle(int)
     */
    public Particle getParticle(final int index) {
        // -- Some trickery to properly handle
        // -- XML Schema spec 20000407 section 4.3.5
        if (_contentModel.getParticleCount() == 1) {
            Particle particle = _contentModel.getParticle(0);
            if (particle.getStructureType() == Structure.GROUP) {
                if ((getMinOccurs() == 1) && (getMaxOccurs() == 1)) {
                    return ((Group) particle).getParticle(index);
                }
            }
        }
        return _contentModel.getParticle(index);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.ContentModelGroup#getParticleCount()
     */
    public int getParticleCount() {
        // -- Some trickery to properly handle
        // -- XML Schema spec 20000407 section 4.3.5
        if (_contentModel.getParticleCount() == 1) {
            Particle particle = _contentModel.getParticle(0);
            if (particle.getStructureType() == Structure.GROUP) {
                if ((getMinOccurs() == 1) && (getMaxOccurs() == 1)) {
                    return ((Group) particle).getParticleCount();
                }
            }
        }
        return _contentModel.getParticleCount();
    }

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.schema.Structure#getStructureType()
     */
    public short getStructureType() {
        return Structure.GROUP;
    }

   /**
    * A helper method that returns true if this group
    * contains an {@literal <any>} element.
    * @return  method that returns true if this group
    * contains an {@literal <any>} element.
    */
    public boolean hasAny() {
        boolean result = false;
        Enumeration<Structure> enumeration = _contentModel.enumerate();
        while (enumeration.hasMoreElements() && !result) {
            Structure struct = enumeration.nextElement();
            switch (struct.getStructureType()) {
                case Structure.ELEMENT:
                    break;
                case Structure.GROUP:
                case Structure.MODELGROUP:
                    result = ((Group) struct).hasAny();
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
     * Checks the validity of this {@link Group} defintion.
     *
     * @throws ValidationException when this {@link Group} definition
     * is invalid.
    **/
    public void validate() throws ValidationException {
        if (_order == Order.all) {
            if (getMaxOccurs() != 1) {
                String err = "Wrong maxOccurs value for a <all>:" + getMaxOccurs();
                err += "\n1 is the only possible value.";
                throw new ValidationException(err);
            }
            if (getMinOccurs() > 1) {
                String err = "Wrong minOccurs value for a <all>:" + getMinOccurs();
                err += "\n0 or 1 are the only possible values.";
                throw new ValidationException(err);
            }
        }
        Enumeration<Structure> enumeration = _contentModel.enumerate();
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement().validate();
        }
    }

    /**
     * Sets the parent for this {@link Group}.
     *
     * @param parent the parent {@link Structure} for this {@link Group}
     **/
    protected void setParent(final Structure parent) {
        if (parent != null) {
            switch (parent.getStructureType()) {
                case Structure.COMPLEX_TYPE:
                case Structure.GROUP:
                case Structure.MODELGROUP:
                case Structure.SCHEMA:
                    break;
                default:
                    String error = "Invalid parent for group";
                    throw new IllegalArgumentException(error);
            }
        }
        _parent = parent;
    }

    /**
     * Indicates whether this {@link Particle} is 'emptiable'
     * @return true if this Particle is 'emptiable'
     */
    public boolean isEmptiable() {
      if (getMinOccurs() == 0) {
            return true;
        }
      
      boolean result = false;
      switch (this.getOrder()) {
      case choice:
        {
            result = false;
            Enumeration<Annotated> enumerate = this.enumerate();
            while (enumerate.hasMoreElements()) {
                Particle p = (Particle) enumerate.nextElement();
                if (p.isEmptiable()) {
                    result = true;
                    break;
                }
            }
        }
        break;
        
      case all:
      case sequence:
        {
            result = true;
            Enumeration<Annotated> enumerate = this.enumerate();
            while (enumerate.hasMoreElements()) {
                Particle p = (Particle) enumerate.nextElement();
                if (!p.isEmptiable()) {
                    result = false;
                    break;
                }
            }
        }
        break;
      }        
      return result;

    }

}