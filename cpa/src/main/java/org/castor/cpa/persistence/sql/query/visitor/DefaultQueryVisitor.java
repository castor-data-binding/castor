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

import java.util.Iterator;
import java.util.List;

import org.castor.cpa.persistence.sql.query.Assignment;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.Qualifier;
import org.castor.cpa.persistence.sql.query.QueryConstants;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.Update;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.CompoundCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.condition.IsNullPredicate;
import org.castor.cpa.persistence.sql.query.condition.OrCondition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Expression;
import org.castor.cpa.persistence.sql.query.expression.NextVal;
import org.castor.cpa.persistence.sql.query.expression.Parameter;

/**
 * Visitor constructing queryString.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class DefaultQueryVisitor implements Visitor {
    //-----------------------------------------------------------------------------------    

    /** StringBuilder used to append query string.*/
    private final StringBuilder _queryString = new StringBuilder();

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public void visit(final Assignment assignment) {
        assignment.leftExpression().accept(this);
        _queryString.append(QueryConstants.ASSIGN);
        assignment.rightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Delete delete) {
        Condition condition = delete.getCondition();
        
        _queryString.append(QueryConstants.DELETE);
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.FROM);
        _queryString.append(QueryConstants.SPACE);
        delete.getQualifier().accept(this);

        if (condition != null) {
            _queryString.append(QueryConstants.SPACE);
            _queryString.append(QueryConstants.WHERE);
            _queryString.append(QueryConstants.SPACE);
            condition.accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Insert insert) { 
        _queryString.append(QueryConstants.INSERT);
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.INTO);
        _queryString.append(QueryConstants.SPACE);
        
        insert.getQualifier().accept(this);
        
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.LPAREN);
        
        for (Iterator<Assignment> iter = insert.getAssignment().iterator(); iter.hasNext(); ) {
            iter.next().leftExpression().accept(this);
            if (iter.hasNext()) {
                _queryString.append(QueryConstants.SEPERATOR);
                _queryString.append(QueryConstants.SPACE);
            }
        }

        _queryString.append(QueryConstants.RPAREN); 
        
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.VALUES);

        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.LPAREN);

        for (Iterator<Assignment> iter = insert.getAssignment().iterator(); iter.hasNext(); ) {
            iter.next().rightExpression().accept(this);
            if (iter.hasNext()) {
                _queryString.append(QueryConstants.SEPERATOR);
                _queryString.append(QueryConstants.SPACE);
            }
        }

        _queryString.append(QueryConstants.RPAREN);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Select select) {
        List<Expression> expressions = select.getSelect();
        Condition condition = select.getCondition();
        
        _queryString.append(QueryConstants.SELECT);
        _queryString.append(QueryConstants.SPACE);
        
        if (expressions.isEmpty()) {
            _queryString.append(QueryConstants.STAR);
        } else {
            for (Iterator<Expression> iter = expressions.iterator(); iter.hasNext(); ) {
                iter.next().accept(this);
                if (iter.hasNext()) {
                    _queryString.append(QueryConstants.SEPERATOR);
                    _queryString.append(QueryConstants.SPACE);
                }
            }
        }
        
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.FROM);
        _queryString.append(QueryConstants.SPACE);

        select.getQualifier().accept(this);
        
        if (condition != null) {
            _queryString.append(QueryConstants.SPACE);
            _queryString.append(QueryConstants.WHERE);
            _queryString.append(QueryConstants.SPACE);
            condition.accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Table table) {
        _queryString.append(quoteName(table.name()));
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Update update) {
        Condition condition = update.getCondition();
        
        _queryString.append(QueryConstants.UPDATE);
        _queryString.append(QueryConstants.SPACE);
        update.getQualifier().accept(this);
        
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.SET);
        _queryString.append(QueryConstants.SPACE);

        for (Iterator<Assignment> iter = update.getAssignment().iterator(); iter.hasNext(); ) {
            iter.next().accept(this);
            if (iter.hasNext()) {
                _queryString.append(QueryConstants.SEPERATOR);
                _queryString.append(QueryConstants.SPACE);
            }
        }
        
        if (condition != null) {
            _queryString.append(QueryConstants.SPACE);
            _queryString.append(QueryConstants.WHERE);
            _queryString.append(QueryConstants.SPACE);
            condition.accept(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final AndCondition andCondition) {
        Condition condition = null;
        for (Iterator<Condition> iter = andCondition.iterator(); iter.hasNext(); ) {
            condition = iter.next();
            if (condition instanceof CompoundCondition) {
                _queryString.append(QueryConstants.LPAREN);
                condition.accept(this);
                _queryString.append(QueryConstants.RPAREN);
            } else {
                condition.accept(this);
            }
            if (iter.hasNext()) {
                _queryString.append(QueryConstants.SPACE);
                _queryString.append(QueryConstants.AND);
                _queryString.append(QueryConstants.SPACE);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Compare compare) {
        compare.leftExpression().accept(this);
        _queryString.append(compare.operator().toString());
        compare.rightExpression().accept(this);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final IsNullPredicate isNullPredicate) {
        isNullPredicate.expression().accept(this);
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.IS);
        if (!isNullPredicate.evaluateTo()) {
            _queryString.append(QueryConstants.SPACE);
            _queryString.append(QueryConstants.NOT);
        }
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.NULL);
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final OrCondition orCondition) {
        Condition condition = null;
        for (Iterator<Condition> iter = orCondition.iterator(); iter.hasNext(); ) {
            condition = iter.next();
            if (condition instanceof CompoundCondition) {
                _queryString.append(QueryConstants.LPAREN);
                condition.accept(this);
                _queryString.append(QueryConstants.RPAREN);
            } else {
                condition.accept(this);
            }
            if (iter.hasNext()) {
                _queryString.append(QueryConstants.SPACE);
                _queryString.append(QueryConstants.OR);
                _queryString.append(QueryConstants.SPACE);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Column column) {
        Qualifier qualifier = column.qualifier();
        if (qualifier != null) {
            qualifier.accept(this);
            _queryString.append(QueryConstants.DOT);
        }
        _queryString.append(quoteName(column.name()));
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final NextVal nextVal) {
        _queryString.append(getSequenceNextValString(nextVal.getSeqName()));
    }

    /**
     * {@inheritDoc}
     */
    public void visit(final Parameter parameter) {
        _queryString.append(QueryConstants.PARAMETER);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning prepared string to match requirements of specific databases
     * if needed.
     * 
     * @param name String to be prepared.
     * @return Prepared string.
     */
    protected String quoteName(final String name) {
        return name;
    }

    /**
     * Returns the database engine specific string to fetch sequence next value.
     * 
     * @param seqName Name of the sequence.
     * @return String to fetch sequence next value.
     */
    protected String getSequenceNextValString(final String seqName) {
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    public String toString() {
        return _queryString.toString();
    }

    //-----------------------------------------------------------------------------------    
}
