/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.object.function;

import java.util.Iterator;
import java.util.List;

import org.castor.cpa.query.Expression;

/**
 * Final class that represents functions with custom names of CastorQL.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class CustomFunction extends AbstractFunction {
    //--------------------------------------------------------------------------

    /** The name of CastorFunction. */
    private final String _name;
    
    /** The list of parameter expressions of CastorFunction. */
    private List < Expression > _parameters;
    
    //--------------------------------------------------------------------------

    /**
     * Construct a new CustomFunction with given name.
     * 
     * @param name Name of the function.
     */
    public CustomFunction(final String name) {
        if (name == null) { throw new NullPointerException(); }
        _name = name;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Gets the name of CastorFunction.
     * 
     * @return The name of CastorFunction.
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Gets the list of parameter expressions of CastorFunction.
     * 
     * @return The list of parameter expressions of CastorFunction.
     */
    public List < Expression > getParameters() {
        return _parameters;
    }
    
    /**
     * Sets the list of parameter expressions of CastorFunction.
     * 
     * @param parameters The list of parameter expressions of CastorFunction.
     */
    public void setParameters(final List < Expression > parameters) {
        _parameters = parameters;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append(_name).append('(');
        if (_parameters != null) {
            for (Iterator < Expression > iter = _parameters.iterator(); iter.hasNext(); ) {
                Expression expression = iter.next();
                if (expression != null) { expression.toString(sb); }
                if (iter.hasNext()) { sb.append(", "); }
            }
        }
        return sb.append(')');
    }

    //--------------------------------------------------------------------------
}
