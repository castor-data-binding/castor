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
package org.castor.jdo.jpa.info;

import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

public class JPATableGeneratorDescriptor extends JPAKeyGeneratorDescriptor {

	private String pkColumnName;
	private String pkColumnValue;
	private String schema;
	private String table;
	private JPAUniqueConstraint[] uniqueConstraints;
	private String valueColumnName;
	private Class<?> primaryKeyType;
	
	public static JPATableGeneratorDescriptor extract(TableGenerator tableGenerator) {
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

	private static JPAUniqueConstraint[] extractConstraints(
			TableGenerator tableGenerator) {
		JPAUniqueConstraint[] constraints = new JPAUniqueConstraint[]{};
		int i = 0;
		for (UniqueConstraint uniqueConstraint : tableGenerator.uniqueConstraints()) {
			constraints[i++] = JPAUniqueConstraint.extract(uniqueConstraint);
		}
		return constraints;
	}
	
	
	public String getPkColumnName() {
		return pkColumnName;
	}
	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}
	public String getPkColumnValue() {
		return pkColumnValue;
	}
	public void setPkColumnValue(String pkColumnValue) {
		this.pkColumnValue = pkColumnValue;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public JPAUniqueConstraint[] getUniqueConstraints() {
		return uniqueConstraints;
	}
	public void setUniqueConstraints(JPAUniqueConstraint[] uniqueConstraints) {
		this.uniqueConstraints = uniqueConstraints;
	}
	public String getValueColumnName() {
		return valueColumnName;
	}
	public void setValueColumnName(String valueColumnName) {
		this.valueColumnName = valueColumnName;
	}

    public Class<?> getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(Class<?> primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }	
}
