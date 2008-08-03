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

import org.castor.cpa.query.Expression;

/**
 * Final class that represents comparison simple condition.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Comparison extends SimpleCondition {
    //--------------------------------------------------------------------------

    /** The compare of comparison simple condition. */
    private Expression _compare;

    /** The operator of comparison simple condition. */
    private ComparisonOperator _operator;

    //--------------------------------------------------------------------------

    /**
     * Gets the compare of comparison simple condition.
     * 
     * @return the compare of comparison simple condition
     */
    public Expression getCompare() {
        return _compare;
    }

    /**
     * Sets the compare of comparison simple condition.
     * 
     * @param compare the new compare of comparison simple condition
     */
    public void setCompare(final Expression compare) {
        _compare = compare;
    }

    /**
     * Gets the operator of comparison simple condition.
     * 
     * @return the operator of comparison simple condition
     */
    public ComparisonOperator getOperator() {
        return _operator;
    }

    /**
     * Sets the operator of comparison simple condition.
     * 
     * @param operator the new operator of comparison simple condition
     */
    public void setOperator(final ComparisonOperator operator) {
        _operator = operator;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        if (_operator != null) {
           sb.append('(');
           _operator.getExpression().toString(sb);
           _operator.toString(sb);
        }
        if (_compare != null) {
           _compare.toString(sb);
           sb.append(')');
        }
        return sb;
    }

    //--------------------------------------------------------------------------

}
