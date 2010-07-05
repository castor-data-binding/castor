/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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
 *
 * $Id: SQLStatementDelete.java 8469 2009-12-28 16:47:54Z rjoachim $
 */

package org.castor.cpa.persistence.sql.query;

import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.IsNullPredicate;
import org.castor.cpa.persistence.sql.query.condition.OrCondition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.NextVal;
import org.castor.cpa.persistence.sql.query.expression.Parameter;

/**
 * Interface providing methods for Elements of the implementation of the visitor pattern.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public interface Visitor {
    //-----------------------------------------------------------------------------------    

    /**
     * Visit method to handle <code>Assignment</code> elements.
     * 
     * @param assignment Assignment object to be handled.
     */
    void visit(final Assignment assignment);

    /**
     * Visit method to handle <code>Delete</code> elements.
     * 
     * @param delete Delete object to be handled.
     */
    void visit(final Delete delete);

    /**
     * Visit method to handle <code>Insert</code> elements.
     * 
     * @param insert Insert object to be handled.
     */
    void visit(final Insert insert);

    /**
     * Visit method to handle <code>Join</code> elements.
     * 
     * @param join Join object to be handled
     */
    void visit(final Join join);

    /**
     * Visit method to handle select elements.
     * 
     * @param select Select object to be handled.
     */
    void visit(final Select select);

    /**
     * Visit method to handle <code>Table</code> elements.
     * 
     * @param table Table object to be handled.
     */
    void visit(final Table table);

    /**
     * Visit method to handle <code>TableAlias</code> elements.
     * 
     * @param tableAlias TableAlias object to be handled.
     */
    void visit(final TableAlias tableAlias);

    /**
     * Visit method to handle update elements.
     * 
     * @param update Update object to be handled.
     */
    void visit(final Update update);

    /**
     * Visit method to handle <code>AndCondition</code> elements.
     * 
     * @param andCondition AndCondition object to be handled.
     */
    void visit(final AndCondition andCondition);

    /**
     * Visit method to handle <code>Compare</code> elements.
     * 
     * @param compare Compare object to be handled.
     */
    void visit(final Compare compare);

    /**
     * Visit method to handle <code>IsNullPredicate</code> elements.
     * 
     * @param isNullPredicate IsNullPredicate object to be handled.
     */
    void visit(final IsNullPredicate isNullPredicate);

    /**
     * Visit method to handle <code>OrCondition</code> elements.
     * 
     * @param orCondition OrCondition object to be handled.
     */
    void visit(final OrCondition orCondition);

    /**
     * Visit method to handle <code>Column</code> elements.
     * 
     * @param column Column object to be handled.
     */
    void visit(final Column column);

    /**
     * Visit method to handle <code>NextVal</code> elements.
     * 
     * @param nextVal NextVal object to be handled.
     */
    void visit(final NextVal nextVal);

    /**
     * Visit method to handle <code>Parameter</code> elements.
     * 
     * @param parameter Parameter object to be handled.
     */
    void visit(final Parameter parameter);

    //-----------------------------------------------------------------------------------

    /**
     * Method returning constructed String.
     * 
     * @return Constructed query string.
     */
    String toString();

    //-----------------------------------------------------------------------------------    
}
