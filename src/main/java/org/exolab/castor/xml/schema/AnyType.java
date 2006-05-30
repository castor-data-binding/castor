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
 * Copyright 2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;


import org.exolab.castor.xml.ValidationException;

/**
 * A Class which represents the XML Schema AnyType.
 * <BR />
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
**/
public final class AnyType extends XMLType {
    /** SerialVersionUID */
    private static final long serialVersionUID = 7670252205849057981L;

    /**
     * The name of this type
     */
    private String name = "anyType";


    /**
     * The parent Schema for this AnyType
     */
    private Schema _parent = null;

    //------------------/
    //- Constructor(s) -/
    //------------------/
    
    
    /**
     * Creates a new AnyType for the given Schema.
     */
    public AnyType(Schema schema) {
        super();
        if (schema == null) {
            String error = "The 'schema' argument must not be null.";
            throw new IllegalArgumentException(error);
        }
        _parent = schema;
        setSchema(schema);
    } //-- AnyType

    //-------------------/
    //-- Public Methods -/
    //-------------------/

    /**
     * Returns the name of this type. This method
     * always returns 'anyType'.
     *
     * @return the name of this type.
     */
    public String getName() { 
        return name; 
    } //-- getName


    /**
     * Returns the type of this Schema Structure. This
     * method returns Structure.ANYTYPE.
     *
     * @return the type of this Schema Structure.
     */
    public short getStructureType() {
        return Structure.ANYTYPE;
    } //-- getStructureType

    /**
     * Overrides XMLType#setName. The Name of anyType cannot be changed.
     *
     * @param name of the type
     */
    public synchronized void setName(String name) {
        String error = "The name of 'anyType' cannot be changed.";
        throw new IllegalStateException(error);
    } //-- setName


    /**
     * Overrides XMLType#setBaseType(), anyType cannot have a Base type.
     *
     * @param base the base type which this datatype inherits from
     */
    public void setBaseType(XMLType baseType) {
        String error = "'anyType' cannot have a base type";
        throw new IllegalStateException(error);
    } //-- setBaseType


    /**
     * Overrides XMLType#setDerivationMethod, anyType cannot
     * be derived from any other type.
     * 
     * @param derivationMethod the derivation method.
     */
    public void setDerivationMethod(String derivationMethod)
    {
        String error = "'anyType' cannot be derived from other types.";
        throw new IllegalStateException(error);
    }

    /**
     * Sets the Id for this XMLType. The Id must be globally unique
     * within the Schema. Use a null value to remove the Id.
     *
     * @param id the unique Id for this XMLType
    **/
    public void setId(String id) {
        //-- ignore
    } //-- setId

    /**
     * Checks the validity of this Schema defintion.
     *
     * @exception ValidationException when this Schema definition
     * is invalid.
     */
    public void validate() 
        throws ValidationException
    {
         //-- do nothing, this type is always valid.
    } //-- validate

    //----------------------/
    //-- Protected Methods -/
    //----------------------/
    
    /**
     * Sets the parent for this XMLType
     *
     * @param parent the parent Structure for this XMLType
    **/
    protected void setParent(Structure parent) {
        if (parent != _parent) {
            String error = "The parent of 'anyType' cannot be changed.";
            throw new IllegalArgumentException(error);
        }
    } //-- setParent


} //-- AnyType
