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

import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;

/**
 * Class to generate SQL update query statements. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 13 Jul 2009) $
 */
public final class Update extends QueryObject {    
    //-----------------------------------------------------------------------------------    

    /** Qualifier of the table to update records of. */
    private final Qualifier _qualifier;
    
    /** Array of Assignment objects to hold the set clause assignments. */
    private List<Assignment> _assignment;  
    
    /** Condition that specifies which records to update. */
    private AndCondition _condition;    
    
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
        if (_assignment == null) {
            _assignment = new ArrayList<Assignment>();
        } 
        _assignment.add(assignment);        
    }

    /**
     * Appends an assignment of the form 'name=?' to set the record to update. The parameter
     * name given will be used as a name to bind a value to the column.
     * 
     * @param name Name of the column of the assignment.
     * @param param Name of the parameter that will be bound to the column in the assignment.
     */
    public void addAssignment(final String name, final String param) {
        addAssignment(new Assignment(new Column(name), new Parameter(param)));
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Appends given condition to be anded with all others to specify the record to update.
     * 
     * @param condition Condition to be anded with all others to specify the record to update.
     */
    public void addCondition(final Condition condition) {
        if (_condition == null) {
            _condition = new AndCondition();
        } 
        _condition.and(condition);        
    }

    /**
     * Appends a condition of the form 'name=?' to be anded with all others to specify
     * the record to update. The name given will be used as column name and to bind a
     * value to the parameter.
     * 
     * @param name Name of the column and parameter of the condition.
     */
    public void addCondition(final String name) {
        addCondition(new Column(name).equal(new Parameter(name)));
    }
    
    /**
     * Appends a condition of the form 'name IS NULL' to be anded with all others to specify
     * the record to update. The name given will be used as column name.
     * 
     * @param name Name of the column which is compared with NULL.
     */
    public void addNullCondition(final String name) {
        addCondition(new Column(name).isNull());
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Returns the condition object.
     * 
     * @return the condition
     */
    public AndCondition getCondition() {
        return _condition;
    }
    
    /**
     * Assigns the provided condition object to the class attribute.
     * 
     * @param condition the condition to set
     */
    public void setCondition(final AndCondition condition) {
        _condition = condition;
    }
    
    //-----------------------------------------------------------------------------------    

    @Override
    public void toString(final QueryContext ctx) {
        ctx.append(QueryConstants.UPDATE);
        ctx.append(QueryConstants.SPACE);
        
        _qualifier.toString(ctx);
        
        if (_assignment != null) {
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
