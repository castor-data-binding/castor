/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.object.condition;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Expression;

/**
 * Final class that represents comparison simple condition.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Comparison extends AbstractCondition {
    //--------------------------------------------------------------------------
    
    /** Equal comparison operator. */
    public static final ComparisonOperator EQUAL = new Equal();

    /** Not equal comparison operator. */
    public static final ComparisonOperator NOT_EQUAL = new NotEqual();

    /** Less than comparison operator. */
    public static final ComparisonOperator LESS_THAN = new LessThan();
    
    /** Less equal comparison operator. */
    public static final ComparisonOperator LESS_EQUAL = new LessEqual();
    
    /** Greater equal comparison operator. */
    public static final ComparisonOperator GREATER_EQUAL = new GreaterEqual();
    
    /** Greater than comparison operator. */
    public static final ComparisonOperator GREATER_THAN = new GreaterThan();
    
    //--------------------------------------------------------------------------

    /** Comarison operator of comparison condition. */
    private ComparisonOperator _operator;

    /** Left side expression of comparison condition. */
    private Expression _leftSide;

    /** Right side expression of comparison condition. */
    private Expression _rightSide;

    //--------------------------------------------------------------------------
    
    /**
     * Construct new comparison.
     * 
     * @param operator Comparison operator for the comparison.
     */
    public Comparison(final ComparisonOperator operator) {
        if (operator == null) { throw new NullPointerException(); }
        _operator = operator;
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public Condition not() {
        _operator = _operator.not();
        return this;
    }

    //--------------------------------------------------------------------------

    /**
     * Get comarison operator of comparison condition.
     * 
     * @return Comarison operator of comparison condition.
     */
    public ComparisonOperator getOperator() {
        return _operator;
    }

    /**
     * Get left side expression of comparison condition.
     * 
     * @return Left side expression of comparison condition.
     */
    public Expression getLeftSide() {
        return _leftSide;
    }

    /**
     * Set left side expression of comparison condition.
     * 
     * @param expression Left side expression of comparison condition.
     */
    public void setLeftSide(final Expression expression) {
        _leftSide = expression;
    }

    /**
     * Get right side expression of comparison condition.
     * 
     * @return Right side expression of comparison condition.
     */
    public Expression getRightSide() {
        return _rightSide;
    }

    /**
     * Set right side expression of comparison condition.
     * 
     * @param expression Right side expression of comparison condition.
     */
    public void setRightSide(final Expression expression) {
        _rightSide = expression;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append('(');
        if (_leftSide != null) {
            _leftSide.toString(sb);
        }
        sb.append(_operator.getOperator());
        if (_rightSide != null) {
            _rightSide.toString(sb);
        }
        sb.append(')');
        return sb;
    }

    //--------------------------------------------------------------------------
}
