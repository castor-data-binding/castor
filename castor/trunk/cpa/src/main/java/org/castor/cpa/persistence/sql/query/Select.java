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
import org.castor.cpa.persistence.sql.query.expression.Expression;

/**
 * Class to generate SQL select query statements. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Mon, 27 Jul 2009) $
 */
public final class Select extends QueryObject {    
    //-----------------------------------------------------------------------------------    

    /** Qualifier of the table to select records of. */
    private final Qualifier _qualifier;
    
    /** Array of Column objects that represents the fields to be fetched using select statement. */
    private final List<Expression> _select;  
    
    /** Condition that specifies which records to select. */
    private AndCondition _condition;    
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a SQL select statement that selects records of the table 
     * provided.
     *  
     * @param name Name of the table in select statement. 
     */
    public Select(final String name) {        
        _qualifier = new Table(name);
        _select = new ArrayList<Expression>();
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

    /**
     * Appends a field representing a column to be fetched from the table. 
     * 
     * @param name Name of the column to be fetched.
     */
    public void addSelect(final String name) {
        addSelect(new Column(name));
    }
    
    /**
     * Appends a field representing a column to be fetched from the table. 
     * 
     * @param qualifier Qualifier to be appended.
     * @param name Name of the column to be fetched.
     */
    public void addSelect(final String qualifier, final String name) {
        addSelect(new Column(new Table(qualifier), name));
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Appends given condition to be anded with all others to specify the record to select.
     * 
     * @param condition Condition to be anded with all others to specify the record to select.
     */
    public void addCondition(final Condition condition) {
        if (_condition == null) {
            _condition = new AndCondition();
        } 
        _condition.and(condition);        
    }

    /**
     * Appends a condition of the form 'name=?' to be anded with all others to specify
     * the record to select. The name given will be used as column name and to bind a
     * value to the parameter.
     * 
     * @param name Name of the column and parameter of the condition.
     */
    public void addCondition(final String name) {
        addCondition(new Column(name).equal(new Parameter(name)));
    }
    
    /**
     * Appends a condition of the form 'name=?' to be anded with all others to specify
     * the record to select. The qualifier will be appended before the actual column name.
     * The name given will be used as column name and to bind a
     * value to the parameter.
     * 
     * @param qualifier Qualifier to be appended.
     * @param name Name of the column and parameter of the condition.
     */
    public void addCondition(final String qualifier, final String name) {
        addCondition(new Column(new Table(qualifier), name).equal(new Parameter(name)));
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
        ctx.append(QueryConstants.SELECT);
        ctx.append(QueryConstants.SPACE);
        
        if (!_select.isEmpty()) {
            for (Iterator<Expression> iter = _select.iterator(); iter.hasNext(); ) {
                iter.next().toString(ctx);
                if (iter.hasNext()) {
                    ctx.append(QueryConstants.SEPERATOR);
                    ctx.append(QueryConstants.SPACE);
                }
            }
        } else {
            ctx.append(QueryConstants.STAR);
        }
        
        ctx.append(QueryConstants.SPACE);
        ctx.append(QueryConstants.FROM);
        ctx.append(QueryConstants.SPACE);
        _qualifier.toString(ctx);
        
        if (_condition != null) {
            ctx.append(QueryConstants.SPACE);
            ctx.append(QueryConstants.WHERE);
            ctx.append(QueryConstants.SPACE);
            _condition.toString(ctx);
        }
    }
    
    //-----------------------------------------------------------------------------------
}
