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
package org.castor.cpa.persistence.sql.query.expression;

import org.castor.cpa.persistence.sql.query.QueryConstants;
import org.castor.cpa.persistence.sql.query.QueryContext;
import org.castor.cpa.persistence.sql.query.Visitor;

/**
 * A parameter in a SQL query. Values are always bound to parameter with names.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class Parameter extends Expression {
    //-----------------------------------------------------------------------------------    

    /** Name of the parameter for binding of values. */
    private final String _name;
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct parameter with given name for binding.
     *  
     * @param name Name of the parameter for binding of values. 
     */
    public Parameter(final String name) {
        if (name == null) { throw new NullPointerException(); }
        _name = name;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Returns name of the parameter for binding of values.
     * 
     * @return Name of the parameter for binding of values.
     */
    public String name() {
        return _name;
    }

    //-----------------------------------------------------------------------------------    

    @Override
    public void toString(final QueryContext ctx) {
        ctx.addParameter(_name);
        ctx.append(QueryConstants.PARAMETER);
    }

    /**
     * {@inheritDoc}
     */
    public void accept (final Visitor visitor) { visitor.visit(this); }

    //-----------------------------------------------------------------------------------    
}
