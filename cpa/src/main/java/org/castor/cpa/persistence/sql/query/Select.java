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

    /** List of expressions to be returned by select statement. */
    private final List<Expression> _select = new ArrayList<Expression>();

    /** List of qualifiers to be used in from-clause. */
    private final List<Qualifier> _from = new ArrayList<Qualifier>();

    /** Condition that specifies which records to select. */
    private Condition _condition;    

    /** Flag to determine if statement should be executed with or without lock. */
    private boolean _locked = false;

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
        _from.add(qualifier);
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
     * Appends the provided qualifier to the list of qualifiers to be used to build from-clause.
     * 
     * @param qualifier Qualifier to be added to from-clause.
     */
    public void addFrom(final Qualifier qualifier) {
        _from.add(qualifier);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Getter returning list of Expressions currently set.
     * 
     * @return List of current expressions.
     */
    public List<Expression> getSelect() { return _select; }

    /**
     * Getter returning from-list currently set.
     * 
     * @return From-list currently set.
     */
    public List<Qualifier> getFrom() { return _from; }

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

    /**
     * Get locked-flag that specifies if query has to be executed in locked mode or not.
     * 
     * @return True: execute query in locked mode, False: execute without lock.
     */
    public boolean isLocked() {
        return _locked;
    }

    /**
     * Set locked-flag that specifies if query has to be executed in locked mode or not.
     * 
     * @param locked True: execute query in locked mode, False: execute without lock.
     */
    public void setLocked(final boolean locked) {
        _locked = locked;
    }

    /**
     * Method to check if qualifiers exist to add to from-clause.
     * 
     * @return True: List of froms is not empty. False: List of froms is empty.
     */
    public boolean hasFrom() {
        return !_from.isEmpty();
    }

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

        for (Iterator<Qualifier> iter = _from.iterator(); iter.hasNext(); ) {
            Qualifier qualifier = iter.next();
            if (!qualifier.hasJoin()) {
                if (qualifier instanceof TableAlias) {
                    sb.append(((TableAlias) qualifier).getTable().toString());
                    sb.append(QueryConstants.SPACE);
                }
                sb.append(qualifier.toString());
            } else {
                // Open all necessary parentheses before starting any joins.
                for (int i = 0; i < qualifier.getJoins().size(); i++) {
                    sb.append(QueryConstants.LPAREN);
                }
                for (int i = 0; i < qualifier.getJoins().size(); i++) {
                    Join join = qualifier.getJoins().get(i);

                    if (i == 0) {
                        if (qualifier instanceof TableAlias) {
                            sb.append(((TableAlias) qualifier).getTable().toString());
                            sb.append(QueryConstants.SPACE);
                        }
                        sb.append(qualifier.toString());
                    }

                    sb.append(QueryConstants.SPACE);
                    sb.append(join.toString());

                    // Close opened parentheses after every JOIN-expression in the query.
                    sb.append(QueryConstants.RPAREN);
                }
            }

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

    /**
     * {@inheritDoc}
     */
    public void accept (final Visitor visitor) { visitor.visit(this); }

    //-----------------------------------------------------------------------------------
}
