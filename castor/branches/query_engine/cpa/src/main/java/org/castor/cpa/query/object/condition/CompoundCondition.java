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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.castor.cpa.query.Condition;

/**
 * Abstract base class compound condition.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public abstract class CompoundCondition extends AbstractCondition {
    //--------------------------------------------------------------------------

    /** List of conditions. */
    private List < Condition > _conditions = new ArrayList < Condition > ();
  
    //--------------------------------------------------------------------------

    /**
     * Get operator of the compound condition.
     * 
     * @return Operator of the compound condition.
     */
    protected abstract String getOperator();
    
    /**
     * {@inheritDoc}
     */
    public final Condition not() {
        Not n = new Not();
        n.setCondition(this);
        return n;
    }

    //--------------------------------------------------------------------------

    /**
     * Add condition to the end of the list.
     * 
     * @param condition Condition to add to end of list.
     */
    public final void addCondition(final Condition condition) {
        if (condition == null) { throw new NullPointerException(); }
        _conditions.add(condition);
    }

    /**
     * Add all conditions to the end of the list.
     * 
     * @param conditions List of condition to add to end of list.
     */
    public final void addAllConditions(final List < Condition > conditions) {
        if (conditions.contains(null)) { throw new NullPointerException(); }
        _conditions.addAll(conditions);
    }

    /**
     * Get list of conditions.
     * 
     * @return List of conditions.
     */
    public final List < Condition > getConditions() {
        return _conditions;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */ 
    public final StringBuilder toString(final StringBuilder sb) {
        for (Iterator < Condition > iter = _conditions.iterator(); iter.hasNext(); ) {
            Condition condition = iter.next();
            if (condition instanceof CompoundCondition) {
                sb.append('(');
                condition.toString(sb);
                sb.append(')');
            } else if (condition != null) {
                condition.toString(sb);
            }
            if (iter.hasNext()) { sb.append(getOperator()); }
        }
        return sb;
    }

    //--------------------------------------------------------------------------
}
