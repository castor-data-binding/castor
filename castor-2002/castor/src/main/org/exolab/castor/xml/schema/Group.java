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
 * An XML Schema Group
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Group extends ContentModelGroup 
    implements Referable
{

    
    /**
     * The type of collection
    **/
    private Collection collection = Collection.no;
    
    /**
     * The name of this Group
    **/
    private String    name       = null;
    
    /**
     *
    **/
    private boolean   export     = false;
    
    
    private int _maxOccurs = -1;
    
    private int _minOccurs = 1;
    
    
    private Order order = Order.seq;
    
    /**
     * Creates a new Group, with no name
    **/
    public Group() {
        this(null);
    } //-- Group
    
    /**
     * Creates a new Group with the given name
     * @param name of the Group
    **/
    public Group(String name) {
        super();
        this.name  = name;
    } //-- Group

    
    public Collection getCollection() {
        return this.collection;
    } //-- getCollection

    /**
     * Returns the maximum number of occurances that this grouping may appear
     * @return the maximum number of occurances that this grouping may appear.
     * A non positive (n < 1) value indicates that the value is unspecified.
    **/
    public int getMaxOccurs() {
        return _maxOccurs;
    } //-- getMaxOccurs
    
    /**
     * Returns the minimum number of occurances that this grouping must appear
     * @return the minimum number of occurances that this grouping must appear
     * A negative (n < 0) value indicates that the value is unspecified.
    **/
    public int getMinOccurs() {
        return _minOccurs;
    } //-- getMinOccurs

    
    /**
     * Returns the name of this Group, or null if no name was defined
     * @return the name of this Group, or null if no name was defined
    **/
    public String getName() {
        return name;
    } //-- getName
    
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
     * Sets the maximum occurance that this group may appear
     * @param max the maximum occurance that this group may appear
    **/
    public void setMaxOccurs(int max) {
        _maxOccurs = max;
    } //-- setMaxOccurs
    
    /**
     * Sets the minimum occurance that this group may appear
     * @param min the minimum occurance that this group may appear
    **/
    public void setMinOccurs(int min) {
        _minOccurs = min;
    } //-- setMinOccurs
    
    /**
     * Sets the name of this Group
     * @param name the new name for this Group
    **/
    public void setName(String name) {
        this.name = name;
    } //--setName
    
    /**
     * Sets the type of collection for this Group
     * @param collection the type of collection that this group represents
    **/
    public void setCollection(Collection collection) {
        if (collection == null) this.collection = Collection.no;
        else this.collection = collection;
    } //-- setCollection
    
    /**
     * Sets the Order option for this Group
     * @param order the type of order that this group is restricted to
    **/
    public void setOrder(Order order) {
        if (order == null) this.order = Order.all;
        else this.order = order;
    } //-- setOrder
    
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.GROUP;
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
    
} //-- Group