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

/**
 * An XML Schema Attribute Definition
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeDecl extends SchemaBase {

    
    
    /**
     * The name of attributes defined by this AttributeDecl
    **/
    private String name = null;
    
    /**
     * The minimum occurance that this attribute must appear
    **/
    private int minOccurs = 0;
    
    DataType dataType = null;
    
    /**
     * Creates a default Attribute declaration.
     * Since name is required for a valid Attribute declaration
     * this should be set before using
    **/
    public AttributeDecl() {
        this(null, 0);
    } //-- Attribute
    
    /**
     * Creates a new AttrDecl with the given name
     * @param name of the Attribute defined by this attribute declaration 
     * @param minOccurs the minimum number of occurances that attributes
     * defined by this definition must appear
     * <BR />
     * The only valid values for minOccurs are 0 and 1.
    **/
    public AttributeDecl(String name, int minOccurs) {
        this.name = name;
        setMinOccurs(minOccurs);
        dataType = new DataType(null, "string");
    } //-- AttrDecl
    
    
    /**
     * Returns the type of this SchemaBase
     * @return the type of this SchemaBase
     * @see org.exolab.xml.schema.SchemaBase
    **/
    public short getDefType() {
        return SchemaBase.ATTRIBUTE;
    } //-- getDefType

    /**
     * Returns the name of attributes defined by this AttributeDecl
     * @return the name of attributes defined by this AttributeDecl
    **/
    public String getName() {
        return name;
    } //-- getName
    
    /**
     * Returns the data type associated with this AttributeDecl
     * @return the data type associated with this AttributeDecl
    **/
    public DataType getDataType() {
        return dataType;
    } //-- getDataType

    public String getDataTypeRef() {
        return dataType.getBaseTypeRef();
    } //-- getDataTypeRef
    
    /**
     * Returns the maximum occurance that attributes defined by this
     * definition must appear
     * @return the maximum occurance that attributes defined by this
     * definition must appear
     * <BR />
     * This value will always be 1. It's simply here because it's
     * specified in the XML Schema.
    **/
    public int getMaxOccurs() {
        return 1;
    } //-- getMaxOccurs
    
    /**
     * Returns the minimum occurance that attributes defined by this
     * definition must appear
     * @return the minimum occurance that attributes defined by this
     * definition must appear
    **/
    public int getMinOccurs() {
        return this.minOccurs;
    } //-- getMinOccurs

    
    /**
     * Checks to see if attributes defined by this definition are required.
     * This is the same as checking for minOccurs equal to 1.     
     * @return true if minOccurs is equal to 1.
    **/
    public boolean getRequired() {
        return (minOccurs == 1);
    } //-- getRequired
    
    public void setDataTypeRef(String name) {
        dataType.setBaseTypeRef(name);
    } //-- setDataTypeRef
    
    
    /**
     * Sets the minimum occurance that attributes defined by this
     * definition must appear
     * @param minOccurs the minimum occurance that attributes defined
     * by this definition must appear
     * <BR />
     * The only valid values for minOccurs are 0 and 1.
    **/
    public void setMinOccurs(int minOccurs) {
        if (minOccurs > 0) minOccurs = 1;
        else minOccurs = 0;
    } //-- setMinOccurs
    
    /**
     * Sets the name of attributes defined by this attribute definition
     * @param name 
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName
    
    /**
     * Checks the validity of this Attribute declaration
     * @exception ValidationException when this Attribute declaration
     * is invalid
    **/
    public void validate() 
        throws ValidationException 
    {
        if (name == null)  {
            String err = "<attribute> is missing required 'name' attribute.";
            throw new ValidationException(err);
        }
        dataType.validate();
        
    } //-- validate
    
} //-- AttrDecl