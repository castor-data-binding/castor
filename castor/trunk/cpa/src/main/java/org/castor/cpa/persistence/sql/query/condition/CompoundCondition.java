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
package org.castor.cpa.persistence.sql.query.condition;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Compound condition is the abstract base class for AndCondition and OrCondition.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public abstract class CompoundCondition extends Condition {
    //-----------------------------------------------------------------------------------    

    /** List of conditions. */
    private ArrayList<Condition> _conditions = new ArrayList<Condition>();
    
    //-----------------------------------------------------------------------------------
    
    /**
     * Default constructor.
     */
    protected CompoundCondition() {
        super();
    }
    
    /**
     * Copy constructor.
     * 
     * @param condition Compound condition to create a copy of.
     */
    protected CompoundCondition(final CompoundCondition condition) {
        super();
        
        _conditions.addAll(condition._conditions);
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Inserts the specified condition at begin of this list of conditions.
     * 
     * @param condition Condition to be inserted to this list of conditions.
     */
    protected final void insert(final Condition condition) {
        if (condition == null) { throw new NullPointerException(); }
        _conditions.add(0, condition);
    }
    
    /**
     * Appends the specified condition to end of this list of conditions.
     * 
     * @param condition Condition to be appended to this list of conditions.
     */
    protected final void append(final Condition condition) {
        if (condition == null) { throw new NullPointerException(); }
        _conditions.add(condition);
    }
    
    /**
     * Returns an iterator over the elements in this list in proper sequence.
     * 
     * @return An iterator over the elements in this list in proper sequence.
     */
    public final Iterator<Condition>  iterator() {
        return _conditions.iterator();
    }

    //-----------------------------------------------------------------------------------    
}
