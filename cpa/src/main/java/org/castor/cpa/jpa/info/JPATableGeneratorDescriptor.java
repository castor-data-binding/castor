/*
 * Copyright 2010 Werner Guttmann
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
package org.castor.cpa.jpa.info;

import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

public class JPATableGeneratorDescriptor extends JPAKeyGeneratorDescriptor {

    private String _pkColumnName;
    private String _pkColumnValue;
    private String _schema;
    private String _table;
    private JPAUniqueConstraint[] _uniqueConstraints;
    private String _valueColumnName;
    private Class<?> _primaryKeyType;
    
    public static JPATableGeneratorDescriptor extract(final TableGenerator tableGenerator) {
        JPATableGeneratorDescriptor descriptor = new JPATableGeneratorDescriptor();
        
        descriptor.setAllocationSize(tableGenerator.allocationSize());
        descriptor.setInitialValue(tableGenerator.initialValue());
        descriptor.setName(tableGenerator.name());
        descriptor.setPkColumnName(tableGenerator.pkColumnName());
        descriptor.setPkColumnValue(tableGenerator.pkColumnValue());
        descriptor.setSchema(tableGenerator.schema());
        descriptor.setTable(tableGenerator.table());
        JPAUniqueConstraint[] constraints = extractConstraints(tableGenerator);
        descriptor.setUniqueConstraints(constraints);
        descriptor.setValueColumnName(tableGenerator.valueColumnName());
        
        return descriptor;
    }

    private static JPAUniqueConstraint[] extractConstraints(final TableGenerator tableGenerator) {
        JPAUniqueConstraint[] constraints = new JPAUniqueConstraint[]{};
        int i = 0;
        for (UniqueConstraint uniqueConstraint : tableGenerator.uniqueConstraints()) {
            constraints[i++] = JPAUniqueConstraint.extract(uniqueConstraint);
        }
        return constraints;
    }
    
    
    public String getPkColumnName() {
        return _pkColumnName;
    }
    public void setPkColumnName(final String pkColumnName) {
        this._pkColumnName = pkColumnName;
    }
    public String getPkColumnValue() {
        return _pkColumnValue;
    }
    public void setPkColumnValue(final String pkColumnValue) {
        this._pkColumnValue = pkColumnValue;
    }
    public String getSchema() {
        return _schema;
    }
    public void setSchema(final String schema) {
        this._schema = schema;
    }
    public String getTable() {
        return _table;
    }
    public void setTable(final String table) {
        this._table = table;
    }
    public JPAUniqueConstraint[] getUniqueConstraints() {
        return _uniqueConstraints;
    }
    public void setUniqueConstraints(final JPAUniqueConstraint[] uniqueConstraints) {
        this._uniqueConstraints = uniqueConstraints;
    }
    public String getValueColumnName() {
        return _valueColumnName;
    }
    public void setValueColumnName(final String valueColumnName) {
        this._valueColumnName = valueColumnName;
    }

    public Class<?> getPrimaryKeyType() {
        return _primaryKeyType;
    }

    public void setPrimaryKeyType(final Class<?> primaryKeyType) {
        this._primaryKeyType = primaryKeyType;
    }   
}
