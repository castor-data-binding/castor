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
 * Copyright 2000-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.ValidationException;

import java.util.Vector;
import java.util.Enumeration;

/**
 * An XML Schema Attribute Group Definition
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
**/
public final class AttributeGroupDecl extends AttributeGroup {
    /** SerialVersionUID */
    private static final long serialVersionUID = -5401452412514803353L;

    /**
     * Error message for a null argument
    **/
    private static String NULL_ARGUMENT
        = "A null argument was passed to the constructor of " +
           AttributeDecl.class.getName();


    /**
     * The name of this AttributeGroup
    **/
    private String _name = null;

    /**
     * The Schema to which this AttributeDecl belongs
    **/
    private Schema _schema = null;


    /**
     * The collection of attributes for this AttributeGroup
    **/
    private Vector _attributes = null;

    /**
     * The collection of AttributesGroupReferences for this
     * AttributeGroup
    **/
    private Vector _references = null;

    /**
     * the anyattribute wilcard, if any
     */
     private Wildcard _anyAttribute = null;

     /**
      * An attribute that indicates if this AttributeGroupDecl is
      * a redefinition
      */
     private boolean _redefinition = false;
     
    /**
     * Creates a new AttributeGroup definition
     * @param schema the Schema that this AttributeGroup
     * belongs to.
    **/
    public AttributeGroupDecl(Schema schema) {
        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _schema = schema;
        _attributes = new Vector();
        _references = new Vector();
    } //-- AttributeGroupDecl

    /**
     * Adds the given attribute definition to this AttributeGroup
     *
     * @param attrDecl the AttributeDecl to add
    **/
    public void addAttribute(AttributeDecl attrDecl) {

        if (attrDecl == null) return;

        //-- add validation code

        //-- add to internal collection
        _attributes.addElement(attrDecl);
        //--set the parent
        attrDecl.setParent(this);

    } //-- addAttribute

    /**
     * Adds the given AttributeGroupReference to this AttributeGroup
     *
     * @param attrGroup the AttributeGroupReference to add
    **/
    public void addReference(AttributeGroupReference attrGroup) {

        if (attrGroup == null) return;

        //-- add validation code

        //-- add to internal collection
        _references.addElement(attrGroup);

    } //-- addReference


    /**
     * Returns the attributes of THIS attribute group.
     * (not those of the nested groups)
     */
    public Enumeration getLocalAttributes() { return _attributes.elements(); }

    /**
     * Returns the AttributeGroupReference of THIS attribute group.
     * (not those of the nested groups)
     */
    public Enumeration getLocalAttributeGroupReferences() { return _references.elements(); }

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
    public AttributeDecl getAttribute(String name) {

        if (name == null) return null;

        for (int i = 0; i < _attributes.size(); i++) {
            AttributeDecl attr = (AttributeDecl) _attributes.elementAt(i);
            if (name.equals(attr.getName())) return attr;
        }

        for (int i = 0; i < _references.size(); i++) {
            AttributeGroupReference ref =
                (AttributeGroupReference) _references.elementAt(i);

            AttributeDecl attr = ref.getAttribute(name);
            if (attr != null) return attr;
        }

        return null;
    } //-- getAttribute

    /**
     * Returns an Enumeration of all the attributes of this
     * attribute group. The enumeration includes attributes from
     * all AttributeGroupReferences contained in this AttributeGroup.
     *
     * @return an Enumeration of all the attributes of this
     * attribute group.
    **/
    public Enumeration getAttributes() {
        return new AttributeGroupEnumeration(_attributes, _references);
    } //-- getAttributes


    /**
     * Returns the name of this AttributeGroup
     * @return the name of this AttributeGroup, or null, if
     * no name was defined.
    **/
    public String getName() {
        return _name;
    } //-- getName

    /**
     * Returns the Schema that this AttributeGroupDecl belongs to.
     *
     * @return the Schema that this AttributeGroupDecl belongs to
    **/
    public Schema getSchema() {
        return _schema;
    } //-- getSchema

