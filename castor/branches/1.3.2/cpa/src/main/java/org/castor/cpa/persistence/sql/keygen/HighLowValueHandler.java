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

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandler;
import org.exolab.castor.jdo.PersistenceException;

public final class HighLowValueHandler <T> {
    private final String _table;
    private final int _grab;
    private final KeyGeneratorTypeHandler <T> _typeHandler;
    private T _last;
    private T _max;
    private int _values;
    
    public HighLowValueHandler(final String table, final int grab,
            final KeyGeneratorTypeHandler <T> typeHandler) {
        _table = table;
        _grab = grab;
        _typeHandler = typeHandler;
    }

    public void init(final ResultSet rs) throws PersistenceException, SQLException {
        _last = _typeHandler.getValue(rs);
        _max = _typeHandler.add(_last, _grab);
        _values = _grab;
    }
    
    public boolean hasNext() {
        return (_values > 0);
    }
    
    public T next() {
        _last = _typeHandler.increment(_last);
        _values--;
        return _last;
    }
    
    public void bindTable(final PreparedStatement stmt, final int index) throws SQLException {
        stmt.setString(index, _table);
    }
    
    public void bindLast(final PreparedStatement stmt, final int index) throws SQLException {
        _typeHandler.bindValue(stmt, index, _last);
    }
    
    public void bindMax(final PreparedStatement stmt, final int index) throws SQLException {
        _typeHandler.bindValue(stmt, index, _max);
    }
}
