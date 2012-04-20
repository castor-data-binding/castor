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

import org.castor.cpa.persistence.sql.query.QueryConstants;
import org.castor.cpa.persistence.sql.query.Visitor;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Predicate that checks expressions for being null or not null.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class IsNullPredicate extends Predicate {
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a predicate that checks the given expression for being null.
     * 
     * @param expression Expression to check for null.
     */
    public IsNullPredicate(final Expression expression) {
        this(expression, true);
    }

    /**
     * Construct a predicate that checks the given expression for being null.
     * 
     * @param expression Expression to check for null.
     * @param evaluateTo What is the expected result of the evaluation?
     */
    public IsNullPredicate(final Expression expression, final boolean evaluateTo) {
        super(expression, evaluateTo);
    }

    //-----------------------------------------------------------------------------------    

    /** 
     * {@inheritDoc}
     */
    public void accept (final Visitor visitor) { visitor.visit(this); }

    //-----------------------------------------------------------------------------------    

    /** 
     * Method constructing query string.
     * 
     * @return Constructed query string.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(expression().toString());
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.IS);
        if (!evaluateTo()) {
            sb.append(QueryConstants.SPACE);
            sb.append(QueryConstants.NOT);
        }
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.NULL);

        return sb.toString();
    }

    //-----------------------------------------------------------------------------------    
}
