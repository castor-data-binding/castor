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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Class to generate SQL update query statements. 
 * <br/>
 * Note: Be aware that the SQL statement will be invalid without any assignment
 * of for empty compound conditions. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class Update extends QueryObject {    
    //-----------------------------------------------------------------------------------    

    /** Qualifier of the table to update records of. */
    private final Qualifier _qualifier;
    
    /** List of Assignment objects to hold the set clause assignments. */
    private final List<Assignment> _assignment = new ArrayList<Assignment>();
    
    /** Condition that specifies which records to update. */
    private Condition _condition;    
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a SQL update statement that updates records of the table 
     * provided.
     *  
     * @param name Name of the table in update statement. 
     */
    public Update(final String name) {        
        _qualifier = new Table(name);
    }    
    
    //-----------------------------------------------------------------------------------    

    /**
     * Appends given assignment to the list of Assignment objects.
     * 
     * @param assignment Assignment object added to the list of assignments that will
     *        be appended to SET clause of sql statement.
     */
    public void addAssignment(final Assignment assignment) {
        _assignment.add(assignment);        
    }

    /**
     * Appends an assignment of the given value to the given column.
     * 
     * @param column Column to assign the value to.
     * @param value Expression to be assigned to the column.
     */
    public void addAssignment(final Column column, final Expression value) {
        addAssignment(new Assignment(column, value));
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Get condition that specifies which records to update.
     * 
     * @return Condition that specifies which records to update.
     */
    public Condition getCondition() {
        return _condition;
    }

    /**
     * Set condition that specifies which records to update.
     * 
     * @param condition Condition that specifies which records to update.
     */
    public void setCondition(final Condition condition) {
        _condition = condition;
    }
    
    //-----------------------------------------------------------------------------------    

    @Override
    public void toString(final QueryContext ctx) {
        ctx.append(QueryConstants.UPDATE);
        ctx.append(QueryConstants.SPACE);
        
        _qualifier.toString(ctx);
        
        ctx.append(QueryConstants.SPACE);
        ctx.append(QueryConstants.SET);
        ctx.append(QueryConstants.SPACE);

        for (Iterator<Assignment> iter = _assignment.iterator(); iter.hasNext(); ) {
            iter.next().toString(ctx);
            if (iter.hasNext()) {
                ctx.append(QueryConstants.SEPERATOR);
                ctx.append(QueryConstants.SPACE);
            }
        }
        
        if (_condition != null) {
            ctx.append(QueryConstants.SPACE);
            ctx.append(QueryConstants.WHERE);
            ctx.append(QueryConstants.SPACE);
            _condition.toString(ctx);
        }
    }   
    
    //-----------------------------------------------------------------------------------
}
