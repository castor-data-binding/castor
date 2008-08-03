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

import junit.framework.TestCase;

import org.castor.cpa.query.Foo;
import org.castor.cpa.query.Order;
import org.castor.cpa.query.OrderDirection;
import org.castor.cpa.query.QueryFactory;
import org.castor.cpa.query.Schema;
import org.castor.cpa.query.SelectQuery;

/**
 * Junit test for testing order query object.
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public class TestOrder extends TestCase {
    //--------------------------------------------------------------------------
   
    private static String _common = "SELECT o.bar FROM org.castor.cpa.query.Foo AS o ";
 
    //--------------------------------------------------------------------------

    public static void testWithField() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        Order order = select.newOrder(schema.field("check"));
        select.setOrder(order);
        String expected = "ORDER BY o.check ASC";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithFieldDirection() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        Order order = select.newOrder(schema.field("check"), OrderDirection.DESCENDING);
        select.setOrder(order);
        String expected = "ORDER BY o.check DESC";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
    
    public static void testWithAddMultiple() {
        SelectQuery select = QueryFactory.newSelectQuery();
        Schema schema = select.newSchema(Foo.class, "o");
        select.addProjection(schema.field("bar"));
        select.addSchema(schema);
        Order order = select.newOrder(schema.field("check"), OrderDirection.DESCENDING);
        order.add(schema.field("book"));
        select.setOrder(order);
        String expected = "ORDER BY o.check DESC, o.book ASC";
        assertEquals(_common + expected, select.toString());
        System.out.println(select.toString());
    }
  
    //--------------------------------------------------------------------------
}
