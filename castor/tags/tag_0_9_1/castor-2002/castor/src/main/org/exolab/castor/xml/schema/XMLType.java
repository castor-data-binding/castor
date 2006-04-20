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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;


import org.exolab.castor.xml.ValidationException;

/**
 * The base type for XML Schema types, such as complex types
 * and simple types.
 * <BR />
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class XMLType extends Annotated
{
    /**
     * Error message for a null argument
     */
    protected static String NULL_ARGUMENT
        = "A null argument was passed to " + XMLType.class.getName();

    /**
     * The name of this type
     */
    private String name = null;

   /**
     * The base datatype reference
    **/
    private XMLType baseType = null;

    /**
     * The owning Schema to which this type belongs
    **/
    private Schema schema = null;

    /**
     * The name of the derivation method (if any)
     */
    private String derivationMethod= null;


    ///////////////////////////////  Methods  //////////////////////////////////

    /**
     * Returns the name of this type (null if none was defined)
    **/
    public String getName() { return name; }

    /**
     * Sets the name of this type
     * @param name of the type
    **/
    public void setName(String name) { this.name= name; }


    /**
     * Returns true if this XMLType is a ComplexType
     * @return true if this XMLType is a ComplexType
    **/
    public final boolean isComplexType() {
        return (getStructureType() == Structure.COMPLEX_TYPE);
    } //-- isComplexType

    /**
     * Returns true if this XMLType is a SimpleType
     * @return true if this XMLType is a SimpleType
    **/
    public final boolean isSimpleType() {
        return (getStructureType() == Structure.SIMPLE_TYPE);
    } //-- isSimpleType


    /**
     * Returns the schema to which this type belongs
     * @return the Schema to which this type belongs
    **/
    public Schema getSchema() {
        return schema;
    } //-- getSchema

    /**
     * Sets the name of this SimpleType
     * @param schema the Schema to which this Simpletype belongs
    **/
    public void setSchema(Schema schema)
    {
      if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
      }
      this.schema    = schema;
    }

    /**
     * Returns the base type that this type inherits from.
     * If this type is a Simpletype that is a built in primitive type then null is returned.
     * @return the parent type.
    **/
    public XMLType getBaseType() {
        return baseType;
    } //-- getBaseType

    /**
     * Sets the base type for this datatype
     * @param base the base type which this datatype inherits from
    **/
    public void setBaseType(XMLType baseType) {
        this.baseType = baseType;
    } //-- setBaseType

    /**
     * Gets the name of the derivation method used to derive this type from its
     * parent. null for primitive types.
     */
    public String getDerivationMethod() { return derivationMethod; }

    /**
     * Sets the derivation method name
     */
    public void setDerivationMethod(String derivationMethod)
    {
        this.derivationMethod= derivationMethod;
    }

    /**
     * Returns the type this type "really" represents
     * ("this" in most cases), provides the indirection needed by references
     * and forward declarations.
     */
    XMLType getType() { return this; }


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

} //-- XMLType
