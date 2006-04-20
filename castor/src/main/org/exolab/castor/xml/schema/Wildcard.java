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
 * Date         Author          Changes
 * 04/02/2001   Arnaud Blandin  Created
 */

package org.exolab.castor.xml.schema;

import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.ValidationException;

/**
 * A class that represents an XML Schema Wildcard.
 * A wilcard is represented by the XML elements <any> and
 * <anyAttribute> and can be hold in a complexType or in
 * a ModelGroup (<group>).
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 */
public class Wildcard extends Particle {

    /**
     * The vector where we store the list of namespaces
     */
    //don't use ArrayList to keep compatibility with jdk1.1
    private Vector _namespaces;

    /**
     * A boolean that indicates if this wildcard represents
     * <anyAttribute>.
     * By default a wildcard represents <any>
     */
    private boolean _attribute = false;

    /**
     * The complexType that holds this wildcard.
     */
     private ComplexType _complexType;

    /**
     * The Group (<sequence> or <choice>) that holds this wildcard.
     */
     private Group _group;

     /**
      * The Attribute Group that holds the wildcard
      */
      private AttributeGroup  _attGroup;

    /**
     * the processContent of this wildcard.
     * (strict by default)
     */
     private String _processContents;

    /**
     * the id for this wildcard
     */
    private String _id  = null;


    /**
     * The wildcard is embedded in a complexType
     * @param ComplexType the complexType that contains this wildcard
     */
    public Wildcard(ComplexType complexType) {
        _complexType = complexType;
        init();
    }

    /**
     * The wildcard is embedded in a ModelGroup (<group>)
     * @param group the ModelGoup that contains this wildcard
     */
    public Wildcard(Group group) {
        _group = group;
        init();
    }

    /**
     * The wildcard is embedded in an AttributeGroup.
     * @param attGroup the AttributeGroup that contains this wildcard
     */
    public Wildcard(AttributeGroup attGroup) {
        _attGroup = attGroup;
        init();
    }

    private void init() {
        //in general not more than one namespace
        _namespaces = new Vector(1);
        setMaxOccurs(1);
        setMinOccurs(1);
        try {
           setProcessContents(SchemaNames.STRICT);
        } catch (SchemaException e) {
           //nothing to do since we are
           //not 'out of bounds' by using an hard coded value
        }
    }

    /**
     * add a namespace
     * @param String the namespace to add
     */
     public void addNamespace(String Namespace) {
         _namespaces.addElement(Namespace);
     }

   /**
    * Removes the given namespace from the namespace collection
    * @param namespace the namespace to remove.
    */
    public boolean removeNamespace(String namespace) {
       if (namespace == null)
           return false;
       int position = _namespaces.indexOf(namespace);
	   if (position >= 0) {
	        _namespaces.removeElementAt(position);
	         return true;
	   }
       return false;
    }

    /**
     * Returns the complexType that contains this wildcard, can return null.
     * @return the complexType that contains this wildcard (can be null).
     */
    public ComplexType getComplexType() {
        return _complexType;
    }

    /**
     * Returns the model group that contains this wildcard, can return null.
     * @return the model group that contains this wildcard (can be null).
     */
    public Group getModelGroup() {
        return _group;
    }

    /**
     * Returns the AttributeGroup that contains this wilcard (can return null)
     * @return the AttributeGroup that contains this wilcard (can return null)
     */
    public AttributeGroup getAttributeGroup() {
        return _attGroup;
    }

    /**
     * Returns the parent schema in which this wildcard is located.
     *
     * @return the schema that contains the parent structure of this wildcard.
     */
    public Schema getSchema() {
        if (_complexType != null)
            return _complexType.getSchema();
        else if (_attGroup != null) {
            if (_attGroup instanceof AttributeGroupDecl)
                return ((AttributeGroupDecl)_attGroup).getSchema();
            else if (_attGroup instanceof AttributeGroupReference) {
                AttributeGroup tempRef = ((AttributeGroupReference)_attGroup).resolveReference();
                if (tempRef instanceof AttributeGroupDecl)
                    return ((AttributeGroupDecl)tempRef).getSchema();
                else {
                     tempRef = null;
                     return null;
                }
            }
        }
        else if (_group != null) {
            Structure parent = _group.getParent();
            Schema result = null;
            if (parent != null) {
                while (result == null) {
                    switch (parent.getStructureType()) {
                        case Structure.COMPLEX_TYPE:
                           result = ((ComplexType)parent).getSchema();
                            break;
                        case Structure.MODELGROUP:
                            result = ((ModelGroup)parent).getSchema();
                            break;
                        case Structure.GROUP:
                            parent = ((Group)parent).getParent();
                            break;
                        default:
                            String err = "A group can only be child of a complexType";
                            err += " or a ModelGroup or a group.";
                            throw new IllegalStateException(err);
                    }
                }
            }//--if
            return result;
        }
        return null;
    }
    /**
     * Returns an enumeration that contains the different namespaces
     * of this wildcard
     * @return an enumeration that contains the different namespaces
     * of this wildcard
     */
     public Enumeration getNamespaces() {
         return _namespaces.elements();
     }

    /**
     * Returns the processContent of this wildcard
     * @return the processContent of this wildcard
     */
     public String getProcessContent() {
         return _processContents;
     }


    /**
     * Returns true if this wildcard represents <anyAttribute> otherwise false
     * @return true if this wildcard represents <anyAttribute> otherwise false
     */
     public boolean isAttributeWildcard() {
         return _attribute;
     }

    /**
     * Sets this wildcard to represent <anyAttribute>
     */
    public void setAttributeWildcard() {
         _attribute = true;
    }

    /**
     * Sets the ID for this Group
     * @param id the ID for this Group
     */
    public void setId(String id) {
        _id = id;
    } //-- setId


    /**
     * Sets the processContent of the wildCard
     * @param process the process content to set
     * @exception SchemaException thrown when the processContent is not valid
     */
    public void setProcessContents(String process)
        throws SchemaException
    {
        if (!SchemaNames.isProcessName(process))
           throw new SchemaException("processContents attribute not valid:" +process);
        _processContents = process;
    }
    public void validate() throws ValidationException {
    //only do the validation on the namespace
    }

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.WILDCARD;
    }
}