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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
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
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ModelGroup extends Particle
       implements Referable
{


    /**
     * The name of this ModelGroup
    **/
    private String    name       = null;

    /**
     *
    **/
    private boolean   export     = false;

    /**
     * the name of the ModelGroup referenced
     */
     private String groupRef = null;

    /**
     * An ordered list of all ModelGroup definitions
    **/
    private Vector modelDefs;

    /**
     * the implementation of ContentModelGroup
    **/
    private ContentModelGroup _contentModel = null;


    /**
     * An unordered list of just the the element declarations
    **/
    private Hashtable elements;

    /**
     *
    **/
    private Order order = Order.all;
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
        super();
        this.name  = name;
        _schema = schema;
        modelDefs = new Vector();
        elements = new Hashtable();
        _contentModel = new ContentModelGroupImpl();
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
     * Gets the contentModelGroup
     * @returns the contentModelGroup
     */
     public ContentModelGroup getContentModelGroup() {
        return _contentModel;
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
    } //-- addGroup

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
     * Sets the reference for this ModelGroup definition
     * @param reference the name of the ModelGroup that this
     * definition references
    **/
    public void setReference(String reference) {
        this.groupRef = reference;
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
        if (name != null) return "group:"+name;
        return null;
    } //-- getReferenceId

    /**
     * Returns the reference if any
     * @returns the reference if any
     */
     public ModelGroup getReference() {
        if (groupRef != null)
            return _schema.getModelGroup(groupRef);
        return null;
    } //-- getReference


     /**
      * Returns true if this ModelGroup is referencing another one
      * @returns true if this ModelGroup is referencing another one
      */
     public boolean hasReference() {
         return (groupRef.length() !=0);
     }
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

} //-- Group