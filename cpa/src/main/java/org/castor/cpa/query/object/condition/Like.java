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
package org.castor.cpa.query.object.condition;

import org.castor.cpa.query.Expression;

/**
 * Final class that represents like simple condition.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Like extends SimpleCondition {
    //--------------------------------------------------------------------------

    /** The pattern of like simple condition. */
    private Expression _pattern;

    /** The escape of like simple condition. */
    private Expression _escape;
    
    /** The _not of like simple condition. */
    private boolean _not;

    //--------------------------------------------------------------------------

    /**
     * Gets the pattern of like simple condition.
     * 
     * @return the pattern of like simple condition
     */
    public Expression getPattern() {
        return _pattern;
    }

    /**
     * Sets the pattern of like simple condition.
     * 
     * @param pattern the new pattern of like simple condition
     */
    public void setPattern(final Expression pattern) {
        _pattern = pattern;
    }

    /**
     * Gets the escape of like simple condition.
     * 
     * @return the escape of like simple condition
     */
    public Expression getEscape() {
        return _escape;
    }

    /**
     * Sets the escape of like simple condition.
     * 
     * @param escape the new escape of like simple condition
     */
    public void setEscape(final Expression escape) {
        _escape = escape;
    }
    
    /**
     * Checks if is not of like simple condition.
     * 
     * @return true, if is not of like simple condition
     */
    public boolean isNot() {
        return _not;
    }

    /**
     * Sets the not of like simple condition.
     * 
     * @param not the new not of like simple condition
     */
    public void setNot(final boolean not) {
        _not = not;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        if (this.getExpression() != null) {
            this.getExpression().toString(sb);
        }
        if (_not) {
            sb.append(" NOT");
        }
        if (_pattern != null) {
            sb.append(" LIKE ");
            _pattern.toString(sb);
            if (_escape != null) {
             sb.append(" ESCAPE ");
             _escape.toString(sb);
            }
        }
        return sb;
    }

    //--------------------------------------------------------------------------

}
