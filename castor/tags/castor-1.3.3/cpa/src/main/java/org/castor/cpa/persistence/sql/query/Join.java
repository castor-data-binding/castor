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
 * $Id$
 */
package org.castor.cpa.persistence.sql.query;

import org.castor.cpa.persistence.sql.query.condition.Condition;

/**
 * Class holding join informations.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class Join implements QueryObject {
    //------------------------------------------------------------------------

    /** Variable storing the to be used for this join. */
    private final JoinOperator _operator;

    /** Variable storing the right side of the join. */
    private final Qualifier _join;

    /** Variable storing the condition for the join. */
    private final Condition _condition;

    //------------------------------------------------------------------------

    /**
     * Constructor taking two parameters, constructing JoinOperator instance from string
     * and delegating call to the default constructor.
     * 
     * @param oper String to be used to get the correct JoinOperator.
     * @param join Qualifier to be used as right side of the join.
     */
    public Join(final JoinOperator oper, final Qualifier join) {
        this(oper, join, null);
    }

    /** 
     * Default constructor copying passed values to class variables.
     * 
     * @param oper Passed JoinOperator to be set.
     * @param join Qualifier to be used as right side of the join.
     * @param cond Condition to be used for the join.
     */
    public Join(final JoinOperator oper, final Qualifier join, final Condition cond) {
        _operator = oper;
        _join = join;
        _condition = cond;
    }

    //------------------------------------------------------------------------

    /**
     * Method returning operator currently set.
     * 
     * @return Operator currently set.
     */
    public JoinOperator getOperator() {
        return _operator;
    }

    /**
     * Method returning qualifier currently set.
     * 
     * @return Qualifier currently set.
     */
    public Qualifier getJoin() {
        return _join;
    }

    /**
     * Method returning condition currently set.
     * 
     * @return Condition currently set.
     */
    public Condition getCondition() {
        return _condition;
    }

    //------------------------------------------------------------------------

    /** 
     * Method constructing query string.
     * 
     * @return Constructed query string.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(_operator.toString());
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.JOIN);
        sb.append(QueryConstants.SPACE);

        if (!_join.hasJoin()) {
            if (_join instanceof TableAlias) {
                sb.append(((TableAlias) _join).getTable().toString());
                sb.append(QueryConstants.SPACE);
            }
            sb.append(_join.toString());
        } else {
            // Open all necessary parentheses before starting any joins.
            for (int i = 0; i < _join.getJoins().size(); i++) {
                sb.append(QueryConstants.LPAREN);
            }
            for (int i = 0; i < _join.getJoins().size(); i++) {
                Join join = _join.getJoins().get(i);

                if (i == 0) {
                    if (_join instanceof TableAlias) {
                        sb.append(((TableAlias) _join).getTable().toString());
                        sb.append(QueryConstants.SPACE);
                    }
                    sb.append(_join.toString());
                }

                sb.append(QueryConstants.SPACE);
                sb.append(join.toString());

                // Close opened parentheses after every JOIN-expression in the query.
                sb.append(QueryConstants.RPAREN);
            }
        }

        if (_condition != null) {
            sb.append(QueryConstants.SPACE);
            sb.append(QueryConstants.ON);
            sb.append(QueryConstants.SPACE);
            sb.append(_condition.toString());
        }

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public void accept(final Visitor visitor) { visitor.visit(this); }

    //------------------------------------------------------------------------
}

