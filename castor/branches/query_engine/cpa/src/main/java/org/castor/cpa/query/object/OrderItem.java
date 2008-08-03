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

import org.castor.cpa.query.Field;
import org.castor.cpa.query.OrderDirection;

/**
 * Final immutable class that represents an order item.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class OrderItem extends AbstractQueryObject {
    //--------------------------------------------------------------------------
    
    /** The field to order by. */
    private Field _field;
    
    /** The order direction. */
    private OrderDirection _direction;

    //--------------------------------------------------------------------------

    /**
     * Construct order item with given field and ascending order.
     * 
     * @param field The field to order by.
     */
    public OrderItem(final Field field) {
        this(field, OrderDirection.ASCENDING);
    }
    
    /**
     * Construct order item with given field and direction.
     * 
     * @param field The field to order by.
     * @param direction The order direction.
     */
    public OrderItem(final Field field, final OrderDirection direction) {
        _field = field;
        _direction = direction;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Gets the field to order by.
     * 
     * @return The field to order by.
     */
    public Field getField() {
        return _field;
    }
    
    /**
     * Gets the order direction.
     * 
     * @return The order direction.
     */
    public OrderDirection getDirection() {
        return _direction;
    }

    //--------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        if (_field != null) {
            _field.toString(sb);
        }
        if (_direction != null) {
            if (_direction == OrderDirection.ASCENDING) {
                sb.append(" ASC");
            } else {
                sb.append(" DESC");
            }
        }
        return sb;
    }

    //--------------------------------------------------------------------------
}
