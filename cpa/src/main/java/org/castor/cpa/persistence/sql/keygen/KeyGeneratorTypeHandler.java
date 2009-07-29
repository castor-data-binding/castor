/*
 * Copyright 2009 Ralf Joachim
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
package org.castor.cpa.persistence.sql.keygen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.exolab.castor.jdo.PersistenceException;

public interface KeyGeneratorTypeHandler <T> {
    T getNextValue(ResultSet rs) throws PersistenceException, SQLException;
    T getValue(ResultSet rs) throws PersistenceException, SQLException;
    T increment(T value);
    T add(T value, int offset);
    void bindValue(PreparedStatement stmt, int index, T value) throws SQLException;
}
