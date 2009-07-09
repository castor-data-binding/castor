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

import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Predicate that checks expressions for being null or not null.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class IsNullPredicate extends Predicate {
    //-----------------------------------------------------------------------------------
    
    /** 'IS NULL' predicate string. */
    public static final String PREDICATE = " IS NULL";
    
    /** 'IS NOT NULL' predicate string. */
    public static final String INVERS_PREDICATE = " IS NOT NULL";
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a predicate that checks the given expression for being null.
     * 
     * @param expression Expession to check for null.
     */
    public IsNullPredicate(final Expression expression) {
        super(expression);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * {@inheritDoc}
     */
    public void toString(final StringBuilder sb) {
        expression().toString(sb);
        if (evaluateTo()) {
            sb.append(INVERS_PREDICATE);
        } else {
            sb.append(PREDICATE);
        }
    }

    //-----------------------------------------------------------------------------------    
}

