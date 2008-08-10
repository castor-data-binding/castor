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

import java.util.ArrayList;
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
    
    /** List of expressions. */
    private List < Expression > _expressions;

    //--------------------------------------------------------------------------

    /**
     * Get operator of the compound expression.
     * 
     * @return Operator of the compound expression.
     */
    protected abstract String getOperator();
    
    //--------------------------------------------------------------------------

    /**
     * Add expression to the end of the list.
     * 
     * @param expression Expression to add to end of list.
     */
    public final void addExpression(final Expression expression) {
        if (_expressions == null) { _expressions = new ArrayList < Expression > (); }
        _expressions.add(expression);
    }

    /**
     * Get list of expressions.
     * 
     * @return List of expressions.
     */
    public final List < Expression > getExpressions() {
        return _expressions;
    }

    /**
     * Set list of expressions.
     * 
     * @param expressions List of expressions.
     */
    public final void setExpressions(final List < Expression > expressions) {
        _expressions = expressions;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */ 
    public final StringBuilder toString(final StringBuilder sb) {
        for (Iterator < Expression > iter = _expressions.iterator(); iter.hasNext(); ) {
            Expression expression = iter.next();
            if (expression instanceof CompoundExpression) {
                sb.append('(');
                expression.toString(sb);
                sb.append(')');
            } else {
                expression.toString(sb);
            }
            if (iter.hasNext()) { sb.append(getOperator()); }
        }
        return sb;
    }

    //--------------------------------------------------------------------------
}
