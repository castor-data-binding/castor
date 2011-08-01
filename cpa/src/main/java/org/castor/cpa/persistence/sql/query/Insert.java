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
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Class to generate SQL Insert query statements.
 * <br/>
 * Note: Be aware that the SQL statement will be invalid without any assignment.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class Insert implements QueryObject {    
    //-----------------------------------------------------------------------------------    

    /** Qualifier of the table to update records of. */
    private final Qualifier _qualifier;
    
    /** List of Assignment objects to hold the set clause assignments. */
    private final List<Assignment> _assignment = new ArrayList<Assignment>();  
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a SQL insert statement that inserts into the table.
     *  
     * @param name Name of the table in update statement. 
     */
    public Insert(final String name) {        
        _qualifier = new Table(name);
    }    
    
    //-----------------------------------------------------------------------------------    

    /** 
     * Getter returning Qualifier currently set.
     * 
     * @return Qualifier currently set.
     */
    public Qualifier getQualifier() {
        return _qualifier;
    }

    /** 
     * Getter returning list of assignments currently set.
     * 
     * @return List of assignments currently set.
     */
    public List<Assignment> getAssignment() {
        return _assignment;
    }

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
        sb.append(QueryConstants.INSERT);
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.INTO);
        sb.append(QueryConstants.SPACE);
        
        sb.append(_qualifier.toString());
        
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.LPAREN);
        
        for (Iterator<Assignment> iter = _assignment.iterator(); iter.hasNext(); ) {
            sb.append(iter.next().leftExpression().toString());
            if (iter.hasNext()) {
                sb.append(QueryConstants.SEPERATOR);
                sb.append(QueryConstants.SPACE);
            }
        }

        sb.append(QueryConstants.RPAREN); 
        
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.VALUES);

        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.LPAREN);   

        for (Iterator<Assignment> iter = _assignment.iterator(); iter.hasNext(); ) {
            sb.append(iter.next().rightExpression().toString());
            if (iter.hasNext()) {
                sb.append(QueryConstants.SEPERATOR);
                sb.append(QueryConstants.SPACE);
            }
        }

        sb.append(QueryConstants.RPAREN);

        return sb.toString();
    }

    //-----------------------------------------------------------------------------------
}
