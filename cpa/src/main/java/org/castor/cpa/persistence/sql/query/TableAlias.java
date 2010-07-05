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

package org.castor.cpa.persistence.sql.query;

/**
 * Visitor constructing queryString.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TableAlias extends Qualifier {
    //-----------------------------------------------------------------------------------    

    /** Variable holding the table to be aliased. */
    private final Table _table;

    //-----------------------------------------------------------------------------------    

    /**
     * Method constructing a table from a string to be aliased by the second
     * passed string.
     * 
     * @param table String to construct table with.
     * @param name String used to alias the table.
     */
    public TableAlias(final String table, final String name) {
        this(new Table(table), name);
    }

    /**
     * Method constructing a new TableAlias by taking a pre-constructed table and a string
     * to be used as alias for the given table.
     * 
     * @param table Table to be aliased.
     * @param name String to be used to alias table.
     */
    public TableAlias(final Table table, final String name) {
        super(name);
        _table = table;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Get method returning table currently set.
     * 
     * @return Table currently set.
     */
    public Table getTable() {
        return _table;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public void accept (final Visitor visitor) { visitor.visit(this); }

    //-----------------------------------------------------------------------------------    
}

