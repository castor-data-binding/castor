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

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.DDLWriter;

/**
 * Abstract base class for all primary keys.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class PrimaryKey extends AbstractSchemaObject {
    //--------------------------------------------------------------------------

    /** List of primary key fields. */
    private List _fields = new ArrayList();

    /** Table the primary key is used for. */
    private Table _table;

    //--------------------------------------------------------------------------
    
    /**
     * Add given field to list of primary key fields.
     * 
     * @param field Field to add to list of primary key fields.
     */
    public final void addField(final Field field) {
        _fields.add(field);
    }
    
    /**
     * Get number of primary key fields.
     * 
     * @return Number of primary key fields.
     */
    public final int getFieldCount() {
        return _fields.size();
    }
    
    /**
     * Get primary key field at given index.
     * 
     * @param index Index of primary key field to return.
     * @return Primary key field at given index.
     */
    public final Field getField(final int index) {
        return (Field) _fields.get(index);
    }
    
    /**
     * Set table the primary key is used for.
     * 
     * @param table Table the primary key is used for.
     */
    public final void setTable(final Table table) {
        _table = table;
    }

    /**
     * Get table the primary key is used for.
     * 
     * @return Table the primary key is used for.
     */
    public final Table getTable() {
        return _table;
    }

    //--------------------------------------------------------------------------

    /**
     * Concatenate all field names delimited by field delimiter and whitespace.
     * 
     * @param writer DDLWriter to write schema objects to.
     */
    protected final void fieldNames(final DDLWriter writer) {
        String delimiter = DDLGenConfiguration.DEFAULT_FIELD_DELIMITER;
        
        for (int i = 0; i < getFieldCount(); i++) {
            if (i > 0) {
                writer.print(delimiter);
                writer.print(" ");
            }
            writer.print(getField(i).getName());
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final void toDropDDL(final DDLWriter writer) { }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (other.getClass() != this.getClass()) { return false; }
        
        PrimaryKey pk = (PrimaryKey) other;
        return equals(getName(), pk.getName())
            && equals(_table, pk._table)
            && equals(_fields, pk._fields);
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
        return hashCode;
    }

    //--------------------------------------------------------------------------
}
