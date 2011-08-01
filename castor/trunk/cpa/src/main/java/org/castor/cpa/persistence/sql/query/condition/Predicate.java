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
 * Abstract base class for all predicates.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class Predicate extends Condition {
    //-----------------------------------------------------------------------------------    

    /** Expression to apply the predicate to. */
    private final Expression _expression;
    
    /** What is the expected result of the evaluation? */
    private boolean _evaluateTo;

    //-----------------------------------------------------------------------------------    

    /**
     * Construct a predicate with given expression.
     * 
     * @param expression Expression to apply the predicate to.
     * @param evaluateTo What is the expected result of the evaluation?
     */
    protected Predicate(final Expression expression, final boolean evaluateTo) {
        if (expression == null) { throw new NullPointerException(); }
        _expression = expression;
        _evaluateTo = evaluateTo;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Expression to apply the predicate to.
     * 
     * @return Expression to apply the predicate to.
     */
    public final Expression expression() {
        return _expression;
    }

    /**
     * Returns if the predicate should evaluate to <code>true</code> or <code>false</code>.
     * 
     * @return <code>true</code> if the predicate should evaluate to <code>true</code>,
     *         <code>false</code> otherwise.
     */
    public final boolean evaluateTo() {
        return _evaluateTo;
    }

    //-----------------------------------------------------------------------------------    

    @Override
    public final Condition not() {
        _evaluateTo = !_evaluateTo;
        return this;
    }

    //-----------------------------------------------------------------------------------    
}
