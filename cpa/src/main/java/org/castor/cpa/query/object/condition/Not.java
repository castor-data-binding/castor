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

/**
 * Final class that represents not condition.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Not  extends AbstractCondition {
    //--------------------------------------------------------------------------

    /** The condition of not condition. */
    private Condition _condition;

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public Condition not() {
        return _condition;
    }

    //--------------------------------------------------------------------------

    /**
     * Get condition of not condition.
     * 
     * @return Condition of not condition.
     */
    public Condition getCondition() {
        return _condition;
    }

    /**
     * Set condition of not condition.
     * 
     * @param condition Condition of not condition.
     */
    public void setCondition(final Condition condition) {
        _condition = condition;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append("NOT ");
        if (_condition instanceof CompoundCondition) {
            sb.append('(');
            _condition.toString(sb);
            sb.append(')');
        } else if (_condition != null) {
            _condition.toString(sb);
        }  
        return sb;
    }

    //--------------------------------------------------------------------------
}
