/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.jdo.engine;

/**
 * Holds information about table names and counters.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2005-07-24 14:37:16 -0600 (Sun, 24 Jul 2005) $
 * @since 0.9.9
 */
public final class CounterRef {
    //--------------------------------------------------------------------------

    /** The table name. */
    private String  _tableName;
    
    /** The counter value. */
    private int     _counter;
    
    //--------------------------------------------------------------------------

    /**
     * Get the table name.
     * 
     * @return the table name.
     */
    public String getTableName() {
        return _tableName;
    }
    
    /**
     * Set the tabel name.
     * 
     * @param tableName The tabel name
     */
    public void setTableName(final String tableName) {
        _tableName = tableName;
    }
    
    /**
     * Get the counter value.
     * 
     * @return The counter value.
     */
    public int getCounter() {
        return _counter;
    }
    
    /**
     * Set the counter value.
     * 
     * @param counter The counter value.
     */
    public void setCounter(final int counter) {
        _counter = counter;
    }

    //--------------------------------------------------------------------------
}
