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

import org.castor.cpa.query.Expression;

/**
 * Final class that represents UPPER function.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Upper extends AbstractFunction {
    //--------------------------------------------------------------------------   
    
    /** The string expression of UPPER function. */
    private Expression _string;

    //--------------------------------------------------------------------------
    
    /**
     * Gets the string expression of UPPER function.
     * 
     * @return The string expression of UPPER function.
     */
    public Expression getString() {
        return _string;
    }

    /**
     * Sets the string expression of UPPER function.
     * 
     * @param string The new string expression of UPPER function.
     */
    public void setString(final Expression string) {
        _string = string;
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append("UPPER(");
        if (_string != null) { _string.toString(sb); }
        return sb.append(')');
    }
    
    //--------------------------------------------------------------------------
}
