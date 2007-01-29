/*
 * Copyright 2006 Assaf Arkin, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.jdo.engine;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.TypeInfo;

/**
 * JDO field descriptor. Wraps {@link FieldDescriptorImpl} and adds
 * SQL-related information, type conversion, and set/get for JDBC.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-06 14:55:28 -0700 (Tue, 06 Dec 2005) $
 */
public final class JDOFieldDescriptorImpl
extends FieldDescriptorImpl
implements JDOFieldDescriptor {
    //--------------------------------------------------------------------------

    /** The type convertor from Java type to SQL type. */
    private final TypeConvertor _convertor;

    /** The type convertor parameter. */
    private final String _convertorParam;
    
    /** The SQL name of the field. */
    private final String[] _sqlName;

    private final int[] _sqlType;

    private final String _manyTable;

    private final String[] _manyKey;

    /** True if the field requires dirty checking. */
    private final boolean _dirtyCheck;

    private final boolean _readonly;

    //--------------------------------------------------------------------------

    /**
     * Construct a new field descriptor for the specified field.
     *
     * @param fieldName The field name
     * @param typeInfo The field type information
     * @param handler The field handler (may be null)
     * @param trans True if the field is transient
     */
    public JDOFieldDescriptorImpl(final String fieldName, final TypeInfo typeInfo,
            final FieldHandler handler, final boolean trans,
            final String[] sqlName, final int[] sqlType, final String manyTable,
            final String[] manyKey, final boolean dirtyCheck, final boolean readonly) {
        super();
        
        if (fieldName == null) {
            throw new IllegalArgumentException("Argument 'fieldName' is null");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Argument 'fieldDesc' has no handler");
        }
        
        setFieldName(fieldName);
        setFieldType(typeInfo.getFieldType());
        setHandler(handler);
        setTransient(trans);
        setImmutable(typeInfo.isImmutable());
        setRequired(typeInfo.isRequired());
        setMultivalued(typeInfo.getCollectionHandler() != null);
        
        _convertor = typeInfo.getConvertorFrom();
        _convertorParam = typeInfo.getConvertorParam();
        _sqlName = (sqlName.length == 0 ? null : sqlName);
        _sqlType = sqlType;
        _manyTable = manyTable;
        _manyKey = (manyKey.length > 0 ? manyKey : null);
        _dirtyCheck = dirtyCheck;
        _readonly = readonly;
    }

    //--------------------------------------------------------------------------

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#getConvertor()
     * {@inheritDoc}
     */
    public TypeConvertor getConvertor() {
        return _convertor;
    }

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#getConvertorParam()
     * {@inheritDoc}
     */
    public String getConvertorParam() {
        return _convertorParam;
    }

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#getSQLName()
     * {@inheritDoc}
     */
    public String[] getSQLName() {
        return _sqlName;
    }

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#getSQLType()
     * {@inheritDoc}
     */
    public int[] getSQLType() {
        return _sqlType;
    }

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#getManyTable()
     * {@inheritDoc}
     */
    public String getManyTable() {
        return _manyTable;
    }

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#getManyKey()
     * {@inheritDoc}
     */
    public String[] getManyKey() {
        return _manyKey;
    }

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#isDirtyCheck()
     * {@inheritDoc}
     */
    public boolean isDirtyCheck() {
        return _dirtyCheck;
    }

    /**
     * @see org.exolab.castor.jdo.engine.JDOFieldDescriptor#isReadonly()
     * {@inheritDoc}
     */
    public boolean isReadonly() {
        return _readonly;
    }

    //--------------------------------------------------------------------------

    public String toString() {
        return getFieldName() + "(" + getFieldType().getName() + ")"
            + (_sqlName == null ? "" : " AS " + _sqlName[0]);
    }

    //--------------------------------------------------------------------------
}