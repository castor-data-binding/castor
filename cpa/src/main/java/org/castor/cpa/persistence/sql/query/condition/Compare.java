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

import org.castor.cpa.persistence.sql.query.QueryContext;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Compare 2 conditions with an operator.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class Compare extends Condition {
    //-----------------------------------------------------------------------------------

    /** Left operand of the comparison. */
    private final Expression _left;
    
    /** Operator of comparison. */
    private CompareOperator _operator;
    
    /** Right operand of the comparison. */
    private final Expression _right;
    
    //-----------------------------------------------------------------------------------

    /**
     * Constructor a compare condition that compares given left and rigth hand operand
     * with given comparison operator.
     *  
     * @param left Left operand of the comparison.
     * @param operator Operator of comparison.
     * @param right Right operand of the comparison. 
     */
    public Compare(final Expression left, final CompareOperator operator, final Expression right) {
        if ((left == null) || (operator == null) || (right == null)) {
            throw new NullPointerException();
         }
        
        _left = left;
        _operator = operator;
        _right = right;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Returns left operand of the comparison.
     * 
     * @return Left operand of the comparison.
     */
    public Expression leftExpression() {
        return _left;
    }
    
    /**
     * Returns operator of comparison.
     * 
     * @return Operator of comparison.
     */
    public CompareOperator operator() {
        return _operator;
    }

    /**
     * Returns right operand of the comparison.
     * 
     * @return right operand of the comparison.
     */
    public Expression rightExpression() {
        return _right;
    }
    
    //-----------------------------------------------------------------------------------

    @Override
    public Condition not() {
        _operator = _operator.inverse();
        return this;
    }

    //-----------------------------------------------------------------------------------    

    @Override
    public void toString(final QueryContext ctx) {
        _left.toString(ctx);
        ctx.append(_operator.toString());
        _right.toString(ctx);
    }
    
    //-----------------------------------------------------------------------------------    
}
