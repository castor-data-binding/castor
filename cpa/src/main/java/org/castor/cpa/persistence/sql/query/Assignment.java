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
package org.castor.cpa.persistence.sql.query;

import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Assignment used for SQL update statements. 
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class Assignment implements QueryObject {
    //-----------------------------------------------------------------------------------

    /** Left operand of the assignment. */
    private final Column _left;
    
    /** Right operand of the assignment. */
    private final Expression _right;
    
    //-----------------------------------------------------------------------------------
    
    /**
     * Constructor that assigns the given right hand operand to the left hand one. 
     * 
     * @param left Left operand of the assignment.
     * @param right Right operand of the assignment. 
     */
    public Assignment(final Column left, final Expression right) {
        if ((left == null) || (right == null)) {
            throw new NullPointerException();
        }
        
        _left = left;
        _right = right;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Returns left operand of the assignment.
     * 
     * @return Left operand of the assignment.
     */
    public Column leftExpression() {
        return _left;
    }

    /**
     * Returns right operand of the assignment.
     * 
     * @return right operand of the assignment.
     */
    public Expression rightExpression() {
        return _right;
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
        sb.append(_left.toString());
        sb.append(QueryConstants.ASSIGN);
        sb.append(_right.toString());

        return sb.toString();
    }

    //-----------------------------------------------------------------------------------

}
