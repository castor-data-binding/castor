/*
 * Copyright 2006 Ralf Joachim
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

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.TypeConvertor;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-06 14:55:28 -0700 (Tue, 06 Dec 2005) $
 */
public interface JDOFieldDescriptor extends FieldDescriptor {
    /**
     * Returns the convertor from the field type to an external type.
     *
     * @return Convertor from field type
     */
    TypeConvertor getConvertor();

    /**
     * Returns the convertor parameter.
     *
     * @return Convertor parameter
     */
    String getConvertorParam();

    /**
     * Returns the SQL name of this field.
     *
     * @return The SQL name of this field
     */
    String[] getSQLName();

    /**
     * Returns the SQL type of this field.
     *
     * @return The SQL type of this field
     */
    int[] getSQLType();

    String getManyTable();

    String[] getManyKey();

    /**
     * Returns true if dirty checking is required for this field.
     *
     * @return True if dirty checking required
     */
    boolean isDirtyCheck();

    boolean isReadonly();
}