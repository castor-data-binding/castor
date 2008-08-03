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

import java.math.BigDecimal;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.object.literal.BigDecimalLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;

/**
 * Final class that represents LOCATE function.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Locate extends AbstractFunction {
    //--------------------------------------------------------------------------
    
    /** Maximum offset when comparing double and BigDecimal values. */
    private static final double OFFSET = 1E-10;
    
    //--------------------------------------------------------------------------

    /** The string expression to be searched in by LOCATE function. */
    private Expression _string;
    
    /** The value expression to search for by LOCATE function. */
    private Expression _value;

    /** The index expression defining the start position of LOCATE function. */
    private Expression _index;

    //--------------------------------------------------------------------------
    
    /**
     * Construct an instance of Locate function with default start position.
     */
    public Locate() {
        _index = new LongLiteral(1);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Gets the string expression to be searched in by LOCATE function.
     * 
     * @return The string expression to be searched in by LOCATE function.
     */
    public Expression getString() {
        return _string;
    }
    
    /**
     * Sets the string expression to be searched in by LOCATE function.
     * 
     * @param string The new string expression to be searched in by LOCATE function.
     */
    public void setString(final Expression string) {
        _string = string;
    }
    
    /**
     * Gets the expression to search for by LOCATE function.
     * 
     * @return The expression to search for by LOCATE function.
     */
    public Expression getValue() {
        return _value;
    }
    
    /**
     * Sets the expression to search for by LOCATE function.
     * 
     * @param value The new expression to search for by LOCATE function.
     */
    public void setValue(final Expression value) {
        _value = value;
    }
    
    /**
     * Gets the expression defining the start position of LOCATE function.
     * 
     * @return The expression defining the start position of LOCATE function.
     */
    public Expression getIndex() {
        return _index;
    }
    
    /**
     * Sets the expression defining the start position of LOCATE function.
     * 
     * @param index The new expression defining the start position of LOCATE function.
     */
    public void setIndex(final Expression index) {
        _index = index;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        boolean defaultStart = false;
        if (_index instanceof LongLiteral) {
            LongLiteral literal = (LongLiteral) _index;
            defaultStart = (literal.getValue() == 1);
        } else if (_index instanceof DoubleLiteral) {
            DoubleLiteral literal = (DoubleLiteral) _index;
            defaultStart = (Math.abs(literal.getValue() - 1.0) < OFFSET);
        } else if (_index instanceof BigDecimalLiteral) {
            BigDecimalLiteral literal = (BigDecimalLiteral) _index;
            BigDecimal value = literal.getValue();
            BigDecimal diff = value.subtract(new BigDecimal(1.0)).abs();
            defaultStart = (diff.compareTo(new BigDecimal(OFFSET)) < 0);
        }

        sb.append("LOCATE(");
        if (_string != null) {
            _string.toString(sb);
        }
        sb.append(", ");
        if (_value != null) {
            _value.toString(sb);
        }
        if (!defaultStart) {
            sb.append(", ");
            if (_index != null) {
                _index.toString(sb);
            }
        }
        return sb.append(')');
    }

    //--------------------------------------------------------------------------
}
