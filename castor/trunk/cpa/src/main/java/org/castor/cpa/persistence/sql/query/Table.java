/*
 * Copyright 2009 Ralf Joachim, Ahmad Hassan
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
package org.castor.cpa.persistence.sql.query;

/**
 * Class representing a table of the database.
 * <br/>
 * As some database engines require quoting of table names we may have to overwrite this
 * class with one that implements the appropriate quoting. Therefore instances of this
 * class have to be constructed through a factory which is specific to the database
 * engine.  
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class Table extends Qualifier {
    //-----------------------------------------------------------------------------------    
    
    /**
     * Construct a table with given name.
     * 
     * @param name Name of the table.
     */
    public Table(final String name) {
        super(name);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public void toString(final StringBuilder sb) {
        sb.append(name());
    }
    
    //-----------------------------------------------------------------------------------    
}

