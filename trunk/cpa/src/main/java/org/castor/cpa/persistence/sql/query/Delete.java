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

import org.castor.cpa.persistence.sql.query.condition.Condition;

/**
 * Class to generate SQL delete query statements.
 * <br/>
 * Note: Be aware that the SQL statement will be invalid for empty compound conditions. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class Delete implements QueryObject {
    //-----------------------------------------------------------------------------------    
    
    /** Qualifier of the table to delete records from. */
    private final Qualifier _qualifier;
    
    /** Condition that specifies which records to delete. */
    private Condition _condition;    
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct a SQL delete statement that deletes records of the table with given name.
     * 
     * @param name Name of the table to delete records of. 
     */
    public Delete(final String name) {
        _qualifier = new Table(name);
    }
  
    //-----------------------------------------------------------------------------------
    
    /**
     * Get condition that specifies which records to delete.
     * 
     * @return Condition that specifies which records to delete.
     */
    public Condition getCondition() {
        return _condition;
    }

    /**
     * Set condition that specifies which records to delete.
     * 
     * @param condition Condition that specifies which records to delete.
     */
    public void setCondition(final Condition condition) {
        _condition = condition;
    }

    /**
     * Get method returning <code>Qualifier</code> currently set.
     * 
     * @return Qualifier currently set.
     */
    public Qualifier getQualifier() { return _qualifier; }

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
        sb.append(QueryConstants.DELETE);
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