    /**
     * Returns true if this AttributeGroup does not contain any
     * AttributeDecls or any non-empty AttributeGroups
     *
     * @return true if this AttributeGroup does not contain any
     * AttributeDecls or any non-empty AttributeGroups
    **/
    public boolean isEmpty() {

        if (_attributes.size() > 0) return false;

        if (_references.size() == 0) return true;

        for (int i = 0; i < _references.size(); i++) {
            if (!((AttributeGroup)_references.elementAt(i)).isEmpty())
                return false;
        }
        return true;

    } //-- isEmpty

    /**
     * Returns true if this attributeGroupDecl is a redefinition.
     * 
     * @return true if this attributeGroupDecl is a redefinition.
     */
    public boolean isRedefined() {
    	return _redefinition;
    }
    
    /**
     * Removes the given AttributeDecl from this AttributeGroup.
     * @param attr the attribute to remove.
    **/
    public boolean removeAttribute(AttributeDecl attr) {
        if (attr == null )   return false;
        if (_attributes.contains(attr)) {
            _attributes.removeElement(attr);
            return true;
        }
        return false;
    } //-- removeAttribute

    /**
     * Removes the given AttributeGroupReference from this AttributeGroup.
     * @param attrGroup the AttributeGroupReference to remove.
    **/
    public boolean removeReference(AttributeGroupReference attrGroupReference) {
         if (attrGroupReference == null ) return false;
         if (_references.contains(attrGroupReference)) {
            _references.removeElement(attrGroupReference);
            return true;
         }
         return false;
    } //-- removeReference

    /**
     * Sets the wildcard (anyAttribute) of this attribute Group
     * @exception SchemaException thrown when a wildcard as already be set
     * or when the wildCard is not an <anyAttribute>.
     */
     public void setAnyAttribute(Wildcard wildcard)
            throws SchemaException
     {
        if (wildcard != null) {
           if (_anyAttribute != null) {
              String err = "<anyAttribute> already set in this AttributeGroup: "
                           + this.getName();
              throw new SchemaException(err);
           }

           if (!wildcard.isAttributeWildcard()){
              String err = "In AttributeGroup, "+this.getName()
                            +"the wildcard must be an <anyAttribute>";
               throw new SchemaException(err);
           }
        }
        _anyAttribute = wildcard;

     }

    /**
     * Sets the name of this AttributeGroup
     * @param name the name of this AttributeGroup
    **/
    public void setName(String name) {

        if (name == null)
            throw new IllegalArgumentException("name must not be null");

        //-- strip namespace prefix if necessary
        int idx = name.indexOf(':');
        if (idx >= 0)
            this._name = name.substring(idx+1);
        else
            this._name = name;

    } //-- setName
    

    /**
     * Sets this Group has redefined. 
     */
    public void setRedefined() {
    	_redefinition = true;
    }
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ATTRIBUTE_GROUP;
    } //-- getStructureType

    /**
     * Checks the validity of this Attribute declaration
     * @exception ValidationException when this Attribute declaration
     * is invalid
    **/
    public void validate()
        throws ValidationException
    {
        //-- no validation currently needed

    } //-- validate


} //-- AttributeGroup

/**
 * A simple enumerator for the AttributeGroup class
**/
class AttributeGroupEnumeration implements Enumeration {

    private Vector references = null;
    int index = 0;

    private Enumeration enumeration = null;


    AttributeGroupEnumeration(Vector definitions, Vector references) {
        enumeration = definitions.elements();
        if (!enumeration.hasMoreElements()) enumeration = null;
        this.references = references;
    } //-- AttributeGroupEnumeration

    public boolean hasMoreElements() {
        if (enumeration != null) return true;

        int i = index;
        while (i < references.size()) {
            AttributeGroupReference ref =
                (AttributeGroupReference)references.elementAt(i);
            ++i;
            if (!ref.isEmpty()) return true;
        }
        return false;

    } //-- hasMoreElements

    public Object nextElement() {

        if (enumeration != null) {
            Object obj = enumeration.nextElement();
            if (!enumeration.hasMoreElements()) enumeration = null;
            return obj;
        }

        while (index < references.size()) {
            AttributeGroupReference ref =
                (AttributeGroupReference)references.elementAt(index);

            ++index;

            enumeration = ref.getAttributes();
            if (enumeration.hasMoreElements()) {
                Object obj = enumeration.nextElement();
                if (!enumeration.hasMoreElements()) enumeration = null;
                return obj;
            }
        }

        return null;
    }
} //-- AttributeGroupEnumeration
