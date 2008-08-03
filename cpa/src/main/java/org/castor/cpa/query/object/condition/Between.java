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

    /** The low of between simple condition. */
    private Expression _low;

    /** The high of between simple condition. */
    private Expression _high;

    /** The not of between simple condition.. */
    private boolean _not;
    //--------------------------------------------------------------------------

    /**
     * Gets the low of between simple condition.
     * 
     * @return the low of between simple condition
     */
    public Expression getLow() {
        return _low;
    }

    /**
     * Sets the low of between simple condition.
     * 
     * @param low the new low of between simple condition
     */
    public void setLow(final Expression low) {
        _low = low;
    }

    /**
     * Gets the high of between simple condition.
     * 
     * @return the high of between simple condition
     */
    public Expression getHigh() {
        return _high;
    }

    /**
     * Sets the high of between simple condition.
     * 
     * @param high the new high of between simple condition
     */
    public void setHigh(final Expression high) {
        _high = high;
    }

    /**
     * Checks if is not.
     * 
     * @return true, if is not
     */
    public boolean isNot() {
        return _not;
    }

    /**
     * Sets the not.
     * 
     * @param not the new not
     */
    public void setNot(final boolean not) {
        _not = not;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        if (this.getExpression() != null) {
            this.getExpression().toString(sb);
        }
        if (_not) {
            sb.append(" NOT");
        }
        if (_low != null && _high != null) {
            sb.append(" BETWEEN ");
            _low.toString(sb);
            sb.append(" AND ");
            _high.toString(sb);
        }
        return sb;
    }

    //--------------------------------------------------------------------------

}
