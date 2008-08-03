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
 * Final class that represents CONCAT function.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Concat extends AbstractFunction {
    //--------------------------------------------------------------------------
    
    /** The first string expression of CONCAT function. */
    private Expression _string1;
    
    /** The second string expression of CONCAT function. */
    private Expression _string2;

    //--------------------------------------------------------------------------

    /**
     * Gets the first string expression of CONCAT function.
     * 
     * @return The first string expression of CONCAT function.
     */
    public Expression getString1() {
        return _string1;
    }
    
    /**
     * Sets the first string expression of CONCAT function.
     * 
     * @param string The first string expression of CONCAT function.
     */
    public void setString1(final Expression string) {
        _string1 = string;
    }
    
    /**
     * Gets the second string expression of CONCAT function.
     * 
     * @return The second string expression of CONCAT function.
     */
    public Expression getString2() {
        return _string2;
    }
    
    /**
     * Sets the string expression of CONCAT function.
     * 
     * @param string The second string expression of CONCAT function.
     */
    public void setString2(final Expression string) {
        _string2 = string;
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append("CONCAT(");
        if (_string1 != null) { _string1.toString(sb); }
        sb.append(", ");
        if (_string2 != null) { _string2.toString(sb); }
        return sb.append(')');
    }

    //--------------------------------------------------------------------------
}
