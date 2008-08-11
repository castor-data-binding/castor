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
 * Final class that represents between simple condition.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Between extends SimpleCondition {
    //--------------------------------------------------------------------------

    /** Low bound of between range. */
    private Expression _low;

    /** High bound of between range. */
    private Expression _high;

    //--------------------------------------------------------------------------

    /**
     * Get low bound of between range.
     * 
     * @return Low bound of between range.
     */
    public Expression getLow() {
        return _low;
    }

    /**
     * Set low bound of between range.
     * 
     * @param low Low bound of between range.
     */
    public void setLow(final Expression low) {
        _low = low;
    }

    /**
     * Get high bound of between range.
     * 
     * @return High bound of between range.
     */
    public Expression getHigh() {
        return _high;
    }

    /**
     * Set high bound of between range.
     * 
     * @param high High bound of between range.
     */
    public void setHigh(final Expression high) {
        _high = high;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append('(');
        if (getExpression() != null) {
            getExpression().toString(sb);
        }
        if (isNot()) {
            sb.append(" NOT");
        }
        sb.append(" BETWEEN ");
        if (_low != null) {
            _low.toString(sb);
        }
        sb.append(" AND ");
        if (_high != null) {
            _high.toString(sb);
        }
        sb.append(')');
        return sb;
    }

    //--------------------------------------------------------------------------
}
