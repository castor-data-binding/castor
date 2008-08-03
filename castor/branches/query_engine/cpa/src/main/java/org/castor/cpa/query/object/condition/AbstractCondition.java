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
import java.util.List;

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
public abstract class AbstractCondition 
extends AbstractQueryObject implements Condition {

    /**
     * {@inheritDoc}
     */
    public final Condition and(final Condition condition) {
        And a = new And();
        if (a.getConditions() == null) {
           List < Condition > con = new ArrayList < Condition > ();
           a.setConditions(con); 
        }
        a.setParent(this);
        a.setCondition(condition);
        //a.getConditions().add(this);
        return a;
    }

    /**
     * {@inheritDoc}
     */
    //TODO implementation
    public final Condition not() {
        Not n = new Not();
        n.setCondition(this);
        return n;
    }

    /**
     * {@inheritDoc}
     */
    public final Condition or(final Condition condition) {
        Or o = new Or();
        if (o.getConditions() == null) {
            List < Condition > con = new ArrayList < Condition > ();
            o.setConditions(con); 
         }
         o.setParent(this);
         o.setCondition(condition);
        return o;
    }

}
