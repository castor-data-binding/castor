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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.castor.cpa.query.Expression;
import org.castor.cpa.query.InCondition;
import org.castor.cpa.query.Literal;
import org.castor.cpa.query.Parameter;
import org.castor.cpa.query.object.literal.BigDecimalLiteral;
import org.castor.cpa.query.object.literal.BooleanLiteral;
import org.castor.cpa.query.object.literal.DoubleLiteral;
import org.castor.cpa.query.object.literal.EnumLiteral;
import org.castor.cpa.query.object.literal.LongLiteral;
import org.castor.cpa.query.object.literal.StringLiteral;

/**
 * Final class that represents in simple condition.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class In extends SimpleCondition implements InCondition {
    //--------------------------------------------------------------------------

    /** List of items to test against. */
    private List < Expression > _items = new ArrayList < Expression > ();

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void add(final boolean value) {
        _items.add(new BooleanLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public void add(final long value) {
        _items.add(new LongLiteral(value));  
    }

    /**
     * {@inheritDoc}
     */
    public void add(final double value) {
        _items.add(new DoubleLiteral(value)); 
    }

    /**
     * {@inheritDoc}
     */
    public void add(final BigDecimal value) {
        _items.add(new BigDecimalLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public void add(final String value) {
        _items.add(new StringLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public void add(final Enum < ? > value) {
        _items.add(new EnumLiteral(value));
    }

    /**
     * {@inheritDoc}
     */
    public void add(final Literal value) {
        _items.add(value);
    }

    /**
     * {@inheritDoc}
     */
    public void add(final Parameter value) {
        _items.add(value);
    }

    //--------------------------------------------------------------------------

    /**
     * Get list of items to test against.
     * 
     * @return List of items to test against.
     */
    public List < Expression > getItems() {
        return _items;
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
            sb.append(" NOT ");
        }
        sb.append(" IN (");   
        for (Iterator < Expression > iter = _items.iterator(); iter.hasNext(); ) {
            iter.next().toString(sb);
            if (iter.hasNext()) { sb.append(", "); }
        }
        sb.append(")");
        sb.append(')');
        return sb;
    }
    
    //--------------------------------------------------------------------------
}
