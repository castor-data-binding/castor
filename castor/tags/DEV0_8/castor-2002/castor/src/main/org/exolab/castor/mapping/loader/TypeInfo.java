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


package org.exolab.castor.mapping.loader;


import org.exolab.castor.mapping.TypeConvertor;


/**
 * Type information passed on creation of a {@link FieldHandlerImpl}.
 * 
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class TypeInfo
{


    /**
     * The field type.
     */
    private Class          _fieldType;


    /**
     * Convertor to the field type from external type.
     */
    private TypeConvertor _convertorTo;


    /**
     * Convertor from the field type to external type.
     */
    private TypeConvertor _convertorFrom;


    /**
     * True if the field type is immutable.
     */
    private boolean       _immutable = false;


    /**
     * True if the field is required.
     */
    private boolean        _required = false;


    /**
     * The default value of the field.
     */
    private Object         _default;


    /**
     * The collection type of the field.
     */
    private Class          _colType;


    /**
     * Construct new type information for a field. This field
     * requires no type conversion, and has no default value.
     *
     * @param fieldType The field type
    **/
    public TypeInfo(Class fieldType) {
        this(fieldType, null, null, false, null, null );
    } //-- TypeInfo

    
    /**
     * Construct new type information for the field.
     *
     * @param fieldType The field type
     * @param convertorTo Convertor to the field type from external
     *  type, or null if no conversion is required
     * @param convertorFrom Convertor from the field type to external
     *  type, or null if no conversion is required
     * @param required True if the field is required
     * @param defaultValue The default value of the field, null to
     *  use the known Java defaults
     * @param colType The collection type for this field, or null if
     *  field is singular
     */
    public TypeInfo( Class fieldType, TypeConvertor convertorTo, TypeConvertor convertorFrom,
                     boolean required, Object defaultValue, Class colType )
    {
        _fieldType = Types.typeFromPrimitive( fieldType );
        _convertorTo = convertorTo;
        _convertorFrom = convertorFrom;
        _immutable = Types.isImmutable( fieldType );
        _required = required;
        // Note: must be called with fieldType (might be primitive) and not
        // _fieldType (never primitive) to get the proper default value
        _default = ( defaultValue == null ? Types.getDefault( fieldType ) : defaultValue );
        _colType = colType;
    }


    /**
     * Returns the field type.
     *
     * @return The field type
     */
    public Class getFieldType()
    {
        return _fieldType;
    }


    /**
     * Returns the convertor to the field type from an external type.
     *
     * @return Convertor to field type
     */
    public TypeConvertor getConvertorTo()
    {
        return _convertorTo;
    }


    /**
     * Returns the convertor from the field type to an external type.
     *
     * @return Convertor from field type
     */
    public TypeConvertor getConvertorFrom()
    {
        return _convertorFrom;
    }


    /**
     * Returns true if field type is immutable.
     *
     * @return True if type is immutable
     */
    public boolean isImmutable()
    {
        return _immutable;
    }


    /**
     * Returns true if field type is required.
     *
     * @return True if field is required
     */
    public boolean isRequired()
    {
        return _required;
    }


    /**
     * Returns the default value for the field.
     *
     * @return The default value
     */
    public Object getDefaultValue()
    {
        return _default;
    }


    /**
     * Return the collection type of this field.
     *
     * @return The collection type of this field
     */
    public Class getCollectionType()
    {
        return _colType;
    }

    /**
     * Sets a flag indictating if the field is required.
     *
     * @param required the value of the flag. Should be true if the 
     * field is required, false otherwise.
     */
    public void setRequired(boolean required) {
        this._required = required;
    } //-- setRequired
}


