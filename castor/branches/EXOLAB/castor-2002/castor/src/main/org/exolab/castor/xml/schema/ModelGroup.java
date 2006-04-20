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
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * An XML Schema ModelGroup
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ModelGroup extends ContentModelType {

    
    /**
     * The name of this ModelGroup
    **/
    private String    name       = null;
    
    /**
     *
    **/
    private boolean   export     = false;
    
    
    /**
     * An ordered list of all ModelGroup definitions
    **/
    private Vector modelDefs;
    
    /**
     * An unordered list of just the the element declarations
    **/
    private Hashtable elements;
    
    /**
     *
    **/
    private Order order = Order.all;
    
    /**
     * Creates a new ModelGroup, with no name
    **/
    public ModelGroup() {
        this(null);
    } //-- ModelGroup
    
    /**
     * Creates a new ModelGroup with the given name
     * @param name of the ModelGroup
    **/
    public ModelGroup(String name) {
        super();
        this.name  = name;
        modelDefs = new Vector();
        elements = new Hashtable();
    } //-- ModelGroup


    /**
     * Adds the given ElementDecl to this Archetype
     * @param elementDecl the ElementDecl to add to this Archetype
     * @exception SchemaException when an ElementDecl already
     * exists with the same name as the given ElementDecl
     * @exception SchemaException when this Archetype content
     * model is either "empty" or "textOnly"
    **/
    public void addElementDecl(ElementDecl elementDecl) 
        throws SchemaException
    {
        //-- check for naming collisions
        if (elements.get(elementDecl.getName()) != null) {
            String err = "An element declaration with the given name, ";
            err += elementDecl.getName() + ", already exists in this scope.";
            throw new SchemaException(err);
        }
        
        //-- add element to modelDefs
        modelDefs.addElement(elementDecl);
        //-- save elements in cache for doing name lookups
        elements.put(elementDecl.getName(), elementDecl);
    } //-- addElementDecl
    
    
    /**
     * Adds the given ModelGroup to this ModelGroup
     * @param modelGroup the ModelGroup to add to this ModelGroup
    **/
    public void addModelGroup(ModelGroup modelGroup) {
        if (!modelDefs.contains(modelGroup)) {
            modelDefs.addElement(modelGroup);
        }
    } //-- addModelGroup
    
    
    /**
     * Returns the type of this SchemaBase
     * @return the type of this SchemaBase
     * @see org.exolab.xml.schema.SchemaBase
    **/
    public short getDefType() {
        return SchemaBase.MODELGROUP;
    } //-- getDefType
    
    /** 
     * Returns an ordered Enumeration of all the ContentModelType
     * definitions (element, group, modelGroupRef)+
    **/
    public Enumeration getDeclarations() {
        return modelDefs.elements();   
    } //-- getDeclarations
    
    public boolean getExport() {
        return this.export;
    } //-- getExport
    
    /**
     * Returns the name of this ModelGroup, or null if no name was defined
     * @return the name of this ModelGroup, or null if no name was defined
    **/
    public String getName() {
        return name;
    } //-- getName
    
    public Order getOrder() {
        return order;
    } //-- getOrder

    /**
     * Sets the name of this ModelGroup
     * @param name the new name for this ModelGroup
    **/
    public void setName(String name) {
        this.name = name;
    } //--setName
    
    public void setOrder(Order order) {
        if (order == null) this.order = Order.all;
        this.order = order;
    }
    
    /**
     * Checks the validity of this ModelGroup declaration
     * @exception ValidationException when this ModelGroup declaration
     * is invalid
    **/
    public void validate() throws ValidationException {
        //-- do nothing
    } //-- validate
    
} //-- Group