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


package org.exolab.castor.mapping.loader;


import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;


/**
 * A basic field descriptor implementation. Engines will extend this
 * class to provide additional functionality.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2005-12-06 14:55:28 -0700 (Tue, 06 Dec 2005) $
 * @see ClassDescriptorImpl
 */
public class FieldDescriptorImpl
    implements FieldDescriptor
{

    /**
     * Containing Class (parent)
     */
    private ClassDescriptor     _contClsDesc;

    /**
     * The field handler for get/set field value.
     */
    private FieldHandler        _handler;

    /**
     * The name of this field in the object. The field must have a
     * name, even if it set through accessor methods.
     */
    private String        _fieldName;

    /**
     * The field type.
     */
    private Class         _fieldType;

    /**
     * True if the field is transient and should not be saved/stored.
     */
    private boolean       _transient;

    /**
     * True if the field type is immutable.
     */
    private boolean       _immutable;

    /**
     * True if the field type is required.
     */
    private boolean             _required;

    /**
     * True if the field is multi valued (a collection).
     */
    private boolean       _multi;

    /**
     * The type class descriptor, if this field is of a type
     * known by a descriptor.
     */
    private ClassDescriptor      _clsDesc;

    /**
     * The type convertor from Java type to SQL type.
     */
    private TypeConvertor        _convertor;

    /**
     * The type convertor parameter.
     */
    private String               _convertorParam;
    
    /**
     * Name of a class that implements the {@see java.util.Comparator} interface.
     * To be used together with the "sortedSet" collection type.
     */
    private String _comparator;

    /**
     * Constructs a new field descriptor.
     *
     * @param fieldName The field name
     * @param typeInfo The field type information
     * @param handler The field handler (may be null)
     * @param trans True if the field is transient
     */
    public FieldDescriptorImpl( String fieldName, TypeInfo typeInfo,
                FieldHandler handler, boolean trans, String comparator)
        throws MappingException
    {
        if ( fieldName == null )
            throw new IllegalArgumentException( "Argument 'fieldName' is null" );
        _fieldName = fieldName;
        _handler = handler;
        _immutable = typeInfo.isImmutable();
        _fieldType = typeInfo.getFieldType();
        _required = typeInfo.isRequired();
        _transient = trans;
        _multi = ( typeInfo.getCollectionHandler() != null );
        _convertor = typeInfo.getConvertorFrom();
        _convertorParam = typeInfo.getConvertorParam();
        _comparator = comparator;
    }


    /**
     * Constructor used by derived clases.
     */
    protected FieldDescriptorImpl( FieldDescriptor fieldDesc )
    {
        this._contClsDesc = fieldDesc.getContainingClassDescriptor();
        this._handler = fieldDesc.getHandler();
        this._fieldName = fieldDesc.getFieldName();
        this._fieldType = fieldDesc.getFieldType();
        this._transient = fieldDesc.isTransient();
        this._immutable = fieldDesc.isImmutable();
        this._required = fieldDesc.isRequired();
        this._clsDesc = fieldDesc.getClassDescriptor();
        this._multi = fieldDesc.isMultivalued();
        this._comparator = fieldDesc.getComparator();
    }

    /**
     * Constructor used by derived clases.
     */
    protected FieldDescriptorImpl( FieldDescriptorImpl fieldDesc )
    {
        //-- it is the same code as the previous constructor
        //-- but due to 1.1 compilation compatibility, we can't use super()
        this._contClsDesc = fieldDesc.getContainingClassDescriptor();
        this._handler = fieldDesc.getHandler();
        this._fieldName = fieldDesc.getFieldName();
        this._fieldType = fieldDesc.getFieldType();
        this._transient = fieldDesc.isTransient();
        this._immutable = fieldDesc.isImmutable();
        this._required = fieldDesc.isRequired();
        this._clsDesc = fieldDesc.getClassDescriptor();
        this._multi = fieldDesc.isMultivalued();
        //--



        this._convertor = fieldDesc.getConvertor();
        this._convertorParam = fieldDesc.getConvertorParam();

    }

    public void setContainingClassDescriptor( ClassDescriptor contClsDesc )
    {
        _contClsDesc = contClsDesc;
    }
    public ClassDescriptor getContainingClassDescriptor()
    {
        return _contClsDesc;
    }


    public String getFieldName()
    {
        return _fieldName;
    }


    public Class getFieldType()
    {
        return _fieldType;
    }


    public boolean isTransient()
    {
        return _transient;
    }


    public boolean isImmutable()
    {
        return _immutable;
    }


    public boolean isRequired()
    {
        return _required;
    }


    public boolean isMultivalued()
    {
        return _multi;
    }


    public ClassDescriptor getClassDescriptor()
    {
        return _clsDesc;
    }


    public FieldHandler getHandler()
    {
        return _handler;
    }


    /**
     * Mutator method used by {@link MappingLoader}.
     */
    void setHandler( FieldHandler handler )
    {
        _handler = handler;
    }


    /**
     * Mutator method used by {@link MappingLoader}.
     */
    void setClassDescriptor( ClassDescriptor clsDesc )
    {
        _clsDesc = clsDesc;
    }


    /**
     * Mutator method used by {@link MappingLoader}.
     */
    public void setRequired( boolean required )
    {
        _required = required;
    }

    /**
     * Mutator method used by {@link MappingLoader}.
     */
    public void setTransient (boolean isTransient) {
    	this._transient = isTransient;
    }
    
    /**
     * Returns the convertor from the field type to an external type.
     *
     * @return Convertor from field type
     */
    public TypeConvertor getConvertor()
    {
        return _convertor;
    }


    /**
     * Returns the convertor parameter.
     *
     * @return Convertor parameter
     */
    public String getConvertorParam()
    {
        return _convertorParam;
    }


    public String toString()
    {
        return _fieldName + "(" + _fieldType.getName() + ")";
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.mapping.FieldDescriptor#getComparator()
     */
    public String getComparator() {
        return _comparator;
    }

    /**
     * Sets the name of a class that implements the java.util.Comparator interface.
     * @param comparator Name of a class that implements the java.util.Comparator interface.
     */
    void setComparator(String comparator) {
        this._comparator = comparator;
        
    }

    
}

