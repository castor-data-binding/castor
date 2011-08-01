/*
 * Copyright 2011 Ralf Joachim
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
 * $Id$
 */
package org.castor.cpa.persistence.sql.query.visitor;

import java.util.Iterator;
import java.util.List;

import org.castor.cpa.persistence.sql.query.Join;
import org.castor.cpa.persistence.sql.query.Qualifier;
import org.castor.cpa.persistence.sql.query.QueryConstants;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Visitor defining special behavior of query building for hsql database.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class HsqlQueryVisitor extends DefaultQueryVisitor {
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void visit(final Join join) {
        _queryString.append(join.getOperator().toString());
        _queryString.append(QueryConstants.SPACE);
        _queryString.append(QueryConstants.JOIN);
        _queryString.append(QueryConstants.SPACE);

        
        addTableNames(join.getJoin());

        Condition condition = join.getCondition();
        if (condition != null) {
            _queryString.append(QueryConstants.SPACE);
            _queryString.append(QueryConstants.ON);
            _queryString.append(QueryConstants.SPACE);
            condition.accept(this);
        }

        handleJoinConstruction(join.getJoin());

        if (LOG.isTraceEnabled()) { 
            LOG.trace("Visit join: " + toString());
        }
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

        for (Iterator<Qualifier> iter = select.getFrom().iterator(); iter.hasNext(); ) {
            Qualifier qualifier = iter.next();

            addTableNames(qualifier);
            
            handleJoinConstruction(qualifier);

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

        handleLock(select);

        if (LOG.isTraceEnabled()) { 
            LOG.trace("Visit select: " + toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void handleJoinConstruction(final Qualifier qualifier) {
        if (qualifier.hasJoin()) {
            for (Join join : qualifier.getJoins()) {
                _queryString.append(QueryConstants.SPACE);
                join.accept(this);
            }
        }
    }

    //-----------------------------------------------------------------------------------
}
