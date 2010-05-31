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

/** 
 * Interface for various type handlers. The type handler handles
 * integer, BigDecimal, Long and String values depending on the database engine.
 *
 * @param <T> Name of KeyGeneratorTypeHandler interface.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public interface KeyGeneratorTypeHandler <T> {
    //-----------------------------------------------------------------------------------    

    /**
     * Gets the value from resultset by calling getValue method and then 
     * calls the increment method to increment the extracted value.
     * 
     * @param rs A ResultSet object.
     * @return Returns the new value after incrementing it.
     * @throws PersistenceException If ResultSet is empty or if the type handler 
     * should fail when current row of the record set is not valid,
     * @throws SQLException If database error occurs.
     */
    T getNextValue(ResultSet rs) throws PersistenceException, SQLException;
    
    /**
     * Reads the resultset and return the extracted typehandler value from the
     * resultset.
     * 
     * @param rs ResultSet object
     * @return Value extracted from the ResultSet.
     * @throws PersistenceException If ResultSet is empty or if the type handler 
     * should fail when current row of the record set is not valid,
     * @throws SQLException If database error occurs.
     */
    T getValue(ResultSet rs) throws PersistenceException, SQLException;
    
    /** 
     * Increments the provided value by ONE. 
     *  
     * @param value value to be incremented.
     * @return Modified TypeHandler object with incremented value..
     */
    T increment(T value);
    
    /** 
     * Adds the new Type Handler of type T to the provided handler instance at
     * the provided offset.
     * 
     * @param value Handler instance in which new value will be added
     * @param offset Offset location.     * 
     * @return Modified object.
     */
    T add(T value, int offset);
    
    /**
     * Binds the value in the sql preparedstatement at the provided index
     * location.
     * 
     * @param stmt A SQL PreparedStatement.
     * @param index Index location for binding parameter to statement.
     * @param value Value to be binded
     * @throws SQLException If SQL error occurs in binding param to sql statement.
     */
    void bindValue(PreparedStatement stmt, int index, T value) throws SQLException;

    //-----------------------------------------------------------------------------------    
}