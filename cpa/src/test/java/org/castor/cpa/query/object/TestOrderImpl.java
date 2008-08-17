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

import java.util.List;

import junit.framework.TestCase;

import org.castor.cpa.query.Field;
import org.castor.cpa.query.Order;
import org.castor.cpa.query.OrderDirection;
import org.castor.cpa.query.QueryObject;

/**
 * Junit test for testing order query object.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class TestOrderImpl extends TestCase {
    //--------------------------------------------------------------------------
    
    /**
     * Junit Test for instance.
     */
    public void testInstance() {
        QueryObject n = new OrderImpl();
        assertTrue(n instanceof Order);
    }

    /**
     * Junit Test for Getter and Setter methods.
     */
    public void testGSetter() {
        Field field1 = new MockField();
        Field field2 = new MockField();
        Field field3 = new MockField();
        
        OrderImpl order = new OrderImpl();
        order.add(field1);
        order.add(field2, OrderDirection.ASCENDING);
        order.add(field3, OrderDirection.DESCENDING);
        List < OrderImpl.Item > items = order.getItems();
        assertNotNull(items);
        assertEquals(3, items.size());
        assertEquals(field1, items.get(0).getField());
        assertEquals(OrderDirection.ASCENDING, items.get(0).getDirection());
        assertEquals(field2, items.get(1).getField());
        assertEquals(OrderDirection.ASCENDING, items.get(1).getDirection());
        assertEquals(field3, items.get(2).getField());
        assertEquals(OrderDirection.DESCENDING, items.get(2).getDirection());
    }
     
    /**
     * Junit Test for toString method.
     */
    public void testToString() {
        Order order = new OrderImpl();
        order.add(new MockField());
        order.add(new MockField(), OrderDirection.ASCENDING);
        order.add(new MockField(), OrderDirection.DESCENDING);
        assertEquals("field ASC, field ASC, field DESC", order.toString());
    } 

    //--------------------------------------------------------------------------
}
