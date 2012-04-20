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
package org.castor.cpa.persistence.sql.query.expression;

import org.castor.cpa.persistence.sql.query.QueryObject;
import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.CompareOperator;
import org.castor.cpa.persistence.sql.query.condition.IsNullPredicate;
import org.castor.cpa.persistence.sql.query.condition.Predicate;

/**
 * Abstract base class for all expressions. 
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class Expression implements QueryObject {
    //-----------------------------------------------------------------------------------    
    
    /**
     * Compare if this expression is equal to the given one.
     * 
     * @param expression Right hand side of the compare.
     * @return Compare condition.
     */
    public final Compare equal(final Expression expression) {
        return new Compare(this, CompareOperator.EQ, expression);
    }

    /**
     * Compare if this expression is not equal to the given one.
     * 
     * @param expression Right hand side of the compare.
     * @return Compare condition.
     */
    public final Compare notEqual(final Expression expression) {
        return new Compare(this, CompareOperator.NE, expression);
    }

    /**
     * Compare if this expression is greater than the given one.
     * 
     * @param expression Right hand side of the compare.
     * @return Compare condition.
     */
    public final Compare greaterThan(final Expression expression) {
        return new Compare(this, CompareOperator.GT, expression);
    }

    /**
     * Compare if this expression is greater than or equal to the given one.
     * 
     * @param expression Right hand side of the compare.
     * @return Compare condition.
     */
    public final Compare greaterEqual(final Expression expression) {
        return new Compare(this, CompareOperator.GE, expression);
    }

    /**
     * Compare if this expression is less than or equal to the given one.
     * 
     * @param expression Right hand side of the compare.
     * @return Compare condition.
     */
    public final Compare lessEqual(final Expression expression) {
        return new Compare(this, CompareOperator.LE, expression);
    }

    /**
     * Compare if this expression is less than the given one.
     * 
     * @param expression Right hand side of the compare.
     * @return Compare condition.
     */
    public final Compare lessThan(final Expression expression) {
        return new Compare(this, CompareOperator.LT, expression);
    }

    /**
     * Check if this expression is null.
     * 
     * @return Compare condition.
     */
    public final Predicate isNull() {
        return new IsNullPredicate(this, true);
    }

    /**
     * Check if this expression is not null.
     * 
     * @return Compare condition.
     */
    public final Predicate isNotNull() {
        return new IsNullPredicate(this, false);
    }

    //-----------------------------------------------------------------------------------    
}
