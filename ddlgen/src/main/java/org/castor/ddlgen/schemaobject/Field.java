/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen.schemaobject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.typeinfo.TypeInfo;

/**
 * Abstract base class of all field implementations.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class Field extends AbstractSchemaObject {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Field.class);
        
    //--------------------------------------------------------------------------

    /** Key generator for this field. */
    private KeyGenerator _keyGenerator;
    
    /** Is this field part of the identity? */
    private boolean _isIdentity;

    /** Is this field a required one? */
    private boolean _isRequired;
    
    /** Type information of this field. */
    private TypeInfo _type;

    /** Table which contains this field. */
    private Table _table;

    //--------------------------------------------------------------------------

    /**
     * Set key generator for this field. <code>null</code> if the field has no key
     * generator.
     * 
     * @param keyGenerator Key generator for this field.
     */
    public final void setKeyGenerator(final KeyGenerator keyGenerator) {
        _keyGenerator = keyGenerator;
    }
    
    /**
     * Get key generator for this field.
     * 
     * @return Key generator for this field.
     */
    public final KeyGenerator getKeyGenerator() {
        return _keyGenerator;
    }

    /**
     * Set if this field is part of the identity?
     * 
     * @param isIdentity <code>true</code> if the field is part of the identity,
     *        <code>true</code> otherwise.
     */
    public final void setIdentity(final boolean isIdentity) {
        _isIdentity = isIdentity;
    }

    /**
     * Get if this field is part of the identity?
     * 
     * @return <code>true</code> if the field is part of the identity,
     *         <code>true</code> otherwise.
     */
    public final boolean isIdentity() {
        return _isIdentity;
    }

    /**
     * Set if this field is a required one?
     * 
     * @param isRequired <code>true</code> if the field is required, <code>true</code>
     *        otherwise.
     */
    public final void setRequired(final boolean isRequired) {
        _isRequired = isRequired;
    }

    /**
     * Get if this field is a required one?
     * 
     * @return <code>true</code> if the field is required, <code>true</code>
     *         otherwise.
     */
    public final boolean isRequired() {
        return _isRequired;
    }

    /**
     * Set type information of this field.
     * 
     * @param type Type information of this field.
     */
    public final void setType(final TypeInfo type) {
        _type = type;
    }

    /**
     * Get type information of this field.
     * 
     * @return Type information of this field.
     */
    public final TypeInfo getType() {
        return _type;
    }

    /**
     * Set table which contains this field.
     * 
     * @param table Table which contains this field.
     */
    public final void setTable(final Table table) {
        _table = table;
    }

    /**
     * Get table which contains this field.
     * 
     * @return Table which contains this field.
     */
    public final Table getTable() {
        return _table;
    }

    //--------------------------------------------------------------------------
    
    /**
     * Get length parameter from mapping of sql field.
     * <br/>
     * Returns <code>null</code> as it is not supported yet.
     * 
     * @return length Length parameter from mapping of sql field.
     */
    public final Integer getLength() { return null; }

    /**
     * Get precision parameter from mapping of sql field.
     * <br/>
     * Returns <code>null</code> as it is not supported yet.
     * 
     * @return precision Precision parameter from mapping of sql field.
     */
    public final Integer getPrecision() { return null; }

    /**
     * Get decimals parameter from mapping of sql field.
     * <br/>
     * Returns <code>null</code> as it is not supported yet.
     * 
     * @return decimals Decimals parameter from mapping of sql field.
     */
    public final Integer getDecimals() { return null; }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final String toDropDDL() {
        return "";
    }
    
    //--------------------------------------------------------------------------

    /**
     * Check if given field can be merged with this one.
     * 
     * @param field Field to check if it is able to be merged.
     * @throws GeneratorException If fields cannot be merged.
     */
    public final void merge(final Field field) throws GeneratorException {
        if (field == null) {
            String msg = "Field to merge is missing.";
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (!equals(getName(), field.getName())) {
            String msg = "Name of field differs from: " + getName();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (!equals(getTable(), field.getTable())) {
            String msg = "Table of field differs from: " + getTable().getName();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (_isIdentity != field._isIdentity) {
            LOG.warn("Merge table: Field 'identity' attributes are different");
        }
        
        if (_isRequired != field._isRequired) {
            LOG.warn("Merge table: Field 'required' attributes are different");
        }
        
        _type.merge(field._type);
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (other.getClass() != this.getClass()) { return false; }
        
        Field field = (Field) other;
        return equals(getName(), field.getName())
            && equals(_table, field._table)
            && equals(_type, field._type)
            && (_isRequired == field._isRequired)
            && (_isIdentity == field._isIdentity)
            && equals(_keyGenerator, field._keyGenerator);
    }

    /**
     * {@inheritDoc}
     */
    public final int hashCode() {
        int hashCode = 0;
        if (getName() != null) { hashCode += getName().hashCode(); }
        hashCode *= HASHFACTOR;
        if (_table != null) { hashCode += _table.hashCode(); }
        hashCode *= HASHFACTOR;
        if (_type != null) { hashCode += _type.hashCode(); }
        hashCode *= HASHFACTOR;
        hashCode += Boolean.valueOf(_isRequired).hashCode();
        hashCode *= HASHFACTOR;
        hashCode += Boolean.valueOf(_isIdentity).hashCode();
        hashCode *= HASHFACTOR;
        if (_keyGenerator != null) { hashCode += _keyGenerator.hashCode(); }
        return hashCode;
    }

    //--------------------------------------------------------------------------
}
