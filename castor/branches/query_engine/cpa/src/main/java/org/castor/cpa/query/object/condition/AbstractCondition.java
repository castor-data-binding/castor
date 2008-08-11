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
import org.castor.cpa.query.object.AbstractQueryObject;

/**
 * Abstract base class for Conditions.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public abstract class AbstractCondition extends AbstractQueryObject implements Condition {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Condition and(final Condition condition) {
        And and = new And();
        and.addCondition(this);
        if (condition instanceof And) {
            and.addAllConditions(((And) condition).getConditions());
        } else {
            and.addCondition(condition);
        }
        return and;
    }

    /**
     * {@inheritDoc}
     */
    public Condition or(final Condition condition) {
        Or or = new Or();
        or.addCondition(this);
        if (condition instanceof Or) {
            or.addAllConditions(((Or) condition).getConditions());
        } else {
            or.addCondition(condition);
        }
        return or;
    }

    //--------------------------------------------------------------------------
}
