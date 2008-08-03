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
package org.castor.cpa.query.object.expression;

import java.util.Iterator;
import java.util.List;

import org.castor.cpa.query.Expression;

/**
 * Abstract base class for compound expression.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public abstract class CompoundExpression extends AbstractExpression {

    //--------------------------------------------------------------------------
    
    /** The expressions. */
    private List < Expression > _expressions;
    
    

    //--------------------------------------------------------------------------
    

    /**
     * Adds the expression.
     * 
     * @param index the index
     * @param expression the expression
     */
    public final void addExpression(final int index, final Expression expression) {
        _expressions.add(index, expression);
    }
    
    /**
     * Adds the expression.
     * 
     * @param expression the expression
     */
    public final void addExpression(final Expression expression) {
        _expressions.add(expression);
    }


    /**
     * Gets the expressions.
     * 
     * @return the expressions
     */
    public final List < Expression > getExpressions() {
        return _expressions;
    }

    /**
     * Sets the expressions.
     * 
     * @param expressions the new expressions
     */
    public final void setExpressions(final List < Expression > expressions) {
        _expressions = expressions;
    }
    
    /**
     * Gets the operator.
     * 
     * @return the operator
     */
    protected abstract String getOperator();
    
    /**
     * {@inheritDoc}
     */ 
   public final StringBuilder toString(final StringBuilder sb) {
       for (Iterator < Expression > iter = _expressions.iterator(); iter.hasNext(); ) {
          Expression expression = iter.next();
          if (expression instanceof Add 
                  || expression instanceof Multiply  
                  || expression instanceof Concat) {
              sb.append('(');
              expression.toString(sb);
              sb.append(')');
          } else if (expression != null) {
           if (expression instanceof Subtract 
                   || expression instanceof Divide 
                   || expression instanceof Remainder) {
               sb.append('(');
               expression.toString(sb);
               sb.append(')');
           } else if (expression instanceof Negate) {
               sb.append("(");
               sb.append(" - ");
               expression.toString(sb);
               sb.append(')'); 
           } else {  
              expression.toString(sb);
           }
          }
          if (iter.hasNext()) { sb.append(getOperator()); }
        }
        return sb;
    }
}
