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
import java.util.List;

import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;

/**
 * Abstract base class for all qualifiers.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class Qualifier implements QueryObject {
    //-----------------------------------------------------------------------------------    

    /** Name of the qualifier. */
    private final String _name;

    /** List of joins belonging to this qualifier. */
    private final List<Join> _joins = new ArrayList<Join>();

    //-----------------------------------------------------------------------------------    

    /**
     * Construct a qualifier with given name.
     * 
     * @param name Name of the qualifier.
     */
    protected Qualifier(final String name) {
        if (name == null) { throw new NullPointerException(); }
        _name = name;
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Returns name of the qualifier.
     * 
     * @return Name of the qualifier.
     */
    public final String name() {
        return _name;
    }

    /**
     * Returns list of joins currently set.
     * 
     * @return List of joins currently set.
     */
    public final List<Join> getJoins() {
        return _joins;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Builder method to create a column with given name belonging to this qualifier.
     * 
     * @param name Name of the column.
     * @return Column belonging to this qualifier.
     */
    public final Column column(final String name) {
        return new Column(this, name);
    }

    /**
     * Method providing possibility to add inner join.
     * 
     * @param join Qualifier to construct and add join for.
     */
    public final void addInnerJoin(final Qualifier join) {
        _joins.add(new Join(JoinOperator.INNER, join));
    }

    /**
     * Method providing possibility to add inner join with a specific condition.
     * 
     * @param join Qualifier to construct and add join for.
     * @param cond Condition to be used for the join.
     */
    public final void addInnerJoin(final Qualifier join, final Condition cond) {
        _joins.add(new Join(JoinOperator.INNER, join, cond));
    }

    /**
     * Method providing possibility to add left join.
     * 
     * @param join Qualifier to construct and add join for.
     */
    public final void addLeftJoin(final Qualifier join) {
        _joins.add(new Join(JoinOperator.LEFT, join));
    }

    /**
     * Method providing possibility to add left join with a specific condition.
     * 
     * @param join Qualifier to construct and add join for.
     * @param cond Condition to be used for the join.
     */
    public final void addLeftJoin(final Qualifier join, final Condition cond) {
        _joins.add(new Join(JoinOperator.LEFT, join, cond));
    }

    /**
     * Method providing possibility to add right join.
     * 
     * @param join Qualifier to construct and add join for.
     */
    public final void addRightJoin(final Qualifier join) {
        _joins.add(new Join(JoinOperator.RIGHT, join));
    }

    /**
     * Method providing possibility to add right join with a specific condition.
     * 
     * @param join Qualifier to construct and add join for.
     * @param cond Condition to be used for the join.
     */
    public final void addRightJoin(final Qualifier join, final Condition cond) {
        _joins.add(new Join(JoinOperator.RIGHT, join, cond));
    }

    /**
     * Method providing possibility to add full join.
     * 
     * @param join Qualifier to construct and add join for.
     */
    public final void addFullJoin(final Qualifier join) {
        _joins.add(new Join(JoinOperator.FULL, join));
    }

    /**
     * Method providing possibility to add full join with a specific condition.
     * 
     * @param join Qualifier to construct and add join for.
     * @param cond Condition to be used for the join.
     */
    public final void addFullJoin(final Qualifier join, final Condition cond) {
        _joins.add(new Join(JoinOperator.FULL, join, cond));
    }

    /**
     * Method providing possibility to add passed join.
     * 
     * @param join Join to be added.
     */
    public final void addJoin(final Join join) {
        _joins.add(join);
    }

    /**
     * Method to check if joins exist for this qualifier.
     * 
     * @return True: List of joins is not empty. False: List of joins is empty.
     */
    public final boolean hasJoin() {
        return !_joins.isEmpty();
    }

    //-----------------------------------------------------------------------------------    

    /** 
     * Method constructing query string.
     * 
     * @return Constructed query string.
     */
    public final String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(_name);

        return sb.toString();
    }

    //-----------------------------------------------------------------------------------    
}
