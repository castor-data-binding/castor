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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.schema;

import java.util.Enumeration;
import java.util.Vector;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.validators.ValidationUtils;

/**
 * The base class for the XML Schema Identity Constraints 
 * (key, keyref, unique).
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public abstract class IdentityConstraint extends Annotated {
    
    
    /**
     * Identity Constraint id
    **/
    private String _id   = null;
    
    /**
     * Identity Constraint name
    **/
    private String _name = null;
    
    /**
     * Identity Constraint Selector
    **/
    private IdentitySelector _selector = null;
    
    /**
     * The fields of this Identity Constraint
    **/
    private Vector _fields = null;
    
    /**
     * Constructor used by sub-classes. Creates a new IdentityConstraint.
     *
     * @param name the name for the IdentityConstraint. Must not be null.
    **/
    protected IdentityConstraint(String name) 
        throws SchemaException
    {
        setName(name);
        _fields = new Vector(3);
    } //-- IdentityConstraint

    /**
     * Adds the given IdentityField to this IdentityConstraint
     *
     * @param field the IdentityField to add.
    **/
    public void addField(IdentityField field) {
        if (field != null)
            _fields.addElement(field);
    } //-- addField
    
    /**
     * Returns an Enumeration of the IdentityFields contained within this
     * IdentityConstraint. 
     *
     * @return an Enumeration of the IdentityField objects contain within
     * this IdentityConstraint.
    **/
    public Enumeration getFields() {
        return _fields.elements();
    } //-- getFields
    
    /**
     * Returns the Id of this IdentityConstraint, or null if no
     * Id has been set.
     *
     * @return the Id of this IdentityConstraint, or null if no
     * Id has been set.
    **/
    public String getId() {
        return _id;
    } //-- getId
    
    /**
     * Returns the name of this IdentityConstraint. This value will
     * never be null.
     *
     * @return the name of this IdentityConstraint
    **/
    public String getName() {
        return _name;
    } //-- getName

    /**
     * Returns the selector of this IdentityConstraint. 
     *
     * @return the IdentitySelector of this IdentityConstraint
    **/
    public IdentitySelector getSelector() {
        return _selector;
    } //-- getSelector
    
    /**
     * Removes the given IdentityField from this IdentityConstraint.
     *
     * @return true if the IdentityField was contained within this 
     * IdentityConstraint, otherwise false.
    **/
    public boolean removeField(IdentityField field) {
        return _fields.removeElement(field);
    } //-- removeField
    
     
    /**
     * Sets the Id for this IdentityConstraint.
     *
     * @param id the Id for this IdentityConstraint. 
    **/
    public void setId(String id) {
        _id = id;
    } //-- setId
    
    /**
     * Sets the name for this IdentityConstraint.
     *
     * @param name the name for this IdentityConstraint. Must not be null.
     * @exception SchemaException if name is null.
    **/
    public void setName(String name) 
        throws SchemaException
    {
        if (name == null) 
            throw new SchemaException("The name of an IdentityConstraint must not be null.");
            
        _name = name;
    } //-- setName
    
    /**
     * Sets the selector for this IdentityConstraint.
     *
     * @param selector the Selector for this IdentityConstraint. Must not be 
     * null.
     * @exception SchemaException if selector is null.
    **/
    public void setSelector(IdentitySelector selector) 
        throws SchemaException
    {
        if (selector == null) 
            throw new SchemaException("The selector of an IdentityConstraint must not be null.");
        _selector = selector;
    } //-- setSelector
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public abstract short getStructureType();

    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
        String err = null;
        
        //-- name must be a NCName
        if (!ValidationUtils.isNCName(_name)) {
            err = "The name of an IdentityConstraint must be an NCName.";
        }
        //-- selector must exist
        else if (_selector == null) {
            err = "Selector for IdentityConstraint cannot be null.";
        }
        //-- at least 1 (one) field must exist
        else if (_fields.size() < 1) {
            err = "There must be at least one 'field' in an "
                + "identity constraint.";
        }
        
        if (err != null) throw new ValidationException(err);
        
    } //-- validate
    
} //-- class IdentityConstraint