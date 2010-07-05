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

package org.castor.cpa.persistence.sql.query.visitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.castor.cpa.persistence.sql.query.Assignment;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.Join;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.TableAlias;
import org.castor.cpa.persistence.sql.query.Update;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.IsNullPredicate;
import org.castor.cpa.persistence.sql.query.condition.OrCondition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Expression;
import org.castor.cpa.persistence.sql.query.expression.NextVal;
import org.castor.cpa.persistence.sql.query.expression.Parameter;

/**
 * Visitor constructing result column map for queries to uncouple the order of
 * select-columns from the order of resultset-columns.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class UncoupleVisitor implements Visitor {
    //-----------------------------------------------------------------------------------    

    /** Map of parameter names to indices filled during build of SQL query string. */
    private final Map<String, Integer> _resultColumnMap = new HashMap<String, Integer>();

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public void visit(final Assignment assignment) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Delete delete) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Insert insert) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Join join) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Select select) {
        for (Iterator<Expression> iter = select.getSelect().iterator(); iter.hasNext(); ) {
            iter.next().accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Table table) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final TableAlias tableAlias) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Update update) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final AndCondition andCondition) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Compare compare) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final IsNullPredicate isNullPredicate) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final OrCondition orCondition) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Column column) {
        _resultColumnMap.put(column.toString(), _resultColumnMap.size() + 1);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final NextVal nextVal) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Parameter parameter) { }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning constructed result columns map.
     * 
     * @return Constructed result columns map.
     */
    public Map<String, Integer> getResultColumnMap() {
        return _resultColumnMap;
    }

    //-----------------------------------------------------------------------------------    
}
