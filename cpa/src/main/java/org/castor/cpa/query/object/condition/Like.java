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

    /** Pattern of like condition. */
    private Expression _pattern;

    /** Escape character of like condition. */
    private Expression _escape;
    
    //--------------------------------------------------------------------------

    /**
     * Get pattern of like condition.
     * 
     * @return Pattern of like condition.
     */
    public Expression getPattern() {
        return _pattern;
    }

    /**
     * Set pattern of like condition.
     * 
     * @param pattern Pattern of like condition.
     */
    public void setPattern(final Expression pattern) {
        _pattern = pattern;
    }

    /**
     * Gets escape character of like condition.
     * 
     * @return Escape character of like condition.
     */
    public Expression getEscape() {
        return _escape;
    }

    /**
     * Set escape character of like condition.
     * 
     * @param escape Escape character of like condition.
     */
    public void setEscape(final Expression escape) {
        _escape = escape;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append('(');
        if (getExpression() != null) {
            getExpression().toString(sb);
        }
        if (isNot()) {
            sb.append(" NOT");
        }
        sb.append(" LIKE ");
        if (_pattern != null) {
            _pattern.toString(sb);
        }
        if (_escape != null) {
            sb.append(" ESCAPE ");
            _escape.toString(sb);
        }
        sb.append(')');
        return sb;
    }

    //--------------------------------------------------------------------------
}
