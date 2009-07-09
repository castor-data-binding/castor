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

import java.util.Iterator;

/**
 * Represents a list of conditions that are concatenated by an AND operator.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class AndCondition extends CompoundCondition {
    //-----------------------------------------------------------------------------------
    
    /** 'AND' operator string. */
    public static final String AND = " AND ";
    
    //-----------------------------------------------------------------------------------    
    
    /**
     * Default constructor for an empty AND condition. 
     */
    public AndCondition () {
        super();
    }
    
    /**
     * Constructor for a compound condition that concatenates the 2 given conditions with
     * an AND operator. 
     *  
     * @param condition1 First condition. 
     * @param condition2 Second condition. 
     */
    public AndCondition(final Condition condition1, final Condition condition2) {
        this();
        
        append(condition1);
        append(condition2);
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public Condition and(final Condition condition) {
        append(condition);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Condition not() {
        Condition condition = new OrCondition();
        for (Iterator<Condition> iter = iterator(); iter.hasNext(); ) {
            condition.or(iter.next().not());
        }
        return condition;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public void toString(final StringBuilder sb) {
        for (Iterator<Condition> iter = iterator(); iter.hasNext(); ) {
            Condition condition = iter.next();
            if (condition instanceof CompoundCondition) {
                sb.append('(');
                condition.toString(sb);
                sb.append(')');
            } else {
                condition.toString(sb);
            }
            if (iter.hasNext()) { sb.append(AND); }
        }
    }
    
    //-----------------------------------------------------------------------------------    
}
