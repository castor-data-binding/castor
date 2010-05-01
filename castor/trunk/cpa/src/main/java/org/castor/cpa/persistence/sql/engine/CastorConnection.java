/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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
 *
 * $Id: SQLStatementDelete.java 8469 2009-12-28 16:47:54Z rjoachim $
 */
package org.castor.cpa.persistence.sql.engine;

import java.sql.Connection;

import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * CastorConnection class holding connection and factory parameters to encapsulate
 * their usage in case of creation of CastorStatements.
 * 
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class CastorConnection {
    //-----------------------------------------------------------------------------------    

    /** Variable to store connection. */
    private Connection _connection;

    /** Variable to store the PersistenceFactory. */
    private final PersistenceFactory _factory;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param connection Instance of the connection to be used to create CastorStatements.
     * @param factory Instance of PersistenceFactory to be used to create CastorStatements.
     */
    public CastorConnection(final Connection connection, final PersistenceFactory factory) {
        _connection = connection;
        _factory = factory;
    }

    /**
     * Method to create new CastorStatement using local instances of
     * Connection & PersistenceFactory.
     * 
     * @return New instance of CastorStatement.
     */
    public CastorStatement createStatement() {
        return new CastorStatement(_factory, _connection);
    }

    //-----------------------------------------------------------------------------------    
}
