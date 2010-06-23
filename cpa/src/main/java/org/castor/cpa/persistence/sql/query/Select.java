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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Class to generate SQL select query statements. 
 * <br/>
 * Note: Be aware that the SQL statement will be invalid for empty compound conditions. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 27 Jul 2009) $
 */
public final class Select implements QueryObject {    
    //-----------------------------------------------------------------------------------    

    /** Qualifier of the table to select records of. */
    private final Qualifier _qualifier;
    
    /** List of expressions to be returned by select statement. */
    private final List<Expression> _select = new ArrayList<Expression>();  
    
    /** Condition that specifies which records to select. */
    private Condition _condition;    
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a SQL select statement that selects records of the table provided.
     *  
     * @param name Name of the table to select records of. 
     */
    public Select(final String name) {        
        this(new Table(name));
    }    
    
    /**
     * Construct a SQL select statement that selects records of the table provided.
     *  
     * @param qualifier Qualifier to select records of. 
     */
    public Select(final Qualifier qualifier) {        
        if (qualifier == null) { throw new NullPointerException(); }
        _qualifier = qualifier;
    }    
    
    //-----------------------------------------------------------------------------------    

    /**
     * Appends the provided field to the list of fields to be fetched from table. 
     * 
     * @param name Column object representing a column to be fetched.
     */
    public void addSelect(final Column name) {       
        _select.add(name);        
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Getter returning list of Expressions currently set.
     * 
     * @return List of current expressions.
     */
    public List<Expression> getSelect() { return _select; }

    /**
     * Getter returning Qualifier currently set.
     * 
     * @return Qualifier currently set.
     */
    public Qualifier getQualifier() { return _qualifier; }

    /**
     * Get condition that specifies which records to select.
     * 
     * @return Condition that specifies which records to select.
     */
    public Condition getCondition() {
        return _condition;
    }

    /**
     * Set condition that specifies which records to select.
     * 
     * @param condition Condition that specifies which records to select.
     */
    public void setCondition(final Condition condition) {
        _condition = condition;
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
        sb.append(QueryConstants.SELECT);
        sb.append(QueryConstants.SPACE);
        
        if (_select.isEmpty()) {
            sb.append(QueryConstants.STAR);
        } else {
            for (Iterator<Expression> iter = _select.iterator(); iter.hasNext(); ) {
                sb.append(iter.next().toString());
                if (iter.hasNext()) {
                    sb.append(QueryConstants.SEPERATOR);
                    sb.append(QueryConstants.SPACE);
                }
            }
        }
        
        sb.append(QueryConstants.SPACE);
        sb.append(QueryConstants.FROM);
        sb.append(QueryConstants.SPACE);

        sb.append(_qualifier.toString());
        
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
