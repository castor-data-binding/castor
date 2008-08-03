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
package org.castor.cpa.query.object;

import org.castor.cpa.query.Condition;
import org.castor.cpa.query.Field;
import org.castor.cpa.query.InCondition;
import org.castor.cpa.query.object.condition.In;
import org.castor.cpa.query.object.condition.Null;
import org.castor.cpa.query.object.expression.AbstractExpression;

/**
 * Abstract base class for Field and Schema.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public abstract class AbstractField extends AbstractExpression implements Field {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Field field(final String name) {
        return new FieldImpl(this, name);
    }
    
    /**
     * {@inheritDoc}
     */
    public final InCondition in() {
        In cond = new In();
        cond.setExpression(this);
        cond.setNot(false);
        return cond;
    }

    /**
     * {@inheritDoc}
     */
    public final InCondition notIn() {
        In cond = new In();
        cond.setExpression(this);
        cond.setNot(true);
        return cond;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition isNull() {
        Null cond = new Null();
        cond.setExpression(this);
        cond.setNot(false);
        return cond;
    }
    
    /**
     * {@inheritDoc}
     */
    public final Condition isNotNull() {
        Null cond = new Null();
        cond.setExpression(this);
        cond.setNot(true);
        return cond;
    }

    //--------------------------------------------------------------------------
}
