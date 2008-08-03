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
package org.castor.cpa.query.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.castor.cpa.query.Field;
import org.castor.cpa.query.Order;
import org.castor.cpa.query.OrderDirection;

/**
 * Final class that implements Order.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class OrderImpl implements Order {
    //--------------------------------------------------------------------------

    /** List of order items of the order. */
    private final List < OrderItem > _items = new ArrayList < OrderItem > ();
    
    //--------------------------------------------------------------------------
 
    /**
     * Instantiates a new order impl.
     * 
     * @param field the field
     */
    
    //--------------------------------------------------------------------------
    
    public OrderImpl(final Field field) {
        add(field);
    }
    
    /**
     * Instantiates a new order impl.
     * 
     * @param field the field
     * @param direction the direction
     */
    public OrderImpl(final Field field, final OrderDirection direction) {
        add(field, direction);
    }
    
    /**
     * {@inheritDoc}
     */
    public void add(final Field field) {
        _items.add(new OrderItem(field));
    }
    
    /**
     * {@inheritDoc}
     */
    public void add(final Field field, final OrderDirection direction) {
        _items.add(new OrderItem(field, direction));
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        for (Iterator < OrderItem > iter = _items.iterator(); iter.hasNext(); ) {
            iter.next().toString(sb);
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb;
    }

    //--------------------------------------------------------------------------
}
