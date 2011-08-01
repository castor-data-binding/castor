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
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.condition.IsNullPredicate;
import org.castor.cpa.persistence.sql.query.condition.OrCondition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.NextVal;
import org.castor.cpa.persistence.sql.query.expression.Parameter;

/**
 * Visitor constructing parameter map for queries.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class ParameterVisitor implements Visitor {
    //-----------------------------------------------------------------------------------    

    /** Map of parameter names to indices filled during build of SQL query string. */
    private final Map<String, Integer> _parameters = new HashMap<String, Integer>();

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public void visit(final Assignment assignment) {
        assignment.rightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Delete delete) {
        if (delete.getCondition() == null) { return; }
        delete.getCondition().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Insert insert) { 
        for (Iterator<Assignment> iter = insert.getAssignment().iterator(); iter.hasNext(); ) {
            iter.next().accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Join join) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Select select) {
        if (select.getCondition() == null) { return; }
        select.getCondition().accept(this);
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
    public void visit(final Update update) {
        for (Iterator<Assignment> iter = update.getAssignment().iterator(); iter.hasNext(); ) {
            iter.next().accept(this);
        }
        if (update.getCondition() == null) { return; }
        update.getCondition().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final AndCondition andCondition) {
        for (Iterator<Condition> iter = andCondition.iterator(); iter.hasNext(); ) {
            iter.next().accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Compare compare) {
        compare.leftExpression().accept(this);
        compare.rightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final IsNullPredicate isNullPredicate) {
        isNullPredicate.expression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final OrCondition orCondition) {
        for (Iterator<Condition> iter = orCondition.iterator(); iter.hasNext(); ) {
            iter.next().accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Column column) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final NextVal nextVal) { }

    /**
     * {@inheritDoc}
     */
    public void visit(final Parameter parameter) {
        _parameters.put(parameter.name(), Integer.valueOf(_parameters.size() + 1));
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning constructed parameter map.
     * 
     * @return Constructed parameter map.
     */
    public Map<String, Integer> getParameters() {
        return _parameters;
    }

    //-----------------------------------------------------------------------------------    
}
