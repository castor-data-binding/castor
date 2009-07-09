/*
 * Copyright 2009 Ralf Joachim, Ahmad Hassan
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
package org.castor.cpa.persistence.sql.query.condition;

import org.castor.cpa.persistence.sql.query.QueryObject;

/**
 * Abstract base class for all conditions.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public abstract class Condition extends QueryObject {    
    //-----------------------------------------------------------------------------------    

    /**
     * Builder method to concatenate the given condition with all others that have
     * previously been added with an AND operator.
     * 
     * @param condition Condition to concatenate. 
     * @return Resulting condition object. 
     */
    public Condition and(final Condition condition) {
        if (condition instanceof AndCondition) {
            ((AndCondition) condition).insert(this);
            return condition;
        } 
        return new AndCondition(this, condition);        
    }
    
    /**
     * Builder method to concatenate the given condition with all others that have
     * previously been added with an OR operator.
     * 
     * @param condition Condition to concatenate. 
     * @return Resulting condition object. 
     */
    public Condition or(final Condition condition) {
        if (condition instanceof OrCondition) {
            ((OrCondition) condition).insert(this);
            return condition;
        }
        return new OrCondition(this, condition);
    }

    /**
     * Builder method to invert a condition. A condition that evaluated to TRUE before
     * calling this method will evaluate to FALSE thereafter.
     * 
     * @return Negated condition.
     */
    public abstract Condition not();

    //-----------------------------------------------------------------------------------    
}