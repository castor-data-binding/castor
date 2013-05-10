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
import org.castor.cpa.query.TrimSpecification;
import org.castor.cpa.query.object.literal.StringLiteral;

/**
 * Final class that represents TRIM function.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Trim extends AbstractFunction {
    //--------------------------------------------------------------------------

    /** The string expression to trim by TRIM function. */
    private Expression _string;
    
    /** The trim character expression of TRIM function. */
    private Expression _character;
    
    /** The trim specification of TRIM function. */
    private TrimSpecification _specification;
    
    //--------------------------------------------------------------------------

    /**
     * Construct an instance Trim function with default trim character and specification.
     */
    public Trim() {
        _character = new StringLiteral(" ");
        _specification = TrimSpecification.BOTH;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Gets the string expression of TRIM function.
     * 
     * @return The string expression of TRIM function.
     */
    public Expression getString() {
        return _string;
    }
    
    /**
     * Sets the string expression of TRIM function.
     * 
     * @param string The new string expression of TRIM function.
     */
    public void setString(final Expression string) {
        _string = string;
    }
    
    /**
     * Gets the trim character expression of TRIM function.
     * 
     * @return The trim character expression of TRIM function.
     */
    public Expression getCharacter() {
        return _character;
    }
    
    /**
     * Sets the trim character expression of TRIM function.
     * 
     * @param character Trim character expression TRIM function.
     */
    public void setCharacter(final Expression character) {
        _character = character;
    }
    
    /**
     * Gets the trim specification of TRIM function.
     * 
     * @return Trim specification of TRIM function.
     */
    public TrimSpecification getSpecification() {
        return _specification;
    }
    
    /**
     * Sets the trim specification of TRIM function.
     * 
     * @param trimSpecification Trim specification of TRIM function.
     */
    public void setSpecification(final TrimSpecification trimSpecification) {
        _specification = trimSpecification;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        boolean withSpec = (_specification != TrimSpecification.BOTH);
        boolean withChar = (!(_character instanceof StringLiteral)
                || !" ".equals(((StringLiteral) _character).getValue()));

        sb.append("TRIM(");
        if (withSpec || withChar) {
            if (withSpec) {
                if (_specification != null) {
                    sb.append(_specification);
                }
                sb.append(' ');
            }
            if (withChar) {
                if (_character != null) {
                    _character.toString(sb);
                }
                sb.append(' ');
            }
            sb.append("FROM ");
        }
        if (_string != null) {
            _string.toString(sb);
        }
        return sb.append(')');
    }

    //--------------------------------------------------------------------------
}
