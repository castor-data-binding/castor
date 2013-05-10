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
 * Final class that represents SUBSTRING function.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Substring extends AbstractFunction {
    //--------------------------------------------------------------------------

    /** The string expression to create substring of by SUBSTRING function. */
    private Expression _string;
    
    /** The index expression of SUBSTRING function. */
    private Expression _index;
    
    /** The length expression of SUBSTRING function. */
    private Expression _length;

    //--------------------------------------------------------------------------

    /**
     * Gets the string expression of SUBSTRING function.
     * 
     * @return The string expression of SUBSTRING function.
     */
    public Expression getString() {
        return _string;
    }
    
    /**
     * Sets the string expression of SUBSTRING function.
     * 
     * @param string The new string expression of SUBSTRING function.
     */
    public void setString(final Expression string) {
        _string = string;
    }
    
    /**
     * Gets the start expression of SUBSTRING function.
     * 
     * @return The start expression of SUBSTRING function.
     */
    public Expression getIndex() {
        return _index;
    }
    
    /**
     * Sets the start expression of SUBSTRING function.
     * 
     * @param index The new start expression of SUBSTRING function.
     */
    public void setIndex(final Expression index) {
        _index = index;
    }
    
    /**
     * Gets the length expression of SUBSTRING function.
     * 
     * @return The length expression of SUBSTRING function.
     */
    public Expression getLength() {
        return _length;
    }
    
    /**
     * Sets the length expression of SUBSTRING function.
     * 
     * @param length The new length expression of SUBSTRING function.
     */
    public void setLength(final Expression length) {
        _length = length;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append("SUBSTRING(");
        if (_string != null) { _string.toString(sb); }
        sb.append(", ");
        if (_index != null) { _index.toString(sb); }
        sb.append(", ");
        if (_length != null) { _length.toString(sb); }
        return sb.append(')');
    }

    //--------------------------------------------------------------------------
}
