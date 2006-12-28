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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.GeneratorException;

/**
 * Abstract base class for all foreign keys.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class ForeignKey extends AbstractSchemaObject  {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(ForeignKey.class);

    //--------------------------------------------------------------------------

    /** Relation type: one-one. */
    public static final int ONE_ONE = 0;
    
    /** Relation type: one-many. */
    public static final int ONE_MANY = 1;
    
    /** Relation type: many-many. */
    public static final int MANY_MANY = 2;

    /** Type of the relation. */
    private int _relationType = ONE_ONE;
    
    /** List of fields referenced by the foreign key. */
    private List _referencedFields = new ArrayList();

    /** Table referenced by the foreign key. */
    private Table _referencedTable;
    
    /** List of foreign key fields. */
    private List _fields = new ArrayList();

    /** Table that holds foreign key. */
    private Table _table;

    //--------------------------------------------------------------------------

    /**
     * Set type of relation.
     * 
     * @param relationType Type of relation.
     */
    public final void setRelationType(final int relationType) {
        _relationType = relationType;
    }

    /**
     * Get type of relation.
     * 
     * @return Type of relation
     */
    public final int getRelationType() {
        return _relationType;
    }

    /**
     * Add given field to list of fields referenced by the foreign key.
     * 
     * @param field Field to add to list of fields referenced by the foreign key.
     */
    public final void addReferenceField(final Field field) {
        _referencedFields.add(field);
    }
    
    /**
     * Get number of fields referenced by the foreign key.
     * 
     * @return Number of fields referenced by the foreign key.
     */
    public final int getReferenceFieldCount() {
        return _referencedFields.size();
    }
    
    /**
     * Get field referenced by the foreign key at given index.
     * 
     * @param index Index of referenced field to return.
     * @return Referneced field at given index.
     */
    public final Field getReferenceField(final int index) {
        return (Field) _referencedFields.get(index);
    }
    
    /**
     * Set table referenced by the foreign key.
     * 
     * @param table Table referenced by the foreign key.
     */
    public final void setReferenceTable(final Table table) {
        _referencedTable = table;
    }

    /**
     * Get table referenced by the foreign key.
     * 
     * @return Table referenced by the foreign key.
     */
    public final Table getReferenceTable() {
        return _referencedTable;
    }

    /**
     * Add given field to list of foreign key fields.
     * 
     * @param field Field to add to list of foreign key fields.
     */
    public final void addField(final Field field) {
        _fields.add(field);
    }
    
    /**
     * Get number of foreign key fields.
     * 
     * @return Number of foreign key fields.
     */
    public final int getFieldCount() {
        return _fields.size();
    }
    
    /**
     * Get foreign key field at given index.
     * 
     * @param index Index of foreign key field to return.
     * @return Foreign key field at given index.
     */
    public final Field getField(final int index) {
        return (Field) _fields.get(index);
    }
    
    /**
     * Set table that holds foreign key.
     * 
     * @param table Table that holds foreign key.
     */
    public final void setTable(final Table table) {
        _table = table;
    }

    /**
     * Get table that holds foreign key.
     * 
     * @return Table that holds foreign key.
     */
    public final Table getTable() {
        return _table;
    }

    //--------------------------------------------------------------------------

    /**
     * Concatenate all field names delimited by field delimiter and whitespace.
     * 
     * @return Field names delimited by field delimiter and whitespace.
     */
    protected final String fieldNames() {
        String delimiter = DDLGenConfiguration.DEFAULT_FIELD_DELIMITER;
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < getFieldCount(); i++) {
            if (i > 0) { sb.append(delimiter).append(' '); }
            sb.append(getField(i).getName());
        }
        return sb.toString();
    }

    /**
     * Concatenate all referenced field names delimited by field delimiter and whitespace.
     * 
     * @return Referenced field names delimited by field delimiter and whitespace.
     */
    protected final String referencedFieldNames() {
        String delimiter = DDLGenConfiguration.DEFAULT_FIELD_DELIMITER;
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < getReferenceFieldCount(); i++) {
            if (i > 0) { sb.append(delimiter).append(' '); }
            sb.append(getReferenceField(i).getName());
        }
        return sb.toString();
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final String toDropDDL() {
        return "";
    }
    
    //--------------------------------------------------------------------------

    /**
     * Check if given foreign key can be merged with this one.
     * 
     * @param fk Foreign key to check if it is able to be merged.
     * @throws GeneratorException If foreign keys cannot be merged.
     */
    public final void merge(final ForeignKey fk) throws GeneratorException {
        if (fk == null) {
            String msg = "Foreign key to merge is missing.";
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (!equals(getName(), fk.getName())) {
            String msg = "Name of foreign key differs from: " + getName();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (!equals(getTable(), fk.getTable())) {
            String msg = "Table of foreign key differs from: " + getTable().getName();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (getFieldCount() != fk.getFieldCount()) {
            String msg = "Field count of foreign key differs from: " + getFieldCount();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        for (int i = 0; i < getFieldCount(); i++) {
            if (!equals(getField(i), fk.getField(i))) {
                String msg = "Field of foreign key differs from: "
                           + getField(i).getName();
                LOG.error(msg);
                throw new GeneratorException(msg); 
            }
        }
        
        if (!equals(getReferenceTable().getName(), fk.getReferenceTable().getName())) {
            String msg = "Referenced table of foreign key differs from: "
                       + getReferenceTable().getName();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (getReferenceFieldCount() != fk.getReferenceFieldCount()) {
            String msg = "Referenced field count of foreign key differs from: "
                       + getReferenceFieldCount();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        for (int i = 0; i < getReferenceFieldCount(); i++) {
            if (!equals(getReferenceField(i), fk.getReferenceField(i))) {
                String msg = "Referenced field of foreign key differs from: "
                           + getReferenceField(i).getName();
                LOG.error(msg);
                throw new GeneratorException(msg); 
            }
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (other.getClass() != this.getClass()) { return false; }
        
        ForeignKey fk = (ForeignKey) other;
        return equals(getName(), fk.getName())
            && equals(_table, fk._table)
            && equals(_fields, fk._fields)
            && equals(_referencedTable, fk._referencedTable)
            && equals(_referencedFields, fk._referencedFields)
            && (_relationType == fk._relationType);
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
        hashCode += _fields.hashCode();
        hashCode *= HASHFACTOR;
        if (_referencedTable != null) { hashCode += _referencedTable.hashCode(); }
        hashCode *= HASHFACTOR;
        hashCode += _referencedFields.hashCode();
        hashCode *= HASHFACTOR;
        hashCode += _relationType;
        return hashCode;
    }

    //--------------------------------------------------------------------------
}
