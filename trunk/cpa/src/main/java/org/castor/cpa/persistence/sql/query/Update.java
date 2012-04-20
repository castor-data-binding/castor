/*
 * Copyright 2010 Ralf Joachim, Ahmad Hassan, Dennis Butterstein
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
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision$ $Date$
 */
public final class Update implements QueryObject {    
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

    /**
     * Get method returning qualifier currently set.
     *  
     *  @return Qualifier of the table to update records of.
     */
    public Qualifier getQualifier() { return _qualifier; }

    /**
     * Get method returning current list of assignments.
     *  
     *  @return List of assignments.
     */
    public List<Assignment> getAssignment() { return _assignment; }

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
        sb.append(QueryConstants.UPDATE);
        sb.append(QueryConstants.SPACE);
        
        sb.append(_qualifier.toString());
        
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.SET);
        sb.append(QueryConstants.SPACE);

        for (Iterator<Assignment> iter = _assignment.iterator(); iter.hasNext(); ) {
            sb.append(iter.next().toString());
            if (iter.hasNext()) {
                sb.append(QueryConstants.SEPERATOR);
                sb.append(QueryConstants.SPACE);
            }
        }
        
        if (_condition != null) {
            sb.append(QueryConstants.SPACE);
            sb.append(QueryConstants.WHERE);
            sb.append(QueryConstants.SPACE);
            sb.append(_condition.toString());
        }

        return sb.toString();
    }

    //-----------------------------------------------------------------------------------
}
