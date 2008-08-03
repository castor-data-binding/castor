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
 * Final class that represents MOD (modulo) function.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class Mod extends AbstractFunction {
    //--------------------------------------------------------------------------

    /** The dividend expression of MOD function. */
    private Expression _dividend;


    /** The divisor expression of MOD function. */
    private Expression _divisor;

    //--------------------------------------------------------------------------

    /**
     * Gets the dividend expression of MOD function.
     * 
     * @return The dividend expression of MOD function.
     */
    public Expression getDividend() {
        return _dividend;
    }


    /**
     * Sets the dividend expression of MOD function.
     * 
     * @param dividend The new dividend expression of MOD function.
     */
    public void setDividend(final Expression dividend) {
        _dividend = dividend;
    }


    /**
     * Gets the divisor expression of MOD function.
     * 
     * @return The divisor expression of MOD function.
     */
    public Expression getDivisor() {
        return _divisor;
    }


    /**
     * Sets the divisor expression of MOD function.
     * 
     * @param divisor The new divisor expression of MOD function.
     */
    public void setDivisor(final Expression divisor) {
        _divisor = divisor;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        sb.append("MOD(");
        if (_divisor != null) { _divisor.toString(sb); }
        sb.append(", ");
        if (_dividend != null) { _dividend.toString(sb); }
        return sb.append(')');
    }

    //--------------------------------------------------------------------------
}
