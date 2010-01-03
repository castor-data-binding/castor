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
package org.castor.ddlgen.typeinfo;

import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.schemaobject.Field;

/**
 * Interface associates JDBC to SQL type and its parameters.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public interface TypeInfo {
    //--------------------------------------------------------------------------

    /**
     * Get JDBC type.
     * 
     * @return The JDBC type.
     */
    String getJdbcType();
    
    /**
     * Get SQL type.
     * 
     * @return The SQL type.
     */
    String getSqlType();
    
    //--------------------------------------------------------------------------

    /**
     * Merge 2 TypeInfo's.
     * 
     * @param type type infor
     * @throws GeneratorException exception
     */
    void merge(TypeInfo type) throws GeneratorException;

    //--------------------------------------------------------------------------

    /**
     * Build DDL string with SQL type and parameters.
     * 
     * @param field The field to get specific parameters from.
     * @return Type string for DDL.
     * @throws GeneratorException If required parameters is not defined.
     */
    String toDDL(Field field) throws GeneratorException;

    //--------------------------------------------------------------------------
}
