package org.castor.cpa.persistence.sql.keygen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.exolab.castor.jdo.PersistenceException;

public interface KeyGeneratorTypeHandler < T > {
    T getNextValue(ResultSet rs) throws PersistenceException, SQLException;
    T getValue(ResultSet rs) throws PersistenceException, SQLException;
    T increment(T value);
    T add(T value, int offset);
    void bindValue(PreparedStatement stmt, int index, T value) throws SQLException;
}
